package com.jaciel.kiko.entity.combat;

import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.inventory.KikoInventory;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;

/**
 * Kiko se cuida sola con lo que trae en la mochila.
 *  - Con menos de la mitad de vida toma poción de curación o regeneración, si no come lo que más llena
 *  - Las manzanas doradas le dan regeneración y absorción como al jugador
 *  - Al empezar una pelea se toma las pociones buenas que traiga (fuerza, velocidad, resistencia...)
 */
public final class SelfCare {
    private SelfCare() {}

    /** Se llama cada segundo. Regresa true si comió o tomó algo. */
    public static boolean heal(KikoEntity kiko) {
        if (kiko.getHealth() >= kiko.getMaxHealth() * 0.5f) return false;
        KikoInventory inv = kiko.getInventory();

        int potion = inv.findInBag(s -> isDrinkable(s) && hasEffect(s, true));
        if (potion >= 0) {
            drink(kiko, inv.bag().get(potion));
            return true;
        }

        // La comida que más llena
        int best = -1;
        int bestNutrition = 0;
        for (int i = 0; i < KikoInventory.BAG_SIZE; i++) {
            FoodComponent food = inv.bag().get(i).get(DataComponentTypes.FOOD);
            if (food != null && food.nutrition() > bestNutrition && !isBadFood(inv.bag().get(i))) {
                best = i;
                bestNutrition = food.nutrition();
            }
        }
        if (best < 0) return false;
        ItemStack stack = inv.bag().get(best);
        kiko.heal(Math.max(2f, bestNutrition));
        if (stack.isOf(Items.GOLDEN_APPLE)) {
            kiko.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 1));
            kiko.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 0));
        } else if (stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
            kiko.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1));
            kiko.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3));
            kiko.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 0));
            kiko.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 6000, 0));
        }
        kiko.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1f, 1f);
        kiko.spawnParticles(ParticleTypes.HAPPY_VILLAGER, 3);
        stack.decrement(1);
        return true;
    }

    /** Al empezar una pelea. Se toma UNA poción buena que no tenga ya activa. */
    public static void buffForFight(KikoEntity kiko) {
        KikoInventory inv = kiko.getInventory();
        int i = inv.findInBag(s -> isDrinkable(s) && hasEffect(s, false) && !alreadyActive(kiko, s));
        if (i >= 0) drink(kiko, inv.bag().get(i));
    }

    private static boolean isDrinkable(ItemStack s) {
        return s.isOf(Items.POTION) && s.contains(DataComponentTypes.POTION_CONTENTS);
    }

    /** Comida que no conviene (carne podrida, ojo de araña, pez globo...). */
    private static boolean isBadFood(ItemStack s) {
        return s.isOf(Items.ROTTEN_FLESH) || s.isOf(Items.SPIDER_EYE) || s.isOf(Items.PUFFERFISH)
                || s.isOf(Items.POISONOUS_POTATO) || s.isOf(Items.CHORUS_FRUIT) || s.isOf(Items.SUSPICIOUS_STEW);
    }

    /**
     * @param healing true = pociones que curan (curación, regeneración)
     *                false = pociones de pelea (cualquier efecto bueno que no sea de curar)
     */
    private static boolean hasEffect(ItemStack s, boolean healing) {
        PotionContentsComponent pc = s.get(DataComponentTypes.POTION_CONTENTS);
        if (pc == null || !pc.hasEffects()) return false;
        for (StatusEffectInstance e : pc.getEffects()) {
            boolean heals = e.getEffectType().equals(StatusEffects.INSTANT_HEALTH) || e.getEffectType().equals(StatusEffects.REGENERATION);
            if (!e.getEffectType().value().isBeneficial()) return false;
            if (healing == heals) return true;
        }
        return false;
    }

    private static boolean alreadyActive(KikoEntity kiko, ItemStack s) {
        PotionContentsComponent pc = s.get(DataComponentTypes.POTION_CONTENTS);
        if (pc == null) return true;
        for (StatusEffectInstance e : pc.getEffects()) {
            if (!kiko.hasStatusEffect(e.getEffectType())) return false;
        }
        return true;
    }

    private static void drink(KikoEntity kiko, ItemStack stack) {
        PotionContentsComponent pc = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (pc != null) pc.forEachEffect(e -> kiko.addStatusEffect(new StatusEffectInstance(e)));
        kiko.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1f, 1f);
        stack.decrement(1);
        ItemStack rest = kiko.getInventory().insert(new ItemStack(Items.GLASS_BOTTLE));
        if (!rest.isEmpty()) kiko.dropStack(rest);
    }
}
