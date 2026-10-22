package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Ordenar cofres. Va de cofre en cofre (los abre y revisa) y al final los deja ordenados por categoría:
 * equipo, comida, minerales, madera, piedra y tierra, plantas, cosas de mobs, bloques y otros.
 * Sirve con cofres, cofres con trampa y barriles en el área de trabajo. Al terminar te dice qué quedó dónde.
 */
public class SortChestsGoal extends Goal {
    private static final int MAX_CHESTS = 32;

    private final KikoEntity kiko;
    private final List<BlockPos> chests = new ArrayList<>();
    private int index;
    private int ticks;
    private int openTicks;
    private boolean finished;

    public SortChestsGoal(KikoEntity kiko) {
        this.kiko = kiko;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return kiko.getWorkMode() == WorkMode.SORT && kiko.getTarget() == null;
    }

    @Override
    public boolean shouldContinue() {
        return !finished && kiko.getWorkMode() == WorkMode.SORT && kiko.getTarget() == null;
    }

    @Override
    public void start() {
        finished = false;
        index = 0;
        ticks = 0;
        openTicks = 0;
        chests.clear();
        World world = kiko.getWorld();
        BlockPos center = kiko.getWorkCenter() != null ? kiko.getWorkCenter() : kiko.getBlockPos();
        int r = KikoConfig.get().sortRadius;
        for (BlockPos p : BlockPos.iterateOutwards(center, r, 6, r)) {
            BlockEntity be = world.getBlockEntity(p);
            if (be instanceof ChestBlockEntity || be instanceof BarrelBlockEntity) {
                chests.add(p.toImmutable());
                if (chests.size() >= MAX_CHESTS) break;
            }
        }
        if (chests.isEmpty()) {
            kiko.notifyOwner("message.kiko.sort_no_chests");
            done();
        }
    }

    @Override
    public void stop() {
        kiko.getNavigation().stop();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (finished) return;
        if (index >= chests.size()) {
            sortAll();
            return;
        }
        BlockPos pos = chests.get(index);
        Vec3d c = Vec3d.ofCenter(pos);
        kiko.getLookControl().lookAt(c.x, c.y, c.z);
        ticks++;
        if (kiko.getEyePos().squaredDistanceTo(c) > 3.2 * 3.2) {
            if (ticks % 20 == 1) kiko.getNavigation().startMovingTo(c.x, pos.getY(), c.z, 1.1);
            if (ticks > 200) next(); // no llega a este, sigue con el siguiente
            return;
        }
        kiko.getNavigation().stop();
        // Abre el cofre un ratito, como si lo revisara
        if (openTicks == 0) {
            setOpen(pos, true);
            kiko.swingHand(Hand.MAIN_HAND);
        }
        if (++openTicks >= 16) {
            setOpen(pos, false);
            next();
        }
    }

    private void next() {
        index++;
        ticks = 0;
        openTicks = 0;
    }

    private void setOpen(BlockPos pos, boolean open) {
        World world = kiko.getWorld();
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof ChestBlock) {
            world.addSyncedBlockEvent(pos, state.getBlock(), 1, open ? 1 : 0);
            world.playSound(null, pos, open ? SoundEvents.BLOCK_CHEST_OPEN : SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5f, 1.1f);
        } else {
            world.playSound(null, pos, open ? SoundEvents.BLOCK_BARREL_OPEN : SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.BLOCKS, 0.5f, 1.1f);
        }
    }

    private void sortAll() {
        World world = kiko.getWorld();
        List<Inventory> invs = new ArrayList<>();
        for (BlockPos p : chests) {
            if (world.getBlockEntity(p) instanceof Inventory inv) invs.add(inv);
        }
        ChestSorter.Result r = ChestSorter.sort(invs);
        PlayerEntity owner = kiko.getOwnerUuid() != null ? world.getPlayerByUuid(kiko.getOwnerUuid()) : null;
        if (owner != null) {
            if (r.ok()) {
                MutableText list = Text.empty();
                for (int i = 0; i < r.used().size(); i++) {
                    if (i > 0) list.append(Text.literal(", ").formatted(Formatting.GRAY));
                    list.append(Text.translatable(r.used().get(i).translationKey()).formatted(Formatting.YELLOW));
                }
                KikoInteractionHandler.say(owner, kiko, "dialogue.kiko.sort_done", 1);
                owner.sendMessage(Text.translatable("message.kiko.sort_result", r.chests(), r.items(), list), false);
            } else {
                owner.sendMessage(Text.translatable("message.kiko.sort_full").formatted(Formatting.RED), false);
            }
        }
        done();
    }

    private void done() {
        finished = true;
        kiko.setWorkMode(WorkMode.NONE, null);
    }
}
