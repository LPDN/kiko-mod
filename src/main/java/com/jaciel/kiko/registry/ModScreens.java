package com.jaciel.kiko.registry;

import com.jaciel.kiko.KikoMod;
import com.jaciel.kiko.inventory.KikoScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

/** Tipo de pantalla del inventario. Manda el id de la entidad al abrirla. */
public final class ModScreens {
    public static final ScreenHandlerType<KikoScreenHandler> KIKO_INVENTORY = Registry.register(
            Registries.SCREEN_HANDLER,
            KikoMod.id("kiko_inventory"),
            new ExtendedScreenHandlerType<>(KikoScreenHandler::new, PacketCodecs.VAR_INT));

    private ModScreens() {}

    public static void register() {}
}
