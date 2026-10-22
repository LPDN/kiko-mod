package com.jaciel.kiko.town;

import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.animation.KikoPose;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.family.KikoChildEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * La feria del pueblo (v3.9). Cada sábado (y al día siguiente de rechazar un ataque, la fiesta de victoria)
 * la plaza se adorna con postes, faroles y papel picado, suena música, los vecinos bailan (en pareja si
 * hay con quién), brindan, y los puestos venden más barato. En la noche se quitan los adornos solos.
 */
public final class TownFestival {
    private TownFestival() {}

    public static final int SATURDAY = 5;
    private static final long OPEN = 1000, CLOSE = 12500;
    private static final String[] PANES = {"pink", "orange", "yellow", "lime", "light_blue", "magenta", "red", "cyan"};
    /** Una melodía sencilla (semitonos desde Fa#) que se repite. */
    private static final int[] SONG = {12, 14, 16, 12, 16, 17, 19, -1, 19, 17, 16, 14, 12, 14, 12, -1,
            7, 9, 12, 9, 12, 14, 16, -1, 14, 12, 9, 7, 9, 12, -1, -1};

    // ------------------------------------------------------------------
    // Estado (se guarda para quitar los adornos aunque se cierre el mundo)
    // ------------------------------------------------------------------
    public static final class State extends PersistentState {
        long day = -1;
        BlockPos bell = BlockPos.ORIGIN;
        BlockPos center = BlockPos.ORIGIN;
        boolean victory;
        final List<long[]> placed = new ArrayList<>();      // {pos, rawId del bloque puesto}
        /** campana → día de la fiesta de victoria */
        final Map<Long, Long> victories = new HashMap<>();

        boolean active() {
            return day >= 0;
        }

        @Override
        public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
            nbt.putLong("day", day);
            nbt.putLong("bell", bell.asLong());
            nbt.putLong("center", center.asLong());
            nbt.putBoolean("victory", victory);
            long[] flat = new long[placed.size() * 2];
            for (int i = 0; i < placed.size(); i++) {
                flat[i * 2] = placed.get(i)[0];
                flat[i * 2 + 1] = placed.get(i)[1];
            }
            nbt.putLongArray("placed", flat);
            NbtList vs = new NbtList();
            for (Map.Entry<Long, Long> e : victories.entrySet()) {
                NbtCompound c = new NbtCompound();
                c.putLong("bell", e.getKey());
                c.putLong("day", e.getValue());
                vs.add(c);
            }
            nbt.put("victories", vs);
            return nbt;
        }

