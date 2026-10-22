package com.jaciel.kiko.registry;

import com.jaciel.kiko.KikoMod;
import com.jaciel.kiko.item.KikoOutfitItem;
import com.jaciel.kiko.outfit.KikoOutfit;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

/**
 * Ítems del mod.
 *  - Caja de regalo (el regalo favorito de Kiko)
 *  - 3 conjuntos de ropa que se le dan con clic derecho
 *  - Huevo de spawn
 *  - Cama matrimonial de 2x2
 */
public final class ModItems {
    public static final Item GIFT_BOX = register("gift_box", new Item(new Item.Settings().maxCount(16)));

    public static final Item OUTFIT_SCHOOL = register("outfit_school", new KikoOutfitItem(KikoOutfit.SCHOOL, new Item.Settings().maxCount(1)));
    public static final Item OUTFIT_CASUAL1 = register("outfit_casual1", new KikoOutfitItem(KikoOutfit.CASUAL1, new Item.Settings().maxCount(1)));
    public static final Item OUTFIT_CASUAL2 = register("outfit_casual2", new KikoOutfitItem(KikoOutfit.CASUAL2, new Item.Settings().maxCount(1)));

    public static final Item KIKO_SPAWN_EGG = register("kiko_spawn_egg",
            new SpawnEggItem(ModEntities.KIKO, 0xFF7F6E, 0xFFD23C, new Item.Settings()));

    public static final Item KIKO_BED = register("kiko_bed",
            new com.jaciel.kiko.item.KikoBedItem(ModBlocks.KIKO_BED, new Item.Settings().maxCount(1)));

    /** v3.3.1 las camas de colores. */
    public static final java.util.List<Item> BED_ITEMS = new java.util.ArrayList<>();

    static {
        ModBlocks.BEDS.forEach((id, block) -> BED_ITEMS.add(register(id,
                new com.jaciel.kiko.item.KikoBedItem(block, new Item.Settings().maxCount(1)))));
    }

    public static final Item WHISTLE = register("whistle",
            new com.jaciel.kiko.item.KikoWhistleItem(new Item.Settings().maxCount(1)));

    public static final Item MARU_SPAWN_EGG = register("maru_spawn_egg",
            new SpawnEggItem(ModEntities.MARU, 0x3A363E, 0xE87830, new Item.Settings()));
    public static final Item LUMI_SPAWN_EGG = register("lumi_spawn_egg",
            new SpawnEggItem(ModEntities.LUMI, 0xB8A2DE, 0xF0E2C4, new Item.Settings()));
    public static final Item NADI_SPAWN_EGG = register("nadi_spawn_egg",
            new SpawnEggItem(ModEntities.NADI, 0x78482C, 0xC4B07E, new Item.Settings()));

    public static final Item RINA_SPAWN_EGG = register("rina_spawn_egg",
            new SpawnEggItem(ModEntities.RINA, 0xC0D0E2, 0x84CEC4, new Item.Settings()));
    public static final Item TAMI_SPAWN_EGG = register("tami_spawn_egg",
            new SpawnEggItem(ModEntities.TAMI, 0xDE7836, 0xFCFAF6, new Item.Settings()));

    /** v3.3 los chicos. */
    public static final Item TONO_SPAWN_EGG = register("tono_spawn_egg",
            new SpawnEggItem(ModEntities.TONO, 0x96683E, 0x4678C8, new Item.Settings()));
    public static final Item LEO_SPAWN_EGG = register("leo_spawn_egg",
            new SpawnEggItem(ModEntities.LEO, 0xF4C428, 0x283660, new Item.Settings()));
    public static final Item DANTE_SPAWN_EGG = register("dante_spawn_egg",
            new SpawnEggItem(ModEntities.DANTE, 0xB0B6BE, 0x96222C, new Item.Settings()));
    public static final Item IVAN_SPAWN_EGG = register("ivan_spawn_egg",
            new SpawnEggItem(ModEntities.IVAN, 0xFAC828, 0xDC7832, new Item.Settings()));
    public static final Item EMI_SPAWN_EGG = register("emi_spawn_egg",
            new SpawnEggItem(ModEntities.EMI, 0x2C282E, 0xF07896, new Item.Settings()));
    public static final Item BETO_SPAWN_EGG = register("beto_spawn_egg",
            new SpawnEggItem(ModEntities.BETO, 0x6E408C, 0x966438, new Item.Settings()));
    /** v3.4: vecino del pueblo al azar (para probar; normalmente salen solos en los pueblos). */
    public static final Item TOWNSFOLK_SPAWN_EGG = register("townsfolk_spawn_egg",
            new SpawnEggItem(ModEntities.TOWNSFOLK, 0xD6A070, 0x5AAA6E, new Item.Settings()));

    /** v3.0: ramo para pedirle que sea tu novia, anillo para casarte y el Anillo del Abismo (para tener varias esposas). */
    public static final Item BOUQUET = register("bouquet", new com.jaciel.kiko.item.RomanceItem("item.kiko.bouquet.tooltip",
            net.minecraft.util.Formatting.LIGHT_PURPLE, new Item.Settings().maxCount(16)));
    public static final Item RING = register("ring", new com.jaciel.kiko.item.RomanceItem("item.kiko.ring.tooltip",
            net.minecraft.util.Formatting.GOLD, new Item.Settings().maxCount(1).rarity(net.minecraft.util.Rarity.UNCOMMON)));
    public static final Item ABYSS_RING = register("abyss_ring", new com.jaciel.kiko.item.RomanceItem("item.kiko.abyss_ring.tooltip",
            net.minecraft.util.Formatting.DARK_AQUA, new Item.Settings().maxCount(1).rarity(net.minecraft.util.Rarity.EPIC)
                    .fireproof()
                    .component(net.minecraft.component.DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)));

