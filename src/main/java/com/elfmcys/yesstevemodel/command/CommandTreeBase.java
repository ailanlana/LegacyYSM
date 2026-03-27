/*
 * Minecraft Forge
 * Copyright (c) 2016-2018.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.elfmcys.yesstevemodel.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Base class for commands that has subcommands.
 * <p>
 * E.g. /team settings set [value]
 * settings is subcommand of team and set is subcommand of settings
 */
public abstract class CommandTreeBase extends CommandBase {
    private final Map<String, ICommand> commandMap = new HashMap<>();
    private final Map<String, ICommand> commandAliasMap = new HashMap<>();

    public void addSubcommand(ICommand command) {
        this.commandMap.put(command.getCommandName(), command);
        List<String> aliases = command.getCommandAliases();
        if (aliases != null) {
            for (String alias : aliases) {
                this.commandAliasMap.put(alias, command);
            }
        }
    }

    public Collection<ICommand> getSubCommands() {
        return this.commandMap.values();
    }

    @Nullable
    public ICommand getSubCommand(String command) {
        ICommand cmd = this.commandMap.get(command);
        if (cmd != null) {
            return cmd;
        }
        return this.commandAliasMap.get(command);
    }

    public Map<String, ICommand> getCommandMap() {
        return Collections.unmodifiableMap(this.commandMap);
    }

    public List<ICommand> getSortedCommandList() {
        List<ICommand> list = new ArrayList<>(this.getSubCommands());
        Collections.sort(list);
        return list;
    }

    private static String[] shiftArgs(@Nullable String[] s) {
        if (s == null || s.length == 0) {
            return new String[0];
        }

        String[] s1 = new String[s.length - 1];
        System.arraycopy(s, 1, s1, 0, s1.length);
        return s1;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> keys = new ArrayList<>();

            for (ICommand c : this.getSubCommands()) {
                if (c.canCommandSenderUseCommand(sender)) {
                    keys.add(c.getCommandName());
                }
            }

            keys.sort(null);
            return getListOfStringsMatchingLastWord(args, keys.toArray(new String[0]));
        }

        ICommand cmd = this.getSubCommand(args[0]);

        if (cmd != null) {
            return cmd.addTabCompletionOptions(sender, shiftArgs(args));
        }

        return super.addTabCompletionOptions(sender, args);
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        if (index > 0 && args.length > 1) {
            ICommand cmd = this.getSubCommand(args[0]);
            if (cmd != null) {
                return cmd.isUsernameIndex(shiftArgs(args), index - 1);
            }
        }

        return false;
    }

    /**
     * Callback for when the command is executed
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            String subCommandsString = this.getAvailableSubCommandsString(sender);
            sender.addChatMessage(new ChatComponentTranslation("commands.tree_base.available_subcommands", subCommandsString));
        } else {
            ICommand cmd = this.getSubCommand(args[0]);

            if (cmd == null) {
                String subCommandsString = this.getAvailableSubCommandsString(sender);
                throw new CommandException("commands.tree_base.invalid_cmd.list_subcommands", args[0], subCommandsString);
            } else if (!cmd.canCommandSenderUseCommand(sender)) {
                throw new CommandException("commands.generic.permission");
            } else {
                cmd.processCommand(sender, shiftArgs(args));
            }
        }
    }

    private String getAvailableSubCommandsString(ICommandSender sender) {
        Collection<String> availableCommands = new ArrayList<>();
        for (ICommand command : this.getSubCommands()) {
            if (command.canCommandSenderUseCommand(sender)) {
                availableCommands.add(command.getCommandName());
            }
        }
        return CommandBase.joinNiceStringFromCollection(availableCommands);
    }
}
