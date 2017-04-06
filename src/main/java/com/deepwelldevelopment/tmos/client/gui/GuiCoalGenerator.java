package com.deepwelldevelopment.tmos.client.gui;

import com.deepwelldevelopment.tmos.common.TMOS;
import com.deepwelldevelopment.tmos.common.tile.generator.TileGeneratorCoal;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiCoalGenerator extends GuiContainer {

    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    private static final ResourceLocation background = new ResourceLocation(TMOS.modId, "textures/gui/generatorcoal.png");

    TileGeneratorCoal tile;

    public GuiCoalGenerator(TileGeneratorCoal tileEntity, Container container) {
        super(container);

        this.tile = tileEntity;

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString("Coal Generator", this.xSize / 2 - this.fontRendererObj.getStringWidth("Coal Generator") / 2, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

//        int l = this.getProgressScaled(36);
//        this.drawTexturedModalRect(guiLeft + 71, guiTop + 24, 180, 0, l + 1, 21);
    }

    int getProgressScaled(int pixels) {
        return 0;
    }
}
