package com.deepwelldevelopment.tmos.common.capability;

import com.deepwelldevelopment.tmos.transport.core.transmitter.IGridTransmitter;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class Capabilities {

    @CapabilityInject(IGridTransmitter.class)
    public static Capability<IGridTransmitter> GRID_TRANSMITTER_CAPABILITY = null;

    public static void registerCapabilities() {
        DefaultGridTransmitter.register();
    }
}
