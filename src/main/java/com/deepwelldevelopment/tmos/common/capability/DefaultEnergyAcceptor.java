package com.deepwelldevelopment.tmos.common.capability;

import cofh.api.energy.IEnergyReceiver;
import com.deepwelldevelopment.tmos.common.capability.DefaultStorageHelper.DefaultStorage;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class DefaultEnergyAcceptor implements IEnergyReceiver {
    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return false;
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
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

    public static void register() {
        CapabilityManager.INSTANCE.register(IEnergyReceiver.class, new DefaultStorage<>(), DefaultEnergyAcceptor.class);
    }
}
