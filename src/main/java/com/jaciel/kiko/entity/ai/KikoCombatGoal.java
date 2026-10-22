package com.jaciel.kiko.entity.ai;

import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.entity.combat.WeaponHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import com.jaciel.kiko.entity.combat.CombatMode;
import com.jaciel.kiko.entity.combat.SelfCare;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ShieldItem;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;

/**
 * Pelea de Kiko. Usa lo que traiga en la mano y cambia de arma sola.
 *  - Arco o ballesta con flechas en la mochila   dispara de lejos y se acerca si no te ve
 *  - Cualquier otra cosa                        cuerpo a cuerpo, con la velocidad de ataque del arma
 */
public class KikoCombatGoal extends Goal {
    private final KikoEntity kiko;
    private int cooldown;
    private int weaponTimer;
    private int repathTimer;

    public KikoCombatGoal(KikoEntity kiko) {
        this.kiko = kiko;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity t = kiko.getTarget();
        return t != null && t.isAlive() && !(t instanceof PlayerEntity) && kiko.getCombatMode() != CombatMode.PASSIVE;
    }

    @Override
    public boolean shouldContinue() {
        LivingEntity t = kiko.getTarget();
        return t != null && t.isAlive() && kiko.squaredDistanceTo(t) < 32 * 32 && kiko.getCombatMode() != CombatMode.PASSIVE;
    }

    @Override
    public void start() {
        kiko.setAttacking(true);
        weaponTimer = 0;
        repathTimer = 0;
        SelfCare.buffForFight(kiko); // se toma sus pociones de pelea
    }

    @Override
    public void stop() {
        kiko.setAttacking(false);
        kiko.getNavigation().stop();
        lowerShield();
        LivingEntity t = kiko.getTarget();
        if (t == null || !t.isAlive()) kiko.setTarget(null);
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = kiko.getTarget();
        if (target == null) return;
        double dist = kiko.squaredDistanceTo(target);
        boolean canSee = kiko.getVisibilityCache().canSee(target);
        kiko.getLookControl().lookAt(target, 30f, 30f);

        if (--weaponTimer <= 0) {
            weaponTimer = 40;
            WeaponHelper.equipBestWeapon(kiko, dist);
        }
        if (cooldown > 0) cooldown--;

        boolean ranged = WeaponHelper.canShoot(kiko);
        updateShield(target, dist, canSee, ranged);

        if (ranged) {
            if (dist < 3 * 3) {
                // Muy cerca para el arco, saca el arma cuerpo a cuerpo
                WeaponHelper.equipBestWeapon(kiko, 0);
                weaponTimer = 40;
                return;
            }
            if (dist > 14 * 14 || !canSee) {
                if (--repathTimer <= 0) {
                    repathTimer = 10;
                    kiko.getNavigation().startMovingTo(target, 1.1);
                }
            } else {
                kiko.getNavigation().stop();
            }
            if (canSee && dist <= 18 * 18 && cooldown <= 0) {
                lowerShield();
                shoot(target);
                cooldown = kiko.getMainHandStack().getItem() instanceof CrossbowItem ? 30 : 22;
            }
            return;
        }

        // Cuerpo a cuerpo
        if (--repathTimer <= 0) {
            repathTimer = 8;
            kiko.getNavigation().startMovingTo(target, 1.2);
        }
        if (cooldown <= 0 && kiko.isInAttackRange(target) && canSee) {
            lowerShield();
            kiko.swingHand(Hand.MAIN_HAND);
            kiko.tryAttack(target);
            cooldown = meleeCooldown();
        }
    }

    /**
     * Escudo en la otra mano
     *  - Se cubre de los que disparan (esqueletos, etc.) mientras se acerca
     *  - Se cubre entre golpe y golpe cuando el enemigo está pegado
     *  - Lo baja justo antes de atacar
     */
    private void updateShield(LivingEntity target, double dist, boolean canSee, boolean ranged) {
        boolean hasShield = kiko.getOffHandStack().getItem() instanceof ShieldItem;
        boolean block = hasShield && !ranged && canSee && (
                (target instanceof RangedAttackMob && dist > 3.5 * 3.5)
                        || (dist < 3.5 * 3.5 && cooldown > 4));
        if (block && !kiko.isUsingItem()) {
            kiko.setCurrentHand(Hand.OFF_HAND);
        } else if (!block) {
            lowerShield();
        }
    }

    private void lowerShield() {
        if (kiko.isUsingItem() && kiko.getActiveHand() == Hand.OFF_HAND) kiko.clearActiveItem();
    }

    /** Ticks entre golpes según la velocidad de ataque del arma (espada 12, hacha 20, puños 8). */
    private int meleeCooldown() {
        double speed = kiko.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED);
        if (speed <= 0) return 20;
        return MathHelper.clamp((int) Math.round(20.0 / speed), 8, 30);
    }

    private void shoot(LivingEntity target) {
        ItemStack weapon = kiko.getMainHandStack();
        int ammoIndex = WeaponHelper.findAmmo(kiko, weapon);
        if (ammoIndex < 0) return;
        ItemStack ammoStack = kiko.getInventory().bag().get(ammoIndex);

        PersistentProjectileEntity arrow = ProjectileUtil.createArrowProjectile(kiko, ammoStack.copyWithCount(1), 1.0f, weapon);
        double dx = target.getX() - kiko.getX();
        double dy = target.getBodyY(0.3333) - arrow.getY();
        double dz = target.getZ() - kiko.getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        arrow.setVelocity(dx, dy + horizontal * 0.2, dz, 1.6f, 2.0f);
        kiko.getWorld().spawnEntity(arrow);
        kiko.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0f, 1.0f / (kiko.getRandom().nextFloat() * 0.4f + 0.8f));

        ammoStack.decrement(1);
        weapon.damage(1, kiko, EquipmentSlot.MAINHAND);
        kiko.swingHand(Hand.MAIN_HAND);
    }
}
