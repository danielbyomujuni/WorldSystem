package com.github.hexocraft.addlight;

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

import com.github.hexocraft.addlight.configuration.Permissions;
import com.github.hexocraft.addlight.utils.ConnectedBlocks;
import com.github.hexocraftapi.lights.Lights;
import com.github.hexocraftapi.message.predifined.message.SimplePrefixedMessage;
import com.google.common.collect.Lists;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.math.BlockVector3;
//import com.sk89q.worldedit.util.
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.github.hexocraft.addlight.AddLightPlugin.*;
import static com.github.hexocraft.addlight.commands.AlCommands.prefix;

/**
 * This file is part of AddLight
 *
 * @author <b>hexosse</b> (<a href="https://github.com/hexosse">hexosse on GitHub</a>).
 */
public class LightsApi
{
	public static class LigthPlayer
	{
		public boolean enable          = false;
		public boolean connectedBlocks = false;
		public int     lightlevel      = 15;

		public LigthPlayer() {}

		public LigthPlayer(boolean enable)
		{
			this.enable = enable;
		}

		public LigthPlayer(boolean connectedBlocks, int lightlevel)
		{
			this.connectedBlocks = connectedBlocks;
			this.lightlevel = lightlevel;
		}
	}


	private static Map<UUID, LigthPlayer> players = new HashMap<>();      // List of players
	private static int                    delay   = 10;                   // Delay(in ticks) before executing asynchronous tasks


	// Get the player
	public static LigthPlayer getPlayer(Player player)
	{
		if(player == null)  return null;

		// Get the player if exist
		LigthPlayer ligthPlayer = players.get(player.getUniqueId());

		// If not create it
		if(ligthPlayer == null)
			players.put(player.getUniqueId(), new LigthPlayer());

		return players.get(player.getUniqueId());
	}

	// Enable player to create a chest preview
	public static void enable(Player player) { getPlayer(player).enable = true; }

	// Disable player from creating a chest preview
	public static void disable(Player player) { getPlayer(player).enable = false; }

	// Remove all players from the player list
	public static void remove(Player player) { players.remove(player.getUniqueId()); }
	public static void removeAll() { players.clear(); }

	// Get player info
	public static boolean isEnable(Player player) { return getPlayer(player).enable; };
	public static boolean connectedBlocks(Player player) { return getPlayer(player).connectedBlocks; };
	public static int ligthLevel(Player player) { return getPlayer(player).lightlevel; };


	public static void createLight(final Player player, final Location location, final int lightLevel)
	{
		createLight(player, location, lightLevel, false);
	}

