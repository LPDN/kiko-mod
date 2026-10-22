package com.jaciel.kiko.entity.ai;

import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

/**
 * Como los lobos. Si su mejor amigo le pega a algo, Kiko lo ataca también.
 * Y si algo le pega a su amigo, va contra eso.
 * Nunca ataca jugadores, otras Kikos ni mascotas de su amigo.
 */
public class HelpFriendTargetGoal extends Goal {
    private final KikoEntity kiko;
    private int lastAttackTime;
    private int lastAttackedTime;
    private LivingEntity candidate;

    public HelpFriendTargetGoal(KikoEntity kiko) {
        this.kiko = kiko;
        this.setControls(EnumSet.of(Control.TARGET));
    }

    @Override
    public boolean canStart() {
        if (kiko.getCombatMode() == com.jaciel.kiko.entity.combat.CombatMode.PASSIVE) return false;
        PlayerEntity friend = kiko.getFriendAbove(KikoConfig.get().defendThreshold);
        if (friend == null) return false;

        LivingEntity attacked = friend.getAttacking();
        if (attacked != null && friend.getLastAttackTime() != lastAttackTime
                && friend.age - friend.getLastAttackTime() < 100 && isValid(attacked, friend)) {
            lastAttackTime = friend.getLastAttackTime();
            candidate = attacked;
            return true;
        }
        LivingEntity attacker = friend.getAttacker();
        if (attacker != null && friend.getLastAttackedTime() != lastAttackedTime && isValid(attacker, friend)) {
            lastAttackedTime = friend.getLastAttackedTime();
            candidate = attacker;
            return true;
        }
        return false;
    }

    private boolean isValid(LivingEntity target, PlayerEntity friend) {
        if (!target.isAlive() || target == kiko || target instanceof PlayerEntity || target instanceof KikoEntity) return false;
        if (target instanceof Tameable pet && pet.getOwner() == friend) return false;
        return kiko.squaredDistanceTo(target) < 24 * 24;
    }

    @Override
    public void start() {
        kiko.setTarget(candidate);
        candidate = null;
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }
}
