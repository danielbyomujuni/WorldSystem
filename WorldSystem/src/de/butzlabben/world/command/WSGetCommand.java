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

public class WSGetCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command arg1, String arg2, String[] arg3) {
		if (!(cs instanceof Player))
			return true;
		Player p = (Player) cs;
		// create New Entry
		DependenceConfig dc = new DependenceConfig(p);
		if (dc.hasWorld()) {
			p.sendMessage(MessageConfig.getWorldAlreadyExists());
			return true;
		}
		Bukkit.getScheduler().runTask(WorldSystem.getInstance(), () -> {
			if (SystemWorld.create(p))
				p.sendMessage(MessageConfig.getWorldCreated());
		});
		return true;
	}

}
