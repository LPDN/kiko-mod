package com.jaciel.kiko.town;

import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.family.KikoChildEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Cumpleaños (v3.9). Cada vecino y cada compañera cumple un día de un año de 56 días de Minecraft (sale de su UUID).
 *  - En la mañana aviso de quién cumple, y anda con un globito todo el día
 *  - Si lo saludas te lo dice, y los regalos ese día valen el doble (el pastel es el mejor regalo)
 *  - En la tarde los vecinos se juntan en la plaza, le ponen pastel con velitas y le aplauden
 *  - Si es tu compañera (eres su mejor amigo) y no la felicitaste en todo el día, al otro día amanece triste
 */
public final class Birthdays {
    private Birthdays() {}

    public static final int YEAR = 56;
    private static final Vector3f[] BALLOONS = {new Vector3f(0.95f, 0.25f, 0.3f), new Vector3f(0.3f, 0.6f, 1f),
            new Vector3f(1f, 0.85f, 0.2f), new Vector3f(0.55f, 0.9f, 0.35f), new Vector3f(0.85f, 0.4f, 0.95f)};

    public static int dayOf(UUID id) {
        long h = id.getMostSignificantBits() * 31L + id.getLeastSignificantBits();
        return (int) Math.floorMod(h ^ (h >>> 29), (long) YEAR);
    }

    /** Para probar: cumpleaños puestos a mano con /kiko birthday force (no se guardan). */
    private static final Map<UUID, Long> FORCED = new HashMap<>();

    public static void force(KikoEntity e) {
        FORCED.put(e.getUuid(), TownCalendar.day(e.getWorld()));
    }

    public static boolean isBirthday(KikoEntity e) {
        if (!KikoConfig.get().birthdays) return false;
        Long f = FORCED.get(e.getUuid());
        if (f != null && f == TownCalendar.day(e.getWorld())) return true;
        return Math.floorMod(TownCalendar.day(e.getWorld()), (long) YEAR) == dayOf(e.getUuid());
    }

    /** Días que faltan para su cumpleaños (0 si es hoy). */
    public static int daysUntil(KikoEntity e) {
        int today = (int) Math.floorMod(TownCalendar.day(e.getWorld()), (long) YEAR);
        return Math.floorMod(dayOf(e.getUuid()) - today, YEAR);
    }

    /** Lo felicitó su mejor amigo hoy (lo guarda la entidad para el día siguiente). */
    public static void greeted(KikoEntity e, net.minecraft.entity.player.PlayerEntity p) {
        if (p.getUuid().equals(e.getFriendUuid())) e.setBirthdayGreeted(TownCalendar.day(e.getWorld()));
    }

    // ------------------------------------------------------------------
    // Tick
    // ------------------------------------------------------------------
    private static final Map<RegistryKey<World>, Long> ANNOUNCED = new HashMap<>();
    private static final Map<RegistryKey<World>, Party> PARTIES = new HashMap<>();

