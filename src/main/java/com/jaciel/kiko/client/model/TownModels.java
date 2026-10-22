package com.jaciel.kiko.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;

/**
 * Gente del pueblo (v3.4). Los genera tools/gen_townsfolk.py (no editar a mano).
 * Texturas pintables como las de los hijos (textures/entity/kid/), así cada vecino sale con sus colores.
 * Los niños del pueblo usan los modelos de KidModels.
 */
@SuppressWarnings("unused")
public final class TownModels {
    private TownModels() {}

    /** Alto de cada modelo en unidades (para escalarlo a su altura en bloques). */
    public static final java.util.Map<String, Float> UNITS = java.util.Map.ofEntries(
        java.util.Map.entry("man_shirt", 61.750f),
        java.util.Map.entry("man_farmer", 61.750f),
        java.util.Map.entry("man_vest", 61.750f),
        java.util.Map.entry("man_baker", 61.750f),
        java.util.Map.entry("man_elder", 61.750f),
        java.util.Map.entry("woman_dress", 61.750f),
        java.util.Map.entry("woman_bun", 61.750f),
        java.util.Map.entry("woman_bob", 61.750f),
        java.util.Map.entry("woman_braid", 61.750f),
        java.util.Map.entry("woman_elder", 61.750f),
        java.util.Map.entry("guard_man", 61.750f),
        java.util.Map.entry("guard_woman", 61.750f));

