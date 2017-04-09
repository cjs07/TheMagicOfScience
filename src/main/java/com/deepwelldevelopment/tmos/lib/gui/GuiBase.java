package com.deepwelldevelopment.tmos.lib.gui;

import com.deepwelldevelopment.tmos.core.reflection.ReflectionFields;
import com.deepwelldevelopment.tmos.lib.gui.element.ElementBase;
import com.deepwelldevelopment.tmos.lib.gui.element.TabBase;
import com.deepwelldevelopment.tmos.lib.gui.slot.SlotFalseCopy;
import com.deepwelldevelopment.tmos.lib.util.helper.RenderHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_QUADS;


/**
 * The base class of the modular GUI system. Extend this class to be able to use the
 * functionality of this library. Works with Elements to create advanced, functional GUis
 * Tabs are also available for creating even more advanced GUIs
 *
 * @author cjs07
 */
public class GuiBase extends GuiContainer {

    protected boolean drawTitle = true;
    protected boolean drawInventory = true;
    protected int mouseX = 0;
    protected int mouseY = 0;

    protected int lastIndex = -1;

    protected String name;
    protected ResourceLocation texture;

    public ArrayList<TabBase> tabs = new ArrayList<TabBase>();
    protected ArrayList<ElementBase> elements = new ArrayList<ElementBase>();

