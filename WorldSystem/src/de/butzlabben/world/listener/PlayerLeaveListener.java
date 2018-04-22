package de.butzlabben.world.listener;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.butzlabben.world.wrapper.SystemWorld;

public class PlayerLeaveListener implements Listener {

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		World w = p.getWorld();
		SystemWorld.tryUnloadLater(w);
	}
}