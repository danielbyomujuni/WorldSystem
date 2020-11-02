package com.github.hexocraft.addlight.commands;

/*
 * Copyright 2017 hexosse
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import com.github.hexocraft.addlight.AddLightPlugin;
import com.github.hexocraft.addlight.LightsApi;
import com.github.hexocraft.addlight.configuration.Permissions;
import com.github.hexocraftapi.command.Command;
import com.github.hexocraftapi.command.CommandInfo;
import com.github.hexocraftapi.message.predifined.message.EmptyMessage;
import com.github.hexocraftapi.message.predifined.message.ErrorMessage;
import com.github.hexocraftapi.message.predifined.message.PluginTitleMessage;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * This file is part of AddLight
 *
 * @author <b>hexosse</b> (<a href="https://github.com/hexosse">hexosse on GitHub</a>).
 */
public class AlCommandConnected extends Command<AddLightPlugin>
{
    /**
     * @param plugin The plugin that this object belong to.
     */
    public AlCommandConnected(AddLightPlugin plugin) {
        super("ConnectedBlocks", plugin);
        this.setAliases(Lists.newArrayList("cb"));
        this.setDescription(plugin.messages.cConnectedBlock);
        this.setPermission(Permissions.CONNECTED.toString());
        this.setPermissionMessage(plugin.messages.AccesDenied);
    }

    /**
     * Executes the given command, returning its success
     *
     * @param commandInfo Info about the command
     *
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandInfo commandInfo)
    {
        // Get the player
        final Player player = commandInfo.getPlayer();

        // Player is mandatory
        if(player == null)  { ErrorMessage.toSender(commandInfo.getSender(), plugin.messages.ePlayer); return false; }

        // toglle connected block mode fot the player
        LightsApi.getPlayer(player).connectedBlocks = !LightsApi.connectedBlocks(player);

        // Message
        EmptyMessage.toSender(commandInfo.getPlayer());
        PluginTitleMessage titleMessage = new PluginTitleMessage(plugin, plugin.messages.connectedblocks + " " +  ChatColor.AQUA + (LightsApi.connectedBlocks(player) ? "on" : "off"));
        titleMessage.send(commandInfo.getSenders());

        return true;
    }
}
