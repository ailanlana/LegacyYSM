package com.elfmcys.yesstevemodel.mixin.late.backhand.client;

import com.elfmcys.yesstevemodel.geckolib3.extended.ItemRendererAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xonin.backhand.client.hooks.ItemRendererHooks;
import xonin.backhand.client.utils.BackhandRenderHelper;

@SuppressWarnings("UnusedMixin")
@Mixin(value = ItemRendererHooks.class, remap = false)
public class ItemRendererHooksMixin {
    @Inject(method = "renderOffhandReturn", at = @At("HEAD"))
    private static void markRenderingLeft(float frame, CallbackInfo ci) {
        ((ItemRendererAccessor) BackhandRenderHelper.itemRenderer).ysm$markRenderingLeft();
    }
}
