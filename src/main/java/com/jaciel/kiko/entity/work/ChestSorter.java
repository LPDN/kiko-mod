package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.entity.combat.WeaponHelper;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.CropBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Ordena varios cofres por categoría (Fase 5).
 *  1. Junta todo y completa los stacks a medias
 *  2. A cada categoría le toca el cofre que ya tenía más de esas cosas (y más cofres si no cabe)
 *  3. Lo que sobra va a los cofres libres
 *  4. Dentro de cada cofre acomoda por tipo
 * Si algo no cabe regresa todo como estaba, nunca se pierde nada.
 */
public final class ChestSorter {
    private ChestSorter() {}

    public enum Category {
        EQUIPMENT("equipment"), FOOD("food"), MINERALS("minerals"), WOOD("wood"), STONE("stone"),
        PLANTS("plants"), MOB_DROPS("mob_drops"), BLOCKS("blocks"), MISC("misc");

        public final String id;

        Category(String id) {
            this.id = id;
        }

        public String translationKey() {
            return "sort.kiko." + id;
        }
    }

    public static Category classify(ItemStack s) {
        if (s.isDamageable() || s.getItem() instanceof ArmorItem || s.getItem() instanceof ShieldItem
                || WeaponHelper.isRanged(s) || s.isIn(ItemTags.ARROWS)) return Category.EQUIPMENT;
        if (s.contains(DataComponentTypes.FOOD)) return Category.FOOD;
        if (s.isIn(ConventionalItemTags.ORES) || s.isIn(ConventionalItemTags.RAW_MATERIALS) || s.isIn(ConventionalItemTags.INGOTS)
                || s.isIn(ConventionalItemTags.GEMS) || s.isIn(ConventionalItemTags.NUGGETS) || s.isIn(ConventionalItemTags.DUSTS)
                || s.isOf(Items.COAL) || s.isOf(Items.CHARCOAL) || s.isOf(Items.FLINT) || s.isOf(Items.AMETHYST_SHARD)
                || s.isOf(Items.ANCIENT_DEBRIS) || s.isOf(Items.NETHERITE_SCRAP)) return Category.MINERALS;
        if (s.isIn(ItemTags.LOGS) || s.isIn(ItemTags.PLANKS) || s.isIn(ItemTags.WOODEN_SLABS) || s.isIn(ItemTags.WOODEN_STAIRS)
                || s.isIn(ItemTags.WOODEN_FENCES) || s.isOf(Items.STICK)) return Category.WOOD;
        if (s.isIn(ItemTags.SAPLINGS) || s.isIn(ItemTags.FLOWERS) || s.isIn(ItemTags.LEAVES)
                || s.isOf(Items.WHEAT) || s.isOf(Items.SUGAR_CANE) || s.isOf(Items.BAMBOO) || s.isOf(Items.CACTUS)
                || s.isOf(Items.KELP) || s.isOf(Items.VINE) || s.isOf(Items.BONE_MEAL)) return Category.PLANTS;
        if (s.isOf(Items.BONE) || s.isOf(Items.STRING) || s.isOf(Items.ROTTEN_FLESH) || s.isOf(Items.GUNPOWDER)
                || s.isOf(Items.SPIDER_EYE) || s.isOf(Items.ENDER_PEARL) || s.isOf(Items.SLIME_BALL) || s.isOf(Items.LEATHER)
                || s.isOf(Items.FEATHER) || s.isOf(Items.BLAZE_ROD) || s.isOf(Items.GHAST_TEAR) || s.isOf(Items.PHANTOM_MEMBRANE)
                || s.isOf(Items.INK_SAC) || s.isOf(Items.GLOW_INK_SAC) || s.isOf(Items.RABBIT_HIDE) || s.isOf(Items.EGG)
                || s.isIn(ItemTags.WOOL)) return Category.MOB_DROPS;
        if (s.getItem() instanceof BlockItem bi) {
            Block b = bi.getBlock();
            var st = b.getDefaultState();
            if (b instanceof CropBlock || b instanceof StemBlock || b instanceof PlantBlock) return Category.PLANTS;
            if (st.isIn(BlockTags.BASE_STONE_OVERWORLD) || st.isIn(BlockTags.BASE_STONE_NETHER) || st.isIn(BlockTags.DIRT)
                    || st.isIn(BlockTags.SAND) || s.isOf(Items.COBBLESTONE) || s.isOf(Items.COBBLED_DEEPSLATE)
                    || s.isOf(Items.GRAVEL) || s.isOf(Items.CLAY)) return Category.STONE;
            return Category.BLOCKS;
        }
        return Category.MISC;
    }

