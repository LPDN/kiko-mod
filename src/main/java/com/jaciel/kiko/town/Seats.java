package com.jaciel.kiko.town;

import com.jaciel.kiko.animation.KikoPose;
import com.jaciel.kiko.entity.KikoEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Sentarse en sillas (v3.9). Las sillas son escalones normales: el respaldo es la parte alta (hacia donde
 * dice "facing") y el asiento la mitad baja, así que se sientan viendo al lado contrario.
 */
public final class Seats {
    private Seats() {}

    public static boolean isChair(BlockState s) {
        return s.getBlock() instanceof StairsBlock && s.get(StairsBlock.HALF) == BlockHalf.BOTTOM;
    }

    /** Hacia dónde ve quien se sienta en esa silla. */
    public static Direction facingOf(BlockState s) {
        return s.get(StairsBlock.FACING).getOpposite();
    }

    /** La sienta en la silla, viendo hacia el frente del asiento. */
    public static void sit(KikoEntity e, BlockPos chair) {
        BlockState s = e.getWorld().getBlockState(chair);
        if (!isChair(s)) return;
        Direction look = facingOf(s);
        double x = chair.getX() + 0.5 + look.getOffsetX() * 0.18;
        double z = chair.getZ() + 0.5 + look.getOffsetZ() * 0.18;
        float yaw = look.asRotation();
        e.getNavigation().stop();
        e.setVelocity(0, 0, 0);
        e.refreshPositionAndAngles(x, chair.getY() + 0.5, z, yaw, 0f);
        e.setBodyYaw(yaw);
        e.setHeadYaw(yaw);
        e.setKikoPose(KikoPose.SIT);
    }

    /** Mientras sigue sentada: no se mueve de la silla. */
    public static void keep(KikoEntity e, BlockPos chair) {
        BlockState s = e.getWorld().getBlockState(chair);
        if (!isChair(s)) {
            stand(e, chair);
            return;
        }
        if (e.squaredDistanceTo(chair.getX() + 0.5, chair.getY() + 0.5, chair.getZ() + 0.5) > 0.3) sit(e, chair);
        float yaw = facingOf(s).asRotation();
        e.setBodyYaw(yaw);
        e.getNavigation().stop();
    }

    /** Se para y se hace a un lado (donde haya piso y espacio). */
    public static void stand(KikoEntity e, @Nullable BlockPos chair) {
        e.setKikoPose(KikoPose.NONE);
        if (chair == null) return;
        BlockPos free = freeSpot(e.getWorld(), chair);
        if (free != null) e.refreshPositionAndAngles(free.getX() + 0.5, free.getY(), free.getZ() + 0.5, e.getYaw(), 0f);
    }

    /** Un lugar para pararse junto a algo (piso firme y dos de aire). */
    @Nullable
    public static BlockPos freeSpot(World w, BlockPos near) {
        for (BlockPos p : BlockPos.iterateOutwards(near, 2, 1, 2)) {
            if (p.equals(near)) continue;
            BlockPos below = p.down();
            if (!w.getBlockState(below).isSolidBlock(w, below)) continue;
            if (!w.getBlockState(p).getCollisionShape(w, p).isEmpty()) continue;
            if (!w.getBlockState(p.up()).getCollisionShape(w, p.up()).isEmpty()) continue;
            return p.toImmutable();
        }
        return null;
    }

    /** ¿Está alguien ya sentado ahí? */
    public static boolean taken(World w, BlockPos chair, KikoEntity me) {
        return !w.getEntitiesByClass(KikoEntity.class, new net.minecraft.util.math.Box(chair).expand(0.2),
                k -> k != me && k.getKikoPose() == KikoPose.SIT).isEmpty();
    }
}
