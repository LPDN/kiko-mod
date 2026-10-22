package com.jaciel.kiko.entity.combat;

/**
 * Cómo pelea Kiko.
 *  AGGRESSIVE  ataca a cualquier mob hostil cerca (a los creepers solo de lejos con arco)
 *  DEFENSIVE   te defiende, ataca lo que tú atacas y a lo que la ataque (el de siempre)
 *  PASSIVE     no pelea con nada, ni aunque le peguen
 */
public enum CombatMode {
    AGGRESSIVE("aggressive"),
    DEFENSIVE("defensive"),
    PASSIVE("passive");

    private final String id;

    CombatMode(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public String translationKey() {
        return "combat.kiko." + id;
    }

    public CombatMode next() {
        CombatMode[] all = values();
        return all[(ordinal() + 1) % all.length];
    }

    public static CombatMode byIndex(int i) {
        CombatMode[] all = values();
        return i >= 0 && i < all.length ? all[i] : DEFENSIVE;
    }

    public static CombatMode byId(String id) {
        for (CombatMode m : values()) if (m.id.equals(id)) return m;
        return DEFENSIVE;
    }
}
