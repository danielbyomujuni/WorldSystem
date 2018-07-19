package de.butzlabben.world.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.butzlabben.event.WorldResetEvent;
import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.wrapper.SystemWorld;

public class WSResetCommand implements CommandExecutor {

	private ArrayList<Player> toConfirm = new ArrayList<>();

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		Player p = (Player) cs;
		if (args.length > 2) {
			p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage",
					WorldSystem.getInstance().getCommand("ws reset").getUsage()));
			return true;
		}
		DependenceConfig dc = new DependenceConfig(p);
		String worldname = dc.getWorldname();
		SystemWorld sw = SystemWorld.getSystemWorld(worldname);
		if (!dc.hasWorld()) {
			p.sendMessage(MessageConfig.getNoWorldOwn());
			return true;
		}
		if (args.length > 1) {
			if (args[1].equals("confirm")) {
				if (sw.isLoaded())
					sw.directUnload(Bukkit.getWorld(worldname));

				if (!toConfirm.contains(p)) {
					p.sendMessage(MessageConfig.getNoRequestSend());
					return true;
				}
				WorldResetEvent event = new WorldResetEvent(cs, sw);
				Bukkit.getPluginManager().callEvent(event);
				if (event.isCancelled())
					return true;

				if (sw.isLoaded()) {
					p.sendMessage(MessageConfig.getWorldStillLoaded());
					return true;
				}
				File f = new File(PluginConfig.getWorlddir() + "/" + worldname);

				File[] files = f.listFiles();
				for (File file : files) {
					if (file.getName().equals("worldconfig.yml"))
						continue;
					FileUtils.deleteQuietly(file);
				}
				File exampleworld = new File(
						"plugins//WorldSystem//worldsources//" + PluginConfig.getExampleWorldName());
				try {
					FileUtils.copyDirectory(exampleworld, f);
					toConfirm.remove(p);

					FileUtils.moveDirectoryToDirectory(f, Bukkit.getWorldContainer(), false);

					p.sendMessage(MessageConfig.getWorldReseted());

					// For fast worldcreating after reset
					WorldCreator creator = new WorldCreator(worldname);
					long seed = PluginConfig.getSeed();
					Environment env = PluginConfig.getEnvironment();
					WorldType type = PluginConfig.getWorldType();
					if (seed != 0)
						creator.seed(seed);
					creator.type(type);
					creator.environment(env);
					String generator = PluginConfig.getGenerator();
					if (!generator.trim().isEmpty())
						creator.generator(generator);

					sw.setCreating(true);
					// For #16
					WorldSystem.creator.create(creator, sw, () -> {
						if (p != null && p.isOnline())
							p.sendMessage(MessageConfig.getWorldCreated());
					});

				} catch (IOException e) {
					e.printStackTrace();
					p.sendMessage(MessageConfig.getUnknownError());
					System.err.println("Couldn't reset world of " + p.getName());
				}
			} else {
				p.sendMessage(MessageConfig.getInvalidInput().replaceAll("input", args[0]));
				return true;
			}
		} else if (args.length == 1) {
			if (sw.isLoaded())
				sw.directUnload(Bukkit.getWorld(worldname));

			if (toConfirm.contains(p)) {
				p.sendMessage(MessageConfig.getRequestAlreadySent());
				return true;
			}

			int time = PluginConfig.getRequestExpire();
			p.sendMessage(MessageConfig.getConfirmRequest().replaceAll("%command", "/ws reset confirm"));
			p.sendMessage(MessageConfig.getTimeUntilExpires().replaceAll("%time", String.valueOf(time)));
			toConfirm.add(p);
			Bukkit.getScheduler().runTaskLater(WorldSystem.getInstance(), () -> {
				if (toConfirm.contains(p)) {
					p.sendMessage(MessageConfig.getRequestExpired());
					toConfirm.remove(p);
				}
			}, time * 20L);
		}
		return true;
	}
}
