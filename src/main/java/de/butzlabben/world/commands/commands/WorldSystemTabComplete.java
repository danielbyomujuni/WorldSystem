package de.butzlabben.world.commands.commands;

import de.butzlabben.world.commands.WorldSystemCommandExecuter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WorldSystemTabComplete implements TabCompleter
{
  private String[] cmdlist;

  public WorldSystemTabComplete(WorldSystemCommandExecuter ex) {
    cmdlist = ex.commandList();
  }
  @Nullable
  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings)
  {
    return List.of(cmdlist);
  }
}
