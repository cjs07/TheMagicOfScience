package com.deepwelldevelopment.tmos.client.gui;

import com.deepwelldevelopment.tmos.api.items.TMOSItems;
import com.deepwelldevelopment.tmos.common.TMOS;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TMOSTab extends CreativeTabs {

    public TMOSTab() {
        super(TMOS.modId);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(TMOSItems.ingotChromium);
    }
}
