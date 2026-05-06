package com.pocketbeacons.menu;

import com.pocketbeacons.PocketBeacons;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.StyleSpriteSource;
import com.daqem.uilib.gui.*;

import static net.minecraft.item.Items.GOLD_INGOT;

public class PocketBeaconMenu extends ScreenHandler {

    private final Inventory inventory;
    public PocketBeaconMenu(int syncId, Inventory playerInventory) {
        super(PocketBeacons.POCKET_BEACON_MENU, syncId);

        // Container inventory
        this.inventory = new SimpleInventory(1); // 1 slot for your beacon

        // Beacon slot
        this.addSlot(new Slot(this.inventory, 0, 135 + 13, 109 + 6) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.IRON_INGOT)
                        || stack.isOf(GOLD_INGOT)
                        || stack.isOf(Items.DIAMOND)
                        || stack.isOf(Items.NETHERITE_INGOT)
                        || stack.isOf(Items.EMERALD);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }
        });

        // Add player inventory slots
        addPlayerInventory((PlayerInventory) playerInventory);
        addPlayerHotbar((PlayerInventory) playerInventory);
        }

    // Player inventory
    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        35 + 13 + col * 18, 136+6 + row * 18));
            }
        }
    }

    // Hotbar
    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 35 + 13 + col * 18, 194+6));
        }
    }


    public boolean stillValid(StyleSpriteSource.Player player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slotObj = this.slots.get(slot);

        if (slotObj.hasStack()) {
            ItemStack slotStack = slotObj.getStack();
            newStack = slotStack.copy();

            if (slot == 0) {
                // Move from beacon slot to player inventory
                if (!this.insertItem(slotStack, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Move from player inventory to beacon slot
                if (!this.insertItem(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slotObj.setStack(ItemStack.EMPTY);
            } else {
                slotObj.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    // Close gui without applying
    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        ItemStack stack = this.inventory.getStack(0);
        if (!stack.isEmpty()) {
            player.giveItemStack(stack);
            this.inventory.setStack(0, ItemStack.EMPTY);
        }
    }


}