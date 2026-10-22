package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.entity.combat.WeaponHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Criadora de animales.
 *  - Si trae tijeras, trasquila a las ovejas con lana
 *  - Le da de comer a los animales adultos para que tengan crías (trigo, zanahorias, semillas...
 *    lo que le guste a cada animal, también los de mods)
 *  - No pasa del límite de animales de cada tipo (breedLimit en la config, 12 por defecto)
 */
public class BreedGoal extends Goal {
    private enum Job { SHEAR, FEED }

    private final KikoEntity kiko;
    @Nullable
    private AnimalEntity animal;
    private Job job = Job.FEED;
    private int ticks;
    private int scanCooldown;

    public BreedGoal(KikoEntity kiko) {
        this.kiko = kiko;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (kiko.getWorkMode() != WorkMode.BREED || kiko.getTarget() != null || kiko.isDepositing()) return false;
        if (--scanCooldown > 0) return false;
        scanCooldown = 40;
        animal = findAnimal();
        if (animal == null) {
            scanCooldown = 100;
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return animal != null && animal.isAlive() && kiko.getWorkMode() == WorkMode.BREED
                && kiko.getTarget() == null && !kiko.isDepositing() && ticks < 200 && stillNeeded(animal);
    }

    @Override
    public void start() {
        ticks = 0;
        if (animal != null) kiko.getNavigation().startMovingTo(animal, 1.1);
    }

    @Override
    public void stop() {
        animal = null;
        kiko.getNavigation().stop();
        scanCooldown = 10;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (animal == null) return;
        ticks++;
        kiko.getLookControl().lookAt(animal, 30f, 30f);
        if (kiko.squaredDistanceTo(animal) > 2.6 * 2.6) {
            if (ticks % 10 == 0) kiko.getNavigation().startMovingTo(animal, 1.1);
            return;
        }
        kiko.getNavigation().stop();
        kiko.swingHand(Hand.MAIN_HAND);

        if (job == Job.SHEAR && animal instanceof SheepEntity sheep && WeaponHelper.equipTool(kiko, s -> s.isOf(Items.SHEARS))) {
            sheep.sheared(SoundCategory.NEUTRAL);
            KikoWork.damageHand(kiko);
        } else if (job == Job.FEED) {
            int slot = kiko.getInventory().findInBag(animal::isBreedingItem);
            if (slot >= 0 && animal.canEat() && animal.getBreedingAge() == 0) {
                kiko.getInventory().bag().get(slot).decrement(1);
                animal.lovePlayer(null);
                if (kiko.getWorld() instanceof ServerWorld sw) {
                    sw.spawnParticles(ParticleTypes.HEART, animal.getX(), animal.getBodyY(0.8), animal.getZ(), 3, 0.3, 0.3, 0.3, 0);
                }
            }
        }
        animal = null; // shouldContinue la suelta y busca al siguiente
    }

    // ------------------------------------------------------------------

    @Nullable
    private AnimalEntity findAnimal() {
        BlockPos center = kiko.getWorkCenter() != null ? kiko.getWorkCenter() : kiko.getBlockPos();
        int r = KikoConfig.get().workRadius;
        Box box = new Box(center).expand(r, 6, r);
        List<AnimalEntity> animals = kiko.getWorld().getEntitiesByClass(AnimalEntity.class, box,
                a -> a.isAlive() && !(a instanceof net.minecraft.entity.passive.TameableEntity t && t.isTamed()));
        if (animals.isEmpty()) return null;

        // 1) Ovejas con lana, si trae tijeras
        boolean hasShears = kiko.getMainHandStack().isOf(Items.SHEARS)
                || kiko.getInventory().findInBag(s -> s.isOf(Items.SHEARS) && !KikoWork.isAlmostBroken(s)) >= 0;
        if (hasShears) {
            AnimalEntity sheep = nearest(animals, a -> a instanceof SheepEntity s && s.isShearable());
            if (sheep != null) {
                job = Job.SHEAR;
                return sheep;
            }
        }

        // 2) Parejas para cruzar, sin pasarse del límite
        Map<EntityType<?>, Integer> count = new HashMap<>();
        Map<EntityType<?>, Integer> ready = new HashMap<>();
        for (AnimalEntity a : animals) {
            count.merge(a.getType(), 1, Integer::sum);
            if (canBreedNow(a)) ready.merge(a.getType(), 1, Integer::sum);
        }
        int limit = KikoConfig.get().breedLimit;
        // Animales que ya están enamorados esperan a su pareja
        Map<EntityType<?>, Integer> inLove = new HashMap<>();
        for (AnimalEntity a : animals) if (a.isInLove()) inLove.merge(a.getType(), 1, Integer::sum);

        AnimalEntity best = nearest(animals, a -> {
            if (!canBreedNow(a)) return false;
            if (count.getOrDefault(a.getType(), 0) >= limit) return false;
            int partners = ready.getOrDefault(a.getType(), 0) + inLove.getOrDefault(a.getType(), 0);
            if (partners < 2) return false;
            return kiko.getInventory().findInBag(a::isBreedingItem) >= 0;
        });
        if (best != null) {
            job = Job.FEED;
            return best;
        }
        return null;
    }

    private static boolean canBreedNow(AnimalEntity a) {
        return !a.isBaby() && a.getBreedingAge() == 0 && a.canEat() && !a.isInLove();
    }

    private boolean stillNeeded(AnimalEntity a) {
        if (job == Job.SHEAR) return a instanceof SheepEntity s && s.isShearable();
        return canBreedNow(a);
    }

    @Nullable
    private AnimalEntity nearest(List<AnimalEntity> list, java.util.function.Predicate<AnimalEntity> test) {
        AnimalEntity best = null;
        double bestD = Double.MAX_VALUE;
        for (AnimalEntity a : list) {
            if (!test.test(a)) continue;
            double d = a.squaredDistanceTo(kiko);
            if (d < bestD) {
                bestD = d;
                best = a;
            }
        }
        return best;
    }
}
