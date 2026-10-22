package com.jaciel.kiko.town;

import com.jaciel.kiko.family.KikoChildEntity;
import com.jaciel.kiko.family.Traits;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Vecino del pueblo (v3.4). Sale en las casas del pueblo con rasgos, nombre y oficio al azar.
 *  - Familias: papá y mamá (a veces con hijos, que sacan los colores de ellos) y casas de abuelitos
 *  - Los niños crecen con los días (niño, chavo y ya de grande agarran oficio). Los adultos no envejecen
 *  - No trabaja para ti, no te sigue y no se enamora. Solo platica y recibe regalos (y cocina si le das ingredientes)
 *  - De día anda cerca de su casa, en la noche se mete y se duerme en su cama
 *  - Los niños juegan a las traes y al futbol con los demás niños (también con los tuyos)
 *
 * La casa lo pone con la etiqueta TownRole (dad, mom, grandpa, grandma, kid) y TownChance (de 0 a 1, para que no siempre haya hijos).
 */
public class TownsfolkEntity extends KikoChildEntity {
    /** Modelos (tools/gen_townsfolk.py; los niños usan los de los hijos). */
    public static final String[] MODELS = {"man_shirt", "man_farmer", "man_vest", "man_baker", "man_elder",
            "woman_dress", "woman_bun", "woman_bob", "woman_braid", "woman_elder",
            "child_boy", "child_girl", "teen_boy", "teen_girl", "baby",
            // v3.8 guardias
            "guard_man", "guard_woman"};
    private static final int BABY_MODEL = 14;
    private static final int GUARD_M = 15, GUARD_W = 16, GUARD_JOB = 16;
    /** Muertes de mobs hostiles para llegar a cada rango (recluta, guardia, sargento, capitán). */
    private static final int[] RANK_KILLS = {0, 6, 18, 40};
    private static final int[] DADS = {0, 1, 2, 3}, MOMS = {5, 6, 7, 8};
    private static final int GRANDPA = 4, GRANDMA = 9;

    /** Oficios (llave job.kiko.<id>). Cada modelo tiene los que le quedan con su ropa. */
    public static final String[] JOBS = {"none", "woodcutter", "carpenter", "farmer", "shopkeeper", "mailman", "baker",
            "grandpa", "seamstress", "florist", "cook", "teacher", "doctor", "gardener", "grandma", "student", "guard"};
    private static final int[][] JOBS_BY_MODEL = {
            {1, 2}, {3}, {4, 5}, {6}, {7},
            {8, 9}, {10, 6}, {11, 12}, {3, 13}, {14},
            {0}, {0}, {15}, {15}, {0}, {16}, {16}};

    /** Peinados de hombre que le caben con su ropa (0 normal, 1 greñas, 2 chinitos, 3 copete). Con sombrero solo 0 y 1. */
    private static final int[][] HAIR_BY_MODEL = {{0, 1, 2, 3}, {0, 1}, {0, 1}, {0, 1, 2, 3}, {0, 1, 3}};

    public static final String[] NAMES_M = {"Ramón", "Julio", "Pedro", "Chuy", "Memo", "Lalo", "Paco", "Rigo", "Tavo", "Nacho",
            "Beni", "Chencho", "Fer", "Rubén", "Raúl", "Pancho", "Goyo", "Chava", "Poncho", "Neto", "Lencho", "Toñito", "Quique", "Rafa"};
    public static final String[] NAMES_F = {"Lupita", "Rosa", "Carmen", "Mari", "Chela", "Paty", "Lety", "Vero", "Nena", "Juana",
            "Tere", "Male", "Sofi", "Cata", "Lola", "Meche", "Chayo", "Pili", "Fany", "Rocío", "Yola", "Licha", "Dulce", "Elena"};
    private static final int[] GRAYS = {0xECE8DE, 0xC0D0E2, 0xAAAAB0, 0xD8D4CC};
    /** Colores de pelo natural (sin los de fantasía) para la mayoría de los adultos. */
    private static final int[] NATURAL = {0, 1, 2, 3, 4, 5, 7};

