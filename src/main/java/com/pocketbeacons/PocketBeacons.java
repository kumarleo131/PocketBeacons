package com.pocketbeacons;

import com.pocketbeacons.menu.PocketBeaconMenu;
import net.fabricmc.api.ModInitializer;


import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pocketbeacons.items.ModItems;



public class PocketBeacons implements ModInitializer {
	public static final String MOD_ID = "pocket-beacons";

	// This logger is used to write text to the console and the log file.
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
				ServerPlayerEntity player = context.player();

				if (player.currentScreenHandler instanceof PocketBeaconMenu menu) {
					ItemStack slotItem = menu.getSlot(0).getStack();
					int duration;

					if (slotItem.isOf(Items.NETHERITE_INGOT)) {
						duration = 20 * 600; // 10 minutes
					} else if (slotItem.isOf(Items.EMERALD)) {
						duration = 20 * 300; // 5 minutes
					} else if (slotItem.isOf(Items.DIAMOND)) {
						duration = 20 * 240; // 4 minutes
					} else if (slotItem.isOf(Items.GOLD_INGOT)) {
						duration = 20 * 180; // 3 minutes
					} else if (slotItem.isOf(Items.IRON_INGOT)) {
						duration = 20 * 120; // 2 minutes
					} else {
						duration = 0; // no item
					}

					// Remove any existing pocket beacon effects before applying new one
					player.removeStatusEffect(StatusEffects.HASTE);
					player.removeStatusEffect(StatusEffects.SPEED);
					player.removeStatusEffect(StatusEffects.JUMP_BOOST);

					player.addStatusEffect(new StatusEffectInstance(payload.effect(), duration, 1));
					menu.effectApplied = true;
					menu.getSlot(0).setStack(ItemStack.EMPTY);
					menu.inventory.markDirty();
					player.currentScreenHandler.syncState();
					player.closeHandledScreen();
					player.networkHandler.sendPacket(new PlaySoundS2CPacket(
							Registries.SOUND_EVENT.getEntry(SoundEvents.BLOCK_BEACON_ACTIVATE),
							SoundCategory.PLAYERS,
							player.getX(), player.getY(), player.getZ(),
							1.0f, 1.3f, 0L
					));
				}
			});
		});
	}
}