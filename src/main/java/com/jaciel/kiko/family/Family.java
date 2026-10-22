package com.jaciel.kiko.family;

import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoCharacter;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import com.jaciel.kiko.registry.ModEntities;
import com.jaciel.kiko.romance.MarriageState;
import com.jaciel.kiko.romance.Romance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Formar una familia (v3.1).
 *  1. En el menú de tu esposa, "Formar una familia". Necesita una cuna a 16 bloques de ella o de su casa
 *  2. A la mañana siguiente aparece el bebé acostadito en la cuna y te pregunta cómo se va a llamar
 *  3. Crece solo (ver KikoChildEntity). Entre un bebé y otro hay que esperar unos días (config)
 */
public final class Family {
    private Family() {}

    private static final String[] BOYS = {"Mateo", "Santiago", "Leo", "Diego", "Emiliano", "Iker", "Daniel", "Tomás",
            "Gael", "Julián", "Andrés", "Nico", "Bruno", "Elías"};
    private static final String[] GIRLS = {"Sofía", "Valentina", "Camila", "Regina", "Ximena", "Renata", "Luna", "Mía",
            "Victoria", "Ana", "Elena", "Paula", "Abril", "Frida"};

    /** El botón del menú. */
    public static void request(ServerPlayerEntity player, KikoEntity wife) {
        KikoConfig cfg = KikoConfig.get();
        ServerWorld world = player.getServerWorld();
        if (!cfg.familyEnabled || wife.isKid()) return;
        if (wife.getRelation() != Romance.WIFE || !player.getUuid().equals(wife.getPartnerUuid())) {
            player.sendMessage(Text.translatable("message.kiko.family_need_wife").formatted(Formatting.GRAY), true);
            return;
        }
        if (wife.getFamilyRequestDay() >= 0) {
            player.sendMessage(Text.translatable("message.kiko.family_pending").formatted(Formatting.LIGHT_PURPLE), true);
            return;
        }
        if (FamilyState.get(world.getServer()).countWithMother(player.getUuid(), wife.getUuid()) >= cfg.maxKidsPerWife) {
            player.sendMessage(Text.translatable("message.kiko.family_max", cfg.maxKidsPerWife).formatted(Formatting.GRAY), false);
            return;
        }
        long wait = wife.getLastBirthTime() + cfg.familyCooldownDays * 24000L - world.getTimeOfDay();
        if (wife.getLastBirthTime() > world.getTimeOfDay()) wait = 0;   // regresaron el reloj con /time set
        if (wife.getLastBirthTime() > 0 && wait > 0) {
            player.sendMessage(Text.translatable("message.kiko.family_wait", (wait + 23999) / 24000).formatted(Formatting.GRAY), false);
            return;
        }
        BlockPos crib = findCrib(world, wife);
        if (crib == null) {
            KikoInteractionHandler.say(player, wife, "dialogue.kiko.family_no_crib", 3);
            player.sendMessage(Text.translatable("message.kiko.family_need_crib").formatted(Formatting.GOLD), false);
            return;
        }
        wife.setFamilyRequest(world.getTimeOfDay() / 24000L, crib);
        wife.setExpression(KikoExpression.EMBARRASSED, 160);
        wife.spawnParticles(ParticleTypes.HEART, 10);
        KikoInteractionHandler.say(player, wife, "dialogue.kiko.family_yes", 3);
        player.sendMessage(Text.translatable("message.kiko.family_ok").formatted(Formatting.LIGHT_PURPLE), false);
    }

    @Nullable
    private static BlockPos findCrib(ServerWorld world, KikoEntity wife) {
        for (BlockPos base : new BlockPos[]{wife.getHome(), wife.getBlockPos()}) {
            if (base == null) continue;
            for (BlockPos p : BlockPos.iterateOutwards(base, 16, 6, 16)) {
                if (KikoChildEntity.cribFree(world, p)) return p.toImmutable();
            }
        }
        return null;
    }

