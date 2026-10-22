package com.jaciel.kiko.inventory;

import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.entity.combat.WeaponHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Inventario de Kiko.
 *
 *  slot 0        mano principal (lo que usa para pelear o trabajar, se ve en su mano)
 *  slot 1        otra mano (escudo, antorcha, etc.)
 *  slots 2..5    armadura (casco, pechera, pantalones, botas)
 *  slots 6..32   mochila de 27 espacios
 *
 * Las manos y la armadura son el equipo real de la entidad, así se sincronizan solas con los
 * clientes y cualquier arma o armadura (vanilla o de mods) aplica sus atributos de forma automática.
 */
public class KikoInventory implements Inventory {
    public static final int MAIN_HAND = 0;
    public static final int OFF_HAND = 1;
    public static final int ARMOR_START = 2;
    /** Orden de los espacios de armadura en la pantalla. */
    public static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public static final int BAG_START = ARMOR_START + 4;
    public static final int BAG_SIZE = 27;
    public static final int SIZE = BAG_START + BAG_SIZE;

    private final KikoEntity kiko;
    private final DefaultedList<ItemStack> bag = DefaultedList.ofSize(BAG_SIZE, ItemStack.EMPTY);

    public KikoInventory(KikoEntity kiko) {
        this.kiko = kiko;
    }

    // ------------------------------------------------------------------
    // Inventory
    // ------------------------------------------------------------------

