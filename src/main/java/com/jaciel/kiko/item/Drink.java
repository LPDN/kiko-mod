package com.jaciel.kiko.item;

import com.jaciel.kiko.entity.KikoCharacter;
import com.jaciel.kiko.registry.ModItems;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;

/**
 * Las bebidas (v3.6). Refrescos en botella, aguas frescas en vaso, calientitas en jarrito, licuados y la energética.
 * Todas regresan su envase al tomarlas (menos la lata). Las de Tami duran el doble.
 */
public enum Drink {
    KIKO_COLA("kiko_cola", Kind.SODA, 2, 0.3f, 0, true, false, new Fx(StatusEffects.SPEED, 600, 1)),
    NARANJIKO("naranjiko", Kind.SODA, 8, 0.6f, 0, true, false, new Fx(StatusEffects.SPEED, 1200, 0)),
    FRESQUIKO("fresquiko", Kind.SODA, 2, 0.3f, 0, true, false, new Fx(StatusEffects.FIRE_RESISTANCE, 800, 0)),
    HORCHATA("horchata", Kind.CUP, 2, 0.4f, 4, false, false),
    JAMAICA("jamaica", Kind.CUP, 1, 0.2f, 0, false, false, new Fx(StatusEffects.REGENERATION, 300, 0)),
    AGUA_SANDIA("agua_sandia", Kind.CUP, 1, 0.3f, 6, false, false),
    CAFE_DE_OLLA("cafe_de_olla", Kind.MUG, 1, 0.2f, 0, false, false, new Fx(StatusEffects.HASTE, 1800, 0)),
    CHOCOLATE("chocolate_caliente", Kind.MUG, 3, 0.4f, 0, false, true, new Fx(StatusEffects.REGENERATION, 100, 0)),
    CHAMPURRADO("champurrado", Kind.MUG, 8, 0.8f, 0, false, false),
    TE_MANZANILLA("te_manzanilla", Kind.MUG, 1, 0.2f, 4, false, false, new Fx(StatusEffects.SLOWNESS, 200, 0)),
    LICUADO_FRESA("licuado_fresa", Kind.CUP, 3, 0.5f, 0, false, false, new Fx(StatusEffects.REGENERATION, 200, 1)),
    LICUADO_MANZANA("licuado_manzana", Kind.CUP, 5, 0.6f, 0, false, false, new Fx(StatusEffects.ABSORPTION, 2400, 0)),
    CHUPACABRAS("chupacabras_blanco", Kind.CAN, 0, 0f, 0, false, false,
            new Fx(StatusEffects.STRENGTH, 1200, 0), new Fx(StatusEffects.SPEED, 1200, 0), new Fx(StatusEffects.REGENERATION, 1200, 0));

    public enum Kind { SODA, CUP, MUG, CAN }

    public record Fx(RegistryEntry<StatusEffect> effect, int ticks, int amp) {}

    public final String id;
    public final Kind kind;
    public final int nutrition;
    public final float saturation;
    /** Vida que cura al instante (en medios corazones). */
    public final float heal;
    /** Es refresco (Rina regaña si te tomas muchos seguidos). */
    public final boolean soda;
    /** Te quita el frío un rato. */
    public final boolean warm;
    public final Fx[] effects;
    public Item item;

    Drink(String id, Kind kind, int nutrition, float saturation, float heal, boolean soda, boolean warm, Fx... effects) {
        this.id = id;
        this.kind = kind;
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.heal = heal;
        this.soda = soda;
        this.warm = warm;
        this.effects = effects;
    }

    /** El envase que regresa al tomarla (o null si no regresa nada). */
    @Nullable
    public Item container() {
        return switch (kind) {
            case SODA -> Items.GLASS_BOTTLE;
            case CUP -> ModItems.CUP;
            case MUG -> ModItems.MUG;
            case CAN -> null;
        };
    }

    /** Su bebida favorita según quién sea (para regalo). */
    @Nullable
    public static Drink favoriteOf(KikoCharacter c) {
        return switch (c) {
            case KIKO -> KIKO_COLA;
            case LUMI -> TE_MANZANILLA;
            case IVAN -> CAFE_DE_OLLA;
            case TONO -> HORCHATA;
            case MARU -> CAFE_DE_OLLA;
            case NADI -> JAMAICA;
            case RINA -> AGUA_SANDIA;
            case TAMI -> LICUADO_FRESA;
            case LEO -> NARANJIKO;
            case DANTE -> CHUPACABRAS;
            case EMI -> LICUADO_MANZANA;
            case BETO -> CHAMPURRADO;
        };
    }

    @Nullable
    public static Drink of(Item i) {
        for (Drink d : values()) if (d.item == i) return d;
        return null;
    }
}
