package de.butzlabben.world.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.WorldConfig2;
import de.butzlabben.world.wrapper.WorldPlayer;

public class WSInfoCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		Player p = (Player) cs;
		WorldPlayer wp = new WorldPlayer(p, p.getWorld().getName());
		if (!wp.isOnSystemWorld()) {
			p.sendMessage(MessageConfig.getNotOnWorld());
			return  true;
		}
		WorldConfig2.getInfos(p, p.getWorld().getName());
		return true;
	}
}
