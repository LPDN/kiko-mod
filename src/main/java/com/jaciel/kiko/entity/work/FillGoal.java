package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * La otra mitad de aplanar: tapa los hoyos del piso (un bloque abajo de sus pies) con
 * tierra, piedra o pizarra que traiga en la mochila. Así queda un piso parejo.
 */
public class FillGoal extends WalkWorkGoal {
    public FillGoal(KikoEntity kiko) {
        super(kiko, WorkMode.FLATTEN);
    }

    private int findFiller() {
        return kiko.getInventory().findInBag(s -> s.isOf(Items.DIRT) || s.isOf(Items.COARSE_DIRT)
                || s.isOf(Items.COBBLESTONE) || s.isOf(Items.COBBLED_DEEPSLATE) || s.isOf(Items.STONE));
    }

    @Override
    protected boolean prepare() {
        if (findFiller() < 0) {
            kiko.notifyOwner("message.kiko.need_filler");
            return false;
        }
        return true;
    }

    @Override
    protected int idleCooldown() {
        return 80;
    }

    @Override
    @Nullable
    protected BlockPos findTarget(World world, BlockPos from, BlockPos center, int radius) {
        int r = Math.min(radius, KikoConfig.get().flattenRadius);
        BlockPos best = null;
        double bestD = Double.MAX_VALUE;
        for (BlockPos p : BlockPos.iterate(center.add(-r, -1, -r), center.add(r, -1, r))) {
            if (!inArea(p, center, r) || isIgnored(p) || !isHole(world, p)) continue;
            double d = p.getSquaredDistance(from);
            if (d < bestD) {
                bestD = d;
                best = p.toImmutable();
            }
        }
        return best;
    }

    @Override
    protected boolean isValidTarget(World world, BlockPos pos) {
        return isHole(world, pos);
    }

    @Override
    protected double reachSq() {
        return 3.6 * 3.6;
    }

    @Override
    protected boolean act(ServerWorld world, BlockPos pos) {
        if (actTicks < 4) return false;
        // No se tapa a sí misma
        if (kiko.getBoundingBox().intersects(new net.minecraft.util.math.Box(pos))) {
            ignore(pos, 100);
            return true;
        }
        int slot = findFiller();
        if (slot < 0) return true;
        ItemStack stack = kiko.getInventory().bag().get(slot);
        Block block = ((BlockItem) stack.getItem()).getBlock();
        BlockState place = block.getDefaultState();
        world.setBlockState(pos, place);
        world.playSound(null, pos, place.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1f, 0.9f);
        kiko.swingHand(Hand.MAIN_HAND);
        stack.decrement(1);
        return true;
    }

    /** Aire, agua o plantitas donde debería haber piso. */
    private static boolean isHole(World world, BlockPos pos) {
        BlockState s = world.getBlockState(pos);
        if (world.getBlockEntity(pos) != null) return false;
        return s.isAir() || s.isReplaceable();
    }
}
