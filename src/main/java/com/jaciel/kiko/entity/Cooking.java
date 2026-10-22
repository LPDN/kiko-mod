package com.jaciel.kiko.entity;

import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import com.jaciel.kiko.item.Dish;
import com.jaciel.kiko.item.DishItem;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cocina. Todas las chicas cocinan: agáchate y dale clic derecho con ingredientes crudos en la mano.
 *  - Cualquiera: 2 ingredientes = 1 platillo (máximo 8 por tanda)
 *  - Tami (chef): 1 ingrediente = 1 platillo de chef (máximo 16), que llena más y trae un efecto
 * Se queda quieta unos segundos cocinando (humito y sonido) y luego te da los platillos.
 */
public final class Cooking {
    private Cooking() {}

    /** Lo que está cocinando ahorita. */
    public static final class Job {
        final UUID player;
        final List<ItemStack> result;
        int ticks;

        Job(UUID player, List<ItemStack> result, int ticks) {
            this.player = player;
            this.result = result;
            this.ticks = ticks;
        }
    }

    /** Qué platillo sale de este ingrediente (o null si no se cocina). */
    @Nullable
    public static Dish dishFor(ItemStack s) {
        if (s.isEmpty() || s.getItem() instanceof DishItem) return null;
        if (s.isOf(Items.CHICKEN)) return Dish.CALDO_DE_POLLO;
        if (s.isOf(Items.BEEF) || s.isOf(Items.PORKCHOP) || s.isOf(Items.MUTTON) || s.isOf(Items.RABBIT)) return Dish.GUISADO;
        if (s.isOf(Items.COD) || s.isOf(Items.SALMON)) return Dish.RAMEN;
        if (s.isOf(Items.POTATO)) return Dish.PURE_DE_PAPA;
        if (s.isOf(Items.CARROT)) return Dish.CREMA_DE_ZANAHORIA;
        if (s.isOf(Items.EGG)) return Dish.HUEVOS_RANCHEROS;
        if (s.isOf(Items.WHEAT)) return Dish.CONCHA;
        if (s.isOf(Items.APPLE) || s.isOf(Items.SWEET_BERRIES) || s.isOf(Items.GLOW_BERRIES) || s.isOf(Items.MELON_SLICE)) {
            return Dish.PAY_DE_FRUTAS;
        }
        // Carne y pescado crudo de otros mods
        if (s.isIn(ConventionalItemTags.RAW_FISH_FOODS)) return Dish.RAMEN;
        if (s.isIn(ConventionalItemTags.RAW_MEAT_FOODS)) return Dish.GUISADO;
        return null;
    }

    public static boolean isChef(KikoEntity kiko) {
        return kiko.getCharacter() == KikoCharacter.TAMI;
    }

    /** Hace un platillo (de chef si lo hizo Tami). */
    public static ItemStack make(Dish dish, int count, boolean chef) {
        ItemStack out = new ItemStack(dish.item, count);
        if (chef) {
            out.set(DataComponentTypes.FOOD, dish.chefFood());
            out.set(DataComponentTypes.ITEM_NAME, Text.translatable("item.kiko.chef_dish", Text.translatable(dish.item.getTranslationKey())));
            out.set(DataComponentTypes.LORE, new LoreComponent(List.of(
                    Text.translatable("item.kiko.chef_dish.lore").formatted(Formatting.GOLD))));
        }
        return out;
    }

