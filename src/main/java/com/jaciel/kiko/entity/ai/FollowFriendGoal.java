package com.jaciel.kiko.entity.ai;

import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

/**
 * Kiko sigue a su mejor amigo si el afecto llega al umbral de seguir (50 por defecto)
 * y si no le dijiste que se quedara quieta. Si te alejas mucho se teletransporta cerca.
 */
public class FollowFriendGoal extends Goal {
    private static final double START_DISTANCE = 5.0;
    private static final double STOP_DISTANCE = 2.5;

    private final KikoEntity kiko;
    private final double speed;
    private PlayerEntity friend;
    private int repathTimer;

    public FollowFriendGoal(KikoEntity kiko, double speed) {
        this.kiko = kiko;
        this.speed = speed;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (!kiko.isFollowEnabled() || kiko.getTarget() != null || kiko.hasVehicle() || kiko.isSleeping()
                || kiko.getWorkMode() != com.jaciel.kiko.entity.work.WorkMode.NONE) return false;
        PlayerEntity p = kiko.getFriendAbove(KikoConfig.get().followThreshold);
        if (p == null || kiko.squaredDistanceTo(p) < START_DISTANCE * START_DISTANCE) return false;
        this.friend = p;
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return friend != null && friend.isAlive() && kiko.isFollowEnabled() && kiko.getTarget() == null
                && kiko.getWorkMode() == com.jaciel.kiko.entity.work.WorkMode.NONE
                && kiko.squaredDistanceTo(friend) > STOP_DISTANCE * STOP_DISTANCE;
    }

    @Override
    public void start() {
        this.repathTimer = 0;
        kiko.spawnParticles(ParticleTypes.HAPPY_VILLAGER, 4); // estrellitas al empezar a seguirte
    }

    @Override
    public void stop() {
        this.friend = null;
        kiko.getNavigation().stop();
    }

    @Override
    public void tick() {
        kiko.getLookControl().lookAt(friend, 10.0f, kiko.getMaxLookPitchChange());
        if (--repathTimer > 0) return;
        repathTimer = 10;

        float tp = KikoConfig.get().followTeleportDistance;
        if (kiko.squaredDistanceTo(friend) > tp * tp && friend.isOnGround()) {
            kiko.teleportNear(friend);
        } else {
            kiko.getNavigation().startMovingTo(friend, speed);
        }
    }
}
