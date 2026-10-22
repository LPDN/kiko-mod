package com.jaciel.kiko.town;

import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.registry.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradedItem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * La tiendita de cada vecino con oficio (v3.5). Agachado con la mano vacía se abre la pantalla de comercio.
 *  - Cada oficio vende lo suyo (4 cosas) y te compra cosas de su chamba (2)
 *  - Los domingos (mercadito) está a precio normal, los demás días 50 % más caro
 *  - Si te quiere te hace descuento como Beto
 */
public class TownShop implements Merchant {
    private static final Map<UUID, Object[]> TODAY = new ConcurrentHashMap<>();

    private final TownsfolkEntity npc;
    @Nullable
    private PlayerEntity customer;
    private final TradeOfferList offers;

    private TownShop(TownsfolkEntity npc, TradeOfferList offers) {
        this.npc = npc;
        this.offers = offers;
    }

    /** ¿Este oficio tiene tiendita? */
    public static boolean sells(String job) {
        return !sellsFor(job).isEmpty();
    }

    public static void open(ServerPlayerEntity player, TownsfolkEntity npc) {
        ServerWorld world = player.getServerWorld();
        long day = TownCalendar.day(world);
        Object[] cached = TODAY.get(npc.getUuid());
        TradeOfferList offers;
        if (cached != null && (Long) cached[0] == day) {
            offers = (TradeOfferList) cached[1];
        } else {
            boolean fiesta = TownFestival.activeNear(world, npc.getBlockPos());
            offers = generate(npc, day, TownCalendar.isSunday(world) || fiesta, fiesta);
            TODAY.put(npc.getUuid(), new Object[]{day, offers});
        }
        float aff = npc.getAffection().get(player.getUuid());
        float off = aff >= KikoConfig.get().specialThreshold ? 0.30f : aff >= KikoConfig.get().followThreshold ? 0.15f : 0f;
        for (TradeOffer o : offers) {
            o.clearSpecialPrice();
            int base = o.getOriginalFirstBuyItem().getCount();
            int cut = (int) Math.floor(base * off);
            if (cut > 0) o.setSpecialPrice(-Math.min(cut, base - 1));
        }
        TownShop shop = new TownShop(npc, offers);
        npc.holdStill(player, 2400);
        npc.playAnimation(KikoAnimation.WAVE);
        npc.setExpression(KikoExpression.SMILE, 60);
        if (!TownCalendar.isSunday(world) && !TownFestival.activeNear(world, npc.getBlockPos())) {
            player.sendMessage(Text.translatable("message.kiko.shop_not_sunday", TownCalendar.daysToSunday(world)), true);
        }
        shop.setCustomer(player);
        shop.sendOffers(player, npc.getDisplayName(), 0);
    }

    // ------------------------------------------------------------------
    // Lo de cada oficio
    // ------------------------------------------------------------------

    private record Deal(Item item, int count, int emeralds, int uses) {}

