package com.elfmcys.yesstevemodel.client.input;

import com.elfmcys.yesstevemodel.client.compat.KeyCompat;
import com.elfmcys.yesstevemodel.client.gui.AnimationRouletteScreen;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class AnimationRouletteKey {
    public static final KeyBinding ANIMATION_ROULETTE_KEY = KeyCompat.createKeyBinding(
            "key.yes_steve_model.animation_roulette.desc",
            KeyCompat.ConflictContext.IN_GAME,
            KeyCompat.Modifier.NONE,
            Keyboard.KEY_Z,
            "key.category.yes_steve_model"
    );

    @SubscribeEvent
    public void onKeyboardInput(InputEvent.KeyInputEvent event) {
        if (ANIMATION_ROULETTE_KEY.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new AnimationRouletteScreen());
        }
    }
}
