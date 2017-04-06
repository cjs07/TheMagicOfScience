package com.deepwelldevelopment.tmos.lib.gui;

import com.deepwelldevelopment.tmos.lib.gui.element.ElementBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * The base class of the modular GUI system. Extend this class to be able to use the
 * functionality of this library. Works with Elements to create advanced, functional GUis
 *
 * @author cjs07
 */
public class GuiBase extends GuiContainer {

    protected int mouseX = 0;
    protected int mouseY = 0;

    protected String name;
    protected ResourceLocation texture;

    ArrayList<ElementBase> elements = new ArrayList<>();

    public GuiBase(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    public GuiBase(Container container, ResourceLocation texture) {

        super(container);
        this.texture = texture;
    }

    @Override
    public void initGui() {
        super.initGui();
        elements.clear();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateElementInformation();

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (tooltips && mc.player.inventory.getItemStack() == null) {
            addTooltips(tooltip);
            drawTooltip(tooltip);
        }

        this.mouseX = mouseX;
        this.mouseY = mouseY;

        updateElements();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawElements(0, true);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        this.mouseX = mouseX;
        this.mouseY = mouseY;

        GL11.glPushMatrix();
        GL11.glTranslatef(guiLeft, guiTop, 0.0F);
        drawElements(partialTicks, false);
        GL11.glPopMatrix();
    }

    @Override
    protected void keyTyped(char characterTyped, int keyPressed) {
        for (int i = elements.size(); i-- > 0;) {
            ElementBase c = elements.get(i);
            if (!c.isVisible() || !c.isEnabled()) {
                continue;
            }
            if (c.onKeyTyped(characterTyped, keyPressed)) {
                return;
            }
        }
        super.keyTyped(characterTyped, keyPressed);
    }

    @Override
    public void handleMouseInput() {

        int x = Mouse.getEventX() * width / mc.displayWidth;
        int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;

        mouseX = x - guiLeft;
        mouseY = y - guiTop;

        int wheelMovement = Mouse.getEventDWheel();

        if (wheelMovement != 0) {
            for (int i = elements.size(); i-- > 0;) {
                ElementBase c = elements.get(i);
                if (!c.isVisible() || !c.isEnabled() || !c.intersectsWith(mouseX, mouseY)) {
                    continue;
                }
                if (c.onMouseWheel(mouseX, mouseY, wheelMovement)) {
                    return;
                }
            }

            if (onMouseWheel(mouseX, mouseY, wheelMovement)) {
                return;
            }
        }
        super.handleMouseInput();
    }

    protected boolean onMouseWheel(int mouseX, int mouseY, int wheelMovement) {
        return false;
    }

    @Override
    protected void mouseClicked(int mX, int mY, int mouseButton) {
        mX -= guiLeft;
        mY -= guiTop;

        for (int i = elements.size(); i-- > 0;) {
            ElementBase c = elements.get(i);
            if (!c.isVisible() || !c.isEnabled() || !c.intersectsWith(mX, mY)) {
                continue;
            }
            if (c.onMousePressed(mX, mY, mouseButton)) {
                return;
            }
        }

        TabBase tab = getTabAtPosition(mX, mY);
        if (tab != null) {
            int tMx = mX;

            if (!tab.onMousePressed(tMx, mY, mouseButton)) {
                for (int i = tabs.size(); i-- > 0;) {
                    TabBase other = tabs.get(i);
                    if (other != tab && other.open && other.side == tab.side) {
                        other.toggleOpen();
                    }
                }
                tab.toggleOpen();
                return;
            }
        }

        mX += guiLeft;
        mY += guiTop;

        if (tab != null) {
            switch (tab.side) {
                case TabBase.LEFT:
                    // guiLeft -= tab.currentWidth;
                    break;
                case TabBase.RIGHT:
                    xSize += tab.currentWidth;
                    break;
            }
        }
        super.mouseClicked(mX, mY, mouseButton);
        if (tab != null) {
            switch (tab.side) {
                case TabBase.LEFT:
                    // guiLeft += tab.currentWidth;
                    break;
                case TabBase.RIGHT:
                    xSize -= tab.currentWidth;
                    break;
            }
        }
    }

    @Override
    protected void mouseMovedOrUp(int mX, int mY, int mouseButton) {

        mX -= guiLeft;
        mY -= guiTop;

        if (mouseButton >= 0 && mouseButton <= 2) { // 0:left, 1:right, 2: middle
            for (int i = elements.size(); i-- > 0;) {
                ElementBase c = elements.get(i);
                if (!c.isVisible() || !c.isEnabled()) { // no bounds checking on mouseUp events
                    continue;
                }
                c.onMouseReleased(mX, mY);
            }
        }
        mX += guiLeft;
        mY += guiTop;

        super.mouseMovedOrUp(mX, mY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mX, int mY, int lastClick, long timeSinceClick) {

        Slot slot = getSlotAtPosition(mX, mY);
        ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();

        if (this.field_147007_t && slot != null && itemstack != null && slot instanceof SlotFalseCopy) {
            if (lastIndex != slot.slotNumber) {
                lastIndex = slot.slotNumber;
                this.handleMouseClick(slot, slot.slotNumber, 0, 0);
            }
        } else {
            lastIndex = -1;
            super.mouseClickMove(mX, mY, lastClick, timeSinceClick);
        }
    }

    public Slot getSlotAtPosition(int xCoord, int yCoord) {

        for (int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k) {
            Slot slot = (Slot) this.inventorySlots.inventorySlots.get(k);

            if (this.isMouseOverSlot(slot, xCoord, yCoord)) {
                return slot;
            }
        }
        return null;
    }

    public boolean isMouseOverSlot(Slot theSlot, int xCoord, int yCoord) {

        return this.func_146978_c(theSlot.xDisplayPosition, theSlot.yDisplayPosition, 16, 16, xCoord, yCoord);
    }

    /**
     * Draws the elements for this GUI.
     */
    protected void drawElements(float partialTick, boolean foreground) {

        if (foreground) {
            for (int i = 0; i < elements.size(); i++) {
                ElementBase element = elements.get(i);
                if (element.isVisible()) {
                    element.drawForeground(mouseX, mouseY);
                }
            }
        } else {
            for (int i = 0; i < elements.size(); i++) {
                ElementBase element = elements.get(i);
                if (element.isVisible()) {
                    element.drawBackground(mouseX, mouseY, partialTick);
                }
            }
        }
    }
}