    /** v3.1: cuna para el bebé y libro de familia. */
    public static final Item CRIB = register("crib", new net.minecraft.item.BlockItem(ModBlocks.CRIB, new Item.Settings()));
    public static final Item NOTICE_BOARD = register("notice_board", new net.minecraft.item.BlockItem(ModBlocks.NOTICE_BOARD, new Item.Settings()));
    public static final Item FAMILY_BOOK = register("family_book", new com.jaciel.kiko.item.FamilyBookItem(new Item.Settings().maxCount(1)));
    /** v3.2.1 pelota de futbol. */
    public static final Item SOCCER_BALL = register("soccer_ball", new com.jaciel.kiko.item.SoccerBallItem(new Item.Settings().maxCount(16)));

    /** v2.9: los platillos que cocinan las chicas (ver Dish y Cooking). */
    static {
        for (com.jaciel.kiko.item.Dish d : com.jaciel.kiko.item.Dish.values()) {
            d.item = register(d.id, new com.jaciel.kiko.item.DishItem(d.bowl,
                    new Item.Settings().food(d.food()).maxCount(16)));
        }
    }

    /** v3.6 envases y bebidas. */
    public static final Item CUP = register("cup", new Item(new Item.Settings().maxCount(16)));
    public static final Item MUG = register("mug", new Item(new Item.Settings().maxCount(16)));
    public static final Item EMPTY_CAN = register("empty_can", new Item(new Item.Settings().maxCount(16)));
    public static final Item PITCHER = register("pitcher", new net.minecraft.item.BlockItem(ModBlocks.PITCHER, new Item.Settings()));
    public static final Item RECIPE_BOOK = register("recipe_book", new com.jaciel.kiko.item.RecipeBookItem(new Item.Settings().maxCount(1)));
    /** v3.9 paraguas (lo sacan los vecinos cuando llueve; tú también lo puedes traer). */
    public static final Item UMBRELLA = register("umbrella", new Item(new Item.Settings().maxCount(1)));
    public static final java.util.List<Item> PITCHER_ITEMS = new java.util.ArrayList<>();

    static {
        for (com.jaciel.kiko.item.Drink d : com.jaciel.kiko.item.Drink.values()) {
            d.item = register(d.id, new com.jaciel.kiko.item.DrinkItem(d, new Item.Settings().maxCount(16)
                    .food(new net.minecraft.component.type.FoodComponent.Builder().nutrition(d.nutrition)
                            .saturationModifier(d.saturation).alwaysEdible().build())));
        }
        ModBlocks.PITCHERS.forEach((d, b) -> PITCHER_ITEMS.add(register("pitcher_" + d.id,
                new net.minecraft.item.BlockItem(b, new Item.Settings().maxCount(1)))));
    }

    public static final ItemGroup GROUP = Registry.register(Registries.ITEM_GROUP, KikoMod.id("main"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(GIFT_BOX))
                    .displayName(Text.translatable("itemGroup.kiko.main"))
                    .entries((context, entries) -> {
                        entries.add(KIKO_SPAWN_EGG);
                        entries.add(MARU_SPAWN_EGG);
                        entries.add(LUMI_SPAWN_EGG);
                        entries.add(NADI_SPAWN_EGG);
                        entries.add(RINA_SPAWN_EGG);
                        entries.add(TAMI_SPAWN_EGG);
                        entries.add(TONO_SPAWN_EGG);
                        entries.add(LEO_SPAWN_EGG);
                        entries.add(DANTE_SPAWN_EGG);
                        entries.add(IVAN_SPAWN_EGG);
                        entries.add(EMI_SPAWN_EGG);
                        entries.add(BETO_SPAWN_EGG);
                        entries.add(TOWNSFOLK_SPAWN_EGG);
                        entries.add(GIFT_BOX);
                        entries.add(OUTFIT_SCHOOL);
                        entries.add(OUTFIT_CASUAL1);
                        entries.add(OUTFIT_CASUAL2);
                        entries.add(KIKO_BED);
                        for (Item bed : BED_ITEMS) entries.add(bed);
                        entries.add(WHISTLE);
                        entries.add(BOUQUET);
                        entries.add(RING);
                        entries.add(ABYSS_RING);
                        entries.add(CRIB);
                        entries.add(NOTICE_BOARD);
                        entries.add(FAMILY_BOOK);
                        entries.add(SOCCER_BALL);
                        for (com.jaciel.kiko.item.Dish d : com.jaciel.kiko.item.Dish.values()) entries.add(d.item);
                        entries.add(CUP);
                        entries.add(MUG);
                        entries.add(EMPTY_CAN);
                        entries.add(RECIPE_BOOK);
                        entries.add(UMBRELLA);
                        entries.add(PITCHER);
                        for (Item pi : PITCHER_ITEMS) entries.add(pi);
                        for (com.jaciel.kiko.item.Drink d : com.jaciel.kiko.item.Drink.values()) entries.add(d.item);
                    })
                    .build());

    private ModItems() {}

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, KikoMod.id(name), item);
    }

    public static void register() {}
}
