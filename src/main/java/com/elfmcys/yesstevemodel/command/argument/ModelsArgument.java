package com.elfmcys.yesstevemodel.command.argument;

import com.elfmcys.yesstevemodel.client.ClientModelManager;
import com.elfmcys.yesstevemodel.model.ServerModelManager;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.util.ResourceLocation;

public final class ModelsArgument {
    public static String[] listSuggestions() {
        if (FMLCommonHandler.instance().getSide().isServer()) {
            return ServerModelManager.CACHE_NAME_INFO.keySet().toArray(new String[0]);
        } else {
            return ClientModelManager.MODELS.keySet().stream().map(ResourceLocation::getResourcePath).toArray(String[]::new);
        }
    }
}
