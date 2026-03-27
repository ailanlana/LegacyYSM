package com.elfmcys.yesstevemodel.client.input;

import com.elfmcys.yesstevemodel.client.compat.KeyCompat;
import com.elfmcys.yesstevemodel.network.NetworkHandler;
import com.elfmcys.yesstevemodel.network.message.SetPlayAnimation;
import com.google.common.collect.Lists;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ExtraAnimationKey {
    public static final List<KeyBinding> EXTRA_ANIMATION_KEYS = Lists.newArrayList();

    public static void registerKeyBinding() {
        for (int i = 0; i <= 7; i++) {
            String name = String.format("key.yes_steve_model.extra_animation.%d.desc", i);
            KeyBinding keyMapping = KeyCompat.createKeyBinding(
                    name,
                    KeyCompat.ConflictContext.IN_GAME,
                    KeyCompat.Modifier.NONE,
                    Keyboard.KEY_NONE,
                    "key.category.yes_steve_model"
            );
            ClientRegistry.registerKeyBinding(keyMapping);
            EXTRA_ANIMATION_KEYS.add(keyMapping);
        }
    }

    @SubscribeEvent
    public void onKeyboardInput(InputEvent.KeyInputEvent event) {
        for (KeyBinding key : EXTRA_ANIMATION_KEYS) {
            if (key.isPressed()) {
                NetworkHandler.CHANNEL.sendToServer(new SetPlayAnimation(EXTRA_ANIMATION_KEYS.indexOf(key)));
                return;
            }
        }
    }
}
