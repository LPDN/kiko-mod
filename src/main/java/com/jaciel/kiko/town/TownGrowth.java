package com.jaciel.kiko.town;

import com.jaciel.kiko.config.KikoConfig;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.JigsawReplacementStructureProcessor;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.PersistentState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * El pueblo crece (v3.7).
 * Cada tantos días, si hay jugador cerca, el pueblo escoge un terreno parejo alrededor de la campana
 * y unos vecinos se ponen a construir una casa nueva, una capa a la vez, hasta que queda terminada
 * y se muda una familia. Si nadie está trabajando (es de noche, los vecinos duermen) la obra se pausa.
 */
public final class TownGrowth {
    private TownGrowth() {}

    /** Cada cuántos ticks sube una capa cuando hay constructores trabajando. */
    private static final int LAYER_TICKS = 160;
    /** Si la obra lleva dos días de Minecraft sin terminar, se termina sola para no dejar ruinas. */
    private static final int STALL_LIMIT = 48000;
    private static final int MAX_BUILDERS = 3;

    /** Quién está construyendo qué (no se guarda, se rearma cada vez). */
    private static final Map<UUID, Job> BUILDERS = new HashMap<>();

    // ------------------------------------------------------------------
    // Datos
    // ------------------------------------------------------------------
    public static final class Job {
        BlockPos pos = BlockPos.ORIGIN;
        BlockPos bell = BlockPos.ORIGIN;
        Identifier template = Identifier.of("kiko", "town/oak/house_small");
        BlockRotation rot = BlockRotation.NONE;
        int layer = -1;
        int sizeY = 10;
        int timer, stall;
        @Nullable
        BlockBox box;
        final List<UUID> builders = new ArrayList<>();
    }

    public static final class State extends PersistentState {
        /** campana → { casas construidas, día del último intento } */
        final Map<Long, long[]> towns = new HashMap<>();
        /** v3.9 campana → { origen de la escuela, giro } */
        final Map<Long, long[]> schools = new HashMap<>();
        final List<Job> jobs = new ArrayList<>();
        long lastScan = -1;

        @Override
        public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
            NbtList ts = new NbtList();
            for (Map.Entry<Long, long[]> e : towns.entrySet()) {
                NbtCompound c = new NbtCompound();
                c.putLong("bell", e.getKey());
                c.putLong("built", e.getValue()[0]);
                c.putLong("last", e.getValue()[1]);
                ts.add(c);
            }
            nbt.put("towns", ts);
            NbtList ss = new NbtList();
            for (Map.Entry<Long, long[]> e : schools.entrySet()) {
                NbtCompound c = new NbtCompound();
                c.putLong("bell", e.getKey());
                c.putLong("pos", e.getValue()[0]);
                c.putLong("rot", e.getValue()[1]);
                ss.add(c);
            }
            nbt.put("schools", ss);
            NbtList js = new NbtList();
            for (Job j : jobs) {
                NbtCompound c = new NbtCompound();
                c.putLong("pos", j.pos.asLong());
                c.putLong("bell", j.bell.asLong());
                c.putString("template", j.template.toString());
                c.putInt("rot", j.rot.ordinal());
                c.putInt("layer", j.layer);
                c.putInt("sizeY", j.sizeY);
                c.putInt("stall", j.stall);
                js.add(c);
            }
            nbt.put("jobs", js);
            return nbt;
        }

