package com.deepwelldevelopment.tmos.common.lib.network;

import com.deepwelldevelopment.tmos.common.TMOS;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class TMOSPacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(TMOS.modId.toLowerCase());
}