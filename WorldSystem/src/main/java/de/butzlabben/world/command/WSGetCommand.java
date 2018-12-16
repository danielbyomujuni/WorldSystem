package de.butzlabben.world.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.DependenceConfig;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.gui.WorldChooseGUI;
import de.butzlabben.world.wrapper.SystemWorld;
import de.butzlabben.world.wrapper.WorldTemplate;
import de.butzlabben.world.wrapper.WorldTemplateProvider;

public class WSGetCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (!(cs instanceof Player))
			return true;

		Player p = (Player) cs;
		// create New Entry
		DependenceConfig dc = new DependenceConfig(p);
		if (dc.hasWorld()) {
			p.sendMessage(MessageConfig.getWorldAlreadyExists());
			return true;
		}

		if (PluginConfig.isMultiChoose()) {
			if (args.length >= 2) {
				String key = args[1];
				WorldTemplate template = WorldTemplateProvider.getInstace().getTemplate(key);
				if (template != null) {
					create(p, template);
					return true;
				}
			}
			p.openInventory(new WorldChooseGUI().getInventory(p));
		} else {
			WorldTemplate template = WorldTemplateProvider.getInstace()
					.getTemplate(PluginConfig.getDefaultWorldTemplate());
			if(template != null)
				create(p, template);
			else {
				p.sendMessage(PluginConfig.getPrefix() + "§cError in config at \"worldtemplates.default\"");
				p.sendMessage(PluginConfig.getPrefix() + "§cPlease contact an administrator");
			}
		}
		return true;
	}

	private void create(Player p, WorldTemplate template) {
		Bukkit.getScheduler().runTask(WorldSystem.getInstance(), () -> {
			if (SystemWorld.create(p, template))
				p.sendMessage(MessageConfig.getSettingUpWorld());
		});
	}
}
