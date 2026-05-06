package com.pocketbeacons.items;

import java.util.function.Function;

import com.pocketbeacons.PocketBeacons;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {

    public static Item POCKET_BEACON = register("pocket_beacon", PocketBeaconItem::new, new Item.Settings());
    public static final RegistryKey<ItemGroup> POCKET_BEACONS_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(PocketBeacons.MOD_ID, "pocket_beacons"));

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(PocketBeacons.MOD_ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        item = Registry.register(Registries.ITEM, itemKey, item);
        return item;
    }
    public static void initialize() {
        // creative tab
        Registry.register(Registries.ITEM_GROUP, POCKET_BEACONS_GROUP_KEY,
                FabricItemGroup.builder()
                        .icon(() -> new ItemStack(ModItems.POCKET_BEACON))
                        .displayName(Text.translatable("Pocket Beacons"))
                        .entries((context, entries) -> {
                            entries.add(ModItems.POCKET_BEACON);
                        })
                        .build()
        );
    }
}
