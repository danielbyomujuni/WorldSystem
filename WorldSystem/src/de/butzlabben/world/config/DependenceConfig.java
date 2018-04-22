package de.butzlabben.world.config;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.butzlabben.world.WorldSystem;

public class DependenceConfig {

	private OfflinePlayer op;

	public DependenceConfig() {
		setConfig();
	}

	public DependenceConfig(String s) {
		@SuppressWarnings("deprecation")
		OfflinePlayer op = Bukkit.getOfflinePlayer(s);
		if(op == null)
			return;		
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

	public boolean createNewEntry() {
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
		String uuid = this.op.getUniqueId().toString();
		int id = cfg.getInt("HighestID");
		id++;
		if (Entry.entrys() >= WorldSystem.getMaxWorlds())
			return false;
		cfg.set("HighestID", id);
		cfg.set("Dependences." + uuid + ".ID", id);
		cfg.set("Dependences." + uuid + ".ActualName", op.getName());
		try {
			cfg.save(dconfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean hasWorld() {
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
		String uuid = op.getUniqueId().toString();
		String entry = cfg.getString("Dependences." + uuid + ".ActualName");
		if (entry != null) {
			return true;
		}
		return false;
	}

	public String getWorldname() {
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration dcfg = YamlConfiguration.loadConfiguration(dconfig);
		String uuid = op.getUniqueId().toString();
		int id = dcfg.getInt("Dependences." + uuid + ".ID");
		String worldname = "ID" + id + "-" + uuid;
		return worldname;
	}
	
	public String getOldWorldname() {
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration dcfg = YamlConfiguration.loadConfiguration(dconfig);
		String uuid = op.getUniqueId().toString();
		int id = dcfg.getInt("Dependences." + uuid + ".ID");
		String worldname = "ID" + id + " " + uuid;
		return worldname;
	}
	
	public String getOldWorldNamebyOfflinePlayer() {
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

	public String getWorldNamebyOfflinePlayer() {
		String name = "";
		String uuid = op.getUniqueId().toString();
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
		if (cfg.getString("Dependences." + uuid + ".ActualName") == null) {
			name = "n";
		} else {
			name = "ID" + cfg.getInt("Dependences." + uuid + ".ID") + " " + uuid;
		}
		return name;
	}

	public int getHighestID() {
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration dcfg = YamlConfiguration.loadConfiguration(dconfig);
		return dcfg.getInt("HighestID");
	}

	public int getID() {
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration dcfg = YamlConfiguration.loadConfiguration(dconfig);
		return dcfg.getInt("Dependences." + op.getUniqueId().toString() + ".ID");
	}

}
