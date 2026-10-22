package com.jaciel.kiko.family;

import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoCharacter;
import com.jaciel.kiko.entity.KikoEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Hijo o hija (v3.1). Usa la misma IA que las chicas, pero
 *  - Crece solo: bebé (en su cuna) → niño/niña → chavo/chava → adulto
 *  - Mientras es menor no trabaja, no pelea, no le pasa nada (no recibe daño) y nunca se enamora
 *  - Ya de grande agarra un oficio: 50 % el de su mamá, si no uno al azar (sin bonus de esposa)
 *  - Hereda el pelo, los ojos y la piel de su papá o mamá (el jugador) y de su mamá (la chica)
 */
public class KikoChildEntity extends KikoEntity {
    public static final int BABY = 0, CHILD = 1, TEEN = 2, ADULT = 3;
    private static final String[] STAGE_NAMES = {"baby", "child", "teen", "adult"};

    private static final TrackedData<Integer> STAGE = DataTracker.registerData(KikoChildEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> MALE = DataTracker.registerData(KikoChildEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> SKIN = DataTracker.registerData(KikoChildEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> HAIR = DataTracker.registerData(KikoChildEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> EYES = DataTracker.registerData(KikoChildEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> CLOTH1 = DataTracker.registerData(KikoChildEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> CLOTH2 = DataTracker.registerData(KikoChildEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> SHOE = DataTracker.registerData(KikoChildEntity.class, TrackedDataHandlerRegistry.INTEGER);
    /** El papá o mamá (jugador), para que el cliente le ponga "tu hijo" / "tu hija" a la etiqueta. */
    private static final TrackedData<java.util.Optional<UUID>> PARENT = DataTracker.registerData(KikoChildEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    private long birthTime;
    @Nullable
    private UUID parent;
    @Nullable
    private UUID mother;
    private KikoCharacter motherCharacter = KikoCharacter.KIKO;
    @Nullable
    private KikoCharacter role;
    @Nullable
    private BlockPos crib;

    // ---- v3.2 jugar ----
    int playTicks, playCooldown = 200, tagCooldown;
    @Nullable
    private UUID playmate;
    private boolean it;

    // ---- v3.2 aprendiz ----
    /** Segundos que pasó de chavo junto a cada oficio (índice = KikoCharacter.ordinal()). */
    private int[] learn = new int[KikoCharacter.values().length];
    private boolean master;
    private long masterGiftDay = -1;

    public KikoChildEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(2, new com.jaciel.kiko.town.TownSchool.StudentGoal(this));
        this.goalSelector.add(3, new KidSoccerGoal(this));
        this.goalSelector.add(3, new TeenApprenticeGoal(this));
        this.goalSelector.add(4, new KidPlayGoal(this));
    }

    // ------------------------------------------------------------------
    // Jugar (v3.2)
    // ------------------------------------------------------------------

    // ---- v3.9 escuela ----
    private int schoolDays;
    private long lastSchoolDay = -1;

    public int getSchoolDays() {
        return schoolDays;
    }

    /** Cuenta un día de clases (una vez por día). */
    public void attendSchool(long day) {
        if (day == lastSchoolDay) return;
        lastSchoolDay = day;
        schoolDays++;
    }

    void startGame(@Nullable KikoChildEntity mate, boolean it, int ticks) {
        this.playmate = mate == null ? null : mate.getUuid();
        this.it = it;
        this.playTicks = ticks;
        this.tagCooldown = it ? 30 : 0;
    }

    void endGame() {
        this.playmate = null;
        this.it = false;
        this.playTicks = 0;
        this.playCooldown = 1200 + this.getRandom().nextInt(1800);
    }

    boolean hasPlaymateId() {
        return playmate != null;
    }

    @Nullable
    UUID getPlaymateId() {
        return playmate;
    }

    @Nullable
    KikoChildEntity getPlaymate() {
        if (playmate == null || !(this.getWorld() instanceof ServerWorld w)) return null;
        return w.getEntity(playmate) instanceof KikoChildEntity k ? k : null;
    }

    boolean isIt() {
        return it;
    }

    void setIt(boolean it) {
        this.it = it;
    }

    // ------------------------------------------------------------------
    // Aprendiz (v3.2)
    // ------------------------------------------------------------------

    /** El oficio en el que más tiempo lleva (o null si nada). */
    @Nullable
    public KikoCharacter getLearning() {
        int best = -1, bestV = 0;
        for (int i = 0; i < learn.length; i++) {
            if (KikoCharacter.values()[i] == KikoCharacter.KIKO) continue;
            if (learn[i] > bestV) {
                bestV = learn[i];
                best = i;
            }
        }
        return best < 0 ? null : KikoCharacter.values()[best];
    }

    public int learnPercent() {
        KikoCharacter l = getLearning();
        if (l == null) return 0;
        return (int) Math.min(100, learn[l.ordinal()] * 100L / Math.max(1, KikoConfig.get().apprenticeSeconds));
    }

    public boolean isMaster() {
        return master;
    }

    /** Su mamá si anda cargada, si no null. */
    @Nullable
    public KikoEntity getMother() {
        if (mother == null || !(this.getWorld() instanceof ServerWorld w)) return null;
        return w.getEntity(mother) instanceof KikoEntity k && k.isAlive() ? k : null;
    }

    /** ¿Ella le puede enseñar? (grande, con oficio) */
    static boolean canTeach(KikoEntity k) {
        return k.isAlive() && !k.isMinor() && k.getCharacter() != KikoCharacter.KIKO && !k.isAway();
    }

    /** Cada segundo de chavo: si anda junto a alguien con oficio, aprende (el doble si ella está trabajando). */
    private void tickLearning(ServerWorld world) {
        KikoEntity teacher = null;
        double best = 10 * 10;
        for (KikoEntity k : world.getEntitiesByClass(KikoEntity.class, this.getBoundingBox().expand(10), k -> k != this && canTeach(k))) {
            double d = k.squaredDistanceTo(this);
            // Su mamá cuenta primero
            if (k.getUuid().equals(mother)) d *= 0.25;
            if (d < best) {
                best = d;
                teacher = k;
            }
        }
        if (teacher == null) return;
        boolean working = teacher.getWorkMode() != com.jaciel.kiko.entity.work.WorkMode.NONE || teacher.isFishing();
        int before = learnPercent();
        KikoCharacter before_role = getLearning();
        learn[teacher.getCharacter().ordinal()] += working ? 2 : 1;
        if (this.getRandom().nextInt(45) == 0) {
            this.playAnimation(com.jaciel.kiko.animation.KikoAnimation.THINK);
            world.spawnParticles(net.minecraft.particle.ParticleTypes.ENCHANT, this.getX(), this.getBodyY(1.0), this.getZ(), 8, 0.3, 0.3, 0.3, 0.6);
        }
        // Aviso cuando ya se lo sabe
        if (before < 100 && learnPercent() >= 100 && before_role == getLearning() && parent != null) {
            PlayerEntity p = world.getPlayerByUuid(parent);
            if (p != null) {
                String g = isMale() ? "m" : "f";
                p.sendMessage(Text.translatable("message.kiko.kid_learned." + g, getDisplayName(),
                        Text.translatable("role.kiko." + getLearning().id() + "." + g), teacher.getDisplayName())
                        .formatted(Formatting.GREEN), false);
                if (p.squaredDistanceTo(this) < 16 * 16)
                    com.jaciel.kiko.interaction.KikoInteractionHandler.say(p, this, "dialogue.kiko.learned", 3);
            }
            this.playAnimation(com.jaciel.kiko.animation.KikoAnimation.CHEER);
            Family.updateKid(world, this);
        } else if (this.age % 1200 == 13) {
            Family.updateKid(world, this);
        }
    }

    /** De grande y maestro: cada 3 días te trae algo de su oficio. */
    private void tickMasterGift(ServerWorld world) {
        if (!master || getStage() != ADULT || parent == null || role == null) return;
        long day = world.getTimeOfDay() / 24000L;
        if (day == masterGiftDay || day % 3 != 0 || world.getTimeOfDay() % 24000L > 12000) return;
        PlayerEntity p = world.getPlayerByUuid(parent);
        if (p == null || p.squaredDistanceTo(this) > 24 * 24 || this.isSleeping()) return;
        masterGiftDay = day;
        com.jaciel.kiko.romance.WifeBonuses.masterGift(world, this, p, day);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(STAGE, BABY);
        builder.add(MALE, true);
        builder.add(SKIN, Traits.SKINS[1]);
        builder.add(HAIR, Traits.HAIRS[1]);
        builder.add(EYES, Traits.EYES[0]);
        builder.add(CLOTH1, Traits.CLOTHES[1]);
        builder.add(CLOTH2, Traits.CLOTHES[6]);
        builder.add(SHOE, Traits.SHOES[0]);
        builder.add(PARENT, java.util.Optional.empty());
    }

    // ------------------------------------------------------------------
    // Datos
    // ------------------------------------------------------------------

    /** Lo arma al nacer. */
    public void setup(PlayerEntity dad, KikoEntity mom, BlockPos cribPos, String name, boolean male) {
        this.parent = dad.getUuid();
        this.dataTracker.set(PARENT, java.util.Optional.of(dad.getUuid()));
        this.mother = mom.getUuid();
        this.motherCharacter = mom.getCharacter();
        this.crib = cribPos;
        // Se cuenta con el reloj del día (avanza al dormir y con /time), no con el tiempo real
        this.birthTime = this.getWorld().getTimeOfDay();
        var r = this.getRandom();
        int[] c = Traits.inherit(Traits.of(dad), mom.getCharacter(), r);
        this.dataTracker.set(MALE, male);
        this.dataTracker.set(SKIN, c[0]);
        this.dataTracker.set(HAIR, c[1]);
        this.dataTracker.set(EYES, c[2]);
        this.dataTracker.set(CLOTH1, Traits.CLOTHES[r.nextInt(Traits.CLOTHES.length)]);
        this.dataTracker.set(CLOTH2, Traits.CLOTHES[r.nextInt(Traits.CLOTHES.length)]);
        this.dataTracker.set(SHOE, Traits.SHOES[r.nextInt(Traits.SHOES.length)]);
        this.setCustomName(Text.literal(name));
        this.setCustomNameVisible(false);
        this.setStage(BABY);
        this.getAffection().add(dad.getUuid(), 60f, this.getWorld().getTime());
        if (mom.getHome() != null) this.setHome(mom.getHome());
        this.setPersistent();
    }

    public int getStage() {
        return this.dataTracker.get(STAGE);
    }

    public boolean isMale() {
        return this.dataTracker.get(MALE);
    }

    public int[] colors() {
        return new int[]{dataTracker.get(SKIN), dataTracker.get(HAIR), dataTracker.get(EYES),
                dataTracker.get(CLOTH1), dataTracker.get(CLOTH2), dataTracker.get(SHOE)};
    }

    @Nullable
    public UUID getParentUuid() {
        return this.getWorld().isClient ? this.dataTracker.get(PARENT).orElse(null) : parent;
    }

    @Nullable
    public UUID getMotherUuid() {
        return mother;
    }

    public KikoCharacter getMotherCharacter() {
        return motherCharacter;
    }

    @Nullable
    public KikoCharacter getRole() {
        return role;
    }

    public long getBirthTime() {
        return birthTime;
    }

    // ------------------------------------------------------------------
    // Para la gente del pueblo (v3.4, town.TownsfolkEntity)
    // ------------------------------------------------------------------

    /** ¿Va creciendo con los días? Los vecinos del pueblo se quedan de la edad que salieron. */
    protected boolean growsUp() {
        return true;
    }

    /** ¿Se duerme en camas? Los hijos no (se levantan), los vecinos sí (en su casa). */
    protected boolean usesBeds() {
        return false;
    }

    /** Pone de una vez sus rasgos (piel, pelo, ojos, ropa 1, ropa 2, zapatos) y si es hombre. */
    protected void setLooks(boolean male, int[] c) {
        this.dataTracker.set(MALE, male);
        this.dataTracker.set(SKIN, c[0]);
        this.dataTracker.set(HAIR, c[1]);
        this.dataTracker.set(EYES, c[2]);
        this.dataTracker.set(CLOTH1, c[3]);
        this.dataTracker.set(CLOTH2, c[4]);
        this.dataTracker.set(SHOE, c[5]);
    }

    /** Lo pone directo en una etapa (sin mensajes ni oficio). */
    protected void setFixedStage(int stage) {
        setStage(stage);
    }

    /** Nombre del modelo según su etapa (ver tools/gen_kids.py). */
    public String modelName() {
        return switch (getStage()) {
            case BABY -> "baby";
            case CHILD -> isMale() ? "child_boy" : "child_girl";
            case TEEN -> isMale() ? "teen_boy" : "teen_girl";
            default -> isMale() ? "adult_man" : "adult_woman";
        };
    }

    /** Altura en bloques de cada etapa. */
    public float stageHeight() {
        return switch (getStage()) {
            case BABY -> 0.6f;
            case CHILD -> 1.1f;
            case TEEN -> 1.55f;
            default -> isMale() ? 1.85f : 1.75f;
        };
    }

    /** La hitbox mide lo de su etapa. El modelo lo escala KidRenderer con la misma cuenta. */
    @Override
    public net.minecraft.entity.EntityDimensions getBaseDimensions(net.minecraft.entity.EntityPose pose) {
        return super.getBaseDimensions(pose).scaled(stageHeight() / 1.8f);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (STAGE.equals(data) || MALE.equals(data)) this.calculateDimensions();
    }

    private void setStage(int stage) {
        this.dataTracker.set(STAGE, stage);
        EntityAttributeInstance scale = this.getAttributeInstance(EntityAttributes.GENERIC_SCALE);
        // v3.1.9 ya no usamos el atributo de escala (vanilla escalaba el modelo desde la cadera y quedaba flotando)
        if (scale != null && scale.getBaseValue() != 1.0) scale.setBaseValue(1.0);
        this.setAiDisabled(stage == BABY);
        this.calculateDimensions();
        if (stage != BABY && this.isSleeping()) this.wakeUp();
        if (this.getPose() == net.minecraft.entity.EntityPose.SLEEPING) this.setPose(net.minecraft.entity.EntityPose.STANDING);
    }

    /** Cuántos ticks de edad tiene al empezar cada etapa. */
    private static long stageStart(int stage) {
        KikoConfig cfg = KikoConfig.get();
        long days = switch (stage) {
            case BABY -> 0;
            case CHILD -> cfg.babyDays;
            case TEEN -> cfg.babyDays + cfg.childDays;
            default -> cfg.babyDays + cfg.childDays + cfg.teenDays;
        };
        return days * 24000L;
    }

    /** Qué etapa le toca según cuántos días tiene (días del reloj del juego, avanzan al dormir). */
    public static int stageFor(long ageTicks) {
        KikoConfig cfg = KikoConfig.get();
        long days = ageTicks / 24000L;
        if (days < cfg.babyDays) return BABY;
        if (days < cfg.babyDays + cfg.childDays) return CHILD;
        if (days < cfg.babyDays + cfg.childDays + cfg.teenDays) return TEEN;
        return ADULT;
    }

    // ------------------------------------------------------------------
    // Lo que cambia respecto a las chicas
    // ------------------------------------------------------------------

    @Override
    public boolean isKid() {
        return true;
    }

    @Override
    public boolean isMinor() {
        return getStage() < ADULT;
    }

    /** De menor no tiene oficio (Kiko = ninguno); ya de grande el que le tocó. */
    @Override
    public KikoCharacter getCharacter() {
        return role != null && getStage() == ADULT ? role : KikoCharacter.KIKO;
    }

    @Override
    protected boolean keepSleeping() {
        return getStage() == BABY;
    }

    @Override
    public boolean isImmobile() {
        return getStage() == BABY || super.isImmobile();
    }

    /** Los menores no reciben daño (salvo /kill o caer al vacío). */
    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (isMinor() && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) return true;
        return super.isInvulnerableTo(source);
    }

    /** Los menores no pelean. */
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(isMinor() ? null : target);
    }

    /**
     * Diálogos de hijo: "dialogue.kid.<etapa>.<lo que sea>" o "dialogue.kid.any.<lo que sea>".
     * Si no hay versión de hijo usa una frase genérica (nunca las de romance de las chicas).
     */
    @Override
    public String dialogueKey(String key) {
        String rest = key;
        if (key.startsWith("dialogue.")) {
            int dot = key.indexOf('.', "dialogue.".length());
            rest = dot > 0 ? key.substring(dot + 1) : key;
        }
        String stage = STAGE_NAMES[getStage()];
        Language lang = Language.getInstance();
        if (getStage() == BABY) {
            return "dialogue.kid.baby.babble." + this.getRandom().nextInt(4);
        }
        for (String cand : new String[]{"dialogue.kid." + stage + "." + rest, "dialogue.kid.any." + rest}) {
            if (lang.hasTranslation(cand)) {
                // Las hijas tienen su versión cuando la frase habla de ella misma ("enojada", "ocupada")
                return !isMale() && lang.hasTranslation(cand + ".girl") ? cand + ".girl" : cand;
            }
        }
        // Lo de trabajar y pelear sí lo puede decir como las chicas (ya de grande)
        if (getStage() == ADULT && !isRomantic(rest) && lang.hasTranslation(key)) return getCharacter().dialogue(key);
        return "dialogue.kid.any.ok." + this.getRandom().nextInt(3);
    }

    private static boolean isRomantic(String rest) {
        for (String p : new String[]{"greet.", "talk.", "gift", "levelup.", "good_morning", "confess", "wedding", "jealous",
                "already_", "propose", "breakup", "wife", "taken", "called", "food", "cook_"}) {
            if (rest.startsWith(p)) return true;
        }
        return false;
    }

    // ------------------------------------------------------------------
    // Tick
    // ------------------------------------------------------------------

    @Override
    public void tick() {
        super.tick();
        if (!(this.getWorld() instanceof ServerWorld world)) return;
        if (growsUp() && this.age % 20 == 13) {
            // Si alguien regresó el reloj con /time set, recorre su nacimiento para que no se quede atorado
            if (world.getTimeOfDay() < birthTime) birthTime = world.getTimeOfDay() - Math.max(0, stageStart(getStage()));
            if (this.age == 33) Family.updateKid(world, this);
            int want = stageFor(world.getTimeOfDay() - birthTime);
            if (want != getStage()) grow(world, want);
        }
        if (this.age % 20 == 7) {
            if (getStage() == TEEN) tickLearning(world);
            tickMasterGift(world);
        }
        // Los hijos no duermen en camas ajenas (si se habían metido a una, se levantan)
        if (!usesBeds() && this.isSleeping() && this.age % 20 == 3) this.wakeUp();
        // El bebé se queda sentadito en su cuna (cada medio segundo lo vuelve a acomodar)
        if (getStage() == BABY && this.age % 10 == 0) layInCrib(world);
    }

    /**
     * Sienta al bebé en el centro del colchón de la cuna (KikoModel le pone la pose de sentadito).
     */
    private void layInCrib(ServerWorld world) {
        BlockState st = crib != null ? world.getBlockState(crib) : null;
        // Si su cuna ya no está (la quitaste o la moviste) busca la más cercana; si no hay, se baja al piso
        if (st == null || !(st.getBlock() instanceof com.jaciel.kiko.block.CribBlock)) {
            crib = null;
            for (BlockPos p : BlockPos.iterateOutwards(this.getBlockPos(), 8, 4, 8)) {
                if (world.getBlockState(p).getBlock() instanceof com.jaciel.kiko.block.CribBlock) {
                    crib = p.toImmutable();
                    break;
                }
            }
            st = crib != null ? world.getBlockState(crib) : null;
            if (crib == null) {
                // Sin cuna: se sienta en el piso (como no tiene IA no le aplica la gravedad sola)
                BlockPos at = this.getBlockPos();
                int n = 0;
                while (n++ < 6 && world.getBlockState(at.down()).getCollisionShape(world, at.down()).isEmpty()) at = at.down();
                double floorY = at.getY() + Math.max(0, world.getBlockState(at).getCollisionShape(world, at).getMax(net.minecraft.util.math.Direction.Axis.Y));
                if (Math.abs(this.getY() - floorY) > 0.05) this.refreshPositionAndAngles(this.getX(), floorY, this.getZ(), this.getYaw(), 0f);
            }
        }
        if (st != null && st.getBlock() instanceof com.jaciel.kiko.block.CribBlock) {
            net.minecraft.util.math.Direction d = st.get(com.jaciel.kiko.block.CribBlock.FACING);
            double x = crib.getX() + 0.5, y = crib.getY() + 0.53, z = crib.getZ() + 0.5;   // arriba de la cobija y la almohada
            float yaw = d.asRotation();
            if (this.squaredDistanceTo(x, y, z) > 0.0025 || this.getYaw() != yaw) {
                this.refreshPositionAndAngles(x, y, z, yaw, 0f);
            }
        }
        this.setVelocity(net.minecraft.util.math.Vec3d.ZERO);
        if (this.getPose() != net.minecraft.entity.EntityPose.STANDING) this.setPose(net.minecraft.entity.EntityPose.STANDING);
    }

    /** El bebé no se acuesta (va sentadito); en la noche cierra los ojitos. */
    @Override
    public boolean isLyingDown() {
        return getStage() != BABY && super.isLyingDown();
    }

    public boolean isBabyAsleep() {
        return getStage() == BABY && this.getWorld().isNight();
    }

    /** Del lado del cliente también: que el cuerpo y la cabeza se queden derechitos en la cuna. */
    @Override
    public void tickMovement() {
        super.tickMovement();
        if (getStage() == BABY) {
            this.setBodyYaw(this.getYaw());
            this.setHeadYaw(this.getYaw());
            this.prevBodyYaw = this.getYaw();
            this.prevHeadYaw = this.getYaw();
        }
    }

    private void grow(ServerWorld world, int stage) {
        boolean becameAdult = stage == ADULT && role == null;
        if (becameAdult) {
            // v3.2 si de chavo aprendió un oficio, ese es el suyo y sale maestro
            if (getLearning() != null && learnPercent() >= 100) {
                role = getLearning();
                master = true;
            } else {
                // 50 % el oficio de su mamá, si no uno al azar (Kiko no tiene oficio, así que sus hijos escogen al azar)
                KikoCharacter[] roles = KikoCharacter.WITH_TRADE;
                role = motherCharacter != KikoCharacter.KIKO && this.getRandom().nextBoolean()
                        ? motherCharacter : roles[this.getRandom().nextInt(roles.length)];
            }
        }
        setStage(stage);
        this.spawnParticles(net.minecraft.particle.ParticleTypes.HAPPY_VILLAGER, 12);
        PlayerEntity p = parent != null ? world.getPlayerByUuid(parent) : null;
        if (p != null) {
            String g = isMale() ? "m" : "f";
            Text msg = stage == ADULT
                    ? Text.translatable((master ? "message.kiko.kid_adult_master." : "message.kiko.kid_adult.") + g, getDisplayName(),
                            Text.translatable("role.kiko." + role.id() + "." + g))
                    : Text.translatable("message.kiko.kid_grew." + STAGE_NAMES[stage] + "." + g, getDisplayName());
            p.sendMessage(msg.copy().formatted(Formatting.LIGHT_PURPLE), false);
        }
        Family.updateKid(world, this);
    }

    // ------------------------------------------------------------------
    // Guardado
    // ------------------------------------------------------------------

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("SchoolDays", schoolDays);
        nbt.putLong("LastSchoolDay", lastSchoolDay);
        nbt.putInt("Stage", getStage());
        nbt.putBoolean("Male", isMale());
        int[] c = colors();
        nbt.putIntArray("Colors", c);
        nbt.putLong("BirthDay", birthTime);
        if (parent != null) nbt.putUuid("Parent", parent);
        if (mother != null) nbt.putUuid("Mother", mother);
        nbt.putString("MotherCharacter", motherCharacter.id());
        if (role != null) nbt.putString("Role", role.id());
        if (crib != null) nbt.putLong("Crib", crib.asLong());
        nbt.putIntArray("Learn", learn);
        nbt.putBoolean("Master", master);
        nbt.putLong("MasterGiftDay", masterGiftDay);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        schoolDays = nbt.getInt("SchoolDays");
        lastSchoolDay = nbt.contains("LastSchoolDay") ? nbt.getLong("LastSchoolDay") : -1;
        this.dataTracker.set(MALE, nbt.getBoolean("Male"));
        int[] c = nbt.getIntArray("Colors");
        if (c.length == 6) {
            this.dataTracker.set(SKIN, c[0]);
            this.dataTracker.set(HAIR, c[1]);
            this.dataTracker.set(EYES, c[2]);
            this.dataTracker.set(CLOTH1, c[3]);
            this.dataTracker.set(CLOTH2, c[4]);
            this.dataTracker.set(SHOE, c[5]);
        }
        if (nbt.contains("BirthDay")) {
            birthTime = nbt.getLong("BirthDay");
        } else if (nbt.contains("BirthTime")) {
            // v3.1.0 contaba con el tiempo real: lo pasa al reloj del día
            long realAge = this.getWorld().getTime() - nbt.getLong("BirthTime");
            birthTime = this.getWorld().getTimeOfDay() - Math.max(0, realAge);
        }
        parent = nbt.containsUuid("Parent") ? nbt.getUuid("Parent") : null;
        this.dataTracker.set(PARENT, java.util.Optional.ofNullable(parent));
        mother = nbt.containsUuid("Mother") ? nbt.getUuid("Mother") : null;
        motherCharacter = KikoCharacter.byId(nbt.getString("MotherCharacter"));
        role = nbt.contains("Role") ? KikoCharacter.byId(nbt.getString("Role")) : null;
        crib = nbt.contains("Crib") ? BlockPos.fromLong(nbt.getLong("Crib")) : null;
        int[] l = nbt.getIntArray("Learn");
        learn = new int[KikoCharacter.values().length];
        System.arraycopy(l, 0, learn, 0, Math.min(l.length, learn.length));
        master = nbt.getBoolean("Master");
        masterGiftDay = nbt.contains("MasterGiftDay") ? nbt.getLong("MasterGiftDay") : -1;
        setStage(nbt.getInt("Stage"));
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (this.getWorld() instanceof ServerWorld world) FamilyState.get(world.getServer()).remove(getUuid());
    }

    /** ¿La cuna sigue ahí y libre? */
    public static boolean cribFree(World world, BlockPos pos) {
        BlockState s = world.getBlockState(pos);
        if (!(s.getBlock() instanceof com.jaciel.kiko.block.CribBlock)) return false;
        return world.getEntitiesByClass(KikoChildEntity.class, new net.minecraft.util.math.Box(pos).expand(0.5),
                k -> k.getStage() == BABY).isEmpty();
    }
}
