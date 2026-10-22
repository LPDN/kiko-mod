package com.jaciel.kiko.entity.ai;

import com.jaciel.kiko.entity.KikoEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

/**
 * Mientras le hablas, tienes abierto su menú o su inventario, Kiko se queda quieta y te mira.
 * Si algo la ataca deja esto y se defiende.
 */
public class StayStillGoal extends Goal {
    private final KikoEntity kiko;

    public StayStillGoal(KikoEntity kiko) {
        this.kiko = kiko;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
    }

    @Override
    public boolean canStart() {
        return kiko.isBeingAttended() && kiko.getTarget() == null;
    }

    @Override
    public boolean shouldContinue() {
        return canStart();
    }

    @Override
    public void start() {
        kiko.getNavigation().stop();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        kiko.getNavigation().stop();
        PlayerEntity p = kiko.getAttendingPlayer();
        if (p != null) kiko.getLookControl().lookAt(p, 30f, 30f);
    }
}
