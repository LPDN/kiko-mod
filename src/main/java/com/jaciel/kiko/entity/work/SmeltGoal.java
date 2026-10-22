package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.inventory.KikoInventory;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SmokerBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Usa el horno. Atiende todos los hornos, altos hornos y ahumadores a 16 bloques.
 * Reconoce cada tipo: los minerales van al alto horno, la comida al ahumador y lo demás
 * (o todo, si no hay hornos especiales) al horno normal.
 *  - Saca lo que ya está listo y lo guarda en su mochila (luego lo lleva al cofre con lo demás)
 *  - Mete minerales crudos y comida cruda que traiga en la mochila
 *  - Le pone combustible (carbón primero, luego bloques de carbón, varas de blaze, tablas o troncos)
 * No mete sus herramientas, armas ni nada que no sea para fundir o cocinar.
 */
public class SmeltGoal extends WalkWorkGoal {
    private static final int RANGE = 16;

    /** Hay alto horno / ahumador cerca (los minerales y la comida se mandan ahí, que es el doble de rápido). */
    private boolean hasBlast;
    private boolean hasSmoker;

    public SmeltGoal(KikoEntity kiko) {
        super(kiko, WorkMode.SMELT);
    }

    @Override
    protected boolean prepare() {
        return true;
    }

    @Override
    protected int radius() {
        return RANGE;
    }

    @Override
    protected int idleCooldown() {
        return 60;
    }

