package de.butzlabben.world;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.butzlabben.autoupdater.AutoUpdater;
import de.butzlabben.inventory.OrcInventory;
import de.butzlabben.world.command.WSAddmemberCommand;
import de.butzlabben.world.command.WSCommand;
import de.butzlabben.world.command.WSConfirmCommand;
import de.butzlabben.world.command.WSDeleteCommand;
import de.butzlabben.world.command.WSDelmemberCommand;
import de.butzlabben.world.command.WSFireCommand;
import de.butzlabben.world.command.WSGetCommand;
import de.butzlabben.world.command.WSHomeCommand;
import de.butzlabben.world.command.WSInfoCommand;
import de.butzlabben.world.command.WSLeaveCommand;
import de.butzlabben.world.command.WSResetCommand;
import de.butzlabben.world.command.WSTPCommand;
import de.butzlabben.world.command.WSTnTCommand;
import de.butzlabben.world.command.WSToggleBuildCommand;
import de.butzlabben.world.command.WSToggleGMCommand;
import de.butzlabben.world.command.WSToggleTPCommand;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.Entry;
import de.butzlabben.world.config.GuiConfig;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.config.SettingsConfig;
import de.butzlabben.world.gui.GuiCommand;
import de.butzlabben.world.gui.PlayerOptionsGUI;
import de.butzlabben.world.gui.WorldOptionsGUI;
import de.butzlabben.world.gui.WorldSystemGUI;
import de.butzlabben.world.listener.BlockListener;
import de.butzlabben.world.listener.CommandListener;
import de.butzlabben.world.listener.PlayerDeathListener;
import de.butzlabben.world.listener.PlayerLeaveListener;
import de.butzlabben.world.wrapper.AsyncCreatorAdapter;
import de.butzlabben.world.wrapper.CreatorAdapter;
import de.butzlabben.world.wrapper.SystemWorld;

/**
 * @author Butzlabben
 * @author Jubeki
 * @since 10.07.2017
 * @version 1.0
 */
public class WorldSystem extends JavaPlugin {

	public static HashMap<Player, World> deathLocations = new HashMap<>();

	final private String version = this.getDescription().getVersion();
	public static OrcInventory mainGUI;

	public static CreatorAdapter creator;

