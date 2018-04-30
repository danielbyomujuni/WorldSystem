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
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.config.WorldConfig2;

public class WSDelmemberCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (!(cs instanceof Player))
			return true;
		Player p = (Player) cs;
		if(args.length != 2) {
			p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", WorldSystem.getInstance().getCommand("ws delmember").getUsage()));
			return true;			
		}
		DependenceConfig dc = new DependenceConfig(p);
		if (!dc.hasWorld()) {
			p.sendMessage(MessageConfig.getNoWorldOwn());
			return true;
		}
		@SuppressWarnings("deprecation")
		OfflinePlayer a = Bukkit.getOfflinePlayer(args[1]);
		if (a == null) {
			p.sendMessage(MessageConfig.getNotRegistered());
			return true;
		} else if (!WorldConfig2.isMember(a, new DependenceConfig(p).getWorldname()) && !WorldConfig2.isMember(a, new DependenceConfig(p).getWorldname())) {
			p.sendMessage(MessageConfig.getNoMemberOwn());
			return true;
		}
		WorldConfig2.delMember(p, a);
		if(a.isOnline()) {
			Player t = (Player) a;
			if(t.getWorld().getName().equals(new DependenceConfig(p).getWorldname())) {
				t.teleport(PluginConfig.getSpawn());
				t.setGameMode(PluginConfig.getSpawnGamemode());
			}			
		}
		p.sendMessage(MessageConfig.getMemberRemoved().replaceAll("%player", a.getName()));
		return true;
	}

}
