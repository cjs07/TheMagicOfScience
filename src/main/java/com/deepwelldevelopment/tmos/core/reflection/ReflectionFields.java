package com.deepwelldevelopment.tmos.core.reflection;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

public class ReflectionFields {

    public static final Field GUI_CONTAINER_DRAGGED_STACK = ReflectionHelper.findField(GuiContainer.class, "draggedStack");
}
