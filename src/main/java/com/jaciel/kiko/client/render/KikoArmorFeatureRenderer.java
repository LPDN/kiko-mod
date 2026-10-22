package com.jaciel.kiko.client.render;

import com.jaciel.kiko.client.model.KikoModel;
import com.jaciel.kiko.entity.KikoEntity;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

/**
 * Dibuja la armadura encima de Kiko.
 *
 * Su modelo no es como el del jugador, así que no sirve el render de armadura de vanilla.
 * Aquí se arma un "maniquí" con los mismos cubos y UV que la armadura vanilla (textura 64x32)
 * y cada pieza se estira para cubrir la parte del cuerpo de Kiko que le toca, siguiendo su pose.
 * Funciona con cualquier armadura que use texturas de material (vanilla y la mayoría de mods).
 * Las armaduras de mods con modelo 3D propio no se ven, pero igual la protegen.
 */
public class KikoArmorFeatureRenderer extends FeatureRenderer<KikoEntity, KikoModel> {
    /** Capa 1 (casco, pechera, botas) va inflada 1.0, capa 2 (pantalones) 0.5, como en vanilla. */
    private final Pieces outer;
    private final Pieces inner;

    public KikoArmorFeatureRenderer(FeatureRendererContext<KikoEntity, KikoModel> context) {
        super(context);
        this.outer = new Pieces(1.0f);
        this.inner = new Pieces(0.5f);
    }

    /** Cubos con la UV de la armadura vanilla, ya escalados al cuerpo de Kiko. */
    private static final class Pieces {
        final ModelPart head;
        final ModelPart body;
        final ModelPart belt;
        final ModelPart rightArm, rightForearm, leftArm, leftForearm;
        final ModelPart rightThigh, rightShin, leftThigh, leftShin;