    /** Cada segundo por cada esposa: si ya amaneció después de que lo decidieron, llega el bebé. */
    public static void tickWife(ServerWorld world, KikoEntity wife) {
        long day = wife.getFamilyRequestDay();
        if (day < 0) return;
        long today = world.getTimeOfDay() / 24000L;
        long timeOfDay = world.getTimeOfDay() % 24000L;
        if (today <= day || timeOfDay > 12000) return;     // en la mañana del día siguiente
        PlayerEntity dad = wife.getPartnerUuid() != null ? world.getPlayerByUuid(wife.getPartnerUuid()) : null;
        if (dad == null) return;                            // espera a que estés conectado
        BlockPos crib = wife.getFamilyCrib();
        if (crib == null || !KikoChildEntity.cribFree(world, crib)) crib = findCrib(world, wife);
        if (crib == null) return;                           // sin cuna espera (ya te avisó)
        birth(world, wife, dad, crib);
    }

    private static void birth(ServerWorld world, KikoEntity wife, PlayerEntity dad, BlockPos crib) {
        KikoChildEntity baby = ModEntities.CHILD.create(world);
        if (baby == null) return;
        boolean male = world.getRandom().nextBoolean();
        String[] names = male ? BOYS : GIRLS;
        String name = names[world.getRandom().nextInt(names.length)];
        baby.refreshPositionAndAngles(crib.getX() + 0.5, crib.getY() + 0.6, crib.getZ() + 0.5, 0f, 0f);
        baby.setup(dad, wife, crib, name, male);
        world.spawnEntity(baby);
        wife.setFamilyRequest(-1, null);
        wife.setLastBirthTime(world.getTimeOfDay());
        FamilyState.get(world.getServer()).put(dad.getUuid(), record(baby, wife.getDisplayName().getString()));

        world.spawnParticles(ParticleTypes.HEART, crib.getX() + 0.5, crib.getY() + 1.2, crib.getZ() + 0.5, 12, 0.4, 0.3, 0.4, 0);
        world.playSound(null, crib, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.NEUTRAL, 0.8f, 1.6f);
        wife.setExpression(KikoExpression.LAUGH, 200);
        KikoInteractionHandler.say(dad, wife, "dialogue.kiko.baby_born", 3);
        dad.sendMessage(Text.translatable(male ? "message.kiko.baby_born.m" : "message.kiko.baby_born.f", wife.getDisplayName())
                .formatted(Formatting.LIGHT_PURPLE), false);
        if (dad instanceof ServerPlayerEntity sp) com.jaciel.kiko.network.KikoNetworking.askKidName(sp, baby, male);
    }

    private static FamilyState.Kid record(KikoChildEntity kid, String motherName) {
        return new FamilyState.Kid(kid.getUuid(), kid.getDisplayName().getString(), kid.isMale(),
                kid.getMotherUuid(), motherName, kid.getBirthTime(), kid.getRole() == null ? "" : kid.getRole().id(),
                kid.getLearning() == null ? "" : kid.getLearning().id(), kid.learnPercent(), kid.isMaster());
    }

    /** Actualiza su registro (nombre, oficio). */
    public static void updateKid(ServerWorld world, KikoChildEntity kid) {
        if (kid.getParentUuid() == null) return;
        FamilyState fs = FamilyState.get(world.getServer());
        String motherName = "";
        for (FamilyState.Kid k : fs.kidsOf(kid.getParentUuid())) if (k.uuid().equals(kid.getUuid())) motherName = k.motherName();
        fs.put(kid.getParentUuid(), record(kid, motherName));
    }

    /** Le pones nombre (desde la pantallita que sale al nacer). */
    public static void rename(ServerPlayerEntity player, KikoChildEntity kid, String name) {
        if (!player.getUuid().equals(kid.getParentUuid())) return;
        String clean = name.strip();
        if (clean.isEmpty()) return;
        if (clean.length() > 24) clean = clean.substring(0, 24);
        kid.setCustomName(Text.literal(clean));
        updateKid(player.getServerWorld(), kid);
        player.sendMessage(Text.translatable("message.kiko.kid_named", clean).formatted(Formatting.LIGHT_PURPLE), false);
    }

