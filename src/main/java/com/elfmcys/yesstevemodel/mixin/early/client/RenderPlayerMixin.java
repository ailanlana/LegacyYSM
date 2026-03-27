package com.elfmcys.yesstevemodel.mixin.early.client;

import com.elfmcys.yesstevemodel.client.compat.Hand;
import com.elfmcys.yesstevemodel.client.event.ReplacePlayerHandRenderEvent;
import com.elfmcys.yesstevemodel.geckolib3.extended.RenderPlayerEventPreAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnusedMixin")
@Mixin(RenderPlayer.class)
public class RenderPlayerMixin {
    /// 虽然理论上不会再调用了，但以防万一
    @Inject(method = "renderFirstPersonArm", at = @At("HEAD"), cancellable = true)
    private void addRenderRightArmEvent(EntityPlayer player, CallbackInfo ci) {
        if (ReplacePlayerHandRenderEvent.shouldRenderArm()) {
            ReplacePlayerHandRenderEvent.renderArm(Hand.MAIN_HAND);
            ci.cancel();
        }
    }

    @Redirect(
            method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/client/renderer/entity/RenderPlayer;F)Lnet/minecraftforge/client/event/RenderPlayerEvent$Pre;"
            )
    )
    private RenderPlayerEvent.Pre attachPos(
            EntityPlayer player, RenderPlayer renderer, float tick,
            @Local(ordinal = 0, argsOnly = true) double x,
            @Local(ordinal = 1, argsOnly = true) double y,
            @Local(ordinal = 2, argsOnly = true) double z
    ) {
        RenderPlayerEvent.Pre event = new RenderPlayerEvent.Pre(player, renderer, tick);
        RenderPlayerEventPreAccessor accessor = (RenderPlayerEventPreAccessor) event;
        accessor.ysm$setX(x);
        accessor.ysm$setY(y);
        accessor.ysm$setZ(z);
        return event;
    }
}
