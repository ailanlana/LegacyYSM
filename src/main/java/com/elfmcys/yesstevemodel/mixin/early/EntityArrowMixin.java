package com.elfmcys.yesstevemodel.mixin.early;

import com.elfmcys.yesstevemodel.api.IArrowExtraInfo;
import com.elfmcys.yesstevemodel.capability.ArrowModelCapability;
import com.elfmcys.yesstevemodel.event.CapabilityEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@SuppressWarnings({"UnusedMixin", "AddedMixinMembersNamePattern"})
@Mixin(EntityArrow.class)
public class EntityArrowMixin implements IArrowExtraInfo {
    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;F)V", at = @At("RETURN"))
    private void setOwner(World world, EntityLivingBase entity, float p_i1756_3_, CallbackInfo ci) {
        if (entity instanceof EntityPlayer player) {
            this.ysm$setOwner(player);
        }
    }

    /// 理论上只有骷髅调用，以防万一
    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/entity/EntityLivingBase;FF)V", at = @At("RETURN"))
    private void setOwner(World world, EntityLivingBase entity, EntityLivingBase targetEntity, float velocity, float inaccuracy, CallbackInfo ci) {
        if (entity instanceof EntityPlayer player) {
            this.ysm$setOwner(player);
        }
    }

    @Unique
    private void ysm$setOwner(EntityPlayer player) {
        CapabilityEvent.getModelInfoCap(player).ifPresent(cap -> {
            EntityArrow self = (EntityArrow) (Object) this;
            CapabilityEvent.getArrowModelCap(self).ifPresent(arrowCap -> {
                arrowCap.setModelId(cap.getModelId().toString());
            });
        });
    }

    @Override
    @Unique
    public String getYsmModelId() {
        EntityArrow self = (EntityArrow) (Object) this;
        Optional<ArrowModelCapability> optional = CapabilityEvent.getArrowModelCap(self);
        return optional.isPresent() ? optional.get().getModelId() : IArrowExtraInfo.EMPTY;
    }
}
