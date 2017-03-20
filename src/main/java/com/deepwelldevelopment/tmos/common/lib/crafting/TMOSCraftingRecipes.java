package com.deepwelldevelopment.tmos.common.lib.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TMOSCraftingRecipes {

    public static void init() {
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.BRICK_BLOCK), "XX ", "XX ", 'X', Blocks.PLANKS);
    }
}
