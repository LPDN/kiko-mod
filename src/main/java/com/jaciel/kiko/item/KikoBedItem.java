package com.jaciel.kiko.item;

import com.jaciel.kiko.block.KikoBedBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/**
 * Pone la cama de 2x2. Tú quedas en los pies: la cabecera va hacia donde miras
 * y la otra mitad queda a tu derecha.
 */
public class KikoBedItem extends BlockItem {
    public KikoBedItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult place(ItemPlacementContext ctx) {
        if (!ctx.canPlace()) return ActionResult.FAIL;
        World world = ctx.getWorld();
        Direction facing = ctx.getHorizontalPlayerFacing();
        Direction right = facing.rotateYClockwise();
        BlockPos footL = ctx.getBlockPos();
        BlockPos headL = footL.offset(facing);
        BlockPos footR = footL.offset(right);
        BlockPos headR = headL.offset(right);
        BlockPos[] all = {footL, headL, footR, headR};

        for (BlockPos p : all) {
            if (!world.getWorldBorder().contains(p) || world.isOutOfHeightLimit(p)) return ActionResult.FAIL;
            if (!p.equals(footL) && !world.getBlockState(p).isReplaceable()) return failSpace(ctx);
            if (!world.getBlockState(p.down()).isSideSolidFullSquare(world, p.down(), Direction.UP)) return failSpace(ctx);
        }

        if (!world.isClient) {
            BlockState base = getBlock().getDefaultState().with(BedBlock.FACING, facing);
            world.setBlockState(footL, base.with(BedBlock.PART, BedPart.FOOT).with(KikoBedBlock.SIDE, KikoBedBlock.Side.LEFT), Block.NOTIFY_ALL);
            world.setBlockState(headL, base.with(BedBlock.PART, BedPart.HEAD).with(KikoBedBlock.SIDE, KikoBedBlock.Side.LEFT), Block.NOTIFY_ALL);
            world.setBlockState(footR, base.with(BedBlock.PART, BedPart.FOOT).with(KikoBedBlock.SIDE, KikoBedBlock.Side.RIGHT), Block.NOTIFY_ALL);
            world.setBlockState(headR, base.with(BedBlock.PART, BedPart.HEAD).with(KikoBedBlock.SIDE, KikoBedBlock.Side.RIGHT), Block.NOTIFY_ALL);
            world.playSound(null, footL, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1f, 0.9f);
        }
        PlayerEntity player = ctx.getPlayer();
        if (player == null || !player.getAbilities().creativeMode) ctx.getStack().decrement(1);
        return ActionResult.success(world.isClient);
    }

    private static ActionResult failSpace(ItemPlacementContext ctx) {
        if (ctx.getPlayer() != null && !ctx.getWorld().isClient) {
            ctx.getPlayer().sendMessage(net.minecraft.text.Text.translatable("message.kiko.bed_no_space"), true);
        }
        return ActionResult.FAIL;
    }
}