	@Override
	public void onEnable() {
		createConfigs();

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerLeaveListener(), this);
		pm.registerEvents(new BlockListener(), this);
		pm.registerEvents(new PlayerDeathListener(), this);
		pm.registerEvents(new CommandListener(), this);

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new WorldCheckerRunnable(), 20 * 5,
				20 * PluginConfig.getLagCheckPeriod());
		if (PluginConfig.useGC()) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new GCRunnable(), 20 * 5,
					20 * PluginConfig.getGCPeriod());
		}

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			for (World w : Bukkit.getWorlds()) {
				SystemWorld sw = SystemWorld.getSystemWorld(w.getName());
				if (sw != null && sw.isLoaded())
					SettingsConfig.editWorld(w);
			}
		}, 20, 20 * 10);

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for (World w : Bukkit.getWorlds()) {
					SystemWorld.tryUnloadLater(w);
				}
			}
		}, 20 * 60 * 2, 20 * 60 * 2);

		// COMMANDS
		getCommand("ws").setExecutor(new WSCommand());
		getCommand("ws get").setExecutor(new WSGetCommand());
		getCommand("ws addmember").setExecutor(new WSAddmemberCommand());
		getCommand("ws delmember").setExecutor(new WSDelmemberCommand());
		getCommand("ws home").setExecutor(new WSHomeCommand());
		getCommand("ws leave").setExecutor(new WSLeaveCommand());
		getCommand("ws fire").setExecutor(new WSFireCommand());
		getCommand("ws info").setExecutor(new WSInfoCommand());
		getCommand("ws tnt").setExecutor(new WSTnTCommand());
		getCommand("ws tp").setExecutor(new WSTPCommand());
		getCommand("ws reset").setExecutor(new WSResetCommand());
		getCommand("ws toggletp").setExecutor(new WSToggleTPCommand());
		getCommand("ws togglegm").setExecutor(new WSToggleGMCommand());
		getCommand("ws togglebuild").setExecutor(new WSToggleBuildCommand());
		getCommand("ws delete").setExecutor(new WSDeleteCommand());

		getCommand("ws confirm").setExecutor(new WSConfirmCommand());

		getCommand("ws gui").setExecutor(new GuiCommand());

		mainGUI = new WorldSystemGUI();

		System.setProperty("bstats.relocatecheck", "false");
		Metrics m = new Metrics(this);
		m.addCustomChart(new Metrics.SingleLineChart("worlds", Entry::entrys));

		AutoUpdater.startAsync();

		// Choose right creatoradapter for #16
		if (Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit") != null
				&& Bukkit.getPluginManager().getPlugin("WorldEdit") != null) {
			creator = new AsyncCreatorAdapter();
			Bukkit.getConsoleSender()
					.sendMessage(PluginConfig.getPrefix() + "Found FAWE! Try now to create worlds async");
		} else {
			creator = (c, sw) -> {
				Bukkit.getWorlds().add(c.createWorld());
				if (sw != null)
					sw.stopCreating();
			};
		}

		Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "Succesfully enabled WorldSystem v" + version);
	}

	@Override
	public void onDisable() {
		for (World w : Bukkit.getWorlds()) {
			SystemWorld sw = SystemWorld.getSystemWorld(w.getName());
			if (sw != null && sw.isLoaded()) {
				sw.directUnload(w);
			}
		}
		mainGUI.unregister();
		PlayerOptionsGUI.instance.unregister();
		WorldOptionsGUI.instance.unregister();
		Bukkit.getConsoleSender()
				.sendMessage(PluginConfig.getPrefix() + "Succesfully disabled WorldSystem v" + version);
	}

	public static void createConfigs() {
		File dir = new File(JavaPlugin.getPlugin(WorldSystem.class).getDataFolder() + "/worldsources");
		File config = new File(JavaPlugin.getPlugin(WorldSystem.class).getDataFolder(), "config.yml");
		File dconfig = new File(JavaPlugin.getPlugin(WorldSystem.class).getDataFolder(), "dependence.yml");
		File languages = new File(JavaPlugin.getPlugin(WorldSystem.class).getDataFolder() + "/languages");
		File gui = new File(JavaPlugin.getPlugin(WorldSystem.class).getDataFolder(), "gui.yml");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (languages.exists() == false)
			languages.mkdirs();
		PluginConfig.checkConfig(config);
		// Done with #6
		MessageConfig.checkConfig(new File(languages, "en.yml"));
		MessageConfig.checkConfig(new File(languages, "de.yml"));
		MessageConfig.checkConfig(new File(languages, "hu.yml"));
		MessageConfig.checkConfig(new File(languages, "nl.yml"));
		MessageConfig.checkConfig(new File(languages, "pl.yml"));
		MessageConfig.checkConfig(new File(languages, "es.yml"));
		MessageConfig.checkConfig(new File(languages, "ru.yml"));
		MessageConfig.checkConfig(new File(languages, "fi.yml"));
		// Here we are for #5
		MessageConfig.checkConfig(new File(languages, "zh.yml"));
		MessageConfig.checkConfig(new File(languages, PluginConfig.getLanguage() + ".yml"));
		if (!dconfig.exists()) {
			try {
				dconfig.createNewFile();
			} catch (IOException e) {
				System.err.println("Wasn't able to create DependenceConfig");
				e.printStackTrace();
			}
			new DependenceConfig();
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(config);
		SettingsConfig.checkConfig();
		File worlddir = new File(cfg.getString("worldfolder"));
		if (!worlddir.exists()) {
			worlddir.mkdirs();
		}
		GuiConfig.checkConfig(gui);
	}

	public static WorldSystem getInstance() {
		return JavaPlugin.getPlugin(WorldSystem.class);
	}

}
