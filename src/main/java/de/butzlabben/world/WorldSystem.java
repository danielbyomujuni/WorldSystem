package de.butzlabben.world;

import de.butzlabben.world.commands.WorldSystemCommandExecuter;
import de.butzlabben.world.commands.commands.DebugCommandTest;
import de.butzlabben.world.commands.commands.WorldSystemTabComplete;
import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.utils.PluginRunner;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class WorldSystem
{
  private PluginRunner runner;
  protected static WorldSystem activeInst = null;

  private static PluginConfig cfg;


  private WorldSystem(PluginRunner run) {
    runner = run;
  }

  protected WorldSystem() {
    runner = null;
  }

  public void saveDefaultConfig()
  {
    runner.saveDefaultConfig();
  }

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

    runner.getCommand("ws").setExecutor(cmdExecuter);
    runner.getCommand("ws").setTabCompleter(new WorldSystemTabComplete(cmdExecuter));
  }

  private void createConfigs() {
    File folder = runner.getInstance().getDataFolder();
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
    if (activeInst == null) {
      activeInst = new WorldSystem(JavaPlugin.getPlugin(PluginRunner.class));
    }
    return activeInst;
  }

  public InputStream getResource(String filename) {
    return runner.getResource(filename);
  }

}
