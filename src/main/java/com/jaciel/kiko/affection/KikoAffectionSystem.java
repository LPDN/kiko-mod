package com.jaciel.kiko.affection;

import com.jaciel.kiko.config.KikoConfig;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Guarda cuánto quiere Kiko a cada jugador (de -100 a 100, empieza en 0).
 * Cada Kiko tiene su propia tabla, así que dos jugadores pueden llevarse distinto con ella.
 * Todo se persiste en el NBT de la entidad.
 */
public class KikoAffectionSystem {
    public static final float MIN = -100f;
    public static final float MAX = 100f;
    public static final int TICKS_PER_DAY = 24000;

    /** Niveles de relación que usan la IA, los diálogos y el modelo. */
    public enum Tier {
        DISTANT,   // < 0       se aleja
        NEUTRAL,   // 0 a 40    puede ignorarte
        FRIENDLY,  // 40 a 80   te defiende (y te sigue desde 50)
        BESTIE;    // 80+       reacciones especiales

        public String key() {
            return name().toLowerCase();
        }
    }

    /** Datos de un jugador. */
    public static class Record {
        /** v3.9 día en que te dijo que cumplía años (no se guarda). */
        public long lastBirthdayHello = -1;
        public float affection;
        public long lastInteraction;
        public long lastGreet = -100000L;
        public long lastTalk = -100000L;
        public long lastGift = -100000L;
    }

    private final Map<UUID, Record> records = new HashMap<>();

    public Record record(UUID player) {
        return records.computeIfAbsent(player, u -> new Record());
    }

    public float get(UUID player) {
        Record r = records.get(player);
        return r == null ? 0f : r.affection;
    }

    /** Suma (o resta) afecto y marca que hubo interacción. Regresa el valor nuevo. */
    public float add(UUID player, float amount, long worldTime) {
        Record r = record(player);
        r.affection = MathHelper.clamp(r.affection + amount, MIN, MAX);
        r.lastInteraction = worldTime;
        return r.affection;
    }

    public void set(UUID player, float value, long worldTime) {
        Record r = record(player);
        r.affection = MathHelper.clamp(value, MIN, MAX);
        r.lastInteraction = worldTime;
    }

    /** El jugador que más la quiere (si llega al umbral), o null. */
    public UUID topFriend(float threshold) {
        UUID best = null;
        float bestValue = threshold - 0.001f;
        for (Map.Entry<UUID, Record> e : records.entrySet()) {
            if (e.getValue().affection > bestValue) {
                bestValue = e.getValue().affection;
                best = e.getKey();
            }
        }
        return best;
    }

    public Map<UUID, Record> all() {
        return records;
    }

    public static Tier tierOf(float affection) {
        KikoConfig c = KikoConfig.get();
        if (affection < 0) return Tier.DISTANT;
        if (affection >= c.specialThreshold) return Tier.BESTIE;
        if (affection >= c.defendThreshold) return Tier.FRIENDLY;
        return Tier.NEUTRAL;
    }

    /**
     * Se llama una vez por día de juego. A quien no le hizo caso en todo el día
     * le baja un poquito el afecto (negligencia).
     */
    public void applyDailyNeglect(long worldTime) {
        float decay = KikoConfig.get().affectionDailyDecay;
        for (Record r : records.values()) {
            if (worldTime - r.lastInteraction >= TICKS_PER_DAY) {
                r.affection = MathHelper.clamp(r.affection - decay, MIN, MAX);
            }
        }
    }

    // ---------------- NBT ----------------

    public NbtList writeNbt() {
        NbtList list = new NbtList();
        records.forEach((uuid, r) -> {
            NbtCompound tag = new NbtCompound();
            tag.putUuid("Player", uuid);
            tag.putFloat("Affection", r.affection);
            tag.putLong("LastInteraction", r.lastInteraction);
            tag.putLong("LastGreet", r.lastGreet);
            tag.putLong("LastTalk", r.lastTalk);
            list.add(tag);
        });
        return list;
    }

    public void readNbt(NbtList list) {
        records.clear();
        for (int i = 0; i < list.size(); i++) {
            NbtCompound tag = list.getCompound(i);
            if (!tag.containsUuid("Player")) continue;
            Record r = new Record();
            r.affection = MathHelper.clamp(tag.getFloat("Affection"), MIN, MAX);
            r.lastInteraction = tag.getLong("LastInteraction");
            r.lastGreet = tag.getLong("LastGreet");
            r.lastTalk = tag.getLong("LastTalk");
            records.put(tag.getUuid("Player"), r);
        }
    }

    public static final int NBT_LIST_TYPE = NbtElement.COMPOUND_TYPE;

    /** Texto tipo ❤❤❤♡♡ para mostrar el nivel. */
    public static String hearts(float affection) {
        if (affection < 0) {
            int cold = Math.min(5, Math.max(1, Math.round(-affection / 20f)));
            return "❄".repeat(cold); // copitos de nieve, está fría contigo
        }
        int full = MathHelper.clamp(Math.round(affection / 20f), 0, 5);
        return "❤".repeat(full) + "♡".repeat(5 - full);
    }
}
