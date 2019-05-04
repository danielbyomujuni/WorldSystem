package de.butzlabben.world.config;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Entry {

	private final OfflinePlayer op;
	private int id;
	private final String worldname;
	
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
        return !"n".equals(worldname);
    }
	
	protected Entry(OfflinePlayer op) {
		String uuid = op.getUniqueId().toString();
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
