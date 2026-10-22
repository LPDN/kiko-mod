package com.jaciel.kiko;

import com.jaciel.kiko.command.KikoCommands;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.network.KikoNetworking;
import com.jaciel.kiko.registry.ModBlocks;
import com.jaciel.kiko.registry.ModEntities;
import com.jaciel.kiko.registry.ModItems;
import com.jaciel.kiko.registry.ModScreens;
import com.jaciel.kiko.registry.ModSounds;
import com.jaciel.kiko.world.VillageSpawner;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Punto de entrada principal (lado común, corre en cliente y servidor).
 * Aquí solo se llama a los registros de cada módulo para mantenerlo limpio.
 */
public class KikoMod implements ModInitializer {
    public static final String MOD_ID = "kiko";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        KikoConfig.load();
        ModSounds.register();
        ModBlocks.register();
        ModEntities.register();
        ModItems.register();
        com.jaciel.kiko.recipe.ModRecipes.register();
        com.jaciel.kiko.item.DrinkItem.register();
        ModScreens.register();
        KikoNetworking.registerCommon();
        KikoCommands.register();
        VillageSpawner.register();
        com.jaciel.kiko.town.TownCalendar.register();
        com.jaciel.kiko.town.TownGrowth.register();
        com.jaciel.kiko.town.TownRaid.register();
        com.jaciel.kiko.town.Birthdays.register();
        com.jaciel.kiko.town.TownWeather.register();
        com.jaciel.kiko.town.TownFestival.register();
        com.jaciel.kiko.romance.Dates.register();
        com.jaciel.kiko.romance.WifeBonuses.register();
        com.jaciel.kiko.romance.Romance.register();
        com.jaciel.kiko.family.Traits.register();
        com.jaciel.kiko.romance.AbyssRingLoot.register();
        LOGGER.info("Kiko llegó al mundo~");
    }
}