    private static List<Deal> sellsFor(String job) {
        return switch (job) {
            case "baker" -> List.of(new Deal(Items.BREAD, 6, 1, 12), new Deal(Items.CAKE, 1, 2, 4), new Deal(Items.COOKIE, 8, 1, 12),
                    new Deal(Items.PUMPKIN_PIE, 3, 1, 8), new Deal(Items.SUGAR, 8, 1, 8),
                    new Deal(com.jaciel.kiko.item.Drink.CHAMPURRADO.item, 2, 1, 8), new Deal(com.jaciel.kiko.item.Drink.CHOCOLATE.item, 2, 1, 8));
            case "farmer" -> List.of(new Deal(Items.CARROT, 10, 1, 12), new Deal(Items.POTATO, 10, 1, 12), new Deal(Items.APPLE, 6, 1, 10),
                    new Deal(Items.EGG, 8, 1, 10), new Deal(Items.HAY_BLOCK, 2, 1, 8), new Deal(Items.MELON_SLICE, 12, 1, 10),
                    new Deal(com.jaciel.kiko.item.Drink.LICUADO_MANZANA.item, 2, 1, 8), new Deal(com.jaciel.kiko.item.Drink.AGUA_SANDIA.item, 2, 1, 8));
            case "florist" -> List.of(new Deal(Items.POPPY, 6, 1, 10), new Deal(Items.CORNFLOWER, 6, 1, 10), new Deal(Items.ALLIUM, 6, 1, 10),
                    new Deal(ModItems.BOUQUET, 1, 3, 4), new Deal(Items.FLOWER_POT, 3, 1, 8), new Deal(Items.SUNFLOWER, 3, 1, 8));
            case "carpenter" -> List.of(new Deal(Items.CHEST, 2, 1, 8), new Deal(Items.LADDER, 8, 1, 10), new Deal(Items.OAK_DOOR, 2, 1, 8),
                    new Deal(Items.CRAFTING_TABLE, 1, 1, 6), new Deal(Items.OAK_FENCE, 8, 1, 10), new Deal(ModItems.CRIB, 1, 3, 2));
            case "woodcutter" -> List.of(new Deal(Items.OAK_LOG, 12, 1, 12), new Deal(Items.SPRUCE_LOG, 12, 1, 12), new Deal(Items.BIRCH_LOG, 12, 1, 12),
                    new Deal(Items.CHARCOAL, 8, 1, 10), new Deal(Items.OAK_SAPLING, 4, 1, 8));
            case "seamstress" -> List.of(new Deal(Items.WHITE_WOOL, 8, 1, 10), new Deal(Items.RED_WOOL, 8, 1, 10), new Deal(Items.LIGHT_BLUE_WOOL, 8, 1, 10),
                    new Deal(Items.WHITE_BANNER, 1, 2, 4), new Deal(Items.PINK_CARPET, 12, 1, 8), new Deal(Items.LEATHER_CHESTPLATE, 1, 3, 3));
            case "cook" -> List.of(new Deal(Items.COOKED_BEEF, 4, 1, 10), new Deal(Items.COOKED_PORKCHOP, 4, 1, 10), new Deal(Items.RABBIT_STEW, 1, 1, 6),
                    new Deal(Items.MUSHROOM_STEW, 1, 1, 6), new Deal(Items.BAKED_POTATO, 8, 1, 10),
                    new Deal(com.jaciel.kiko.item.Drink.HORCHATA.item, 3, 1, 8), new Deal(com.jaciel.kiko.item.Drink.CAFE_DE_OLLA.item, 2, 1, 8));
            case "shopkeeper" -> List.of(new Deal(com.jaciel.kiko.item.Drink.KIKO_COLA.item, 3, 1, 8), new Deal(com.jaciel.kiko.item.Drink.CHUPACABRAS.item, 1, 3, 4), new Deal(Items.TORCH, 16, 1, 12), new Deal(Items.LANTERN, 3, 1, 8), new Deal(Items.NAME_TAG, 1, 4, 3),
                    new Deal(Items.LEAD, 2, 1, 6), new Deal(ModItems.SOCCER_BALL, 1, 2, 4), new Deal(Items.CANDLE, 4, 1, 8));
            case "mailman" -> List.of(new Deal(Items.PAPER, 16, 1, 12), new Deal(Items.BOOK, 2, 1, 8), new Deal(Items.MAP, 1, 2, 4),
                    new Deal(Items.COMPASS, 1, 3, 3), new Deal(ModItems.GIFT_BOX, 1, 2, 4));
            case "teacher" -> List.of(new Deal(Items.BOOK, 3, 1, 8), new Deal(Items.WRITABLE_BOOK, 1, 1, 6), new Deal(Items.BOOKSHELF, 1, 2, 4),
                    new Deal(Items.INK_SAC, 6, 1, 8), new Deal(Items.EXPERIENCE_BOTTLE, 3, 2, 4));
            case "doctor" -> List.of(new Deal(Items.GOLDEN_APPLE, 1, 6, 2), new Deal(Items.HONEY_BOTTLE, 2, 1, 8), new Deal(Items.GLISTERING_MELON_SLICE, 1, 2, 4),
                    new Deal(Items.MILK_BUCKET, 1, 1, 6), new Deal(Items.GOLDEN_CARROT, 3, 2, 6));
            case "gardener" -> List.of(new Deal(Items.BONE_MEAL, 12, 1, 10), new Deal(Items.SWEET_BERRIES, 10, 1, 10), new Deal(Items.CHERRY_SAPLING, 2, 1, 6),
                    new Deal(Items.MOSS_BLOCK, 6, 1, 8), new Deal(Items.AZALEA, 2, 1, 6), new Deal(Items.PUMPKIN_SEEDS, 8, 1, 8));
            default -> List.of();
        };
    }

