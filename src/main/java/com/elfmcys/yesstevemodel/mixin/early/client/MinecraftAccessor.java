package com.elfmcys.yesstevemodel.mixin.early.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SuppressWarnings("UnusedMixin")
@Mixin(Minecraft.class)
public interface MinecraftAccessor {
    @Accessor("timer")
    Timer getTimer();
}
