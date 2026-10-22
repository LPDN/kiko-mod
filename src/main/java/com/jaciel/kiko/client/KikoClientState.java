package com.jaciel.kiko.client;

import com.jaciel.kiko.entity.KikoEntity;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Opciones solo del cliente (se guardan en config/kiko-client.properties). */
public final class KikoClientState {
    private KikoClientState() {}

    /** Marcador: contorno de tus Kikos a través de las paredes. */
    public static boolean marker = true;

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("kiko-client.properties");
    }

    public static void load() {
        try (Reader r = Files.newBufferedReader(file())) {
            Properties p = new Properties();
            p.load(r);
            marker = Boolean.parseBoolean(p.getProperty("marker", "true"));
        } catch (Exception ignored) {
            // Primera vez: se queda con lo de por defecto
        }
    }

    public static void save() {
        try (Writer w = Files.newBufferedWriter(file())) {
            Properties p = new Properties();
            p.setProperty("marker", Boolean.toString(marker));
            p.store(w, "Kiko - opciones del cliente");
        } catch (Exception ignored) {
        }
    }

    /** Se dibuja el contorno si es tuya y está a más de 6 bloques. */
    public static boolean shouldGlow(KikoEntity kiko) {
        if (!marker) return false;
        ClientPlayerEntity me = MinecraftClient.getInstance().player;
        return me != null && me.getUuid().equals(kiko.getFriendUuid()) && kiko.squaredDistanceTo(me) > 6 * 6;
    }
}
