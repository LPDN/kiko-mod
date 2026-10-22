package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.entity.combat.WeaponHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Base de los trabajos con bloques (talar y minar).
 *  1. Busca el bloque objetivo más cercano a ELLA (no al punto de trabajo), sin salirse del área
 *  2. Camina hasta él. Si no puede llegar lo ignora un rato y pasa al siguiente
 *  3. Lo rompe con la velocidad real de su herramienta (con Eficiencia, Prisa y bonus de afecto)
 *  4. Mete lo que suelta en su mochila y busca el siguiente de inmediato
 */
public abstract class BlockWorkGoal extends Goal {
    protected static final double REACH_SQ = 3.6 * 3.6;

    protected final KikoEntity kiko;
    private final EnumSet<WorkMode> modes;
    private final Predicate<ItemStack> isTool;
    private final String noToolKey;

    /** Bloques que no pudo alcanzar, con el tick hasta el que se ignoran. */
    private final Map<Long, Long> blacklist = new HashMap<>();
    @Nullable
    protected BlockPos target;
    private float progress;
    private int travelTicks;
    private int stuckTicks;
    private Vec3d lastPos = Vec3d.ZERO;
    private int scanCooldown;
    private int lastStage = -1;

    protected BlockWorkGoal(KikoEntity kiko, WorkMode mode, Predicate<ItemStack> isTool, String noToolKey) {
        this(kiko, EnumSet.of(mode), isTool, noToolKey);
    }

