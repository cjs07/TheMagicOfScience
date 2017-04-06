package com.deepwelldevelopment.tmos.common.config;

import com.deepwelldevelopment.tmos.common.block.BlockGenerator;
import com.deepwelldevelopment.tmos.common.block.BlockWorkbench;
import com.deepwelldevelopment.tmos.common.block.TMOSOre;
import com.deepwelldevelopment.tmos.common.item.ItemOreDict;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.deepwelldevelopment.tmos.api.blocks.TMOSBlocks.*;

public class ConfigBlocks {

    public static void init() {
        ConfigBlocks.initializeBlocks();
    }

    private static void initializeBlocks() {
        oreCopper = registerBlock(new TMOSOre("oreCopper", "oreCopper"));
        oreTin = registerBlock(new TMOSOre("oreTin", "oreTin"));
        oreAluminum = registerBlock(new TMOSOre("oreAluminum", "oreAluminum"));
        oreSilver = registerBlock(new TMOSOre("oreSilver", "oreSilver"));
        oreLead = registerBlock(new TMOSOre("oreLead", "oreLead"));
        oreNickel = registerBlock(new TMOSOre("oreNickel", "oreNickel"));
        orePlatinum = registerBlock(new TMOSOre("orePlatinum", "orePlatinum"));
        oreIridium = registerBlock(new TMOSOre("oreIridium", "oreIridium"));
        oreChromium = registerBlock(new TMOSOre("oreChromium", "oreChromium"));

        oreCopper.setHarvestLevel("pickaxe", Item.ToolMaterial.STONE.getHarvestLevel());
        oreTin.setHarvestLevel("pickaxe", Item.ToolMaterial.STONE.getHarvestLevel());
        oreAluminum.setHarvestLevel("pickaxe", Item.ToolMaterial.STONE.getHarvestLevel());
        oreLead.setHarvestLevel("pickaxe", Item.ToolMaterial.IRON.getHarvestLevel());
        oreNickel.setHarvestLevel("pickaxe", Item.ToolMaterial.IRON.getHarvestLevel());
        orePlatinum.setHarvestLevel("pickaxe", Item.ToolMaterial.IRON.getHarvestLevel());
        oreIridium.setHarvestLevel("pickaxe", Item.ToolMaterial.DIAMOND.getHarvestLevel());
        oreChromium.setHarvestLevel("pickaxe", Item.ToolMaterial.DIAMOND.getHarvestLevel());

        tmosWorkbench = registerBlock(new BlockWorkbench());
        coalGenerator = registerBlock(new BlockGenerator("generatorCoal"));
    }

    private static <T extends Block>T registerBlock(T block, ItemBlock itemBlock) {
        GameRegistry.register(block);
        GameRegistry.register(itemBlock);

        if (block instanceof TMOSOre) {
            ((TMOSOre)block).registerItemModel(itemBlock);
        }
        if (block instanceof ItemOreDict) {
            ((ItemOreDict)block).initOreDict();
        }
        if (itemBlock instanceof ItemOreDict) {
            ((ItemOreDict)itemBlock).initOreDict();
        }

        return block;
    }

    private static <T extends Block> T registerBlock(T block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        return registerBlock(block, itemBlock);
    }
}
