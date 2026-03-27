package com.elfmcys.yesstevemodel.client.gui.button;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.bukkit.message.OpenModelGuiMessage;
import com.elfmcys.yesstevemodel.bukkit.message.SetNpcModelAndTexture;
import com.elfmcys.yesstevemodel.client.ClientModelManager;
import com.elfmcys.yesstevemodel.client.config.GeneralConfig;
import com.elfmcys.yesstevemodel.client.gui.Screen;
import com.elfmcys.yesstevemodel.event.CapabilityEvent;
import com.elfmcys.yesstevemodel.geckolib3.geo.raw.pojo.ExtraInfo;
import com.elfmcys.yesstevemodel.network.NetworkHandler;
import com.elfmcys.yesstevemodel.network.message.SetModelAndTexture;
import com.elfmcys.yesstevemodel.util.ModelIdUtil;
import com.elfmcys.yesstevemodel.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ModelButton extends Button {
    private final static ResourceLocation ICON = new ResourceLocation(YesSteveModel.MOD_ID, "texture/icon.png");
    private final Pair<ResourceLocation, List<ResourceLocation>> modelInfo;
    private final boolean needAuth;
    private final int color;
    private final @Nullable List<String> tooltips;
    private final EntityPlayer player;
    private final String modelName;
    private final String previewAnimation;
    private final boolean disablePreviewRotation;
    private final @Nullable ResourceLocation backgroundTexture;
    private final @Nullable ResourceLocation foregroundTexture;

    /**
     * @param modelInfo Model Id, Textures
     */
    public ModelButton(int pX, int pY, boolean needAuth, Pair<ResourceLocation, List<ResourceLocation>> modelInfo, @Nullable List<String> tooltips, EntityPlayer player) {
        super(pX, pY, 52, 90, modelInfo.getLeft().getResourcePath(), (b) -> {
        });
        this.modelInfo = modelInfo;
        this.needAuth = needAuth;
        this.color = needAuth ? 0x7F_000000 : 0xFF_434242;
        this.tooltips = tooltips;
        this.player = player;
        final ResourceLocation modelId = this.modelInfo.getLeft();
        final ExtraInfo extraInfo = ClientModelManager.EXTRA_INFO.get(ModelIdUtil.getInfoId(modelId));
        this.previewAnimation = extraInfo.getPreviewAnimation() != null ? extraInfo.getPreviewAnimation() : "idle";
        this.disablePreviewRotation = extraInfo.getDisablePreviewRotation();
        this.modelName = extraInfo.getName() != null ? extraInfo.getName() : "";
        final String guiBackground = extraInfo.getGuiBackground();
        this.backgroundTexture = guiBackground != null && !guiBackground.isEmpty() ?
                ModelIdUtil.getSubModelId(modelId, guiBackground) : null;
        final String guiForeground = extraInfo.getGuiForeground();
        this.foregroundTexture = guiForeground != null && !guiForeground.isEmpty() ?
                ModelIdUtil.getSubModelId(modelId, guiForeground) : null;
    }

    @Override
    public void onPress() {
        if (this.needAuth) {
            return;
        }
        CapabilityEvent.getModelInfoCap(this.player).ifPresent(cap ->
                cap.setModelAndTexture(this.modelInfo.getLeft(), this.modelInfo.getRight().get(0)));
        EntityPlayerSP localPlayer = Minecraft.getMinecraft().thePlayer;
        if (this.player.equals(localPlayer)) {
            NetworkHandler.CHANNEL.sendToServer(new SetModelAndTexture(this.modelInfo.getLeft(), this.modelInfo.getRight().get(0)));
        } else {
            NetworkHandler.CHANNEL.sendToServer(new SetNpcModelAndTexture(this.modelInfo.getLeft(), this.modelInfo.getRight().get(0), OpenModelGuiMessage.CURRENT_NPC_ID));
        }
    }

    @Override
    protected void renderWidget(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        final FontRenderer font = mc.fontRenderer;
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        // 灰背景
        this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, this.color, this.color);
        // 背景图
        if (this.backgroundTexture != null) {
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            mc.getTextureManager().bindTexture(this.backgroundTexture);
            RenderUtil.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0.0F, 0.0F, this.width, this.height, 52, 90);
            GL11.glDisable(GL11.GL_BLEND);
        }
        // 玩家模型
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderUtil.scissor(this.xPosition, this.yPosition, this.width, this.height - 20);
        RenderUtil.renderEntityInInventory(this.xPosition + this.width / 2, this.yPosition + this.height / 2 + 20, 30, mc.thePlayer, this.modelInfo.getLeft(), this.modelInfo.getRight().get(0), custom -> {
            if (!this.previewAnimation.isEmpty() && !custom.hasPreviewAnimation(this.previewAnimation)) {
                custom.setPreviewAnimation(this.previewAnimation);
            }
        }, this.disablePreviewRotation);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        // 前景图
        if (this.foregroundTexture != null) {
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            mc.getTextureManager().bindTexture(this.foregroundTexture);
            RenderUtil.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0.0F, 0.0F, this.width, this.height, 52, 90);
            GL11.glDisable(GL11.GL_BLEND);
        }
        // 文字
        final String modelName = GeneralConfig.SHOW_MODEL_ID_FIRST || this.modelName.isEmpty() ? this.displayString : this.modelName;
        List<String> split = font.listFormattedStringToWidth(modelName, 45);
        if (split.size() > 1) {
            this.drawCenteredString(font, split.get(0), this.xPosition + this.width / 2, this.yPosition + this.height - 19, 0xF3EFE0);
            this.drawCenteredString(font, split.get(1), this.xPosition + this.width / 2, this.yPosition + this.height - 10, 0xF3EFE0);
        } else {
            this.drawCenteredString(font, modelName, this.xPosition + this.width / 2, this.yPosition + this.height - 15, 0xF3EFE0);
        }
        // 悬停边框
        if (!this.needAuth && this.isMouseOver()) {
            this.drawGradientRect(this.xPosition, this.yPosition + 1, this.xPosition + 1, this.yPosition + this.height - 1, 0xff_F3EFE0, 0xff_F3EFE0);
            this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + 1, 0xff_F3EFE0, 0xff_F3EFE0);
            this.drawGradientRect(this.xPosition + this.width - 1, this.yPosition + 1, this.xPosition + this.width, this.yPosition + this.height - 1, 0xff_F3EFE0, 0xff_F3EFE0);
            this.drawGradientRect(this.xPosition, this.yPosition + this.height - 1, this.xPosition + this.width, this.yPosition + this.height, 0xff_F3EFE0, 0xff_F3EFE0);
        }
        // 锁定遮罩
        if (this.needAuth) {
            this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 0x9f_222222, 0x9f_222222);
        }
        // 收藏图标
        CapabilityEvent.getStarModelsCap(mc.thePlayer).ifPresent(cap -> {
            if (cap.containModel(this.modelInfo.getLeft())) {
                mc.getTextureManager().bindTexture(ICON);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                this.drawTexturedModalRect(this.xPosition + this.width - 14, this.yPosition, 16, 0, 16, 16);
            }
        });
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public void renderComponentTooltip(Screen screen, int pMouseX, int pMouseY) {
        if (this.isMouseOver() && this.tooltips != null) {
            screen.drawHoveringText(this.tooltips, pMouseX, pMouseY);
        }
    }

    @Override
    public boolean mousePressed(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        return !this.needAuth && super.mousePressed(mc, mouseX, mouseY);
    }
}
