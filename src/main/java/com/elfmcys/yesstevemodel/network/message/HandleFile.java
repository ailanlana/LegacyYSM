package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.model.ServerModelManager;
import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

public class HandleFile implements IExtendedBufferMessage {
    private String name;
    private UploadFile.Dir dir;
    private String action;
    private String rename;

    @SuppressWarnings("unused")
    public HandleFile() {
    }

    public HandleFile(String name, UploadFile.Dir dir, String action, String rename) {
        this.name = name;
        this.dir = dir;
        this.action = action;
        this.rename = rename;
    }

    @Override
    public void toBytes(ExtendedBuffer buf) {
        buf.writeStringToBuffer(this.name);
        buf.writeEnumValueToBuffer(this.dir);
        buf.writeStringToBuffer(this.action);
        buf.writeStringToBuffer(this.rename);
    }

    @Override
    public void fromBytes(ExtendedBuffer buf) {
        this.name = buf.readStringFromBuffer();
        this.dir = buf.readEnumValueFromBuffer(UploadFile.Dir.class);
        this.action = buf.readStringFromBuffer();
        this.rename = buf.readStringFromBuffer();
    }

    public static class Handler implements IMessageHandler<HandleFile, IMessage> {
        @Nullable
        @Override
        public IMessage onMessage(HandleFile message, MessageContext ctx) {
            if (ctx.side.isServer() && ctx.getServerHandler().playerEntity.canCommandSenderUseCommand(4, "")) {
                if (message.dir == UploadFile.Dir.CUSTOM) {
                    String actionIn = message.action;
                    File file = ServerModelManager.CUSTOM.resolve(message.name).toFile();
                    if (file.isFile() || file.isDirectory()) {
                        if (actionIn.equals("delete")) {
                            FileUtils.deleteQuietly(file);
                        }
                        if (actionIn.equals("move")) {
                            File destFile = ServerModelManager.AUTH.resolve(message.name).toFile();
                            try {
                                if (file.isFile()) {
                                    FileUtils.moveFile(file, destFile);
                                }
                                if (file.isDirectory()) {
                                    FileUtils.moveDirectory(file, destFile);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (actionIn.equals("rename") && StringUtils.isNotBlank(message.rename)) {
                            File destFile = ServerModelManager.CUSTOM.resolve(message.rename).toFile();
                            try {
                                if (file.isFile()) {
                                    FileUtils.moveFile(file, destFile);
                                }
                                if (file.isDirectory()) {
                                    FileUtils.moveDirectory(file, destFile);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                if (message.dir == UploadFile.Dir.AUTH) {
                    String actionIn = message.action;
                    File file = ServerModelManager.AUTH.resolve(message.name).toFile();
                    if (file.isFile() || file.isDirectory()) {
                        if (actionIn.equals("delete")) {
                            FileUtils.deleteQuietly(file);
                        }
                        if (actionIn.equals("move")) {
                            File destFile = ServerModelManager.CUSTOM.resolve(message.name).toFile();
                            try {
                                if (file.isFile()) {
                                    FileUtils.moveFile(file, destFile);
                                }
                                if (file.isDirectory()) {
                                    FileUtils.moveDirectory(file, destFile);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (actionIn.equals("rename") && StringUtils.isNotBlank(message.rename)) {
                            File destFile = ServerModelManager.AUTH.resolve(message.rename).toFile();
                            try {
                                if (file.isFile()) {
                                    FileUtils.moveFile(file, destFile);
                                }
                                if (file.isDirectory()) {
                                    FileUtils.moveDirectory(file, destFile);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            return null;
        }
    }
}
