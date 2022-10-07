package de.butzlabben.world.commands.commands;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.commands.WorldSystemCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BaseCommand implements WorldSystemCommand
{
  @Override
  public boolean run(CommandSender sender)
  {
    return false;

//    String prefix = WorldSystem.getWSConfig().getPrefix();
//    sender.sendMessage(
//        prefix + "WorldSystem by Butzlabben v" + WorldSystem.getInstance().getDescription().getVersion());
//    sender.sendMessage(prefix + "Contributors: Jubeki, montlikadani, jstoeckm2");
//    List<String> cmdHelp = MessageConfig.getCommandHelp();
//    cmdHelp.forEach(s -> sender.sendMessage(prefix + s));
//    if (sender.hasPermission("ws.delete"))
//    {
//      sender.sendMessage(MessageConfig.getDeleteCommandHelp());
//    }
  }

  @Override
  public String[] arguments()
  {
    return new String[0];
  }
}
