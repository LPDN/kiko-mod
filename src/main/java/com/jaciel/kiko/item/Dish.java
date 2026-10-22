package com.jaciel.kiko.item;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;

/**
 * Los platillos que cocinan las chicas.
 * Cualquiera te cocina (2 ingredientes = 1 platillo). Tami es chef: con 1 ingrediente hace 1 platillo
 * y el suyo llena más, dura más y trae un efecto extra.
 *
 *  nutrición / saturación normal  →  versión de Tami (+2 de hambre, saturación x1.4 y su efecto)
 */
public enum Dish {
    GUISADO("guisado", 10, 1.0f, true, null, 0, StatusEffects.RESISTANCE, 1200),
    CALDO_DE_POLLO("caldo_de_pollo", 8, 1.0f, true, StatusEffects.REGENERATION, 160, StatusEffects.REGENERATION, 400),
    RAMEN("ramen", 9, 1.0f, true, null, 0, StatusEffects.HASTE, 1800),
    PURE_DE_PAPA("pure_de_papa", 7, 0.9f, true, null, 0, StatusEffects.ABSORPTION, 1200),
    CREMA_DE_ZANAHORIA("crema_de_zanahoria", 7, 0.9f, true, null, 0, StatusEffects.NIGHT_VISION, 2400),
    HUEVOS_RANCHEROS("huevos_rancheros", 8, 0.9f, false, null, 0, StatusEffects.STRENGTH, 1200),
    CONCHA("concha", 6, 0.8f, false, null, 0, StatusEffects.SPEED, 1800),
    PAY_DE_FRUTAS("pay_de_frutas", 7, 0.9f, false, null, 0, StatusEffects.LUCK, 2400);

    public final String id;
    public final int nutrition;
    public final float saturation;
    public final boolean bowl;
    private final RegistryEntry<StatusEffect> effect;
    private final int effectTicks;
    private final RegistryEntry<StatusEffect> chefEffect;
    private final int chefTicks;
    /** Se llena al registrar los ítems (ModItems). */
    public Item item;

    Dish(String id, int nutrition, float saturation, boolean bowl,
         RegistryEntry<StatusEffect> effect, int effectTicks, RegistryEntry<StatusEffect> chefEffect, int chefTicks) {
        this.id = id;
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.bowl = bowl;
        this.effect = effect;
        this.effectTicks = effectTicks;
        this.chefEffect = chefEffect;
        this.chefTicks = chefTicks;
    }

    /** La comida normal (la que hace cualquier chica). */
    public FoodComponent food() {
        FoodComponent.Builder b = new FoodComponent.Builder().nutrition(nutrition).saturationModifier(saturation);
        if (effect != null) b.statusEffect(new StatusEffectInstance(effect, effectTicks, 0), 1f);
        return b.build();
    }

    /** La comida de Tami: llena más y da su efecto. */
    public FoodComponent chefFood() {
        FoodComponent.Builder b = new FoodComponent.Builder().nutrition(nutrition + 2).saturationModifier(saturation * 1.4f)
                .alwaysEdible();
        if (effect != null && effect != chefEffect) b.statusEffect(new StatusEffectInstance(effect, effectTicks, 0), 1f);
        b.statusEffect(new StatusEffectInstance(chefEffect, chefTicks, 0), 1f);
        return b.build();
    }
}
