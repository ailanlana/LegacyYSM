package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.model.ServerModelManager;
import com.elfmcys.yesstevemodel.network.NetworkHandler;
import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;

public class UploadFile implements IExtendedBufferMessage {
    private String name;
    private byte[] fileBytes;
    private Dir dir;

    @SuppressWarnings("unused")
    public UploadFile() {
    }

    public UploadFile(String name, byte[] fileBytes, Dir dir) {
        this.name = name;
        this.fileBytes = fileBytes;
        this.dir = dir;
    }

    @Override
    public void toBytes(ExtendedBuffer buf) {
        buf.writeStringToBuffer(this.name);
        buf.writeByteArrayToBuffer(this.fileBytes);
        buf.writeEnumValueToBuffer(this.dir);
    }

    @Override
    public void fromBytes(ExtendedBuffer buf) {
        this.name = buf.readStringFromBuffer();
        this.fileBytes = buf.readByteArrayFromBuffer();
        this.dir = buf.readEnumValueFromBuffer(Dir.class);
    }

    public static class Handler implements IMessageHandler<UploadFile, IMessage> {
        @Nullable
        @Override
        public IMessage onMessage(UploadFile message, MessageContext ctx) {
            if (ctx.side.isServer() && ctx.getServerHandler().playerEntity.canCommandSenderUseCommand(4, "")) {
                writeFile(message, ctx.getServerHandler().playerEntity);
            }
            return null;
        }

        private static void writeFile(UploadFile message, EntityPlayerMP player) {
            Path filePath;
            if (message.dir == Dir.CUSTOM) {
                filePath = ServerModelManager.CUSTOM.resolve(message.name);
            } else {
                filePath = ServerModelManager.AUTH.resolve(message.name);
            }
            try {
                FileUtils.writeByteArrayToFile(filePath.toFile(), message.fileBytes);
                NetworkHandler.sendToClientPlayer(new CompleteFeedback(), player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public enum Dir {
        CUSTOM,
        AUTH
    }
}
