package com.jaciel.kiko.entity;

import com.jaciel.kiko.affection.KikoAffectionSystem;
import com.jaciel.kiko.affection.KikoAffectionSystem.Tier;
import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.ai.FollowFriendGoal;
import com.jaciel.kiko.entity.ai.HelpFriendTargetGoal;
import com.jaciel.kiko.entity.ai.KikoCombatGoal;
import com.jaciel.kiko.entity.combat.CombatMode;
import com.jaciel.kiko.entity.combat.SelfCare;
import com.jaciel.kiko.entity.combat.WeaponHelper;
import com.jaciel.kiko.entity.work.ChopTreeGoal;
import com.jaciel.kiko.entity.work.DepositGoal;
import com.jaciel.kiko.entity.work.TunnelGoal;
import net.minecraft.util.math.Direction;
import com.jaciel.kiko.entity.work.MineOreGoal;
import com.jaciel.kiko.entity.work.WorkMode;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import com.jaciel.kiko.inventory.KikoInventory;
import com.jaciel.kiko.outfit.KikoOutfit;
import com.jaciel.kiko.registry.ModSounds;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Kiko. NPC compañera con sistema de afecto, ropa intercambiable y animaciones.
 *
 * Lado servidor  -> IA, afecto, interacciones, guardado en NBT.
 * Lado cliente   -> solo lee el outfit / ánimo sincronizados y la animación activa.
 */
public class KikoEntity extends PathAwareEntity {
    private static final TrackedData<Integer> OUTFIT = DataTracker.registerData(KikoEntity.class, TrackedDataHandlerRegistry.INTEGER);
    /** Si la armadura la protege pero no se dibuja. */
    private static final TrackedData<Boolean> HIDE_ARMOR = DataTracker.registerData(KikoEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    /** Expresión de la cara (-1 = la escoge el cliente según su ánimo) y si está hablando. */
    private static final TrackedData<Integer> EXPRESSION = DataTracker.registerData(KikoEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> TALKING = DataTracker.registerData(KikoEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    /** Su mejor amigo (para el marcador, el silbato y el menú rápido). */
    private static final TrackedData<java.util.Optional<UUID>> FRIEND = DataTracker.registerData(KikoEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    /** Anda de expedición (invisible y sin IA hasta que regresa). */
    private static final TrackedData<Boolean> AWAY = DataTracker.registerData(KikoEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    /** Está pescando (el cliente lo usa para la pose con la caña). */
    private static final TrackedData<Boolean> FISHING = DataTracker.registerData(KikoEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    /** Ánimo hacia el jugador más cercano (0 fría, 1 neutral, 2 amigable, 3 bestie). Lo usa el modelo. */
    /** v3.9 pose que dura (sentada, paraguas, de la mano...). Ver KikoPose. */
    private static final TrackedData<Integer> POSE_STATE = DataTracker.registerData(KikoEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MOOD = DataTracker.registerData(KikoEntity.class, TrackedDataHandlerRegistry.INTEGER);
    /** v3.0: ropa de las chicas que no son Kiko (0 trabajo, 1 casual, 2 boda). */
    private static final TrackedData<Integer> GIRL_OUTFIT = DataTracker.registerData(KikoEntity.class, TrackedDataHandlerRegistry.INTEGER);
    /** v3.0: 0 nada, 1 novia, 2 esposa (de PARTNER). */
    private static final TrackedData<Integer> RELATION = DataTracker.registerData(KikoEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<java.util.Optional<UUID>> PARTNER = DataTracker.registerData(KikoEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    private final KikoAffectionSystem affection = new KikoAffectionSystem();
    private final KikoInventory inventory = new KikoInventory(this);
    private boolean followEnabled = true;
    private int dayCounter = 0;

    // --- Trabajo ---
    private WorkMode workMode = WorkMode.NONE;
    @Nullable
    private BlockPos workCenter;
    /** Quién le encargó el trabajo (a quién le avisa si le falta algo). */
    @Nullable
    private UUID owner;
    /** Para no repetir el mismo aviso cada segundo. */
    private final Map<String, Long> lastNotice = new HashMap<>();
    /** Hacia dónde cava el túnel o la escalera, y cuántos pasos lleva. */
    private Direction workDirection = Direction.NORTH;
    private int tunnelStep;
    /** 1 = túnel de 1x2, 3 = túnel de 3x3. */
    private int tunnelSize = KikoConfig.get().tunnelSize;
    /** "Su cofre", donde guarda sola cuando se le llena la mochila. */
    @Nullable
    private BlockPos homeChest;
    private boolean depositing;
    private int healCooldown;

    // --- Pelea ---
    private CombatMode combatMode = CombatMode.DEFENSIVE;

    // --- Ropa ---
    /** La que tú le escogiste. Es a la que regresa cuando termina un trabajo. */
    private KikoOutfit chosenOutfit = KikoOutfit.SCHOOL;
    /** Ropa de temporada según el bioma (null = la escogida). */
    @Nullable
    private KikoOutfit seasonalOutfit;
    private boolean autoOutfit = true;
    private int biomeCategory;
    private int biomeTicks;
    /** v3.3 chicas y chicos: la ropa que escogiste (0 trabajo, 1 casual, 2 boda, 3 verano, 4 invierno) y la temporada (-1 sin ver, 0 normal, 1 calor, 2 frío). */
    private int chosenGirlOutfit;
    private int girlSeason = -1;

    // --- Quieta mientras la atiendes ---
    private int viewers;
    private long interactUntil;
    @Nullable
    private UUID interactingWith;

    /** Jugador con más afecto que esté cerca (se recalcula cada segundo). */
    @Nullable
    private UUID bestFriend;

    // --- Estado de animación (solo cliente) ---
    @Nullable
    private KikoAnimation currentAnimation;
    /** v3.2: animaciones sueltas cuando no hace nada (bostezar, pensar, asomarse...). */
    private int idleAnimCooldown = 200;
    private int pendingStretch = -1;
    /** Cliente: tocadiscos que está sonando cerca (para bailar). */
    @Nullable
    private BlockPos songSource;
    private int animationStartAge;

    public KikoEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
        this.experiencePoints = 0;
        // Lo que trae en las manos siempre se tira completo al morir (más de 1 = seguro)
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 2.0f);
        this.setEquipmentDropChance(EquipmentSlot.OFFHAND, 2.0f);
        for (EquipmentSlot slot : KikoInventory.ARMOR_SLOTS) this.setEquipmentDropChance(slot, 2.0f);
    }

    public static DefaultAttributeContainer.Builder createKikoAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                // 0.3 es la velocidad normal de un mob (equivale a caminar como el jugador).
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0)
                // Igual que el jugador, así las armas aplican su velocidad de ataque real
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 4.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.2)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24.0);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(OUTFIT, KikoOutfit.SCHOOL.ordinal());
        builder.add(HIDE_ARMOR, false);
        builder.add(FISHING, false);
        builder.add(EXPRESSION, -1);
        builder.add(TALKING, false);
        builder.add(FRIEND, java.util.Optional.empty());
        builder.add(AWAY, false);
        builder.add(MOOD, 1);
        builder.add(POSE_STATE, 0);
        builder.add(GIRL_OUTFIT, 0);
        builder.add(RELATION, 0);
        builder.add(PARTNER, java.util.Optional.empty());
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        // Abre y cierra puertas (para entrar y salir de su casa)
        if (this.getNavigation() instanceof net.minecraft.entity.ai.pathing.MobNavigation nav) {
            nav.setCanPathThroughDoors(true);
            nav.setCanEnterOpenDoors(true);
        }
        this.goalSelector.add(1, new net.minecraft.entity.ai.goal.LongDoorInteractGoal(this, true));
        // Quieta mirándote mientras le hablas o tienes abierto su menú o inventario
        this.goalSelector.add(1, new com.jaciel.kiko.entity.ai.StayStillGoal(this));
        // Se aleja de quien le cae mal (afecto negativo)
        this.goalSelector.add(1, new FleeEntityGoal<>(this, PlayerEntity.class,
                living -> this.affection.get(living.getUuid()) < 0,
                6.0f, 1.0, 1.25,
                living -> !living.isSpectator() && !((PlayerEntity) living).isCreative()));
        this.goalSelector.add(2, new KikoCombatGoal(this));
        this.goalSelector.add(2, new com.jaciel.kiko.entity.ai.KikoSleepGoal(this));
        this.goalSelector.add(3, new FollowFriendGoal(this, 1.15));
        this.goalSelector.add(3, new DepositGoal(this));
        this.goalSelector.add(4, new ChopTreeGoal(this));
        this.goalSelector.add(4, new MineOreGoal(this));
        this.goalSelector.add(4, new TunnelGoal(this));
        this.goalSelector.add(4, new com.jaciel.kiko.entity.work.PlantTreesGoal(this));
        this.goalSelector.add(4, new com.jaciel.kiko.entity.work.FarmGoal(this));
        this.goalSelector.add(4, new com.jaciel.kiko.entity.work.BreedGoal(this));
        this.goalSelector.add(4, new com.jaciel.kiko.entity.work.FishGoal(this));
        this.goalSelector.add(4, new com.jaciel.kiko.entity.work.SmeltGoal(this));
        this.goalSelector.add(4, new com.jaciel.kiko.entity.work.FlattenGoal(this));
        this.goalSelector.add(4, new com.jaciel.kiko.entity.work.FillGoal(this));
        this.goalSelector.add(4, new com.jaciel.kiko.entity.work.SortChestsGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.7) {
            @Override
            public boolean canStart() {
                return workMode == WorkMode.NONE && super.canStart();
            }
        });
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(7, new LookAroundGoal(this));

        // Si alguien la ataca se defiende (pero nunca contra jugadores)
        this.targetSelector.add(1, new RevengeGoal(this, PlayerEntity.class) {
            @Override
            public boolean canStart() {
                return combatMode != CombatMode.PASSIVE && super.canStart();
            }
        });
        // Ataca lo que su amigo ataca y a quien lo lastima
        this.targetSelector.add(2, new HelpFriendTargetGoal(this));
        // Defiende a su mejor amigo de los mobs hostiles cercanos (menos creepers)
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, HostileEntity.class, 5, true, false,
                this::shouldDefendAgainst));
    }