    /** man_shirt (1.85 bloques, 61.75 unidades de alto). */
    public static TexturedModelData man_shirt() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(194, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(38, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(94, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(64, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(200, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(40, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(100, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(70, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(106, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(112, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(206, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(214, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(222, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(230, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(238, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(246, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(0, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(8, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(16, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(24, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(76, 34).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(82, 34).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(88, 34).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(90, 34).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(92, 34).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(32, 34).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(60, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(12, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f)),
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
                        .uv(136, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(150, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("hs_long", ModelPartBuilder.create()
                        .uv(86, 21).cuboid(-5.5f, -9f, 4.8f, 11, 8, 1, new Dilation(0.35f))
                        .uv(40, 21).cuboid(-5.9f, -8f, 1f, 1, 6, 4, new Dilation(0.25f))
                        .uv(50, 21).cuboid(4.9f, -8f, 1f, 1, 6, 4, new Dilation(0.25f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_curly", ModelPartBuilder.create()
                        .uv(166, 0).cuboid(-5.5f, -12.2f, -5.3f, 11, 3, 11, new Dilation(0.55f))
                        .uv(42, 34).cuboid(-5f, -9.6f, -6.1f, 10, 1, 1, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_quiff", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-3.5f, -12.4f, -5.8f, 7, 2, 6, new Dilation(0.35f))
                        .uv(176, 21).cuboid(-3f, -12.9f, -6.8f, 6, 1, 3, new Dilation(0.3f)),
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

    /** man_farmer (1.85 bloques, 61.75 unidades de alto). */
    public static TexturedModelData man_farmer() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(58, 35).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(156, 35).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(212, 35).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(182, 35).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(64, 35).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(158, 35).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(218, 35).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(188, 35).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(224, 35).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(230, 35).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(70, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(78, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(86, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(94, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(102, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(110, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(118, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(126, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(134, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(142, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(194, 35).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(200, 35).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(206, 35).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(208, 35).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(210, 35).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(150, 35).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(28, 35).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(200, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(176, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(128, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(44, 21).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(68, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(56, 21).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(80, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(176, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(0, 35).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(188, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(14, 35).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-8f, -11.2f, -8f, 16, 1, 16)
                        .uv(92, 21).cuboid(-4.5f, -14.2f, -4.5f, 9, 3, 9, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_long", ModelPartBuilder.create()
                        .uv(202, 21).cuboid(-5.5f, -9f, 4.8f, 11, 8, 1, new Dilation(0.35f))
                        .uv(156, 21).cuboid(-5.9f, -8f, 1f, 1, 6, 4, new Dilation(0.25f))
                        .uv(166, 21).cuboid(4.9f, -8f, 1f, 1, 6, 4, new Dilation(0.25f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_curly", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-5.5f, -12.2f, -5.3f, 11, 3, 11, new Dilation(0.55f))
                        .uv(160, 35).cuboid(-5f, -9.6f, -6.1f, 10, 1, 1, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_quiff", ModelPartBuilder.create()
                        .uv(226, 21).cuboid(-3.5f, -12.4f, -5.8f, 7, 2, 6, new Dilation(0.35f))
                        .uv(40, 35).cuboid(-3f, -12.9f, -6.8f, 6, 1, 3, new Dilation(0.3f)),
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

    /** man_vest (1.85 bloques, 61.75 unidades de alto). */
    public static TexturedModelData man_vest() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(56, 35).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(154, 35).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(210, 35).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(180, 35).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(62, 35).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(156, 35).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(216, 35).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(186, 35).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(222, 35).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(228, 35).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(68, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(76, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(84, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(92, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(100, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(108, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(116, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(124, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(132, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(140, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(192, 35).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(198, 35).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(204, 35).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(206, 35).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(208, 35).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(148, 35).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(26, 35).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(166, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(128, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(80, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(80, 0).cuboid(-4.8f, 0f, -2.8f, 10, 15, 5, new Dilation(0.8f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(240, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(56, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(68, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(110, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(142, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(204, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(126, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(154, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(218, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(12, 21).cuboid(-5.5f, -11.5f, -5.5f, 11, 2, 11, new Dilation(0.45f))
                        .uv(0, 35).cuboid(-4.5f, -10.6f, -9f, 9, 1, 4),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_long", ModelPartBuilder.create()
                        .uv(154, 21).cuboid(-5.5f, -9f, 4.8f, 11, 8, 1, new Dilation(0.35f))
                        .uv(108, 21).cuboid(-5.9f, -8f, 1f, 1, 6, 4, new Dilation(0.25f))
                        .uv(118, 21).cuboid(4.9f, -8f, 1f, 1, 6, 4, new Dilation(0.25f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_curly", ModelPartBuilder.create()
                        .uv(196, 0).cuboid(-5.5f, -12.2f, -5.3f, 11, 3, 11, new Dilation(0.55f))
                        .uv(158, 35).cuboid(-5f, -9.6f, -6.1f, 10, 1, 1, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_quiff", ModelPartBuilder.create()
                        .uv(178, 21).cuboid(-3.5f, -12.4f, -5.8f, 7, 2, 6, new Dilation(0.35f))
                        .uv(38, 35).cuboid(-3f, -12.9f, -6.8f, 6, 1, 3, new Dilation(0.3f)),
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

    /** man_baker (1.85 bloques, 61.75 unidades de alto). */
    public static TexturedModelData man_baker() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(206, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(54, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(110, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(80, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(212, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(56, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(116, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(86, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(122, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(128, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(218, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(226, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(234, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(242, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
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
                        .uv(92, 34).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(98, 34).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(104, 34).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(106, 34).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(108, 34).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(48, 34).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(154, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(72, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(24, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(80, 0).cuboid(-4f, 4f, -3.4f, 8, 18, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(228, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(240, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(12, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(98, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(130, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(148, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(114, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(142, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(162, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("hs_long", ModelPartBuilder.create()
                        .uv(98, 21).cuboid(-5.5f, -9f, 4.8f, 11, 8, 1, new Dilation(0.35f))
                        .uv(52, 21).cuboid(-5.9f, -8f, 1f, 1, 6, 4, new Dilation(0.25f))
                        .uv(62, 21).cuboid(4.9f, -8f, 1f, 1, 6, 4, new Dilation(0.25f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_curly", ModelPartBuilder.create()
                        .uv(184, 0).cuboid(-5.5f, -12.2f, -5.3f, 11, 3, 11, new Dilation(0.55f))
                        .uv(58, 34).cuboid(-5f, -9.6f, -6.1f, 10, 1, 1, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_quiff", ModelPartBuilder.create()
                        .uv(122, 21).cuboid(-3.5f, -12.4f, -5.8f, 7, 2, 6, new Dilation(0.35f))
                        .uv(188, 21).cuboid(-3f, -12.9f, -6.8f, 6, 1, 3, new Dilation(0.3f)),
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

    /** man_elder (1.75 bloques, 61.75 unidades de alto). */
    public static TexturedModelData man_elder() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(214, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(62, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(118, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(88, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(64, 34).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(124, 34).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(94, 34).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(130, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(136, 34).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(226, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(234, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(242, 21).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(0, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(8, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(16, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(24, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(32, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(40, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(48, 34).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(100, 34).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(106, 34).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(112, 34).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(114, 34).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(116, 34).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(56, 34).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(164, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(136, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(60, 21).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(12, 21).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f)),
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
                        .uv(136, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(150, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("npc_glasses", ModelPartBuilder.create()
                        .uv(176, 21).cuboid(-5f, -6.5f, -5.5f, 10, 4, 0),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_long", ModelPartBuilder.create()
                        .uv(86, 21).cuboid(-5.5f, -9f, 4.8f, 11, 8, 1, new Dilation(0.35f))
                        .uv(40, 21).cuboid(-5.9f, -8f, 1f, 1, 6, 4, new Dilation(0.25f))
                        .uv(50, 21).cuboid(4.9f, -8f, 1f, 1, 6, 4, new Dilation(0.25f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_curly", ModelPartBuilder.create()
                        .uv(166, 0).cuboid(-5.5f, -12.2f, -5.3f, 11, 3, 11, new Dilation(0.55f))
                        .uv(66, 34).cuboid(-5f, -9.6f, -6.1f, 10, 1, 1, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_quiff", ModelPartBuilder.create()
                        .uv(110, 21).cuboid(-3.5f, -12.4f, -5.8f, 7, 2, 6, new Dilation(0.35f))
                        .uv(196, 21).cuboid(-3f, -12.9f, -6.8f, 6, 1, 3, new Dilation(0.3f)),
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

    /** woman_dress (1.72 bloques, 61.75 unidades de alto). */
    public static TexturedModelData woman_dress() {
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

    /** woman_bun (1.72 bloques, 61.75 unidades de alto). */
    public static TexturedModelData woman_bun() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(36, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(76, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(208, 22).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
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
                        .uv(214, 22).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
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
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(188, 0).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(42, 22).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(12, 22).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(146, 35).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(220, 22).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(90, 22).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(188, 22).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(198, 22).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(0, 35).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(20, 35).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(66, 22).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(168, 22).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(178, 22).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(40, 35).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(156, 22).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(116, 0).cuboid(-3.5f, 8f, -3.6f, 7, 20, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("kid_skirt", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, 0f, -3.2f, 11, 15, 7, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 17f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(216, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(-6.6f, -47f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(240, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(228, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(6.6f, -47f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(0, 22).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(132, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(164, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(128, 22).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(148, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(176, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(142, 22).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(114, 22).cuboid(-2f, -3f, -0.6f, 4, 4, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, -8f, 6f));
        headD.addChild("hair_back", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -6f, 6.2f));
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

    /** woman_bob (1.72 bloques, 61.75 unidades de alto). */
    public static TexturedModelData woman_bob() {
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
                        .uv(146, 30).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(156, 30).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(122, 30).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(184, 21).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(148, 30).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(162, 30).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(128, 30).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(168, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(174, 30).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(20, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(28, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(36, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(44, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(52, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(60, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(68, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(76, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(84, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(92, 30).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(134, 30).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(140, 30).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(150, 30).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(152, 30).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(154, 30).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(100, 30).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-5.5f, 0f, -0.6f, 11, 7, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(136, 0).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(26, 21).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(212, 0).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(106, 30).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(190, 21).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(74, 21).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(158, 21).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(168, 21).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(210, 21).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(230, 21).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(50, 21).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(138, 21).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(148, 21).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(0, 30).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(126, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(164, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(-6.6f, -47f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(188, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(176, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(6.6f, -47f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(200, 0).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(98, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(112, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
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

    /** woman_braid (1.72 bloques, 61.75 unidades de alto). */
    public static TexturedModelData woman_braid() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(20, 35).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(238, 35).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(248, 35).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(214, 35).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(26, 35).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(240, 35).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(0, 39).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(220, 35).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(6, 39).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(12, 39).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(112, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(120, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(128, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(136, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(144, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(152, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(160, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(168, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(176, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(184, 35).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(226, 35).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(232, 35).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(242, 35).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(244, 35).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(246, 35).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(192, 35).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(210, 0).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(102, 21).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(72, 21).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(198, 35).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(32, 35).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(150, 21).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(0, 35).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(10, 35).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(52, 35).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(72, 35).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(126, 21).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(214, 21).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(224, 21).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(92, 35).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(202, 21).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(238, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(-6.6f, -47f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(12, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(6.6f, -47f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(24, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(176, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(174, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(188, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(188, 21).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(200, 0).cuboid(-1.5f, 0f, 0f, 3, 14, 2, new Dilation(0.3f))
                        .uv(234, 21).cuboid(-2f, -0.5f, -0.4f, 4, 1, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, -8f, 6f));
        headD.addChild("guy_hat", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-8f, -11.2f, -8f, 16, 1, 16)
                        .uv(36, 21).cuboid(-4.5f, -14.2f, -4.5f, 9, 3, 9, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -6f, 6.2f));
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

    /** woman_elder (1.62 bloques, 61.75 unidades de alto). */
    public static TexturedModelData woman_elder() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(36, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(76, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(240, 25).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(206, 38).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(216, 38).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(182, 38).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(246, 25).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(208, 38).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(222, 38).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(188, 38).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(228, 38).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(234, 38).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(80, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(88, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(96, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(104, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(112, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(120, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(128, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(136, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(144, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(152, 38).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(194, 38).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(200, 38).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(210, 38).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(212, 38).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(214, 38).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(160, 38).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(172, 0).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(54, 25).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(24, 25).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(166, 38).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(0, 38).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(102, 25).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(220, 25).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(230, 25).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(20, 38).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(40, 38).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(78, 25).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(180, 25).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(190, 25).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(60, 38).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(168, 25).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(224, 0).cuboid(-5.2f, 0f, -3f, 10, 7, 6, new Dilation(0.7f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        bodyD.addChild("kid_skirt", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, 0f, -3.2f, 11, 18, 7, new Dilation(0.45f)),
                        ModelTransform.pivot(0f, 17f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(200, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(-6.6f, -47f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(0, 25).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(212, 0).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f)),
                        ModelTransform.of(6.6f, -47f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(12, 25).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(116, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(148, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(140, 25).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(132, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(160, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(154, 25).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(126, 25).cuboid(-2f, -3f, -0.6f, 4, 4, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, -8f, 6f));
        headD.addChild("npc_glasses", ModelPartBuilder.create()
                        .uv(200, 25).cuboid(-5f, -6.5f, -5.5f, 10, 4, 0),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -6f, 6.2f));
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

    /** guard_man (1.95 bloques, 61.75 unidades de alto). */
    public static TexturedModelData guard_man() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(246, 36).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(92, 47).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(126, 47).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(96, 47).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(0, 47).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(94, 47).cuboid(-0.5f, -3f, 0f, 1, 3, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(132, 47).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(102, 47).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(138, 47).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(144, 47).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(6, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(14, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(22, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(30, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(38, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(46, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(54, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(62, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(70, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(78, 47).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(108, 47).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(114, 47).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(120, 47).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(122, 47).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(124, 47).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(86, 47).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(222, 36).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(176, 0).cuboid(-5f, 0f, -2.5f, 10, 10, 5, new Dilation(0.55f))
                        .uv(70, 36).cuboid(-4.5f, 10f, -2.25f, 9, 5, 4, new Dilation(0.5f))
                        .uv(12, 36).cuboid(-4.5f, 15f, -2.5f, 9, 5, 5, new Dilation(0.6f))
                        .uv(206, 0).cuboid(-5f, 1f, -2.9f, 10, 10, 5, new Dilation(0.95f))
                        .uv(160, 36).cuboid(-5f, 12f, -2.6f, 10, 2, 5, new Dilation(0.85f))
                        .uv(40, 36).cuboid(-5f, 15f, -2.8f, 10, 4, 5, new Dilation(0.9f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(22, 21).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f))
                        .uv(96, 36).cuboid(-2.5f, -0.5f, -2.5f, 5, 4, 5, new Dilation(0.6f)),
                        ModelTransform.of(-7.1f, -47f, 0f, 0f, 0f, 0.08f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(68, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f))
                        .uv(136, 36).cuboid(-1.5f, 3.5f, -1.5f, 3, 5, 3, new Dilation(0.55f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(34, 21).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.65f))
                        .uv(116, 36).cuboid(-2.5f, -0.5f, -2.5f, 5, 4, 5, new Dilation(0.6f)),
                        ModelTransform.of(7.1f, -47f, 0f, 0f, 0f, -0.08f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(80, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.4f))
                        .uv(148, 36).cuboid(-1.5f, 3.5f, -1.5f, 3, 5, 3, new Dilation(0.55f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(-2.6f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(112, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(190, 36).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f))
                        .uv(240, 21).cuboid(-1.5f, 2f, -1.5f, 3, 8, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.8f)),
                        ModelTransform.pivot(2.6f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(124, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.6f))
                        .uv(204, 36).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.4f))
                        .uv(0, 36).cuboid(-1.5f, 2f, -1.5f, 3, 8, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("guard_helmet", ModelPartBuilder.create()
                        .uv(136, 0).cuboid(-5f, -10.5f, -5f, 10, 5, 10, new Dilation(1.05f))
                        .uv(180, 21).cuboid(-5f, -5.8f, -5f, 10, 1, 10, new Dilation(1.1f))
                        .uv(218, 36).cuboid(-0.5f, -8.5f, -5.9f, 1, 5, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_long", ModelPartBuilder.create()
                        .uv(220, 21).cuboid(-1f, -14.4f, -4f, 2, 3, 8, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_curly", ModelPartBuilder.create()
                        .uv(46, 21).cuboid(-1f, -15.4f, -4.5f, 2, 4, 9, new Dilation(0.35f))
                        .uv(92, 21).cuboid(-5.5f, -5.8f, -5.5f, 11, 1, 11, new Dilation(0.95f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_quiff", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-1f, -17.4f, -4.5f, 2, 6, 9, new Dilation(0.4f))
                        .uv(136, 21).cuboid(-5.5f, -5.8f, -5.5f, 11, 1, 11, new Dilation(0.95f))
                        .uv(234, 36).cuboid(-1.5f, -11.2f, -1.5f, 3, 1, 3, new Dilation(0.4f)),
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

    /** guard_woman (1.8 bloques, 61.75 unidades de alto). */
    public static TexturedModelData guard_woman() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(40, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(96, 46).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(62, 52).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(72, 52).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(38, 52).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(102, 46).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(64, 52).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(78, 52).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(44, 52).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(84, 52).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(90, 52).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(188, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(196, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(204, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(212, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(220, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(228, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(236, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(244, 46).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(0, 52).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(8, 52).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(50, 52).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(56, 52).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(66, 52).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(68, 52).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(70, 52).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(16, 52).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(0, 21).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(74, 35).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(226, 21).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(22, 52).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(108, 46).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(216, 35).cuboid(-4.5f, 3.4f, -3.37f, 9, 4, 3, new Dilation(0.3f))
                        .uv(76, 46).cuboid(-4.3f, 4f, -3.52f, 4, 3, 1, new Dilation(0.25f))
                        .uv(86, 46).cuboid(0.3f, 4f, -3.52f, 4, 3, 1, new Dilation(0.25f))
                        .uv(128, 46).cuboid(-4f, 7.2f, -3.25f, 8, 1, 2, new Dilation(0.2f))
                        .uv(148, 46).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(138, 35).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(30, 46).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(40, 46).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(168, 46).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(18, 46).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3)
                        .uv(112, 0).cuboid(-5f, 1f, -3.4f, 10, 10, 6, new Dilation(0.95f))
                        .uv(186, 35).cuboid(-5f, 12f, -2.6f, 10, 2, 5, new Dilation(0.85f))
                        .uv(44, 35).cuboid(-5f, 15f, -2.8f, 10, 4, 5, new Dilation(0.9f)),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(28, 21).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f))
                        .uv(98, 35).cuboid(-2.5f, -0.5f, -2.5f, 5, 4, 5, new Dilation(0.6f)),
                        ModelTransform.of(-6.6f, -47f, 0f, 0f, 0f, 0.07f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(74, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.35f))
                        .uv(162, 35).cuboid(-1.5f, 3.5f, -1.5f, 3, 5, 3, new Dilation(0.55f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(40, 21).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.5f))
                        .uv(118, 35).cuboid(-2.5f, -0.5f, -2.5f, 5, 4, 5, new Dilation(0.6f)),
                        ModelTransform.of(6.6f, -47f, 0f, 0f, 0f, -0.07f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(86, 21).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.35f))
                        .uv(174, 35).cuboid(-1.5f, 3.5f, -1.5f, 3, 5, 3, new Dilation(0.55f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(80, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(144, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(240, 35).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f))
                        .uv(20, 35).cuboid(-1.5f, 2f, -1.5f, 3, 8, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(96, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.6f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(156, 0).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.45f))
                        .uv(0, 46).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.3f))
                        .uv(32, 35).cuboid(-1.5f, 2f, -1.5f, 3, 8, 3, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(168, 0).cuboid(-1.5f, 0f, 0f, 3, 14, 2, new Dilation(0.3f))
                        .uv(50, 46).cuboid(-2f, -0.5f, -0.4f, 4, 1, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, -8f, 6f));
        headD.addChild("guard_helmet", ModelPartBuilder.create()
                        .uv(178, 0).cuboid(-5f, -10.5f, -5f, 10, 5, 10, new Dilation(1.05f))
                        .uv(186, 21).cuboid(-5f, -5.8f, -5f, 10, 1, 10, new Dilation(1.1f))
                        .uv(14, 46).cuboid(-0.5f, -8.5f, -5.9f, 1, 5, 1, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_long", ModelPartBuilder.create()
                        .uv(0, 35).cuboid(-1f, -14.4f, -4f, 2, 3, 8, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_curly", ModelPartBuilder.create()
                        .uv(52, 21).cuboid(-1f, -15.4f, -4.5f, 2, 4, 9, new Dilation(0.35f))
                        .uv(98, 21).cuboid(-5.5f, -5.8f, -5.5f, 11, 1, 11, new Dilation(0.95f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hs_quiff", ModelPartBuilder.create()
                        .uv(218, 0).cuboid(-1f, -17.4f, -4.5f, 2, 6, 9, new Dilation(0.4f))
                        .uv(142, 21).cuboid(-5.5f, -5.8f, -5.5f, 11, 1, 11, new Dilation(0.95f))
                        .uv(64, 46).cuboid(-1.5f, -11.2f, -1.5f, 3, 1, 3, new Dilation(0.4f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create(),
                        ModelTransform.pivot(0f, -6f, 6.2f));
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
