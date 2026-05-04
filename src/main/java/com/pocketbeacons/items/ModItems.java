package com.pocketbeacons.items;

import java.util.function.Function;

import com.pocketbeacons.PocketBeacons;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems {

    public static Item POCKET_BEACON = register("pocket_beacon", PocketBeaconItem::new, new Item.Settings());

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(PocketBeacons.MOD_ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        item = Registry.register(Registries.ITEM, itemKey, item);
        return item;
    }

    public static void initialize() {
        PocketBeacons.LOGGER.info("Registering items for Pocket Beacons!");
    }
}
