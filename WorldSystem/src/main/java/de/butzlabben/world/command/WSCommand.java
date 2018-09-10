package de.butzlabben.world.command;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.PluginConfig;

public class WSCommand implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
		List<String> list = new LinkedList<>();
		if (args.length == 1) {
			list.add("addmember");
			list.add("delete");
			list.add("delmember");
			list.add("fire");
			list.add("get");
			list.add("gui");
			list.add("home");
			list.add("info");
			list.add("leave");
			list.add("reset");
			list.add("sethome");
			list.add("tnt");
			list.add("togglegm");
			list.add("toggletp");
			list.add("togglebuild");
			list.add("tp");
		} else if (args.length == 2) {
			switch (args[0].toLowerCase()) {
			case "reset":
				list.add("confirm");
				break;
			case "addmember":
			case "delete":
			case "togglebuild":
			case "togglegm":
			case "toggletp":
			case "tp":
			case "delmember":
				Bukkit.getOnlinePlayers().forEach((p) -> {
					if (cs instanceof Player == false || ((Player) cs).canSee(p))
						list.add(p.getName());
				});
				break;
			}
		}
		return list;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			String subcommand = args[0].toLowerCase();
			if (subcommand.equals("get")) {
				if (!(cs instanceof Player))
					return true;
				WorldSystem.getInstance().getCommand("ws get").execute(cs, label, args);
			} else if (subcommand.equals("addmember")) {
				if (!(cs instanceof Player))
					return true;
				WorldSystem.getInstance().getCommand("ws addmember").execute(cs, label, args);
			} else if (subcommand.equals("delmember")) {
				if (!(cs instanceof Player))
					return true;
				WorldSystem.getInstance().getCommand("ws delmember").execute(cs, label, args);
			} else if (subcommand.equals("fire")) {
				if (!(cs instanceof Player))
					return true;
				WorldSystem.getInstance().getCommand("ws fire").execute(cs, label, args);
			} else if (subcommand.equals("home")) {
				if (!(cs instanceof Player))
					return true;
				WorldSystem.getInstance().getCommand("ws home").execute(cs, label, args);
			} else if (subcommand.equals("sethome")) {
				if (!(cs instanceof Player))
					return true;
				WorldSystem.getInstance().getCommand("ws sethome").execute(cs, label, args);
			} else if (subcommand.equals("leave")) {
				WorldSystem.getInstance().getCommand("ws leave").execute(cs, label, args);
			} else if (subcommand.equals("info")) {
				if (!(cs instanceof Player))
					return true;
				WorldSystem.getInstance().getCommand("ws info").execute(cs, label, args);
			} else if (subcommand.equals("tnt")) {
				if (!(cs instanceof Player))
					return true;
				WorldSystem.getInstance().getCommand("ws tnt").execute(cs, label, args);
			} else if (subcommand.equals("tp")) {
				if (!(cs instanceof Player))
					return true;
				WorldSystem.getInstance().getCommand("ws tp").execute(cs, label, args);
			} else if (subcommand.equals("reset")) {
				if (!(cs instanceof Player))
					return true;
				WorldSystem.getInstance().getCommand("ws reset").execute(cs, label, args);
			} else if (subcommand.equals("toggletp")) {
				if (!(cs instanceof Player))
					return true;
				WorldSystem.getInstance().getCommand("ws toggletp").execute(cs, label, args);
			} else if (subcommand.equals("togglegm")) {
				if (!(cs instanceof Player))
					return true;
				WorldSystem.getInstance().getCommand("ws togglegm").execute(cs, label, args);
			} else if (subcommand.equals("togglebuild")) {
				if (!(cs instanceof Player))
					return true;
				WorldSystem.getInstance().getCommand("ws togglebuild").execute(cs, label, args);
			} else if (subcommand.equals("delete")) {
				WorldSystem.getInstance().getCommand("ws delete").execute(cs, label, args);
			} else if (subcommand.equals("gui")) {
				WorldSystem.getInstance().getCommand("ws gui").execute(cs, label, args);
			} else if (subcommand.equals("confirm")) {
				WorldSystem.getInstance().getCommand("ws confirm").execute(cs, label, args);
			} else {
				if (cs instanceof Player) {
					Player p = (Player) cs;
					p.chat("/ws");
				} else if (cs instanceof ConsoleCommandSender) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ws");
				}
			}
			return true;
		}
		String prefix = PluginConfig.getPrefix();
		cs.sendMessage(
				prefix + "WorldSystem by Butzlabben v" + WorldSystem.getInstance().getDescription().getVersion());
		cs.sendMessage(prefix + "Contributor: Jubeki");
		List<String> cmdHelp = MessageConfig.getCommandHelp();
		cmdHelp.forEach(s -> cs.sendMessage(prefix + s));
		if (cs.hasPermission("ws.delete"))
			cs.sendMessage(MessageConfig.getDeleteCommandHelp());
		return true;
	}

}
