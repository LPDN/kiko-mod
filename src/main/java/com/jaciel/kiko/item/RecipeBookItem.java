package com.jaciel.kiko.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * Recetario de Tami (v3.6.3). Un libro que se lee con clic derecho y trae todo lo de cocina y bebidas.
 * Las páginas son textos traducibles (item.kiko.recipe_book.page.N), así que salen en el idioma del juego.
 * Se abre con la pantalla de libro del cliente (BookScreen.Contents.create solo acepta libros de vanilla).
 */
public class RecipeBookItem extends Item {
    public static final int PAGES = 11;

    public RecipeBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) com.jaciel.kiko.client.RecipeBookClient.open();
        return TypedActionResult.success(stack, world.isClient());
    }
}
