package de.butzlabben.world;

import de.butzlabben.world.commands.WorldSystemCommandExecuter;
import de.butzlabben.world.commands.commands.DebugCommandTest;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldSystem extends JavaPlugin
{
  @Override
  public void onEnable() {
    createCommands();
  }


  private void createCommands() {
    WorldSystemCommandExecuter cmdExecuter = new WorldSystemCommandExecuter();

    cmdExecuter.addCommand("test", new DebugCommandTest());

    this.getCommand("ws").setExecutor(cmdExecuter);
  }

}
