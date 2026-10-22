package com.jaciel.kiko.client.animation;

import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.client.model.KikoModel;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.entity.combat.WeaponHelper;
import net.minecraft.util.math.MathHelper;

/**
 * Animaciones procedurales de Kiko (Fase 2), todo con rotaciones y escalas de ModelPart.
 *
 * Capas, se suman en este orden
 *  1. Base       cabeza mira al jugador, respiración, cambio de peso, pelo
 *  2. Caminar    caderas, rodillas y codos, rebote, pelo y falda con inercia
 *  3. Ánimo      bestie ladea la cabeza, fría mira hacia abajo
 *  4. Golpe      puñetazo cuando ataca
 *  5. Especial   saludo, felicidad o rechazo (entrada y salida suaves)
 *  6. Ojos       parpadeo, pupilas que siguen la mirada, ojos felices y entrecerrados
 *
 * Todo depende de age + tickDelta, así que se interpola entre ticks sin jitter.
 */
public final class KikoAnimationManager {
    private static final float DEG = MathHelper.RADIANS_PER_DEGREE;
    private static final float PI = MathHelper.PI;

    private KikoAnimationManager() {}

    public static void animate(KikoModel m, KikoEntity e, float limbAngle, float limbDistance,
                               float age, float headYaw, float headPitch) {
        limbDistance = Math.min(limbDistance, 1f);
        float swing = limbAngle * 0.6662f;
        float cos = MathHelper.cos(swing);
        float sin = MathHelper.sin(swing);

        // ---------- 1. Base ----------
        m.head.yaw = headYaw * DEG;
        m.head.pitch = headPitch * DEG;

        float breath = MathHelper.sin(age * 0.07f);
        float idle = 1f - limbDistance;
        m.head.pitch += breath * 0.015f;
        m.body.pitch += breath * 0.012f * idle;
        // Cambio de peso de una pierna a otra cuando está parada
        float sway = MathHelper.sin(age * 0.025f) * idle;
        m.body.roll += sway * 0.025f;
        m.rightLeg.roll += sway * 0.02f;
        m.leftLeg.roll += sway * 0.02f;

        m.rightArm.roll += 0.09f + breath * 0.025f;
        m.leftArm.roll -= 0.09f + breath * 0.025f;
        m.rightForearm.pitch -= 0.12f;
        m.leftForearm.pitch -= 0.12f;

        m.hairBack.pitch += 0.05f + MathHelper.sin(age * 0.06f) * 0.015f;
        m.ponytail.roll += MathHelper.sin(age * 0.08f) * 0.04f;

        // ---------- 2. Caminar ----------
        m.rightLeg.pitch += cos * 0.85f * limbDistance;
        m.leftLeg.pitch -= cos * 0.85f * limbDistance;
        // Rodillas, se doblan cuando la pierna regresa hacia adelante
        m.rightShin.pitch += (Math.max(0f, -sin) * 1.1f + 0.08f) * limbDistance;
        m.leftShin.pitch += (Math.max(0f, sin) * 1.1f + 0.08f) * limbDistance;

        m.rightArm.pitch += -cos * 0.7f * limbDistance;
        m.leftArm.pitch += cos * 0.7f * limbDistance;
        m.rightForearm.pitch -= (0.2f + Math.max(0f, -cos) * 0.4f) * limbDistance;
        m.leftForearm.pitch -= (0.2f + Math.max(0f, cos) * 0.4f) * limbDistance;

        m.body.yaw += sin * 0.08f * limbDistance;      // los hombros giran un poquito
        m.body.roll += cos * 0.02f * limbDistance;
        m.root.pivotY -= Math.abs(cos) * limbDistance * 0.9f;

        m.hairBack.pitch += limbDistance * 0.25f + Math.abs(sin) * 0.06f * limbDistance;
        m.ponytail.pitch += limbDistance * 0.3f + MathHelper.sin(swing * 2f) * 0.07f * limbDistance;
        m.ponytail.roll -= sin * 0.12f * limbDistance;
        m.skirt.pitch += Math.abs(cos) * 0.05f * limbDistance;
        m.skirt.xScale = 1f + Math.abs(cos) * 0.04f * limbDistance;

        // ---------- 2a. Sentada (barca, camello, carrito) ----------
        if (e.hasVehicle()) {
            m.rightLeg.pitch = -1.4f;
            m.leftLeg.pitch = -1.4f;
            m.rightLeg.yaw = 0.25f;
            m.leftLeg.yaw = -0.25f;
            m.rightShin.pitch = 0.35f;
            m.leftShin.pitch = 0.35f;
            m.rightArm.pitch = -0.5f;
            m.leftArm.pitch = -0.5f;
            m.root.pivotY = 24f;
        }

        // ---------- 2b. Dormida: acostada derechita, brazos a los lados ----------
        if (e.isLyingDown()) {
            float slow = MathHelper.sin(age * 0.04f);
            m.head.yaw = 0f;
            m.head.pitch = slow * 0.01f;
            m.head.roll = 0.12f;
            m.body.pitch = slow * 0.012f;
            m.body.yaw = 0f;
            m.body.roll = 0f;
            m.root.pivotY = 24f;
            m.rightArm.pitch = 0f;
            m.leftArm.pitch = 0f;
            m.rightArm.roll = 0.1f;
            m.leftArm.roll = -0.1f;
            m.rightArm.yaw = 0f;
            m.leftArm.yaw = 0f;
            m.rightForearm.pitch = 0f;
            m.leftForearm.pitch = 0f;
            m.rightForearm.yaw = 0f;
            m.leftForearm.yaw = 0f;
            m.rightLeg.pitch = 0f;
            m.leftLeg.pitch = 0f;
            m.rightLeg.roll = 0.02f;
            m.leftLeg.roll = -0.02f;
            m.rightShin.pitch = 0f;
            m.leftShin.pitch = 0f;
            m.hairBack.pitch = 0f;
            m.ponytail.pitch = 0f;
        }

        // ---------- 2c. Poses que duran (v3.9) ----------
        com.jaciel.kiko.animation.KikoPose pose = e.getKikoPose();
        if (pose != com.jaciel.kiko.animation.KikoPose.NONE && !e.isLyingDown() && !e.hasVehicle()) {
            applyPose(m, pose, age, idle);
        }

        // ---------- 3. Ánimo ----------
        int mood = e.getMood();
        if (mood == 3) {
            m.head.roll += MathHelper.sin(age * 0.05f) * 0.08f;
        } else if (mood == 0) {
            m.head.pitch += 0.16f;
            m.body.pitch += 0.04f;
        }

        // ---------- 3b. Pescando: brazo al frente con la caña levantada ----------
        if (e.isFishing()) {
            float wait = MathHelper.sin(age * 0.08f) * 0.03f;
            m.rightArm.pitch = -0.8f + wait;
            m.rightArm.yaw = -0.12f;
            m.rightArm.roll = 0.05f;
            m.rightForearm.pitch = -0.45f;
            // La otra mano agarra la caña más abajo
            m.leftArm.pitch = -0.55f;
            m.leftArm.yaw = 0.35f;
            m.leftArm.roll = 0f;
            m.leftForearm.pitch = -0.6f;
        }

        // ---------- 4. Golpe ----------
        float swingProgress = m.handSwingProgress;
        if (swingProgress > 0f) {
            float p = MathHelper.sin(MathHelper.sqrt(swingProgress) * PI);
            m.rightArm.pitch = -1.4f * p - 0.2f;
            m.rightArm.yaw = 0.3f * p;
            m.rightForearm.pitch = -0.3f * (1f - p);
            m.body.yaw = -0.25f * p;
        }

        // ---------- 4b. Apuntar con arco o ballesta ----------
        if (e.isAttacking() && WeaponHelper.isRanged(e.getMainHandStack())) {
            float aim = headPitch * DEG;
            m.rightArm.pitch = -1.5f + aim;
            m.leftArm.pitch = -1.5f + aim;
            m.rightArm.yaw = -0.1f + m.head.yaw;
            m.leftArm.yaw = 0.45f + m.head.yaw;
            m.rightArm.roll = 0f;
            m.leftArm.roll = 0f;
            m.rightForearm.pitch = 0f;
            m.leftForearm.pitch = 0f;
        }

        // ---------- 4c. Cubrirse con el escudo ----------
        if (e.isUsingItem() && e.getActiveHand() == net.minecraft.util.Hand.OFF_HAND) {
            m.leftArm.pitch = -0.95f;
            m.leftArm.yaw = 0.45f;
            m.leftArm.roll = 0f;
            m.leftForearm.pitch = -0.55f;
        }

        // ---------- 5. Especiales ----------
        KikoAnimation anim = e.getCurrentAnimation();
        float t = e.getAnimationProgress(age);
        float w = anim == null || t < 0f ? 0f : envelope(t);
        // Con música cerca (tocadiscos) baila sin parar mientras no camine
        if ((anim == null || t < 0f) && e.isDancingToMusic() && idle > 0.5f) {
            anim = KikoAnimation.DANCE;
            t = (age % 100f) / 100f;
            w = MathHelper.clamp((idle - 0.5f) * 2f, 0f, 1f);
        }

        if (anim != null && t >= 0f) {
            switch (anim) {
                case WAVE -> {
                    float wave = MathHelper.sin(t * PI * 8f) * 0.35f;
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 2.3f);
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -0.1f);
                    m.rightForearm.roll = MathHelper.lerp(w, 0f, 0.35f + wave);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, 0f);
                    m.head.roll += 0.14f * w;
                    m.body.roll -= 0.04f * w;
                }
                case HAPPY -> {
                    float hop = Math.abs(MathHelper.sin(t * PI * 3f));
                    m.root.pivotY -= hop * 2.6f * w;
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0.5f);
                    m.leftArm.roll = MathHelper.lerp(w, m.leftArm.roll, -0.5f);
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -0.6f);
                    m.leftArm.pitch = MathHelper.lerp(w, m.leftArm.pitch, -0.6f);
                    // manos juntas frente al pecho, aplaudiendo
                    float clap = MathHelper.sin(t * PI * 6f) * 0.15f;
                    m.rightArm.yaw = MathHelper.lerp(w, m.rightArm.yaw, -0.45f - clap);
                    m.leftArm.yaw = MathHelper.lerp(w, m.leftArm.yaw, 0.45f + clap);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -1.1f);
                    m.leftForearm.pitch = MathHelper.lerp(w, m.leftForearm.pitch, -1.1f);
                    m.head.pitch -= 0.2f * w;
                    m.rightShin.pitch += hop * 0.5f * w;
                    m.leftShin.pitch += hop * 0.5f * w;
                    m.ponytail.roll -= hop * 0.3f * w;
                }
                case REJECT -> {
                    // Voltea la cara y se cruza de brazos
                    m.head.yaw += MathHelper.sin(t * PI * 4f) * 0.4f * w + 0.4f * w;
                    m.head.pitch = MathHelper.lerp(w, m.head.pitch, -0.12f);
                    m.body.yaw += 0.3f * w;
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -0.55f);
                    m.leftArm.pitch = MathHelper.lerp(w, m.leftArm.pitch, -0.55f);
                    m.rightArm.yaw = MathHelper.lerp(w, m.rightArm.yaw, -0.3f);
                    m.leftArm.yaw = MathHelper.lerp(w, m.leftArm.yaw, 0.3f);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0f);
                    m.leftArm.roll = MathHelper.lerp(w, m.leftArm.roll, 0f);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -1.3f);
                    m.leftForearm.pitch = MathHelper.lerp(w, m.leftForearm.pitch, -1.3f);
                    m.rightForearm.yaw = MathHelper.lerp(w, 0f, -0.9f);
                    m.leftForearm.yaw = MathHelper.lerp(w, 0f, 0.9f);
                }
                case CLAP -> {
                    float clap = Math.abs(MathHelper.sin(t * PI * 7f));
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -0.9f);
                    m.leftArm.pitch = MathHelper.lerp(w, m.leftArm.pitch, -0.9f);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0f);
                    m.leftArm.roll = MathHelper.lerp(w, m.leftArm.roll, 0f);
                    m.rightArm.yaw = MathHelper.lerp(w, m.rightArm.yaw, -0.25f - clap * 0.35f);
                    m.leftArm.yaw = MathHelper.lerp(w, m.leftArm.yaw, 0.25f + clap * 0.35f);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -0.7f);
                    m.leftForearm.pitch = MathHelper.lerp(w, m.leftForearm.pitch, -0.7f);
                    m.rightForearm.yaw = MathHelper.lerp(w, 0f, -0.5f * clap);
                    m.leftForearm.yaw = MathHelper.lerp(w, 0f, 0.5f * clap);
                    m.head.pitch -= 0.1f * w;
                }
                case DANCE -> {
                    float beat = t * PI * 10f;
                    float side = MathHelper.sin(beat);
                    float bounce = Math.abs(MathHelper.cos(beat));
                    m.root.pivotY -= bounce * 1.2f * w;
                    m.body.roll += side * 0.12f * w;
                    m.body.yaw += MathHelper.sin(beat * 0.5f) * 0.25f * w;
                    m.head.roll += -side * 0.1f * w;
                    // Un brazo arriba y el otro abajo, alternando
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 1.2f + side * 0.9f);
                    m.leftArm.roll = MathHelper.lerp(w, m.leftArm.roll, -1.2f + side * 0.9f);
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -0.3f);
                    m.leftArm.pitch = MathHelper.lerp(w, m.leftArm.pitch, -0.3f);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -0.6f);
                    m.leftForearm.pitch = MathHelper.lerp(w, m.leftForearm.pitch, -0.6f);
                    // Pasito de un pie al otro
                    m.rightLeg.pitch = MathHelper.lerp(w, m.rightLeg.pitch, Math.max(0f, side) * -0.5f);
                    m.leftLeg.pitch = MathHelper.lerp(w, m.leftLeg.pitch, Math.max(0f, -side) * -0.5f);
                    m.rightShin.pitch += Math.max(0f, side) * 0.7f * w;
                    m.leftShin.pitch += Math.max(0f, -side) * 0.7f * w;
                    m.skirt.yaw += side * 0.15f * w;
                    m.ponytail.roll -= side * 0.35f * w;
                    m.hairBack.roll -= side * 0.08f * w;
                }
                case STRETCH -> {
                    // Brazos arriba, se echa para atrás y se le estiran las piernas
                    float s = MathHelper.sin(MathHelper.clamp(t * 1.4f, 0f, 1f) * PI);
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -2.9f);
                    m.leftArm.pitch = MathHelper.lerp(w, m.leftArm.pitch, -2.9f);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0.25f);
                    m.leftArm.roll = MathHelper.lerp(w, m.leftArm.roll, -0.25f);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -0.15f);
                    m.leftForearm.pitch = MathHelper.lerp(w, m.leftForearm.pitch, -0.15f);
                    m.body.pitch -= 0.15f * s * w;
                    m.head.pitch -= 0.35f * s * w;
                    m.root.pivotY -= 0.6f * s * w;
                }
                case YAWN -> {
                    // Se tapa la boca con la mano derecha y se le va la cabeza para atrás
                    float s = MathHelper.sin(t * PI);
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -1.25f);
                    m.rightArm.yaw = MathHelper.lerp(w, m.rightArm.yaw, -0.5f);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0f);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -1.6f);
                    m.leftArm.roll = MathHelper.lerp(w, m.leftArm.roll, -0.5f * s);
                    m.head.pitch -= 0.3f * s * w;
                    m.body.pitch -= 0.06f * s * w;
                }
                case HUG -> {
                    float close = MathHelper.clamp((t - 0.15f) / 0.3f, 0f, 1f);
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -1.35f);
                    m.leftArm.pitch = MathHelper.lerp(w, m.leftArm.pitch, -1.35f);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0f);
                    m.leftArm.roll = MathHelper.lerp(w, m.leftArm.roll, 0f);
                    m.rightArm.yaw = MathHelper.lerp(w, m.rightArm.yaw, 0.3f - close * 0.75f);
                    m.leftArm.yaw = MathHelper.lerp(w, m.leftArm.yaw, -0.3f + close * 0.75f);
                    m.rightForearm.yaw = MathHelper.lerp(w, 0f, -0.7f * close);
                    m.leftForearm.yaw = MathHelper.lerp(w, 0f, 0.7f * close);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -0.2f);
                    m.leftForearm.pitch = MathHelper.lerp(w, m.leftForearm.pitch, -0.2f);
                    m.head.roll += 0.2f * close * w;
                    m.body.pitch += 0.08f * close * w;
                }
                case TAG -> {
                    float p = MathHelper.sin(t * PI);
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -1.5f * p);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0f);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, 0f);
                    m.body.yaw -= 0.3f * p * w;
                    m.body.pitch += 0.12f * p * w;
                }
                case CHEER -> {
                    float hop = Math.abs(MathHelper.sin(t * PI * 2f));
                    float shake = MathHelper.sin(t * PI * 10f) * 0.12f;
                    m.root.pivotY -= hop * 2f * w;
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -2.8f);
                    m.leftArm.pitch = MathHelper.lerp(w, m.leftArm.pitch, -2.8f);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, -0.35f + shake);
                    m.leftArm.roll = MathHelper.lerp(w, m.leftArm.roll, 0.35f - shake);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -0.2f);
                    m.leftForearm.pitch = MathHelper.lerp(w, m.leftForearm.pitch, -0.2f);
                    m.head.pitch -= 0.25f * w;
                    m.rightShin.pitch += hop * 0.4f * w;
                    m.leftShin.pitch += hop * 0.4f * w;
                }
                case THINK -> {
                    // Mano derecha en la barbilla, la otra cruzada; ladea la cabeza y mira arriba
                    float tap = MathHelper.sin(t * PI * 6f) * 0.04f;
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -0.75f);
                    m.rightArm.yaw = MathHelper.lerp(w, m.rightArm.yaw, -0.35f);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0f);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -1.75f + tap);
                    m.leftArm.pitch = MathHelper.lerp(w, m.leftArm.pitch, -0.45f);
                    m.leftArm.yaw = MathHelper.lerp(w, m.leftArm.yaw, 0.35f);
                    m.leftArm.roll = MathHelper.lerp(w, m.leftArm.roll, 0f);
                    m.leftForearm.pitch = MathHelper.lerp(w, m.leftForearm.pitch, -1.2f);
                    m.leftForearm.yaw = MathHelper.lerp(w, 0f, 0.8f);
                    m.head.roll += 0.18f * w;
                    m.head.pitch -= 0.25f * w;
                    m.head.yaw += 0.2f * w;
                }
                case CRY -> {
                    float sob = MathHelper.sin(t * PI * 12f) * 0.05f;
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -1.35f);
                    m.leftArm.pitch = MathHelper.lerp(w, m.leftArm.pitch, -1.35f);
                    m.rightArm.yaw = MathHelper.lerp(w, m.rightArm.yaw, -0.35f);
                    m.leftArm.yaw = MathHelper.lerp(w, m.leftArm.yaw, 0.35f);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0f);
                    m.leftArm.roll = MathHelper.lerp(w, m.leftArm.roll, 0f);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -1.7f);
                    m.leftForearm.pitch = MathHelper.lerp(w, m.leftForearm.pitch, -1.7f);
                    m.head.pitch = MathHelper.lerp(w, m.head.pitch, 0.35f + sob);
                    m.body.pitch += (0.1f + sob) * w;
                }
                case KICK -> {
                    // Echa la pierna para atrás y patea hacia adelante
                    float k = t < 0.35f ? t / 0.35f : 1f - (t - 0.35f) / 0.65f;
                    float swingLeg = t < 0.35f ? 0.6f * k : -1.3f * MathHelper.sin((t - 0.35f) / 0.65f * PI);
                    m.rightLeg.pitch = swingLeg;
                    m.rightShin.pitch = t < 0.35f ? 0.9f * k : 0.1f;
                    m.leftArm.pitch = MathHelper.lerp(w, m.leftArm.pitch, -swingLeg * 0.6f);
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, swingLeg * 0.4f);
                    m.body.pitch -= swingLeg * 0.08f;
                }
                case BLOW -> {
                    // Se agacha hacia el pastel, manos atrás, y sopla dos veces
                    float puff = Math.max(0f, MathHelper.sin(t * PI * 4f));
                    m.body.pitch += 0.35f * w;
                    m.head.pitch = MathHelper.lerp(w, m.head.pitch, 0.3f - puff * 0.08f);
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, 0.45f);
                    m.leftArm.pitch = MathHelper.lerp(w, m.leftArm.pitch, 0.45f);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0.1f);
                    m.leftArm.roll = MathHelper.lerp(w, m.leftArm.roll, -0.1f);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -0.2f);
                    m.leftForearm.pitch = MathHelper.lerp(w, m.leftForearm.pitch, -0.2f);
                }
                case GIFT -> {
                    // Abraza el regalo contra el pecho y se mece
                    float rock = MathHelper.sin(t * PI * 4f) * 0.08f;
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -1.0f);
                    m.leftArm.pitch = MathHelper.lerp(w, m.leftArm.pitch, -1.0f);
                    m.rightArm.yaw = MathHelper.lerp(w, m.rightArm.yaw, -0.55f);
                    m.leftArm.yaw = MathHelper.lerp(w, m.leftArm.yaw, 0.55f);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0f);
                    m.leftArm.roll = MathHelper.lerp(w, m.leftArm.roll, 0f);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -1.25f);
                    m.leftForearm.pitch = MathHelper.lerp(w, m.leftForearm.pitch, -1.25f);
                    m.rightForearm.yaw = MathHelper.lerp(w, 0f, -0.4f);
                    m.leftForearm.yaw = MathHelper.lerp(w, 0f, 0.4f);
                    m.head.pitch += 0.2f * w;
                    m.head.roll += (0.12f + rock) * w;
                    m.body.roll += rock * w;
                }
                case SCARED -> {
                    // Brinca con los brazos arriba y se encoge
                    float jump = MathHelper.sin(MathHelper.clamp(t * 2.2f, 0f, 1f) * PI);
                    m.root.pivotY -= jump * 4f * w;
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -2.3f);
                    m.leftArm.pitch = MathHelper.lerp(w, m.leftArm.pitch, -2.3f);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0.6f);
                    m.leftArm.roll = MathHelper.lerp(w, m.leftArm.roll, -0.6f);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -0.3f);
                    m.leftForearm.pitch = MathHelper.lerp(w, m.leftForearm.pitch, -0.3f);
                    m.rightShin.pitch += 0.6f * jump * w;
                    m.leftShin.pitch += 0.6f * jump * w;
                    m.head.pitch -= 0.2f * w;
                }
                case TOAST -> {
                    // Alza el vaso con la derecha, la izquierda en la cintura
                    float lift = MathHelper.sin(MathHelper.clamp(t * 1.6f, 0f, 1f) * PI * 0.5f);
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -1.2f - 1.1f * lift);
                    m.rightArm.yaw = MathHelper.lerp(w, m.rightArm.yaw, -0.25f);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0f);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -0.45f);
                    m.leftArm.roll = MathHelper.lerp(w, m.leftArm.roll, -0.35f);
                    m.leftForearm.pitch = MathHelper.lerp(w, m.leftForearm.pitch, -1.6f);
                    m.leftForearm.yaw = MathHelper.lerp(w, 0f, 0.6f);
                    m.head.pitch -= 0.2f * lift * w;
                }
                case RAISE_HAND -> {
                    float wiggle = MathHelper.sin(t * PI * 6f) * 0.08f;
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -3.0f);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0.12f + wiggle);
                    m.rightArm.yaw = MathHelper.lerp(w, m.rightArm.yaw, 0f);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, 0f);
                    m.head.pitch -= 0.1f * w;
                }
                case WRITE -> {
                    // Cabeza agachada y la mano derecha va y viene sobre el pupitre
                    float pen = MathHelper.sin(t * PI * 16f) * 0.1f;
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -0.95f);
                    m.rightArm.yaw = MathHelper.lerp(w, m.rightArm.yaw, -0.15f + pen);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0f);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -0.6f);
                    m.leftArm.pitch = MathHelper.lerp(w, m.leftArm.pitch, -0.8f);
                    m.leftArm.yaw = MathHelper.lerp(w, m.leftArm.yaw, 0.35f);
                    m.leftArm.roll = MathHelper.lerp(w, m.leftArm.roll, 0f);
                    m.leftForearm.pitch = MathHelper.lerp(w, m.leftForearm.pitch, -0.5f);
                    m.head.pitch = MathHelper.lerp(w, m.head.pitch, 0.45f);
                    m.body.pitch += 0.12f * w;
                }
                case POINT -> {
                    // Brazo al lado señalando el pizarrón, mirando a los alumnos
                    float tap = MathHelper.sin(t * PI * 6f) * 0.06f;
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -0.6f);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 1.35f + tap);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, 0f);
                    m.leftArm.pitch = MathHelper.lerp(w, m.leftArm.pitch, -0.3f);
                    m.leftForearm.pitch = MathHelper.lerp(w, m.leftForearm.pitch, -1.2f);
                    m.head.yaw += -0.5f * w;
                }
                case LOOK -> {
                    // Mano de visera y voltea a un lado y luego al otro
                    float look = MathHelper.sin(t * PI * 2f);
                    m.head.yaw = MathHelper.lerp(w, m.head.yaw, look * 0.8f);
                    m.body.yaw += look * 0.15f * w;
                    m.rightArm.pitch = MathHelper.lerp(w, m.rightArm.pitch, -1.9f);
                    m.rightArm.yaw = MathHelper.lerp(w, m.rightArm.yaw, -0.6f + look * 0.5f);
                    m.rightArm.roll = MathHelper.lerp(w, m.rightArm.roll, 0f);
                    m.rightForearm.pitch = MathHelper.lerp(w, m.rightForearm.pitch, -1.3f);
                }
            }
        }

        // ---------- 6. Cara: expresión, ojos y boca ----------
        KikoExpression ex = chooseExpression(e, age, mood, anim, w);
        animateEyes(m, e, age, headYaw, headPitch, mood, anim, w, ex);
        animateFace(m, e, age, ex);
    }

    /** La expresión que manda el servidor, o una según lo que esté pasando y su ánimo. */
    private static KikoExpression chooseExpression(KikoEntity e, float age, int mood, KikoAnimation anim, float w) {
        if (e.isLyingDown()) return KikoExpression.SLEEPY;
        if (e instanceof com.jaciel.kiko.family.KikoChildEntity kid && kid.isBabyAsleep()) return KikoExpression.SLEEPY;
        KikoExpression ex = e.getExpression();
        if (ex != null) return ex;
        if (anim == KikoAnimation.HAPPY && w > 0.3f) return KikoExpression.LAUGH;
        if (anim == KikoAnimation.REJECT && w > 0.2f) return KikoExpression.POUT;
        if (anim == KikoAnimation.WAVE && w > 0.2f) return KikoExpression.SMILE;
        if (anim != null && w > 0.25f) {
            switch (anim) {
                case CLAP, CHEER, DANCE -> { return KikoExpression.LAUGH; }
                case YAWN -> { return KikoExpression.YAWN; }
                case HUG -> { return KikoExpression.EMBARRASSED; }
                case TAG, KICK -> { return KikoExpression.GRIN; }
                case THINK -> { return KikoExpression.NEUTRAL; }
                case CRY -> { return KikoExpression.SAD; }
                case LOOK -> { return KikoExpression.SURPRISED; }
                case STRETCH -> { return KikoExpression.SLEEPY; }
                case BLOW, SCARED -> { return KikoExpression.SURPRISED; }
                case GIFT -> { return KikoExpression.LOVESTRUCK; }
                case TOAST -> { return KikoExpression.LAUGH; }
                case RAISE_HAND, POINT -> { return KikoExpression.SMILE; }
                case WRITE -> { return KikoExpression.NEUTRAL; }
                default -> {}
            }
        }
        switch (e.getKikoPose()) {
            case SHIVER, COVER -> { return KikoExpression.NERVOUS; }
            case STARGAZE -> { return KikoExpression.SMILE; }
            case HOLD_HAND, LEAN -> { return KikoExpression.LOVESTRUCK; }
            case PAIR_DANCE -> { return KikoExpression.LAUGH; }
            default -> {}
        }
        if (e.isAttacking()) return KikoExpression.ANGRY;
        if (e.getHealth() < e.getMaxHealth() * 0.3f) return KikoExpression.NERVOUS;
        if (e.isFishing()) return KikoExpression.SMILE;

        // Sola: cambia de cara de vez en cuando según qué tanto te quiere
        int seed = e.getId() * 17;
        int cycle = MathHelper.floor(age / 160f);
        int roll = hash(seed + cycle * 977) % 10;
        return switch (mood) {
            case 0 -> roll < 7 ? KikoExpression.POUT : KikoExpression.NEUTRAL;
            case 2 -> roll < 4 ? KikoExpression.SMILE : roll < 6 ? KikoExpression.SMUG : KikoExpression.NEUTRAL;
            case 3 -> roll < 5 ? KikoExpression.SMILE : roll < 7 ? KikoExpression.GRIN
                    : roll < 8 ? KikoExpression.EMBARRASSED : KikoExpression.SMUG;
            default -> roll < 2 ? KikoExpression.SMILE : KikoExpression.NEUTRAL;
        };
    }

    /** Boca (y que se mueva al hablar), cejas y extras: sonrojo, sudor, lágrimas, marca de enojo. */
    private static void animateFace(KikoModel m, KikoEntity e, float age, KikoExpression ex) {
        String mouth = ex.mouth;
        if (e.isTalking() && !e.isSleeping()) {
            // Abre y cierra la boca en sílabas, cada Kiko con su ritmo
            int syllable = MathHelper.floor(age / 3f);
            int h = hash(e.getId() * 13 + syllable) % 4;
            if (h == 0 || h == 2) mouth = "open";
            else if (h == 3) mouth = ex == KikoExpression.LAUGH ? "laugh" : "o";
        }
        for (var entry : m.mouths.entrySet()) entry.getValue().visible = entry.getKey().equals(mouth);

        // Cejas: suben, bajan y se inclinan (enojada \ /, preocupada / \)
        m.rightBrow.pivotY += ex.browY;
        m.leftBrow.pivotY += ex.browY;
        m.rightBrow.roll = ex.browRoll;
        m.leftBrow.roll = -ex.browRoll;
        if (ex == KikoExpression.SMUG) {
            m.leftBrow.pivotY -= 0.4f; // una ceja arriba
        }

        m.blush.visible = ex.blush;
        m.sweat.visible = ex.sweat;
        m.tears.visible = ex.tears;
        m.anger.visible = ex.anger;
        if (ex.sweat) m.sweat.pivotY += (age * 0.04f) % 1f;            // la gotita resbala
        if (ex.tears) m.tears.pivotY += (age * 0.06f) % 1.5f;          // las lágrimas caen
        if (ex.anger) {
            float pulse = 1f + MathHelper.sin(age * 0.5f) * 0.12f;      // la marca de enojo late
            m.anger.xScale = pulse;
            m.anger.yScale = pulse;
        }
    }

    private static void animateEyes(KikoModel m, KikoEntity e, float age, float headYaw, float headPitch,
                                    int mood, KikoAnimation anim, float w, KikoExpression ex) {
        boolean happy = ex.happyEyes;
        boolean annoyed = mood == 0 || (anim == KikoAnimation.REJECT && w > 0.2f);

        // Parpadeo. Cada Kiko tiene su propio ritmo según su id, y a veces parpadea doble
        int seed = e.getId() * 31;
        int period = 70 + Math.floorMod(seed, 50);
        int cycle = MathHelper.floor(age / period);
        float local = age - cycle * (float) period;
        float closeness = blink(local);
        if (hash(seed + cycle) % 4 == 0) closeness = Math.max(closeness, blink(local - 7f));
        float open = 1f - closeness;
        if (annoyed) open = Math.min(open, 0.55f);
        open *= ex.eyeOpen;

        m.rightEyeHappy.visible = happy;
        m.leftEyeHappy.visible = happy;
        boolean closed = !happy && open < 0.25f;
        m.rightEyeClosed.visible = closed;
        m.leftEyeClosed.visible = closed;
        boolean showEyes = !happy && !closed;
        m.rightEye.visible = showEyes;
        m.leftEye.visible = showEyes;
        // Guiño (presumida): el ojo derecho hace la carita feliz
        if (ex.wink && showEyes) {
            m.rightEye.visible = false;
            m.rightEyeHappy.visible = true;
        }
        m.rightEye.yScale = open;
        m.leftEye.yScale = open;

        // Pupilas. De vez en cuando miran de reojo. v3.3.3 se mueven de a un pixel completo
        // (moverlas medio pixel se veía chueco, sobre todo en los ojos con blanco de los chicos)
        float gaze = 0f;
        int glanceCycle = MathHelper.floor(age / 90f);
        float glanceT = age - glanceCycle * 90f;
        if (glanceT >= 3f && glanceT < 27f) {
            int h = hash(seed + 7919 * glanceCycle) % 3;   // 0 izquierda, 1 nada, 2 derecha
            gaze = h == 0 ? -1f : h == 2 ? 1f : 0f;
        }
        if (Math.abs(headYaw) > 30f && gaze == 0f) gaze = Math.signum(headYaw);
        m.rightPupil.pivotX += gaze;
        m.leftPupil.pivotX += gaze;
    }

    /** 0 abierto, 1 cerrado. Dura 5 ticks. */
    private static float blink(float t) {
        if (t < 0f || t > 5f) return 0f;
        return MathHelper.sin(t / 5f * PI);
    }

    private static int hash(int x) {
        x ^= x >>> 16;
        x *= 0x7feb352d;
        x ^= x >>> 15;
        x *= 0x846ca68b;
        x ^= x >>> 16;
        return x & 0x7fffffff;
    }

    /** v3.9 poses que duran. Las piernas de las que caminan siguen normales. */
    private static void applyPose(KikoModel m, com.jaciel.kiko.animation.KikoPose pose, float age, float idle) {
        switch (pose) {
            case SIT -> {
                // En silla (la entidad está a la altura del asiento): baja la cadera y dobla las rodillas
                m.root.pivotY += m.hipHeight - 3f;
                m.rightLeg.pitch = -1.5f;
                m.leftLeg.pitch = -1.5f;
                m.rightLeg.yaw = 0.12f;
                m.leftLeg.yaw = -0.12f;
                m.rightLeg.roll = 0f;
                m.leftLeg.roll = 0f;
                m.rightShin.pitch = 1.5f;
                m.leftShin.pitch = 1.5f;
                m.rightArm.pitch = -0.35f;
                m.leftArm.pitch = -0.35f;
                m.rightArm.roll = 0.1f;
                m.leftArm.roll = -0.1f;
                m.rightForearm.pitch = -0.5f;
                m.leftForearm.pitch = -0.5f;
                m.body.roll = 0f;
                m.skirt.pitch = -0.6f;
            }
            case UMBRELLA -> {
                m.rightArm.pitch = -0.95f;
                m.rightArm.yaw = -0.25f;
                m.rightArm.roll = 0.05f;
                m.rightForearm.pitch = -0.75f;
            }
            case COVER -> {
                m.rightArm.pitch = -2.75f;
                m.leftArm.pitch = -2.75f;
                m.rightArm.yaw = -0.35f;
                m.leftArm.yaw = 0.35f;
                m.rightArm.roll = 0f;
                m.leftArm.roll = 0f;
                m.rightForearm.pitch = -1.25f;
                m.leftForearm.pitch = -1.25f;
                m.head.pitch += 0.25f;
                m.body.pitch += 0.15f;
            }
            case SHIVER -> {
                float shake = MathHelper.sin(age * 2.6f) * 0.035f;
                m.rightArm.pitch = -0.75f;
                m.leftArm.pitch = -0.75f;
                m.rightArm.yaw = -0.6f;
                m.leftArm.yaw = 0.6f;
                m.rightArm.roll = 0f;
                m.leftArm.roll = 0f;
                m.rightForearm.pitch = -1.35f;
                m.leftForearm.pitch = -1.35f;
                m.rightForearm.yaw = -0.8f;
                m.leftForearm.yaw = 0.8f;
                m.head.pitch += 0.18f;
                m.body.roll += shake;
                m.head.roll += shake * 1.5f;
            }
            case STARGAZE -> {
                float slow = MathHelper.sin(age * 0.03f) * 0.05f;
                ground(m, false);
                m.body.pitch = -0.45f;
                m.rightArm.pitch = 0.75f;
                m.leftArm.pitch = 0.75f;
                m.rightArm.roll = 0.15f;
                m.leftArm.roll = -0.15f;
                m.rightForearm.pitch = 0.2f;
                m.leftForearm.pitch = 0.2f;
                m.head.pitch = -0.55f + slow;
                m.head.yaw = slow * 3f;
            }
            case PAIR_DANCE -> {
                float beat = age * 0.32f;
                float side = MathHelper.sin(beat);
                m.root.pivotY -= Math.abs(MathHelper.cos(beat)) * 1.0f;
                m.body.yaw = MathHelper.sin(age * 0.06f) * 0.45f;
                m.body.roll = side * 0.08f;
                m.rightArm.pitch = -1.15f;
                m.rightArm.yaw = -0.35f;
                m.rightArm.roll = 0f;
                m.rightForearm.pitch = -0.2f;
                m.leftArm.pitch = -1.4f;
                m.leftArm.yaw = 0.45f;
                m.leftArm.roll = -0.2f;
                m.leftForearm.pitch = -0.9f;
                m.rightLeg.pitch = Math.max(0f, side) * -0.4f;
                m.leftLeg.pitch = Math.max(0f, -side) * -0.4f;
                m.rightShin.pitch = Math.max(0f, side) * 0.6f;
                m.leftShin.pitch = Math.max(0f, -side) * 0.6f;
                m.skirt.yaw = side * 0.2f;
                m.ponytail.roll -= side * 0.3f;
            }
            case HOLD_HAND -> {
                m.leftArm.pitch = MathHelper.lerp(idle, m.leftArm.pitch * 0.3f, -0.35f);
                m.leftArm.roll = -0.4f;
                m.leftArm.yaw = 0f;
                m.leftForearm.pitch = -0.15f;
                m.head.roll += 0.08f;
            }
            case LEAN -> {
                ground(m, true);
                m.rightArm.pitch = -0.5f;
                m.leftArm.pitch = -0.5f;
                m.rightArm.roll = 0.1f;
                m.leftArm.roll = -0.1f;
                m.rightForearm.pitch = -0.7f;
                m.leftForearm.pitch = -0.7f;
                m.body.roll = 0.18f;
                m.head.roll = 0.4f;
                m.head.pitch = 0.1f;
            }
            default -> {}
        }
    }

    /** Sentada en el piso: piernas estiradas (o dobladas si bent). */
    private static void ground(KikoModel m, boolean bent) {
        m.root.pivotY += m.hipHeight - 2f;
        m.rightLeg.pitch = bent ? -1.25f : -1.5f;
        m.leftLeg.pitch = bent ? -1.25f : -1.5f;
        m.rightLeg.yaw = bent ? 0.2f : 0.15f;
        m.leftLeg.yaw = bent ? -0.2f : -0.15f;
        m.rightLeg.roll = 0f;
        m.leftLeg.roll = 0f;
        m.rightShin.pitch = bent ? 0.9f : 0.1f;
        m.leftShin.pitch = bent ? 0.9f : 0.1f;
        m.skirt.pitch = -0.7f;
    }

    /** Curva 0→1→0 con entrada y salida suaves (smoothstep). */
    private static float envelope(float t) {
        float x = MathHelper.clamp(Math.min(t, 1f - t) / 0.15f, 0f, 1f);
        return x * x * (3f - 2f * x);
    }
}
