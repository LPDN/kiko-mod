package com.jaciel.kiko.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;

/**
 * Los 6 chicos (v3.3) con sus 5 ropas (trabajo, casual, boda, verano, invierno).
 * Los genera tools/gen_guys_outfits.py (no editar a mano). Usan la misma clase KikoModel que las chicas.
 */
@SuppressWarnings("unused")
public final class GuyModels {
    private GuyModels() {}

    // La cabeza de todos llega a 61.75 unidades (igual que las chicas), así que KikoRenderer los escala igual.

    /** tono (1.88 bloques, 65.80 unidades). */
    public static TexturedModelData tono() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(228, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(70, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(104, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(74, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(234, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(72, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(110, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(80, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(116, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(122, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(240, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(248, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(0, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(8, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(16, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(24, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(32, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(40, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(48, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(56, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(86, 34).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(92, 34).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(98, 34).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(100, 34).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(102, 34).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(64, 34).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(200, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(88, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(60, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(114, 21).cuboid(-3f, -0.5f, -3f, 6, 2, 6, new Dilation(0.35f))
                        .uv(138, 21).cuboid(-4.7f, 14.4f, -2.7f, 9, 1, 5, new Dilation(0.7f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(230, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(36, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(242, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(48, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(176, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(166, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(188, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(180, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-8f, -11.2f, -8f, 16, 1, 16)
                        .uv(0, 21).cuboid(-4.5f, -15.2f, -4.5f, 9, 4, 9, new Dilation(0.1f))
                        .uv(194, 21).cuboid(-3.5f, -15.8f, -2f, 7, 1, 4),
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

    /** tono_casual (1.88 bloques, 65.80 unidades). */
    public static TexturedModelData tono_casual() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(22, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(56, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(26, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(182, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(24, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(62, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(32, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(68, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(74, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(188, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(196, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(204, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(212, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(228, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(236, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(244, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(0, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(8, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(38, 34).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(44, 34).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(50, 34).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(52, 34).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(54, 34).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(16, 34).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(200, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(88, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(60, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(230, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(36, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(242, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(48, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(176, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(114, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(188, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(128, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-8f, -11.2f, -8f, 16, 1, 16)
                        .uv(0, 21).cuboid(-4.5f, -15.2f, -4.5f, 9, 4, 9, new Dilation(0.1f))
                        .uv(142, 21).cuboid(-3.5f, -15.8f, -2f, 7, 1, 4),
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

    /** tono_wedding (1.88 bloques, 66.30 unidades). */
    public static TexturedModelData tono_wedding() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(178, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(22, 35).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(60, 35).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(26, 35).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(184, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(24, 35).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(66, 35).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(32, 35).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(72, 35).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(78, 35).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(190, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(206, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(214, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(222, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(230, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(238, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(246, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(0, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(8, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(38, 35).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(44, 35).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(54, 35).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(56, 35).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(58, 35).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(16, 35).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(166, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(204, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(112, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(84, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(50, 35).cuboid(2.6f, 2.5f, -3.2f, 1, 1, 1, new Dilation(0.15f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(36, 21).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(60, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(48, 21).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(72, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(148, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(180, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(138, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(164, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(192, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(152, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-8.5f, -11.2f, -8.5f, 17, 1, 17)
                        .uv(0, 21).cuboid(-4.5f, -16.2f, -4.5f, 9, 5, 9, new Dilation(0.1f)),
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

    /** tono_summer (1.88 bloques, 61.75 unidades). */
    public static TexturedModelData tono_summer() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(66, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(186, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(72, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(166, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(222, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(228, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(234, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(78, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(86, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(94, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(102, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(126, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(134, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(204, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(210, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(212, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(214, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(158, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(54, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f)),
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
                        .uv(26, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(40, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-4.5f, -5.5f, -5.55f, 9, 2, 0),
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

    /** tono_winter (1.88 bloques, 65.10 unidades). */
    public static TexturedModelData tono_winter() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(22, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(56, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(26, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(182, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(24, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(62, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(32, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(68, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(74, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(188, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(196, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(204, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(212, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(228, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(236, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(244, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(0, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(8, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(38, 34).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(44, 34).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(50, 34).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(52, 34).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(54, 34).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(16, 34).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(156, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(180, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(40, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(12, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(66, 21).cuboid(-5.2f, 15f, -2.9f, 10, 2, 6, new Dilation(0.8f))
                        .uv(98, 21).cuboid(-3.2f, -0.2f, -3.2f, 6, 2, 6, new Dilation(0.5f))
                        .uv(122, 21).cuboid(1.2f, 2f, -4.2f, 2, 7, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(210, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(234, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(222, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(128, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(142, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(136, 0).cuboid(-5.5f, -12.4f, -5.5f, 11, 4, 11, new Dilation(0.6f))
                        .uv(168, 21).cuboid(-1f, -14.8f, -1f, 2, 2, 2, new Dilation(0.3f)),
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

    /** leo (1.82 bloques, 62.25 unidades). */
    public static TexturedModelData leo() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(130, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(240, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(18, 35).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(244, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(136, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(242, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(24, 35).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(250, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(30, 35).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(36, 35).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(158, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(166, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(174, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(182, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(190, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(206, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(214, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(0, 35).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(6, 35).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(12, 35).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(14, 35).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(16, 35).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(222, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(166, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(64, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(36, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(80, 0).cuboid(-4.8f, 0f, -2.8f, 10, 16, 5, new Dilation(0.8f))
                        .uv(228, 21).cuboid(-4.5f, 11f, -3.9f, 2, 2, 1)
                        .uv(234, 21).cuboid(2.5f, 11f, -3.9f, 2, 2, 1),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(240, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(12, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(24, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(110, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(142, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(90, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(126, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(154, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(104, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(196, 0).cuboid(-5.5f, -11.8f, -5.5f, 11, 3, 11, new Dilation(0.45f)),
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

    /** leo_casual (1.82 bloques, 61.75 unidades). */
    public static TexturedModelData leo_casual() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(66, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(72, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(166, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(204, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(174, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(210, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(78, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(86, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(94, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(102, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(126, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(134, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(180, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(186, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(194, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(196, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(158, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(54, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f)),
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
                        .uv(26, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(40, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
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

    /** leo_wedding (1.82 bloques, 61.75 unidades). */
    public static TexturedModelData leo_wedding() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(66, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(202, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(72, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(166, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(208, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(174, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(214, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(78, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(86, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(94, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(102, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(126, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(134, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(180, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(186, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(196, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(200, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(158, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(54, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(192, 21).cuboid(2.6f, 2.5f, -3.2f, 1, 1, 1, new Dilation(0.15f)),
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
                        .uv(26, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(40, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
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

    /** leo_summer (1.82 bloques, 61.75 unidades). */
    public static TexturedModelData leo_summer() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(66, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(186, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(72, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(166, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(222, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(228, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(234, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(78, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(86, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(94, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(102, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(126, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(134, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(204, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(210, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(212, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(214, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(158, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(54, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f)),
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
                        .uv(26, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(40, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-4.5f, -5.5f, -5.55f, 9, 2, 0),
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

    /** leo_winter (1.82 bloques, 65.10 unidades). */
    public static TexturedModelData leo_winter() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(22, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(56, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(26, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(182, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(24, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(62, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(32, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(68, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(74, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(188, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(196, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(204, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(212, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(228, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(236, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(244, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(0, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(8, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(38, 34).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(44, 34).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(50, 34).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(52, 34).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(54, 34).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(16, 34).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(156, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(180, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(40, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(12, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(66, 21).cuboid(-5.2f, 15f, -2.9f, 10, 2, 6, new Dilation(0.8f))
                        .uv(98, 21).cuboid(-3.2f, -0.2f, -3.2f, 6, 2, 6, new Dilation(0.5f))
                        .uv(122, 21).cuboid(1.2f, 2f, -4.2f, 2, 7, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(210, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(234, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(222, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(128, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(142, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(136, 0).cuboid(-5.5f, -12.4f, -5.5f, 11, 4, 11, new Dilation(0.6f))
                        .uv(168, 21).cuboid(-1f, -14.8f, -1f, 2, 2, 2, new Dilation(0.3f)),
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

    /** dante (1.92 bloques, 61.75 unidades). */
    public static TexturedModelData dante() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(22, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(62, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(188, 27).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(30, 40).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(64, 40).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(34, 40).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(194, 27).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(32, 40).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(70, 40).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(40, 40).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(76, 40).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(82, 40).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(200, 27).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(208, 27).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(216, 27).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(224, 27).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(232, 27).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(240, 27).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(248, 27).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(0, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(8, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(16, 40).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(46, 40).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(52, 40).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(58, 40).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(60, 40).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(62, 40).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(24, 40).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(176, 27).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(188, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(52, 27).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(24, 27).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(134, 0).cuboid(-5f, 0f, -2.8f, 10, 12, 5, new Dilation(0.85f))
                        .uv(12, 27).cuboid(-2.5f, 12f, -3.9f, 5, 10, 1, new Dilation(0.1f))
                        .uv(0, 0).cuboid(-5f, 0f, 3f, 10, 26, 1, new Dilation(0.3f))
                        .uv(118, 27).cuboid(-4.8f, 14.5f, -2.9f, 10, 1, 5, new Dilation(0.9f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(218, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f))
                        .uv(78, 27).cuboid(-2.5f, -0.5f, -2.5f, 5, 3, 5, new Dilation(0.3f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(242, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(230, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f))
                        .uv(98, 27).cuboid(-2.5f, -0.5f, -2.5f, 5, 3, 5, new Dilation(0.3f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(0, 27).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(102, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(164, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(148, 27).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(118, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(176, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(162, 27).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
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

    /** dante_casual (1.92 bloques, 61.75 unidades). */
    public static TexturedModelData dante_casual() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(66, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(72, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(166, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(204, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(174, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(210, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(78, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(86, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(94, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(102, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(126, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(134, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(180, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(186, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(194, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(196, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(158, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(54, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f)),
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
                        .uv(26, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(40, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
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

    /** dante_wedding (1.92 bloques, 61.75 unidades). */
    public static TexturedModelData dante_wedding() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(66, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(202, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(72, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(166, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(208, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(174, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(214, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(78, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(86, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(94, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(102, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(126, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(134, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(180, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(186, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(196, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(200, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(158, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(54, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(192, 21).cuboid(2.6f, 2.5f, -3.2f, 1, 1, 1, new Dilation(0.15f)),
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
                        .uv(26, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(40, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
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

    /** dante_summer (1.92 bloques, 61.75 unidades). */
    public static TexturedModelData dante_summer() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(66, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(186, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(72, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(166, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(222, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(228, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(234, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(78, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(86, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(94, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(102, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(126, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(134, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(204, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(210, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(212, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(214, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(158, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(54, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f)),
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
                        .uv(26, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(40, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-4.5f, -5.5f, -5.55f, 9, 2, 0),
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

    /** dante_winter (1.92 bloques, 65.10 unidades). */
    public static TexturedModelData dante_winter() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(22, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(56, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(26, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(182, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(24, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(62, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(32, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(68, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(74, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(188, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(196, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(204, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(212, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(228, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(236, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(244, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(0, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(8, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(38, 34).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(44, 34).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(50, 34).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(52, 34).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(54, 34).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(16, 34).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(156, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(180, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(40, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(12, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(66, 21).cuboid(-5.2f, 15f, -2.9f, 10, 2, 6, new Dilation(0.8f))
                        .uv(98, 21).cuboid(-3.2f, -0.2f, -3.2f, 6, 2, 6, new Dilation(0.5f))
                        .uv(122, 21).cuboid(1.2f, 2f, -4.2f, 2, 7, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(210, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(234, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(222, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(128, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(142, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(136, 0).cuboid(-5.5f, -12.4f, -5.5f, 11, 4, 11, new Dilation(0.6f))
                        .uv(168, 21).cuboid(-1f, -14.8f, -1f, 2, 2, 2, new Dilation(0.3f)),
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

    /** ivan (1.8 bloques, 63.00 unidades). */
    public static TexturedModelData ivan() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(12, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(46, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(16, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(170, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(14, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(52, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(22, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(58, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(64, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(184, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(200, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(208, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(224, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(232, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(240, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(248, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(28, 34).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(34, 34).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(40, 34).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(42, 34).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(44, 34).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(0, 34).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(124, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(40, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(12, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(66, 21).cuboid(-4.8f, 15.5f, -2.8f, 10, 2, 5, new Dilation(0.85f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(210, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(234, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(222, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(96, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(110, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(166, 0).cuboid(-5.5f, -12.6f, -5.5f, 11, 3, 11, new Dilation(0.4f))
                        .uv(136, 21).cuboid(-5.5f, -10.3f, -8.6f, 11, 1, 3)
                        .uv(6, 34).cuboid(-1f, -12.2f, -7.1f, 2, 2, 1),
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

    /** ivan_casual (1.8 bloques, 61.75 unidades). */
    public static TexturedModelData ivan_casual() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(66, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(72, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(166, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(204, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(174, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(210, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(78, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(86, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(94, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(102, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(126, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(134, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(180, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(186, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(194, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(196, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(158, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(54, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f)),
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
                        .uv(26, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(40, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
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

    /** ivan_wedding (1.8 bloques, 61.75 unidades). */
    public static TexturedModelData ivan_wedding() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(66, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(202, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(72, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(166, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(208, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(174, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(214, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(78, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(86, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(94, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(102, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(126, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(134, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(180, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(186, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(196, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(200, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(158, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(54, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(192, 21).cuboid(2.6f, 2.5f, -3.2f, 1, 1, 1, new Dilation(0.15f)),
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
                        .uv(26, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(40, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
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

    /** ivan_summer (1.8 bloques, 61.75 unidades). */
    public static TexturedModelData ivan_summer() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(66, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(186, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(72, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(166, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(222, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(228, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(234, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(78, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(86, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(94, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(102, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(118, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(126, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(134, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(142, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(150, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(204, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(210, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(212, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(214, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(158, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(54, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f)),
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
                        .uv(26, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(40, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-4.5f, -5.5f, -5.55f, 9, 2, 0),
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

    /** ivan_winter (1.8 bloques, 65.10 unidades). */
    public static TexturedModelData ivan_winter() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(22, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(56, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(26, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(182, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(24, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(62, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(32, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(68, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(74, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(188, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(196, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(204, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(212, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(228, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(236, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(244, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(0, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(8, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(38, 34).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(44, 34).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(50, 34).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(52, 34).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(54, 34).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(16, 34).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(156, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(180, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(40, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(12, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(66, 21).cuboid(-5.2f, 15f, -2.9f, 10, 2, 6, new Dilation(0.8f))
                        .uv(98, 21).cuboid(-3.2f, -0.2f, -3.2f, 6, 2, 6, new Dilation(0.5f))
                        .uv(122, 21).cuboid(1.2f, 2f, -4.2f, 2, 7, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(210, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(234, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(222, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(128, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(142, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(136, 0).cuboid(-5.5f, -12.4f, -5.5f, 11, 4, 11, new Dilation(0.6f))
                        .uv(168, 21).cuboid(-1f, -14.8f, -1f, 2, 2, 2, new Dilation(0.3f)),
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

    /** emi (1.76 bloques, 61.75 unidades). */
    public static TexturedModelData emi() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(156, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(254, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(36, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(2, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(162, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(0, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(42, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(8, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(48, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(54, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(184, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(200, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(208, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(224, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(232, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(240, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(14, 34).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(20, 34).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(30, 34).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(32, 34).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(34, 34).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(248, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(144, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(180, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(56, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(12, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(80, 0).cuboid(-4.8f, 0f, -2.8f, 10, 16, 5, new Dilation(0.8f))
                        .uv(40, 21).cuboid(-3f, 13f, 3.4f, 6, 8, 2, new Dilation(0.1f))
                        .uv(234, 0).cuboid(-0.5f, 0f, 3.9f, 1, 13, 1)
                        .uv(142, 0).cuboid(-4.6f, 0f, -2.9f, 1, 10, 6, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(210, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(238, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(222, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(110, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(156, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(116, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(126, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(168, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(130, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(82, 21).cuboid(-5.5f, 0f, -0.6f, 11, 5, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(108, 21).cuboid(-1f, 1f, -0.4f, 2, 5, 2, new Dilation(0.25f)),
                        ModelTransform.pivot(0f, -3f, 7f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(26, 34).cuboid(5.1f, -4.5f, -0.5f, 1, 1, 1),
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

    /** emi_casual (1.76 bloques, 61.75 unidades). */
    public static TexturedModelData emi_casual() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(100, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(236, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(202, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(106, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(200, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(242, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(208, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(248, 21).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(0, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(112, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(120, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(128, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(136, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(144, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(152, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(160, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(184, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(214, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(220, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(230, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(232, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(234, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(26, 21).cuboid(-5.5f, 0f, -0.6f, 11, 5, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(52, 21).cuboid(-1f, 1f, -0.4f, 2, 5, 2, new Dilation(0.25f)),
                        ModelTransform.pivot(0f, -3f, 7f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(88, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f)),
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
                        .uv(60, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(74, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(226, 21).cuboid(5.1f, -4.5f, -0.5f, 1, 1, 1),
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

    /** emi_wedding (1.76 bloques, 61.75 unidades). */
    public static TexturedModelData emi_wedding() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(100, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(240, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(202, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(106, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(200, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(246, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(208, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(0, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(6, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(112, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(120, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(128, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(136, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(144, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(152, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(160, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(184, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(214, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(220, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(234, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(236, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(238, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(26, 21).cuboid(-5.5f, 0f, -0.6f, 11, 5, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(52, 21).cuboid(-1f, 1f, -0.4f, 2, 5, 2, new Dilation(0.25f)),
                        ModelTransform.pivot(0f, -3f, 7f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(88, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(226, 21).cuboid(2.6f, 2.5f, -3.2f, 1, 1, 1, new Dilation(0.15f)),
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
                        .uv(60, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(74, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(230, 21).cuboid(5.1f, -4.5f, -0.5f, 1, 1, 1),
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

    /** emi_summer (1.76 bloques, 61.75 unidades). */
    public static TexturedModelData emi_summer() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(100, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(198, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(250, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(106, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(200, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(0, 30).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(226, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(6, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(12, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(112, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(120, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(128, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(136, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(144, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(152, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(160, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(184, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(232, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(238, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(244, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(246, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(248, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(26, 21).cuboid(-5.5f, 0f, -0.6f, 11, 5, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(52, 21).cuboid(-1f, 1f, -0.4f, 2, 5, 2, new Dilation(0.25f)),
                        ModelTransform.pivot(0f, -3f, 7f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(88, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f)),
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
                        .uv(60, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(74, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(202, 21).cuboid(-4.5f, -5.5f, -5.55f, 9, 2, 0),
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

    /** emi_winter (1.76 bloques, 65.10 unidades). */
    public static TexturedModelData emi_winter() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(210, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(54, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(88, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(58, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(56, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(94, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(64, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(100, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(106, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(222, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(230, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(238, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(246, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(0, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(8, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(16, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(24, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(32, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(40, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(70, 34).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(76, 34).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(82, 34).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(84, 34).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(86, 34).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(48, 34).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(128, 21).cuboid(-5.5f, 0f, -0.6f, 11, 5, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(154, 21).cuboid(-1f, 1f, -0.4f, 2, 5, 2, new Dilation(0.25f)),
                        ModelTransform.pivot(0f, -3f, 7f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(190, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(180, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(40, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(12, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(66, 21).cuboid(-5.2f, 15f, -2.9f, 10, 2, 6, new Dilation(0.8f))
                        .uv(98, 21).cuboid(-3.2f, -0.2f, -3.2f, 6, 2, 6, new Dilation(0.5f))
                        .uv(122, 21).cuboid(1.2f, 2f, -4.2f, 2, 7, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(210, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(234, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(222, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(162, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(176, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(136, 0).cuboid(-5.5f, -12.4f, -5.5f, 11, 4, 11, new Dilation(0.6f))
                        .uv(202, 21).cuboid(-1f, -14.8f, -1f, 2, 2, 2, new Dilation(0.3f)),
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

    /** beto (1.84 bloques, 63.70 unidades). */
    public static TexturedModelData beto() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(188, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(30, 31).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(64, 31).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(34, 31).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(194, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(32, 31).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(70, 31).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(40, 31).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(76, 31).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(82, 31).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(200, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(208, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(224, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(232, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(240, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(248, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(0, 31).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(8, 31).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(16, 31).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(46, 31).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(52, 31).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(58, 31).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(60, 31).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(62, 31).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(24, 31).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(140, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(170, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(44, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(0, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(80, 0).cuboid(-4.8f, 0f, -2.8f, 10, 15, 5, new Dilation(0.8f))
                        .uv(166, 0).cuboid(-0.5f, 0f, -3.8f, 1, 15, 1, new Dilation(0.1f))
                        .uv(28, 21).cuboid(3.6f, 15f, -2.5f, 3, 5, 5, new Dilation(0.2f))
                        .uv(70, 21).cuboid(-4.7f, 14.4f, -2.7f, 9, 1, 5, new Dilation(0.7f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(200, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(224, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(212, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(236, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(110, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(142, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(98, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(126, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(154, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(112, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("spikes", ModelPartBuilder.create()
                        .uv(152, 21).cuboid(-4.5f, -13f, -3.5f, 3, 2, 3, new Dilation(0.3f))
                        .uv(164, 21).cuboid(-1f, -13.4f, -1f, 3, 2, 3, new Dilation(0.3f))
                        .uv(176, 21).cuboid(2f, -13f, -3f, 3, 2, 3, new Dilation(0.3f))
                        .uv(126, 21).cuboid(-2f, -13f, 2f, 4, 2, 3, new Dilation(0.3f)),
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

    /** beto_casual (1.84 bloques, 63.70 unidades). */
    public static TexturedModelData beto_casual() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(116, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(214, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(248, 21).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(218, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(122, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(0, 30).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(224, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(6, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(12, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(128, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(136, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(144, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(152, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(160, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(184, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(200, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(230, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(236, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(242, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(244, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(246, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(208, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("spikes", ModelPartBuilder.create()
                        .uv(68, 21).cuboid(-4.5f, -13f, -3.5f, 3, 2, 3, new Dilation(0.3f))
                        .uv(80, 21).cuboid(-1f, -13.4f, -1f, 3, 2, 3, new Dilation(0.3f))
                        .uv(92, 21).cuboid(2f, -13f, -3f, 3, 2, 3, new Dilation(0.3f))
                        .uv(54, 21).cuboid(-2f, -13f, 2f, 4, 2, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(104, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f)),
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
                        .uv(26, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(40, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
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

    /** beto_wedding (1.84 bloques, 63.70 unidades). */
    public static TexturedModelData beto_wedding() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(116, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(214, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(0, 30).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(218, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(122, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(6, 30).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(224, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(12, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(18, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(128, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(136, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(144, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(152, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(160, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(184, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(200, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(230, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(236, 21).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(246, 21).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(248, 21).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(250, 21).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(208, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("spikes", ModelPartBuilder.create()
                        .uv(68, 21).cuboid(-4.5f, -13f, -3.5f, 3, 2, 3, new Dilation(0.3f))
                        .uv(80, 21).cuboid(-1f, -13.4f, -1f, 3, 2, 3, new Dilation(0.3f))
                        .uv(92, 21).cuboid(2f, -13f, -3f, 3, 2, 3, new Dilation(0.3f))
                        .uv(54, 21).cuboid(-2f, -13f, 2f, 4, 2, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(104, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(242, 21).cuboid(2.6f, 2.5f, -3.2f, 1, 1, 1, new Dilation(0.15f)),
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
                        .uv(26, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(40, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
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

    /** beto_summer (1.84 bloques, 63.70 unidades). */
    public static TexturedModelData beto_summer() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(116, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(214, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(12, 30).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(236, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(122, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(216, 21).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(18, 30).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(242, 21).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(24, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(30, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(128, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(136, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(144, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(152, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(160, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(168, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(184, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(192, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(200, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(248, 21).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(0, 30).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(6, 30).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(8, 30).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(10, 30).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(208, 21).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("spikes", ModelPartBuilder.create()
                        .uv(68, 21).cuboid(-4.5f, -13f, -3.5f, 3, 2, 3, new Dilation(0.3f))
                        .uv(80, 21).cuboid(-1f, -13.4f, -1f, 3, 2, 3, new Dilation(0.3f))
                        .uv(92, 21).cuboid(2f, -13f, -3f, 3, 2, 3, new Dilation(0.3f))
                        .uv(54, 21).cuboid(-2f, -13f, 2f, 4, 2, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(104, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(0, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(214, 0).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f)),
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
                        .uv(26, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(40, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(218, 21).cuboid(-4.5f, -5.5f, -5.55f, 9, 2, 0),
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

    /** beto_winter (1.84 bloques, 65.10 unidades). */
    public static TexturedModelData beto_winter() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(22, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(56, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(26, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(182, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(24, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(62, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(32, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(68, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(74, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(188, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(196, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(204, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(212, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(228, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(236, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(244, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(0, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(8, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(38, 34).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(44, 34).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(50, 34).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(52, 34).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(54, 34).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(16, 34).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(156, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(180, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(40, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(12, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(66, 21).cuboid(-5.2f, 15f, -2.9f, 10, 2, 6, new Dilation(0.8f))
                        .uv(98, 21).cuboid(-3.2f, -0.2f, -3.2f, 6, 2, 6, new Dilation(0.5f))
                        .uv(122, 21).cuboid(1.2f, 2f, -4.2f, 2, 7, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(210, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(234, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(222, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(128, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(142, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(136, 0).cuboid(-5.5f, -12.4f, -5.5f, 11, 4, 11, new Dilation(0.6f))
                        .uv(168, 21).cuboid(-1f, -14.8f, -1f, 2, 2, 2, new Dilation(0.3f)),
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
}
