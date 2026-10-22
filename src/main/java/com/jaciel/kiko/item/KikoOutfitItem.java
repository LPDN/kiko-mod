package com.jaciel.kiko.item;

import com.jaciel.kiko.outfit.KikoOutfit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

/**
 * Conjunto de ropa. Clic derecho sobre Kiko para que se lo ponga.
 * No se gasta, funciona como "guardarropa".
 */
public class KikoOutfitItem extends Item {
    private final KikoOutfit outfit;

    public KikoOutfitItem(KikoOutfit outfit, Settings settings) {
        super(settings);
        this.outfit = outfit;
    }

    public KikoOutfit getOutfit() {
        return outfit;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("item.kiko.outfit.tooltip").formatted(Formatting.GRAY));
    }
}