    private static final TrackedData<Integer> MODEL = DataTracker.registerData(TownsfolkEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> HAIR_STYLE = DataTracker.registerData(TownsfolkEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> JOB = DataTracker.registerData(TownsfolkEntity.class, TrackedDataHandlerRegistry.INTEGER);
    /** v3.4.3 tipo de cuerpo (0 normal, 1 delgado, 2 atlético, 3 suave, 4 llenito; ver KikoModel). */
    private static final TrackedData<Integer> BODY = DataTracker.registerData(TownsfolkEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private boolean townInit;
    private int guardKills;
    private String townRole = "any";
    private float townChance = 1f;
    @Nullable
    private UUID spouse;
    /** v3.4.1 los niños del pueblo sí crecen (día del reloj en que "nacieron"). */
    private long townBirth = -1;

    // ---- v3.5 vida del pueblo ----
    @Nullable
    private UUID townMom, townDad, fiance;
    private long weddingDay = -1, lastLifeDay = -1, lastBabyDay = -100, lastBabyTry = -1;
    private int kidsBorn, weddingStage, weddingTicks;
    @Nullable
    private BlockPos weddingBell;
    // ---- v3.5 encargo ----
    private String requestItem = "";
    private int requestCount;
    private long requestDay = -1;

    public TownsfolkEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        // Si hay monstruos se hace a un lado (no pelea)
        this.goalSelector.add(1, new FleeEntityGoal<>(this, HostileEntity.class, 10f, 1.0, 1.3, e -> !isGuard() && !TownRaid.hiding(this)));
        this.goalSelector.add(1, new TownRaid.HideGoal(this));
        this.goalSelector.add(2, new TownWeather.RainHomeGoal(this));
        this.goalSelector.add(2, new TownWeather.StargazeGoal(this));
        this.goalSelector.add(2, new TownSchool.TeacherGoal(this));
        this.goalSelector.add(2, new TownSleepGoal(this));
        this.goalSelector.add(3, new Birthdays.GatherGoal(this));
        this.goalSelector.add(3, new TownFestival.FestivalGoal(this));
        this.goalSelector.add(3, new TownMarket.Goal_(this));
        this.goalSelector.add(3, new TownGrowth.BuildGoal(this));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(MODEL, 0);
        builder.add(HAIR_STYLE, 0);
        builder.add(JOB, 0);
        builder.add(BODY, 0);
    }

    // ------------------------------------------------------------------
    // Lo que lo hace vecino
    // ------------------------------------------------------------------

    @Override
    public boolean isTownsfolk() {
        return true;
    }

    @Override
    protected boolean growsUp() {
        return false;
    }

    @Override
    protected boolean usesBeds() {
        return true;
    }

    @Override
    public String modelName() {
        return MODELS[Math.floorMod(this.dataTracker.get(MODEL), MODELS.length)];
    }

    public int getModelIndex() {
        return Math.floorMod(this.dataTracker.get(MODEL), MODELS.length);
    }

    public boolean isElder() {
        int m = getModelIndex();
        return m == GRANDPA || m == GRANDMA;
    }

    public boolean isTownKid() {
        return kidModel(getModelIndex());
    }

    /** Un guardia de plaza nuevo (para pueblos que se generaron sin ellos). */
    public static boolean spawnGuard(ServerWorld world, BlockPos pos) {
        TownsfolkEntity g = com.jaciel.kiko.registry.ModEntities.TOWNSFOLK.create(world);
        if (g == null) return false;
        g.townRole = "guard";
        g.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world.getRandom().nextInt(360), 0);
        return world.spawnEntity(g);
    }

    private static boolean kidModel(int m) {
        return m >= 10 && m <= 14;
    }

    // ------------------------------------------------------------------
    // Guardias (v3.8)
    // ------------------------------------------------------------------
    public boolean isGuard() {
        int m = getModelIndex();
        return m == GUARD_M || m == GUARD_W;
    }

    /** 0 recluta, 1 guardia, 2 sargento, 3 capitán (se guarda en la ranura del peinado, que ahí pinta el penacho). */
    public int getRank() {
        return isGuard() ? Math.max(0, Math.min(3, getHairStyle())) : 0;
    }

    /** Unos vigilan toda la noche y otros duermen. */
    public boolean isNightWatch() {
        return isGuard() && (this.getUuid().hashCode() & 1) == 0;
    }

    private void initGuard(ServerWorld world, Random r) {
        this.dataTracker.set(JOB, GUARD_JOB);
        int roll = r.nextInt(100);
        int rank = roll < 50 ? 0 : roll < 82 ? 1 : roll < 96 ? 2 : 3;
        if (rank == 3 && captainNearby(world)) rank = 2;
        guardKills = RANK_KILLS[rank];
        this.dataTracker.set(HAIR_STYLE, rank);
        this.dataTracker.set(BODY, r.nextBoolean() ? 2 : 4);
        applyRank();
        this.setHealth(this.getMaxHealth());
    }

    private boolean captainNearby(ServerWorld world) {
        return !world.getEntitiesByClass(TownsfolkEntity.class, this.getBoundingBox().expand(96),
                t -> t != this && t.isAlive() && t.isGuard() && t.getRank() >= 3).isEmpty();
    }

    /** Vida y espada según el rango. */
    private void applyRank() {
        int rank = getRank();
        var hp = this.getAttributeInstance(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MAX_HEALTH);
        if (hp != null) hp.setBaseValue(30 + rank * 6);
        this.setCombatMode(com.jaciel.kiko.entity.combat.CombatMode.AGGRESSIVE);
        net.minecraft.item.Item sword = switch (rank) {
            case 0 -> net.minecraft.item.Items.STONE_SWORD;
            case 3 -> net.minecraft.item.Items.DIAMOND_SWORD;
            default -> net.minecraft.item.Items.IRON_SWORD;
        };
        this.equipStack(net.minecraft.entity.EquipmentSlot.MAINHAND, new net.minecraft.item.ItemStack(sword));
        this.setEquipmentDropChance(net.minecraft.entity.EquipmentSlot.MAINHAND, 0f);
    }

    private void addKill(ServerWorld world) {
        guardKills++;
        int rank = getRank();
        int want = 0;
        for (int i = 0; i < RANK_KILLS.length; i++) if (guardKills >= RANK_KILLS[i]) want = i;
        if (want >= 3 && captainNearby(world)) want = 2;
        if (want <= rank) return;
        this.dataTracker.set(HAIR_STYLE, want);
        applyRank();
        this.setHealth(this.getMaxHealth());
        world.spawnParticles(net.minecraft.particle.ParticleTypes.HAPPY_VILLAGER, this.getX(), this.getBodyY(1.0), this.getZ(), 20, 0.5, 0.6, 0.5, 0);
        Text msg = Text.translatable("message.kiko.guard_promoted", this.getDisplayName(),
                Text.translatable("rank.kiko.guard." + want + (isMale() ? ".m" : ".f")));
        for (net.minecraft.server.network.ServerPlayerEntity p : world.getPlayers()) {
            if (p.squaredDistanceTo(this) < 64 * 64) p.sendMessage(msg, false);
        }
    }

    @Override
    public boolean onKilledOther(ServerWorld world, LivingEntity other) {
        boolean r = super.onKilledOther(world, other);
        if (isGuard() && other instanceof HostileEntity) addKill(world);
        return r;
    }

    /** Los monstruos no matan niños ni bebés. */
    @Override
    public boolean damage(net.minecraft.entity.damage.DamageSource source, float amount) {
        if (isTownKid() && source.getAttacker() instanceof HostileEntity) return false;
        return super.damage(source, amount);
    }

    @Override
    public float stageHeight() {
        return switch (getModelIndex()) {
            case BABY_MODEL -> 0.6f;
            case 10, 11 -> 1.1f;
            case 12, 13 -> 1.55f;
            case GRANDPA -> 1.75f;
            case GRANDMA -> 1.62f;
            case GUARD_M -> 1.95f;
            case GUARD_W -> 1.8f;
            default -> getModelIndex() < 5 ? 1.85f : 1.72f;
        };
    }

    public int getHairStyle() {
        return this.dataTracker.get(HAIR_STYLE);
    }

    public int getBodyType() {
        return this.dataTracker.get(BODY);
    }

    private void randomBody() {
        this.dataTracker.set(BODY, this.getRandom().nextInt(5));
    }

    public String getJobId() {
        return JOBS[Math.floorMod(this.dataTracker.get(JOB), JOBS.length)];
    }

    @Nullable
    public UUID getSpouse() {
        return spouse;
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (MODEL.equals(data)) this.calculateDimensions();
    }

    /** Dormido no se mueve ni voltea (si no, la IA lo andaba moviendo en la cama). */
    @Override
    public boolean isImmobile() {
        return this.isSleeping() || super.isImmobile();
    }

    /** No pelea con nadie (de eso se encargan tus compañeras). */
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(isGuard() && target instanceof HostileEntity ? target : null);
    }

