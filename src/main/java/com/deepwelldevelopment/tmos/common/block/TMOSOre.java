package com.deepwelldevelopment.tmos.common.block;

import com.deepwelldevelopment.tmos.common.item.ItemOreDict;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.oredict.OreDictionary;

public class TMOSOre extends TMOSBlock implements ItemOreDict {

    protected String oreName;

    public TMOSOre(String blockName, String oreName) {
        super(Material.ROCK, blockName);

        this.oreName = oreName;

        setHardness(3f);
        setResistance(5f);
    }

    @Override
    public TMOSOre setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    @Override
    public void initOreDict() {
        OreDictionary.registerOre(oreName, this);
    }
}