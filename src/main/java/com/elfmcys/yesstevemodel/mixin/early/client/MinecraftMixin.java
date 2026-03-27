package com.elfmcys.yesstevemodel.mixin.early.client;

import com.elfmcys.yesstevemodel.client.gui.Screen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("UnusedMixin")
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Redirect(
            method = "resize",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiScreen;setWorldAndResolution(Lnet/minecraft/client/Minecraft;II)V"
            )
    )
    private void onResize(GuiScreen instance, Minecraft mc, int width, int height) {
        if (instance instanceof Screen screen) screen.onResize(mc, width, height);
        else instance.setWorldAndResolution(mc, width, height);
    }
}
