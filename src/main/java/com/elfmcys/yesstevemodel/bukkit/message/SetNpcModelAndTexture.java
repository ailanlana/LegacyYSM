package com.elfmcys.yesstevemodel.bukkit.message;

import com.elfmcys.yesstevemodel.network.message.IExtendedBufferMessage;
import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.util.ResourceLocation;

public class SetNpcModelAndTexture implements IExtendedBufferMessage {
    private ResourceLocation modelId;
    private ResourceLocation selectTexture;
    private int npcId;

    @SuppressWarnings("unused")
    public SetNpcModelAndTexture() {
    }

    public SetNpcModelAndTexture(ResourceLocation modelId, ResourceLocation selectTexture, int npcId) {
        this.modelId = modelId;
        this.selectTexture = selectTexture;
        this.npcId = npcId;
    }

    @Override
    public void toBytes(ExtendedBuffer buf) {
        buf.writeResourceLocationToBuffer(this.modelId);
        buf.writeResourceLocationToBuffer(this.selectTexture);
        buf.writeInt(this.npcId);
    }

    @Override
    public void fromBytes(ExtendedBuffer buf) {
        this.modelId = buf.readResourceLocationFromBuffer();
        this.selectTexture = buf.readResourceLocationFromBuffer();
        this.npcId = buf.readInt();
    }

    public static class Handler implements IMessageHandler<SetNpcModelAndTexture, IMessage> {
        @Override
        public IMessage onMessage(SetNpcModelAndTexture message, MessageContext ctx) {
            return null;
        }
    }
}
