package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.event.CapabilityEvent;
import com.elfmcys.yesstevemodel.model.ServerModelManager;
import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class SetModelAndTexture implements IExtendedBufferMessage {
    private ResourceLocation modelId;
    private ResourceLocation selectTexture;

    @SuppressWarnings("unused")
    public SetModelAndTexture() {
    }

    public SetModelAndTexture(ResourceLocation modelId, ResourceLocation selectTexture) {
        this.modelId = modelId;
        this.selectTexture = selectTexture;
    }

    @Override
    public void toBytes(ExtendedBuffer buf) {
        buf.writeResourceLocationToBuffer(this.modelId);
        buf.writeResourceLocationToBuffer(this.selectTexture);
    }

    @Override
    public void fromBytes(ExtendedBuffer buf) {
        this.modelId = buf.readResourceLocationFromBuffer();
        this.selectTexture = buf.readResourceLocationFromBuffer();
    }

    public static class Handler implements IMessageHandler<SetModelAndTexture, IMessage> {
        @Nullable
        @Override
        public IMessage onMessage(SetModelAndTexture message, MessageContext ctx) {
            if (ctx.side.isServer()) {
                EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
                if (sender != null) handleCapability(message, sender);
            }
            return null;
        }

        private static void handleCapability(SetModelAndTexture message, EntityPlayerMP sender) {
            CapabilityEvent.getModelInfoCap(sender).ifPresent(modelIdCap -> CapabilityEvent.getAuthModelsCap(sender).ifPresent(ownModelsCap -> {
                if (!ServerModelManager.AUTH_MODELS.contains(message.modelId.getResourcePath()) || ownModelsCap.containModel(message.modelId)) {
                    modelIdCap.setModelAndTexture(message.modelId, message.selectTexture);
                }
            }));
        }
    }
}
