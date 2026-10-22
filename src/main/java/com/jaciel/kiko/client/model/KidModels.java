package com.jaciel.kiko.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;

/**
 * Modelos de los hijos por etapa (bebé, niño/niña, chavo/chava, adultos). Los genera tools/gen_kids.py (no editar a mano).
 * Mismas piezas que Kiko, así que usan KikoModel con todas sus animaciones y caras.
 */
@SuppressWarnings("unused")
public final class KidModels {
    private KidModels() {}

    /** Alto de cada modelo en unidades (para escalarlo a su altura en bloques). */
    public static final java.util.Map<String, Float> UNITS = java.util.Map.of(
        "baby", 32.100f,
        "child_boy", 41.100f,
        "child_girl", 39.750f,
        "teen_boy", 51.100f,
        "teen_girl", 50.750f,
        "adult_man", 62.600f,
        "adult_woman", 61.750f);

    /** baby (0.6 bloques, 32.10 unidades de alto). */
    public static TexturedModelData baby() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -19f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(36, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(166, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(42, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(182, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(148, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(188, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(194, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(48, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(56, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(64, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(72, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(80, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(88, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(96, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(104, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(112, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(120, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(154, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(160, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(170, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(172, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(174, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(136, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("spikes", ModelPartBuilder.create()
                        .uv(224, 0).cuboid(-2f, -12f, -2f, 4, 1, 4, new Dilation(0.4f))
                        .uv(128, 21).cuboid(-1f, -12.8f, 0f, 2, 1, 2, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(14, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(80, 0).cuboid(-3.5f, 0f, -2.5f, 7, 6, 5, new Dilation(0.6f))
                        .uv(104, 0).cuboid(-3.5f, 6f, -2.5f, 7, 3, 5, new Dilation(0.7f))
                        .uv(26, 21).cuboid(-2f, 0f, -3.4f, 4, 3, 1, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, -17f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(152, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 3, 3, new Dilation(0.5f)),
                        ModelTransform.of(-4.6f, -16f, 0f, 0f, 0f, 0.3f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(164, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 3, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 2f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(176, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 3, 3, new Dilation(0.5f)),
                        ModelTransform.of(4.6f, -16f, 0f, 0f, 0f, -0.3f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(188, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 3, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 2f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(128, 0).cuboid(-1.5f, 0f, -1.5f, 3, 4, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(-2f, -8f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(200, 0).cuboid(-1.5f, 0f, -1.5f, 3, 3, 3, new Dilation(0.5f))
                        .uv(240, 0).cuboid(-1.5f, 3f, -2.5f, 3, 1, 4, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 4f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(140, 0).cuboid(-1.5f, 0f, -1.5f, 3, 4, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(2f, -8f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(212, 0).cuboid(-1.5f, 0f, -1.5f, 3, 3, 3, new Dilation(0.5f))
                        .uv(0, 21).cuboid(-1.5f, 3f, -2.5f, 3, 1, 4, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 4f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -37f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create(),
                        ModelTransform.pivot(6f, -39.5f, 0.5f));
        headD.addChild("clip", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -31f, 0f));
        ModelPartData skirtD = bodyD.addChild("skirt", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -17f, 0f));
        skirtD.addChild("dress", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        bodyD.addChild("hood", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -31f, 0f));
        bodyD.addChild("scarf", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -31f, 0f));
        headD.addChild("hat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -31f, 0f));
        headD.addChild("hardhat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -31f, 0f));
        headD.addChild("beanie", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -31f, 0f));
        return TexturedModelData.of(data, 256, 256);
    }

    /** child_boy (1.1 bloques, 41.10 unidades de alto). */
    public static TexturedModelData child_boy() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -28f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(42, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(172, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(182, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(148, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(48, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(174, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(188, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(154, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(194, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(200, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(54, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(62, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(70, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(78, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(86, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(94, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(102, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(126, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(160, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(166, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(178, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(180, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("spikes", ModelPartBuilder.create()
                        .uv(14, 21).cuboid(-2f, -12f, -2f, 4, 1, 4, new Dilation(0.4f))
                        .uv(134, 21).cuboid(-1f, -12.8f, 0f, 2, 1, 2, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(30, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(80, 0).cuboid(-3.5f, 0f, -2f, 7, 6, 4, new Dilation(0.45f))
                        .uv(220, 0).cuboid(-3f, 6f, -2f, 6, 3, 4, new Dilation(0.4f))
                        .uv(198, 0).cuboid(-3.5f, 9f, -2f, 7, 3, 4, new Dilation(0.55f)),
                        ModelTransform.pivot(0f, -26f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(126, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 6, 3, new Dilation(0.4f)),
                        ModelTransform.of(-5f, -25f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(150, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 5, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 5f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(138, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 6, 3, new Dilation(0.4f)),
                        ModelTransform.of(5f, -25f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(162, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 5, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 5f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(102, 0).cuboid(-1.5f, 0f, -1.5f, 3, 7, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(-1.9f, -14f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(174, 0).cuboid(-1.5f, 0f, -1.5f, 3, 5, 3, new Dilation(0.4f))
                        .uv(240, 0).cuboid(-1.5f, 5f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 7f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(114, 0).cuboid(-1.5f, 0f, -1.5f, 3, 7, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(1.9f, -14f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(186, 0).cuboid(-1.5f, 0f, -1.5f, 3, 5, 3, new Dilation(0.4f))
                        .uv(0, 21).cuboid(-1.5f, 5f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 7f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -28f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create(),
                        ModelTransform.pivot(6f, -30.5f, 0.5f));
        headD.addChild("clip", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        ModelPartData skirtD = bodyD.addChild("skirt", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -8f, 0f));
        skirtD.addChild("dress", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        bodyD.addChild("hood", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        bodyD.addChild("scarf", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        headD.addChild("hat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        headD.addChild("hardhat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        headD.addChild("beanie", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        return TexturedModelData.of(data, 256, 256);
    }

    /** child_girl (1.1 bloques, 39.75 unidades de alto). */
    public static TexturedModelData child_girl() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -28f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(82, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(230, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(196, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(88, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(222, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(236, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(202, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(242, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(248, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(94, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(102, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(126, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(134, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(158, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(166, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(208, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(214, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(224, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(226, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(228, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(190, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(180, 0).cuboid(-7.4f, 0f, -1f, 2, 7, 2, new Dilation(0.3f))
                        .uv(188, 0).cuboid(5.4f, 0f, -1f, 2, 7, 2, new Dilation(0.3f))
                        .uv(174, 21).cuboid(-7.6f, 0.5f, -1.2f, 2, 1, 2, new Dilation(0.35f))
                        .uv(182, 21).cuboid(5.6f, 0.5f, -1.2f, 2, 1, 2, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, -5f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(70, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(110, 0).cuboid(-3.5f, 0f, -2f, 7, 6, 4, new Dilation(0.45f))
                        .uv(22, 21).cuboid(-3f, 6f, -2f, 6, 3, 4, new Dilation(0.4f))
                        .uv(0, 21).cuboid(-3.5f, 9f, -2f, 7, 3, 4, new Dilation(0.55f)),
                        ModelTransform.pivot(0f, -26f, 0f));
        bodyD.addChild("kid_skirt", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-4.5f, 0f, -3f, 9, 8, 6, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 9f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(156, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 6, 3, new Dilation(0.4f)),
                        ModelTransform.of(-5f, -25f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(196, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 5, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 5f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(168, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 6, 3, new Dilation(0.4f)),
                        ModelTransform.of(5f, -25f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(208, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 5, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 5f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(132, 0).cuboid(-1.5f, 0f, -1.5f, 3, 7, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(-1.9f, -14f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(220, 0).cuboid(-1.5f, 0f, -1.5f, 3, 5, 3, new Dilation(0.4f))
                        .uv(42, 21).cuboid(-1.5f, 5f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 7f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(144, 0).cuboid(-1.5f, 0f, -1.5f, 3, 7, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(1.9f, -14f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(232, 0).cuboid(-1.5f, 0f, -1.5f, 3, 5, 3, new Dilation(0.4f))
                        .uv(56, 21).cuboid(-1.5f, 5f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 7f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -28f, 6.2f));
        headD.addChild("clip", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        ModelPartData skirtD = bodyD.addChild("skirt", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -8f, 0f));
        skirtD.addChild("dress", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        bodyD.addChild("hood", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        bodyD.addChild("scarf", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        headD.addChild("hat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        headD.addChild("hardhat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        headD.addChild("beanie", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        return TexturedModelData.of(data, 256, 256);
    }

    /** teen_boy (1.55 bloques, 51.10 unidades de alto). */
    public static TexturedModelData teen_boy() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -39f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(90, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(212, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(222, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(188, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(96, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(214, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(228, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(194, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(234, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(240, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(102, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(126, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(134, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(158, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(166, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(174, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(200, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(206, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(218, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(220, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(182, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("spikes", ModelPartBuilder.create()
                        .uv(64, 21).cuboid(-5f, -11.6f, -5.6f, 10, 1, 3, new Dilation(0.5f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(52, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(104, 0).cuboid(-4f, 0f, -2f, 8, 8, 4, new Dilation(0.45f))
                        .uv(224, 0).cuboid(-3.5f, 8f, -2f, 7, 4, 4, new Dilation(0.4f))
                        .uv(200, 0).cuboid(-4f, 12f, -2f, 8, 4, 4, new Dilation(0.5f))
                        .uv(28, 21).cuboid(-4f, 11.5f, -2.2f, 8, 1, 4, new Dilation(0.55f)),
                        ModelTransform.pivot(0f, -37f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(128, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 9, 3, new Dilation(0.45f)),
                        ModelTransform.of(-5.6f, -36f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(176, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 8, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 8f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(140, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 9, 3, new Dilation(0.45f)),
                        ModelTransform.of(5.6f, -36f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(188, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 8, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 8f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-1.5f, 0f, -1.5f, 3, 10, 3, new Dilation(0.5f)),
                        ModelTransform.pivot(-2.1f, -21f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(152, 0).cuboid(-1.5f, 0f, -1.5f, 3, 9, 3, new Dilation(0.4f))
                        .uv(0, 21).cuboid(-1.5f, 9f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(92, 0).cuboid(-1.5f, 0f, -1.5f, 3, 10, 3, new Dilation(0.5f)),
                        ModelTransform.pivot(2.1f, -21f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(164, 0).cuboid(-1.5f, 0f, -1.5f, 3, 9, 3, new Dilation(0.4f))
                        .uv(14, 21).cuboid(-1.5f, 9f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -17f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create(),
                        ModelTransform.pivot(6f, -19.5f, 0.5f));
        headD.addChild("clip", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -11f, 0f));
        ModelPartData skirtD = bodyD.addChild("skirt", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 3f, 0f));
        skirtD.addChild("dress", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        bodyD.addChild("hood", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -11f, 0f));
        bodyD.addChild("scarf", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -11f, 0f));
        headD.addChild("hat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -11f, 0f));
        headD.addChild("hardhat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -11f, 0f));
        headD.addChild("beanie", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -11f, 0f));
        return TexturedModelData.of(data, 256, 256);
    }

    /** teen_girl (1.55 bloques, 50.75 unidades de alto). */
    public static TexturedModelData teen_girl() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -39f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(136, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(6, 32).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(16, 32).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(234, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(8, 32).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(22, 32).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(240, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(28, 32).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(34, 32).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(148, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(156, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(172, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(180, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(188, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(196, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(204, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(212, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(246, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(0, 32).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(10, 32).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(12, 32).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(14, 32).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(228, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(136, 0).cuboid(-5.5f, 0f, -0.6f, 11, 10, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(234, 0).cuboid(-1.5f, 0f, 0f, 3, 10, 2, new Dilation(0.3f))
                        .uv(122, 21).cuboid(-2f, -0.5f, -0.4f, 4, 1, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, -8f, 6f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(162, 0).cuboid(-4f, 0f, -2f, 8, 8, 4, new Dilation(0.45f))
                        .uv(36, 21).cuboid(-3.5f, 8f, -2f, 7, 4, 4, new Dilation(0.4f))
                        .uv(12, 21).cuboid(-4f, 12f, -2f, 8, 4, 4, new Dilation(0.5f))
                        .uv(86, 21).cuboid(-4f, 11.5f, -2.2f, 8, 1, 4, new Dilation(0.55f)),
                        ModelTransform.pivot(0f, -37f, 0f));
        bodyD.addChild("kid_skirt", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-4.8f, 0f, -2.8f, 10, 11, 6, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 13f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(186, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 9, 3, new Dilation(0.45f)),
                        ModelTransform.of(-5.6f, -36f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(244, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 8, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 8f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(198, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 9, 3, new Dilation(0.45f)),
                        ModelTransform.of(5.6f, -36f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 8, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 8f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-1.5f, 0f, -1.5f, 3, 10, 3, new Dilation(0.5f)),
                        ModelTransform.pivot(-2.1f, -21f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(210, 0).cuboid(-1.5f, 0f, -1.5f, 3, 9, 3, new Dilation(0.4f))
                        .uv(58, 21).cuboid(-1.5f, 9f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 10, 3, new Dilation(0.5f)),
                        ModelTransform.pivot(2.1f, -21f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(222, 0).cuboid(-1.5f, 0f, -1.5f, 3, 9, 3, new Dilation(0.4f))
                        .uv(72, 21).cuboid(-1.5f, 9f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        headD.addChild("clip", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -11f, 0f));
        ModelPartData skirtD = bodyD.addChild("skirt", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 3f, 0f));
        skirtD.addChild("dress", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        bodyD.addChild("hood", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -11f, 0f));
        bodyD.addChild("scarf", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -11f, 0f));
        headD.addChild("hat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -11f, 0f));
        headD.addChild("hardhat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -11f, 0f));
        headD.addChild("beanie", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -11f, 0f));
        return TexturedModelData.of(data, 256, 256);
    }

    /** adult_man (1.85 bloques, 62.60 unidades de alto). */
    public static TexturedModelData adult_man() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(18, 30).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(28, 30).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(248, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(156, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(20, 30).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(34, 30).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(0, 30).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(40, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(46, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(162, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(170, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(178, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(186, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(194, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(202, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(210, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(218, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(226, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(234, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(6, 30).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(12, 30).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(22, 30).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(24, 30).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(26, 30).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(242, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("spikes", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-5f, -11.6f, -5.6f, 10, 1, 4, new Dilation(0.55f))
                        .uv(26, 21).cuboid(-4f, -12.2f, -2f, 8, 1, 6, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(138, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(54, 21).cuboid(-4.7f, 14.4f, -2.7f, 9, 1, 5, new Dilation(0.7f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(166, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(190, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(178, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(202, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(82, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(96, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create(),
                        ModelTransform.pivot(6f, -8.5f, 0.5f));
        headD.addChild("clip", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData skirtD = bodyD.addChild("skirt", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 14f, 0f));
        skirtD.addChild("dress", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        bodyD.addChild("hood", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        bodyD.addChild("scarf", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hardhat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("beanie", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        return TexturedModelData.of(data, 256, 256);
    }

    /** adult_woman (1.75 bloques, 61.75 unidades de alto). */
    public static TexturedModelData adult_woman() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(36, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(76, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(206, 22).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(186, 35).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(196, 35).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(162, 35).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(212, 22).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(188, 35).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(202, 35).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(168, 35).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(208, 35).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(214, 35).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(60, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(68, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(76, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(84, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(92, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(100, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(108, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(116, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(124, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(132, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(174, 35).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(180, 35).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(190, 35).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(192, 35).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(194, 35).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(140, 35).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(116, 0).cuboid(-5.5f, 0f, -0.6f, 11, 18, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(198, 0).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(54, 22).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(24, 22).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(146, 35).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(218, 22).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(102, 22).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(186, 22).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(196, 22).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(0, 35).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(20, 35).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(78, 22).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(166, 22).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(176, 22).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(40, 35).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(154, 22).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("kid_skirt", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, 0f, -3.2f, 11, 15, 7, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 17f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(226, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(-6.6f, -47f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(0, 22).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(238, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(6.6f, -47f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(12, 22).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(142, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(174, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(126, 22).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(158, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(186, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(140, 22).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("ponytail", ModelPartBuilder.create(),
                        ModelTransform.pivot(6f, -8.5f, 0.5f));
        headD.addChild("clip", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData skirtD = bodyD.addChild("skirt", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 14f, 0f));
        skirtD.addChild("dress", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        bodyD.addChild("hood", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        bodyD.addChild("scarf", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hardhat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("beanie", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        return TexturedModelData.of(data, 256, 256);
    }
}
