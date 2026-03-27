package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.command.sub.ManageCommand;
import com.elfmcys.yesstevemodel.model.ServerModelManager;
import com.elfmcys.yesstevemodel.network.NetworkHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;
import java.util.List;

public class RefreshModelManage implements IMessage {
    public RefreshModelManage() {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<RefreshModelManage, IMessage> {
        @Nullable
        @Override
        public IMessage onMessage(RefreshModelManage message, MessageContext ctx) {
            if (ctx.side.isServer() && ctx.getServerHandler().playerEntity.canCommandSenderUseCommand(4, "")) {
                List<RequestServerModelInfo.Info> builtinInfo = ManageCommand.getFilesInfo(ServerModelManager.BUILTIN);
                List<RequestServerModelInfo.Info> customInfo = ManageCommand.getFilesInfo(ServerModelManager.CUSTOM);
                List<RequestServerModelInfo.Info> authInfo = ManageCommand.getFilesInfo(ServerModelManager.AUTH);
                NetworkHandler.sendToClientPlayer(new RequestServerModelInfo(builtinInfo, customInfo, authInfo), ctx.getServerHandler().playerEntity);
            }
            return null;
        }
    }
}
