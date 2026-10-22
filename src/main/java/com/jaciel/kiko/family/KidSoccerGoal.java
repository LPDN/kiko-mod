package com.jaciel.kiko.family;

import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.entity.SoccerBallEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

/**
 * Cascarita (v3.2.1). Si hay una pelota cerca, los niños (y a veces los adolescentes) van tras ella y la patean.
 *  - Si hay portería cerca (dos cercas con 2 a 4 bloques libres) tiran a gol
 *  - Si no, se la pasan a otro niño o a ti si andas cerca
 */
public class KidSoccerGoal extends Goal {
    private final KikoChildEntity kid;
    private SoccerBallEntity ball;
    @Nullable
    private Vec3d goal;
    private int playTicks, repath, cooldown, goalScan;

    public KidSoccerGoal(KikoChildEntity kid) {
        this.kid = kid;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
    }

    private boolean basicOk() {
        int st = kid.getStage();
        return (st == KikoChildEntity.CHILD || st == KikoChildEntity.TEEN) && !kid.getWorld().isNight() && !kid.isSleeping()
                && kid.getTarget() == null && !kid.isBeingAttended() && !kid.isFollowEnabled() && !kid.hasVehicle()
                && !kid.isTouchingWater();
    }

    @Override
    public boolean canStart() {
        if (!basicOk()) return false;
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        if (kid.getRandom().nextInt(kid.getStage() == KikoChildEntity.TEEN ? 120 : 30) != 0) return false;
        List<SoccerBallEntity> balls = kid.getWorld().getEntitiesByClass(SoccerBallEntity.class, kid.getBoundingBox().expand(16),
                b -> b.isAlive() && !b.isTouchingWater());
        if (balls.isEmpty()) return false;
        ball = balls.get(0);
        for (SoccerBallEntity b : balls) if (b.squaredDistanceTo(kid) < ball.squaredDistanceTo(kid)) ball = b;
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return basicOk() && playTicks > 0 && ball != null && ball.isAlive() && ball.squaredDistanceTo(kid) < 24 * 24;
    }

    @Override
    public void start() {
        playTicks = 900 + kid.getRandom().nextInt(900);
        repath = 0;
        goalScan = 0;
        goal = null;
        // Si estaba jugando a las traes lo deja
        if (kid.playTicks > 0) kid.endGame();
    }

    @Override
    public void stop() {
        kid.getNavigation().stop();
        ball = null;
        cooldown = 600 + kid.getRandom().nextInt(1200);
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        playTicks--;
        kid.getLookControl().lookAt(ball, 30f, 30f);
        if (--goalScan <= 0) {
            goal = SoccerBallEntity.findGoalNear(kid.getWorld(), ball.getBlockPos(), 12);
            goalScan = 100;
        }
        double d = kid.squaredDistanceTo(ball);
        if (--repath <= 0) {
            // Se pone detrás de la pelota (del lado contrario a donde va a tirar) para patearla bien
            Vec3d aim = aimPoint();
            Vec3d behind = ball.getPos();
            if (aim != null && d < 4 * 4) {
                Vec3d dir = new Vec3d(aim.x - ball.getX(), 0, aim.z - ball.getZ());
                if (dir.lengthSquared() > 0.01) behind = behind.subtract(dir.normalize().multiply(0.6));
            }
            kid.getNavigation().startMovingTo(behind.x, behind.y, behind.z, d > 6 * 6 ? 1.25 : 1.1);
            repath = 6;
        }
        if (d < 1.5 * 1.5 && ball.canBeKicked()) kickBall();
    }

    /** Hacia dónde quiere tirar: la portería, otro niño o tú. */
    @Nullable
    private Vec3d aimPoint() {
        if (goal != null) return goal;
        List<KikoChildEntity> mates = kid.getWorld().getEntitiesByClass(KikoChildEntity.class, kid.getBoundingBox().expand(12),
                o -> o != kid && o.isMinor() && o.getStage() != KikoChildEntity.BABY);
        if (!mates.isEmpty()) return mates.get(Math.floorMod(kid.getId() + kid.age / 100, mates.size())).getPos();
        if (kid.getParentUuid() != null) {
            PlayerEntity p = kid.getWorld().getPlayerByUuid(kid.getParentUuid());
            if (p != null && p.squaredDistanceTo(kid) < 14 * 14) return p.getPos();
        }
        return null;
    }

    private void kickBall() {
        Vec3d aim = aimPoint();
        Vec3d dir;
        double power;
        if (aim != null) {
            dir = new Vec3d(aim.x - ball.getX(), 0, aim.z - ball.getZ());
            double dist = Math.sqrt(dir.horizontalLengthSquared());
            // Pase suavecito a los cuates, tiro fuerte a gol
            power = goal != null ? Math.min(1.0, 0.45 + dist * 0.05) : Math.min(0.8, 0.25 + dist * 0.045);
            // Un poquito de error para que no sean robots
            double err = (kid.getRandom().nextDouble() - 0.5) * 0.5;
            dir = dir.rotateY((float) err);
        } else {
            dir = new Vec3d(ball.getX() - kid.getX(), 0, ball.getZ() - kid.getZ());
            dir = dir.rotateY((float) ((kid.getRandom().nextDouble() - 0.5) * 1.5));
            power = 0.35 + kid.getRandom().nextDouble() * 0.3;
        }
        if (kid.getStage() == KikoChildEntity.TEEN) power *= 1.15;
        ball.kick(dir, power, kid.getRandom().nextInt(4) == 0 ? 0.3 : 0.12, kid);
        kid.playAnimation(KikoAnimation.KICK);
        repath = 0;
    }
}
