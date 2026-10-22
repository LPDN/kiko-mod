package com.jaciel.kiko.town;

import com.jaciel.kiko.config.KikoConfig;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Días de la semana (v3.5). Minecraft no tiene, así que salen del contador de días del mundo.
 * El día 0 del mundo es lunes, así que cada 7 días cae domingo (día de mercadito).
 * Al amanecer avisa en la barra de acción qué día es.
 */
public final class TownCalendar {
    private TownCalendar() {}

    public static final int SUNDAY = 6;
    private static final Map<String, Long> ANNOUNCED = new HashMap<>();

    public static long day(World world) {
        return world.getTimeOfDay() / 24000L;
    }

    /** 0 lunes ... 6 domingo. */
    public static int weekday(World world) {
        return (int) Math.floorMod(day(world), 7L);
    }

    public static boolean isSunday(World world) {
        return weekday(world) == SUNDAY;
    }

    public static int daysToSunday(World world) {
        return Math.floorMod(SUNDAY - weekday(world), 7);
    }

    public static Text dayName(World world) {
        return Text.translatable("day.kiko." + weekday(world));
    }

    /** "Día 23, sábado" (y "hay mercadito en el pueblo" si es domingo). */
    public static MutableText dayLine(World world) {
        MutableText t = Text.translatable("message.kiko.day", day(world) + 1, dayName(world));
        if (isSunday(world)) t.append(Text.translatable("message.kiko.day_market"));
        return t;
    }

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(TownCalendar::tick);
    }

    private static void tick(ServerWorld world) {
        if (world.getRegistryKey() != World.OVERWORLD || world.getTime() % 20 != 0) return;
        if (!KikoConfig.get().dayAnnounce) return;
        long tod = world.getTimeOfDay() % 24000L;
        if (tod > 600) return;
        String key = world.getRegistryKey().getValue().toString();
        long d = day(world);
        Long last = ANNOUNCED.get(key);
        if (last != null && last == d) return;
        ANNOUNCED.put(key, d);
        if (last == null && tod > 100) return;   // recién cargado el mundo a media mañana: no repite
        Text msg = dayLine(world).formatted(isSunday(world) ? Formatting.GOLD : Formatting.YELLOW);
        for (ServerPlayerEntity p : world.getPlayers()) p.sendMessage(msg, true);
    }
}