    /** Nunca se va con nadie ni lo puedes poner a trabajar. */
    @Override
    public boolean isFollowEnabled() {
        return false;
    }

    // ------------------------------------------------------------------
    // Diálogos: dialogue.town.<kid|elder|adult>.<lo que sea>, luego dialogue.town.any.<lo que sea>
    // (".girl" si es mujer y hay versión, ".f" la pone el que habla si tú eres mujer)
    // ------------------------------------------------------------------

    @Override
    public String dialogueKey(String key) {
        String rest = key;
        if (key.startsWith("dialogue.")) {
            int dot = key.indexOf('.', "dialogue.".length());
            rest = dot > 0 ? key.substring(dot + 1) : key;
        }
        if (getStage() == BABY) return "dialogue.kid.baby.babble." + this.getRandom().nextInt(4);
        String group = isTownKid() ? "kid" : isElder() ? "elder" : "adult";
        Language lang = Language.getInstance();
        // v3.9 los niños que van a la escuela te platican lo que aprendieron
        if (rest.startsWith("talk.") && isTownKid() && getSchoolDays() > 0 && this.getRandom().nextInt(3) == 0) {
            String school = "dialogue.town.kid.school." + this.getRandom().nextInt(6);
            if (lang.hasTranslation(school)) return girlish(lang, school);
        }
        // De vez en cuando platica de su oficio
        if (rest.startsWith("talk.") && !rest.startsWith("talk.distant") && !isTownKid() && this.getRandom().nextInt(3) == 0) {
            String job = "dialogue.town.job." + getJobId() + "." + this.getRandom().nextInt(2);
            if (lang.hasTranslation(job)) return girlish(lang, job);
        }
        for (String cand : new String[]{"dialogue.town." + group + "." + rest, "dialogue.town.any." + rest}) {
            if (lang.hasTranslation(cand)) return girlish(lang, cand);
        }
        // Sin la variante numerada, prueba con la 0
        String base = rest.replaceAll("\\.\\d+$", "");
        for (String cand : new String[]{"dialogue.town." + group + "." + base + ".0", "dialogue.town.any." + base + ".0"}) {
            if (lang.hasTranslation(cand)) return girlish(lang, cand);
        }
        return "dialogue.town.any.ok." + this.getRandom().nextInt(3);
    }

    private String girlish(Language lang, String key) {
        return !isMale() && lang.hasTranslation(key + ".girl") ? key + ".girl" : key;
    }

    // ------------------------------------------------------------------
    // Tick: se arma solo la primera vez
    // ------------------------------------------------------------------

    @Override
    public void tick() {
        super.tick();
        if (!(this.getWorld() instanceof ServerWorld world)) return;
        // Ya amaneció (o no es de noche) y sigue en la cama: se levanta
        if (this.isSleeping() && this.age % 20 == 2 && !TownSleepGoal.isNightTime(world)) this.wakeUp();
        if (townInit) {
            if (isGuard()) {
                if (this.isSleeping() && this.getTarget() != null) this.wakeUp();
                if (this.age % 40 == 9 && this.getHealth() < this.getMaxHealth() && this.getTarget() == null) this.heal(1.0f);
            }
            if (this.age % 20 == 17 && isTownKid()) tickGrowing(world);
            if (!isTownKid()) {
                if (this.age % 20 == 5) tickLife(world);
                if (weddingStage > 0) tickWedding(world);
            }
            return;
        }
        // Los niños esperan tantito a que sus papás ya tengan colores (para parecérseles)
        if (townRole.equals("kid") && this.age < 30) return;
        setupTown(world);
    }

