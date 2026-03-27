package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.client.upload.UploadManager;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;

public class CompleteFeedback implements IMessage {
    public CompleteFeedback() {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<CompleteFeedback, IMessage> {
        @Nullable
        @Override
        public IMessage onMessage(CompleteFeedback message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                handleClient();
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private static void handleClient() {
            UploadManager.finishUpload();
        }
    }
}
