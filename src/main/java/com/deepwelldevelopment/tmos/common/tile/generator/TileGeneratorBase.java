package com.deepwelldevelopment.tmos.common.tile.generator;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyProvider;
import com.deepwelldevelopment.tmos.common.lib.util.ServerHelper;
import com.deepwelldevelopment.tmos.common.lib.util.TimeTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;


public abstract class TileGeneratorBase extends TileEntity implements IEnergyProvider, ITickable {

    public static final int SIZE = 2;

    protected EnergyStorage energyStorage = new EnergyStorage(0);
    protected TimeTracker tracker = new TimeTracker();

    int fuelRF;
    int fuelTicks;
    int rfPerTick;

    private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
            TileGeneratorBase.this.markDirty();
        }
    };

    public TileGeneratorBase(int maxEnergy, int maxPower) {
        energyStorage = new EnergyStorage(maxEnergy, maxPower * 2);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        energyStorage.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        energyStorage.writeToNBT(nbt);

        return nbt;
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemStackHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        if (ServerHelper.isServerWorld(world)) {


            markDirty();
        }
    }

    public int getField(int id) {
        switch (id) {
            default:
                return 0;
        }
    }

    public void setField(int id, int value) {
        switch (id) {
        }
    }

//    /* NETWORK METHODS */
//    @Override
//    public PacketTMOSBase getPacket() {
//
//        PacketCoFHBase payload = super.getPacket();
//
//        payload.addByte(facing);
//        payload.addBool(augmentRedstoneControl);
//
//        return payload;
//    }
//
//    @Override
//    public PacketTMOSBase getGuiPacket() {
//
//        PacketTMOSBase payload = super.getGuiPacket();
//
//        payload.addInt(energyStorage.getMaxEnergyStored());
//        payload.addInt(energyStorage.getEnergyStored());
//        payload.addInt(fuelRF);
//
//        payload.addBool(augmentRedstoneControl);
//
//        return payload;
//    }
//
//    @Override
//    protected void handleGuiPacket(PacketTMOSBase payload) {
//        super.handleGuiPacket(payload);
//
//        energyStorage.setCapacity(payload.getInt());
//        energyStorage.setEnergyStored(payload.getInt());
//        fuelRF = payload.getInt();
//
//        boolean prevControl = augmentRedstoneControl;
//        augmentRedstoneControl = payload.getBool();
//
//        if (augmentRedstoneControl != prevControl) {
//            onInstalled();
//            sendUpdatePacket(Side.SERVER);
//        }
//    }

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
