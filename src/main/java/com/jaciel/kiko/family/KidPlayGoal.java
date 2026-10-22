package com.jaciel.kiko.family;

import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.List;

/**
 * Los niños juegan (v3.2).
 *  - Con otro niño cerca juegan a "las traes": uno persigue, el otro huye, y al tocarlo se cambian
 *  - Solito corretea alrededor de su casa (o de ti si andas cerca) y brinca
 * Solo de día, sin pelea, sin que lo estés atendiendo y sin seguirte.
 */
public class KidPlayGoal extends Goal {
    private final KikoChildEntity kid;
    private int repath;
    private int jumpTimer;

    public KidPlayGoal(KikoChildEntity kid) {
        this.kid = kid;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
    }

    private boolean basicOk() {
        return kid.getStage() == KikoChildEntity.CHILD && !kid.getWorld().isNight() && !kid.isSleeping()
                && kid.getTarget() == null && !kid.isBeingAttended() && !kid.isFollowEnabled() && !kid.hasVehicle()
                && !kid.isTouchingWater();
    }

    @Override
    public boolean canStart() {
        if (!basicOk()) return false;
        if (kid.playTicks > 0) return true;                // otro niño ya lo invitó
        if (kid.playCooldown > 0) {
            kid.playCooldown--;
            return false;
        }
        if (kid.getRandom().nextInt(60) != 0) return false;
        // ¿Hay otro niño con quién jugar?
        List<KikoChildEntity> others = kid.getWorld().getEntitiesByClass(KikoChildEntity.class, kid.getBoundingBox().expand(16),
                o -> o != kid && o.getStage() == KikoChildEntity.CHILD && o.playTicks <= 0 && !o.isFollowEnabled()
                        && !o.isSleeping() && !o.isBeingAttended());
        int length = 600 + kid.getRandom().nextInt(600);
        if (!others.isEmpty()) {
            KikoChildEntity other = others.get(kid.getRandom().nextInt(others.size()));
            kid.startGame(other, true, length);
            other.startGame(kid, false, length);
            kid.playAnimation(KikoAnimation.TAG);
            kid.setExpression(KikoExpression.GRIN, 60);
            sayToParent("dialogue.kiko.play_start", 3);
        } else {
            kid.startGame(null, false, 300 + kid.getRandom().nextInt(300));
        }
        return true;
    }

    @Override
    public boolean shouldContinue() {
        if (!basicOk() || kid.playTicks <= 0) return false;
        KikoChildEntity mate = kid.getPlaymate();
        if (kid.hasPlaymateId() && (mate == null || !mate.isAlive() || mate.squaredDistanceTo(kid) > 28 * 28)) return false;
        return true;
    }

    @Override
    public void start() {
        repath = 0;
        jumpTimer = 20 + kid.getRandom().nextInt(40);
    }

    @Override
    public void stop() {
        KikoChildEntity mate = kid.getPlaymate();
        kid.endGame();
        if (mate != null && kid.getUuid().equals(mate.getPlaymateId())) mate.endGame();
        kid.getNavigation().stop();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        kid.playTicks--;
        if (kid.tagCooldown > 0) kid.tagCooldown--;
        // Brinquitos de vez en cuando
        if (--jumpTimer <= 0) {
            if (kid.isOnGround()) kid.getJumpControl().setActive();
            jumpTimer = 25 + kid.getRandom().nextInt(50);
        }
        KikoChildEntity mate = kid.getPlaymate();
        if (mate == null) {
            soloPlay();
            return;
        }
        kid.getLookControl().lookAt(mate, 30f, 30f);
        if (kid.isIt()) {
            // El que las trae espera tantito después de que lo tocaron ("contando") y luego persigue
            if (kid.tagCooldown > 0) {
                kid.getNavigation().stop();
                return;
            }
            if (--repath <= 0) {
                kid.getNavigation().startMovingTo(mate, 1.25);
                repath = 8;
            }
            if (kid.squaredDistanceTo(mate) < 1.4 * 1.4) tag(mate);
        } else {
            // Huye cuando lo vienen persiguiendo
            if (--repath <= 0 || kid.getNavigation().isIdle()) {
                double d = kid.squaredDistanceTo(mate);
                if (d < 7 * 7 || kid.getNavigation().isIdle()) {
                    Vec3d away = NoPenaltyTargeting.findFrom(kid, 9, 3, mate.getPos());
                    if (away != null) kid.getNavigation().startMovingTo(away.x, away.y, away.z, 1.2);
                }
                repath = 15;
            }
        }
    }

    /** ¡Te toqué! Se cambian los papeles. */
    private void tag(KikoChildEntity mate) {
        kid.setIt(false);
        mate.setIt(true);
        mate.tagCooldown = 30;
        kid.tagCooldown = 10;
        kid.playAnimation(KikoAnimation.TAG);
        kid.setExpression(KikoExpression.LAUGH, 50);
        mate.setExpression(KikoExpression.SURPRISED, 30);
        mate.playAnimation(KikoAnimation.CHEER);
        if (kid.getWorld() instanceof ServerWorld world) {
            world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, mate.getX(), mate.getBodyY(0.7), mate.getZ(), 6, 0.3, 0.3, 0.3, 0);
            world.playSound(null, mate.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), SoundCategory.NEUTRAL, 0.5f, 1.6f + kid.getRandom().nextFloat() * 0.3f);
        }
        if (kid.getRandom().nextInt(3) == 0) sayToParent("dialogue.kiko.play_tag", 4);
        repath = 0;
    }

    /** Solito: corretea alrededor de ti (si andas cerca) o de su casa. */
    private void soloPlay() {
        if (--repath > 0 && !kid.getNavigation().isIdle()) return;
        repath = 30 + kid.getRandom().nextInt(30);
        PlayerEntity parent = kid.getParentUuid() != null ? kid.getWorld().getPlayerByUuid(kid.getParentUuid()) : null;
        Vec3d target;
        if (parent != null && parent.squaredDistanceTo(kid) < 12 * 12) {
            double a = kid.getRandom().nextDouble() * Math.PI * 2;
            target = parent.getPos().add(Math.cos(a) * 3, 0, Math.sin(a) * 3);
        } else {
            target = NoPenaltyTargeting.find(kid, 7, 2);
        }
        if (target != null) kid.getNavigation().startMovingTo(target.x, target.y, target.z, 1.15);
        if (kid.getRandom().nextInt(4) == 0) kid.playAnimation(KikoAnimation.CHEER);
    }

    private void sayToParent(String key, int variants) {
        if (kid.getParentUuid() == null) return;
        PlayerEntity p = kid.getWorld().getPlayerByUuid(kid.getParentUuid());
        if (p != null && p.squaredDistanceTo(kid) < 16 * 16) KikoInteractionHandler.say(p, kid, key, variants);
    }
}
