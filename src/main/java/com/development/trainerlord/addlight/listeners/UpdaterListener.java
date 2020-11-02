package com.github.hexocraft.addlight.listeners;

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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author <b>Hexosse</b> (<a href="https://github.com/hexosse">on GitHub</a>))
 */
public class UpdaterListener implements Listener
{
	public UpdaterListener(AddLightPlugin plugin)
	{
		super();
	}

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if(event.getPlayer().isOp())
			AddLightPlugin.instance.runUpdater(event.getPlayer(), 20);
	}
}
