package com.deepwelldevelopment.tmos.core.transformer;

import com.deepwelldevelopment.tmos.core.init.TMOSLoadingPlugin;
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
    public static final String MD_SHAPED_RECIPE_OUTPUT = "";
    public static final String MD_CRAFTING_FIND_MATCHING = "net/minecraft/item/crafting/CraftingManager/findMatchingRecipe (Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/world/World;)Lnet/minecraft/item/ItemStack;";

    public static final String MD_TMOS_SHAPED_MATCHES ="com/deepwelldevelopment/tmos/core/research/ResearchManger/doesPlayerHaveCraftingRequisite (Lnet/minecraft/item/ItemStack;)Z";

    public static final String FD_ITEMSTACK_RECIPE_OUTPUT = "net/minecraft/item/crafting/ShapedRecipes/recipeOutput Lnet/minecraft/item/ItemStack;";

    public static final String FD_RETURN_VAL = "com/deepwelldevelopment/tmos/core/transformer/RecipeTransformer/ returnVal Z";

    public static final String CL_ITEMSTACK = "net/minecraft/item/ItemStack";

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
