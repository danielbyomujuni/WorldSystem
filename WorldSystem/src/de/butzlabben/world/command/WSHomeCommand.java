package de.butzlabben.world.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.wrapper.SystemWorld;
import de.butzlabben.world.wrapper.WorldPlayer;

public class WSHomeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (!(cs instanceof Player))
			return true;
		Player p = (Player) cs;
		if (args.length != 1) {
			p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage",
					WorldSystem.getInstance().getCommand("ws home").getUsage()));
			return true;
		}
		String worldname = p.getWorld().getName();
		DependenceConfig dc = new DependenceConfig(p);
		if (!dc.hasWorld()) {
			p.sendMessage(MessageConfig.getNoWorldOwn());
			return true;
		}
		WorldPlayer wp = new WorldPlayer(p, worldname);
		if (wp.isOnSystemWorld()) {
			SystemWorld.tryUnloadLater(Bukkit.getWorld(worldname));
		}
		SystemWorld sw = SystemWorld.getSystemWorld(dc.getWorldname());
		if(sw == null) {
			p.sendMessage(MessageConfig.getNoWorldOwn());
			return true;
		}
		if (sw.isLoaded()) {
			sw.teleportToWorldSpawn(p);
		} else {
			sw.load(p);
		}
		return true;
	}
}
