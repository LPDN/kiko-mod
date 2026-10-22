package com.jaciel.kiko.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

/**
 * Platillo que cocinan las chicas. Rinde más que la comida normal (más saturación).
 * Los que vienen en plato hondo te regresan el plato al comértelos.
 */
public class DishItem extends Item {
    private final boolean bowl;

    public DishItem(boolean bowl, Settings settings) {
        super(settings);
        this.bowl = bowl;
    }

    public boolean usesBowl() {
        return bowl;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack result = super.finishUsing(stack, world, user);
        if (!bowl || !(user instanceof PlayerEntity p) || p.getAbilities().creativeMode) return result;
        ItemStack plate = new ItemStack(Items.BOWL);
        if (result.isEmpty()) return plate;
        if (!p.getInventory().insertStack(plate)) p.dropItem(plate, false);
        return result;
    }
}
