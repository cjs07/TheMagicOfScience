package com.deepwelldevelopment.tmos.common.capability;

import cofh.api.energy.IEnergyProvider;
import com.deepwelldevelopment.tmos.common.capability.DefaultStorageHelper.NullStorage;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class DefaultCableOutputter implements IEnergyProvider {

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

    public static void register() {
        CapabilityManager.INSTANCE.register(IEnergyProvider.class, new NullStorage<>(), DefaultCableOutputter.class);
    }
}