        Pieces(float inflate) {
            Dilation d = new Dilation(inflate);
            ModelData data = new ModelData();
            var root = data.getRoot();
            root.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4f, -8f, -4f, 8, 8, 8, d), ModelTransform.NONE);
            root.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4f, 0f, -2f, 8, 12, 4, d), ModelTransform.NONE);
            root.addChild("belt", ModelPartBuilder.create().uv(16, 16).cuboid(-4f, 0f, -2f, 8, 12, 4, d), ModelTransform.NONE);
            root.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-2f, -1f, -2f, 4, 12, 4, d), ModelTransform.NONE);
            root.addChild("right_forearm", ModelPartBuilder.create().uv(40, 16).cuboid(-2f, 0f, -2f, 4, 12, 4, d), ModelTransform.NONE);
            root.addChild("left_arm", ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-2f, -1f, -2f, 4, 12, 4, d), ModelTransform.NONE);
            root.addChild("left_forearm", ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-2f, 0f, -2f, 4, 12, 4, d), ModelTransform.NONE);
            root.addChild("right_thigh", ModelPartBuilder.create().uv(0, 16).cuboid(-2f, 0f, -2f, 4, 12, 4, d), ModelTransform.NONE);
            root.addChild("right_shin", ModelPartBuilder.create().uv(0, 16).cuboid(-2f, 0f, -2f, 4, 12, 4, d), ModelTransform.NONE);
            root.addChild("left_thigh", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-2f, 0f, -2f, 4, 12, 4, d), ModelTransform.NONE);
            root.addChild("left_shin", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-2f, 0f, -2f, 4, 12, 4, d), ModelTransform.NONE);
            ModelPart m = TexturedModelData.of(data, 64, 32).createModel();

            head = m.getChild("head");
            body = m.getChild("body");
            belt = m.getChild("belt");
            rightArm = m.getChild("right_arm");
            rightForearm = m.getChild("right_forearm");
            leftArm = m.getChild("left_arm");
            leftForearm = m.getChild("left_forearm");
            rightThigh = m.getChild("right_thigh");
            rightShin = m.getChild("right_shin");
            leftThigh = m.getChild("left_thigh");
            leftShin = m.getChild("left_shin");

            // Escalas para que cada cubo vanilla cubra la parte de Kiko (medidas en KikoModel)
            scale(head, 10f / 8f, 11f / 8f, 10f / 8f);           // cabeza 10x11x10
            scale(body, 10f / 8f, 14f / 12f, 1.8f);              // pecho + cintura, 14 de alto
            body.pivotZ = -1.25f;                                 // al frente para cubrir el busto
            scale(belt, 11.4f / 8f, 6.5f / 12f, 2.05f);          // cadera y glúteos
            belt.pivotY = 13.6f;
            belt.pivotZ = 0.75f;
            for (ModelPart arm : new ModelPart[]{rightArm, leftArm}) scale(arm, 0.85f, 11f / 12f, 0.85f);
            for (ModelPart arm : new ModelPart[]{rightForearm, leftForearm}) scale(arm, 0.8f, 10f / 12f, 0.8f);
            for (ModelPart leg : new ModelPart[]{rightThigh, leftThigh}) scale(leg, 1.2f, 14f / 12f, 1.2f);
            for (ModelPart leg : new ModelPart[]{rightShin, leftShin}) scale(leg, 1.0f, 15f / 12f, 1.0f);
        }

        private static void scale(ModelPart p, float x, float y, float z) {
            p.xScale = x;
            p.yScale = y;
            p.zScale = z;
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, KikoEntity entity,
                       float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isArmorHidden()) return;
        KikoModel m = this.getContextModel();

        renderPiece(matrices, vertexConsumers, light, entity.getEquippedStack(EquipmentSlot.HEAD), false,
                new ModelPart[][]{{m.root, m.head}}, new ModelPart[]{outer.head});

        renderPiece(matrices, vertexConsumers, light, entity.getEquippedStack(EquipmentSlot.CHEST), false,
                new ModelPart[][]{
                        {m.root, m.body},
                        {m.root, m.rightArm}, {m.root, m.rightArm, m.rightForearm},
                        {m.root, m.leftArm}, {m.root, m.leftArm, m.leftForearm}},
                new ModelPart[]{outer.body, outer.rightArm, outer.rightForearm, outer.leftArm, outer.leftForearm});

        renderPiece(matrices, vertexConsumers, light, entity.getEquippedStack(EquipmentSlot.LEGS), true,
                new ModelPart[][]{
                        {m.root, m.body},
                        {m.root, m.rightLeg}, {m.root, m.rightLeg, m.rightShin},
                        {m.root, m.leftLeg}, {m.root, m.leftLeg, m.leftShin}},
                new ModelPart[]{inner.belt, inner.rightThigh, inner.rightShin, inner.leftThigh, inner.leftShin});

        renderPiece(matrices, vertexConsumers, light, entity.getEquippedStack(EquipmentSlot.FEET), false,
                new ModelPart[][]{{m.root, m.rightLeg, m.rightShin}, {m.root, m.leftLeg, m.leftShin}},
                new ModelPart[]{outer.rightShin, outer.leftShin});
    }

    /**
     * @param chains  para cada pieza, la cadena de partes de Kiko que hay que seguir (raíz primero)
     * @param pieces  los cubos de armadura a dibujar en el espacio de la última parte de cada cadena
     */
    private void renderPiece(MatrixStack matrices, VertexConsumerProvider consumers, int light, ItemStack stack,
                             boolean innerLayer, ModelPart[][] chains, ModelPart[] pieces) {
        if (!(stack.getItem() instanceof ArmorItem armor)) return;
        ArmorMaterial material = armor.getMaterial().value();
        boolean dyeable = stack.isIn(ItemTags.DYEABLE);

        for (ArmorMaterial.Layer layer : material.layers()) {
            int color = layer.isDyeable() && dyeable
                    ? ColorHelper.Argb.fullAlpha(DyedColorComponent.getColor(stack, -6265536))
                    : -1;
            Identifier texture = layer.getTexture(innerLayer);
            VertexConsumer vc = consumers.getBuffer(RenderLayer.getArmorCutoutNoCull(texture));
            drawAll(matrices, vc, light, color, chains, pieces);
        }
        if (stack.hasGlint()) {
            drawAll(matrices, consumers.getBuffer(RenderLayer.getArmorEntityGlint()), light, -1, chains, pieces);
        }
    }

    private static void drawAll(MatrixStack matrices, VertexConsumer vc, int light, int color,
                                ModelPart[][] chains, ModelPart[] pieces) {
        for (int i = 0; i < pieces.length; i++) {
            matrices.push();
            for (ModelPart p : chains[i]) p.rotate(matrices);
            pieces[i].render(matrices, vc, light, OverlayTexture.DEFAULT_UV, color);
            matrices.pop();
        }
    }
}
