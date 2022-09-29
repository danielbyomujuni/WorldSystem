package de.butzlabben.world.commands;

import java.util.HashMap;
import java.util.Map;

public class WorldSystemCommandExecuter
{
  private static Map<String, WorldSystemCommand> commands = new HashMap<String, WorldSystemCommand>();

  public static void addCommand(String commandName, WorldSystemCommand cmd)
  {
    if (commands.containsValue(commandName)) {
      //Log Command Excists
      return;
    }
    commands.put(commandName, cmd);
  }

  public static boolean execute(String cmd) {
    return commands.get(cmd).run();
  }


}
