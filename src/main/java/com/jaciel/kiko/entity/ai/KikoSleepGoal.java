package com.jaciel.kiko.entity.ai;

import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.block.KikoBedBlock;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.entity.work.WorkMode;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

/**
 * Dormir (Fase 4).
 *  - Si su amigo (con afecto para seguirlo) se acuesta cerca, se va a dormir a su lado:
 *    la otra mitad de la cama de Kiko, o una cama pegada a la tuya
 *  - En la noche, si no te está siguiendo ni trabajando, busca una cama de Kiko libre y se duerme
 *    (si le pusiste casa, se va a su casa a dormir)
 *  - Se despierta en la mañana, si la atacan o si te levantas y te está siguiendo
 *  - Si durmieron juntos te da los buenos días y sube un poquito el afecto
 */
public class KikoSleepGoal extends Goal {
    private static final int SEARCH = 16;

    private final KikoEntity kiko;
    @Nullable
    private BlockPos bed;
    @Nullable
    private PlayerEntity sleptWith;
    private int ticks;
    private int cooldown;
    /** A qué lugar junto a la cama va caminando, los que ya probó sin éxito y para saber si se atoró. */
    @Nullable
    private BlockPos spot;
    private final java.util.Set<BlockPos> triedSpots = new java.util.HashSet<>();
    private int stuckTicks;
    private int failedPlans;
    private Vec3d lastPos = Vec3d.ZERO;

    public KikoSleepGoal(KikoEntity kiko) {
        this.kiko = kiko;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
    }

    @Nullable
    private PlayerEntity friend() {
        return kiko.getFriendAbove(KikoConfig.get().followThreshold);
    }

    private boolean friendSleeping(@Nullable PlayerEntity f) {
        return f != null && f.isSleeping() && f.squaredDistanceTo(kiko) < 24 * 24;
    }

    /** ¿Tiene ganas de dormir ahorita? */
    private boolean wantsToSleep() {
        if (kiko.getTarget() != null || kiko.isBeingAttended()) return false;
        PlayerEntity f = friend();
        if (friendSleeping(f)) return true;
        World world = kiko.getWorld();
        if (!world.isNight() || kiko.getWorkMode() != WorkMode.NONE) return false;
        // Si te está siguiendo y estás cerca y despierto, se queda contigo
        boolean followingYou = f != null && kiko.isFollowEnabled() && f.squaredDistanceTo(kiko) < 16 * 16;
        return !followingYou;
    }

    @Override
    public boolean canStart() {
        // Los hijos no se meten a la cama de nadie (ni a la tuya ni a la de su mamá)
        if (kiko.isKid() || kiko.isSleeping() || --cooldown > 0) return false;
        cooldown = 40;
        if (!wantsToSleep()) return false;
        bed = findBed();
        return bed != null;
    }

    @Override
    public boolean shouldContinue() {
        if (bed == null || !wantsToSleep()) return false;
        BlockState s = kiko.getWorld().getBlockState(bed);
        if (!(s.getBlock() instanceof BedBlock)) return false;
        if (!kiko.isSleeping()) return ticks < 520 && !s.get(BedBlock.OCCUPIED);
        return true;
    }

    @Override
    public void start() {
        ticks = 0;
        sleptWith = null;
        triedSpots.clear();
        spot = null;
        stuckTicks = 0;
        failedPlans = 0;
        lastPos = kiko.getPos();
        if (bed == null) return;
        // Si su casa está lejos se va directo (como cuando te sigue y se teletransporta)
        if (kiko.squaredDistanceTo(Vec3d.ofCenter(bed)) > 48 * 48) {
            teleportToBed();
            return;
        }
        planRoute();
    }

    /** Los bloques de la cama (las 2 o 4 piezas). */
    private java.util.List<BlockPos> footprint() {
        java.util.List<BlockPos> out = new java.util.ArrayList<>();
        World world = kiko.getWorld();
        for (BlockPos p : BlockPos.iterate(bed.add(-1, 0, -1), bed.add(1, 0, 1))) {
            if (world.getBlockState(p).getBlock() instanceof BedBlock) out.add(p.toImmutable());
        }
        return out;
    }

