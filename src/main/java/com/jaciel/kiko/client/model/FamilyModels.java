package com.jaciel.kiko.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;

/**
 * Modelos de los hijos e hijas (bebé, niños, adolescentes y adultos). Los genera tools/gen_family.py (no editar a mano).
 * Mismas piezas que Kiko, así que usan KikoModel con todas sus animaciones y caras.
 * La piel, el pelo y los ojos se pintan al dibujar (capas en textures/entity/family).
 */
@SuppressWarnings("unused")
public final class FamilyModels {
    private FamilyModels() {}

    /** baby (0.55 bloques, mide 23.30 unidades). */
    public static final float BABY_UNITS = 23.30f;

    public static TexturedModelData baby() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -11f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(168, 0).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(46, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(56, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(22, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(174, 0).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(48, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(62, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(28, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(68, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(74, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(180, 0).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(188, 0).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(196, 0).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(204, 0).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(212, 0).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(220, 0).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(228, 0).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(236, 0).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(244, 0).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(34, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(40, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(50, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(52, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(54, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(8, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-4.5f, 0f, -3.5f, 9, 11, 7, new Dilation(0.4f))
                        .uv(14, 21).cuboid(-1.5f, 4f, -4.4f, 3, 1, 1)
                        .uv(156, 0).cuboid(-1.5f, 0f, -1.5f, 3, 1, 3),
                        ModelTransform.pivot(0f, -11f, 0f));
        headD.addChild("hat", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-5.5f, -12f, -5.6f, 11, 6, 11, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create(),
                        ModelTransform.pivot(-3f, -9f, 0f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 3f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create(),
                        ModelTransform.pivot(-2f, -4f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 2f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create(),
                        ModelTransform.pivot(3f, -9f, 0f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 3f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create(),
                        ModelTransform.pivot(2f, -4f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 2f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -45f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create(),
                        ModelTransform.pivot(6f, -47.5f, 0.5f));
        headD.addChild("clip", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -39f, 0f));
        ModelPartData skirtD = bodyD.addChild("skirt", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -23f, 0f));
        skirtD.addChild("dress", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        bodyD.addChild("hood", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -37f, 0f));
        bodyD.addChild("scarf", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -37f, 0f));
        headD.addChild("hardhat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -39f, 0f));
        headD.addChild("beanie", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -39f, 0f));
        return TexturedModelData.of(data, 256, 256);
    }

    /** child_girl (1.1 bloques, mide 38.75 unidades). */
    public static final float CHILD_GIRL_UNITS = 38.75f;

    public static TexturedModelData child_girl() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -27f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(88, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(210, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(186, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(94, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(212, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(226, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(232, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(238, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(100, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(108, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(116, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(124, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(132, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(140, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(148, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(156, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(172, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(204, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(214, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(218, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(180, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(230, 0).cuboid(-7.4f, -1f, -1f, 2, 7, 2, new Dilation(0.3f))
                        .uv(238, 0).cuboid(5.4f, -1f, -1f, 2, 7, 2, new Dilation(0.3f))
                        .uv(52, 21).cuboid(-7.9f, -1.5f, -1.5f, 3, 1, 3, new Dilation(0.1f))
                        .uv(64, 21).cuboid(4.9f, -1.5f, -1.5f, 3, 1, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, -7f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(132, 0).cuboid(-5.5f, -5f, 0f, 11, 8, 2),
                        ModelTransform.pivot(0f, -6f, 5.6f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(76, 21).cuboid(-1.5f, -1f, -1.5f, 3, 1, 3)
                        .uv(110, 0).cuboid(-3.5f, 0f, -2f, 7, 7, 4, new Dilation(0.3f))
                        .uv(80, 0).cuboid(-4.5f, 7f, -3f, 9, 10, 6, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, -26f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(158, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 6, 3, new Dilation(0.25f)),
                        ModelTransform.of(-4.6f, -25.5f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(170, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 6, 3, new Dilation(0.15f)),
                        ModelTransform.pivot(0f, 5f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(182, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 6, 3, new Dilation(0.25f)),
                        ModelTransform.of(4.6f, -25.5f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(194, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 6, 3, new Dilation(0.15f)),
                        ModelTransform.pivot(0f, 5f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(206, 0).cuboid(-1.5f, 0f, -1.5f, 3, 6, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(-1.7f, -13f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, 0f, -1.5f, 3, 5, 3, new Dilation(0.24f))
                        .uv(24, 21).cuboid(-1.5f, 5f, -2.5f, 3, 2, 4, new Dilation(0.18f)),
                        ModelTransform.pivot(0f, 6f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(218, 0).cuboid(-1.5f, 0f, -1.5f, 3, 6, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(1.7f, -13f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(12, 21).cuboid(-1.5f, 0f, -1.5f, 3, 5, 3, new Dilation(0.24f))
                        .uv(38, 21).cuboid(-1.5f, 5f, -2.5f, 3, 2, 4, new Dilation(0.18f)),
                        ModelTransform.pivot(0f, 6f, 0f));
        headD.addChild("clip", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -23f, 0f));
        ModelPartData skirtD = bodyD.addChild("skirt", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -8f, 0f));
        skirtD.addChild("dress", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        bodyD.addChild("hood", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        bodyD.addChild("scarf", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        headD.addChild("hat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -23f, 0f));
        headD.addChild("hardhat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -23f, 0f));
        headD.addChild("beanie", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -23f, 0f));
        return TexturedModelData.of(data, 256, 256);
    }

    /** child_boy (1.1 bloques, mide 40.90 unidades). */
    public static final float CHILD_BOY_UNITS = 40.90f;

    public static TexturedModelData child_boy() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -27f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(36, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(158, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(134, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(42, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(160, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(174, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(140, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(180, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(186, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
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
                        .uv(146, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(152, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(162, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(166, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(128, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("spikes", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-3f, -13.4f, -2f, 3, 2, 3, new Dilation(0.3f))
                        .uv(12, 21).cuboid(1f, -13.6f, -4f, 3, 2, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(24, 21).cuboid(-1.5f, -1f, -1.5f, 3, 1, 3)
                        .uv(80, 0).cuboid(-3.5f, 0f, -2f, 7, 9, 4, new Dilation(0.3f))
                        .uv(174, 0).cuboid(-3.5f, 9f, -2f, 7, 4, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, -26f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(102, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 6, 3, new Dilation(0.25f)),
                        ModelTransform.of(-4.6f, -25.5f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(114, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 6, 3, new Dilation(0.15f)),
                        ModelTransform.pivot(0f, 5f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(126, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 6, 3, new Dilation(0.25f)),
                        ModelTransform.of(4.6f, -25.5f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(138, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 6, 3, new Dilation(0.15f)),
                        ModelTransform.pivot(0f, 5f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(150, 0).cuboid(-1.5f, 0f, -1.5f, 3, 6, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(-1.7f, -13f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(196, 0).cuboid(-1.5f, 0f, -1.5f, 3, 5, 3, new Dilation(0.24f))
                        .uv(220, 0).cuboid(-1.5f, 5f, -2.5f, 3, 2, 4, new Dilation(0.18f)),
                        ModelTransform.pivot(0f, 6f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(162, 0).cuboid(-1.5f, 0f, -1.5f, 3, 6, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(1.7f, -13f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(208, 0).cuboid(-1.5f, 0f, -1.5f, 3, 5, 3, new Dilation(0.24f))
                        .uv(234, 0).cuboid(-1.5f, 5f, -2.5f, 3, 2, 4, new Dilation(0.18f)),
                        ModelTransform.pivot(0f, 6f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -29f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create(),
                        ModelTransform.pivot(6f, -31.5f, 0.5f));
        headD.addChild("clip", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -23f, 0f));
        ModelPartData skirtD = bodyD.addChild("skirt", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -8f, 0f));
        skirtD.addChild("dress", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        bodyD.addChild("hood", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        bodyD.addChild("scarf", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -22f, 0f));
        headD.addChild("hat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -23f, 0f));
        headD.addChild("hardhat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -23f, 0f));
        headD.addChild("beanie", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -23f, 0f));
        return TexturedModelData.of(data, 256, 256);
    }

    /** teen_girl (1.55 bloques, mide 49.75 unidades). */
    public static final float TEEN_GIRL_UNITS = 49.75f;

    public static TexturedModelData teen_girl() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(26, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(66, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -38f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(58, 23).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(186, 23).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(196, 23).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(162, 23).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(64, 23).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(188, 23).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(202, 23).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(168, 23).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(208, 23).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(214, 23).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(70, 23).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(78, 23).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(86, 23).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(94, 23).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(102, 23).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(110, 23).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(118, 23).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(126, 23).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(134, 23).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(142, 23).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(174, 23).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(180, 23).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(190, 23).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(192, 23).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(194, 23).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(150, 23).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, -5f, 0f, 11, 21, 2),
                        ModelTransform.pivot(0f, -6f, 5.6f));
        headD.addChild("clip", ModelPartBuilder.create()
                        .uv(156, 23).cuboid(2.5f, -10.4f, -6f, 2, 2, 1),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(46, 23).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(106, 0).cuboid(-4f, 0f, -2f, 8, 10, 4, new Dilation(0.4f))
                        .uv(28, 23).cuboid(-3.5f, 0f, 2f, 7, 3, 2, new Dilation(0.3f))
                        .uv(226, 0).cuboid(-4f, 10f, -2f, 8, 5, 4, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, -36f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(178, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 8, 3, new Dilation(0.3f)),
                        ModelTransform.of(-5.4f, -35.5f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(202, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 7, 3, new Dilation(0.18f)),
                        ModelTransform.pivot(0f, 7f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(190, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 8, 3, new Dilation(0.3f)),
                        ModelTransform.of(5.4f, -35.5f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(214, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 7, 3, new Dilation(0.18f)),
                        ModelTransform.pivot(0f, 7f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(130, 0).cuboid(-1.5f, 0f, -1.5f, 3, 10, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(-2f, -21f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(154, 0).cuboid(-1.5f, 0f, -1.5f, 3, 9, 3, new Dilation(0.36f))
                        .uv(0, 23).cuboid(-1.5f, 9f, -2.5f, 3, 2, 4, new Dilation(0.27f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(142, 0).cuboid(-1.5f, 0f, -1.5f, 3, 10, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(2f, -21f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(166, 0).cuboid(-1.5f, 0f, -1.5f, 3, 9, 3, new Dilation(0.36f))
                        .uv(14, 23).cuboid(-1.5f, 9f, -2.5f, 3, 2, 4, new Dilation(0.27f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        headD.addChild("ponytail", ModelPartBuilder.create(),
                        ModelTransform.pivot(6f, -20.5f, 0.5f));
        ModelPartData skirtD = bodyD.addChild("skirt", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 2f, 0f));
        skirtD.addChild("dress", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, 0f, 0f));
        bodyD.addChild("hood", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -12f, 0f));
        bodyD.addChild("scarf", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -12f, 0f));
        headD.addChild("hat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -12f, 0f));
        headD.addChild("hardhat", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -12f, 0f));
        headD.addChild("beanie", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -12f, 0f));
        return TexturedModelData.of(data, 256, 256);
    }

    /** teen_boy (1.6 bloques, mide 53.00 unidades). */
    public static final float TEEN_BOY_UNITS = 53.00f;

    public static TexturedModelData teen_boy() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -39f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(38, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(174, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(184, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(44, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(190, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(156, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(196, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(202, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(64, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(72, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(80, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(88, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(96, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(104, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(112, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(120, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(128, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(136, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(162, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(168, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(178, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(180, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(182, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(144, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("spikes", ModelPartBuilder.create()
                        .uv(14, 21).cuboid(-4f, -13.4f, -3f, 3, 2, 3, new Dilation(0.3f))
                        .uv(0, 21).cuboid(0f, -13.7f, -1f, 4, 2, 3, new Dilation(0.3f))
                        .uv(50, 21).cuboid(-5.4f, -10.5f, -5.9f, 6, 2, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(26, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(80, 0).cuboid(-4.5f, 0f, -2.25f, 9, 11, 4, new Dilation(0.5f))
                        .uv(202, 0).cuboid(-4.5f, 11f, -2.25f, 9, 4, 4, new Dilation(0.5f)),
                        ModelTransform.pivot(0f, -37f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(154, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 8, 3, new Dilation(0.4f)),
                        ModelTransform.of(-6f, -36.5f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(178, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 7, 3, new Dilation(0.24f)),
                        ModelTransform.pivot(0f, 7f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(166, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 8, 3, new Dilation(0.4f)),
                        ModelTransform.of(6f, -36.5f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(190, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 7, 3, new Dilation(0.24f)),
                        ModelTransform.pivot(0f, 7f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(106, 0).cuboid(-1.5f, 0f, -1.5f, 3, 10, 3, new Dilation(0.5f)),
                        ModelTransform.pivot(-2.25f, -22f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(118, 0).cuboid(-1.5f, 0f, -1.5f, 3, 10, 3, new Dilation(0.4f))
                        .uv(228, 0).cuboid(-1.5f, 10f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(130, 0).cuboid(-1.5f, 0f, -1.5f, 3, 10, 3, new Dilation(0.5f)),
                        ModelTransform.pivot(2.25f, -22f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(142, 0).cuboid(-1.5f, 0f, -1.5f, 3, 10, 3, new Dilation(0.4f))
                        .uv(242, 0).cuboid(-1.5f, 10f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
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

    /** adult_daughter (1.75 bloques, mide 61.75 unidades). */
    public static final float ADULT_DAUGHTER_UNITS = 61.75f;

    public static TexturedModelData adult_daughter() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(62, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(102, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(226, 26).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(206, 40).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(216, 40).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(182, 40).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(232, 26).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(208, 40).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(222, 40).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(188, 40).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(228, 40).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(234, 40).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(80, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(88, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(96, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(104, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(112, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(120, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(128, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(136, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(144, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(152, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(194, 40).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(200, 40).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(210, 40).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(212, 40).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(214, 40).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(160, 40).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(36, 0).cuboid(-5.5f, 0f, -0.6f, 11, 20, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(206, 0).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(66, 26).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(36, 26).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(166, 40).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(0, 40).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(114, 26).cuboid(-4.5f, 3.4f, -4.82f, 9, 4, 3, new Dilation(0.3f))
                        .uv(206, 26).cuboid(-4.3f, 4f, -5.22f, 4, 3, 1, new Dilation(0.25f))
                        .uv(216, 26).cuboid(0.3f, 4f, -5.22f, 4, 3, 1, new Dilation(0.25f))
                        .uv(20, 40).cuboid(-4f, 7.2f, -4.5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(40, 40).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(90, 26).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(186, 26).cuboid(-4.3f, 15.1f, 4.04f, 4, 4, 1, new Dilation(0.25f))
                        .uv(196, 26).cuboid(0.3f, 15.1f, 4.04f, 4, 4, 1, new Dilation(0.25f))
                        .uv(60, 40).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(174, 26).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("casual_skirt", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, 0f, -3.2f, 11, 19, 7, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 17f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(234, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.45f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(12, 26).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.27f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(0, 26).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.45f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(24, 26).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.27f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(142, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(174, 0).cuboid(-2f, 0f, -2f, 4, 13, 4, new Dilation(0.48f))
                        .uv(138, 26).cuboid(-2f, 13f, -3f, 4, 2, 5, new Dilation(0.36f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(158, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(190, 0).cuboid(-2f, 0f, -2f, 4, 13, 4, new Dilation(0.48f))
                        .uv(156, 26).cuboid(-2f, 13f, -3f, 4, 2, 5, new Dilation(0.36f)),
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

    /** adult_son (1.85 bloques, mide 64.00 unidades). */
    public static final float ADULT_SON_UNITS = 64.00f;

    public static TexturedModelData adult_son() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(162, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(30, 31).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(40, 31).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(6, 31).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(32, 31).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(46, 31).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(12, 31).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(52, 31).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(58, 31).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(174, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(182, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(190, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(206, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(214, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(222, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(230, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(238, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(246, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(18, 31).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(24, 31).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(34, 31).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(36, 31).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(38, 31).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(0, 31).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("spikes", ModelPartBuilder.create()
                        .uv(136, 21).cuboid(-4f, -13.4f, -3f, 4, 2, 3, new Dilation(0.3f))
                        .uv(122, 21).cuboid(0.5f, -13.7f, -1f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-1.75f, -2f, -1.75f, 3, 2, 3, new Dilation(0.25f))
                        .uv(144, 0).cuboid(-5.5f, 0f, -2.75f, 11, 10, 5, new Dilation(0.6f))
                        .uv(0, 21).cuboid(-4.75f, 10f, -2.5f, 9, 5, 5, new Dilation(0.5f))
                        .uv(92, 21).cuboid(-5f, 14.5f, -2.75f, 10, 1, 5, new Dilation(0.55f))
                        .uv(28, 21).cuboid(-4.75f, 15f, -2.5f, 9, 5, 5, new Dilation(0.55f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(176, 0).cuboid(-2f, -0.5f, -2f, 4, 11, 4, new Dilation(0.5f)),
                        ModelTransform.of(-7.4f, -47.5f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(208, 0).cuboid(-2f, 0.5f, -2f, 4, 10, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(192, 0).cuboid(-2f, -0.5f, -2f, 4, 11, 4, new Dilation(0.5f)),
                        ModelTransform.of(7.4f, -47.5f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(224, 0).cuboid(-2f, 0.5f, -2f, 4, 10, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.7f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-2f, 0f, -2f, 4, 13, 4, new Dilation(0.56f))
                        .uv(56, 21).cuboid(-2f, 13f, -3f, 4, 2, 5, new Dilation(0.42f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.7f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(128, 0).cuboid(-2f, 0f, -2f, 4, 13, 4, new Dilation(0.56f))
                        .uv(74, 21).cuboid(-2f, 13f, -3f, 4, 2, 5, new Dilation(0.42f)),
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
}
