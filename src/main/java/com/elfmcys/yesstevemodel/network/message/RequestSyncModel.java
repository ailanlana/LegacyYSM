package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.client.ClientModelManager;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;

public class RequestSyncModel implements IMessage {
    public RequestSyncModel() {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<RequestSyncModel, IMessage> {
        @Nullable
        @Override
        public IMessage onMessage(RequestSyncModel message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                handleClient();
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private static void handleClient() {
            ClientModelManager.sendSyncModelMessage();
        }
    }
}
