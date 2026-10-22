package com.jaciel.kiko.entity;

import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.animation.KikoExpression;
import com.jaciel.kiko.config.KikoConfig;
import com.jaciel.kiko.registry.ModItems;
import com.jaciel.kiko.romance.Romance;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradedItem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * La tiendita de Beto (v3.3.1). En su menú sale "Comerciar" y se abre la misma pantalla de comercio que los aldeanos.
 *  - Cada día del juego cambia lo que vende y lo que compra (6 cosas que vende y 4 que te compra)
 *  - Si te quiere te hace descuento (amigos 15 %, mejores amigos 30 %, su pareja 40 %)
 *  - Cada trato le sube tantito el afecto
 */
public class BetoShop implements Merchant {
    /** Las ofertas de cada Beto (se renuevan cada día, con el mismo azar para ese día). */
    private static final Map<UUID, Object[]> TODAY = new ConcurrentHashMap<>();

    private final KikoEntity beto;
    @Nullable
    private PlayerEntity customer;
    private final TradeOfferList offers;

    private BetoShop(KikoEntity beto, TradeOfferList offers) {
        this.beto = beto;
        this.offers = offers;
    }

    /** Abre la tienda para este jugador. */
    public static void open(ServerPlayerEntity player, KikoEntity beto) {
        ServerWorld world = player.getServerWorld();
        long day = world.getTimeOfDay() / 24000L;
        Object[] cached = TODAY.get(beto.getUuid());
        TradeOfferList offers;
        if (cached != null && (Long) cached[0] == day) {
            offers = (TradeOfferList) cached[1];
        } else {
            offers = generate(world, beto, day);
            TODAY.put(beto.getUuid(), new Object[]{day, offers});
        }
        // Descuento según qué tanto te quiere (se recalcula cada vez que abres)
        float aff = beto.getAffection().get(player.getUuid());
        boolean partner = player.getUuid().equals(beto.getPartnerUuid()) && beto.getRelation() != Romance.NONE;
        float off = partner ? 0.40f : aff >= KikoConfig.get().specialThreshold ? 0.30f : aff >= KikoConfig.get().followThreshold ? 0.15f : 0f;
        for (TradeOffer o : offers) {
            o.clearSpecialPrice();
            int base = o.getOriginalFirstBuyItem().getCount();
            int cut = (int) Math.floor(base * off);
            if (cut > 0) o.setSpecialPrice(-Math.min(cut, base - 1));
        }
        BetoShop shop = new BetoShop(beto, offers);
        beto.holdStill(player, 2400);
        beto.playAnimation(KikoAnimation.WAVE);
        beto.setExpression(KikoExpression.GRIN, 60);
        shop.setCustomer(player);
        shop.sendOffers(player, beto.getDisplayName(), 0);
    }

    // ------------------------------------------------------------------
    // Lo que vende y lo que compra
    // ------------------------------------------------------------------

    private static TradeOffer sell(Item item, int count, int emeralds, int uses) {
        return new TradeOffer(new TradedItem(Items.EMERALD, emeralds), new ItemStack(item, count), uses, 0, 0.05f);
    }

    private static TradeOffer sell(ItemStack stack, int emeralds, int uses) {
        return new TradeOffer(new TradedItem(Items.EMERALD, emeralds), stack, uses, 0, 0.05f);
    }

    private static TradeOffer buy(Item item, int count, int emeralds, int uses) {
        return new TradeOffer(new TradedItem(item, count), new ItemStack(Items.EMERALD, emeralds), uses, 0, 0.05f);
    }

    private static TradeOfferList generate(ServerWorld world, KikoEntity beto, long day) {
        Random r = Random.create(beto.getUuid().getLeastSignificantBits() ^ (day * 0x9E3779B97F4A7C15L));
        List<TradeOffer> sells = new ArrayList<>(List.of(
                sell(Items.BREAD, 6, 1, 12),
                sell(Items.COOKED_BEEF, 4, 2, 10),
                sell(Items.GOLDEN_CARROT, 3, 3, 8),
                sell(ModItems.BOUQUET, 1, 3, 4),
                sell(ModItems.GIFT_BOX, 1, 2, 6),
                sell(ModItems.SOCCER_BALL, 1, 2, 4),
                sell(ModItems.CRIB, 1, 4, 2),
                sell(Items.NAME_TAG, 1, 5, 3),
                sell(Items.SADDLE, 1, 6, 2),
                sell(Items.LANTERN, 4, 2, 6),
                sell(Items.GOLDEN_APPLE, 1, 8, 3),
                sell(Items.DIAMOND, 1, 12, 3),
                sell(Items.SPYGLASS, 1, 4, 3),
                sell(ModItems.RING, 1, 24, 1),
                sell(new Item[]{Items.MUSIC_DISC_CAT, Items.MUSIC_DISC_BLOCKS, Items.MUSIC_DISC_CHIRP, Items.MUSIC_DISC_MALL,
                        Items.MUSIC_DISC_STAL, Items.MUSIC_DISC_WAIT}[r.nextInt(6)], 1, 6, 2),
                sell(EnchantmentHelper.enchant(r, new ItemStack(Items.BOOK), 15 + r.nextInt(11), world.getRegistryManager(), Optional.empty()), 12, 2)
        ));
        List<TradeOffer> buys = new ArrayList<>(List.of(
                buy(Items.WHEAT, 20, 1, 16),
                buy(Items.COAL, 16, 1, 16),
                buy(Items.IRON_INGOT, 4, 1, 12),
                buy(Items.LEATHER, 6, 1, 12),
                buy(Items.STRING, 12, 1, 12),
                buy(Items.ROTTEN_FLESH, 10, 1, 16),
                buy(Items.GOLD_INGOT, 2, 1, 12),
                buy(Items.PAPER, 18, 1, 12),
                buy(Items.PUMPKIN, 6, 1, 12),
                buy(Items.DIAMOND, 1, 6, 4)
        ));
        TradeOfferList list = new TradeOfferList();
        for (int i = 0; i < 6 && !sells.isEmpty(); i++) list.add(sells.remove(r.nextInt(sells.size())));
        for (int i = 0; i < 4 && !buys.isEmpty(); i++) list.add(buys.remove(r.nextInt(buys.size())));
        return list;
    }

    // ------------------------------------------------------------------
    // Merchant
    // ------------------------------------------------------------------

    @Override
    public void setCustomer(@Nullable PlayerEntity customer) {
        this.customer = customer;
        if (customer == null) beto.releaseHold();
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
        beto.swingHand(net.minecraft.util.Hand.MAIN_HAND);
        beto.setExpression(KikoExpression.GRIN, 40);
        if (customer != null && beto.getWorld() instanceof ServerWorld sw) {
            beto.getAffection().add(customer.getUuid(), 0.3f, sw.getTime());
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
        return beto.getWorld().isClient;
    }
}
