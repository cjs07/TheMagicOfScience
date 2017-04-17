package com.deepwelldevelopment.tmos.lib.tile.packet;

import com.deepwelldevelopment.tmos.lib.tile.ITileCommunicator;
import com.deepwelldevelopment.tmos.lib.tile.TileCommunicatorBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Basically this class just exists to act as a common superclass for the packets
 * used in the Tile Entity Communications Library
 */
public abstract class PacketTileBase {

    TileCommunicatorBase creator;
    TileCommunicatorBase lastTile;

    private int ticksUntilTransmission;

    public PacketTileBase(TileCommunicatorBase creator) {
        this.creator =  creator;
    }

    public PacketTileBase(NBTTagCompound compound, World world) {
        BlockPos creatorPos = new BlockPos(compound.getInteger("creatorX"), compound.getInteger("creatorY"), compound.getInteger("creatorZ"));
        BlockPos lastPos = new BlockPos(compound.getInteger("lastX"), compound.getInteger("lastY"), compound.getInteger("lastZ"));
        ticksUntilTransmission = compound.getInteger("ticksUntilTransmission");
        creator = (TileCommunicatorBase) world.getTileEntity(creatorPos);
        lastTile = (TileCommunicatorBase) world.getTileEntity(lastPos);
    }

    /**
     * Called every tick that it is in "stored" (waiting to be transmitted) in a Tile Entity
     * This is a delegation method that allows each packet to independently keep its own transmission timer
     * as well as enables packets to do special things for every tick hey are in the transmission process (i.e. energy decay)
     */
    public void tick() {
        ticksUntilTransmission--;
    }

    /**
     * Called whenever the packet is transmitted from one Tile Entity to another
     */
    public void onTransmission(TileCommunicatorBase lastTile, int next) {
        this.lastTile = lastTile;
        ticksUntilTransmission = next;
    }

    public void readFromNBT(NBTTagCompound compound) {
        ticksUntilTransmission = compound.getInteger("ticksUntilTransmission");
    }

    public void writeToNBT(NBTTagCompound compound) {
        compound.setInteger("ticksUntilTransmission", ticksUntilTransmission);
        BlockPos creatorPos = creator.getPos();
        BlockPos lastPos = lastTile.getPos();
        compound.setInteger("creatorX", creatorPos.getX());
        compound.setInteger("creatorY", creatorPos.getY());
        compound.setInteger("creatorZ", creatorPos.getZ());
        compound.setInteger("lastX", creatorPos.getX());
        compound.setInteger("lastY", creatorPos.getY());
        compound.setInteger("lastZ", creatorPos.getZ());
    }

    /**
     * Return this packet to the Tile that created it.
     * Called when a packet reaches a dead end in transmission, or cannot be accepted anywhere
     * Tile Entities should handle this packet differently then a packet that is received normally (i.e. redispatch the packet rather than return its
     * contents to its inventory)
     */
    public void returnToSender() {
        creator.receiveReturnedPacket(this);
    }

    public ITileCommunicator getCreator() {
        return creator;
    }

    public ITileCommunicator getLastTile() {
        return lastTile;
    }

    public int getTicksUntilTransmission() {
        return ticksUntilTransmission;
    }

    public void setTicksUntilTransmission(int ticksUntilTransmission) {
        this.ticksUntilTransmission = ticksUntilTransmission;
    }
}
