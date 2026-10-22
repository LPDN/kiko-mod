package com.jaciel.kiko.item;

import com.jaciel.kiko.entity.KikoCalls;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

/** Silbato: trae a tus Kikos (las que te quieren lo suficiente para seguirte) a donde estés. */
public class KikoWhistleItem extends Item {
    public KikoWhistleItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_FLUTE.value(), SoundCategory.PLAYERS, 1f, 1.8f);
        world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_FLUTE.value(), SoundCategory.PLAYERS, 0.8f, 2.0f);
        if (!world.isClient && user instanceof ServerPlayerEntity sp) {
            int n = KikoCalls.callAll(sp);
            sp.sendMessage(n > 0 ? Text.translatable("message.kiko.whistle_ok", n).formatted(Formatting.LIGHT_PURPLE)
                    : Text.translatable("message.kiko.whistle_none").formatted(Formatting.GRAY), true);
        }
        user.getItemCooldownManager().set(this, 60);
        return TypedActionResult.success(stack, world.isClient);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("item.kiko.whistle.tooltip").formatted(Formatting.GRAY));
    }
}
