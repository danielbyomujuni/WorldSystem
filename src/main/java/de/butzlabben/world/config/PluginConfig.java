package de.butzlabben.world.config;

import de.butzlabben.world.exceptions.InvalidConfigFormatException;
import de.butzlabben.world.utils.PlanerCords;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;

public class PluginConfig {


    //New Config
    private YamlConfiguration config;
    private File configFile;


    //TODO Document
    public PluginConfig(File configFile) throws FileNotFoundException {
        this.configFile = configFile;
        try {
            config = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Cannot access config file");
        }

        try {
            verifyConfigFormating();
        } catch (InvalidConfigFormatException e) {
            try {
                Files.copy(configFile.toPath(),
                        new File(configFile.getParentFile(), "config-broken-"
                                + new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new Date()) + ".yml").toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
                Files.delete(configFile.toPath());
                System.err.println("[WorldSystem] Config is broken, creating a new one!");

                //TODO Create new Config
            } catch (IOException ex) {

                //Somthing Really Bad Happened
                //TODO Log it
                ex.printStackTrace();
            }
            try {
                verifyConfigFormating();
            } catch (InvalidConfigFormatException ex) {
                //Should Never Run
                throw new RuntimeException(ex);
            }
        }

    }

    private void verifyConfigFormating() throws InvalidConfigFormatException
    {
        //Verify General
        if (!(config.isString("playerWorldsDir") &&
            config.isInt("unloadTime") &&
            config.isString("prefix") &&
            config.isInt("deleteAfterDays") &&
            config.isString("worldDifficulty")))
        {
            throw new InvalidConfigFormatException("Invaild Config Format in General Settings");
        }
        //Verify World Creation Settings
        if (!(config.isBoolean("multiChoose") &&
            config.isString("defaultGenerator") &&
            config.isString("worldGenTemplates") &&
            config.isInt("worldBorderDefaultSize") &&
            config.isInt("worldBorderCenter.x") &&
            config.isInt("worldBorderCenter.z")
        ))
        {
            throw new InvalidConfigFormatException("Invaild Config Format in World Creation Settings");
        }

        if (!(config.isString("serverSpawn.serverGamemode") &&
            config.isString("serverSpawn.serverSpawnPoint.worldName") &&
            config.isInt("serverSpawn.serverSpawnPoint.x") &&
            config.isInt("serverSpawn.serverSpawnPoint.y") &&
            config.isInt("serverSpawn.serverSpawnPoint.z") &&
            config.isString("wsWorldSpawn.worldGameMode") &&
            config.isBoolean("wsWorldSpawn.useLastLocation") &&
            config.isString("wsWorldSpawn.defaultWorldSpawnPoint.worldName") &&
            config.isInt("wsWorldSpawn.defaultWorldSpawnPoint.x") &&
            config.isInt("wsWorldSpawn.defaultWorldSpawnPoint.y") &&
            config.isInt("wsWorldSpawn.defaultWorldSpawnPoint.z")))
        {
            throw new InvalidConfigFormatException("Invaild Config Format in World Entering/Exiting");
        }

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
    }


    //General Setting Getters


    public String getLanguage()
    {
        return config.getString("language");
    }

    public String getWorldDir()
    {
        return config.getString("playerWorldsDir");
    }

    public int getUnloadTime()
    {
        return config.getInt("unloadTime");
    }

    public String getPrefix()
    {
        return config.getString("prefix");
    }

    public int getDeleteAfter() {
        return config.getInt("deleteAfterDays");
    }

    public Difficulty getWorldDifficulty() {
        return stringToDifficulty(config.getString("worldDifficulty"));
    }

    //World Creation Setting Getters

    public boolean allowsMultiChoice() {
        return config.getBoolean("multiChoose");
    }

    public String getDefaultWorldGenerator() {
        return config.getString("defaultGenerator");
    }

    public String getGeneratorTemplatesDir() {
        return config.getString("worldGenTemplates");
    }

    public int getDefaultWorldBorderSize() {
        return config.getInt("worldBorderDefaultSize");
    }

    public PlanerCords getWorldBorderCords() {
        return new PlanerCords(config.getInt("worldBorderCenter.x"), config.getInt("worldBorderCenter.y"));
    }

    //World Entering/Exiting Getters

    public GameMode getServerGamemode() {

        return stringToGamemode(config.getString("serverSpawn.serverGamemode"));
    }






    private GameMode stringToGamemode(String gm) {
        switch (gm.toLowerCase()) {
            case "creative":
                return GameMode.CREATIVE;
            case "adventure":
                return GameMode.ADVENTURE;
            default:
                return GameMode.SURVIVAL;
        }
    }

    private Difficulty stringToDifficulty(String diff) {
        switch (diff.toUpperCase()) {
            case "EASY":
                return Difficulty.EASY;
            case "NORMAL":
                return Difficulty.NORMAL;
            case "HARD":
                return Difficulty.HARD;
            default:
                return Difficulty.PEACEFUL;
        }
    }
}