    protected List<String> tooltip = new LinkedList<String>();
    protected boolean tooltips = true;

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
        if (drawTitle && name != null) {
            //TODO: WRITE METHODS FOR LOCALIZING NAMES
            fontRendererObj.drawString(name, getCenteredOffset(name), 6, 0x404040);
        }
        if (drawInventory) {
            //TODO: LOCALIZE THE PLAYER INVENTORY NAME AND DISPLAY IT
        }
        drawElements(0, true);
        drawTabs(0, true);
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
        drawTabs(partialTicks, false);
        GL11.glPopMatrix();
    }

    @Override
    protected void keyTyped(char characterTyped, int keyPressed) throws IOException {
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
    public void handleMouseInput() throws IOException {

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
    protected void mouseClicked(int mX, int mY, int mouseButton) throws IOException {
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
    protected void mouseClickMove(int mX, int mY, int lastClick, long timeSinceClick) {
        Slot slot = getSlotAtPosition(mX, mY);
        ItemStack itemstack = this.mc.player.inventory.getItemStack();

        if (this.dragSplitting && slot != null && itemstack != null && slot instanceof SlotFalseCopy) {
            if (lastIndex != slot.slotNumber) {
                lastIndex = slot.slotNumber;
                this.handleMouseClick(slot, slot.slotNumber, 0, ClickType.PICKUP);
            }
        } else {
            lastIndex = -1;
            super.mouseClickMove(mX, mY, lastClick, timeSinceClick);
        }
    }

    /**
     * Determines if the given x and y coordinates are contained within a slot in the GUI
     * @param xCoord
     * @param yCoord
     * @return The Slot that contains the given coordinates. Returns null if there is no slot
     */
    public Slot getSlotAtPosition(int xCoord, int yCoord) {
        for (int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k) {
            Slot slot = (Slot) this.inventorySlots.inventorySlots.get(k);

            if (this.isMouseOverSlot(slot, xCoord, yCoord)) {
                return slot;
            }
        }
        return null;
    }

    /**
     * Determines if the coordinates given are contained within the given slot
     * @param slot The slot to test for
     * @param xCoord
     * @param yCoord
     * @return Returns true if the slot contains the given coordinates
     */
    public boolean isMouseOverSlot(Slot slot, int xCoord, int yCoord) {
        return this.isPointInRegion(slot.xPos, slot.yPos, 16, 16, xCoord, yCoord);
    }

    /**
     * Draws the elements for this GUI.
     * Generally only called from the background/foreground draw methods
     * @param partialTick The partial tick from this frame
     *  @param foreground Whether or not the elements are being drawn in the foreground
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

    /**
     * Draws the tabs for this GUI. Handles Tab open/close animation.
     * Generally only called from the background/foreground draw methods
     * @param partialTick The partial tick from this frame
     *  @param foreground Whether or not the elements are being drawn in the foreground
     */
    protected void drawTabs(float partialTick, boolean foreground) {
        int yPosRight = 4;
        int yPosLeft = 4;

        if (foreground) {
            for (int i = 0; i < tabs.size(); i++) {
                TabBase tab = tabs.get(i);
                tab.update();
                if (!tab.isVisible()) {
                    continue;
                }
                if (tab.side == TabBase.LEFT) {
                    tab.drawForeground(mouseX, mouseY);
                    yPosLeft += tab.currentHeight;
                } else {
                    tab.drawForeground(mouseX, mouseY);
                    yPosRight += tab.currentHeight;
                }
            }
        } else {
            for (int i = 0; i < tabs.size(); i++) {
                TabBase tab = tabs.get(i);
                tab.update();
                if (!tab.isVisible()) {
                    continue;
                }
                if (tab.side == TabBase.LEFT) {
                    tab.setPosition(0, yPosLeft);
                    tab.drawBackground(mouseX, mouseY, partialTick);
                    yPosLeft += tab.currentHeight;
                } else {
                    tab.setPosition(xSize, yPosRight);
                    tab.drawBackground(mouseX, mouseY, partialTick);
                    yPosRight += tab.currentHeight;
                }
            }
        }
    }

    /**
     * Called by NEI if installed
     */
    // @Override
    public List<String> handleTooltip(int mousex, int mousey, List<String> tooltip) {
        if (mc.player.inventory.getItemStack() == null) {
            addTooltips(tooltip);
        }
        return tooltip;
    }

    public void addTooltips(List<String> tooltip) {
        TabBase tab = getTabAtPosition(mouseX, mouseY);

        if (tab != null) {
            tab.addTooltip(tooltip);
        }
        ElementBase element = getElementAtPosition(mouseX, mouseY);

        if (element != null && element.isVisible()) {
            element.addTooltip(tooltip);
        }
    }

    /* ELEMENTS */
    public ElementBase addElement(ElementBase element) {
        elements.add(element);
        return element;
    }

    public TabBase addTab(TabBase tab) {
        int yOffset = 4;
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).side == tab.side && tabs.get(i).isVisible()) {
                yOffset += tabs.get(i).currentHeight;
            }
        }
        tab.setPosition(tab.side == TabBase.LEFT ? 0 : xSize, yOffset);
        tabs.add(tab);

        if (TabTracker.getOpenedLeftTab() != null && tab.getClass().equals(TabTracker.getOpenedLeftTab())) {
            tab.setFullyOpen();
        } else if (TabTracker.getOpenedRightTab() != null && tab.getClass().equals(TabTracker.getOpenedRightTab())) {
            tab.setFullyOpen();
        }
        return tab;
    }

    protected ElementBase getElementAtPosition(int mX, int mY) {
        for (int i = elements.size(); i-- > 0;) {
            ElementBase element = elements.get(i);
            if (element.intersectsWith(mX, mY)) {
                return element;
            }
        }
        return null;
    }

    protected TabBase getTabAtPosition(int mX, int mY) {
        int xShift = 0;
        int yShift = 4;

        for (int i = 0; i < tabs.size(); i++) {
            TabBase tab = tabs.get(i);
            if (!tab.isVisible() || tab.side == TabBase.RIGHT) {
                continue;
            }
            tab.setCurrentShift(xShift, yShift);
            if (tab.intersectsWith(mX, mY, xShift, yShift)) {
                return tab;
            }
            yShift += tab.currentHeight;
        }

        xShift = xSize;
        yShift = 4;

        for (int i = 0; i < tabs.size(); i++) {
            TabBase tab = tabs.get(i);
            if (!tab.isVisible() || tab.side == TabBase.LEFT) {
                continue;
            }
            tab.setCurrentShift(xShift, yShift);
            if (tab.intersectsWith(mX, mY, xShift, yShift)) {
                return tab;
            }
            yShift += tab.currentHeight;
        }
        return null;
    }

    protected final void updateElements() {
        for (int i = elements.size(); i-- > 0;) {
            ElementBase c = elements.get(i);
            if (c.isVisible() && c.isEnabled()) {
                c.update(mouseX, mouseY);
            }
        }
    }

    protected void updateElementInformation() {
    }

    public void handleElementButtonClick(String buttonName, int mouseButton) {
    }

    /* HELPERS */
    public void bindTexture(ResourceLocation texture) {
        mc.renderEngine.bindTexture(texture);
    }

    public void drawItemStack(ItemStack stack, int x, int y, boolean drawOverlay, String overlayTxt) {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        itemRender.zLevel = 200.0F;

        FontRenderer font = null;
        if (stack != null) {
            font = stack.getItem().getFontRenderer(stack);
        }
        if (font == null) {
            font = fontRendererObj;
        }

        itemRender.renderItemAndEffectIntoGUI(stack, x, y);

        if (drawOverlay) {
            try {
                itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (ReflectionFields.GUI_CONTAINER_DRAGGED_STACK.get(this) == null ? 0 : 8), overlayTxt);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        this.zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    /**
     * Simple method used to draw a fluid of arbitrary size.
     */
    public void drawFluid(int x, int y, FluidStack fluid, int width, int height) {
        if (fluid == null || fluid.getFluid() == null) {
            return;
        }
        RenderHelper.setColor3ub(fluid.getFluid().getColor(fluid));

        drawTiledTexture(x, y, fluid.getFluid().getStill(fluid), width, height);
    }

    public void drawTiledTexture(int x, int y, ResourceLocation texture, int width, int height) {
        int i = 0;
        int j = 0;

        int drawHeight = 0;
        int drawWidth = 0;

        for (i = 0; i < width; i += 16) {
            for (j = 0; j < height; j += 16) {
                drawWidth = Math.min(width - i, 16);
                drawHeight = Math.min(height - j, 16);
                drawScaledTexturedModelRect(x + i, y + j, texture, 0, 0, 16, 16, drawWidth, drawHeight);
            }
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
    }

    public void drawSizedModalRect(int x1, int y1, int x2, int y2, int color) {
        int temp;

        if (x1 < x2) {
            temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if (y1 < y2) {
            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        float a = (color >> 24 & 255) / 255.0F;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = tessellator.getBuffer();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(r, g, b, a);
        vertexBuffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        vertexBuffer.pos(x1, y2, this.zLevel).endVertex();
        vertexBuffer.pos(x2, y2, this.zLevel).endVertex();
        vertexBuffer.pos(x2, y1, this.zLevel).endVertex();
        vertexBuffer.pos(x1, y1, this.zLevel).endVertex();
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawSizedRect(int x1, int y1, int x2, int y2, int color) {
        int temp;

        if (x1 < x2) {
            temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if (y1 < y2) {
            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        float a = (color >> 24 & 255) / 255.0F;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = tessellator.getBuffer();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(r, g, b, a);
        vertexBuffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        vertexBuffer.pos(x1, y2, this.zLevel).endVertex();
        vertexBuffer.pos(x2, y2, this.zLevel).endVertex();
        vertexBuffer.pos(x2, y1, this.zLevel).endVertex();
        vertexBuffer.pos(x1, y1, this.zLevel).endVertex();
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public void drawSizedTexturedModalRect(int x, int y, int u, int v, int width, int height, float texW, float texH) {
        float texU = 1 / texW;
        float texV = 1 / texH;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = tessellator.getBuffer();
        vertexBuffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        vertexBuffer.pos(x, y + height, this.zLevel).tex((u) * texU, (v + height) * texV).endVertex();
        vertexBuffer.pos(x + width, y + height, this.zLevel).tex((u + width) * texU, (v + height) * texV).endVertex();
        vertexBuffer.pos(x + width, y, this.zLevel).tex((u + width) * texU, (v) * texV).endVertex();
        vertexBuffer.pos(x, y, this.zLevel).tex((u) * texU, (v) * texV).endVertex();
        tessellator.draw();
    }

    public void drawScaledTexturedModelRect(int x, int y,  ResourceLocation texture, int textureX, int textureY, int textureWidth, int textureHeight, int width, int height) {
        if (texture == null) {
            return;
        }

        int maxTextureX = textureX + textureWidth;
        int maxTextureY = textureY + textureHeight;

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = tessellator.getBuffer();
        vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexBuffer.pos(x + 0, y + height, this.zLevel).tex(textureX, textureY + (maxTextureY - textureY) * height / 16F).endVertex();
        vertexBuffer.pos(x + width, y + height, this.zLevel).tex(textureX + (maxTextureX - textureX) * width / 16F, textureY + (maxTextureY - textureY) * height / 16F).endVertex();
        vertexBuffer.pos(x + width, y + 0, this.zLevel).tex(textureX + (maxTextureX - textureX) * width / 16F, textureY).endVertex();
        vertexBuffer.pos(x + 0, y + 0, this.zLevel).tex(textureX, textureY).endVertex();
        tessellator.draw();
    }

    public void drawTooltip(List<String> list) {
        drawTooltipHoveringText(list, mouseX + guiLeft, mouseY + guiTop, fontRendererObj);
        tooltip.clear();
    }

    @SuppressWarnings("rawtypes")
    protected void drawTooltipHoveringText(List list, int x, int y, FontRenderer font) {

        if (list == null || list.isEmpty()) {
            return;
        }
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        int k = 0;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            int l = font.getStringWidth(s);

            if (l > k) {
                k = l;
            }
        }
        int i1 = x + 12;
        int j1 = y - 12;
        int k1 = 8;

        if (list.size() > 1) {
            k1 += 2 + (list.size() - 1) * 10;
        }
        if (i1 + k > this.width) {
            i1 -= 28 + k;
        }
        if (j1 + k1 + 6 > this.height) {
            j1 = this.height - k1 - 6;
        }
        this.zLevel = 300.0F;
        itemRender.zLevel = 300.0F;
        int l1 = -267386864;
        this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
        this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
        this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
        this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
        this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
        int i2 = 1347420415;
        int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
        this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
        this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
        this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
        this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

        for (int k2 = 0; k2 < list.size(); ++k2) {
            String s1 = (String) list.get(k2);
            font.drawStringWithShadow(s1, i1, j1, -1);

            if (k2 == 0) {
                j1 += 2;
            }
            j1 += 10;
        }
        this.zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }

    /**
     * Passthrough method for tab use.
     */
    public void mouseClicked(int mouseButton) throws IOException {
        super.mouseClicked(guiLeft + mouseX, guiTop + mouseY, mouseButton);
    }

    public FontRenderer getFontRenderer() {
        return fontRendererObj;
    }

    protected int getCenteredOffset(String string) {
        return this.getCenteredOffset(string, xSize);
    }

    protected int getCenteredOffset(String string, int xWidth) {
        return (xWidth - fontRendererObj.getStringWidth(string)) / 2;
    }

    public int getGuiLeft() {
        return guiLeft;
    }

    public int getGuiTop() {
        return guiTop;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void overlayRecipe() {
    }
}
