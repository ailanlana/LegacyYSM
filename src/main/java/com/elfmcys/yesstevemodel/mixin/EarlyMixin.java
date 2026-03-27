package com.elfmcys.yesstevemodel.mixin;

import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class EarlyMixin implements IFMLLoadingPlugin, IEarlyMixinLoader {
    @Nullable
    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Nullable
    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public String getMixinConfig() {
        return "mixins.yes_steve_model.early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        List<String> mixins = new ArrayList<>();
        if (FMLLaunchHandler.side().isClient()) {
            mixins.add("client.ItemRendererMixin");
            mixins.add("client.MinecraftAccessor");
            mixins.add("client.MinecraftMixin");
            mixins.add("client.RenderArrowMixin");
            mixins.add("client.RenderPlayerEventPreMixin");
            mixins.add("client.RenderPlayerMixin");
        }
        mixins.add("EntityArrowMixin");
        return mixins;
    }
}
