package com.elfmcys.yesstevemodel.mixin;

import com.elfmcys.yesstevemodel.client.compat.Mods;
import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@com.gtnewhorizon.gtnhmixins.LateMixin
public class LateMixin implements ILateMixinLoader {
    @Override
    public String getMixinConfig() {
        return "mixins.yes_steve_model.late.json";
    }

    @Nonnull
    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        return loadedMods.contains(Mods.BACKHAND) ?
                Collections.singletonList("backhand.client.ItemRendererHooksMixin") :
                Collections.emptyList();
    }
}
