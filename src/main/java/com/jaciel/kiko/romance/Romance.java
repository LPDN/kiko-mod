package com.jaciel.kiko.romance;

import com.jaciel.kiko.affection.KikoAffectionSystem;
import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoCharacter;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import com.jaciel.kiko.outfit.KikoOutfit;
import com.jaciel.kiko.registry.ModItems;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

/**
 * Novias y esposas (v3.0).
 *
 *  Ramo de flores  → si te tiene en nivel Mejor amiga se vuelve tu novia
 *  Anillo          → si ya es tu novia y te quiere mucho (90+) se casa contigo, con todo y boda.
 *                    Por defecto solo puedes tener una esposa
 *  Anillo del Abismo (rarísimo, en los cofres de las ciudades antiguas)
 *                  → funciona igual que el anillo pero te deja casarte aunque ya tengas esposa
 *
 *  Celos: si traes novia o esposa cerca y le das regalos a otra chica, se pone celosa (baja el afecto).
 *  Si a tu novia le baja mucho el afecto, truena contigo. Las esposas no se van (solo si mueren).
 *  Entre esposas (casadas con el Anillo del Abismo) no hay celos, ya saben cómo está la cosa.
 */
public final class Romance {
    private Romance() {}

    public static final int NONE = 0, GIRLFRIEND = 1, WIFE = 2;

    /** Quién no se lastima con fuegos artificiales y hasta cuándo (tick del mundo). */
    private static final java.util.Map<java.util.UUID, Long> FIREWORK_IMMUNE = new java.util.concurrent.ConcurrentHashMap<>();

