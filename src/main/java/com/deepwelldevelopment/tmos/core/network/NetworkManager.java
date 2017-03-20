package com.deepwelldevelopment.tmos.core.network;

import com.deepwelldevelopment.tmos.core.init.TMOSLoadingPlugin;
import com.deepwelldevelopment.tmos.core.mod.TMOSCoreDummyContainer;
import com.deepwelldevelopment.tmos.core.util.tuplet.Quintet;
import com.deepwelldevelopment.tmos.core.util.tuplet.Triplet;
import com.deepwelldevelopment.tmos.core.util.tuplet.Tuple;
import com.deepwelldevelopment.tmos.core.util.tuplet.Unit;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class NetworkManager {

    private static final Map<String, Triplet<String, PacketProcessor, FMLEventChannel>> PROCESSORS = Maps.newHashMap();

    public static void registerModHandler(String modId, String modChannel) {
        PROCESSORS.put(modId, Triplet.with(modChannel, new PacketProcessor(modId), NetworkRegistry.INSTANCE.newEventDrivenChannel(modChannel)));
    }

    public static void registerModPacketCls(String modId, int packetId, Class<? extends IPacket> packetCls) {
        PROCESSORS.get(modId).getValue1().addPacketCls(packetId, packetCls);
    }

    public static void initPacketHandler() {
        for( Map.Entry<String, Triplet<String, PacketProcessor, FMLEventChannel>> processor : PROCESSORS.entrySet() ) {
            TMOSCoreDummyContainer.proxy.registerPacketHandler(processor.getKey(), processor.getValue().getValue0(), processor.getValue().getValue1());
        }
    }

    public static void sendToServer(String modId, short packet, Tuple data) {
        sendPacketTo(modId, packet, EnumPacketDirections.TO_SERVER, null, data);
    }

    public static void sendToAll(String modId, short packed, Tuple data) {
        sendPacketTo(modId, packed, EnumPacketDirections.TO_ALL, null, data);
    }

    public static void sendToPlayer(String modId, short packed, EntityPlayerMP player, Tuple data) {
        sendPacketTo(modId, packed, EnumPacketDirections.TO_PLAYER, Unit.with(player), data);
    }

    public static void sendToAllInDimension(String modId, short packed, int dimensionId, Tuple data) {
        sendPacketTo(modId, packed, EnumPacketDirections.TO_ALL_IN_DIMENSION, Unit.with(dimensionId), data);
    }

    public static void sendToAllAround(String modId, short packed, int dimensionId, double x, double y, double z, double range, Tuple data) {
        sendPacketTo(modId, packed, EnumPacketDirections.TO_ALL_IN_RANGE, Quintet.with(dimensionId, x, y, z, range), data);
    }

    private static void sendPacketTo(String modId, short packet, EnumPacketDirections direction, Tuple dirData, Tuple packetData) {
        try( ByteBufOutputStream bbos = new ByteBufOutputStream(Unpooled.buffer()) ) {
            bbos.writeShort(packet);
            IPacket pktInst = getPacketProcessor(modId).getPacketCls(packet).getConstructor().newInstance();
            FMLEventChannel channel = getPacketChannel(modId);
            pktInst.writeData(bbos, packetData);
            FMLProxyPacket proxyPacket = new FMLProxyPacket((PacketBuffer) bbos.buffer(), getPacketChannelName(modId));
            switch( direction ) {
                case TO_SERVER:
                    channel.sendToServer(proxyPacket);
                    break;
                case TO_ALL:
                    channel.sendToAll(proxyPacket);
                    break;
                case TO_PLAYER:
                    channel.sendTo(proxyPacket, (EntityPlayerMP) dirData.getValue(0));
                    break;
                case TO_ALL_IN_RANGE:
                    channel.sendToAllAround(proxyPacket, new NetworkRegistry.TargetPoint((int) dirData.getValue(0), (double) dirData.getValue(1),
                            (double) dirData.getValue(2), (double) dirData.getValue(3),
                            (double) dirData.getValue(4)));
                    break;
                case TO_ALL_IN_DIMENSION:
                    channel.sendToDimension(proxyPacket, (int) dirData.getValue(0));
                    break;
            }
        } catch( IOException ioe ) {
            TMOSLoadingPlugin.MOD_LOG.log(Level.ERROR, "The packet ID %d from %s cannot be processed!", packet, modId);
            ioe.printStackTrace();
        } catch( IllegalAccessException | InstantiationException rex ) {
            TMOSLoadingPlugin.MOD_LOG.log(Level.ERROR, "The packet ID %d from %s cannot be instantiated!", packet, modId);
            rex.printStackTrace();
        } catch( NoSuchMethodException | InvocationTargetException e ) {
            e.printStackTrace();
        }
    }

    public static String getPacketChannelName(String modId) {
        return PROCESSORS.get(modId).getValue0();
    }

    public static PacketProcessor getPacketProcessor(String modId) {
        return PROCESSORS.get(modId).getValue1();
    }

    public static FMLEventChannel getPacketChannel(String modId) {
        return PROCESSORS.get(modId).getValue2();
    }

    private enum EnumPacketDirections
    {
        TO_SERVER, TO_PLAYER, TO_ALL, TO_ALL_IN_RANGE, TO_ALL_IN_DIMENSION
    }
}