    private void setupTown(ServerWorld world) {
        townInit = true;
        Random r = this.getRandom();
        if (r.nextFloat() >= townChance) {
            this.discard();
            return;
        }
        int model = switch (townRole) {
            case "dad" -> DADS[r.nextInt(DADS.length)];
            case "mom" -> MOMS[r.nextInt(MOMS.length)];
            case "grandpa" -> GRANDPA;
            case "grandma" -> GRANDMA;
            case "kid" -> 10 + r.nextInt(4);
            case "guard" -> r.nextBoolean() ? GUARD_M : GUARD_W;
            default -> r.nextBoolean() ? DADS[r.nextInt(DADS.length)] : MOMS[r.nextInt(MOMS.length)];
        };
        // Algunos papás y mamás del pueblo son guardias
        if ((townRole.equals("dad") || townRole.equals("mom")) && r.nextInt(100) < com.jaciel.kiko.config.KikoConfig.get().townGuardChance) {
            model = townRole.equals("dad") ? GUARD_M : GUARD_W;
        }
        boolean male = model < 5 || model == 10 || model == 12 || model == GUARD_M;
        this.dataTracker.set(MODEL, model);

        // Colores: piel, pelo y ojos (los niños de uno de sus papás), ropa y zapatos al azar
        int skin = Traits.SKINS[r.nextInt(Traits.SKINS.length)];
        int hair = model == GRANDPA || model == GRANDMA ? GRAYS[r.nextInt(GRAYS.length)]
                : r.nextFloat() < 0.85f ? Traits.HAIRS[NATURAL[r.nextInt(NATURAL.length)]] : Traits.HAIRS[8 + r.nextInt(Traits.HAIRS.length - 8)];
        int eyes = Traits.EYES[r.nextInt(Traits.EYES.length)];
        if (townRole.equals("kid")) {
            TownsfolkEntity dad = null, mom = null;
            for (TownsfolkEntity t : world.getEntitiesByClass(TownsfolkEntity.class, this.getBoundingBox().expand(9),
                    t -> t != this && t.townInit && !t.isTownKid() && !t.isElder())) {
                if (t.isMale() && dad == null) dad = t;
                if (!t.isMale() && mom == null) mom = t;
            }
            if (dad != null) townDad = dad.getUuid();
            if (mom != null) townMom = mom.getUuid();
            if (dad != null || mom != null) {
                int[] a = (dad != null ? dad : mom).colors(), b = (mom != null ? mom : dad).colors();
                skin = switch (r.nextInt(3)) {
                    case 0 -> a[0];
                    case 1 -> b[0];
                    default -> Traits.mix(a[0], b[0]);
                };
                hair = r.nextBoolean() ? a[1] : b[1];
                eyes = r.nextBoolean() ? a[2] : b[2];
            }
        }
        int c1 = Traits.CLOTHES[r.nextInt(Traits.CLOTHES.length)];
        int c2;
        do {
            c2 = Traits.CLOTHES[r.nextInt(Traits.CLOTHES.length)];
        } while (c2 == c1);
        int shoe = Traits.SHOES[r.nextInt(Traits.SHOES.length)];
        setLooks(male, new int[]{skin, hair, eyes, c1, c2, shoe});
        setFixedStage(model >= 12 && model <= 14 ? TEEN : kidModel(model) ? CHILD : ADULT);
        if (kidModel(model)) townBirth = bornFor(model >= 12 ? TEEN : CHILD, world, r);
        if (model < 5) {
            int[] styles = HAIR_BY_MODEL[model];
            this.dataTracker.set(HAIR_STYLE, styles[r.nextInt(styles.length)]);
        }
        int[] jobs = JOBS_BY_MODEL[model];
        this.dataTracker.set(JOB, jobs[r.nextInt(jobs.length)]);
        if (!kidModel(model)) randomBody();
        if (model == GUARD_M || model == GUARD_W) initGuard(world, r);

        // Nombre que no se repita en el pueblo
        Set<String> used = new HashSet<>();
        for (TownsfolkEntity t : world.getEntitiesByClass(TownsfolkEntity.class, this.getBoundingBox().expand(96), t -> t != this)) {
            if (t.getCustomName() != null) used.add(t.getCustomName().getString());
        }
        String[] pool = male ? NAMES_M : NAMES_F;
        String name = pool[r.nextInt(pool.length)];
        for (int i = 0; i < 12 && used.contains(name); i++) name = pool[r.nextInt(pool.length)];
        this.setCustomName(Text.literal(name));
        this.setCustomNameVisible(false);

        // Su casa es donde salió; la pareja (papá con mamá, abuelito con abuelita) queda anotada para después
        this.setHome(this.getBlockPos());
        if (!townRole.equals("kid")) {
            for (TownsfolkEntity t : world.getEntitiesByClass(TownsfolkEntity.class, this.getBoundingBox().expand(9),
                    t -> t != this && t.townInit && t.spouse == null && !t.isTownKid() && t.isMale() != male && t.isElder() == isElder())) {
                this.spouse = t.getUuid();
                t.spouse = this.getUuid();
                break;
            }
        }
        this.setPersistent();
    }

    // ------------------------------------------------------------------
    // Crecer (v3.4.1)
    // ------------------------------------------------------------------

    /** Un día de nacimiento que lo deja a la mitad (al azar) de esa etapa, para que no crezcan todos el mismo día. */
    private static long bornFor(int stage, ServerWorld world, Random r) {
        com.jaciel.kiko.config.KikoConfig cfg = com.jaciel.kiko.config.KikoConfig.get();
        long days = stage == TEEN ? cfg.babyDays + cfg.childDays + r.nextInt(Math.max(1, cfg.teenDays))
                : cfg.babyDays + r.nextInt(Math.max(1, cfg.childDays));
        return world.getTimeOfDay() - days * 24000L;
    }

