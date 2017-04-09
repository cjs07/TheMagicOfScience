package com.deepwelldevelopment.tmos.lib.gui.element;

import com.deepwelldevelopment.tmos.lib.gui.GuiBase;
import com.deepwelldevelopment.tmos.lib.util.helper.RenderHelper;

/**
 * Represents a GUI progress bar
 */
public class ElementDualScaled extends ElementBase {

    /* The amount of the texture that is used*/
    public int quantity;
    /*Draw mode. 0: Vertical T--> B  1: Vertical B --> Top 2: Horizontal L --> R 3: Horizontal R --> L*/
    public int mode;
    public boolean background = true;

    public ElementDualScaled(GuiBase gui, int posX, int posY) {
        super(gui, posX, posY);
    }

    public ElementDualScaled setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public ElementDualScaled setMode(int mode) {
        this.mode = mode;
        return this;
    }

    public ElementDualScaled setBackground(boolean background) {
        this.background = background;
        return this;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks) {
        RenderHelper.bindTexture(texture);
        if (background) {
            drawTexturedModalRect(posX, posY, 0, 0, sizeX, sizeY);
        }
        switch (mode) {
            case 0: //T --> B
                drawTexturedModalRect(posX, posY, sizeX, 0, sizeX, quantity);
                return;
            case 1: //B --> T
                drawTexturedModalRect(posX, posY + sizeY - quantity, sizeX, sizeY - quantity, sizeX, quantity);
                return;
            case 2: //L --> R
                drawTexturedModalRect(posX, posY, sizeX, 0, quantity, sizeY);
                return;
            case 3: //R --> L
                drawTexturedModalRect(posX + sizeX - quantity, posY, sizeX + sizeX - quantity, 0, quantity, sizeY);
                return;
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
    }
}