    static final class Party {
        BlockPos bell = BlockPos.ORIGIN;
        UUID celebrant;
        @Nullable
        BlockPos cake;
        int stage;
        int ticks;
        long day;
    }

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(Birthdays::tick);
    }

    private static void tick(ServerWorld world) {
        if (!KikoConfig.get().birthdays || world.getPlayers().isEmpty()) return;
        long time = world.getTime();
        if (time % 40L == 7L) balloons(world);
        if (time % 20L != 3L) return;
        long tod = world.getTimeOfDay() % 24000L;
        long day = TownCalendar.day(world);
        if (tod >= 1000 && tod < 9000) {
            Long a = ANNOUNCED.get(world.getRegistryKey());
            if (a == null || a != day) {
                ANNOUNCED.put(world.getRegistryKey(), day);
                announce(world, day);
            }
        }
        party(world, tod, day);
    }

    private static void balloons(ServerWorld world) {
        for (ServerPlayerEntity p : world.getPlayers()) {
            for (KikoEntity e : world.getEntitiesByClass(KikoEntity.class, p.getBoundingBox().expand(40), k -> k.isAlive() && !k.isSleeping())) {
                if (!isBirthday(e)) continue;
                Vector3f col = BALLOONS[Math.floorMod(e.getUuid().hashCode(), BALLOONS.length)];
                double bob = Math.sin((world.getTime() + e.getId()) * 0.1) * 0.08;
                double y = e.getY() + e.getHeight() + 0.9 + bob;
                world.spawnParticles(new DustParticleEffect(col, 1.8f), e.getX() + 0.25, y, e.getZ(), 4, 0.06, 0.08, 0.06, 0.0);
                world.spawnParticles(new DustParticleEffect(new Vector3f(0.9f, 0.9f, 0.9f), 0.5f), e.getX() + 0.2, y - 0.5, e.getZ(), 1, 0.0, 0.15, 0.0, 0.0);
            }
        }
    }

    private static void announce(ServerWorld world, long day) {
        for (ServerPlayerEntity p : world.getPlayers()) {
            List<KikoEntity> near = world.getEntitiesByClass(KikoEntity.class, p.getBoundingBox().expand(96), KikoEntity::isAlive);
            List<Text> names = new ArrayList<>();
            for (KikoEntity e : near) {
                // Tu compañera que no felicitaste ayer
                if (!(e instanceof TownsfolkEntity) && !(e instanceof KikoChildEntity) && p.getUuid().equals(e.getFriendUuid())
                        && Math.floorMod(day - 1, (long) YEAR) == dayOf(e.getUuid()) && e.getBirthdayGreeted() != day - 1) {
                    e.getAffection().add(p.getUuid(), -6f, world.getTime());
                    e.setExpression(KikoExpression.SAD, 400);
                    e.playAnimation(KikoAnimation.CRY);
                    p.sendMessage(Text.translatable("message.kiko.birthday_forgot", e.getDisplayName()).formatted(Formatting.GRAY), false);
                }
                if (!isBirthday(e)) continue;
                if (!(e instanceof TownsfolkEntity) && p.getUuid().equals(e.getFriendUuid())) {
                    p.sendMessage(Text.translatable("message.kiko.birthday_companion", e.getDisplayName()).formatted(Formatting.LIGHT_PURPLE), false);
                } else if (names.size() < 4) {
                    names.add(e.getDisplayName());
                }
            }
            if (!names.isEmpty()) {
                MutableText list = Text.empty();
                for (int i = 0; i < names.size(); i++) {
                    if (i > 0) list.append(Text.literal(", "));
                    list.append(names.get(i).copy().formatted(Formatting.GOLD));
                }
                p.sendMessage(Text.translatable("message.kiko.birthday_today", list).formatted(Formatting.YELLOW), false);
            }
        }
    }

    // ------------------------------------------------------------------
    // Fiesta en la plaza
    // ------------------------------------------------------------------
    private static void party(ServerWorld world, long tod, long day) {
        Party party = PARTIES.get(world.getRegistryKey());
        if (party != null && (party.day != day || tod >= 11800)) {
            endParty(world, party);
            return;
        }
        if (party == null) {
            if (tod < 9300 || tod >= 10800) return;
            for (ServerPlayerEntity p : world.getPlayers()) {
                BlockPos bell = TownMarket.findBell(world, p.getBlockPos());
                if (bell == null || bell.getSquaredDistance(p.getPos()) > 96 * 96) continue;
                for (TownsfolkEntity t : world.getEntitiesByClass(TownsfolkEntity.class, new Box(bell).expand(80),
                        t -> t.isAlive() && t.getStage() != KikoChildEntity.BABY)) {
                    if (!isBirthday(t)) continue;
                    Party np = new Party();
                    np.bell = bell.toImmutable();
                    np.celebrant = t.getUuid();
                    np.day = day;
                    PARTIES.put(world.getRegistryKey(), np);
                    tell(world, bell, Text.translatable("message.kiko.birthday_party", t.getDisplayName()).formatted(Formatting.YELLOW));
                    return;
                }
            }
            return;
        }
        party.ticks += 20;
        if (party.stage == 0 && tod >= 10200) {
            BlockPos spot = cakeSpot(world, party.bell);
            if (spot != null) {
                world.setBlockState(spot, Blocks.CANDLE_CAKE.getDefaultState().with(CandleCakeBlock.LIT, true), 3);
                party.cake = spot;
            }
            if (world.getEntity(party.celebrant) instanceof TownsfolkEntity c) {
                if (spot != null) c.getLookControl().lookAt(spot.getX() + 0.5, spot.getY() + 0.5, spot.getZ() + 0.5);
                c.playAnimation(KikoAnimation.BLOW);
            }
            party.stage = 1;
            party.ticks = 0;
        } else if (party.stage == 1 && party.ticks >= 60) {
            if (party.cake != null && world.getBlockState(party.cake).isOf(Blocks.CANDLE_CAKE)) {
                world.setBlockState(party.cake, world.getBlockState(party.cake).with(CandleCakeBlock.LIT, false), 3);
                world.playSound(null, party.cake, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);
            }
            for (TownsfolkEntity t : world.getEntitiesByClass(TownsfolkEntity.class, new Box(party.bell).expand(14), TownsfolkEntity::isAlive)) {
                if (t.getUuid().equals(party.celebrant)) {
                    t.playAnimation(KikoAnimation.HAPPY);
                    t.setExpression(KikoExpression.LAUGH, 100);
                } else {
                    t.playAnimation(t.getRandom().nextBoolean() ? KikoAnimation.CLAP : KikoAnimation.CHEER);
                }
            }
            world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, party.bell.getX() + 0.5, party.bell.getY() + 1.5, party.bell.getZ() + 0.5,
                    20, 3, 1, 3, 0);
            world.playSound(null, party.bell, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.NEUTRAL, 1f, 1.2f);
            party.stage = 2;
        }
    }

    private static void endParty(ServerWorld world, Party party) {
        PARTIES.remove(world.getRegistryKey());
        if (party.cake != null && world.isChunkLoaded(party.cake) && world.getBlockState(party.cake).isOf(Blocks.CANDLE_CAKE)) {
            world.setBlockState(party.cake, Blocks.AIR.getDefaultState(), 3);
        }
    }

    @Nullable
    private static BlockPos cakeSpot(ServerWorld world, BlockPos bell) {
        for (BlockPos p : BlockPos.iterateOutwards(bell, 3, 2, 3)) {
            if (p.getSquaredDistance(bell) < 4) continue;
            BlockPos below = p.down();
            if (world.getBlockState(p).isAir() && world.getBlockState(below).isSideSolidFullSquare(world, below, net.minecraft.util.math.Direction.UP)
                    && world.getBlockState(p.up()).isAir()) return p.toImmutable();
        }
        return null;
    }

    private static void tell(ServerWorld world, BlockPos at, Text text) {
        for (ServerPlayerEntity p : world.getPlayers()) {
            if (p.squaredDistanceTo(at.getX(), at.getY(), at.getZ()) < 110 * 110) p.sendMessage(text, false);
        }
    }

    @Nullable
    static Party partyNear(TownsfolkEntity npc) {
        Party p = PARTIES.get(npc.getWorld().getRegistryKey());
        return p != null && p.stage < 2 && p.bell.getSquaredDistance(npc.getPos()) < 80 * 80 ? p : null;
    }

    /** Los vecinos se juntan alrededor de la campana (y el festejado junto al pastel). */
    public static class GatherGoal extends Goal {
        private final TownsfolkEntity npc;
        @Nullable
        private Party party;
        private int repath;

        public GatherGoal(TownsfolkEntity npc) {
            this.npc = npc;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            if (npc.isSleeping() || npc.isBeingAttended() || npc.getTarget() != null || npc.getStage() == KikoChildEntity.BABY) return false;
            party = partyNear(npc);
            return party != null;
        }

        @Override
        public boolean shouldContinue() {
            return party != null && partyNear(npc) == party && !npc.isSleeping() && npc.getTarget() == null;
        }

        @Override
        public void tick() {
            if (party == null) return;
            boolean me = npc.getUuid().equals(party.celebrant);
            BlockPos c = party.cake != null ? party.cake : party.bell;
            double d = npc.squaredDistanceTo(c.getX() + 0.5, c.getY(), c.getZ() + 0.5);
            double want = me ? 2.5 : 4.5;
            if (d > want * want && --repath <= 0) {
                repath = 30;
                double a = npc.getRandom().nextDouble() * Math.PI * 2;
                double r = me ? 1.5 : 3 + npc.getRandom().nextDouble() * 1.5;
                npc.getNavigation().startMovingTo(c.getX() + 0.5 + Math.cos(a) * r, c.getY(), c.getZ() + 0.5 + Math.sin(a) * r, 0.8);
            } else if (d <= want * want) {
                npc.getNavigation().stop();
            }
            if (!me && npc.getWorld() instanceof ServerWorld sw && sw.getEntity(party.celebrant) instanceof TownsfolkEntity cel) {
                npc.getLookControl().lookAt(cel);
            } else {
                npc.getLookControl().lookAt(c.getX() + 0.5, c.getY() + 0.5, c.getZ() + 0.5);
            }
        }
    }
}
