package de.butzlabben.world.listener;

import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.wrapper.WorldPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.io.File;
import java.util.Objects;

public class BlockListener implements Listener {
	
	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		File file = WorldConfig.getWorldFile(Objects.requireNonNull(e.getLocation().getWorld()).getName());
		if(!file.exists())
			return;
		WorldConfig wc = WorldConfig.getWorldConfig(e.getLocation().getWorld().getName());
		e.setCancelled(!wc.isTnt());
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
		if(!wp.isMember())
			e.setCancelled(true);
		if (!wp.isOwnerofWorld()) {
			e.setCancelled(!wp.canBuild());
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
		if(!wp.isMember())
			e.setCancelled(true);
		if (!wp.isOwnerofWorld()) {
			e.setCancelled(!wp.canBuild());
		}
	}
	@EventHandler
	public void onFire(BlockIgniteEvent e) {
		File file = WorldConfig.getWorldFile(e.getBlock().getWorld().getName());
		if(!file.exists())
			return;
		WorldConfig wc = WorldConfig.getWorldConfig(e.getBlock().getWorld().getName());
		e.setCancelled(!wc.isFire());
	}

	@EventHandler
	public void onFire(BlockBurnEvent e) {
		File file = WorldConfig.getWorldFile(e.getBlock().getWorld().getName());
		if(!file.exists())
			return;
		WorldConfig wc = WorldConfig.getWorldConfig(e.getBlock().getWorld().getName());
		e.setCancelled(!wc.isFire());
	}
}
