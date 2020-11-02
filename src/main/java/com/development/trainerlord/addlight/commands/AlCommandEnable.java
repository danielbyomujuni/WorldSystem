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
import org.bukkit.entity.Player;

public class AlCommandEnable extends Command<AddLightPlugin>
{
    /**
     * @param plugin The plugin that this object belong to.
     */
    public AlCommandEnable(AddLightPlugin plugin)
    {
        super("enable", plugin);
        this.setAliases(Lists.newArrayList("on", "1"));
        this.setDescription(plugin.messages.cEnable);
        this.setPermission(Permissions.USE.toString());
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

        // Enable LigthApi for the player
        LightsApi.enable(player);

        // Message
        EmptyMessage.toSender(player);
        PluginTitleMessage titleMessage = new PluginTitleMessage(plugin, plugin.messages.chatPrefix + " " + plugin.messages.enable);
        titleMessage.add(plugin.messages.leftClick);
        titleMessage.add(plugin.messages.rightClick);
        titleMessage.send(commandInfo.getSenders());

        return true;
    }
}
