package com.jaciel.kiko.recipe;

import com.jaciel.kiko.item.Drink;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Refrescos (v3.6). Botella de agua (la poción de agua) + azúcar + un sabor.
 *  cacao → Kiko-Cola, zanahoria → Naranjiko, hojas → Fresquiko de limón
 */
public class BottleDrinkRecipe extends SpecialCraftingRecipe {
    public BottleDrinkRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    public static boolean isWaterBottle(ItemStack s) {
        if (!s.isOf(Items.POTION)) return false;
        PotionContentsComponent c = s.get(DataComponentTypes.POTION_CONTENTS);
        return c != null && c.matches(Potions.WATER);
    }

    @Nullable
    private static Drink flavor(ItemStack s) {
        if (s.isOf(Items.COCOA_BEANS)) return Drink.KIKO_COLA;
        if (s.isOf(Items.CARROT)) return Drink.NARANJIKO;
        if (s.isIn(ItemTags.LEAVES)) return Drink.FRESQUIKO;
        return null;
    }

    @Nullable
    private Drink find(CraftingRecipeInput input) {
        boolean water = false, sugar = false;
        Drink d = null;
        for (int i = 0; i < input.getSize(); i++) {
            ItemStack s = input.getStackInSlot(i);
            if (s.isEmpty()) continue;
            if (isWaterBottle(s) && !water) water = true;
            else if (s.isOf(Items.SUGAR) && !sugar) sugar = true;
            else if (d == null && flavor(s) != null) d = flavor(s);
            else return null;
        }
        return water && sugar ? d : null;
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        return find(input) != null;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        Drink d = find(input);
        return d == null ? ItemStack.EMPTY : new ItemStack(d.item);
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 3;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipes.BOTTLE_DRINK;
    }
}