    // ------------------------------------------------------------------
    // Tick
    // ------------------------------------------------------------------

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) return;

        if (this.age % 20 == 0) {
            updateBestFriendAndMood();
        }
        if (workMode != WorkMode.NONE && this.age % 10 == 0) {
            pickUpNearbyItems();
        }
        if (this.age % 20 == 0) updateAutoOutfit();
        if (this.age % 30 == 0) placeTorchIfDark();
        tickExpression();
        if (isAway()) {
            com.jaciel.kiko.entity.work.Expedition.tick(this);
            return;
        }
        if (this.age % 20 == 5) tickPet();
        if (claimHomeBed && this.age % 20 == 7) claimNearbyBed();
        if (this.age % 20 == 11) {
            com.jaciel.kiko.romance.Romance.tick(this);
            tickWeddingDress();
        }
        if (cookJob != null) Cooking.tick(this);
        tickSpecialty();
        if (this.age % 10 == 3) tickRideWithFriend();
        if (this.age % 40 == 7) {
            BlockPos h = getHome();
            // De día, si no te sigue ni trabaja, se queda cerca de su casa
            if (h != null && workMode == WorkMode.NONE && !followEnabled) this.setPositionTarget(h, 16);
            else this.clearPositionTarget();
        }
        // Por si acaso: acostada sin estar dormida (se quedó con la pose) → se para
        if (this.age % 20 == 9 && !this.isSleeping() && this.getPose() == net.minecraft.entity.EntityPose.SLEEPING && !this.isKid()) {
            this.setPose(net.minecraft.entity.EntityPose.STANDING);
        }
        // Dormida pero lejos de su cama (la movieron o se cayó) → la regresa a la cama
        if (this.isSleeping() && this.age % 20 == 9) {
            this.getSleepingPosition().ifPresent(p -> {
                if (this.squaredDistanceTo(p.getX() + 0.5, p.getY() + 0.6875, p.getZ() + 0.5) > 1.0
                        && this.getWorld().getBlockState(p).getBlock() instanceof net.minecraft.block.BedBlock) {
                    this.setPosition(p.getX() + 0.5, p.getY() + 0.6875, p.getZ() + 0.5);
                }
            });
        }
        // Por si acaso: si es de día o ya no está la cama, se levanta
        if (this.isSleeping() && this.age % 20 == 0) {
            boolean bedGone = this.getSleepingPosition()
                    .map(p -> !(this.getWorld().getBlockState(p).getBlock() instanceof net.minecraft.block.BedBlock))
                    .orElse(true);
            PlayerEntity f = getFriendAbove(KikoConfig.get().followThreshold);
            boolean friendAsleep = f != null && f.isSleeping() && f.squaredDistanceTo(this) < 24 * 24;
            if (!keepSleeping() && (bedGone || (!this.getWorld().isNight() && !friendAsleep))) this.wakeUp();
        }
        tickIdleAnimations();
        // Se cura sola con comida o pociones de su mochila
        if (healCooldown > 0) healCooldown--;
        else if (this.age % 20 == 0 && SelfCare.heal(this)) healCooldown = 60;

        // Negligencia, una vez por día de juego
        if (++this.dayCounter >= KikoAffectionSystem.TICKS_PER_DAY) {
            this.dayCounter = 0;
            this.affection.applyDailyNeglect(this.getWorld().getTime());
        }

        // Corazoncitos de vez en cuando si te adora
        if (getMood() == Tier.BESTIE.ordinal() && this.random.nextInt(160) == 0) {
            spawnParticles(ParticleTypes.HEART, 1);
        }
    }

    private void updateBestFriendAndMood() {
        PlayerEntity nearest = this.getWorld().getClosestPlayer(this, 12.0);
        float nearestAffection = nearest != null ? this.affection.get(nearest.getUuid()) : 0f;
        this.dataTracker.set(MOOD, nearest != null ? KikoAffectionSystem.tierOf(nearestAffection).ordinal() : Tier.NEUTRAL.ordinal());

        UUID best = null;
        float bestValue = Float.NEGATIVE_INFINITY;
        for (Map.Entry<UUID, KikoAffectionSystem.Record> e : this.affection.all().entrySet()) {
            PlayerEntity p = this.getWorld().getPlayerByUuid(e.getKey());
            if (p == null || p.isSpectator() || p.squaredDistanceTo(this) > 64 * 64) continue;
            if (e.getValue().affection > bestValue) {
                bestValue = e.getValue().affection;
                best = e.getKey();
            }
        }
        this.bestFriend = best;
        UUID top = this.affection.topFriend(KikoConfig.get().followThreshold);
        if (!java.util.Objects.equals(top, getFriendUuid())) this.dataTracker.set(FRIEND, java.util.Optional.ofNullable(top));
    }

    // ------------------------------------------------------------------
    // Fase 5: amigo, marcador, casa, mascota, expedición, montarse
    // ------------------------------------------------------------------

    // ---- Personaje (Fase 6) ----
    @Nullable
    private KikoCharacter character;

    /** Qué chica es (según su tipo de entidad: kiko, maru, lumi o nadi). */
    public KikoCharacter getCharacter() {
        if (character == null) {
            character = KikoCharacter.byId(net.minecraft.registry.Registries.ENTITY_TYPE.getId(this.getType()).getPath());
        }
        return character;
    }

    private int specialtyCooldown;
    private int lessonTimer;

    /** La especialidad de cada chica (Kiko no tiene: ella cambia de ropa). */
    private void tickSpecialty() {
        if (specialtyCooldown > 0) specialtyCooldown--;
        // La enfermera cura aunque esté peleando
        if (this.age % 100 == 37 && !this.isSleeping() && this.getTarget() == null && !this.getWorld().isClient) {
            SelfCraft.tick(this, getFriendAbove(KikoConfig.get().followThreshold));
        }
        if (getCharacter() == KikoCharacter.RINA && !this.isSleeping()) {
            tickNurse(getFriendAbove(KikoConfig.get().followThreshold));
            return;
        }
        // v3.3 Dante el guardia: los monstruos que van contra ti se le van encima a él
        if (getCharacter() == KikoCharacter.DANTE && !this.isSleeping() && this.age % 20 == 4 && this.getWorld() instanceof ServerWorld dw) {
            PlayerEntity fr = getFriendAbove(KikoConfig.get().followThreshold);
            if (fr != null && fr.squaredDistanceTo(this) < 16 * 16) {
                for (net.minecraft.entity.mob.HostileEntity mob : dw.getEntitiesByClass(net.minecraft.entity.mob.HostileEntity.class,
                        fr.getBoundingBox().expand(10), m -> m.getTarget() == fr && !(m instanceof net.minecraft.entity.mob.CreeperEntity))) {
                    mob.setTarget(this);
                    if (this.getTarget() == null) this.setTarget(mob);
                }
            }
        }
        if (this.isSleeping() || this.getTarget() != null) return;
        PlayerEntity f = getFriendAbove(KikoConfig.get().followThreshold);
        switch (getCharacter()) {
            case TONO -> {
                // Ranchero: los cultivos alrededor de él crecen más rápido
                if (this.age % 100 == 0 && this.getWorld() instanceof ServerWorld sw) growCrops(sw, this.getBlockPos(), 6, 12);
            }
            case EMI -> tickMusician(f);
            case BETO -> {
                // Comerciante: lo que le sobra en la mochila (piedra, tierra, huesos...) lo vende por esmeraldas
                if (this.age % 1200 == 600 && sellJunk() && f != null && f.squaredDistanceTo(this) < 12 * 12) {
                    KikoInteractionHandler.say(f, this, "dialogue.beto.sold", 3);
                }
            }
            case MARU -> {
                // Herrera: con un yunque cerca le da mantenimiento a su equipo y al tuyo
                if (this.age % 100 != 0 || !nearAnvil()) return;
                boolean fixed = repairAll(this.getInventory());
                if (f != null && f.squaredDistanceTo(this) < 5 * 5) {
                    for (net.minecraft.entity.EquipmentSlot slot : net.minecraft.entity.EquipmentSlot.values()) {
                        fixed |= repair(f.getEquippedStack(slot));
                    }
                }
                if (fixed) {
                    this.swingHand(Hand.MAIN_HAND);
                    this.getWorld().playSound(null, this.getBlockPos(), net.minecraft.sound.SoundEvents.BLOCK_ANVIL_USE,
                            net.minecraft.sound.SoundCategory.NEUTRAL, 0.35f, 1.4f);
                    spawnParticles(ParticleTypes.CRIT, 4);
                }
            }
            case LUMI -> {
                if (f == null || f.squaredDistanceTo(this) > 8 * 8) return;
                // Te enseña cosas: experiencia cada 2 minutos y medio si andan juntos
                if (++lessonTimer >= 3000) {
                    lessonTimer = 0;
                    if (this.getWorld() instanceof ServerWorld sw) {
                        net.minecraft.entity.ExperienceOrbEntity.spawn(sw, f.getPos(), 10 + this.random.nextInt(11));
                    }
                    setExpression(com.jaciel.kiko.animation.KikoExpression.EMBARRASSED, 60);
                    KikoInteractionHandler.say(f, this, "dialogue.lumi.lesson", 3);
                }
                // Estudia: con un libro y 3 de lapislázuli en su mochila escribe un libro encantado
                if (specialtyCooldown == 0 && this.age % 200 == 0 && writeEnchantedBook()) {
                    specialtyCooldown = 4800;
                    KikoInteractionHandler.say(f, this, "dialogue.lumi.study", 3);
                }
            }
            case TAMI -> {
                // Cocina sola lo crudo que traiga en la mochila
                if (this.age % 600 == 0 && getCookJob() == null && Cooking.chefCookBag(this)) {
                    this.getWorld().playSound(null, this.getBlockPos(), net.minecraft.sound.SoundEvents.BLOCK_SMOKER_SMOKE,
                            net.minecraft.sound.SoundCategory.NEUTRAL, 0.8f, 1f);
                    spawnParticles(ParticleTypes.SMOKE, 4);
                }
                // Si tienes hambre te da de comer de lo que cocinó
                if (specialtyCooldown == 0 && this.age % 40 == 0 && f != null && f.squaredDistanceTo(this) < 8 * 8
                        && f.getHungerManager().getFoodLevel() <= 12) {
                    int slot = Cooking.findDish(this);
                    if (slot >= 0) {
                        ItemStack dish = this.getInventory().bag().get(slot).split(1);
                        f.getInventory().offerOrDrop(dish);
                        this.swingHand(Hand.MAIN_HAND);
                        setExpression(com.jaciel.kiko.animation.KikoExpression.GRIN, 60);
                        KikoInteractionHandler.say(f, this, "dialogue.tami.serve", 3);
                        specialtyCooldown = 2400;
                    }
                }
                // v3.6 si andas lastimado (después de una pelea) te ofrece algo de tomar
                if (specialtyCooldown == 0 && this.age % 40 == 20 && f != null && f.squaredDistanceTo(this) < 8 * 8
                        && f.getHealth() <= 12f) {
                    int slot = this.getInventory().findInBag(st -> st.getItem() instanceof com.jaciel.kiko.item.DrinkItem);
                    if (slot >= 0) {
                        ItemStack drink = this.getInventory().bag().get(slot).split(1);
                        f.getInventory().offerOrDrop(drink);
                        this.swingHand(Hand.MAIN_HAND);
                        setExpression(com.jaciel.kiko.animation.KikoExpression.GRIN, 60);
                        KikoInteractionHandler.say(f, this, "dialogue.tami.drink_serve", 3);
                        specialtyCooldown = 2400;
                    }
                }
            }
            default -> {}
        }
    }

    /** Le da "ticks aleatorios" a cultivos cercanos (crecen más rápido). Lo usan Toño y su bonus de esposo. */
    public static void growCrops(ServerWorld world, BlockPos center, int radius, int tries) {
        for (int i = 0; i < tries; i++) {
            BlockPos p = center.add(world.random.nextInt(radius * 2 + 1) - radius, world.random.nextInt(5) - 2,
                    world.random.nextInt(radius * 2 + 1) - radius);
            net.minecraft.block.BlockState st = world.getBlockState(p);
            if (st.getBlock() instanceof net.minecraft.block.CropBlock || st.getBlock() instanceof net.minecraft.block.StemBlock
                    || st.getBlock() instanceof net.minecraft.block.SweetBerryBushBlock || st.getBlock() instanceof net.minecraft.block.SaplingBlock
                    || st.getBlock() instanceof net.minecraft.block.CocoaBlock || st.getBlock() instanceof net.minecraft.block.NetherWartBlock) {
                st.randomTick(world, p, world.random);
                if (world.random.nextInt(4) == 0) world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5, 1, 0.2, 0.2, 0.2, 0);
            }
        }
    }

    private int songTicks, songCooldown = 1200;

    /** Emi toca la guitarra: suena una melodía, los de cerca bailan y te cae mejor todo el grupo. */
    private void tickMusician(@Nullable PlayerEntity f) {
        if (!(this.getWorld() instanceof ServerWorld sw)) return;
        if (songTicks > 0) {
            songTicks--;
            if (songTicks % 6 == 0) {
                float[] scale = {0.5f, 0.6f, 0.67f, 0.75f, 0.84f, 1.0f, 1.12f, 1.26f};
                float pitch = scale[(int) ((this.age / 6 + this.getId()) * 7 % scale.length)];
                sw.playSound(null, this.getBlockPos(), net.minecraft.sound.SoundEvents.BLOCK_NOTE_BLOCK_GUITAR.value(),
                        net.minecraft.sound.SoundCategory.NEUTRAL, 0.8f, pitch);
                sw.spawnParticles(ParticleTypes.NOTE, this.getX(), this.getY() + 2.1, this.getZ(), 1, 0.3, 0.1, 0.3, pitch / 2f);
                if (songTicks % 24 == 0) this.swingHand(Hand.MAIN_HAND);
            }
            if (songTicks == 0) {
                // Al terminar, a los que escucharon les caes mejor
                for (PlayerEntity p : sw.getPlayers(p -> p.squaredDistanceTo(this) < 10 * 10)) {
                    for (KikoEntity k : sw.getEntitiesByClass(KikoEntity.class, this.getBoundingBox().expand(10), k -> !k.isSleeping())) {
                        k.getAffection().add(p.getUuid(), k == this ? 1f : 0.3f, sw.getTime());
                    }
                }
                setExpression(com.jaciel.kiko.animation.KikoExpression.GRIN, 60);
            }
            return;
        }
        if (songCooldown > 0) {
            songCooldown--;
            return;
        }
        if (f == null || f.squaredDistanceTo(this) > 8 * 8 || !isIdleForAnimation()) return;
        songTicks = 120;
        songCooldown = 3600 + this.random.nextInt(2400);
        KikoInteractionHandler.say(f, this, "dialogue.emi.play", 3);
        for (KikoEntity k : sw.getEntitiesByClass(KikoEntity.class, this.getBoundingBox().expand(10), k -> k != this && k.isIdleForAnimation())) {
            k.playAnimation(com.jaciel.kiko.animation.KikoAnimation.DANCE);
        }
    }

    /** Beto vende lo que le sobra. Cada 32 de cosas comunes = 1 esmeralda. */
    private boolean sellJunk() {
        java.util.Set<net.minecraft.item.Item> junk = java.util.Set.of(net.minecraft.item.Items.COBBLESTONE, net.minecraft.item.Items.DIRT,
                net.minecraft.item.Items.COBBLED_DEEPSLATE, net.minecraft.item.Items.GRAVEL, net.minecraft.item.Items.SAND,
                net.minecraft.item.Items.ANDESITE, net.minecraft.item.Items.DIORITE, net.minecraft.item.Items.GRANITE,
                net.minecraft.item.Items.TUFF, net.minecraft.item.Items.NETHERRACK, net.minecraft.item.Items.ROTTEN_FLESH,
                net.minecraft.item.Items.BONE, net.minecraft.item.Items.STRING, net.minecraft.item.Items.SPIDER_EYE,
                net.minecraft.item.Items.FLINT, net.minecraft.item.Items.STICK, net.minecraft.item.Items.WHEAT_SEEDS);
        var bag = this.getInventory().bag();
        int emeralds = 0;
        for (int i = 0; i < bag.size(); i++) {
            ItemStack st = bag.get(i);
            if (st.isEmpty() || !junk.contains(st.getItem()) || st.getCount() < 32) continue;
            int lots = st.getCount() / 32;
            st.decrement(lots * 32);
            emeralds += lots;
        }
        if (emeralds == 0) return false;
        ItemStack rest = this.getInventory().insert(new ItemStack(net.minecraft.item.Items.EMERALD, emeralds));
        if (!rest.isEmpty()) this.dropStack(rest);
        this.getWorld().playSound(null, this.getBlockPos(), net.minecraft.sound.SoundEvents.ENTITY_VILLAGER_YES,
                net.minecraft.sound.SoundCategory.NEUTRAL, 0.6f, 1.2f);
        spawnParticles(ParticleTypes.HAPPY_VILLAGER, 6);
        return true;
    }

    private int nurseSmallCooldown;
    private int nurseGirlsTimer;

    /**
     * Enfermera. Si andas muy mal (35 % de vida o menos) te cura de golpe (+8 corazones de vida, regeneración II
     * y te quita lo malo como veneno o wither). Si nomás andas lastimado te da regeneración II.
     * También cura a las demás chicas cerca y a ella misma.
     */
    private void tickNurse(@Nullable PlayerEntity f) {
        if (nurseSmallCooldown > 0) nurseSmallCooldown--;
        if (!(this.getWorld() instanceof ServerWorld sw)) return;
        if (++nurseGirlsTimer >= 100) {
            nurseGirlsTimer = 0;
            for (KikoEntity g : sw.getEntitiesByClass(KikoEntity.class, this.getBoundingBox().expand(8), k -> k.getHealth() < k.getMaxHealth())) {
                g.heal(3f);
                sw.spawnParticles(ParticleTypes.HEART, g.getX(), g.getBodyY(0.9), g.getZ(), 1, 0.3, 0.3, 0.3, 0);
            }
        }
        if (f == null || this.age % 10 != 0 || f.squaredDistanceTo(this) > 10 * 10 || f.isCreative() || f.isSpectator()) return;
        boolean bad = hasHarmfulEffect(f);
        if (specialtyCooldown == 0 && f.getHealth() <= f.getMaxHealth() * 0.35f) {
            f.heal(16f);
            f.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    net.minecraft.entity.effect.StatusEffects.REGENERATION, 100, 1));
            clearHarmful(f);
            sw.spawnParticles(ParticleTypes.HEART, f.getX(), f.getBodyY(0.8), f.getZ(), 8, 0.4, 0.5, 0.4, 0);
            sw.spawnParticles(ParticleTypes.HAPPY_VILLAGER, f.getX(), f.getBodyY(0.5), f.getZ(), 12, 0.4, 0.6, 0.4, 0);
            sw.playSound(null, f.getBlockPos(), net.minecraft.sound.SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                    net.minecraft.sound.SoundCategory.NEUTRAL, 1f, 1.2f);
            this.swingHand(Hand.MAIN_HAND);
            setExpression(com.jaciel.kiko.animation.KikoExpression.NERVOUS, 60);
            KikoInteractionHandler.say(f, this, "dialogue.rina.heal_big", 3);
            specialtyCooldown = 1800;
            nurseSmallCooldown = 400;
        } else if (nurseSmallCooldown == 0 && (f.getHealth() < f.getMaxHealth() * 0.75f || bad)) {
            f.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    net.minecraft.entity.effect.StatusEffects.REGENERATION, 160, 1));
            if (bad) clearHarmful(f);
            sw.spawnParticles(ParticleTypes.HEART, f.getX(), f.getBodyY(0.8), f.getZ(), 4, 0.4, 0.4, 0.4, 0);
            this.swingHand(Hand.MAIN_HAND);
            setExpression(com.jaciel.kiko.animation.KikoExpression.SMILE, 60);
            KikoInteractionHandler.say(f, this, "dialogue.rina.heal_small", 3);
            nurseSmallCooldown = 600;
        }
    }

    private static boolean hasHarmfulEffect(PlayerEntity p) {
        for (net.minecraft.entity.effect.StatusEffectInstance e : p.getStatusEffects()) {
            if (e.getEffectType().value().getCategory() == net.minecraft.entity.effect.StatusEffectCategory.HARMFUL) return true;
        }
        return false;
    }

    private static void clearHarmful(PlayerEntity p) {
        java.util.List<net.minecraft.registry.entry.RegistryEntry<net.minecraft.entity.effect.StatusEffect>> bad = new java.util.ArrayList<>();
        for (net.minecraft.entity.effect.StatusEffectInstance e : p.getStatusEffects()) {
            if (e.getEffectType().value().getCategory() == net.minecraft.entity.effect.StatusEffectCategory.HARMFUL) bad.add(e.getEffectType());
        }
        for (var e : bad) p.removeStatusEffect(e);
    }

    /** Lumi: libro + 3 lapislázuli de su mochila = un libro encantado al azar. */
    private boolean writeEnchantedBook() {
        var inv = this.getInventory();
        int book = inv.findInBag(st -> st.isOf(net.minecraft.item.Items.BOOK));
        int lapis = inv.findInBag(st -> st.isOf(net.minecraft.item.Items.LAPIS_LAZULI) && st.getCount() >= 3);
        if (book < 0 || lapis < 0) return false;
        inv.bag().get(book).decrement(1);
        inv.bag().get(lapis).decrement(3);
        ItemStack out = net.minecraft.enchantment.EnchantmentHelper.enchant(this.random, new ItemStack(net.minecraft.item.Items.BOOK),
                10 + this.random.nextInt(16), this.getWorld().getRegistryManager(), java.util.Optional.empty());
        ItemStack rest = inv.insert(out);
        if (!rest.isEmpty()) this.dropStack(rest);
        this.swingHand(Hand.MAIN_HAND);
        spawnParticles(ParticleTypes.ENCHANT, 20);
        this.getWorld().playSound(null, this.getBlockPos(), net.minecraft.sound.SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE,
                net.minecraft.sound.SoundCategory.NEUTRAL, 1f, 1f);
        setExpression(com.jaciel.kiko.animation.KikoExpression.SMILE, 60);
        return true;
    }

    /** La llave de diálogo que usa esta chica (las de los hijos cambian, ver KikoChildEntity). */
    public String dialogueKey(String key) {
        return getCharacter().dialogue(key);
    }

    /** Dormida en cama o acostada (el bebé en su cuna). Lo usa la animación. */
    public boolean isLyingDown() {
        return this.isSleeping() || this.getPose() == net.minecraft.entity.EntityPose.SLEEPING;
    }

    /** ¿Es hijo o hija? (KikoChildEntity) */
    public boolean isKid() {
        return false;
    }

    /** ¿Es vecino del pueblo? (v3.4, town.TownsfolkEntity) Solo platica y recibe regalos. */
    public boolean isTownsfolk() {
        return false;
    }

    /** ¿Es menor de edad (bebé, niño o adolescente)? No trabaja, no pelea ni se enamora. */
    public boolean isMinor() {
        return false;
    }

    /** Si se queda dormida aunque no haya cama (el bebé en su cuna). */
    protected boolean keepSleeping() {
        return false;
    }

    // ---- Su casa al nacer en su estructura (v3.0.1) ----
    private boolean claimHomeBed;

    /** La cama de Kiko más cercana (8 bloques) queda como su casa, así no se va a dormir a casas ajenas. */
    private void claimNearbyBed() {
        claimHomeBed = false;
        if (getHome() != null) return;
        for (BlockPos p : BlockPos.iterateOutwards(this.getBlockPos(), 8, 3, 8)) {
            if (this.getWorld().getBlockState(p).getBlock() instanceof com.jaciel.kiko.block.KikoBedBlock) {
                setHome(p.toImmutable());
                return;
            }
        }
    }

    // ---- Novias y esposas (v3.0) ----
    private int weddingTicks;
    private int prevGirlOutfit;
    @Nullable
    private KikoOutfit prevOutfit;
    private long dailyDay = -1;
    /** v3.2 aniversario: día de la boda, último aniversario festejado y último día que le diste flores ese día. */
    private long weddingDay = -1, anniversaryDay = -1, anniversaryFlowersDay = -1;
    private long saveDay = -1;
    private long lastJealous;

    public int getGirlOutfit() {
        return this.dataTracker.get(GIRL_OUTFIT);
    }

    /** Lo que escogiste en el menú (si el cambio por bioma está prendido, en calor o frío se pone su ropa de temporada). */
    public void chooseGirlOutfit(int outfit) {
        this.chosenGirlOutfit = Math.max(0, Math.min(4, outfit));
        setGirlOutfit(this.chosenGirlOutfit);
    }

    public int getChosenGirlOutfit() {
        return chosenGirlOutfit;
    }

    public void setGirlOutfit(int outfit) {
        this.dataTracker.set(GIRL_OUTFIT, Math.max(0, Math.min(4, outfit)));
    }

    public int getRelation() {
        return this.dataTracker.get(RELATION);
    }

    @Nullable
    public UUID getPartnerUuid() {
        return this.dataTracker.get(PARTNER).orElse(null);
    }

    /** Novia o esposa de este jugador (null y 0 para dejarla libre). */
    public void setRelation(@Nullable UUID partner, int relation) {
        this.dataTracker.set(PARTNER, java.util.Optional.ofNullable(partner));
        this.dataTracker.set(RELATION, partner == null ? 0 : relation);
        // Sin esposo ya no se queda con el vestido de novia
        if (relation != com.jaciel.kiko.romance.Romance.WIFE) {
            if (getGirlOutfit() == com.jaciel.kiko.romance.Romance.OUTFIT_WEDDING) setGirlOutfit(0);
            if (chosenGirlOutfit == com.jaciel.kiko.romance.Romance.OUTFIT_WEDDING) chosenGirlOutfit = 0;
            if (chosenOutfit == KikoOutfit.WEDDING) setChosenOutfit(KikoOutfit.SCHOOL);
        }
    }

    /** La boda: se pone el vestido de novia unos ticks y luego regresa a lo que traía. */
    public void wearWeddingDress(int ticks) {
        if (weddingTicks <= 0) {
            prevGirlOutfit = getGirlOutfit();
            prevOutfit = chosenOutfit;
        }
        weddingTicks = ticks;
        if (getCharacter() == KikoCharacter.KIKO) setChosenOutfit(KikoOutfit.WEDDING);
        else setGirlOutfit(com.jaciel.kiko.romance.Romance.OUTFIT_WEDDING);
    }

    private void tickWeddingDress() {
        if (weddingTicks <= 0) return;
        weddingTicks -= 20;
        if (weddingTicks > 0) return;
        if (getCharacter() == KikoCharacter.KIKO) {
            if (chosenOutfit == KikoOutfit.WEDDING && prevOutfit != null && prevOutfit != KikoOutfit.WEDDING) setChosenOutfit(prevOutfit);
        } else if (getGirlOutfit() == com.jaciel.kiko.romance.Romance.OUTFIT_WEDDING) {
            setGirlOutfit(prevGirlOutfit == com.jaciel.kiko.romance.Romance.OUTFIT_WEDDING ? 0 : prevGirlOutfit);
        }
    }

    public long getWeddingDay() { return weddingDay; }
    public void setWeddingDay(long d) { this.weddingDay = d; }
    public long getAnniversaryDay() { return anniversaryDay; }
    public void setAnniversaryDay(long d) { this.anniversaryDay = d; }
    public long getAnniversaryFlowersDay() { return anniversaryFlowersDay; }
    public void setAnniversaryFlowersDay(long d) { this.anniversaryFlowersDay = d; }

    public long getDailyDay() {
        return dailyDay;
    }

    public void setDailyDay(long day) {
        this.dailyDay = day;
    }

    public long getSaveDay() {
        return saveDay;
    }

    public void setSaveDay(long day) {
        this.saveDay = day;
    }

    // ---- Familia (v3.1) ----
    private long familyRequestDay = -1;
    @Nullable
    private BlockPos familyCrib;
    private long lastBirthTime;

    public long getFamilyRequestDay() {
        return familyRequestDay;
    }

    @Nullable
    public BlockPos getFamilyCrib() {
        return familyCrib;
    }

    public void setFamilyRequest(long day, @Nullable BlockPos crib) {
        this.familyRequestDay = day;
        this.familyCrib = crib;
    }

    public long getLastBirthTime() {
        return lastBirthTime;
    }

    public void setLastBirthTime(long time) {
        this.lastBirthTime = time;
    }

    /** Se pone celosa como máximo cada 30 segundos. */
    public boolean tryJealous(long now) {
        if (now - lastJealous < 600) return false;
        lastJealous = now;
        return true;
    }

    // ---- Cocina (v2.9) ----
    @Nullable
    private Cooking.Job cookJob;

    @Nullable
    public Cooking.Job getCookJob() {
        return cookJob;
    }

    public void setCookJob(@Nullable Cooking.Job job) {
        this.cookJob = job;
    }

    private boolean nearAnvil() {
        for (BlockPos p : BlockPos.iterateOutwards(this.getBlockPos(), 6, 2, 6)) {
            if (this.getWorld().getBlockState(p).isIn(net.minecraft.registry.tag.BlockTags.ANVIL)) return true;
        }
        return false;
    }

    private static boolean repairAll(net.minecraft.inventory.Inventory inv) {
        boolean any = false;
        for (int i = 0; i < inv.size(); i++) any |= repair(inv.getStack(i));
        return any;
    }

    /** Repara 4 % de un objeto dañado. */
    private static boolean repair(ItemStack s) {
        if (s.isEmpty() || !s.isDamageable() || !s.isDamaged()) return false;
        s.setDamage(Math.max(0, s.getDamage() - Math.max(1, s.getMaxDamage() / 25)));
        return true;
    }

    /** El cliente pone aquí si se dibuja el marcador (contorno a través de paredes). */
    public static java.util.function.Predicate<KikoEntity> clientGlow = k -> false;

    @Nullable
    public UUID getFriendUuid() {
        return this.dataTracker.get(FRIEND).orElse(null);
    }

    @Override
    public boolean isGlowing() {
        if (this.getWorld().isClient && !isAway() && clientGlow.test(this)) return true;
        return super.isGlowing();
    }

    @Override
    public int getTeamColorValue() {
        return this.getScoreboardTeam() == null ? 0xFF8A7A : super.getTeamColorValue();
    }

    // ---- Casa ----
    @Nullable
    private BlockPos home;
    @Nullable
    private String homeDimension;

    @Nullable
    public BlockPos getHome() {
        if (home == null || homeDimension == null) return null;
        return homeDimension.equals(this.getWorld().getRegistryKey().getValue().toString()) ? home : null;
    }

    /** Pone su casa aquí (en la cama de Kiko más cercana si hay). null la quita. */
    public void setHome(@Nullable BlockPos pos) {
        this.home = pos;
        this.homeDimension = pos == null ? null : this.getWorld().getRegistryKey().getValue().toString();
        if (pos == null) this.clearPositionTarget();
    }

    // ---- Mascota ----
    @Nullable
    private UUID petUuid;

    @Nullable
    public net.minecraft.entity.mob.MobEntity getPet() {
        if (petUuid == null || !(this.getWorld() instanceof ServerWorld sw)) return null;
        return sw.getEntity(petUuid) instanceof net.minecraft.entity.mob.MobEntity m && m.isAlive() ? m : null;
    }

    public boolean hasPet() {
        return petUuid != null;
    }

    public void setPet(@Nullable net.minecraft.entity.mob.MobEntity pet) {
        this.petUuid = pet == null ? null : pet.getUuid();
        if (pet != null) pet.attachLeash(this, true);
    }

    /** La mascota siempre va con ella: si se suelta la vuelve a amarrar, si quedó lejos la trae. */
    private void tickPet() {
        net.minecraft.entity.mob.MobEntity pet = getPet();
        if (pet == null) return;
        if (isAway()) return;
        double d = pet.squaredDistanceTo(this);
        if (d > 12 * 12) {
            pet.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), pet.getYaw(), pet.getPitch());
            pet.getNavigation().stop();
        }
        if (!pet.isLeashed() || pet.getLeashHolder() != this) pet.attachLeash(this, true);
        // De vez en cuando la acaricia
        if (!this.isSleeping() && this.getTarget() == null && d < 3 * 3 && this.random.nextInt(60) == 0) {
            this.swingHand(Hand.MAIN_HAND);
            if (this.getWorld() instanceof ServerWorld sw) {
                sw.spawnParticles(ParticleTypes.HEART, pet.getX(), pet.getBodyY(0.8), pet.getZ(), 2, 0.2, 0.2, 0.2, 0);
            }
            setExpression(com.jaciel.kiko.animation.KikoExpression.SMILE, 40);
        }
    }

    // ---- Teletransporte (seguir, silbato) ----
    /** La lleva junto a un jugador (en un lugar seguro). Regresa true si pudo. */
    public boolean teleportNear(PlayerEntity p) {
        BlockPos base = p.getBlockPos();
        World w = this.getWorld();
        for (int i = 0; i < 24; i++) {
            int dx = this.random.nextInt(5) - 2;
            int dz = this.random.nextInt(5) - 2;
            int dy = this.random.nextInt(3) - 1;
            if (Math.abs(dx) < 1 && Math.abs(dz) < 1) continue;
            BlockPos pos = base.add(dx, dy, dz);
            if (w.getBlockState(pos.down()).isSolidBlock(w, pos.down()) && w.isAir(pos) && w.isAir(pos.up())) {
                if (this.isSleeping()) this.wakeUp();
                if (this.hasVehicle()) this.stopRiding();
                this.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, this.getYaw(), this.getPitch());
                this.getNavigation().stop();
                net.minecraft.entity.mob.MobEntity pet = getPet();
                if (pet != null) pet.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, pet.getYaw(), pet.getPitch());
                return true;
            }
        }
        return false;
    }

    // ---- Expedición ----
    private int awayTicks;

    public boolean isAway() {
        return this.dataTracker.get(AWAY);
    }

    public int getAwayTicks() {
        return awayTicks;
    }

    /** Resta un tick a la expedición. Regresa cuántos faltan. */
    public int decrementAway() {
        return awayTicks > 0 ? --awayTicks : 0;
    }

    @Nullable
    public UUID getOwnerUuid() {
        return owner;
    }

    public void setAway(boolean away, int ticks) {
        this.dataTracker.set(AWAY, away);
        this.awayTicks = ticks;
        this.setInvisible(away);
        this.setInvulnerable(away);
        this.setAiDisabled(away);
        this.setNoGravity(away);
        if (away) {
            this.getNavigation().stop();
            this.setVelocity(net.minecraft.util.math.Vec3d.ZERO);
        }
    }

    // ---- Montarse contigo ----
    private void tickRideWithFriend() {
        if (this.isSleeping() || isAway()) return;
        PlayerEntity f = getFriendAbove(KikoConfig.get().followThreshold);
        net.minecraft.entity.Entity myRide = this.getVehicle();
        if (myRide != null) {
            // Se baja cuando tú te bajas
            if (f == null || f.getVehicle() != myRide) this.stopRiding();
            return;
        }
        if (f == null || !followEnabled || workMode != WorkMode.NONE || this.getTarget() != null) return;
        net.minecraft.entity.Entity ride = f.getVehicle();
        if (ride != null && ride.squaredDistanceTo(this) < 6 * 6) {
            this.startRiding(ride); // solo funciona si hay lugar (barca, camello, etc.)
        }
    }

    /** Mejor amigo cercano si su afecto llega al umbral, si no null. */
    @Nullable
    public PlayerEntity getFriendAbove(float threshold) {
        if (this.bestFriend == null) return null;
        PlayerEntity p = this.getWorld().getPlayerByUuid(this.bestFriend);
        if (p == null || !p.isAlive() || p.isSpectator()) return null;
        return this.affection.get(p.getUuid()) >= threshold ? p : null;
    }

    private boolean shouldDefendAgainst(LivingEntity target) {
        switch (combatMode) {
            case PASSIVE -> {
                return false;
            }
            case AGGRESSIVE -> {
                // Cualquier hostil cerca. A los creepers solo si puede dispararles de lejos
                if (target instanceof CreeperEntity) return WeaponHelper.canShoot(this) && target.squaredDistanceTo(this) > 6 * 6;
                return target.squaredDistanceTo(this) < 16 * 16;
            }
            default -> {
                if (target instanceof CreeperEntity) return false; // nada de explotar a su amigo
                PlayerEntity friend = getFriendAbove(KikoConfig.get().defendThreshold);
                return friend != null && target.squaredDistanceTo(friend) < 12 * 12;
            }
        }
    }

    // ------------------------------------------------------------------
    // Interacción
    // ------------------------------------------------------------------

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
        if (this.getWorld().isClient) return ActionResult.SUCCESS;
        return KikoInteractionHandler.handle(this, player);
    }

    /** Acostada no la empujan (el SleepGoal ya la tiene quieta, la IA sigue corriendo para que se despierte). */
    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.isSleeping();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean hurt = super.damage(source, amount);
        if (hurt && !this.getWorld().isClient) {
            if (this.isSleeping()) this.wakeUp();
            setExpression(source.getAttacker() instanceof PlayerEntity ? com.jaciel.kiko.animation.KikoExpression.ANGRY
                    : com.jaciel.kiko.animation.KikoExpression.SURPRISED, 30);
        }
        if (hurt && !this.getWorld().isClient && source.getAttacker() instanceof PlayerEntity player) {
            this.affection.add(player.getUuid(), -KikoConfig.get().affectionDamagePenalty, this.getWorld().getTime());
            playAnimation(KikoAnimation.REJECT);
            spawnParticles(ParticleTypes.ANGRY_VILLAGER, 3);
            // Si ya le caes mal deja de seguirte en ese momento
            if (this.getTarget() == player) this.setTarget(null);
        }
        return hurt;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        com.jaciel.kiko.romance.Romance.onDeath(this);
        if (!this.getWorld().isClient) {
            spawnParticles(ParticleTypes.POOF, 20);
        }
    }

    // ------------------------------------------------------------------
    // Spawn / despawn
    // ------------------------------------------------------------------

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setPersistent();
        // Las que nacen en su casa (estructura) se quedan con la cama de ahí como su casa
        if (spawnReason == SpawnReason.STRUCTURE) this.claimHomeBed = true;
        // Outfit inicial al azar para que cada Kiko se vea distinta
        KikoOutfit[] casual = java.util.Arrays.stream(KikoOutfit.values()).filter(KikoOutfit::isCasual).toArray(KikoOutfit[]::new);
        this.setChosenOutfit(casual[this.random.nextInt(casual.length)]);
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    // ------------------------------------------------------------------
    // Sonidos
    // ------------------------------------------------------------------

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return getMood() >= Tier.FRIENDLY.ordinal() ? ModSounds.HAPPY : null;
    }

    @Override
    public int getMinAmbientSoundDelay() {
        return 400;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.SAD;
    }

    @Override
    public float getSoundPitch() {
        return 1.0f + (this.random.nextFloat() - 0.5f) * 0.15f;
    }

    // ------------------------------------------------------------------
    // Animaciones
    // ------------------------------------------------------------------

    /** ¿Está paradita sin hacer nada? (para las animaciones sueltas) */
    public boolean isIdleForAnimation() {
        return !this.isSleeping() && this.getTarget() == null && !isBeingAttended() && !this.hasVehicle()
                && this.getNavigation().isIdle() && this.getVelocity().horizontalLengthSquared() < 0.0004
                && !this.isImmobile() && !isAway() && !isFishing();
    }

    /** Servidor, cada tick: de vez en cuando hace algo con el cuerpo para que no se vea como estatua. */
    private void tickIdleAnimations() {
        if (pendingStretch >= 0 && --pendingStretch == 0) {
            if (!this.isSleeping()) playAnimation(KikoAnimation.STRETCH);
            pendingStretch = -1;
            idleAnimCooldown = 300;
        }
        if (idleAnimCooldown > 0) {
            idleAnimCooldown--;
            return;
        }
        if (this.age % 20 != 17 || !isIdleForAnimation()) return;
        if (this.random.nextInt(25) != 0) return;
        long tod = this.getWorld().getTimeOfDay() % 24000L;
        boolean night = tod > 13000 && tod < 23000;
        KikoAnimation pick;
        PlayerEntity near = this.getWorld().getClosestPlayer(this, 6.0);
        boolean likes = near != null && this.affection.get(near.getUuid()) >= KikoConfig.get().followThreshold;
        if (night && this.random.nextInt(2) == 0) pick = KikoAnimation.YAWN;
        else if (likes && this.random.nextInt(4) == 0) pick = KikoAnimation.WAVE;
        else {
            KikoAnimation[] pool = isKid() && isMinor()
                    ? new KikoAnimation[]{KikoAnimation.CHEER, KikoAnimation.LOOK, KikoAnimation.THINK, KikoAnimation.HAPPY}
                    : new KikoAnimation[]{KikoAnimation.THINK, KikoAnimation.LOOK, KikoAnimation.STRETCH, KikoAnimation.THINK};
            pick = pool[this.random.nextInt(pool.length)];
        }
        if (pick == KikoAnimation.WAVE && near != null) this.getLookControl().lookAt(near, 30f, 30f);
        playAnimation(pick);
        idleAnimCooldown = 600 + this.random.nextInt(900);
    }

    /** Al despertar se estira. */
    @Override
    public void wakeUp() {
        boolean was = this.isSleeping();
        super.wakeUp();
        if (was && !this.getWorld().isClient) {
            stepOffBed();
            if (!isKid()) pendingStretch = 15;
        }
    }

    /**
     * Si al despertar quedó parada encima de la cama (no había lugar libre a los lados), la baja al espacio libre más cercano.
     */
    private void stepOffBed() {
        World w = this.getWorld();
        BlockPos here = this.getBlockPos();
        boolean onBed = w.getBlockState(here).getBlock() instanceof net.minecraft.block.BedBlock
                || w.getBlockState(here.down()).getBlock() instanceof net.minecraft.block.BedBlock;
        if (!onBed) return;
        BlockPos best = null;
        double bestD = Double.MAX_VALUE;
        for (BlockPos p : BlockPos.iterateOutwards(here, 3, 2, 3)) {
            BlockPos below = p.down();
            if (w.getBlockState(p).getBlock() instanceof net.minecraft.block.BedBlock
                    || w.getBlockState(below).getBlock() instanceof net.minecraft.block.BedBlock) continue;
            if (!w.getBlockState(below).isSideSolidFullSquare(w, below, net.minecraft.util.math.Direction.UP)) continue;
            if (!w.getBlockState(p).getCollisionShape(w, p).isEmpty() || !w.getBlockState(p.up()).getCollisionShape(w, p.up()).isEmpty()) continue;
            double d = p.getSquaredDistance(here);
            if (d < bestD) {
                bestD = d;
                best = p.toImmutable();
            }
        }
        if (best != null) this.refreshPositionAndAngles(best.getX() + 0.5, best.getY(), best.getZ() + 0.5, this.getYaw(), 0f);
    }

    /** El tocadiscos avisa (del lado del cliente) cuando empieza o termina una canción cerca. */
    @Override
    public void setNearbySongPlaying(BlockPos songPosition, boolean playing) {
        this.songSource = playing ? songPosition : null;
    }

    /** Cliente: ¿hay música cerca y está paradita? → baila. */
    public boolean isDancingToMusic() {
        if (songSource == null) return false;
        if (!songSource.isWithinDistance(this.getPos(), 10.0)
                || !this.getWorld().getBlockState(songSource).isOf(net.minecraft.block.Blocks.JUKEBOX)) {
            songSource = null;
            return false;
        }
        return !this.isSleeping() && !this.isLyingDown() && !this.hasVehicle() && this.getTarget() == null && !this.isImmobile();
    }

    // ---- v3.9 cumpleaños y citas ----
    private long birthdayGreeted = -1;
    /** Citas que ha tenido (día:lugar), para el diario de la 3.10. */
    private final java.util.List<String> dateLog = new java.util.ArrayList<>();

    public long getBirthdayGreeted() {
        return birthdayGreeted;
    }

    public void setBirthdayGreeted(long day) {
        this.birthdayGreeted = day;
    }

    public java.util.List<String> getDateLog() {
        return dateLog;
    }

    public com.jaciel.kiko.animation.KikoPose getKikoPose() {
        return com.jaciel.kiko.animation.KikoPose.byIndex(this.dataTracker.get(POSE_STATE));
    }

    public void setKikoPose(com.jaciel.kiko.animation.KikoPose pose) {
        int v = pose == null ? 0 : pose.ordinal();
        if (this.dataTracker.get(POSE_STATE) != v) this.dataTracker.set(POSE_STATE, v);
    }

    /** Servidor: pide a todos los clientes cercanos que reproduzcan una animación. */
    public void playAnimation(KikoAnimation animation) {
        this.getWorld().sendEntityStatus(this, animation.status());
    }

    @Override
    public void handleStatus(byte status) {
        KikoAnimation anim = KikoAnimation.byStatus(status);
        if (anim != null) {
            this.currentAnimation = anim;
            this.animationStartAge = this.age;
        } else {
            super.handleStatus(status);
        }
    }

    @Nullable
    public KikoAnimation getCurrentAnimation() {
        return currentAnimation;
    }

    /**
     * Progreso 0..1 de la animación activa, o -1 si no hay.
     * @param ageWithDelta age + tickDelta, para que se vea suave entre ticks
     */
    public float getAnimationProgress(float ageWithDelta) {
        if (currentAnimation == null) return -1f;
        float t = (ageWithDelta - animationStartAge) / currentAnimation.durationTicks();
        if (t >= 1f || t < 0f) {
            currentAnimation = null;
            return -1f;
        }
        return t;
    }

    // ------------------------------------------------------------------
    // Inventario y trabajo
    // ------------------------------------------------------------------

    /** Mientras trabaja recoge lo que haya tirado cerca (madera, brotes, minerales). */
    private void pickUpNearbyItems() {
        List<ItemEntity> items = this.getWorld().getEntitiesByClass(ItemEntity.class,
                this.getBoundingBox().expand(3.0, 1.5, 3.0), e -> e.isAlive() && !e.cannotPickup());
        for (ItemEntity item : items) {
            ItemStack stack = item.getStack();
            int before = stack.getCount();
            ItemStack rest = inventory.insert(stack);
            int taken = before - rest.getCount();
            if (taken > 0) this.sendPickup(item, taken);
            if (rest.isEmpty()) item.discard();
            else item.setStack(rest);
        }
    }

    /** En cuevas oscuras va poniendo antorchas de su mochila (si trae y está activado en la config). */
    private void placeTorchIfDark() {
        if (!KikoConfig.get().autoTorches || isTownsfolk() || !this.isOnGround() || this.isTouchingWater()) return;
        BlockPos pos = this.getBlockPos();
        World world = this.getWorld();
        if (world.getLightLevel(net.minecraft.world.LightType.BLOCK, pos) > 0) return;
        if (world.getLightLevel(net.minecraft.world.LightType.SKY, pos) >= 8) return;
        if (!world.getBlockState(pos).isAir()) return;
        net.minecraft.block.BlockState torch = net.minecraft.block.Blocks.TORCH.getDefaultState();
        if (!torch.canPlaceAt(world, pos)) return;

        ItemStack source;
        if (this.getOffHandStack().isOf(net.minecraft.item.Items.TORCH)) {
            source = this.getOffHandStack();
        } else {
            int i = inventory.findInBag(st -> st.isOf(net.minecraft.item.Items.TORCH));
            if (i < 0) return;
            source = inventory.bag().get(i);
        }
        world.setBlockState(pos, torch);
        world.playSound(null, pos, net.minecraft.sound.SoundEvents.BLOCK_WOOD_PLACE,
                net.minecraft.sound.SoundCategory.BLOCKS, 0.8f, 1f);
        this.swingHand(Hand.OFF_HAND);
        source.decrement(1);
    }

    // ------------------------------------------------------------------
    // Expresiones (Fase 4)
    // ------------------------------------------------------------------

    private int expressionTicks;
    private int talkTicks;

    /** Pone una expresión por un rato (en ticks). */
    public void setExpression(com.jaciel.kiko.animation.KikoExpression expression, int ticks) {
        this.dataTracker.set(EXPRESSION, expression == null ? -1 : expression.ordinal());
        this.expressionTicks = ticks;
    }

    @Nullable
    public com.jaciel.kiko.animation.KikoExpression getExpression() {
        return com.jaciel.kiko.animation.KikoExpression.byIndex(this.dataTracker.get(EXPRESSION));
    }

    /** Mueve la boca como si hablara. */
    public void talk(int ticks) {
        this.dataTracker.set(TALKING, true);
        this.talkTicks = Math.max(this.talkTicks, ticks);
    }

    public boolean isTalking() {
        return this.dataTracker.get(TALKING);
    }

    private void tickExpression() {
        if (expressionTicks > 0 && --expressionTicks == 0) this.dataTracker.set(EXPRESSION, -1);
        if (talkTicks > 0 && --talkTicks == 0) this.dataTracker.set(TALKING, false);
    }

    public boolean isFishing() {
        return this.dataTracker.get(FISHING);
    }

    public void setFishing(boolean fishing) {
        this.dataTracker.set(FISHING, fishing);
    }

    public KikoInventory getInventory() {
        return inventory;
    }

    public WorkMode getWorkMode() {
        return workMode;
    }

    @Nullable
    public BlockPos getWorkCenter() {
        return workCenter;
    }

    /** Le encarga un trabajo. El área de trabajo es alrededor de donde está ahorita. */
    public void setWorkMode(WorkMode mode, @Nullable PlayerEntity by) {
        this.workMode = mode;
        this.workCenter = mode == WorkMode.NONE ? null : this.getBlockPos();
        this.tunnelStep = 0;
        // El túnel va hacia donde está mirando el jugador que se lo pidió
        if (by != null) this.workDirection = by.getHorizontalFacing();
        if (by != null) this.owner = by.getUuid();
        this.lastNotice.clear();
        this.getNavigation().stop();
    }

    public Direction getWorkDirection() {
        return workDirection;
    }

    public int getTunnelSize() {
        return tunnelSize;
    }

    public void setTunnelSize(int size) {
        this.tunnelSize = size >= 3 ? 3 : 1;
    }

    public int getTunnelStep() {
        return tunnelStep;
    }

    public void setTunnelStep(int step) {
        this.tunnelStep = step;
    }

    @Nullable
    public BlockPos getHomeChest() {
        return homeChest;
    }

    public void setHomeChest(@Nullable BlockPos pos) {
        this.homeChest = pos;
    }

    public boolean isDepositing() {
        return depositing;
    }

    public void setDepositing(boolean depositing) {
        this.depositing = depositing;
    }

    public CombatMode getCombatMode() {
        return combatMode;
    }

    public void setCombatMode(CombatMode mode) {
        this.combatMode = mode;
        if (mode == CombatMode.PASSIVE) this.setTarget(null);
    }

    /** Afecto de su mejor amigo cercano (0 si no hay). Le da bonus de velocidad al trabajar. */
    public float getBestFriendAffection() {
        return bestFriend == null ? 0f : affection.get(bestFriend);
    }

    /** Avisa (con cooldown de 30 s por mensaje) al jugador que le encargó el trabajo. */
    /** Mensaje corto en la barra de acción del jugador que le encargó el trabajo (sin cooldown). */
    public void tellOwnerActionBar(Text message) {
        if (owner == null) return;
        PlayerEntity p = this.getWorld().getPlayerByUuid(owner);
        if (p != null && p.squaredDistanceTo(this) < 32 * 32) p.sendMessage(message, true);
    }

    public void notifyOwner(String key) {
        if (owner == null) return;
        long now = this.getWorld().getTime();
        Long last = lastNotice.get(key);
        if (last != null && now - last < 600) return;
        lastNotice.put(key, now);
        PlayerEntity p = this.getWorld().getPlayerByUuid(owner);
        if (p != null && p.squaredDistanceTo(this) < 64 * 64) {
            KikoInteractionHandler.say(p, this, key, 1);
        }
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
        for (int i = 0; i < KikoInventory.BAG_SIZE; i++) {
            ItemStack s = inventory.bag().get(i);
            if (!s.isEmpty()) {
                this.dropStack(s);
                inventory.bag().set(i, ItemStack.EMPTY);
            }
        }
    }

    // ------------------------------------------------------------------
    // Utilidades
    // ------------------------------------------------------------------

    public void spawnParticles(ParticleEffect particle, int count) {
        if (this.getWorld() instanceof ServerWorld sw) {
            sw.spawnParticles(particle, this.getX(), this.getY() + this.getHeight() + 0.3, this.getZ(),
                    count, 0.35, 0.25, 0.35, 0.02);
        }
    }

    public KikoAffectionSystem getAffection() {
        return affection;
    }

    public KikoOutfit getOutfit() {
        return KikoOutfit.byIndex(this.dataTracker.get(OUTFIT));
    }

    /** Cambia lo que trae puesto ahorita (no lo que escogiste). */
    public void setOutfit(KikoOutfit outfit) {
        this.dataTracker.set(OUTFIT, outfit.ordinal());
    }

    /** Ropa escogida por ti. Se la pone de inmediato. */
    public void setChosenOutfit(KikoOutfit outfit) {
        this.chosenOutfit = outfit;
        this.seasonalOutfit = null;
        this.biomeTicks = 0;
        this.setOutfit(outfit);
    }

    public KikoOutfit getChosenOutfit() {
        return chosenOutfit;
    }

    public boolean isAutoOutfit() {
        return autoOutfit;
    }

    public void setAutoOutfit(boolean auto) {
        this.autoOutfit = auto;
        if (!auto) this.setOutfit(chosenOutfit);
    }

    public boolean isArmorHidden() {
        return this.dataTracker.get(HIDE_ARMOR);
    }

    public void setArmorHidden(boolean hidden) {
        this.dataTracker.set(HIDE_ARMOR, hidden);
    }

    /**
     * Ropa automática (cada segundo)
     *  1. Si está trabajando se pone la ropa de ese trabajo
     *  2. Si lleva 10 minutos en un lugar caliente (playa, desierto, sabana) se pone el vestido de playa,
     *     en uno frío (nieve) el abrigo, y en uno normal regresa a la que le escogiste
     */
    private void updateAutoOutfit() {
        if (getCharacter() != KikoCharacter.KIKO) {
            if (!isKid()) updateGirlSeason();
            return;
        }
        if (!autoOutfit) return;
        int cat = biomeCategoryAt(this.getBlockPos());
        if (cat == biomeCategory) {
            biomeTicks += 20;
        } else {
            biomeCategory = cat;
            biomeTicks = 0;
        }
        int delay = KikoConfig.get().autoOutfitDelayTicks;
        if (biomeTicks >= delay && biomeTicks - 20 < delay) {
            seasonalOutfit = cat == 1 ? KikoOutfit.BEACH : cat == 2 ? KikoOutfit.WINTER : null;
        }
        KikoOutfit desired = workMode.outfit() != null ? workMode.outfit()
                : seasonalOutfit != null ? seasonalOutfit : chosenOutfit;
        if (getOutfit() != desired) {
            setOutfit(desired);
            spawnParticles(ParticleTypes.HAPPY_VILLAGER, 6);
        }
    }

    /** v3.3 chicas y chicos: en calor se ponen su ropa de verano y en frío la de invierno (si traen la de trabajo o la casual). */
    private void updateGirlSeason() {
        if (weddingTicks > 0) return;
        int cat = biomeCategoryAt(this.getBlockPos());
        if (cat == biomeCategory) {
            biomeTicks += 20;
        } else {
            biomeCategory = cat;
            biomeTicks = 0;
        }
        int delay = KikoConfig.get().autoOutfitDelayTicks;
        if (girlSeason < 0 || (biomeTicks >= delay && biomeTicks - 20 < delay)) girlSeason = cat;
        int desired = chosenGirlOutfit;
        if (autoOutfit && (chosenGirlOutfit == 0 || chosenGirlOutfit == 1) && girlSeason > 0) desired = girlSeason == 1 ? 3 : 4;
        if (desired == com.jaciel.kiko.romance.Romance.OUTFIT_WEDDING && getRelation() != com.jaciel.kiko.romance.Romance.WIFE) desired = 0;
        if (getGirlOutfit() != desired) {
            setGirlOutfit(desired);
            spawnParticles(ParticleTypes.HAPPY_VILLAGER, 6);
        }
    }

    /** 0 normal, 1 caliente, 2 frío. */
    private int biomeCategoryAt(BlockPos pos) {
        var biome = this.getWorld().getBiome(pos);
        if (biome.isIn(net.minecraft.registry.tag.BiomeTags.IS_BEACH) || biome.value().getTemperature() >= 1.5f) return 1;
        if (biome.value().isCold(pos)) return 2;
        return 0;
    }

    // ------------------------------------------------------------------
    // Quieta mientras la atiendes
    // ------------------------------------------------------------------

    /** Se queda quieta mirando al jugador durante ticks. */
    public void holdStill(PlayerEntity player, int ticks) {
        this.interactingWith = player.getUuid();
        this.interactUntil = Math.max(this.interactUntil, this.getWorld().getTime() + ticks);
        this.getNavigation().stop();
    }

    public void releaseHold() {
        this.interactUntil = 0;
    }

    public void addViewer(PlayerEntity player, int delta) {
        this.viewers = Math.max(0, this.viewers + delta);
        if (delta > 0) this.interactingWith = player.getUuid();
    }

    public boolean isBeingAttended() {
        return viewers > 0 || this.getWorld().getTime() < interactUntil;
    }

    @Nullable
    public PlayerEntity getAttendingPlayer() {
        return interactingWith == null ? null : this.getWorld().getPlayerByUuid(interactingWith);
    }

    public int getMood() {
        return this.dataTracker.get(MOOD);
    }

    public boolean isFollowEnabled() {
        return followEnabled;
    }

    public void setFollowEnabled(boolean followEnabled) {
        this.followEnabled = followEnabled;
        if (!followEnabled) this.getNavigation().stop();
    }

    // ------------------------------------------------------------------
    // NBT
    // ------------------------------------------------------------------

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Outfit", getOutfit().id());
        nbt.putString("ChosenOutfit", chosenOutfit.id());
        if (seasonalOutfit != null) nbt.putString("SeasonalOutfit", seasonalOutfit.id());
        nbt.putBoolean("AutoOutfit", autoOutfit);
        nbt.putBoolean("HideArmor", isArmorHidden());
        nbt.putBoolean("FollowEnabled", followEnabled);
        nbt.putInt("DayCounter", dayCounter);
        nbt.put("Affection", affection.writeNbt());
        inventory.writeNbt(nbt, this.getRegistryManager());
        nbt.putString("WorkMode", workMode.id());
        if (workCenter != null) nbt.putLong("WorkCenter", workCenter.asLong());
        if (owner != null) nbt.putUuid("WorkOwner", owner);
        nbt.putString("WorkDirection", workDirection.asString());
        nbt.putInt("TunnelStep", tunnelStep);
        nbt.putInt("TunnelSize", tunnelSize);
        if (homeChest != null) nbt.putLong("HomeChest", homeChest.asLong());
        nbt.putString("CombatMode", combatMode.id());
        nbt.putLong("BdayGreeted", birthdayGreeted);
        if (!dateLog.isEmpty()) nbt.putString("DateLog", String.join(";", dateLog));
        if (home != null && homeDimension != null) {
            nbt.putLong("Home", home.asLong());
            nbt.putString("HomeDim", homeDimension);
        }
        if (petUuid != null) nbt.putUuid("Pet", petUuid);
        if (isAway()) nbt.putInt("AwayTicks", awayTicks);
        nbt.putInt("LessonTimer", lessonTimer);
        nbt.putInt("GirlOutfit", getGirlOutfit());
        nbt.putBoolean("ClaimHome", claimHomeBed);
        nbt.putInt("Relation", getRelation());
        if (getPartnerUuid() != null) nbt.putUuid("Partner", getPartnerUuid());
        nbt.putInt("WeddingTicks", weddingTicks);
        nbt.putInt("PrevGirlOutfit", prevGirlOutfit);
        nbt.putInt("ChosenGirlOutfit", chosenGirlOutfit);
        nbt.putInt("GirlSeason", girlSeason);
        if (prevOutfit != null) nbt.putString("PrevOutfit", prevOutfit.id());
        nbt.putLong("DailyDay", dailyDay);
        nbt.putLong("WeddingDay", weddingDay);
        nbt.putLong("AnniversaryDay", anniversaryDay);
        nbt.putLong("AnniversaryFlowers", anniversaryFlowersDay);
        nbt.putLong("SaveDay", saveDay);
        nbt.putLong("FamilyDay", familyRequestDay);
        if (familyCrib != null) nbt.putLong("FamilyCrib", familyCrib.asLong());
        nbt.putLong("LastBirth", lastBirthTime);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        KikoOutfit outfit = KikoOutfit.byId(nbt.getString("Outfit"));
        if (outfit != null) setOutfit(outfit);
        KikoOutfit chosen = KikoOutfit.byId(nbt.getString("ChosenOutfit"));
        chosenOutfit = chosen != null ? chosen : (outfit != null ? outfit : KikoOutfit.SCHOOL);
        seasonalOutfit = KikoOutfit.byId(nbt.getString("SeasonalOutfit"));
        if (nbt.contains("AutoOutfit")) autoOutfit = nbt.getBoolean("AutoOutfit");
        setArmorHidden(nbt.getBoolean("HideArmor"));
        if (nbt.contains("FollowEnabled")) followEnabled = nbt.getBoolean("FollowEnabled");
        dayCounter = nbt.getInt("DayCounter");
        affection.readNbt(nbt.getList("Affection", KikoAffectionSystem.NBT_LIST_TYPE));
        inventory.readNbt(nbt, this.getRegistryManager());
        workMode = WorkMode.byId(nbt.getString("WorkMode"));
        workCenter = nbt.contains("WorkCenter") ? BlockPos.fromLong(nbt.getLong("WorkCenter")) : null;
        owner = nbt.containsUuid("WorkOwner") ? nbt.getUuid("WorkOwner") : null;
        Direction dir = Direction.byName(nbt.getString("WorkDirection"));
        workDirection = dir != null && dir.getAxis().isHorizontal() ? dir : Direction.NORTH;
        tunnelStep = nbt.getInt("TunnelStep");
        if (nbt.contains("TunnelSize")) setTunnelSize(nbt.getInt("TunnelSize"));
        homeChest = nbt.contains("HomeChest") ? BlockPos.fromLong(nbt.getLong("HomeChest")) : null;
        if (nbt.contains("CombatMode")) combatMode = CombatMode.byId(nbt.getString("CombatMode"));
        birthdayGreeted = nbt.contains("BdayGreeted") ? nbt.getLong("BdayGreeted") : -1;
        dateLog.clear();
        if (nbt.contains("DateLog")) for (String d : nbt.getString("DateLog").split(";")) if (!d.isEmpty()) dateLog.add(d);
        if (nbt.contains("Home")) {
            home = BlockPos.fromLong(nbt.getLong("Home"));
            homeDimension = nbt.getString("HomeDim");
        }
        petUuid = nbt.containsUuid("Pet") ? nbt.getUuid("Pet") : null;
        if (nbt.contains("AwayTicks")) setAway(true, nbt.getInt("AwayTicks"));
        lessonTimer = nbt.getInt("LessonTimer");
        this.dataTracker.set(GIRL_OUTFIT, nbt.getInt("GirlOutfit"));
        claimHomeBed = nbt.getBoolean("ClaimHome");
        this.dataTracker.set(RELATION, nbt.getInt("Relation"));
        this.dataTracker.set(PARTNER, nbt.containsUuid("Partner") ? java.util.Optional.of(nbt.getUuid("Partner")) : java.util.Optional.empty());
        weddingTicks = nbt.getInt("WeddingTicks");
        prevGirlOutfit = nbt.getInt("PrevGirlOutfit");
        // Antes de la v3.3 solo se guardaba la que traía puesta
        chosenGirlOutfit = nbt.contains("ChosenGirlOutfit") ? nbt.getInt("ChosenGirlOutfit") : nbt.getInt("GirlOutfit");
        girlSeason = nbt.contains("GirlSeason") ? nbt.getInt("GirlSeason") : -1;
        prevOutfit = nbt.contains("PrevOutfit") ? KikoOutfit.byId(nbt.getString("PrevOutfit")) : null;
        dailyDay = nbt.contains("DailyDay") ? nbt.getLong("DailyDay") : -1;
        weddingDay = nbt.contains("WeddingDay") ? nbt.getLong("WeddingDay") : -1;
        anniversaryDay = nbt.contains("AnniversaryDay") ? nbt.getLong("AnniversaryDay") : -1;
        anniversaryFlowersDay = nbt.contains("AnniversaryFlowers") ? nbt.getLong("AnniversaryFlowers") : -1;
        saveDay = nbt.contains("SaveDay") ? nbt.getLong("SaveDay") : -1;
        familyRequestDay = nbt.contains("FamilyDay") ? nbt.getLong("FamilyDay") : -1;
        familyCrib = nbt.contains("FamilyCrib") ? BlockPos.fromLong(nbt.getLong("FamilyCrib")) : null;
        lastBirthTime = nbt.getLong("LastBirth");
    }
}
