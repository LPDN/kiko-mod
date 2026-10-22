package com.jaciel.kiko.town;

import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.animation.KikoPose;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.family.KikoChildEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * La escuela (v3.9). La construyen los vecinos (TownGrowth) cuando el pueblo tiene 3 niños o más.
 * De lunes a viernes en la mañana los niños (también tus hijos si no te andan siguiendo) se sientan en
 * los pupitres, levantan la mano y escriben, y la maestra del pueblo da la clase frente al pizarrón.
 * Los que fueron 5 días o más salen de grandes con mejor oficio.
 */
public final class TownSchool {
    private TownSchool() {}

    public static final long START = 1000, END = 5000;

    /** Lo que se necesita de cada escuela (se arma revisando sus bloques una vez). */
    static final class School {
        BlockPos bell = BlockPos.ORIGIN;
        final List<BlockPos> seats = new ArrayList<>();
        @Nullable
        BlockPos teacherSpot;
        Vec3d front = Vec3d.ZERO;
        long day = -1;
        final Map<BlockPos, UUID> claims = new HashMap<>();
    }

    private static final Map<RegistryKey<World>, Map<Long, School>> CACHE = new HashMap<>();
    private static final Map<Long, Long> TRIED = new HashMap<>();

    /** Se construyó una escuela nueva: hay que volver a buscar. */
    static void forget(ServerWorld world) {
        CACHE.remove(world.getRegistryKey());
        TRIED.clear();
    }

    public static boolean isClassTime(World w) {
        long tod = w.getTimeOfDay() % 24000L;
        return KikoConfig.get().townSchool && TownCalendar.weekday(w) <= 4 && tod >= START && tod < END;
    }

    @Nullable
    static School near(ServerWorld world, BlockPos pos) {
        BlockPos bell = TownMarket.findBell(world, pos);
        if (bell == null || bell.getSquaredDistance(pos) > 110 * 110) return null;
        Map<Long, School> map = CACHE.computeIfAbsent(world.getRegistryKey(), k -> new HashMap<>());
        School cached = map.get(bell.asLong());
        if (cached != null) return cached;
        Long tried = TRIED.get(bell.asLong());
        if (tried != null && world.getTime() - tried < 1200) return null;
        School s = build(world, bell);
        if (s == null) TRIED.put(bell.asLong(), world.getTime());
        else map.put(bell.asLong(), s);
        return s;
    }

    @Nullable
    private static School build(ServerWorld world, BlockPos bell) {
        BlockBox box = TownGrowth.schoolBox(world, bell);
        if (box == null || !world.isChunkLoaded(new BlockPos(box.getMinX(), box.getMinY(), box.getMinZ()))
                || !world.isChunkLoaded(new BlockPos(box.getMaxX(), box.getMinY(), box.getMaxZ()))) return null;
        School s = new School();
        s.bell = bell.toImmutable();
        BlockPos lectern = null;
        double bx = 0, by = 0, bz = 0;
        int boards = 0;
        for (BlockPos p : BlockPos.iterate(box.getMinX(), box.getMinY(), box.getMinZ(), box.getMaxX(), box.getMinY() + 4, box.getMaxZ())) {
            BlockState st = world.getBlockState(p);
            if (Seats.isChair(st)) s.seats.add(p.toImmutable());
            else if (st.isOf(Blocks.LECTERN)) lectern = p.toImmutable();
            else if (st.isOf(Blocks.GREEN_TERRACOTTA)) {
                bx += p.getX();
                by += p.getY();
                bz += p.getZ();
                boards++;
            }
        }
        if (s.seats.isEmpty() || boards == 0) return null;
        BlockPos board = BlockPos.ofFloored(bx / boards, by / boards, bz / boards);
        // La silla de la maestra es la que está junto al atril; no es de alumnos
        if (lectern != null) {
            final BlockPos lec = lectern;
            s.seats.stream().min((a, b) -> Double.compare(a.getSquaredDistance(lec), b.getSquaredDistance(lec))).ifPresent(s.seats::remove);
        }
        double fx = 0, fz = 0;
        for (BlockPos p : s.seats) {
            fx += p.getX() + 0.5;
            fz += p.getZ() + 0.5;
        }
        s.front = new Vec3d(fx / s.seats.size(), s.seats.get(0).getY() + 1.6, fz / s.seats.size());
        // La maestra se para entre el pizarrón y los pupitres
        int floorY = s.seats.get(0).getY();
        Vec3d toSeats = s.front.subtract(board.getX() + 0.5, s.front.y, board.getZ() + 0.5).normalize();
        BlockPos want = BlockPos.ofFloored(board.getX() + 0.5 + toSeats.x * 1.6, floorY, board.getZ() + 0.5 + toSeats.z * 1.6);
        s.teacherSpot = world.getBlockState(want).getCollisionShape(world, want).isEmpty() ? want : Seats.freeSpot(world, want);
        return s;
    }

