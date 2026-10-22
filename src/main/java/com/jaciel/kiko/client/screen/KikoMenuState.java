package com.jaciel.kiko.client.screen;

import com.jaciel.kiko.entity.combat.CombatMode;
import com.jaciel.kiko.entity.work.WorkMode;
import com.jaciel.kiko.network.KikoNetworking;
import com.jaciel.kiko.network.KikoNetworking.MenuActionPayload;
import com.jaciel.kiko.network.KikoNetworking.OpenMenuPayload;
import com.jaciel.kiko.outfit.KikoOutfit;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/** Lo que muestran los menús de Kiko. Se comparte entre el menú principal y los submenús. */
public class KikoMenuState {
    public final int entityId;
    public final float affection;
    public KikoOutfit outfit;
    public WorkMode work;
    public CombatMode combat;
    public boolean following;
    public boolean autoOutfit;
    public boolean hideArmor;
    public boolean tunnel3x3;
    public boolean hasHome;
    public boolean hasPet;
    /** v3.0: tu novia / tu esposa y la ropa de las chicas (0 trabajo, 1 casual, 2 boda). */
    public boolean girlfriend;
    public boolean wife;
    public int girlOutfit;
    public int chosenGirlOutfit;
    /** v3.1: es tu hijo o hija (y si todavía es menor). */
    public boolean kid;
    public boolean minor;
    public boolean myKid;
    public boolean kidMale;

    public KikoMenuState(OpenMenuPayload p) {
        this.entityId = p.entityId();
        this.affection = p.affection();
        this.outfit = KikoOutfit.byIndex(p.outfit());
        this.work = WorkMode.byIndex(p.work());
        this.combat = CombatMode.byIndex(p.combat());
        this.following = (p.flags() & KikoNetworking.FLAG_FOLLOWING) != 0;
        this.autoOutfit = (p.flags() & KikoNetworking.FLAG_AUTO_OUTFIT) != 0;
        this.hideArmor = (p.flags() & KikoNetworking.FLAG_HIDE_ARMOR) != 0;
        this.tunnel3x3 = (p.flags() & KikoNetworking.FLAG_TUNNEL_3X3) != 0;
        this.hasHome = (p.flags() & KikoNetworking.FLAG_HAS_HOME) != 0;
        this.hasPet = (p.flags() & KikoNetworking.FLAG_HAS_PET) != 0;
        this.girlfriend = (p.flags() & KikoNetworking.FLAG_GIRLFRIEND) != 0;
        this.wife = (p.flags() & KikoNetworking.FLAG_WIFE) != 0;
        this.girlOutfit = (p.flags() >> KikoNetworking.GIRL_OUTFIT_SHIFT) & 7;
        this.chosenGirlOutfit = (p.flags() >> KikoNetworking.CHOSEN_OUTFIT_SHIFT) & 7;
        this.kid = (p.flags() & KikoNetworking.FLAG_KID) != 0;
        this.minor = (p.flags() & KikoNetworking.FLAG_MINOR) != 0;
        this.myKid = (p.flags() & KikoNetworking.FLAG_MY_KID) != 0;
        this.kidMale = (p.flags() & KikoNetworking.FLAG_KID_MALE) != 0;
    }

    /** ¿Es Kiko? (las demás tienen su propia ropa) */
    public boolean isKiko() {
        if (kid) return false;
        var mc = net.minecraft.client.MinecraftClient.getInstance();
        return mc.world == null || !(mc.world.getEntityById(entityId) instanceof com.jaciel.kiko.entity.KikoEntity k)
                || k.getCharacter() == com.jaciel.kiko.entity.KikoCharacter.KIKO;
    }

    /** El nombre de quien abriste el menú (Kiko, Maru, Victoria...). */
    public net.minecraft.text.Text name() {
        var mc = net.minecraft.client.MinecraftClient.getInstance();
        if (mc.world != null && mc.world.getEntityById(entityId) instanceof com.jaciel.kiko.entity.KikoEntity k) return k.getDisplayName();
        return net.minecraft.text.Text.translatable("screen.kiko.title");
    }

    public static final String[] GIRL_OUTFIT_KEYS = {"outfit.kiko.girl_work", "outfit.kiko.girl_casual", "outfit.kiko.wedding",
            "outfit.kiko.girl_summer", "outfit.kiko.girl_winter"};

    /** v3.3.1 ¿es Beto (ya grande)? Tiene tiendita. */
    public boolean isTrader() {
        if (kid) return false;
        var mc = net.minecraft.client.MinecraftClient.getInstance();
        return mc.world != null && mc.world.getEntityById(entityId) instanceof com.jaciel.kiko.entity.KikoEntity k
                && k.getCharacter() == com.jaciel.kiko.entity.KikoCharacter.BETO;
    }

    /** v3.3 ¿es chico? (novio/esposo y "traje de boda") */
    public boolean isMale() {
        if (kid) return kidMale;
        var mc = net.minecraft.client.MinecraftClient.getInstance();
        return mc.world != null && mc.world.getEntityById(entityId) instanceof com.jaciel.kiko.entity.KikoEntity k && k.getCharacter().isMale();
    }

    /** Nombre de la ropa i (la de boda de los chicos es traje). */
    public String girlOutfitKey(int i) {
        String k = GIRL_OUTFIT_KEYS[Math.max(0, Math.min(4, i))];
        return i == 2 && isMale() ? k + ".m" : k;
    }

    /** Nombre de lo que trae puesto (para el botón del menú). */
    public String outfitKey() {
        if (kid) return "outfit.kiko.kid";
        return isKiko() ? outfit.translationKey() : girlOutfitKey(girlOutfit);
    }

    public void send(int action, int value) {
        ClientPlayNetworking.send(new MenuActionPayload(entityId, action, value));
    }
}
