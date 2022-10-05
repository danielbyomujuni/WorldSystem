package de.butzlabben.world.commands.commands;

import de.butzlabben.world.commands.WorldSystemCommand;
import org.bukkit.command.CommandSender;

public class DebugCommandTest implements WorldSystemCommand
{
  @Override
  public boolean run(CommandSender sender)
  {
    sender.sendMessage("The Command Framework is Working");
    return true;
  }
  @Override
  public String[] arguments()
  {
    return new String[0];
  }
}
