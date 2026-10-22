package com.jaciel.kiko.family;

import com.jaciel.kiko.entity.KikoEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.server.world.ServerWorld;

import java.util.EnumSet;

/**
 * De chavo acompaña a su mamá para aprenderle el oficio (v3.2).
 * Si su mamá es Kiko (no tiene oficio) o no anda cerca, se pega a la chica con oficio más cercana (la "tía").
 * Solo de día, si no te está siguiendo y nadie lo está atendiendo. Lo que aprende se cuenta en KikoChildEntity.
 */
public class TeenApprenticeGoal extends Goal {
    private final KikoChildEntity teen;
    private KikoEntity mentor;
    private int repath;
    private int checkCooldown;

    public TeenApprenticeGoal(KikoChildEntity teen) {
        this.teen = teen;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    private boolean basicOk() {
        return teen.getStage() == KikoChildEntity.TEEN && !teen.getWorld().isNight() && !teen.isSleeping()
                && teen.getTarget() == null && !teen.isBeingAttended() && !teen.isFollowEnabled() && !teen.hasVehicle();
    }

    @Override
    public boolean canStart() {
        if (!basicOk()) return false;
        if (--checkCooldown > 0) return false;
        checkCooldown = 40;
        mentor = findMentor();
        return mentor != null && mentor.squaredDistanceTo(teen) > 4 * 4;
    }

    private KikoEntity findMentor() {
        if (!(teen.getWorld() instanceof ServerWorld)) return null;
        KikoEntity mom = teen.getMother();
        if (mom != null && KikoChildEntity.canTeach(mom) && mom.squaredDistanceTo(teen) < 48 * 48 && !mom.isSleeping()) return mom;
        KikoEntity best = null;
        double bestD = 32 * 32;
        for (KikoEntity k : teen.getWorld().getEntitiesByClass(KikoEntity.class, teen.getBoundingBox().expand(32),
                k -> k != teen && KikoChildEntity.canTeach(k) && !k.isSleeping())) {
            double d = k.squaredDistanceTo(teen);
            if (d < bestD) {
                bestD = d;
                best = k;
            }
        }
        return best;
    }

    @Override
    public boolean shouldContinue() {
        return basicOk() && mentor != null && mentor.isAlive() && !mentor.isSleeping()
                && mentor.squaredDistanceTo(teen) > 2.5 * 2.5 && mentor.squaredDistanceTo(teen) < 56 * 56;
    }

    @Override
    public void start() {
        repath = 0;
    }

    @Override
    public void stop() {
        teen.getNavigation().stop();
        mentor = null;
    }

    @Override
    public void tick() {
        teen.getLookControl().lookAt(mentor, 30f, 30f);
        if (--repath <= 0) {
            repath = 10;
            double d = mentor.squaredDistanceTo(teen);
            teen.getNavigation().startMovingTo(mentor, d > 12 * 12 ? 1.15 : 0.95);
        }
    }
}
