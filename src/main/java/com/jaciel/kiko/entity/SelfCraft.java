package com.jaciel.kiko.entity;

import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

import java.util.List;
import java.util.function.Predicate;

/**
 * Crafteo propio (v3.6). Las chicas se hacen sus cosas solas con lo que traen en la mochila, sin mesa de crafteo.
 *  - Herramientas (pico, hacha, pala, azadón) con el mejor material que tengan, y si juntan algo mejor se mejoran (la vieja se guarda)
 *  - Espada y armadura de hierro o diamante, sin quitar nada encantado ni algo mejor
 *  - Antorchas con carbón y palitos, palitos y tablas con troncos
 * Nunca toca cofres, solo su mochila. Se apaga con "companionCraft" en la config.
 * Los niños no craftean. Iván empieza por el pico y siempre tiene antorchas, Dante por la espada y la armadura.
 */
public final class SelfCraft {
    private SelfCraft() {}

    private enum Task { PICK, AXE, SHOVEL, HOE, SWORD, ARMOR, TORCH }

    private record Mat(String key, int durability, Predicate<ItemStack> test) {}

    private static final Mat DIAMOND = new Mat("diamond", 1561, s -> s.isOf(Items.DIAMOND));
    private static final Mat IRON = new Mat("iron", 250, s -> s.isOf(Items.IRON_INGOT));
    private static final Mat STONE = new Mat("stone", 131, s -> s.isIn(ItemTags.STONE_TOOL_MATERIALS));
    private static final Mat WOOD = new Mat("wood", 59, s -> s.isIn(ItemTags.PLANKS));
    private static final Mat[] MATS = {DIAMOND, IRON, STONE, WOOD};

    private record Tool(TagKey<Item> tag, int cost, int sticks, Item wood, Item stone, Item iron, Item diamond) {
        Item of(Mat m) {
            return m == DIAMOND ? diamond : m == IRON ? iron : m == STONE ? stone : wood;
        }
    }

    private static final Tool PICKAXE = new Tool(ItemTags.PICKAXES, 3, 2, Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE);
    private static final Tool AXE = new Tool(ItemTags.AXES, 3, 2, Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.DIAMOND_AXE);
    private static final Tool SHOVEL = new Tool(ItemTags.SHOVELS, 1, 2, Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL);
    private static final Tool HOE = new Tool(ItemTags.HOES, 2, 2, Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.DIAMOND_HOE);
    private static final Tool SWORD = new Tool(ItemTags.SWORDS, 2, 1, Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.DIAMOND_SWORD);

    /** Armadura: [diamante, hierro] por pieza. */
    private static final EquipmentSlot[] ARMOR = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private static final Item[][] ARMOR_ITEMS = {
            {Items.DIAMOND_HELMET, Items.IRON_HELMET}, {Items.DIAMOND_CHESTPLATE, Items.IRON_CHESTPLATE},
            {Items.DIAMOND_LEGGINGS, Items.IRON_LEGGINGS}, {Items.DIAMOND_BOOTS, Items.IRON_BOOTS}};
    private static final int[] ARMOR_COST = {5, 8, 7, 4};

    private static List<Task> order(KikoEntity k) {
        return switch (k.getCharacter()) {
            case IVAN -> List.of(Task.PICK, Task.TORCH, Task.AXE, Task.SWORD, Task.ARMOR, Task.SHOVEL);
            case DANTE -> List.of(Task.SWORD, Task.ARMOR, Task.PICK, Task.AXE, Task.TORCH, Task.SHOVEL);
            case TONO -> List.of(Task.HOE, Task.AXE, Task.PICK, Task.SWORD, Task.TORCH, Task.ARMOR, Task.SHOVEL);
            default -> List.of(Task.PICK, Task.AXE, Task.SWORD, Task.ARMOR, Task.TORCH, Task.SHOVEL);
        };
    }

    // ---------- mochila ----------

    private static void each(KikoEntity k, java.util.function.Consumer<ItemStack> c) {
        c.accept(k.getMainHandStack());
        c.accept(k.getOffHandStack());
        for (ItemStack s : k.getInventory().bag()) c.accept(s);
    }

    private static int count(KikoEntity k, Predicate<ItemStack> t) {
        int[] n = {0};
        each(k, s -> {
            if (!s.isEmpty() && t.test(s)) n[0] += s.getCount();
        });
        return n[0];
    }

    private static void remove(KikoEntity k, Predicate<ItemStack> t, int n) {
        int[] left = {n};
        each(k, s -> {
            if (left[0] <= 0 || s.isEmpty() || !t.test(s)) return;
            int d = Math.min(left[0], s.getCount());
            s.decrement(d);
            left[0] -= d;
        });
    }

