package com.jaciel.kiko.animation;

/**
 * Expresiones de la cara de Kiko (Fase 4). El servidor elige una por un rato (al hablar, recibir
 * regalos, pelear...) y el cliente arma la cara: boca, cejas, ojos y extras (sonrojo, sudor, lágrimas,
 * marca de enojo). Cuando no hay ninguna, el cliente escoge una según su ánimo.
 */
public enum KikoExpression {
    //          boca       cejaY  cejaRot ojos   felices guiño  sonrojo sudor  lágrimas enojo
    NEUTRAL    ("neutral",  0f,    0f,    1f,    false, false, false, false, false, false),
    SMILE      ("smile",   -0.3f,  0f,    1f,    false, false, false, false, false, false),
    GRIN       ("grin",    -0.3f,  0f,    0.85f, false, false, false, false, false, false),
    LAUGH      ("laugh",   -0.5f,  0f,    1f,    true,  false, false, false, false, false),
    SURPRISED  ("o",       -0.8f,  0f,    1.15f, false, false, false, false, false, false),
    ANGRY      ("frown",    0.4f,  0.35f, 0.6f,  false, false, false, false, false, true),
    SAD        ("frown",    0f,   -0.3f,  0.8f,  false, false, false, false, true,  false),
    POUT       ("pout",     0.2f,  0.2f,  0.7f,  false, false, true,  false, false, false),
    SMUG       ("smirk",   -0.2f,  0f,    1f,    false, true,  false, false, false, false),
    EMBARRASSED("smile",   -0.1f, -0.3f,  1f,    true,  false, true,  false, false, false),
    NERVOUS    ("smile",   -0.2f, -0.3f,  1f,    false, false, false, true,  false, false),
    SLEEPY     ("sleepy",   0.2f,  0f,    0f,    false, false, false, false, false, false),
    YAWN       ("o",        0.1f, -0.15f, 0f,    false, false, false, false, false, false),
    // v3.9 enamorada: ojitos felices, sonrojo y sonrisa
    LOVESTRUCK ("smile",   -0.25f, 0f,    1f,    true,  false, true,  false, false, false);

    public final String mouth;
    public final float browY;
    public final float browRoll;
    /** 0 = cerrados, 1 = normal, más de 1 = muy abiertos. */
    public final float eyeOpen;
    public final boolean happyEyes;
    public final boolean wink;
    public final boolean blush;
    public final boolean sweat;
    public final boolean tears;
    public final boolean anger;

    KikoExpression(String mouth, float browY, float browRoll, float eyeOpen, boolean happyEyes, boolean wink,
                   boolean blush, boolean sweat, boolean tears, boolean anger) {
        this.mouth = mouth;
        this.browY = browY;
        this.browRoll = browRoll;
        this.eyeOpen = eyeOpen;
        this.happyEyes = happyEyes;
        this.wink = wink;
        this.blush = blush;
        this.sweat = sweat;
        this.tears = tears;
        this.anger = anger;
    }

    public static KikoExpression byIndex(int i) {
        KikoExpression[] all = values();
        return i >= 0 && i < all.length ? all[i] : null;
    }
}
