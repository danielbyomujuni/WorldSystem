package de.butzlabben.world.commands;

import de.butzlabben.world.exceptions.InvaildCommandException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class WorldSystemCommandExecuter implements CommandExecutor
{


  private Map<String, WorldSystemCommand> commands = new HashMap<String, WorldSystemCommand>();

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
  {
    try {
      return executeCommand(args[0],commandSender);
    }
    catch (InvaildCommandException e)
    {
      //Tell the Player that the commands was Wrong
      return false;
    }
  }

  public void addCommand(String commandName, WorldSystemCommand cmd)
  {
    if (commands.containsValue(commandName)) {
      //Log Command Excists
      return;
    }
    commands.put(commandName, cmd);
  }

  public boolean executeCommand(String cmd, CommandSender commandSender) throws InvaildCommandException
  {
    if (commands.get(cmd) == null) {
      throw new InvaildCommandException(cmd + "Is not a command");
    }
    return commands.get(cmd).run(commandSender);
  }

}
