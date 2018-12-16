package de.butzlabben.world.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.wrapper.SystemWorld;
import de.butzlabben.world.wrapper.WorldPlayer;

public class WSLeaveCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (!(cs instanceof Player))
			return true;
		Player p = (Player) cs;
		if (args.length != 1) {
			p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage",
					WorldSystem.getInstance().getCommand("ws leave").getUsage()));
			return true;
		}
		String worldname = p.getWorld().getName();
		WorldPlayer wp = new WorldPlayer(p, worldname);

		if (wp.isOnSystemWorld()) {
			// Extra safety for #2
			if (PluginConfig.getSpawn().getWorld() == null) {
				Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "§cThe spawn is not properly set");
				cs.sendMessage(PluginConfig.getPrefix() + "§cThe spawn is not properly set");
				return true;
			}
			
			p.teleport(PluginConfig.getSpawn());
			p.setGameMode(PluginConfig.getSpawnGamemode());
			World w = Bukkit.getWorld(p.getWorld().getName());
			SystemWorld.tryUnloadLater(w);
		} else {
			p.sendMessage(MessageConfig.getNotOnWorld());
		}
		return true;
	}
}
