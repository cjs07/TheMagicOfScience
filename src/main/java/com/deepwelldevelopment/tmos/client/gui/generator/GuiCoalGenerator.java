package com.deepwelldevelopment.tmos.client.gui.generator;

import com.deepwelldevelopment.tmos.common.container.ContainerCoalGenerator;
import com.deepwelldevelopment.tmos.common.tile.generator.TileGeneratorCoal;
import com.deepwelldevelopment.tmos.lib.gui.GuiProps;
import com.deepwelldevelopment.tmos.lib.gui.element.ElementDualScaled;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiCoalGenerator extends GuiGeneratorBase {

    private static final ResourceLocation TEXTURE = new ResourceLocation(GuiProps.PATH_GUI + "generatorcoal.png");

    ElementDualScaled duration;

    public GuiCoalGenerator(InventoryPlayer playerInv, TileGeneratorCoal tile) {
        super(new ContainerCoalGenerator(playerInv, tile), tile, playerInv.player, TEXTURE);
    }

    @Override
    public void initGui() {
        super.initGui();
        duration = (ElementDualScaled) addElement(new ElementDualScaled(this, 82, 19).setSize(16, 16).setTexture(TEX_FLAME, 32, 16));
        duration.setMode(0);
    }

    @Override
    protected void updateElementInformation() {
        super.updateElementInformation();
        duration.setQuantity(tile.getScaledDuration(16));
    }
}
