package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.event.CapabilityEvent;
import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nullable;

public class SetPlayAnimation implements IExtendedBufferMessage {
    private static final int STOP = -1;
    private int extraAnimationId;

    @SuppressWarnings("unused")
    public SetPlayAnimation() {
    }

    public SetPlayAnimation(int extraAnimationId) {
        this.extraAnimationId = extraAnimationId;
    }

    public static SetPlayAnimation stop() {
        return new SetPlayAnimation(STOP);
    }

    @Override
    public void toBytes(ExtendedBuffer buf) {
        buf.writeInt(this.extraAnimationId);
    }

    @Override
    public void fromBytes(ExtendedBuffer buf) {
        this.extraAnimationId = buf.readInt();
    }

    public static class Handler implements IMessageHandler<SetPlayAnimation, IMessage> {
        @Nullable
        @Override
        public IMessage onMessage(SetPlayAnimation message, MessageContext ctx) {
            if (ctx.side.isServer()) {
                EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
                if (sender != null && STOP <= message.extraAnimationId && message.extraAnimationId < 8) {
                    handleCapability(message, sender);
                }
            }
            return null;
        }

        private static void handleCapability(SetPlayAnimation message, EntityPlayerMP sender) {
            CapabilityEvent.getModelInfoCap(sender).ifPresent(modelIdCap -> {
                if (message.extraAnimationId == STOP) {
                    modelIdCap.stopAnimation();
                } else {
                    modelIdCap.playAnimation("extra" + message.extraAnimationId);
                }
            });
        }
    }
}
