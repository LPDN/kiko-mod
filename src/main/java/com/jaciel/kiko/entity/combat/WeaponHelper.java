package com.jaciel.kiko.entity.combat;

import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.inventory.KikoInventory;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.registry.tag.ItemTags;

/**
 * Cómo escoge Kiko sus armas y herramientas.
 *
 * No depende de ítems específicos, así que funciona con armas de vanilla y de otros mods
 *  - Cuerpo a cuerpo   cualquier ítem cuyo atributo de daño en la mano sume (espadas, hachas, tridentes, mazas, katanas de mods...)
 *  - A distancia       cualquier ítem que extienda RangedWeaponItem (arcos, ballestas y los de mods que los heredan)
 *  - Hacha / pico      tags de vanilla #axes y #pickaxes (los mods bien hechos agregan sus herramientas ahí)
 */
public final class WeaponHelper {
    private WeaponHelper() {}

    /** Daño extra que da el ítem en la mano principal (0 si no es arma). */
    public static double meleeBonus(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        AttributeModifiersComponent mods = stack.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);
        double[] total = {0};
        mods.applyModifiers(EquipmentSlot.MAINHAND, (attribute, modifier) -> {
            if (attribute.equals(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
                total[0] += modifier.value();
            }
        });
        return total[0];
    }

    public static boolean isRanged(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof RangedWeaponItem;
    }

    public static boolean isAxe(ItemStack stack) {
        return !stack.isEmpty() && stack.isIn(ItemTags.AXES);
    }

    public static boolean isPickaxe(ItemStack stack) {
        return !stack.isEmpty() && stack.isIn(ItemTags.PICKAXES);
    }

    /** Busca en la mochila flechas (o lo que acepte el arma). Índice de la mochila o -1. */
    public static int findAmmo(KikoEntity kiko, ItemStack weapon) {
        if (!(weapon.getItem() instanceof RangedWeaponItem ranged)) return -1;
        return kiko.getInventory().findInBag(s -> ranged.getProjectiles().test(s));
    }

    public static boolean canShoot(KikoEntity kiko) {
        ItemStack hand = kiko.getMainHandStack();
        return isRanged(hand) && findAmmo(kiko, hand) >= 0;
    }

    /**
     * Pone en la mano el arma adecuada según la distancia al objetivo.
     *  - Lejos (más de 6 bloques) y hay arco con flechas  -> arco
     *  - Si no                                           -> la mejor arma cuerpo a cuerpo
     */
    public static void equipBestWeapon(KikoEntity kiko, double distanceSq) {
        KikoInventory inv = kiko.getInventory();
        ItemStack hand = kiko.getMainHandStack();

        if (distanceSq > 6 * 6) {
            if (isRanged(hand) && findAmmo(kiko, hand) >= 0) return;
            int bow = inv.findInBag(s -> isRanged(s) && findAmmo(kiko, s) >= 0);
            if (bow >= 0) {
                inv.swapToMainHand(bow);
                return;
            }
        }

        // Mejor arma cuerpo a cuerpo de la mochila contra lo que trae en la mano
        double best = isRanged(hand) ? -1 : meleeBonus(hand);
        int bestIndex = -1;
        for (int i = 0; i < KikoInventory.BAG_SIZE; i++) {
            ItemStack s = inv.bag().get(i);
            if (s.isEmpty() || isRanged(s)) continue;
            double v = meleeBonus(s);
            if (v > best + 0.01 && v > 0) {
                best = v;
                bestIndex = i;
            }
        }
        if (bestIndex >= 0) inv.swapToMainHand(bestIndex);
    }

    /** Pone en la mano una herramienta que cumpla. Regresa false si no tiene. */
    public static boolean equipTool(KikoEntity kiko, java.util.function.Predicate<ItemStack> isTool) {
        if (isTool.test(kiko.getMainHandStack())) return true;
        int i = kiko.getInventory().findInBag(isTool);
        if (i < 0) return false;
        kiko.getInventory().swapToMainHand(i);
        return true;
    }
}
