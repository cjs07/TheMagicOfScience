package com.deepwelldevelopment.tmos.common.capability;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class Capabilities {

    @CapabilityInject(IEnergyStorage.class)
    public static Capability<IEnergyStorage> ENERGY_STORAGE_CAPABILITY = null;

    @CapabilityInject(IEnergyReceiver.class)
    public static Capability<IEnergyReceiver> ENERGY_ACCEPTOR_CAPABILITY = null;

    @CapabilityInject(IEnergyProvider.class)
    public static Capability<IEnergyProvider> ENERGY_OUTPUTTER_CAPABILITY = null;

    public static void registerCapabilities() {
        DefaultEnergyStorage.register();
        DefaultEnergyAcceptor.register();
        DefaultCableOutputter.register();
    }
}
