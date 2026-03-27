package com.elfmcys.yesstevemodel.client.config;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.config.Config;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

@SuppressWarnings("unused")
public class ConfigGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Nonnull
    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ConfigGui.class;
    }

    @Nonnull
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return Collections.emptySet();
    }

    @Nullable
    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

    /**
     * Forge 的 Config Gui，更直观一点。
     */
    public static class ConfigGui extends GuiConfig {
        public ConfigGui(final GuiScreen parent) {
            super(parent, Config.getRootConfigElements(), YesSteveModel.MOD_ID, false, false, YesSteveModel.MOD_NAME);
        }
    }
}
