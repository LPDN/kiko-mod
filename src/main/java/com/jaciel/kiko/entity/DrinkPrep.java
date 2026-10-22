package com.jaciel.kiko.entity;

import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import com.jaciel.kiko.item.Drink;
import com.jaciel.kiko.item.DrinkItem;
import com.jaciel.kiko.recipe.BottleDrinkRecipe;
import com.jaciel.kiko.registry.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Preparación de bebidas (v3.6). Agáchate con un envase en la mano (botella de agua, vaso, jarrito o lata vacía)
 * y la chica busca en tu inventario lo que necesita y te prepara las bebidas. Tami las hace con el doble de duración.
 */
public final class DrinkPrep {
    private DrinkPrep() {}

    private record Req(Predicate<ItemStack> test, int count) {}

    private record Recipe(Drink drink, Predicate<ItemStack> container, int yield, Req... reqs) {}

    private static Predicate<ItemStack> of(Item... items) {
        return s -> {
            for (Item i : items) if (s.isOf(i)) return true;
            return false;
        };
    }

    private static final Predicate<ItemStack> WATER = BottleDrinkRecipe::isWaterBottle;
    private static final Predicate<ItemStack> CUP = s -> s.isOf(ModItems.CUP);
    private static final Predicate<ItemStack> MUG = s -> s.isOf(ModItems.MUG);
    private static final Predicate<ItemStack> CAN = s -> s.isOf(ModItems.EMPTY_CAN);
    private static final Req SUGAR = new Req(of(Items.SUGAR), 1);
    private static final Req MILK = new Req(of(Items.MILK_BUCKET), 1);

    private static final List<Recipe> RECIPES = List.of(
            new Recipe(Drink.KIKO_COLA, WATER, 1, SUGAR, new Req(of(Items.COCOA_BEANS), 1)),
            new Recipe(Drink.NARANJIKO, WATER, 1, SUGAR, new Req(of(Items.CARROT), 1)),
            new Recipe(Drink.FRESQUIKO, WATER, 1, SUGAR, new Req(s -> s.isIn(ItemTags.LEAVES), 1)),
            new Recipe(Drink.HORCHATA, CUP, 4, new Req(of(Items.WHEAT), 1), SUGAR, MILK),
            new Recipe(Drink.JAMAICA, CUP, 4, new Req(of(Items.POPPY, Items.RED_TULIP), 2), SUGAR),
            new Recipe(Drink.AGUA_SANDIA, CUP, 4, new Req(of(Items.MELON_SLICE), 4)),
            new Recipe(Drink.LICUADO_FRESA, CUP, 4, new Req(of(Items.SWEET_BERRIES), 4), MILK),
            new Recipe(Drink.LICUADO_MANZANA, CUP, 4, new Req(of(Items.APPLE), 2), MILK),
            new Recipe(Drink.CAFE_DE_OLLA, MUG, 1, new Req(of(Items.COCOA_BEANS), 1), SUGAR, new Req(of(Items.STICK), 1)),
            new Recipe(Drink.CHOCOLATE, MUG, 4, new Req(of(Items.COCOA_BEANS), 2), MILK),
            new Recipe(Drink.CHAMPURRADO, MUG, 1, new Req(of(Items.WHEAT), 1), new Req(of(Items.COCOA_BEANS), 1)),
            new Recipe(Drink.TE_MANZANILLA, MUG, 1, new Req(of(Items.OXEYE_DAISY), 2)),
            new Recipe(Drink.CHUPACABRAS, CAN, 1, SUGAR, new Req(of(Items.GLOWSTONE_DUST), 1)));

    public static boolean isContainer(ItemStack s) {
        if (s.isEmpty()) return false;
        for (Recipe r : RECIPES) if (r.container().test(s)) return true;
        return false;
    }

    private static int count(PlayerEntity p, Predicate<ItemStack> t) {
        int n = 0;
        for (ItemStack s : p.getInventory().main) if (!s.isEmpty() && t.test(s)) n += s.getCount();
        return n;
    }

    private static void take(PlayerEntity p, Predicate<ItemStack> t, int n) {
        for (ItemStack s : p.getInventory().main) {
            if (n <= 0) return;
            if (s.isEmpty() || !t.test(s)) continue;
            int k = Math.min(n, s.getCount());
            if (s.isOf(Items.MILK_BUCKET)) {
                for (int i = 0; i < k; i++) p.getInventory().offerOrDrop(new ItemStack(Items.BUCKET));
            }
            s.decrement(k);
            n -= k;
        }
    }

    /** @return true si hizo algo o explicó por qué no. */
    public static boolean tryStart(KikoEntity kiko, PlayerEntity player, ItemStack held) {
        if (kiko.getCookJob() != null) {
            player.sendMessage(Text.translatable("message.kiko.cook_busy").formatted(Formatting.GRAY), true);
            return true;
        }
        boolean chef = Cooking.isChef(kiko);
        boolean creative = player.getAbilities().creativeMode;
        for (Recipe r : RECIPES) {
            if (!r.container().test(held)) continue;
            int batches = held.getCount() / r.yield();
            for (Req q : r.reqs()) batches = Math.min(batches, count(player, q.test()) / q.count());
            batches = Math.min(batches, (chef ? 16 : 8) / r.yield());
            if (batches <= 0) continue;
            int made = batches * r.yield();
            if (!creative) {
                held.decrement(made);
                for (Req q : r.reqs()) take(player, q.test(), q.count() * batches);
            }
            List<ItemStack> result = new ArrayList<>();
            result.add(chef ? DrinkItem.chefMade(r.drink(), made) : new ItemStack(r.drink().item, made));
            int ticks = chef ? 30 + made * 3 : 50 + made * 6;
            kiko.setCookJob(new Cooking.Job(player.getUuid(), result, ticks));
            kiko.holdStill(player, ticks + 10);
            kiko.setExpression(KikoExpression.GRIN, 60);
            kiko.swingHand(Hand.MAIN_HAND);
            kiko.getWorld().playSound(null, kiko.getBlockPos(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1f, 1f);
            KikoInteractionHandler.say(player, kiko, "dialogue.kiko.cook_start", 3);
            return true;
        }
        player.sendMessage(Text.translatable("message.kiko.drink_missing").formatted(Formatting.GRAY), true);
        return true;
    }
}
