package com.jaciel.kiko.romance;

import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.Cooking;
import com.jaciel.kiko.entity.KikoCharacter;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import com.jaciel.kiko.item.Dish;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bonus de esposa. Solo cuentan cuando anda cerca de ti (24 bloques) y se pueden apagar en la config.
 *
 *  Kiko  suerte mientras están juntos y un regalito cada día
 *  Maru  cada mañana le pone un encantamiento a una herramienta, arma o armadura tuya que no tenga,
 *        y te repara el equipo sin yunque
 *  Lumi  +50 % de experiencia y un libro encantado cada 2 días
 *  Nadi  velocidad y salto cuando andan juntos, y un mapa a una estructura cada 3 días
 *  Rina  regeneración suavecita y una vez al día te salva de morir (como un tótem)
 *  Tami  te da menos hambre y un platillo de chef cada día
 */
public final class WifeBonuses {
    private WifeBonuses() {}

    private static final double RANGE = 24;
    /** Jugador → último tick (del mundo) en que cada chica estuvo cerca. */
    private static final Map<UUID, long[]> NEAR = new ConcurrentHashMap<>();

    public static void register() {
        // Rina: una vez al día te salva de morir
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) -> {
            if (!(entity instanceof ServerPlayerEntity player) || !KikoConfig.get().wifeBonuses) return true;
            ServerWorld world = player.getServerWorld();
            for (KikoEntity rina : world.getEntitiesByClass(KikoEntity.class, player.getBoundingBox().expand(32),
                    k -> k.getCharacter() == KikoCharacter.RINA && k.getRelation() == Romance.WIFE
                            && player.getUuid().equals(k.getPartnerUuid()) && k.isAlive())) {
                long day = world.getTimeOfDay() / 24000L;
                if (rina.getSaveDay() == day) continue;
                rina.setSaveDay(day);
                player.setHealth(6f);
                player.clearStatusEffects();
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
                world.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, player.getX(), player.getBodyY(0.6), player.getZ(), 60, 0.5, 0.8, 0.5, 0.4);
                world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1f, 1.1f);
                rina.setExpression(KikoExpression.NERVOUS, 120);
                KikoInteractionHandler.say(player, rina, "dialogue.rina.save", 3);
                return false;
            }
            return true;
        });
    }

    /** Para los mixins del jugador: ¿anda cerca su esposa de este tipo? */
    public static boolean isNear(PlayerEntity player, KikoCharacter who) {
        long[] t = NEAR.get(player.getUuid());
        return t != null && player.getWorld().getTime() - t[who.ordinal()] < 60 && KikoConfig.get().wifeBonuses;
    }

    /** Cada segundo, por cada esposa. */
    static void tick(ServerWorld world, KikoEntity wife, UUID partner) {
        if (!KikoConfig.get().wifeBonuses || wife.isAway()) return;
        PlayerEntity p = world.getPlayerByUuid(partner);
        if (p == null || p.isSpectator() || p.squaredDistanceTo(wife) > RANGE * RANGE) return;
        long now = world.getTime();
        NEAR.computeIfAbsent(p.getUuid(), k -> new long[KikoCharacter.values().length])[wife.getCharacter().ordinal()] = now;

        // Lo de cada rato
        switch (wife.getCharacter()) {
            case KIKO -> effect(p, StatusEffects.LUCK, 0);
            case NADI -> {
                effect(p, StatusEffects.SPEED, 0);
                effect(p, StatusEffects.JUMP_BOOST, 0);
            }
            case MARU -> {
                if (now % 200 < 20) {
                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        ItemStack s = p.getEquippedStack(slot);
                        if (!s.isEmpty() && s.isDamageable() && s.isDamaged()) {
                            s.setDamage(Math.max(0, s.getDamage() - Math.max(1, s.getMaxDamage() / 20)));
                        }
                    }
                }
            }
            case RINA -> {
                if (now % 200 < 20 && p.getHealth() < p.getMaxHealth()) {
                    p.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, 0, true, false, true));
                }
            }
            // ---- v3.3 esposos ----
            case TONO -> {
                // Tus cultivos crecen más rápido cuando andan juntos
                if (now % 100 < 20) KikoEntity.growCrops(world, p.getBlockPos(), 8, 16);
            }
            case LEO -> {
                if (p.isTouchingWater()) {
                    effect(p, StatusEffects.DOLPHINS_GRACE, 0);
                    effect(p, StatusEffects.WATER_BREATHING, 0);
                }
            }
            case DANTE -> effect(p, StatusEffects.RESISTANCE, 0);
            case IVAN -> {
                effect(p, StatusEffects.HASTE, 0);
                // En las cuevas ves en la oscuridad
                if (p.getY() < 50 && !world.isSkyVisible(p.getBlockPos())) {
                    StatusEffectInstance nv = p.getStatusEffect(StatusEffects.NIGHT_VISION);
                    if (nv == null || nv.getDuration() < 240) p.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 400, 0, true, false, true));
                }
            }
            case EMI -> {
                // De vez en cuando te canta algo y te da ánimos (corazones dorados)
                if (now % 1200 < 20) {
                    p.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 1200, 0, true, false, true));
                    world.spawnParticles(ParticleTypes.NOTE, wife.getX(), wife.getY() + 2.1, wife.getZ(), 4, 0.4, 0.2, 0.4, 0.5);
                    world.playSound(null, wife.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_GUITAR.value(), SoundCategory.NEUTRAL, 0.8f, 1.2f);
                }
            }
            case BETO -> effect(p, StatusEffects.HERO_OF_THE_VILLAGE, 0);   // los aldeanos te dan descuento
            default -> {}
        }

        // Lo de cada día (en cuanto se ven ese día)
        long day = world.getTimeOfDay() / 24000L;
        if (wife.getDailyDay() == day) return;
        wife.setDailyDay(day);
        daily(world, wife, p, day);
    }

    private static void effect(PlayerEntity p, RegistryEntry<StatusEffect> e, int amp) {
        StatusEffectInstance cur = p.getStatusEffect(e);
        if (cur == null || cur.getDuration() < 40) p.addStatusEffect(new StatusEffectInstance(e, 100, amp, true, false, true));
    }

    private static void daily(ServerWorld world, KikoEntity wife, PlayerEntity p, long day) {
        switch (wife.getCharacter()) {
            case KIKO -> {
                ItemStack[] gifts = {new ItemStack(Items.CAKE), new ItemStack(Items.GOLDEN_APPLE), new ItemStack(Items.EMERALD, 4),
                        new ItemStack(Items.COOKIE, 8), new ItemStack(Items.DIAMOND), new ItemStack(Items.POPPY, 3),
                        new ItemStack(Items.EXPERIENCE_BOTTLE, 4), new ItemStack(Items.GOLD_INGOT, 3)};
                give(wife, p, gifts[wife.getRandom().nextInt(gifts.length)], "dialogue.kiko.wife_gift");
            }
            case MARU -> {
                // Busca algo tuyo sin encantar (herramienta, arma o armadura) y le pone un encantamiento moderado
                List<Integer> slots = new ArrayList<>();
                PlayerInventory inv = p.getInventory();
                for (int i = 0; i < inv.size(); i++) {
                    ItemStack s = inv.getStack(i);
                    if (!s.isEmpty() && s.isEnchantable() && !s.hasEnchantments() && !s.isOf(Items.BOOK)) slots.add(i);
                }
                if (slots.isEmpty()) return;
                int slot = slots.get(wife.getRandom().nextInt(slots.size()));
                ItemStack s = inv.getStack(slot);
                ItemStack out = EnchantmentHelper.enchant(wife.getRandom(), s, 12 + wife.getRandom().nextInt(12),
                        world.getRegistryManager(), Optional.empty());
                inv.setStack(slot, out);
                world.playSound(null, p.getBlockPos(), SoundEvents.BLOCK_SMITHING_TABLE_USE, SoundCategory.PLAYERS, 1f, 1.1f);
                world.spawnParticles(ParticleTypes.ENCHANT, p.getX(), p.getBodyY(0.8), p.getZ(), 30, 0.4, 0.6, 0.4, 0.5);
                wife.setExpression(KikoExpression.SMUG, 80);
                KikoInteractionHandler.say(p, wife, "dialogue.maru.wife_enchant", 3);
                p.sendMessage(Text.translatable("message.kiko.wife_enchant", out.getName()).formatted(Formatting.AQUA), false);
            }
            case LUMI -> {
                if (day % 2 != 0) return;
                ItemStack book = EnchantmentHelper.enchant(wife.getRandom(), new ItemStack(Items.BOOK), 20 + wife.getRandom().nextInt(11),
                        world.getRegistryManager(), Optional.empty());
                give(wife, p, book, "dialogue.lumi.wife_book");
            }
            case NADI -> {
                if (day % 3 != 0) return;
                ItemStack map = com.jaciel.kiko.entity.work.Expedition.scoutMap(world, p.getBlockPos());
                if (map != null) give(wife, p, map, "dialogue.nadi.wife_map");
            }
            case RINA -> KikoInteractionHandler.say(p, wife, "dialogue.rina.wife_morning", 3);
            case TAMI -> {
                Dish[] all = Dish.values();
                give(wife, p, Cooking.make(all[wife.getRandom().nextInt(all.length)], 2, true), "dialogue.tami.wife_dish");
            }
            // ---- v3.3 esposos ----
            case TONO -> {
                ItemStack[] basket = {new ItemStack(Items.BREAD, 6), new ItemStack(Items.CARROT, 8), new ItemStack(Items.BAKED_POTATO, 6),
                        new ItemStack(Items.EGG, 6), new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.GOLDEN_CARROT, 3)};
                give(wife, p, basket[wife.getRandom().nextInt(basket.length)], "dialogue.tono.husband_gift");
            }
            case LEO -> {
                ItemStack fish = wife.getRandom().nextInt(4) == 0
                        ? new ItemStack(new Item[]{Items.NAUTILUS_SHELL, Items.NAME_TAG, Items.SADDLE, Items.HEART_OF_THE_SEA}[wife.getRandom().nextInt(wife.getRandom().nextInt(10) == 0 ? 4 : 3)])
                        : new ItemStack(wife.getRandom().nextBoolean() ? Items.COOKED_SALMON : Items.COOKED_COD, 5);
                give(wife, p, fish, "dialogue.leo.husband_gift");
            }
            case DANTE -> {
                if (day % 2 != 0) return;
                give(wife, p, wife.getRandom().nextInt(4) == 0 ? new ItemStack(Items.GOLDEN_APPLE) : new ItemStack(Items.ARROW, 16), "dialogue.dante.husband_gift");
            }
            case IVAN -> {
                int r = wife.getRandom().nextInt(10);
                ItemStack ore = r == 0 ? new ItemStack(Items.DIAMOND) : r < 4 ? new ItemStack(Items.RAW_GOLD, 3)
                        : r < 7 ? new ItemStack(Items.RAW_IRON, 5) : new ItemStack(Items.REDSTONE, 8);
                give(wife, p, ore, "dialogue.ivan.husband_gift");
            }
            case EMI -> {
                if (day % 2 != 0) return;
                Item[] discs = {Items.MUSIC_DISC_CAT, Items.MUSIC_DISC_BLOCKS, Items.MUSIC_DISC_CHIRP, Items.MUSIC_DISC_FAR,
                        Items.MUSIC_DISC_MALL, Items.MUSIC_DISC_MELLOHI, Items.MUSIC_DISC_STAL, Items.MUSIC_DISC_STRAD, Items.MUSIC_DISC_WAIT};
                give(wife, p, wife.getRandom().nextInt(3) == 0 ? new ItemStack(discs[wife.getRandom().nextInt(discs.length)])
                        : new ItemStack(Items.EXPERIENCE_BOTTLE, 4), "dialogue.emi.husband_gift");
            }
            case BETO -> give(wife, p, new ItemStack(Items.EMERALD, 3 + wife.getRandom().nextInt(4)), "dialogue.beto.husband_gift");
        }
    }

    private static void give(KikoEntity wife, PlayerEntity p, ItemStack stack, String dialogue) {
        p.getInventory().offerOrDrop(stack);
        wife.swingHand(net.minecraft.util.Hand.MAIN_HAND);
        wife.setExpression(KikoExpression.EMBARRASSED, 80);
        wife.spawnParticles(ParticleTypes.HEART, 4);
        KikoInteractionHandler.say(p, wife, dialogue, 3);
    }

    /**
     * v3.2 Hijos maestros (aprendieron el oficio de chavos). Cada 3 días te traen algo de su oficio,
     * más leve que lo de una esposa.
     */
    public static void masterGift(ServerWorld world, KikoEntity kid, PlayerEntity p, long day) {
        ItemStack gift = switch (kid.getCharacter()) {
            case MARU -> {
                // Una herramienta de hierro encantada
                ItemStack[] tools = {new ItemStack(Items.IRON_PICKAXE), new ItemStack(Items.IRON_AXE), new ItemStack(Items.IRON_SHOVEL),
                        new ItemStack(Items.IRON_SWORD)};
                yield EnchantmentHelper.enchant(kid.getRandom(), tools[kid.getRandom().nextInt(tools.length)], 10 + kid.getRandom().nextInt(10),
                        world.getRegistryManager(), Optional.empty());
            }
            case LUMI -> EnchantmentHelper.enchant(kid.getRandom(), new ItemStack(Items.BOOK), 10 + kid.getRandom().nextInt(11),
                    world.getRegistryManager(), Optional.empty());
            case NADI -> {
                ItemStack map = com.jaciel.kiko.entity.work.Expedition.scoutMap(world, p.getBlockPos());
                yield map != null ? map : new ItemStack(Items.SPYGLASS);
            }
            case RINA -> net.minecraft.component.type.PotionContentsComponent.createStack(Items.POTION,
                    kid.getRandom().nextBoolean() ? net.minecraft.potion.Potions.HEALING : net.minecraft.potion.Potions.REGENERATION);
            case TAMI -> {
                Dish[] all = Dish.values();
                yield Cooking.make(all[kid.getRandom().nextInt(all.length)], 1, true);
            }
            case TONO -> new ItemStack(Items.BREAD, 6);
            case LEO -> new ItemStack(Items.COOKED_SALMON, 4);
            case DANTE -> new ItemStack(Items.ARROW, 16);
            case IVAN -> new ItemStack(Items.RAW_IRON, 4);
            case EMI -> new ItemStack(Items.EXPERIENCE_BOTTLE, 3);
            case BETO -> new ItemStack(Items.EMERALD, 2);
            default -> new ItemStack(Items.COOKIE, 4);
        };
        if (gift == null || gift.isEmpty()) return;
        p.getInventory().offerOrDrop(gift);
        kid.swingHand(net.minecraft.util.Hand.MAIN_HAND);
        kid.playAnimation(com.jaciel.kiko.animation.KikoAnimation.WAVE);
        kid.setExpression(KikoExpression.GRIN, 80);
        kid.spawnParticles(ParticleTypes.HAPPY_VILLAGER, 6);
        KikoInteractionHandler.say(p, kid, "dialogue.kiko.master_gift", 3);
    }
}
