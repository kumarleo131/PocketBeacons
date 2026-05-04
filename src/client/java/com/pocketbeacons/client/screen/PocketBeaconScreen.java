package com.pocketbeacons.client.screen;

import com.daqem.uilib.gui.AbstractContainerScreen;
import com.daqem.uilib.gui.widget.ButtonWidget;
import com.pocketbeacons.menu.PocketBeaconMenu;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


import static com.pocketbeacons.PocketBeacons.MOD_ID;
import static net.minecraft.text.Text.literal;


public class PocketBeaconScreen extends AbstractContainerScreen<PocketBeaconMenu> {
    private PocketBeaconScreenState state;
    private static final Identifier TEXTURE = Identifier.of("minecraft", "assets/pocket-beacons/textures/gui/container/inventory.png");

    public PocketBeaconScreen(PocketBeaconMenu menu, PlayerInventory inventory, Text title) {
        super(menu, inventory, Text.of("")); // Temp get rid of wierd title on GUI
        this.width = 256;
        this.height = 256;
        //setBackground(new BlurredBackground());

    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // The gui background image
        context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                Identifier.of(MOD_ID, "textures/gui/beacon_gui.png"),
                this.x, this.y,
                0f, 0f,
                this.width, this.height,
                256, 256,
                -1
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta); // world dim
        super.render(context, mouseX, mouseY, delta);          // calls drawBackground → slots → components
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void init() {
        // 1. Clear previous elements (handled by super, but good practice to know)
        super.init();

        // 2. Define Layout Variables
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Add close button
        ButtonWidget closeBtn = new ButtonWidget(0, 0, 100, 20, literal("Close"), (b) -> close());
        closeBtn.uilib$updateParentPosition(centerX - 50, centerY + 90);
        this.addWidget(closeBtn);



    }
}