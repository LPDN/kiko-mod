package com.jaciel.kiko.town;

import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.config.KikoConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

import java.util.List;

/**
 * Encargos de los vecinos (v3.5).
 *  - A veces cuando platicas con un vecino te pide algo según su oficio ("¿me traes 16 de trigo?")
 *  - Se lo das con clic derecho (todo junto) y te paga con esmeraldas, te quiere más y a veces te regala algo de su chamba
 *  - Duran 3 días. En el pueblo solo hay unos cuantos a la vez (config townMaxRequests)
 *  - El tablero de encargos de la plaza los enseña todos
 */
public final class TownRequests {
    private TownRequests() {}

    private record Ask(Item item, int min, int max) {}

    private static List<Ask> asksFor(TownsfolkEntity npc) {
        if (npc.isTownKid()) {
            return List.of(new Ask(Items.COOKIE, 3, 6), new Ask(Items.APPLE, 2, 4), new Ask(Items.SWEET_BERRIES, 6, 10), new Ask(Items.SNOWBALL, 8, 16));
        }
        return switch (npc.getJobId()) {
            case "baker" -> List.of(new Ask(Items.WHEAT, 12, 24), new Ask(Items.EGG, 4, 8), new Ask(Items.SUGAR, 6, 12));
            case "farmer" -> List.of(new Ask(Items.BONE_MEAL, 8, 16), new Ask(Items.WHEAT_SEEDS, 16, 32), new Ask(Items.BUCKET, 1, 2));
            case "florist" -> List.of(new Ask(Items.BONE_MEAL, 8, 16), new Ask(Items.POPPY, 4, 8), new Ask(Items.BLUE_ORCHID, 2, 4));
            case "carpenter" -> List.of(new Ask(Items.OAK_LOG, 12, 24), new Ask(Items.IRON_INGOT, 2, 4), new Ask(Items.SPRUCE_LOG, 12, 24));
            case "woodcutter" -> List.of(new Ask(Items.IRON_INGOT, 2, 4), new Ask(Items.BREAD, 3, 6), new Ask(Items.FLINT, 3, 6));
            case "seamstress" -> List.of(new Ask(Items.STRING, 8, 16), new Ask(Items.WHITE_WOOL, 6, 12), new Ask(Items.LAPIS_LAZULI, 4, 8));
            case "cook" -> List.of(new Ask(Items.BEEF, 4, 8), new Ask(Items.POTATO, 8, 16), new Ask(Items.BROWN_MUSHROOM, 3, 6));
            case "shopkeeper" -> List.of(new Ask(Items.COAL, 10, 20), new Ask(Items.GOLD_INGOT, 2, 4), new Ask(Items.PAPER, 8, 16));
            case "mailman" -> List.of(new Ask(Items.PAPER, 12, 24), new Ask(Items.FEATHER, 6, 12), new Ask(Items.LEATHER, 3, 6));
            case "teacher" -> List.of(new Ask(Items.BOOK, 2, 4), new Ask(Items.PAPER, 10, 20), new Ask(Items.INK_SAC, 3, 6));
            case "doctor" -> List.of(new Ask(Items.HONEY_BOTTLE, 1, 3), new Ask(Items.GOLDEN_CARROT, 2, 4), new Ask(Items.GLISTERING_MELON_SLICE, 1, 2));
            case "gardener" -> List.of(new Ask(Items.BONE_MEAL, 10, 20), new Ask(Items.OAK_SAPLING, 4, 8), new Ask(Items.SUNFLOWER, 2, 4));
            case "grandpa", "grandma" -> List.of(new Ask(Items.MILK_BUCKET, 1, 1), new Ask(Items.BREAD, 3, 6), new Ask(Items.PUMPKIN_PIE, 1, 3),
                    new Ask(Items.COOKIE, 4, 8));
            default -> List.of(new Ask(Items.BREAD, 3, 6), new Ask(Items.APPLE, 3, 6));
        };
    }

    /** Lo que le das por cada encargo (según qué tanto pidió). */
    private static int reward(Item item, int count) {
        if (item == Items.IRON_INGOT || item == Items.GOLD_INGOT || item == Items.GOLDEN_CARROT
                || item == Items.GLISTERING_MELON_SLICE || item == Items.HONEY_BOTTLE || item == Items.MILK_BUCKET
                || item == Items.BUCKET || item == Items.BLUE_ORCHID || item == Items.BOOK) return 2 + count / 2;
        return 1 + count / 8;
    }

