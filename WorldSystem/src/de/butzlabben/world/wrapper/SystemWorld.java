package de.butzlabben.world.wrapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import com.google.common.base.Preconditions;

import de.butzlabben.event.WorldCreateEvent;
import de.butzlabben.event.WorldLoadEvent;
import de.butzlabben.event.WorldUnloadEvent;
import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.config.WorldConfig;

/**
 * This class represents a systemworld, loaded or not
 * 
 * @author Butzlabben
 * @since 14.02.2018
 */
public class SystemWorld {

	private World w;
	private String worldname;
	private boolean unloading = false;
	private boolean creating = false;

	private static HashMap<String, SystemWorld> cached = new HashMap<>();

	/**
	 * This method is the online way to get a system world instance
	 * 
	 * @param worldname
	 *            as in minecraft
	 * @return a systemworld instance if it is a systemworld or null if is not a
	 *         systemworld
	 * @exception NullPointerException
	 *                worldname == null
	 */
	public static SystemWorld getSystemWorld(String worldname) {
		Preconditions.checkNotNull(worldname, "worldname must not be null");
		if (cached.containsKey(worldname))
			return cached.get(worldname);
		SystemWorld sw = new SystemWorld(worldname);
		if (sw != null && sw.exists()) {
			cached.put(worldname, sw);
			return sw;
		}
		return null;
	}

	/**
	 * @param w
	 *            a world in bukkit, no matter if systemworld or not Trys to
	 *            unload a systemworld later, with the given delay in the config
	 */
	public static void tryUnloadLater(World w) {
		if (w != null)
			Bukkit.getScheduler().runTaskLater(WorldSystem.getInstance(), () -> {
				if (w.getPlayers().size() == 0) {
					SystemWorld sw = SystemWorld.getSystemWorld(w.getName());
					if (sw != null && sw.isLoaded())
						sw.unloadLater(w);
				}
			}, 20);
	}

	private SystemWorld(String worldname) {
		this.worldname = worldname;
		w = Bukkit.getWorld(worldname);
	}

