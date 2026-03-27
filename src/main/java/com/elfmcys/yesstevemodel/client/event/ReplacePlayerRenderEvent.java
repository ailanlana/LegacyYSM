package com.elfmcys.yesstevemodel.client.event;

import com.elfmcys.yesstevemodel.client.ClientProxy;
import com.elfmcys.yesstevemodel.client.config.GeneralConfig;
import com.elfmcys.yesstevemodel.geckolib3.extended.RenderPlayerEventPreAccessor;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class ReplacePlayerRenderEvent {
    @SubscribeEvent
    public void onRender(RenderPlayerEvent.Pre event) {
        EntityPlayer playerRender = event.entityPlayer;
        EntityPlayerSP playerSelf = Minecraft.getMinecraft().thePlayer;
        if (playerRender.equals(playerSelf) && GeneralConfig.DISABLE_SELF_MODEL) {
            return;
        }
        if (!playerRender.equals(playerSelf) && GeneralConfig.DISABLE_OTHER_MODEL) {
            return;
        }
        event.setCanceled(true);
        RenderPlayerEventPreAccessor accessor = (RenderPlayerEventPreAccessor) event;
        ClientProxy.getInstance().doRender(playerRender, accessor.ysm$getX(), accessor.ysm$getY(), accessor.ysm$getZ(), playerRender.rotationYaw, event.partialRenderTick);
    }
}
