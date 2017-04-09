package com.deepwelldevelopment.tmos.lib.util.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderHelper {

    public static final double RENDER_OFFSET = 1.0D / 1024.0D;

    private RenderHelper() {
    }

    public static final TextureManager engine() {
        return Minecraft.getMinecraft().renderEngine;
    }

    public static final Tessellator tessellator() {
        return Tessellator.getInstance();
    }

    public static void setColor3ub(int color) {
        GL11.glColor3ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF));
    }

    public static void setColor4ub(int color) {
        GL11.glColor4ub((byte) (color >> 24 & 0xFF), (byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF));
    }

    public static void resetColor() {
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    public static final void bindTexture(ResourceLocation texture) {
        engine().bindTexture(texture);
    }
}