    /**
     * Busca por dónde llegar: revisa todos los lugares libres alrededor de la cama, saca la ruta a cada uno
     * y se queda con la más corta que sí llegue (rodeando paredes y pasando por puertas).
     * Busca con más calma que un mob normal (4 veces más pasos) para poder rodear casas grandes.
     * Si aun así no llega, primero camina a una puerta de la casa y desde ahí vuelve a buscar.
     * Si no hay forma, se va directo a la cama con una nubecita en vez de quedarse viendo la pared.
     */
    private void planRoute() {
        World world = kiko.getWorld();
        net.minecraft.entity.ai.pathing.EntityNavigation nav = kiko.getNavigation();
        Path best = null;
        BlockPos bestSpot = null;
        BlockPos bestDoor = null;
        nav.setRangeMultiplier(4f);
        try {
            for (BlockPos b : footprint()) {
                for (Direction d : Direction.Type.HORIZONTAL) {
                    BlockPos s = b.offset(d);
                    if (triedSpots.contains(s) || !standable(world, s)) continue;
                    Path path = nav.findPathTo(s, 0);
                    if (path == null || !path.reachesTarget()) continue;
                    if (best == null || path.getLength() < best.getLength()) {
                        best = path;
                        bestSpot = s;
                    }
                }
            }
            // Sin ruta completa: ¿puede llegar a alguna puerta cerca de la cama?
            if (best == null) {
                for (BlockPos door : doorsNearBed(world)) {
                    if (triedSpots.contains(door) || door.isWithinDistance(kiko.getPos(), 1.5)) continue;
                    Path path = nav.findPathTo(door, 0);
                    if (path == null || !path.reachesTarget()) continue;
                    if (best == null || path.getLength() < best.getLength()) {
                        best = path;
                        bestDoor = door;
                    }
                }
            }
        } finally {
            nav.resetRangeMultiplier();
        }
        if (best != null) {
            spot = bestSpot != null ? bestSpot : bestDoor;
            failedPlans = 0;
            nav.startMovingAlong(best, 1.0);
        } else {
            spot = null;
            nav.stop();
            // Dos veces sin encontrar camino: se va directo
            if (++failedPlans >= 2) teleportToBed();
        }
    }

    /** Puertas de madera (la mitad de abajo) cerca de la cama. */
    private java.util.List<BlockPos> doorsNearBed(World world) {
        java.util.List<BlockPos> out = new java.util.ArrayList<>();
        for (BlockPos p : BlockPos.iterateOutwards(bed, 10, 3, 10)) {
            BlockState st = world.getBlockState(p);
            if (st.isIn(net.minecraft.registry.tag.BlockTags.WOODEN_DOORS)
                    && st.get(net.minecraft.block.DoorBlock.HALF) == net.minecraft.block.enums.DoubleBlockHalf.LOWER) {
                out.add(p.toImmutable());
            }
        }
        return out;
    }

    private static boolean standable(World world, BlockPos p) {
        return world.getBlockState(p).getCollisionShape(world, p).isEmpty()
                && world.getBlockState(p.up()).getCollisionShape(world, p.up()).isEmpty()
                && !world.getBlockState(p.down()).getCollisionShape(world, p.down()).isEmpty()
                && world.getFluidState(p).isEmpty();
    }

    /** ¿Ya está junto a la cama? */
    private boolean atBed() {
        for (BlockPos b : footprint()) {
            Vec3d c = Vec3d.ofBottomCenter(b);
            double dx = kiko.getX() - c.x, dz = kiko.getZ() - c.z;
            if (dx * dx + dz * dz < 1.6 * 1.6 && Math.abs(kiko.getY() - c.y) < 1.5) return true;
        }
        return false;
    }

    /** Último recurso si de plano no puede llegar caminando: aparece en la cama con una nubecita. */
    private void teleportToBed() {
        if (kiko.getWorld() instanceof net.minecraft.server.world.ServerWorld sw) {
            sw.spawnParticles(net.minecraft.particle.ParticleTypes.CLOUD, kiko.getX(), kiko.getBodyY(0.5), kiko.getZ(), 10, 0.3, 0.4, 0.3, 0.02);
        }
        kiko.refreshPositionAndAngles(bed.getX() + 0.5, bed.getY() + 0.6, bed.getZ() + 0.5, kiko.getYaw(), kiko.getPitch());
        kiko.getNavigation().stop();
    }

