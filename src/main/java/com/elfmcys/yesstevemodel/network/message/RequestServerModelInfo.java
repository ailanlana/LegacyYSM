package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.client.gui.ModelManageScreen;
import com.elfmcys.yesstevemodel.model.format.Type;
import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;
import java.util.List;

public class RequestServerModelInfo implements IExtendedBufferMessage {
    private List<Info> builtinModels;
    private List<Info> customModels;
    private List<Info> authModels;

    @SuppressWarnings("unused")
    public RequestServerModelInfo() {
    }

    public RequestServerModelInfo(List<Info> builtinModels, List<Info> customModels, List<Info> authModels) {
        this.builtinModels = builtinModels;
        this.customModels = customModels;
        this.authModels = authModels;
    }

    @Override
    public void toBytes(ExtendedBuffer buf) {
        buf.writeVarIntToBuffer(this.builtinModels.size());
        for (Info info : this.builtinModels) {
            infoToBuffer(buf, info);
        }
        buf.writeVarIntToBuffer(this.customModels.size());
        for (Info info : this.customModels) {
            infoToBuffer(buf, info);
        }
        buf.writeVarIntToBuffer(this.authModels.size());
        for (Info info : this.authModels) {
            infoToBuffer(buf, info);
        }
    }

    @Override
    public void fromBytes(ExtendedBuffer buf) {
        this.builtinModels = Lists.newArrayList();
        int builtinSize = buf.readVarIntFromBuffer();
        for (int i = 0; i < builtinSize; i++) {
            this.builtinModels.add(bufferToInfo(buf));
        }
        this.customModels = Lists.newArrayList();
        int customSize = buf.readVarIntFromBuffer();
        for (int i = 0; i < customSize; i++) {
            this.customModels.add(bufferToInfo(buf));
        }
        this.authModels = Lists.newArrayList();
        int authSize = buf.readVarIntFromBuffer();
        for (int i = 0; i < authSize; i++) {
            this.authModels.add(bufferToInfo(buf));
        }
    }

    public static class Handler implements IMessageHandler<RequestServerModelInfo, IMessage> {
        @Nullable
        @Override
        public IMessage onMessage(RequestServerModelInfo message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                handleClient(message);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private static void handleClient(RequestServerModelInfo message) {
            Minecraft.getMinecraft().displayGuiScreen(new ModelManageScreen(message.customModels, message.authModels));
        }
    }

    private static void infoToBuffer(ExtendedBuffer buf, Info info) {
        buf.writeStringToBuffer(info.fileName);
        buf.writeEnumValueToBuffer(info.type);
        buf.writeLong(info.size);
    }

    private static Info bufferToInfo(ExtendedBuffer buf) {
        return new Info(buf.readStringFromBuffer(), buf.readEnumValueFromBuffer(Type.class), buf.readLong());
    }

    public static class Info {
        /// 不含拓展名
        private String fileName;
        private Type type;
        private long size;

        public Info(String fileName, Type type, long size) {
            this.fileName = fileName;
            this.type = type;
            this.size = size;
        }

        public String getFileName() {
            return this.fileName;
        }

        public String getFullFileName() {
            return this.type.addExtension(this.fileName);
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Type getType() {
            return this.type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public long getSize() {
            return this.size;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }
}