    /** Al platicar: a veces te pide algo (si no tiene encargo y en el pueblo no hay muchos). */
    public static void maybeAsk(TownsfolkEntity npc, PlayerEntity player) {
        if (!(npc.getWorld() instanceof ServerWorld world)) return;
        if (npc.getStage() == TownsfolkEntity.BABY) return;
        Random r = npc.getRandom();
        // Si ya te había pedido algo, te lo recuerda de vez en cuando
        if (npc.hasRequest()) {
            if (r.nextInt(2) == 0) {
                Identifier id = Identifier.tryParse(npc.getRequestItem());
                Item it = id != null ? Registries.ITEM.get(id) : Items.AIR;
                player.sendMessage(Text.translatable("message.kiko.town_request_remind", npc.getDisplayName(), npc.getRequestCount(),
                        Text.translatable(it.getTranslationKey())).formatted(Formatting.GRAY), true);
            }
            return;
        }
        if (r.nextInt(2) != 0) return;
        int active = world.getEntitiesByClass(TownsfolkEntity.class, npc.getBoundingBox().expand(96), TownsfolkEntity::hasRequest).size();
        if (active >= KikoConfig.get().townMaxRequests) return;
        List<Ask> asks = asksFor(npc);
        Ask a = asks.get(r.nextInt(asks.size()));
        int count = a.min() + (a.max() > a.min() ? r.nextInt(a.max() - a.min() + 1) : 0);
        npc.setRequest(Registries.ITEM.getId(a.item()).toString(), count, TownCalendar.day(world));
        npc.setExpression(KikoExpression.SMUG, 60);
        npc.playAnimation(KikoAnimation.THINK);
        player.sendMessage(Text.literal("<").formatted(Formatting.WHITE)
                .append(npc.getDisplayName().copy().formatted(Formatting.GOLD))
                .append(Text.literal("> ").formatted(Formatting.WHITE))
                .append(Text.translatable("message.kiko.town_request", count, Text.translatable(a.item().getTranslationKey()),
                        reward(a.item(), count))), false);
    }

    /** Si traes lo que pidió, se lo das. */
    public static boolean tryComplete(TownsfolkEntity npc, PlayerEntity player, ItemStack stack) {
        if (!(npc.getWorld() instanceof ServerWorld world) || !npc.hasRequest() || stack.isEmpty()) return false;
        Identifier want = Identifier.tryParse(npc.getRequestItem());
        if (want == null || !Registries.ITEM.getId(stack.getItem()).equals(want)) return false;
        int count = npc.getRequestCount();
        if (stack.getCount() < count && !player.isCreative()) {
            player.sendMessage(Text.translatable("message.kiko.town_request_short", count - stack.getCount()).formatted(Formatting.GRAY), true);
            return true;
        }
        stack.decrementUnlessCreative(count, player);
        int pay = reward(Registries.ITEM.get(want), count);
        player.giveItemStack(new ItemStack(Items.EMERALD, pay));
        npc.clearRequest();
        npc.getAffection().add(player.getUuid(), 8f, world.getTime());
        npc.playAnimation(KikoAnimation.CHEER);
        npc.setExpression(KikoExpression.LAUGH, 80);
        npc.spawnParticles(ParticleTypes.HAPPY_VILLAGER, 10);
        world.playSound(null, npc.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, net.minecraft.sound.SoundCategory.NEUTRAL, 0.6f, 1.3f);
        // A veces te regala algo de su oficio
        if (npc.getRandom().nextInt(3) == 0) {
            ItemStack bonus = switch (npc.getJobId()) {
                case "baker" -> new ItemStack(Items.CAKE);
                case "farmer", "gardener" -> new ItemStack(Items.GOLDEN_CARROT, 3);
                case "florist" -> new ItemStack(com.jaciel.kiko.registry.ModItems.BOUQUET);
                case "cook" -> new ItemStack(Items.RABBIT_STEW);
                case "doctor" -> new ItemStack(Items.GOLDEN_APPLE);
                case "teacher", "mailman" -> new ItemStack(Items.EXPERIENCE_BOTTLE, 3);
                case "grandpa", "grandma" -> new ItemStack(Items.COOKIE, 8);
                default -> new ItemStack(Items.EMERALD, 1);
            };
            player.giveItemStack(bonus);
        }
        player.sendMessage(Text.literal("<").formatted(Formatting.WHITE)
                .append(npc.getDisplayName().copy().formatted(Formatting.GOLD))
                .append(Text.literal("> ").formatted(Formatting.WHITE))
                .append(Text.translatable("message.kiko.town_request_done." + npc.getRandom().nextInt(3), pay)), false);
        return true;
    }

    /** Renglón para el tablero ("Ramón (panadero) quiere 16 de trigo, paga 3 esmeraldas"). */
    public static MutableText line(TownsfolkEntity npc) {
        Identifier id = Identifier.tryParse(npc.getRequestItem());
        Item item = id != null ? Registries.ITEM.get(id) : Items.AIR;
        MutableText who = npc.getDisplayName().copy().formatted(Formatting.GOLD);
        if (!npc.getJobId().equals("none")) {
            who.append(Text.literal(" (").formatted(Formatting.GRAY))
                    .append(Text.translatable("job.kiko." + npc.getJobId() + (npc.isMale() ? ".m" : ".f")).formatted(Formatting.GRAY))
                    .append(Text.literal(")").formatted(Formatting.GRAY));
        }
        return Text.literal("• ").append(who).append(Text.translatable("message.kiko.board_line", npc.getRequestCount(),
                Text.translatable(item.getTranslationKey()), reward(item, npc.getRequestCount())).formatted(Formatting.WHITE));
    }
}
