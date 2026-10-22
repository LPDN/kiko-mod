package com.jaciel.kiko.client;

import com.jaciel.kiko.client.model.KikoModel;
import com.jaciel.kiko.client.render.KikoRenderer;
import com.jaciel.kiko.client.screen.KikoInventoryScreen;
import com.jaciel.kiko.client.screen.KikoMenuScreen;
import com.jaciel.kiko.registry.ModScreens;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import com.jaciel.kiko.network.KikoNetworking.OpenMenuPayload;
import com.jaciel.kiko.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

/** Entrada del lado cliente: modelo, render y menú. */
public class KikoClient implements ClientModInitializer {
    /** Registra una chica o chico con sus 5 ropas (trabajo, casual, boda, verano, invierno): un modelo y una textura por ropa. */
    private static void registerGirl(net.minecraft.entity.EntityType<com.jaciel.kiko.entity.KikoEntity> type, String id, float height,
                                     EntityModelLayerRegistry.TexturedModelDataProvider... models) {
        String[] suffix = {"", "_casual", "_wedding", "_summer", "_winter"};
        int n = models.length;
        net.minecraft.client.render.entity.model.EntityModelLayer[] layers = new net.minecraft.client.render.entity.model.EntityModelLayer[n];
        net.minecraft.util.Identifier[] texs = new net.minecraft.util.Identifier[n];
        for (int i = 0; i < n; i++) {
            layers[i] = new net.minecraft.client.render.entity.model.EntityModelLayer(com.jaciel.kiko.KikoMod.id(id + suffix[i]), "main");
            EntityModelLayerRegistry.registerModelLayer(layers[i], models[i]);
            texs[i] = com.jaciel.kiko.KikoMod.id("textures/entity/" + id + suffix[i] + ".png");
        }
        EntityRendererRegistry.register(type, ctx -> new KikoRenderer(ctx, layers, height, texs));
    }

