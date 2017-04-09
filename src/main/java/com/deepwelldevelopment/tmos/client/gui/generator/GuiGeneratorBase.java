package com.deepwelldevelopment.tmos.client.gui.generator;

import com.deepwelldevelopment.tmos.common.tile.generator.TileGeneratorBase;
import com.deepwelldevelopment.tmos.lib.gui.GuiBaseAdvanced;
import com.deepwelldevelopment.tmos.lib.gui.element.ElementEnergyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiGeneratorBase extends GuiBaseAdvanced {

    protected TileGeneratorBase tile;

    public GuiGeneratorBase(Container inventorySlotsIn, TileEntity tile, EntityPlayer player, ResourceLocation texture) {
        super(inventorySlotsIn, texture);
        this.tile = (TileGeneratorBase)tile;
    }

    @Override
    public void initGui() {
        super.initGui();
        addElement(new ElementEnergyStorage(this, 80, 18, tile.getEnergyStorage()));
    }

    @Override
    protected void updateElementInformation() {
        super.updateElementInformation();
    }
}
