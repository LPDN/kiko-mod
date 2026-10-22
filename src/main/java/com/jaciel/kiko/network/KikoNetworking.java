package com.jaciel.kiko.network;

import com.jaciel.kiko.KikoMod;
import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import com.jaciel.kiko.outfit.KikoOutfit;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.combat.CombatMode;
import com.jaciel.kiko.entity.work.WorkMode;
import com.jaciel.kiko.inventory.KikoInventory;
import com.jaciel.kiko.inventory.KikoScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;

/**
 * Paquetes entre cliente y servidor para el menú de Kiko.
 *
 *  Servidor -> Cliente  OpenMenu   (abre la pantalla con el estado actual)
 *  Cliente -> Servidor  MenuAction (cambiar ropa o activar / desactivar seguir)
 */
public final class KikoNetworking {
    private KikoNetworking() {}

    public static final int ACTION_OUTFIT = 0;
    public static final int ACTION_TOGGLE_FOLLOW = 1;
    public static final int ACTION_OPEN_INVENTORY = 2;
    public static final int ACTION_SET_WORK = 3;
    public static final int ACTION_DEPOSIT = 4;
    public static final int ACTION_CYCLE_COMBAT = 5;
    public static final int ACTION_TOGGLE_AUTO_OUTFIT = 6;
    public static final int ACTION_TOGGLE_HIDE_ARMOR = 7;
    public static final int ACTION_TOGGLE_TUNNEL_SIZE = 8;
    public static final int ACTION_MENU_CLOSED = 9;
    // Fase 5
    public static final int ACTION_SET_HOME = 10;
    public static final int ACTION_CALL = 11;
    public static final int ACTION_REMOTE_MENU = 12;
    public static final int ACTION_RELEASE_PET = 13;
    public static final int ACTION_CALL_ALL = 14;
    /** v3.0: ropa de las chicas que no son Kiko (value = 0 trabajo, 1 casual, 2 boda). */
    public static final int ACTION_GIRL_OUTFIT = 15;
    /** v3.1: formar una familia (menú de tu esposa) y abrir la pantalla de tus rasgos (menú K). */
    public static final int ACTION_FAMILY = 16;
    public static final int ACTION_TRAITS = 17;
    /** v3.3.1: abrir la tiendita de Beto. */
    public static final int ACTION_TRADE = 18;
    /** v3.9 invitarla a salir. */
    public static final int ACTION_DATE = 19;
    /** v3.1: bandera de hijo/hija (no trabaja ni se cambia de ropa mientras es menor). */
    public static final int FLAG_KID = 1 << 10, FLAG_MINOR = 1 << 11;
    /** v3.1.4: es tu hijo o hija, y si es niño (para el letrero del menú). */
    public static final int FLAG_MY_KID = 1 << 12, FLAG_KID_MALE = 1 << 13;

    /** Bits de OpenMenuPayload.flags */
    public static final int FLAG_FOLLOWING = 1, FLAG_AUTO_OUTFIT = 2, FLAG_HIDE_ARMOR = 4, FLAG_TUNNEL_3X3 = 8,
            FLAG_HAS_HOME = 16, FLAG_HAS_PET = 32, FLAG_GIRLFRIEND = 64, FLAG_WIFE = 128;
    /** La ropa de las chicas y chicos (0 trabajo, 1 casual, 2 boda, 3 verano, 4 invierno) va en los bits 14 a 16 de flags. */
    public static final int GIRL_OUTFIT_SHIFT = 14;
    /** v3.3: lo que escogiste (puede ser distinto de lo que trae si se cambió por el bioma), bits 17 a 19. */
    public static final int CHOSEN_OUTFIT_SHIFT = 17;

    /** Bits de KikoInfo.flags (menú rápido) */
    public static final int INFO_SLEEPING = 1, INFO_AWAY = 2, INFO_FOLLOWING = 4, INFO_PET = 8, INFO_HOME = 16;

