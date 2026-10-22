package com.jaciel.kiko.interaction;

import com.jaciel.kiko.affection.KikoAffectionSystem;
import com.jaciel.kiko.affection.KikoAffectionSystem.Tier;
import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.item.KikoOutfitItem;
import com.jaciel.kiko.network.KikoNetworking;
import com.jaciel.kiko.registry.ModSounds;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Map;

/**
 * Todo lo que pasa cuando le das clic derecho a Kiko (solo servidor).
 *
 *  Shift + mano vacía   -> abre el menú (ropa, seguir / quedarse, ver afecto)
 *  Mano vacía           -> saludo (+2) o plática (+1, cooldown 30 s) o ver estado
 *  Conjunto de ropa     -> se lo pone
 *  Regalo de la lista   -> +15 aprox. según config
 *  Cualquier comida     -> +5 y se la come
 */
public final class KikoInteractionHandler {
    private KikoInteractionHandler() {}

    public static ActionResult handle(KikoEntity kiko, PlayerEntity player) {
        ItemStack stack = player.getMainHandStack();
        long now = kiko.getWorld().getTime();
        KikoConfig cfg = KikoConfig.get();
        KikoAffectionSystem.Record rec = kiko.getAffection().record(player.getUuid());
        // De expedición no está
        if (kiko.isAway()) {
            player.sendMessage(Text.translatable("message.kiko.away", Math.max(1, kiko.getAwayTicks() / 1200)).formatted(Formatting.GRAY), true);
            return ActionResult.CONSUME;
        }
        // Mascota: si traes un animal amarrado con correa, se lo das a Kiko
        if (!player.isSneaking()) {
            var leashed = kiko.getWorld().getEntitiesByClass(net.minecraft.entity.mob.MobEntity.class,
                    player.getBoundingBox().expand(10), m -> m != kiko && m.isLeashed() && m.getLeashHolder() == player);
            if (!leashed.isEmpty()) {
                net.minecraft.entity.mob.MobEntity pet = leashed.get(0);
                net.minecraft.entity.mob.MobEntity old = kiko.getPet();
                if (old != null && old != pet) old.detachLeash(true, true);
                kiko.setPet(pet);
                kiko.setExpression(KikoExpression.EMBARRASSED, 80);
                kiko.playAnimation(KikoAnimation.HAPPY);
                kiko.spawnParticles(ParticleTypes.HEART, 6);
                say(player, kiko, "dialogue.kiko.pet_new", 3);
                return ActionResult.CONSUME;
            }
            // Con una correa en la mano te regresa su mascota
            if (stack.isOf(net.minecraft.item.Items.LEAD) && kiko.hasPet()) {
                net.minecraft.entity.mob.MobEntity pet = kiko.getPet();
                kiko.setPet(null);
                if (pet != null) pet.attachLeash(player, true);
                kiko.setExpression(KikoExpression.SAD, 60);
                say(player, kiko, "dialogue.kiko.pet_return", 1);
                return ActionResult.CONSUME;
            }
        }
        // Dormida no la despiertas con clic
        if (kiko.isSleeping()) {
            player.sendMessage(Text.translatable(com.jaciel.kiko.romance.Romance.nk(kiko, "message.kiko.sleeping"), kiko.getDisplayName()).formatted(Formatting.GRAY), true);
            return ActionResult.CONSUME;
        }
        kiko.holdStill(player, 60); // se detiene a platicar contigo 3 segundos

        // 1) Menú
        if (stack.isEmpty() && player.isSneaking()) {
            if (player instanceof ServerPlayerEntity sp) KikoNetworking.openMenu(sp, kiko);
            return ActionResult.CONSUME;
        }

        // 1.5) Agachado con ingredientes crudos: te cocina (todas cocinan, Tami mejor)
        if (player.isSneaking() && !kiko.isMinor() && (com.jaciel.kiko.entity.Cooking.dishFor(stack) != null || com.jaciel.kiko.entity.DrinkPrep.isContainer(stack))) {
            if (KikoAffectionSystem.tierOf(rec.affection) == Tier.DISTANT) {
                kiko.setExpression(KikoExpression.POUT, 60);
                say(player, kiko, "dialogue.kiko.talk.distant", 3);
                return ActionResult.CONSUME;
            }
            if (com.jaciel.kiko.entity.DrinkPrep.isContainer(stack)) {
                if (com.jaciel.kiko.entity.DrinkPrep.tryStart(kiko, player, stack)) return ActionResult.CONSUME;
            } else if (com.jaciel.kiko.entity.Cooking.tryStart(kiko, player, stack)) return ActionResult.CONSUME;
        }

        // 2) Ropa
        if (stack.getItem() instanceof KikoOutfitItem outfitItem && kiko.getCharacter() == com.jaciel.kiko.entity.KikoCharacter.KIKO && !kiko.isKid()) {
            if (kiko.getOutfit() == outfitItem.getOutfit()) {
                say(player, kiko, "dialogue.kiko.outfit_same", 1);
                return ActionResult.CONSUME;
            }
            kiko.setChosenOutfit(outfitItem.getOutfit());
            kiko.playAnimation(KikoAnimation.HAPPY);
            kiko.spawnParticles(ParticleTypes.HAPPY_VILLAGER, 8);
            kiko.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER.value(), 1f, 1.1f);
            kiko.setExpression(KikoExpression.SMUG, 80);
            say(player, kiko, "dialogue.kiko.outfit", 3);
            return ActionResult.CONSUME;
        }

