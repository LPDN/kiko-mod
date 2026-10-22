package com.jaciel.kiko.entity;

import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.family.KikoChildEntity;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import com.jaciel.kiko.registry.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Pelota de futbol (v3.2.1).
 *  - Rueda, rebota en paredes y en el piso, y flota en el agua
 *  - Si caminas o corres hacia ella la empujas. Si le pegas (clic izquierdo) la pateas fuerte hacia donde miras
 *  - Agachado + clic derecho la levantas
 *  - Portería: dos cercas (o bardas) en línea con 2 a 4 bloques libres entre ellas. Si la pelota pasa entre ellas es gol
 * Los niños juegan con ella solos (ver family.KidSoccerGoal).
 */
public class SoccerBallEntity extends Entity {
    public static final float RADIUS = 0.25f;

    private int kickCooldown;
    private int goalCooldown;
    @Nullable
    private Entity lastKicker;
    private int lastKickAge;

    // Solo cliente: para que se vea rodar
    public float roll, prevRoll, rollYaw;

    public SoccerBallEntity(EntityType<? extends SoccerBallEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    // ------------------------------------------------------------------
    // Física
    // ------------------------------------------------------------------

    @Override
    public void tick() {
        super.tick();
        if (kickCooldown > 0) kickCooldown--;
        if (goalCooldown > 0) goalCooldown--;

        Vec3d v = this.getVelocity();
        boolean inWater = this.getWorld().getFluidState(this.getBlockPos()).isIn(FluidTags.WATER);
        if (inWater) {
            // Flota y se frena
            v = new Vec3d(v.x * 0.85, v.y * 0.6 + 0.05, v.z * 0.85);
        } else if (!this.hasNoGravity()) {
            v = v.add(0, -0.04, 0);
        }
        double startX = this.getX(), startZ = this.getZ();
        this.move(MovementType.SELF, v);
        double movedX = this.getX() - startX, movedZ = this.getZ() - startZ;

        // Rebote en paredes (se perdió parte del movimiento en ese eje)
        if (this.horizontalCollision) {
            boolean hitX = Math.abs(movedX - v.x) > 1.0E-4;
            boolean hitZ = Math.abs(movedZ - v.z) > 1.0E-4;
            if (hitX) v = new Vec3d(-v.x * 0.6, v.y, v.z);
            if (hitZ) v = new Vec3d(v.x, v.y, -v.z * 0.6);
            if ((hitX || hitZ) && v.horizontalLength() > 0.15) sfx(SoundEvents.ENTITY_SLIME_JUMP_SMALL, 0.4f, 1.6f);
        }
        // Rebote en el piso
        if (this.verticalCollision && v.y < 0) {
            if (v.y < -0.12) {
                v = new Vec3d(v.x, -v.y * 0.55, v.z);
                sfx(SoundEvents.ENTITY_SLIME_JUMP_SMALL, 0.3f, 1.8f);
            } else {
                v = new Vec3d(v.x, 0, v.z);
            }
        }
        // Roce (en el piso se frena más)
        double f = this.isOnGround() ? 0.93 : 0.99;
        v = new Vec3d(v.x * f, v.y, v.z * f);
        if (this.isOnGround() && v.horizontalLengthSquared() < 1.0E-5) v = new Vec3d(0, v.y, 0);
        this.setVelocity(v);

        if (this.getWorld().isClient) {
            // Gira según lo que avanzó
            prevRoll = roll;
            double dist = Math.sqrt(movedX * movedX + movedZ * movedZ);
            if (dist > 1.0E-4) {
                rollYaw = (float) (MathHelper.atan2(movedX, movedZ) * MathHelper.DEGREES_PER_RADIAN);
                roll += (float) (dist / RADIUS);
            }
            return;
        }

        if (this.getY() < this.getWorld().getBottomY() - 32) {
            this.discard();
            return;
        }
        pushedByEntities();
        checkGoal();
    }

    /** Lo empujan los que caminan hacia ella (jugadores, chicas, niños, mobs). */
    private void pushedByEntities() {
        if (kickCooldown > 0) return;
        for (LivingEntity e : this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(0.15),
                e -> e.isAlive() && !e.isSpectator())) {
            Vec3d step = new Vec3d(e.getX() - e.prevX, 0, e.getZ() - e.prevZ);
            double speed = step.horizontalLength();
            Vec3d dir = new Vec3d(this.getX() - e.getX(), 0, this.getZ() - e.getZ());
            if (dir.lengthSquared() < 1.0E-4) dir = step;
            if (dir.lengthSquared() < 1.0E-4) continue;
            dir = dir.normalize();
            // Si va hacia la pelota la empuja con su velocidad, si solo está parado encima apenas la mueve
            double power = speed > 0.02 ? 0.2 + speed * 1.6 : 0.08;
            kick(dir, power, speed > 0.2 ? 0.12 : 0.04, e);
            return;
        }
    }

