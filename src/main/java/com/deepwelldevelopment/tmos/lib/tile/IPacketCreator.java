package com.deepwelldevelopment.tmos.lib.tile;

import com.deepwelldevelopment.tmos.lib.tile.packet.PacketTileBase;

/**
 *  Implement this interface on Tile Entities that can create packets for communicating
 *  Tile Entities that implement this interface create packets and transmit them to an IPacketTransmitter
 *  but cannot receive or otherwise handle them
 *  A Tile Entity may implement both IPacketCreator and IPacketReceiver, but should not also implement IPacketTransmitter
 * @param <P> The type of packet that this Tile Entity creates
 */
public interface IPacketCreator<P extends PacketTileBase> {

    public P createPacket();
}
