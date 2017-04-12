package com.deepwelldevelopment.tmos.transport.core.tile;

import com.deepwelldevelopment.tmos.api.lib.CapabilityUtil;
import com.deepwelldevelopment.tmos.common.capability.Capabilities;
import com.deepwelldevelopment.tmos.common.lib.util.ServerHelper;
import com.deepwelldevelopment.tmos.common.tile.TileTMOSBase;
import com.deepwelldevelopment.tmos.transport.core.block.TransportBlockState.TransmitterType;
import com.deepwelldevelopment.tmos.transport.core.transmitter.IBlockableConnection;
import com.deepwelldevelopment.tmos.transport.core.transmitter.ITransmitter;
import com.deepwelldevelopment.tmos.transport.core.transmitter.TransmissionType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public abstract class TileTransportBase extends TileTMOSBase implements IBlockableConnection, ITransmitter {

    int delayTicks;

    @Override
    public void update() {
        if (ServerHelper.isClientWorld(world)) {
            if (delayTicks == 5) {
                delayTicks = 6;
                refreshConnections();
            } else if (delayTicks < 5) {
                delayTicks++;
            }
        } else {
            refreshConnections();
        }
    }

    public byte getPossibleTransmitterConnections() {
        byte connections = 0x00;
        for (EnumFacing side : EnumFacing.values()) {
            if (canConnectMutual(side)) {
                TileEntity tile = world.getTileEntity(pos.offset(side));
                if (tile != null && CapabilityUtil.hasCapability(tile, Capabilities.GRID_TRANSMITTER_CAPABILITY, side.getOpposite())
                        && TransmissionType.checkTransmissionType(CapabilityUtil.getCapability(tile, Capabilities.GRID_TRANSMITTER_CAPABILITY, side.getOpposite()), getTransmitterType().getTransmission())
                        && isValidTransmitter(tile)) {
                    connections |= 1 << side.ordinal();
                }
            }
        }
        return connections;
    }

    public boolean isValidTransmitter(TileEntity tileEntity) {
        return true;
    }

    public abstract TransmitterType getTransmitterType();

    public void refreshConnections() {
        if (ServerHelper.isServerWorld(world)) {
            byte possibleTransmitters = getPossibleTransmitterConnections();
        }
    }

    @Override
    public boolean canConnectMutual(EnumFacing side) {
        return false;
    }

    @Override
    public boolean canConnect(EnumFacing side) {
        return false;
    }

    @Override
    public TransmissionType getTransmissionType() {
        return null;
    }
}