    protected BlockWorkGoal(KikoEntity kiko, EnumSet<WorkMode> modes, Predicate<ItemStack> isTool, String noToolKey) {
        this.kiko = kiko;
        this.modes = modes;
        this.isTool = s -> isTool.test(s) && !KikoWork.isAlmostBroken(s);
        this.noToolKey = noToolKey;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    /**
     * Busca el siguiente bloque a trabajar.
     * @param from    desde dónde buscar (la posición de Kiko)
     * @param center  centro del área de trabajo
     * @param radius  radio del área
     */
    @Nullable
    protected abstract BlockPos findTarget(World world, BlockPos from, BlockPos center, int radius);

    /** Revisa si el bloque sigue siendo válido. */
    protected abstract boolean isValidTarget(World world, BlockPos pos, BlockState state);

    /** Qué pasa al terminar de romper el bloque principal (talar el árbol, sacar la veta...). */
    protected abstract void onBroken(ServerWorld world, BlockPos pos, BlockState state);

    /** true si pos está dentro del área de trabajo. */
    protected static boolean inArea(BlockPos pos, BlockPos center, int radius) {
        int dx = pos.getX() - center.getX();
        int dz = pos.getZ() - center.getZ();
        return dx * dx + dz * dz <= radius * radius;
    }

    @Override
    public boolean canStart() {
        if (!modes.contains(kiko.getWorkMode()) || kiko.getTarget() != null || kiko.isDepositing()) return false;
        if (kiko.getInventory().isBagFull()) return false; // DepositGoal se encarga
        if (--scanCooldown > 0) return false;
        scanCooldown = 10;
        if (!WeaponHelper.equipTool(kiko, isTool)) {
            kiko.notifyOwner(noToolKey);
            scanCooldown = 60;
            return false;
        }
        cleanBlacklist();
        BlockPos center = kiko.getWorkCenter() != null ? kiko.getWorkCenter() : kiko.getBlockPos();
        target = findTarget(kiko.getWorld(), kiko.getBlockPos(), center, KikoConfig.get().workRadius);
        if (target == null) {
            kiko.notifyOwner(nothingLeftKey());
            scanCooldown = 40;
        }
        return target != null;
    }

    @Override
    public boolean shouldContinue() {
        return target != null && modes.contains(kiko.getWorkMode()) && kiko.getTarget() == null && !kiko.isDepositing()
                && isTool.test(kiko.getMainHandStack())
                && isValidTarget(kiko.getWorld(), target, kiko.getWorld().getBlockState(target));
    }

    @Override
    public void start() {
        progress = 0f;
        travelTicks = 0;
        stuckTicks = 0;
        lastStage = -1;
        lastPos = kiko.getPos();
        moveToTarget();
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

    @Override
    public void tick() {
        if (target == null) return;
        World world = kiko.getWorld();
        Vec3d center = Vec3d.ofCenter(target);
        kiko.getLookControl().lookAt(center.x, center.y, center.z);

        if (kiko.getEyePos().squaredDistanceTo(center) > REACH_SQ) {
            travelTicks++;
            // Si lleva 3 segundos sin moverse o 8 segundos caminando, se rinde con este bloque
            if (travelTicks % 20 == 0) {
                if (kiko.getPos().squaredDistanceTo(lastPos) < 0.25) stuckTicks += 20;
                else stuckTicks = 0;
                lastPos = kiko.getPos();
                moveToTarget();
            }
            if (stuckTicks >= 60 || travelTicks > 160) giveUp(1200);
            return;
        }

        kiko.getNavigation().stop();
        BlockState state = world.getBlockState(target);
        beforeBreak(state);
        float delta = KikoWork.breakDelta(kiko, state, target);
        if (kiko.getCharacter() == com.jaciel.kiko.entity.KikoCharacter.IVAN) delta *= 1.5f;   // v3.3 Iván pica más rápido
        if (delta <= 0f) {
            giveUp(6000);
            return;
        }
        progress += delta;

        if (kiko.age % 5 == 0) kiko.swingHand(Hand.MAIN_HAND);
        int stage = Math.min(9, (int) (progress * 10f));
        if (stage != lastStage) {
            world.setBlockBreakingInfo(kiko.getId(), target, stage);
            lastStage = stage;
        }

        if (progress >= 1f) {
            world.setBlockBreakingInfo(kiko.getId(), target, -1);
            if (world instanceof ServerWorld sw) onBroken(sw, target, state);
            target = null;
            scanCooldown = 0; // busca el siguiente de inmediato
        }
    }

    private void giveUp(int ticks) {
        if (target != null) {
            kiko.getWorld().setBlockBreakingInfo(kiko.getId(), target, -1);
            ignore(target, ticks);
        }
        target = null;
        scanCooldown = 0;
    }

    private void moveToTarget() {
        if (target == null) return;
        Path path = kiko.getNavigation().findPathTo(target, 1);
        if (path != null) {
            kiko.getNavigation().startMovingAlong(path, 1.1);
        } else if (kiko.getEyePos().squaredDistanceTo(Vec3d.ofCenter(target)) > REACH_SQ) {
            giveUp(1200);
        }
    }

    // ------------------------------------------------------------------
    // Utilidades para las subclases
    // ------------------------------------------------------------------

    /** Justo antes de picar, por si la subclase quiere cambiar de herramienta. */
    protected void beforeBreak(BlockState state) {}

    /** Mensaje cuando ya no queda nada que hacer en el área. */
    protected String nothingLeftKey() {
        return "message.kiko.nothing_left";
    }

    protected boolean harvest(ServerWorld world, BlockPos pos) {
        return KikoWork.harvest(kiko, world, pos);
    }

    protected boolean isIgnored(BlockPos pos) {
        Long until = blacklist.get(pos.asLong());
        if (until != null && until > kiko.getWorld().getTime()) return true;
        return KikoWork.isProtected(kiko.getWorld(), pos, kiko.getWorld().getBlockState(pos));
    }

    protected void ignore(BlockPos pos, int ticks) {
        blacklist.put(pos.asLong(), kiko.getWorld().getTime() + ticks);
    }

    private void cleanBlacklist() {
        long now = kiko.getWorld().getTime();
        blacklist.values().removeIf(until -> until <= now);
    }
}
