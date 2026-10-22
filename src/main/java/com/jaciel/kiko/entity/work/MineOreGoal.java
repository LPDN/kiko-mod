package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.entity.combat.WeaponHelper;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * Minar. Con un pico busca minerales que estén a la vista (con al menos una cara al aire),
 * los saca con todo y la veta conectada y se guarda lo que suelten.
 *
 * Usa el tag común c:ores, así que también reconoce minerales de otros mods.
 * Solo mina lo que su pico puede sacar (con pico de piedra no saca diamante).
 * No cava túneles por su cuenta para no destrozarte la base ni perderse.
 */
public class MineOreGoal extends BlockWorkGoal {
    private static final int MAX_VEIN = 16;

    public MineOreGoal(KikoEntity kiko) {
        super(kiko, WorkMode.MINE, WeaponHelper::isPickaxe, "message.kiko.need_pickaxe");
    }

    @Override
    @Nullable
    protected BlockPos findTarget(World world, BlockPos from, BlockPos center, int radius) {
        ItemStack tool = kiko.getMainHandStack();
        int range = radius + 6;
        for (BlockPos pos : BlockPos.iterateOutwards(from, range, 10, range)) {
            if (!inArea(pos, center, radius) || !world.isChunkLoaded(pos)) continue;
            BlockState state = world.getBlockState(pos);
            if (!isOre(state) || !tool.isSuitableFor(state)) continue;
            BlockPos p = pos.toImmutable();
            if (isIgnored(p) || !isExposed(world, p)) continue;
            return p;
        }
        return null;
    }

    @Override
    protected boolean isValidTarget(World world, BlockPos pos, BlockState state) {
        return isOre(state);
    }

    @Override
    protected void onBroken(ServerWorld world, BlockPos pos, BlockState state) {
        KikoWork.mineVein(kiko, world, pos, MAX_VEIN);
    }

    private static boolean isOre(BlockState state) {
        return KikoWork.isOre(state);
    }

    private static boolean isExposed(World world, BlockPos pos) {
        return KikoWork.isExposed(world, pos);
    }
}
