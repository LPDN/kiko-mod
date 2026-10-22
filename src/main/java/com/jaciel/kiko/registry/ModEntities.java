package com.jaciel.kiko.registry;

import com.jaciel.kiko.KikoMod;
import com.jaciel.kiko.entity.KikoBobberEntity;
import com.jaciel.kiko.entity.KikoEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/** Registro de la entidad. Se puede invocar en juego con /summon kiko:kiko */
public final class ModEntities {
    public static final EntityType<KikoEntity> KIKO = girl("kiko", 1.8f);
    /** Fase 6: las otras chicas. Misma IA, otro modelo, personalidad y especialidad (ver KikoCharacter). */
    public static final EntityType<KikoEntity> MARU = girl("maru", 1.8f);
    public static final EntityType<KikoEntity> LUMI = girl("lumi", 1.64f);
    public static final EntityType<KikoEntity> NADI = girl("nadi", 1.8f);
    /** v2.9: la enfermera y la chef. */
    public static final EntityType<KikoEntity> RINA = girl("rina", 1.8f);
    public static final EntityType<KikoEntity> TAMI = girl("tami", 1.8f);
    /** v3.3 los chicos. Misma IA y misma clase (KikoCharacter dice quién es cada uno). */
    public static final EntityType<KikoEntity> TONO = girl("tono", 1.88f);
    public static final EntityType<KikoEntity> LEO = girl("leo", 1.82f);
    public static final EntityType<KikoEntity> DANTE = girl("dante", 1.92f);
    public static final EntityType<KikoEntity> IVAN = girl("ivan", 1.80f);
    public static final EntityType<KikoEntity> EMI = girl("emi", 1.76f);
    public static final EntityType<KikoEntity> BETO = girl("beto", 1.84f);

    /** v3.1: hijos e hijas. Mide 1.8 de base y se encoge con el atributo de escala según su etapa. */
    public static final EntityType<com.jaciel.kiko.family.KikoChildEntity> CHILD = Registry.register(Registries.ENTITY_TYPE,
            KikoMod.id("child"),
            EntityType.Builder.create(com.jaciel.kiko.family.KikoChildEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.6f, 1.8f)
                    .eyeHeight(1.6f)
                    .vehicleAttachment(-0.72f)
                    .maxTrackingRange(10)
                    .disableSummon()
                    .build("child"));

    /** v3.4: gente del pueblo (misma base que los hijos, pero ya no crecen y salen con rasgos al azar). */
    public static final EntityType<com.jaciel.kiko.town.TownsfolkEntity> TOWNSFOLK = Registry.register(Registries.ENTITY_TYPE,
            KikoMod.id("townsfolk"),
            EntityType.Builder.create(com.jaciel.kiko.town.TownsfolkEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.6f, 1.8f)
                    .eyeHeight(1.6f)
                    .vehicleAttachment(-0.72f)
                    .maxTrackingRange(10)
                    .build("townsfolk"));

    /** Registra una chica. El ancho, los ojos y el asiento en la barca se escalan con su altura. */
    private static EntityType<KikoEntity> girl(String id, float height) {
        // v3.3.1 los más altos que el jugador se ven altos, pero su hitbox no pasa de 1.8 x 0.6 (si no se atoran en las puertas)
        float k = Math.min(height, 1.8f) / 1.8f;
        return Registry.register(Registries.ENTITY_TYPE, KikoMod.id(id),
                EntityType.Builder.create(KikoEntity::new, SpawnGroup.CREATURE)
                        .dimensions(0.6f * (0.5f + 0.5f * k), Math.min(height, 1.8f))
                        .eyeHeight(1.6f * k)
                        // Negativo = más abajo: las caderas quedan en el asiento de la barca
                        .vehicleAttachment(-0.72f * k)
                        .maxTrackingRange(10)
                        .build(id));
    }


    /** El corcho de su caña de pescar (solo visual, no se guarda). */
    public static final EntityType<KikoBobberEntity> BOBBER = Registry.register(
            Registries.ENTITY_TYPE,
            KikoMod.id("bobber"),
            EntityType.Builder.<KikoBobberEntity>create(KikoBobberEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25f, 0.25f)
                    .disableSaving()
                    .disableSummon()
                    .maxTrackingRange(4)
                    .trackingTickInterval(5)
                    .build("bobber")
    );

    /** v3.2.1 pelota de futbol. */
    public static final EntityType<com.jaciel.kiko.entity.SoccerBallEntity> SOCCER_BALL = Registry.register(
            Registries.ENTITY_TYPE,
            KikoMod.id("soccer_ball"),
            EntityType.Builder.<com.jaciel.kiko.entity.SoccerBallEntity>create(com.jaciel.kiko.entity.SoccerBallEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .maxTrackingRange(8)
                    .trackingTickInterval(2)
                    .build("soccer_ball")
    );

    private ModEntities() {}

    public static void register() {
        FabricDefaultAttributeRegistry.register(KIKO, KikoEntity.createKikoAttributes());
        // Maru: más fuerte y resistente. Lumi: más frágil. Nadi: más rápida.
        FabricDefaultAttributeRegistry.register(MARU, KikoEntity.createKikoAttributes()
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MAX_HEALTH, 26.0)
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.5)
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ARMOR, 4.0)
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5));
        FabricDefaultAttributeRegistry.register(LUMI, KikoEntity.createKikoAttributes()
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MAX_HEALTH, 18.0)
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0));
        FabricDefaultAttributeRegistry.register(NADI, KikoEntity.createKikoAttributes()
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.34));
        // Rina: aguanta un poquito más (se cura sola). Tami: normal, pero pega con el cucharón
        FabricDefaultAttributeRegistry.register(CHILD, KikoEntity.createKikoAttributes());
        FabricDefaultAttributeRegistry.register(TOWNSFOLK, KikoEntity.createKikoAttributes());
        // v3.3 Dante es el más fuerte; Iván aguanta; Toño y Beto normales; Leo y Emi más ligeros
        FabricDefaultAttributeRegistry.register(TONO, KikoEntity.createKikoAttributes()
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MAX_HEALTH, 24.0));
        FabricDefaultAttributeRegistry.register(LEO, KikoEntity.createKikoAttributes());
        FabricDefaultAttributeRegistry.register(DANTE, KikoEntity.createKikoAttributes()
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ARMOR, 6.0)
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.6));
        FabricDefaultAttributeRegistry.register(IVAN, KikoEntity.createKikoAttributes()
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MAX_HEALTH, 24.0)
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ARMOR, 2.0));
        FabricDefaultAttributeRegistry.register(EMI, KikoEntity.createKikoAttributes()
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.32));
        FabricDefaultAttributeRegistry.register(BETO, KikoEntity.createKikoAttributes());
        FabricDefaultAttributeRegistry.register(RINA, KikoEntity.createKikoAttributes()
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MAX_HEALTH, 22.0));
        FabricDefaultAttributeRegistry.register(TAMI, KikoEntity.createKikoAttributes()
                .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.5));
    }
}
