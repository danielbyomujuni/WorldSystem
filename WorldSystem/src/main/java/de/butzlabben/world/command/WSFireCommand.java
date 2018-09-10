package de.butzlabben.world.command;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.config.WorldConfig;
import de.butzlabben.world.event.WorldToggleFireEvent;
import de.butzlabben.world.wrapper.SystemWorld;

public class WSFireCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		Player p = (Player) cs;
		if (args.length > 1) {
			p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage",
					WorldSystem.getInstance().getCommand("ws fire").getUsage()));
			return true;
		}
		DependenceConfig dc = new DependenceConfig(p);
		WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname());
		boolean fire = wc.isFire();
		WorldToggleFireEvent event = new WorldToggleFireEvent(p, SystemWorld.getSystemWorld(dc.getWorldname()), fire);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return true;

		wc.setFire(p.getUniqueId(), !fire);
		try {
			wc.save();
		} catch (IOException e) {
			p.sendMessage(PluginConfig.getPrefix() + "§cSomething went wrong");
			e.printStackTrace();
		}
		fire = wc.isFire();
		if (fire) {
			p.sendMessage(MessageConfig.getToggleFireEnabled());
		} else {
			p.sendMessage(MessageConfig.getToggleFireDisabled());
		}
		return true;
	}
}
