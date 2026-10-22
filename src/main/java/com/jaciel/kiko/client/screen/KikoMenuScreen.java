package com.jaciel.kiko.client.screen;

import com.jaciel.kiko.affection.KikoAffectionSystem;
import com.jaciel.kiko.network.KikoNetworking;
import com.jaciel.kiko.network.KikoNetworking.OpenMenuPayload;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Menú principal de Kiko (Shift + clic derecho con la mano vacía).
 * Mientras está abierto, Kiko se queda quieta mirándote.
 *
 *  [ Ropa... ]        [ Inventario ]
 *  [ Trabajo... ]     [ Guardar en cofre ]
 *  [ Pelea » Defensiva                    ]
 *  [ Siguiéndote / Esperando aquí         ]
 *              [ Listo ]
 */
public class KikoMenuScreen extends Screen {
    private final KikoMenuState state;

    public KikoMenuScreen(OpenMenuPayload payload) {
        this(new KikoMenuState(payload));
    }

    public KikoMenuScreen(KikoMenuState state) {
        super(state.name());
        this.state = state;
    }

    private int top() {
        return this.height / 2 - 60;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int gap = 4, bh = 20, full = 220;
        int half = (full - gap) / 2;
        int left = cx - full / 2;
        int y = top();

        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.kiko.outfit_button")
                        .append(Text.literal(" » ").formatted(Formatting.GRAY))
                        .append(Text.translatable(state.outfitKey()).formatted(Formatting.YELLOW)),
                b -> client.setScreen(new KikoOutfitScreen(this, state))).dimensions(left, y, half, bh).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.kiko.inventory_button"),
                b -> state.send(KikoNetworking.ACTION_OPEN_INVENTORY, 0)).dimensions(left + half + gap, y, half, bh).build());

        y += bh + gap;
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.kiko.work_button")
                        .append(Text.literal(" » ").formatted(Formatting.GRAY))
                        .append(Text.translatable(state.work.translationKey()).formatted(Formatting.YELLOW)),
                b -> client.setScreen(new KikoWorkScreen(this, state))).dimensions(left, y, half, bh).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.kiko.deposit"),
                b -> state.send(KikoNetworking.ACTION_DEPOSIT, 0)).dimensions(left + half + gap, y, half, bh).build());

        y += bh + gap;
        Text combatLabel = Text.translatable("screen.kiko.combat",
                Text.translatable(state.combat.translationKey()).formatted(Formatting.YELLOW));
        addDrawableChild(ButtonWidget.builder(combatLabel, b -> {
            state.send(KikoNetworking.ACTION_CYCLE_COMBAT, 0);
            state.combat = state.combat.next();
            clearAndInit();
        }).dimensions(left, y, full, bh).build());

        y += bh + gap;
        Text followLabel = Text.translatable(state.following ? "screen.kiko.follow_on" : "screen.kiko.follow_off");
        addDrawableChild(ButtonWidget.builder(followLabel, b -> {
            state.send(KikoNetworking.ACTION_TOGGLE_FOLLOW, 0);
            state.following = !state.following;
            clearAndInit();
        }).dimensions(left, y, full, bh).build());

        // Fase 5: casa y mascota
        y += bh + gap;
        addDrawableChild(ButtonWidget.builder(Text.translatable(state.hasHome ? "screen.kiko.home_clear" : "screen.kiko.home_set"), b -> {
            state.send(KikoNetworking.ACTION_SET_HOME, 0);
            state.hasHome = !state.hasHome;
            clearAndInit();
        }).dimensions(left, y, state.hasPet ? half : full, bh).build());
        if (state.hasPet) {
            addDrawableChild(ButtonWidget.builder(Text.translatable("screen.kiko.pet_release"), b -> {
                state.send(KikoNetworking.ACTION_RELEASE_PET, 0);
                state.hasPet = false;
                clearAndInit();
            }).dimensions(left + half + gap, y, half, bh).build());
        }

        // v3.1: con tu esposa, formar una familia
        if (state.wife) {
            y += bh + gap;
            addDrawableChild(ButtonWidget.builder(Text.literal("♥ ").append(Text.translatable("screen.kiko.family")), b -> {
                state.send(KikoNetworking.ACTION_FAMILY, 0);
                close();
            }).dimensions(left, y, full, bh).build());
        }

        // v3.9: invitarla a salir (con cariño alto)
        if (!state.kid && state.affection >= com.jaciel.kiko.config.KikoConfig.get().dateAffection) {
            y += bh + gap;
            addDrawableChild(ButtonWidget.builder(Text.literal("♥ ").append(Text.translatable("screen.kiko.date")), b -> {
                state.send(KikoNetworking.ACTION_DATE, 0);
                close();
            }).dimensions(left, y, full, bh).build());
        }

        // v3.3.1: la tiendita de Beto
        if (state.isTrader()) {
            y += bh + gap;
            addDrawableChild(ButtonWidget.builder(Text.literal("$ ").append(Text.translatable("screen.kiko.trade")), b -> {
                state.send(KikoNetworking.ACTION_TRADE, 0);
            }).dimensions(left, y, full, bh).build());
        }

        y += bh + 10;
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), b -> close())
                .dimensions(cx - 50, y, 100, bh).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int cx = this.width / 2;
        int top = top();
        KikoAffectionSystem.Tier tier = KikoAffectionSystem.tierOf(state.affection);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, cx, top - 30, 0xFFD23C);
        Text hearts = Text.literal(KikoAffectionSystem.hearts(state.affection) + "  " + Math.round(state.affection) + "/100  ")
                .append(Text.translatable("tier.kiko." + tier.key()));
        if (state.myKid) hearts = Text.literal("✿ ").append(Text.translatable(state.kidMale ? "screen.kiko.your_son" : "screen.kiko.your_daughter"))
                .append("   ").append(hearts);
        else if (state.wife) hearts = Text.literal("♥♥ ").append(Text.translatable(state.isMale() ? "screen.kiko.your_wife.m" : "screen.kiko.your_wife")).append("   ").append(hearts);
        else if (state.girlfriend) hearts = Text.literal("❤ ").append(Text.translatable(state.isMale() ? "screen.kiko.your_girlfriend.m" : "screen.kiko.your_girlfriend")).append("   ").append(hearts);
        context.drawCenteredTextWithShadow(this.textRenderer, hearts, cx, top - 16, 0xFF7F9E);
    }

    /** Al cerrar el menú principal le avisa que ya puede moverse. */
    @Override
    public void close() {
        state.send(KikoNetworking.ACTION_MENU_CLOSED, 0);
        super.close();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
