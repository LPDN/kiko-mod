package com.jaciel.kiko.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;

/**
 * Modelos de Maru, Lumi y Nadi. Los genera tools/gen_girls.py (no editar a mano).
 * Tienen las mismas piezas que Kiko (cabeza, ojos, bocas, brazos con codo, piernas con rodilla...),
 * así que usan la misma clase KikoModel y todas sus animaciones y expresiones.
 */
@SuppressWarnings("unused")
public final class GirlModels {
    private GirlModels() {}

    /** Maru (1.8 bloques en el juego). */
    public static TexturedModelData maru() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(52, 33).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(64, 38).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(74, 38).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(40, 38).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(58, 33).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(66, 38).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(80, 38).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(46, 38).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(86, 38).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(92, 38).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(144, 33).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(152, 33).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(160, 33).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(168, 33).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(176, 33).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(184, 33).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(192, 33).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(200, 33).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(208, 33).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(216, 33).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(52, 38).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(58, 38).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(68, 38).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(70, 38).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(72, 38).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(240, 33).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("spikes", ModelPartBuilder.create()
                        .uv(226, 21).cuboid(-4f, -13f, -3f, 3, 2, 3, new Dilation(0.3f))
                        .uv(238, 21).cuboid(0f, -13.5f, -1f, 3, 2, 3, new Dilation(0.3f))
                        .uv(212, 21).cuboid(-1.5f, -13f, 2f, 4, 2, 3, new Dilation(0.3f))
                        .uv(0, 38).cuboid(-5.5f, -10.5f, -5.8f, 11, 1, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("goggles", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-5.6f, -11.2f, -5.6f, 11, 1, 11, new Dilation(0.15f))
                        .uv(224, 33).cuboid(-4f, -11.9f, -6.4f, 3, 2, 1, new Dilation(0.1f))
                        .uv(232, 33).cuboid(1f, -11.9f, -6.4f, 3, 2, 1, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(158, 0).cuboid(-4.75f, 0f, -2.5f, 9, 9, 5, new Dilation(0.6f))
                        .uv(74, 21).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.42f))
                        .uv(44, 21).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(24, 38).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(64, 33).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(122, 21).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(32, 33).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(42, 33).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(84, 33).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(104, 33).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(98, 21).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(12, 33).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(22, 33).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(124, 33).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(182, 21).cuboid(-5.2f, 13.8f, -2.8f, 10, 1, 5, new Dilation(0.8f))
                        .uv(0, 33).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(112, 0).cuboid(-4.8f, 14f, -4.3f, 10, 16, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(186, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(-6.9f, -47.5f, 0f, 0f, 0f, 0.1f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(210, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(198, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(6.9f, -47.5f, 0f, 0f, 0f, -0.1f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(222, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.85f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(134, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.65f))
                        .uv(146, 21).cuboid(-2f, 13f, -3f, 4, 2, 5, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.85f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(146, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.65f))
                        .uv(164, 21).cuboid(-2f, 13f, -3f, 4, 2, 5, new Dilation(0.35f)),
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

    /** Lumi (1.64 bloques en el juego). */
    public static TexturedModelData lumi() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(26, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(66, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(20, 46).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(254, 46).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(8, 50).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(230, 46).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(26, 46).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(0, 50).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(14, 50).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(236, 46).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(20, 50).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(26, 50).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(112, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(120, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(128, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(136, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(144, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(152, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(160, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(168, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(176, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(184, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(242, 46).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(248, 46).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(2, 50).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(4, 50).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(6, 50).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(192, 46).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("glasses", ModelPartBuilder.create()
                        .uv(226, 33).cuboid(-4.5f, -7.5f, -5.45f, 4, 5, 0)
                        .uv(234, 33).cuboid(0.5f, -7.5f, -5.45f, 4, 5, 0)
                        .uv(32, 50).cuboid(-0.5f, -6.5f, -5.45f, 1, 1, 0),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, 0f, -0.6f, 11, 31, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(166, 0).cuboid(-0.8f, 2f, -1.2f, 2, 15, 2, new Dilation(0.2f))
                        .uv(242, 33).cuboid(-1.3f, 17f, -1.7f, 3, 1, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(-5.6f, -4f, -1f));
        headD.addChild("clip", ModelPartBuilder.create()
                        .uv(198, 46).cuboid(2.5f, -10.4f, -6f, 2, 2, 1),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(222, 0).cuboid(-4.25f, 0f, -2.25f, 9, 9, 4, new Dilation(0.4f))
                        .uv(80, 33).cuboid(-3.75f, 9f, -1.75f, 8, 5, 4, new Dilation(0.3f))
                        .uv(24, 33).cuboid(-4.75f, 14f, -2.25f, 10, 6, 4, new Dilation(0.7f))
                        .uv(204, 46).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(32, 46).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(142, 33).cuboid(-4.5f, 3.4f, -4.965f, 9, 4, 3, new Dilation(0.3f))
                        .uv(0, 46).cuboid(-4.3f, 4f, -5.39f, 4, 3, 1, new Dilation(0.25f))
                        .uv(10, 46).cuboid(0.3f, 4f, -5.39f, 4, 3, 1, new Dilation(0.25f))
                        .uv(52, 46).cuboid(-4f, 7.2f, -4.625f, 8, 1, 2, new Dilation(0.2f))
                        .uv(72, 46).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(118, 33).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(206, 33).cuboid(-4.3f, 15.1f, 4.1675f, 4, 4, 1, new Dilation(0.25f))
                        .uv(216, 33).cuboid(0.3f, 15.1f, 4.1675f, 4, 4, 1, new Dilation(0.25f))
                        .uv(92, 46).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(52, 33).cuboid(-4.6f, 14.5f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(194, 33).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(220, 46).cuboid(-2f, -0.6f, -2.7f, 4, 1, 1, new Dilation(0.15f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("lumi_skirt", ModelPartBuilder.create()
                        .uv(106, 0).cuboid(-5.5f, 6f, -3.2f, 11, 14, 7, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(198, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.15f)),
                        ModelTransform.of(-6f, -47.5f, 0f, 0f, 0f, 0.06f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(0, 33).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(210, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.15f)),
                        ModelTransform.of(6f, -47.5f, 0f, 0f, 0f, -0.06f));
        ModelPartData leftForearmD = leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(12, 33).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        leftForearmD.addChild("book", ModelPartBuilder.create()
                        .uv(104, 33).cuboid(-1.3f, -2f, -4.2f, 3, 5, 4),
                        ModelTransform.pivot(0f, 8.5f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(142, 0).cuboid(-1.5f, 0f, -1.5f, 3, 14, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.4f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(174, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(166, 33).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(154, 0).cuboid(-1.5f, 0f, -1.5f, 3, 14, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(2.4f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(186, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(180, 33).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
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

    /** Nadi (1.8 bloques en el juego). */
    public static TexturedModelData nadi() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(214, 35).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(186, 43).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(196, 43).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(162, 43).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(220, 35).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(188, 43).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(202, 43).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(168, 43).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(208, 43).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(214, 43).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(60, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(68, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(76, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(84, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(92, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(100, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(108, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(116, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(124, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(132, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(174, 43).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(180, 43).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(190, 43).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(192, 43).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(194, 43).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(140, 43).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("nadi_hat", ModelPartBuilder.create()
                        .uv(148, 0).cuboid(-8f, -11.2f, -8f, 16, 1, 16)
                        .uv(52, 21).cuboid(-5f, -14.2f, -5f, 10, 3, 10, new Dilation(0.2f))
                        .uv(106, 35).cuboid(4.4f, -15.5f, -1f, 1, 4, 2),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(92, 21).cuboid(-5.5f, 0f, -0.6f, 11, 11, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(140, 0).cuboid(-1f, 1f, -0.6f, 2, 16, 2, new Dilation(0.25f))
                        .uv(182, 35).cuboid(-1.5f, 16f, -1.1f, 3, 1, 3),
                        ModelTransform.pivot(0f, 3f, 7f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(192, 21).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(162, 21).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(146, 43).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(226, 35).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(24, 35).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(194, 35).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(204, 35).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(0, 43).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(20, 43).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(216, 21).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(124, 35).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(134, 35).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(40, 43).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(112, 35).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(80, 0).cuboid(-4.6f, 0f, -2.7f, 9, 15, 5, new Dilation(0.75f))
                        .uv(48, 35).cuboid(-5f, 14f, -2.8f, 10, 1, 5, new Dilation(0.85f))
                        .uv(144, 35).cuboid(-5.6f, 14.5f, -2.2f, 2, 3, 2, new Dilation(0.3f))
                        .uv(152, 35).cuboid(3.6f, 14.5f, -2.2f, 2, 3, 2, new Dilation(0.3f))
                        .uv(0, 35).cuboid(-3f, -0.5f, -3f, 6, 2, 6, new Dilation(0.3f))
                        .uv(142, 21).cuboid(-3.5f, 4f, 3.2f, 7, 9, 3, new Dilation(0.2f))
                        .uv(160, 35).cuboid(-4.5f, 2f, 3.6f, 9, 2, 2, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(28, 21).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.55f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(40, 21).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.55f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(130, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(108, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.65f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(212, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.5f))
                        .uv(78, 35).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.65f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(224, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.5f))
                        .uv(92, 35).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
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

    /** Rina (1.8 bloques en el juego). */
    public static TexturedModelData rina() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(28, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(68, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(0, 46).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(230, 46).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(240, 46).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(200, 46).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(6, 46).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(232, 46).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(246, 46).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(206, 46).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(0, 50).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(6, 50).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(92, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(100, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(108, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(116, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(124, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(132, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(140, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(148, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(156, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(164, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(212, 46).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(218, 46).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(234, 46).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(236, 46).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(238, 46).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(172, 46).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("rina_cap", ModelPartBuilder.create()
                        .uv(166, 33).cuboid(-3.5f, -12.6f, -5.6f, 7, 2, 3, new Dilation(0.15f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(226, 0).cuboid(-5.5f, 0f, -0.6f, 11, 11, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(78, 33).cuboid(-1.5f, -2f, -1.4f, 3, 6, 3, new Dilation(0.1f))
                        .uv(164, 0).cuboid(-1.5f, 4f, -2f, 3, 13, 2, new Dilation(0.2f))
                        .uv(218, 33).cuboid(-2f, 3f, -1.8f, 4, 1, 3),
                        ModelTransform.pivot(0f, 6f, 5f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(174, 0).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(54, 33).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(24, 33).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(184, 46).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(12, 46).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(114, 33).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(232, 33).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(242, 33).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(32, 46).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(52, 46).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(90, 33).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(198, 33).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(208, 33).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(72, 46).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(186, 33).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(0, 0).cuboid(-4.7f, 0f, -2.8f, 9, 28, 5, new Dilation(0.75f))
                        .uv(224, 46).cuboid(-4.2f, 5f, -3.9f, 2, 1, 1)
                        .uv(178, 46).cuboid(2f, 16f, -3.9f, 2, 2, 1),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(202, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.55f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(0, 33).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(214, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.55f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(12, 33).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(108, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.85f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(140, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(138, 33).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.85f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(152, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(152, 33).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 14f, 0f));
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

    /** Tami (1.8 bloques en el juego). */
    public static TexturedModelData tami() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(118, 34).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(86, 40).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(96, 40).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(62, 40).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(124, 34).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(88, 40).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(102, 40).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(68, 40).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(108, 40).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(114, 40).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(210, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(218, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(226, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(234, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(242, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(0, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(8, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(16, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(24, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(32, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(74, 40).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(80, 40).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(90, 40).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(92, 40).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(94, 40).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(40, 40).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("tami_hat", ModelPartBuilder.create()
                        .uv(24, 21).cuboid(-5f, -12.4f, -5f, 10, 2, 10, new Dilation(0.4f))
                        .uv(158, 0).cuboid(-5.5f, -16.4f, -5.5f, 11, 4, 11, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(122, 21).cuboid(-5.5f, 0f, -0.6f, 11, 6, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-2f, -2f, -0.2f, 4, 4, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, -5f, 7f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(202, 0).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(94, 21).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(64, 21).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(46, 40).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(130, 34).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(196, 21).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(90, 34).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(100, 34).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(150, 34).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(170, 34).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(148, 21).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(70, 34).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(80, 34).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(190, 34).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(58, 34).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(172, 21).cuboid(-3f, -0.5f, -3f, 6, 2, 6, new Dilation(0.35f))
                        .uv(112, 0).cuboid(-4.8f, 14f, -4.2f, 10, 16, 1, new Dilation(0.2f))
                        .uv(0, 34).cuboid(-5.2f, 13.8f, -2.8f, 10, 1, 5, new Dilation(0.8f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(230, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.55f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.08f));
        ModelPartData rightForearmD = rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.15f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(242, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.55f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(12, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.15f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        rightForearmD.addChild("ladle", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-0.5f, -1f, -3.5f, 1, 8, 1)
                        .uv(110, 34).cuboid(-1f, 6.5f, -4f, 2, 2, 2),
                        ModelTransform.pivot(0f, 8.5f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.85f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(134, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(30, 34).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.85f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(146, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(44, 34).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 14f, 0f));
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

    /** Maru_casual (1.8 bloques en el juego). */
    public static TexturedModelData maru_casual() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(222, 30).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(232, 30).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(198, 30).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(226, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(224, 30).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(238, 30).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(204, 30).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(244, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(250, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(60, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(68, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(76, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(84, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(92, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(100, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(108, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(116, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(124, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(132, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(210, 30).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(216, 30).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(226, 30).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(228, 30).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(230, 30).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(140, 30).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("spikes", ModelPartBuilder.create()
                        .uv(144, 21).cuboid(-4f, -13f, -3f, 3, 2, 3, new Dilation(0.3f))
                        .uv(156, 21).cuboid(0f, -13.5f, -1f, 3, 2, 3, new Dilation(0.3f))
                        .uv(130, 21).cuboid(-1.5f, -13f, 2f, 4, 2, 3, new Dilation(0.3f))
                        .uv(146, 30).cuboid(-5.5f, -10.5f, -5.8f, 11, 1, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(136, 0).cuboid(-4.75f, 0f, -2.5f, 9, 9, 5, new Dilation(0.6f))
                        .uv(0, 21).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.42f))
                        .uv(212, 0).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(170, 30).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(232, 21).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(48, 21).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(200, 21).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(210, 21).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(0, 30).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(20, 30).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(24, 21).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(180, 21).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(190, 21).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(40, 30).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(168, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(72, 21).cuboid(-5.2f, 13.8f, -2.8f, 10, 1, 5, new Dilation(0.8f))
                        .uv(186, 30).cuboid(-2.5f, -0.4f, -2.9f, 5, 1, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(164, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(-6.9f, -47.5f, 0f, 0f, 0f, 0.1f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(188, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(176, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(6.9f, -47.5f, 0f, 0f, 0f, -0.1f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(200, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.85f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.65f))
                        .uv(102, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.85f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.65f))
                        .uv(116, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
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

    /** Maru_wedding (1.8 bloques en el juego). */
    public static TexturedModelData maru_wedding() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(68, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(108, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(76, 39).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(102, 44).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(112, 44).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(46, 44).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(82, 39).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(104, 44).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(118, 44).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(52, 44).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(124, 44).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(130, 44).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(168, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(176, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(184, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(192, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(200, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(208, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(216, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(224, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(232, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(240, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(58, 44).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(64, 44).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(106, 44).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(108, 44).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(110, 44).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(0, 44).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("spikes", ModelPartBuilder.create()
                        .uv(0, 39).cuboid(-4f, -13f, -3f, 3, 2, 3, new Dilation(0.3f))
                        .uv(12, 39).cuboid(0f, -13.5f, -1f, 3, 2, 3, new Dilation(0.3f))
                        .uv(234, 25).cuboid(-1.5f, -13f, 2f, 4, 2, 3, new Dilation(0.3f))
                        .uv(6, 44).cuboid(-5.5f, -10.5f, -5.8f, 11, 1, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(0, 25).cuboid(-4.75f, 0f, -2.5f, 9, 9, 5, new Dilation(0.6f))
                        .uv(106, 25).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.42f))
                        .uv(76, 25).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(30, 44).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(88, 39).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(154, 25).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(56, 39).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(66, 39).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(108, 39).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(128, 39).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(130, 25).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(36, 39).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(46, 39).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(148, 39).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(24, 39).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(178, 25).cuboid(-4.4f, 13.6f, -2.6f, 9, 1, 5, new Dilation(0.7f))
                        .uv(248, 39).cuboid(-1.5f, 13f, 2.9f, 3, 2, 1, new Dilation(0.2f))
                        .uv(148, 0).cuboid(-5.5f, 14f, -3.6f, 11, 12, 8, new Dilation(0.5f))
                        .uv(0, 0).cuboid(-6.5f, 26f, -4.6f, 13, 15, 10, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(28, 25).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.85f)),
                        ModelTransform.of(-6.9f, -47.5f, 0f, 0f, 0f, 0.1f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(52, 25).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(40, 25).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.85f)),
                        ModelTransform.of(6.9f, -47.5f, 0f, 0f, 0f, -0.1f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(64, 25).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(186, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(218, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.4f))
                        .uv(206, 25).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(202, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(230, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.4f))
                        .uv(220, 25).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("veil", ModelPartBuilder.create()
                        .uv(70, 44).cuboid(-4.5f, -12.3f, -5.6f, 1, 1, 1, new Dilation(0.3f))
                        .uv(74, 44).cuboid(-2f, -12.3f, -5.9f, 1, 1, 1, new Dilation(0.3f))
                        .uv(78, 44).cuboid(0.5f, -12.3f, -5.9f, 1, 1, 1, new Dilation(0.3f))
                        .uv(82, 44).cuboid(3f, -12.3f, -5.6f, 1, 1, 1, new Dilation(0.3f))
                        .uv(86, 44).cuboid(-5.6f, -12.3f, -3f, 1, 1, 1, new Dilation(0.3f))
                        .uv(90, 44).cuboid(4.6f, -12.3f, -3f, 1, 1, 1, new Dilation(0.3f))
                        .uv(94, 44).cuboid(-5.6f, -12.3f, 0f, 1, 1, 1, new Dilation(0.3f))
                        .uv(98, 44).cuboid(4.6f, -12.3f, 0f, 1, 1, 1, new Dilation(0.3f))
                        .uv(46, 0).cuboid(-5.5f, -11f, 5.9f, 11, 25, 0),
                        ModelTransform.pivot(0f, 0f, 0f));
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

    /** Lumi_casual (1.64 bloques en el juego). */
    public static TexturedModelData lumi_casual() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(26, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(66, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(48, 46).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(40, 51).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(50, 51).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(16, 51).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(54, 46).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(42, 51).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(56, 51).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(22, 51).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(62, 51).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(68, 51).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(154, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(162, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(170, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(178, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(186, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(194, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(202, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(210, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(218, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(226, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(28, 51).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(34, 51).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(44, 51).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(46, 51).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(48, 51).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(234, 46).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("glasses", ModelPartBuilder.create()
                        .uv(0, 46).cuboid(-4.5f, -7.5f, -5.45f, 4, 5, 0)
                        .uv(8, 46).cuboid(0.5f, -7.5f, -5.45f, 4, 5, 0)
                        .uv(74, 51).cuboid(-0.5f, -6.5f, -5.45f, 1, 1, 0),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, 0f, -0.6f, 11, 31, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(166, 0).cuboid(-0.8f, 2f, -1.2f, 2, 15, 2, new Dilation(0.2f))
                        .uv(16, 46).cuboid(-1.3f, 17f, -1.7f, 3, 1, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(-5.6f, -4f, -1f));
        headD.addChild("clip", ModelPartBuilder.create()
                        .uv(240, 46).cuboid(2.5f, -10.4f, -6f, 2, 2, 1),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(222, 0).cuboid(-4.25f, 0f, -2.25f, 9, 9, 4, new Dilation(0.4f))
                        .uv(52, 33).cuboid(-3.75f, 9f, -1.75f, 8, 5, 4, new Dilation(0.3f))
                        .uv(24, 33).cuboid(-4.75f, 14f, -2.25f, 10, 6, 4, new Dilation(0.7f))
                        .uv(0, 51).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(60, 46).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(144, 33).cuboid(-4.5f, 3.4f, -4.965f, 9, 4, 3, new Dilation(0.3f))
                        .uv(28, 46).cuboid(-4.3f, 4f, -5.39f, 4, 3, 1, new Dilation(0.25f))
                        .uv(38, 46).cuboid(0.3f, 4f, -5.39f, 4, 3, 1, new Dilation(0.25f))
                        .uv(80, 46).cuboid(-4f, 7.2f, -4.625f, 8, 1, 2, new Dilation(0.2f))
                        .uv(100, 46).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(120, 33).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(230, 33).cuboid(-4.3f, 15.1f, 4.1675f, 4, 4, 1, new Dilation(0.25f))
                        .uv(240, 33).cuboid(0.3f, 15.1f, 4.1675f, 4, 4, 1, new Dilation(0.25f))
                        .uv(120, 46).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(218, 33).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(168, 33).cuboid(-4f, 0f, 2f, 8, 4, 3, new Dilation(0.4f))
                        .uv(140, 46).cuboid(-3f, 11.5f, -3.3f, 6, 2, 1, new Dilation(0.1f))
                        .uv(246, 46).cuboid(-1.5f, 2f, -3.6f, 3, 3, 0)
                        .uv(90, 33).cuboid(-4.8f, 14.2f, -2.6f, 10, 3, 5, new Dilation(0.55f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("casual_skirt", ModelPartBuilder.create()
                        .uv(106, 0).cuboid(-5.5f, 0f, -3.2f, 11, 12, 7, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 17f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(198, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.3f)),
                        ModelTransform.of(-6f, -47.5f, 0f, 0f, 0f, 0.06f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(0, 33).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.25f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(210, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.3f)),
                        ModelTransform.of(6f, -47.5f, 0f, 0f, 0f, -0.06f));
        ModelPartData leftForearmD = leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(12, 33).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.25f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        leftForearmD.addChild("book", ModelPartBuilder.create()
                        .uv(76, 33).cuboid(-1.3f, -2f, -4.2f, 3, 5, 4),
                        ModelTransform.pivot(0f, 8.5f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(142, 0).cuboid(-1.5f, 0f, -1.5f, 3, 14, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.4f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(174, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(190, 33).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(154, 0).cuboid(-1.5f, 0f, -1.5f, 3, 14, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(2.4f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(186, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(204, 33).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
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

    /** Lumi_wedding (1.64 bloques en el juego). */
    public static TexturedModelData lumi_wedding() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(94, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(134, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(68, 49).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(78, 54).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(88, 54).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(22, 54).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(74, 49).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(80, 54).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(94, 54).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(28, 54).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(100, 54).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(106, 54).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(160, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(168, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(176, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(184, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(192, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(200, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(208, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(216, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(224, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(232, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(34, 54).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(40, 54).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(82, 54).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(84, 54).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(86, 54).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(248, 49).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("glasses", ModelPartBuilder.create()
                        .uv(20, 49).cuboid(-4.5f, -7.5f, -5.45f, 4, 5, 0)
                        .uv(28, 49).cuboid(0.5f, -7.5f, -5.45f, 4, 5, 0)
                        .uv(112, 54).cuboid(-0.5f, -6.5f, -5.45f, 1, 1, 0),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, 0f, -0.6f, 11, 31, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(236, 0).cuboid(-0.8f, 2f, -1.2f, 2, 15, 2, new Dilation(0.2f))
                        .uv(36, 49).cuboid(-1.3f, 17f, -1.7f, 3, 1, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(-5.6f, -4f, -1f));
        headD.addChild("clip", ModelPartBuilder.create()
                        .uv(0, 54).cuboid(2.5f, -10.4f, -6f, 2, 2, 1),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(36, 33).cuboid(-4.25f, 0f, -2.25f, 9, 9, 4, new Dilation(0.4f))
                        .uv(114, 33).cuboid(-3.75f, 9f, -1.75f, 8, 5, 4, new Dilation(0.3f))
                        .uv(86, 33).cuboid(-4.75f, 14f, -2.25f, 10, 6, 4, new Dilation(0.7f))
                        .uv(6, 54).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(80, 49).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(162, 33).cuboid(-4.5f, 3.4f, -4.965f, 9, 4, 3, new Dilation(0.3f))
                        .uv(48, 49).cuboid(-4.3f, 4f, -5.39f, 4, 3, 1, new Dilation(0.25f))
                        .uv(58, 49).cuboid(0.3f, 4f, -5.39f, 4, 3, 1, new Dilation(0.25f))
                        .uv(100, 49).cuboid(-4f, 7.2f, -4.625f, 8, 1, 2, new Dilation(0.2f))
                        .uv(120, 49).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(138, 33).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(0, 49).cuboid(-4.3f, 15.1f, 4.1675f, 4, 4, 1, new Dilation(0.25f))
                        .uv(10, 49).cuboid(0.3f, 15.1f, 4.1675f, 4, 4, 1, new Dilation(0.25f))
                        .uv(140, 49).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(242, 33).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(186, 33).cuboid(-4.4f, 13.6f, -2.6f, 9, 1, 5, new Dilation(0.7f))
                        .uv(240, 49).cuboid(-1.5f, 13f, 2.9f, 3, 2, 1, new Dilation(0.2f))
                        .uv(174, 0).cuboid(-5.5f, 14f, -3.6f, 11, 12, 8, new Dilation(0.5f))
                        .uv(26, 0).cuboid(-6.5f, 26f, -4.6f, 13, 15, 10, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(12, 33).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.85f)),
                        ModelTransform.of(-6f, -47.5f, 0f, 0f, 0f, 0.06f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(62, 33).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(24, 33).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.85f)),
                        ModelTransform.of(6f, -47.5f, 0f, 0f, 0f, -0.06f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(74, 33).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(212, 0).cuboid(-1.5f, 0f, -1.5f, 3, 14, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.4f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(244, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.4f))
                        .uv(214, 33).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(224, 0).cuboid(-1.5f, 0f, -1.5f, 3, 14, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(2.4f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(0, 33).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.4f))
                        .uv(228, 33).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("veil", ModelPartBuilder.create()
                        .uv(46, 54).cuboid(-4.5f, -12.3f, -5.6f, 1, 1, 1, new Dilation(0.3f))
                        .uv(50, 54).cuboid(-2f, -12.3f, -5.9f, 1, 1, 1, new Dilation(0.3f))
                        .uv(54, 54).cuboid(0.5f, -12.3f, -5.9f, 1, 1, 1, new Dilation(0.3f))
                        .uv(58, 54).cuboid(3f, -12.3f, -5.6f, 1, 1, 1, new Dilation(0.3f))
                        .uv(62, 54).cuboid(-5.6f, -12.3f, -3f, 1, 1, 1, new Dilation(0.3f))
                        .uv(66, 54).cuboid(4.6f, -12.3f, -3f, 1, 1, 1, new Dilation(0.3f))
                        .uv(70, 54).cuboid(-5.6f, -12.3f, 0f, 1, 1, 1, new Dilation(0.3f))
                        .uv(74, 54).cuboid(4.6f, -12.3f, 0f, 1, 1, 1, new Dilation(0.3f))
                        .uv(72, 0).cuboid(-5.5f, -11f, 7.75f, 11, 25, 0),
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

    /** Nadi_casual (1.8 bloques en el juego). */
    public static TexturedModelData nadi_casual() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(0, 35).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(218, 35).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(228, 35).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(194, 35).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(6, 35).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(220, 35).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(234, 35).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(200, 35).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(240, 35).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(246, 35).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(92, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(100, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(108, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(116, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(124, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(132, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(140, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(148, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(156, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(164, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(206, 35).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(212, 35).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(222, 35).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(224, 35).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(226, 35).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(172, 35).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(12, 21).cuboid(-5.5f, 0f, -0.6f, 11, 11, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(152, 0).cuboid(-1f, 1f, -0.6f, 2, 16, 2, new Dilation(0.25f))
                        .uv(224, 21).cuboid(-1.5f, 16f, -1.1f, 3, 1, 3),
                        ModelTransform.pivot(0f, 3f, 7f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(212, 0).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(92, 21).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(62, 21).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(178, 35).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(12, 35).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(140, 21).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(236, 21).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(246, 21).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(32, 35).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(52, 35).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(116, 21).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(204, 21).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(214, 21).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(72, 35).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(192, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(160, 0).cuboid(-4.6f, 0f, -2.7f, 9, 11, 5, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("casual_skirt", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-6f, 0f, -3.7f, 12, 13, 8, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 17f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(240, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.55f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(38, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.55f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(50, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(120, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.65f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(188, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.5f))
                        .uv(164, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(136, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.65f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(200, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.5f))
                        .uv(178, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
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

    /** Nadi_wedding (1.8 bloques en el juego). */
    public static TexturedModelData nadi_wedding() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(68, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(108, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(78, 39).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(86, 45).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(96, 45).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(30, 45).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(84, 39).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(88, 45).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(102, 45).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(36, 45).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(108, 45).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(114, 45).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(170, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(178, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(186, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(194, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(202, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(210, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(218, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(226, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(234, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(242, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(42, 45).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(48, 45).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(90, 45).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(92, 45).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(94, 45).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(8, 45).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(52, 25).cuboid(-5.5f, 0f, -0.6f, 11, 11, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(218, 0).cuboid(-1f, 1f, -0.6f, 2, 16, 2, new Dilation(0.25f))
                        .uv(46, 39).cuboid(-1.5f, 16f, -1.1f, 3, 1, 3),
                        ModelTransform.pivot(0f, 3f, 7f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(0, 25).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(132, 25).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(102, 25).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(14, 45).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(90, 39).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(180, 25).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(58, 39).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(68, 39).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(110, 39).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(130, 39).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(156, 25).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(26, 39).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(36, 39).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(150, 39).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(14, 39).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(204, 25).cuboid(-4.4f, 13.6f, -2.6f, 9, 1, 5, new Dilation(0.7f))
                        .uv(0, 45).cuboid(-1.5f, 13f, 2.9f, 3, 2, 1, new Dilation(0.2f))
                        .uv(148, 0).cuboid(-5.5f, 14f, -3.6f, 11, 12, 8, new Dilation(0.5f))
                        .uv(0, 0).cuboid(-6.5f, 26f, -4.6f, 13, 15, 10, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(28, 25).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.85f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(78, 25).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(40, 25).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.85f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(90, 25).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(186, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(226, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.4f))
                        .uv(232, 25).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(202, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(238, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.4f))
                        .uv(0, 39).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("veil", ModelPartBuilder.create()
                        .uv(54, 45).cuboid(-4.5f, -12.3f, -5.6f, 1, 1, 1, new Dilation(0.3f))
                        .uv(58, 45).cuboid(-2f, -12.3f, -5.9f, 1, 1, 1, new Dilation(0.3f))
                        .uv(62, 45).cuboid(0.5f, -12.3f, -5.9f, 1, 1, 1, new Dilation(0.3f))
                        .uv(66, 45).cuboid(3f, -12.3f, -5.6f, 1, 1, 1, new Dilation(0.3f))
                        .uv(70, 45).cuboid(-5.6f, -12.3f, -3f, 1, 1, 1, new Dilation(0.3f))
                        .uv(74, 45).cuboid(4.6f, -12.3f, -3f, 1, 1, 1, new Dilation(0.3f))
                        .uv(78, 45).cuboid(-5.6f, -12.3f, 0f, 1, 1, 1, new Dilation(0.3f))
                        .uv(82, 45).cuboid(4.6f, -12.3f, 0f, 1, 1, 1, new Dilation(0.3f))
                        .uv(46, 0).cuboid(-5.5f, -11f, 7.75f, 11, 25, 0),
                        ModelTransform.pivot(0f, 0f, 0f));
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

    /** Rina_casual (1.8 bloques en el juego). */
    public static TexturedModelData rina_casual() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(36, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(76, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(34, 43).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(6, 47).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(16, 47).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(238, 43).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(40, 43).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(8, 47).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(22, 47).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(244, 43).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(28, 47).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(34, 47).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(126, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(134, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(142, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(150, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(158, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(166, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(174, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(182, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(190, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(198, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(250, 43).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(0, 47).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(10, 47).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(12, 47).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(14, 47).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(206, 43).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(0, 30).cuboid(-5.5f, 0f, -0.6f, 11, 11, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(104, 30).cuboid(-1.5f, -2f, -1.4f, 3, 6, 3, new Dilation(0.1f))
                        .uv(172, 0).cuboid(-1.5f, 4f, -2f, 3, 13, 2, new Dilation(0.2f))
                        .uv(0, 43).cuboid(-2f, 3f, -1.8f, 4, 1, 3),
                        ModelTransform.pivot(0f, 6f, 5f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(182, 0).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(80, 30).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(50, 30).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(212, 43).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(46, 43).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(168, 30).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(14, 43).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(24, 43).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(66, 43).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(86, 43).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(144, 30).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(232, 30).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(242, 30).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(106, 43).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(220, 30).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(228, 43).cuboid(-2f, -0.6f, -2.7f, 4, 1, 1, new Dilation(0.15f))
                        .uv(116, 30).cuboid(-4.6f, 13.5f, -2.5f, 9, 3, 5, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("casual_skirt", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, 0f, -3.2f, 11, 23, 7, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 17f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(210, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.45f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(26, 30).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(222, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.45f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(38, 30).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(116, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(148, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(192, 30).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(132, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(160, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(206, 30).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
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

    /** Rina_wedding (1.8 bloques en el juego). */
    public static TexturedModelData rina_wedding() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(68, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(108, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(94, 39).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(102, 45).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(112, 45).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(46, 45).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(100, 39).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(104, 45).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(118, 45).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(52, 45).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(124, 45).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(130, 45).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(186, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(194, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(202, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(210, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(218, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(226, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(234, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(242, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(0, 45).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(8, 45).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(58, 45).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(64, 45).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(106, 45).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(108, 45).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(110, 45).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(24, 45).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(52, 25).cuboid(-5.5f, 0f, -0.6f, 11, 11, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(156, 25).cuboid(-1.5f, -2f, -1.4f, 3, 6, 3, new Dilation(0.1f))
                        .uv(242, 0).cuboid(-1.5f, 4f, -2f, 3, 13, 2, new Dilation(0.2f))
                        .uv(60, 39).cuboid(-2f, 3f, -1.8f, 4, 1, 3),
                        ModelTransform.pivot(0f, 6f, 5f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(0, 25).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(132, 25).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(102, 25).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(30, 45).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(106, 39).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(192, 25).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(74, 39).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(84, 39).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(126, 39).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(146, 39).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(168, 25).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(40, 39).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(50, 39).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(166, 39).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(28, 39).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(216, 25).cuboid(-4.4f, 13.6f, -2.6f, 9, 1, 5, new Dilation(0.7f))
                        .uv(16, 45).cuboid(-1.5f, 13f, 2.9f, 3, 2, 1, new Dilation(0.2f))
                        .uv(148, 0).cuboid(-5.5f, 14f, -3.6f, 11, 12, 8, new Dilation(0.5f))
                        .uv(0, 0).cuboid(-6.5f, 26f, -4.6f, 13, 15, 10, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(28, 25).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.85f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(78, 25).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(40, 25).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.85f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(90, 25).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(186, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(218, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.4f))
                        .uv(0, 39).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(202, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(230, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.4f))
                        .uv(14, 39).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("veil", ModelPartBuilder.create()
                        .uv(70, 45).cuboid(-4.5f, -12.3f, -5.6f, 1, 1, 1, new Dilation(0.3f))
                        .uv(74, 45).cuboid(-2f, -12.3f, -5.9f, 1, 1, 1, new Dilation(0.3f))
                        .uv(78, 45).cuboid(0.5f, -12.3f, -5.9f, 1, 1, 1, new Dilation(0.3f))
                        .uv(82, 45).cuboid(3f, -12.3f, -5.6f, 1, 1, 1, new Dilation(0.3f))
                        .uv(86, 45).cuboid(-5.6f, -12.3f, -3f, 1, 1, 1, new Dilation(0.3f))
                        .uv(90, 45).cuboid(4.6f, -12.3f, -3f, 1, 1, 1, new Dilation(0.3f))
                        .uv(94, 45).cuboid(-5.6f, -12.3f, 0f, 1, 1, 1, new Dilation(0.3f))
                        .uv(98, 45).cuboid(4.6f, -12.3f, 0f, 1, 1, 1, new Dilation(0.3f))
                        .uv(46, 0).cuboid(-5.5f, -11f, 7.75f, 11, 25, 0),
                        ModelTransform.pivot(0f, 0f, 0f));
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

    /** Tami_casual (1.8 bloques en el juego). */
    public static TexturedModelData tami_casual() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(222, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(186, 32).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(196, 32).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(162, 32).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(228, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(188, 32).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(202, 32).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(168, 32).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(208, 32).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(214, 32).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(60, 32).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(68, 32).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(76, 32).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(84, 32).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(92, 32).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(100, 32).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(108, 32).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(116, 32).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(124, 32).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(132, 32).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(174, 32).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(180, 32).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(190, 32).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(192, 32).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(194, 32).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(140, 32).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(54, 21).cuboid(-5.5f, 0f, -0.6f, 11, 6, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(128, 21).cuboid(-2f, -2f, -0.2f, 4, 4, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, -5f, 7f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(172, 0).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(30, 21).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(0, 21).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(146, 32).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(234, 21).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(104, 21).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(202, 21).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(212, 21).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(0, 32).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(20, 32).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(80, 21).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(182, 21).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(192, 21).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(40, 32).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(170, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("casual_skirt", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-5.5f, 0f, -3.2f, 11, 12, 7, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 17f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(200, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(224, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.15f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(212, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(236, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.15f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(116, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.85f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(148, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(142, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(132, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.85f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(160, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(156, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 14f, 0f));
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

    /** Tami_wedding (1.8 bloques en el juego). */
    public static TexturedModelData tami_wedding() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(68, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(108, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(80, 39).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(86, 45).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(96, 45).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(30, 45).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(86, 39).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(88, 45).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(102, 45).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(36, 45).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(108, 45).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(114, 45).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(172, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(180, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(188, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(196, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(204, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(212, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(220, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(228, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(236, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(244, 39).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(42, 45).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(48, 45).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(90, 45).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(92, 45).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(94, 45).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(8, 45).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(130, 25).cuboid(-5.5f, 0f, -0.6f, 11, 6, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(204, 25).cuboid(-2f, -2f, -0.2f, 4, 4, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, -5f, 7f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(0, 25).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(106, 25).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(76, 25).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(14, 45).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(92, 39).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(180, 25).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(60, 39).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(70, 39).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(112, 39).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(132, 39).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(156, 25).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(40, 39).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(50, 39).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(152, 39).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(28, 39).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(218, 25).cuboid(-4.4f, 13.6f, -2.6f, 9, 1, 5, new Dilation(0.7f))
                        .uv(0, 45).cuboid(-1.5f, 13f, 2.9f, 3, 2, 1, new Dilation(0.2f))
                        .uv(148, 0).cuboid(-5.5f, 14f, -3.6f, 11, 12, 8, new Dilation(0.5f))
                        .uv(0, 0).cuboid(-6.5f, 26f, -4.6f, 13, 15, 10, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(28, 25).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.85f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(52, 25).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(40, 25).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.85f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(64, 25).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(186, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(218, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.4f))
                        .uv(0, 39).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(202, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(230, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.4f))
                        .uv(14, 39).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("veil", ModelPartBuilder.create()
                        .uv(54, 45).cuboid(-4.5f, -12.3f, -5.6f, 1, 1, 1, new Dilation(0.3f))
                        .uv(58, 45).cuboid(-2f, -12.3f, -5.9f, 1, 1, 1, new Dilation(0.3f))
                        .uv(62, 45).cuboid(0.5f, -12.3f, -5.9f, 1, 1, 1, new Dilation(0.3f))
                        .uv(66, 45).cuboid(3f, -12.3f, -5.6f, 1, 1, 1, new Dilation(0.3f))
                        .uv(70, 45).cuboid(-5.6f, -12.3f, -3f, 1, 1, 1, new Dilation(0.3f))
                        .uv(74, 45).cuboid(4.6f, -12.3f, -3f, 1, 1, 1, new Dilation(0.3f))
                        .uv(78, 45).cuboid(-5.6f, -12.3f, 0f, 1, 1, 1, new Dilation(0.3f))
                        .uv(82, 45).cuboid(4.6f, -12.3f, 0f, 1, 1, 1, new Dilation(0.3f))
                        .uv(46, 0).cuboid(-5.5f, -11f, 7.75f, 11, 25, 0),
                        ModelTransform.pivot(0f, 0f, 0f));
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
