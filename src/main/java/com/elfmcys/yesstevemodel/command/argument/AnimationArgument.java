package com.elfmcys.yesstevemodel.command.argument;

import com.elfmcys.yesstevemodel.client.model.CustomPlayerModel;
import com.elfmcys.yesstevemodel.geckolib3.file.AnimationFile;
import com.elfmcys.yesstevemodel.geckolib3.resource.GeckoLibCache;
import com.google.common.collect.Sets;
import cpw.mods.fml.common.FMLCommonHandler;

import java.util.Set;

public final class AnimationArgument {
    private static final String STOP = "stop";

    public static String[] listSuggestions() {
        if (FMLCommonHandler.instance().getSide().isServer()) {
            // Fixme: 应该为服务器后台也添加提示功能
            return new String[0];
        } else {
            AnimationFile main = GeckoLibCache.getInstance().getAnimations().get(CustomPlayerModel.DEFAULT_MAIN_ANIMATION);
            Set<String> animations = Sets.newHashSet();
            animations.addAll(main.animations().keySet());
            animations.add(STOP);
            return animations.toArray(new String[0]);
        }
    }
}
