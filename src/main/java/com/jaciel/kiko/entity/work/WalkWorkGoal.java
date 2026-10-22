package com.jaciel.kiko.entity.work;

import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Base de los trabajos de "ir a un lugar y hacer algo" (sembrar, plantar, pescar, el horno, rellenar).
 *  1. Busca un lugar donde hacer su trabajo dentro del área
 *  2. Camina hasta ahí. Si no puede llegar lo ignora un rato
 *  3. Llama a act() cada tick hasta que regrese true
 */
public abstract class WalkWorkGoal extends Goal {
    protected final KikoEntity kiko;
    private final EnumSet<WorkMode> modes;
    private final Map<Long, Long> blacklist = new HashMap<>();

    @Nullable
    protected BlockPos target;
    protected int actTicks;
    private int travelTicks;
    private int stuckTicks;
    private Vec3d lastPos = Vec3d.ZERO;
    private int scanCooldown;

    protected WalkWorkGoal(KikoEntity kiko, WorkMode mode) {
        this(kiko, EnumSet.of(mode));
    }

    protected WalkWorkGoal(KikoEntity kiko, EnumSet<WorkMode> modes) {
        this.kiko = kiko;
        this.modes = modes;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    /** Revisa herramientas o materiales. Regresa false (y avisa) si le falta algo. */
    protected abstract boolean prepare();

    /** Siguiente lugar donde trabajar, o null si no hay nada que hacer. */
    @Nullable
    protected abstract BlockPos findTarget(World world, BlockPos from, BlockPos center, int radius);

    /** true si el lugar todavía sirve. */
    protected abstract boolean isValidTarget(World world, BlockPos pos);

    /** Se llama cada tick ya que llegó. Regresa true cuando terminó con este lugar. */
    protected abstract boolean act(ServerWorld world, BlockPos pos);

    /** Qué tan cerca (al cuadrado, desde los ojos) tiene que estar para trabajar. */
    protected double reachSq() {
        return 3.2 * 3.2;
    }

    /** Ticks entre búsquedas cuando no encontró nada. */
    protected int idleCooldown() {
        return 40;
    }

    /** Mensaje cuando no hay nada que hacer (null = no avisa). */
    @Nullable
    protected String nothingLeftKey() {
        return null;
    }

    protected int radius() {
        return KikoConfig.get().workRadius;
    }

    @Override
    public boolean canStart() {
        if (!modes.contains(kiko.getWorkMode()) || kiko.getTarget() != null || kiko.isDepositing()) return false;
        if (--scanCooldown > 0) return false;
        scanCooldown = 10;
        if (!prepare()) {
            scanCooldown = 60;
            return false;
        }
        long now = kiko.getWorld().getTime();
        blacklist.values().removeIf(until -> until <= now);
        BlockPos center = kiko.getWorkCenter() != null ? kiko.getWorkCenter() : kiko.getBlockPos();
        target = findTarget(kiko.getWorld(), kiko.getBlockPos(), center, radius());
        if (target == null) {
            String key = nothingLeftKey();
            if (key != null) kiko.notifyOwner(key);
            scanCooldown = idleCooldown();
        }
        return target != null;
    }

    @Override
    public boolean shouldContinue() {
        return target != null && modes.contains(kiko.getWorkMode()) && kiko.getTarget() == null
                && !kiko.isDepositing() && isValidTarget(kiko.getWorld(), target);
    }

    @Override
    public void start() {
        actTicks = 0;
        travelTicks = 0;
        stuckTicks = 0;
        lastPos = kiko.getPos();
        moveToTarget();
    }

    @Override
    public void stop() {
        target = null;
        kiko.getNavigation().stop();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (target == null) return;
        Vec3d c = Vec3d.ofCenter(target);
        kiko.getLookControl().lookAt(c.x, c.y, c.z);

        if (kiko.getEyePos().squaredDistanceTo(c) > reachSq()) {
            travelTicks++;
            if (travelTicks % 20 == 0) {
                if (kiko.getPos().squaredDistanceTo(lastPos) < 0.25) stuckTicks += 20;
                else stuckTicks = 0;
                lastPos = kiko.getPos();
                moveToTarget();
            }
            if (stuckTicks >= 60 || travelTicks > 200) giveUp(1200);
            return;
        }

        kiko.getNavigation().stop();
        actTicks++;
        if (kiko.getWorld() instanceof ServerWorld sw && act(sw, target)) {
            target = null;
            scanCooldown = 0;
        }
    }

    protected void giveUp(int ticks) {
        if (target != null) ignore(target, ticks);
        target = null;
        scanCooldown = 0;
    }

    private void moveToTarget() {
        if (target == null) return;
        Path path = kiko.getNavigation().findPathTo(target, 1);
        if (path != null) {
            kiko.getNavigation().startMovingAlong(path, 1.1);
        } else if (kiko.getEyePos().squaredDistanceTo(Vec3d.ofCenter(target)) > reachSq()) {
            giveUp(1200);
        }
    }

    protected boolean isIgnored(BlockPos pos) {
        Long until = blacklist.get(pos.asLong());
        return until != null && until > kiko.getWorld().getTime();
    }

    protected void ignore(BlockPos pos, int ticks) {
        blacklist.put(pos.asLong(), kiko.getWorld().getTime() + ticks);
    }

    protected static boolean inArea(BlockPos pos, BlockPos center, int radius) {
        int dx = pos.getX() - center.getX();
        int dz = pos.getZ() - center.getZ();
        return dx * dx + dz * dz <= radius * radius;
    }
}
