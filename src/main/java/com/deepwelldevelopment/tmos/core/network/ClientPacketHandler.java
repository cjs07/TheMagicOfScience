package com.deepwelldevelopment.tmos.core.network;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ClientPacketHandler {
    private final String channel;
    private final String modId;

    public ClientPacketHandler(String modId, String modChannel) {
        this.channel = modChannel;
        this.modId = modId;
    }

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        NetHandlerPlayClient netHandlerPlayClient = (NetHandlerPlayClient) event.getHandler();

        if( event.getPacket().channel().equals(this.channel) ) {
            NetworkManager.getPacketProcessor(this.modId).processPacket(event.getPacket().payload(), netHandlerPlayClient);
        }
    }
}