    public static void register() {
        net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (FIREWORK_IMMUNE.isEmpty() || !source.isOf(net.minecraft.entity.damage.DamageTypes.FIREWORKS)) return true;
            Long until = FIREWORK_IMMUNE.get(entity.getUuid());
            if (until == null) return true;
            if (entity.getWorld().getTime() > until) {
                FIREWORK_IMMUNE.remove(entity.getUuid());
                return true;
            }
            return false;
        });
    }

    /** v3.3 la versión de chico de un texto (novio, esposo...) si existe. */
    public static String nk(KikoEntity npc, String key) {
        if (npc.getCharacter().isMale() && net.minecraft.util.Language.getInstance().hasTranslation(key + ".m")) return key + ".m";
        return key;
    }

    /** Ropa de las chicas (que no son Kiko): 0 trabajo, 1 casual, 2 boda. */
    public static final int OUTFIT_WORK = 0, OUTFIT_CASUAL = 1, OUTFIT_WEDDING = 2;

    // ------------------------------------------------------------------
    // Ramo y anillo
    // ------------------------------------------------------------------

    /** @return true si el ítem era de romance (ramo o anillo) y ya se atendió. */
    public static boolean handleItem(KikoEntity girl, PlayerEntity player, ItemStack stack) {
        // Con los hijos nada de romance
        if (girl.isKid() && (stack.isOf(ModItems.BOUQUET) || stack.isOf(ModItems.RING) || stack.isOf(ModItems.ABYSS_RING))) {
            girl.setExpression(KikoExpression.SURPRISED, 60);
            KikoInteractionHandler.say(player, girl, "dialogue.kid.no_romance", 3);
            return true;
        }
        if (stack.isOf(ModItems.BOUQUET)) {
            confess(girl, player, stack);
            return true;
        }
        if (stack.isOf(ModItems.RING) || stack.isOf(ModItems.ABYSS_RING)) {
            propose(girl, player, stack, stack.isOf(ModItems.ABYSS_RING));
            return true;
        }
        return false;
    }

    private static void confess(KikoEntity girl, PlayerEntity player, ItemStack stack) {
        float aff = girl.getAffection().get(player.getUuid());
        boolean mine = player.getUuid().equals(girl.getPartnerUuid());
        if (mine) {
            // v3.2 en su aniversario sí le cuentan las flores
            if (Anniversary.flowers(girl, player, stack)) return;
            girl.setExpression(KikoExpression.EMBARRASSED, 80);
            KikoInteractionHandler.say(player, girl, "dialogue.kiko.already_yours", 3);
            return;
        }
        if (girl.getPartnerUuid() != null) {
            girl.setExpression(KikoExpression.SAD, 80);
            KikoInteractionHandler.say(player, girl, "dialogue.kiko.taken", 3);
            return;
        }
        if (aff < KikoConfig.get().specialThreshold) {
            girl.setExpression(KikoExpression.NERVOUS, 80);
            girl.playAnimation(KikoAnimation.REJECT);
            KikoInteractionHandler.say(player, girl, "dialogue.kiko.confess_no", 3);
            player.sendMessage(Text.translatable("message.kiko.need_bestie").formatted(Formatting.GRAY), true);
            return;
        }
        stack.decrementUnlessCreative(1, player);
        jealousyFromRival(girl, player, 10f);
        girl.setRelation(player.getUuid(), GIRLFRIEND);
        girl.getAffection().add(player.getUuid(), 5f, girl.getWorld().getTime());
        girl.setExpression(KikoExpression.EMBARRASSED, 140);
        girl.playAnimation(KikoAnimation.HUG);
        girl.spawnParticles(ParticleTypes.HEART, 14);
        girl.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 0.8f, 1.6f);
        KikoInteractionHandler.say(player, girl, "dialogue.kiko.confess_yes", 3);
        player.sendMessage(Text.translatable(nk(girl, "message.kiko.now_girlfriend"), girl.getDisplayName()).formatted(Formatting.LIGHT_PURPLE), false);
    }

    private static void propose(KikoEntity girl, PlayerEntity player, ItemStack stack, boolean abyss) {
        if (!(girl.getWorld() instanceof ServerWorld world)) return;
        boolean mine = player.getUuid().equals(girl.getPartnerUuid());
        if (mine && girl.getRelation() == WIFE) {
            girl.setExpression(KikoExpression.EMBARRASSED, 80);
            KikoInteractionHandler.say(player, girl, "dialogue.kiko.already_wife", 3);
            return;
        }
        if (!mine || girl.getRelation() != GIRLFRIEND) {
            girl.setExpression(KikoExpression.NERVOUS, 80);
            KikoInteractionHandler.say(player, girl, "dialogue.kiko.propose_not_yet", 3);
            player.sendMessage(Text.translatable(nk(girl, "message.kiko.need_girlfriend")).formatted(Formatting.GRAY), true);
            return;
        }
        if (girl.getAffection().get(player.getUuid()) < KikoConfig.get().marriageThreshold) {
            girl.setExpression(KikoExpression.NERVOUS, 80);
            KikoInteractionHandler.say(player, girl, "dialogue.kiko.propose_wait", 3);
            player.sendMessage(Text.translatable("message.kiko.need_love", KikoConfig.get().marriageThreshold).formatted(Formatting.GRAY), true);
            return;
        }
        MarriageState ms = MarriageState.get(world.getServer());
        if (!abyss && ms.count(player.getUuid()) > 0) {
            player.sendMessage(Text.translatable(KikoInteractionHandler.gendered(player, "message.kiko.already_married")).formatted(Formatting.GOLD), false);
            return;
        }
        stack.decrementUnlessCreative(1, player);
        ms.add(player.getUuid(), girl.getUuid(), girl.getCharacter().id(), girl.getDisplayName().getString(), world.getTimeOfDay() / 24000L);
        girl.setWeddingDay(world.getTimeOfDay() / 24000L);
        girl.setRelation(player.getUuid(), WIFE);
        wedding(world, girl, player);
    }

    /** La boda: se pone el vestido de novia (un día), campanas, cohetes, corazones y todos aplauden. */
    private static void wedding(ServerWorld world, KikoEntity girl, PlayerEntity player) {
        immune(world, girl, player);
        girl.getAffection().add(player.getUuid(), 10f, world.getTime());
        girl.wearWeddingDress(24000);
        girl.setExpression(KikoExpression.LAUGH, 200);
        girl.playAnimation(KikoAnimation.HAPPY);
        girl.spawnParticles(ParticleTypes.HEART, 30);
        world.playSound(null, girl.getBlockPos(), SoundEvents.BLOCK_BELL_USE, SoundCategory.NEUTRAL, 1.5f, 1.0f);
        world.playSound(null, girl.getBlockPos(), SoundEvents.BLOCK_BELL_RESONATE, SoundCategory.NEUTRAL, 1.0f, 1.2f);
        world.playSound(null, girl.getBlockPos(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.NEUTRAL, 0.8f, 1.0f);
        world.spawnParticles(ParticleTypes.CHERRY_LEAVES, girl.getX(), girl.getY() + 2.5, girl.getZ(), 60, 2.5, 0.5, 2.5, 0.02);
        rockets(world, girl, 5);
        // Las demás chicas que estén cerca festejan
        for (KikoEntity other : world.getEntitiesByClass(KikoEntity.class, girl.getBoundingBox().expand(24), k -> k != girl)) {
            other.playAnimation(KikoAnimation.CLAP);
            other.setExpression(KikoExpression.GRIN, 100);
            other.spawnParticles(ParticleTypes.HEART, 3);
        }
        KikoInteractionHandler.say(player, girl, "dialogue.kiko.wedding", 3);
        Text msg = Text.translatable("message.kiko.wedding", player.getDisplayName(), girl.getDisplayName()).formatted(Formatting.LIGHT_PURPLE);
        for (ServerPlayerEntity p : world.getPlayers(p -> p.squaredDistanceTo(girl) < 96 * 96)) p.sendMessage(msg, false);
    }

    /** Los cohetes no lastiman a nadie de la fiesta (10 segundos de inmunidad a fuegos artificiales). */
    static void immune(ServerWorld world, KikoEntity girl, PlayerEntity player) {
        long until = world.getTime() + 200;
        FIREWORK_IMMUNE.put(girl.getUuid(), until);
        FIREWORK_IMMUNE.put(player.getUuid(), until);
        for (net.minecraft.entity.LivingEntity e : world.getEntitiesByClass(net.minecraft.entity.LivingEntity.class,
                girl.getBoundingBox().expand(24), e -> e instanceof PlayerEntity || e instanceof KikoEntity)) {
            FIREWORK_IMMUNE.put(e.getUuid(), until);
        }
    }

    /** v3.5 inmunidad a los cohetes para todos los de alrededor (bodas del pueblo). */
    public static void immuneAround(ServerWorld world, net.minecraft.entity.Entity center) {
        long until = world.getTime() + 200;
        for (net.minecraft.entity.LivingEntity e : world.getEntitiesByClass(net.minecraft.entity.LivingEntity.class,
                center.getBoundingBox().expand(32), e -> e instanceof PlayerEntity || e instanceof KikoEntity)) {
            FIREWORK_IMMUNE.put(e.getUuid(), until);
        }
    }

    /** Cohetes de colores en círculo alrededor de ella. */
    public static void rockets(ServerWorld world, KikoEntity girl, int n) {
        for (int i = 0; i < n; i++) {
            double a = i * Math.PI * 2 / n;
            ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
            rocket.set(DataComponentTypes.FIREWORKS, new FireworksComponent(1 + i % 2, List.of(new FireworkExplosionComponent(
                    i % 2 == 0 ? FireworkExplosionComponent.Type.LARGE_BALL : FireworkExplosionComponent.Type.STAR,
                    IntList.of(0xFF7FAA, 0xFFFFFF, 0xFFD23C), IntList.of(0xFFC0D8), true, true))));
            world.spawnEntity(new FireworkRocketEntity(world, girl.getX() + Math.cos(a) * 4, girl.getY() + 0.5, girl.getZ() + Math.sin(a) * 4, rocket));
        }
    }

    // ------------------------------------------------------------------
    // Celos
    // ------------------------------------------------------------------

    /** Le diste un regalo a `girl`: tu novia o esposa (otra) que ande cerca se pone celosa. */
    public static void onGift(KikoEntity girl, PlayerEntity player) {
        jealousyFromRival(girl, player, 3f);
    }

    private static void jealousyFromRival(KikoEntity girl, PlayerEntity player, float amount) {
        if (!(girl.getWorld() instanceof ServerWorld world)) return;
        boolean girlIsWife = player.getUuid().equals(girl.getPartnerUuid()) && girl.getRelation() == WIFE;
        for (KikoEntity rival : world.getEntitiesByClass(KikoEntity.class, player.getBoundingBox().expand(16),
                k -> k != girl && player.getUuid().equals(k.getPartnerUuid()) && k.getRelation() != NONE)) {
            // Entre esposas no hay celos
            if (girlIsWife && rival.getRelation() == WIFE) continue;
            if (!rival.tryJealous(world.getTime())) continue;
            rival.getAffection().add(player.getUuid(), -amount, world.getTime());
            rival.setExpression(KikoExpression.POUT, 120);
            rival.playAnimation(KikoAnimation.REJECT);
            world.spawnParticles(ParticleTypes.ANGRY_VILLAGER, rival.getX(), rival.getBodyY(1.0), rival.getZ(), 4, 0.3, 0.2, 0.3, 0);
            KikoInteractionHandler.say(player, rival, "dialogue.kiko.jealous", 3);
        }
    }

    // ------------------------------------------------------------------
    // Cada segundo (lado servidor)
    // ------------------------------------------------------------------

    public static void tick(KikoEntity girl) {
        if (girl.getRelation() == NONE || !(girl.getWorld() instanceof ServerWorld world)) return;
        java.util.UUID partner = girl.getPartnerUuid();
        if (partner == null) return;
        // Si a tu novia le baja mucho el afecto, truena contigo
        if (girl.getRelation() == GIRLFRIEND
                && KikoAffectionSystem.tierOf(girl.getAffection().get(partner)).ordinal() < KikoAffectionSystem.Tier.FRIENDLY.ordinal()) {
            girl.setRelation(null, NONE);
            girl.setExpression(KikoExpression.SAD, 200);
            girl.playAnimation(KikoAnimation.CRY);
            PlayerEntity p = world.getPlayerByUuid(partner);
            if (p != null) {
                KikoInteractionHandler.say(p, girl, "dialogue.kiko.breakup", 3);
                p.sendMessage(Text.translatable(nk(girl, "message.kiko.breakup"), girl.getDisplayName()).formatted(Formatting.GRAY), false);
            }
            return;
        }
        if (girl.getRelation() == WIFE) {
            WifeBonuses.tick(world, girl, partner);
            Anniversary.tick(world, girl, partner);
            com.jaciel.kiko.family.Family.tickWife(world, girl);
        }
    }

    /** Si una esposa muere se borra del registro. */
    public static void onDeath(KikoEntity girl) {
        if (girl.getRelation() == WIFE && girl.getWorld() instanceof ServerWorld world) {
            MarriageState.get(world.getServer()).remove(girl.getUuid());
        }
    }

    /** ¿Esta ropa de Kiko se puede escoger? (la de novia solo si es tu esposa) */
    public static boolean canWear(KikoEntity girl, KikoOutfit outfit) {
        return outfit != KikoOutfit.WEDDING || girl.getRelation() == WIFE;
    }

    public static boolean isKiko(KikoEntity girl) {
        return girl.getCharacter() == KikoCharacter.KIKO;
    }
}
