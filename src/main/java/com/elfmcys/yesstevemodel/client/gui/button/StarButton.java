package com.elfmcys.yesstevemodel.client.gui.button;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.event.CapabilityEvent;
import com.elfmcys.yesstevemodel.network.NetworkHandler;
import com.elfmcys.yesstevemodel.network.message.SetStarModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class StarButton extends FlatColorButton {
    private final static ResourceLocation ICON = new ResourceLocation(YesSteveModel.MOD_ID, "texture/icon.png");

    public StarButton(int x, int y) {
        super(x, y, 20, 20, "", (b) -> {
        });
    }

    @Override
    protected void renderWidget(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        super.renderWidget(mc, mouseX, mouseY);
        mc.getTextureManager().bindTexture(ICON);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        int startX = (this.width - 16) / 2;
        int startY = (this.height - 16) / 2;
        EntityPlayerSP player = mc.thePlayer;
        CapabilityEvent.getModelInfoCap(player).ifPresent(modelInfoCap -> CapabilityEvent.getStarModelsCap(player).ifPresent(starModelsCap -> {
            ResourceLocation modelId = modelInfoCap.getModelId();
            if (starModelsCap.containModel(modelId)) {
                this.drawTexturedModalRect(this.xPosition + startX, this.yPosition + startY, 16, 0, 16, 16);
            } else {
                this.drawTexturedModalRect(this.xPosition + startX, this.yPosition + startY, 0, 0, 16, 16);
            }
        }));
    }

    @Override
    public void onPress() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        CapabilityEvent.getModelInfoCap(player).ifPresent(modelInfoCap -> CapabilityEvent.getStarModelsCap(player).ifPresent(starModelsCap -> {
            ResourceLocation modelId = modelInfoCap.getModelId();
            if (starModelsCap.containModel(modelId)) {
                starModelsCap.removeModel(modelId);
                NetworkHandler.CHANNEL.sendToServer(SetStarModel.remove(modelId));
            } else {
                starModelsCap.addModel(modelId);
                NetworkHandler.CHANNEL.sendToServer(SetStarModel.add(modelId));
            }
        }));
    }
}
