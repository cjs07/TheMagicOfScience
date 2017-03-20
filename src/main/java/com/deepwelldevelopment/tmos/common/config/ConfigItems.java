package com.deepwelldevelopment.tmos.common.config;

import com.deepwelldevelopment.tmos.common.item.ItemOre;
import com.deepwelldevelopment.tmos.common.item.ItemOreDict;
import com.deepwelldevelopment.tmos.common.item.TMOSItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.deepwelldevelopment.tmos.api.items.TMOSItems.*;

public class ConfigItems {

    public static void preInit() {
        ConfigItems.initializeItems();
    }

    private static void initializeItems() {
        ingotCopper = register(new ItemOre("ingotCopper", "ingotCopper"));
        ingotTin = register(new ItemOre("ingotTin", "ingotTin"));
        ingotAluminum = register(new ItemOre("ingotAluminum", "ingotAluminum"));
        ingotLead = register(new ItemOre("ingotLead", "ingotLead"));
        ingotSilver = register(new ItemOre("ingotSilver", "ingotSilver"));
        ingotNickel = register(new ItemOre("ingotNickel", "ingotNickel"));
        ingotPlatinum = register(new ItemOre("ingotPlatinum", "ingotPlatinum"));
        ingotIridium = register(new ItemOre("ingotIridium", "ingotIridium"));
        ingotChromium = register(new ItemOre("ingotChromium", "ingotChromium"));
    }

    private static <T extends Item> T register(T item) {
        GameRegistry.register(item);

        if (item instanceof TMOSItem) {
            ((TMOSItem) item).registerItemModel();
        }
        if (item instanceof ItemOreDict) {
            ((ItemOreDict)item).initOreDict();
        }

        return item;
    }
}
