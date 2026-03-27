package com.elfmcys.yesstevemodel;

import com.elfmcys.yesstevemodel.command.RootCommand;
import com.elfmcys.yesstevemodel.config.Config;
import com.elfmcys.yesstevemodel.event.CapabilityEvent;
import com.elfmcys.yesstevemodel.event.EnterServerEvent;
import com.elfmcys.yesstevemodel.model.ServerModelManager;
import com.elfmcys.yesstevemodel.network.NetworkHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Config.init(event.getSuggestedConfigurationFile(), event.getSide());
        ServerModelManager.reloadPacks();
        NetworkHandler.init();
        this.registerEvents();
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new RootCommand());
    }

    protected void registerEvents() {
        FMLCommonHandler.instance().bus().register(new CapabilityEvent());
        FMLCommonHandler.instance().bus().register(new EnterServerEvent());

        MinecraftForge.EVENT_BUS.register(new CapabilityEvent());
    }
}
