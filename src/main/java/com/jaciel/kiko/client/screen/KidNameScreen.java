package com.jaciel.kiko.client.screen;

import com.jaciel.kiko.network.KikoNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

/** ¡Nació tu bebé! Ponle nombre (v3.1). Si cierras sin escribir se queda con el que traía. */
public class KidNameScreen extends Screen {
    private final KikoNetworking.KidNamePayload payload;
    private TextFieldWidget field;

    public KidNameScreen(KikoNetworking.KidNamePayload payload) {
        super(Text.translatable(payload.male() ? "screen.kiko.kid_name_title.m" : "screen.kiko.kid_name_title.f"));
        this.payload = payload;
    }

    @Override
    protected void init() {
        int cx = this.width / 2, y = this.height / 2 - 10;
        field = new TextFieldWidget(textRenderer, cx - 100, y, 200, 20, Text.translatable("screen.kiko.kid_name"));
        field.setMaxLength(24);
        field.setText(payload.name());
        addDrawableChild(field);
        setInitialFocus(field);
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), b -> done()).dimensions(cx - 50, y + 30, 100, 20).build());
    }

    private void done() {
        String name = field.getText().strip();
        if (!name.isEmpty() && !name.equals(payload.name())) {
            ClientPlayNetworking.send(new KikoNetworking.KidNamePayload(payload.entityId(), payload.male(), name));
        }
        close();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 || keyCode == 335) {   // Enter
            done();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawCenteredTextWithShadow(textRenderer, title, this.width / 2, this.height / 2 - 40, 0xFF9EC8);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
