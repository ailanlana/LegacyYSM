package com.elfmcys.yesstevemodel.bukkit.message;

import com.elfmcys.yesstevemodel.client.gui.PlayerModelScreen;
import com.elfmcys.yesstevemodel.network.message.IExtendedBufferMessage;
import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class OpenModelGuiMessage implements IExtendedBufferMessage {
    public static int CURRENT_NPC_ID = -1;
    private int entityId;
    private int npcId;

    @SuppressWarnings("unused")
    public OpenModelGuiMessage() {
    }

    @Override
    public void toBytes(ExtendedBuffer buf) {
    }

    @Override
    public void fromBytes(ExtendedBuffer buf) {
        this.entityId = buf.readInt();
        this.npcId = buf.readInt();
    }

    public static class Handler implements IMessageHandler<OpenModelGuiMessage, IMessage> {
        @Override
        public IMessage onMessage(OpenModelGuiMessage message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                handleMessage(message);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private static void handleMessage(OpenModelGuiMessage message) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP localPlayer = mc.thePlayer;
            if (localPlayer != null) {
                Entity entity = localPlayer.getEntityWorld().getEntityByID(message.entityId);
                if (entity instanceof EntityPlayer player) {
                    CURRENT_NPC_ID = message.npcId;
                    mc.displayGuiScreen(new PlayerModelScreen(player));
                }
            }
        }
    }
}
