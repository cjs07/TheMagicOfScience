package com.deepwelldevelopment.tmos.core.research;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ResearchManager {

    public static final String RESEARCH_TAG = "TMOS.RESEARCH";

    public static boolean b = false;

    public static boolean doesPlayerHaveCraftingRequisite(ItemStack is) {
        Item item = is.getItem();
        System.out.println("[TMOS] " + item.getUnlocalizedName());
        b = false;
        return false;
    }
}