    /** Clic agachado con ingredientes. @return true si empezó a cocinar (o explicó por qué no). */
    public static boolean tryStart(KikoEntity kiko, PlayerEntity player, ItemStack stack) {
        Dish dish = dishFor(stack);
        if (dish == null) return false;
        if (kiko.getCookJob() != null) {
            player.sendMessage(Text.translatable("message.kiko.cook_busy").formatted(Formatting.GRAY), true);
            return true;
        }
        boolean chef = isChef(kiko);
        int per = chef ? 1 : 2;
        int dishes = Math.min(stack.getCount() / per, chef ? 16 : 8);
        if (dishes <= 0) {
            player.sendMessage(Text.translatable("message.kiko.cook_need_two").formatted(Formatting.GRAY), true);
            return true;
        }
        if (!player.getAbilities().creativeMode) stack.decrement(dishes * per);
        int ticks = chef ? 30 + dishes * 4 : 50 + dishes * 8;
        List<ItemStack> result = new ArrayList<>();
        result.add(make(dish, dishes, chef));
        kiko.setCookJob(new Job(player.getUuid(), result, ticks));
        kiko.holdStill(player, ticks + 10);
        kiko.setExpression(KikoExpression.GRIN, 60);
        kiko.swingHand(Hand.MAIN_HAND);
        kiko.getWorld().playSound(null, kiko.getBlockPos(), SoundEvents.BLOCK_SMOKER_SMOKE, SoundCategory.NEUTRAL, 1f, 1f);
        KikoInteractionHandler.say(player, kiko, "dialogue.kiko.cook_start", 3);
        return true;
    }

    /** Cada tick mientras cocina. */
    public static void tick(KikoEntity kiko) {
        Job job = kiko.getCookJob();
        if (job == null || !(kiko.getWorld() instanceof ServerWorld world)) return;
        PlayerEntity p = world.getPlayerByUuid(job.player);
        if (p != null && job.ticks > 5) kiko.holdStill(p, 10);
        if (job.ticks % 8 == 0) {
            world.spawnParticles(ParticleTypes.SMOKE, kiko.getX(), kiko.getBodyY(0.7), kiko.getZ(), 2, 0.25, 0.1, 0.25, 0.01);
            if (kiko.getRandom().nextInt(3) == 0) {
                world.spawnParticles(ParticleTypes.CRIT, kiko.getX(), kiko.getBodyY(0.6), kiko.getZ(), 2, 0.2, 0.1, 0.2, 0.05);
            }
        }
        if (job.ticks % 20 == 0) {
            world.playSound(null, kiko.getBlockPos(), SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.NEUTRAL, 0.8f, 1.1f);
            kiko.swingHand(Hand.MAIN_HAND);
        }
        if (--job.ticks > 0) return;

        kiko.setCookJob(null);
        kiko.releaseHold();
        world.playSound(null, kiko.getBlockPos(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.NEUTRAL, 0.5f, 1.4f);
        world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, kiko.getX(), kiko.getBodyY(0.8), kiko.getZ(), 8, 0.4, 0.4, 0.4, 0);
        kiko.setExpression(KikoExpression.LAUGH, 60);
        for (ItemStack s : job.result) {
            if (p != null && p.squaredDistanceTo(kiko) < 16 * 16) {
                p.getInventory().offerOrDrop(s);
            } else {
                ItemStack rest = kiko.getInventory().insert(s);
                if (!rest.isEmpty()) kiko.dropStack(rest);
            }
        }
        if (p != null) {
            kiko.getAffection().add(p.getUuid(), 1f, world.getTime());
            KikoInteractionHandler.say(p, kiko, "dialogue.kiko.cook_done", 3);
        }
    }

    /**
     * Tami cocina sola lo crudo que trae en la mochila (de 4 en 4).
     * @return true si cocinó algo
     */
    public static boolean chefCookBag(KikoEntity kiko) {
        var bag = kiko.getInventory().bag();
        for (int i = 0; i < bag.size(); i++) {
            ItemStack s = bag.get(i);
            Dish dish = dishFor(s);
            if (dish == null) continue;
            int n = Math.min(4, s.getCount());
            s.decrement(n);
            ItemStack rest = kiko.getInventory().insert(make(dish, n, true));
            if (!rest.isEmpty()) kiko.dropStack(rest);
            return true;
        }
        return false;
    }

    /** Un platillo de su mochila para darte de comer (índice o -1). */
    public static int findDish(KikoEntity kiko) {
        return kiko.getInventory().findInBag(s -> s.getItem() instanceof DishItem);
    }
}
