package com.jaciel.kiko.block;

import com.jaciel.kiko.town.TownCalendar;
import com.jaciel.kiko.town.TownRequests;
import com.jaciel.kiko.town.TownsfolkEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

/**
 * Tablero de encargos del pueblo (v3.5). Con clic derecho te dice qué día es, cuánto falta para el mercadito
 * y todos los encargos pendientes de los vecinos (hasta 96 bloques).
 */
public class NoticeBoardBlock extends HorizontalFacingBlock {
    public static final MapCodec<NoticeBoardBlock> CODEC = createCodec(NoticeBoardBlock::new);
    private static final VoxelShape NS = VoxelShapes.union(Block.createCuboidShape(1, 4, 7, 15, 15, 9),
            Block.createCuboidShape(2, 0, 7.5, 3.5, 4, 8.5), Block.createCuboidShape(12.5, 0, 7.5, 14, 4, 8.5));
    private static final VoxelShape EW = VoxelShapes.union(Block.createCuboidShape(7, 4, 1, 9, 15, 15),
            Block.createCuboidShape(7.5, 0, 2, 8.5, 4, 3.5), Block.createCuboidShape(7.5, 0, 12.5, 8.5, 4, 14));

    public NoticeBoardBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
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
        return state.get(FACING).getAxis() == Direction.Axis.Z ? NS : EW;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;
        player.sendMessage(Text.translatable("message.kiko.board_title").formatted(Formatting.GOLD, Formatting.BOLD), false);
        player.sendMessage(TownCalendar.dayLine(world).formatted(Formatting.YELLOW), false);
        int left = TownCalendar.daysToSunday(world);
        if (left > 0) player.sendMessage(Text.translatable("message.kiko.board_market", left).formatted(Formatting.GRAY), false);
        List<TownsfolkEntity> list = world.getEntitiesByClass(TownsfolkEntity.class, new Box(pos).expand(96), TownsfolkEntity::hasRequest);
        if (list.isEmpty()) {
            player.sendMessage(Text.translatable("message.kiko.board_empty").formatted(Formatting.GRAY), false);
        } else {
            for (TownsfolkEntity t : list) player.sendMessage(TownRequests.line(t), false);
        }
        return ActionResult.SUCCESS;
    }
}
