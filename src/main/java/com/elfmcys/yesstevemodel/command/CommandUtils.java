package com.elfmcys.yesstevemodel.command;

import com.google.common.collect.Lists;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Collections;
import java.util.List;

public final class CommandUtils {
    public static void notifyCommandListener(ICommandSender sender, ICommand command, String translationKey, Object... translationArgs) {
        CommandBase.func_152373_a(sender, command, translationKey, translationArgs);
    }

    public static List<EntityPlayerMP> getPlayers(ICommandSender sender, String username) throws CommandException {
        EntityPlayerMP[] matchedPlayers = PlayerSelector.matchPlayers(sender, username);
        return matchedPlayers != null && matchedPlayers.length == 0 ? Lists.newArrayList(CommandBase.getPlayer(sender, username)) : Collections.emptyList();
    }
}
