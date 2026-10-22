package com.jaciel.kiko.family;

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

/** Los hijos de cada jugador, guardados en el mundo (para el libro de familia aunque estén lejos). */
public class FamilyState extends PersistentState {
    private static final String NAME = "kiko_families";

    /** learning = oficio que está aprendiendo (o ya aprendió), learnPct = cuánto lleva (100 = ya), master = maestro de grande. */
    public record Kid(UUID uuid, String name, boolean male, UUID mother, String motherName, long birthTime, String role,
                      String learning, int learnPct, boolean master) {}

    private final Map<UUID, List<Kid>> kids = new HashMap<>();

    public static final Type<FamilyState> TYPE = new Type<>(FamilyState::new, FamilyState::fromNbt, null);

    public static FamilyState get(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(TYPE, NAME);
    }

    public List<Kid> kidsOf(UUID player) {
        return kids.getOrDefault(player, List.of());
    }

    public int countWithMother(UUID player, UUID mother) {
        int n = 0;
        for (Kid k : kidsOf(player)) if (k.mother().equals(mother)) n++;
        return n;
    }

    public void put(UUID player, Kid kid) {
        List<Kid> list = kids.computeIfAbsent(player, k -> new ArrayList<>());
        list.removeIf(k -> k.uuid().equals(kid.uuid()));
        list.add(kid);
        markDirty();
    }

    public void remove(UUID kid) {
        boolean changed = false;
        for (List<Kid> list : kids.values()) changed |= list.removeIf(k -> k.uuid().equals(kid));
        kids.values().removeIf(List::isEmpty);
        if (changed) markDirty();
    }

    private static FamilyState fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        FamilyState s = new FamilyState();
        NbtList players = nbt.getList("Players", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < players.size(); i++) {
            NbtCompound p = players.getCompound(i);
            List<Kid> list = new ArrayList<>();
            NbtList ks = p.getList("Kids", NbtElement.COMPOUND_TYPE);
            for (int j = 0; j < ks.size(); j++) {
                NbtCompound k = ks.getCompound(j);
                list.add(new Kid(k.getUuid("Uuid"), k.getString("Name"), k.getBoolean("Male"), k.getUuid("Mother"),
                        k.getString("MotherName"), k.getLong("Birth"), k.getString("Role"),
                        k.getString("Learning"), k.getInt("LearnPct"), k.getBoolean("Master")));
            }
            if (!list.isEmpty()) s.kids.put(p.getUuid("Player"), list);
        }
        return s;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        NbtList players = new NbtList();
        for (Map.Entry<UUID, List<Kid>> e : kids.entrySet()) {
            NbtCompound p = new NbtCompound();
            p.putUuid("Player", e.getKey());
            NbtList ks = new NbtList();
            for (Kid k : e.getValue()) {
                NbtCompound c = new NbtCompound();
                c.putUuid("Uuid", k.uuid());
                c.putString("Name", k.name());
                c.putBoolean("Male", k.male());
                c.putUuid("Mother", k.mother());
                c.putString("MotherName", k.motherName());
                c.putLong("Birth", k.birthTime());
                c.putString("Role", k.role() == null ? "" : k.role());
                c.putString("Learning", k.learning() == null ? "" : k.learning());
                c.putInt("LearnPct", k.learnPct());
                c.putBoolean("Master", k.master());
                ks.add(c);
            }
            p.put("Kids", ks);
            players.add(p);
        }
        nbt.put("Players", players);
        return nbt;
    }
}
