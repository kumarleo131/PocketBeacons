package com.pocketbeacons.menu;

import com.pocketbeacons.PocketBeacons;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.StyleSpriteSource;
import com.daqem.uilib.gui.*;
public class PocketBeaconMenu extends ScreenHandler {

    private final Inventory inventory;
    public PocketBeaconMenu(int syncId, Inventory playerInventory) {
        super(PocketBeacons.POCKET_BEACON_MENU, syncId);

        // Container inventory (for your custom slots)
        this.inventory = new SimpleInventory(1); // example: 1 slot for your beacon

        // Add your container slots
        this.addSlot(new Slot(this.inventory, 0, 135, 109)); // position in GUI

        // Add **player inventory slots**
        addPlayerInventory((PlayerInventory) playerInventory);
        addPlayerHotbar((PlayerInventory) playerInventory);
        }

    // Player inventory
    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        35 + col * 18, 136 + row * 18));
            }
        }
    }

    // Hotbar
    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 35 + col * 18, 194));
        }
    }


    public boolean stillValid(StyleSpriteSource.Player player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }


}