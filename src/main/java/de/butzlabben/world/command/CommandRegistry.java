package de.butzlabben.world.command;

import de.butzlabben.world.command.commands.WSCommands;
import de.butzlabben.world.command.commands.WorldAdministrateCommand;
import de.butzlabben.world.command.commands.WorldSettingsCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import static de.butzlabben.world.command.commands.WSCommands.*;
import static de.butzlabben.world.command.commands.WorldAdministrateCommand.*;

import java.util.List;


public class CommandRegistry implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            WSCommands ws = new WSCommands();
            return ws.mainCommand(sender, command, label, args);
        } else {
            WSCommands ws = new WSCommands();
            WorldAdministrateCommand admin = new WorldAdministrateCommand();
            WorldSettingsCommands settings = new WorldSettingsCommands();
            switch (args[0]) {
                //WSCommands
                case "get":
                        return ws.getCommand(sender, command, label, args);
                case "gui":
                        return ws.guiCommand(sender, command, label, args);
                case "confirm":
                    if (sender.hasPermission("ws.confirm")) {
                        return ws.confirmCommand(sender, command, label, args);
                    } else {
                        return false;
                    }
                case "home":
                    return ws.homeCommand(sender, command, label, args);
                case "info":
                    return ws.infoComannd(sender, command, label, args);
                case "leave":
                    return ws.leaveCommand(sender, command, label, args);
                case "tp":
                        return ws.tpCommand(sender, command, label, args);
                //Admin Command
                case "delmember":
                    return admin.delMemberCommand(sender, command, label, args);
                case "delete":
                    if (sender.hasPermission("ws.delete")) {
                        return admin.deleteCommand(sender, command, label, args);
                    } else {
                        return false;
                    }
                case "addmember":
                    return admin.addMemberCommand(sender, command, label, args);
                case "toggletp":
                    return admin.toggleTeleportCommand(sender, command, label, args);
                case "togglegm":
                    return admin.toggleGamemodeCommand(sender, command, label, args);
                case "togglewe":
                    return admin.toggleWorldeditCommand(sender, command, label, args);
                case "togglebuild":
                    return admin.toggleBuildCommand(sender, command, label, args);
                //World Settings Command
                case "reset":
                    return settings.resetCommand(sender, command, label, args);
                case "sethome":
                    sender.sendMessage("Disabled For Major Error and Rework");
                            /*
                    if (sender.hasPermission("ws.sethome")) {
                        return settings.setHomeCommand(sender, command, label, args);
                    } else {
                        return false;
                    }*/
                case "tnt":
                    return settings.tntCommand(sender, command, label, args);
                case "fire":
                    return settings.fireCommand(sender, command, label, args);
                case "reload":
                    if(!sender.isOp()){
                        sender.sendMessage("Reloaded Templates!");
                        WorldTemplateProvider.getInstance().reload();
                        return true;
                    }
                default:
                    //default command is better than the other one
                    return ws.mainCommand(sender, command, label, args);
            }
        }
    }
}
