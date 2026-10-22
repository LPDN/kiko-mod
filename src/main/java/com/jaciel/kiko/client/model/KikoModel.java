package com.jaciel.kiko.client.model;

import com.jaciel.kiko.KikoMod;
import com.jaciel.kiko.client.animation.KikoAnimationManager;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.outfit.KikoOutfit;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

/**
 * Modelo de Kiko, Fase 2. Proporciones de adulta (unas 5.6 cabezas de alto), rodillas y codos
 * articulados y ojos en piezas separadas para poder animarlos.
 *
 * La geometría la escribe tools/gen_assets.py entre los marcadores. Si quieres moverla,
 * edita PARTS en ese script y vuelve a correrlo, así la textura y el modelo siempre cuadran.
 *
 * Jerarquía
 *  root
 *   ├─ head ─ hair_back, ponytail, clip, hat, hardhat, beanie
 *   │        ├─ right_eye ─ right_pupil      left_eye ─ left_pupil
 *   │        ├─ right/left_eye_closed, right/left_eye_happy
 *   │        └─ right/left_brow, mouth_*, blush, sweat, tears, anger (expresiones)
 *   ├─ body ─ skirt (─ dress), hood, scarf
 *   ├─ right_arm ─ right_forearm      left_arm ─ left_forearm
 *   └─ right_leg ─ right_shin         left_leg ─ left_shin
 */
public class KikoModel extends SinglePartEntityModel<KikoEntity> implements ModelWithArms {
    /**
     * El renderer encoge todo el modelo (unas 0.4x), así que el ítem en la mano se agranda
     * para que se vea del tamaño normal. Largo del antebrazo en unidades del modelo.
     */
    private static final float ITEM_SCALE = 2.0f;
    private static final float FOREARM_LENGTH = 10f;

    public static final String[] MOUTH_NAMES = {"neutral", "smile", "grin", "open", "laugh", "o", "frown", "pout", "smirk", "sleepy"};

    public static final EntityModelLayer LAYER = new EntityModelLayer(KikoMod.id("kiko"), "main");

    public final ModelPart root;
    public final ModelPart head;
    public final ModelPart hairBack;
    public final ModelPart ponytail;
    public final ModelPart clip;
    public final ModelPart rightEye;
    public final ModelPart leftEye;
    public final ModelPart rightPupil;
    public final ModelPart leftPupil;
    public final ModelPart rightEyeClosed;
    public final ModelPart leftEyeClosed;
    public final ModelPart rightEyeHappy;
    public final ModelPart leftEyeHappy;
    // Fase 4: cara
    public final ModelPart rightBrow;
    public final ModelPart leftBrow;
    /** Bocas por nombre (neutral, smile, grin, open, laugh, o, frown, pout, smirk, sleepy). */
    public final java.util.Map<String, ModelPart> mouths = new java.util.LinkedHashMap<>();
    public final ModelPart blush;
    public final ModelPart sweat;
    public final ModelPart tears;
    public final ModelPart anger;
    public final ModelPart body;
    public final ModelPart skirt;
    public final ModelPart hood;
    public final ModelPart dress;
    public final ModelPart scarf;
    public final ModelPart hat;
    public final ModelPart hardhat;
    public final ModelPart beanie;
    /** Velo de novia de Kiko (null en las demás chicas). */
    @org.jetbrains.annotations.Nullable
    public final ModelPart kikoVeil;
    private final ModelPart[] extraHats;
    private final ModelPart[] hairStyles;
    public final ModelPart rightArm;
    public final ModelPart rightForearm;
    public final ModelPart leftArm;
    public final ModelPart leftForearm;
    public final ModelPart rightLeg;
    public final ModelPart rightShin;
    public final ModelPart leftLeg;
    public final ModelPart leftShin;

