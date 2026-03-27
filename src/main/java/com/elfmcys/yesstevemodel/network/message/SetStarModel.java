package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.event.CapabilityEvent;
import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class SetStarModel implements IExtendedBufferMessage {
    private ResourceLocation modelId;
    private boolean isAdd;

    @SuppressWarnings("unused")
    public SetStarModel() {
    }

    private SetStarModel(ResourceLocation modelId, boolean isAdd) {
        this.modelId = modelId;
        this.isAdd = isAdd;
    }

    public static SetStarModel add(ResourceLocation modelId) {
        return new SetStarModel(modelId, true);
    }

    public static SetStarModel remove(ResourceLocation modelId) {
        return new SetStarModel(modelId, false);
    }

    @Override
    public void toBytes(ExtendedBuffer buf) {
        buf.writeResourceLocationToBuffer(this.modelId);
        buf.writeBoolean(this.isAdd);
    }

    @Override
    public void fromBytes(ExtendedBuffer buf) {
        this.modelId = buf.readResourceLocationFromBuffer();
        this.isAdd = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<SetStarModel, IMessage> {
        @Nullable
        @Override
        public IMessage onMessage(SetStarModel message, MessageContext ctx) {
            if (ctx.side.isServer()) {
                EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
                if (sender != null) handleCapability(message, sender);
            }
            return null;
        }

        private static void handleCapability(SetStarModel message, EntityPlayerMP sender) {
            CapabilityEvent.getStarModelsCap(sender).ifPresent(cap -> {
                if (message.isAdd) {
                    cap.addModel(message.modelId);
                } else {
                    cap.removeModel(message.modelId);
                }
            });
        }
    }
}