    @Override
    public int size() {
        return SIZE;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < BAG_START; i++) if (!getStack(i).isEmpty()) return false;
        for (ItemStack s : bag) if (!s.isEmpty()) return false;
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        if (slot == MAIN_HAND) return kiko.getEquippedStack(EquipmentSlot.MAINHAND);
        if (slot == OFF_HAND) return kiko.getEquippedStack(EquipmentSlot.OFFHAND);
        if (slot < BAG_START) return kiko.getEquippedStack(ARMOR_SLOTS[slot - ARMOR_START]);
        return bag.get(slot - BAG_START);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack current = getStack(slot);
        if (current.isEmpty() || amount <= 0) return ItemStack.EMPTY;
        ItemStack taken = current.split(amount);
        if (slot < BAG_START) setStack(slot, current); // vuelve a equipar lo que quedó
        markDirty();
        return taken;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack current = getStack(slot);
        setStack(slot, ItemStack.EMPTY);
        return current;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot == MAIN_HAND) kiko.equipStack(EquipmentSlot.MAINHAND, stack);
        else if (slot == OFF_HAND) kiko.equipStack(EquipmentSlot.OFFHAND, stack);
        else if (slot < BAG_START) kiko.equipStack(ARMOR_SLOTS[slot - ARMOR_START], stack);
        else bag.set(slot - BAG_START, stack);
        markDirty();
    }

    @Override
    public void markDirty() {}

    /** Mientras tengas abierto su inventario se queda quieta. */
    @Override
    public void onOpen(PlayerEntity player) {
        kiko.addViewer(player, 1);
    }

    @Override
    public void onClose(PlayerEntity player) {
        kiko.addViewer(player, -1);
    }

    /** En los espacios de armadura solo entra la pieza que corresponde. */
    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (slot >= ARMOR_START && slot < BAG_START) {
            return kiko.getPreferredEquipmentSlot(stack) == ARMOR_SLOTS[slot - ARMOR_START];
        }
        return true;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return kiko.isAlive() && player.squaredDistanceTo(kiko) < 8 * 8;
    }

    @Override
    public void clear() {
        bag.clear();
        kiko.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        kiko.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        for (EquipmentSlot s : ARMOR_SLOTS) kiko.equipStack(s, ItemStack.EMPTY);
    }

    // ------------------------------------------------------------------
    // Utilidades para la IA
    // ------------------------------------------------------------------

    public DefaultedList<ItemStack> bag() {
        return bag;
    }

    /**
     * Mete un stack en la mochila. Regresa lo que no cupo (vacío si cupo todo).
     */
    public ItemStack insert(ItemStack stack) {
        ItemStack rest = stack.copy();
        // Primero junta con stacks iguales
        for (int i = 0; i < BAG_SIZE && !rest.isEmpty(); i++) {
            ItemStack s = bag.get(i);
            if (!s.isEmpty() && ItemStack.areItemsAndComponentsEqual(s, rest) && s.getCount() < s.getMaxCount()) {
                int move = Math.min(rest.getCount(), s.getMaxCount() - s.getCount());
                s.increment(move);
                rest.decrement(move);
            }
        }
        // Luego espacios vacíos
        for (int i = 0; i < BAG_SIZE && !rest.isEmpty(); i++) {
            if (bag.get(i).isEmpty()) {
                bag.set(i, rest.copy());
                rest = ItemStack.EMPTY;
            }
        }
        return rest;
    }

    public int freeBagSlots() {
        int free = 0;
        for (ItemStack s : bag) if (s.isEmpty()) free++;
        return free;
    }

    /** Herramientas, armas y armadura: nunca las guarda en el cofre ni las mete al horno. */
    public static boolean shouldKeepTool(ItemStack s) {
        if (s.isEmpty()) return false;
        if (WeaponHelper.isAxe(s) || WeaponHelper.isPickaxe(s) || s.isIn(ItemTags.SHOVELS) || s.isIn(ItemTags.HOES)) return true;
        if (s.getItem() instanceof FishingRodItem || s.isOf(Items.SHEARS)) return true;
        if (WeaponHelper.isRanged(s) || WeaponHelper.meleeBonus(s) > 0) return true;
        return s.getItem() instanceof ArmorItem || s.getItem() instanceof ShieldItem;
    }

    /**
     * Lo que Kiko se queda completo al guardar en un cofre. Todo lo demás (madera, minerales, piedra...) se guarda.
     */
    public static boolean shouldKeep(ItemStack s) {
        if (s.isEmpty()) return false;
        if (shouldKeepTool(s)) return true;
        if (s.contains(DataComponentTypes.POTION_CONTENTS)) return true;
        return s.isOf(Items.TORCH) || s.isIn(ItemTags.ARROWS) || s.isIn(ItemTags.SAPLINGS) || s.isOf(Items.BONE_MEAL);
    }

    /**
     * Cosas de las que se queda UN stack (de cada tipo) para seguir trabajando:
     * piedra para tapar lava, tierra para rellenar, semillas para replantar y comida para curarse.
     */
    private static boolean keepOneStack(ItemStack s) {
        if (s.isOf(Items.COBBLESTONE) || s.isOf(Items.COBBLED_DEEPSLATE) || s.isOf(Items.DIRT)) return true;
        if (s.contains(DataComponentTypes.FOOD)) return true;
        if (s.getItem() instanceof BlockItem bi) {
            Block b = bi.getBlock();
            return b instanceof CropBlock || b instanceof StemBlock || b == Blocks.NETHER_WART;
        }
        return s.isOf(Items.WHEAT);
    }

    /**
     * Pasa la mochila a un contenedor, quedándose con lo útil y un stack de piedra, semillas y comida.
     * @return cuántos ítems pasó
     */
    public int depositInto(Inventory container) {
        int moved = 0;
        Set<Item> keptOne = new HashSet<>();
        for (int i = 0; i < BAG_SIZE; i++) {
            ItemStack stack = bag.get(i);
            if (stack.isEmpty() || shouldKeep(stack)) continue;
            if (keepOneStack(stack) && keptOne.add(stack.getItem())) continue;
            int before = stack.getCount();
            insertInto(container, stack);
            moved += before - stack.getCount();
            if (stack.isEmpty()) bag.set(i, ItemStack.EMPTY);
        }
        container.markDirty();
        return moved;
    }

    /** Mete stack en el contenedor (lo que no cabe se queda en stack). */
    private static void insertInto(Inventory container, ItemStack stack) {
        for (int s = 0; s < container.size() && !stack.isEmpty(); s++) {
            ItemStack there = container.getStack(s);
            if (!there.isEmpty() && ItemStack.areItemsAndComponentsEqual(there, stack)) {
                int max = Math.min(there.getMaxCount(), container.getMaxCountPerStack());
                int move = Math.min(stack.getCount(), max - there.getCount());
                if (move > 0) {
                    there.increment(move);
                    stack.decrement(move);
                }
            }
        }
        for (int s = 0; s < container.size() && !stack.isEmpty(); s++) {
            if (container.getStack(s).isEmpty() && container.isValid(s, stack)) {
                int move = Math.min(stack.getCount(), Math.min(stack.getMaxCount(), container.getMaxCountPerStack()));
                container.setStack(s, stack.split(move));
            }
        }
    }

    public boolean isBagFull() {
        for (ItemStack s : bag) {
            if (s.isEmpty() || s.getCount() < s.getMaxCount()) return false;
        }
        return true;
    }

    /** Índice en la mochila (0..26) del primer stack que cumple, o -1. */
    public int findInBag(Predicate<ItemStack> test) {
        for (int i = 0; i < BAG_SIZE; i++) {
            ItemStack s = bag.get(i);
            if (!s.isEmpty() && test.test(s)) return i;
        }
        return -1;
    }

    /** Intercambia la mano principal con un espacio de la mochila. */
    public void swapToMainHand(int bagIndex) {
        if (bagIndex < 0 || bagIndex >= BAG_SIZE) return;
        ItemStack hand = kiko.getMainHandStack().copy();
        ItemStack fromBag = bag.get(bagIndex);
        kiko.equipStack(EquipmentSlot.MAINHAND, fromBag);
        bag.set(bagIndex, hand);
    }

    // ------------------------------------------------------------------
    // Guardado (las manos las guarda MobEntity por su cuenta)
    // ------------------------------------------------------------------

    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtCompound tag = new NbtCompound();
        Inventories.writeNbt(tag, bag, registries);
        nbt.put("KikoBag", tag);
    }

    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        bag.clear();
        if (nbt.contains("KikoBag")) {
            Inventories.readNbt(nbt.getCompound("KikoBag"), bag, registries);
        }
    }
}