    /** Patada. dir horizontal (se normaliza), power de 0 a ~1.2, up lo que se levanta. */
    public void kick(Vec3d dir, double power, double up, @Nullable Entity kicker) {
        Vec3d d = new Vec3d(dir.x, 0, dir.z);
        if (d.lengthSquared() < 1.0E-6) return;
        d = d.normalize().multiply(power);
        this.setVelocity(d.x, Math.max(this.getVelocity().y, up), d.z);
        this.velocityModified = true;
        this.kickCooldown = 4;
        this.lastKicker = kicker;
        this.lastKickAge = this.age;
        if (power > 0.3) sfx(SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, 0.6f, 1.4f + this.random.nextFloat() * 0.2f);
    }

    /** Sonido solo desde el servidor (si no, en el cliente se oye doble). */
    private void sfx(net.minecraft.sound.SoundEvent sound, float volume, float pitch) {
        if (!this.getWorld().isClient) this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), sound, SoundCategory.NEUTRAL, volume, pitch);
    }

    public boolean canBeKicked() {
        return kickCooldown <= 0;
    }

    // ------------------------------------------------------------------
    // Portería
    // ------------------------------------------------------------------

    private static boolean isPost(BlockState s) {
        return s.getBlock() instanceof FenceBlock || s.getBlock() instanceof WallBlock;
    }

    /**
     * ¿pos está entre dos postes alineados en `axis`? (2 a 4 bloques libres entre ellos)
     * @return el centro de la portería, o null
     */
    @Nullable
    public static Vec3d goalAt(World world, BlockPos pos, Direction.Axis axis) {
        Direction neg = Direction.from(axis, Direction.AxisDirection.NEGATIVE);
        Direction posDir = Direction.from(axis, Direction.AxisDirection.POSITIVE);
        int a = -1, b = -1;
        for (int i = 1; i <= 4; i++) {
            BlockState s = world.getBlockState(pos.offset(neg, i));
            if (isPost(s)) { a = i; break; }
            if (!s.getCollisionShape(world, pos.offset(neg, i)).isEmpty()) return null;
        }
        if (a < 0) return null;
        for (int i = 1; i <= 4; i++) {
            BlockState s = world.getBlockState(pos.offset(posDir, i));
            if (isPost(s)) { b = i; break; }
            if (!s.getCollisionShape(world, pos.offset(posDir, i)).isEmpty()) return null;
        }
        if (b < 0) return null;
        int gap = a + b - 1;
        if (gap < 2 || gap > 4) return null;
        BlockPos left = pos.offset(neg, a), right = pos.offset(posDir, b);
        return new Vec3d((left.getX() + right.getX()) / 2.0 + 0.5, pos.getY(), (left.getZ() + right.getZ()) / 2.0 + 0.5);
    }

    /** Busca una portería cerca (para que los niños tiren hacia ella). */
    @Nullable
    public static Vec3d findGoalNear(World world, BlockPos center, int radius) {
        for (BlockPos p : BlockPos.iterateOutwards(center, radius, 2, radius)) {
            if (!isPost(world.getBlockState(p))) continue;
            for (Direction d : Direction.Type.HORIZONTAL) {
                BlockPos mouth = p.offset(d);
                if (!world.getBlockState(mouth).getCollisionShape(world, mouth).isEmpty()) continue;
                Vec3d g = goalAt(world, mouth, d.getAxis());
                if (g != null) return g;
            }
        }
        return null;
    }

    private void checkGoal() {
        if (goalCooldown > 0 || lastKicker == null || this.age - lastKickAge > 200) return;
        BlockPos cur = this.getBlockPos();
        BlockPos prev = BlockPos.ofFloored(this.prevX, this.prevY, this.prevZ);
        if (cur.equals(prev)) return;
        Vec3d g = null;
        // Entró a la fila de la portería cruzando de frente
        if (cur.getX() != prev.getX()) g = goalAt(this.getWorld(), cur, Direction.Axis.Z);
        if (g == null && cur.getZ() != prev.getZ()) g = goalAt(this.getWorld(), cur, Direction.Axis.X);
        if (g == null) return;
        goalCooldown = 100;
        celebrateGoal();
    }

    private void celebrateGoal() {
        if (!(this.getWorld() instanceof ServerWorld world)) return;
        world.spawnParticles(ParticleTypes.FIREWORK, this.getX(), this.getY() + 0.5, this.getZ(), 40, 0.4, 0.6, 0.4, 0.15);
        world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, this.getX(), this.getY() + 0.5, this.getZ(), 15, 0.8, 0.5, 0.8, 0);
        world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.NEUTRAL, 1f, 1.2f);
        world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.NEUTRAL, 0.6f, 1.4f);
        Text who = lastKicker != null ? lastKicker.getDisplayName() : Text.literal("?");
        Text msg = Text.translatable("message.kiko.goal", who).formatted(Formatting.GOLD, Formatting.BOLD);
        for (ServerPlayerEntity p : world.getPlayers(p -> p.squaredDistanceTo(this) < 32 * 32)) p.sendMessage(msg, true);
        // Los que andan cerca festejan
        for (KikoEntity k : world.getEntitiesByClass(KikoEntity.class, this.getBoundingBox().expand(20), k -> !k.isSleeping())) {
            k.playAnimation(k == lastKicker ? KikoAnimation.CHEER : (k.isKid() ? KikoAnimation.CHEER : KikoAnimation.CLAP));
            k.setExpression(KikoExpression.LAUGH, 60);
        }
        if (lastKicker instanceof KikoChildEntity kid && kid.getParentUuid() != null) {
            PlayerEntity parent = world.getPlayerByUuid(kid.getParentUuid());
            if (parent != null && parent.squaredDistanceTo(kid) < 24 * 24) KikoInteractionHandler.say(parent, kid, "dialogue.kiko.goal", 3);
        }
    }

    // ------------------------------------------------------------------
    // Jugador
    // ------------------------------------------------------------------

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }

    /** Clic izquierdo = patada fuerte hacia donde miras. */
    @Override
    public boolean handleAttack(Entity attacker) {
        if (!this.getWorld().isClient && attacker instanceof PlayerEntity p) {
            Vec3d look = p.getRotationVec(1f);
            double up = MathHelper.clamp(-look.y * 0.6 + 0.25, 0.15, 0.6);
            kick(look, p.isSprinting() ? 1.1 : 0.8, up, p);
        }
        return true;
    }

    /** Agachado + clic derecho la levantas. Sin agacharte la tocas tantito. */
    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (this.getWorld().isClient) return ActionResult.SUCCESS;
        if (player.isSneaking()) {
            player.getInventory().offerOrDrop(new ItemStack(ModItems.SOCCER_BALL));
            sfx(SoundEvents.ENTITY_ITEM_PICKUP, 0.5f, 1.2f);
            this.discard();
        } else {
            kick(player.getRotationVec(1f), 0.35, 0.3, player);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) return false;
        if (source.isIn(DamageTypeTags.IS_EXPLOSION) && source.getPosition() != null) {
            kick(this.getPos().subtract(source.getPosition()), 1.0, 0.6, null);
            return false;
        }
        if (source.isIn(DamageTypeTags.IS_FIRE) || source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (!this.getWorld().isClient) this.discard();
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(ModItems.SOCCER_BALL);
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
