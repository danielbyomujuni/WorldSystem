package de.butzlabben.world;

import de.butzlabben.world.commands.WorldSystemCommandExecuter;
import de.butzlabben.world.commands.commands.DebugCommandTest;
import de.butzlabben.world.commands.commands.WorldSystemTabComplete;
import de.butzlabben.world.config.PluginConfig;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.io.FileNotFoundException;

public class WorldSystem extends JavaPlugin
{

  private static PluginConfig cfg;

  public WorldSystem()
  {
    super();
  }

  protected WorldSystem(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
  {
    super(loader, description, dataFolder, file);
  }

  @Override
  public void onEnable() {
    createConfigs();

    createCommands();
  }


  private void createCommands() {
    WorldSystemCommandExecuter cmdExecuter = new WorldSystemCommandExecuter();

    if (getWSConfig().allowDevCommands())
    {
      cmdExecuter.addCommand("test", new DebugCommandTest());
    }

    this.getCommand("ws").setExecutor(cmdExecuter);
    this.getCommand("ws").setTabCompleter(new WorldSystemTabComplete(cmdExecuter));
  }

  private void createConfigs() {
    File folder = getInstance().getDataFolder();
    try
    {
      cfg = new PluginConfig(new File(folder, "config.yml"));
    }
    catch (FileNotFoundException e)
    {
      throw new RuntimeException(e);
    }
  }

  public static PluginConfig getWSConfig() {
    return cfg;
  }

  public static WorldSystem getInstance() {
    return JavaPlugin.getPlugin(WorldSystem.class);
  }

}
