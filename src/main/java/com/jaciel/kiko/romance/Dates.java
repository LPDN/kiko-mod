package com.jaciel.kiko.romance;

import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.animation.KikoPose;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoCharacter;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import com.jaciel.kiko.town.Seats;
import com.jaciel.kiko.town.TownCalendar;
import com.jaciel.kiko.town.TownFestival;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Citas con tus compañeras (v3.9). Con cariño alto, en su menú sale "Invitar a salir". Escoges el lugar
 * en el chat y ella te sigue de la mano. Al llegar se quedan un rato (bailan en la feria, se sientan a ver
 * el paisaje en el mirador o en el muelle, cenan juntos) y al final sube el cariño (el doble si es su lugar favorito).
 */
public final class Dates {
    private Dates() {}

    public enum Place {
        FERIA("feria"), MIRADOR("mirador"), MUELLE("muelle"), CENA("cena");

        public final String id;

        Place(String id) {
            this.id = id;
        }

        @Nullable
        public static Place byId(String id) {
            for (Place p : values()) if (p.id.equals(id)) return p;
            return null;
        }
    }

    /** El lugar favorito de cada quien según su forma de ser. */
    public static Place favorite(KikoCharacter c) {
        return switch (c) {
            case KIKO, MARU, TONO, EMI -> Place.FERIA;
            case LUMI, DANTE -> Place.MIRADOR;
            case NADI, RINA, LEO -> Place.MUELLE;
            case TAMI, IVAN, BETO -> Place.CENA;
        };
    }

    static final class Date {
        UUID player, kiko;
        Place place;
        int ticks, progress, far;
        boolean arrived;
    }

