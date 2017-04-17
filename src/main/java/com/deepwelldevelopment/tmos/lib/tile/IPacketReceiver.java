package com.deepwelldevelopment.tmos.lib.tile;

import com.deepwelldevelopment.tmos.lib.tile.packet.PacketTileBase;

/**
 * Implement this interface on Tile Entities that can receive communication packets
 * Tile Entities that implement this interface can receive and handle packets, but cannot create or transmit them
 * A Tile Entity may implement both IPacketCreator and IPacketReceiver, but should not also implement IPacketTransmitter
 * @param <P>
 */
public interface IPacketReceiver<P extends PacketTileBase> {

    public void recievePacket(P packet);
}
