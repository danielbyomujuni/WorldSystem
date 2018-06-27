package de.butzlabben.world.listener;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.wrapper.SystemWorld;

public class PlayerListener implements Listener {

	//#17
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) {
		if (PluginConfig.spawnTeleportation()) {
			Player p = e.getPlayer();
			DependenceConfig dc = new DependenceConfig(p);
			if (dc.hasWorld()) {
				SystemWorld sw = SystemWorld.getSystemWorld(dc.getWorldname());
				if (sw != null && !sw.isLoaded()) {
					e.getPlayer().teleport(PluginConfig.getSpawn());
				}
			}
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		World w = p.getWorld();
		SystemWorld.tryUnloadLater(w);
	}
}