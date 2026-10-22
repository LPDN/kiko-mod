package com.jaciel.kiko.animation;

/**
 * Poses que duran mientras pasa algo (v3.9). A diferencia de KikoAnimation (que dura unos ticks),
 * el servidor la pone y la quita y el cliente la dibuja todo ese tiempo.
 */
public enum KikoPose {
    NONE,
    /** Sentada en una silla (escalón): pupitre, feria, citas. */
    SIT,
    /** Caminando con el paraguas arriba (llueve). */
    UMBRELLA,
    /** Se tapa la cabeza con las manos y corre (llueve y no trae paraguas). */
    COVER,
    /** Tiembla de frío con los brazos cruzados. */
    SHIVER,
    /** Sentada en el pasto echada para atrás viendo las estrellas. */
    STARGAZE,
    /** Baile en pareja en la feria. */
    PAIR_DANCE,
    /** Camina de la mano contigo (cita). */
    HOLD_HAND,
    /** Sentada en el piso recargando la cabeza en tu hombro (cita). */
    LEAN;

    public static KikoPose byIndex(int i) {
        KikoPose[] all = values();
        return i >= 0 && i < all.length ? all[i] : NONE;
    }

    /** ¿Está sentada (en silla o en el piso)? */
    public boolean seated() {
        return this == SIT || this == STARGAZE || this == LEAN;
    }
}
