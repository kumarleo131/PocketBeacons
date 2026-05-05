package com.pocketbeacons;

import com.pocketbeacons.menu.PocketBeaconMenu;
import net.fabricmc.api.ModInitializer;


import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pocketbeacons.items.ModItems;



public class PocketBeacons implements ModInitializer {
	public static final String MOD_ID = "pocket-beacons";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ScreenHandlerType<PocketBeaconMenu> POCKET_BEACON_MENU =
			new ScreenHandlerType<>((syncId, inv) -> new PocketBeaconMenu(syncId, inv), FeatureFlags.VANILLA_FEATURES);



	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Initializing Pocket Beacons!");
		ModItems.initialize();

		Registry.register(
				Registries.SCREEN_HANDLER,
				Identifier.of(MOD_ID, "pocket_beacon_menu"),
				POCKET_BEACON_MENU
		);

		PayloadTypeRegistry.playC2S().register(
				ApplyBeaconEffectPayload.ID,
				ApplyBeaconEffectPayload.CODEC
		);

		ServerPlayNetworking.registerGlobalReceiver(ApplyBeaconEffectPayload.ID, (payload, context) -> {
			context.server().execute(() -> {
				context.player().addStatusEffect(
						new StatusEffectInstance(payload.effect(), 200, 0)
				);
			});
		});


	}
}