        // 2.4) v3.5 encargo de un vecino: si traes lo que pidió se lo das
        if (kiko instanceof com.jaciel.kiko.town.TownsfolkEntity t && com.jaciel.kiko.town.TownRequests.tryComplete(t, player, stack)) {
            return ActionResult.CONSUME;
        }

        // 2.5) Ramo y anillos (v3.0)
        if (com.jaciel.kiko.romance.Romance.handleItem(kiko, player, stack)) return ActionResult.CONSUME;

        // 3) Regalos y comida (con un mini cooldown para que no se gaste todo por doble clic)
        boolean bday = com.jaciel.kiko.town.Birthdays.isBirthday(kiko);
        float giftValue = giftValue(stack);
        // v3.9 el pastel es el regalo perfecto de cumpleaños
        if (bday && stack.isOf(net.minecraft.item.Items.CAKE)) giftValue = Math.max(giftValue, 20f);
        FoodComponent food = stack.get(DataComponentTypes.FOOD);
        if (giftValue > 0 || food != null) {
            if (now - rec.lastGift < cfg.giftCooldownSeconds * 20L) return ActionResult.CONSUME;
            rec.lastGift = now;

            float amount = giftValue > 0 ? giftValue : cfg.affectionFood;
            // v3.6 bebida favorita
            boolean favDrink = false;
            if (stack.getItem() instanceof com.jaciel.kiko.item.DrinkItem di) {
                com.jaciel.kiko.item.Drink fav = kiko instanceof com.jaciel.kiko.town.TownsfolkEntity tf
                        ? (tf.isTownKid() ? com.jaciel.kiko.item.Drink.CHOCOLATE : null)
                        : com.jaciel.kiko.item.Drink.favoriteOf(kiko.getCharacter());
                favDrink = fav == di.getDrink();
                if (favDrink) amount += 12f;
            }
            if (bday) amount *= 2f;
            float before = rec.affection;
            float after = kiko.getAffection().add(player.getUuid(), amount, now);
            stack.decrementUnlessCreative(1, player);
            if (giftValue > 0) com.jaciel.kiko.romance.Romance.onGift(kiko, player);

            if (bday) {
                com.jaciel.kiko.town.Birthdays.greeted(kiko, player);
                kiko.setExpression(KikoExpression.LOVESTRUCK, 120);
                say(player, kiko, "dialogue.kiko.birthday_gift", 3);
                kiko.playAnimation(KikoAnimation.GIFT);
                kiko.playSound(ModSounds.HAPPY, 1f, 1.1f);
                kiko.spawnParticles(ParticleTypes.HEART, 8);
                announceTierChange(player, kiko, before, after);
                showStatus(player, kiko);
                return ActionResult.CONSUME;
            } else if (favDrink) {
                kiko.setExpression(KikoExpression.EMBARRASSED, 100);
                say(player, kiko, "dialogue.kiko.drink_fav", 1);
            } else if (food != null && giftValue <= 0) {
                kiko.heal(Math.max(2f, food.nutrition()));
                kiko.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1f, 1.2f);
                kiko.setExpression(KikoExpression.SMILE, 80);
                say(player, kiko, "dialogue.kiko.food", 3);
            } else if (giftValue >= 15) {
                kiko.setExpression(KikoExpression.EMBARRASSED, 100);
                say(player, kiko, "dialogue.kiko.gift_love", 3);
            } else {
                kiko.setExpression(KikoExpression.GRIN, 80);
                say(player, kiko, "dialogue.kiko.gift", 3);
            }
            kiko.playAnimation(KikoAnimation.HAPPY);
            kiko.playSound(ModSounds.HAPPY, 1f, 1f);
            kiko.spawnParticles(ParticleTypes.HEART, giftValue >= 15 ? 6 : 3);
            announceTierChange(player, kiko, before, after);
            showStatus(player, kiko);
            return ActionResult.CONSUME;
        }

        // 4) Mano vacía: saludo, plática o estado
        if (stack.isEmpty()) {
            Tier tier = KikoAffectionSystem.tierOf(rec.affection);
            float before = rec.affection;
            // v3.9 hoy cumple: la primera vez que la saludas en el día te lo dice
            if (bday && rec.lastBirthdayHello != com.jaciel.kiko.town.TownCalendar.day(kiko.getWorld())) {
                rec.lastBirthdayHello = com.jaciel.kiko.town.TownCalendar.day(kiko.getWorld());
                rec.lastGreet = now;
                rec.lastTalk = now;
                com.jaciel.kiko.town.Birthdays.greeted(kiko, player);
                kiko.getAffection().add(player.getUuid(), cfg.affectionGreet + 3f, now);
                kiko.playAnimation(KikoAnimation.CHEER);
                kiko.playSound(ModSounds.HAPPY, 1f, 1.1f);
                kiko.setExpression(KikoExpression.LAUGH, 100);
                say(player, kiko, "dialogue.kiko.birthday_today", 3);
                showStatus(player, kiko);
                return ActionResult.CONSUME;
            }

            if (now - rec.lastGreet >= cfg.greetCooldownSeconds * 20L) {
                rec.lastGreet = now;
                rec.lastTalk = now;
                kiko.getAffection().add(player.getUuid(), cfg.affectionGreet, now);
                if (tier == Tier.DISTANT) {
                    kiko.playAnimation(KikoAnimation.REJECT);
                    kiko.playSound(ModSounds.SAD, 1f, 1f);
                    kiko.setExpression(KikoExpression.POUT, 80);
                    say(player, kiko, "dialogue.kiko.talk.distant", 3);
                } else {
                    kiko.playAnimation(KikoAnimation.WAVE);
                    kiko.playSound(ModSounds.GREET, 1f, 1f);
                    kiko.setExpression(tier == Tier.BESTIE ? KikoExpression.LAUGH : KikoExpression.SMILE, 70);
                    say(player, kiko, "dialogue.kiko.greet." + tier.key(), 3);
                }
            } else if (now - rec.lastTalk >= cfg.talkCooldownSeconds * 20L) {
                rec.lastTalk = now;
                kiko.getAffection().add(player.getUuid(), cfg.affectionTalk, now);
                if (tier == Tier.DISTANT) {
                    kiko.playAnimation(KikoAnimation.REJECT);
                    kiko.playSound(ModSounds.SAD, 1f, 1f);
                } else {
                    kiko.playAnimation(tier == Tier.BESTIE ? KikoAnimation.HAPPY : KikoAnimation.WAVE);
                    kiko.playSound(ModSounds.GREET, 1f, 1.1f);
                }
                kiko.setExpression(switch (tier) {
                    case DISTANT -> KikoExpression.POUT;
                    case NEUTRAL -> KikoExpression.NEUTRAL;
                    case FRIENDLY -> kiko.getRandom().nextBoolean() ? KikoExpression.SMILE : KikoExpression.SMUG;
                    case BESTIE -> kiko.getRandom().nextBoolean() ? KikoExpression.GRIN : KikoExpression.EMBARRASSED;
                }, 80);
                say(player, kiko, "dialogue.kiko.talk." + tier.key(), 4);
                if (tier == Tier.BESTIE) kiko.spawnParticles(ParticleTypes.HEART, 2);
            }
            announceTierChange(player, kiko, before, kiko.getAffection().get(player.getUuid()));
            showStatus(player, kiko);
            // v3.5 a veces el vecino te pide un encargo
            if (kiko instanceof com.jaciel.kiko.town.TownsfolkEntity t && tier != Tier.DISTANT) {
                com.jaciel.kiko.town.TownRequests.maybeAsk(t, player);
            }
            return ActionResult.CONSUME;
        }

        return ActionResult.PASS;
    }

    /** Valor de regalo del ítem según la config (0 si no es regalo). */
    public static float giftValue(ItemStack stack) {
        if (stack.isEmpty()) return 0f;
        Identifier itemId = Registries.ITEM.getId(stack.getItem());
        float best = 0f;
        for (Map.Entry<String, Float> e : KikoConfig.get().gifts.entrySet()) {
            String key = e.getKey();
            boolean match;
            if (key.startsWith("#")) {
                Identifier tagId = Identifier.tryParse(key.substring(1));
                match = tagId != null && stack.isIn(TagKey.of(RegistryKeys.ITEM, tagId));
            } else {
                match = key.equals(itemId.toString());
            }
            if (match && e.getValue() > best) best = e.getValue();
        }
        return best;
    }

    /** Muestra en la barra de acción el afecto actual y el outfit. */
    public static void showStatus(PlayerEntity player, KikoEntity kiko) {
        float a = kiko.getAffection().get(player.getUuid());
        Tier tier = KikoAffectionSystem.tierOf(a);
        Formatting color = switch (tier) {
            case DISTANT -> Formatting.AQUA;
            case NEUTRAL -> Formatting.GRAY;
            case FRIENDLY -> Formatting.LIGHT_PURPLE;
            case BESTIE -> Formatting.RED;
        };
        MutableText msg = Text.literal(KikoAffectionSystem.hearts(a) + " ").formatted(color)
                .append(Text.literal(Math.round(a) + "/100 ").formatted(Formatting.WHITE))
                .append(Text.translatable(com.jaciel.kiko.romance.Romance.nk(kiko, "tier.kiko." + tier.key())).formatted(color));
        player.sendMessage(msg, true);
    }

    private static void announceTierChange(PlayerEntity player, KikoEntity kiko, float before, float after) {
        Tier oldTier = KikoAffectionSystem.tierOf(before);
        Tier newTier = KikoAffectionSystem.tierOf(after);
        if (newTier.ordinal() > oldTier.ordinal()) {
            kiko.setExpression(KikoExpression.LAUGH, 80);
            say(player, kiko, "dialogue.kiko.levelup." + newTier.key(), 1);
            kiko.spawnParticles(ParticleTypes.HEART, 10);
        }
        float follow = KikoConfig.get().followThreshold;
        if (before < follow && after >= follow && !kiko.isTownsfolk()) {
            player.sendMessage(Text.translatable("message.kiko.will_follow").formatted(Formatting.GREEN), false);
        }
    }

    /** Manda una línea de diálogo al azar: <Kiko> texto */
    /**
     * v3.1: si eres mujer y el diálogo tiene versión en femenino (la misma llave con ".f"), usa esa.
     * Así "¿Otra vez solito?" se vuelve "¿Otra vez solita?" sin repetir todos los diálogos.
     */
    public static String gendered(PlayerEntity player, String key) {
        if (com.jaciel.kiko.family.Traits.isFemale(player) && net.minecraft.util.Language.getInstance().hasTranslation(key + ".f")) {
            return key + ".f";
        }
        return key;
    }

    public static void say(PlayerEntity player, KikoEntity kiko, String baseKey, int variants) {
        int i = kiko.getRandom().nextInt(variants);
        String key = kiko.dialogueKey(variants > 1 ? baseKey + "." + i : baseKey);
        key = gendered(player, key);
        kiko.talk(30 + kiko.getRandom().nextInt(20));
        player.sendMessage(Text.literal("<").formatted(Formatting.WHITE)
                .append(kiko.getDisplayName().copy().formatted(Formatting.GOLD))
                .append(Text.literal("> ").formatted(Formatting.WHITE))
                .append(Text.translatable(key)), false);
    }
}