    @Nullable
    private static BlockPos claim(ServerWorld world, School s, UUID who) {
        long day = TownCalendar.day(world);
        if (s.day != day) {
            s.day = day;
            s.claims.clear();
        }
        for (Map.Entry<BlockPos, UUID> e : s.claims.entrySet()) if (e.getValue().equals(who)) return e.getKey();
        for (BlockPos seat : s.seats) {
            if (s.claims.containsKey(seat) || !Seats.isChair(world.getBlockState(seat))) continue;
            s.claims.put(seat, who);
            return seat;
        }
        return null;
    }

    // ------------------------------------------------------------------
    // Alumnos (niños del pueblo y tus hijos)
    // ------------------------------------------------------------------
    public static class StudentGoal extends Goal {
        private final KikoChildEntity kid;
        @Nullable
        private School school;
        @Nullable
        private BlockPos seat;
        private int ticks, next;

        public StudentGoal(KikoChildEntity kid) {
            this.kid = kid;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
        }

        private boolean ok() {
            int st = kid.getStage();
            return (st == KikoChildEntity.CHILD || st == KikoChildEntity.TEEN) && isClassTime(kid.getWorld()) && !kid.isSleeping()
                    && !kid.isBeingAttended() && kid.getTarget() == null && !(kid.isFollowEnabled() && kid.getParentUuid() != null)
                    && !(kid instanceof TownsfolkEntity t && TownRaid.hiding(t));
        }

        @Override
        public boolean canStart() {
            if (kid.age % 40 != 13 || !(kid.getWorld() instanceof ServerWorld sw) || !ok()) return false;
            school = near(sw, kid.getBlockPos());
            if (school == null) return false;
            seat = claim(sw, school, kid.getUuid());
            return seat != null;
        }

        @Override
        public boolean shouldContinue() {
            return seat != null && ok();
        }

        @Override
        public void start() {
            ticks = 0;
            next = 100 + kid.getRandom().nextInt(100);
        }

        @Override
        public void stop() {
            if (kid.getKikoPose() == KikoPose.SIT) Seats.stand(kid, seat);
            if (seat != null && kid.getWorld() instanceof ServerWorld sw && ticks > 1200) kid.attendSchool(TownCalendar.day(sw));
            seat = null;
        }

        @Override
        public void tick() {
            if (seat == null) return;
            ticks++;
            if (kid.getKikoPose() == KikoPose.SIT) {
                Seats.keep(kid, seat);
                if (school != null) kid.getLookControl().lookAt(school.front.x, school.front.y, school.front.z);
                if (--next <= 0) {
                    next = 160 + kid.getRandom().nextInt(200);
                    kid.playAnimation(kid.getRandom().nextInt(3) == 0 ? KikoAnimation.RAISE_HAND : KikoAnimation.WRITE);
                }
                if (ticks > 1200 && kid.getWorld() instanceof ServerWorld sw) kid.attendSchool(TownCalendar.day(sw));
                return;
            }
            double d = kid.squaredDistanceTo(seat.getX() + 0.5, seat.getY() + 0.5, seat.getZ() + 0.5);
            if (d < 2.2) {
                if (!Seats.taken(kid.getWorld(), seat, kid)) Seats.sit(kid, seat);
            } else if (ticks % 30 == 1) {
                kid.getNavigation().startMovingTo(seat.getX() + 0.5, seat.getY(), seat.getZ() + 0.5, 1.0);
            }
            // Si no llega en un buen rato (puerta cerrada, camino tapado) se rinde por hoy
            if (ticks > 2400 && kid.getKikoPose() != KikoPose.SIT) seat = null;
        }
    }

    // ------------------------------------------------------------------
    // La maestra
    // ------------------------------------------------------------------
    public static class TeacherGoal extends Goal {
        private final TownsfolkEntity npc;
        @Nullable
        private School school;
        private int ticks;

        public TeacherGoal(TownsfolkEntity npc) {
            this.npc = npc;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        private boolean ok() {
            return "teacher".equals(npc.getJobId()) && !npc.isTownKid() && isClassTime(npc.getWorld()) && !npc.isSleeping()
                    && !npc.isBeingAttended() && !TownRaid.hiding(npc);
        }

        @Override
        public boolean canStart() {
            if (npc.age % 40 != 21 || !(npc.getWorld() instanceof ServerWorld sw) || !ok()) return false;
            school = near(sw, npc.getBlockPos());
            return school != null && school.teacherSpot != null;
        }

        @Override
        public boolean shouldContinue() {
            return school != null && ok();
        }

        @Override
        public void start() {
            ticks = 0;
        }

        @Override
        public void tick() {
            if (school == null || school.teacherSpot == null) return;
            ticks++;
            BlockPos at = school.teacherSpot;
            if (npc.squaredDistanceTo(at.getX() + 0.5, at.getY(), at.getZ() + 0.5) > 1.5) {
                if (ticks % 30 == 1) npc.getNavigation().startMovingTo(at.getX() + 0.5, at.getY(), at.getZ() + 0.5, 0.8);
                return;
            }
            npc.getNavigation().stop();
            npc.getLookControl().lookAt(school.front.x, school.front.y, school.front.z);
            if (ticks % 140 == 0) {
                npc.playAnimation(KikoAnimation.POINT);
                npc.talk(40);
            }
        }
    }
}