    // ------------------------------------------------------------------
    // Libro de familia
    // ------------------------------------------------------------------

    public static void showBook(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        long now = world.getTimeOfDay();
        player.sendMessage(Text.translatable("book.kiko.family_title").formatted(Formatting.GOLD, Formatting.BOLD), false);
        List<MarriageState.Wife> wives = MarriageState.get(world.getServer()).wivesOf(player.getUuid());
        if (wives.isEmpty()) {
            player.sendMessage(Text.translatable("book.kiko.no_wife").formatted(Formatting.GRAY), false);
        } else {
            long today = now / 24000L;
            int period = Math.max(1, KikoConfig.get().anniversaryDays);
            for (MarriageState.Wife w : wives) {
                MutableText line = Text.literal("  ♥ ").formatted(Formatting.LIGHT_PURPLE)
                        .append(Text.translatable(KikoCharacter.byId(w.character()).isMale() ? "book.kiko.wife.m" : "book.kiko.wife", w.name()).formatted(Formatting.WHITE));
                if (w.day() >= 0 && today >= w.day()) {
                    long since = today - w.day();
                    long next = period - since % period;
                    line.append(Text.literal(" · ").formatted(Formatting.GRAY))
                            .append(Text.translatable("book.kiko.married_days", since).formatted(Formatting.GRAY));
                    if (KikoConfig.get().anniversaryEnabled) {
                        line.append(Text.literal(" · ").formatted(Formatting.GRAY))
                                .append((since > 0 && since % period == 0
                                        ? Text.translatable("book.kiko.anniversary_today", since / period)
                                        : Text.translatable("book.kiko.anniversary_next", since / period + 1, next)).formatted(Formatting.LIGHT_PURPLE));
                    }
                }
                player.sendMessage(line, false);
            }
        }
        List<FamilyState.Kid> kids = FamilyState.get(world.getServer()).kidsOf(player.getUuid());
        if (kids.isEmpty()) {
            player.sendMessage(Text.translatable("book.kiko.no_kids").formatted(Formatting.GRAY), false);
            return;
        }
        for (FamilyState.Kid k : kids) {
            long age = Math.max(0, now - k.birthTime());
            long days = age / 24000L;
            int stage = KikoChildEntity.stageFor(age);
            String g = k.male() ? "m" : "f";
            MutableText line = Text.literal("  ✿ ").formatted(Formatting.AQUA)
                    .append(Text.literal(k.name()).formatted(Formatting.YELLOW))
                    .append(Text.literal(" · ").formatted(Formatting.GRAY))
                    .append(Text.translatable("stage.kiko." + new String[]{"baby", "child", "teen", "adult"}[stage] + "." + g))
                    .append(Text.literal(" · ").formatted(Formatting.GRAY))
                    .append(Text.translatable("book.kiko.days", days))
                    .append(Text.literal(" · ").formatted(Formatting.GRAY))
                    .append(Text.translatable("book.kiko.mother." + g, k.motherName()));
            if (k.role() != null && !k.role().isEmpty()) {
                line.append(Text.literal(" · ").formatted(Formatting.GRAY)).append(Text.translatable("role.kiko." + k.role() + "." + g));
                if (k.master()) line.append(Text.literal(" ★").formatted(Formatting.GOLD))
                        .append(Text.translatable("book.kiko.master." + g).formatted(Formatting.GOLD));
            } else if (stage == KikoChildEntity.TEEN && k.learning() != null && !k.learning().isEmpty()) {
                line.append(Text.literal(" · ").formatted(Formatting.GRAY))
                        .append(Text.translatable("book.kiko.learning", Text.translatable("role.kiko." + k.learning() + "." + g),
                                Math.min(100, k.learnPct())).formatted(Formatting.GREEN));
            }
            player.sendMessage(line, false);
        }
    }
}