    private void tickGrowing(ServerWorld world) {
        int model = getModelIndex();
        int now = model == BABY_MODEL ? BABY : model >= 12 ? TEEN : CHILD;
        if (townBirth < 0 || world.getTimeOfDay() < townBirth) townBirth = now == BABY ? world.getTimeOfDay() : bornFor(now, world, this.getRandom());
        int want = KikoChildEntity.stageFor(world.getTimeOfDay() - townBirth);
        if (want <= now) return;
        Random r = this.getRandom();
        if (want == CHILD) {
            // De bebé a niño: se baja de la cuna
            this.dataTracker.set(MODEL, isMale() ? 10 : 11);
            setFixedStage(CHILD);
        } else if (want == TEEN) {
            this.dataTracker.set(MODEL, (model == BABY_MODEL ? (isMale() ? 10 : 11) : model) + 2);
            this.dataTracker.set(JOB, 15);
            setFixedStage(TEEN);
        } else {
            int adult = isMale() ? DADS[r.nextInt(DADS.length)] : MOMS[r.nextInt(MOMS.length)];
            // v3.9 los que fueron a la escuela salen con mejor oficio
            boolean schooled = getSchoolDays() >= com.jaciel.kiko.config.KikoConfig.get().schoolDaysForJob;
            if (schooled) adult = isMale() ? (r.nextBoolean() ? 2 : 0) : 7;
            boolean guard = r.nextInt(100) < com.jaciel.kiko.config.KikoConfig.get().townGuardChance;
            if (guard) adult = isMale() ? GUARD_M : GUARD_W;
            this.dataTracker.set(MODEL, adult);
            if (adult < 5) {
                int[] styles = HAIR_BY_MODEL[adult];
                this.dataTracker.set(HAIR_STYLE, styles[r.nextInt(styles.length)]);
            }
            int[] jobs = JOBS_BY_MODEL[adult];
            this.dataTracker.set(JOB, jobs[r.nextInt(jobs.length)]);
            if (schooled && !guard) this.dataTracker.set(JOB, adult == 2 ? 4 : adult == 0 ? 2 : (r.nextBoolean() ? 11 : 12));
            // Ya de grande estrena ropa
            int[] c = colors();
            c[3] = Traits.CLOTHES[r.nextInt(Traits.CLOTHES.length)];
            c[4] = Traits.CLOTHES[r.nextInt(Traits.CLOTHES.length)];
            setLooks(isMale(), c);
            townRole = isMale() ? "dad" : "mom";
            randomBody();
            setFixedStage(ADULT);
            if (guard) initGuard(world, r);
        }
        this.calculateDimensions();
        world.spawnParticles(net.minecraft.particle.ParticleTypes.HAPPY_VILLAGER, this.getX(), this.getBodyY(0.6), this.getZ(), 12, 0.4, 0.5, 0.4, 0);
    }

    // ------------------------------------------------------------------
    // Novios, bodas y bebés (v3.5)
    // ------------------------------------------------------------------

    /** ¿Son familia? (pareja, papás e hijos, hermanos) */
    public boolean isRelatedTo(TownsfolkEntity o) {
        UUID me = getUuid(), them = o.getUuid();
        if (me.equals(them) || them.equals(spouse) || them.equals(townMom) || them.equals(townDad)) return true;
        if (me.equals(o.townMom) || me.equals(o.townDad)) return true;
        return (townMom != null && townMom.equals(o.townMom)) || (townDad != null && townDad.equals(o.townDad));
    }

    private boolean single() {
        return spouse == null && fiance == null && !isTownKid();
    }

    @Nullable
    private TownsfolkEntity byUuid(ServerWorld world, @Nullable UUID id) {
        return id != null && world.getEntity(id) instanceof TownsfolkEntity t && t.isAlive() ? t : null;
    }

    private void tickLife(ServerWorld world) {
        com.jaciel.kiko.config.KikoConfig cfg = com.jaciel.kiko.config.KikoConfig.get();
        long day = TownCalendar.day(world);
        long tod = world.getTimeOfDay() % 24000L;
        Random r = this.getRandom();
        // 1) Solteros: cada mañana hay chance de hacerse novios
        if (cfg.townWeddings && single() && day != lastLifeDay && tod >= 1000 && tod < 4000) {
            lastLifeDay = day;
            if (r.nextInt(4) == 0) {
                for (TownsfolkEntity o : world.getEntitiesByClass(TownsfolkEntity.class, this.getBoundingBox().expand(64),
                        o -> o != this && o.townInit && o.single() && o.isMale() != isMale() && o.isElder() == isElder() && !isRelatedTo(o))) {
                    this.fiance = o.getUuid();
                    o.fiance = this.getUuid();
                    this.weddingDay = o.weddingDay = day + 2;
                    this.lastLifeDay = o.lastLifeDay = day;
                    this.spawnParticles(net.minecraft.particle.ParticleTypes.HEART, 8);
                    o.spawnParticles(net.minecraft.particle.ParticleTypes.HEART, 8);
                    announce(world, Text.translatable("message.kiko.town_engaged", getDisplayName(), o.getDisplayName()), Formatting.LIGHT_PURPLE);
                    break;
                }
            }
        }
        // 2) Novios: corazoncitos cuando andan juntos; el día de la boda (al mediodía) ella arma todo
        if (fiance != null) {
            TownsfolkEntity o = byUuid(world, fiance);
            if (o != null && o.squaredDistanceTo(this) < 6 * 6 && r.nextInt(8) == 0) this.spawnParticles(net.minecraft.particle.ParticleTypes.HEART, 1);
            if (o != null && !isMale() && weddingStage == 0 && day >= weddingDay && tod >= 5000 && tod < 11000) startWedding(world, o);
            // Si el otro ya no existe (se murió o lo quitaron) se cancela
            if (o == null && day > weddingDay + 3) {
                fiance = null;
                weddingDay = -1;
            }
        }
        // 3) Casados: a veces en la noche llega un bebé
        if (cfg.townBabies && spouse != null && !isMale() && !isElder() && this.isSleeping() && day != lastBabyTry) {
            lastBabyTry = day;
            if (kidsBorn < cfg.townMaxKids && day - lastBabyDay >= cfg.townBabyCooldownDays && r.nextInt(3) == 0) tryBaby(world, day);
        }
        // 4) Encargo vencido (3 días)
        if (hasRequest() && day - requestDay > 3) clearRequest();
    }

