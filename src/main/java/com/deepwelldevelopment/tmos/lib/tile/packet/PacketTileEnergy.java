package com.deepwelldevelopment.tmos.lib.tile.packet;

import com.deepwelldevelopment.tmos.api.energy.EnergyStack;
import com.deepwelldevelopment.tmos.lib.tile.TileCommunicatorBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PacketTileEnergy extends PacketTileBase {

    EnergyStack energyStack;

    public PacketTileEnergy(EnergyStack energyStack, TileCommunicatorBase creator) {
        super(creator);
        this.energyStack = energyStack;
    }

    public PacketTileEnergy(NBTTagCompound compound, World world) {
        super(compound, world);
        energyStack = new EnergyStack(compound.getInteger("energy"));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
    }
}
