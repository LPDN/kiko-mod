package com.jaciel.kiko.client.render;

import com.jaciel.kiko.KikoMod;
import com.jaciel.kiko.client.model.KidModels;
import com.jaciel.kiko.client.model.KikoModel;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.family.KikoChildEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Dibuja a los hijos (v3.1). Un modelo por etapa (y por niño o niña) y la textura pintada con sus rasgos.
 * La altura la da el atributo de escala de la entidad; aquí solo se ajusta cada modelo a 1.8 bloques.
 */
public class KidRenderer extends MobEntityRenderer<KikoEntity, KikoModel> {
    public static final String[] MODELS = {"baby", "child_boy", "child_girl", "teen_boy", "teen_girl", "adult_man", "adult_woman",
            // v3.4 gente del pueblo
            "man_shirt", "man_farmer", "man_vest", "man_baker", "man_elder",
            "woman_dress", "woman_bun", "woman_bob", "woman_braid", "woman_elder",
            // v3.8 guardias
            "guard_man", "guard_woman"};

    public static EntityModelLayer layer(String model) {
        return new EntityModelLayer(KikoMod.id("kid_" + model), "main");
    }

    private final Map<String, KikoModel> models = new HashMap<>();

    public KidRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new KikoModel(ctx.getPart(layer("child_boy"))), 0.3f);
        for (String m : MODELS) models.put(m, new KikoModel(ctx.getPart(layer(m))));
        this.addFeature(new KikoArmorFeatureRenderer(this));
        this.addFeature(new HeldItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()) {
            @Override
            public void render(MatrixStack matrices, VertexConsumerProvider consumers, int light, KikoEntity entity, float limbAngle,
                               float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
                if (entity.isSleeping()) return;
                super.render(matrices, consumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
            }
        });
    }

    @Override
    public void render(KikoEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider consumers, int light) {
        if (entity instanceof KikoChildEntity kid) {
            KikoModel m = models.get(kid.modelName());
            if (m != null) this.model = m;
            // v3.4 el peinado de los vecinos (los modelos de hombre traen los 3 extra escondidos)
            this.model.setHairStyle(kid instanceof com.jaciel.kiko.town.TownsfolkEntity t ? t.getHairStyle() : 0);
            // v3.4.3 los vecinos adultos con su tipo de cuerpo (los niños y tus hijos normales)
            this.model.setBodyType(kid instanceof com.jaciel.kiko.town.TownsfolkEntity t2 && !t2.isTownKid() ? t2.getBodyType() : 0);
            this.shadowRadius = 0.45f * kid.stageHeight() / 1.8f;
        }
        // v3.5.1 acostados en camas normales ya no se suben (flotaban); las chicas sí porque su cama es más alta
        super.render(entity, yaw, tickDelta, matrices, consumers, light);
    }

    /** A tus hijos se les ve "· tu hijo" o "· tu hija" junto al nombre. */
    @Override
    protected void renderLabelIfPresent(KikoEntity entity, net.minecraft.text.Text text, MatrixStack matrices,
                                        VertexConsumerProvider consumers, int light, float tickDelta) {
        var me = net.minecraft.client.MinecraftClient.getInstance().player;
        if (entity instanceof KikoChildEntity kid && me != null && me.getUuid().equals(kid.getParentUuid())) {
            text = text.copy().append(net.minecraft.text.Text.literal(" · ").formatted(net.minecraft.util.Formatting.GRAY))
                    .append(net.minecraft.text.Text.translatable(kid.isMale() ? "label.kiko.your_son" : "label.kiko.your_daughter")
                            .formatted(net.minecraft.util.Formatting.LIGHT_PURPLE));
        } else if (entity instanceof com.jaciel.kiko.town.TownsfolkEntity g && g.isGuard()) {
            // v3.8 a los guardias se les ve su rango: "Ramón · Sargento"
            text = text.copy().append(net.minecraft.text.Text.literal(" · ").formatted(net.minecraft.util.Formatting.GRAY))
                    .append(net.minecraft.text.Text.translatable("rank.kiko.guard." + g.getRank() + (g.isMale() ? ".m" : ".f"))
                            .formatted(net.minecraft.util.Formatting.AQUA));
        } else if (entity instanceof com.jaciel.kiko.town.TownsfolkEntity t && !t.getJobId().equals("none")) {
            // v3.4 a los vecinos se les ve su oficio: "Ramón · panadero"
            text = text.copy().append(net.minecraft.text.Text.literal(" · ").formatted(net.minecraft.util.Formatting.GRAY))
                    .append(net.minecraft.text.Text.translatable("job.kiko." + t.getJobId() + (t.isMale() ? ".m" : ".f"))
                            .formatted(net.minecraft.util.Formatting.YELLOW));
        }
        super.renderLabelIfPresent(entity, text, matrices, consumers, light, tickDelta);
    }

    @Override
    public Identifier getTexture(KikoEntity entity) {
        if (entity instanceof KikoChildEntity kid) return KidTextures.get(kid.modelName(), kid.colors());
        return KikoMod.id("textures/entity/kid/child_boy.png");
    }

    /** Cada modelo mide distinto en unidades; aquí se lleva directo a la altura de su etapa (sin el atributo de escala). */
    @Override
    protected void scale(KikoEntity entity, MatrixStack matrices, float amount) {
        String name = entity instanceof KikoChildEntity kid ? kid.modelName() : "child_boy";
        Float u = KidModels.UNITS.get(name);
        if (u == null) u = com.jaciel.kiko.client.model.TownModels.UNITS.getOrDefault(name, 61.75f);
        float units = u;
        float h = entity instanceof KikoChildEntity kid ? kid.stageHeight() : 1.8f;
        float s = h * 16f / units;
        matrices.scale(s, s, s);
    }
}
