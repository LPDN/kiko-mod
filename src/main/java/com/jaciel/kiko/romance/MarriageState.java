package com.jaciel.kiko.romance;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Quién está casado con quién, guardado en el mundo (así se sabe aunque la esposa esté lejos y su chunk descargado).
 * Por jugador: la lista de sus esposas (uuid, quién es y su nombre).
 */
public class MarriageState extends PersistentState {
    private static final String NAME = "kiko_marriages";

    /** day = día del juego en que se casaron (-1 si es de antes de la v3.2 y todavía no se sabe). */
    public record Wife(UUID uuid, String character, String name, long day) {}

    private final Map<UUID, List<Wife>> wives = new HashMap<>();

    public static final Type<MarriageState> TYPE = new Type<>(MarriageState::new, MarriageState::fromNbt, null);

    public static MarriageState get(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(TYPE, NAME);
    }

    public List<Wife> wivesOf(UUID player) {
        return wives.getOrDefault(player, List.of());
    }

    public int count(UUID player) {
        return wivesOf(player).size();
    }

    public void add(UUID player, UUID girl, String character, String name, long day) {
        List<Wife> list = wives.computeIfAbsent(player, k -> new ArrayList<>());
        list.removeIf(w -> w.uuid().equals(girl));
        list.add(new Wife(girl, character, name, day));
        markDirty();
    }

    /** Pone el día de boda (para las bodas de antes de la v3.2) o actualiza el nombre. */
    public void update(UUID player, UUID girl, String name, long day) {
        List<Wife> list = wives.get(player);
        if (list == null) return;
        for (int i = 0; i < list.size(); i++) {
            Wife w = list.get(i);
            if (w.uuid().equals(girl) && (w.day() != day || !w.name().equals(name))) {
                list.set(i, new Wife(girl, w.character(), name, day));
                markDirty();
            }
        }
    }

    /** Se quita a esta chica de donde esté (por ejemplo si muere). */
    public void remove(UUID girl) {
        boolean changed = false;
        for (List<Wife> list : wives.values()) changed |= list.removeIf(w -> w.uuid().equals(girl));
        wives.values().removeIf(List::isEmpty);
        if (changed) markDirty();
    }

    private static MarriageState fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        MarriageState s = new MarriageState();
        NbtList players = nbt.getList("Players", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < players.size(); i++) {
            NbtCompound p = players.getCompound(i);
            UUID player = p.getUuid("Player");
            List<Wife> list = new ArrayList<>();
            NbtList ws = p.getList("Wives", NbtElement.COMPOUND_TYPE);
            for (int j = 0; j < ws.size(); j++) {
                NbtCompound w = ws.getCompound(j);
                list.add(new Wife(w.getUuid("Uuid"), w.getString("Character"), w.getString("Name"),
                        w.contains("Day") ? w.getLong("Day") : -1));
            }
            if (!list.isEmpty()) s.wives.put(player, list);
        }
        return s;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        NbtList players = new NbtList();
        for (Map.Entry<UUID, List<Wife>> e : wives.entrySet()) {
            NbtCompound p = new NbtCompound();
            p.putUuid("Player", e.getKey());
            NbtList ws = new NbtList();
            for (Wife w : e.getValue()) {
                NbtCompound c = new NbtCompound();
                c.putUuid("Uuid", w.uuid());
                c.putString("Character", w.character());
                c.putString("Name", w.name());
                c.putLong("Day", w.day());
                ws.add(c);
            }
            p.put("Wives", ws);
            players.add(p);
        }
        nbt.put("Players", players);
        return nbt;
    }
}
