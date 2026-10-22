package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.entity.KikoEntity;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Bosque infinito (junto con ChopTreeGoal, que tala).
 *  - Planta los brotes que trae en pasto o tierra, separados 3 bloques para que crezcan bien
 *  - Si trae polvo de hueso se lo echa a los brotes para que crezcan rápido
 * Los brotes que caen de las hojas los recoge sola, así que nunca se le acaban.
 */
public class PlantTreesGoal extends WalkWorkGoal {
    private static final int SPACING = 3;

    public PlantTreesGoal(KikoEntity kiko) {
        super(kiko, WorkMode.FOREST);
    }

    @Override
    protected boolean prepare() {
        return findSapling() >= 0 || findBoneMeal() >= 0;
    }

    @Override
    protected int idleCooldown() {
        return 80;
    }

    @Override
    @Nullable
    protected BlockPos findTarget(World world, BlockPos from, BlockPos center, int radius) {
        boolean hasSapling = findSapling() >= 0;
        boolean hasBoneMeal = findBoneMeal() >= 0;
        int range = radius + 4;
        for (BlockPos pos : BlockPos.iterateOutwards(from, range, 4, range)) {
            if (!inArea(pos, center, radius) || !world.isChunkLoaded(pos) || isIgnored(pos)) continue;
            BlockState state = world.getBlockState(pos);
            if (hasBoneMeal && state.isIn(BlockTags.SAPLINGS)) return pos.toImmutable();
            if (hasSapling && canPlantAt(world, pos, state)) return pos.toImmutable();
        }
        return null;
    }

    @Override
    protected boolean isValidTarget(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isIn(BlockTags.SAPLINGS) || canPlantAt(world, pos, state);
    }

    @Override
    protected boolean act(ServerWorld world, BlockPos pos) {
        if (actTicks == 1) kiko.swingHand(Hand.MAIN_HAND);
        if (actTicks < 8) return false;

        BlockState state = world.getBlockState(pos);
        if (!state.isIn(BlockTags.SAPLINGS)) {
            int slot = findSapling();
            if (slot < 0) return true;
            ItemStack sapling = kiko.getInventory().bag().get(slot);
            BlockState plant = ((BlockItem) sapling.getItem()).getBlock().getDefaultState();
            if (!plant.canPlaceAt(world, pos)) {
                ignore(pos, 6000);
                return true;
            }
            world.setBlockState(pos, plant);
            world.playSound(null, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1f, 1f);
            sapling.decrement(1);
        }

        // Polvo de hueso de la mochila hasta que crezca el árbol
        KikoWork.boneMeal(kiko, world, pos, 8);
        // Si no creció (o no tiene polvo de hueso) no insiste con este brote por un rato
        if (world.getBlockState(pos).isIn(BlockTags.SAPLINGS)) ignore(pos, 2400);
        return true;
    }

    // ------------------------------------------------------------------

    private boolean canPlantAt(World world, BlockPos pos, BlockState state) {
        if (!state.isAir() && !state.isReplaceable()) return false;
        if (!world.getBlockState(pos.down()).isIn(BlockTags.DIRT)) return false;
        if (!world.getBlockState(pos.up()).isAir()) return false;
        if (!world.isSkyVisible(pos)) return false;
        // Separados de otros árboles, brotes y del agua para que crezcan
        for (BlockPos p : BlockPos.iterate(pos.add(-SPACING + 1, 0, -SPACING + 1), pos.add(SPACING - 1, 2, SPACING - 1))) {
            BlockState s = world.getBlockState(p);
            if (s.isIn(BlockTags.SAPLINGS) || s.isIn(BlockTags.LOGS) || !s.getFluidState().isEmpty()) return false;
        }
        return true;
    }

    private int findSapling() {
        return kiko.getInventory().findInBag(s -> s.isIn(ItemTags.SAPLINGS) && s.getItem() instanceof BlockItem);
    }

    private int findBoneMeal() {
        return kiko.getInventory().findInBag(s -> s.isOf(Items.BONE_MEAL));
    }
}
