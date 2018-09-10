package de.butzlabben.world.command;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.event.WorldDeleteEvent;
import de.butzlabben.world.wrapper.SystemWorld;

public class WSDeleteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command arg1, String arg2, String[] args) {
		if (args.length < 2) {
			cs.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage",
					WorldSystem.getInstance().getCommand("ws delete").getUsage()));
			return true;
		}
		DependenceConfig dc = new DependenceConfig(args[1]);
		if (!dc.hasWorld()) {
			cs.sendMessage(MessageConfig.getNoWorldOther());
			return true;
		}
		String worldname = dc.getWorldname();
		SystemWorld sw = SystemWorld.getSystemWorld(worldname);
		WorldDeleteEvent event = new WorldDeleteEvent(cs, sw);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return true;
		if (sw != null && sw.isLoaded())
			sw.directUnload(Bukkit.getWorld(worldname));
		Bukkit.getScheduler().runTaskLater(WorldSystem.getInstance(), () -> {
			OfflinePlayer op = dc.getOwner();

			String uuid = op.getUniqueId().toString();
			File dir = new File(PluginConfig.getWorlddir() + "/" + worldname);
			if(!dir.exists())
				dir = new File(Bukkit.getWorldContainer(), worldname);
			if (dir.exists())
				try {
					FileUtils.deleteDirectory(dir);
				} catch (Exception e) {
					cs.sendMessage(MessageConfig.getUnknownError());
					e.printStackTrace();
				}
			File dconfig = new File("plugins//WorldSystem//dependence.yml");
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
			cfg.set("Dependences." + uuid + ".ID", null);
			cfg.set("Dependences." + uuid + ".ActualName", null);
			cfg.set("Dependences." + uuid, null);
			try {
				cfg.save(dconfig);
			} catch (Exception e) {
				cs.sendMessage(MessageConfig.getUnknownError());
				e.printStackTrace();
			}
			cs.sendMessage(MessageConfig.getDeleteWorldOther().replaceAll("%player", op.getName()));
			if (op.isOnline()) {
				Player p1 = Bukkit.getPlayer(op.getUniqueId());
				p1.sendMessage(MessageConfig.getDeleteWorldOwn());
			}
		}, 10);
		return true;

	}
}
