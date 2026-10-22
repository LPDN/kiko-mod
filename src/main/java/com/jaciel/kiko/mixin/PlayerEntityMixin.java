package com.jaciel.kiko.mixin;

import com.jaciel.kiko.entity.KikoCharacter;
import com.jaciel.kiko.romance.WifeBonuses;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Bonus de esposa que tocan al jugador directo (v3.0).
 *  - Lumi cerca: +50 % de experiencia
 *  - Tami cerca: te da 40 % menos hambre
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @ModifyVariable(method = "addExperience", at = @At("HEAD"), argsOnly = true)
    private int kiko$lumiXp(int experience) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (experience > 0 && !self.getWorld().isClient && WifeBonuses.isNear(self, KikoCharacter.LUMI)) {
            return experience + Math.max(1, experience / 2);
        }
        return experience;
    }

    @ModifyVariable(method = "addExhaustion", at = @At("HEAD"), argsOnly = true)
    private float kiko$tamiHunger(float exhaustion) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (exhaustion > 0 && !self.getWorld().isClient && WifeBonuses.isNear(self, KikoCharacter.TAMI)) {
            return exhaustion * 0.6f;
        }
        return exhaustion;
    }
}
