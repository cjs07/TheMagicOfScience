package com.deepwelldevelopment.tmos.common.item;

import com.deepwelldevelopment.tmos.common.TMOS;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TMOSItem extends Item {

    protected String name;

    public TMOSItem(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(TMOS.tmosTab);
    }

    public void registerItemModel() {
        TMOS.proxy.registerItemRenderer(this, 0, name);
    }

    @Override
    public TMOSItem setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }
}
