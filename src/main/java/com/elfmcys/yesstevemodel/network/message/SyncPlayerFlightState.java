package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import com.elfmcys.yesstevemodel.util.EntityUtil;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public class SyncPlayerFlightState implements IExtendedBufferMessage {
    private int entityId;
    private boolean flying;

    @SuppressWarnings("unused")
    public SyncPlayerFlightState() {
    }

    public SyncPlayerFlightState(int entityId, boolean flying) {
        this.entityId = entityId;
        this.flying = flying;
    }

    @Override
    public void toBytes(ExtendedBuffer buf) {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeBoolean(this.flying);
    }

    @Override
    public void fromBytes(ExtendedBuffer buf) {
        this.entityId = buf.readVarIntFromBuffer();
        this.flying = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<SyncPlayerFlightState, IMessage> {
        @Nullable
        @Override
        public IMessage onMessage(SyncPlayerFlightState message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                handleMessage(message);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private static void handleMessage(SyncPlayerFlightState message) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.theWorld != null && mc.theWorld.getEntityByID(message.entityId) instanceof EntityPlayer) {
                EntityUtil.setSyncedPlayerFlying(message.entityId, message.flying);
            }
        }
    }
}
