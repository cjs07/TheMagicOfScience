package com.deepwelldevelopment.tmos.transport.core;

import com.deepwelldevelopment.tmos.api.energy.EnergyStack;
import com.deepwelldevelopment.tmos.common.TMOS;
import com.deepwelldevelopment.tmos.common.block.TMOSBlock;
import com.deepwelldevelopment.tmos.common.lib.util.ServerHelper;
import com.deepwelldevelopment.tmos.lib.tile.TileCommunicatorBase;
import com.deepwelldevelopment.tmos.lib.tile.packet.PacketTileEnergy;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTransport extends TMOSBlock implements ITileEntityProvider {

    public BlockTransport() {
        super(Material.PISTON, "cable");
        setCreativeTab(TMOS.tmosTab);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (ServerHelper.isServerWorld(worldIn)) {
            if (worldIn.getTileEntity(pos) instanceof TileCommunicatorBase) {
                TileCommunicatorBase tile = (TileCommunicatorBase) worldIn.getTileEntity(pos);
                System.out.println("[TE COMM] TE at " + pos + " is creating a packet");
                tile.sendPacket(tile, new PacketTileEnergy(new EnergyStack(0), tile));
                return true;
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity te= worldIn.getTileEntity(pos);
        if (te instanceof TileTransportBase) {
            TileTransportBase tile = ((TileTransportBase) te);
            tile.onAdded();
        }
        for (EnumFacing side : EnumFacing.values()) {
            if (worldIn.getTileEntity(pos.offset(side)) instanceof TileTransportBase) {
                ((TileTransportBase) worldIn.getTileEntity(pos.offset(side))).onNeighborChanged();
            }
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        TileTransportBase tile = (TileTransportBase) world.getTileEntity(pos);
        if (tile != null) {
            tile.onNeighborChanged();
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEnergyTransmitter();
    }
}
