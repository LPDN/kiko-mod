package com.jaciel.kiko.client.screen;

import com.jaciel.kiko.family.Traits;
import com.jaciel.kiko.network.KikoNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * Tus rasgos (v3.1). Solo son descriptivos
 *  - Hombre o mujer, cambia cómo te hablan las chicas y tus hijos
 *  - Piel, pelo y ojos, los heredan tus hijos
 */
public class TraitsScreen extends Screen {
    private boolean male;
    private int skin, hair, eyes;

    public TraitsScreen(KikoNetworking.TraitsPayload p) {
        super(Text.translatable("screen.kiko.traits_title"));
        this.male = p.male();
        this.skin = p.skin();
        this.hair = p.hair();
        this.eyes = p.eyes();
    }

    private int top() {
        return this.height / 2 - 70;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int w = 200, left = cx - w / 2 + 40, y = top();
        addDrawableChild(ButtonWidget.builder(Text.translatable(male ? "screen.kiko.trait_male" : "screen.kiko.trait_female"), b -> {
            male = !male;
            clearAndInit();
        }).dimensions(left, y, 120, 20).build());
        y += 28;
        y = row(y, left, () -> skin = Math.floorMod(skin - 1, Traits.SKINS.length), () -> skin = Math.floorMod(skin + 1, Traits.SKINS.length));
        y = row(y, left, () -> hair = Math.floorMod(hair - 1, Traits.HAIRS.length), () -> hair = Math.floorMod(hair + 1, Traits.HAIRS.length));
        y = row(y, left, () -> eyes = Math.floorMod(eyes - 1, Traits.EYES.length), () -> eyes = Math.floorMod(eyes + 1, Traits.EYES.length));
        y += 10;
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.kiko.traits_save"), b -> {
            ClientPlayNetworking.send(new KikoNetworking.SaveTraitsPayload(male, skin, hair, eyes));
            close();
        }).dimensions(cx - 102, y, 100, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.cancel"), b -> close()).dimensions(cx + 2, y, 100, 20).build());
    }

    private int row(int y, int left, Runnable prev, Runnable next) {
        addDrawableChild(ButtonWidget.builder(Text.literal("<"), b -> {
            prev.run();
            clearAndInit();
        }).dimensions(left, y, 20, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal(">"), b -> {
            next.run();
            clearAndInit();
        }).dimensions(left + 100, y, 20, 20).build());
        return y + 26;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        int cx = this.width / 2;
        int left = cx - 100 + 40, y = top();
        ctx.drawCenteredTextWithShadow(textRenderer, title, cx, y - 30, 0xFFD23C);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.translatable("screen.kiko.traits_hint"), cx, y - 16, 0xAAAAAA);
        ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.kiko.trait_gender"), left - 80, y + 6, 0xFFFFFF);
        String[] labels = {"screen.kiko.trait_skin", "screen.kiko.trait_hair", "screen.kiko.trait_eyes"};
        int[] colors = {Traits.SKINS[skin], Traits.HAIRS[hair], Traits.EYES[eyes]};
        for (int i = 0; i < 3; i++) {
            int ry = y + 28 + i * 26;
            ctx.drawTextWithShadow(textRenderer, Text.translatable(labels[i]), left - 80, ry + 6, 0xFFFFFF);
            // Muestra del color
            ctx.fill(left + 26, ry + 1, left + 94, ry + 19, 0xFF000000);
            ctx.fill(left + 27, ry + 2, left + 93, ry + 18, 0xFF000000 | colors[i]);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
