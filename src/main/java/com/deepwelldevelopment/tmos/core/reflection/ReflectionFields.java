package com.deepwelldevelopment.tmos.core.reflection;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

public class ReflectionFields {

    public static final Field GUI_CONTAINER_DRAGGED_STACK = ReflectionHelper.findField(GuiContainer.class, "draggedStack");
    public static final Field eventHandlerField = ReflectionHelper.findField(InventoryCrafting.class, "eventHandler");
    public static final Field containerPlayerPlayerField = ReflectionHelper.findField(ContainerPlayer.class, "player");
    public static final Field slotCraftingPlayerField = ReflectionHelper.findField(SlotCrafting.class, "player");
}
