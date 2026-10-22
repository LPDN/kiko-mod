package com.jaciel.kiko.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

/**
 * Cuna (v3.1). Cuando tu esposa y tú deciden formar una familia, a la mañana siguiente
 * aparece un bebé acostadito aquí. Tiene que estar a 16 bloques o menos de ella o de su casa.
 */
public class CribBlock extends HorizontalFacingBlock {
    public static final MapCodec<CribBlock> CODEC = createCodec(CribBlock::new);
    private static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0, 0, 0, 16, 7, 16),
            Block.createCuboidShape(0, 7, 0, 16, 14, 1),
            Block.createCuboidShape(0, 7, 15, 16, 14, 16),
            Block.createCuboidShape(0, 7, 0, 1, 14, 16),
            Block.createCuboidShape(15, 7, 0, 16, 14, 16));

    public CribBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, net.minecraft.util.math.Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
