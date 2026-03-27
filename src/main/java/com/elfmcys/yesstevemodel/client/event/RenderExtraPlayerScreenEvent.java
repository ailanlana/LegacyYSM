package com.elfmcys.yesstevemodel.client.event;

import com.elfmcys.yesstevemodel.client.config.ExtraPlayerScreenConfig;
import com.elfmcys.yesstevemodel.client.gui.ExtraPlayerConfigScreen;
import com.elfmcys.yesstevemodel.util.RenderUtil;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class RenderExtraPlayerScreenEvent {
    @SubscribeEvent
    public void render(RenderGameOverlayEvent.Text event) {
        if (ExtraPlayerScreenConfig.DISABLE_PLAYER_RENDER) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        if (player == null) {
            return;
        }
        if (mc.currentScreen instanceof ExtraPlayerConfigScreen) {
            return;
        }

        double posX = ExtraPlayerScreenConfig.PLAYER_POS_X;
        double posY = ExtraPlayerScreenConfig.PLAYER_POS_Y;
        float scale = ExtraPlayerScreenConfig.PLAYER_SCALE;
        float yawOffset = ExtraPlayerScreenConfig.PLAYER_YAW_OFFSET;

        RenderUtil.renderPlayerEntity(player, posX, posY, scale, yawOffset, -500);
    }
}
