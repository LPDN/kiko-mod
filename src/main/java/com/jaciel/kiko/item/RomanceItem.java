package com.jaciel.kiko.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

/**
 * Ramo de flores, anillo y Anillo del Abismo (v3.0). Se usan con clic derecho sobre una chica
 * (lo atiende KikoInteractionHandler → Romance). Aquí solo va su descripción.
 */
public class RomanceItem extends Item {
    private final String tooltipKey;
    private final Formatting color;

    public RomanceItem(String tooltipKey, Formatting color, Settings settings) {
        super(settings);
        this.tooltipKey = tooltipKey;
        this.color = color;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable(tooltipKey).formatted(color));
        tooltip.add(Text.translatable(tooltipKey + ".2").formatted(Formatting.GRAY));
    }
}
