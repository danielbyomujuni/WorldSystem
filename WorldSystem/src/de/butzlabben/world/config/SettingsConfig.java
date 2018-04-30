package de.butzlabben.world.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.wrapper.SystemWorld;

public class SettingsConfig {

	private static File file;

	public static void editWorld(World w) {
		YamlConfiguration cfg = getConfig();

		SystemWorld sw = SystemWorld.getSystemWorld(w.getName());

		boolean shouldChange = cfg.getBoolean("worldborder.should_change", false);
		if (shouldChange) {
			double size = cfg.getDouble("worldborder.normal", 1000);
			if (sw != null && sw.isLoaded()) {
				String worldname = w.getName();
				UUID uuid = UUID.fromString(worldname.substring(worldname.length() - 36));
				Player p = Bukkit.getPlayer(uuid);
				if (p != null && p.isOnline()) {
					if (p.hasPermission("ws.large")) {
						size = cfg.getDouble("worldborder.large", 5000);
					} else if (p.hasPermission("ws.big"))
						size = cfg.getDouble("worldborder.big", 2000);
				}
			}

			w.getWorldBorder().setSize(size);

			if (cfg.getBoolean("worldborder.center.as_spawn", true)) {
				if (PluginConfig.useWorldSpawn()) {
					w.getWorldBorder().setCenter(PluginConfig.getWorldSpawn(w));
				} else {
					w.getWorldBorder().setCenter(w.getSpawnLocation());
				}
			} else {
				Location loc = new Location(w, cfg.getDouble("worldborder.center.x", 0),
						cfg.getDouble("worldborder.center.y", 20), cfg.getDouble("worldborder.center.z", 0));
				w.getWorldBorder().setCenter(loc);
			}
		}

		if (w.isGameRule("announceAdvancements"))
			w.setGameRuleValue("announceAdvancements", cfg.getString("announceAdvancements"));

		if (w.isGameRule("commandBlockOutput"))
			w.setGameRuleValue("commandBlockOutput", cfg.getString("commandBlockOutput"));

		if (w.isGameRule("disableElytraMovementCheck"))
			w.setGameRuleValue("disableElytraMovementCheck", cfg.getString("disableElytraMovementCheck"));

		if (w.isGameRule("doDaylightCycle"))
			w.setGameRuleValue("doDaylightCycle", cfg.getString("doDaylightCycle"));

		if (w.isGameRule("doEntityDrops"))
			w.setGameRuleValue("doEntityDrops", cfg.getString("doEntityDrops"));

		if (w.isGameRule("doFireTick"))
			w.setGameRuleValue("doFireTick", cfg.getString("doFireTick"));

		if (w.isGameRule("doLimitedCrafting"))
			w.setGameRuleValue("doLimitedCrafting", cfg.getString("doLimitedCrafting"));

		if (w.isGameRule("doMobLoot"))
			w.setGameRuleValue("doMobLoot", cfg.getString("doMobLoot"));

		if (w.isGameRule("doMobSpawning"))
			w.setGameRuleValue("doMobSpawning", cfg.getString("doMobSpawning"));

		if (w.isGameRule("doTileDrops"))
			w.setGameRuleValue("doTileDrops", cfg.getString("doTileDrops"));

		if (w.isGameRule("doWeatherCycle"))
			w.setGameRuleValue("doWeatherCycle", cfg.getString("doWeatherCycle"));

		if (w.isGameRule("gameLoopFunction"))
			w.setGameRuleValue("gameLoopFunction", cfg.getString("gameLoopFunction"));

		if (w.isGameRule("keepInventory"))
			w.setGameRuleValue("keepInventory", cfg.getString("keepInventory"));

		if (w.isGameRule("logAdminCommands"))
			w.setGameRuleValue("logAdminCommands", cfg.getString("logAdminCommands"));

		if (w.isGameRule("maxCommandChainLength"))
			w.setGameRuleValue("maxCommandChainLength", cfg.getString("maxCommandChainLength"));

		if (w.isGameRule("maxEntityCramming"))
			w.setGameRuleValue("maxEntityCramming", cfg.getString("maxEntityCramming"));

		if (w.isGameRule("mobGriefing"))
			w.setGameRuleValue("mobGriefing", cfg.getString("mobGriefing"));

		if (w.isGameRule("naturalRegeneration"))
			w.setGameRuleValue("naturalRegeneration", cfg.getString("naturalRegeneration"));

		if (w.isGameRule("randomTickSpeed"))
			w.setGameRuleValue("randomTickSpeed", cfg.getString("randomTickSpeed"));

		if (w.isGameRule("reducedDebugInfo"))
			w.setGameRuleValue("reducedDebugInfo", cfg.getString("reducedDebugInfo"));

		if (w.isGameRule("sendCommandFeedback"))
			w.setGameRuleValue("sendCommandFeedback", cfg.getString("sendCommandFeedback"));

		if (w.isGameRule("showDeathMessages"))
			w.setGameRuleValue("showDeathMessages", cfg.getString("showDeathMessages"));

		if (w.isGameRule("spawnRadius"))
			w.setGameRuleValue("spawnRadius", cfg.getString("spawnRadius"));

		if (w.isGameRule("spectatorsGenerateChunks"))
			w.setGameRuleValue("spectatorsGenerateChunks", cfg.getString("spectatorsGenerateChunks"));
	}

	private static YamlConfiguration getConfig() {
		return YamlConfiguration.loadConfiguration(file);
	}

	public static void checkConfig() {
		File file = new File(WorldSystem.getInstance().getDataFolder(), "settings.yml");
		SettingsConfig.file = file;
		if (!file.exists()) {
			try {
				InputStream in = JavaPlugin.getPlugin(WorldSystem.class).getResource("settings.yml");
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				System.err.println("Wasn't able to create Config");
				e.printStackTrace();
			}
		}
	}

	private SettingsConfig() {
	}
}
