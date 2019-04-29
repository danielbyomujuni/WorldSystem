package de.butzlabben.world.config;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class DependenceConfig {

	private OfflinePlayer op;

	public DependenceConfig() {
		setConfig();
	}

	@SuppressWarnings("deprecation")
	public DependenceConfig(String s) {
		OfflinePlayer op = null;
		try {
			op = Bukkit.getOfflinePlayer(UUID.fromString(s));
		} catch (Exception ignored) {
		}
		if (op == null) {
			op = Bukkit.getOfflinePlayer(s);
		}
		this.op = op;
	}

	private void setConfig() {
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
		cfg.set("HighestID", -1);
		try {
			cfg.save(dconfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DependenceConfig(Player p) {
		this.op = p;
		refreshName();
	}

	public DependenceConfig(OfflinePlayer p) {
		this.op = p;
		refreshName();
	}

	public void refreshName() {
		if (hasWorld()) {
			File dconfig = new File("plugins//WorldSystem//dependence.yml");
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
			String uuid = this.op.getUniqueId().toString();
			cfg.set("Dependences." + uuid + ".ActualName", op.getName());
			try {
				cfg.save(dconfig);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void createNewEntry() {
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
		String uuid = this.op.getUniqueId().toString();
		int id = cfg.getInt("HighestID");
		id++;
		cfg.set("HighestID", id);
		cfg.set("Dependences." + uuid + ".ID", id);
		cfg.set("Dependences." + uuid + ".ActualName", op.getName());
		try {
			cfg.save(dconfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean hasWorld() {
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
		String uuid = op.getUniqueId().toString();
		//Fix for #40
        return cfg.isInt("Dependences." + uuid + ".ID");
    }

	public String getWorldname() {
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration dcfg = YamlConfiguration.loadConfiguration(dconfig);
		String uuid = op.getUniqueId().toString();
		int id = dcfg.getInt("Dependences." + uuid + ".ID");
		return "ID" + id + "-" + uuid;
	}

	public String getWorldNamebyOfflinePlayer() {
		String name = "";
		String uuid = op.getUniqueId().toString();
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
		if (cfg.getString("Dependences." + uuid + ".ActualName") == null) {
			name = "n";
		} else {
			name = "ID" + cfg.getInt("Dependences." + uuid + ".ID") + "-" + uuid;
		}
		return name;
	}

	public void setLastLoaded() {
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
		String uuid = op.getUniqueId().toString();
		cfg.set("Dependences." + uuid + ".last_loaded", System.currentTimeMillis());
		try {
			cfg.save(dconfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getHighestID() {
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration dcfg = YamlConfiguration.loadConfiguration(dconfig);
		return dcfg.getInt("HighestID");
	}

	public int getID() {
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration dcfg = YamlConfiguration.loadConfiguration(dconfig);
		return dcfg.getInt("Dependences." + op.getUniqueId().toString() + ".ID");
	}

	public OfflinePlayer getOwner() {
		return op;
	}

	public static void checkWorlds() {
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);

		long deleteTime = 1000 * 60 * 60 * 24 * PluginConfig.deleteAfter();
		long now = System.currentTimeMillis();
		for (String s : cfg.getConfigurationSection("Dependences").getKeys(false)) {
			if (!cfg.isLong("Dependences." + s + ".last_loaded") && !cfg.isInt("Dependences." + s + ".last_loaded"))
				continue;
			long lastLoaded = cfg.getLong("Dependences." + s + ".last_loaded");
			long diff = now - lastLoaded;
			if (diff > deleteTime) {
				Bukkit.getConsoleSender().sendMessage(
						PluginConfig.getPrefix() + "World of " + s + " was not loaded for too long. Deleting!");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ws delete " + s);
			}
		}
	}
}
