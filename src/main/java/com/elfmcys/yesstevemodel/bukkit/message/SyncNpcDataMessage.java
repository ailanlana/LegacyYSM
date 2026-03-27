package com.elfmcys.yesstevemodel.bukkit.message;

import com.elfmcys.yesstevemodel.bukkit.client.NPCData;
import com.elfmcys.yesstevemodel.network.message.IExtendedBufferMessage;
import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.UUID;

public class SyncNpcDataMessage implements IExtendedBufferMessage {
    private Map<UUID, Pair<ResourceLocation, ResourceLocation>> data;

    @SuppressWarnings("unused")
    public SyncNpcDataMessage() {
    }

    @Override
    public void toBytes(ExtendedBuffer buf) {
    }

    @Override
    public void fromBytes(ExtendedBuffer buf) {
        Map<UUID, Pair<ResourceLocation, ResourceLocation>> map = Maps.newHashMap();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            UUID uuid = buf.readUniqueIdFromBuffer();
            ResourceLocation modelId = new ResourceLocation(buf.readStringFromBuffer());
            ResourceLocation textureId = new ResourceLocation(buf.readStringFromBuffer());
            map.put(uuid, Pair.of(modelId, textureId));
        }
        this.data = map;
    }

    public static class Handler implements IMessageHandler<SyncNpcDataMessage, IMessage> {
        @Override
        public IMessage onMessage(SyncNpcDataMessage message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                handleMessage(message);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private static void handleMessage(SyncNpcDataMessage message) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP localPlayer = mc.thePlayer;
            if (localPlayer != null) {
                NPCData.addAll(message.data);
            }
        }
    }
}