    private static void give(KikoEntity k, ItemStack s) {
        ItemStack rest = k.getInventory().insert(s);
        if (!rest.isEmpty()) k.dropStack(rest);
    }

    private static final Predicate<ItemStack> LOG = s -> s.isIn(ItemTags.LOGS);
    private static final Predicate<ItemStack> STICK = s -> s.isOf(Items.STICK);

    /** Tablas que puede juntar (las que tiene más las que saldrían de sus troncos). */
    private static int planksPossible(KikoEntity k) {
        return count(k, WOOD.test()) + 4 * count(k, LOG);
    }

    private static boolean takePlanks(KikoEntity k, int n) {
        if (planksPossible(k) < n) return false;
        while (count(k, WOOD.test()) < n) {
            remove(k, LOG, 1);
            give(k, new ItemStack(Items.OAK_PLANKS, 4));
        }
        remove(k, WOOD.test(), n);
        return true;
    }

    private static boolean takeSticks(KikoEntity k, int n) {
        int have = count(k, STICK);
        if (have < n) {
            int batches = (n - have + 3) / 4;
            if (!takePlanks(k, batches * 2)) return false;
            give(k, new ItemStack(Items.STICK, batches * 4));
        }
        remove(k, STICK, n);
        return true;
    }

    /** ¿Alcanza para tantos palitos? (tablas incluidas, descontando las que ya va a gastar en el material). */
    private static boolean sticksOk(KikoEntity k, int n, int planksUsed) {
        int have = count(k, STICK);
        if (have >= n) return true;
        int batches = (n - have + 3) / 4;
        return planksPossible(k) - planksUsed >= batches * 2;
    }

    private static int matCount(KikoEntity k, Mat m) {
        return m == WOOD ? planksPossible(k) : count(k, m.test());
    }

    private static int bestDurability(KikoEntity k, TagKey<Item> tag) {
        int[] best = {0};
        each(k, s -> {
            if (!s.isEmpty() && s.isIn(tag) && s.getItem() instanceof ToolItem t) {
                best[0] = Math.max(best[0], t.getMaterial().getDurability());
            } else if (!s.isEmpty() && s.isIn(tag)) {
                best[0] = Integer.MAX_VALUE;                 // herramienta de otro mod, no la toca
            }
        });
        return best[0];
    }

    // ---------- el tick ----------

    /** Cada 5 segundos más o menos. Hace como mucho una cosa por vez. */
    public static void tick(KikoEntity k, PlayerEntity friend) {
        if (!KikoConfig.get().companionCraft || k.isMinor() || k.isTownsfolk() || k.isAway()) return;
        if (k.getInventory().freeBagSlots() == 0) return;
        for (Task t : order(k)) {
            String said = switch (t) {
                case PICK -> tool(k, PICKAXE, "pick", true);
                case AXE -> tool(k, AXE, "axe", true);
                case SHOVEL -> tool(k, SHOVEL, "shovel", false);
                case HOE -> tool(k, HOE, "hoe", false);
                case SWORD -> tool(k, SWORD, "sword", true);
                case ARMOR -> armor(k);
                case TORCH -> torch(k);
            };
            if (said != null) {
                announce(k, friend, said, t);
                return;
            }
        }
        if (k.getCharacter() == KikoCharacter.MARU) forgeForPlayer(k, friend);
    }

    private static void announce(KikoEntity k, PlayerEntity friend, String key, Task t) {
        k.swingHand(Hand.MAIN_HAND);
        k.getWorld().playSound(null, k.getBlockPos(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.NEUTRAL, 0.25f, 1.6f);
        k.spawnParticles(ParticleTypes.CRIT, 3);
        if (friend != null && friend.squaredDistanceTo(k) < 16 * 16) {
            String base = "dialogue.kiko.craft." + key;
            if (k.getCharacter() == KikoCharacter.IVAN && key.equals("pick")) base = "dialogue.ivan.craft.pick";
            if (k.getCharacter() == KikoCharacter.DANTE && key.equals("sword")) base = "dialogue.dante.craft.sword";
            KikoInteractionHandler.say(friend, k, base, 1);
            k.setExpression(KikoExpression.SMUG, 60);
        }
    }

    private static void enchantSmall(KikoEntity k, ItemStack s, boolean sword) {
        if (k.getCharacter() != KikoCharacter.MARU || k.getRandom().nextInt(4) != 0) return;
        var reg = k.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT);
        reg.getEntry(sword ? Enchantments.SHARPNESS : Enchantments.EFFICIENCY).ifPresent(e -> s.addEnchantment(e, 1));
    }

