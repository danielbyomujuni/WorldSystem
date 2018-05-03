package de.butzlabben.world.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.butzlabben.autoupdater.AutoUpdater;
import de.butzlabben.world.config.PluginConfig;

/**
 * @author Butzlabben
 * @since 02.05.2018
 */
public class WSConfirmCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (AutoUpdater.getInstance().confirmed()) {
			cs.sendMessage(PluginConfig.getPrefix() + "§cAlready confirmed or no confirm needed");
			return true;
		}
		AutoUpdater.getInstance().confirm();
		cs.sendMessage(PluginConfig.getPrefix() + "§aAutoupdate confirmed, §crestart §ato apply changes");
		return true;
	}
}
