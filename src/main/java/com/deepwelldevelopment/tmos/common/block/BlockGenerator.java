package com.deepwelldevelopment.tmos.common.block;

import com.deepwelldevelopment.tmos.common.TMOS;
import com.deepwelldevelopment.tmos.common.tile.generator.TileGeneratorCoal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

public class BlockGenerator extends TMOSBlock implements ITileEntityProvider {

    public static enum Types {
        COAL
    }

    public BlockGenerator(String name) {
        super(Material.ANVIL, name);

        GameRegistry.registerTileEntity(TileGeneratorCoal.class, TMOS.modId  + "_generatorcoal");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
//        if (meta >= Types.values().length) {
//            return null;
//        }
//        switch (Types.values()[meta]) {
//            case COAL:
//                return new TileGeneratorCoal();
//            default:
//                return null;
//        }
        return new TileGeneratorCoal();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof TileGeneratorCoal)) {
            return false;
        }
        playerIn.openGui(TMOS.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}