    @Override
    public void stop() {
        if (kiko.isSleeping()) {
            kiko.wakeUp();
            kiko.setExpression(KikoExpression.YAWN, 40);
            if (sleptWith != null && sleptWith.isAlive() && sleptWith.squaredDistanceTo(kiko) < 24 * 24
                    && !kiko.getWorld().isNight()) {
                kiko.getAffection().add(sleptWith.getUuid(), 3f, kiko.getWorld().getTime());
                KikoInteractionHandler.say(sleptWith, kiko, "dialogue.kiko.good_morning", 3);
            }
        }
        kiko.getNavigation().stop();
        bed = null;
        sleptWith = null;
        cooldown = 100;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (bed == null) return;
        if (kiko.isSleeping()) {
            PlayerEntity f = friend();
            if (friendSleeping(f)) sleptWith = f;
            return;
        }
        ticks++;
        Vec3d c = Vec3d.ofCenter(bed);
        if (!atBed()) {
            // ¿Se atoró o ya llegó a la puerta? Cada segundo revisa y vuelve a buscar camino
            if (ticks % 20 == 0) {
                if (kiko.getPos().squaredDistanceTo(lastPos) < 0.5 * 0.5) stuckTicks += 20;
                else stuckTicks = 0;
                lastPos = kiko.getPos();
                if (stuckTicks >= 60 || kiko.getNavigation().isIdle()) {
                    if (spot != null && stuckTicks >= 60) triedSpots.add(spot);
                    if (stuckTicks >= 60) stuckTicks = 0;
                    planRoute();
                    if (kiko.isSleeping() || atBed()) return;
                }
                // Ya probó mucho rato: se va directo
                if (ticks >= 400 && !atBed()) teleportToBed();
            }
            return;
        }
        kiko.getLookControl().lookAt(c.x, c.y, c.z);
        kiko.getNavigation().stop();
        // La cama pudo desaparecer o ocuparse mientras llegaba
        if (isFreeHead(kiko.getWorld(), bed)) {
            kiko.sleep(bed);
            kiko.setExpression(null, 0);
        } else {
            bed = null;
        }
    }

    // ------------------------------------------------------------------

    @Nullable
    private BlockPos findBed() {
        World world = kiko.getWorld();
        PlayerEntity f = friend();

        // 1) Junto a su amigo dormido (los hijos no, ellos en su propia cama)
        if (!kiko.isKid() && friendSleeping(f)) {
            BlockPos fb = f.getSleepingPosition().orElse(null);
            if (fb != null) {
                BlockState fs = world.getBlockState(fb);
                if (fs.getBlock() instanceof KikoBedBlock) {
                    BlockPos other = KikoBedBlock.partnerHead(fs, fb);
                    if (isFreeHead(world, other)) return other;
                }
                // Dos camas normales pegadas
                for (Direction d : Direction.Type.HORIZONTAL) {
                    BlockPos n = fb.offset(d);
                    if (isFreeHead(world, n)) return n;
                }
            }
        }

        // 2) La cama de Kiko libre más cercana (a su casa, si tiene)
        BlockPos base = kiko.getHome() != null && world.isChunkLoaded(kiko.getHome()) ? kiko.getHome() : kiko.getBlockPos();
        BlockPos best = null;
        double bestD = Double.MAX_VALUE;
        for (BlockPos p : BlockPos.iterateOutwards(base, SEARCH, 4, SEARCH)) {
            BlockState s = world.getBlockState(p);
            if (!(s.getBlock() instanceof KikoBedBlock) || !isFreeHead(world, p)) continue;
            if (isSomeoneElsesHome(world, p)) continue;
            double d = p.getSquaredDistance(kiko.getPos());
            if (d < bestD) {
                bestD = d;
                best = p.toImmutable();
            }
        }
        return best;
    }

    /** ¿Esta cama es la casa de otra chica? (cada quien duerme en la suya) */
    private boolean isSomeoneElsesHome(World world, BlockPos bedPos) {
        BlockPos mine = kiko.getHome();
        if (mine != null && mine.isWithinDistance(bedPos, 2.5)) return false;
        for (KikoEntity other : world.getEntitiesByClass(KikoEntity.class, new net.minecraft.util.math.Box(bedPos).expand(64),
                k -> k != kiko && k.isAlive())) {
            BlockPos h = other.getHome();
            if (h != null && h.isWithinDistance(bedPos, 2.5)) return true;
        }
        return false;
    }

    private static boolean isFreeHead(World world, BlockPos pos) {
        BlockState s = world.getBlockState(pos);
        return s.getBlock() instanceof BedBlock && s.get(BedBlock.PART) == BedPart.HEAD && !s.get(BedBlock.OCCUPIED);
    }
}
