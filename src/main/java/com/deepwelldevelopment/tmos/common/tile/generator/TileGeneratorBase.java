package com.deepwelldevelopment.tmos.common.tile.generator;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyStorage;
import com.deepwelldevelopment.tmos.common.lib.util.ServerHelper;
import com.deepwelldevelopment.tmos.common.lib.util.TimeTracker;
import com.deepwelldevelopment.tmos.common.tile.TileTMOSBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;


public abstract class TileGeneratorBase extends TileTMOSBase implements IEnergyProvider, ITickable {

    protected EnergyStorage energyStorage = new EnergyStorage(0);
    protected TimeTracker tracker = new TimeTracker();

    int fuelRf;
    int fuelTicks;

    public TileGeneratorBase(int maxEnergy, int maxPower) {
        energyStorage = new EnergyStorage(maxEnergy, maxPower * 2);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        energyStorage.readFromNBT(nbt);
        fuelRf = nbt.getInteger("fuelRf");
        fuelTicks = nbt.getInteger("fuelTicks");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        energyStorage.writeToNBT(nbt);
        nbt.setInteger("fuelRf", fuelRf);
        nbt.setInteger("fuelTicks", fuelTicks);
        return nbt;
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public void update() {
        if (ServerHelper.isServerWorld(world)) {
            if(canGenerate()) {
                generate();
            }
        }
        markDirty();
    }

    public int getField(int id) {
        switch (id) {
            case 0:
                return fuelRf;
            case 1:
                return energyStorage.getEnergyStored();
            default:
                return 0;
        }
    }

    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.fuelRf = value;
                break;
            case 1:
                energyStorage.setEnergyStored(value);
                break;
        }
    }

    public int getScaledDuration(int scale) {
        return 0;
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public abstract boolean canGenerate();

    public abstract void generate();

    /* IEnergyProvider */
    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return false;
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return 0;
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 0;
    }
}
