package com.jaciel.kiko.block;

import com.jaciel.kiko.item.Drink;
import com.jaciel.kiko.item.DrinkItem;
import com.jaciel.kiko.registry.ModBlocks;
import com.jaciel.kiko.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Jarra de agua fresca (v3.6). Se hace en la mesa con una jarra vacía y los ingredientes, rinde 4 vasos.
 * Con un vaso vacío y clic derecho sacas uno. Cuando se acaba queda la jarra vacía.
 */
public class PitcherBlock extends Block {
    public static final IntProperty LEVEL = IntProperty.of("level", 0, 4);
    public static final MapCodec<PitcherBlock> CODEC = createCodec(s -> new PitcherBlock(s, null));
    private static final VoxelShape SHAPE = Block.createCuboidShape(4, 0, 4, 12, 11, 12);

    @Nullable
    private final Drink drink;

    public PitcherBlock(Settings settings, @Nullable Drink drink) {
        super(settings);
        this.drink = drink;
        setDefaultState(getStateManager().getDefaultState().with(LEVEL, drink == null ? 0 : 4));
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(LEVEL, drink == null ? 0 : 4);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ItemStack held = player.getMainHandStack();
        if (drink == null || !held.isOf(ModItems.CUP)) return ActionResult.PASS;
        if (world.isClient) return ActionResult.SUCCESS;
        int level = state.get(LEVEL);
        if (!player.getAbilities().creativeMode) held.decrement(1);
        ItemStack out = new ItemStack(drink.item);
        if (!player.getInventory().insertStack(out)) player.dropItem(out, false);
        world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 0.8f, 1.2f);
        if (level <= 1) {
            world.setBlockState(pos, ModBlocks.PITCHER.getDefaultState());
        } else {
            world.setBlockState(pos, state.with(LEVEL, level - 1));
        }
        return ActionResult.SUCCESS;
    }
}
