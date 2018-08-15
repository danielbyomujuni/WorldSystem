package de.butzlabben.world.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.wrapper.WorldPlayer;

public class PlayerDeathListener implements Listener {

	private HashMap<UUID, World> deathLocations = new HashMap<>();

	@EventHandler
	public void onDie(PlayerDeathEvent e) {
		Player p = e.getEntity();
		WorldPlayer wp = new WorldPlayer(p, p.getWorld().getName());
		if (wp.isOnSystemWorld()) {
			deathLocations.put(p.getUniqueId(), p.getLocation().getWorld());
		} else {
			p.setGameMode(PluginConfig.getSpawnGamemode());
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if (deathLocations.containsKey(p.getUniqueId())) {
			World world = deathLocations.remove(p.getUniqueId());

			WorldConfig config = WorldConfig.getWorldConfig(world.getName());
			
			if (config.getHome() != null) {
				e.setRespawnLocation(config.getHome());
			} else {
				if (PluginConfig.useWorldSpawn()) {
					e.setRespawnLocation(PluginConfig.getWorldSpawn(world));
				} else {
					e.setRespawnLocation(world.getSpawnLocation());
				}
			}			
		}
	}
}
