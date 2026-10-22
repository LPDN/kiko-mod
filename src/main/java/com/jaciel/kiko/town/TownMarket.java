package com.jaciel.kiko.town;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BellBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * El mercadito de los domingos (v3.5).
 * La plaza se reconoce por su campana. Los puestos son los barriles con toldo de lana 2 bloques arriba;
 * el vendedor se para detrás del barril de en medio.
 * El domingo de 7 a 3 (más o menos) los vecinos con tiendita se van a un puesto libre, y el primero que llega toca la campana.
 */
public final class TownMarket {
    private TownMarket() {}

    /** Puestos de cada plaza (por posición de su campana). */
    private static final Map<BlockPos, List<BlockPos>> STALLS = new HashMap<>();
    /** Quién está en cada puesto y de qué día. */
    private static final Map<BlockPos, Object[]> TAKEN = new HashMap<>();
    private static final Map<BlockPos, Long> RANG = new HashMap<>();

    /** v3.5.1 todo el día (de 6 de la mañana a 5 de la tarde). */
    public static final long OPEN = 0, CLOSE = 11000;

    public static boolean isOpen(World world) {
        long tod = world.getTimeOfDay() % 24000L;
        return TownCalendar.isSunday(world) && tod >= OPEN && tod < CLOSE;
    }

    /** La campana más cercana a `from` (hasta 4 chunks), revisando solo los bloques con entidad de cada chunk. */
    @Nullable
    public static BlockPos findBell(ServerWorld world, BlockPos from) {
        ChunkPos c = new ChunkPos(from);
        BlockPos best = null;
        double bestD = Double.MAX_VALUE;
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                if (!world.isChunkLoaded(c.x + dx, c.z + dz)) continue;
                WorldChunk chunk = world.getChunk(c.x + dx, c.z + dz);
                for (BlockEntity be : chunk.getBlockEntities().values()) {
                    if (!(be instanceof BellBlockEntity)) continue;
                    double d = be.getPos().getSquaredDistance(from);
                    if (d < bestD) {
                        bestD = d;
                        best = be.getPos().toImmutable();
                    }
                }
            }
        }
        return best;
    }

    /** Lugares de los vendedores alrededor de la campana. */
    public static List<BlockPos> stalls(ServerWorld world, BlockPos bell) {
        List<BlockPos> cached = STALLS.get(bell);
        if (cached != null) return cached;
        List<BlockPos> out = new ArrayList<>();
        for (BlockPos p : BlockPos.iterate(bell.add(-11, -3, -11), bell.add(11, 2, 11))) {
            if (!world.getBlockState(p).isOf(Blocks.BARREL) || !world.getBlockState(p.up(2)).isIn(BlockTags.WOOL)) continue;
            // El de en medio tiene barriles a los dos lados
            for (Direction d : new Direction[]{Direction.EAST, Direction.SOUTH}) {
                if (world.getBlockState(p.offset(d)).isOf(Blocks.BARREL) && world.getBlockState(p.offset(d.getOpposite())).isOf(Blocks.BARREL)) {
                    // El vendedor va del lado contrario a la plaza
                    Direction side = d.rotateYClockwise();
                    BlockPos a = p.offset(side), b = p.offset(side.getOpposite());
                    BlockPos spot = a.getSquaredDistance(bell) > b.getSquaredDistance(bell) ? a : b;
                    BlockState s = world.getBlockState(spot);
                    if (s.getCollisionShape(world, spot).isEmpty()) out.add(spot.toImmutable());
                }
            }
        }
        STALLS.put(bell.toImmutable(), out);
        return out;
    }

    @Nullable
    static BlockPos claim(ServerWorld world, BlockPos bell, UUID who) {
        long day = TownCalendar.day(world);
        for (BlockPos s : stalls(world, bell)) {
            Object[] t = TAKEN.get(s);
            if (t != null && (Long) t[1] == day && !who.equals(t[0])) continue;
            TAKEN.put(s, new Object[]{who, day});
            return s;
        }
        return null;
    }

    static void ringOnce(ServerWorld world, BlockPos bell) {
        long day = TownCalendar.day(world);
        Long last = RANG.get(bell);
        if (last != null && last == day) return;
        RANG.put(bell, day);
        for (int i = 0; i < 3; i++) {
            world.playSound(null, bell, SoundEvents.BLOCK_BELL_USE, SoundCategory.BLOCKS, 2.0f, 1.0f - i * 0.05f);
        }
    }

    /** Se va a su puesto el domingo en la mañana y ahí se queda hasta la tarde. */
    public static class Goal_ extends Goal {
        private final TownsfolkEntity npc;
        @Nullable
        private BlockPos spot, bell;
        private int ticks, cooldown;

        public Goal_(TownsfolkEntity npc) {
            this.npc = npc;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        private boolean ok() {
            return !npc.isTownKid() && TownShop.sells(npc.getJobId()) && isOpen(npc.getWorld()) && !npc.isSleeping()
                    && npc.getHome() != null && npc.getTarget() == null;
        }

        @Override
        public boolean canStart() {
            if (--cooldown > 0 || npc.isBeingAttended()) return false;
            cooldown = 100;
            if (!ok() || !(npc.getWorld() instanceof ServerWorld w)) return false;
            bell = findBell(w, npc.getHome());
            if (bell == null) return false;
            spot = claim(w, bell, npc.getUuid());
            return spot != null;
        }

        @Override
        public boolean shouldContinue() {
            return spot != null && ok();
        }

        @Override
        public void start() {
            ticks = 0;
            npc.getNavigation().startMovingTo(spot.getX() + 0.5, spot.getY(), spot.getZ() + 0.5, 0.8);
        }

        @Override
        public void stop() {
            npc.getNavigation().stop();
            spot = null;
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (spot == null) return;
            ticks++;
            Vec3d c = Vec3d.ofBottomCenter(spot);
            if (npc.squaredDistanceTo(c) < 0.8 * 0.8) {
                npc.getNavigation().stop();
                if (bell != null) {
                    npc.getLookControl().lookAt(bell.getX() + 0.5, npc.getEyeY(), bell.getZ() + 0.5);
                    if (npc.getWorld() instanceof ServerWorld w) ringOnce(w, bell);
                }
                return;
            }
            if (ticks % 40 == 0 && npc.getNavigation().isIdle()) npc.getNavigation().startMovingTo(c.x, c.y, c.z, 0.8);
            // Atorado mucho rato: llega con una nubecita
            if (ticks > 900 && npc.getWorld() instanceof ServerWorld w) {
                w.spawnParticles(net.minecraft.particle.ParticleTypes.CLOUD, npc.getX(), npc.getBodyY(0.5), npc.getZ(), 8, 0.3, 0.4, 0.3, 0.02);
                npc.refreshPositionAndAngles(c.x, c.y, c.z, npc.getYaw(), npc.getPitch());
                ticks = 0;
            }
        }
    }
}
