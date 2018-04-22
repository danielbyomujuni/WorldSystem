package de.butzlabben.world.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.WorldConfig2;

public class WSFireCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		Player p = (Player) cs;
		if(args.length > 1) {
			p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", WorldSystem.getInstance().getCommand("ws fire").getUsage()));
			return true;
		}		
		WorldConfig2.changeFireDamage(p);		
		return true;
	}
}
