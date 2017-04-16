package com.deepwelldevelopment.tmos.common.capability;

import cofh.api.energy.IEnergyStorage;
import com.deepwelldevelopment.tmos.common.capability.DefaultStorageHelper.DefaultStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class DefaultEnergyStorage implements IEnergyStorage {
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return 0;
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IEnergyStorage.class, new DefaultStorage<>(), DefaultEnergyStorage.class);
    }
}
