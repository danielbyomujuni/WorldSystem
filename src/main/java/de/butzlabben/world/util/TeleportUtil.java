package de.butzlabben.world.util;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Butzlabben
 * @since 13.09.2018
 */
public class TeleportUtil {
	
	private static HashMap<UUID, Location> oldLocs = new HashMap<>();
	
	public static void teleportPlayer(Player p, Location loc) {
		// Save old player location.
		// If he does another teleport he will be taken back then to the first location
		if(!oldLocs.containsKey(p.getUniqueId())) {
			Location oldLoc = p.getLocation();
			oldLocs.put(p.getUniqueId(), oldLoc);
		}
		
	}

	private TeleportUtil() {
	}
}
