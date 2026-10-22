package com.jaciel.kiko.client.screen;

import com.jaciel.kiko.inventory.KikoScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Inventario de Kiko. Usa el fondo del cofre de vanilla y le agrega un panel a la izquierda
 * con su armadura (casco, pechera, pantalones, botas) y sus dos manos.
 */
public class KikoInventoryScreen extends HandledScreen<KikoScreenHandler> {
    private static final Identifier CHEST = Identifier.ofVanilla("textures/gui/container/generic_54.png");
    private static final Identifier SLOT = Identifier.ofVanilla("container/slot");
    private static final int ROWS = 3;

    public KikoInventoryScreen(KikoScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 114 + ROWS * 18;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = this.x;
        int y = this.y;
        // Cofre de 3 filas + inventario del jugador
        context.drawTexture(CHEST, x, y, 0, 0, this.backgroundWidth, ROWS * 18 + 17);
        context.drawTexture(CHEST, x, y + ROWS * 18 + 17, 0, 126, this.backgroundWidth, 96);

        // Panel de armadura y manos
        int px = x + KikoScreenHandler.HAND_X - 5;
        int top = y + KikoScreenHandler.ARMOR_Y - 5;
        int bottom = y + KikoScreenHandler.HAND_Y + 36 + 5;
        context.fill(px, top, px + 28, bottom, 0xFF373737);
        context.fill(px + 1, top + 1, px + 27, bottom - 1, 0xFFC6C6C6);
        for (int i = 0; i < 4; i++) {
            context.drawGuiTexture(SLOT, x + KikoScreenHandler.HAND_X - 1, y + KikoScreenHandler.ARMOR_Y + i * 18 - 1, 18, 18);
        }
        // Rayita que separa la armadura de las manos
        context.fill(px + 4, y + KikoScreenHandler.HAND_Y - 5, px + 24, y + KikoScreenHandler.HAND_Y - 4, 0xFF8B8B8B);
        context.drawGuiTexture(SLOT, x + KikoScreenHandler.HAND_X - 1, y + KikoScreenHandler.HAND_Y - 1, 18, 18);
        context.drawGuiTexture(SLOT, x + KikoScreenHandler.HAND_X - 1, y + KikoScreenHandler.HAND_Y + 17, 18, 18);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
