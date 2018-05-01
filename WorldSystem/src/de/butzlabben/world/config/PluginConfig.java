package de.butzlabben.world.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.butzlabben.world.WorldSystem;
import net.md_5.bungee.api.ChatColor;

public class PluginConfig {

	private PluginConfig() {
	}

	private static File file;

	public static void checkConfig(File f) {
		file = f;
		if (file.exists()) {
			YamlConfiguration cfg = getConfig();
			if (false == (cfg.isString("worldfolder") && cfg.isString("worldsource") && cfg.isInt("unloadingtime")
					&& cfg.isBoolean("survival") && cfg.isString("language") && cfg.isString("prefix")
					&& cfg.isInt("request_expires") &&

					cfg.isInt("lagsystem.period_in_seconds") && cfg.isInt("lagsystem.entities_per_world")
					&& cfg.isBoolean("lagsystem.garbagecollector.use")
					&& cfg.isInt("lagsystem.garbagecollector.period_in_minutes") &&
					
					cfg.isString("worldgeneration.type") && cfg.isString("worldgeneration.environment")
					&& (cfg.isLong("worldgeneration.seed") || cfg.isInt("worldgeneration.seed")) &&

					cfg.isString("spawn.spawnpoint.world") && cfg.isInt("spawn.gamemode")
					&& (cfg.isDouble("spawn.spawnpoint.x") || cfg.isInt("spawn.spawnpoint.x"))
					&& (cfg.isDouble("spawn.spawnpoint.y") || cfg.isInt("spawn.spawnpoint.y"))
					&& (cfg.isDouble("spawn.spawnpoint.z") || cfg.isInt("spawn.spawnpoint.z"))
					&& (cfg.isDouble("spawn.spawnpoint.yaw") || cfg.isInt("spawn.spawnpoint.yaw"))
					&& (cfg.isDouble("spawn.spawnpoint.pitch") || cfg.isInt("spawn.spawnpoint.pitch")) &&

					cfg.isBoolean("worldspawn.use")
					&& (cfg.isDouble("worldspawn.spawnpoint.x") || cfg.isInt("worldspawn.spawnpoint.x"))
					&& (cfg.isDouble("worldspawn.spawnpoint.y") || cfg.isInt("worldspawn.spawnpoint.y"))
					&& (cfg.isDouble("worldspawn.spawnpoint.z") || cfg.isInt("worldspawn.spawnpoint.z"))
					&& (cfg.isDouble("worldspawn.spawnpoint.yaw") || cfg.isInt("worldspawn.spawnpoint.yaw"))
					&& (cfg.isDouble("worldspawn.spawnpoint.pitch") || cfg.isInt("worldspawn.spawnpoint.pitch")))) {
				try {
					Files.copy(file.toPath(),
							new File(file.getParentFile(), "config-broken-"
									+ new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new Date()) + ".yml").toPath(),
							StandardCopyOption.REPLACE_EXISTING);
					Files.delete(file.toPath());
					System.err.println("[WorldSystem] Config is broken, creating a new one!");
					checkConfig(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				InputStream in = JavaPlugin.getPlugin(WorldSystem.class).getResource("config.yml");
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				System.err.println("Wasn't able to create Config");
				e.printStackTrace();
			}
		}
	}

	private static YamlConfiguration getConfig() {
		return YamlConfiguration.loadConfiguration(file);
	}

	public static String getLicenseKey() {
		return getConfig().getString("license");
	}

	public static int getGCPeriod() {
		return getConfig().getInt("lagsystem.garbagecollector.period_in_minutes", 5);
	}

	public static boolean useGC() {
		return getConfig().getBoolean("lagsystem.garbagecollector.use", false);
	}

	public static int getEntitysPerWorld() {
		return getConfig().getInt("lagsystem.entities_per_world", 350);
	}

	public static int getLagCheckPeriod() {
		return getConfig().getInt("lagsystem.period_in_seconds", 10);
	}

	public static boolean useWorldSpawn() {
		return getConfig().getBoolean("worldspawn.use", true);
	}

	public static boolean isSurvival() {
		return getConfig().getBoolean("survival", false);
	}

	public static int getUnloadingTime() {
		return getConfig().getInt("unloadingtime", 20);
	}

	private final static GameMode[] gamemodes = new GameMode[] { GameMode.SURVIVAL, GameMode.CREATIVE,
			GameMode.ADVENTURE, GameMode.SPECTATOR };

	public static GameMode getSpawnGamemode() {
		return gamemodes[getConfig().getInt("spawn.gamemode", 2)];
	}

	public static String getWorlddir() {
		return getConfig().getString("worldfolder", "plugins/WorldSystem/Worlds") + "/";
	}

	public static String getExampleWorldName() {
		return getConfig().getString("worldsource", "");
	}

	public static String getLanguage() {
		return getConfig().getString("language", "en");
	}

	public static String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix", "§8[§3WorldSystem§8] §6"));
	}

	public static Location getWorldSpawn(World w) {
		return getLocation(getConfig(), "worldspawn.spawnpoint", w);
	}

	public static Location getSpawn() {
		YamlConfiguration cfg = getConfig();
		return getLocation(cfg, "spawn.spawnpoint", Bukkit.getWorld(cfg.getString("spawn.spawnpoint.world", "world")));
	}

	public static long getSeed() {
		return getConfig().getLong("worldgeneration.seed");
	}

	public static Environment getEnvironment() {
		YamlConfiguration cfg = getConfig();
		String t = cfg.getString("worldgeneration.environment");
		Environment e = Environment.NORMAL;
		try {
			e = Environment.valueOf(t.toUpperCase());
		} catch (Exception ex) {
			System.out.println("'" + t + "' is not a valid environment");
		}
		return e;
	}

	public static WorldType getWorldType() {
		YamlConfiguration cfg = getConfig();
		String t = cfg.getString("worldgeneration.type");
		WorldType wt = WorldType.NORMAL;
		try {
			wt = WorldType.valueOf(t.toUpperCase());
		} catch (Exception e) {
			System.out.println("'" + t + "' is not a valid worldtype");
		}
		return wt;
	}

	public static int getRequestExpire() {
		return getConfig().getInt("request_expires", 20);
	}

	private static Location getLocation(YamlConfiguration cfg, String path, World world) {
		return new Location(world, cfg.getDouble(path + ".x", 0), cfg.getDouble(path + ".y", 20),
				cfg.getDouble(path + ".z", 0), (float) cfg.getDouble(path + ".yaw", 0),
				(float) cfg.getDouble(path + ".pitch", 0));
	}
}
