package com.pocketbeacons.client.screen;

import com.daqem.uilib.gui.AbstractContainerScreen;
import com.daqem.uilib.gui.widget.ButtonWidget;
import com.pocketbeacons.ApplyBeaconEffectPayload;
import com.pocketbeacons.menu.PocketBeaconMenu;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


import java.awt.*;

import static com.pocketbeacons.PocketBeacons.MOD_ID;
import static net.minecraft.text.Text.literal;


public class PocketBeaconScreen extends AbstractContainerScreen<PocketBeaconMenu> {
    private PocketBeaconScreenState state;
    private static final Identifier TEXTURE = Identifier.of("minecraft", "assets/pocket-beacons/textures/gui/container/inventory.png");

    private RegistryEntry<StatusEffect> selectedEffect = null;

    public PocketBeaconScreen(PocketBeaconMenu menu, PlayerInventory inventory, Text title) {
        super(menu, inventory, Text.of("")); // Temp get rid of wierd title on GUI
        title = Text.of("");
        this.backgroundWidth = 256;
        this.backgroundHeight = 256;
        super.init();
        //setBackground(new BlurredBackground());

    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // The gui background image
        context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                Identifier.of(MOD_ID, "textures/gui/beacon_gui.png"),
                this.x + 12, this.y + 5,
                0f, 0f,
                this.backgroundWidth, this.backgroundHeight,
                256, 256,
                -1
        );
        // actual gui
        context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                Identifier.of(MOD_ID, "textures/gui/pocket_beacon_gui.png"),
                this.x - 1, this.y - 20,
                0f, 0f,
                256, 128,
                255, 128,
                -1
        );
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //this.renderBackground(context, mouseX, mouseY, delta); // world dim
        super.render(context, mouseX, mouseY, delta);          // calls drawBackground → slots → components
        this.drawMouseoverTooltip(context, mouseX, mouseY);

        // Draw icons ontop of buttons
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                Identifier.of("minecraft", "textures/mob_effect/haste.png"),
                centerX - 11, centerY - 98,
                0f, 0f,
                20, 20,
                20, 20,
                -1
        );
    }

    @Override
    protected void init() {

        //Clear previous elements
        super.init();
        this.clearChildren();
        // get rid of inventory title
        this.playerInventoryTitleY = Integer.MAX_VALUE;

        // Define Layout Variables
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Close button
        ButtonWidget closeBtn = new ButtonWidget(0, 0, 100, 20, literal("Close"), (b) -> close());
        closeBtn.uilib$updateParentPosition(centerX - 50, centerY + 95);
        this.addWidget(closeBtn);

        // Confirm button
        ButtonWidget confirmBtn = new ButtonWidget(70, -75, 50, 20, literal("Apply"), (b) -> {
            if (selectedEffect != null) {
                ClientPlayNetworking.send(new ApplyBeaconEffectPayload(selectedEffect));
                this.close();
            }
        });

        // Haste button
        ButtonWidget hasteBtn = new ButtonWidget(centerX - 14,centerY-100,25,25, literal(""), (b) -> {
        if (selectedEffect == StatusEffects.HASTE) {
            selectedEffect = null;
            confirmBtn.active = false;
            b.setFocused(false); // toggle
        } else {
            selectedEffect = StatusEffects.HASTE;
            confirmBtn.active = true;
            b.setFocused(true); // toggle
            }
        });
        this.addWidget(hasteBtn);

        confirmBtn.uilib$updateParentPosition(centerX - 25, centerY + 60);
        confirmBtn.active = false; // greyed out by default
        this.addWidget(confirmBtn);



    }
}