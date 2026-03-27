package com.elfmcys.yesstevemodel.bukkit.event;

import com.elfmcys.yesstevemodel.bukkit.client.NPCData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class ClearNPCdataEvent {
    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        NPCData.clear();
    }
}
