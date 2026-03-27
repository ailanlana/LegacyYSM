package com.elfmcys.yesstevemodel.client.gui.button;

import com.elfmcys.yesstevemodel.network.message.RequestServerModelInfo;
import com.elfmcys.yesstevemodel.util.FileSizeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ModelInfoButton extends Button {
    private final RequestServerModelInfo.Info info;
    private boolean isSelect = false;

    public ModelInfoButton(int pX, int pY, int pHeight, RequestServerModelInfo.Info info, Consumer<Button> pOnPress) {
        super(pX, pY, 250, pHeight, "", pOnPress);
        this.info = info;
    }

    @Override
    protected void renderWidget(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        FontRenderer font = mc.fontRenderer;
        int color = this.isSelect ? 0xff_1E90FF : 0xff_434242;
        this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, color, color);
        if (this.isMouseOver()) {
            this.drawGradientRect(this.xPosition, this.yPosition + 1, this.xPosition + 1, this.yPosition + this.height - 1, 0xff_F3EFE0, 0xff_F3EFE0);
            this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + 1, 0xff_F3EFE0, 0xff_F3EFE0);
            this.drawGradientRect(this.xPosition + this.width - 1, this.yPosition + 1, this.xPosition + this.width, this.yPosition + this.height - 1, 0xff_F3EFE0, 0xff_F3EFE0);
            this.drawGradientRect(this.xPosition, this.yPosition + this.height - 1, this.xPosition + this.width, this.yPosition + this.height, 0xff_F3EFE0, 0xff_F3EFE0);
        }
        this.drawString(font, this.info.getFileName(), this.xPosition + 5, this.yPosition + (this.height - 8) / 2, 0xF3EFE0);
        final String text = switch (this.info.getType()) {
            case FOLDER -> EnumChatFormatting.AQUA + I18n.format("gui.yes_steve_model.model_manage.type.folder");
            case ZIP -> EnumChatFormatting.GOLD + I18n.format("gui.yes_steve_model.model_manage.type.zip");
            case SEVEN_Z -> EnumChatFormatting.LIGHT_PURPLE + I18n.format("gui.yes_steve_model.model_manage.type.seven_z");
            case YSM -> EnumChatFormatting.YELLOW + I18n.format("gui.yes_steve_model.model_manage.type.ysm");
            case UNKNOWN -> EnumChatFormatting.DARK_GRAY + I18n.format("gui.yes_steve_model.model_manage.type.unknown");
        };
        this.drawString(font, text, this.xPosition + 155, this.yPosition + (this.height - 8) / 2, 0xFFFFFFFF);
        this.drawString(font, FileSizeUtils.size(this.info.getSize()), this.xPosition + 205, this.yPosition + (this.height - 8) / 2, 0xC0C0C0);
    }

    public void setSelect(boolean select) {
        this.isSelect = select;
    }
}
