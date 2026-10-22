package com.jaciel.kiko.client.screen;

import com.jaciel.kiko.client.KikoClientState;
import com.jaciel.kiko.entity.work.WorkMode;
import com.jaciel.kiko.network.KikoNetworking;
import com.jaciel.kiko.network.KikoNetworking.KikoInfo;
import com.jaciel.kiko.network.KikoNetworking.MenuActionPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

/**
 * Menú rápido (tecla K). Lista de tus Kikos con su trabajo, distancia y vida.
 * Desde aquí abres el menú de cualquiera (aunque esté lejos), la llamas, o silbas a todas.
 */
public class KikoQuickScreen extends Screen {
    private static final int ROW = 24;
    private final List<KikoInfo> kikos;

    public KikoQuickScreen(List<KikoInfo> kikos) {
        super(Text.translatable("screen.kiko.quick_title"));
        this.kikos = kikos;
    }

    private int top() {
        int rows = Math.max(1, kikos.size());
        return Math.max(40, this.height / 2 - (rows * ROW + 40) / 2);
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y = top();
        int full = 300;
        int left = cx - full / 2;

        for (KikoInfo k : kikos) {
            boolean away = (k.flags() & KikoNetworking.INFO_AWAY) != 0;
            ButtonWidget menu = ButtonWidget.builder(Text.translatable("screen.kiko.quick_menu"), b -> {
                send(k.entityId(), KikoNetworking.ACTION_REMOTE_MENU);
            }).dimensions(left + full - 120, y, 58, 20).build();
            ButtonWidget call = ButtonWidget.builder(Text.translatable("screen.kiko.quick_call"), b -> {
                send(k.entityId(), KikoNetworking.ACTION_CALL);
                close();
            }).dimensions(left + full - 58, y, 58, 20).build();
            menu.active = !away;
            call.active = !away;
            addDrawableChild(menu);
            addDrawableChild(call);
            y += ROW;
        }

        y += 8;
        int half = (full - 4) / 2;
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.kiko.quick_call_all"), b -> {
            send(0, KikoNetworking.ACTION_CALL_ALL);
            close();
        }).dimensions(left, y, half, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable(KikoClientState.marker ? "screen.kiko.marker_on" : "screen.kiko.marker_off"), b -> {
            KikoClientState.marker = !KikoClientState.marker;
            KikoClientState.save();
            clearAndInit();
        }).dimensions(left + half + 4, y, half, 20).build());
        y += 24;
        // v3.1: tus rasgos (hombre o mujer, piel, pelo y ojos)
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.kiko.my_traits"), b -> send(0, KikoNetworking.ACTION_TRAITS))
                .dimensions(left, y, full, 20).build());
        y += 26;
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), b -> close())
                .dimensions(cx - 50, y, 100, 20).build());
    }

    private void send(int entityId, int action) {
        ClientPlayNetworking.send(new MenuActionPayload(entityId, action, 0));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int cx = this.width / 2;
        int left = cx - 150;
        int y = top();
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, cx, y - 20, 0xFFD23C);
        if (kikos.isEmpty()) {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("screen.kiko.quick_empty"), cx, y + 6, 0xAAAAAA);
            return;
        }
        for (KikoInfo k : kikos) {
            MutableText line = Text.literal(k.name()).formatted(Formatting.GOLD);
            int f = k.flags();
            Text status;
            if ((f & KikoNetworking.INFO_AWAY) != 0) status = Text.translatable("screen.kiko.status_away");
            else if ((f & KikoNetworking.INFO_SLEEPING) != 0) status = Text.translatable("screen.kiko.status_sleeping");
            else status = Text.translatable(WorkMode.byIndex(k.work()).translationKey());
            line.append(Text.literal("  ").append(status).formatted(Formatting.YELLOW));
            context.drawTextWithShadow(this.textRenderer, line, left, y + 2, 0xFFFFFF);
            MutableText info = Text.literal(k.distance() + " m  ").formatted(Formatting.GRAY)
                    .append(Text.literal("♥ " + k.healthPct() + "%").formatted(k.healthPct() < 40 ? Formatting.RED : Formatting.GREEN));
            if ((f & KikoNetworking.INFO_PET) != 0) info.append(Text.literal("  ").append(Text.translatable("screen.kiko.tag_pet")).formatted(Formatting.AQUA));
            if ((f & KikoNetworking.INFO_HOME) != 0) info.append(Text.literal("  ").append(Text.translatable("screen.kiko.tag_home")).formatted(Formatting.AQUA));
            context.drawTextWithShadow(this.textRenderer, info, left, y + 12, 0xFFFFFF);
            y += ROW;
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
