package com.deepwelldevelopment.tmos.transport.core.block;

import com.deepwelldevelopment.tmos.common.TMOS;
import com.deepwelldevelopment.tmos.common.block.TMOSBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TransportBlock extends TMOSBlock implements ITileEntityProvider {

    public TransportBlock(String name) {
        super(Material.PISTON, name);
        setCreativeTab(TMOS.tmosTab);
        setHardness(1F);
        setResistance(10F);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}