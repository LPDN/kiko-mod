package com.jaciel.kiko.town;

import com.jaciel.kiko.config.KikoConfig;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Ataques de pillagers a los pueblos (v3.8).
 * De vez en cuando, con jugador cerca de la campana, llega una banda. Suena la campana, los vecinos (y los niños)
 * se meten a sus casas y los guardias salen a pelear. Los monstruos no matan niños ni bebés (eso lo cuida TownsfolkEntity).
 * Si ganan el pueblo te da esmeraldas. Si pasa mucho tiempo sin que los saquen, los pillagers se retiran.
 */
public final class TownRaid {
    private TownRaid() {}

    private static final String TAG = "kiko_raider";
    private static final int TIMEOUT = 9600;

    public static final class Raid {
        BlockPos bell = BlockPos.ORIGIN;
        final List<UUID> raiders = new ArrayList<>();
        int total, ticks;
    }

    private static final Map<RegistryKey<World>, Raid> ACTIVE = new HashMap<>();
    /** Último día de ataque por campana, y último día que se revisó. */
    private static final Map<Long, Long> LAST = new HashMap<>();
    private static final Map<RegistryKey<World>, Long> SCANNED = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(TownRaid::tick);
        // Si el servidor se reinició a media pelea, los bandidos que quedaron se van
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity.getCommandTags().contains(TAG) && !ACTIVE.containsKey(world.getRegistryKey())) entity.discard();
        });
    }

    /** ¿Este vecino debe estar escondido en su casa? */
    public static boolean hiding(TownsfolkEntity npc) {
        Raid r = ACTIVE.get(npc.getWorld().getRegistryKey());
        return r != null && !npc.isGuard() && r.bell.getSquaredDistance(npc.getPos()) < 110 * 110;
    }

    // ------------------------------------------------------------------
    // Tick
    // ------------------------------------------------------------------
    private static void tick(ServerWorld world) {
        if (world.getTime() % 20L != 0L) return;
        Raid raid = ACTIVE.get(world.getRegistryKey());
        if (raid != null) {
            run(world, raid);
            return;
        }
        KikoConfig cfg = KikoConfig.get();
        topUpGuards(world);
        if (!cfg.townRaids || world.getDifficulty() == Difficulty.PEACEFUL) return;
        long tod = world.getTimeOfDay() % 24000L;
        if (tod < 2500 || tod > 9000) return;
        long day = TownCalendar.day(world);
        Long seen = SCANNED.get(world.getRegistryKey());
        if (seen != null && seen == day) return;
        SCANNED.put(world.getRegistryKey(), day);
        for (ServerPlayerEntity p : world.getPlayers()) {
            BlockPos bell = TownMarket.findBell(world, p.getBlockPos());
            if (bell == null || bell.getSquaredDistance(p.getPos()) > 90 * 90) continue;
            Long last = LAST.get(bell.asLong());
            if (last == null) {
                LAST.put(bell.asLong(), day);
                continue;
            }
            if (day - last < cfg.townRaidCooldownDays) continue;
            if (world.getRandom().nextInt(100) >= cfg.townRaidChance) continue;
            if (start(world, bell, false) == null) break;
        }
    }

    private static final Map<RegistryKey<World>, Long> GUARDED = new HashMap<>();

    /** Los pueblos que salieron sin guardias (generados antes de la 3.8) reciben dos junto a la campana. */
    private static void topUpGuards(ServerWorld world) {
        long tod = world.getTimeOfDay() % 24000L;
        if (tod < 1000 || tod > 9000 || world.getPlayers().isEmpty()) return;
        long day = TownCalendar.day(world);
        Long seen = GUARDED.get(world.getRegistryKey());
        if (seen != null && seen == day) return;
        GUARDED.put(world.getRegistryKey(), day);
        for (ServerPlayerEntity p : world.getPlayers()) {
            BlockPos bell = TownMarket.findBell(world, p.getBlockPos());
            if (bell == null || bell.getSquaredDistance(p.getPos()) > 90 * 90) continue;
            addGuards(world, bell, false);
        }
    }

    /** Pone dos guardias junto a la campana si el pueblo no tiene. Con force pone los dos aunque ya haya. */
    public static int addGuards(ServerWorld world, BlockPos bell, boolean force) {
        Box area = new Box(bell).expand(96);
        if (!force) {
            int guards = world.getEntitiesByClass(TownsfolkEntity.class, area, t -> t.isAlive() && t.isGuard()).size();
            int folks = world.getEntitiesByClass(TownsfolkEntity.class, area, t -> t.isAlive() && !t.isTownKid()).size();
            if (guards > 0 || folks < 3) return 0;
        }
        int made = 0;
        int[][] spots = {{2, 1}, {-2, -1}};
        for (int[] o : spots) {
            int x = bell.getX() + o[0], z = bell.getZ() + o[1];
            int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
            if (TownsfolkEntity.spawnGuard(world, new BlockPos(x, y, z))) made++;
        }
        return made;
    }

    // ------------------------------------------------------------------
    // Empezar
    // ------------------------------------------------------------------
    /** null si arrancó, o la llave del mensaje de por qué no. */
    @Nullable
    public static String start(ServerWorld world, BlockPos bell, boolean force) {
        if (ACTIVE.containsKey(world.getRegistryKey())) return "command.kiko.raid_busy";
        if (world.getDifficulty() == Difficulty.PEACEFUL) return "command.kiko.raid_peaceful";
        Box area = new Box(bell).expand(96);
        int folks = world.getEntitiesByClass(TownsfolkEntity.class, area, t -> t.isAlive() && !t.isTownKid()).size();
        int guards = world.getEntitiesByClass(TownsfolkEntity.class, area, t -> t.isAlive() && t.isGuard()).size();
        if (!force && folks < 4) return "command.kiko.town_few";

        double mult = switch (world.getDifficulty()) {
            case EASY -> 0.7;
            case HARD -> 1.4;
            default -> 1.0;
        };
        int cap = Math.max(3, KikoConfig.get().townRaidMax);
        int n = (int) Math.round((3 + folks / 6.0 + guards / 2.0) * mult);
        n = Math.max(3, Math.min(cap, n));

        Raid raid = new Raid();
        raid.bell = bell.toImmutable();
        ACTIVE.put(world.getRegistryKey(), raid);   // antes de crearlos, para que no los borre el evento de carga
        Random r = world.getRandom();
        double a0 = r.nextDouble() * Math.PI * 2;
        int spawned = 0;
        for (int i = 0; i < n + 6 && spawned < n; i++) {
            double a = a0 + (r.nextDouble() - 0.5) * 1.2;
            int rad = 36 + r.nextInt(12);
            int x = bell.getX() + (int) Math.round(Math.cos(a) * rad);
            int z = bell.getZ() + (int) Math.round(Math.sin(a) * rad);
            if (!world.isChunkLoaded(x >> 4, z >> 4)) continue;
            int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos pos = new BlockPos(x, y, z);
            if (!world.getBlockState(pos.down()).isSolidBlock(world, pos.down()) || !world.getFluidState(pos).isEmpty()) continue;
            EntityType<? extends MobEntity> type;
            if (spawned == 0 && folks >= 15 && n >= 6 && r.nextInt(100) < 40) type = EntityType.RAVAGER;
            else type = r.nextInt(100) < 70 ? EntityType.PILLAGER : EntityType.VINDICATOR;
            MobEntity mob = type.spawn(world, pos, SpawnReason.EVENT);
            if (mob == null) continue;
            mob.addCommandTag(TAG);
            mob.setPersistent();
            mob.setGlowing(true);
            raid.raiders.add(mob.getUuid());
            spawned++;
        }
        if (spawned == 0) {
            ACTIVE.remove(world.getRegistryKey());
            return "command.kiko.town_nosite";
        }
        raid.total = spawned;
        LAST.put(bell.asLong(), TownCalendar.day(world));
        for (int i = 0; i < 4; i++) {
            world.playSound(null, bell, SoundEvents.BLOCK_BELL_USE, SoundCategory.BLOCKS, 3.0f, 0.8f - i * 0.05f);
        }
        tell(world, bell, Text.translatable("message.kiko.raid_start"));
        return null;
    }

    // ------------------------------------------------------------------
    // Durante el ataque
    // ------------------------------------------------------------------
    private static void run(ServerWorld world, Raid raid) {
        raid.ticks += 20;
        Iterator<UUID> it = raid.raiders.iterator();
        List<MobEntity> alive = new ArrayList<>();
        while (it.hasNext()) {
            Entity e = world.getEntity(it.next());
            if (e instanceof MobEntity m && m.isAlive()) alive.add(m);
            else it.remove();
        }
        if (alive.isEmpty()) {
            win(world, raid);
            return;
        }
        if (raid.ticks > TIMEOUT) {
            for (MobEntity m : alive) m.discard();
            ACTIVE.remove(world.getRegistryKey());
            tell(world, raid.bell, Text.translatable("message.kiko.raid_retreat"));
            return;
        }
        // Se preocupan de pelear contra los guardias, los vecinos grandes y tú (nunca contra niños)
        Box area = new Box(raid.bell).expand(110);
        List<TownsfolkEntity> folks = world.getEntitiesByClass(TownsfolkEntity.class, area, t -> t.isAlive() && !t.isTownKid());
        for (MobEntity m : alive) {
            if (m.getTarget() != null && m.getTarget().isAlive() && m.squaredDistanceTo(m.getTarget()) < 28 * 28) continue;
            net.minecraft.entity.LivingEntity best = null;
            double bestD = 26 * 26;
            for (TownsfolkEntity t : folks) {
                // Los guardias primero, los que están escondidos adentro no se ven
                if (!t.isGuard() && hiding(t) && t.squaredDistanceTo(t.getHome() == null ? t.getPos() : net.minecraft.util.math.Vec3d.ofCenter(t.getHome())) < 9) continue;
                double d = m.squaredDistanceTo(t) - (t.isGuard() ? 64 : 0);
                if (d < bestD) {
                    bestD = d;
                    best = t;
                }
            }
            for (PlayerEntity p : world.getPlayers()) {
                if (p.isCreative() || p.isSpectator()) continue;
                double d = m.squaredDistanceTo(p);
                if (d < bestD) {
                    bestD = d;
                    best = p;
                }
            }
            if (best != null) {
                m.setTarget(best);
            } else if (m.getNavigation().isIdle()) {
                // Si no hay a quién atacar, caminan hacia la campana
                m.getNavigation().startMovingTo(raid.bell.getX() + 0.5, raid.bell.getY(), raid.bell.getZ() + 0.5, 1.0);
            }
        }
    }

    private static void win(ServerWorld world, Raid raid) {
        ACTIVE.remove(world.getRegistryKey());
        TownFestival.victory(world, raid.bell);
        List<ServerPlayerEntity> near = new ArrayList<>();
        for (ServerPlayerEntity p : world.getPlayers()) {
            if (p.squaredDistanceTo(raid.bell.getX(), raid.bell.getY(), raid.bell.getZ()) < 90 * 90) near.add(p);
        }
        if (near.isEmpty()) return;
        int reward = 3 + Math.min(10, raid.total);
        for (ServerPlayerEntity p : near) {
            p.giveItemStack(new ItemStack(Items.EMERALD, reward));
            p.sendMessage(Text.translatable("message.kiko.raid_won", reward), false);
        }
        for (int i = 0; i < 3; i++) {
            world.playSound(null, raid.bell, SoundEvents.BLOCK_BELL_USE, SoundCategory.BLOCKS, 2.0f, 1.2f + i * 0.1f);
        }
    }

    private static void tell(ServerWorld world, BlockPos at, Text text) {
        for (ServerPlayerEntity p : world.getPlayers()) {
            if (p.squaredDistanceTo(at.getX(), at.getY(), at.getZ()) < 150 * 150) p.sendMessage(text, false);
        }
    }

    // ------------------------------------------------------------------
    // Esconderse
    // ------------------------------------------------------------------
    public static class HideGoal extends Goal {
        private final TownsfolkEntity npc;
        private int cd;

        public HideGoal(TownsfolkEntity npc) {
            this.npc = npc;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
        }

        @Override
        public boolean canStart() {
            return hiding(npc) && npc.getHome() != null && !npc.isSleeping();
        }

        @Override
        public boolean shouldContinue() {
            return hiding(npc);
        }

        @Override
        public void start() {
            cd = 0;
        }

        @Override
        public void tick() {
            if (--cd > 0) return;
            cd = 20;
            BlockPos h = npc.getHome();
            if (h == null) return;
            if (npc.squaredDistanceTo(h.getX() + 0.5, h.getY(), h.getZ() + 0.5) > 4) {
                npc.getNavigation().startMovingTo(h.getX() + 0.5, h.getY(), h.getZ() + 0.5, 1.25);
            } else {
                npc.getNavigation().stop();
            }
        }
    }

    // ------------------------------------------------------------------
    // Comandos de prueba
    // ------------------------------------------------------------------
    public static Text cmdStart(ServerWorld world, BlockPos from) {
        BlockPos bell = TownMarket.findBell(world, from);
        if (bell == null) return Text.translatable("command.kiko.town_nobell");
        String err = start(world, bell, true);
        return err == null ? Text.translatable("command.kiko.raid_started") : Text.translatable(err);
    }

    public static Text cmdGuards(ServerWorld world, BlockPos from) {
        BlockPos bell = TownMarket.findBell(world, from);
        if (bell == null) return Text.translatable("command.kiko.town_nobell");
        return Text.translatable("command.kiko.guards_added", addGuards(world, bell, true));
    }

    public static Text cmdStop(ServerWorld world) {
        Raid raid = ACTIVE.remove(world.getRegistryKey());
        if (raid == null) return Text.translatable("command.kiko.raid_none");
        for (UUID id : raid.raiders) {
            Entity e = world.getEntity(id);
            if (e != null) e.discard();
        }
        return Text.translatable("command.kiko.raid_stopped");
    }
}
