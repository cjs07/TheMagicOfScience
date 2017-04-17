package com.deepwelldevelopment.tmos.lib.tile;

import com.deepwelldevelopment.tmos.lib.tile.packet.PacketTileBase;

public interface ITileCommunicator {

    public void sendPacket(ITileCommunicator dest, PacketTileBase packet);

    public void receivePacket(PacketTileBase packet);

    public void receiveReturnedPacket(PacketTileBase packet);

}
