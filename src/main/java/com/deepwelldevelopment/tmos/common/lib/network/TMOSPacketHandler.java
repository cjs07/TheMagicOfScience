package com.deepwelldevelopment.tmos.common.lib.network;

import com.deepwelldevelopment.tmos.common.TMOS;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.util.ArrayList;

public class TMOSPacketHandler {

    public final SimpleNetworkWrapper netHandler = NetworkRegistry.INSTANCE.newSimpleChannel(TMOS.modId.toLowerCase());

    public void init() {

    }

    public static EntityPlayer getPlayer(MessageContext context) {
        return TMOS.proxy.getPlayer(context);
    }

    public static void encode(Object[] data, ByteBuf buf) {
        for (Object o : data) {
            if (o instanceof Byte) {
                buf.writeByte((Byte) o);
            } else if (o instanceof Short) {
                buf.writeShort((Short) o);
            } else if (o instanceof Integer) {
                buf.writeInt((Integer) o);
            } else if (o instanceof Long) {
                buf.writeLong((Long) o);
            } else if (o instanceof Double) {
                buf.writeDouble((Double) o);
            } else if (o instanceof Float) {
                buf.writeFloat((Float) o);
            } else if (o instanceof Boolean) {
                buf.writeBoolean((Boolean) o);
            } else if (o instanceof String) {
                writeString((String) o, buf);
            } else if (o instanceof ItemStack) {
                writeItemStack((ItemStack) o, buf);
            } else if (o instanceof EnumFacing) {
                buf.writeInt(((EnumFacing) o).ordinal());
            } else if (o instanceof NBTTagCompound) {
                writeNBT((NBTTagCompound) o, buf);
            } else if (o instanceof byte[]) {
                for (byte b : (byte[]) o) {
                    buf.writeByte(b);
                }
            } else if (o instanceof int[]) {
                for (int i : (int[]) o) {
                    buf.writeInt(i);
                }
            } else if (o instanceof ArrayList) {
                encode(((ArrayList) o).toArray(), buf);
            }
        }
    }

    public static void writeString(String s, ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, s);
    }

    public static String readString(ByteBuf buf) {
        return ByteBufUtils.readUTF8String(buf);
    }

    public static void writeItemStack(ItemStack stack, ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, stack);
    }

    public static ItemStack readItemStack(ByteBuf buf) {
        return ByteBufUtils.readItemStack(buf);
    }

    public static void writeNBT(NBTTagCompound tag, ByteBuf buf) {
        ByteBufUtils.writeTag(buf, tag);
    }

    public static NBTTagCompound readNBT(ByteBuf buf) {
        return ByteBufUtils.readTag(buf);
    }

    public static void handlePacket(Runnable runnable, EntityPlayer player) {
        TMOS.proxy.handlePacket(runnable, player);
    }

    public void sendTo(IMessage message, EntityPlayerMP player) {
        netHandler.sendTo(message, player);
    }

    public void sendToAll(IMessage message) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

        for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
            sendTo(message, player);
        }
    }

    public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
        netHandler.sendToAllAround(message, point);
    }

    public void sendToDimension(IMessage message, int dimensionId) {
        netHandler.sendToDimension(message, dimensionId);
    }

    public void sendToServer(IMessage message) {
        netHandler.sendToServer(message);
    }

    public void sendToCuboid(IMessage message, AxisAlignedBB cuboid, int dimId) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server != null && cuboid != null) {
            for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
                if (player.dimension == dimId && cuboid.isVecInside(new Vec3d(player.posX, player.posY, player.posZ))) {
                    sendTo(message, player);
                }
            }
        }
    }
}