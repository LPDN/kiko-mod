package com.jaciel.kiko.outfit;

import com.jaciel.kiko.KikoMod;
import net.minecraft.util.Identifier;

/**
 * Los conjuntos de ropa de Kiko.
 * Cada uno tiene su propia textura de 256x256 y dice qué piezas extra del modelo se ven.
 *
 * Para agregar uno nuevo
 *  1. Agrega una línea aquí con sus piezas
 *  2. Pon su textura en assets/kiko/textures/entity/kiko_<id>.png (puedes partir de una existente)
 *  3. Agrega "outfit.kiko.<id>" a los archivos de idioma
 */
public enum KikoOutfit {
    //            id            falda  capucha sombrero casco  gorro  bufanda vestido casual
    SCHOOL("school",           true,  false,  false,   false, false, false,  false,  true),
    CASUAL1("casual1",         false, true,   false,   false, false, false,  false,  true),
    CASUAL2("casual2",         true,  false,  false,   false, false, false,  false,  true),
    BEACH("beach",             true,  false,  true,    false, false, false,  true,   false),
    WINTER("winter",           false, false,  false,   false, true,  true,   false,  false),
    LUMBERJACK("lumberjack",   false, false,  false,   false, true,  false,  false,  false),
    MINER("miner",             false, false,  false,   true,  false, false,  false,  false),
    FARMER("farmer",           false, false,  true,    false, false, false,  false,  false),
    FISHER("fisher",           false, false,  true,    false, false, false,  false,  false),
    /** v3.0: vestido de novia con velo (solo cuando es tu esposa). */
    WEDDING("wedding",         true,  false,  false,   false, false, false,  true,   false);

    private final String id;
    private final boolean skirt, hood, hat, hardhat, beanie, scarf, dress, casual;
    private final Identifier texture;

    KikoOutfit(String id, boolean skirt, boolean hood, boolean hat, boolean hardhat,
               boolean beanie, boolean scarf, boolean dress, boolean casual) {
        this.id = id;
        this.skirt = skirt;
        this.hood = hood;
        this.hat = hat;
        this.hardhat = hardhat;
        this.beanie = beanie;
        this.scarf = scarf;
        this.dress = dress;
        this.casual = casual;
        this.texture = KikoMod.id("textures/entity/kiko_" + id + ".png");
    }

    public String id() {
        return id;
    }

    public boolean hasSkirt() {
        return skirt;
    }

    public boolean hasHood() {
        return hood;
    }

    public boolean hasHat() {
        return hat;
    }

    public boolean hasHardhat() {
        return hardhat;
    }

    public boolean hasBeanie() {
        return beanie;
    }

    public boolean hasScarf() {
        return scarf;
    }

    public boolean hasDress() {
        return dress;
    }

    /** Ropa de diario (con la que puede aparecer en las aldeas). */
    public boolean isCasual() {
        return casual;
    }

    public Identifier texture() {
        return texture;
    }

    public String translationKey() {
        return "outfit.kiko." + id;
    }

    public static KikoOutfit byIndex(int index) {
        KikoOutfit[] all = values();
        return index >= 0 && index < all.length ? all[index] : SCHOOL;
    }

    public static KikoOutfit byId(String id) {
        for (KikoOutfit o : values()) {
            if (o.id.equalsIgnoreCase(id)) return o;
        }
        return null;
    }
}
