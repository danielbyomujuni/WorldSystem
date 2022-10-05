package de.butzlabben.world.config;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameRules
{
  @Test
  public void testGameruleInit() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertNotNull(cfg.getGamerules());
  }

  @Test
  public void testAnnounceAdvancements() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertTrue(cfg.getGamerules().announceAdvancements());
  }

  @Test
  public void testCommandBlockOutput() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertFalse(cfg.getGamerules().commandBlockOutput());
  }

  @Test
  public void testElytraMovementCheck() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertFalse(cfg.getGamerules().disableElytraMovementCheck());
  }

  @Test
  public void testDaylightCycle() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertTrue(cfg.getGamerules().doDaylightCycle());
  }

  @Test
  public void testEntityDrops() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertTrue(cfg.getGamerules().doEntityDrops());
  }

  @Test
  public void testFireTick() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertTrue(cfg.getGamerules().doFireTick());
  }

  @Test
  public void testLimitCrafting() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertFalse(cfg.getGamerules().doLimitedCrafting());
  }

  @Test
  public void testMobLoot() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertTrue(cfg.getGamerules().doMobLoot());
  }

  @Test
  public void testMobSpawning() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertTrue(cfg.getGamerules().doMobSpawning());
  }

  @Test
  public void testTileDrops() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertTrue(cfg.getGamerules().doTileDrops());
  }

  @Test
  public void testWeatherCycle() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertFalse(cfg.getGamerules().doWeatherCycle());
  }

  @Test
  public void testLoopFunction() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertFalse(cfg.getGamerules().gameLoopFunction());
  }

  @Test
  public void testKeepInventory() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertTrue(cfg.getGamerules().keepInventory());
  }

  @Test
  public void testAdminCommandLogs() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertTrue(cfg.getGamerules().logAdminCommands());
  }

  @Test
  public void testCommandChainLength() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertEquals(65536, cfg.getGamerules().maxCommandChainLength());
  }

  @Test
  public void testMaxEntityCramming() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertEquals(24, cfg.getGamerules().maxEntityCramming());
  }

  @Test
  public void testMobGriefing() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertTrue(cfg.getGamerules().mobGriefing());
  }

  @Test
  public void testNaturalRegen() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertTrue(cfg.getGamerules().naturalRegeneration());
  }

  @Test
  public void testRandomTickSpeed() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertEquals(3, cfg.getGamerules().randomTickSpeed());
  }

  @Test
  public void testReducedDebugInfo() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertFalse(cfg.getGamerules().reducedDebugInfo());
  }

  @Test
  public void testCommandFeedback() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertTrue(cfg.getGamerules().sendCommandFeedback());
  }

  @Test
  public void testDeathMessage() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertTrue(cfg.getGamerules().showDeathMessages());
  }

  @Test
  public void testSpawnRad() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertEquals(10, cfg.getGamerules().spawnRadius());
  }

  @Test
  public void testSpectatorsChunkGen() throws FileNotFoundException
  {
    File cfgFile = new File("TestFiles/TestConfig.yml");
    PluginConfig cfg = new PluginConfig(cfgFile);

    assertTrue(cfg.getGamerules().spectatorsGenerateChunks());
  }

}
