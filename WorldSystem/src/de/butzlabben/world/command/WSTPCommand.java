package de.butzlabben.world.command;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.wrapper.SystemWorld;
import de.butzlabben.world.wrapper.WorldPlayer;

public class WSTPCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (!(cs instanceof Player))
			return true;
		Player p = (Player) cs;
		if (args.length != 2) {
			p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage",
					WorldSystem.getInstance().getCommand("ws tp").getUsage()));
			return true;
		}
		DependenceConfig dc = new DependenceConfig(args[1]);
		String worldname = dc.getWorldNamebyOfflinePlayer();
		if (!dc.hasWorld()) {
			p.sendMessage(MessageConfig.getNoWorldOther());
			return true;
		}
		SystemWorld sw = SystemWorld.getSystemWorld(worldname);
		if(sw == null) {
			worldname = dc.getOldWorldNamebyOfflinePlayer();
			sw = SystemWorld.getSystemWorld(worldname);
		}
		WorldPlayer wp1 = new WorldPlayer(p, p.getWorld().getName());
		WorldPlayer wp = new WorldPlayer(p, worldname);
		if (p.getWorld().getName().equals(worldname)) {
			sw.teleportToWorldSpawn(p);
			return true;
		}
		if (!p.hasPermission("ws.tp.world")) {
			if (!wp.isMemberofWorld(worldname) && !wp.isOwnerofWorld()) {
				p.sendMessage(MessageConfig.getNoMemberOther());
				return true;
			}
		}		
		if (wp1.isOnSystemWorld()) {
			World w = p.getWorld();
			SystemWorld.tryUnloadLater(w);
		}
		if (sw != null)
			if (!sw.isLoaded()) {
				sw.load(p);
			} else {
				sw.teleportToWorldSpawn(p);
			}
		return true;
	}
}