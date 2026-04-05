package com.elfmcys.yesstevemodel.event;

import com.elfmcys.yesstevemodel.network.NetworkHandler;
import com.elfmcys.yesstevemodel.network.message.SyncPlayerFlightState;
import com.elfmcys.yesstevemodel.util.EntityUtil;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.event.world.WorldEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class FlightSyncEvent {
    private static final Map<UUID, Boolean> LAST_SYNCED_FLIGHT = new HashMap<>();

    @SubscribeEvent
    public void onTrackingPlayer(StartTracking event) {
        if (event.target instanceof EntityPlayer trackPlayer) {
            EntityPlayer player = event.entityPlayer;
            NetworkHandler.sendToClientPlayer(new SyncPlayerFlightState(trackPlayer.getEntityId(), trackPlayer.capabilities.isFlying), player);
        }
    }

    @SubscribeEvent
    public void playerTickEvent(TickEvent.PlayerTickEvent event) {
        if (!event.side.isServer() || event.phase != TickEvent.Phase.END) return;

        EntityPlayer player = event.player;
        boolean flying = player.capabilities.isFlying;
        UUID playerId = player.getUniqueID();
        boolean changed = !Boolean.valueOf(flying).equals(LAST_SYNCED_FLIGHT.get(playerId));
        boolean heartbeat = player.ticksExisted % 100 == 0;
        if (changed || heartbeat) {
            LAST_SYNCED_FLIGHT.put(playerId, flying);
            SyncPlayerFlightState syncMsg = new SyncPlayerFlightState(player.getEntityId(), flying);
            NetworkHandler.sendToAllAroundEntity(syncMsg, player, 512.0D);
            NetworkHandler.sendToClientPlayer(syncMsg, player);
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        LAST_SYNCED_FLIGHT.remove(event.player.getUniqueID());
        EntityUtil.clearSyncedPlayerFlying();
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world.isRemote) {
            EntityUtil.clearSyncedPlayerFlying();
        }
    }
}
