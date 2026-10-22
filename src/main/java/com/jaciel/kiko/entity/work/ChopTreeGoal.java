package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.entity.combat.WeaponHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Talar. Con un hacha en la mano o en la mochila busca árboles, corta el tronco desde abajo
 * y tumba el árbol completo. Si trae un brote en la mochila lo replanta.
 *
 * Para no destruir casas de madera, solo tala troncos que tengan hojas naturales pegadas
 * (las hojas que pones tú a mano no cuentan).
 */
public class ChopTreeGoal extends BlockWorkGoal {
    private static final int MAX_LOGS = 160;

    public ChopTreeGoal(KikoEntity kiko) {
        super(kiko, java.util.EnumSet.of(WorkMode.CHOP, WorkMode.FOREST), WeaponHelper::isAxe, "message.kiko.need_axe");
    }

    @Override
    @Nullable
    protected BlockPos findTarget(World world, BlockPos from, BlockPos center, int radius) {
        int range = radius + 6;
        for (BlockPos pos : BlockPos.iterateOutwards(from, range, 8, range)) {
            if (!inArea(pos, center, radius) || !world.isChunkLoaded(pos)) continue;
            BlockState state = world.getBlockState(pos);
            if (!state.isIn(BlockTags.LOGS)) continue;
            if (world.getBlockState(pos.down()).isIn(BlockTags.LOGS)) continue; // solo la base
            BlockPos base = pos.toImmutable();
            if (isIgnored(base)) continue;
            if (collectTree(world, base) == null) {
                ignore(base, 6000);
                continue;
            }
            return base;
        }
        return null;
    }

    @Override
    protected String nothingLeftKey() {
        // En el bosque infinito no hay "ya no queda": espera a que crezcan los brotes
        return kiko.getWorkMode() == WorkMode.FOREST ? "message.kiko.forest_waiting" : super.nothingLeftKey();
    }

    @Override
    protected boolean isValidTarget(World world, BlockPos pos, BlockState state) {
        return state.isIn(BlockTags.LOGS);
    }

    @Override
    protected void onBroken(ServerWorld world, BlockPos base, BlockState state) {
        List<BlockPos> logs = collectTree(world, base);
        if (logs == null) logs = List.of(base);
        logs.sort(Comparator.comparingInt(BlockPos::getY));
        for (BlockPos log : logs) {
            if (kiko.getInventory().isBagFull()) break;
            if (!harvest(world, log)) break; // se le rompió el hacha
        }
        replant(world, base);
    }

    /**
     * Junta todos los troncos conectados al de la base. Regresa null si no es un árbol natural.
     */
    @Nullable
    private List<BlockPos> collectTree(World world, BlockPos base) {
        List<BlockPos> logs = new ArrayList<>();
        Set<Long> seen = new HashSet<>();
        Deque<BlockPos> queue = new ArrayDeque<>();
        queue.add(base);
        seen.add(base.asLong());
        boolean naturalLeaves = false;

        while (!queue.isEmpty() && logs.size() < MAX_LOGS) {
            BlockPos p = queue.poll();
            logs.add(p);
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = 0; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) continue;
                        BlockPos n = p.add(dx, dy, dz);
                        BlockState ns = world.getBlockState(n);
                        if (!naturalLeaves && ns.isIn(BlockTags.LEAVES)
                                && ns.contains(LeavesBlock.PERSISTENT) && !ns.get(LeavesBlock.PERSISTENT)) {
                            naturalLeaves = true;
                        }
                        if (ns.isIn(BlockTags.LOGS) && seen.add(n.asLong())) queue.add(n);
                    }
                }
            }
        }
        return naturalLeaves ? logs : null;
    }

    private void replant(ServerWorld world, BlockPos base) {
        if (!world.getBlockState(base).isAir()) return;
        if (!world.getBlockState(base.down()).isIn(BlockTags.DIRT)) return;
        int i = kiko.getInventory().findInBag(s -> s.isIn(ItemTags.SAPLINGS) && s.getItem() instanceof BlockItem);
        if (i < 0) return;
        ItemStack sapling = kiko.getInventory().bag().get(i);
        Block block = ((BlockItem) sapling.getItem()).getBlock();
        BlockState plant = block.getDefaultState();
        if (!plant.canPlaceAt(world, base)) return;
        world.setBlockState(base, plant);
        world.playSound(null, base, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1f, 1f);
        sapling.decrement(1);
        // Si trae polvo de hueso, lo hace crecer de una vez (así el bosque nunca se acaba)
        KikoWork.boneMeal(kiko, world, base, 8);
    }
}
