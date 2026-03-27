package com.elfmcys.yesstevemodel.event;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.api.IArrowExtraInfo;
import com.elfmcys.yesstevemodel.capability.ArrowModelCapability;
import com.elfmcys.yesstevemodel.capability.AuthModelsCapability;
import com.elfmcys.yesstevemodel.capability.ModelInfoCapability;
import com.elfmcys.yesstevemodel.capability.StarModelsCapability;
import com.elfmcys.yesstevemodel.model.ServerModelManager;
import com.elfmcys.yesstevemodel.network.NetworkHandler;
import com.elfmcys.yesstevemodel.network.message.SyncArrowModel;
import com.elfmcys.yesstevemodel.network.message.SyncAuthModels;
import com.elfmcys.yesstevemodel.network.message.SyncModelInfo;
import com.elfmcys.yesstevemodel.network.message.SyncStarModels;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.Optional;

public final class CapabilityEvent {
    @SubscribeEvent
    public void onAttachCapabilityEvent(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer player) {
            if (!CapabilityEvent.getModelInfoCap(player).isPresent()) {
                player.registerExtendedProperties(ModelInfoCapability.IDENTIFIER, new ModelInfoCapability());
            }
            if (!CapabilityEvent.getAuthModelsCap(player).isPresent()) {
                player.registerExtendedProperties(AuthModelsCapability.IDENTIFIER, new AuthModelsCapability());
            }
            if (!CapabilityEvent.getStarModelsCap(player).isPresent()) {
                player.registerExtendedProperties(StarModelsCapability.IDENTIFIER, new StarModelsCapability());
            }
        }
        if (event.entity instanceof EntityArrow arrow) {
            if (!CapabilityEvent.getArrowModelCap(arrow).isPresent()) {
                arrow.registerExtendedProperties(ArrowModelCapability.IDENTIFIER, new ArrowModelCapability());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event) {
        Optional<ModelInfoCapability> oldModelInfoCap = getModelInfoCap(event.original);
        Optional<AuthModelsCapability> oldAuthModelsCap = getAuthModelsCap(event.original);
        Optional<StarModelsCapability> oldStarModelsCap = getStarModelsCap(event.original);

        Optional<ModelInfoCapability> newModelInfoCap = getModelInfoCap(event.entityPlayer);
        Optional<AuthModelsCapability> newAuthModelsCap = getAuthModelsCap(event.entityPlayer);
        Optional<StarModelsCapability> newStarModelsCap = getStarModelsCap(event.entityPlayer);

        newModelInfoCap.ifPresent(newModelInfo -> oldModelInfoCap.ifPresent(newModelInfo::copyFrom));
        newAuthModelsCap.ifPresent(newAuthModels -> oldAuthModelsCap.ifPresent(newAuthModels::copyFrom));
        newStarModelsCap.ifPresent(newStarModels -> oldStarModelsCap.ifPresent(newStarModels::copyFrom));
    }

    @SubscribeEvent
    public void onTrackingPlayer(PlayerEvent.StartTracking event) {
        if (event.target instanceof EntityPlayer trackPlayer) {
            EntityPlayer player = event.entityPlayer;
            getModelInfoCap(trackPlayer).ifPresent(cap -> {
                SyncModelInfo syncMsg = new SyncModelInfo(trackPlayer.getEntityId(), cap);
                NetworkHandler.sendToClientPlayer(syncMsg, player);
            });
            return;
        }
        if (event.target instanceof EntityArrow arrow) {
            String modelId = ((IArrowExtraInfo) arrow).getYsmModelId();
            if (!IArrowExtraInfo.EMPTY.equals(modelId)) {
                NetworkHandler.CHANNEL.sendToAllAround(new SyncArrowModel(arrow.getEntityId(), modelId), new NetworkRegistry.TargetPoint(arrow.dimension, arrow.posX, arrow.posY, arrow.posZ, 64.0D));
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayer player) {
            getModelInfoCap(player).ifPresent(modelInfoCap -> {
                if (player instanceof EntityPlayerMP serverPlayer) {
                    getAuthModelsCap(player).ifPresent(authModelsCap -> {
                        NetworkHandler.sendToClientPlayer(new SyncAuthModels(authModelsCap.getAuthModels()), serverPlayer);
                        if (ServerModelManager.AUTH_MODELS.contains(modelInfoCap.getModelId().getResourcePath()) && !authModelsCap.containModel(modelInfoCap.getModelId())) {
                            ResourceLocation defaultModelId = new ResourceLocation(YesSteveModel.MOD_ID, "default");
                            ResourceLocation defaultTextureId = new ResourceLocation(YesSteveModel.MOD_ID, "default/default.png");
                            modelInfoCap.setModelAndTexture(defaultModelId, defaultTextureId);
                        }
                    });
                    SyncModelInfo syncMsg = new SyncModelInfo(serverPlayer.getEntityId(), modelInfoCap);
                    NetworkHandler.sendToClientPlayer(syncMsg, serverPlayer);
                } else {
                    modelInfoCap.markDirty();
                }
            });

            getStarModelsCap(player).ifPresent(starModelCap -> {
                if (player instanceof EntityPlayerMP serverPlayer) {
                    NetworkHandler.sendToClientPlayer(new SyncStarModels(starModelCap.getStarModels()), serverPlayer);
                }
            });
        }
    }

    /**
     * 同步客户端服务端数据
     */
    @SubscribeEvent
    public void playerTickEvent(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
            getModelInfoCap(player).ifPresent(cap -> {
                if (cap.isDirty()) {
                    SyncModelInfo syncMsg = new SyncModelInfo(player.getEntityId(), cap);
                    NetworkHandler.CHANNEL.sendToAllAround(syncMsg, new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 64.0D));
                    NetworkHandler.sendToClientPlayer(syncMsg, player);
                    cap.setDirty(false);
                }
            });
        }
    }

    public static Optional<ModelInfoCapability> getModelInfoCap(EntityPlayer player) {
        return Optional.ofNullable((ModelInfoCapability) player.getExtendedProperties(ModelInfoCapability.IDENTIFIER));
    }

    public static Optional<AuthModelsCapability> getAuthModelsCap(EntityPlayer player) {
        return Optional.ofNullable((AuthModelsCapability) player.getExtendedProperties(AuthModelsCapability.IDENTIFIER));
    }

    public static Optional<StarModelsCapability> getStarModelsCap(EntityPlayer player) {
        return Optional.ofNullable((StarModelsCapability) player.getExtendedProperties(StarModelsCapability.IDENTIFIER));
    }

    public static Optional<ArrowModelCapability> getArrowModelCap(EntityArrow player) {
        return Optional.ofNullable((ArrowModelCapability) player.getExtendedProperties(ArrowModelCapability.IDENTIFIER));
    }
}
