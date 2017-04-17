package com.deepwelldevelopment.tmos.lib.tile;

import com.deepwelldevelopment.tmos.lib.tile.packet.PacketTileBase;

/**
 *  Implement this interface on Tile entities that can transmit packets
 *  Tile Entities that implement this interface can both send and receive packets, however they do not handle to packets
 *  outside of sending/receiving
 *  A Tile Entity may implement both IPacketCreator and IPacketReceiver, but should not also implement IPacketTransmitter
 * @param <P> The type of packet that this Tile Entity can handle
 */
public interface IPacketTransmitter<P extends PacketTileBase> {

    public void sendToReciever(IPacketReceiver reciever, P packet);

    public void sendToTransmitter(IPacketTransmitter transmitter, P packet);

    public void recieveFromTransmitter(P packet);
}
