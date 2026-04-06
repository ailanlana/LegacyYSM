package com.elfmcys.yesstevemodel.client.gui.button;

import com.elfmcys.yesstevemodel.mclib.utils.Interpolations;
import com.elfmcys.yesstevemodel.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * 改良版的按钮，具有新版按钮长文本滚动的特性，同时舍弃了 ID 系统（ID 均为 -1），改为和新版类似的 lamba。
 */
public class Button extends GuiButton {
    protected final Consumer<Button> onPress;

    public Button(int x, int y, int width, int height, String message, Consumer<Button> onPress) {
        super(-1, x, y, width, height, message);
        this.onPress = onPress;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (!this.visible) return;
        this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        this.renderWidget(mc, mouseX, mouseY);
        this.mouseDragged(mc, mouseX, mouseY);
    }

    /**
     * 纯粹的绘制方法，不需要再写悬停检测。
     */
    protected void renderWidget(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        int i = this.getHoverState(this.isMouseOver());
        mc.getTextureManager().bindTexture(buttonTextures);
        this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
        this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderString(mc.fontRenderer, this.getFGColor());
    }

    protected void renderString(FontRenderer font, int color) {
        this.renderScrollingString(font, 2, color);
    }

    @SuppressWarnings("SameParameterValue")
    protected void renderScrollingString(FontRenderer font, int width, int color) {
        int i = this.xPosition + width;
        int j = this.xPosition + this.width - width;
        this.renderScrollingString(font, this.displayString, i, this.yPosition, j, this.yPosition + this.height, color);
    }

    protected void renderScrollingString(FontRenderer font, String text, int minX, int minY, int maxX, int maxY, int color) {
        this.renderScrollingString(font, text, (minX + maxX) / 2, minX, minY, maxX, maxY, color);
    }

    protected void renderScrollingString(FontRenderer font, String text, int centerX, int minX, int minY, int maxX, int maxY, int color) {
        int i = font.getStringWidth(text);
        int j = (minY + maxY - 9) / 2 + 1;
        int k = maxX - minX;
        if (i > k) {
            int l = i - k;
            double d0 = (double) System.currentTimeMillis() / (double) 1000.0F;
            double d1 = Math.max((double) l * (double) 0.5F, 3.0F);
            double d2 = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * d0 / d1)) / (double) 2.0F + (double) 0.5F;
            double d3 = Interpolations.lerp(0.0F, l, d2);
            RenderUtil.scissor(minX, minY, maxX - minX, maxY - minY);
            this.drawString(font, text, minX - (int) d3, j, color);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        } else {
            int i1 = MathHelper.clamp_int(centerX, minX + i / 2, maxX - i / 2);
            this.drawCenteredString(font, text, i1, j, color);
        }
    }

    protected int getFGColor() {
        if (this.packedFGColour != 0) {
            return this.packedFGColour;
        } else if (!this.enabled) {
            return 0xA0A0A0;
        } else if (this.isMouseOver()) {
            return 0xFFFFA0;
        } else {
            return 0xE0E0E0;
        }
    }

    /**
     * 应被 Gui 调用，而不是按钮自我调用。
     */
    public void onPress() {
        this.onPress.accept(this);
    }

    @Override
    public void drawCenteredString(@Nonnull FontRenderer fontRenderer, @Nonnull String text, int x, int y, int color) {
        super.drawCenteredString(fontRenderer, text, x, y, color);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void drawString(@Nonnull FontRenderer fontRenderer, @Nonnull String text, int x, int y, int color) {
        super.drawString(fontRenderer, text, x, y, color);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Whether the mouse cursor is currently over the button.
     *
     * @deprecated Use {@link #isMouseOver()}.
     */
    @Deprecated
    @Override
    public boolean func_146115_a() {
        return this.isMouseOver();
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver() {
        return super.func_146115_a();
    }

    /**
     * @deprecated Use {@link #playPressSound(SoundHandler)}.
     */
    @Deprecated
    @Override
    public void func_146113_a(SoundHandler soundHandlerIn) {
        this.playPressSound(soundHandlerIn);
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        super.func_146113_a(soundHandlerIn);
    }
}
