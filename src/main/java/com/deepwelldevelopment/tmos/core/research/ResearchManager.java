package com.deepwelldevelopment.tmos.core.research;

import com.google.common.base.Throwables;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

import static com.deepwelldevelopment.tmos.core.reflection.ReflectionFields.*;

public class ResearchManager {

    public static final String RESEARCH_TAG = "TMOS.RESEARCH";



    public static boolean doesPlayerHaveCraftingRequisite(ItemStack is, InventoryCrafting inv) {
        EntityPlayer player = findPlayer(inv);
        if (player != null) {
            System.out.println("[TMOS-RESEARCH MODULE] FOUND PLAYER: " + player.getName());
        } else {
            System.out.println("[TMOS-RESEARCH MODULE] COULD NOT FIND PLAYER");
        }
        return true;
    }

    private static EntityPlayer findPlayer(InventoryCrafting inv) {
        try {
            Container container = (Container) eventHandlerField.get(inv);
            if (container instanceof ContainerPlayer) {
                return (EntityPlayer) containerPlayerPlayerField.get(container);
            } else if (container instanceof ContainerWorkbench) {
                return (EntityPlayer) slotCraftingPlayerField.get(container.getSlot(0));
            } else {
                // don't know the player
                return null;
            }
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
