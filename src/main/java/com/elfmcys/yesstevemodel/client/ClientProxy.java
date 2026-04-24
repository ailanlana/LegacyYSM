package com.elfmcys.yesstevemodel.client;

import com.elfmcys.yesstevemodel.CommonProxy;
import com.elfmcys.yesstevemodel.bukkit.event.ClearNPCdataEvent;
import com.elfmcys.yesstevemodel.client.animation.AnimationRegister;
import com.elfmcys.yesstevemodel.client.compat.Mods;
import com.elfmcys.yesstevemodel.client.config.ConfigSyncEvent;
import com.elfmcys.yesstevemodel.client.event.*;
import com.elfmcys.yesstevemodel.client.input.*;
import com.elfmcys.yesstevemodel.client.renderer.CustomArrowRenderer;
import com.elfmcys.yesstevemodel.client.renderer.CustomPlayerRenderer;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {
    private static CustomPlayerRenderer CUSTOM_PLAYER_RENDERER;
    private static CustomArrowRenderer CUSTOM_ARROW_RENDERER;

    public static CustomPlayerRenderer getInstance() {
        return CUSTOM_PLAYER_RENDERER;
    }

    public static CustomArrowRenderer getArrowInstance() {
        return CUSTOM_ARROW_RENDERER;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        Mods.init();

        FMLCommonHandler.instance().bus().register(new ConfigSyncEvent());
        FMLCommonHandler.instance().bus().register(new FlightCacheEvent());
        FMLCommonHandler.instance().bus().register(new PlayerMoveEvent());

        FMLCommonHandler.instance().bus().register(new ClearNPCdataEvent());

        FMLCommonHandler.instance().bus().register(new AnimationRouletteKey());
        FMLCommonHandler.instance().bus().register(new DebugAnimationKey());
        FMLCommonHandler.instance().bus().register(new ExtraAnimationKey());
        FMLCommonHandler.instance().bus().register(new ExtraPlayerConfigKey());
        FMLCommonHandler.instance().bus().register(new PlayerModelScreenKey());

        MinecraftForge.EVENT_BUS.register(new DebugAnimationKey());
        MinecraftForge.EVENT_BUS.register(new FlightCacheEvent());
        MinecraftForge.EVENT_BUS.register(new ReloadResourceEvent());
        MinecraftForge.EVENT_BUS.register(new RenderExtraPlayerScreenEvent());
        MinecraftForge.EVENT_BUS.register(new RenderFirstPlayerBackground());
        MinecraftForge.EVENT_BUS.register(new ReplacePlayerRenderEvent());
        MinecraftForge.EVENT_BUS.register(new VanillaPlayerRenderEvent());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ClientRegistry.registerKeyBinding(PlayerModelScreenKey.PLAYER_MODEL_KEY);
        ClientRegistry.registerKeyBinding(AnimationRouletteKey.ANIMATION_ROULETTE_KEY);
        ClientRegistry.registerKeyBinding(DebugAnimationKey.DEBUG_ANIMATION_KEY);
        ClientRegistry.registerKeyBinding(ExtraPlayerConfigKey.EXTRA_PLAYER_RENDER_KEY);
        ExtraAnimationKey.registerKeyBinding();

        AnimationRegister.registerAnimationState();
        AnimationRegister.registerVariables();

        CUSTOM_PLAYER_RENDERER = new CustomPlayerRenderer();
        CUSTOM_ARROW_RENDERER = new CustomArrowRenderer();
    }
}
