package de.butzlabben.world.commands;

import org.bukkit.command.CommandSender;

public interface WorldSystemCommand
{
  public boolean run(CommandSender sender);

  public String[] arguments();
}
