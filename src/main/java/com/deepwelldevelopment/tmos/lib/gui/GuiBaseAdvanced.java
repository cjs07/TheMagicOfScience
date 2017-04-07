package com.deepwelldevelopment.tmos.lib.gui;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

/**
 * Advanced version of the GuiBase class for the modular GUI system
 * Includes resources for progress bars (arrows, flames, etc.) as well as
 * non-item internal storage (energy, fluids, etc.)
 *
 * @author cjs07
 */
public class GuiBaseAdvanced extends GuiBase {

    public GuiBaseAdvanced(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    public GuiBaseAdvanced(Container container, ResourceLocation texture) {
        super(container, texture);
    }
}
