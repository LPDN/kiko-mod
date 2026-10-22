package com.jaciel.kiko.entity;

import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.work.WorkMode;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import com.jaciel.kiko.registry.ModEntities;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.UUID;

/** Buscar "tus" Kikos (las que te tienen como mejor amigo) y llamarlas. */
public final class KikoCalls {
    private KikoCalls() {}

    /** Tus Kikos cargadas en tu mundo actual, de la más cercana a la más lejana. */
    public static List<KikoEntity> mine(ServerPlayerEntity player, double range) {
        ServerWorld world = player.getServerWorld();
        UUID me = player.getUuid();
        List<KikoEntity> list = new java.util.ArrayList<>(world.getEntitiesByType(net.minecraft.util.TypeFilter.instanceOf(KikoEntity.class),
                k -> k.isAlive() && !k.isTownsfolk() && me.equals(k.getFriendUuid()) && k.squaredDistanceTo(player) < range * range));
        list.sort(java.util.Comparator.comparingDouble(k -> k.squaredDistanceTo(player)));
        return list;
    }

    /**
     * La trae junto a ti: deja de trabajar, se despierta y te sigue.
     * @return false si anda de expedición y no puede venir
     */
    public static boolean call(KikoEntity kiko, ServerPlayerEntity player) {
        if (kiko.isAway() || kiko.isTownsfolk()) return false;
        // El bebé se queda en su cuna (no lo andes teletransportando)
        if (kiko instanceof com.jaciel.kiko.family.KikoChildEntity kid && kid.getStage() == com.jaciel.kiko.family.KikoChildEntity.BABY) return false;
        if (kiko.getWorkMode() != WorkMode.NONE) kiko.setWorkMode(WorkMode.NONE, player);
        kiko.setFollowEnabled(true);
        if (kiko.squaredDistanceTo(player) > 4 * 4) {
            kiko.spawnParticles(ParticleTypes.POOF, 10);
            kiko.teleportNear(player);
            kiko.spawnParticles(ParticleTypes.HAPPY_VILLAGER, 8);
        }
        KikoInteractionHandler.say(player, kiko, "dialogue.kiko.called", 3);
        return true;
    }

    /** Llama a todas tus Kikos. Regresa cuántas vinieron. */
    public static int callAll(ServerPlayerEntity player) {
        int n = 0;
        for (KikoEntity k : mine(player, KikoConfig.get().whistleRange)) {
            if (call(k, player)) n++;
        }
        return n;
    }
}
