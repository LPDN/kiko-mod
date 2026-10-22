package com.jaciel.kiko.world;

import com.jaciel.kiko.KikoMod;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.registry.ModEntities;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.PersistentState;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestTypes;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Hace que Kiko aparezca de forma natural en las aldeas.
 *
 * Cómo funciona
 *  1. Cuando un aldeano se carga en el mundo lo anotamos (sin tocar el mundo durante la carga del chunk).
 *  2. En el siguiente tick revisamos si está dentro de una aldea que nunca se ha revisado.
 *  3. Si sí, buscamos una cama cerca (casa) y ponemos ahí a Kiko con cierta probabilidad.
 *  4. La aldea queda marcada en el guardado del mundo para no repetir.
 */
public final class VillageSpawner {
    private VillageSpawner() {}

    /** Aldeano visto al cargar. La estructura se revisa después, en el tick, no durante la carga. */
    private record Candidate(ServerWorld world, BlockPos villagerPos) {}

    private static final Deque<Candidate> QUEUE = new ArrayDeque<>();
    /** Chunks de aldeanos ya encolados en esta sesión, para no revisar 20 veces la misma aldea. */
    private static final Set<Long> SEEN_CHUNKS = new HashSet<>();

    public static void register() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            KikoConfig cfg = KikoConfig.get();
            if (!cfg.villageSpawnEnabled || !(entity instanceof VillagerEntity)) return;
            String dim = world.getRegistryKey().getValue().toString();
            if (Arrays.stream(cfg.allowedDimensions).noneMatch(dim::equals)) return;
            if (!SEEN_CHUNKS.add(new ChunkPos(entity.getBlockPos()).toLong())) return;
            QUEUE.add(new Candidate(world, entity.getBlockPos()));
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            QUEUE.clear();
            SEEN_CHUNKS.clear();
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            int budget = 2; // máximo 2 revisiones por tick para no dar lag
            int size = QUEUE.size();
            for (int i = 0; i < size && budget > 0; i++) {
                Candidate cand = QUEUE.poll();
                if (cand == null) break;
                if (process(cand)) {
                    budget--;
                } else {
                    QUEUE.add(cand); // chunk todavía no cargado, reintentar luego
                }
            }
        });
    }

    /** @return true si el candidato quedó resuelto (con o sin Kiko). */
    private static boolean process(Candidate cand) {
        ServerWorld world = cand.world();
        if (!world.isChunkLoaded(cand.villagerPos())) return false;

        StructureStart start = world.getStructureAccessor().getStructureContaining(cand.villagerPos(), StructureTags.VILLAGE);
        if (start == null || !start.hasChildren()) return true;

        VillageState state = VillageState.get(world);
        ChunkPos village = start.getPos();
        if (state.isDone(village)) return true;

        KikoConfig cfg = KikoConfig.get();
        int toSpawn = 0;
        for (int i = 0; i < Math.max(1, cfg.maxPerVillage); i++) {
            if (world.getRandom().nextFloat() < cfg.villageSpawnChance) toSpawn++;
        }

        for (int i = 0; i < toSpawn; i++) {
            BlockPos spot = findHomeSpot(world, cand.villagerPos(), start.getBoundingBox().getCenter());
            if (spot == null || !world.isChunkLoaded(spot)) continue;
            // Kiko es la más común; Maru, Lumi y Nadi también viven en las aldeas
            // v3.3 Kiko 20 %, las otras 5 chicas 40 % y los 6 chicos 40 %
            var others = java.util.List.of(ModEntities.MARU, ModEntities.LUMI, ModEntities.NADI, ModEntities.RINA, ModEntities.TAMI);
            var guys = java.util.List.of(ModEntities.TONO, ModEntities.LEO, ModEntities.DANTE, ModEntities.IVAN, ModEntities.EMI, ModEntities.BETO);
            float r = world.getRandom().nextFloat();
            var type = r < 0.20f ? ModEntities.KIKO : r < 0.60f ? others.get(world.getRandom().nextInt(others.size()))
                    : guys.get(world.getRandom().nextInt(guys.size()));
            KikoEntity kiko = type.create(world);
            if (kiko == null) continue;
            kiko.refreshPositionAndAngles(spot.getX() + 0.5, spot.getY(), spot.getZ() + 0.5, world.getRandom().nextFloat() * 360f, 0f);
            kiko.initialize(world, world.getLocalDifficulty(spot), SpawnReason.STRUCTURE, null);
            world.spawnEntity(kiko);
            KikoMod.LOGGER.debug("Kiko apareció en la aldea {}", village);
        }
        state.markDone(village);
        return true;
    }

    /** Busca una cama (POI "home") y un hueco libre junto a ella. */
    private static BlockPos findHomeSpot(ServerWorld world, BlockPos near, BlockPos center) {
        Optional<BlockPos> bed = world.getPointOfInterestStorage().getNearestPosition(
                entry -> entry.matchesKey(PointOfInterestTypes.HOME), near, 48, PointOfInterestStorage.OccupationStatus.ANY);
        BlockPos base = bed.orElseGet(() -> world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, center));

        for (int dy = 0; dy <= 1; dy++) {
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    BlockPos pos = base.add(dx, dy, dz);
                    if (world.isAir(pos) && world.isAir(pos.up())
                            && world.getBlockState(pos.down()).isSolidBlock(world, pos.down())) {
                        return pos;
                    }
                }
            }
        }
        return world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, base);
    }

    /** Guarda en el mundo qué aldeas ya se revisaron. */
    public static class VillageState extends PersistentState {
        private static final String NAME = "kiko_villages";
        private final Set<Long> done = new HashSet<>();

        public static final Type<VillageState> TYPE = new Type<>(VillageState::new, VillageState::fromNbt, null);

        public static VillageState get(ServerWorld world) {
            return world.getPersistentStateManager().getOrCreate(TYPE, NAME);
        }

        public boolean isDone(ChunkPos pos) {
            return done.contains(pos.toLong());
        }

        public void markDone(ChunkPos pos) {
            done.add(pos.toLong());
            markDirty();
        }

        private static VillageState fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
            VillageState s = new VillageState();
            for (long l : nbt.getLongArray("Done")) s.done.add(l);
            return s;
        }

        @Override
        public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
            nbt.putLongArray("Done", done.stream().mapToLong(Long::longValue).toArray());
            return nbt;
        }
    }
}
