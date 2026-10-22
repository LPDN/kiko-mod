package com.jaciel.kiko.client.render;

import com.jaciel.kiko.client.model.KikoModel;
import com.jaciel.kiko.entity.KikoEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

/**
 * Dibuja a Kiko. La textura cambia según el outfit (una por conjunto).
 * El modelo mide 61.75 unidades de alto (con todo y pelo), lo escalamos para que quede en 1.8 bloques (como el jugador).
 */
public class KikoRenderer extends MobEntityRenderer<KikoEntity, KikoModel> {
    /** Escala para que el modelo (61.75 unidades) mida `height` bloques. */
    private final float scale;
    /** Textura fija (las otras chicas). null = la del outfit de Kiko. */
    @org.jetbrains.annotations.Nullable
    private final Identifier fixedTexture;

    /** Kiko: 1.8 bloques (como el jugador) y textura según su ropa. */
    public KikoRenderer(EntityRendererFactory.Context ctx) {
        this(ctx, KikoModel.LAYER, 1.8f, null);
    }

    /** v3.0: modelos y texturas de cada ropa de las chicas (0 trabajo, 1 casual, 2 boda). null para Kiko. */
    @org.jetbrains.annotations.Nullable
    private final KikoModel[] outfitModels;
    @org.jetbrains.annotations.Nullable
    private final Identifier[] outfitTextures;

    /** Las otras chicas: su propio modelo, altura y textura. */
    public KikoRenderer(EntityRendererFactory.Context ctx, net.minecraft.client.render.entity.model.EntityModelLayer layer,
                        float height, @org.jetbrains.annotations.Nullable Identifier texture) {
        this(ctx, new net.minecraft.client.render.entity.model.EntityModelLayer[]{layer}, height,
                texture == null ? null : new Identifier[]{texture});
    }

    /** Las otras chicas con varias ropas: un modelo y una textura por ropa. */
    public KikoRenderer(EntityRendererFactory.Context ctx, net.minecraft.client.render.entity.model.EntityModelLayer[] layers,
                        float height, @org.jetbrains.annotations.Nullable Identifier[] textures) {
        super(ctx, new KikoModel(ctx.getPart(layers[0])), 0.45f * height / 1.8f);
        this.scale = (height * 16f) / 61.75f;
        this.fixedTexture = textures != null ? textures[0] : null;
        if (textures != null && layers.length > 1) {
            this.outfitModels = new KikoModel[layers.length];
            this.outfitModels[0] = this.model;
            for (int i = 1; i < layers.length; i++) this.outfitModels[i] = new KikoModel(ctx.getPart(layers[i]));
            this.outfitTextures = textures;
        } else {
            this.outfitModels = null;
            this.outfitTextures = null;
        }
        // Dibuja el arma o herramienta que trae en las manos
        // Armadura encima del cuerpo (va antes del ítem para que el arma quede por fuera)
        this.addFeature(new KikoArmorFeatureRenderer(this));
        this.addFeature(new HeldItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()) {
            @Override
            public void render(MatrixStack matrices, net.minecraft.client.render.VertexConsumerProvider consumers, int light,
                               KikoEntity entity, float limbAngle, float limbDistance, float tickDelta,
                               float animationProgress, float headYaw, float headPitch) {
                if (entity.isSleeping()) return; // dormida no anda con el pico en la mano
                super.render(matrices, consumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
            }
        });
    }

    /** De expedición no se dibuja nada (ni su ropa, ni lo que trae en la mano, ni su nombre). */
    @Override
    public boolean shouldRender(KikoEntity entity, net.minecraft.client.render.Frustum frustum, double x, double y, double z) {
        return !entity.isAway() && super.shouldRender(entity, frustum, x, y, z);
    }

    @Override
    public Identifier getTexture(KikoEntity entity) {
        if (outfitTextures != null) return outfitTextures[Math.min(entity.getGirlOutfit(), outfitTextures.length - 1)];
        return fixedTexture != null ? fixedTexture : entity.getOutfit().texture();
    }

    /** Acostada en la cama la subimos un poco para que no se hunda en el colchón (su modelo es más gruesito). */
    @Override
    public void render(KikoEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       net.minecraft.client.render.VertexConsumerProvider consumers, int light) {
        // Cada ropa de las chicas es otro modelo (la boda trae falda amplia y velo, por ejemplo)
        if (outfitModels != null) this.model = outfitModels[Math.min(entity.getGirlOutfit(), outfitModels.length - 1)];
        // v3.4.3 cada chica con su cuerpo (los chicos normales)
        this.model.setBodyType(switch (entity.getCharacter()) {
            case MARU -> KikoModel.BODY_ATHLETIC;
            case LUMI, NADI -> KikoModel.BODY_SLIM;
            case RINA -> KikoModel.BODY_SOFT;
            case TAMI -> KikoModel.BODY_FULL;
            default -> KikoModel.BODY_NORMAL;
        });
        if (entity.isSleeping()) {
            matrices.push();
            matrices.translate(0.0, 0.31 * scale / ((1.8f * 16f) / 61.75f), 0.0);
            super.render(entity, yaw, tickDelta, matrices, consumers, light);
            matrices.pop();
            return;
        }
        super.render(entity, yaw, tickDelta, matrices, consumers, light);
    }

    @Override
    protected void scale(KikoEntity entity, MatrixStack matrices, float amount) {
        matrices.scale(scale, scale, scale);
    }
}
