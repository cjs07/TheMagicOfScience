package com.deepwelldevelopment.tmos.core.asm;

import com.deepwelldevelopment.tmos.core.util.tuplet.Triplet;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ASMNames {

    private static final Map<String, String> MAPPINGS = new HashMap<>();

    static final Pattern OWNERNAME = Pattern.compile("(\\S*)/(.*)");

    public static final String MD_SHAPED_MATCHES = "net/minecraft/item/crafting/ShapedRecipes/matches (Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/world/World;)Z";
    public static final String MD_SHAPELESS_MATCHES = "net/minecraft/item/crafting/ShapelessRecipes/matches (Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/world/World;)Z";
    public static final String MD_MATCHES_GENERIC = "/matches (Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/world/World;)Z";
    public static final String MD_SHAPED_RECIPE_OUTPUT = "";
    public static final String MD_CRAFTING_FIND_MATCHING = "net/minecraft/item/crafting/CraftingManager/findMatchingRecipe (Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/world/World;)Lnet/minecraft/item/ItemStack;";

    public static final String MD_TMOS_SHAPED_MATCHES = "net/minecraft/item/crafting/ShapedRecipes/_TMOS_matches (Lnet/minecraft/item/ItemStack;)Z";
    public static final String MD_TMOS_SHAPED_MATCHES_EX = "com/deepwelldevelopment/tmos/core/research/ResearchManager/doesPlayerHaveCraftingRequisite (Lnet/minecraft/item/ItemStack;)Z";


    public static final String FD_ITEMSTACK_RECIPE_OUTPUT_SHAPED = "net/minecraft/item/crafting/ShapedRecipes/recipeOutput Lnet/minecraft/item/ItemStack;";
    public static final String FD_ITEMSTACK_RECIPE_OUTPUT_SHAPELESS = "net/minecraft/item/crafting/ShapelessRecipes/recipeOutput Lnet/minecraft/item/ItemStack;";


    public static final String CL_ITEMSTACK = "net/minecraft/item/ItemStack";
    public static final String CL_BOOK_CLONING_RECIPE = "net.minecraft.item.crafting.RecipeBookCloning";
    public static final String CL_FIREWORKS_RECIPE = "net.minecraft.item.crafting.RecipeFireworks";
    public static final String CL_REPAIR_ITEM_RECIPE = "net.minecraft.item.crafting.RecipeRepairItem";
    public static final String CL_ARMOR_DYE_RECIPE = "net.minecraft.item.crafting.RecipeArmorDyes";
    public static final String CL_MAP_CLONING_RECIPE = "net.minecraft.item.crafting.RecipeMapCloning";
    public static final String CL_TIPPED_ARROW_RECIPE = "net.minecraft.item.crafting.RecipeTippedArrow";
    public static final String CL_SHAPED_RECIPE = "net.minecraft.item.crafting.ShapedRecipes";
    public static final String CL_SHAPELESS_RECIPE = "net.minecraft.item.crafting.ShapelessRecipes";

    public static final String CL_TMOS_RESEARCH_MANAGER = "com/deepwelldevelopment/tmos/core/research/ResearchManger";

    static {
        MAPPINGS.put(MD_SHAPED_MATCHES, "func_77569_a");
    }

    public static Triplet<String, String, String[]> getSrgNameMd(String method) {
        Matcher mtch = OWNERNAME.matcher(method);
        if( !mtch.find() ) {
            TMOSLoadingPlugin.MOD_LOG.log(Level.FATAL, "Method string does not match pattern!");
            throw new RuntimeException("SRG-Name not found!");
        }

        String srgName = ASMHelper.isMCP ? null : MAPPINGS.get(method);

        String owner = mtch.group(1);
        String[] splitMd = mtch.group(2).split(" ");

        String name = srgName == null ? splitMd[0] : srgName;
        String[] additData = Arrays.copyOfRange(splitMd, 1, splitMd.length);

        return Triplet.with(owner, name, additData);
    }

    public static Triplet<String, String, String> getSrgNameFd(String field) {
        Matcher mtch = OWNERNAME.matcher(field);
        if( !mtch.find() ) {
            TMOSLoadingPlugin.MOD_LOG.log(Level.FATAL, "Field string does not match pattern!");
            throw new RuntimeException("SRG-Name not found!");
        }

        String srgName = ASMHelper.isMCP ? null : MAPPINGS.get(field);

        String owner = mtch.group(1);
        String[] splitFd = mtch.group(2).split(" ");

        String name = srgName == null ? splitFd[0] : srgName;
        String desc = splitFd[1];

        return Triplet.with(owner, name, desc);
    }
}
