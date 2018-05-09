package de.butzlabben.world.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.google.common.collect.Sets;

public class WorldConfig {

	private static File getWorldFile(String worldname) {
		File worldconfig = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		if (!worldconfig.exists()) {
			worldconfig = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		}
		if (!worldconfig.exists()) {
			worldconfig = new File(worldname + "/worldconfig.yml");
		}
		return worldconfig;
	}

	private static HashMap<String, WorldConfig> instances = new HashMap<>();

	/**
	 * Gets the worldconfig for a specific worldname or creates a new one of no
	 * exists
	 * 
	 * @param worldname
	 *            of the world
	 * @return WorldConfig of the world
	 */
	public static WorldConfig getWorldConfig(String worldname) {
		if (!instances.containsKey(worldname))
			instances.put(worldname, new WorldConfig(worldname));
		return instances.get(worldname).load();
	}

	private final UUID owner;
	private final int id;

	private final HashMap<UUID, HashSet<WorldPerm>> permissions = new HashMap<>();

	private String ownerName;
	private boolean fire, tnt;

	private WorldConfig(String worldname) {
		File file = getWorldFile(worldname);
		if (file.exists() == false)
			throw new IllegalArgumentException("WorldConfig doesn't exist");
		owner = UUID.fromString(worldname.substring(worldname.length() - 36));
		id = Integer.parseInt(worldname.split("-")[0].substring(2));
	}
	
