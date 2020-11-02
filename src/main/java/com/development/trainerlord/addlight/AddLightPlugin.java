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

import com.github.hexocraft.addlight.commands.AlCommands;
import com.github.hexocraft.addlight.configuration.Config;
import com.github.hexocraft.addlight.configuration.Messages;
import com.github.hexocraft.addlight.integrations.WorldEditHooker;
import com.github.hexocraft.addlight.listeners.BlockListener;
import com.github.hexocraft.addlight.listeners.PlayerListener;
import com.github.hexocraft.addlight.listeners.UpdaterListener;
//import com.github.hexocraftapi.integration.Hook;
//import com.github.hexocraftapi.message.Line;
//import com.github.hexocraftapi.message.predifined.message.PluginMessage;
//import com.github.hexocraftapi.message.predifined.message.PluginTitleMessage;
//import com.github.hexocraftapi.plugin.Plugin;
//import com.github.hexocraftapi.updater.BukkitUpdater;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This file is part of AddLight
 *
 * @author <b>hexosse</b> (<a href="https://github.comp/hexosse">hexosse on GitHub</a>))
 */
public class AddLightPlugin extends JavaPlugin
{
	public static AddLightPlugin instance = null;
	public static Config         config   = null;
	public static Messages       messages = null;

	/* Plugins */
	public static WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
	public static Economy economy = null;


	/**
	 * Activation du plugin
	 */
	@Override
	public void onEnable()
	{
		/* Instance du plugin */
		instance = this;

        /* Chargement de la config */
		config = new Config(this, "config.yml", true);
		messages = new Messages(this, "message.yml", true);

        /* Enregistrement du gestionnaire de commandes */
		registerCommands(new AlCommands(this));

        /* Enregistrement des listeners */
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		Bukkit.getPluginManager().registerEvents(new BlockListener(this), this);
		Bukkit.getPluginManager().registerEvents(new UpdaterListener(this), this);

		/* Plugins */
		worldEdit = (WorldEditHooker) new Hook(WorldEditHooker.class, "WorldEdit", "com.sk89q.worldedit.bukkit.WorldEditPlugin").get();
		economy = setupEconomy();

		/* Enable message */
		PluginTitleMessage titleMessage = new PluginTitleMessage(this, "AddLight is enable ...", ChatColor.YELLOW);
		if(worldEdit != null) titleMessage.add("Integration with " + ChatColor.YELLOW + worldEdit.get().getName());
		if(economy != null) titleMessage.add("Integration with " + ChatColor.YELLOW + "Vault");
		titleMessage.send(Bukkit.getConsoleSender());

        /* Updater */
        runUpdater(getServer().getConsoleSender(), 20 * 10);

        /* Metrics */
        runMetrics(20 * 2);
	}

	/**
	 * Désactivation du plugin
	 */
	@Override
	public void onDisable()
	{
		LightsApi.removeAll();

		super.onDisable();

		PluginMessage.toConsole(this, "Disabled", ChatColor.RED, new Line("AddLight is now disabled", ChatColor.DARK_RED));
	}

	public void runUpdater(final CommandSender sender, int delay)
	{
		if(config.useUpdater)
			super.runUpdater(new BukkitUpdater(this, "255160"), sender, config.downloadUpdate ,delay);
	}

	private void runMetrics(int delay)
	{
		if(config.useMetrics)
			super.RunMetrics(delay);
	}

	/**
	 * Setup Vault Economy
	 *
	 * @return Economy object
	 */
	private Economy setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return null;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return null;
		}
		return rsp.getProvider();
	}
}
