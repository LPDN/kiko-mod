package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.entity.KikoEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.inventory.Inventory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

/**
 * Cuando está trabajando y se le llena la mochila, va sola a guardar al cofre y regresa.
 *  1. Usa "su cofre" (el último donde le pediste guardar) si está a menos de 64 bloques
 *  2. Si no, busca el cofre o barril más cercano a 24 bloques
 *  3. Se queda con sus herramientas, armas, armadura, comida, pociones, antorchas, flechas,
 *     brotes y un stack de piedra para tapar lava en los túneles
 */
public class DepositGoal extends Goal {
    private final KikoEntity kiko;
    @Nullable
    private BlockPos chest;
    private int ticks;
    private int cooldown;

    public DepositGoal(KikoEntity kiko) {
        this.kiko = kiko;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (kiko.getWorkMode() == WorkMode.NONE || kiko.getTarget() != null) return false;
        if (--cooldown > 0) return false;
        if (kiko.getInventory().freeBagSlots() > 1) return false;
        cooldown = 40;
        chest = findChest(kiko.getWorld());
        if (chest == null) {
            kiko.notifyOwner("message.kiko.bag_full");
            cooldown = 200;
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return chest != null && kiko.getTarget() == null && ticks < 600;
    }

    @Override
    public void start() {
        ticks = 0;
        kiko.setDepositing(true);
        moveTo();
    }

    @Override
    public void stop() {
        kiko.setDepositing(false);
        kiko.getNavigation().stop();
        if (chest != null && ticks >= 600) kiko.notifyOwner("message.kiko.chest_unreachable");
        chest = null;
    }

    @Override
    public void tick() {
        if (chest == null) return;
        ticks++;
        Vec3d c = Vec3d.ofCenter(chest);
        kiko.getLookControl().lookAt(c.x, c.y, c.z);
        if (kiko.getPos().squaredDistanceTo(c) > 2.8 * 2.8) {
            if (ticks % 20 == 0) moveTo();
            return;
        }
        kiko.getNavigation().stop();

        World world = kiko.getWorld();
        if (world.getBlockEntity(chest) instanceof Inventory container) {
            world.playSound(null, chest, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.6f, 1.1f);
            int moved = kiko.getInventory().depositInto(container);
            kiko.setHomeChest(chest);
            if (kiko.getInventory().freeBagSlots() <= 1) {
                kiko.notifyOwner("message.kiko.chest_full");
                cooldown = 600;
            } else if (moved > 0) {
                kiko.notifyOwner("message.kiko.auto_stored");
            }
        }
        chest = null;
    }

    private void moveTo() {
        if (chest == null) return;
        Path path = kiko.getNavigation().findPathTo(chest, 1);
        if (path != null) kiko.getNavigation().startMovingAlong(path, 1.15);
    }

    @Nullable
    private BlockPos findChest(World world) {
        BlockPos home = kiko.getHomeChest();
        if (home != null && home.isWithinDistance(kiko.getPos(), 64) && world.isChunkLoaded(home)
                && world.getBlockEntity(home) instanceof Inventory) {
            return home;
        }
        BlockPos from = kiko.getBlockPos();
        for (BlockPos pos : BlockPos.iterateOutwards(from, 24, 8, 24)) {
            if (!world.isChunkLoaded(pos)) continue;
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof Inventory inv && inv.size() >= 9 && !(be instanceof net.minecraft.block.entity.AbstractFurnaceBlockEntity)) {
                return pos.toImmutable();
            }
        }
        return null;
    }
}
