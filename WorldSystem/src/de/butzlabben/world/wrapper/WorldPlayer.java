package de.butzlabben.world.wrapper;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;

import de.butzlabben.world.config.PluginConfig;

public class WorldPlayer {
	
	private OfflinePlayer p;
	private String worldname;
	
	/**
	 * @return the worldname, where the worldplayer object was created for
	 */
	public String getWorldname() {
		return worldname;
	}
	
	/**
	 * toggles building for this player
	 * @return whether can build or not
	 */
	public boolean toggleBuild() {
		Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");
		File file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		if (!file.exists()) {
			file = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		String uuid = p.getUniqueId().toString();
		if (cfg.getString("Members." + uuid + ".PlayerUUID") == null) 
			return false;
		
		boolean teleport = cfg.getBoolean("Members." + uuid + ".Permissions.CanBuild");
		teleport = !teleport;
		cfg.set("Members." + uuid + ".Permissions.CanBuild", teleport);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return teleport;
	}
	
	/**
	 * @return whether can build or not
	 */
	public boolean canBuild() {
		Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");
		File file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		String uuid = p.getUniqueId().toString();
		boolean b = cfg.getBoolean("Members." + uuid + ".Permissions.CanBuild");
		return b;
	}
	
	/**
	 * toggles teleporting for this player
	 * @return whether can teleport or not
	 */
	public boolean toggleTeleport() {
		Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");
		File file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		if (!file.exists()) {
			file = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		String uuid = p.getUniqueId().toString();
		if (cfg.getString("Members." + uuid + ".PlayerUUID") == null) 
			return false;
		
		boolean teleport = cfg.getBoolean("Members." + uuid + ".Permissions.CanTP");
		teleport = !teleport;
		cfg.set("Members." + uuid + ".Permissions.CanTP", teleport);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return teleport;
	}
	
	/**
	 * @return whether can teleport or not
	 */
	public boolean canTeleport() {
		Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");
		File file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		String uuid = p.getUniqueId().toString();
		boolean b = cfg.getBoolean("Members." + uuid + ".Permissions.CanTP");
		return b;
	}
	
	/**
	 * toggles gamemode changing for this player
	 * @return whether can change his gamemode or not
	 */
	public boolean toggleGamemode() {
		Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");
		File file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		if (!file.exists()) {
			file = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		String uuid = p.getUniqueId().toString();
		if (cfg.getString("Members." + uuid + ".PlayerUUID") == null) 
			return false;
		
		boolean changeGamemode = cfg.getBoolean("Members." + uuid + ".Permissions.ChangeGM");
		changeGamemode = !changeGamemode;
		cfg.set("Members." + uuid + ".Permissions.ChangeGM", changeGamemode);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return changeGamemode;
	}
	
	/**
	 * @return whether can change his gamemode or not
	 */
	public boolean canChangeGamemode() {
		Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");
		File file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		String uuid = p.getUniqueId().toString();
		boolean b = cfg.getBoolean("Members." + uuid + ".Permissions.ChangeGM");
		return b;
	}
	
	/**
	 * @return if he is a member
	 */
	public boolean isMember() {
		Preconditions.checkArgument(isOnSystemWorld(), "player must be for this on a systemworld");
		File worldconfig = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		if (!worldconfig.exists()) {
			worldconfig = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(worldconfig);
		String uuid = p.getUniqueId().toString();
		if (uuid.equals(cfg.getString("Members." + uuid + ".PlayerUUID"))) {
			cfg.set("Members." + uuid + ".Actualname", p.getName());
			try {
				cfg.save(worldconfig);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public WorldPlayer(OfflinePlayer p, String worldname) {
		this.p = p;
		this.worldname = worldname;
	}
	
	public WorldPlayer(Player p) {
		this(p, p.getWorld().getName());
	}

	/**
	 * @return if he is on a systemworld or not
	 */
	public boolean isOnSystemWorld() {
		File worldconfig = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		if (worldconfig.exists()) {
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(worldconfig);
			if(cfg.getString("Informations.Owner.PlayerUUID") == null) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * @return the given player
	 */
	public OfflinePlayer getPlayer() {
		return p;
	}

	
	/**
	 * @return if he ist the owner
	 */
	public boolean isOwnerofWorld() {
		File worldconfig = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(worldconfig);
		if (p.getUniqueId().toString().equals(cfg.getString("Informations.Owner.PlayerUUID"))) {
			String playerName = p.getName();
			cfg.set("Informations.Owner.Actualname", playerName);
			try {
				cfg.save(worldconfig);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param worldname of the world to be tested
	 * @return worldname if he is the owner of the specified world
	 */
	public boolean isMemberofWorld(String worldname) {
		File worldconfig = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		if (!worldconfig.exists()) {
			worldconfig = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(worldconfig);
		String uuid = p.getUniqueId().toString();
		if (uuid.equals(cfg.getString("Members." + uuid + ".PlayerUUID"))) {
			return true;
		}
		return false;
	}

}
