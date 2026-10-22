package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.entity.combat.WeaponHelper;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Granjera.
 *  - Cosecha cultivos maduros (trigo, zanahoria, papa, betabel, verrugas y los de mods que usen CropBlock)
 *  - Cosecha sandías y calabazas (dejando el tallo) y la caña de azúcar (dejando la base)
 *  - Replanta con las semillas que saca
 *  - Siembra la tierra arada vacía con las semillas que traiga
 *  - Si trae azada, ara la tierra que está pegada a tu granja y tiene agua cerca
 */
public class FarmGoal extends WalkWorkGoal {
    private enum Job { HARVEST, PLANT, TILL }

    private Job job = Job.HARVEST;

    public FarmGoal(KikoEntity kiko) {
        super(kiko, WorkMode.FARM);
    }

    @Override
    protected boolean prepare() {
        // La azada es opcional: si trae, la usa (Fortuna ayuda a sacar más)
        WeaponHelper.equipTool(kiko, s -> s.isIn(ItemTags.HOES) && !KikoWork.isAlmostBroken(s));
        return true;
    }

    @Override
    protected int idleCooldown() {
        return 100;
    }

    @Override
    @Nullable
    protected BlockPos findTarget(World world, BlockPos from, BlockPos center, int radius) {
        int range = radius + 4;
        boolean hasSeeds = findSeed(world, null) >= 0;
        boolean hasHoe = kiko.getMainHandStack().isIn(ItemTags.HOES);
        BlockPos plantSpot = null, tillSpot = null;

        for (BlockPos pos : BlockPos.iterateOutwards(from, range, 3, range)) {
            if (!inArea(pos, center, radius) || !world.isChunkLoaded(pos) || isIgnored(pos)) continue;
            BlockState state = world.getBlockState(pos);
            if (isRipe(world, pos, state)) {
                job = Job.HARVEST;
                return pos.toImmutable();
            }
            if (plantSpot == null && hasSeeds && state.isAir() && world.getBlockState(pos.down()).isOf(Blocks.FARMLAND)) {
                plantSpot = pos.toImmutable();
            }
            if (tillSpot == null && hasSeeds && hasHoe && canTill(world, pos, state)) {
                tillSpot = pos.toImmutable();
            }
        }
        if (plantSpot != null) {
            job = Job.PLANT;
            return plantSpot;
        }
        if (tillSpot != null) {
            job = Job.TILL;
            return tillSpot;
        }
        return null;
    }

    @Override
    protected boolean isValidTarget(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return switch (job) {
            case HARVEST -> isRipe(world, pos, state);
            case PLANT -> state.isAir() && world.getBlockState(pos.down()).isOf(Blocks.FARMLAND);
            case TILL -> canTill(world, pos, state);
        };
    }

    @Override
    protected boolean act(ServerWorld world, BlockPos pos) {
        if (actTicks == 1) kiko.swingHand(Hand.MAIN_HAND);
        if (actTicks < 6) return false;

        switch (job) {
            case HARVEST -> {
                BlockState state = world.getBlockState(pos);
                boolean replant = state.getBlock() instanceof CropBlock || state.isOf(Blocks.NETHER_WART);
                Item seed = state.getBlock().asItem();
                KikoWork.collect(kiko, world, pos);
                if (replant) plant(world, pos, seed);
            }
            case PLANT -> {
                if (!plant(world, pos, null)) ignore(pos, 1200);
            }
            case TILL -> {
                world.setBlockState(pos, Blocks.FARMLAND.getDefaultState());
                world.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1f, 1f);
                KikoWork.damageHand(kiko);
                plant(world, pos.up(), null);
            }
        }
        return true;
    }

    // ------------------------------------------------------------------

    /** true si es un cultivo listo para cosechar. */
    private static boolean isRipe(World world, BlockPos pos, BlockState state) {
        Block block = state.getBlock();
        if (block instanceof CropBlock crop) return crop.isMature(state);
        if (state.isOf(Blocks.NETHER_WART)) return state.get(Properties.AGE_3) >= 3;
        if (state.isOf(Blocks.MELON) || state.isOf(Blocks.PUMPKIN)) {
            // Solo las que salieron de un tallo (no tira las de tus decoraciones)
            for (Direction d : Direction.Type.HORIZONTAL) {
                if (world.getBlockState(pos.offset(d)).getBlock() instanceof AttachedStemBlock) return true;
            }
            return false;
        }
        if (state.isOf(Blocks.SUGAR_CANE)) {
            // El segundo bloque de la caña (la base se queda para que vuelva a crecer)
            return world.getBlockState(pos.down()).isOf(Blocks.SUGAR_CANE)
                    && !world.getBlockState(pos.down(2)).isOf(Blocks.SUGAR_CANE);
        }
        return false;
    }

    /** Tierra junto a la granja, con agua a 4 bloques, que se puede arar. */
    private static boolean canTill(World world, BlockPos pos, BlockState state) {
        if (!(state.isOf(Blocks.GRASS_BLOCK) || state.isOf(Blocks.DIRT))) return false;
        if (!world.getBlockState(pos.up()).isAir()) return false;
        boolean nextToFarm = false;
        for (Direction d : Direction.Type.HORIZONTAL) {
            if (world.getBlockState(pos.offset(d)).isOf(Blocks.FARMLAND)) {
                nextToFarm = true;
                break;
            }
        }
        if (!nextToFarm) return false;
        for (BlockPos p : BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
            if (world.getFluidState(p).isIn(FluidTags.WATER)) return true;
        }
        return false;
    }

    /**
     * Índice en la mochila de algo que se pueda sembrar (semillas, papas, zanahorias...).
     * @param at si no es null, que se pueda poner en ese lugar
     */
    private int findSeed(World world, @Nullable BlockPos at) {
        return kiko.getInventory().findInBag(s -> {
            if (!(s.getItem() instanceof BlockItem bi)) return false;
            Block b = bi.getBlock();
            if (!(b instanceof CropBlock || b instanceof StemBlock || b == Blocks.NETHER_WART)) return false;
            return at == null || b.getDefaultState().canPlaceAt(world, at);
        });
    }

    /** Siembra en pos, prefiriendo la misma semilla que estaba (o la de los vecinos). */
    private boolean plant(ServerWorld world, BlockPos pos, @Nullable Item preferred) {
        if (!world.getBlockState(pos).isAir()) return false;
        if (preferred == null) {
            for (Direction d : Direction.Type.HORIZONTAL) {
                Block n = world.getBlockState(pos.offset(d)).getBlock();
                if (n instanceof CropBlock || n instanceof StemBlock) {
                    preferred = n.asItem();
                    break;
                }
            }
        }
        int slot = -1;
        if (preferred instanceof BlockItem wanted && wanted.getBlock().getDefaultState().canPlaceAt(world, pos)) {
            slot = kiko.getInventory().findInBag(s -> s.isOf(wanted));
        }
        if (slot < 0) slot = findSeed(world, pos);
        if (slot < 0) return false;

        ItemStack seeds = kiko.getInventory().bag().get(slot);
        if (!(seeds.getItem() instanceof BlockItem bi)) return false;
        BlockState plant = bi.getBlock().getDefaultState();
        if (!plant.canPlaceAt(world, pos)) return false;
        world.setBlockState(pos, plant);
        world.playSound(null, pos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 1f, 1f);
        seeds.decrement(1);
        return true;
    }
}