    public record Result(boolean ok, int chests, int items, List<Category> used) {}

    public static Result sort(List<Inventory> invs) {
        if (invs.isEmpty()) return new Result(false, 0, 0, List.of());

        // Copia de seguridad
        List<List<ItemStack>> backup = new ArrayList<>();
        for (Inventory inv : invs) {
            List<ItemStack> copy = new ArrayList<>();
            for (int i = 0; i < inv.size(); i++) copy.add(inv.getStack(i).copy());
            backup.add(copy);
        }

        // 1) Juntar todo, completando stacks
        List<ItemStack> pool = new ArrayList<>();
        int[][] counts = new int[invs.size()][Category.values().length];
        int total = 0;
        for (int c = 0; c < invs.size(); c++) {
            Inventory inv = invs.get(c);
            for (int i = 0; i < inv.size(); i++) {
                ItemStack s = inv.getStack(i);
                if (s.isEmpty()) continue;
                counts[c][classify(s).ordinal()] += s.getCount();
                total += s.getCount();
                merge(pool, s.copy());
            }
        }
        Map<Category, List<ItemStack>> byCat = new EnumMap<>(Category.class);
        for (ItemStack s : pool) byCat.computeIfAbsent(classify(s), k -> new ArrayList<>()).add(s);
        Comparator<ItemStack> order = Comparator.<ItemStack>comparingInt(s -> Registries.ITEM.getRawId(s.getItem()))
                .thenComparing(Comparator.comparingInt(ItemStack::getCount).reversed());
        byCat.values().forEach(l -> l.sort(order));

        // 2) Categorías con más stacks escogen primero su cofre
        List<Category> cats = new ArrayList<>(byCat.keySet());
        cats.sort(Comparator.comparingInt((Category k) -> byCat.get(k).size()).reversed());
        boolean[] taken = new boolean[invs.size()];
        Map<Category, List<Integer>> assigned = new EnumMap<>(Category.class);
        for (Category cat : cats) {
            int need = byCat.get(cat).size();
            List<Integer> mine = new ArrayList<>();
            while (need > 0) {
                int best = -1;
                for (int c = 0; c < invs.size(); c++) {
                    if (taken[c]) continue;
                    if (best < 0 || counts[c][cat.ordinal()] > counts[best][cat.ordinal()]) best = c;
                }
                if (best < 0) break;
                taken[best] = true;
                mine.add(best);
                need -= invs.get(best).size();
            }
            assigned.put(cat, mine);
        }

        // 3) Vaciar y volver a llenar
        for (Inventory inv : invs) inv.clear();
        int[] next = new int[invs.size()];
        List<ItemStack> leftover = new ArrayList<>();
        for (Category cat : cats) {
            for (ItemStack s : byCat.get(cat)) {
                if (!place(invs, next, assigned.get(cat), s)) leftover.add(s);
            }
        }
        List<Integer> all = new ArrayList<>();
        for (int c = 0; c < invs.size(); c++) if (!taken[c]) all.add(c);
        for (int c = 0; c < invs.size(); c++) if (taken[c]) all.add(c);
        for (ItemStack s : leftover) {
            if (!place(invs, next, all, s)) {
                // No cupo: todo regresa como estaba
                for (int c = 0; c < invs.size(); c++) {
                    Inventory inv = invs.get(c);
                    for (int i = 0; i < inv.size(); i++) inv.setStack(i, backup.get(c).get(i));
                    inv.markDirty();
                }
                return new Result(false, invs.size(), total, List.of());
            }
        }
        for (Inventory inv : invs) inv.markDirty();
        return new Result(true, invs.size(), total, cats);
    }

    /** Mete s en el siguiente espacio libre de esos cofres. */
    private static boolean place(List<Inventory> invs, int[] next, List<Integer> chests, ItemStack s) {
        for (int c : chests) {
            Inventory inv = invs.get(c);
            while (next[c] < inv.size() && !inv.getStack(next[c]).isEmpty()) next[c]++;
            if (next[c] < inv.size()) {
                inv.setStack(next[c]++, s);
                return true;
            }
        }
        return false;
    }

    private static void merge(List<ItemStack> pool, ItemStack s) {
        for (ItemStack p : pool) {
            if (s.isEmpty()) return;
            if (ItemStack.areItemsAndComponentsEqual(p, s) && p.getCount() < p.getMaxCount()) {
                int move = Math.min(s.getCount(), p.getMaxCount() - p.getCount());
                p.increment(move);
                s.decrement(move);
            }
        }
        while (!s.isEmpty()) {
            pool.add(s.split(Math.min(s.getCount(), s.getMaxCount())));
        }
    }
}
