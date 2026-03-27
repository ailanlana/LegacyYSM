package com.elfmcys.yesstevemodel.client.gui.button;

import com.elfmcys.yesstevemodel.YesSteveModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class FlatIconButton extends FlatColorButton {
    private final static ResourceLocation ICON = new ResourceLocation(YesSteveModel.MOD_ID, "texture/icon.png");
    private final int textureX;
    private final int textureY;

    public FlatIconButton(int x, int y, int width, int height, int textureX, int textureY, Consumer<Button> onPress) {
        super(x, y, width, height, "", onPress);
        this.textureX = textureX;
        this.textureY = textureY;
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
        this.drawTexturedModalRect(this.xPosition + startX, this.yPosition + startY, this.textureX, this.textureY, 16, 16);
    }
}
