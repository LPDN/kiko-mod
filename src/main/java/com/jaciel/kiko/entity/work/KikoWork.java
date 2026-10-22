package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.entity.KikoEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Cosas compartidas entre todos los trabajos con bloques. */
public final class KikoWork {
    private KikoWork() {}

    /**
     * Cuánto avanza por tick al romper un bloque (1.0 = roto).
     * Igual que el jugador, pero tomando en cuenta cosas que a los mobs normalmente no les aplican
     *  - Eficiencia de la herramienta
     *  - Efecto de Prisa (Haste)
     *  - Un bonus de hasta 50% según el afecto de su mejor amigo
     */
    public static float breakDelta(KikoEntity kiko, BlockState state, BlockPos pos) {
        World world = kiko.getWorld();
        float hardness = state.getHardness(world, pos);
        if (hardness < 0) return 0f;
        if (hardness == 0) return 1f;

        ItemStack tool = kiko.getMainHandStack();
        float speed = tool.getMiningSpeedMultiplier(state);
        if (speed > 1f) {
            int eff = efficiencyLevel(world, tool);
            if (eff > 0) speed += eff * eff + 1;
        }
        StatusEffectInstance haste = kiko.getStatusEffect(StatusEffects.HASTE);
        if (haste != null) speed *= 1f + (haste.getAmplifier() + 1) * 0.2f;
        speed *= 1f + Math.max(0f, kiko.getBestFriendAffection()) / 200f;

        boolean proper = !state.isToolRequired() || tool.isSuitableFor(state);
        return speed / hardness / (proper ? 30f : 100f);
    }

