package com.elfmcys.yesstevemodel.client.input;

import com.elfmcys.yesstevemodel.client.compat.KeyCompat;
import com.elfmcys.yesstevemodel.client.config.GeneralConfig;
import com.elfmcys.yesstevemodel.client.gui.DisclaimerScreen;
import com.elfmcys.yesstevemodel.client.gui.PlayerModelScreen;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class PlayerModelScreenKey {
    public static final KeyBinding PLAYER_MODEL_KEY = KeyCompat.createKeyBinding(
            "key.yes_steve_model.player_model.desc",
            KeyCompat.ConflictContext.IN_GAME,
            KeyCompat.Modifier.ALT,
            Keyboard.KEY_Y,
            "key.category.yes_steve_model"
    );

    @SubscribeEvent
    public void onKeyboardInput(InputEvent.KeyInputEvent event) {
        if (PLAYER_MODEL_KEY.isPressed()) {
            if (GeneralConfig.DISCLAIMER_SHOW) {
                Minecraft.getMinecraft().displayGuiScreen(new DisclaimerScreen());
            } else {
                Minecraft.getMinecraft().displayGuiScreen(new PlayerModelScreen());
            }
        }
    }
}
