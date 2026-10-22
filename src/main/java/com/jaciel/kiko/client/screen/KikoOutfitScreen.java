package com.jaciel.kiko.client.screen;

import com.jaciel.kiko.network.KikoNetworking;
import com.jaciel.kiko.outfit.KikoOutfit;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Submenú de ropa.
 *  - Todos los conjuntos (3 columnas)
 *  - Ropa automática (de trabajo y por lugar)
 *  - Mostrar o esconder la armadura
 */
public class KikoOutfitScreen extends Screen {
    private final Screen parent;
    private final KikoMenuState state;
    private int titleY;

    public KikoOutfitScreen(Screen parent, KikoMenuState state) {
        super(Text.translatable("screen.kiko.outfit_title"));
        this.parent = parent;
        this.state = state;
    }

    @Override
    protected void init() {
        // Kiko escoge entre sus 9 conjuntos (y el de novia si es tu esposa).
        // Las demás tienen trabajo, casual y novia (si es tu esposa)
        boolean isKiko = state.isKiko();
        java.util.List<Object[]> options = new java.util.ArrayList<>();   // {texto, acción, valor, escogido}
        if (isKiko) {
            for (KikoOutfit o : KikoOutfit.values()) {
                if (o == KikoOutfit.WEDDING && !state.wife) continue;
                options.add(new Object[]{o.translationKey(), KikoNetworking.ACTION_OUTFIT, o.ordinal(), o == state.outfit});
            }
        } else if (!state.kid) {
            for (int i = 0; i < 5; i++) {
                if (i == 2 && !state.wife) continue;   // la de boda solo si es tu esposa o esposo
                options.add(new Object[]{state.girlOutfitKey(i), KikoNetworking.ACTION_GIRL_OUTFIT, i, i == state.chosenGirlOutfit});
            }
        }
        int cols = 3, bw = 96, bh = 20, gap = 4;
        int rows = (options.size() + cols - 1) / cols;
        int totalW = cols * bw + (cols - 1) * gap;
        int left = this.width / 2 - totalW / 2;
        int top = this.height / 2 - (rows * (bh + gap) + 80) / 2;
        this.titleY = top - 16;

        for (int i = 0; i < options.size(); i++) {
            Object[] o = options.get(i);
            int x = left + (i % cols) * (bw + gap);
            int y = top + (i / cols) * (bh + gap);
            Text label = Text.translatable((String) o[0]);
            if ((Boolean) o[3]) label = Text.literal("▶ ").append(label).formatted(Formatting.YELLOW);
            int action = (Integer) o[1], value = (Integer) o[2];
            addDrawableChild(ButtonWidget.builder(label, b -> {
                state.send(action, value);
                if (action == KikoNetworking.ACTION_OUTFIT) state.outfit = KikoOutfit.byIndex(value);
                else {
                    state.girlOutfit = value;
                    state.chosenGirlOutfit = value;
                }
                clearAndInit();
            }).dimensions(x, y, bw, bh).build());
        }

        int y = top + rows * (bh + gap) + 8;
        if (isKiko || !state.kid) {
            Text auto = Text.translatable(state.autoOutfit ? "screen.kiko.auto_outfit_on" : "screen.kiko.auto_outfit_off");
            addDrawableChild(ButtonWidget.builder(auto, b -> {
                state.send(KikoNetworking.ACTION_TOGGLE_AUTO_OUTFIT, 0);
                state.autoOutfit = !state.autoOutfit;
                clearAndInit();
            }).dimensions(left, y, totalW, bh).build());
            y += bh + gap;
        }
        Text armor = Text.translatable(state.hideArmor ? "screen.kiko.armor_hidden" : "screen.kiko.armor_shown");
        addDrawableChild(ButtonWidget.builder(armor, b -> {
            state.send(KikoNetworking.ACTION_TOGGLE_HIDE_ARMOR, 0);
            state.hideArmor = !state.hideArmor;
            clearAndInit();
        }).dimensions(left, y, totalW, bh).build());

        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.back"), b -> close())
                .dimensions(this.width / 2 - 50, y + bh + 8, 100, bh).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, titleY, 0xFFD23C);
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
