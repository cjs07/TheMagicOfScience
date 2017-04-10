package com.deepwelldevelopment.tmos.client;

import com.deepwelldevelopment.tmos.client.gui.generator.GuiCoalGenerator;
import com.deepwelldevelopment.tmos.common.container.ContainerCoalGenerator;
import com.deepwelldevelopment.tmos.common.tile.generator.TileGeneratorCoal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);

        if (te instanceof TileGeneratorCoal) {
            return new ContainerCoalGenerator(player.inventory, (TileGeneratorCoal) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);

        if (te instanceof TileGeneratorCoal) {
            TileGeneratorCoal containerTileEntity = (TileGeneratorCoal)te;
            return new GuiCoalGenerator(player.inventory, containerTileEntity);
        }
        return null;
    }
}
