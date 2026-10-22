package com.jaciel.kiko.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * El corcho de la caña de Kiko. Solo es visual: vuela en arco, flota en el agua y se hunde cuando pica.
 * El hilo lo dibuja KikoBobberRenderer desde la punta de su caña.
 * No se guarda en el mundo; si Kiko deja de pescar desaparece solo.
 */
public class KikoBobberEntity extends Entity {
    private static final TrackedData<Integer> OWNER = DataTracker.registerData(KikoBobberEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> BITE = DataTracker.registerData(KikoBobberEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public KikoBobberEntity(EntityType<? extends KikoBobberEntity> type, World world) {
        super(type, world);
        this.noClip = false;
    }

    public KikoBobberEntity(EntityType<? extends KikoBobberEntity> type, World world, KikoEntity owner) {
        this(type, world);
        this.dataTracker.set(OWNER, owner.getId());
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(OWNER, -1);
        builder.add(BITE, false);
    }

    @Nullable
    public KikoEntity getOwner() {
        Entity e = this.getWorld().getEntityById(this.dataTracker.get(OWNER));
        return e instanceof KikoEntity k ? k : null;
    }

    /** Se hunde (le picó un pez). */
    public void setBite(boolean bite) {
        this.dataTracker.set(BITE, bite);
    }

    public boolean isBite() {
        return this.dataTracker.get(BITE);
    }

    /** true cuando ya cayó y está flotando. */
    public boolean isFloating() {
        return this.getWorld().getFluidState(this.getBlockPos()).isIn(FluidTags.WATER)
                || this.getWorld().getFluidState(this.getBlockPos().down()).isIn(FluidTags.WATER) && !this.isOnGround();
    }

    @Override
    public void tick() {
        super.tick();
        KikoEntity owner = getOwner();
        if (!this.getWorld().isClient) {
            if (owner == null || !owner.isAlive() || !owner.isFishing() || owner.squaredDistanceTo(this) > 32 * 32 || this.age > 2400) {
                this.discard();
                return;
            }
        }

        Vec3d v = this.getVelocity();
        BlockPos pos = this.getBlockPos();
        FluidState fluid = this.getWorld().getFluidState(pos);
        if (fluid.isIn(FluidTags.WATER)) {
            // Flota en la superficie (y se hunde un poco cuando pica)
            double surface = pos.getY() + fluid.getHeight(this.getWorld(), pos);
            double target = surface - 0.12 - (isBite() ? 0.3 : 0.0) + Math.sin(this.age * 0.15) * 0.02;
            v = new Vec3d(v.x * 0.5, v.y * 0.5 + (target - this.getY()) * 0.2, v.z * 0.5);
        } else {
            v = v.add(0, -0.04, 0);
        }
        this.move(MovementType.SELF, v);
        if (this.isOnGround()) v = new Vec3d(v.x * 0.5, 0, v.z * 0.5);
        this.setVelocity(v);
    }

    /** Lanza el corcho para que caiga en target (en unos 15 ticks). */
    public void launchTowards(Vec3d target) {
        int t = 15;
        double dx = (target.x - this.getX()) / t;
        double dz = (target.z - this.getZ()) / t;
        double dy = (target.y - this.getY() + 0.02 * t * (t + 1)) / t;
        this.setVelocity(dx, dy, dz);
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < 64 * 64;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entry) {
        return new EntitySpawnS2CPacket(this, entry);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}
}
