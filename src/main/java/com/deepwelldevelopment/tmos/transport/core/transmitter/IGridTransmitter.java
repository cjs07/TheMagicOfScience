package com.deepwelldevelopment.tmos.transport.core.transmitter;

import com.deepwelldevelopment.tmos.lib.util.Coord4D;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.Collection;

public interface IGridTransmitter<A, N extends DynamicNetwork<A, N>> extends ITransmitter {
    public boolean hasTransmitterNetwork();

    /**
     * Gets the network currently in use by this transmitter segment.
     *
     * @return network this transmitter is using
     */
    public N getTransmitterNetwork();

    /**
     * Sets this transmitter segment's network to a new value.
     *
     * @param network - network to set to
     */
    public void setTransmitterNetwork(N network);

    public void setRequestsUpdate();

    public int getTransmitterNetworkSize();

    public int getTransmitterNetworkAcceptorSize();

    public double getTransmitterNetworkCapacity();

    public int getCapacity();

    public World world();

    public Coord4D coord();

    public Coord4D getAdjacentConnectableTransmitterCoord(EnumFacing side);

    public A getAcceptor(EnumFacing side);

    public boolean isValid();

    public boolean isOrphan();

    public void setOrphan(boolean orphaned);

    public N createEmptyNetwork();

    public N mergeNetworks(Collection<N> toMerge);

    public N getExternalNetwork(Coord4D from);

    public void takeShare();

    public void updateShare();

    public Object getBuffer();
}