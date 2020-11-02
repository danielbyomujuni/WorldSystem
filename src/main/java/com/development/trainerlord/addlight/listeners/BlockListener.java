package com.development.trainerlord.addlight.listeners;

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
//import com.github.hexocraftapi.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * This file is part of AddLight
 *
 * @author <b>hexosse</b> (<a href="https://github.comp/hexosse">hexosse on GitHub</a>))
 */
@SuppressWarnings("unused")
public class BlockListener implements Listener
{
    public BlockListener(AddLightPlugin plugin)
    {
        super();
    }

    /**
     * @param event BlockBreakEvent
     */
    @EventHandler(priority= EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if(LightsApi.isEnable(event.getPlayer())
           && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.GLOWSTONE_DUST
           && Permissions.has(event.getPlayer(), Permissions.USE))
        {
            event.setCancelled(true);
        }
    }
}
