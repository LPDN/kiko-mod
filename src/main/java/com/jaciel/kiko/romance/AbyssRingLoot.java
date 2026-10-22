package com.jaciel.kiko.romance;

import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.registry.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

/**
 * El Anillo del Abismo sale rarísimo en los cofres de las ciudades antiguas (donde vive el Warden).
 * Por defecto 3 % por cofre (se cambia en la config con abyssRingChance).
 */
public final class AbyssRingLoot {
    private AbyssRingLoot() {}

    private static final RegistryKey<LootTable> ANCIENT_CITY =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.ofVanilla("chests/ancient_city"));

    public static void register() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin() || !ANCIENT_CITY.equals(key)) return;
            tableBuilder.pool(LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .conditionally(RandomChanceLootCondition.builder(KikoConfig.get().abyssRingChance))
                    .with(ItemEntry.builder(ModItems.ABYSS_RING)));
        });
    }
}
