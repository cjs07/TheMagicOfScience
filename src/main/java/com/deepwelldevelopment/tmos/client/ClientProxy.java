package com.deepwelldevelopment.tmos.client;

import com.deepwelldevelopment.tmos.common.CommonProxy;
import com.deepwelldevelopment.tmos.common.TMOS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(TMOS.modId + ":" + id, "inventory"));
    }

    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().world;
    }

    @Override
    public EntityPlayer getPlayer(MessageContext context) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            return context.getServerHandler().playerEntity;
        } else {
            return Minecraft.getMinecraft().player;
        }
    }

    @Override
    public void handlePacket(Runnable runnable, EntityPlayer player) {
        if (player == null || player.world.isRemote) {
            Minecraft.getMinecraft().addScheduledTask(runnable);
        } else if (player != null && !player.world.isRemote) {
            ((WorldServer) player.world).addScheduledTask(runnable); //singleplayer
        }
    }
}