    private static final Map<UUID, Date> ACTIVE = new HashMap<>();
    private static final Map<UUID, UUID> PENDING = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(Dates::tick);
    }

    public static boolean canDate(KikoEntity k, ServerPlayerEntity p) {
        return KikoConfig.get().dates && !k.isTownsfolk() && !k.isMinor() && !k.isKid() && !k.isAway()
                && k.getAffection().get(p.getUuid()) >= KikoConfig.get().dateAffection;
    }

    @Nullable
    public static KikoEntity dateOf(ServerPlayerEntity p) {
        Date d = ACTIVE.get(p.getUuid());
        return d != null && p.getServerWorld().getEntity(d.kiko) instanceof KikoEntity k ? k : null;
    }

    /** Del menú: ella pregunta a dónde y te salen los lugares para escoger en el chat. */
    public static void invite(ServerPlayerEntity p, KikoEntity k) {
        if (!canDate(k, p)) {
            k.setExpression(KikoExpression.NERVOUS, 60);
            KikoInteractionHandler.say(p, k, "dialogue.kiko.date.not_yet", 2);
            return;
        }
        if (ACTIVE.containsKey(p.getUuid())) {
            p.sendMessage(Text.translatable("message.kiko.date_busy").formatted(Formatting.GRAY), false);
            return;
        }
        PENDING.put(p.getUuid(), k.getUuid());
        k.setExpression(KikoExpression.LOVESTRUCK, 100);
        k.playAnimation(KikoAnimation.HAPPY);
        KikoInteractionHandler.say(p, k, "dialogue.kiko.date.ask", 3);
        MutableText line = Text.literal("  ");
        for (Place pl : Place.values()) {
            Text label = Text.literal("[").append(Text.translatable("date.kiko.place." + pl.id)).append(Text.literal("]"));
            line.append(label.copy().setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withBold(true)
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kiko date " + pl.id))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("date.kiko.hint." + pl.id)))));
            line.append(Text.literal("  "));
        }
        p.sendMessage(line, false);
    }

    /** /kiko date <lugar> */
    public static Text start(ServerPlayerEntity p, String placeId) {
        if (placeId.equals("cancel")) {
            Date d = ACTIVE.remove(p.getUuid());
            if (d == null) return Text.translatable("message.kiko.date_none");
            if (p.getServerWorld().getEntity(d.kiko) instanceof KikoEntity k) end(k);
            return Text.translatable("message.kiko.date_cancel");
        }
        Place place = Place.byId(placeId);
        UUID kid = PENDING.remove(p.getUuid());
        if (place == null || kid == null || !(p.getServerWorld().getEntity(kid) instanceof KikoEntity k) || !canDate(k, p)) {
            return Text.translatable("message.kiko.date_none");
        }
        Date d = new Date();
        d.player = p.getUuid();
        d.kiko = k.getUuid();
        d.place = place;
        ACTIVE.put(p.getUuid(), d);
        k.setExpression(KikoExpression.LOVESTRUCK, 120);
        k.playAnimation(KikoAnimation.CHEER);
        KikoInteractionHandler.say(p, k, favorite(k.getCharacter()) == place ? "dialogue.kiko.date.start_fav" : "dialogue.kiko.date.start." + place.id, 1);
        return Text.translatable("message.kiko.date_started", k.getDisplayName(), Text.translatable("date.kiko.place." + place.id));
    }

    // ------------------------------------------------------------------
    // Durante la cita
    // ------------------------------------------------------------------
    private static void tick(MinecraftServer server) {
        if (ACTIVE.isEmpty() || server.getTicks() % 5 != 0) return;
        Iterator<Map.Entry<UUID, Date>> it = ACTIVE.entrySet().iterator();
        while (it.hasNext()) {
            Date d = it.next().getValue();
            ServerPlayerEntity p = server.getPlayerManager().getPlayer(d.player);
            KikoEntity k = p == null ? null : p.getServerWorld().getEntity(d.kiko) instanceof KikoEntity kk ? kk : null;
            if (p == null || k == null || !k.isAlive() || k.isSleeping() || k.getTarget() != null) {
                if (k != null) end(k);
                if (p != null) p.sendMessage(Text.translatable("message.kiko.date_cancel").formatted(Formatting.GRAY), false);
                it.remove();
                continue;
            }
            d.ticks += 5;
            double dist = k.squaredDistanceTo(p);
            if (dist > 40 * 40) d.far += 5;
            else d.far = 0;
            if (d.far > 400 || d.ticks > 24000) {
                end(k);
                KikoInteractionHandler.say(p, k, "dialogue.kiko.date.cancel", 1);
                k.setExpression(KikoExpression.SAD, 120);
                it.remove();
                continue;
            }
            ServerWorld w = p.getServerWorld();
            boolean there = atPlace(w, p, d.place) && dist < 5 * 5;
            if (there && !d.arrived) {
                d.arrived = true;
                KikoInteractionHandler.say(p, k, "dialogue.kiko.date.arrive." + d.place.id, 1);
            }
            if (there) {
                d.progress += 5;
                behave(w, p, k, d);
                if (d.progress % 100 == 0) k.spawnParticles(ParticleTypes.HEART, 1);
                if (d.progress >= 1200) {
                    finish(w, p, k, d);
                    it.remove();
                }
                continue;
            }
            // De camino: te sigue de la mano
            if (k.getKikoPose().seated() || k.getKikoPose() == KikoPose.PAIR_DANCE) {
                BlockPos at = k.getBlockPos();
                k.setKikoPose(KikoPose.NONE);
                if (k.getWorld().getBlockState(at).getBlock() instanceof net.minecraft.block.StairsBlock) Seats.stand(k, at);
            }
            if (dist > 2.6 * 2.6) {
                k.getNavigation().startMovingTo(p, dist > 10 * 10 ? 1.25 : 1.05);
                k.setKikoPose(dist < 5 * 5 ? KikoPose.HOLD_HAND : KikoPose.NONE);
            } else {
                k.setKikoPose(KikoPose.HOLD_HAND);
                k.getLookControl().lookAt(p);
            }
        }
    }

    /** ¿Ya llegaron al lugar? */
    static boolean atPlace(ServerWorld w, ServerPlayerEntity p, Place place) {
        BlockPos pos = p.getBlockPos();
        return switch (place) {
            case FERIA -> TownFestival.activeNear(w, pos);
            case MIRADOR -> pos.getY() >= w.getSeaLevel() + 28 && w.isSkyVisible(pos.up());
            case MUELLE -> {
                if (!w.isSkyVisible(pos.up())) yield false;
                for (BlockPos q : BlockPos.iterate(pos.add(-4, -2, -4), pos.add(4, 0, 4))) {
                    if (w.getFluidState(q).isStill() && w.getBlockState(q).isOf(Blocks.WATER)) yield true;
                }
                yield false;
            }
            case CENA -> {
                long tod = w.getTimeOfDay() % 24000L;
                if (tod < 11000 || tod > 15000) yield false;
                for (BlockPos q : BlockPos.iterate(pos.add(-6, -2, -6), pos.add(6, 2, 6))) {
                    BlockState s = w.getBlockState(q);
                    if (s.isOf(Blocks.SMOKER) || s.isOf(Blocks.FURNACE) || s.isIn(BlockTags.CAMPFIRES)) yield true;
                }
                yield false;
            }
        };
    }

    private static void behave(ServerWorld w, ServerPlayerEntity p, KikoEntity k, Date d) {
        k.getNavigation().stop();
        k.getLookControl().lookAt(p);
        switch (d.place) {
            case FERIA -> k.setKikoPose(KikoPose.PAIR_DANCE);
            case MIRADOR, MUELLE -> {
                // Se sienta junto a ti y se recarga
                if (!k.getKikoPose().seated()) k.setKikoPose(KikoPose.LEAN);
            }
            case CENA -> {
                if (k.getKikoPose() != KikoPose.SIT) {
                    for (BlockPos q : BlockPos.iterateOutwards(p.getBlockPos(), 4, 1, 4)) {
                        if (Seats.isChair(w.getBlockState(q)) && !Seats.taken(w, q, k)) {
                            Seats.sit(k, q.toImmutable());
                            break;
                        }
                    }
                }
                if (d.progress % 200 == 0) k.playAnimation(KikoAnimation.TOAST);
            }
        }
        if (d.progress % 300 == 0) {
            k.setExpression(KikoExpression.LOVESTRUCK, 200);
            k.talk(40);
        }
    }

    private static void finish(ServerWorld w, ServerPlayerEntity p, KikoEntity k, Date d) {
        boolean fav = favorite(k.getCharacter()) == d.place;
        float amount = KikoConfig.get().dateAffectionGain * (fav ? 2f : 1f);
        k.getAffection().add(p.getUuid(), amount, w.getTime());
        end(k);
        k.setExpression(KikoExpression.LOVESTRUCK, 300);
        k.playAnimation(KikoAnimation.HUG);
        k.spawnParticles(ParticleTypes.HEART, fav ? 14 : 8);
        KikoInteractionHandler.say(p, k, fav ? "dialogue.kiko.date.end_fav" : "dialogue.kiko.date.end", fav ? 1 : 3);
        k.getDateLog().add(TownCalendar.day(w) + ":" + d.place.id);
        while (k.getDateLog().size() > 60) k.getDateLog().remove(0);
        p.sendMessage(Text.translatable(fav ? "message.kiko.date_done_fav" : "message.kiko.date_done", k.getDisplayName()).formatted(Formatting.LIGHT_PURPLE), false);
        KikoInteractionHandler.showStatus(p, k);
    }

    private static void end(KikoEntity k) {
        KikoPose pose = k.getKikoPose();
        if (pose == KikoPose.SIT) Seats.stand(k, k.getBlockPos());
        else if (pose == KikoPose.HOLD_HAND || pose == KikoPose.LEAN || pose == KikoPose.PAIR_DANCE) k.setKikoPose(KikoPose.NONE);
    }
}
