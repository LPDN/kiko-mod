package com.jaciel.kiko.item;

import com.jaciel.kiko.entity.KikoCharacter;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Bebida (v3.6). Se toma como poción, da su efecto y regresa el envase. */
public class DrinkItem extends Item {
    private final Drink drink;

    public DrinkItem(Drink drink, Settings settings) {
        super(settings);
        this.drink = drink;
    }

    public Drink getDrink() {
        return drink;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 32;
    }

    public static boolean isChef(ItemStack stack) {
        NbtComponent c = stack.get(DataComponentTypes.CUSTOM_DATA);
        return c != null && c.copyNbt().getBoolean("chef");
    }

    /** Bebida hecha por Tami (dura el doble). */
    public static ItemStack chefMade(Drink d, int count) {
        ItemStack s = new ItemStack(d.item, count);
        NbtCompound tag = new NbtCompound();
        tag.putBoolean("chef", true);
        s.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
        s.set(DataComponentTypes.ITEM_NAME, Text.translatable("item.kiko.chef_dish", Text.translatable(d.item.getTranslationKey())));
        s.set(DataComponentTypes.LORE, new net.minecraft.component.type.LoreComponent(List.of(
                Text.translatable("item.kiko.chef_drink.lore").formatted(Formatting.GOLD))));
        return s;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        boolean chef = isChef(stack);
        ItemStack result = super.finishUsing(stack, world, user);
        if (!world.isClient) apply(drink, user, chef);
        Item cont = drink.container();
        if (cont == null || !(user instanceof PlayerEntity p) || p.getAbilities().creativeMode) return result;
        ItemStack back = new ItemStack(cont);
        if (result.isEmpty()) return back;
        if (!p.getInventory().insertStack(back)) p.dropItem(back, false);
        return result;
    }

    // ---------- efectos ----------

    private record Later(UUID who, RegistryKey<World> world, long at, StatusEffectInstance fx, boolean warm) {}

    private static final List<Later> LATER = new ArrayList<>();
    private static final Map<UUID, long[]> SODAS = new HashMap<>();
    private static final Map<UUID, Long> WARM = new HashMap<>();

    public static void apply(Drink d, LivingEntity user, boolean chef) {
        int mul = chef ? 2 : 1;
        for (Drink.Fx f : d.effects) {
            user.addStatusEffect(new StatusEffectInstance(f.effect(), f.ticks() * mul, f.amp()));
        }
        if (d.heal > 0) user.heal(d.heal * (chef ? 1.5f : 1f));
        if (d == Drink.CHUPACABRAS && user.getWorld() instanceof ServerWorld sw) {
            // Cuando se acaba el efecto te cae el hambre de golpe
            LATER.add(new Later(user.getUuid(), sw.getRegistryKey(), sw.getTime() + 1200L * mul,
                    new StatusEffectInstance(StatusEffects.HUNGER, 400, 2), false));
        }
        if (d == Drink.KIKO_COLA && user.getWorld() instanceof ServerWorld sw) {
            LATER.add(new Later(user.getUuid(), sw.getRegistryKey(), sw.getTime() + 600L * mul,
                    new StatusEffectInstance(StatusEffects.SLOWNESS, 160, 0), false));
        }
        if (d.warm && user.getWorld() instanceof ServerWorld sw) {
            user.setFrozenTicks(0);
            WARM.put(user.getUuid(), sw.getTime() + 3600L * mul);
        }
        if (d == Drink.TE_MANZANILLA && user instanceof PlayerEntity p) {
            p.sendMessage(Text.translatable("message.kiko.tea_sleepy").formatted(Formatting.GRAY), true);
        }
        if (user instanceof PlayerEntity p && user.getWorld() instanceof ServerWorld sw) {
            sw.playSound(null, p.getBlockPos(), SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.PLAYERS, 0.6f, 1.1f);
            if (d.soda) rinaScolds(sw, p);
        }
    }

    /** Si te echas 3 refrescos seguidos y Rina anda cerca, te regaña. */
    private static void rinaScolds(ServerWorld sw, PlayerEntity p) {
        long now = sw.getTime();
        long[] s = SODAS.computeIfAbsent(p.getUuid(), k -> new long[]{0, 0});
        s[0] = now - s[1] < 4800 ? s[0] + 1 : 1;
        s[1] = now;
        if (s[0] < 3) return;
        s[0] = 0;
        for (KikoEntity k : sw.getEntitiesByClass(KikoEntity.class, p.getBoundingBox().expand(16), e -> e.getCharacter() == KikoCharacter.RINA)) {
            KikoInteractionHandler.say(p, k, "dialogue.rina.soda_scold", 3);
            k.setExpression(com.jaciel.kiko.animation.KikoExpression.POUT, 80);
            return;
        }
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (LATER.isEmpty() && WARM.isEmpty()) return;
            Iterator<Later> it = LATER.iterator();
            while (it.hasNext()) {
                Later l = it.next();
                ServerWorld w = server.getWorld(l.world());
                if (w == null) { it.remove(); continue; }
                if (w.getTime() < l.at()) continue;
                it.remove();
                if (w.getEntity(l.who()) instanceof LivingEntity e) e.addStatusEffect(l.fx());
            }
            if (server.getTicks() % 10 != 0) return;
            Iterator<Map.Entry<UUID, Long>> wi = WARM.entrySet().iterator();
            while (wi.hasNext()) {
                Map.Entry<UUID, Long> e = wi.next();
                boolean done = true;
                for (ServerWorld w : server.getWorlds()) {
                    if (w.getEntity(e.getKey()) instanceof LivingEntity le) {
                        if (w.getTime() < e.getValue()) {
                            le.setFrozenTicks(0);
                            done = false;
                        }
                        break;
                    }
                }
                if (done) wi.remove();
            }
        });
    }
}
