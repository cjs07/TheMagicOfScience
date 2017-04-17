package com.deepwelldevelopment.tmos.transport.core;

import com.deepwelldevelopment.tmos.common.lib.util.ServerHelper;
import com.deepwelldevelopment.tmos.lib.tile.ITileCommunicator;
import com.deepwelldevelopment.tmos.lib.tile.TileCommunicatorBase;
import com.deepwelldevelopment.tmos.lib.tile.packet.PacketTileBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public abstract class TileTransportBase<P extends PacketTileBase> extends TileCommunicatorBase implements ITileCommunicator {

    ArrayList<P> storedPackets = new ArrayList<>();
    ArrayList<P> receivedPackets = new ArrayList<>();
    ArrayList<P> toRemove = new ArrayList<>();
    TileCommunicatorBase[] surroundingTiles = new TileCommunicatorBase[6];

    @Override
    public void update() {
        if (ServerHelper.isServerWorld(world)) {
            for (P packet : storedPackets) {
                packet.tick();
                if (packet.getTicksUntilTransmission() == 0) {
                    for (ITileCommunicator dest : surroundingTiles) {
                        if (dest != null) {
                            if (dest.equals(packet.getLastTile())) { //don't send packets back to the tile it just came from
                                continue;
                            }
                            sendPacket(dest, packet);
                            toRemove.add(packet);
                            break;
                        }
                    }
                }
            }
            storedPackets.removeAll(toRemove);
            toRemove.clear();
            storedPackets.addAll(receivedPackets);
            receivedPackets.clear();
        }
    }

    public void onAdded() {
        updateSurroundingTiles();
    }

    public void onNeighborChanged() {
        updateSurroundingTiles();
    }

    private void updateSurroundingTiles() {
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighborPos = pos.offset(side);
            if (world.getTileEntity(neighborPos) != null && world.getTileEntity(neighborPos) instanceof TileCommunicatorBase) {
                surroundingTiles[side.ordinal()] = (TileCommunicatorBase) world.getTileEntity(neighborPos);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        updateSurroundingTiles();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < storedPackets.size(); i++) {
            P packet = storedPackets.remove(i);
            NBTTagCompound packetCompound = new NBTTagCompound();
            packet.writeToNBT(packetCompound);
            tagList.appendTag(packetCompound);
        }
        compound.setTag("packets", tagList);

        return compound;
    }

    @Override
    public void sendPacket(ITileCommunicator dest, PacketTileBase packet) {
        packet.onTransmission(this, 20);
        dest.receivePacket(packet);
    }
}
