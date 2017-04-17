package com.deepwelldevelopment.tmos.transport.core;

import com.deepwelldevelopment.tmos.lib.tile.packet.PacketTileBase;
import com.deepwelldevelopment.tmos.lib.tile.packet.PacketTileEnergy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class TileEnergyTransmitter extends TileTransportBase<PacketTileEnergy> {

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void receivePacket(PacketTileBase packet) {
        System.out.println("[TE COMM] TE at " + pos  + " has received a packet");
        if (packet instanceof PacketTileEnergy) {
            receivedPackets.add((PacketTileEnergy) packet);
        } else {
            System.out.println("[TE COMM] TE at " + pos + " has received an illegal packet. It will be destroyed");
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList tagList = compound.getTagList("packets", NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            storedPackets.add(new PacketTileEnergy(compound, world));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }

    @Override
    public void receiveReturnedPacket(PacketTileBase packet) {
        receivePacket(packet);
    }
}
