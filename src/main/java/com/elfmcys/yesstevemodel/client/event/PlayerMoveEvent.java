package com.elfmcys.yesstevemodel.client.event;

import com.elfmcys.yesstevemodel.event.CapabilityEvent;
import com.elfmcys.yesstevemodel.network.NetworkHandler;
import com.elfmcys.yesstevemodel.network.message.SetPlayAnimation;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;

public class PlayerMoveEvent {
    @SubscribeEvent
    public void onKeyboardInput(InputEvent.KeyInputEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (isMoveKey() && player != null) {
            CapabilityEvent.getModelInfoCap(player).ifPresent(cap -> {
                if (cap.isPlayAnimation()) {
                    NetworkHandler.CHANNEL.sendToServer(SetPlayAnimation.stop());
                }
            });
        }
    }

    private static boolean isMoveKey() {
        GameSettings options = Minecraft.getMinecraft().gameSettings;
        return options.keyBindForward.isPressed() || options.keyBindBack.isPressed() || options.keyBindLeft.isPressed() || options.keyBindRight.isPressed()
                || options.keyBindJump.isPressed() || options.keyBindSneak.isPressed();
    }
}
