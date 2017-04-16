package com.deepwelldevelopment.tmos.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {

    public World getClientWorld() {
        return null;
    }

    public void registerItemRenderer(Item item, int meta, String id) {
    }

    public EntityPlayer getPlayer(MessageContext context) {
        return context.getServerHandler().playerEntity;
    }

    public void handlePacket(Runnable runnable, EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            ((WorldServer) player.world).addScheduledTask(runnable);
        }
    }
}