    /** Una Kiko en la lista del menú rápido. */
    public record KikoInfo(int entityId, String name, int work, int distance, int healthPct, int flags) {
        public static final PacketCodec<RegistryByteBuf, KikoInfo> CODEC = PacketCodec.tuple(
                PacketCodecs.VAR_INT, KikoInfo::entityId,
                PacketCodecs.STRING, KikoInfo::name,
                PacketCodecs.VAR_INT, KikoInfo::work,
                PacketCodecs.VAR_INT, KikoInfo::distance,
                PacketCodecs.VAR_INT, KikoInfo::healthPct,
                PacketCodecs.VAR_INT, KikoInfo::flags,
                KikoInfo::new);
    }

    /** C2S. Tecla K: pide la lista de tus Kikos. */
    public record QuickMenuRequestPayload() implements CustomPayload {
        public static final Id<QuickMenuRequestPayload> ID = new Id<>(KikoMod.id("quick_request"));
        public static final PacketCodec<RegistryByteBuf, QuickMenuRequestPayload> CODEC = PacketCodec.unit(new QuickMenuRequestPayload());

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    /** S2C. Lista de tus Kikos para el menú rápido. */
    public record QuickMenuPayload(java.util.List<KikoInfo> kikos) implements CustomPayload {
        public static final Id<QuickMenuPayload> ID = new Id<>(KikoMod.id("quick_menu"));
        public static final PacketCodec<RegistryByteBuf, QuickMenuPayload> CODEC = PacketCodec.tuple(
                KikoInfo.CODEC.collect(PacketCodecs.toList()), QuickMenuPayload::kikos,
                QuickMenuPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    /** S2C. Datos para dibujar el menú. outfit es la ropa escogida, flags los interruptores. */
    public record OpenMenuPayload(int entityId, int outfit, float affection, int work, int combat, int flags) implements CustomPayload {
        public static final Id<OpenMenuPayload> ID = new Id<>(KikoMod.id("open_menu"));
        public static final PacketCodec<RegistryByteBuf, OpenMenuPayload> CODEC = PacketCodec.tuple(
                PacketCodecs.VAR_INT, OpenMenuPayload::entityId,
                PacketCodecs.VAR_INT, OpenMenuPayload::outfit,
                PacketCodecs.FLOAT, OpenMenuPayload::affection,
                PacketCodecs.VAR_INT, OpenMenuPayload::work,
                PacketCodecs.VAR_INT, OpenMenuPayload::combat,
                PacketCodecs.VAR_INT, OpenMenuPayload::flags,
                OpenMenuPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    /** S2C/C2S. Tus rasgos (abrir la pantalla / guardar). */
    public record TraitsPayload(boolean male, int skin, int hair, int eyes) implements CustomPayload {
        public static final Id<TraitsPayload> OPEN = new Id<>(KikoMod.id("traits_open"));
        public static final PacketCodec<RegistryByteBuf, TraitsPayload> CODEC = PacketCodec.tuple(
                PacketCodecs.BOOL, TraitsPayload::male,
                PacketCodecs.VAR_INT, TraitsPayload::skin,
                PacketCodecs.VAR_INT, TraitsPayload::hair,
                PacketCodecs.VAR_INT, TraitsPayload::eyes,
                TraitsPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return OPEN;
        }
    }

    /** C2S. Guardar rasgos (mismo contenido, otro canal). */
    public record SaveTraitsPayload(boolean male, int skin, int hair, int eyes) implements CustomPayload {
        public static final Id<SaveTraitsPayload> ID = new Id<>(KikoMod.id("traits_save"));
        public static final PacketCodec<RegistryByteBuf, SaveTraitsPayload> CODEC = PacketCodec.tuple(
                PacketCodecs.BOOL, SaveTraitsPayload::male,
                PacketCodecs.VAR_INT, SaveTraitsPayload::skin,
                PacketCodecs.VAR_INT, SaveTraitsPayload::hair,
                PacketCodecs.VAR_INT, SaveTraitsPayload::eyes,
                SaveTraitsPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    /** S2C (pregunta el nombre del bebé) y C2S (el nombre que escogiste). */
    public record KidNamePayload(int entityId, boolean male, String name) implements CustomPayload {
        public static final Id<KidNamePayload> ID = new Id<>(KikoMod.id("kid_name"));
        public static final PacketCodec<RegistryByteBuf, KidNamePayload> CODEC = PacketCodec.tuple(
                PacketCodecs.VAR_INT, KidNamePayload::entityId,
                PacketCodecs.BOOL, KidNamePayload::male,
                PacketCodecs.STRING, KidNamePayload::name,
                KidNamePayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public static void askKidName(ServerPlayerEntity player, com.jaciel.kiko.family.KikoChildEntity kid, boolean male) {
        ServerPlayNetworking.send(player, new KidNamePayload(kid.getId(), male, kid.getDisplayName().getString()));
    }

    public static void openTraits(ServerPlayerEntity player) {
        com.jaciel.kiko.family.Traits.PlayerTraits t = com.jaciel.kiko.family.Traits.of(player);
        ServerPlayNetworking.send(player, new TraitsPayload(t.male(), t.skin(), t.hair(), t.eyes()));
    }

    /** C2S. Botón presionado en el menú. */
    public record MenuActionPayload(int entityId, int action, int value) implements CustomPayload {
        public static final Id<MenuActionPayload> ID = new Id<>(KikoMod.id("menu_action"));
        public static final PacketCodec<RegistryByteBuf, MenuActionPayload> CODEC = PacketCodec.tuple(
                PacketCodecs.VAR_INT, MenuActionPayload::entityId,
                PacketCodecs.VAR_INT, MenuActionPayload::action,
                PacketCodecs.VAR_INT, MenuActionPayload::value,
                MenuActionPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public static void registerCommon() {
        PayloadTypeRegistry.playS2C().register(OpenMenuPayload.ID, OpenMenuPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(MenuActionPayload.ID, MenuActionPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(QuickMenuRequestPayload.ID, QuickMenuRequestPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(QuickMenuPayload.ID, QuickMenuPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(TraitsPayload.OPEN, TraitsPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SaveTraitsPayload.ID, SaveTraitsPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(KidNamePayload.ID, KidNamePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(KidNamePayload.ID, KidNamePayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SaveTraitsPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            context.server().execute(() -> {
                com.jaciel.kiko.family.Traits.set(player, new com.jaciel.kiko.family.Traits.PlayerTraits(
                        payload.male(), payload.skin(), payload.hair(), payload.eyes()));
                player.sendMessage(Text.translatable("message.kiko.traits_saved").formatted(net.minecraft.util.Formatting.LIGHT_PURPLE), true);
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(KidNamePayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            context.server().execute(() -> {
                if (player.getWorld().getEntityById(payload.entityId()) instanceof com.jaciel.kiko.family.KikoChildEntity kid) {
                    com.jaciel.kiko.family.Family.rename(player, kid, payload.name());
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(QuickMenuRequestPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            context.server().execute(() -> sendQuickMenu(player));
        });

        ServerPlayNetworking.registerGlobalReceiver(MenuActionPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            context.server().execute(() -> handleAction(player, payload));
        });
    }

    /** Manda la lista de tus Kikos (las que te tienen de mejor amigo, o las que estén muy cerca). */
    public static void sendQuickMenu(ServerPlayerEntity player) {
        java.util.List<KikoEntity> list = new java.util.ArrayList<>(com.jaciel.kiko.entity.KikoCalls.mine(player, 4096));
        for (KikoEntity k : player.getServerWorld().getEntitiesByType(net.minecraft.util.TypeFilter.instanceOf(KikoEntity.class),
                k -> k.isAlive() && !k.isTownsfolk() && k.squaredDistanceTo(player) < 16 * 16)) {
            if (!list.contains(k)) list.add(k);
        }
        java.util.List<KikoInfo> infos = new java.util.ArrayList<>();
        for (KikoEntity k : list) {
            if (infos.size() >= 12) break;
            int flags = (k.isSleeping() ? INFO_SLEEPING : 0) | (k.isAway() ? INFO_AWAY : 0)
                    | (k.isFollowEnabled() ? INFO_FOLLOWING : 0) | (k.hasPet() ? INFO_PET : 0)
                    | (k.getHome() != null ? INFO_HOME : 0);
            infos.add(new KikoInfo(k.getId(), k.getDisplayName().getString(), k.getWorkMode().ordinal(),
                    (int) Math.round(Math.sqrt(k.squaredDistanceTo(player))),
                    Math.round(100f * k.getHealth() / k.getMaxHealth()), flags));
        }
        ServerPlayNetworking.send(player, new QuickMenuPayload(infos));
    }

    public static void openMenu(ServerPlayerEntity player, KikoEntity kiko) {
        // v3.4 los vecinos del pueblo no tienen menú (solo platican y reciben regalos)
        if (kiko.isTownsfolk()) {
            // v3.5 los que tienen oficio de tiendita te venden (domingos más barato)
            if (kiko instanceof com.jaciel.kiko.town.TownsfolkEntity t && !t.isTownKid()
                    && com.jaciel.kiko.town.TownShop.sells(t.getJobId())) {
                com.jaciel.kiko.town.TownShop.open(player, t);
                return;
            }
            player.sendMessage(Text.translatable("message.kiko.town_no_menu", kiko.getDisplayName()).formatted(net.minecraft.util.Formatting.GRAY), true);
            return;
        }
        kiko.holdStill(player, 1200); // quieta mientras tengas el menú abierto (máximo 1 minuto)
        int flags = (kiko.isFollowEnabled() ? FLAG_FOLLOWING : 0)
                | (kiko.isAutoOutfit() ? FLAG_AUTO_OUTFIT : 0)
                | (kiko.isArmorHidden() ? FLAG_HIDE_ARMOR : 0)
                | (kiko.getTunnelSize() >= 3 ? FLAG_TUNNEL_3X3 : 0)
                | (kiko.getHome() != null ? FLAG_HAS_HOME : 0)
                | (kiko.hasPet() ? FLAG_HAS_PET : 0)
                | (player.getUuid().equals(kiko.getPartnerUuid()) && kiko.getRelation() == com.jaciel.kiko.romance.Romance.GIRLFRIEND ? FLAG_GIRLFRIEND : 0)
                | (player.getUuid().equals(kiko.getPartnerUuid()) && kiko.getRelation() == com.jaciel.kiko.romance.Romance.WIFE ? FLAG_WIFE : 0)
                | (kiko.getGirlOutfit() << GIRL_OUTFIT_SHIFT)
                | (kiko.getChosenGirlOutfit() << CHOSEN_OUTFIT_SHIFT)
                | (kiko.isKid() ? FLAG_KID : 0)
                | (kiko.isMinor() ? FLAG_MINOR : 0)
                | (kiko instanceof com.jaciel.kiko.family.KikoChildEntity kid && player.getUuid().equals(kid.getParentUuid()) ? FLAG_MY_KID : 0)
                | (kiko instanceof com.jaciel.kiko.family.KikoChildEntity kid2 && kid2.isMale() ? FLAG_KID_MALE : 0);
        ServerPlayNetworking.send(player, new OpenMenuPayload(
                kiko.getId(),
                kiko.getChosenOutfit().ordinal(),
                kiko.getAffection().get(player.getUuid()),
                kiko.getWorkMode().ordinal(),
                kiko.getCombatMode().ordinal(),
                flags));
    }

    private static void handleAction(ServerPlayerEntity player, MenuActionPayload payload) {
        if (payload.action() == ACTION_TRAITS) {
            openTraits(player);
            return;
        }
        if (payload.action() == ACTION_CALL_ALL) {
            int n = com.jaciel.kiko.entity.KikoCalls.callAll(player);
            player.sendMessage(n > 0 ? Text.translatable("message.kiko.whistle_ok", n)
                    : Text.translatable("message.kiko.whistle_none"), true);
            return;
        }
        Entity e = player.getWorld().getEntityById(payload.entityId());
        if (!(e instanceof KikoEntity kiko) || !kiko.isAlive() || kiko.isTownsfolk()) return;
        // Cerca, o de lejos si eres su mejor amigo (menú rápido con la tecla K)
        boolean near = player.squaredDistanceTo(kiko) <= 10 * 10;
        boolean friend = player.getUuid().equals(kiko.getFriendUuid());
        if (!near && !friend) return;
        if (kiko.isAway() && payload.action() != ACTION_MENU_CLOSED) {
            player.sendMessage(Text.translatable("message.kiko.away", Math.max(1, kiko.getAwayTicks() / 1200)), true);
            return;
        }

        switch (payload.action()) {
            case ACTION_OUTFIT -> {
                if (kiko.isKid()) return;
                KikoOutfit outfit = KikoOutfit.byIndex(payload.value());
                if (outfit == kiko.getOutfit() && outfit == kiko.getChosenOutfit()) return;
                if (!com.jaciel.kiko.romance.Romance.canWear(kiko, outfit) || !player.getUuid().equals(kiko.getPartnerUuid()) && outfit == KikoOutfit.WEDDING) return;
                kiko.setChosenOutfit(outfit);
                kiko.playAnimation(KikoAnimation.HAPPY);
                kiko.spawnParticles(ParticleTypes.HAPPY_VILLAGER, 8);
                kiko.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER.value(), 1f, 1.1f);
                KikoInteractionHandler.say(player, kiko, "dialogue.kiko.outfit", 3);
            }
            case ACTION_GIRL_OUTFIT -> {
                if (kiko.isKid()) return;
                int o = payload.value();
                if (o < 0 || o > 4 || (o == kiko.getGirlOutfit() && o == kiko.getChosenGirlOutfit())) return;
                // El vestido de novia solo se lo pone para su esposo
                if (o == com.jaciel.kiko.romance.Romance.OUTFIT_WEDDING && !(kiko.getRelation() == com.jaciel.kiko.romance.Romance.WIFE
                        && player.getUuid().equals(kiko.getPartnerUuid()))) return;
                kiko.chooseGirlOutfit(o);
                kiko.playAnimation(KikoAnimation.HAPPY);
                kiko.spawnParticles(ParticleTypes.HAPPY_VILLAGER, 8);
                kiko.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER.value(), 1f, 1.1f);
                KikoInteractionHandler.say(player, kiko, "dialogue.kiko.outfit", 3);
            }
            case ACTION_TOGGLE_FOLLOW -> {
                kiko.setFollowEnabled(!kiko.isFollowEnabled());
                KikoInteractionHandler.say(player, kiko,
                        kiko.isFollowEnabled() ? "dialogue.kiko.follow_on" : "dialogue.kiko.follow_off", 1);
            }
            case ACTION_OPEN_INVENTORY -> {
                if (player.squaredDistanceTo(kiko) > 8 * 8) {
                    player.sendMessage(Text.translatable("message.kiko.too_far"), true);
                    return;
                }
                if (!checkAffection(player, kiko, KikoConfig.get().inventoryThreshold)) return;
                openInventory(player, kiko);
                kiko.releaseHold(); // desde aquí la detiene el inventario abierto
            }
            case ACTION_FAMILY -> com.jaciel.kiko.family.Family.request(player, kiko);
            case ACTION_DATE -> com.jaciel.kiko.romance.Dates.invite(player, kiko);
            case ACTION_TRADE -> {
                if (kiko.getCharacter() == com.jaciel.kiko.entity.KikoCharacter.BETO && !kiko.isKid() && !kiko.isSleeping()) {
                    com.jaciel.kiko.entity.BetoShop.open(player, kiko);
                }
            }
            case ACTION_SET_WORK -> {
                WorkMode mode = WorkMode.byIndex(payload.value());
                if (kiko.isMinor() && mode != WorkMode.NONE) {
                    player.sendMessage(Text.translatable("message.kiko.kid_too_young"), true);
                    return;
                }
                if (mode != WorkMode.NONE && !checkAffection(player, kiko, KikoConfig.get().workThreshold)) {
                    openMenu(player, kiko); // regresa el menú con el trabajo de antes
                    return;
                }
                if (kiko.isSleeping()) kiko.wakeUp();
                kiko.setWorkMode(mode, player);
                KikoInteractionHandler.say(player, kiko, "dialogue.kiko.work." + mode.id(), 1);
                if (mode == WorkMode.EXPLORE) {
                    kiko.releaseHold();
                    com.jaciel.kiko.entity.work.Expedition.start(kiko);
                }
            }
            case ACTION_CYCLE_COMBAT -> {
                if (kiko.isMinor()) {
                    player.sendMessage(Text.translatable("message.kiko.kid_too_young"), true);
                    return;
                }
                CombatMode next = kiko.getCombatMode().next();
                kiko.setCombatMode(next);
                KikoInteractionHandler.say(player, kiko, "dialogue.kiko.combat." + next.id(), 1);
            }
            case ACTION_TOGGLE_AUTO_OUTFIT -> kiko.setAutoOutfit(!kiko.isAutoOutfit());
            case ACTION_TOGGLE_HIDE_ARMOR -> kiko.setArmorHidden(!kiko.isArmorHidden());
            case ACTION_TOGGLE_TUNNEL_SIZE -> kiko.setTunnelSize(kiko.getTunnelSize() >= 3 ? 1 : 3);
            case ACTION_MENU_CLOSED -> kiko.releaseHold();
            case ACTION_SET_HOME -> {
                if (kiko.getHome() != null) {
                    kiko.setHome(null);
                    KikoInteractionHandler.say(player, kiko, "dialogue.kiko.home_cleared", 1);
                } else {
                    // Su casa es la cama de Kiko más cercana (si hay), si no donde está parada
                    BlockPos home = kiko.getBlockPos();
                    for (BlockPos p : BlockPos.iterateOutwards(kiko.getBlockPos(), 8, 3, 8)) {
                        if (kiko.getWorld().getBlockState(p).getBlock() instanceof com.jaciel.kiko.block.KikoBedBlock) {
                            home = p.toImmutable();
                            break;
                        }
                    }
                    kiko.setHome(home);
                    KikoInteractionHandler.say(player, kiko, "dialogue.kiko.home_set", 1);
                }
            }
            case ACTION_CALL -> com.jaciel.kiko.entity.KikoCalls.call(kiko, player);
            case ACTION_REMOTE_MENU -> openMenu(player, kiko);
            case ACTION_RELEASE_PET -> {
                var pet = kiko.getPet();
                kiko.setPet(null);
                if (pet != null) {
                    if (pet.squaredDistanceTo(player) < 10 * 10) pet.attachLeash(player, true);
                    else pet.detachLeash(true, true);
                }
                KikoInteractionHandler.say(player, kiko, "dialogue.kiko.pet_return", 1);
            }
            case ACTION_DEPOSIT -> {
                int moved = depositToNearbyContainer(kiko);
                if (moved < 0) KikoInteractionHandler.say(player, kiko, "dialogue.kiko.no_chest", 1);
                else if (moved == 0) KikoInteractionHandler.say(player, kiko, "dialogue.kiko.nothing_to_store", 1);
                else {
                    kiko.playSound(SoundEvents.BLOCK_CHEST_CLOSE, 0.6f, 1.2f);
                    player.sendMessage(Text.translatable("message.kiko.stored", moved), true);
                    KikoInteractionHandler.say(player, kiko, "dialogue.kiko.stored", 1);
                }
            }
            default -> {}
        }
    }

    private static boolean checkAffection(ServerPlayerEntity player, KikoEntity kiko, float needed) {
        if (kiko.getAffection().get(player.getUuid()) >= needed) return true;
        KikoInteractionHandler.say(player, kiko, "dialogue.kiko.not_enough_trust", 1);
        player.sendMessage(Text.translatable("message.kiko.need_affection", Math.round(needed)), true);
        return false;
    }

    public static void openInventory(ServerPlayerEntity player, KikoEntity kiko) {
        player.openHandledScreen(new ExtendedScreenHandlerFactory<Integer>() {
            @Override
            public Integer getScreenOpeningData(ServerPlayerEntity p) {
                return kiko.getId();
            }

            @Override
            public Text getDisplayName() {
                return Text.translatable("screen.kiko.inventory", kiko.getDisplayName());
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity p) {
                return new KikoScreenHandler(syncId, inv, kiko.getInventory(), kiko.getId());
            }
        });
    }

    /**
     * Pasa la mochila (menos lo útil) al cofre, barril o contenedor más cercano (6 bloques).
     * Ese contenedor queda como "su cofre" para cuando guarde sola.
     * @return cuántos ítems pasó, o -1 si no hay contenedor
     */
    private static int depositToNearbyContainer(KikoEntity kiko) {
        for (BlockPos pos : BlockPos.iterateOutwards(kiko.getBlockPos(), 6, 3, 6)) {
            BlockEntity be = kiko.getWorld().getBlockEntity(pos);
            if (be instanceof Inventory inv && inv.size() >= 9) {
                kiko.setHomeChest(pos.toImmutable());
                return kiko.getInventory().depositInto(inv);
            }
        }
        return -1;
    }
}
