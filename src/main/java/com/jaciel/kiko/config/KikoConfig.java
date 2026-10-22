package com.jaciel.kiko.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaciel.kiko.KikoMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Configuración del mod. Se guarda en config/kiko.json.
 * Si el archivo no existe se crea con estos valores por defecto.
 * Los campos son públicos para que Gson los lea y escriba directo.
 */
public class KikoConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("kiko.json");

    private static KikoConfig instance = new KikoConfig();

    // ---------- Afecto ----------
    public float affectionGreet = 2f;
    public float affectionTalk = 1f;
    public float affectionFood = 5f;
    public float affectionDamagePenalty = 10f;
    /** Cuánto baja por cada día de Minecraft sin interactuar. */
    public float affectionDailyDecay = 0.5f;

    /** Regalos aceptados. Clave = id de ítem o "#tag". Valor = afecto que suma. */
    public Map<String, Float> gifts = defaultGifts();

    // ---------- Umbrales de comportamiento ----------
    public float defendThreshold = 40f;
    public float followThreshold = 50f;
    public float specialThreshold = 80f;
    /** Afecto para dejarte abrir su inventario. */
    public float inventoryThreshold = 20f;
    /** Afecto para aceptar trabajos (talar, minar). */
    public float workThreshold = 50f;

    // ---------- Novias y esposas (v3.0) ----------
    /** Afecto que necesita tu novia para aceptar el anillo. (Para el ramo usa specialThreshold.) */
    public float marriageThreshold = 90f;
    /** Bonus de esposa (suerte, encantamientos, experiencia, velocidad, curación, menos hambre). */
    public boolean wifeBonuses = true;
    /** Probabilidad de que salga el Anillo del Abismo en un cofre de ciudad antigua (0.03 = 3 %). */
    public float abyssRingChance = 0.03f;

    // ---------- Familia (v3.1) ----------
    public boolean familyEnabled = true;
    /** Máximo de hijos con cada esposa. */
    public int maxKidsPerWife = 4;
    /** Días de Minecraft que hay que esperar entre un bebé y otro. */
    public int familyCooldownDays = 3;
    /** Días de Minecraft que dura cada etapa (bebé, niño, adolescente). Luego ya es adulto. */
    public int babyDays = 2;
    public int childDays = 5;
    public int teenDays = 5;

    // ---------- Pueblo (v3.5) ----------
    /** Los vecinos solteros se hacen novios y se casan en la plaza. */
    public boolean townWeddings = true;
    /** Las parejas del pueblo tienen bebés (necesitan una cuna libre en su casa). */
    public boolean townBabies = true;
    /** Máximo de vecinos por pueblo (contando 96 bloques alrededor). Ya no nacen más arriba de esto. */
    public int townPopulationCap = 40;
    /** Máximo de hijos por pareja del pueblo y días entre uno y otro. */
    public int townMaxKids = 3;
    public int townBabyCooldownDays = 3;
    /** Aviso del día en las mañanas (tipo "Día 23, sábado"). */
    public boolean dayAnnounce = true;
    /** Encargos de los vecinos (cuántos puede haber a la vez en un pueblo). */
    public int townMaxRequests = 5;

    // ---------- El pueblo crece (v3.7) ----------
    /** Los vecinos construyen casas nuevas con el tiempo. */
    public boolean townGrowth = true;
    /** Cada cuántos días de Minecraft se empieza una casa nueva (si hay jugador cerca). */
    public int townGrowthDays = 4;
    /** Máximo de casas nuevas por pueblo. */
    public int townGrowthMax = 8;

    // ---------- Guardias y ataques (v3.8) ----------
    /** Porcentaje de papás y mamás del pueblo que salen de guardias. */
    public int townGuardChance = 12;
    /** Ataques de pillagers a los pueblos. */
    public boolean townRaids = true;
    /** Probabilidad (por ciento) de que cada día haya ataque en un pueblo con jugador cerca. */
    public int townRaidChance = 10;
    /** Máximo de pillagers y compañía en un ataque. */
    public int townRaidMax = 12;
    /** Días de Minecraft sin ataques después de uno. */
    public int townRaidCooldownDays = 3;

    // ---------- Vida social (v3.9) ----------
    /** Cumpleaños de vecinos y compañeras (aviso, globito, regalos al doble, fiesta en la plaza). */
    public boolean birthdays = true;
    /** Paraguas, frío, truenos, ver las estrellas y que tus compañeras comenten el clima. */
    public boolean townWeather = true;
    /** Feria de los sábados y fiesta de victoria después de un ataque. */
    public boolean townFestival = true;
    /** Escuela (la construyen los vecinos) y clases de lunes a viernes en la mañana. */
    public boolean townSchool = true;
    /** Días de escuela para salir de grande con mejor oficio. */
    public int schoolDaysForJob = 5;
    /** Citas con tus compañeras. */
    public boolean dates = true;
    /** Cariño mínimo para invitarla a salir y cuánto sube al terminar (el doble en su lugar favorito). */
    public float dateAffection = 60f;
    public float dateAffectionGain = 8f;

    // ---------- Bebidas y crafteo (v3.6) ----------
    /** Las chicas se craftean herramientas, armadura, espada y antorchas con lo que traen en la mochila (sin mesa). */
    public boolean companionCraft = true;

    // ---------- Familia (v3.2) ----------
    /** Segundos que un adolescente tiene que pasar junto a alguien con oficio para aprenderlo (y salir maestro). */
    public int apprenticeSeconds = 600;
    /** Aniversario de boda cada tantos días de Minecraft. */
    public boolean anniversaryEnabled = true;
    public int anniversaryDays = 7;

    // ---------- Trabajo ----------
    /** Radio en bloques alrededor de donde le encargaste el trabajo. */
    public int workRadius = 12;
    // ---------- Ropa ----------
    /** Cuánto tiempo (ticks) tiene que estar en un bioma para cambiarse de ropa. 12000 = 10 minutos. */
    public int autoOutfitDelayTicks = 12000;

    // ---------- Antorchas ----------
    /** Pone antorchas en lugares oscuros cuando te sigue (si trae). */
    public boolean autoTorches = true;

    // ---------- Trabajos de la Fase 2 ----------
    /** Máximo de animales de una especie en el área antes de dejar de criar. */
    public int breedLimit = 12;
    /** Radio del área para aplanar. */
    public int flattenRadius = 6;

    // ---------- Fase 5 ----------
    /** Cuánto dura una expedición de la exploradora (ticks). 6000 = 5 minutos. */
    public int expeditionTicks = 6000;
    /** Radio del área donde busca cofres para ordenar. */
    public int sortRadius = 12;
    /** Distancia máxima (bloques) a la que el silbato la encuentra. */
    public int whistleRange = 512;

    /** Largo máximo del túnel en bloques. */
    public int tunnelLength = 64;
    /** 1 = túnel de 1x2, 3 = túnel de 3x3. */
    public int tunnelSize = 1;
    /** Hasta qué capa baja la escalera (-50 es buena para diamantes y evita los lagos de lava). */
    public int stairsTargetY = -50;

    // ---------- Cooldowns (segundos) ----------
    public int talkCooldownSeconds = 30;
    public int greetCooldownSeconds = 300;
    public int giftCooldownSeconds = 3;

    // ---------- Spawn en aldeas ----------
    public boolean villageSpawnEnabled = true;
    /** Probabilidad (0 a 1) de que una aldea tenga a Kiko. */
    public float villageSpawnChance = 0.6f;
    /** Máximo de Kikos por aldea (1 o 2 recomendado). */
    public int maxPerVillage = 1;
    /** Dimensiones donde puede aparecer en aldeas. */
    public String[] allowedDimensions = {"minecraft:overworld"};

    // ---------- Seguir ----------
    /** Distancia a la que se teletransporta contigo si se queda atrás. */
    public float followTeleportDistance = 20f;

    private static Map<String, Float> defaultGifts() {
        Map<String, Float> m = new LinkedHashMap<>();
        m.put("kiko:gift_box", 20f);
        m.put("minecraft:enchanted_golden_apple", 25f);
        m.put("minecraft:golden_apple", 15f);
        m.put("minecraft:diamond", 15f);
        m.put("minecraft:cake", 15f);
        m.put("minecraft:emerald", 10f);
        m.put("minecraft:amethyst_shard", 8f);
        m.put("#minecraft:small_flowers", 6f);
        return m;
    }

    public static KikoConfig get() {
        return instance;
    }

    public static void load() {
        try {
            if (Files.exists(PATH)) {
                try (Reader r = Files.newBufferedReader(PATH)) {
                    KikoConfig loaded = GSON.fromJson(r, KikoConfig.class);
                    if (loaded != null) instance = loaded;
                }
            }
            save(); // reescribe para agregar campos nuevos de versiones futuras
        } catch (Exception e) {
            KikoMod.LOGGER.error("No pude leer config/kiko.json, uso valores por defecto", e);
            instance = new KikoConfig();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer w = Files.newBufferedWriter(PATH)) {
                GSON.toJson(instance, w);
            }
        } catch (Exception e) {
            KikoMod.LOGGER.error("No pude guardar config/kiko.json", e);
        }
    }
}
