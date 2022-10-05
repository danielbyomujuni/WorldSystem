package de.butzlabben.world.config;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestPluginConfig {
    @Test
    public void testPluginConfigInit() throws FileNotFoundException {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);
    }


    //TODO ADD Invalid Config Test



    //TESTS For General Settings

    @Test
    public void testGetLanguage() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);
        assertEquals("en", cfg.getLanguage());
    }

    @Test
    public void testGetWorldDir() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);
        assertEquals("plugins/WorldSystem/Worlds", cfg.getWorldDir());
    }

    @Test
    public void testGetUnloadTime() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);
        assertEquals(20, cfg.getUnloadTime());
    }

    @Test
    public void testGetPrefix() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);
        assertEquals("&8[&3WorldSystem&8] &6", cfg.getPrefix());
    }

    @Test
    public void testGetDeleteAfter() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);
        assertEquals(-1, cfg.getDeleteAfter());
    }

    @Test
    public void testGameDifficulty() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);
        assertEquals(Difficulty.EASY, cfg.getWorldDifficulty());
    }

    //TESTS For General Settings World Creation Setting

    @Test
    public void testMultiChoice() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);
        assertEquals(false, cfg.allowsMultiChoice());
    }
    @Test
    public void testgetDefaultGenerator() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);
        assertEquals("Vanilla", cfg.getDefaultWorldGenerator());
    }
    @Test
    public void worldGenTemplates() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);
        assertEquals("plugins/WorldSystem/Generators", cfg.getGeneratorTemplatesDir());
    }
    @Test
    public void testWorldBorderDefaultSize() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);
        assertEquals(500, cfg.getDefaultWorldBorderSize());
    }

    @Test
    public void testWorldBorderCords() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);

        assertEquals(0, cfg.getWorldBorderCords().getX());
        assertEquals(0, cfg.getWorldBorderCords().getZ());
    }


    //TESTS for World Entering/Exiting


    @Test
    public void testServerGamemode() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);
        assertEquals(GameMode.SURVIVAL, cfg.getServerGamemode());
    }

    @Test
    public void testGetServerSpawnPoint() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);

        assertEquals(0, cfg.getServerSpawnPoint().getX());
        assertEquals(60, cfg.getServerSpawnPoint().getY());
        assertEquals(0, cfg.getServerSpawnPoint().getZ());
    }

    @Test
    public void testGetServerWorldName() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);

        assertEquals("world", cfg.getServerWorldName());
    }

    @Test
    public void testPlayerWorldGamemode() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);
        assertEquals(GameMode.SURVIVAL, cfg.getPlayerWorldGamemode());
    }

    @Test
    public void testUsePlayersLastLocation() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);

        assertFalse(cfg.usePlayerWorldLastLocation());
    }

    @Test
    public void testGetPlayerWorldDefaultSpawnPoint() throws FileNotFoundException
    {
        File cfgFile = new File("TestFiles/TestConfig.yml");
        PluginConfig cfg = new PluginConfig(cfgFile);

        assertEquals(0, cfg.getPlayerWorldSpawnPoint().getX());
        assertEquals(60, cfg.getPlayerWorldSpawnPoint().getY());
        assertEquals(0, cfg.getPlayerWorldSpawnPoint().getZ());
    }

}
