package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.client.ClientModelManager;
import com.elfmcys.yesstevemodel.data.EncryptTools;
import com.elfmcys.yesstevemodel.data.ModelData;
import com.elfmcys.yesstevemodel.model.ServerModelManager;
import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import com.elfmcys.yesstevemodel.util.ThreadTools;
import com.elfmcys.yesstevemodel.util.UuidUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.UUID;

public class RequestLoadModel implements IExtendedBufferMessage {
    private String fileName;

    @SuppressWarnings("unused")
    public RequestLoadModel() {
    }

    public RequestLoadModel(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void toBytes(ExtendedBuffer buf) {
        buf.writeStringToBuffer(this.fileName);
    }

    @Override
    public void fromBytes(ExtendedBuffer buf) {
        this.fileName = buf.readStringFromBuffer();
    }

    public static class Handler implements IMessageHandler<RequestLoadModel, IMessage> {
        @Nullable
        @Override
        public IMessage onMessage(RequestLoadModel message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                handleClient(message);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private static void handleClient(RequestLoadModel message) {
            ClientModelManager.CACHE_MD5.add(message.fileName);
            loadModel(message.fileName);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void loadModel(String fileName) {
        ThreadTools.THREAD_POOL.submit(() -> {
            try {
                while (ClientModelManager.PASSWORD == null) {
                    Thread.sleep(500);
                }
                if (Minecraft.getMinecraft().theWorld != null) {
                    UUID uuid = Minecraft.getMinecraft().thePlayer.getUniqueID();
                    Path modelFile = ServerModelManager.CACHE_CLIENT.resolve(fileName);
                    byte[] fileBytes = FileUtils.readFileToByteArray(modelFile.toFile());
                    ModelData data = EncryptTools.decryptModel(UuidUtils.asBytes(uuid), ClientModelManager.PASSWORD, fileBytes);
                    if (data != null) {
                        ClientModelManager.registerAll(data);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
