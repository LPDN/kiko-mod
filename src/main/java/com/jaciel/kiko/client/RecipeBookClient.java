package com.jaciel.kiko.client;

import com.jaciel.kiko.item.RecipeBookItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/** Abre el Recetario de Tami con la pantalla de libro (solo cliente). */
@Environment(EnvType.CLIENT)
public final class RecipeBookClient {
    private RecipeBookClient() {}

    public static void open() {
        List<Text> pages = new ArrayList<>();
        for (int i = 1; i <= RecipeBookItem.PAGES; i++) {
            pages.add(Text.translatable("item.kiko.recipe_book.page." + i));
        }
        MinecraftClient.getInstance().setScreen(new BookScreen(new BookScreen.Contents(pages)));
    }
}