    private static int efficiencyLevel(World world, ItemStack tool) {
        if (tool.isEmpty()) return 0;
        return world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.EFFICIENCY)
                .map(entry -> EnchantmentHelper.getLevel(entry, tool))
                .orElse(0);
    }

    /**
     * Rompe un bloque como si fuera ella. Guarda lo que suelta en su mochila y gasta la herramienta.
     * @return false si ya se le rompió la herramienta
     */
    /** Estructuras del mod (casas, pueblos...). Dentro de ellas no se rompe nada de lo construido. */
    private static final net.minecraft.registry.tag.TagKey<net.minecraft.world.gen.structure.Structure> PROTECTED =
            net.minecraft.registry.tag.TagKey.of(net.minecraft.registry.RegistryKeys.STRUCTURE, com.jaciel.kiko.KikoMod.id("protected"));

    /**
     * ¿Es parte de una casa o estructura del mod? Hojas, cultivos, plantas y troncos normales sí se pueden trabajar
     * (los árboles y huertos del pueblo), todo lo demás de adentro se respeta.
     */
    public static boolean isProtected(net.minecraft.world.World world, BlockPos pos, BlockState state) {
        if (state.isAir() || !(world instanceof ServerWorld sw)) return false;
        if (state.isIn(net.minecraft.registry.tag.BlockTags.LEAVES) || state.isIn(net.minecraft.registry.tag.BlockTags.CROPS)
                || state.isIn(net.minecraft.registry.tag.BlockTags.FLOWERS) || state.isIn(net.minecraft.registry.tag.BlockTags.SAPLINGS)
                || state.isIn(net.minecraft.registry.tag.BlockTags.REPLACEABLE)) return false;
        if (state.isIn(net.minecraft.registry.tag.BlockTags.LOGS)
                && !net.minecraft.registry.Registries.BLOCK.getId(state.getBlock()).getPath().startsWith("stripped_")) return false;
        return sw.getStructureAccessor().getStructureContaining(pos, PROTECTED).hasChildren();
    }

    public static boolean harvest(KikoEntity kiko, ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.isAir()) return true;
        if (isProtected(world, pos, state)) return true;      // no rompe casas
        ItemStack tool = kiko.getMainHandStack();
        if (tool.isEmpty() || isAlmostBroken(tool)) return false; // no le rompe tus herramientas

        BlockEntity be = world.getBlockEntity(pos);
        List<ItemStack> drops = Block.getDroppedStacks(state, world, pos, be, kiko, tool);
        state.onStacksDropped(world, pos, tool, true); // experiencia de los minerales
        world.breakBlock(pos, false, kiko);
        // v3.3 Iván el minero saca el doble de los minerales
        boolean doubleOre = kiko.getCharacter() == com.jaciel.kiko.entity.KikoCharacter.IVAN
                && (net.minecraft.registry.Registries.BLOCK.getId(state.getBlock()).getPath().endsWith("_ore")
                    || state.isOf(net.minecraft.block.Blocks.ANCIENT_DEBRIS));
        for (ItemStack drop : drops) {
            if (doubleOre && !drop.isOf(state.getBlock().asItem())) drop.setCount(Math.min(drop.getMaxCount(), drop.getCount() * 2));
            ItemStack rest = kiko.getInventory().insert(drop);
            if (!rest.isEmpty()) Block.dropStack(world, pos, rest);
        }
        tool.damage(1, kiko, EquipmentSlot.MAINHAND);
        return !kiko.getMainHandStack().isEmpty();
    }

    /**
     * Rompe un bloque que no necesita herramienta (cultivos, plantas). Guarda lo que suelta.
     * Si trae algo en la mano (una azada con Fortuna, por ejemplo) cuenta para lo que suelta.
     */
    public static void collect(KikoEntity kiko, ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.isAir()) return;
        BlockEntity be = world.getBlockEntity(pos);
        List<ItemStack> drops = Block.getDroppedStacks(state, world, pos, be, kiko, kiko.getMainHandStack());
        world.breakBlock(pos, false, kiko);
        for (ItemStack drop : drops) {
            ItemStack rest = kiko.getInventory().insert(drop);
            if (!rest.isEmpty()) Block.dropStack(world, pos, rest);
        }
    }

    /**
     * Le echa polvo de hueso de la mochila a una planta (brote, cultivo) hasta que crezca o se acabe.
     * @return cuántos usó
     */
    public static int boneMeal(KikoEntity kiko, ServerWorld world, BlockPos pos, int max) {
        int used = 0;
        for (int i = 0; i < max; i++) {
            BlockState before = world.getBlockState(pos);
            if (!(before.getBlock() instanceof net.minecraft.block.Fertilizable)) break;
            int slot = kiko.getInventory().findInBag(s -> s.isOf(net.minecraft.item.Items.BONE_MEAL));
            if (slot < 0) break;
            ItemStack bm = kiko.getInventory().bag().get(slot);
            if (!net.minecraft.item.BoneMealItem.useOnFertilizable(bm, world, pos)) break;
            world.syncWorldEvent(1505, pos, 15); // partículas verdes
            used++;
            if (!world.getBlockState(pos).isOf(before.getBlock())) break; // ya creció (brote -> árbol)
        }
        if (used > 0) kiko.swingHand(net.minecraft.util.Hand.MAIN_HAND);
        return used;
    }

    /** Gasta un uso de lo que trae en la mano (si se puede gastar). */
    public static void damageHand(KikoEntity kiko) {
        ItemStack hand = kiko.getMainHandStack();
        if (hand.isDamageable()) hand.damage(1, kiko, EquipmentSlot.MAINHAND);
    }

    /** Mineral de vanilla o de mods (tag común c:ores). */
    public static boolean isOre(BlockState state) {
        return state.isIn(ConventionalBlockTags.ORES);
    }

    /** true si el bloque tiene al menos una cara al aire. */
    public static boolean isExposed(World world, BlockPos pos) {
        for (Direction d : Direction.values()) {
            if (world.getBlockState(pos.offset(d)).isAir()) return true;
        }
        return false;
    }

    /**
     * Saca una veta completa (bloques iguales conectados) empezando en start.
     * @return cuántos bloques sacó
     */
    public static int mineVein(KikoEntity kiko, ServerWorld world, BlockPos start, int max) {
        Block oreBlock = world.getBlockState(start).getBlock();
        Set<Long> seen = new HashSet<>();
        Deque<BlockPos> queue = new ArrayDeque<>();
        queue.add(start);
        seen.add(start.asLong());
        int mined = 0;
        while (!queue.isEmpty() && mined < max) {
            BlockPos p = queue.poll();
            for (Direction d : Direction.values()) {
                BlockPos n = p.offset(d);
                if (world.getBlockState(n).isOf(oreBlock) && seen.add(n.asLong())) queue.add(n);
            }
            if (kiko.getInventory().isBagFull()) break;
            if (!harvest(kiko, world, p)) break;
            mined++;
        }
        return mined;
    }

    /** true si a la herramienta le quedan menos de 5 usos (para no romperla). */
    public static boolean isAlmostBroken(ItemStack tool) {
        return tool.isDamageable() && tool.getMaxDamage() - tool.getDamage() <= 5;
    }
}
