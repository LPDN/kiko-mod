package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.outfit.KikoOutfit;
import org.jetbrains.annotations.Nullable;

/** Trabajo que le encargaste a Kiko, en el orden en que salen en el menú. */
public enum WorkMode {
    NONE("none", null),
    CHOP("chop", KikoOutfit.LUMBERJACK),
    FOREST("forest", KikoOutfit.LUMBERJACK),
    MINE("mine", KikoOutfit.MINER),
    TUNNEL("tunnel", KikoOutfit.MINER),
    STAIRS("stairs", KikoOutfit.MINER),
    FLATTEN("flatten", KikoOutfit.MINER),
    FARM("farm", KikoOutfit.FARMER),
    BREED("breed", KikoOutfit.FARMER),
    FISH("fish", KikoOutfit.FISHER),
    SMELT("smelt", null),
    SORT("sort", null),
    EXPLORE("explore", KikoOutfit.FISHER);

    private final String id;
    @Nullable
    private final KikoOutfit outfit;

    WorkMode(String id, @Nullable KikoOutfit outfit) {
        this.id = id;
        this.outfit = outfit;
    }

    public String id() {
        return id;
    }

    /** Ropa que se pone para este trabajo (null = se queda con la que trae). */
    @Nullable
    public KikoOutfit outfit() {
        return outfit;
    }

    public String translationKey() {
        return "work.kiko." + id;
    }

    public String descriptionKey() {
        return "work.kiko." + id + ".desc";
    }

    public WorkMode next() {
        WorkMode[] all = values();
        return all[(ordinal() + 1) % all.length];
    }

    public static WorkMode byIndex(int i) {
        WorkMode[] all = values();
        return i >= 0 && i < all.length ? all[i] : NONE;
    }

    public static WorkMode byId(String id) {
        for (WorkMode m : values()) if (m.id.equals(id)) return m;
        return NONE;
    }
}
