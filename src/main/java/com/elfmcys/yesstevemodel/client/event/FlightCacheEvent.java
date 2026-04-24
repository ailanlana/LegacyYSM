package com.elfmcys.yesstevemodel.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FlightCacheEvent {
    private static final Set<Integer> FLYING_PLAYERS = ConcurrentHashMap.newKeySet();

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        FLYING_PLAYERS.clear();
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world.isRemote) {
            FLYING_PLAYERS.clear();
        }
    }

    public static boolean isFlying(int entityId) {
        return FLYING_PLAYERS.contains(entityId);
    }

    public static void setSyncedPlayerFlying(int entityId, boolean flying) {
        if (flying) FLYING_PLAYERS.add(entityId);
        else FLYING_PLAYERS.remove(entityId);
    }
}
