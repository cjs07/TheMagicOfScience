package com.deepwelldevelopment.tmos.common;

import com.deepwelldevelopment.tmos.client.GuiHandler;
import com.deepwelldevelopment.tmos.client.gui.TMOSTab;
import com.deepwelldevelopment.tmos.common.capability.Capabilities;
import com.deepwelldevelopment.tmos.common.config.ConfigBlocks;
import com.deepwelldevelopment.tmos.common.config.ConfigItems;
import com.deepwelldevelopment.tmos.common.lib.crafting.TMOSCraftingRecipes;
import com.deepwelldevelopment.tmos.common.lib.crafting.TMOSSmeltingRecipes;
import com.deepwelldevelopment.tmos.common.lib.event.TMOSEventHandler;
import com.deepwelldevelopment.tmos.common.lib.network.TMOSPacketHandler;
import com.deepwelldevelopment.tmos.common.lib.world.TMOSWorldGen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = TMOS.modId, name = TMOS.name, version = TMOS.version)
public class TMOS {

    public static final String modId = "tmos";
    public static final String name  = "The Magic of Science";
    public static final String version = "0.0.1";

    public static TMOSPacketHandler packetHandler = new TMOSPacketHandler();

    public static Logger logger = LogManager.getLogger("tmos");

    @Mod.Instance(modId)
    public static TMOS instance;

    @SidedProxy(serverSide = "com.deepwelldevelopment.tmos.common.CommonProxy", clientSide = "com.deepwelldevelopment.tmos.client.ClientProxy")
    public static CommonProxy proxy;

    public static final TMOSTab tmosTab = new TMOSTab();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GameRegistry.registerWorldGenerator(new TMOSWorldGen(), 3);
        ConfigBlocks.init();
        ConfigItems.preInit();
        Capabilities.registerCapabilities();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        TMOSCraftingRecipes.init();
        TMOSSmeltingRecipes.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new TMOSEventHandler());
        packetHandler.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
