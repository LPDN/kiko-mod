package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapDecorationType;
import net.minecraft.item.map.MapState;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.structure.Structure;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Exploradora. Kiko se va de expedición unos minutos (desaparece) y al regresar:
 *  - Te dice qué encontró cerca (aldeas, portales en ruinas, barcos hundidos, minas, ruinas del océano,
 *    tesoros enterrados, monumentos y cámaras de prueba) con la distancia, dirección y coordenadas
 *  - Te trae un mapa marcado con una X hacia lo más cercano que encontró
 *  - Trae cositas que juntó en el camino
 * El tiempo solo corre mientras su chunk está cargado (si te vas muy lejos, te espera).
 */
public final class Expedition {
    private Expedition() {}

    /** Estructuras que busca: tag de estructura y llave de idioma. */
    private static final String[][] TARGETS = {
            {"village", "explore.kiko.village"},
            {"ruined_portal", "explore.kiko.ruined_portal"},
            {"shipwreck", "explore.kiko.shipwreck"},
            {"mineshaft", "explore.kiko.mineshaft"},
            {"ocean_ruin", "explore.kiko.ocean_ruin"},
            {"on_treasure_maps", "explore.kiko.treasure"},
            {"on_ocean_explorer_maps", "explore.kiko.monument"},
            {"on_trial_chambers_maps", "explore.kiko.trial_chambers"},
    };

    private record Found(BlockPos pos, String key, double dist) {}

    public static void start(KikoEntity kiko) {
        if (!(kiko.getWorld() instanceof ServerWorld world)) return;
        world.spawnParticles(ParticleTypes.CLOUD, kiko.getX(), kiko.getBodyY(0.5), kiko.getZ(), 20, 0.3, 0.5, 0.3, 0.02);
        world.playSound(null, kiko.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.NEUTRAL, 0.6f, 1.4f);
        int ticks = Math.max(200, KikoConfig.get().expeditionTicks);
        // Nadi es exploradora de verdad: tarda la mitad
        if (kiko.getCharacter() == com.jaciel.kiko.entity.KikoCharacter.NADI) ticks /= 2;
        kiko.setAway(true, ticks);
    }

    /** Se llama cada tick mientras anda fuera. */
    public static void tick(KikoEntity kiko) {
        if (kiko.decrementAway() > 0) return;
        finish(kiko);
    }

    private static void finish(KikoEntity kiko) {
        if (!(kiko.getWorld() instanceof ServerWorld world)) return;
        BlockPos origin = kiko.getWorkCenter() != null ? kiko.getWorkCenter() : kiko.getBlockPos();
        kiko.setAway(false, 0);
        kiko.setWorkMode(WorkMode.NONE, null);

        // ¿Qué encontró?
        List<Found> found = search(world, origin);
        if (found.size() > 4) found = found.subList(0, 4);

        // Lo que trae en la mochila
        List<ItemStack> loot = new ArrayList<>();
        roll(world, origin, "chests/village/village_plains_house", loot);
        if (kiko.getRandom().nextFloat() < 0.35f) roll(world, origin, "chests/shipwreck_supply", loot);
        if (kiko.getCharacter() == com.jaciel.kiko.entity.KikoCharacter.NADI) {
            // Y trae más cosas
            roll(world, origin, "chests/village/village_cartographer", loot);
            if (kiko.getRandom().nextFloat() < 0.5f) roll(world, origin, "chests/shipwreck_treasure", loot);
        }
        if (!found.isEmpty()) loot.add(treasureMap(world, found.get(0)));
        for (ItemStack s : loot) {
            ItemStack rest = kiko.getInventory().insert(s);
            if (!rest.isEmpty()) kiko.dropStack(rest);
        }

        world.spawnParticles(ParticleTypes.CLOUD, kiko.getX(), kiko.getBodyY(0.5), kiko.getZ(), 20, 0.3, 0.5, 0.3, 0.02);
        world.playSound(null, kiko.getBlockPos(), SoundEvents.ENTITY_VILLAGER_CELEBRATE, SoundCategory.NEUTRAL, 0.8f, 1.5f);
        kiko.setExpression(KikoExpression.LAUGH, 80);

        // El reporte para quien la mandó
        PlayerEntity p = kiko.getOwnerUuid() != null ? world.getPlayerByUuid(kiko.getOwnerUuid()) : null;
        if (p == null) return;
        kiko.getAffection().add(p.getUuid(), 2f, world.getTime());
        KikoInteractionHandler.say(p, kiko, "dialogue.kiko.explore_back", 3);
        if (found.isEmpty()) {
            p.sendMessage(Text.translatable("message.kiko.explore_nothing").formatted(Formatting.GRAY), false);
            return;
        }
        for (Found f : found) {
            int dx = f.pos().getX() - origin.getX();
            int dz = f.pos().getZ() - origin.getZ();
            p.sendMessage(Text.literal("  • ").formatted(Formatting.GOLD)
                    .append(Text.translatable("message.kiko.explore_found",
                            Text.translatable(f.key()).formatted(Formatting.YELLOW),
                            Math.round(f.dist()),
                            Text.translatable(compass(dx, dz)),
                            f.pos().getX(), f.pos().getZ()).formatted(Formatting.WHITE)), false);
        }
        p.sendMessage(Text.translatable("message.kiko.explore_map").formatted(Formatting.AQUA), false);
    }

