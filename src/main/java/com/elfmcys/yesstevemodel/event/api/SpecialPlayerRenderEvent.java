package com.elfmcys.yesstevemodel.event.api;

import com.elfmcys.yesstevemodel.client.entity.CustomPlayerEntity;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

@Cancelable
public class SpecialPlayerRenderEvent extends Event {
    private final EntityPlayer player;
    private final CustomPlayerEntity customPlayer;
    private final ResourceLocation modelId;

    public SpecialPlayerRenderEvent(EntityPlayer player, CustomPlayerEntity customPlayer, ResourceLocation modelId) {
        this.player = player;
        this.customPlayer = customPlayer;
        this.modelId = modelId;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }

    public CustomPlayerEntity getCustomPlayer() {
        return this.customPlayer;
    }

    public ResourceLocation getModelId() {
        return this.modelId;
    }
}
