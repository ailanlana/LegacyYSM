package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.event.CapabilityEvent;
import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntityArrow;

import javax.annotation.Nullable;

public class SyncArrowModel implements IExtendedBufferMessage {
    private int entityId;
    private String modelId;

    @SuppressWarnings("unused")
    public SyncArrowModel() {
    }

    public SyncArrowModel(int entityId, String modelId) {
        this.entityId = entityId;
        this.modelId = modelId;
    }

    @Override
    public void toBytes(ExtendedBuffer buf) {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeStringToBuffer(this.modelId);
    }

    @Override
    public void fromBytes(ExtendedBuffer buf) {
        this.entityId = buf.readVarIntFromBuffer();
        this.modelId = buf.readStringFromBuffer();
    }

    public static class Handler implements IMessageHandler<SyncArrowModel, IMessage> {
        @Nullable
        @Override
        public IMessage onMessage(SyncArrowModel message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                handleCapability(message);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private static void handleCapability(SyncArrowModel message) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.thePlayer != null && mc.theWorld.getEntityByID(message.entityId) instanceof EntityArrow arrow) {
                CapabilityEvent.getArrowModelCap(arrow).ifPresent(cap -> cap.setModelId(message.modelId));
            }
        }
    }
}
