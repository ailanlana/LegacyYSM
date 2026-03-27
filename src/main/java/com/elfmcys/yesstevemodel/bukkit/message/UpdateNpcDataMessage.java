package com.elfmcys.yesstevemodel.bukkit.message;

import com.elfmcys.yesstevemodel.bukkit.client.NPCData;
import com.elfmcys.yesstevemodel.network.message.IExtendedBufferMessage;
import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class UpdateNpcDataMessage implements IExtendedBufferMessage {
    private UUID uuid;
    private ResourceLocation modelId;
    private ResourceLocation textureId;

    @SuppressWarnings("unused")
    public UpdateNpcDataMessage() {
    }

    @Override
    public void toBytes(ExtendedBuffer buf) {
    }

    @Override
    public void fromBytes(ExtendedBuffer buf) {
        this.uuid = buf.readUniqueIdFromBuffer();
        this.modelId = new ResourceLocation(buf.readStringFromBuffer());
        this.textureId = new ResourceLocation(buf.readStringFromBuffer());
    }

    public static class Handler implements IMessageHandler<UpdateNpcDataMessage, IMessage> {
        @Override
        public IMessage onMessage(UpdateNpcDataMessage message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                handleMessage(message);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private static void handleMessage(UpdateNpcDataMessage message) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP localPlayer = mc.thePlayer;
            if (localPlayer != null) {
                NPCData.put(message.uuid, message.modelId, message.textureId);
            }
        }
    }
}
