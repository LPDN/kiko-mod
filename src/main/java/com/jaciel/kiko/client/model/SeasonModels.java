package com.jaciel.kiko.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;

/**
 * Ropa de verano e invierno de las chicas (v3.3). La genera tools/gen_guys_outfits.py (no editar a mano).
 */
@SuppressWarnings("unused")
public final class SeasonModels {
    private SeasonModels() {}

    /** maru_summer (1.8 bloques, 61.75 unidades). */
    public static TexturedModelData maru_summer() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(80, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(104, 37).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(108, 43).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(118, 43).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(84, 43).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(110, 37).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(110, 43).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(124, 43).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(90, 43).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(130, 43).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(136, 43).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(204, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(212, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(220, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(228, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(236, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(244, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(0, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(8, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(16, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(24, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(96, 43).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(102, 43).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(112, 43).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(114, 43).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(116, 43).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(32, 43).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("spikes", ModelPartBuilder.create()
                        .uv(28, 37).cuboid(-4f, -13f, -3f, 3, 2, 3, new Dilation(0.3f))
                        .uv(40, 37).cuboid(0f, -13.5f, -1f, 3, 2, 3, new Dilation(0.3f))
                        .uv(14, 37).cuboid(-1.5f, -13f, 2f, 4, 2, 3, new Dilation(0.3f))
                        .uv(44, 43).cuboid(-5.5f, -10.5f, -5.8f, 11, 1, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(0, 23).cuboid(-4.75f, 0f, -2.5f, 9, 9, 5, new Dilation(0.6f))
                        .uv(142, 23).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.42f))
                        .uv(112, 23).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(68, 43).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(124, 37).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(190, 23).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(84, 37).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(94, 37).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(144, 37).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(164, 37).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(166, 23).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(64, 37).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(74, 37).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(184, 37).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(52, 37).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(116, 37).cuboid(-3.5f, 0f, -2.9f, 1, 3, 1)
                        .uv(120, 37).cuboid(2.5f, 0f, -2.9f, 1, 3, 1)
                        .uv(214, 23).cuboid(-4.3f, 13.6f, -2.5f, 9, 1, 5, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("season_skirt", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-6f, 0f, -3.7f, 12, 15, 8, new Dilation(0.5f)),
                        ModelTransform.pivot(0f, 16f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(28, 23).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.4f)),
                        ModelTransform.of(-6.9f, -47.5f, 0f, 0f, 0f, 0.1f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(52, 23).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(40, 23).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.4f)),
                        ModelTransform.of(6.9f, -47.5f, 0f, 0f, 0f, -0.1f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(64, 23).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(120, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(216, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(242, 23).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(136, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(228, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(0, 37).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("sun_hat", ModelPartBuilder.create()
                        .uv(152, 0).cuboid(-8f, -11.4f, -8f, 16, 1, 16)
                        .uv(76, 23).cuboid(-4.5f, -14.4f, -4.5f, 9, 3, 9, new Dilation(0.1f))
                        .uv(38, 43).cuboid(3.5f, -13.4f, -5.2f, 2, 2, 1),
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

    /** maru_winter (1.8 bloques, 61.75 unidades). */
    public static TexturedModelData maru_winter() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(36, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(76, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(28, 38).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(246, 38).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(0, 42).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(222, 38).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(34, 38).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(248, 38).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(6, 42).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(228, 38).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(12, 42).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(18, 42).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(120, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(128, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(136, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(144, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(152, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(160, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(168, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(176, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(184, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(192, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(234, 38).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(240, 38).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(250, 38).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(252, 38).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(254, 38).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(200, 38).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(216, 0).cuboid(-4.75f, 0f, -2.5f, 9, 9, 5, new Dilation(0.6f))
                        .uv(66, 24).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.42f))
                        .uv(36, 24).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(206, 38).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(40, 38).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(144, 24).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(0, 38).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(10, 38).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(60, 38).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(80, 38).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(90, 24).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(236, 24).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(246, 24).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(100, 38).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(224, 24).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(114, 24).cuboid(-3.2f, -0.2f, -3.2f, 6, 2, 6, new Dilation(0.5f))
                        .uv(138, 24).cuboid(1.2f, 2f, -4.2f, 2, 7, 1, new Dilation(0.2f))
                        .uv(168, 24).cuboid(-4.4f, 13.6f, -2.6f, 9, 1, 5, new Dilation(0.7f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("season_skirt", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, 0f, -3.2f, 11, 17, 7, new Dilation(0.55f)),
                        ModelTransform.pivot(0f, 15f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(244, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.6f)),
                        ModelTransform.of(-6.9f, -47.5f, 0f, 0f, 0f, 0.1f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(12, 24).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(0, 24).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.6f)),
                        ModelTransform.of(6.9f, -47.5f, 0f, 0f, 0f, -0.1f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(24, 24).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(116, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(148, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(196, 24).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(132, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(160, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(210, 24).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("winter_hat", ModelPartBuilder.create()
                        .uv(172, 0).cuboid(-5.5f, -12.4f, -5.5f, 11, 4, 11, new Dilation(0.55f))
                        .uv(20, 38).cuboid(-1f, -14.8f, -1f, 2, 2, 2, new Dilation(0.3f)),
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

    /** lumi_summer (1.8 bloques, 61.75 unidades). */
    public static TexturedModelData lumi_summer() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(66, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(106, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(108, 49).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(90, 55).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(100, 55).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(66, 55).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(114, 49).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(92, 55).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(106, 55).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(72, 55).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(112, 55).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(118, 55).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(208, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(216, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(224, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(232, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(240, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(248, 49).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(0, 55).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(8, 55).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(16, 55).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(24, 55).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(78, 55).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(84, 55).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(94, 55).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(96, 55).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(98, 55).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(32, 55).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("glasses", ModelPartBuilder.create()
                        .uv(60, 49).cuboid(-4.5f, -7.5f, -5.45f, 4, 5, 0)
                        .uv(68, 49).cuboid(0.5f, -7.5f, -5.45f, 4, 5, 0)
                        .uv(124, 55).cuboid(-0.5f, -6.5f, -5.45f, 1, 1, 0),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, 0f, -0.6f, 11, 31, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(234, 0).cuboid(-0.8f, 2f, -1.2f, 2, 15, 2, new Dilation(0.2f))
                        .uv(76, 49).cuboid(-1.3f, 17f, -1.7f, 3, 1, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(-5.6f, -4f, -1f));
        headD.addChild("clip", ModelPartBuilder.create()
                        .uv(38, 55).cuboid(2.5f, -10.4f, -6f, 2, 2, 1),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(36, 33).cuboid(-4.25f, 0f, -2.25f, 9, 9, 4, new Dilation(0.4f))
                        .uv(150, 33).cuboid(-3.75f, 9f, -1.75f, 8, 5, 4, new Dilation(0.3f))
                        .uv(122, 33).cuboid(-4.75f, 14f, -2.25f, 10, 6, 4, new Dilation(0.7f))
                        .uv(50, 55).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(128, 49).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(198, 33).cuboid(-4.5f, 3.4f, -4.965f, 9, 4, 3, new Dilation(0.3f))
                        .uv(88, 49).cuboid(-4.3f, 4f, -5.39f, 4, 3, 1, new Dilation(0.25f))
                        .uv(98, 49).cuboid(0.3f, 4f, -5.39f, 4, 3, 1, new Dilation(0.25f))
                        .uv(148, 49).cuboid(-4f, 7.2f, -4.625f, 8, 1, 2, new Dilation(0.2f))
                        .uv(168, 49).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(174, 33).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(40, 49).cuboid(-4.3f, 15.1f, 4.1675f, 4, 4, 1, new Dilation(0.25f))
                        .uv(50, 49).cuboid(0.3f, 15.1f, 4.1675f, 4, 4, 1, new Dilation(0.25f))
                        .uv(188, 49).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(28, 49).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(120, 49).cuboid(-3.5f, 0f, -2.9f, 1, 3, 1)
                        .uv(124, 49).cuboid(2.5f, 0f, -2.9f, 1, 3, 1)
                        .uv(222, 33).cuboid(-4.3f, 13.6f, -2.5f, 9, 1, 5, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("season_skirt", ModelPartBuilder.create()
                        .uv(26, 0).cuboid(-6f, 0f, -3.7f, 12, 15, 8, new Dilation(0.5f)),
                        ModelTransform.pivot(0f, 16f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(12, 33).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.4f)),
                        ModelTransform.of(-6f, -47.5f, 0f, 0f, 0f, 0.06f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(62, 33).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(24, 33).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.4f)),
                        ModelTransform.of(6f, -47.5f, 0f, 0f, 0f, -0.06f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(74, 33).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(210, 0).cuboid(-1.5f, 0f, -1.5f, 3, 14, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.4f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(242, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(0, 49).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(222, 0).cuboid(-1.5f, 0f, -1.5f, 3, 14, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(2.4f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(0, 33).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(14, 49).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("sun_hat", ModelPartBuilder.create()
                        .uv(146, 0).cuboid(-8f, -11.4f, -8f, 16, 1, 16)
                        .uv(86, 33).cuboid(-4.5f, -14.4f, -4.5f, 9, 3, 9, new Dilation(0.1f))
                        .uv(44, 55).cuboid(3.5f, -13.4f, -5.2f, 2, 2, 1),
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

    /** lumi_winter (1.8 bloques, 61.75 unidades). */
    public static TexturedModelData lumi_winter() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(62, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(102, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(88, 47).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(60, 52).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(70, 52).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(36, 52).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(94, 47).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(62, 52).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(76, 52).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(42, 52).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(82, 52).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(88, 52).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(180, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(188, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(196, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(204, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(212, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(220, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(228, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(236, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(244, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(0, 52).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(48, 52).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(54, 52).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(64, 52).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(66, 52).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(68, 52).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(8, 52).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("glasses", ModelPartBuilder.create()
                        .uv(32, 47).cuboid(-4.5f, -7.5f, -5.45f, 4, 5, 0)
                        .uv(40, 47).cuboid(0.5f, -7.5f, -5.45f, 4, 5, 0)
                        .uv(94, 52).cuboid(-0.5f, -6.5f, -5.45f, 1, 1, 0),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, 0f, -0.6f, 11, 31, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(166, 0).cuboid(-0.8f, 2f, -1.2f, 2, 15, 2, new Dilation(0.2f))
                        .uv(48, 47).cuboid(-1.3f, 17f, -1.7f, 3, 1, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(-5.6f, -4f, -1f));
        headD.addChild("clip", ModelPartBuilder.create()
                        .uv(14, 52).cuboid(2.5f, -10.4f, -6f, 2, 2, 1),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(12, 33).cuboid(-4.25f, 0f, -2.25f, 9, 9, 4, new Dilation(0.4f))
                        .uv(90, 33).cuboid(-3.75f, 9f, -1.75f, 8, 5, 4, new Dilation(0.3f))
                        .uv(62, 33).cuboid(-4.75f, 14f, -2.25f, 10, 6, 4, new Dilation(0.7f))
                        .uv(20, 52).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(100, 47).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(168, 33).cuboid(-4.5f, 3.4f, -4.965f, 9, 4, 3, new Dilation(0.3f))
                        .uv(60, 47).cuboid(-4.3f, 4f, -5.39f, 4, 3, 1, new Dilation(0.25f))
                        .uv(70, 47).cuboid(0.3f, 4f, -5.39f, 4, 3, 1, new Dilation(0.25f))
                        .uv(120, 47).cuboid(-4f, 7.2f, -4.625f, 8, 1, 2, new Dilation(0.2f))
                        .uv(140, 47).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(114, 33).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(12, 47).cuboid(-4.3f, 15.1f, 4.1675f, 4, 4, 1, new Dilation(0.25f))
                        .uv(22, 47).cuboid(0.3f, 15.1f, 4.1675f, 4, 4, 1, new Dilation(0.25f))
                        .uv(160, 47).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(0, 47).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(138, 33).cuboid(-3.2f, -0.2f, -3.2f, 6, 2, 6, new Dilation(0.5f))
                        .uv(162, 33).cuboid(1.2f, 2f, -4.2f, 2, 7, 1, new Dilation(0.2f))
                        .uv(192, 33).cuboid(-4.4f, 13.6f, -2.6f, 9, 1, 5, new Dilation(0.7f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("season_skirt", ModelPartBuilder.create()
                        .uv(26, 0).cuboid(-5.5f, 0f, -3.2f, 11, 17, 7, new Dilation(0.55f)),
                        ModelTransform.pivot(0f, 15f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(242, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.6f)),
                        ModelTransform.of(-6f, -47.5f, 0f, 0f, 0f, 0.06f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(38, 33).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(0, 33).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.6f)),
                        ModelTransform.of(6f, -47.5f, 0f, 0f, 0f, -0.06f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(50, 33).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(142, 0).cuboid(-1.5f, 0f, -1.5f, 3, 14, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.4f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(174, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(220, 33).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(154, 0).cuboid(-1.5f, 0f, -1.5f, 3, 14, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(2.4f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(186, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(234, 33).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("winter_hat", ModelPartBuilder.create()
                        .uv(198, 0).cuboid(-5.5f, -12.4f, -5.5f, 11, 4, 11, new Dilation(0.55f))
                        .uv(80, 47).cuboid(-1f, -14.8f, -1f, 2, 2, 2, new Dilation(0.3f)),
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

    /** nadi_summer (1.8 bloques, 61.75 unidades). */
    public static TexturedModelData nadi_summer() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(80, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(120, 37).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(100, 43).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(110, 43).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(76, 43).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(126, 37).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(102, 43).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(116, 43).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(82, 43).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(122, 43).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(128, 43).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(220, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(228, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(236, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(244, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(0, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(8, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(16, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(24, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(32, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(40, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(88, 43).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(94, 43).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(104, 43).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(106, 43).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(108, 43).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(48, 43).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(52, 23).cuboid(-5.5f, 0f, -0.6f, 11, 11, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(152, 0).cuboid(-1f, 1f, -0.6f, 2, 16, 2, new Dilation(0.25f))
                        .uv(88, 37).cuboid(-1.5f, 16f, -1.1f, 3, 1, 3),
                        ModelTransform.pivot(0f, 3f, 7f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(0, 23).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(168, 23).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(138, 23).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(60, 43).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(140, 37).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(216, 23).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(100, 37).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(110, 37).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(160, 37).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(180, 37).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(192, 23).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(68, 37).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(78, 37).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(200, 37).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(56, 37).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(132, 37).cuboid(-3.5f, 0f, -2.9f, 1, 3, 1)
                        .uv(136, 37).cuboid(2.5f, 0f, -2.9f, 1, 3, 1)
                        .uv(0, 37).cuboid(-4.3f, 13.6f, -2.5f, 9, 1, 5, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("season_skirt", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-6f, 0f, -3.7f, 12, 15, 8, new Dilation(0.5f)),
                        ModelTransform.pivot(0f, 16f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(28, 23).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.4f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(78, 23).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(40, 23).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.4f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(90, 23).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(120, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(224, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(28, 37).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(136, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(236, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(42, 37).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("sun_hat", ModelPartBuilder.create()
                        .uv(160, 0).cuboid(-8f, -11.4f, -8f, 16, 1, 16)
                        .uv(102, 23).cuboid(-4.5f, -14.4f, -4.5f, 9, 3, 9, new Dilation(0.1f))
                        .uv(54, 43).cuboid(3.5f, -13.4f, -5.2f, 2, 2, 1),
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

    /** nadi_winter (1.8 bloques, 61.75 unidades). */
    public static TexturedModelData nadi_winter() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(36, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(76, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(86, 38).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(54, 44).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(64, 44).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(30, 44).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(92, 38).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(56, 44).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(70, 44).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(36, 44).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(76, 44).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(82, 44).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(178, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(186, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(194, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(202, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(210, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(218, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(226, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(234, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(242, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(0, 44).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(42, 44).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(48, 44).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(58, 44).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(60, 44).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(62, 44).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(8, 44).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(24, 24).cuboid(-5.5f, 0f, -0.6f, 11, 11, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(148, 0).cuboid(-1f, 1f, -0.6f, 2, 16, 2, new Dilation(0.25f))
                        .uv(46, 38).cuboid(-1.5f, 16f, -1.1f, 3, 1, 3),
                        ModelTransform.pivot(0f, 3f, 7f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(224, 0).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(104, 24).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(74, 24).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(14, 44).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(98, 38).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(182, 24).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(58, 38).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(68, 38).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(118, 38).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(138, 38).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(128, 24).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(26, 38).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(36, 38).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(158, 38).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(14, 38).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(152, 24).cuboid(-3.2f, -0.2f, -3.2f, 6, 2, 6, new Dilation(0.5f))
                        .uv(176, 24).cuboid(1.2f, 2f, -4.2f, 2, 7, 1, new Dilation(0.2f))
                        .uv(206, 24).cuboid(-4.4f, 13.6f, -2.6f, 9, 1, 5, new Dilation(0.7f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("season_skirt", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, 0f, -3.2f, 11, 17, 7, new Dilation(0.55f)),
                        ModelTransform.pivot(0f, 15f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(0, 24).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.6f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(50, 24).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(12, 24).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.6f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(62, 24).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(116, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(156, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(234, 24).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(132, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(168, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(0, 38).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("winter_hat", ModelPartBuilder.create()
                        .uv(180, 0).cuboid(-5.5f, -12.4f, -5.5f, 11, 4, 11, new Dilation(0.55f))
                        .uv(78, 38).cuboid(-1f, -14.8f, -1f, 2, 2, 2, new Dilation(0.3f)),
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

    /** rina_summer (1.8 bloques, 61.75 unidades). */
    public static TexturedModelData rina_summer() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(80, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(122, 37).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(100, 43).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(110, 43).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(76, 43).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(128, 37).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(102, 43).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(116, 43).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(82, 43).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(122, 43).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(128, 43).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(222, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(230, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(238, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(246, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(0, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(8, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(16, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(24, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(32, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(40, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(88, 43).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(94, 43).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(104, 43).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(106, 43).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(108, 43).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(48, 43).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(52, 23).cuboid(-5.5f, 0f, -0.6f, 11, 11, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(192, 23).cuboid(-1.5f, -2f, -1.4f, 3, 6, 3, new Dilation(0.1f))
                        .uv(240, 0).cuboid(-1.5f, 4f, -2f, 3, 13, 2, new Dilation(0.2f))
                        .uv(88, 37).cuboid(-2f, 3f, -1.8f, 4, 1, 3),
                        ModelTransform.pivot(0f, 6f, 5f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(0, 23).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(168, 23).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(138, 23).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(60, 43).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(142, 37).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(228, 23).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(102, 37).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(112, 37).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(162, 37).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(182, 37).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(204, 23).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(68, 37).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(78, 37).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(202, 37).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(56, 37).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(134, 37).cuboid(-3.5f, 0f, -2.9f, 1, 3, 1)
                        .uv(138, 37).cuboid(2.5f, 0f, -2.9f, 1, 3, 1)
                        .uv(0, 37).cuboid(-4.3f, 13.6f, -2.5f, 9, 1, 5, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("season_skirt", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-6f, 0f, -3.7f, 12, 15, 8, new Dilation(0.5f)),
                        ModelTransform.pivot(0f, 16f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(28, 23).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.4f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(78, 23).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(40, 23).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.4f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(90, 23).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(120, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(216, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(28, 37).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(136, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(228, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(42, 37).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("sun_hat", ModelPartBuilder.create()
                        .uv(152, 0).cuboid(-8f, -11.4f, -8f, 16, 1, 16)
                        .uv(102, 23).cuboid(-4.5f, -14.4f, -4.5f, 9, 3, 9, new Dilation(0.1f))
                        .uv(54, 43).cuboid(3.5f, -13.4f, -5.2f, 2, 2, 1),
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

    /** rina_winter (1.8 bloques, 61.75 unidades). */
    public static TexturedModelData rina_winter() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(36, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(76, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(102, 38).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(70, 44).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(80, 44).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(46, 44).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(108, 38).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(72, 44).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(86, 44).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(52, 44).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(92, 44).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(98, 44).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(194, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(202, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(210, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(218, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(226, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(234, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(242, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(0, 44).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(8, 44).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(16, 44).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(58, 44).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(64, 44).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(74, 44).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(76, 44).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(78, 44).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(24, 44).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(24, 24).cuboid(-5.5f, 0f, -0.6f, 11, 11, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(128, 24).cuboid(-1.5f, -2f, -1.4f, 3, 6, 3, new Dilation(0.1f))
                        .uv(216, 0).cuboid(-1.5f, 4f, -2f, 3, 13, 2, new Dilation(0.2f))
                        .uv(60, 38).cuboid(-2f, 3f, -1.8f, 4, 1, 3),
                        ModelTransform.pivot(0f, 6f, 5f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(226, 0).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(104, 24).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(74, 24).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(30, 44).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(114, 38).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(194, 24).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(74, 38).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(84, 38).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(134, 38).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(154, 38).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(140, 24).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(40, 38).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(50, 38).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(174, 38).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(28, 38).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(164, 24).cuboid(-3.2f, -0.2f, -3.2f, 6, 2, 6, new Dilation(0.5f))
                        .uv(188, 24).cuboid(1.2f, 2f, -4.2f, 2, 7, 1, new Dilation(0.2f))
                        .uv(218, 24).cuboid(-4.4f, 13.6f, -2.6f, 9, 1, 5, new Dilation(0.7f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("season_skirt", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, 0f, -3.2f, 11, 17, 7, new Dilation(0.55f)),
                        ModelTransform.pivot(0f, 15f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(0, 24).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.6f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(50, 24).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(12, 24).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.6f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(62, 24).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(116, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(148, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(0, 38).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(132, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(160, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(14, 38).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("winter_hat", ModelPartBuilder.create()
                        .uv(172, 0).cuboid(-5.5f, -12.4f, -5.5f, 11, 4, 11, new Dilation(0.55f))
                        .uv(94, 38).cuboid(-1f, -14.8f, -1f, 2, 2, 2, new Dilation(0.3f)),
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

    /** tami_summer (1.8 bloques, 61.75 unidades). */
    public static TexturedModelData tami_summer() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(80, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(108, 37).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(84, 43).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(94, 43).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(60, 43).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(114, 37).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(86, 43).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(100, 43).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(66, 43).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(106, 43).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(112, 43).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(208, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(216, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(224, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(232, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(240, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(248, 37).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(0, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(8, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(16, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(24, 43).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(72, 43).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(78, 43).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(88, 43).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(90, 43).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(92, 43).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(32, 43).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(166, 23).cuboid(-5.5f, 0f, -0.6f, 11, 6, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(240, 23).cuboid(-2f, -2f, -0.2f, 4, 4, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, -5f, 7f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(0, 23).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(142, 23).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(112, 23).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(44, 43).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(128, 37).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(216, 23).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(88, 37).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(98, 37).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(148, 37).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(168, 37).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(192, 23).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(68, 37).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(78, 37).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(188, 37).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(56, 37).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(120, 37).cuboid(-3.5f, 0f, -2.9f, 1, 3, 1)
                        .uv(124, 37).cuboid(2.5f, 0f, -2.9f, 1, 3, 1)
                        .uv(0, 37).cuboid(-4.3f, 13.6f, -2.5f, 9, 1, 5, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("season_skirt", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-6f, 0f, -3.7f, 12, 15, 8, new Dilation(0.5f)),
                        ModelTransform.pivot(0f, 16f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(28, 23).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.4f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(52, 23).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(40, 23).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.4f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(64, 23).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(120, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(216, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(28, 37).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(136, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(228, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(42, 37).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("sun_hat", ModelPartBuilder.create()
                        .uv(152, 0).cuboid(-8f, -11.4f, -8f, 16, 1, 16)
                        .uv(76, 23).cuboid(-4.5f, -14.4f, -4.5f, 9, 3, 9, new Dilation(0.1f))
                        .uv(38, 43).cuboid(3.5f, -13.4f, -5.2f, 2, 2, 1),
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

    /** tami_winter (1.8 bloques, 61.75 unidades). */
    public static TexturedModelData tami_winter() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(36, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(76, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(74, 38).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(40, 44).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(50, 44).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(16, 44).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(80, 38).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(42, 44).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(56, 44).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(22, 44).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(62, 44).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(68, 44).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(166, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(174, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(182, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(190, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(198, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(206, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(214, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(222, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(230, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(238, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(28, 44).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(34, 44).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(44, 44).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(46, 44).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(48, 44).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(246, 38).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(90, 24).cuboid(-5.5f, 0f, -0.6f, 11, 6, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(194, 24).cuboid(-2f, -2f, -0.2f, 4, 4, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, -5f, 7f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(216, 0).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(66, 24).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(36, 24).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(0, 44).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(86, 38).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(170, 24).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(46, 38).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(56, 38).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(106, 38).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(126, 38).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(116, 24).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(26, 38).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(36, 38).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(146, 38).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(14, 38).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(140, 24).cuboid(-3.2f, -0.2f, -3.2f, 6, 2, 6, new Dilation(0.5f))
                        .uv(164, 24).cuboid(1.2f, 2f, -4.2f, 2, 7, 1, new Dilation(0.2f))
                        .uv(208, 24).cuboid(-4.4f, 13.6f, -2.6f, 9, 1, 5, new Dilation(0.7f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("season_skirt", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, 0f, -3.2f, 11, 17, 7, new Dilation(0.55f)),
                        ModelTransform.pivot(0f, 15f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(244, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.6f)),
                        ModelTransform.of(-6.6f, -47.5f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(12, 24).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(0, 24).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.6f)),
                        ModelTransform.of(6.6f, -47.5f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(24, 24).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(116, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(148, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(236, 24).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(132, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(160, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(0, 38).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("winter_hat", ModelPartBuilder.create()
                        .uv(172, 0).cuboid(-5.5f, -12.4f, -5.5f, 11, 4, 11, new Dilation(0.55f))
                        .uv(66, 38).cuboid(-1f, -14.8f, -1f, 2, 2, 2, new Dilation(0.3f)),
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
