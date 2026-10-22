package com.jaciel.kiko.romance;

import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.family.KikoChildEntity;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.UUID;

/**
 * Aniversario de boda (v3.2).
 *  Cada tantos días de juego (config anniversaryDays, 7 por defecto) desde la boda, en la mañana y si andas cerca
 *    - se pone el vestido de novia todo el día, baila, cohetes, corazones y campanas
 *    - te da pastel y un regalito que crece con los años (diamantes, y cada 5 aniversarios una manzana encantada)
 *    - los hijos y las demás chicas que anden cerca aplauden
 *  Si ese día le das un ramo de flores se pone feliz de verdad (+10 de afecto)
 */
public final class Anniversary {
    private Anniversary() {}

    /** Cada segundo por cada esposa. */
    static void tick(ServerWorld world, KikoEntity girl, UUID partner) {
        long now = world.getTimeOfDay();
        long today = now / 24000L;
        // Bodas de antes de la v3.2 (o si regresaron el reloj): cuenta desde hoy
        if (girl.getWeddingDay() < 0 || girl.getWeddingDay() > today) {
            girl.setWeddingDay(today);
            MarriageState.get(world.getServer()).update(partner, girl.getUuid(), girl.getDisplayName().getString(), today);
        } else if (girl.age % 600 == 11) {
            MarriageState.get(world.getServer()).update(partner, girl.getUuid(), girl.getDisplayName().getString(), girl.getWeddingDay());
        }
        KikoConfig cfg = KikoConfig.get();
        if (!cfg.anniversaryEnabled) return;
        long since = today - girl.getWeddingDay();
        int period = Math.max(1, cfg.anniversaryDays);
        if (since <= 0 || since % period != 0 || girl.getAnniversaryDay() == today) return;
        if (now % 24000L > 12000L || girl.isSleeping() || girl.isAway()) return;   // de día y despierta
        PlayerEntity p = world.getPlayerByUuid(partner);
        if (p == null || p.isSpectator() || p.squaredDistanceTo(girl) > 24 * 24) return;   // espera a que llegues
        celebrate(world, girl, p, (int) (since / period));
    }

    private static void celebrate(ServerWorld world, KikoEntity girl, PlayerEntity p, int number) {
        long now = world.getTimeOfDay();
        girl.setAnniversaryDay(now / 24000L);
        Romance.immune(world, girl, p);
        Romance.rockets(world, girl, 3);
        girl.wearWeddingDress((int) Math.max(6000, 24000 - now % 24000L));
        girl.getAffection().add(p.getUuid(), 5f, world.getTime());
        girl.playAnimation(KikoAnimation.DANCE);
        girl.setExpression(KikoExpression.LAUGH, 200);
        girl.spawnParticles(ParticleTypes.HEART, 20);
        world.playSound(null, girl.getBlockPos(), SoundEvents.BLOCK_BELL_USE, SoundCategory.NEUTRAL, 1.2f, 1.1f);
        world.playSound(null, girl.getBlockPos(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.NEUTRAL, 0.7f, 1.2f);
        world.spawnParticles(ParticleTypes.CHERRY_LEAVES, girl.getX(), girl.getY() + 2.5, girl.getZ(), 40, 2.0, 0.5, 2.0, 0.02);

        // Regalito
        p.getInventory().offerOrDrop(new ItemStack(Items.CAKE));
        p.getInventory().offerOrDrop(new ItemStack(Items.DIAMOND, Math.min(5, number)));
        if (number % 5 == 0) p.getInventory().offerOrDrop(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));

        // Todos los que estén cerca aplauden (los hijos de ella también)
        for (KikoEntity other : world.getEntitiesByClass(KikoEntity.class, girl.getBoundingBox().expand(24),
                k -> k != girl && !k.isSleeping())) {
            boolean ourKid = other instanceof KikoChildEntity kid && girl.getUuid().equals(kid.getMotherUuid());
            other.playAnimation(ourKid ? KikoAnimation.CHEER : KikoAnimation.CLAP);
            other.setExpression(KikoExpression.GRIN, 100);
            other.spawnParticles(ParticleTypes.HEART, ourKid ? 4 : 2);
            if (ourKid && other.getRandom().nextInt(2) == 0) KikoInteractionHandler.say(p, other, "dialogue.kiko.anniversary_kid", 3);
        }
        KikoInteractionHandler.say(p, girl, "dialogue.kiko.anniversary", 3);
        p.sendMessage(Text.translatable("message.kiko.anniversary", girl.getDisplayName(), number).formatted(Formatting.LIGHT_PURPLE), false);
        p.sendMessage(Text.translatable("message.kiko.anniversary_hint").formatted(Formatting.GRAY), false);
    }

    /** Le diste un ramo a tu esposa: si hoy es su aniversario cuenta (una vez). */
    static boolean flowers(KikoEntity girl, PlayerEntity p, ItemStack stack) {
        if (!(girl.getWorld() instanceof ServerWorld world) || girl.getRelation() != Romance.WIFE) return false;
        long today = world.getTimeOfDay() / 24000L;
        if (girl.getAnniversaryDay() != today || girl.getAnniversaryFlowersDay() == today) return false;
        stack.decrementUnlessCreative(1, p);
        girl.setAnniversaryFlowersDay(today);
        girl.getAffection().add(p.getUuid(), 10f, world.getTime());
        girl.playAnimation(KikoAnimation.HUG);
        girl.setExpression(KikoExpression.EMBARRASSED, 160);
        girl.spawnParticles(ParticleTypes.HEART, 16);
        world.playSound(null, girl.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.NEUTRAL, 0.7f, 1.5f);
        KikoInteractionHandler.say(p, girl, "dialogue.kiko.anniversary_flowers", 3);
        return true;
    }
}
