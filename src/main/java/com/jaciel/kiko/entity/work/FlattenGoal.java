package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.entity.combat.WeaponHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Constructora sencilla: aplanar el terreno.
 * Quita la tierra, piedra, arena, pasto y hojas que estén al nivel de sus pies o más arriba (hasta 4 bloques)
 * en un círculo de flattenRadius alrededor de donde le encargaste el trabajo. FillGoal tapa los hoyos del piso.
 * Solo quita bloques naturales: nada de tablas, ladrillos, cofres, etc.
 * Usa pala para tierra y arena, y pico para la piedra (cambia sola entre las dos).
 */
public class FlattenGoal extends BlockWorkGoal {
    private static final int HEIGHT = 4;

    public FlattenGoal(KikoEntity kiko) {
        super(kiko, WorkMode.FLATTEN, FlattenGoal::isDiggingTool, "message.kiko.need_shovel");
    }

    static boolean isDiggingTool(ItemStack s) {
        return s.isIn(ItemTags.SHOVELS) || WeaponHelper.isPickaxe(s);
    }

    @Override
    protected String nothingLeftKey() {
        return "message.kiko.flatten_done";
    }

    @Override
    @Nullable
    protected BlockPos findTarget(World world, BlockPos from, BlockPos center, int radius) {
        int r = Math.min(radius, KikoConfig.get().flattenRadius);
        // De arriba hacia abajo para no dejar bloques flotando
        for (int dy = HEIGHT - 1; dy >= 0; dy--) {
            BlockPos best = null;
            double bestD = Double.MAX_VALUE;
            for (BlockPos p : BlockPos.iterate(center.add(-r, dy, -r), center.add(r, dy, r))) {
                if (!inArea(p, center, r) || isIgnored(p)) continue;
                if (!isNatural(world, p, world.getBlockState(p))) continue;
                double d = p.getSquaredDistance(from);
                if (d < bestD) {
                    bestD = d;
                    best = p.toImmutable();
                }
            }
            if (best != null) return best;
        }
        return null;
    }

    @Override
    protected boolean isValidTarget(World world, BlockPos pos, BlockState state) {
        return isNatural(world, pos, state);
    }

    @Override
    protected void beforeBreak(BlockState state) {
        // La herramienta correcta para cada bloque
        if (kiko.getMainHandStack().isSuitableFor(state) || kiko.getMainHandStack().getMiningSpeedMultiplier(state) > 1f) return;
        int i = kiko.getInventory().findInBag(s -> isDiggingTool(s) && !KikoWork.isAlmostBroken(s)
                && (s.isSuitableFor(state) || s.getMiningSpeedMultiplier(state) > 1f));
        if (i >= 0) kiko.getInventory().swapToMainHand(i);
    }

    @Override
    protected void onBroken(ServerWorld world, BlockPos pos, BlockState state) {
        harvest(world, pos);
    }

    /** Bloques del terreno natural que sí puede quitar. */
    static boolean isNatural(World world, BlockPos pos, BlockState s) {
        if (s.isAir() || !s.getFluidState().isEmpty() || world.getBlockEntity(pos) != null) return false;
        if (s.getHardness(world, pos) < 0) return false;
        if (s.isIn(BlockTags.LEAVES)) return !s.contains(LeavesBlock.PERSISTENT) || !s.get(LeavesBlock.PERSISTENT);
        return s.isIn(BlockTags.DIRT) || s.isIn(BlockTags.BASE_STONE_OVERWORLD) || s.isIn(BlockTags.SAND)
                || s.isIn(BlockTags.TERRACOTTA) || s.isIn(BlockTags.FLOWERS) || s.isIn(BlockTags.SNOW)
                || s.isOf(Blocks.GRAVEL) || s.isOf(Blocks.CLAY) || s.isOf(Blocks.SHORT_GRASS) || s.isOf(Blocks.TALL_GRASS)
                || s.isOf(Blocks.FERN) || s.isOf(Blocks.LARGE_FERN) || s.isOf(Blocks.DEAD_BUSH) || s.isOf(Blocks.SWEET_BERRY_BUSH)
                || s.isOf(Blocks.SANDSTONE) || s.isOf(Blocks.RED_SANDSTONE)
                || KikoWork.isOre(s);
    }
}
