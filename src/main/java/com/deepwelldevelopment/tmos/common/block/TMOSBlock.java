package com.deepwelldevelopment.tmos.common.block;

import com.deepwelldevelopment.tmos.common.TMOS;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TMOSBlock extends Block{
    public ImmutableSet<IBlockState> states;

    protected String name;

    public TMOSBlock(Material material, String name) {
        super(material);

        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(TMOS.tmosTab);
    }

    public void registerItemModel(ItemBlock itemBlock) {
        TMOS.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    @Override
    public TMOSBlock setCreativeTab(CreativeTabs tab)  {
        super.setCreativeTab(tab);
        return this;
    }

    @SideOnly(value= Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        if (this.hasProperties()) {
            for (IBlockState state : this.states) {
                list.add(new ItemStack(item, 1, this.getMetaFromState(state)));
            }
        } else {
            list.add(new ItemStack(item, 1, 0));
        }
    }

    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }

    public IProperty[] getProperties() {
        return null;
    }

    public boolean hasProperties() {
        return this.getProperties() != null;
    }

    public String getStateName(IBlockState state, boolean fullName) {
        String unlocalizedName = state.getBlock().getUnlocalizedName();
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    public boolean defineVariantsForItemBlock() {
        return false;
    }
}