	/**
	 * Trys to force-unload this world
	 * 
	 * @param w
	 *            associated world
	 * @exception NullPointerException
	 *                w == null
	 */
	public void directUnload(World w) {
		Preconditions.checkNotNull(w, "world must not be null");
		unloading = true;
		w.save();
		Chunk[] arrayOfChunk;
		int j = (arrayOfChunk = w.getLoadedChunks()).length;
		for (int i = 0; i < j; i++) {
			Chunk c = arrayOfChunk[i];
			c.unload();
		}
		for (Player a : w.getPlayers()) {
			a.teleport(PluginConfig.getSpawn());
			a.setGameMode(PluginConfig.getSpawnGamemode());
		}
		if (unloading) {
			if (Bukkit.unloadWorld(w, true)) {
				File worldinserver = new File(Bukkit.getWorldContainer(), worldname);
				File worlddir = new File(PluginConfig.getWorlddir());
				try {
					FileUtils.moveDirectoryToDirectory(worldinserver, worlddir, false);
					Bukkit.getWorlds().remove(w);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Trys to unload this world later, with the given delay in the config
	 * 
	 * @param w
	 *            the associated world
	 * @exception NullPointerException
	 *                w == null
	 */
	public void unloadLater(World w) {
		Preconditions.checkNotNull(w, "world must not be null");
		WorldUnloadEvent event = new WorldUnloadEvent(this);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		//Set unloading to true
		unloading = true;
		w.save();
		Chunk[] arrayOfChunk;
		int j = (arrayOfChunk = w.getLoadedChunks()).length;
		for (int i = 0; i < j; i++) {
			Chunk c = arrayOfChunk[i];
			c.unload();
		}
		for (Player a : w.getPlayers()) {
			a.teleport(PluginConfig.getSpawn());
			a.setGameMode(PluginConfig.getSpawnGamemode());
		}
		
		Bukkit.getScheduler().runTaskLater(WorldSystem.getInstance(), new Runnable() {
			@Override
			public void run() {
				// Still in world unloading process?
				if (unloading && w.getPlayers().size() == 0) {
					if (Bukkit.unloadWorld(w, true)) {
						File worldinserver = new File(Bukkit.getWorldContainer(), worldname);
						File worlddir = new File(PluginConfig.getWorlddir());
						try {
							FileUtils.moveDirectoryToDirectory(worldinserver, worlddir, false);
							Bukkit.getWorlds().remove(w);
							unloading = false;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}, 20 * PluginConfig.getUnloadingTime());
	}

	/**
	 * Trys to load this world, and messages the player about the process
	 * 
	 * @param p
	 *            the player to teleport on the world
	 * @exception NullPointerException
	 *                if p is null
	 * @exception IllegalArgumentException
	 *                if player is not online
	 */
	public void load(Player p) {
		Preconditions.checkNotNull(p, "player must not be null");
		Preconditions.checkArgument(p.isOnline(), "player must be online");

		if (creating)
			return;

		
		WorldLoadEvent event = new WorldLoadEvent(p, this);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		
		unloading = false;
		
		p.sendMessage(MessageConfig.getSettingUpWorld());
		// Move World into Server dir
		String worlddir = PluginConfig.getWorlddir();
		File world = new File(worlddir + "/" + worldname);
		if (!world.exists()) {
			world = new File(Bukkit.getWorldContainer(), worldname);
		} else {
			if (new File(Bukkit.getWorldContainer(), worldname).exists()
					&& new File(PluginConfig.getWorlddir() + "/" + worldname).exists()) {
				System.err.println("World " + worldname + " exists twice!");
//				try {
//					FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer(), worldname));
//				} catch (IOException e) {
//					p.sendMessage(PluginConfig.getPrefix() + "§cError");
//					e.printStackTrace();
//				}
			}
			try {
				FileUtils.moveDirectoryToDirectory(world, Bukkit.getWorldContainer(), false);
			} catch (IOException e) {
				System.err.println("Couldn't load world of " + p.getName());
				p.sendMessage(PluginConfig.getPrefix() + "§cError");
				e.printStackTrace();
			}
		}
		
		if (worldname.charAt(worldname.length() - 37) == ' ') {
			StringBuilder myName = new StringBuilder(worldname);
			myName.setCharAt(worldname.length() - 37, '-');
			world.renameTo(new File(Bukkit.getWorldContainer(), myName.toString()));
			worldname = myName.toString();
		}

		WorldCreator creator = PluginConfig.getWorldCreator(worldname);

		World w = Bukkit.getWorld(worldname);
		if (w == null)
			w = Bukkit.createWorld(creator);

		this.w = w;
		if (PluginConfig.isSurvival()) {
			p.setGameMode(GameMode.SURVIVAL);
		} else {
			p.setGameMode(GameMode.CREATIVE);
		}
		if (PluginConfig.useWorldSpawn()) {
			p.teleport(PluginConfig.getWorldSpawn(w));
		} else {
			p.teleport(w.getSpawnLocation());
		}
		
		OfflinePlayer owner = Bukkit.getOfflinePlayer(WorldConfig.getWorldConfig(worldname).getOwner());
		DependenceConfig dc = new DependenceConfig(owner);
		dc.setLastLoaded();
	}

	/**
	 * Trys to create a new systemworld with all entries etc. finally loads the
	 * world
	 * 
	 * @param p
	 *            Player to create the world for
	 * @return whether it succesfull or not
	 */
	public static boolean create(Player p) {
		DependenceConfig dc = new DependenceConfig(p);

		String uuid = p.getUniqueId().toString();
		int id = dc.getHighestID() + 1;
		String worldname = "ID" + id + "-" + uuid;

		WorldCreator creator = PluginConfig.getWorldCreator(worldname);

		WorldCreateEvent event = new WorldCreateEvent(p, creator);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return false;

		dc.createNewEntry();

		String worlddir = PluginConfig.getWorlddir();
		File exampleworld = new File("plugins//WorldSystem//worldsources//" + PluginConfig.getExampleWorldName());
		if (new File("plugins//WorldSystem//worldsources//" + PluginConfig.getExampleWorldName() + "/uid.dat")
				.exists()) {
			new File("plugins//WorldSystem//worldsources//" + PluginConfig.getExampleWorldName() + "/uid.dat").delete();
		}

		File newworld = new File(worlddir + "/" + worldname);
		try {
			FileUtils.copyDirectory(exampleworld, newworld);
		} catch (IOException e) {
			System.err.println("Couldn't create world for " + p.getName());
			e.printStackTrace();
		}

		WorldConfig.create(p);

		if (PluginConfig.getExampleWorldName() == null || PluginConfig.getExampleWorldName().equals("")
				|| !exampleworld.exists()) {
			// Move World into Server dir
			File world = new File(worlddir + "/" + worldname);
			if (!world.exists()) {
				world = new File(Bukkit.getWorldContainer(), worldname);
			} else {
				if (new File(Bukkit.getWorldContainer(), worldname).exists()
						&& new File(PluginConfig.getWorlddir() + "/" + worldname).exists()) {
					try {
						FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer(), worldname));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					FileUtils.moveDirectoryToDirectory(world, Bukkit.getWorldContainer(), false);
				} catch (IOException e) {
					System.err.println("Couldn't load world of " + p.getName());
					e.printStackTrace();
				}
			}
			// For #16
			WorldSystem.creator.create(event.getWorldCreator(), null);
		}
		return true;
	}

	/**
	 * @return if the world is loaded
	 */
	public boolean isLoaded() {
		File worldAsDir = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		World w = Bukkit.getWorld(worldname);
		if (worldAsDir.exists() && w != null)
			return true;
		return false;
	}

	private boolean exists() {
		File worldAsDir = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
		if (worldAsDir.exists()) {
			return true;
		}
		worldAsDir = new File(PluginConfig.getWorlddir() + "/" + worldname + "/worldconfig.yml");
		if (worldAsDir.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * Teleports the player to the world with the given settings in the config
	 * 
	 * @param p
	 *            player to teleport
	 * @exception NullPointerException
	 *                if player is null
	 * @exception IllegalArgumentException
	 *                if player is not online
	 */
	public void teleportToWorldSpawn(Player p) {
		Preconditions.checkNotNull(p, "player must not be null");
		Preconditions.checkArgument(p.isOnline(), "player must be online");
		unloading = false;
		w = Bukkit.getWorld(worldname);
		if (w == null)
			return;
		if (PluginConfig.useWorldSpawn()) {
			p.teleport(PluginConfig.getWorldSpawn(w));
		} else {
			p.teleport(w.getSpawnLocation());
		}
		if (PluginConfig.isSurvival()) {
			p.setGameMode(GameMode.SURVIVAL);
		} else {
			p.setGameMode(GameMode.CREATIVE);
		}
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return w;
	}

	public void stopCreating() {
		creating = false;
	}

	public boolean isCreating() {
		return creating;
	}

	/**
	 * @return the worldname
	 */
	public String getName() {
		return worldname;
	}
}