    /** Busca las estructuras cercanas (de la más cercana a la más lejana). */
    private static List<Found> search(ServerWorld world, BlockPos origin) {
        List<Found> found = new ArrayList<>();
        for (String[] t : TARGETS) {
            TagKey<Structure> tag = TagKey.of(RegistryKeys.STRUCTURE, Identifier.ofVanilla(t[0]));
            try {
                // Primero algo que todavía no conoces; si no hay, lo más cercano que no esté pegado a ti
                BlockPos p = world.locateStructure(tag, origin, 32, true);
                if (p == null) p = world.locateStructure(tag, origin, 32, false);
                if (p == null) continue;
                double dist = Math.sqrt(p.getSquaredDistance(origin.getX(), p.getY(), origin.getZ()));
                if (dist < 96) continue; // eso ya lo tienes a un lado (como la aldea donde vives)
                found.add(new Found(p, t[1], dist));
            } catch (Exception ignored) {
                // Un tag que no existe en esta versión o en un datapack: lo salta
            }
        }
        found.sort(Comparator.comparingDouble(Found::dist));
        return found;
    }

    /** v3.0 (Nadi esposa): un mapa con la X en la estructura más cercana que no conozcas, o null. */
    @org.jetbrains.annotations.Nullable
    public static ItemStack scoutMap(ServerWorld world, BlockPos origin) {
        List<Found> found = search(world, origin);
        if (found.isEmpty()) return null;
        // Para que no sea siempre el mismo, escoge entre las 3 más cercanas
        Found f = found.get(world.getRandom().nextInt(Math.min(3, found.size())));
        return treasureMap(world, f);
    }

    private static void roll(ServerWorld world, BlockPos at, String table, List<ItemStack> out) {
        RegistryKey<LootTable> key = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.ofVanilla(table));
        LootTable lt = world.getServer().getReloadableRegistries().getLootTable(key);
        LootContextParameterSet params = new LootContextParameterSet.Builder(world)
                .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(at))
                .build(LootContextTypes.CHEST);
        out.addAll(lt.generateLoot(params));
    }

    private static ItemStack treasureMap(ServerWorld world, Found f) {
        // Los mapas se acomodan en una cuadrícula, así que escoge la escala donde la X quede más adentro.
        // Si de plano cae en la orilla, la X se recorre poquito hacia adentro (Minecraft no dibuja las de la orilla)
        byte scale = 2;
        double bestEdge = Double.MAX_VALUE;
        for (byte sc : new byte[]{2, 1, 3}) {
            double edge = Math.max(Math.abs(mapOffset(f.pos().getX(), sc)), Math.abs(mapOffset(f.pos().getZ(), sc)));
            if (edge < bestEdge - 0.5) {
                bestEdge = edge;
                scale = sc;
            }
            if (edge <= 57) break;
        }
        BlockPos mark = new BlockPos(clampToMap(f.pos().getX(), scale), f.pos().getY(), clampToMap(f.pos().getZ(), scale));
        ItemStack map = FilledMapItem.createMap(world, f.pos().getX(), f.pos().getZ(), scale, true, true);
        FilledMapItem.fillExplorationMap(world, map);
        Optional<RegistryEntry.Reference<MapDecorationType>> x = world.getRegistryManager()
                .get(RegistryKeys.MAP_DECORATION_TYPE)
                .getEntry(RegistryKey.of(RegistryKeys.MAP_DECORATION_TYPE, Identifier.ofVanilla("red_x")));
        x.ifPresent(type -> MapState.addDecorationsNbt(map, mark, "+", type));
        map.set(DataComponentTypes.ITEM_NAME, Text.translatable("item.kiko.explore_map", Text.translatable(f.key())));
        return map;
    }

    private static int mapCenter(int coord, byte scale) {
        int size = 128 * (1 << scale);
        int cell = (int) Math.floor((coord + 64.0) / size);
        return cell * size + size / 2 - 64;
    }

    /** Qué tan lejos del centro cae la coordenada, en píxeles del mapa (el mapa va de -64 a 64). */
    private static double mapOffset(int coord, byte scale) {
        return (coord - mapCenter(coord, scale)) / (double) (1 << scale);
    }

    /** Recorre la coordenada para que quede a máximo 57 píxeles del centro. */
    private static int clampToMap(int coord, byte scale) {
        int center = mapCenter(coord, scale);
        int max = 57 * (1 << scale);
        return Math.max(center - max, Math.min(center + max, coord));
    }

    /** Dirección en la brújula (norte es -Z). */
    private static String compass(int dx, int dz) {
        double angle = Math.toDegrees(Math.atan2(dx, -dz));
        int i = Math.floorMod((int) Math.round(angle / 45.0), 8);
        return "dir.kiko." + new String[]{"n", "ne", "e", "se", "s", "sw", "w", "nw"}[i];
    }
}
