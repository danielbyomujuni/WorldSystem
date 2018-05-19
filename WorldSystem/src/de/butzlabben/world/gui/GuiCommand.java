package de.butzlabben.world.gui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.wrapper.WorldPlayer;

public class GuiCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player == false) {
			sender.sendMessage("You are not a player");
			return true;
		}
		WorldPlayer wp = new WorldPlayer((Player) sender);
		if(!wp.isOnSystemWorld()) {
			sender.sendMessage(MessageConfig.getNotOnWorld());
			return true;
		}
		if (!wp.isOwnerofWorld()) {
			sender.sendMessage(MessageConfig.getNoPermission());
			return true;
		}
		((Player) sender).openInventory(WorldSystem.mainGUI.getInventory((Player) sender));
		return true;
	}

}
