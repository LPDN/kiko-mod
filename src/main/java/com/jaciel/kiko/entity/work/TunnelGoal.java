package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.entity.combat.WeaponHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Túnel y escalera.
 *
 *  TÚNEL     cava derecho hacia donde estabas mirando cuando se lo pediste
 *            (2 de alto x 1 de ancho, o 3x3 según la config)
 *  ESCALERA  baja en diagonal un bloque por paso hasta la capa de la config (por defecto Y -50)
 *
 * Mientras cava
 *  - Saca los minerales que vea en las paredes, con toda la veta
 *  - Pone antorchas cada 8 bloques si trae
 *  - Si encuentra lava o agua la tapa con piedra (si trae), si no se detiene y te avisa
 *  - Rellena huecos del piso con piedra para poder seguir caminando
 *  - Quita la grava o arena que esté encima para que no se le caiga
 *  - Nunca rompe la herramienta. Si se le está acabando cambia a otro pico o se detiene
 */
public class TunnelGoal extends Goal {
    private static final double REACH_SQ = 4.3 * 4.3;
    private static final Set<net.minecraft.item.Item> FILLERS = Set.of(
            Items.COBBLESTONE, Items.COBBLED_DEEPSLATE, Items.STONE, Items.DEEPSLATE, Items.DIRT,
            Items.NETHERRACK, Items.ANDESITE, Items.DIORITE, Items.GRANITE, Items.TUFF);

    private final KikoEntity kiko;
    @Nullable
    private BlockPos target;
    private float progress;
    private int lastStage = -1;
    private int travelTicks;
    private int cooldown;

