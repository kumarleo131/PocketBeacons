package com.pocketbeacons.items;

import com.pocketbeacons.menu.PocketBeaconMenu;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;


public class PocketBeaconItem extends Item {
    private static final String PRIMARY_EFFECT_KEY = "PrimaryEffect";
    private static final String SECONDARY_EFFECT_KEY = "SecondaryEffect";
    private static final String FUEL_COUNT_KEY = "FuelCount";
    private static final String BURN_TICKS_KEY = "BurnTicks";

    private static final int EFFECT_REFRESH_INTERVAL = 80;
    private static final int EFFECT_DURATION_TICKS = 220;
    private static final int FUEL_BURN_TICKS = 20 * 60;

    public PocketBeaconItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (hand != Hand.MAIN_HAND) {
            return ActionResult.PASS;
        }

        if (!world.isClient()) {
            user.openHandledScreen(new NamedScreenHandlerFactory() {
                @Override
                public Text getDisplayName() {
                    return Text.translatable("screen.pocket-beacons.pocket_beacon");
                }

                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                    return new PocketBeaconMenu(syncId, playerInventory);
                }
            });
        }

        return ActionResult.SUCCESS;
    }
}
