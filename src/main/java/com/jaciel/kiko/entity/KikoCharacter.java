package com.jaciel.kiko.entity;

/**
 * Las chicas del mod. Todas usan la misma IA (KikoEntity); cambia su modelo, su altura,
 * sus diálogos personales y su especialidad.
 *
 *  KIKO  la bromista       — se cambia de ropa (9 conjuntos)
 *  MARU  la herrera        — pega más fuerte y aguanta más; cerca de un yunque te repara herramientas y armadura
 *  LUMI  la tímida lectora — te enseña cosas (experiencia seguido) y con libros y lapislázuli escribe libros encantados
 *  NADI  la exploradora    — camina más rápido; sus expediciones duran la mitad y trae más cosas
 *  RINA  la enfermera      — te cura de golpe si estás muy mal, o te da regeneración fuerte; quita veneno y cura a las demás
 *  TAMI  la chef           — cocina el doble y mejor (más saturación y un efecto); cocina sola lo crudo y te da de comer
 *
 * Todas cocinan (agáchate y dale clic con ingredientes crudos en la mano).
 *
 * v3.3 los chicos (misma IA, su propio modelo, diálogos y especialidad)
 *  TONO  el ranchero      — los cultivos y animales de su casa crecen más rápido
 *  LEO   el pescador      — pesca más rápido y saca tesoros
 *  DANTE el guardia       — el más fuerte y resistente, protege a todos los de cerca
 *  IVAN  el minero        — minando trae el doble de minerales; trae visión nocturna en las cuevas
 *  EMI   el músico        — toca la guitarra y a los de cerca les da ánimos (absorción)
 *  BETO  el comerciante   — vende lo que le sobra por esmeraldas y te consigue descuentos
 */
public enum KikoCharacter {
    KIKO("kiko", 1.8f),
    MARU("maru", 1.8f),
    LUMI("lumi", 1.64f),
    NADI("nadi", 1.8f),
    RINA("rina", 1.8f),
    TAMI("tami", 1.8f),
    // ---- v3.3 los chicos ----
    TONO("tono", 1.88f, true),
    LEO("leo", 1.82f, true),
    DANTE("dante", 1.92f, true),
    IVAN("ivan", 1.80f, true),
    EMI("emi", 1.76f, true),
    BETO("beto", 1.84f, true);

    private final String id;
    private final float height;
    private final boolean male;

    KikoCharacter(String id, float height) {
        this(id, height, false);
    }

    KikoCharacter(String id, float height, boolean male) {
        this.id = id;
        this.height = height;
        this.male = male;
    }

    /** ¿Es chico? (cambia novio/esposo, sus frases y su ropa de boda) */
    public boolean isMale() {
        return male;
    }

    /** Los chicos (para la lista de oficios, huevos, etc.). */
    public static final KikoCharacter[] GUYS = {TONO, LEO, DANTE, IVAN, EMI, BETO};
    /** Todos los que tienen oficio (Kiko no). */
    public static final KikoCharacter[] WITH_TRADE = {MARU, LUMI, NADI, RINA, TAMI, TONO, LEO, DANTE, IVAN, EMI, BETO};

    public String id() {
        return id;
    }

    /** Altura en bloques (el render escala el modelo a esta altura). */
    public float height() {
        return height;
    }

    /** Diálogos con su propia personalidad (los demás son los de Kiko). */
    private static final String[] PERSONAL = {"greet.", "talk.", "gift.", "gift_love.", "food.", "levelup.", "called.", "good_morning.", "cook_start.", "cook_done.", "confess_yes.", "confess_no.", "wedding.", "jealous.", "anniversary.", "anniversary_flowers."};

    /** Cambia "dialogue.kiko.X" por "dialogue.<chica>.X" si ese diálogo es personal. */
    public String dialogue(String key) {
        if (this == KIKO || !key.startsWith("dialogue.kiko.")) return key;
        String rest = key.substring("dialogue.kiko.".length());
        for (String p : PERSONAL) {
            if (rest.startsWith(p)) return "dialogue." + id + "." + rest;
        }
        // Los chicos dicen las frases generales en masculino
        if (male && net.minecraft.util.Language.getInstance().hasTranslation("dialogue.guy." + rest)) return "dialogue.guy." + rest;
        return key;
    }

    public static KikoCharacter byId(String id) {
        for (KikoCharacter c : values()) if (c.id.equals(id)) return c;
        return KIKO;
    }
}
