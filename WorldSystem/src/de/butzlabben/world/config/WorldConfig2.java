package de.butzlabben.world.config;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
//import java.util.List;
//import java.util.UUID;
//
//import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import de.butzlabben.event.WorldToggleFireEvent;
import de.butzlabben.event.WorldToggleTntEvent;
import de.butzlabben.world.wrapper.SystemWorld;

public class WorldConfig2 {

	public static File getWorldFile(String worldname) {
		File worldconfig = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		if (!worldconfig.exists()) {
			worldconfig = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		}
		return worldconfig;
	}

	public static void saveConfig(YamlConfiguration cfg, File file) {
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static UUID[] getMembersFiltered(String worldname) {
		File file = getWorldFile(worldname);
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if (cfg.getConfigurationSection("Members") == null)
			return null;
		Set<String> players = cfg.getConfigurationSection("Members").getKeys(false);
		Set<UUID> set = players.stream().map(new Function<String, UUID>() {
			@Override
			public UUID apply(String t) {
				return UUID.fromString(t);
			}
		}).filter(uuid -> Bukkit.getOfflinePlayer(uuid) != null && Bukkit.getOfflinePlayer(uuid).getName() != null)
				.collect(Collectors.toSet());
		return set.toArray(new UUID[] {});
	}

	public static UUID[] getMembers(String worldname) {
		File file = getWorldFile(worldname);
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		Set<String> players = cfg.getConfigurationSection("Members").getKeys(false);
		UUID[] uuids = new UUID[players.size()];
		int i = 0;
		for (String s : players) {
			uuids[i] = UUID.fromString(s);
			i++;
		}
		return uuids;
	}

	public static boolean isMember(OfflinePlayer op, String worldname) {
		File worldconfig = getWorldFile(worldname);
		if (!worldconfig.exists())
			return false;
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(worldconfig);
		UUID uuid1 = UUID.fromString(cfg.getString("Informations.Owner.PlayerUUID"));
		cfg.set("Informations.Owner.Actualname", Bukkit.getOfflinePlayer(uuid1).getName());
		String uuid = op.getUniqueId().toString();
		if (uuid.equals(cfg.getString("Members." + uuid + ".PlayerUUID"))) {
			cfg.set("Members." + uuid + ".Actualname", op.getName());
			saveConfig(cfg, worldconfig);
			return true;
		}
		saveConfig(cfg, worldconfig);
		return false;
	}

	public void createConfig(Player p) {
		String uuid = p.getUniqueId().toString();
		DependenceConfig dc = new DependenceConfig(p);
		File file = new File(PluginConfig.getWorlddir() + "ID" + dc.getID() + "-" + uuid + "/worldconfig.yml");
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("Error while creating worldconfig for " + p.getName());
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set("Informations.ID", dc.getID());
		cfg.set("Informations.Owner.PlayerUUID", uuid);
		cfg.set("Informations.Owner.Actualname", p.getName());
		cfg.set("Settings.TNTDamage", false);
		cfg.set("Settings.Fire", false);
		cfg.set("Members", null);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error while saving worldconfig for " + p.getName());
		}
	}

	public static boolean hasPermission(UUID setter, UUID world, String permission) {

		return true;
	}

	public static void addMember(Player owner, OfflinePlayer target) {
		DependenceConfig dc = new DependenceConfig(owner);
		String worldname = dc.getWorldname();
		File file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		if (!file.exists()) {
			worldname = dc.getOldWorldname();
			file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		}
		if (!file.exists()) {
			worldname = dc.getWorldname();
			file = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		}
		if (!file.exists()) {
			worldname = dc.getOldWorldname();
			file = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		}
		if (!file.exists())
			throw new IllegalArgumentException("This World does not exist");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		String uuid = target.getUniqueId().toString();
		cfg.set("Members." + uuid + ".PlayerUUID", uuid);
		cfg.set("Members." + uuid + ".ActualName", target.getName());
		cfg.set("Members." + uuid + ".Permissions.ChangeGM", true);
		cfg.set("Members." + uuid + ".Permissions.CanBuild", true);
		cfg.set("Members." + uuid + ".Permissions.CanTP", false);
		saveConfig(cfg, file);
	}

