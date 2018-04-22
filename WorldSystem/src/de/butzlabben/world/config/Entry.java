package de.butzlabben.world.config;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

public class Entry {

	private OfflinePlayer op;
	private String uuid;
	private int id;
	private String worldname;
	
	public static int entrys() {
		int entrys = 0;
		for(OfflinePlayer op : Bukkit.getOfflinePlayers()) {
			Entry e = new Entry(op);
			if(e.hasWorld())
				++entrys;
		}
		return entrys;
	}
	
	protected OfflinePlayer getOfflinePlayer() {
		return op;
	}
	
	protected int getID() {
		return id;
	}
	
	protected String getWorldname() {
		return worldname;
	}

	protected boolean hasWorld() {
		if(worldname.equals("n"))
			return false;
		return true;
	}
	
	protected Entry(OfflinePlayer op) {
		uuid = op.getUniqueId().toString();
		this.op = op;
		File dconfig = new File("plugins//WorldSystem//dependence.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
		if (cfg.getString("Dependences." + uuid + ".ActualName") == null) {
			worldname = "n";
		} else {
			worldname = "ID" + cfg.getInt("Dependences." + uuid + ".ID") + " " + uuid;
			id = cfg.getInt("Dependences." + uuid + ".ID");
		}
	}
}
