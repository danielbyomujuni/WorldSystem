package de.butzlabben.world.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.wrapper.SystemWorld;
import de.butzlabben.world.wrapper.WorldPlayer;

public class WSToggleBuildCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (!(cs instanceof Player))
			return true;
		Player p = (Player) cs;
		if (args.length != 2) {
			p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage",
					WorldSystem.getInstance().getCommand("ws togglebuild").getUsage()));
			return true;
		}
		DependenceConfig dc = new DependenceConfig(p);
		if (!dc.hasWorld()) {
			p.sendMessage(MessageConfig.getNoWorldOwn());
			return true;
		}
		@SuppressWarnings("deprecation")
		OfflinePlayer a = Bukkit.getOfflinePlayer(args[1]);
		SystemWorld sw = SystemWorld.getSystemWorld(dc.getWorldname());
		WorldPlayer wp = new WorldPlayer(a, dc.getWorldname());
		if(sw == null)
			wp = new WorldPlayer(a, dc.getOldWorldname());
		if (wp.toggleBuild()) {
			p.sendMessage(MessageConfig.getToggleBuildEnabled().replaceAll("%player", a.getName()));
		} else {
			p.sendMessage(MessageConfig.getToggleBuildDisabled().replaceAll("%player", a.getName()));
		}
		return true;
	}
}
