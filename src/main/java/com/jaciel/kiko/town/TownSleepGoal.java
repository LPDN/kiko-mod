package com.jaciel.kiko.town;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

/**
 * Los vecinos se meten a su casa en la noche y se duermen en una cama normal libre cerca de donde viven (v3.4).
 * Si no hay cama se quedan quietos adentro. En la mañana KikoEntity los levanta solo.
 */
public class TownSleepGoal extends Goal {
    private static final int SEARCH = 8;
    private final TownsfolkEntity npc;
    @Nullable
    private BlockPos bed;
    private int ticks, cooldown;

    public TownSleepGoal(TownsfolkEntity npc) {
        this.npc = npc;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
    }

    /** Noche según la hora del día (no según la luz, así una tormenta de día no los acuesta). */
    public static boolean isNightTime(World w) {
        long t = w.getTimeOfDay() % 24000L;
        return t >= 12600L && t < 23400L;
    }

    private boolean night() {
        return isNightTime(npc.getWorld()) && npc.getHome() != null && !npc.isBeingAttended() && !npc.isNightWatch();
    }

    @Override
    public boolean canStart() {
        if (npc.isSleeping() || --cooldown > 0) return false;
        cooldown = 40;
        return night();
    }

    @Override
    public boolean shouldContinue() {
        // Ya dormido sigue "ocupado" en esto, así ninguna otra cosa lo mueve hasta que amanezca
        return npc.isSleeping() || night();
    }

    @Override
    public void start() {
        ticks = 0;
        bed = findBed();
        BlockPos target = bed != null ? bed : npc.getHome();
        if (target != null) npc.getNavigation().startMovingTo(target.getX() + 0.5, target.getY(), target.getZ() + 0.5, 0.9);
    }

    @Override
    public void stop() {
        npc.getNavigation().stop();
        bed = null;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (npc.isSleeping()) {
            npc.getNavigation().stop();
            return;
        }
        ticks++;
        BlockPos home = npc.getHome();
        if (home == null) return;
        BlockPos target = bed != null ? bed : home;
        Vec3d c = Vec3d.ofBottomCenter(target);
        double d = npc.squaredDistanceTo(c);
        if (bed != null && d < 2.2 * 2.2) {
            npc.getNavigation().stop();
            if (isFreeHead(npc.getWorld(), bed)) npc.sleep(bed);
            else bed = findBed();
            return;
        }
        if (bed == null && d < 1.5 * 1.5) {
            // Sin cama: se queda en su casa
            npc.getNavigation().stop();
            if (ticks % 100 == 0) bed = findBed();
            return;
        }
        if (ticks % 40 == 0 && npc.getNavigation().isIdle()) {
            npc.getNavigation().startMovingTo(c.x, c.y, c.z, 0.9);
        }
        // Se atoró mucho rato (puerta cerrada, escalón raro): aparece en su casa con una nubecita
        if (ticks >= 600) {
            if (npc.getWorld() instanceof ServerWorld sw) {
                sw.spawnParticles(net.minecraft.particle.ParticleTypes.CLOUD, npc.getX(), npc.getBodyY(0.5), npc.getZ(), 8, 0.3, 0.4, 0.3, 0.02);
            }
            npc.refreshPositionAndAngles(c.x, c.y + (bed != null ? 0.6 : 0), c.z, npc.getYaw(), npc.getPitch());
            npc.getNavigation().stop();
            ticks = 0;
        }
    }

    @Nullable
    private BlockPos findBed() {
        BlockPos home = npc.getHome();
        if (home == null) return null;
        World world = npc.getWorld();
        BlockPos best = null;
        double bestD = Double.MAX_VALUE;
        for (BlockPos p : BlockPos.iterateOutwards(home, SEARCH, 3, SEARCH)) {
            BlockState s = world.getBlockState(p);
            if (s.getBlock() instanceof com.jaciel.kiko.block.KikoBedBlock || !isFreeHead(world, p)) continue;
            double dd = p.getSquaredDistance(home);
            if (dd < bestD) {
                bestD = dd;
                best = p.toImmutable();
            }
        }
        return best;
    }

    private static boolean isFreeHead(World world, BlockPos pos) {
        BlockState s = world.getBlockState(pos);
        return s.getBlock() instanceof BedBlock && s.get(BedBlock.PART) == BedPart.HEAD && !s.get(BedBlock.OCCUPIED);
    }
}