    private static List<Deal> buysFor(String job) {
        return switch (job) {
            case "baker" -> List.of(new Deal(Items.WHEAT, 20, 1, 16), new Deal(Items.EGG, 10, 1, 12), new Deal(Items.SUGAR_CANE, 16, 1, 12));
            case "farmer" -> List.of(new Deal(Items.BONE, 12, 1, 12), new Deal(Items.WHEAT_SEEDS, 24, 1, 12), new Deal(Items.LEATHER, 6, 1, 10));
            case "florist" -> List.of(new Deal(Items.BONE_MEAL, 16, 1, 12), new Deal(Items.DANDELION, 12, 1, 12));
            case "carpenter" -> List.of(new Deal(Items.OAK_LOG, 12, 1, 12), new Deal(Items.IRON_INGOT, 3, 1, 10), new Deal(Items.STICK, 32, 1, 12));
            case "woodcutter" -> List.of(new Deal(Items.STICK, 32, 1, 12), new Deal(Items.IRON_INGOT, 3, 1, 10));
            case "seamstress" -> List.of(new Deal(Items.STRING, 12, 1, 12), new Deal(Items.WHITE_WOOL, 10, 1, 12), new Deal(Items.LEATHER, 6, 1, 10));
            case "cook" -> List.of(new Deal(Items.BEEF, 8, 1, 12), new Deal(Items.PORKCHOP, 8, 1, 12), new Deal(Items.RABBIT, 4, 1, 10));
            case "shopkeeper" -> List.of(new Deal(Items.COAL, 12, 1, 12), new Deal(Items.GOLD_INGOT, 2, 1, 10));
            case "mailman" -> List.of(new Deal(Items.FEATHER, 12, 1, 12), new Deal(Items.LEATHER, 6, 1, 10));
            case "teacher" -> List.of(new Deal(Items.PAPER, 20, 1, 12), new Deal(Items.FEATHER, 10, 1, 12));
            case "doctor" -> List.of(new Deal(Items.SPIDER_EYE, 6, 1, 10), new Deal(Items.GLOWSTONE_DUST, 8, 1, 10));
            case "gardener" -> List.of(new Deal(Items.WHEAT_SEEDS, 24, 1, 12), new Deal(Items.OAK_SAPLING, 8, 1, 12));
            default -> List.of();
        };
    }

    /** v3.9 lo de la feria (cualquier puesto vende un par de estas ese día). */
    private static final List<Deal> FIESTA = List.of(new Deal(Items.CAKE, 1, 1, 4), new Deal(Items.COOKIE, 12, 1, 8),
            new Deal(Items.PUMPKIN_PIE, 4, 1, 8), new Deal(Items.HONEY_BOTTLE, 2, 1, 6), new Deal(Items.FIREWORK_ROCKET, 6, 1, 6),
            new Deal(com.jaciel.kiko.item.Drink.HORCHATA.item, 3, 1, 8), new Deal(com.jaciel.kiko.item.Drink.CHAMPURRADO.item, 3, 1, 8));

    private static TradeOfferList generate(TownsfolkEntity npc, long day, boolean sunday, boolean fiesta) {
        Random r = Random.create(npc.getUuid().getMostSignificantBits() ^ (day * 0x9E3779B97F4A7C15L));
        String job = npc.getJobId();
        List<Deal> sells = new ArrayList<>(sellsFor(job));
        List<Deal> buys = new ArrayList<>(buysFor(job));
        TradeOfferList list = new TradeOfferList();
        for (int i = 0; i < 4 && !sells.isEmpty(); i++) {
            Deal d = sells.remove(r.nextInt(sells.size()));
            int price = sunday ? d.emeralds() : (int) Math.ceil(d.emeralds() * 1.5);
            list.add(new TradeOffer(new TradedItem(Items.EMERALD, price), new ItemStack(d.item(), d.count()), d.uses(), 0, 0.05f));
        }
        if (fiesta) {
            List<Deal> extra = new ArrayList<>(FIESTA);
            for (int i = 0; i < 2 && !extra.isEmpty(); i++) {
                Deal d = extra.remove(r.nextInt(extra.size()));
                list.add(new TradeOffer(new TradedItem(Items.EMERALD, d.emeralds()), new ItemStack(d.item(), d.count()), d.uses(), 0, 0.05f));
            }
        }
        for (int i = 0; i < 2 && !buys.isEmpty(); i++) {
            Deal d = buys.remove(r.nextInt(buys.size()));
            list.add(new TradeOffer(new TradedItem(d.item(), d.count()), new ItemStack(Items.EMERALD, d.emeralds()), d.uses(), 0, 0.05f));
        }
        return list;
    }

    // ------------------------------------------------------------------
    // Merchant
    // ------------------------------------------------------------------

    @Override
    public void setCustomer(@Nullable PlayerEntity customer) {
        this.customer = customer;
        if (customer == null) npc.releaseHold();
    }

    @Nullable
    @Override
    public PlayerEntity getCustomer() {
        return customer;
    }

    @Override
    public TradeOfferList getOffers() {
        return offers;
    }

    @Override
    public void setOffersFromServer(TradeOfferList offers) {}

    @Override
    public void trade(TradeOffer offer) {
        offer.use();
        npc.swingHand(net.minecraft.util.Hand.MAIN_HAND);
        npc.setExpression(KikoExpression.GRIN, 40);
        if (customer != null && npc.getWorld() instanceof ServerWorld sw) {
            npc.getAffection().add(customer.getUuid(), 0.3f, sw.getTime());
        }
    }

    @Override
    public void onSellingItem(ItemStack stack) {}

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public void setExperienceFromServer(int experience) {}

    @Override
    public boolean isLeveledMerchant() {
        return false;
    }

    @Override
    public SoundEvent getYesSound() {
        return SoundEvents.ENTITY_VILLAGER_YES;
    }

    @Override
    public boolean canRefreshTrades() {
        return false;
    }

    @Override
    public boolean isClient() {
        return npc.getWorld().isClient;
    }
}
