package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.capability.ModelInfoCapability;
import com.elfmcys.yesstevemodel.event.CapabilityEvent;
import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import com.elfmcys.yesstevemodel.util.ThreadTools;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class SyncModelInfo implements IExtendedBufferMessage {
    private int entityId;
    private ModelInfoCapability capability;

    @SuppressWarnings("unused")
    public SyncModelInfo() {
    }

    public SyncModelInfo(int entityId, ModelInfoCapability capability) {
        this.entityId = entityId;
        this.capability = capability;
    }

    @Override
    public void toBytes(ExtendedBuffer buf) {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeNBTTagCompoundToBuffer(this.capability.serializeNBT());
    }

    @Override
    public void fromBytes(ExtendedBuffer buf) {
        this.entityId = buf.readVarIntFromBuffer();
        NBTTagCompound compoundTag = buf.readNBTTagCompoundFromBuffer();
        ModelInfoCapability cap = new ModelInfoCapability();
        if (compoundTag != null) {
            cap.deserializeNBT(compoundTag);
        }
        this.capability = cap;
    }

    public static class Handler implements IMessageHandler<SyncModelInfo, IMessage> {
        @Nullable
        @Override
        public IMessage onMessage(SyncModelInfo message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                handleCapability(message);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private static void handleCapability(SyncModelInfo message) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.theWorld != null) {
                ThreadTools.THREAD_POOL.submit(() -> {
                    try {
                        int time = 0;
                        while (mc.theWorld.getEntityByID(message.entityId) == null && time < 5) {
                            Thread.sleep(500);
                            time++;
                        }
                        Entity entity = mc.theWorld.getEntityByID(message.entityId);
                        if (entity instanceof EntityPlayer player) {
                            CapabilityEvent.getModelInfoCap(player).ifPresent(cap -> cap.copyFrom(message.capability));
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }
}
