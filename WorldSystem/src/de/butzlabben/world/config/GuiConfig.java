package de.butzlabben.world.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.butzlabben.inventory.OrcItem;
import de.butzlabben.world.WorldSystem;

public class GuiConfig {

	private GuiConfig() {
	}

	private static File file;

	public static void checkConfig(File f) {
		file = f;
		if (file.exists() == false) {
			try {
				InputStream in = JavaPlugin.getPlugin(WorldSystem.class).getResource("gui.yml");
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				System.err.println("Wasn't able to create Config");
				e.printStackTrace();
			}
		}
		OrcItem.enabled = getEnabled();
		OrcItem.disabled = getDisabled();
		OrcItem.coming_soon = getComingSoon();
	}

	public static YamlConfiguration getConfig() {
		return YamlConfiguration.loadConfiguration(file);
	}

	public static int getSlot(String path) {
		YamlConfiguration cfg = getConfig();
		return (cfg.getInt(path + ".slot.row") - 1) * 9 + cfg.getInt(path + ".slot.col") - 1;
	}

	public static int getState(String path) {
		YamlConfiguration cfg = getConfig();
		return (cfg.getInt(path + ".state.row") - 1) * 9 + cfg.getInt(path + ".state.col") - 1;
	}

	public static boolean isEnabled(String path) {
		return getConfig().getBoolean(path + ".enabled", true);
	}

	public static int getRows(String path) {
		return getConfig().getInt(path + ".rows", 1);
	}

	public static String getDisplay(FileConfiguration cfg, String path) {
		return ChatColor.translateAlternateColorCodes('&', cfg.getString(path + ".display"));
	}

	public static ArrayList<String> getLore(FileConfiguration cfg, String path) {
		List<String> list = cfg.getStringList(path + ".lore");
		ArrayList<String> colored = new ArrayList<>(list.size());
		for (String s : list) {
			colored.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		return colored;
	}

	public static int getId(FileConfiguration cfg, String path) {
		return cfg.getInt(path + ".material");
	}

	public static byte getData(FileConfiguration cfg, String path) {
		return (byte) cfg.getInt(path + ".data");
	}

	public static Material getMaterial(FileConfiguration cfg, String path) {
		try {
			return Material.valueOf(cfg.getString(path + ".material").toUpperCase());
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	public static OrcItem getItem(String path) {
		YamlConfiguration cfg = getConfig();
		try {
			return new OrcItem(getId(cfg, path), getData(cfg, path), getDisplay(cfg, path), getLore(cfg, path));
		} catch (Exception e) {
		}
		try {
			return new OrcItem(getMaterial(cfg, path), getDisplay(cfg, path), getLore(cfg, path));
		} catch (Exception e) {
		}
		return OrcItem.error.clone().setDisplay("§c" + path);
	}

	public static OrcItem getEnabled() {
		return getItem("options.enabled");
	}

	public static OrcItem getDisabled() {
		return getItem("options.disabled");
	}

	public static OrcItem getComingSoon() {
		return getItem("options.coming_soon");
	}

}
