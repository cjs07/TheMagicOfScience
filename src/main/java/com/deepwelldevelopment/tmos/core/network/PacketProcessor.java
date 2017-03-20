package com.deepwelldevelopment.tmos.core.network;

import com.deepwelldevelopment.tmos.core.init.TMOSLoadingPlugin;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import net.minecraft.network.INetHandler;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class PacketProcessor {

    private final String modId;
    private final Map<Short, Class<? extends IPacket>> packetMap = Maps.newHashMap();

    public PacketProcessor(String modId) {
        this.modId = modId;
    }

    public void processPacket(ByteBuf data, INetHandler handler) {
        short packetId = -1;
        try( ByteBufInputStream bbis = new ByteBufInputStream(data) ) {
            packetId = bbis.readShort();
            if( this.packetMap.containsKey(packetId) ) {
                this.packetMap.get(packetId).getConstructor().newInstance().process(bbis, data, handler);
            }
        } catch( IOException ioe ) {
            TMOSLoadingPlugin.MOD_LOG.log(Level.ERROR, "[TMOS] The packet with the ID %d from %s cannot be processed!", packetId, this.modId);
            ioe.printStackTrace();
        } catch( IllegalAccessException | InstantiationException rex ) {
            TMOSLoadingPlugin.MOD_LOG.log(Level.ERROR, "[TMOS] The packet with the ID %d from %s cannot be instantiated!", packetId, this.modId);
            rex.printStackTrace();
        } catch( NoSuchMethodException | InvocationTargetException e ) {
            e.printStackTrace();
        }
    }

    public Class<? extends IPacket> getPacketCls(short packetId) {
        return this.packetMap.get(packetId);
    }

    public void addPacketCls(int packetId, Class<? extends IPacket> packetCls) {
        this.packetMap.put((short) packetId, packetCls);
    }
}
