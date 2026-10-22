package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.entity.KikoBobberEntity;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.registry.ModEntities;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.MathHelper;
import com.jaciel.kiko.entity.combat.WeaponHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Pescadora. Con una caña busca agua abierta (al menos 3x3), se para en la orilla y pesca.
 * Saca lo mismo que un jugador (peces, basura y a veces tesoros) usando la tabla de pesca de vanilla,
 * así que también funcionan los mods que agregan peces a esa tabla.
 * Cada pescado tarda entre 15 y 35 segundos y gasta un uso de la caña.
 * Lanza el corcho de verdad (se ve el hilo), el corcho se hunde cuando pica y el pescado sale volando hacia ella.
 */
public class FishGoal extends WalkWorkGoal {
    private static final RegistryKey<LootTable> FISHING =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.ofVanilla("gameplay/fishing"));

    /** Agua donde "cae" el anzuelo. El target es la orilla donde se para. */
    @Nullable
    private BlockPos water;
    private int nextCatch;
    private int biteAt;
    private int recastAt;
    private int caught;
    @Nullable
    private KikoBobberEntity bobber;

    public FishGoal(KikoEntity kiko) {
        super(kiko, WorkMode.FISH);
    }

    private static boolean isRod(ItemStack s) {
        return s.getItem() instanceof FishingRodItem && !KikoWork.isAlmostBroken(s);
    }

    @Override
    protected boolean prepare() {
        if (!WeaponHelper.equipTool(kiko, FishGoal::isRod)) {
            kiko.notifyOwner("message.kiko.need_rod");
            return false;
        }
        return true;
    }

    @Override
    protected double reachSq() {
        return 1.2 * 1.2;
    }

    @Override
    protected String nothingLeftKey() {
        return "message.kiko.no_water";
    }

    @Override
    protected int idleCooldown() {
        return 100;
    }

    @Override
    @Nullable
    protected BlockPos findTarget(World world, BlockPos from, BlockPos center, int radius) {
        for (BlockPos pos : BlockPos.iterateOutwards(from, radius + 4, 4, radius + 4)) {
            if (!inArea(pos, center, radius) || isIgnored(pos)) continue;
            if (!isOpenWater(world, pos)) continue;
            // Busca una orilla firme junto a esa agua
            for (BlockPos shore : BlockPos.iterateOutwards(pos, 2, 1, 2)) {
                if (isStandable(world, shore)) {
                    water = pos.toImmutable();
                    return shore.toImmutable();
                }
            }
            ignore(pos.toImmutable(), 1200);
        }
        return null;
    }

    @Override
    protected boolean isValidTarget(World world, BlockPos pos) {
        return water != null && isRod(kiko.getMainHandStack()) && isOpenWater(world, water);
    }

    @Override
    public void start() {
        super.start();
        nextCatch = 0;
        recastAt = 0;
        caught = 0;
    }

    @Override
    public void stop() {
        super.stop();
        kiko.setFishing(false);
        removeBobber();
    }

    @Override
    protected boolean act(ServerWorld world, BlockPos pos) {
        if (water == null) return true;
        Vec3d w = Vec3d.ofCenter(water);
        kiko.getLookControl().lookAt(w.x, w.y, w.z);
        kiko.setFishing(true);

        // Espera un segundo entre pescado y pescado antes de volver a lanzar
        if (nextCatch == 0) {
            if (actTicks < recastAt) return false;
            cast(world, w);
            // v3.3 Leo el pescador saca el doble de rápido
            boolean leo = kiko.getCharacter() == com.jaciel.kiko.entity.KikoCharacter.LEO;
            nextCatch = actTicks + (leo ? 150 + kiko.getRandom().nextInt(200) : 300 + kiko.getRandom().nextInt(400));
            biteAt = nextCatch - 20 - kiko.getRandom().nextInt(20);
            return false;
        }

        // Burbujitas acercándose mientras espera
        if (actTicks % 15 == 0 && bobber != null && bobber.isFloating()) {
            world.spawnParticles(ParticleTypes.FISHING, bobber.getX(), bobber.getY() + 0.1, bobber.getZ(), 2, 0.3, 0, 0.3, 0);
        }

        // ¡Picó! El corcho se hunde
        if (actTicks == biteAt && bobber != null) {
            bobber.setBite(true);
            world.spawnParticles(ParticleTypes.SPLASH, bobber.getX(), bobber.getY() + 0.3, bobber.getZ(), 12, 0.2, 0.1, 0.2, 0.1);
            world.spawnParticles(ParticleTypes.BUBBLE, bobber.getX(), bobber.getY(), bobber.getZ(), 6, 0.2, 0.1, 0.2, 0.05);
            world.playSound(null, bobber.getX(), bobber.getY(), bobber.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_SPLASH,
                    SoundCategory.NEUTRAL, 0.4f, 1f);
        }
        if (actTicks < nextCatch) return false;

        // Jala la caña
        kiko.swingHand(Hand.MAIN_HAND);
        Vec3d from = bobber != null ? bobber.getPos() : w;
        removeBobber();

        ItemStack rod = kiko.getMainHandStack();
        LootContextParameterSet params = new LootContextParameterSet.Builder(world)
                .add(LootContextParameters.ORIGIN, from)
                .add(LootContextParameters.TOOL, rod)
                .add(LootContextParameters.THIS_ENTITY, kiko)
                .luck(kiko.getCharacter() == com.jaciel.kiko.entity.KikoCharacter.LEO ? 3f : 0f)   // Leo saca más tesoros
                .build(LootContextTypes.FISHING);
        LootTable table = world.getServer().getReloadableRegistries().getLootTable(FISHING);
        List<ItemStack> loot = table.generateLoot(params);
        for (ItemStack stack : loot) {
            // El pescado sale volando del agua hacia ella, como cuando pescas tú
            ItemEntity flying = new ItemEntity(world, from.x, from.y + 0.3, from.z, stack.copy());
            double dx = kiko.getX() - from.x, dy = kiko.getY() - from.y, dz = kiko.getZ() - from.z;
            flying.setVelocity(dx * 0.1, dy * 0.1 + Math.sqrt(Math.sqrt(dx * dx + dy * dy + dz * dz)) * 0.08, dz * 0.1);
            flying.setPickupDelay(10);
            world.spawnEntity(flying);
        }
        ExperienceOrbEntity.spawn(world, kiko.getPos(), 1 + kiko.getRandom().nextInt(4));
        world.playSound(null, kiko.getX(), kiko.getY(), kiko.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE,
                SoundCategory.NEUTRAL, 1f, 1f);
        rod.damage(1, kiko, net.minecraft.entity.EquipmentSlot.MAINHAND);

        kiko.setExpression(com.jaciel.kiko.animation.KikoExpression.GRIN, 40);
        caught++;
        nextCatch = 0;
        recastAt = actTicks + 30;
        // Sigue pescando en el mismo lugar mientras tenga caña y espacio
        return !isRod(kiko.getMainHandStack()) || kiko.getInventory().isBagFull();
    }

    private void cast(ServerWorld world, Vec3d water) {
        removeBobber();
        kiko.swingHand(Hand.MAIN_HAND);
        world.playSound(null, kiko.getX(), kiko.getY(), kiko.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW,
                SoundCategory.NEUTRAL, 0.5f, 0.4f);
        float yaw = kiko.bodyYaw * MathHelper.RADIANS_PER_DEGREE;
        double sx = kiko.getX() - MathHelper.sin(yaw) * 0.9;
        double sz = kiko.getZ() + MathHelper.cos(yaw) * 0.9;
        bobber = new KikoBobberEntity(ModEntities.BOBBER, world, kiko);
        bobber.refreshPositionAndAngles(sx, kiko.getY() + 1.74, sz, 0f, 0f);
        bobber.launchTowards(new Vec3d(water.x + (kiko.getRandom().nextDouble() - 0.5),
                water.y + 0.4, water.z + (kiko.getRandom().nextDouble() - 0.5)));
        world.spawnEntity(bobber);
    }

    private void removeBobber() {
        if (bobber != null) bobber.discard();
        bobber = null;
    }

    // ------------------------------------------------------------------

    /** Agua quieta con aire arriba y agua alrededor (3x3). */
    private static boolean isOpenWater(World world, BlockPos pos) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos p = pos.add(dx, 0, dz);
                if (!world.getFluidState(p).isIn(FluidTags.WATER) || !world.getFluidState(p).isStill()) return false;
                if (!world.getBlockState(p.up()).isAir()) return false;
            }
        }
        return true;
    }

    /** Un lugar donde se puede parar (piso firme, dos de aire, nada de agua). */
    private static boolean isStandable(World world, BlockPos pos) {
        BlockState floor = world.getBlockState(pos.down());
        return floor.isSolidBlock(world, pos.down())
                && world.getBlockState(pos).isAir() && world.getBlockState(pos.up()).isAir()
                && world.getFluidState(pos.down()).isEmpty();
    }
}