    @Override
    public void onInitializeClient() {
        // v3.6 las jarras son de vidrio (transparentes)
        net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap.INSTANCE.putBlock(com.jaciel.kiko.registry.ModBlocks.PITCHER,
                net.minecraft.client.render.RenderLayer.getTranslucent());
        com.jaciel.kiko.registry.ModBlocks.PITCHERS.values().forEach(b ->
                net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap.INSTANCE.putBlock(b, net.minecraft.client.render.RenderLayer.getTranslucent()));
        EntityModelLayerRegistry.registerModelLayer(KikoModel.LAYER, KikoModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.KIKO, KikoRenderer::new);
        // Fase 6: Maru, Lumi y Nadi (modelos en GirlModels, generados por tools/gen_girls.py)
        registerGirl(ModEntities.MARU, "maru", 1.8f, com.jaciel.kiko.client.model.GirlModels::maru,
                com.jaciel.kiko.client.model.GirlModels::maru_casual, com.jaciel.kiko.client.model.GirlModels::maru_wedding,
                com.jaciel.kiko.client.model.SeasonModels::maru_summer, com.jaciel.kiko.client.model.SeasonModels::maru_winter);
        registerGirl(ModEntities.LUMI, "lumi", 1.64f, com.jaciel.kiko.client.model.GirlModels::lumi,
                com.jaciel.kiko.client.model.GirlModels::lumi_casual, com.jaciel.kiko.client.model.GirlModels::lumi_wedding,
                com.jaciel.kiko.client.model.SeasonModels::lumi_summer, com.jaciel.kiko.client.model.SeasonModels::lumi_winter);
        registerGirl(ModEntities.NADI, "nadi", 1.8f, com.jaciel.kiko.client.model.GirlModels::nadi,
                com.jaciel.kiko.client.model.GirlModels::nadi_casual, com.jaciel.kiko.client.model.GirlModels::nadi_wedding,
                com.jaciel.kiko.client.model.SeasonModels::nadi_summer, com.jaciel.kiko.client.model.SeasonModels::nadi_winter);
        registerGirl(ModEntities.RINA, "rina", 1.8f, com.jaciel.kiko.client.model.GirlModels::rina,
                com.jaciel.kiko.client.model.GirlModels::rina_casual, com.jaciel.kiko.client.model.GirlModels::rina_wedding,
                com.jaciel.kiko.client.model.SeasonModels::rina_summer, com.jaciel.kiko.client.model.SeasonModels::rina_winter);
        registerGirl(ModEntities.TAMI, "tami", 1.8f, com.jaciel.kiko.client.model.GirlModels::tami,
                com.jaciel.kiko.client.model.GirlModels::tami_casual, com.jaciel.kiko.client.model.GirlModels::tami_wedding,
                com.jaciel.kiko.client.model.SeasonModels::tami_summer, com.jaciel.kiko.client.model.SeasonModels::tami_winter);
        // v3.3 los chicos (modelos en GuyModels, generados por tools/gen_guys_outfits.py)
        registerGirl(ModEntities.TONO, "tono", 1.88f, com.jaciel.kiko.client.model.GuyModels::tono, com.jaciel.kiko.client.model.GuyModels::tono_casual, com.jaciel.kiko.client.model.GuyModels::tono_wedding,
                com.jaciel.kiko.client.model.GuyModels::tono_summer, com.jaciel.kiko.client.model.GuyModels::tono_winter);
        registerGirl(ModEntities.LEO, "leo", 1.82f, com.jaciel.kiko.client.model.GuyModels::leo, com.jaciel.kiko.client.model.GuyModels::leo_casual, com.jaciel.kiko.client.model.GuyModels::leo_wedding,
                com.jaciel.kiko.client.model.GuyModels::leo_summer, com.jaciel.kiko.client.model.GuyModels::leo_winter);
        registerGirl(ModEntities.DANTE, "dante", 1.92f, com.jaciel.kiko.client.model.GuyModels::dante, com.jaciel.kiko.client.model.GuyModels::dante_casual, com.jaciel.kiko.client.model.GuyModels::dante_wedding,
                com.jaciel.kiko.client.model.GuyModels::dante_summer, com.jaciel.kiko.client.model.GuyModels::dante_winter);
        registerGirl(ModEntities.IVAN, "ivan", 1.80f, com.jaciel.kiko.client.model.GuyModels::ivan, com.jaciel.kiko.client.model.GuyModels::ivan_casual, com.jaciel.kiko.client.model.GuyModels::ivan_wedding,
                com.jaciel.kiko.client.model.GuyModels::ivan_summer, com.jaciel.kiko.client.model.GuyModels::ivan_winter);
        registerGirl(ModEntities.EMI, "emi", 1.76f, com.jaciel.kiko.client.model.GuyModels::emi, com.jaciel.kiko.client.model.GuyModels::emi_casual, com.jaciel.kiko.client.model.GuyModels::emi_wedding,
                com.jaciel.kiko.client.model.GuyModels::emi_summer, com.jaciel.kiko.client.model.GuyModels::emi_winter);
        registerGirl(ModEntities.BETO, "beto", 1.84f, com.jaciel.kiko.client.model.GuyModels::beto, com.jaciel.kiko.client.model.GuyModels::beto_casual, com.jaciel.kiko.client.model.GuyModels::beto_wedding,
                com.jaciel.kiko.client.model.GuyModels::beto_summer, com.jaciel.kiko.client.model.GuyModels::beto_winter);
        // v3.1: hijos (un modelo por etapa, generados por tools/gen_kids.py)
        java.util.Map<String, EntityModelLayerRegistry.TexturedModelDataProvider> kidModels = java.util.Map.of(
                "baby", com.jaciel.kiko.client.model.KidModels::baby,
                "child_boy", com.jaciel.kiko.client.model.KidModels::child_boy,
                "child_girl", com.jaciel.kiko.client.model.KidModels::child_girl,
                "teen_boy", com.jaciel.kiko.client.model.KidModels::teen_boy,
                "teen_girl", com.jaciel.kiko.client.model.KidModels::teen_girl,
                "adult_man", com.jaciel.kiko.client.model.KidModels::adult_man,
                "adult_woman", com.jaciel.kiko.client.model.KidModels::adult_woman);
        kidModels.forEach((name, provider) -> EntityModelLayerRegistry.registerModelLayer(
                com.jaciel.kiko.client.render.KidRenderer.layer(name), provider));
        EntityRendererRegistry.register(ModEntities.CHILD, com.jaciel.kiko.client.render.KidRenderer::new);
        // v3.4: gente del pueblo (tools/gen_townsfolk.py); los niños del pueblo usan los modelos de los hijos
        java.util.Map<String, EntityModelLayerRegistry.TexturedModelDataProvider> townModels = new java.util.HashMap<>();
        townModels.put("man_shirt", com.jaciel.kiko.client.model.TownModels::man_shirt);
        townModels.put("man_farmer", com.jaciel.kiko.client.model.TownModels::man_farmer);
        townModels.put("man_vest", com.jaciel.kiko.client.model.TownModels::man_vest);
        townModels.put("man_baker", com.jaciel.kiko.client.model.TownModels::man_baker);
        townModels.put("man_elder", com.jaciel.kiko.client.model.TownModels::man_elder);
        townModels.put("woman_dress", com.jaciel.kiko.client.model.TownModels::woman_dress);
        townModels.put("woman_bun", com.jaciel.kiko.client.model.TownModels::woman_bun);
        townModels.put("woman_bob", com.jaciel.kiko.client.model.TownModels::woman_bob);
        townModels.put("woman_braid", com.jaciel.kiko.client.model.TownModels::woman_braid);
        townModels.put("woman_elder", com.jaciel.kiko.client.model.TownModels::woman_elder);
        townModels.put("guard_man", com.jaciel.kiko.client.model.TownModels::guard_man);
        townModels.put("guard_woman", com.jaciel.kiko.client.model.TownModels::guard_woman);
        townModels.forEach((name, provider) -> EntityModelLayerRegistry.registerModelLayer(
                com.jaciel.kiko.client.render.KidRenderer.layer(name), provider));
        EntityRendererRegistry.register(ModEntities.TOWNSFOLK, com.jaciel.kiko.client.render.KidRenderer::new);
        ClientPlayNetworking.registerGlobalReceiver(com.jaciel.kiko.network.KikoNetworking.TraitsPayload.OPEN, (payload, context) ->
                context.client().execute(() -> context.client().setScreen(new com.jaciel.kiko.client.screen.TraitsScreen(payload))));
        ClientPlayNetworking.registerGlobalReceiver(com.jaciel.kiko.network.KikoNetworking.KidNamePayload.ID, (payload, context) ->
                context.client().execute(() -> context.client().setScreen(new com.jaciel.kiko.client.screen.KidNameScreen(payload))));
        EntityRendererRegistry.register(ModEntities.BOBBER, com.jaciel.kiko.client.render.KikoBobberRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(com.jaciel.kiko.client.render.SoccerBallRenderer.LAYER,
                com.jaciel.kiko.client.render.SoccerBallRenderer::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.SOCCER_BALL, com.jaciel.kiko.client.render.SoccerBallRenderer::new);
        HandledScreens.register(ModScreens.KIKO_INVENTORY, KikoInventoryScreen::new);

        ClientPlayNetworking.registerGlobalReceiver(OpenMenuPayload.ID, (payload, context) ->
                context.client().execute(() -> context.client().setScreen(new KikoMenuScreen(payload))));

        // Fase 5: menú rápido con la tecla K y marcador a través de las paredes
        KikoClientState.load();
        com.jaciel.kiko.entity.KikoEntity.clientGlow = KikoClientState::shouldGlow;
        net.minecraft.client.option.KeyBinding quickKey = net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding(
                new net.minecraft.client.option.KeyBinding("key.kiko.quick_menu", net.minecraft.client.util.InputUtil.Type.KEYSYM,
                        org.lwjgl.glfw.GLFW.GLFW_KEY_K, "key.category.kiko"));
        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (quickKey.wasPressed()) {
                if (client.player != null && client.currentScreen == null) {
                    ClientPlayNetworking.send(new com.jaciel.kiko.network.KikoNetworking.QuickMenuRequestPayload());
                }
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(com.jaciel.kiko.network.KikoNetworking.QuickMenuPayload.ID, (payload, context) ->
                context.client().execute(() -> context.client().setScreen(
                        new com.jaciel.kiko.client.screen.KikoQuickScreen(payload.kikos()))));
    }
}
