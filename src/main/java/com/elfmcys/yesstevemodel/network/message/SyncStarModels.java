package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.event.CapabilityEvent;
import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import com.google.common.collect.Sets;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Set;

public class SyncStarModels implements IExtendedBufferMessage {
    private Set<ResourceLocation> starModels;

    @SuppressWarnings("unused")
    public SyncStarModels() {
    }

    public SyncStarModels(Set<ResourceLocation> starModels) {
        this.starModels = starModels;
    }

    @Override
    public void toBytes(ExtendedBuffer buf) {
        buf.writeVarIntToBuffer(this.starModels.size());
        for (ResourceLocation modelId : this.starModels) {
            buf.writeResourceLocationToBuffer(modelId);
        }
    }

    @Override
    public void fromBytes(ExtendedBuffer buf) {
        int size = buf.readVarIntFromBuffer();
        Set<ResourceLocation> tmp = Sets.newHashSet();
        for (int i = 0; i < size; i++) {
            tmp.add(buf.readResourceLocationFromBuffer());
        }
        this.starModels = tmp;
    }

    public static class Handler implements IMessageHandler<SyncStarModels, IMessage> {
        @Nullable
        @Override
        public IMessage onMessage(SyncStarModels message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                handleCapability(message);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private static void handleCapability(SyncStarModels message) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.thePlayer != null) {
                CapabilityEvent.getStarModelsCap(mc.thePlayer).ifPresent(cap -> cap.setStarModels(message.starModels));
            }
        }
    }
}
