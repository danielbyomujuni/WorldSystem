package de.butzlabben.world.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MessageConfig
{
  private static final List<String> defaultCmdHelp = new ArrayList<>(20);
  private static File languageFile;

  static {
    defaultCmdHelp.add("/ws get §8- §7Will give you a World");
    defaultCmdHelp.add("/ws home §8- §7Teleports you on your World");
    defaultCmdHelp.add("/ws sethome §8- §7Sets a specific home");
    defaultCmdHelp.add("/ws tp §8- §7Teleports you on a specific World");
    defaultCmdHelp.add("/ws addmember §8- §7Adds a player to your World");
    defaultCmdHelp.add("/ws delmember§8 - §7Removes a player from your World");
    defaultCmdHelp.add("/ws tnt §8- §7Allows/Denys TNT on your World");
    defaultCmdHelp.add("/ws fire §8- §7Allows/Denys Fire on your World");
    defaultCmdHelp.add("/ws togglechgm §8- §7Allows/Denys a player changing gamemode");
    defaultCmdHelp.add("/ws togglebuild §8- §7Allows/Denys a player building");
    defaultCmdHelp.add("/ws toggletp §8- §7Allows/Denys a player teleporting");
    defaultCmdHelp.add("/ws info §8- §7Shows information about the World");
    defaultCmdHelp.add("/ws reset §8- §7Will reset your World");
  }

}
