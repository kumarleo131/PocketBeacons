package com.pocketbeacons;

import com.pocketbeacons.client.screen.PocketBeaconScreen;
import com.pocketbeacons.menu.PocketBeaconMenu;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandler;

public class PocketBeaconsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HandledScreens.register(PocketBeacons.POCKET_BEACON_MENU, PocketBeaconScreen::new);
	}
}