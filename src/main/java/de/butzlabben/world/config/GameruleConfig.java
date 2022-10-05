package de.butzlabben.world.config;

import de.butzlabben.world.exceptions.InvalidConfigFormatException;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * This class handels the data from the
 * config relevant to Minecraft's Gamerules
 */
public class GameruleConfig
{

  private YamlConfiguration config;
  public GameruleConfig(PluginConfig cfg) {
    config = cfg.config;
    try
    {
      verifyGamerules();
    }
    catch (InvalidConfigFormatException e)
    {
      try
      {
        cfg.brokenConfig();
      }
      catch (IOException ex)
      {
        //Bad Thing
        //Log
        throw new RuntimeException(ex);
      }
    }
  }

  private boolean verifyGamerules() throws InvalidConfigFormatException
  {
    if (!(config.isBoolean("announceAdvancements") &&
        config.isBoolean("commandBlockOutput") &&
        config.isBoolean("disableElytraMovementCheck") &&
        config.isBoolean("doDaylightCycle") &&
        config.isBoolean("doEntityDrops") &&
        config.isBoolean("doFireTick") &&
        config.isBoolean("doLimitedCrafting") &&
        config.isBoolean("doMobLoot") &&
        config.isBoolean("doMobSpawning") &&
        config.isBoolean("doTileDrops") &&
        config.isBoolean("doWeatherCycle") &&
        config.isBoolean("gameLoopFunction") &&
        config.isBoolean("keepInventory") &&
        config.isBoolean("logAdminCommands") &&
        config.isInt("maxCommandChainLength") &&
        config.isInt("maxEntityCramming") &&
        config.isBoolean("mobGriefing") &&
        config.isBoolean("naturalRegeneration") &&
        config.isInt("randomTickSpeed") &&
        config.isBoolean("reducedDebugInfo") &&
        config.isBoolean("sendCommandFeedback") &&
        config.isBoolean("showDeathMessages") &&
        config.isInt("spawnRadius") &&
        config.isBoolean("spectatorsGenerateChunks")))
    {
      throw new InvalidConfigFormatException("Invaild Config Format in Gamerules ");
    }
    return true;
  }

  public boolean commandBlockOutput() {
    return config.getBoolean("commandBlockOutput");
  }

  public boolean announceAdvancements() {
    return config.getBoolean("announceAdvancements");
  }

  public boolean disableElytraMovementCheck() {
    return config.getBoolean("disableElytraMovementCheck");
  }

  public boolean doDaylightCycle() {
    return config.getBoolean("doDaylightCycle");
  }

  public boolean doEntityDrops() {
    return config.getBoolean("doEntityDrops");
  }


  public boolean doFireTick() {
    return config.getBoolean("doFireTick");
  }

  public boolean doLimitedCrafting() {
    return config.getBoolean("doLimitedCrafting");
  }

  public boolean doMobLoot() {
    return config.getBoolean("doMobLoot");
  }

  public boolean doMobSpawning() {
    return config.getBoolean("doMobSpawning");
  }

  public boolean doTileDrops() {
    return config.getBoolean("doTileDrops");
  }

  public boolean doWeatherCycle() {
    return config.getBoolean("doWeatherCycle");
  }

  public boolean gameLoopFunction() {
    return config.getBoolean("gameLoopFunction");
  }

  public boolean keepInventory() {
    return config.getBoolean("doDaylightCycle");
  }

  public boolean logAdminCommands() {
    return config.getBoolean("logAdminCommands");
  }

  public int maxCommandChainLength() {
    return config.getInt("maxCommandChainLength");
  }

  public int maxEntityCramming() {
    return config.getInt("maxEntityCramming");
  }

  public boolean mobGriefing() {
    return config.getBoolean("doDaylightCycle");
  }

  public boolean naturalRegeneration() {
    return config.getBoolean("logAdminCommands");
  }

  public int randomTickSpeed() {
    return config.getInt("randomTickSpeed");
  }

  public boolean reducedDebugInfo() {
    return config.getBoolean("reducedDebugInfo");
  }

  public boolean sendCommandFeedback() {
    return config.getBoolean("sendCommandFeedback");
  }

  public boolean showDeathMessages() {
    return config.getBoolean("showDeathMessages");
  }

  public int spawnRadius() {
    return config.getInt("spawnRadius");
  }

  public boolean spectatorsGenerateChunks() {
    return config.getBoolean("spectatorsGenerateChunks");
  }
}
