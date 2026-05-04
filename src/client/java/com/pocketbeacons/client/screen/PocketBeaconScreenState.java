package com.pocketbeacons.client.screen;

import net.minecraft.item.Item;

public class PocketBeaconScreenState {
    // Data
    public enum PrimaryPower {
        SPEED,
        HASTE,
        JUMP_BOOST
    }
    PrimaryPower currentPower;

    public PocketBeaconScreenState() {

    }

    // Getters and Setters
    public PrimaryPower getCurrentPower() {
        return currentPower;
    }
    public void setCurrentPower(PrimaryPower newPower) {
        currentPower = newPower;
    }

    // Logic can also live here
}