    public KikoModel(ModelPart modelRoot) {
        this.root = modelRoot.getChild("root");
        this.head = root.getChild("head");
        this.hairBack = head.getChild("hair_back");
        this.ponytail = head.getChild("ponytail");
        this.clip = head.getChild("clip");
        this.rightEye = head.getChild("right_eye");
        this.leftEye = head.getChild("left_eye");
        this.rightPupil = rightEye.getChild("right_pupil");
        this.leftPupil = leftEye.getChild("left_pupil");
        this.rightEyeClosed = head.getChild("right_eye_closed");
        this.leftEyeClosed = head.getChild("left_eye_closed");
        this.rightEyeHappy = head.getChild("right_eye_happy");
        this.leftEyeHappy = head.getChild("left_eye_happy");
        this.rightBrow = head.getChild("right_brow");
        this.leftBrow = head.getChild("left_brow");
        for (String m : MOUTH_NAMES) mouths.put(m, head.getChild("mouth_" + m));
        this.blush = head.getChild("blush");
        this.sweat = head.getChild("sweat");
        this.tears = head.getChild("tears");
        this.anger = head.getChild("anger");
        this.body = root.getChild("body");
        this.skirt = body.getChild("skirt");
        this.hood = body.getChild("hood");
        this.dress = skirt.getChild("dress");
        this.scarf = body.getChild("scarf");
        this.hat = head.getChild("hat");
        this.hardhat = head.getChild("hardhat");
        this.beanie = head.getChild("beanie");
        // El velo de novia solo lo trae el modelo de Kiko (las demás lo traen en su modelo de boda)
        this.kikoVeil = head.hasChild("kiko_veil") ? head.getChild("kiko_veil") : null;
        // v3.3 sombreros de los chicos y de la ropa de temporada (se esconden con casco)
        java.util.List<ModelPart> hats = new java.util.ArrayList<>();
        for (String n : new String[]{"guy_hat", "sun_hat", "winter_hat"}) if (head.hasChild(n)) hats.add(head.getChild(n));
        this.extraHats = hats.toArray(new ModelPart[0]);
        // v3.4 peinados extra de los vecinos (greñas, chinitos, copete)
        java.util.List<ModelPart> styles = new java.util.ArrayList<>();
        for (String n : new String[]{"hs_long", "hs_curly", "hs_quiff"}) styles.add(head.hasChild(n) ? head.getChild(n) : null);
        this.hairStyles = styles.toArray(new ModelPart[0]);
        this.rightArm = root.getChild("right_arm");
        this.rightForearm = rightArm.getChild("right_forearm");
        this.leftArm = root.getChild("left_arm");
        this.leftForearm = leftArm.getChild("left_forearm");
        this.rightLeg = root.getChild("right_leg");
        this.rightShin = rightLeg.getChild("right_shin");
        this.leftLeg = root.getChild("left_leg");
        this.leftShin = leftLeg.getChild("left_shin");
        this.hipHeight = Math.abs(rightLeg.pivotY);
    }

    /** v3.9 qué tan alta está la cadera (en unidades del modelo), para sentarlas. */
    public final float hipHeight;

