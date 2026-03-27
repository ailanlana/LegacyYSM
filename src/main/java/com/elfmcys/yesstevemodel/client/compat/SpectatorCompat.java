package com.elfmcys.yesstevemodel.client.compat;

import cpw.mods.fml.common.Optional;
import ganymedes01.etfuturum.spectator.SpectatorMode;
import net.minecraft.entity.player.EntityPlayer;

public class SpectatorCompat {
    public static boolean isSpectator(EntityPlayer player) {
        return Mods.ET_FUTURUM_INSTALLED && isEFSpectator(player);
    }

    @Optional.Method(modid = Mods.ET_FUTURUM)
    private static boolean isEFSpectator(EntityPlayer player) {
        return SpectatorMode.isSpectator(player);
    }
}