	public static void delMember(Player owner, OfflinePlayer target) {
		DependenceConfig dc = new DependenceConfig(owner);
		String worldname = dc.getWorldname();
		File file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		if (!file.exists()) {
			worldname = dc.getOldWorldname();
			file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		}
		if (!file.exists()) {
			worldname = dc.getWorldname();
			file = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		}
		if (!file.exists()) {
			worldname = dc.getOldWorldname();
			file = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		}
		if (!file.exists())
			throw new IllegalArgumentException("This World does not exist");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		String uuid = target.getUniqueId().toString();
		cfg.set("Members." + uuid, null);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void changeTnTDamage(Player p) {
		DependenceConfig dc = new DependenceConfig(p);
		if (!dc.hasWorld()) {
			p.sendMessage(MessageConfig.getNoWorldOwn());
			return;
		}
		String worldname = dc.getWorldname();
		File file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		if (!file.exists()) {
			worldname = dc.getOldWorldname();
			file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		}
		if (!file.exists()) {
			worldname = dc.getWorldname();
			file = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		}
		if (!file.exists()) {
			worldname = dc.getOldWorldname();
			file = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		}

		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		WorldToggleTntEvent event = new WorldToggleTntEvent(p, SystemWorld.getSystemWorld(worldname),
				cfg.getBoolean("Settings.TNTDamage"));
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		if (cfg.getBoolean("Settings.TNTDamage")) {
			cfg.set("Settings.TNTDamage", false);
			p.sendMessage(MessageConfig.getToggleTntDisabled());
		} else {
			cfg.set("Settings.TNTDamage", true);
			p.sendMessage(MessageConfig.getToggleTntEnabled());
		}
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void changeFireDamage(Player p) {
		DependenceConfig dc = new DependenceConfig(p);
		if (!dc.hasWorld()) {
			p.sendMessage(MessageConfig.getNoWorldOwn());
			return;
		}
		String worldname = dc.getWorldname();
		File file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		if (!file.exists()) {
			worldname = dc.getOldWorldname();
			file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		}
		if (!file.exists()) {
			worldname = dc.getWorldname();
			file = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		}
		if (!file.exists()) {
			worldname = dc.getOldWorldname();
			file = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		WorldToggleFireEvent event = new WorldToggleFireEvent(p, SystemWorld.getSystemWorld(worldname),
				cfg.getBoolean("Settings.TNTDamage"));
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		if (cfg.getBoolean("Settings.Fire")) {
			cfg.set("Settings.Fire", false);
			p.sendMessage(MessageConfig.getToggleFireDisabled());
		} else {
			cfg.set("Settings.Fire", true);
			p.sendMessage(MessageConfig.getToggleFireEnabled());
		}
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getInfos(Player p, String worldname) {
		File file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		if (!file.exists())
			return;
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		p.sendMessage(MessageConfig.getInfoOwner().replaceAll("%data", cfg.getString("Informations.Owner.Actualname")));
		p.sendMessage(MessageConfig.getInfoId().replaceAll("%data", String.valueOf(cfg.getInt("Informations.ID"))));
		p.sendMessage(MessageConfig.getInfoTnt().replaceAll("%data", cfg.getBoolean("Settings.TNTDamage")
				? MessageConfig.getInfoEnabled() : MessageConfig.getInfoDisabled()));
		p.sendMessage(MessageConfig.getInfoFire().replaceAll("%data",
				cfg.getBoolean("Settings.Fire") ? MessageConfig.getInfoEnabled() : MessageConfig.getInfoDisabled()));
		StringBuilder sb = new StringBuilder();
		if (cfg.getConfigurationSection("Members") != null) {
			for (String s : cfg.getConfigurationSection("Members").getKeys(false)) {
				UUID uuid = UUID.fromString(cfg.getString("Members." + s + ".PlayerUUID"));
				OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
				if (op == null || op.getName() == null) {
					try {
						GameProfile prof = de.butzlabben.world.GameProfileBuilder.fetch(uuid);
						sb.append(prof.getName()).append(" ");
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else
					sb.append(op.getName()).append(" ");

			}
			p.sendMessage(MessageConfig.getInfoMember().replaceAll("%data", sb.toString().trim()));
		}
	}
}
