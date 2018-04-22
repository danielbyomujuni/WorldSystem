package de.butzlabben.world.listener;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import de.butzlabben.world.wrapper.WorldPlayer;

public class BlockListener implements Listener {
	
	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		World w = e.getLocation().getWorld();
		File file = new File(Bukkit.getWorldContainer(), w.getName() + "/worldconfig.yml");
		if (file.exists()) {
			e.setCancelled(true);
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			boolean b = cfg.getBoolean("Settings.TNTDamage");
			e.setCancelled(!b);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (p.hasPermission("ws.build"))
			return;
		String worldname = p.getWorld().getName();
		WorldPlayer wp = new WorldPlayer(p, worldname);
		if (!wp.isOnSystemWorld())
			return;
		if (!wp.isOwnerofWorld()) {
			File file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			String uuid = p.getUniqueId().toString();
			boolean b = cfg.getBoolean("Members." + uuid + ".Permissions.CanBuild");
			e.setCancelled(!b);
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (p.hasPermission("ws.build"))
			return;
		String worldname = p.getWorld().getName();
		WorldPlayer wp = new WorldPlayer(p, worldname);
		if (!wp.isOnSystemWorld())
			return;
		if (!wp.isOwnerofWorld()) {
			File file = new File(Bukkit.getWorldContainer(), worldname + "/worldconfig.yml");
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			String uuid = p.getUniqueId().toString();
			boolean b = cfg.getBoolean("Members." + uuid + ".Permissions.CanBuild");
			e.setCancelled(!b);
		}
	}
	@EventHandler
	public void onFire(BlockIgniteEvent e) {
		World w = e.getBlock().getWorld();
		File file = new File(Bukkit.getWorldContainer(), w.getName() + "/worldconfig.yml");
		if (file.exists()) {
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			boolean b = cfg.getBoolean("Settings.Fire");
			e.setCancelled(!b);
		}
	}

	@EventHandler
	public void onFire(BlockBurnEvent e) {
		World w = e.getBlock().getWorld();
		File file = new File(Bukkit.getWorldContainer(), w.getName() + "/worldconfig.yml");
		if (file.exists()) {
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			boolean b = cfg.getBoolean("Settings.Fire");
			e.setCancelled(!b);
		}
	}
}
