package de.butzlabben.world;

import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.wrapper.SystemWorld;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WorldCheckerRunnable implements Runnable {

	@Override
	public void run() {
		for (World world : Bukkit.getWorlds()) {
			if (SystemWorld.getSystemWorld(world.getName()) == null || !SystemWorld.getSystemWorld(world.getName()).isLoaded())
				continue;
			int other = world.getEntities().size() - world.getPlayers().size();
			if (other > PluginConfig.getEntitysPerWorld()) {
				String worldname = world.getName();
				for (Entity e : world.getEntities()) {
					if (!(e instanceof Player)) {
						e.remove();
					}
				}
				String ownerofWorld = null;
				for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
					if (p.getUniqueId().toString()
							.equals(worldname.substring(worldname.length() - 36)))
						ownerofWorld = p.getName();
				}
				StringBuilder members = new StringBuilder();
				for (Player p : world.getPlayers()) {
					members.append(p.getName()).append(" ");
				}
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (!p.hasPermission("ws.lag"))
						continue;
					p.sendMessage(MessageConfig.getLagDetection().replaceAll("%world",
							ownerofWorld + " ( ID: " + world.getName().substring(2, worldname.length() - 37) + " )"));
					p.sendMessage(MessageConfig.getPlayerList().replaceAll("%players", members.toString()));

				}
			}
		}
	}
}
