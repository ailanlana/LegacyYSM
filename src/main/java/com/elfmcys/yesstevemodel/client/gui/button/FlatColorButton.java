package com.elfmcys.yesstevemodel.client.gui.button;

import com.elfmcys.yesstevemodel.client.gui.Screen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class FlatColorButton extends Button {
    private boolean isSelect = false;
    private @Nullable List<String> tooltips;

    public FlatColorButton(int pX, int pY, int pWidth, int pHeight, String pMessage, Consumer<Button> pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
    }

    public FlatColorButton setTooltips(String key) {
        this.tooltips = Collections.singletonList(I18n.format(key));
        return this;
    }

    public FlatColorButton setTooltips(@Nullable List<String> tooltips) {
        this.tooltips = tooltips;
        return this;
    }

    public void renderToolTip(Screen screen, int pMouseX, int pMouseY) {
        if (this.isMouseOver() && this.tooltips != null) {
            screen.drawHoveringText(this.tooltips, pMouseX, pMouseY);
        }
    }

    @Override
    protected void renderWidget(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        FontRenderer font = mc.fontRenderer;
        if (this.isSelect) {
            this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 0xff_1E90FF, 0xff_1E90FF);
        } else {
            this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 0xff_434242, 0xff_434242);
        }
        if (this.isMouseOver()) {
            this.drawGradientRect(this.xPosition, this.yPosition + 1, this.xPosition + 1, this.yPosition + this.height - 1, 0xff_F3EFE0, 0xff_F3EFE0);
            this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + 1, 0xff_F3EFE0, 0xff_F3EFE0);
            this.drawGradientRect(this.xPosition + this.width - 1, this.yPosition + 1, this.xPosition + this.width, this.yPosition + this.height - 1, 0xff_F3EFE0, 0xff_F3EFE0);
            this.drawGradientRect(this.xPosition, this.yPosition + this.height - 1, this.xPosition + this.width, this.yPosition + this.height, 0xff_F3EFE0, 0xff_F3EFE0);
        }
        this.renderString(font, 0xF3EFE0);
    }

    public void setSelect(boolean select) {
        this.isSelect = select;
    }
}
