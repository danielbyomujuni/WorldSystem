package de.butzlabben.world.utils;

import de.butzlabben.world.WorldSystem;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginRunner extends JavaPlugin
{

  private WorldSystem plugin;

  @Override
  public void onEnable() {
    plugin = WorldSystem.getInstance();
    plugin.onEnable();
  }

  public static PluginRunner getInstance() {
    return JavaPlugin.getPlugin(PluginRunner.class);
  }


}