	public static void create(Player p) {
		DependenceConfig dc = new DependenceConfig(p);
		String worldname = dc.getWorldname();
		File file = new File(PluginConfig.getWorlddir() + worldname +  "/worldconfig.yml");
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("Error while creating worldconfig for " + p.getUniqueId().toString());
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set("Informations.ID", dc.getID());
		cfg.set("Informations.Owner.PlayerUUID", p.getUniqueId().toString());
		cfg.set("Informations.Owner.Actualname", p.getName());
		cfg.set("Settings.TNTDamage", false);
		cfg.set("Settings.Fire", false);
		cfg.set("Members", null);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error while saving worldconfig for " +  p.getUniqueId().toString());
		}
	}

	/**
	 * Add a permission to a player of this world
	 * 
	 * @param player
	 *            who is edited
	 * @param perm
	 *            which permission will be added
	 * @return true if the permission was added, false if he already has the
	 *         permission
	 */
	public boolean addPermission(UUID player, WorldPerm perm) {
		if (owner.equals(player))
			throw new IllegalArgumentException("Permissions of the owner cannot change");
		HashSet<WorldPerm> perms = permissions.get(player);
		if (perms == null) {
			perms = new HashSet<>();
			permissions.put(player, perms);
		}
		return perms.add(perm);
	}

	/**
	 * Remove a permission from a player of this world
	 * 
	 * @param player
	 *            who is edited
	 * @param perm
	 *            which permission will be removed
	 * @return true if the permission was removed, false if he doesn't have the
	 *         permission
	 */
	public boolean removePermission(UUID player, WorldPerm perm) {
		if (owner.equals(player))
			throw new IllegalArgumentException("Permissions of the owner cannot change");
		HashSet<WorldPerm> perms = permissions.get(player);
		if (perms == null) {
			return false;
		}
		return perms.remove(perm);
	}

	/**
	 * Remove all permissions from a player of this world
	 * 
	 * @param player
	 *            who is edited
	 * @return true if a permission was removed false otherwise
	 */
	public boolean removeAllPermissions(UUID player) {
		if (owner.equals(player))
			throw new IllegalArgumentException("Permissions of the owner cannot change");
		HashSet<WorldPerm> perms = permissions.remove(player);
		return perms != null && perms.size() != 0;
	}

	/**
	 * Add all permissions to a player of this world
	 * 
	 * @param player
	 *            who is edited
	 * @return true if the permissions of the player changed, false otherwiste
	 */
	public boolean addAllPermissions(UUID player) {
		if (owner.equals(player))
			throw new IllegalArgumentException("Permissions of the owner cannot change");
		HashSet<WorldPerm> perms = permissions.get(player);
		if (perms == null) {
			perms = new HashSet<>();
			permissions.put(player, perms);
		}
		return perms.addAll(Sets.newHashSet(WorldPerm.values()));
	}

	/**
	 * Checks the permission of a player
	 * 
	 * @param player
	 *            who gets their permission checked
	 * @param perm
	 *            to check
	 * @return true if the player has the permission, false otherwise
	 */
	public boolean hasPermission(UUID player, WorldPerm perm) {
		if (owner.equals(player))
			return true;
		HashSet<WorldPerm> perms = permissions.get(player);
		return perms != null && perms.contains(perm);
	}

	/**
	 * Get all permissions of a player
	 * 
	 * @param player
	 *            from who to get the permissions
	 * @return all permissions for this player
	 */
	public HashSet<WorldPerm> getPermissions(UUID player) {
		if (owner == player) {
			return Sets.newHashSet(WorldPerm.values());
		}
		HashSet<WorldPerm> perms = permissions.get(player);
		if (perms == null) {
			return Sets.newHashSet();
		}
		return Sets.newHashSet(perms);
	}

	/**
	 * Adds a player to the world
	 * 
	 * @param player
	 *            who gets added
	 * @return true if the player was no member
	 */
	public boolean addMember(UUID player) {
		return addPermission(player, WorldPerm.MEMBER);
	}

	/**
	 * Try to add a player to the world
	 * 
	 * @param player
	 *            who's permission gets checked
	 * @param target
	 *            to add to the world
	 * @return true if the player was successfully added
	 */
	public boolean addMember(UUID player, UUID target) {
		if (hasPermission(player, WorldPerm.EDITMEMBERS)) {
			return addMember(target);
		}
		return false;
	}

	/**
	 * @return the owner of this world
	 */
	public UUID getOwner() {
		return owner;
	}

	/**
	 * Is the player a member of this world
	 * 
	 * @param player
	 *            to check
	 * @return if the player is a member of this world
	 */
	public boolean isMember(UUID player) {
		return hasPermission(player, WorldPerm.MEMBER);
	}

	/**
	 * Removes a Member from this world
	 * 
	 * @param player
	 *            to remove
	 * @return if the player was a member of this world
	 */
	public boolean removeMember(UUID player) {
		return removeAllPermissions(player);
	}

	/**
	 * Try to remove a Member from this world
	 * 
	 * @param player
	 *            who gets his permissions checked
	 * @param target
	 *            to remove
	 * @return if the player was a member of this world
	 */
	public boolean removeMember(UUID player, UUID target) {
		if (hasPermission(player, WorldPerm.EDITMEMBERS) && hasPermission(player, WorldPerm.EDITMEMBERS) == false) {
			return removeMember(target);
		}
		return false;
	}

	/**
	 * Checks wheater a player can build or not
	 * @param player to check
	 * @return if the can build or not
	 */
	public boolean canBuild(UUID player) {
		return hasPermission(player, WorldPerm.BUILD);
	}

	/**
	 * Allow or disallow a player to build on this world
	 * @param player to edit
	 * @param allowed if he is allowed to build
	 */
	public void setBuild(UUID player, boolean allowed) {
		if (allowed) {
			addPermission(player, WorldPerm.BUILD);
		} else {
			removePermission(player, WorldPerm.BUILD);
		}
	}

	/**
	 * Allow or disallow a player to build with the permissions of a spefic player
	 * @param player who gets his permission checked
	 * @param target to allow or disallow
	 * @param allowed if he is allowed to build
	 * @return if the player has the permissions
	 */
	public boolean setBuild(UUID player, UUID target, boolean allowed) {
		if (isAllowedToAdministrateMember(player, target) == false)
			return false;
		setBuild(target, allowed);
		return true;
	}

	private boolean isAllowedToAdministrateMember(UUID player, UUID target) {
		return target != owner && player != target && hasPermission(player, WorldPerm.ADMINISTRATEMEMBERS)
				&& hasPermission(player, WorldPerm.ADMINISTRATEMEMBERS);
	}

	/**
	 * Checks wheater a player can build on this world or not
	 * @param player to check
	 * @return if the player can build
	 */
	public boolean canGamemode(UUID player) {
		return hasPermission(player, WorldPerm.GAMEMODE);
	}

	/**
	 * Allow or disallow a player to change his gamemode
	 * @param player to allow or disallow
	 * @param allowed if he is allowed to change his gamemode or not
	 */
	public void setGamemode(UUID player, boolean allowed) {
		if (allowed) {
			addPermission(player, WorldPerm.GAMEMODE);
		} else {
			removePermission(player, WorldPerm.GAMEMODE);
		}
	}

	public boolean setGamemode(UUID player, UUID target, boolean allowed) {
		if (isAllowedToAdministrateMember(player, target) == false)
			return false;
		setGamemode(target, allowed);
		return true;
	}

	public boolean canTeleport(UUID player) {
		return hasPermission(player, WorldPerm.TELEPORT);
	}

	/**
	 * Allow or disallow a player to teleport
	 * @param player to allow or disallow
	 * @param allowed if he is allowed to teleport or not
	 */
	public void setTeleport(UUID player, boolean allowed) {
		if (allowed) {
			addPermission(player, WorldPerm.TELEPORT);
		} else {
			removePermission(player, WorldPerm.TELEPORT);
		}
	}

	public boolean setTeleport(UUID player, UUID target, boolean allowed) {
		if (isAllowedToAdministrateMember(player, target) == false)
			return false;
		setTeleport(target, allowed);
		return true;
	}

	public HashSet<UUID> getMembers() {
		return Sets.newHashSet(permissions.keySet());
	}

	public WorldConfig save() throws IOException {
		File file = getWorldFile(getWorldName());
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		cfg.set("Informations.ID", id);
		cfg.set("Informations.Owner.Actualname", ownerName);
		cfg.set("Informations.Owner.PlayerUUID", owner.toString());
		cfg.set("Settings.TNTDamage", tnt);
		cfg.set("Settings.Fire", fire);

		cfg.set("Members", null);

		for (java.util.Map.Entry<UUID, HashSet<WorldPerm>> entry : permissions.entrySet()) {
			ArrayList<String> array = new ArrayList<>(entry.getValue().size());
			for (WorldPerm perm : entry.getValue()) {
				array.add(perm.name());
			}
			cfg.set("Members." + entry.getKey(), array);
		}

		cfg.save(file);
		return this;
	}

	private String getWorldName() {
		return "ID" + id + "-" + owner;
	}

	public WorldConfig load() {
		File file = getWorldFile(getWorldName());
		permissions.clear();

		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		ownerName = cfg.getString("Informations.Owner.Actualname", "Unknown Playername");
		tnt = cfg.getBoolean("Settings.TNTDamage", true);
		fire = cfg.getBoolean("Settings.Fire", true);

		if (membersOldFormatted(cfg)) {
			for (String s : cfg.getConfigurationSection("Members").getKeys(false)) {
				HashSet<WorldPerm> perms = new HashSet<>();
				perms.add(WorldPerm.MEMBER);
				if (cfg.getBoolean("Members." + s + ".Permissions.CanBuild"))
					perms.add(WorldPerm.BUILD);
				if (cfg.getBoolean("Members." + s + ".Permissions.CanTP"))
					perms.add(WorldPerm.TELEPORT);
				if (cfg.getBoolean("Members." + s + ".Permissions.CanBuild"))
					perms.add(WorldPerm.GAMEMODE);
				permissions.put(UUID.fromString(s), perms);
			}
			try {
				save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			ConfigurationSection section = cfg.getConfigurationSection("Members");
			if (section != null) {
				for (String suuid : section.getKeys(false)) {
					UUID uuid = UUID.fromString(suuid);
					List<String> list = section.getStringList(suuid);
					HashSet<WorldPerm> perms = new HashSet<>(list.size());
					for (String perm : list) {
						perms.add(WorldPerm.valueOf(perm));
					}
					permissions.put(uuid, perms);
				}
			}
		}
		return this;
	}

	private boolean membersOldFormatted(YamlConfiguration cfg) {
		if (cfg.getConfigurationSection("Members") == null)
			return false;
		String name = cfg.getString(
				"Members." + cfg.getConfigurationSection("Members").getKeys(false).iterator().next() + ".ActualName");
		return name != null;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public boolean isFire() {
		return fire;
	}

	public void setFire(boolean fire) {
		this.fire = fire;
	}

	/**
	 * Allow or Disallow Fire Damage on this world
	 * @param player
	 * @param tnt if tnt is enabled
	 * @return if the player has the permissions to change the value
	 */
	public boolean setFire(UUID player, boolean fire) {
		if (hasPermission(player, WorldPerm.ADMINISTRATEWORLD) == false)
			return false;
		setFire(fire);
		return true;
	}

	public boolean isTnt() {
		return tnt;
	}

	public void setTnt(boolean tnt) {
		this.tnt = tnt;
	}

	/**
	 * Allow or Disallow TNT Damage on this world
	 * @param player
	 * @param tnt if tnt is enabled
	 * @return if the player has the permissions to change the value
	 */
	public boolean setTnt(UUID player, boolean tnt) {
		if (hasPermission(player, WorldPerm.ADMINISTRATEWORLD) == false)
			return false;
		setTnt(tnt);
		return true;
	}

	/**
	 * Get the id of this world. The id is written in the filename at the xx position: 'IDxx-uuid.yml'
	 * @return id of this world
	 */
	public int getId() {
		return id;
	}

}
