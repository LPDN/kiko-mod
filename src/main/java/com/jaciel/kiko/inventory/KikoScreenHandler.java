package com.jaciel.kiko.inventory;

import com.jaciel.kiko.registry.ModScreens;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.screen.slot.Slot;

/**
 * Pantalla del inventario de Kiko (funciona como un cofre).
 *
 * Distribución
 *  - Panel a la izquierda con armadura (4) y manos (2)
 *  - 27 espacios de mochila (3 filas como un cofre)
 *  - inventario del jugador abajo
 */
public class KikoScreenHandler extends ScreenHandler {
    public static final int HAND_X = -21;
    public static final int ARMOR_Y = 8;
    public static final int HAND_Y = ARMOR_Y + 4 * 18 + 8;
    /** Íconos vacíos indexados por getEntitySlotId (botas 0, pantalones 1, pechera 2, casco 3). */
    private static final Identifier[] EMPTY_ARMOR = {
            PlayerScreenHandler.EMPTY_BOOTS_SLOT_TEXTURE, PlayerScreenHandler.EMPTY_LEGGINGS_SLOT_TEXTURE,
            PlayerScreenHandler.EMPTY_CHESTPLATE_SLOT_TEXTURE, PlayerScreenHandler.EMPTY_HELMET_SLOT_TEXTURE};
    private final Inventory inventory;
    private final int entityId;

    /** Cliente. Inventario espejo vacío, el servidor lo llena al sincronizar. */
    public KikoScreenHandler(int syncId, PlayerInventory playerInv, Integer entityId) {
        this(syncId, playerInv, new SimpleInventory(KikoInventory.SIZE), entityId);
    }

    /** Servidor. Usa el inventario real de Kiko. */
    public KikoScreenHandler(int syncId, PlayerInventory playerInv, Inventory inventory, int entityId) {
        super(ModScreens.KIKO_INVENTORY, syncId);
        checkSize(inventory, KikoInventory.SIZE);
        this.inventory = inventory;
        this.entityId = entityId;
        inventory.onOpen(playerInv.player);

        PlayerEntity player = playerInv.player;

        // Manos (los índices 0 y 1 del inventario de Kiko)
        addSlot(new Slot(inventory, KikoInventory.MAIN_HAND, HAND_X, HAND_Y));
        addSlot(new Slot(inventory, KikoInventory.OFF_HAND, HAND_X, HAND_Y + 18) {
            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandler.EMPTY_OFFHAND_ARMOR_SLOT);
            }
        });

        // Armadura
        for (int i = 0; i < 4; i++) {
            final EquipmentSlot equip = KikoInventory.ARMOR_SLOTS[i];
            addSlot(new Slot(inventory, KikoInventory.ARMOR_START + i, HAND_X, ARMOR_Y + i * 18) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return player.getPreferredEquipmentSlot(stack) == equip;
                }

                @Override
                public int getMaxItemCount() {
                    return 1;
                }

                @Override
                public Pair<Identifier, Identifier> getBackgroundSprite() {
                    return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, EMPTY_ARMOR[equip.getEntitySlotId()]);
                }
            });
        }

        // Mochila 3x9
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inventory, KikoInventory.BAG_START + col + row * 9, 8 + col * 18, 18 + row * 18));
            }
        }

        // Jugador
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 85 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col, 8 + col * 18, 143));
        }
    }

    public int getEntityId() {
        return entityId;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasStack()) return result;

        ItemStack stack = slot.getStack();
        result = stack.copy();
        int kikoEnd = KikoInventory.SIZE;           // 0..32 son de Kiko
        int playerEnd = this.slots.size();

        if (index < kikoEnd) {
            // De Kiko al jugador
            if (!this.insertItem(stack, kikoEnd, playerEnd, true)) return ItemStack.EMPTY;
        } else {
            // Del jugador a Kiko. Si es armadura y el espacio está libre, se la pone
            boolean placed = false;
            for (int i = 0; i < 4 && !placed; i++) {
                Slot armor = this.slots.get(KikoInventory.ARMOR_START + i);
                if (!armor.hasStack() && armor.canInsert(stack)) {
                    placed = this.insertItem(stack, KikoInventory.ARMOR_START + i, KikoInventory.ARMOR_START + i + 1, false);
                }
            }
            if (!placed && !this.insertItem(stack, KikoInventory.BAG_START, kikoEnd, false)) return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.setStack(ItemStack.EMPTY);
        else slot.markDirty();
        return result;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        inventory.onClose(player);
    }
}
