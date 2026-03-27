package com.elfmcys.yesstevemodel.client.input;

import com.elfmcys.yesstevemodel.client.compat.KeyCompat;
import com.elfmcys.yesstevemodel.client.gui.ExtraPlayerConfigScreen;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class ExtraPlayerConfigKey {
    public static final KeyBinding EXTRA_PLAYER_RENDER_KEY = KeyCompat.createKeyBinding(
            "key.yes_steve_model.open_extra_player_render.desc",
            KeyCompat.ConflictContext.IN_GAME,
            KeyCompat.Modifier.ALT,
            Keyboard.KEY_P,
            "key.category.yes_steve_model"
    );

    @SubscribeEvent
    public void onKeyboardInput(InputEvent.KeyInputEvent event) {
        if (EXTRA_PLAYER_RENDER_KEY.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new ExtraPlayerConfigScreen());
        }
    }
}