    /** v3.4 prende uno de los peinados extra (1 greñas, 2 chinitos, 3 copete; 0 ninguno). */
    public void setHairStyle(int style) {
        for (int i = 0; i < hairStyles.length; i++) if (hairStyles[i] != null) hairStyles[i].visible = style == i + 1;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        // <gen>
        ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));
        ModelPartData headD = root.addChild("head", ModelPartBuilder.create()
                        .uv(22, 0).cuboid(-5f, -11f, -5f, 10, 11, 10)
                        .uv(62, 0).cuboid(-5f, -11f, -5f, 10, 11, 10, new Dilation(0.75f)),
                        ModelTransform.pivot(0f, -50f, 0f));
        headD.addChild("hair_back", ModelPartBuilder.create()
                        .uv(102, 0).cuboid(-5.5f, 0f, -0.6f, 11, 19, 2),
                        ModelTransform.pivot(0f, -6f, 6.2f));
        headD.addChild("ponytail", ModelPartBuilder.create()
                        .uv(0, 25).cuboid(-1f, 0.5f, -1.5f, 3, 13, 3),
                        ModelTransform.of(6f, -8.5f, 0.5f, 0f, 0f, -0.18f));
        headD.addChild("clip", ModelPartBuilder.create()
                        .uv(40, 60).cuboid(-4.5f, -10f, -6f, 2, 2, 1),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData rightEyeD = headD.addChild("right_eye", ModelPartBuilder.create()
                        .uv(116, 54).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(-2.5f, -3f, -5.15f));
        rightEyeD.addChild("right_pupil", ModelPartBuilder.create()
                        .uv(130, 60).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("right_eye_closed", ModelPartBuilder.create()
                        .uv(140, 60).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        headD.addChild("right_eye_happy", ModelPartBuilder.create()
                        .uv(74, 60).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(-2.5f, -4f, -5.15f));
        ModelPartData leftEyeD = headD.addChild("left_eye", ModelPartBuilder.create()
                        .uv(122, 54).cuboid(-1.5f, -4f, 0f, 3, 4, 0),
                        ModelTransform.pivot(2.5f, -3f, -5.15f));
        leftEyeD.addChild("left_pupil", ModelPartBuilder.create()
                        .uv(132, 60).cuboid(-0.5f, -2f, 0f, 1, 2, 0),
                        ModelTransform.pivot(0f, 0f, -0.15f));
        headD.addChild("left_eye_closed", ModelPartBuilder.create()
                        .uv(146, 60).cuboid(-1.5f, -1f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("left_eye_happy", ModelPartBuilder.create()
                        .uv(80, 60).cuboid(-1.5f, -2f, 0f, 3, 2, 0),
                        ModelTransform.pivot(2.5f, -4f, -5.15f));
        headD.addChild("right_brow", ModelPartBuilder.create()
                        .uv(152, 60).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(-2.5f, -7.5f, -5.2f));
        headD.addChild("left_brow", ModelPartBuilder.create()
                        .uv(158, 60).cuboid(-1.5f, -0.5f, 0f, 3, 1, 0),
                        ModelTransform.pivot(2.5f, -7.5f, -5.2f));
        headD.addChild("mouth_neutral", ModelPartBuilder.create()
                        .uv(214, 54).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smile", ModelPartBuilder.create()
                        .uv(222, 54).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_grin", ModelPartBuilder.create()
                        .uv(230, 54).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_open", ModelPartBuilder.create()
                        .uv(238, 54).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_laugh", ModelPartBuilder.create()
                        .uv(246, 54).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_o", ModelPartBuilder.create()
                        .uv(0, 60).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_frown", ModelPartBuilder.create()
                        .uv(8, 60).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_pout", ModelPartBuilder.create()
                        .uv(16, 60).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_smirk", ModelPartBuilder.create()
                        .uv(24, 60).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("mouth_sleepy", ModelPartBuilder.create()
                        .uv(32, 60).cuboid(-2f, 0f, 0f, 4, 3, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("blush", ModelPartBuilder.create()
                        .uv(86, 60).cuboid(-4.5f, -1.5f, -0.03f, 3, 2, 0)
                        .uv(92, 60).cuboid(1.5f, -1.5f, -0.03f, 3, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("sweat", ModelPartBuilder.create()
                        .uv(134, 60).cuboid(-0.5f, -0.5f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(4.5f, -7f, -5.15f));
        headD.addChild("tears", ModelPartBuilder.create()
                        .uv(136, 60).cuboid(-4f, -1f, -0.04f, 1, 2, 0)
                        .uv(138, 60).cuboid(3f, -1f, -0.04f, 1, 2, 0),
                        ModelTransform.pivot(0f, -2f, -5.15f));
        headD.addChild("anger", ModelPartBuilder.create()
                        .uv(46, 60).cuboid(-1.5f, -1.5f, 0f, 3, 3, 0),
                        ModelTransform.pivot(3.5f, -10f, -5.9f));
        ModelPartData bodyD = root.addChild("body", ModelPartBuilder.create()
                        .uv(180, 25).cuboid(-4.5f, 0f, -2.5f, 9, 9, 5, new Dilation(0.45f))
                        .uv(58, 60).cuboid(-3.5f, 1.6f, -3.8f, 7, 1, 1, new Dilation(0.25f))
                        .uv(134, 54).cuboid(-4f, 2.7f, -4.7f, 8, 1, 2, new Dilation(0.3f))
                        .uv(228, 41).cuboid(-4.5f, 3.4f, -5.4f, 9, 4, 3, new Dilation(0.3f))
                        .uv(88, 54).cuboid(-4.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(98, 54).cuboid(0.3f, 4f, -5.9f, 4, 3, 1, new Dilation(0.25f))
                        .uv(154, 54).cuboid(-4f, 7.2f, -5f, 8, 1, 2, new Dilation(0.2f))
                        .uv(130, 41).cuboid(-4f, 9f, -2f, 8, 5, 4, new Dilation(0.35f))
                        .uv(100, 41).cuboid(-5f, 14f, -2.5f, 10, 6, 5, new Dilation(0.8f))
                        .uv(174, 54).cuboid(-4f, 14f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(186, 41).cuboid(-4.5f, 14.6f, 2f, 9, 5, 3, new Dilation(0.5f))
                        .uv(40, 54).cuboid(-4.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(50, 54).cuboid(0.3f, 15.1f, 4.55f, 4, 4, 1, new Dilation(0.25f))
                        .uv(194, 54).cuboid(-4f, 19.3f, 2f, 8, 1, 2, new Dilation(0.3f))
                        .uv(28, 54).cuboid(-1.5f, -2f, -1.5f, 3, 2, 3),
                        ModelTransform.pivot(0f, -48f, 0f));
        ModelPartData skirtD = bodyD.addChild("skirt", ModelPartBuilder.create()
                        .uv(0, 41).cuboid(-5.5f, 0f, -3f, 11, 4, 9, new Dilation(0.55f))
                        .uv(208, 0).cuboid(-6f, 4f, -3.5f, 12, 6, 10, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        bodyD.addChild("hood", ModelPartBuilder.create()
                        .uv(210, 41).cuboid(-3.5f, 0f, 2.5f, 7, 6, 2),
                        ModelTransform.pivot(0f, 0f, 0f));
        skirtD.addChild("dress", ModelPartBuilder.create()
                        .uv(160, 0).cuboid(-6.5f, 10f, -4f, 13, 6, 11, new Dilation(0.6f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        bodyD.addChild("scarf", ModelPartBuilder.create()
                        .uv(154, 41).cuboid(-5f, 0f, -3f, 10, 2, 6, new Dilation(0.35f))
                        .uv(128, 54).cuboid(1.2f, 2f, -6.4f, 2, 3, 1, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hat", ModelPartBuilder.create()
                        .uv(36, 25).cuboid(-7f, -11.4f, -7f, 14, 1, 14)
                        .uv(64, 41).cuboid(-4.5f, -14.4f, -4.5f, 9, 3, 9, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("hardhat", ModelPartBuilder.create()
                        .uv(136, 25).cuboid(-5.5f, -12.6f, -5.5f, 11, 3, 11, new Dilation(0.35f))
                        .uv(60, 54).cuboid(-5.5f, -10.3f, -8.4f, 11, 1, 3)
                        .uv(52, 60).cuboid(-1f, -12.2f, -6.9f, 2, 2, 1),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("beanie", ModelPartBuilder.create()
                        .uv(92, 25).cuboid(-5.5f, -12.5f, -5.5f, 11, 4, 11, new Dilation(0.4f))
                        .uv(108, 54).cuboid(-1f, -14.9f, -1f, 2, 2, 2, new Dilation(0.2f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        headD.addChild("kiko_veil", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.5f, -11f, 7.75f, 11, 25, 0)
                        .uv(98, 60).cuboid(-4.5f, -12.3f, -5.6f, 1, 1, 1, new Dilation(0.3f))
                        .uv(102, 60).cuboid(-2f, -12.3f, -5.9f, 1, 1, 1, new Dilation(0.3f))
                        .uv(106, 60).cuboid(0.5f, -12.3f, -5.9f, 1, 1, 1, new Dilation(0.3f))
                        .uv(110, 60).cuboid(3f, -12.3f, -5.6f, 1, 1, 1, new Dilation(0.3f))
                        .uv(114, 60).cuboid(-5.6f, -12.3f, -3f, 1, 1, 1, new Dilation(0.3f))
                        .uv(118, 60).cuboid(4.6f, -12.3f, -3f, 1, 1, 1, new Dilation(0.3f))
                        .uv(122, 60).cuboid(-5.6f, -12.3f, 0f, 1, 1, 1, new Dilation(0.3f))
                        .uv(126, 60).cuboid(4.6f, -12.3f, 0f, 1, 1, 1, new Dilation(0.3f)),
                        ModelTransform.pivot(0f, 0f, 0f));
        ModelPartData rightArmD = root.addChild("right_arm", ModelPartBuilder.create()
                        .uv(208, 25).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(-6.6f, -47.5f, 0f));
        rightArmD.addChild("right_forearm", ModelPartBuilder.create()
                        .uv(40, 41).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData leftArmD = root.addChild("left_arm", ModelPartBuilder.create()
                        .uv(220, 25).cuboid(-1.5f, -0.5f, -1.5f, 3, 11, 3, new Dilation(0.3f)),
                        ModelTransform.pivot(6.6f, -47.5f, 0f));
        leftArmD.addChild("left_forearm", ModelPartBuilder.create()
                        .uv(52, 41).cuboid(-1.5f, 0.5f, -1.5f, 3, 10, 3, new Dilation(0.1f)),
                        ModelTransform.pivot(0f, 10f, 0f));
        ModelPartData rightLegD = root.addChild("right_leg", ModelPartBuilder.create()
                        .uv(128, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.85f)),
                        ModelTransform.pivot(-2.75f, -29f, 0f));
        rightLegD.addChild("right_shin", ModelPartBuilder.create()
                        .uv(12, 25).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(0, 54).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        ModelPartData leftLegD = root.addChild("left_leg", ModelPartBuilder.create()
                        .uv(144, 0).cuboid(-2f, 0f, -2f, 4, 14, 4, new Dilation(0.85f)),
                        ModelTransform.pivot(2.75f, -29f, 0f));
        leftLegD.addChild("left_shin", ModelPartBuilder.create()
                        .uv(24, 25).cuboid(-1.5f, 0f, -1.5f, 3, 13, 3, new Dilation(0.55f))
                        .uv(14, 54).cuboid(-1.5f, 13f, -2.5f, 3, 2, 4, new Dilation(0.35f)),
                        ModelTransform.pivot(0f, 14f, 0f));
        // </gen>
        return TexturedModelData.of(data, 256, 256);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    /**
     * Lleva la matriz hasta la mano para que HeldItemFeatureRenderer dibuje ahí el ítem.
     * Vanilla espera el origen en el hombro con la mano 10 px más abajo, así que nos paramos
     * en el codo, bajamos lo necesario y escalamos.
     */
    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        ModelPart upper = arm == Arm.RIGHT ? rightArm : leftArm;
        ModelPart fore = arm == Arm.RIGHT ? rightForearm : leftForearm;
        root.rotate(matrices);
        upper.rotate(matrices);
        fore.rotate(matrices);
        // Vanilla pone el ítem 10 px (ya escalados) abajo del origen. Subimos el origen para que
        // esos 10 px escalados terminen justo en la mano y no a la altura de la rodilla.
        matrices.translate(0f, (FOREARM_LENGTH - FOREARM_LENGTH * ITEM_SCALE) / 16f, 0f);
        matrices.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
    }

    @Override
    public void setAngles(KikoEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        root.traverse().forEach(ModelPart::resetTransform);

        // Piezas que dependen de la ropa
        KikoOutfit outfit = entity.getOutfit();
        skirt.visible = outfit.hasSkirt();
        hood.visible = outfit.hasHood();
        dress.visible = outfit.hasDress();
        scarf.visible = outfit.hasScarf();
        hat.visible = outfit.hasHat();
        hardhat.visible = outfit.hasHardhat();
        beanie.visible = outfit.hasBeanie();
        if (kikoVeil != null) kikoVeil.visible = outfit == KikoOutfit.WEDDING;
        // El casco de armadura tapa los accesorios de la cabeza
        boolean helmet = !entity.isArmorHidden()
                && !entity.getEquippedStack(net.minecraft.entity.EquipmentSlot.HEAD).isEmpty();
        for (ModelPart h : extraHats) h.visible = !helmet;
        if (helmet) {
            hat.visible = false;
            hardhat.visible = false;
            beanie.visible = false;
        }

        KikoAnimationManager.animate(this, entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        // v3.1.7: el bebé sentadito en su cuna (piernas al frente, manitas al frente) mirando a su alrededor
        if (entity instanceof com.jaciel.kiko.family.KikoChildEntity kid && kid.getStage() == com.jaciel.kiko.family.KikoChildEntity.BABY) {
            float sway = net.minecraft.util.math.MathHelper.sin(animationProgress * 0.08f) * 0.05f;
            root.pitch = 0f;
            root.pivotY = 30.5f;           // baja lo que miden las piernas: las nalguitas tocan el colchón
            root.pivotZ = 0f;
            body.pitch = sway;
            rightLeg.pitch = -1.5f;
            leftLeg.pitch = -1.5f;
            rightLeg.yaw = 0.3f;
            leftLeg.yaw = -0.3f;
            rightLeg.roll = 0f;
            leftLeg.roll = 0f;
            rightShin.pitch = 0f;
            leftShin.pitch = 0f;
            rightArm.pitch = -0.7f + sway;
            leftArm.pitch = -0.7f - sway;
            rightArm.roll = 0.15f;
            leftArm.roll = -0.15f;
            rightForearm.pitch = -0.3f;
            leftForearm.pitch = -0.3f;
            rightForearm.yaw = 0f;
            leftForearm.yaw = 0f;
        }
        applyBody();
    }

    // ------------------------------------------------------------------
    // v3.4.3 cuerpos distintos (ver tools/gen_bodies.py)
    //   ancho torso, fondo torso, hombros +-, ancho piernas, cadera +-
    // ------------------------------------------------------------------
    public static final int BODY_NORMAL = 0, BODY_SLIM = 1, BODY_ATHLETIC = 2, BODY_SOFT = 3, BODY_FULL = 4;
    private static final float[][] BODIES = {
            {1.0f, 1.0f, 0.0f, 1.0f, 0.0f},
            {0.88f, 0.9f, -0.5f, 0.86f, -0.3f},
            {1.12f, 1.06f, 0.9f, 1.08f, 0.2f},
            {1.08f, 1.1f, 0.3f, 1.1f, 0.4f},
            {1.2f, 1.2f, 0.7f, 1.18f, 0.7f}};
    private int bodyType;

    /** El renderer lo pone antes de dibujar (el modelo se comparte entre todas las del mismo tipo). */
    public void setBodyType(int type) {
        this.bodyType = Math.floorMod(type, BODIES.length);
    }

    private void applyBody() {
        // La escala no se reinicia sola con resetTransform: siempre se pone completa (también la normal)
        float[] b = BODIES[bodyType];
        body.xScale = b[0];
        body.zScale = b[1];
        rightLeg.xScale = b[3];
        rightLeg.zScale = b[3];
        leftLeg.xScale = b[3];
        leftLeg.zScale = b[3];
        if (bodyType == BODY_NORMAL) return;
        rightArm.pivotX -= b[2];
        leftArm.pivotX += b[2];
        rightLeg.pivotX -= b[4];
        leftLeg.pivotX += b[4];
    }
}
