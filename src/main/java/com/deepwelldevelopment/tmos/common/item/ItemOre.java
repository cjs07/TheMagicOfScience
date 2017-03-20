package com.deepwelldevelopment.tmos.common.item;

import net.minecraftforge.oredict.OreDictionary;

public class ItemOre extends TMOSItem implements ItemOreDict {

    protected String oreName;

    public ItemOre(String itemName, String oreName) {
        super(itemName);

        this.oreName = oreName;
    }

    @Override
    public void initOreDict() {
        OreDictionary.registerOre(name, this);
    }
}
