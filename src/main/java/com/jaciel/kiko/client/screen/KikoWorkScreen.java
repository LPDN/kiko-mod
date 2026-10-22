package com.jaciel.kiko.client.screen;

import com.jaciel.kiko.entity.work.WorkMode;
import com.jaciel.kiko.network.KikoNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Submenú de trabajos. Cada botón trae su descripción al pasar el mouse.
 * Abajo, la opción del tamaño del túnel.
 */
public class KikoWorkScreen extends Screen {
    private final Screen parent;
    private final KikoMenuState state;

    public KikoWorkScreen(Screen parent, KikoMenuState state) {
        super(Text.translatable("screen.kiko.work_title"));
        this.parent = parent;
        this.state = state;
    }

    @Override
    protected void init() {
        WorkMode[] modes = WorkMode.values();
        int cols = 2, bw = 130, bh = 20, gap = 4;
        int rows = (modes.length + cols - 1) / cols;
        int totalW = cols * bw + (cols - 1) * gap;
        int left = this.width / 2 - totalW / 2;
        int top = this.height / 2 - (rows * (bh + gap) + 56) / 2;

        for (int i = 0; i < modes.length; i++) {
            WorkMode mode = modes[i];
            int x = left + (i % cols) * (bw + gap);
            int y = top + (i / cols) * (bh + gap);
            Text label = Text.translatable(mode.translationKey());
            if (mode == state.work) label = Text.literal("▶ ").append(label).formatted(Formatting.YELLOW);
            addDrawableChild(ButtonWidget.builder(label, b -> {
                        state.send(KikoNetworking.ACTION_SET_WORK, mode.ordinal());
                        state.work = mode;
                        client.setScreen(parent);
                    })
                    .dimensions(x, y, bw, bh)
                    .tooltip(Tooltip.of(Text.translatable(mode.descriptionKey())))
                    .build());
        }

        int y = top + rows * (bh + gap) + 8;
        Text tunnel = Text.translatable("screen.kiko.tunnel_size",
                Text.literal(state.tunnel3x3 ? "3x3" : "1x2").formatted(Formatting.YELLOW));
        addDrawableChild(ButtonWidget.builder(tunnel, b -> {
            state.send(KikoNetworking.ACTION_TOGGLE_TUNNEL_SIZE, 0);
            state.tunnel3x3 = !state.tunnel3x3;
            clearAndInit();
        }).dimensions(left, y, totalW, bh).build());

        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.back"), b -> close())
                .dimensions(this.width / 2 - 50, y + bh + 8, 100, bh).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int rows = (WorkMode.values().length + 1) / 2;
        int top = this.height / 2 - (rows * 24 + 56) / 2;
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, top - 16, 0xFFD23C);
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
