package com.deepwelldevelopment.tmos.core.network;

import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ServerPacketHandler {

    private final String channel;
    private final String modId;

    public ServerPacketHandler(String modId, String modChannel) {
        this.channel = modChannel;
        this.modId = modId;
    }

    @SubscribeEvent
    public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event) {
        NetHandlerPlayServer netHandlerPlayServer = (NetHandlerPlayServer) event.getHandler();

        if( event.getPacket().channel().equals(this.channel) ) {
            NetworkManager.getPacketProcessor(this.modId).processPacket(event.getPacket().payload(), netHandlerPlayServer);
        }
    }
}
