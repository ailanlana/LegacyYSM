package com.elfmcys.yesstevemodel.client.event;

import com.elfmcys.yesstevemodel.client.entity.CustomPlayerEntity;
import com.elfmcys.yesstevemodel.event.api.SpecialPlayerRenderEvent;
import com.elfmcys.yesstevemodel.util.ModelIdUtil;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

// TODO：兼容皮肤移植模组，目前这个用起来大抵很灾难
public class VanillaPlayerRenderEvent {
    private static final ResourceLocation STEVE_SKIN_LOCATION = new ResourceLocation("textures/entity/steve.png");
    private static final ResourceLocation ALEX_SKIN_LOCATION = new ResourceLocation("textures/entity/alex.png");
    private static final String STEVE = "steve";
    private static final String ALEX = "alex";

    @SubscribeEvent
    public void onRenderPlayer(SpecialPlayerRenderEvent event) {
        EntityPlayer player = event.getPlayer();
        CustomPlayerEntity animatable = event.getCustomPlayer();
        if (isVanillaPlayer(event.getModelId()) && player instanceof EntityPlayerSP clientPlayer) {
            animatable.setMainModel(ModelIdUtil.getMainId(event.getModelId()));
            ResourceLocation location;
            Minecraft minecraft = Minecraft.getMinecraft();
            /// {@link net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer#func_152674_a(float, float, float, int, float, int, GameProfile)}
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.func_152342_ad().func_152788_a(clientPlayer.getGameProfile());
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                location = minecraft.func_152342_ad().func_152792_a(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            } else {
                location = getDefaultSkin(event.getModelId());
            }
            animatable.setTexture(location);
        }
    }

    private static boolean isVanillaPlayer(ResourceLocation modelId) {
        return modelId.getResourcePath().equals(STEVE) || modelId.getResourcePath().equals(ALEX);
    }

    private static ResourceLocation getDefaultSkin(ResourceLocation modelId) {
        return modelId.getResourcePath().equals(STEVE) ? STEVE_SKIN_LOCATION : ALEX_SKIN_LOCATION;
    }
}
