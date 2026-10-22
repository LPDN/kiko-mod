package com.jaciel.kiko.client.render;

import com.jaciel.kiko.KikoMod;
import com.jaciel.kiko.entity.SoccerBallEntity;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

/** Dibuja la pelota de futbol: un cubito de 8 px que gira según hacia dónde rueda. */
public class SoccerBallRenderer extends EntityRenderer<SoccerBallEntity> {
    public static final EntityModelLayer LAYER = new EntityModelLayer(KikoMod.id("soccer_ball"), "main");
    private static final Identifier TEXTURE = KikoMod.id("textures/entity/soccer_ball.png");

    private final ModelPart ball;

    public SoccerBallRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.ball = ctx.getPart(LAYER).getChild("ball");
        this.shadowRadius = 0.25f;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        root.addChild("ball", ModelPartBuilder.create().uv(0, 0).cuboid(-4f, -4f, -4f, 8, 8, 8, Dilation.NONE), ModelTransform.NONE);
        return TexturedModelData.of(data, 32, 16);
    }

    @Override
    public void render(SoccerBallEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider consumers, int light) {
        matrices.push();
        matrices.translate(0.0, SoccerBallEntity.RADIUS, 0.0);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.rollYaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(MathHelper.lerp(tickDelta, entity.prevRoll, entity.roll)));
        ball.render(matrices, consumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, consumers, light);
    }

    @Override
    public Identifier getTexture(SoccerBallEntity entity) {
        return TEXTURE;
    }
}
