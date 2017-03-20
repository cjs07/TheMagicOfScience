package com.deepwelldevelopment.tmos.core.mod.client;

import com.deepwelldevelopment.tmos.core.mod.CommonProxy;
import com.deepwelldevelopment.tmos.core.network.ClientPacketHandler;
import com.deepwelldevelopment.tmos.core.network.NetworkManager;
import com.deepwelldevelopment.tmos.core.network.PacketProcessor;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static final KeyBinding KEY_UPDATE_GUI = new KeyBinding("key.sapmanpack.updateKey", Keyboard.KEY_U, "key.categories.sapmanpack");

    @Override
    public void registerRenderStuff() {
        ClientRegistry.registerKeyBinding(KEY_UPDATE_GUI);
        KeyHandler kHandler = new KeyHandler();
        FMLCommonHandler.instance().bus().register(kHandler);
        MinecraftForge.EVENT_BUS.register(kHandler);
    }

    @Override
    public void registerPacketHandler(String modId, String modChannel, PacketProcessor packetProcessor) {
        super.registerPacketHandler(modId, modChannel, packetProcessor);
        NetworkManager.getPacketChannel(modId).register(new ClientPacketHandler(modId, modChannel));
    }
}
