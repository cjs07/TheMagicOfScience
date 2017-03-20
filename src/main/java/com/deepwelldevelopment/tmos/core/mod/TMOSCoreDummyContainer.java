package com.deepwelldevelopment.tmos.core.mod;

import com.deepwelldevelopment.tmos.core.init.TMOSLoadingPlugin;
import com.deepwelldevelopment.tmos.core.mod.client.ClientProxy;
import com.deepwelldevelopment.tmos.core.network.NetworkManager;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraftforge.fml.client.FMLFileResourcePack;
import net.minecraftforge.fml.client.FMLFolderResourcePack;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.util.Arrays;

public class TMOSCoreDummyContainer extends DummyModContainer {

    public static CommonProxy proxy;

    public TMOSCoreDummyContainer() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "tmoscore";
        meta.name = "TMOS Core";
        meta.version = "@VERSION@";
        meta.credits = "Roll Credits ...";
        meta.authorList = Arrays.asList("cjs07");
        meta.description = "";
        meta.screenshots = new String[0];
        meta.logoFile = "";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void modConstruction(FMLConstructionEvent evt){
        NetworkRegistry.INSTANCE.register(this, this.getClass(), null, evt.getASMHarvestedData());
    }

    @Subscribe
    @SideOnly(Side.CLIENT )
    public void injectClientProxy(FMLPreInitializationEvent evt) {
        proxy = new ClientProxy();
        this.preInit(evt);
    }

    @Subscribe
    @SideOnly(Side.SERVER )
    public void injectServerProxy(FMLPreInitializationEvent evt) {
        proxy = new CommonProxy();
        this.preInit(evt);
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent evt) {

    }

    @Subscribe
    public void init(FMLInitializationEvent evt) {
        proxy.registerRenderStuff();
    }


    @Subscribe
    public void postInit(FMLPostInitializationEvent evt) {
        NetworkManager.initPacketHandler();
    }

    @Override
    public File getSource() {
        return TMOSLoadingPlugin.source;
    }

    @Override
    public Class<?> getCustomResourcePackClass() {
        return getSource().isDirectory() ? FMLFolderResourcePack.class : FMLFileResourcePack.class;
    }
}