	public static void createLight(final Player player, final Location location, final int lightLevel, final boolean useConnectedBlocks)
    {
		// Création de la lumière pour des block connectés
		if(useConnectedBlocks && Permissions.has(player, Permissions.CONNECTED))
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					Location corner1 = null;
					Location corner2 = null;
					if(worldEdit != null && Permissions.has(player, Permissions.WORLDEDIT) && worldEdit//.isLocationInSelection(player, location))
					{
						BlockVector3 vCorner1 = null;//.getS//getSelection(player).getMaximumPoint();
						BlockVector3 vCorner2 = null;
						try {
							vCorner1 = worldEdit.getSession(player).getSelection(worldEdit.getSession(player).getSelectionWorld()).getMaximumPoint();
							vCorner2 = worldEdit.getSession(player).getSelection(worldEdit.getSession(player).getSelectionWorld()).getMinimumPoint();
						} catch (IncompleteRegionException e) {
							e.printStackTrace();
						}


						corner1 = new org.bukkit.Location(player.getWorld(), vCorner1.getX(), vCorner1.getY(), vCorner1.getZ());
						corner2 = new org.bukkit.Location(player.getWorld(), vCorner2.getX(), vCorner2.getY(), vCorner2.getZ());
					}

					List<Location> locations = ConnectedBlocks.getConnectedBlocks(location, config.cbLimit, corner1, corner2);
					int count = Lights.createLight(locations, lightLevel);

					// Message
					//SimplePrefixedMessage.toPlayer(player, prefix, count + " " + messages.lightsCreated);//TODO New Message System
				}
			}.runTaskLaterAsynchronously(instance, delay);
		}
		// Création de la lumière sur une selection WorldEditPlugin
        else if(worldEdit != null && Permissions.has(player, Permissions.WORLDEDIT) && worldEdit.isLocationInSelection(player, location))
        {
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					Iterator<BlockVector3> blocks    = //.getBlockVector(player, location);
					List<Location>         locations = Lists.newArrayList();

					while(blocks != null && blocks.hasNext())
					{
						BlockVector3 pos   = blocks.next();
						Block        block = location.getWorld().getBlockAt(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
						if(block.getType() != Material.AIR)
							locations.add(block.getLocation());
					}

					int count = Lights.createLight(locations, lightLevel);

					// Message
					//SimplePrefixedMessage.toPlayer(player, prefix, "" + count + " " + messages.lightsCreated);//TODO New Message System
				}
			}.runTaskLaterAsynchronously(instance, delay);
		}
        // Création de la lumière
        else
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					Lights.createLight(location, lightLevel);
				}
			}.runTaskLaterAsynchronously(instance, delay);
		}
    }


	public static void removeLight(final Player player, final Location location)
	{
		removeLight(player, location, false);
	}

	public static void removeLight(final Player player, final Location location, final boolean useConnectedBlocks)
    {
        // Suppression de la lumière pour des block connectés
        if(useConnectedBlocks && Permissions.has(player, Permissions.CONNECTED))
        {
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					Location corner1 = null;
					Location corner2 = null;
					if(worldEdit != null && Permissions.has(player, Permissions.WORLDEDIT) && worldEdit.isLocationInSelection(player, location))
					{
						BlockVector3 vCorner1 = worldEdit.getSelection(player).getMaximumPoint();
						BlockVector3 vCorner2 = worldEdit.getSelection(player).getMinimumPoint();

						corner1 = new org.bukkit.Location(player.getWorld(), vCorner1.getX(), vCorner1.getY(), vCorner1.getZ());
						corner2 = new org.bukkit.Location(player.getWorld(), vCorner2.getX(), vCorner2.getY(), vCorner2.getZ());
					}

					List<Location> locations = ConnectedBlocks.getConnectedBlocks(location, config.cbLimit, corner1, corner2);
					int count = Lights.removeLight(locations);

					// Message
					SimplePrefixedMessage.toPlayer(player, prefix, count + " " + messages.lightsDeleted);
				}
			}.runTaskLaterAsynchronously(instance, delay);
		}
		// Suppression de la lumière sur une selection WorldEditPlugin
        else if(worldEdit != null && Permissions.has(player, Permissions.WORLDEDIT) && worldEdit.isLocationInSelection(player, location))
        {
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					Iterator<BlockVector3> blocks = worldEdit.getBlockVector(player, location);
					List<Location> locations = Lists.newArrayList();

					while(blocks != null && blocks.hasNext())
					{
						BlockVector3 pos = blocks.next();
						Block block = location.getWorld().getBlockAt(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
						if(block.getType() != Material.AIR)
							locations.add(block.getLocation());
					}

					int count = Lights.removeLight(locations);

					// Message
					SimplePrefixedMessage.toPlayer(player, prefix, "" + count + " " + messages.lightsDeleted);
				}
			}.runTaskLaterAsynchronously(instance, delay);
		}
        // Suppression de la lumière
        else
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					Lights.removeLight(location);
				}
			}.runTaskLaterAsynchronously(instance, delay);
		}
    }


	public static void reLight(final Player player, final Location location)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				Lights.relight(location);
			}
		}.runTaskLaterAsynchronously(instance, delay);
	}
}
