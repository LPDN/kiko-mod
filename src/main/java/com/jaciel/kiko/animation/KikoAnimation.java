package com.jaciel.kiko.animation;

/**
 * Animaciones especiales que el servidor le pide al cliente que reproduzca.
 * Se mandan con World#sendEntityStatus usando un byte propio (60 en adelante
 * para no chocar con los estados que usa Minecraft).
 */
public enum KikoAnimation {
    /** Saludo con la mano. */
    WAVE((byte) 60, 30),
    /** Brinquitos de felicidad al recibir comida o regalos. */
    HAPPY((byte) 61, 28),
    /** Voltea la cara y se cruza de brazos. */
    REJECT((byte) 62, 30),
    // ---- v3.2 ----
    /** Aplaude (bodas, aniversarios, cuando alguien festeja). */
    CLAP((byte) 100, 40),
    /** Baile: cadera de lado a lado, brazos arriba alternando y brinquitos. */
    DANCE((byte) 101, 100),
    /** Se estira al despertar. */
    STRETCH((byte) 102, 50),
    /** Bosteza tapándose la boca (en la noche, antes de dormir). */
    YAWN((byte) 103, 45),
    /** Abrazo: brazos al frente y se cierran. */
    HUG((byte) 104, 50),
    /** Toque rápido con la mano ("¡las traes!"). */
    TAG((byte) 105, 14),
    /** Festeja con los brazos arriba. */
    CHEER((byte) 106, 32),
    /** Se queda pensando con la mano en la barbilla. */
    THINK((byte) 107, 70),
    /** Llora tapándose la cara. */
    CRY((byte) 108, 70),
    /** Se asoma y mira para los lados, como buscando algo. */
    LOOK((byte) 109, 60),
    // ---- v3.2.1 ----
    /** Patada a la pelota con la pierna derecha. */
    KICK((byte) 110, 12),
    // ---- v3.9 ----
    /** Se agacha y sopla las velitas del pastel. */
    BLOW((byte) 111, 60),
    /** Recibe un regalo y lo abraza contra el pecho. */
    GIFT((byte) 112, 60),
    /** Brinca del susto (truenos). */
    SCARED((byte) 113, 24),
    /** Alza el vaso para brindar. */
    TOAST((byte) 114, 40),
    /** Levanta la mano para participar en clase. */
    RAISE_HAND((byte) 115, 50),
    /** Escribe en el pupitre con la cabeza agachada. */
    WRITE((byte) 116, 80),
    /** La maestra señala el pizarrón. */
    POINT((byte) 117, 60);

    private final byte status;
    private final int durationTicks;

    KikoAnimation(byte status, int durationTicks) {
        this.status = status;
        this.durationTicks = durationTicks;
    }

    public byte status() {
        return status;
    }

    public int durationTicks() {
        return durationTicks;
    }

    public static KikoAnimation byStatus(byte status) {
        for (KikoAnimation a : values()) {
            if (a.status == status) return a;
        }
        return null;
    }
}
