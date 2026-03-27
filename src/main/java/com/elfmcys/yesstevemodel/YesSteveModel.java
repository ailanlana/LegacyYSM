package com.elfmcys.yesstevemodel;

import com.elfmcys.yesstevemodel.geckolib3.geo.raw.pojo.FormatVersion;
import com.elfmcys.yesstevemodel.geckolib3.geo.raw.pojo.LocatorValue;
import com.elfmcys.yesstevemodel.geckolib3.geo.raw.pojo.PolysUnion;
import com.elfmcys.yesstevemodel.geckolib3.geo.raw.pojo.UvUnion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = YesSteveModel.MOD_ID,
        name = YesSteveModel.MOD_NAME,
        version = Tags.VERSION,
        guiFactory = "com.elfmcys.yesstevemodel.client.config.ConfigGuiFactory",
        customProperties = {
                @Mod.CustomProperty(k = "license", v = "BSD-3-Clause"),
                @Mod.CustomProperty(k = "issueTrackerUrl", v = "https://github.com/RuiXuqi/YesSteveModel/issues")
        }
)
public class YesSteveModel {
    public static final String MOD_ID = "yes_steve_model";
    public static final String MOD_NAME = "Yes Steve Model";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting()
            .registerTypeAdapter(UvUnion.class, new UvUnion.Serializer())
            .registerTypeAdapter(LocatorValue.class, new LocatorValue.Serializer())
            .registerTypeAdapter(PolysUnion.class, new PolysUnion.Serializer())
            .registerTypeAdapter(FormatVersion.class, new FormatVersion.Serializer())
            .create();

    @SidedProxy(clientSide = "com.elfmcys.yesstevemodel.client.ClientProxy", serverSide = "com.elfmcys.yesstevemodel.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }
}
