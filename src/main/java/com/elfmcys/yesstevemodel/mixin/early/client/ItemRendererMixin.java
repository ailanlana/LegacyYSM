package com.elfmcys.yesstevemodel.mixin.early.client;

import com.elfmcys.yesstevemodel.client.compat.Hand;
import com.elfmcys.yesstevemodel.client.event.ReplacePlayerHandRenderEvent;
import com.elfmcys.yesstevemodel.geckolib3.extended.ItemRendererAccessor;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("UnusedMixin")
@Mixin(ItemRenderer.class)
public class ItemRendererMixin implements ItemRendererAccessor {
    @Unique
    private boolean ysm$renderingLeft = false;

    @Unique
    @Override
    public void ysm$markRenderingLeft() {
        this.ysm$renderingLeft = true;
    }

    @Redirect(
            method = "renderItemInFirstPerson",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderPlayer;renderFirstPersonArm(Lnet/minecraft/entity/player/EntityPlayer;)V"
            )
    )
    private void passRenderingLeft(RenderPlayer instance, EntityPlayer player) {
        if (ReplacePlayerHandRenderEvent.shouldRenderArm()) {
            ReplacePlayerHandRenderEvent.renderArm(this.ysm$renderingLeft ? Hand.OFF_HAND : Hand.MAIN_HAND);
        } else {
            instance.renderFirstPersonArm(player);
        }
        this.ysm$renderingLeft = false;
    }
}