        static State load(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
            State s = new State();
            s.day = nbt.getLong("day");
            s.bell = BlockPos.fromLong(nbt.getLong("bell"));
            s.center = BlockPos.fromLong(nbt.getLong("center"));
            s.victory = nbt.getBoolean("victory");
            long[] flat = nbt.getLongArray("placed");
            for (int i = 0; i + 1 < flat.length; i += 2) s.placed.add(new long[]{flat[i], flat[i + 1]});
            NbtList vs = nbt.getList("victories", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < vs.size(); i++) s.victories.put(vs.getCompound(i).getLong("bell"), vs.getCompound(i).getLong("day"));
            return s;
        }
    }

    private static final PersistentState.Type<State> TYPE = new PersistentState.Type<>(State::new, State::load, null);

    static State state(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(TYPE, "kiko_festival");
    }

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(TownFestival::tick);
    }

    /** El pueblo repelió un ataque: mañana hay fiesta de victoria. */
    public static void victory(ServerWorld world, BlockPos bell) {
        State st = state(world);
        st.victories.put(bell.asLong(), TownCalendar.day(world) + 1);
        st.markDirty();
    }

    /** ¿Hay feria en esta plaza ahorita? */
    public static boolean activeNear(World world, BlockPos pos) {
        if (!(world instanceof ServerWorld sw)) return false;
        State st = state(sw);
        return st.active() && st.center.getSquaredDistance(pos) < 80 * 80;
    }

    @Nullable
    static BlockPos centerNear(World world, BlockPos pos) {
        if (!(world instanceof ServerWorld sw)) return null;
        State st = state(sw);
        return st.active() && st.center.getSquaredDistance(pos) < 80 * 80 ? st.center : null;
    }

    // ------------------------------------------------------------------
    // Tick
    // ------------------------------------------------------------------
    private static void tick(ServerWorld world) {
        long time = world.getTime();
        if (time % 10L != 5L) return;
        State st = state(world);
        long tod = world.getTimeOfDay() % 24000L;
        long day = TownCalendar.day(world);
        if (st.active()) {
            if (st.day != day || tod >= CLOSE || tod < OPEN || !KikoConfig.get().townFestival) {
                stop(world, st);
                return;
            }
            if (world.isChunkLoaded(st.center)) music(world, st, time);
            if (time % 200L == 5L) cheer(world, st);
            return;
        }
        if (!KikoConfig.get().townFestival || tod < OPEN || tod >= CLOSE - 1500 || time % 100L != 5L) return;
        for (ServerPlayerEntity p : world.getPlayers()) {
            BlockPos bell = TownMarket.findBell(world, p.getBlockPos());
            if (bell == null || bell.getSquaredDistance(p.getPos()) > 96 * 96) continue;
            Long vday = st.victories.get(bell.asLong());
            boolean victory = vday != null && vday == day;
            boolean saturday = TownCalendar.weekday(world) == SATURDAY;
            if (!victory && !saturday) continue;
            start(world, st, bell, victory, day);
            return;
        }
    }

    public static boolean start(ServerWorld world, State st, BlockPos bell, boolean victory, long day) {
        if (st.active()) return false;
        st.day = day;
        st.bell = bell.toImmutable();
        st.center = plazaCenter(world, bell);
        st.victory = victory;
        st.victories.remove(bell.asLong());
        decorate(world, st);
        st.markDirty();
        Text msg = Text.translatable(victory ? "message.kiko.festival_victory" : "message.kiko.festival_start").formatted(Formatting.GOLD);
        for (ServerPlayerEntity p : world.getPlayers()) {
            if (p.squaredDistanceTo(bell.getX(), bell.getY(), bell.getZ()) < 140 * 140) p.sendMessage(msg, false);
        }
        world.playSound(null, bell, SoundEvents.BLOCK_BELL_USE, SoundCategory.BLOCKS, 2f, 1.2f);
        return true;
    }

    public static void stop(ServerWorld world, State st) {
        for (long[] e : st.placed) {
            BlockPos p = BlockPos.fromLong(e[0]);
            if (!world.isChunkLoaded(p)) continue;
            BlockState s = world.getBlockState(p);
            if (Block.getRawIdFromState(s) == (int) e[1] || s.isOf(Blocks.CANDLE_CAKE) || s.isOf(Blocks.CAKE)) {
                world.setBlockState(p, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
        }
        st.placed.clear();
        st.day = -1;
        st.markDirty();
        for (TownsfolkEntity t : world.getEntitiesByClass(TownsfolkEntity.class, new Box(st.center).expand(40),
                t -> t.getKikoPose() == KikoPose.PAIR_DANCE)) t.setKikoPose(KikoPose.NONE);
    }

    /** /kiko town fiesta: la empieza ahorita en la plaza más cercana, o la termina si ya hay. */
    public static Text cmdToggle(ServerWorld world, BlockPos from) {
        State st = state(world);
        if (st.active()) {
            stop(world, st);
            return Text.translatable("command.kiko.fiesta_stopped");
        }
        BlockPos bell = TownMarket.findBell(world, from);
        if (bell == null) return Text.translatable("command.kiko.town_nobell");
        start(world, st, bell, false, TownCalendar.day(world));
        return Text.translatable("command.kiko.fiesta_started");
    }

    /** El centro de la plaza es la fuente (agua cerca de la campana); si no hay, la campana. */
    private static BlockPos plazaCenter(ServerWorld world, BlockPos bell) {
        long sx = 0, sz = 0;
        int n = 0;
        for (BlockPos p : BlockPos.iterate(bell.add(-9, -3, -9), bell.add(9, 1, 9))) {
            if (world.getBlockState(p).isOf(Blocks.WATER)) {
                sx += p.getX();
                sz += p.getZ();
                n++;
            }
        }
        if (n == 0) return bell.down(2);
        return new BlockPos((int) Math.round((double) sx / n), bell.getY() - 2, (int) Math.round((double) sz / n));
    }

    /** Postes con farol en las esquinas y en medio de cada lado, papel picado por las orillas y en cruz, y mesas con pastel. */
    private static void decorate(ServerWorld world, State st) {
        BlockPos c = st.center;
        int h = 8;
        Block fence = Blocks.OAK_FENCE;
        BlockState around = world.getBlockState(st.bell.down());
        if (around.getBlock() instanceof net.minecraft.block.FenceBlock) fence = around.getBlock();
        int[][] posts = {{-h, -h}, {h, -h}, {-h, h}, {h, h}, {0, -h}, {0, h}, {-h, 0}, {h, 0}};
        for (int[] o : posts) {
            for (int y = 1; y <= 5; y++) put(world, st, c.add(o[0], y, o[1]), fence.getDefaultState());
            put(world, st, c.add(o[0], 6, o[1]), Blocks.LANTERN.getDefaultState().with(LanternBlock.HANGING, false));
        }
        int[][][] lines = {{{-h, -h}, {h, -h}}, {{-h, h}, {h, h}}, {{-h, -h}, {-h, h}}, {{h, -h}, {h, h}}, {{0, -h}, {0, h}}, {{-h, 0}, {h, 0}}};
        for (int[][] l : lines) {
            int x0 = l[0][0], z0 = l[0][1], x1 = l[1][0], z1 = l[1][1];
            int n = Math.max(Math.abs(x1 - x0), Math.abs(z1 - z0));
            for (int i = 1; i < n; i++) {
                int x = x0 + Math.round((float) (x1 - x0) * i / n), z = z0 + Math.round((float) (z1 - z0) * i / n);
                String col = PANES[Math.floorMod(x + z, PANES.length)];
                Block pane = Registries.BLOCK.get(Identifier.of("minecraft", col + "_stained_glass_pane"));
                put(world, st, c.add(x, 5, z), pane.getDefaultState());
            }
        }
        // Las conexiones de los vidrios se arreglan solas con una actualización
        for (long[] e : st.placed) {
            BlockPos p = BlockPos.fromLong(e[0]);
            BlockState s = world.getBlockState(p);
            BlockState fixed = Block.postProcessState(s, world, p);
            if (fixed != s) {
                world.setBlockState(p, fixed, Block.NOTIFY_LISTENERS);
                e[1] = Block.getRawIdFromState(fixed);
            }
        }
        int[][] tables = {{-5, -2}, {5, -2}, {-5, 2}, {5, 2}};
        for (int[] o : tables) {
            BlockPos t = c.add(o[0], 1, o[1]);
            if (world.getBlockState(t).isAir() && world.getBlockState(t.up()).isAir() && !world.getBlockState(t.down()).isAir()) {
                put(world, st, t, Blocks.WHITE_WOOL.getDefaultState());
                put(world, st, t.up(), Blocks.CANDLE_CAKE.getDefaultState());
            }
        }
    }

    private static void put(ServerWorld world, State st, BlockPos p, BlockState s) {
        if (!world.isChunkLoaded(p) || !world.getBlockState(p).isAir()) return;
        world.setBlockState(p, s, Block.NOTIFY_LISTENERS);
        st.placed.add(new long[]{p.asLong(), Block.getRawIdFromState(world.getBlockState(p))});
    }

    private static void music(ServerWorld world, State st, long time) {
        int beat = (int) ((time / 10L) % SONG.length);
        int note = SONG[beat];
        if (note < 0) return;
        float pitch = (float) Math.pow(2.0, (note - 12) / 12.0);
        RegistryEntry<SoundEvent> inst = (time / 10L / SONG.length) % 2 == 0 ? SoundEvents.BLOCK_NOTE_BLOCK_HARP : SoundEvents.BLOCK_NOTE_BLOCK_FLUTE;
        BlockPos at = st.center.up(2);
        world.playSound(null, at, inst.value(), SoundCategory.RECORDS, 1.6f, pitch);
        if (beat % 2 == 0) {
            world.playSound(null, at, SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM.value(), SoundCategory.RECORDS, 0.8f, 1f);
        }
        world.spawnParticles(ParticleTypes.NOTE, at.getX() + 0.5, at.getY() + 1.5, at.getZ() + 0.5, 1, 0.6, 0.3, 0.6, note / 24.0);
    }

    /** De vez en cuando alguien brinda o echa porras (en la de victoria, los guardias). */
    private static void cheer(ServerWorld world, State st) {
        for (TownsfolkEntity t : world.getEntitiesByClass(TownsfolkEntity.class, new Box(st.center).expand(12), TownsfolkEntity::isAlive)) {
            if (t.isSleeping() || t.getKikoPose().seated()) continue;
            int r = t.getRandom().nextInt(6);
            if (st.victory && t.isGuard()) {
                t.playAnimation(KikoAnimation.CHEER);
                t.setExpression(KikoExpression.LAUGH, 60);
            } else if (r == 0 && !t.isTownKid()) {
                t.playAnimation(KikoAnimation.TOAST);
            } else if (r == 1) {
                t.playAnimation(KikoAnimation.CLAP);
            }
        }
    }

    // ------------------------------------------------------------------
    // Los vecinos en la feria
    // ------------------------------------------------------------------
    public static class FestivalGoal extends Goal {
        private final TownsfolkEntity npc;
        @Nullable
        private BlockPos center, spot;
        private int ticks, dance;

        public FestivalGoal(TownsfolkEntity npc) {
            this.npc = npc;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        private boolean free() {
            return !npc.isSleeping() && !npc.isBeingAttended() && npc.getTarget() == null && npc.getStage() != KikoChildEntity.BABY
                    && !TownRaid.hiding(npc) && !(npc.isGuard() && !state((ServerWorld) npc.getWorld()).victory && npc.isNightWatch());
        }

        @Override
        public boolean canStart() {
            if (!(npc.getWorld() instanceof ServerWorld) || npc.age % 40 != 0) return false;
            center = centerNear(npc.getWorld(), npc.getBlockPos());
            return center != null && free();
        }

        @Override
        public boolean shouldContinue() {
            return center != null && centerNear(npc.getWorld(), npc.getBlockPos()) != null && free();
        }

        @Override
        public void start() {
            ticks = 0;
            spot = null;
        }

        @Override
        public void stop() {
            if (npc.getKikoPose() == KikoPose.PAIR_DANCE) npc.setKikoPose(KikoPose.NONE);
        }

        @Override
        public void tick() {
            if (center == null) return;
            ticks++;
            if (spot == null || ticks % 400 == 0) {
                double a = npc.getRandom().nextDouble() * Math.PI * 2;
                double r = 2.5 + npc.getRandom().nextDouble() * 4.5;
                spot = BlockPos.ofFloored(center.getX() + 0.5 + Math.cos(a) * r, center.getY() + 1, center.getZ() + 0.5 + Math.sin(a) * r);
                if (npc.getKikoPose() == KikoPose.PAIR_DANCE) npc.setKikoPose(KikoPose.NONE);
            }
            double d = npc.squaredDistanceTo(spot.getX() + 0.5, npc.getY(), spot.getZ() + 0.5);
            if (d > 2.5) {
                if (ticks % 20 == 1) npc.getNavigation().startMovingTo(spot.getX() + 0.5, spot.getY(), spot.getZ() + 0.5, 0.8);
                return;
            }
            npc.getNavigation().stop();
            // ¿Hay pareja a un lado? (grande con grande, niño con niño)
            TownsfolkEntity partner = null;
            for (TownsfolkEntity o : npc.getWorld().getEntitiesByClass(TownsfolkEntity.class, npc.getBoundingBox().expand(2.2),
                    o -> o != npc && o.isAlive() && o.isTownKid() == npc.isTownKid() && !o.getKikoPose().seated())) {
                partner = o;
                break;
            }
            if (partner != null) {
                npc.setKikoPose(KikoPose.PAIR_DANCE);
                npc.getLookControl().lookAt(partner);
            } else {
                if (npc.getKikoPose() == KikoPose.PAIR_DANCE) npc.setKikoPose(KikoPose.NONE);
                npc.getLookControl().lookAt(center.getX() + 0.5, center.getY() + 1.5, center.getZ() + 0.5);
                if (--dance <= 0) {
                    dance = 100 + npc.getRandom().nextInt(60);
                    npc.playAnimation(KikoAnimation.DANCE);
                }
            }
        }
    }
}