    private void announce(ServerWorld world, Text msg, Formatting color) {
        for (net.minecraft.server.network.ServerPlayerEntity p : world.getPlayers(p -> p.squaredDistanceTo(this) < 96 * 96)) {
            p.sendMessage(msg.copy().formatted(color), false);
        }
    }

    private void startWedding(ServerWorld world, TownsfolkEntity groom) {
        BlockPos from = getHome() != null ? getHome() : getBlockPos();
        weddingBell = TownMarket.findBell(world, from);
        if (weddingBell == null) weddingBell = getBlockPos();
        weddingStage = 1;
        weddingTicks = 0;
        // Los invitados se van a la plaza
        for (TownsfolkEntity t : world.getEntitiesByClass(TownsfolkEntity.class, this.getBoundingBox().expand(64), t -> !t.isSleeping())) {
            t.getNavigation().startMovingTo(weddingBell.getX() + 0.5, weddingBell.getY(), weddingBell.getZ() + 0.5, 0.9);
        }
        announce(world, Text.translatable("message.kiko.town_wedding_now", getDisplayName(), groom.getDisplayName()), Formatting.GOLD);
    }

    /** Lugar libre junto a la campana para los novios. */
    private BlockPos freeNear(ServerWorld world, BlockPos c, int skip) {
        int found = 0;
        for (BlockPos p : BlockPos.iterateOutwards(c, 3, 2, 3)) {
            if (p.getSquaredDistance(c) < 2) continue;
            if (world.getBlockState(p).getCollisionShape(world, p).isEmpty() && world.getBlockState(p.up()).getCollisionShape(world, p.up()).isEmpty()
                    && world.getBlockState(p.down()).isSideSolidFullSquare(world, p.down(), net.minecraft.util.math.Direction.UP)
                    && world.getFluidState(p).isEmpty()) {
                if (found++ == skip) return p.toImmutable();
            }
        }
        return c;
    }

    private void tickWedding(ServerWorld world) {
        TownsfolkEntity groom = byUuid(world, fiance);
        if (groom == null || weddingBell == null) {
            weddingStage = 0;
            return;
        }
        weddingTicks++;
        BlockPos bell = weddingBell;
        if (weddingStage == 1) {
            // Caminando a la plaza (si se tardan mucho llegan con una nubecita)
            boolean here = squaredDistanceTo(Vec3d.ofCenter(bell)) < 4 * 4 && groom.squaredDistanceTo(Vec3d.ofCenter(bell)) < 4 * 4;
            if (weddingTicks % 40 == 1) {
                this.getNavigation().startMovingTo(bell.getX() + 0.5, bell.getY(), bell.getZ() + 0.5, 1.0);
                groom.getNavigation().startMovingTo(bell.getX() + 0.5, bell.getY(), bell.getZ() + 0.5, 1.0);
            }
            if (here || weddingTicks > 700) {
                BlockPos a = freeNear(world, bell, 0), b = freeNear(world, bell, 1);
                for (TownsfolkEntity e : new TownsfolkEntity[]{this, groom}) {
                    world.spawnParticles(net.minecraft.particle.ParticleTypes.CLOUD, e.getX(), e.getBodyY(0.5), e.getZ(), 6, 0.3, 0.4, 0.3, 0.02);
                }
                this.refreshPositionAndAngles(a.getX() + 0.5, a.getY(), a.getZ() + 0.5, this.getYaw(), 0);
                groom.refreshPositionAndAngles(b.getX() + 0.5, b.getY(), b.getZ() + 0.5, groom.getYaw(), 0);
                weddingStage = 2;
                weddingTicks = 0;
            }
            return;
        }
        // Ceremonia (10 segundos)
        this.getNavigation().stop();
        groom.getNavigation().stop();
        this.getLookControl().lookAt(groom, 30, 30);
        groom.getLookControl().lookAt(this, 30, 30);
        if (weddingTicks == 1) {
            this.playAnimation(com.jaciel.kiko.animation.KikoAnimation.HUG);
            groom.playAnimation(com.jaciel.kiko.animation.KikoAnimation.HUG);
            this.setExpression(com.jaciel.kiko.animation.KikoExpression.EMBARRASSED, 200);
            groom.setExpression(com.jaciel.kiko.animation.KikoExpression.LAUGH, 200);
        }
        if (weddingTicks % 10 == 0) {
            world.spawnParticles(net.minecraft.particle.ParticleTypes.HEART, getX(), getBodyY(1.0), getZ(), 2, 0.6, 0.3, 0.6, 0);
            world.spawnParticles(net.minecraft.particle.ParticleTypes.CHERRY_LEAVES, getX(), getY() + 3, getZ(), 12, 2.5, 0.5, 2.5, 0);
        }
        if (weddingTicks == 40 || weddingTicks == 120) {
            for (TownsfolkEntity t : world.getEntitiesByClass(TownsfolkEntity.class, this.getBoundingBox().expand(24),
                    t -> t != this && t != groom && !t.isSleeping())) {
                t.playAnimation(t.isTownKid() ? com.jaciel.kiko.animation.KikoAnimation.CHEER : com.jaciel.kiko.animation.KikoAnimation.CLAP);
                t.setExpression(com.jaciel.kiko.animation.KikoExpression.LAUGH, 60);
            }
        }
        if (weddingTicks == 60) {
            com.jaciel.kiko.romance.Romance.immuneAround(world, this);
            com.jaciel.kiko.romance.Romance.rockets(world, this, 6);
        }
        if (weddingTicks == 100) {
            for (TownsfolkEntity t : world.getEntitiesByClass(TownsfolkEntity.class, this.getBoundingBox().expand(24), t -> !t.isSleeping())) {
                t.playAnimation(com.jaciel.kiko.animation.KikoAnimation.DANCE);
            }
        }
        if (weddingTicks >= 200) finishWedding(world, groom);
    }

