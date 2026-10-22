package com.jaciel.kiko.client.render;

import com.jaciel.kiko.entity.KikoBobberEntity;
import com.jaciel.kiko.entity.KikoEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Dibuja el corcho (con la textura del de vanilla) y el hilo desde la punta de la caña de Kiko.
 * Es igual que el render del corcho del jugador, pero calculando la mano de Kiko.
 */
public class KikoBobberRenderer extends EntityRenderer<KikoBobberEntity> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/fishing_hook.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityCutout(TEXTURE);

    public KikoBobberRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(KikoBobberEntity bobber, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider consumers, int light) {
        KikoEntity owner = bobber.getOwner();
        if (owner == null) return;

        // Corcho: un cuadrito que siempre mira a la cámara
        matrices.push();
        matrices.push();
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.multiply(this.dispatcher.getRotation());
        MatrixStack.Entry quad = matrices.peek();
        VertexConsumer vc = consumers.getBuffer(LAYER);
        vertex(vc, quad, light, 0f, 0, 0, 1);
        vertex(vc, quad, light, 1f, 0, 1, 1);
        vertex(vc, quad, light, 1f, 1, 1, 0);
        vertex(vc, quad, light, 0f, 1, 0, 0);
        matrices.pop();

        // Hilo desde la punta de la caña hasta el corcho, con su comba
        Vec3d tip = rodTip(owner, tickDelta);
        double bx = MathHelper.lerp(tickDelta, bobber.prevX, bobber.getX());
        double by = MathHelper.lerp(tickDelta, bobber.prevY, bobber.getY()) + 0.25;
        double bz = MathHelper.lerp(tickDelta, bobber.prevZ, bobber.getZ());
        float dx = (float) (tip.x - bx);
        float dy = (float) (tip.y - by);
        float dz = (float) (tip.z - bz);
        VertexConsumer line = consumers.getBuffer(RenderLayer.getLineStrip());
        MatrixStack.Entry entry = matrices.peek();
        for (int i = 0; i <= 16; i++) {
            fishingLine(dx, dy, dz, line, entry, i / 16f, (i + 1) / 16f);
        }
        matrices.pop();
        super.render(bobber, yaw, tickDelta, matrices, consumers, light);
    }

    /** Punta de la caña: mano derecha levantada al frente (pose de pesca de KikoAnimationManager). */
    private static Vec3d rodTip(KikoEntity k, float tickDelta) {
        float bodyYaw = MathHelper.lerp(tickDelta, k.prevBodyYaw, k.bodyYaw) * MathHelper.RADIANS_PER_DEGREE;
        double fx = -MathHelper.sin(bodyYaw), fz = MathHelper.cos(bodyYaw);   // al frente
        double rx = -MathHelper.cos(bodyYaw), rz = -MathHelper.sin(bodyYaw);  // a su derecha
        double x = MathHelper.lerp(tickDelta, k.prevX, k.getX());
        double y = MathHelper.lerp(tickDelta, k.prevY, k.getY());
        double z = MathHelper.lerp(tickDelta, k.prevZ, k.getZ());
        double forward = 1.16, right = 0.25, up = 1.74;
        return new Vec3d(x + fx * forward + rx * right, y + up, z + fz * forward + rz * right);
    }

    private static void vertex(VertexConsumer vc, MatrixStack.Entry m, int light, float x, int y, int u, int v) {
        vc.vertex(m, x - 0.5f, y - 0.5f, 0f).color(-1).texture(u, v).overlay(OverlayTexture.DEFAULT_UV)
                .light(light).normal(m, 0f, 1f, 0f);
    }

    private static void fishingLine(float x, float y, float z, VertexConsumer buffer, MatrixStack.Entry m, float s, float e) {
        float px = x * s;
        float py = y * (s * s + s) * 0.5f + 0.25f;
        float pz = z * s;
        float nx = x * e - px;
        float ny = y * (e * e + e) * 0.5f + 0.25f - py;
        float nz = z * e - pz;
        float len = MathHelper.sqrt(nx * nx + ny * ny + nz * nz);
        if (len < 1.0E-4f) len = 1f;
        buffer.vertex(m, px, py, pz).color(0xFF000000).normal(m, nx / len, ny / len, nz / len);
    }

    @Override
    public Identifier getTexture(KikoBobberEntity entity) {
        return TEXTURE;
    }
}
