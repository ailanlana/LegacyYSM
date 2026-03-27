package com.elfmcys.yesstevemodel.command.argument;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.client.ClientModelManager;
import com.elfmcys.yesstevemodel.model.ServerModelManager;
import com.elfmcys.yesstevemodel.util.ModelIdUtil;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public final class TexturesArgument {
    public static String[] listSuggestions(String modelName) {
        if (FMLCommonHandler.instance().getSide().isServer()) {
            if (ServerModelManager.CACHE_NAME_INFO.containsKey(modelName)) {
                return ServerModelManager.CACHE_NAME_INFO.get(modelName).getTextures().toArray(new String[0]);
            }
        } else {
            ResourceLocation modelId = new ResourceLocation(YesSteveModel.MOD_ID, modelName);
            if (ClientModelManager.MODELS.containsKey(modelId)) {
                List<ResourceLocation> textures = ClientModelManager.MODELS.get(modelId);
                return textures.stream().map(ModelIdUtil::getSubNameFromId).filter(StringUtils::isNoneBlank).toArray(String[]::new);
            }
        }
        return new String[0];
    }
}
