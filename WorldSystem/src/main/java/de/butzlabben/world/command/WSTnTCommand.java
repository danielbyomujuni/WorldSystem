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
import de.butzlabben.world.event.WorldToggleTntEvent;
import de.butzlabben.world.wrapper.SystemWorld;

public class WSTnTCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		Player p = (Player) cs;
		if(args.length > 1) {
			p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", WorldSystem.getInstance().getCommand("ws tnt").getUsage()));
			return true;
		}		
		
		DependenceConfig dc = new DependenceConfig(p);
		WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname());
		boolean tnt = wc.isTnt();
		WorldToggleTntEvent event = new WorldToggleTntEvent(p, SystemWorld.getSystemWorld(dc.getWorldname()), tnt);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return true;

		wc.setTnt(p.getUniqueId(), !tnt);
		try {
			wc.save();
		} catch (IOException e) {
			p.sendMessage(PluginConfig.getPrefix() + "§cSomething went wrong");
			e.printStackTrace();
		}
		tnt = wc.isTnt();
		if (tnt) {
			p.sendMessage(MessageConfig.getToggleTntEnabled());
		} else {
			p.sendMessage(MessageConfig.getToggleTntDisabled());
		}
		return true;
	}
}
