package com.deepwelldevelopment.tmos.core.mod;

import com.deepwelldevelopment.tmos.core.network.NetworkManager;
import com.deepwelldevelopment.tmos.core.network.PacketProcessor;
import com.deepwelldevelopment.tmos.core.network.ServerPacketHandler;

public class CommonProxy {

    public void registerRenderStuff() { }

    public void registerPacketHandler(String modId, String modChannel, PacketProcessor packetProcessor) {
        NetworkManager.getPacketChannel(modId).register(new ServerPacketHandler(modId, modChannel));
    }
}
