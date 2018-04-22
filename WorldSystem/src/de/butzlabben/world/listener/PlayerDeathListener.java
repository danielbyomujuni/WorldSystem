package de.butzlabben.world.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.wrapper.WorldPlayer;

public class PlayerDeathListener implements Listener {

	@EventHandler
	public void onDie(PlayerDeathEvent e) {
		e.setDeathMessage(null);
		Player p = e.getEntity();
		WorldPlayer wp = new WorldPlayer(p, p.getWorld().getName());
		if (wp.isOnSystemWorld()) {
			WorldSystem.deathLocations.put(p, p.getLocation().getWorld());
		} else {
			p.setGameMode(PluginConfig.getSpawnGamemode());
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if (WorldSystem.deathLocations.containsKey(p)) {
			Location loc =PluginConfig.getWorldSpawn(WorldSystem.deathLocations.get(p));
			e.setRespawnLocation(loc);
			WorldSystem.deathLocations.remove(p);
		}
	}
}
