package com.jaciel.kiko.town;

import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.animation.KikoPose;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.family.KikoChildEntity;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import com.jaciel.kiko.registry.ModItems;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * El clima (v3.9).
 *  - Con lluvia los grandes sacan el paraguas (o se tapan la cabeza) y se van a su casa; los niños corren
 *  - Con nieve tiemblan de frío
 *  - Con tormenta los niños se asustan con los truenos
 *  - En noches despejadas (sin luna llena) algunos vecinos salen a ver las estrellas antes de dormir
 *  - Tus compañeras comentan cuando cambia el clima
 */
public final class TownWeather {
    private TownWeather() {}

    public enum Sky { CLEAR, RAIN, SNOW, THUNDER }

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(TownWeather::tick);
    }

    /** ¿Le está cayendo lluvia (o nieve) en la cabeza? */
    public static Sky skyAt(World w, BlockPos pos) {
        if (!w.isRaining() || !w.isSkyVisible(pos.up())) return Sky.CLEAR;
        Biome.Precipitation p = w.getBiome(pos).value().getPrecipitation(pos);
        if (p == Biome.Precipitation.SNOW) return Sky.SNOW;
        if (p == Biome.Precipitation.NONE) return Sky.CLEAR;
        return w.isThundering() ? Sky.THUNDER : Sky.RAIN;
    }

    /** La mitad (más o menos) de los vecinos tiene paraguas. */
    public static boolean ownsUmbrella(TownsfolkEntity t) {
        return Math.floorMod(t.getUuid().hashCode(), 10) < 6;
    }

    private static boolean weatherPose(KikoPose p) {
        return p == KikoPose.UMBRELLA || p == KikoPose.COVER || p == KikoPose.SHIVER;
    }

    // ------------------------------------------------------------------
    // Tick
    // ------------------------------------------------------------------
    private static final Map<UUID, Sky> LAST_SKY = new HashMap<>();
    private static final Map<UUID, Long> STARS_SAID = new HashMap<>();

    private static void tick(ServerWorld world) {
        if (!KikoConfig.get().townWeather || world.getTime() % 20L != 11L) return;
        for (ServerPlayerEntity p : world.getPlayers()) {
            for (TownsfolkEntity t : world.getEntitiesByClass(TownsfolkEntity.class, p.getBoundingBox().expand(72), TownsfolkEntity::isAlive)) {
                folk(world, t);
            }
            companions(world, p);
        }
    }

    private static void folk(ServerWorld world, TownsfolkEntity t) {
        KikoPose pose = t.getKikoPose();
        boolean busy = t.isSleeping() || pose.seated() || pose == KikoPose.PAIR_DANCE || t.isGuard()
                || t.getStage() == KikoChildEntity.BABY || TownRaid.hiding(t);
        Sky sky = busy ? Sky.CLEAR : skyAt(world, t.getBlockPos());
        ItemStack hand = t.getMainHandStack();
        switch (sky) {
            case RAIN, THUNDER -> {
                if (!t.isTownKid() && ownsUmbrella(t) && (hand.isEmpty() || hand.isOf(ModItems.UMBRELLA))) {
                    if (hand.isEmpty()) {
                        t.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.UMBRELLA));
                        t.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0f);
                    }
                    t.setKikoPose(KikoPose.UMBRELLA);
                } else {
                    t.setKikoPose(KikoPose.COVER);
                }
                // Truenos: los niños (y algún grande) brincan del susto
                if (sky == Sky.THUNDER && t.getRandom().nextInt(t.isTownKid() ? 30 : 120) == 0) {
                    t.playAnimation(KikoAnimation.SCARED);
                    t.setExpression(com.jaciel.kiko.animation.KikoExpression.SURPRISED, 40);
                }
            }
            case SNOW -> {
                dropUmbrella(t);
                t.setKikoPose(KikoPose.SHIVER);
            }
            default -> {
                if (weatherPose(pose)) t.setKikoPose(KikoPose.NONE);
                if (!busy || !t.isGuard()) dropUmbrella(t);
            }
        }
    }

    private static void dropUmbrella(TownsfolkEntity t) {
        if (t.getMainHandStack().isOf(ModItems.UMBRELLA)) t.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
    }

    /** Las compañeras que te siguen comentan cuando cambia el clima (y en las noches estrelladas). */
    private static void companions(ServerWorld world, ServerPlayerEntity p) {
        Sky now = skyAt(world, p.getBlockPos());
        Sky before = LAST_SKY.put(p.getUuid(), now);
        long tod = world.getTimeOfDay() % 24000L;
        boolean stars = now == Sky.CLEAR && tod >= 13500 && tod < 14000 && world.getMoonPhase() != 0 && world.isSkyVisible(p.getBlockPos().up());
        long day = TownCalendar.day(world);
        if (stars) {
            Long said = STARS_SAID.get(p.getUuid());
            if (said != null && said == day) stars = false;
            else STARS_SAID.put(p.getUuid(), day);
        }
        if ((before == null || before == now) && !stars) return;
        String key = stars ? "stars" : switch (now) {
            case RAIN -> "rain";
            case SNOW -> "snow";
            case THUNDER -> "thunder";
            default -> "clear";
        };
        if (!stars && now == Sky.CLEAR && before == Sky.SNOW) key = "warm";
        for (KikoEntity k : world.getEntitiesByClass(KikoEntity.class, p.getBoundingBox().expand(16),
                k -> k.isAlive() && !k.isTownsfolk() && !k.isSleeping() && p.getUuid().equals(k.getFriendUuid()))) {
            if (k.getRandom().nextInt(3) == 0) continue;
            if (k instanceof KikoChildEntity kid && kid.getStage() == KikoChildEntity.BABY) continue;
            KikoInteractionHandler.say(p, k, "dialogue.kiko.weather." + key, 3);
            if (now == Sky.THUNDER && k.isMinor()) k.playAnimation(KikoAnimation.SCARED);
            break;   // con una que lo diga basta
        }
    }

    // ------------------------------------------------------------------
    // Goals de los vecinos
    // ------------------------------------------------------------------
    /** Con lluvia se van a su casa (más rápido si no traen paraguas). */
    public static class RainHomeGoal extends Goal {
        private final TownsfolkEntity npc;
        private int repath;

        public RainHomeGoal(TownsfolkEntity npc) {
            this.npc = npc;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        private boolean wet() {
            KikoPose p = npc.getKikoPose();
            return (p == KikoPose.UMBRELLA || p == KikoPose.COVER) && npc.getHome() != null && !npc.isGuard()
                    && npc.getHome().getSquaredDistance(npc.getPos()) > 9;
        }

        @Override
        public boolean canStart() {
            return wet() && npc.getTarget() == null && !npc.isBeingAttended();
        }

        @Override
        public boolean shouldContinue() {
            return wet();
        }

        @Override
        public void tick() {
            if (--repath > 0) return;
            repath = 30;
            BlockPos h = npc.getHome();
            if (h == null) return;
            double speed = npc.getKikoPose() == KikoPose.COVER ? 1.3 : 1.05;
            npc.getNavigation().startMovingTo(h.getX() + 0.5, h.getY(), h.getZ() + 0.5, speed);
        }
    }

    /** Noche despejada: algunos se sientan en el pasto cerca de su casa a ver las estrellas. */
    public static class StargazeGoal extends Goal {
        private final TownsfolkEntity npc;
        @Nullable
        private BlockPos spot;
        private long lastNight = -1;
        private int ticks;

        public StargazeGoal(TownsfolkEntity npc) {
            this.npc = npc;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
        }

        private boolean window() {
            World w = npc.getWorld();
            long tod = w.getTimeOfDay() % 24000L;
            return tod >= 12000 && tod < 14800 && !w.isRaining() && w.getMoonPhase() != 0;
        }

        @Override
        public boolean canStart() {
            if (!KikoConfig.get().townWeather || npc.isTownKid() || npc.isGuard() || npc.isSleeping() || npc.getHome() == null
                    || npc.isBeingAttended() || npc.getTarget() != null || !window()) return false;
            long night = TownCalendar.day(npc.getWorld());
            if (night == lastNight || Math.floorMod(npc.getUuid().hashCode() + night, 3L) != 0) return false;
            lastNight = night;
            spot = findSpot();
            return spot != null;
        }

        @Nullable
        private BlockPos findSpot() {
            World w = npc.getWorld();
            BlockPos home = npc.getHome();
            for (int i = 0; i < 16; i++) {
                int x = home.getX() + npc.getRandom().nextInt(17) - 8;
                int z = home.getZ() + npc.getRandom().nextInt(17) - 8;
                int y = w.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
                BlockPos p = new BlockPos(x, y, z);
                if (w.getBlockState(p.down()).isIn(BlockTags.DIRT) && w.getBlockState(p).isReplaceable() && w.isSkyVisible(p)) return p;
            }
            return null;
        }

        @Override
        public boolean shouldContinue() {
            return spot != null && window() && !npc.isSleeping() && npc.getTarget() == null && !npc.isBeingAttended();
        }

        @Override
        public void start() {
            ticks = 0;
        }

        @Override
        public void stop() {
            if (npc.getKikoPose() == KikoPose.STARGAZE) npc.setKikoPose(KikoPose.NONE);
            spot = null;
        }

        @Override
        public void tick() {
            if (spot == null) return;
            ticks++;
            if (npc.getKikoPose() == KikoPose.STARGAZE) {
                npc.getNavigation().stop();
                npc.getLookControl().lookAt(npc.getX() + Math.sin(ticks * 0.01) * 4, npc.getEyeY() + 6, npc.getZ() + Math.cos(ticks * 0.01) * 4);
                return;
            }
            if (npc.squaredDistanceTo(spot.getX() + 0.5, spot.getY(), spot.getZ() + 0.5) < 2.0) {
                npc.getNavigation().stop();
                npc.setKikoPose(KikoPose.STARGAZE);
            } else if (ticks % 30 == 1) {
                npc.getNavigation().startMovingTo(spot.getX() + 0.5, spot.getY(), spot.getZ() + 0.5, 0.7);
            }
            if (ticks > 600 && npc.getKikoPose() != KikoPose.STARGAZE) spot = null;
        }
    }
}