        static State load(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
            State s = new State();
            NbtList ts = nbt.getList("towns", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < ts.size(); i++) {
                NbtCompound c = ts.getCompound(i);
                s.towns.put(c.getLong("bell"), new long[]{c.getLong("built"), c.getLong("last")});
            }
            NbtList ss = nbt.getList("schools", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < ss.size(); i++) {
                NbtCompound c = ss.getCompound(i);
                s.schools.put(c.getLong("bell"), new long[]{c.getLong("pos"), c.getLong("rot")});
            }
            NbtList js = nbt.getList("jobs", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < js.size(); i++) {
                NbtCompound c = js.getCompound(i);
                Job j = new Job();
                j.pos = BlockPos.fromLong(c.getLong("pos"));
                j.bell = BlockPos.fromLong(c.getLong("bell"));
                Identifier id = Identifier.tryParse(c.getString("template"));
                if (id == null) continue;
                j.template = id;
                BlockRotation[] rots = BlockRotation.values();
                j.rot = rots[Math.floorMod(c.getInt("rot"), rots.length)];
                j.layer = c.getInt("layer");
                j.sizeY = c.getInt("sizeY");
                j.stall = c.getInt("stall");
                s.jobs.add(j);
            }
            return s;
        }
    }

    private static final PersistentState.Type<State> TYPE = new PersistentState.Type<>(State::new, State::load, null);

    private static State state(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(TYPE, "kiko_town_growth");
    }

    // ------------------------------------------------------------------
    // Registro y tick
    // ------------------------------------------------------------------
    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(TownGrowth::tick);
    }

    private static void tick(ServerWorld world) {
        if (world.getTime() % 10L != 0L) return;
        State st = state(world);
        if (!st.jobs.isEmpty()) {
            runJob(world, st, st.jobs.get(0));
            return;
        }
        KikoConfig cfg = KikoConfig.get();
        if (!cfg.townGrowth) return;
        long tod = world.getTimeOfDay() % 24000L;
        if (tod < 1000 || tod > 9000) return;
        long day = TownCalendar.day(world);
        if (day == st.lastScan) return;
        st.lastScan = day;
        for (ServerPlayerEntity p : world.getPlayers()) {
            BlockPos bell = TownMarket.findBell(world, p.getBlockPos());
            if (bell == null || bell.getSquaredDistance(p.getPos()) > 90 * 90) continue;
            long[] info = st.towns.computeIfAbsent(bell.asLong(), k -> new long[]{0, day});
            st.markDirty();
            if (info[0] >= cfg.townGrowthMax || day - info[1] < cfg.townGrowthDays) continue;
            if (start(world, st, bell, false, null) == null) {
                info[1] = day;
                continue;
            }
            info[1] = day;
            break;
        }
    }

    // ------------------------------------------------------------------
    // Empezar una obra
    // ------------------------------------------------------------------
    /** Devuelve null si se pudo empezar, o la llave del mensaje de por qué no. */
    @Nullable
    private static String start(ServerWorld world, State st, BlockPos bell, boolean force, @Nullable String forceKind) {
        if (!st.jobs.isEmpty()) return "command.kiko.town_busy";
        int folks = world.getEntitiesByClass(TownsfolkEntity.class, new net.minecraft.util.math.Box(bell).expand(96),
                t -> t.isAlive() && !t.isTownKid()).size();
        if (!force && folks < 3) return "command.kiko.town_few";
        String wood = woodAround(world, bell);
        Random r = world.getRandom();
        int roll = r.nextInt(100);
        String kind = roll < 40 ? "house_small" : roll < 75 ? "house_medium" : roll < 90 ? "house_large" : "house_elders";
        // v3.9 si hay 3 niños o más y no hay escuela, va primero la escuela
        if (KikoConfig.get().townSchool && !st.schools.containsKey(bell.asLong())) {
            int kids = world.getEntitiesByClass(TownsfolkEntity.class, new net.minecraft.util.math.Box(bell).expand(96),
                    t -> t.isAlive() && t.isTownKid()).size();
            if (kids >= 3) kind = "school";
        }
        if (forceKind != null) kind = forceKind;
        Identifier id = Identifier.of("kiko", "town/" + wood + "/" + kind);
        Optional<StructureTemplate> opt = world.getStructureTemplateManager().getTemplate(id);
        if (opt.isEmpty()) return "command.kiko.town_nosite";
        StructureTemplate tpl = opt.get();

        double a0 = r.nextDouble() * Math.PI * 2;
        for (int ring = 0; ring < 9; ring++) {
            int rad = 18 + ring * 5;
            for (int k = 0; k < 12; k++) {
                double a = a0 + k * Math.PI / 6.0;
                int cx = bell.getX() + (int) Math.round(Math.cos(a) * rad);
                int cz = bell.getZ() + (int) Math.round(Math.sin(a) * rad);
                BlockRotation rot = facing(bell, cx, cz);
                StructurePlacementData data = place(rot, false);
                Vec3i sz = tpl.getRotatedSize(rot);
                int x0 = cx - sz.getX() / 2, z0 = cz - sz.getZ() / 2;
                int y = flatY(world, x0, z0, sz.getX(), sz.getZ());
                if (y == Integer.MIN_VALUE) continue;
                BlockBox tmp = tpl.calculateBoundingBox(data, BlockPos.ORIGIN);
                BlockPos at = new BlockPos(x0 - tmp.getMinX(), y, z0 - tmp.getMinZ());
                Job j = new Job();
                j.pos = at;
                j.bell = bell.toImmutable();
                j.template = id;
                j.rot = rot;
                j.sizeY = sz.getY();
                j.box = tpl.calculateBoundingBox(data, at);
                st.jobs.add(j);
                st.markDirty();
                tell(world, at, "message.kiko.town_grow_start");
                return null;
            }
        }
        return "command.kiko.town_nosite";
    }

    private static StructurePlacementData place(BlockRotation rot, boolean entities) {
        return new StructurePlacementData()
                .setRotation(rot)
                .setMirror(BlockMirror.NONE)
                .setIgnoreEntities(!entities)
                .addProcessor(JigsawReplacementStructureProcessor.INSTANCE);
    }

    /** El frente de las casas está hacia el norte, así que se gira para que mire a la campana. */
    private static BlockRotation facing(BlockPos bell, int x, int z) {
        int dx = bell.getX() - x, dz = bell.getZ() - z;
        if (Math.abs(dz) >= Math.abs(dx)) return dz < 0 ? BlockRotation.NONE : BlockRotation.CLOCKWISE_180;
        return dx > 0 ? BlockRotation.CLOCKWISE_90 : BlockRotation.COUNTERCLOCKWISE_90;
    }

    /** Se fija en lo que distingue a cada estilo (techo y troncos de las esquinas), porque los pisos y puestos mezclan maderas. */
    private static String woodAround(ServerWorld world, BlockPos bell) {
        int oak = 0, spruce = 0, acacia = 0;
        for (BlockPos p : BlockPos.iterate(bell.add(-30, -2, -30), bell.add(30, 8, 30))) {
            BlockState s = world.getBlockState(p);
            if (s.isOf(Blocks.BRICK_STAIRS) || s.isOf(Blocks.OAK_LOG)) oak++;
            else if (s.isOf(Blocks.DARK_OAK_STAIRS) || s.isOf(Blocks.SPRUCE_LOG)) spruce++;
            else if (s.isOf(Blocks.WHITE_TERRACOTTA) || s.isOf(Blocks.ACACIA_LOG)) acacia++;
        }
        if (spruce > oak && spruce >= acacia) return "spruce";
        if (acacia > oak && acacia > spruce) return "acacia";
        return "oak";
    }

    /** Altura del piso si el terreno es parejo y está libre (margen de 2 bloques), o MIN_VALUE si no sirve. */
    private static int flatY(ServerWorld world, int x0, int z0, int w, int d) {
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int x = x0 - 2; x <= x0 + w + 1; x += 2) {
            for (int z = z0 - 2; z <= z0 + d + 1; z += 2) {
                if (!world.isChunkLoaded(x >> 4, z >> 4)) return Integer.MIN_VALUE;
                int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z) - 1;
                BlockPos g = new BlockPos(x, y, z);
                BlockState gs = world.getBlockState(g);
                if (!(gs.isIn(BlockTags.DIRT) || gs.isIn(BlockTags.SAND) || gs.isOf(Blocks.SNOW_BLOCK))) return Integer.MIN_VALUE;
                BlockState up = world.getBlockState(g.up());
                if (!(up.isAir() || up.isReplaceable()) || !up.getFluidState().isEmpty()) return Integer.MIN_VALUE;
                min = Math.min(min, y);
                max = Math.max(max, y);
            }
        }
        if (max - min > 3) return Integer.MIN_VALUE;
        return (min + max + 1) / 2;
    }

    // ------------------------------------------------------------------
    // La obra
    // ------------------------------------------------------------------
    private static void runJob(ServerWorld world, State st, Job j) {
        if (!world.isChunkLoaded(j.pos.getX() >> 4, j.pos.getZ() >> 4)) return;
        Optional<StructureTemplate> opt = world.getStructureTemplateManager().getTemplate(j.template);
        if (opt.isEmpty()) {
            dropJob(st, j);
            return;
        }
        StructureTemplate tpl = opt.get();
        BlockBox full = tpl.calculateBoundingBox(place(j.rot, false), j.pos);
        j.box = full;

        refreshBuilders(world, j);
        int near = 0;
        double cx = (full.getMinX() + full.getMaxX()) / 2.0, cz = (full.getMinZ() + full.getMaxZ()) / 2.0;
        double reach = Math.max(full.getMaxX() - full.getMinX(), full.getMaxZ() - full.getMinZ()) / 2.0 + 9;
        for (UUID id : j.builders) {
            if (world.getEntity(id) instanceof TownsfolkEntity t) {
                double dx = t.getX() - cx, dz = t.getZ() - cz;
                if (dx * dx + dz * dz <= reach * reach && Math.abs(t.getY() - j.pos.getY()) < 8) near++;
            }
        }

        j.stall += 10;
        if (j.stall > STALL_LIMIT) {
            finish(world, st, j, tpl);
            return;
        }
        if (near <= 0) return;
        j.timer += 10;
        if (j.timer % 40 == 0 && j.layer >= 0) {
            world.playSound(null, new BlockPos((int) cx, j.pos.getY() + Math.max(0, j.layer), (int) cz),
                    SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0f, 0.8f + world.random.nextFloat() * 0.4f);
        }
        if (j.timer < LAYER_TICKS) return;
        j.timer = 0;
        step(world, st, j, tpl, full);
        st.markDirty();
    }

    private static void step(ServerWorld world, State st, Job j, StructureTemplate tpl, BlockBox full) {
        if (j.layer < 0) {
            foundation(world, full, j.pos.getY());
        } else if (j.layer < j.sizeY) {
            int y = j.pos.getY() + j.layer;
            StructurePlacementData data = place(j.rot, false);
            data.setBoundingBox(new BlockBox(full.getMinX(), y, full.getMinZ(), full.getMaxX(), y, full.getMaxZ()));
            tpl.place(world, j.pos, j.pos, data, world.getRandom(), Block.NOTIFY_LISTENERS);
            for (int i = 0; i < 6; i++) {
                world.spawnParticles(ParticleTypes.POOF,
                        full.getMinX() + world.random.nextInt(full.getMaxX() - full.getMinX() + 1) + 0.5, y + 1.0,
                        full.getMinZ() + world.random.nextInt(full.getMaxZ() - full.getMinZ() + 1) + 0.5, 1, 0.1, 0.1, 0.1, 0.0);
            }
        } else {
            finish(world, st, j, tpl);
            return;
        }
        j.layer++;
    }

    /** Rellena de tierra lo que quedó hueco abajo para que la casa no flote. */
    private static void foundation(ServerWorld world, BlockBox box, int y0) {
        for (int x = box.getMinX(); x <= box.getMaxX(); x++) {
            for (int z = box.getMinZ(); z <= box.getMaxZ(); z++) {
                for (int y = y0 - 1; y > world.getBottomY() && y > y0 - 8; y--) {
                    BlockPos p = new BlockPos(x, y, z);
                    BlockState s = world.getBlockState(p);
                    if (s.isAir() || s.isReplaceable() || !s.getFluidState().isEmpty() || s.isIn(BlockTags.LEAVES)) {
                        world.setBlockState(p, y == y0 - 1 ? Blocks.GRASS_BLOCK.getDefaultState() : Blocks.DIRT.getDefaultState(),
                                Block.NOTIFY_LISTENERS);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    /** Última capa. Entra la familia, se hace el caminito a la plaza y se avisa. */
    private static void finish(ServerWorld world, State st, Job j, StructureTemplate tpl) {
        BlockBox full = tpl.calculateBoundingBox(place(j.rot, false), j.pos);
        // Si se saltó la obra (se atoró), se pone todo de golpe
        if (j.layer < j.sizeY) {
            if (j.layer < 0) foundation(world, full, j.pos.getY());
            StructurePlacementData all = place(j.rot, false);
            all.setBoundingBox(new BlockBox(full.getMinX(), j.pos.getY(), full.getMinZ(), full.getMaxX(), j.pos.getY() + j.sizeY, full.getMaxZ()));
            tpl.place(world, j.pos, j.pos, all, world.getRandom(), Block.NOTIFY_LISTENERS);
        }
        int folks = world.getEntitiesByClass(TownsfolkEntity.class, new net.minecraft.util.math.Box(j.bell).expand(96), t -> t.isAlive()).size();
        if (folks < KikoConfig.get().townPopulationCap) {
            // Los vecinos de la plantilla salen en la planta baja
            StructurePlacementData ents = place(j.rot, true);
            ents.setBoundingBox(new BlockBox(full.getMinX(), j.pos.getY() + 1, full.getMinZ(), full.getMaxX(), j.pos.getY() + 3, full.getMaxZ()));
            tpl.place(world, j.pos, j.pos, ents, world.getRandom(), Block.NOTIFY_LISTENERS);
        }
        path(world, j, full);
        lamps(world, j, full);
        // Se recalcula la luz de toda la casa para que no se vea oscura
        var light = world.getChunkManager().getLightingProvider();
        for (BlockPos p : BlockPos.iterate(full.getMinX() - 1, full.getMinY() - 1, full.getMinZ() - 1,
                full.getMaxX() + 1, full.getMaxY() + 1, full.getMaxZ() + 1)) {
            light.checkBlock(p);
        }
        world.playSound(null, j.pos, SoundEvents.BLOCK_BELL_USE, SoundCategory.BLOCKS, 2.0f, 1.2f);
        world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, (full.getMinX() + full.getMaxX()) / 2.0, j.pos.getY() + 3.0,
                (full.getMinZ() + full.getMaxZ()) / 2.0, 30, 3, 1.5, 3, 0.0);
        long[] info = st.towns.computeIfAbsent(j.bell.asLong(), k -> new long[]{0, TownCalendar.day(world)});
        info[0]++;
        if (j.template.getPath().endsWith("/school")) {
            st.schools.put(j.bell.asLong(), new long[]{j.pos.asLong(), j.rot.ordinal()});
            TownSchool.forget(world);
        }
        tell(world, j.pos, "message.kiko.town_grow_done");
        dropJob(st, j);
    }

    private static void dropJob(State st, Job j) {
        for (UUID id : j.builders) BUILDERS.remove(id);
        st.jobs.remove(j);
        st.markDirty();
    }

    /** Caminito de tierra de la puerta a la campana. */
    private static void path(ServerWorld world, Job j, BlockBox box) {
        int x, z;
        int midX = (box.getMinX() + box.getMaxX()) / 2, midZ = (box.getMinZ() + box.getMaxZ()) / 2;
        switch (j.rot) {
            case NONE -> { x = midX; z = box.getMinZ() - 1; }
            case CLOCKWISE_180 -> { x = midX; z = box.getMaxZ() + 1; }
            case CLOCKWISE_90 -> { x = box.getMaxX() + 1; z = midZ; }
            default -> { x = box.getMinX() - 1; z = midZ; }
        }
        for (int i = 0; i < 90; i++) {
            if (Math.abs(j.bell.getX() - x) <= 1 && Math.abs(j.bell.getZ() - z) <= 1) break;
            int dx = j.bell.getX() - x, dz = j.bell.getZ() - z;
            // Alterna para que no sea una L perfecta
            if (Math.abs(dx) > Math.abs(dz) || (dz == 0)) x += Integer.signum(dx);
            else z += Integer.signum(dz);
            if (!world.isChunkLoaded(x >> 4, z >> 4)) break;
            int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z) - 1;
            BlockPos g = new BlockPos(x, y, z);
            BlockState gs = world.getBlockState(g);
            if (gs.isOf(Blocks.GRASS_BLOCK) || gs.isOf(Blocks.DIRT) || gs.isOf(Blocks.COARSE_DIRT) || gs.isOf(Blocks.PODZOL)) {
                BlockState up = world.getBlockState(g.up());
                if (up.isAir() || up.isReplaceable()) {
                    world.setBlockState(g.up(), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.setBlockState(g, Blocks.DIRT_PATH.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
            }
        }
    }

    /** Faroles de poste junto a la puerta y a lo largo del caminito, como los del resto del pueblo. */
    private static void lamps(ServerWorld world, Job j, BlockBox box) {
        String[] parts = j.template.getPath().split("/");
        String wood = parts.length > 1 ? parts[1] : "oak";
        Block fence = switch (wood) {
            case "spruce" -> Blocks.SPRUCE_FENCE;
            case "acacia" -> Blocks.ACACIA_FENCE;
            default -> Blocks.OAK_FENCE;
        };
        int midX = (box.getMinX() + box.getMaxX()) / 2, midZ = (box.getMinZ() + box.getMaxZ()) / 2;
        boolean alongZ = j.rot == BlockRotation.NONE || j.rot == BlockRotation.CLOCKWISE_180;
        int fx, fz;
        switch (j.rot) {
            case NONE -> { fx = midX; fz = box.getMinZ() - 2; }
            case CLOCKWISE_180 -> { fx = midX; fz = box.getMaxZ() + 2; }
            case CLOCKWISE_90 -> { fx = box.getMaxX() + 2; fz = midZ; }
            default -> { fx = box.getMinX() - 2; fz = midZ; }
        }
        for (int side : new int[]{-2, 2}) {
            lampPost(world, alongZ ? fx + side : fx, alongZ ? fz : fz + side, fence);
        }
    }

    private static void lampPost(ServerWorld world, int x, int z, Block fence) {
        if (!world.isChunkLoaded(x >> 4, z >> 4)) return;
        int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
        BlockPos g = new BlockPos(x, y - 1, z);
        BlockState gs = world.getBlockState(g);
        if (!(gs.isOf(Blocks.GRASS_BLOCK) || gs.isOf(Blocks.DIRT) || gs.isOf(Blocks.COARSE_DIRT) || gs.isOf(Blocks.PODZOL))) return;
        BlockPos p = g.up();
        if (!(world.getBlockState(p).isAir() || world.getBlockState(p).isReplaceable())) return;
        world.setBlockState(p, fence.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(p.up(), fence.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(p.up(2), Blocks.LANTERN.getDefaultState(), Block.NOTIFY_LISTENERS);
    }

    private static void tell(ServerWorld world, BlockPos at, String key) {
        for (ServerPlayerEntity p : world.getPlayers()) {
            if (p.squaredDistanceTo(at.getX(), at.getY(), at.getZ()) < 140 * 140) {
                p.sendMessage(Text.translatable(key), false);
            }
        }
    }

    // ------------------------------------------------------------------
    // Constructores
    // ------------------------------------------------------------------
    private static void refreshBuilders(ServerWorld world, Job j) {
        Iterator<UUID> it = j.builders.iterator();
        while (it.hasNext()) {
            UUID id = it.next();
            if (!(world.getEntity(id) instanceof TownsfolkEntity t) || !t.isAlive()) {
                BUILDERS.remove(id);
                it.remove();
            }
        }
        if (j.builders.size() >= MAX_BUILDERS) {
            for (UUID id : j.builders) BUILDERS.put(id, j);
            return;
        }
        BlockPos pos = j.pos;
        List<TownsfolkEntity> list = world.getEntitiesByClass(TownsfolkEntity.class,
                new net.minecraft.util.math.Box(pos).expand(56),
                t -> t.isAlive() && !t.isTownKid() && !t.isElder() && !t.isSleeping() && !t.isBeingAttended()
                        && !j.builders.contains(t.getUuid()) && !BUILDERS.containsKey(t.getUuid()));
        list.sort((a, b) -> Double.compare(a.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()), b.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ())));
        for (TownsfolkEntity t : list) {
            if (j.builders.size() >= MAX_BUILDERS) break;
            j.builders.add(t.getUuid());
        }
        for (UUID id : j.builders) BUILDERS.put(id, j);
    }

    @Nullable
    static Job jobFor(UUID id) {
        return BUILDERS.get(id);
    }

    /** El vecino camina alrededor de la obra y martillea. */
    public static class BuildGoal extends Goal {
        private final TownsfolkEntity npc;
        private Job job;
        private int retarget, swing;

        public BuildGoal(TownsfolkEntity npc) {
            this.npc = npc;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        private boolean ok() {
            return job != null && !npc.isSleeping() && npc.getTarget() == null && !npc.isBeingAttended()
                    && !TownSleepGoal.isNightTime(npc.getWorld());
        }

        @Override
        public boolean canStart() {
            job = BUILDERS.get(npc.getUuid());
            return job != null && job.box != null && ok();
        }

        @Override
        public boolean shouldContinue() {
            return BUILDERS.get(npc.getUuid()) == job && ok();
        }

        @Override
        public void start() {
            retarget = 0;
        }

        @Override
        public void stop() {
            npc.getNavigation().stop();
        }

        @Override
        public void tick() {
            BlockBox b = job.box;
            if (b == null) return;
            double cx = (b.getMinX() + b.getMaxX()) / 2.0, cz = (b.getMinZ() + b.getMaxZ()) / 2.0;
            npc.getLookControl().lookAt(cx, job.pos.getY() + Math.max(1, job.layer), cz);
            if (--retarget <= 0) {
                retarget = 60 + npc.getRandom().nextInt(60);
                int x, z;
                if (npc.getRandom().nextBoolean()) {
                    x = b.getMinX() - 2 + npc.getRandom().nextInt(b.getMaxX() - b.getMinX() + 5);
                    z = npc.getRandom().nextBoolean() ? b.getMinZ() - 2 : b.getMaxZ() + 2;
                } else {
                    z = b.getMinZ() - 2 + npc.getRandom().nextInt(b.getMaxZ() - b.getMinZ() + 5);
                    x = npc.getRandom().nextBoolean() ? b.getMinX() - 2 : b.getMaxX() + 2;
                }
                int y = npc.getWorld().getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
                npc.getNavigation().startMovingTo(x + 0.5, y, z + 0.5, 0.9);
            }
            double dx = npc.getX() - cx, dz = npc.getZ() - cz;
            double reach = Math.max(b.getMaxX() - b.getMinX(), b.getMaxZ() - b.getMinZ()) / 2.0 + 6;
            if (dx * dx + dz * dz <= reach * reach && --swing <= 0) {
                swing = 12 + npc.getRandom().nextInt(10);
                npc.swingHand(Hand.MAIN_HAND);
            }
        }
    }

    // ------------------------------------------------------------------
    // Comandos de prueba
    // ------------------------------------------------------------------
    /** /kiko town grow */
    public static Text cmdGrow(ServerWorld world, BlockPos from) {
        BlockPos bell = TownMarket.findBell(world, from);
        if (bell == null) return Text.translatable("command.kiko.town_nobell");
        String err = start(world, state(world), bell, true, null);
        return err == null ? Text.translatable("command.kiko.town_started") : Text.translatable(err);
    }

    /** /kiko town school (manda construir la escuela) */
    public static Text cmdSchool(ServerWorld world, BlockPos from) {
        BlockPos bell = TownMarket.findBell(world, from);
        if (bell == null) return Text.translatable("command.kiko.town_nobell");
        String err = start(world, state(world), bell, true, "school");
        return err == null ? Text.translatable("command.kiko.town_started") : Text.translatable(err);
    }

    /** La escuela del pueblo de esa campana: caja y giro, o null si no hay. */
    @Nullable
    public static BlockBox schoolBox(ServerWorld world, BlockPos bell) {
        long[] sc = state(world).schools.get(bell.asLong());
        if (sc == null) return null;
        String wood = woodAround(world, bell);
        Optional<StructureTemplate> opt = world.getStructureTemplateManager().getTemplate(Identifier.of("kiko", "town/" + wood + "/school"));
        if (opt.isEmpty()) return null;
        BlockRotation rot = BlockRotation.values()[(int) Math.floorMod(sc[1], 4L)];
        return opt.get().calculateBoundingBox(place(rot, false), BlockPos.fromLong(sc[0]));
    }

    /** /kiko town finish */
    public static Text cmdFinish(ServerWorld world) {
        State st = state(world);
        if (st.jobs.isEmpty()) return Text.translatable("command.kiko.town_idle");
        Job j = st.jobs.get(0);
        Optional<StructureTemplate> opt = world.getStructureTemplateManager().getTemplate(j.template);
        if (opt.isEmpty()) {
            dropJob(st, j);
            return Text.translatable("command.kiko.town_idle");
        }
        finish(world, st, j, opt.get());
        return Text.translatable("command.kiko.town_finished");
    }

    /** /kiko town status */
    public static Text cmdStatus(ServerWorld world, BlockPos from) {
        State st = state(world);
        if (st.jobs.isEmpty()) {
            BlockPos bell = TownMarket.findBell(world, from);
            long built = 0;
            if (bell != null) {
                long[] info = st.towns.get(bell.asLong());
                if (info != null) built = info[0];
            }
            return Text.translatable("command.kiko.town_status_idle", built);
        }
        Job j = st.jobs.get(0);
        return Text.translatable("command.kiko.town_status_job", Math.max(0, j.layer), j.sizeY, j.builders.size(),
                j.pos.getX(), j.pos.getY(), j.pos.getZ());
    }
}