    private void finishWedding(ServerWorld world, TownsfolkEntity groom) {
        this.spouse = groom.getUuid();
        groom.spouse = this.getUuid();
        this.fiance = groom.fiance = null;
        this.weddingDay = groom.weddingDay = -1;
        this.weddingStage = groom.weddingStage = 0;
        // Se mudan a una casa vacía del pueblo (si no hay, ella se va a la casa de él)
        BlockPos house = emptyHouse(world, weddingBell != null ? weddingBell : getBlockPos());
        if (house != null) {
            this.setHome(house);
            groom.setHome(house);
        } else if (groom.getHome() != null) {
            this.setHome(groom.getHome());
        }
        announce(world, Text.translatable("message.kiko.town_married", getDisplayName(), groom.getDisplayName()), Formatting.LIGHT_PURPLE);
    }

    /** Una cama normal del pueblo donde no viva nadie (a 5 bloques no hay casa de ningún vecino). */
    @Nullable
    private BlockPos emptyHouse(ServerWorld world, BlockPos center) {
        java.util.List<BlockPos> homes = new java.util.ArrayList<>();
        for (TownsfolkEntity t : world.getEntitiesByClass(TownsfolkEntity.class, new net.minecraft.util.math.Box(center).expand(96), t -> t.getHome() != null)) {
            homes.add(t.getHome());
        }
        BlockPos best = null;
        double bestD = Double.MAX_VALUE;
        for (BlockPos p : BlockPos.iterateOutwards(center, 56, 8, 56)) {
            net.minecraft.block.BlockState s = world.getBlockState(p);
            if (!(s.getBlock() instanceof net.minecraft.block.BedBlock) || s.getBlock() instanceof com.jaciel.kiko.block.KikoBedBlock) continue;
            if (s.get(net.minecraft.block.BedBlock.PART) != net.minecraft.block.enums.BedPart.HEAD) continue;
            boolean taken = false;
            for (BlockPos h : homes) {
                if (h.isWithinDistance(p, 6)) {
                    taken = true;
                    break;
                }
            }
            if (taken) continue;
            double d = p.getSquaredDistance(center);
            if (d < bestD) {
                bestD = d;
                best = p.toImmutable();
            }
        }
        return best;
    }

    private void tryBaby(ServerWorld world, long day) {
        com.jaciel.kiko.config.KikoConfig cfg = com.jaciel.kiko.config.KikoConfig.get();
        TownsfolkEntity dad = byUuid(world, spouse);
        if (dad == null || dad.squaredDistanceTo(this) > 16 * 16) return;
        int pop = world.getEntitiesByClass(TownsfolkEntity.class, this.getBoundingBox().expand(96), TownsfolkEntity::isAlive).size();
        if (pop >= cfg.townPopulationCap) return;
        BlockPos home = getHome() != null ? getHome() : getBlockPos();
        BlockPos crib = null;
        for (BlockPos p : BlockPos.iterateOutwards(home, 10, 3, 10)) {
            if (KikoChildEntity.cribFree(world, p)) {
                crib = p.toImmutable();
                break;
            }
        }
        if (crib == null) return;
        TownsfolkEntity baby = com.jaciel.kiko.registry.ModEntities.TOWNSFOLK.create(world);
        if (baby == null) return;
        baby.refreshPositionAndAngles(crib.getX() + 0.5, crib.getY() + 0.53, crib.getZ() + 0.5, 0, 0);
        baby.setupBaby(world, this, dad);
        world.spawnEntity(baby);
        kidsBorn++;
        dad.kidsBorn++;
        lastBabyDay = dad.lastBabyDay = day;
        world.spawnParticles(net.minecraft.particle.ParticleTypes.HEART, crib.getX() + 0.5, crib.getY() + 1, crib.getZ() + 0.5, 10, 0.4, 0.3, 0.4, 0);
        announce(world, Text.translatable("message.kiko.town_baby", getDisplayName(), dad.getDisplayName(), baby.getDisplayName()), Formatting.LIGHT_PURPLE);
    }