    public TunnelGoal(KikoEntity kiko) {
        this.kiko = kiko;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    private boolean isTunnelMode() {
        WorkMode m = kiko.getWorkMode();
        return m == WorkMode.TUNNEL || m == WorkMode.STAIRS;
    }

    private static boolean goodPickaxe(ItemStack s) {
        return WeaponHelper.isPickaxe(s) && !KikoWork.isAlmostBroken(s);
    }

    @Override
    public boolean canStart() {
        if (!isTunnelMode() || kiko.getTarget() != null || kiko.isDepositing()) return false;
        if (kiko.getInventory().isBagFull()) return false;
        if (--cooldown > 0) return false;
        cooldown = 20;
        if (!WeaponHelper.equipTool(kiko, TunnelGoal::goodPickaxe)) {
            kiko.notifyOwner("message.kiko.need_pickaxe");
            return false;
        }
        return kiko.getWorkCenter() != null;
    }

    @Override
    public boolean shouldContinue() {
        return isTunnelMode() && kiko.getTarget() == null && !kiko.isDepositing()
                && !kiko.getInventory().isBagFull() && goodPickaxe(kiko.getMainHandStack());
    }

    @Override
    public void start() {
        target = null;
        progress = 0;
        lastStage = -1;
    }

    @Override
    public void stop() {
        if (target != null) kiko.getWorld().setBlockBreakingInfo(kiko.getId(), target, -1);
        target = null;
        kiko.getNavigation().stop();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    // ------------------------------------------------------------------
    // Geometría del túnel
    // ------------------------------------------------------------------

    private boolean stairs() {
        return kiko.getWorkMode() == WorkMode.STAIRS;
    }

    /** Bloque del piso (donde se para) del paso i. El paso -1 es donde empezó. */
    private BlockPos baseOf(int i) {
        BlockPos start = kiko.getWorkCenter();
        Direction dir = kiko.getWorkDirection();
        BlockPos p = start.offset(dir, i + 1);
        return stairs() ? p.down(i + 1) : p;
    }

    /** Bloques que hay que dejar libres en el paso i. */
    private List<BlockPos> clearFor(int i) {
        BlockPos base = baseOf(i);
        Direction side = kiko.getWorkDirection().rotateYClockwise();
        int size = kiko.getTunnelSize() >= 3 && !stairs() ? 3 : 1;
        int height = stairs() ? 3 : (size == 3 ? 3 : 2);
        List<BlockPos> list = new ArrayList<>();
        for (int w = -(size / 2); w <= size / 2; w++) {
            for (int h = 0; h < height; h++) list.add(base.offset(side, w).up(h));
        }
        return list;
    }

    /** Bloques del piso del paso i. */
    private List<BlockPos> floorFor(int i) {
        List<BlockPos> list = new ArrayList<>();
        for (BlockPos p : clearFor(i)) {
            if (p.getY() == baseOf(i).getY()) list.add(p.down());
        }
        return list;
    }

    // ------------------------------------------------------------------
    // Tick
    // ------------------------------------------------------------------

    @Override
    public void tick() {
        if (!(kiko.getWorld() instanceof ServerWorld world)) return;
        KikoConfig cfg = KikoConfig.get();
        int step = kiko.getTunnelStep();

        if (step >= cfg.tunnelLength) {
            finish("message.kiko.tunnel_done");
            return;
        }
        if (stairs() && baseOf(step).getY() < cfg.stairsTargetY) {
            finish("message.kiko.stairs_done");
            return;
        }

        if (target == null) {
            target = nextBlockToClear(world, step);
            progress = 0;
            lastStage = -1;
            travelTicks = 0;
            if (target == null) {
                // Paso libre. Piso, antorcha y al siguiente
                if (!fixFloor(world, step)) return;
                if (step % 8 == 0) placeTorch(world, step);
                kiko.setTunnelStep(step + 1);
                return;
            }
            // Antes de romper, revisa que no haya lava o agua al lado
            if (!sealFluids(world, target, step)) return;
        }

        BlockState state = world.getBlockState(target);
        if (state.isAir() || state.isReplaceable() && state.getFluidState().isEmpty()) {
            target = null;
            return;
        }

        Vec3d center = Vec3d.ofCenter(target);
        kiko.getLookControl().lookAt(center.x, center.y, center.z);
        if (kiko.getEyePos().squaredDistanceTo(center) > REACH_SQ) {
            BlockPos stand = step == 0 ? kiko.getWorkCenter() : baseOf(step - 1);
            if (travelTicks++ % 20 == 0) {
                kiko.getNavigation().startMovingTo(stand.getX() + 0.5, stand.getY(), stand.getZ() + 0.5, 1.0);
            }
            if (travelTicks > 300) finish("message.kiko.tunnel_blocked");
            return;
        }
        kiko.getNavigation().stop();

        if (KikoWork.isProtected(world, target, state)) {
            finish("message.kiko.tunnel_blocked");     // llegó a una casa, ahí se detiene
            return;
        }
        float delta = KikoWork.breakDelta(kiko, state, target);
        if (delta <= 0f) {
            finish("message.kiko.tunnel_blocked"); // bedrock o algo irrompible
            return;
        }
        progress += delta;
        if (kiko.age % 5 == 0) kiko.swingHand(Hand.MAIN_HAND);
        int stage = Math.min(9, (int) (progress * 10f));
        if (stage != lastStage) {
            world.setBlockBreakingInfo(kiko.getId(), target, stage);
            lastStage = stage;
        }
        if (progress < 1f) return;

        world.setBlockBreakingInfo(kiko.getId(), target, -1);
        BlockPos broken = target;
        target = null;
        if (!KikoWork.harvest(kiko, world, broken)) {
            if (!WeaponHelper.equipTool(kiko, TunnelGoal::goodPickaxe)) finish("message.kiko.tunnel_tool");
            return;
        }
        // Minerales en las paredes, techo y piso
        for (Direction d : Direction.values()) {
            BlockPos n = broken.offset(d);
            BlockState ns = world.getBlockState(n);
            if (KikoWork.isOre(ns) && kiko.getMainHandStack().isSuitableFor(ns)) {
                KikoWork.mineVein(kiko, world, n, 16);
            }
        }
    }

    /** Siguiente bloque sólido del paso (incluye grava o arena que pueda caer del techo). */
    @Nullable
    private BlockPos nextBlockToClear(ServerWorld world, int step) {
        List<BlockPos> list = clearFor(step);
        BlockPos top = list.get(list.size() - 1);
        // Columna de grava o arena arriba del techo
        BlockPos above = top.up();
        for (int i = 0; i < 6 && world.getBlockState(above).getBlock() instanceof FallingBlock; i++) above = above.up();
        for (BlockPos p = above.down(); p.getY() > top.getY(); p = p.down()) {
            if (world.getBlockState(p).getBlock() instanceof FallingBlock) list.add(p);
        }
        // De arriba para abajo para que lo que caiga no le estorbe
        list.sort((a, b) -> Integer.compare(b.getY(), a.getY()));
        for (BlockPos p : list) {
            BlockState s = world.getBlockState(p);
            if (s.isAir() || s.isOf(Blocks.TORCH) || s.isOf(Blocks.WALL_TORCH)) continue;
            if (s.isReplaceable() && s.getFluidState().isEmpty()) continue; // pasto, nieve
            if (!s.getFluidState().isEmpty()) {
                // Agua o lava dentro del túnel, se tapa con piedra si puede
                if (!placeFiller(world, p)) {
                    finish("message.kiko.tunnel_fluid");
                    return null;
                }
                return p;
            }
            return p.toImmutable();
        }
        return null;
    }

    /** Tapa con piedra la lava o el agua pegada al bloque que va a romper. */
    private boolean sealFluids(ServerWorld world, BlockPos pos, int step) {
        List<BlockPos> path = clearFor(step);
        for (Direction d : Direction.values()) {
            BlockPos n = pos.offset(d);
            if (path.contains(n)) continue;
            if (!world.getBlockState(n).getFluidState().isEmpty()) {
                if (!placeFiller(world, n)) {
                    finish("message.kiko.tunnel_fluid");
                    return false;
                }
            }
        }
        return true;
    }

    /** Rellena los huecos del piso para poder seguir caminando. */
    private boolean fixFloor(ServerWorld world, int step) {
        for (BlockPos f : floorFor(step)) {
            BlockState s = world.getBlockState(f);
            if (!s.getCollisionShape(world, f).isEmpty() && s.getFluidState().isEmpty()) continue;
            if (!placeFiller(world, f)) {
                finish("message.kiko.tunnel_gap");
                return false;
            }
        }
        return true;
    }

    private boolean placeFiller(ServerWorld world, BlockPos pos) {
        int i = kiko.getInventory().findInBag(s -> FILLERS.contains(s.getItem()));
        if (i < 0) return false;
        ItemStack stack = kiko.getInventory().bag().get(i);
        BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
        world.setBlockState(pos, state);
        world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1f, 1f);
        stack.decrement(1);
        kiko.swingHand(Hand.MAIN_HAND);
        return true;
    }

    private void placeTorch(ServerWorld world, int step) {
        int i = kiko.getInventory().findInBag(s -> s.isOf(Items.TORCH));
        if (i < 0) return;
        BlockPos base = baseOf(step);
        int size = kiko.getTunnelSize() >= 3 && !stairs() ? 3 : 1;
        // En el de 3x3 va de lado para no estorbar
        BlockPos spot = size == 3 ? base.offset(kiko.getWorkDirection().rotateYClockwise()) : base;
        BlockState torch = Blocks.TORCH.getDefaultState();
        if (world.getBlockState(spot).isAir() && torch.canPlaceAt(world, spot)) {
            world.setBlockState(spot, torch);
            world.playSound(null, spot, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1f, 1.2f);
            kiko.getInventory().bag().get(i).decrement(1);
        }
    }

    private void finish(String key) {
        kiko.notifyOwner(key);
        if (target != null) kiko.getWorld().setBlockBreakingInfo(kiko.getId(), target, -1);
        target = null;
        kiko.setWorkMode(WorkMode.NONE, null);
    }
}
