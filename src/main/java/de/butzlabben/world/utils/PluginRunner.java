package de.butzlabben.world;

import de.butzlabben.world.commands.WorldSystemCommandExecuter;
import de.butzlabben.world.commands.commands.DebugCommandTest;
import de.butzlabben.world.commands.commands.WorldSystemTabComplete;
import de.butzlabben.world.config.PluginConfig;
import org.bukkit.World;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.io.FileNotFoundException;

public class main extends JavaPlugin
{

  private WorldSystem plugin;
  @Override
  public void onEnable() {
    plugin = new WorldSystem();
    plugin.onEnable();
  }

  public static main getInstance() {
    return JavaPlugin.getPlugin(main.class);
  }

}