    /** Arma al bebé recién nacido (se parece a sus papás). */
    void setupBaby(ServerWorld world, TownsfolkEntity mom, TownsfolkEntity dad) {
        Random r = this.getRandom();
        townInit = true;
        townRole = "kid";
        townMom = mom.getUuid();
        townDad = dad.getUuid();
        boolean male = r.nextBoolean();
        this.dataTracker.set(MODEL, BABY_MODEL);
        int[] a = dad.colors(), b = mom.colors();
        int skin = switch (r.nextInt(3)) {
            case 0 -> a[0];
            case 1 -> b[0];
            default -> Traits.mix(a[0], b[0]);
        };
        int hair = r.nextBoolean() ? a[1] : b[1];
        // Si sus papás ya tienen canas, el bebé no
        for (int g : GRAYS) if (hair == g) hair = Traits.HAIRS[NATURAL[r.nextInt(NATURAL.length)]];
        int eyes = r.nextBoolean() ? a[2] : b[2];
        setLooks(male, new int[]{skin, hair, eyes, Traits.CLOTHES[r.nextInt(Traits.CLOTHES.length)],
                Traits.CLOTHES[r.nextInt(Traits.CLOTHES.length)], Traits.SHOES[r.nextInt(Traits.SHOES.length)]});
        setFixedStage(BABY);
        townBirth = world.getTimeOfDay();
        Set<String> used = new HashSet<>();
        for (TownsfolkEntity t : world.getEntitiesByClass(TownsfolkEntity.class, mom.getBoundingBox().expand(96), t -> t.getCustomName() != null)) {
            used.add(t.getCustomName().getString());
        }
        String[] pool = male ? NAMES_M : NAMES_F;
        String name = pool[r.nextInt(pool.length)];
        for (int i = 0; i < 16 && used.contains(name); i++) name = pool[r.nextInt(pool.length)];
        this.setCustomName(Text.literal(name));
        this.setCustomNameVisible(false);
        if (mom.getHome() != null) this.setHome(mom.getHome());
        this.setPersistent();
    }

    // ------------------------------------------------------------------
    // Encargo (v3.5, ver TownRequests)
    // ------------------------------------------------------------------

    public boolean hasRequest() {
        return !requestItem.isEmpty() && requestCount > 0;
    }

    public String getRequestItem() {
        return requestItem;
    }

    public int getRequestCount() {
        return requestCount;
    }

    void setRequest(String item, int count, long day) {
        requestItem = item;
        requestCount = count;
        requestDay = day;
    }

    void clearRequest() {
        requestItem = "";
        requestCount = 0;
        requestDay = -1;
    }

    // ------------------------------------------------------------------
    // Guardado
    // ------------------------------------------------------------------

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("TownInit", townInit);
        nbt.putString("TownRole", townRole);
        nbt.putFloat("TownChance", townChance);
        nbt.putInt("TownModel", this.dataTracker.get(MODEL));
        nbt.putInt("HairStyle", getHairStyle());
        nbt.putInt("Job", this.dataTracker.get(JOB));
        if (spouse != null) nbt.putUuid("Spouse", spouse);
        nbt.putLong("TownBirth", townBirth);
        nbt.putInt("Body", getBodyType());
        nbt.putInt("GuardKills", guardKills);
        if (townMom != null) nbt.putUuid("TownMom", townMom);
        if (townDad != null) nbt.putUuid("TownDad", townDad);
        if (fiance != null) nbt.putUuid("Fiance", fiance);
        nbt.putLong("WeddingDay", weddingDay);
        nbt.putLong("LastLifeDay", lastLifeDay);
        nbt.putLong("LastBabyDay", lastBabyDay);
        nbt.putInt("KidsBorn", kidsBorn);
        nbt.putString("RequestItem", requestItem);
        nbt.putInt("RequestCount", requestCount);
        nbt.putLong("RequestDay", requestDay);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        townInit = nbt.getBoolean("TownInit");
        if (nbt.contains("TownRole")) townRole = nbt.getString("TownRole");
        townChance = nbt.contains("TownChance") ? nbt.getFloat("TownChance") : 1f;
        this.dataTracker.set(MODEL, nbt.getInt("TownModel"));
        this.dataTracker.set(HAIR_STYLE, nbt.getInt("HairStyle"));
        this.dataTracker.set(JOB, nbt.getInt("Job"));
        spouse = nbt.containsUuid("Spouse") ? nbt.getUuid("Spouse") : null;
        townBirth = nbt.contains("TownBirth") ? nbt.getLong("TownBirth") : -1;
        // Los vecinos de antes de la v3.4.3 agarran un cuerpo al azar
        townMom = nbt.containsUuid("TownMom") ? nbt.getUuid("TownMom") : null;
        townDad = nbt.containsUuid("TownDad") ? nbt.getUuid("TownDad") : null;
        fiance = nbt.containsUuid("Fiance") ? nbt.getUuid("Fiance") : null;
        weddingDay = nbt.contains("WeddingDay") ? nbt.getLong("WeddingDay") : -1;
        lastLifeDay = nbt.contains("LastLifeDay") ? nbt.getLong("LastLifeDay") : -1;
        lastBabyDay = nbt.contains("LastBabyDay") ? nbt.getLong("LastBabyDay") : -100;
        kidsBorn = nbt.getInt("KidsBorn");
        guardKills = nbt.getInt("GuardKills");
        requestItem = nbt.getString("RequestItem");
        requestCount = nbt.getInt("RequestCount");
        requestDay = nbt.contains("RequestDay") ? nbt.getLong("RequestDay") : -1;
        if (nbt.contains("Body")) this.dataTracker.set(BODY, nbt.getInt("Body"));
        else if (townInit && !isTownKid()) randomBody();
        // Recién salido de la estructura no trae etapa (sería bebé): mientras se arma ya va de su tamaño
        if (!nbt.contains("Stage")) setFixedStage(townRole.equals("kid") ? CHILD : ADULT);
    }
}
