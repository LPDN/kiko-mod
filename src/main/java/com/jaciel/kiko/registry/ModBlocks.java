package com.jaciel.kiko.registry;

import com.jaciel.kiko.KikoMod;
import com.jaciel.kiko.block.KikoBedBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/** Bloques del mod. */
public final class ModBlocks {
    /** Cama matrimonial de 2x2 (Fase 4). */
    public static final Block KIKO_BED = Registry.register(Registries.BLOCK, KikoMod.id("kiko_bed"),
            new KikoBedBlock(AbstractBlock.Settings.copy(Blocks.PINK_BED)));

    /** Cuna para el bebé (v3.1). */
    public static final Block CRIB = Registry.register(Registries.BLOCK, KikoMod.id("crib"),
            new com.jaciel.kiko.block.CribBlock(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).nonOpaque().strength(1.0f)));

    /** v3.5 tablero de encargos del pueblo. */
    public static final Block NOTICE_BOARD = Registry.register(Registries.BLOCK, KikoMod.id("notice_board"),
            new com.jaciel.kiko.block.NoticeBoardBlock(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).nonOpaque().strength(1.0f)));

    /** v3.3.1 camas matrimoniales de colores, una por casa (misma cama que la de Kiko con otra cobija). */
    public static final String[] BED_VARIANTS = {"bed_forge", "bed_moon", "bed_trail", "bed_clinic", "bed_kitchen", "bed_ranch", "bed_sailor", "bed_guard", "bed_mine", "bed_music", "bed_merchant"};
    public static final java.util.Map<String, Block> BEDS = new java.util.LinkedHashMap<>();

    static {
        for (String id : BED_VARIANTS) {
            BEDS.put(id, Registry.register(Registries.BLOCK, KikoMod.id(id), new KikoBedBlock(AbstractBlock.Settings.copy(Blocks.PINK_BED))));
        }
    }

    /** v3.6 jarras de agua fresca (la vacía y una por sabor). */
    public static final Block PITCHER = Registry.register(Registries.BLOCK, KikoMod.id("pitcher"),
            new com.jaciel.kiko.block.PitcherBlock(AbstractBlock.Settings.copy(Blocks.GLASS).strength(0.5f), null));
    public static final java.util.Map<com.jaciel.kiko.item.Drink, Block> PITCHERS = new java.util.LinkedHashMap<>();

    static {
        for (com.jaciel.kiko.item.Drink d : new com.jaciel.kiko.item.Drink[]{com.jaciel.kiko.item.Drink.HORCHATA,
                com.jaciel.kiko.item.Drink.JAMAICA, com.jaciel.kiko.item.Drink.AGUA_SANDIA}) {
            PITCHERS.put(d, Registry.register(Registries.BLOCK, KikoMod.id("pitcher_" + d.id),
                    new com.jaciel.kiko.block.PitcherBlock(AbstractBlock.Settings.copy(Blocks.GLASS).strength(0.5f), d)));
        }
    }

    private ModBlocks() {}

    public static void register() {}
}
