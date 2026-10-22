package com.jaciel.kiko.registry;

import com.jaciel.kiko.KikoMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/** Sonidos propios de Kiko (archivos .ogg en assets/kiko/sounds). */
public final class ModSounds {
    public static final SoundEvent GREET = register("kiko.greet");
    public static final SoundEvent HAPPY = register("kiko.happy");
    public static final SoundEvent SAD = register("kiko.sad");
    public static final SoundEvent HURT = register("kiko.hurt");

    private ModSounds() {}

    private static SoundEvent register(String name) {
        Identifier id = KikoMod.id(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    /** Forza la carga de la clase para que los sonidos se registren a tiempo. */
    public static void register() {}
}
