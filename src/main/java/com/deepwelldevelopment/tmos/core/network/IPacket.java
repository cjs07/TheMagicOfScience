package com.deepwelldevelopment.tmos.core.network;

import com.deepwelldevelopment.tmos.core.util.tuplet.Tuple;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.network.INetHandler;

import java.io.IOException;

public interface IPacket {
    /**
     * This is called when the packet is being received. Use this to read data from the packet.
     *
     * @param stream  The stream to read data from.
     * @param rawData The raw data buffer. ONLY refer to this if you're using it directly
     * @param handler An instance of the INetHandler interface.
     * @throws java.io.IOException
     */
    void process(ByteBufInputStream stream, ByteBuf rawData, INetHandler handler) throws IOException;

    /**
     * This is called when the packet is getting prepared for being send. Use this to write data to the packet.
     *
     * @param stream    The stream to write data to.
     * @param dataTuple A tuple containing the data provided by the packet sending call.
     * @throws java.io.IOException
     */
    void writeData(ByteBufOutputStream stream, Tuple dataTuple) throws IOException;
}
