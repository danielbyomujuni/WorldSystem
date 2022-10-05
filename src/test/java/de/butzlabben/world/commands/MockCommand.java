package de.butzlabben.world.commands;

import org.bukkit.command.CommandSender;

public class MockCommand implements WorldSystemCommand
{

  public MockCommand() {
  }

  @Override
  public boolean run(CommandSender sender)
  {
    return true;
  }

  @Override
  public String[] arguments()
  {
    return new String[]{"zero", "one"};
  }
}
