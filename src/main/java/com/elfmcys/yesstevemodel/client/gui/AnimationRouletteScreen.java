package com.elfmcys.yesstevemodel.client.gui;

import com.elfmcys.yesstevemodel.client.ClientModelManager;
import com.elfmcys.yesstevemodel.client.compat.KeyCompat;
import com.elfmcys.yesstevemodel.client.config.GeneralConfig;
import com.elfmcys.yesstevemodel.client.input.AnimationRouletteKey;
import com.elfmcys.yesstevemodel.client.input.ExtraAnimationKey;
import com.elfmcys.yesstevemodel.event.CapabilityEvent;
import com.elfmcys.yesstevemodel.network.NetworkHandler;
import com.elfmcys.yesstevemodel.network.message.SetPlayAnimation;
import com.elfmcys.yesstevemodel.util.ModelIdUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class AnimationRouletteScreen extends Screen {
    private int x;
    private int y;
    private int selectId = -1;
    private @Nullable String[] names;

    public AnimationRouletteScreen() {
    }

    @Override
    public void initGui() {
        this.x = this.width / 2;
        this.y = this.height / 2 - 8;

        if (this.mc != null && this.mc.thePlayer != null) {
            CapabilityEvent.getModelInfoCap(this.mc.thePlayer).ifPresent(cap -> {
                ResourceLocation modelId = cap.getModelId();
                this.names = ClientModelManager.EXTRA_INFO.get(ModelIdUtil.getInfoId(modelId)).getExtraAnimationNames();
            });
        }
    }

    @Override
    public void drawScreen(int pMouseX, int pMouseY, float pPartialTick) {
        this.drawRoulette(pMouseX, pMouseY);
        this.drawRouletteText();
    }

    @Override
    public void mouseClicked(int pMouseX, int pMouseY, int pButton) {
        if (-1 < this.selectId && this.selectId < 8 && this.mc != null) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
            NetworkHandler.CHANNEL.sendToServer(new SetPlayAnimation(this.selectId));
            if (this.mc.thePlayer != null && GeneralConfig.PRINT_ANIMATION_ROULETTE_MSG) {
                this.mc.thePlayer.addChatMessage(new ChatComponentTranslation("message.yes_steve_model.model.animation_roulette.play", this.selectId));
            }
            this.mc.displayGuiScreen(null);
        }
        super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected boolean canGuiClose(int keyCode) {
        return super.canGuiClose(keyCode) || isKeyActiveIgnoreConflict(AnimationRouletteKey.ANIMATION_ROULETTE_KEY, keyCode);
    }

    private void drawRouletteText() {
        int count = 8;
        float startDeg = (float) Math.PI / count;
        for (int i = 0; i < count; i++) {
            int r = 65;
            String keyText = EnumChatFormatting.YELLOW + "[ ";
            KeyBinding keyMapping = ExtraAnimationKey.EXTRA_ANIMATION_KEYS.get(i);
            if (keyMapping.getKeyCode() == Keyboard.KEY_NONE) {
                keyText += I18n.format("key.yes_steve_model.extra_animation.none");
            } else {
                keyText += KeyCompat.getDisplayName(keyMapping);
            }
            keyText += " ]";
            if (this.names != null && this.names.length > i && StringUtils.isNoneBlank(this.names[i])) {
                this.drawCenteredString(this.fontRendererObj, this.names[i], (int) (this.x + r * MathHelper.cos(startDeg)), (int) (this.y + r * MathHelper.sin(startDeg) - (float) this.fontRendererObj.FONT_HEIGHT / 2 - 8), 0xF3EFE0);
            } else {
                this.drawCenteredString(this.fontRendererObj, String.valueOf(i), (int) (this.x + r * MathHelper.cos(startDeg)), (int) (this.y + r * MathHelper.sin(startDeg) - (float) this.fontRendererObj.FONT_HEIGHT / 2 - 8), 0xF3EFE0);
            }
            this.drawCenteredString(this.fontRendererObj, keyText, (int) (this.x + r * MathHelper.cos(startDeg)), (int) (this.y + r * MathHelper.sin(startDeg) - (float) this.fontRendererObj.FONT_HEIGHT / 2 + 4), 0xF3EFE0);
            startDeg = startDeg + 2 * (float) Math.PI / count;
        }
    }

    private void drawRoulette(int mouseX, int mouseY) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tesselator = Tessellator.instance;
        tesselator.startDrawingQuads();

        int count = 8;
        float theta = (float) Math.atan2(mouseY - this.y, mouseX - this.x);
        if (theta < 0) {
            theta = (float) Math.PI * 2 + theta;
        }
        float dx = mouseX - this.x;
        float dy = mouseY - this.y;
        float distance = MathHelper.sqrt_float(dx * dx + dy * dy);
        boolean isSelected = false;
        for (int i = 0; i < count; i++) {
            float spacingDeg = (float) Math.PI / 90;
            float startDeg = (2 * (float) Math.PI / count) * i + spacingDeg;
            float endDeg = (2 * (float) Math.PI / count) * (i + 1) - spacingDeg;
            if (startDeg < theta && theta < endDeg && 50 < distance && distance < 100) {
                this.drawFan(tesselator, 25, 105, startDeg, endDeg, 0xf0FFB100);
                isSelected = true;
                this.selectId = i;
            } else {
                this.drawFan(tesselator, 25, 105, startDeg, endDeg, 0x90000000);
            }
        }
        if (!isSelected) {
            this.selectId = -1;
        }

        tesselator.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void drawFan(Tessellator tess, float rIn, float rOut, float startDeg, float endDeg, int color) {
        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        tess.setColorRGBA_F(red, green, blue, alpha);
        tess.addVertex(this.x + rOut * MathHelper.cos(startDeg), this.y + rOut * MathHelper.sin(startDeg), this.zLevel);
        tess.setColorRGBA_F(red, green, blue, alpha);
        tess.addVertex(this.x + rIn * MathHelper.cos(startDeg), this.y + rIn * MathHelper.sin(startDeg), this.zLevel);
        tess.setColorRGBA_F(red, green, blue, alpha);
        tess.addVertex(this.x + rIn * MathHelper.cos(endDeg), this.y + rIn * MathHelper.sin(endDeg), this.zLevel);
        tess.setColorRGBA_F(red, green, blue, alpha);
        tess.addVertex(this.x + rOut * MathHelper.cos(endDeg), this.y + rOut * MathHelper.sin(endDeg), this.zLevel);
    }
}
