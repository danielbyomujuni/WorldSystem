package de.butzlabben.world.command;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.wrapper.WorldPlayer;

/**
 * @author Butzlabben
 * @since 14.08.2018
 */
public class WSSethomeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String arg2, String[] args) {
		Player p = (Player) cs;
		WorldPlayer wp = new WorldPlayer(p);
		if (!wp.isOnSystemWorld()) {
			p.sendMessage(MessageConfig.getNotOnWorld());
			return true;
		}
		if (!wp.isOwnerofWorld()) {
			p.sendMessage(MessageConfig.getNoPermission());
			return true;
		}
		WorldConfig config = WorldConfig.getWorldConfig(p.getWorld().getName());
		config.setHome(p.getLocation());
		try {
			config.save();
			p.sendMessage(MessageConfig.getHomeSet());
		} catch (IOException e) {
			p.sendMessage(MessageConfig.getUnknownError() + ": " + e.getMessage());
			e.printStackTrace();
		}
		return true;
	}
}
