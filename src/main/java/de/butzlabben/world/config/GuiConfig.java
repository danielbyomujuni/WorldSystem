package de.butzlabben.world.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
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
				String guiFileResource;
				if (WorldSystem.is1_13()) {
					guiFileResource = "1_13_gui.yml";
				} else {
					guiFileResource = "old_gui.yml";
				}
				InputStream in = JavaPlugin.getPlugin(WorldSystem.class).getResource(guiFileResource);
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				System.err.println("Wasn't able to create Config");
				e.printStackTrace();
			}
		}
		OrcItem.enabled = getEnabled();
		OrcItem.disabled = getDisabled();
		OrcItem.coming_soon = getComingSoon();
		OrcItem.back = getBack();
		OrcItem.fill = getFill();
	}

	public static YamlConfiguration getConfig() {
		try {
			return YamlConfiguration
					.loadConfiguration(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
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

	public static byte getData(FileConfiguration cfg, String path) {
		return (byte) cfg.getInt(path + ".data", 0);
	}
	
	public static String getTitle(FileConfiguration cfg, String path) {
		return cfg.getString(path + ".title");
	}	

	public static Material getMaterial(FileConfiguration cfg, String path) {
		try {
			return Material.valueOf(cfg.getString(path + ".material").toUpperCase());
		} catch (IllegalArgumentException ex) {
			Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "§cUnknown material: " + path);
			return null;
		}
	}

	public static OrcItem getItem(String path) {
		YamlConfiguration cfg = getConfig();
		try {
			return new OrcItem(getMaterial(cfg, path), getData(cfg, path), getDisplay(cfg, path), getLore(cfg, path));
		} catch (Exception ignored) {
		}
		try {
			return new OrcItem(getMaterial(cfg, path), getDisplay(cfg, path), getLore(cfg, path));
		} catch (Exception ignored) {
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
	
	private static OrcItem getBack() {
		return getItem("options.back");
	}
	
	private static OrcItem getFill() {
		return getItem("options.fill");
	}
	
	public static boolean isFill(String path) {
		return getConfig().getBoolean(path + ".fill");
	}

	public static Material getSkullItem() {
		return getMaterial(getConfig(), "options.players.playerhead");
	}
}
