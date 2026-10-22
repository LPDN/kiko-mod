package com.jaciel.kiko.block;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.DyeColor;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

/**
 * Cama matrimonial de 2x2. Son dos camas normales de Minecraft pegadas (izquierda y derecha),
 * así que dormir, saltarse la noche y el punto de reaparición funcionan igual que con las de vanilla.
 * Tú te acuestas en una mitad y Kiko en la otra.
 *
 * Mirando desde los pies hacia la cabecera: LEFT es la mitad izquierda, RIGHT la derecha.
 * Si se rompe cualquier bloque se rompe toda la cama y suelta un solo ítem.
 */
public class KikoBedBlock extends BedBlock {
    public enum Side implements StringIdentifiable {
        LEFT("left"), RIGHT("right");

        private final String name;

        Side(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return name;
        }
    }

    public static final EnumProperty<Side> SIDE = EnumProperty.of("side", Side.class);

    public KikoBedBlock(Settings settings) {
        super(DyeColor.PINK, settings);
        this.setDefaultState(this.getDefaultState().with(SIDE, Side.LEFT));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SIDE);
    }

    /** Hacia dónde está la otra mitad (izquierda/derecha). */
    public static Direction partnerDirection(BlockState state) {
        Direction facing = state.get(FACING);
        return state.get(SIDE) == Side.LEFT ? facing.rotateYClockwise() : facing.rotateYCounterclockwise();
    }

    /** Cabecera de la otra mitad, a partir de cualquier bloque de esta cama. */
    public static BlockPos partnerHead(BlockState state, BlockPos pos) {
        BlockPos head = state.get(PART) == BedPart.HEAD ? pos : pos.offset(state.get(FACING));
        return head.offset(partnerDirection(state));
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL; // modelo JSON, no el render animado de las camas de vanilla
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                   WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == partnerDirection(state)) {
            boolean ok = neighborState.isOf(this) && neighborState.get(SIDE) != state.get(SIDE)
                    && neighborState.get(PART) == state.get(PART);
            if (!ok) return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        // En creativo quita las otras 3 partes sin soltar nada
        if (!world.isClient && player.isCreative()) {
            BlockPos head = state.get(PART) == BedPart.HEAD ? pos : pos.offset(state.get(FACING));
            BlockPos foot = head.offset(state.get(FACING).getOpposite());
            Direction side = partnerDirection(state);
            for (BlockPos p : new BlockPos[]{head, foot, head.offset(side), foot.offset(side)}) {
                if (!p.equals(pos) && world.getBlockState(p).isOf(this)) {
                    world.setBlockState(p, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
                }
            }
        }
        return super.onBreak(world, pos, state, player);
    }
}
