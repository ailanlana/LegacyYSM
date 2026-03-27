package com.elfmcys.yesstevemodel.event;

import com.elfmcys.yesstevemodel.model.ServerModelManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public final class EnterServerEvent {
    @SubscribeEvent
    public void onLoggedInServer(PlayerEvent.PlayerLoggedInEvent event) {
        ServerModelManager.sendRequestSyncModelMessage(event.player);
    }
}
