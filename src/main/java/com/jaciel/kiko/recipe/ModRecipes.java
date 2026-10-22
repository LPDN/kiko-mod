package com.jaciel.kiko.recipe;

import com.jaciel.kiko.KikoMod;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class ModRecipes {
    private ModRecipes() {}

    public static final RecipeSerializer<BottleDrinkRecipe> BOTTLE_DRINK = Registry.register(Registries.RECIPE_SERIALIZER,
            KikoMod.id("bottle_drink"), new net.minecraft.recipe.SpecialRecipeSerializer<>(BottleDrinkRecipe::new));

    public static void register() {}
}