    /** Hace (o mejora) una herramienta. @return la clave de su frase o null si no hizo nada. */
    private static String tool(KikoEntity k, Tool tool, String key, boolean upgrade) {
        int owned = bestDurability(k, tool.tag());
        if (owned == Integer.MAX_VALUE || (owned > 0 && !upgrade)) return null;
        for (Mat m : MATS) {
            if (m.durability() <= owned) return null;
            // tablas para el material mas los palitos
            int planksUsed = m == WOOD ? tool.cost() : 0;
            if (matCount(k, m) < tool.cost() || !sticksOk(k, tool.sticks(), planksUsed)) continue;
            // Mejorar con madera o piedra cuando ya trae algo no tiene sentido (la escala ya lo frena arriba)
            boolean ok = m == WOOD ? takePlanks(k, tool.cost()) : true;
            if (m != WOOD) remove(k, m.test(), tool.cost());
            if (!ok || !takeSticks(k, tool.sticks())) return null;
            ItemStack out = new ItemStack(tool.of(m));
            enchantSmall(k, out, tool == SWORD);
            give(k, out);
            return owned > 0 && m != WOOD ? "upgrade" : key;
        }
        return null;
    }

    private static String armor(KikoEntity k) {
        for (int i = 0; i < ARMOR.length; i++) {
            ItemStack cur = k.getEquippedStack(ARMOR[i]);
            if (cur.hasEnchantments()) continue;                       // lo encantado no se cambia
            if (!cur.isEmpty() && !(cur.getItem() instanceof ArmorItem)) continue;
            int curProt = cur.getItem() instanceof ArmorItem a ? a.getProtection() : 0;
            for (int m = 0; m < 2; m++) {
                Item item = ARMOR_ITEMS[i][m];
                Mat mat = m == 0 ? DIAMOND : IRON;
                int prot = ((ArmorItem) item).getProtection();
                if (prot <= curProt || count(k, mat.test()) < ARMOR_COST[i]) continue;
                remove(k, mat.test(), ARMOR_COST[i]);
                ItemStack old = cur.copy();
                k.equipStack(ARMOR[i], new ItemStack(item));
                if (!old.isEmpty()) give(k, old);
                return "armor";
            }
        }
        return null;
    }

    private static String torch(KikoEntity k) {
        int want = k.getCharacter() == KikoCharacter.IVAN ? 32 : 16;
        if (count(k, s -> s.isOf(Items.TORCH)) >= want) return null;
        Predicate<ItemStack> coal = s -> s.isOf(Items.COAL) || s.isOf(Items.CHARCOAL);
        if (count(k, coal) < 1 || !sticksOk(k, 1, 0)) return null;
        remove(k, coal, 1);
        if (!takeSticks(k, 1)) return null;
        give(k, new ItemStack(Items.TORCH, 4));
        return "torch";
    }

    private static long maruCooldown;

    /** Maru: si le sobra hierro (6 o más) le forja a su amigo el pico o la espada que le falte. */
    private static void forgeForPlayer(KikoEntity k, PlayerEntity friend) {
        if (friend == null || friend.squaredDistanceTo(k) > 6 * 6) return;
        long now = k.getWorld().getTime();
        if (now < maruCooldown || count(k, IRON.test()) < 6) return;
        boolean pick = !friend.getInventory().contains(s -> s.isIn(ItemTags.PICKAXES) && s.getItem() instanceof ToolItem t && t.getMaterial().getDurability() >= 250);
        boolean sword = !friend.getInventory().contains(s -> s.isIn(ItemTags.SWORDS) && s.getItem() instanceof ToolItem t && t.getMaterial().getDurability() >= 250);
        if (!pick && !sword) return;
        if (!takeSticks(k, pick ? 2 : 1)) return;
        remove(k, IRON.test(), pick ? 3 : 2);
        ItemStack out = new ItemStack(pick ? Items.IRON_PICKAXE : Items.IRON_SWORD);
        var reg = k.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT);
        reg.getEntry(pick ? Enchantments.EFFICIENCY : Enchantments.SHARPNESS).ifPresent(e -> out.addEnchantment(e, 1));
        friend.getInventory().offerOrDrop(out);
        maruCooldown = now + 12000;
        k.swingHand(Hand.MAIN_HAND);
        KikoInteractionHandler.say(friend, k, "dialogue.maru.craft_gift", 1);
    }
}