    @Override
    @Nullable
    protected BlockPos findTarget(World world, BlockPos from, BlockPos center, int radius) {
        // Primero ve qué tipos de horno hay, para mandar cada cosa al mejor
        hasBlast = false;
        hasSmoker = false;
        for (BlockPos pos : BlockPos.iterateOutwards(center, radius, 6, radius)) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof BlastFurnaceBlockEntity) hasBlast = true;
            else if (be instanceof SmokerBlockEntity) hasSmoker = true;
        }
        boolean anyFurnace = false;
        for (BlockPos pos : BlockPos.iterateOutwards(center, radius, 6, radius)) {
            if (isIgnored(pos)) continue;
            if (!(world.getBlockEntity(pos) instanceof AbstractFurnaceBlockEntity furnace)) continue;
            anyFurnace = true;
            if (needsService(world, furnace)) return pos.toImmutable();
        }
        if (!anyFurnace) kiko.notifyOwner("message.kiko.no_furnace");
        return null;
    }

    @Override
    protected boolean isValidTarget(World world, BlockPos pos) {
        return world.getBlockEntity(pos) instanceof AbstractFurnaceBlockEntity;
    }

    @Override
    protected boolean act(ServerWorld world, BlockPos pos) {
        if (actTicks < 6) return false;
        if (!(world.getBlockEntity(pos) instanceof AbstractFurnaceBlockEntity furnace)) return true;
        kiko.swingHand(Hand.MAIN_HAND);
        KikoInventory inv = kiko.getInventory();
        boolean changed = false;

        // 1) Sacar lo cocinado
        ItemStack out = furnace.getStack(2);
        if (!out.isEmpty()) {
            ItemStack rest = inv.insert(out);
            furnace.setStack(2, rest);
            changed |= rest.getCount() != out.getCount();
        }

        // 2) Meter lo que hay que cocinar
        ItemStack input = furnace.getStack(0);
        int slot = inv.findInBag(s -> canCook(world, furnace, s) && (input.isEmpty() || ItemStack.areItemsAndComponentsEqual(s, input)));
        if (slot >= 0 && (input.isEmpty() || input.getCount() < input.getMaxCount())) {
            ItemStack from = inv.bag().get(slot);
            ItemStack shown = from.copy();
            int before = from.getCount();
            if (moveInto(furnace, 0, from)) {
                changed = true;
                kiko.tellOwnerActionBar(Text.translatable("message.kiko.smelt_loaded", before - from.getCount(),
                        shown.getName(), world.getBlockState(pos).getBlock().getName()).formatted(Formatting.GOLD));
            }
        }

        // 3) Combustible, solo si hay algo que cocinar
        if (!furnace.getStack(0).isEmpty()) {
            ItemStack fuel = furnace.getStack(1);
            int f = findFuel(fuel);
            if (f >= 0 && (fuel.isEmpty() || fuel.getCount() < fuel.getMaxCount())) {
                changed |= moveInto(furnace, 1, inv.bag().get(f));
            }
        }

        if (changed) {
            furnace.markDirty();
            world.playSound(null, pos, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 0.8f, 1f);
        }
        // No regresa al mismo horno hasta dentro de un rato
        ignore(pos, changed ? 100 : 300);
        return true;
    }

    // ------------------------------------------------------------------

    private boolean needsService(World world, AbstractFurnaceBlockEntity furnace) {
        if (!furnace.getStack(2).isEmpty()) return true;
        ItemStack input = furnace.getStack(0);
        KikoInventory inv = kiko.getInventory();
        if (input.isEmpty() && inv.findInBag(s -> canCook(world, furnace, s)) >= 0) return true;
        if (!input.isEmpty() && input.getCount() < input.getMaxCount()
                && inv.findInBag(s -> ItemStack.areItemsAndComponentsEqual(s, input)) >= 0) return true;
        if ((!input.isEmpty() || inv.findInBag(s -> canCook(world, furnace, s)) >= 0)
                && furnace.getStack(1).isEmpty() && findFuel(ItemStack.EMPTY) >= 0) return true;
        return false;
    }

    private static RecipeType<? extends AbstractCookingRecipe> typeOf(AbstractFurnaceBlockEntity furnace) {
        if (furnace instanceof BlastFurnaceBlockEntity) return RecipeType.BLASTING;
        if (furnace instanceof SmokerBlockEntity) return RecipeType.SMOKING;
        return RecipeType.SMELTING;
    }

    /** Minerales y metales crudos (van al alto horno). */
    private static boolean isOreLike(ItemStack s) {
        return s.isIn(ConventionalItemTags.RAW_MATERIALS) || s.isIn(ConventionalItemTags.ORES) || s.isOf(Items.ANCIENT_DEBRIS);
    }

    /** Comida cruda (va al ahumador). */
    private static boolean isRawFood(ItemStack s) {
        return s.isIn(ConventionalItemTags.RAW_MEAT_FOODS) || s.isIn(ConventionalItemTags.RAW_FISH_FOODS)
                || s.isOf(Items.POTATO) || s.isOf(Items.KELP)
                || (s.contains(DataComponentTypes.FOOD) && !s.isOf(Items.BAKED_POTATO));
    }

    /** Bloques que solo el horno normal hace: vidrio, ladrillo, piedra lisa, tinte verde... */
    private static boolean isBlockSmeltable(ItemStack s) {
        return s.isIn(ItemTags.SAND) || s.isOf(Items.CLAY_BALL) || s.isOf(Items.CLAY) || s.isOf(Items.CACTUS)
                || s.isOf(Items.WET_SPONGE) || s.isOf(Items.NETHERRACK) || s.isOf(Items.SEA_PICKLE)
                || s.isOf(Items.CHORUS_FRUIT);
    }

    /**
     * Qué puede meter en cada tipo de horno:
     *  - Alto horno: minerales y metales crudos
     *  - Ahumador: comida cruda
     *  - Horno normal: lo que no tenga un horno especial cerca, más arena, barro, cactus, etc.
     * Siempre revisa que exista la receta en ese horno (así sirven los minerales y comidas de mods).
     */
    private boolean canCook(World world, AbstractFurnaceBlockEntity furnace, ItemStack s) {
        if (s.isEmpty() || KikoInventory.shouldKeepTool(s)) return false;
        boolean wanted;
        if (furnace instanceof BlastFurnaceBlockEntity) wanted = isOreLike(s);
        else if (furnace instanceof SmokerBlockEntity) wanted = isRawFood(s);
        else wanted = (isOreLike(s) && !hasBlast) || (isRawFood(s) && !hasSmoker) || isBlockSmeltable(s);
        if (!wanted) return false;
        return world.getRecipeManager().getFirstMatch(typeOf(furnace), new SingleStackRecipeInput(s), world).isPresent();
    }

    /** Mejor combustible de la mochila (compatible con el que ya está). Índice o -1. */
    private int findFuel(ItemStack current) {
        KikoInventory inv = kiko.getInventory();
        java.util.function.Predicate<ItemStack> same = s -> current.isEmpty() || ItemStack.areItemsAndComponentsEqual(s, current);
        int i = inv.findInBag(s -> same.test(s) && (s.isOf(Items.COAL) || s.isOf(Items.CHARCOAL)));
        if (i < 0) i = inv.findInBag(s -> same.test(s) && (s.isOf(Items.COAL_BLOCK) || s.isOf(Items.BLAZE_ROD) || s.isOf(Items.DRIED_KELP_BLOCK)));
        if (i < 0) i = inv.findInBag(s -> same.test(s) && (s.isIn(ItemTags.PLANKS) || s.isIn(ItemTags.LOGS_THAT_BURN))
                && AbstractFurnaceBlockEntity.canUseAsFuel(s));
        return i;
    }

    /** Pasa todo lo que quepa de stack al espacio del horno. */
    private static boolean moveInto(AbstractFurnaceBlockEntity furnace, int slot, ItemStack stack) {
        if (stack.isEmpty()) return false;
        ItemStack there = furnace.getStack(slot);
        if (there.isEmpty()) {
            furnace.setStack(slot, stack.split(stack.getCount()));
            return true;
        }
        if (!ItemStack.areItemsAndComponentsEqual(there, stack)) return false;
        int move = Math.min(stack.getCount(), there.getMaxCount() - there.getCount());
        if (move <= 0) return false;
        there.increment(move);
        stack.decrement(move);
        return true;
    }
}
