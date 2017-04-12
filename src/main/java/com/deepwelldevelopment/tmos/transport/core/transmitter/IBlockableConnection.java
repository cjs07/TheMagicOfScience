package com.deepwelldevelopment.tmos.transport.core.transmitter;

import net.minecraft.util.EnumFacing;

public interface IBlockableConnection {
    public boolean canConnectMutual(EnumFacing side);

    public boolean canConnect(EnumFacing side);
}
