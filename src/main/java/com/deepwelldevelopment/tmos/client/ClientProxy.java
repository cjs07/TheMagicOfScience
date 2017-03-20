package com.deepwelldevelopment.tmos.client;

import com.deepwelldevelopment.tmos.common.CommonProxy;
import com.deepwelldevelopment.tmos.common.TMOS;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(TMOS.modId + ":" + id, "inventory"));
    }

    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().world;
    }
}
