package de.butzlabben.world.config;

import de.butzlabben.WorldSystem;
import de.butzlabben.world.exceptions.InvalidConfigFormatException;
import de.butzlabben.world.util.PlayerPositions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private void verifyConfigFormating() throws InvalidConfigFormatException {
        //Verify General
        if (!(config.isString("playerWorldsDir") &&
        config.isInt("unloadTime") &&
        config.isString("prefix") &&
        config.isInt("deleteAfterDays") &&
        config.isString("worldDifficulty"))) {
            throw new InvalidConfigFormatException("Invaild Config Format in General Settings");
        }
        //Verify World Creation Settings
        if (!(config.isBoolean("multiChoose") &&
                config.isString("defaultGenerator") &&
                config.isString("worldGenTemplates") &&
                config.isInt("worldBorderDefaultSize") &&
                config.isInt("worldBorderCenter.x") &&
                config.isInt("worldBorderCenter.z")
                )) {
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
                config.isInt("wsWorldSpawn.defaultWorldSpawnPoint.z")))  {
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
                config.isBoolean("spectatorsGenerateChunks"))) {
            throw new InvalidConfigFormatException("Invaild Config Format in Gamerules ");
        }
    }

    public String getWorldDir() {
        return config.getString("playerWorldsDir", "plugins/WorldSystem/Worlds") + "/";
    }

    public boolean useWorldSpawn() {
        return config.getBoolean("wsWorldSpawn.useLastLocation", true);
    }

    public GameMode getWorldSystemGamemode() {
        return stringToGamemode(config.getString("wsWorldSpawn.worldGameMode", "Survival"));
    }

    public GameMode getServerGamemode() {
        return stringToGamemode(config.getString("serverSpawn.serverGamemode", "Survival"));
    }

    public int getUnloadingTime() {
        return config.getInt("unloadTime", 20);
    }

    public boolean isMultiChoose() {
        return config.getBoolean("multiChoose", false);
    }

    public String getDefaultWorldGenerator() {
        return config.getString("defaultGenerator", "Vanilla");
    }

    public String getLanguage() {
        return config.getString("language", "en");
    }

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("prefix", "§8[§3WorldSystem§8] §6"));
    }

    public Location getWorldSpawn(World w) {
        return getLocation(config, "wsWorldSpawn.defaultWorldSpawnPoint", w);
    }

    public Location getSpawn(Player player) {
        Location location = getLocation(config, "wsWorldSpawn.defaultWorldSpawnPoint", Bukkit.getWorld(config.getString("wsWorldSpawn.defaultWorldSpawnPoint.worldName", "world")));

        //TODO Player Positions with PlayerWorldData;
        return PlayerPositions.instance.injectPlayersLocation(player, location);
    }


    private GameMode stringToGamemode(String gamemode) {
        switch (gamemode.toLowerCase()) {
            case "Creative":
                return GameMode.CREATIVE;
            case "Adventure":
                return GameMode.ADVENTURE;
            default:
                return GameMode.SURVIVAL;
        }
    }

    private static Location getLocation(YamlConfiguration cfg, String path, World world) {
        return new Location(world, cfg.getDouble(path + ".x", 0), cfg.getDouble(path + ".y", 20),
                cfg.getDouble(path + ".z", 0));
    }
   /*

    public static int getGCPeriod() {
        return getConfig().getInt("lagsystem.garbagecollector.period_in_minutes", 5);
    }

    public static boolean useGC() {
        return getConfig().getBoolean("lagsystem.garbagecollector.use", false);
    }

    public static int getEntitysPerWorld() {
        return getConfig().getInt("lagsystem.entities_per_world", 350);
    }

    public static int getLagCheckPeriod() {
        return getConfig().getInt("lagsystem.period_in_seconds", 10);
    }

/////////////////////////////////////

    public static Location getSpawn(Player player) {
        YamlConfiguration cfg = getConfig();
        Location location = getLocation(cfg, "spawn.spawnpoint", Bukkit.getWorld(cfg.getString("spawn.spawnpoint.world", "world")));
        return PlayerPositions.instance.injectPlayersLocation(player, location);
    }

    public static int getRequestExpire() {
        return getConfig().getInt("request_expires", 20);
    }

    public static boolean confirmNeed() {
        return getConfig().getBoolean("need_confirm", true);
    }

    public static boolean contactAuth() {
        return getConfig().getBoolean("contact_authserver", true);
    }

    public static boolean spawnTeleportation() {
        return getConfig().getBoolean("spawn_teleportation", true);
    }

    public static boolean shouldDelete() {
        return getConfig().getInt("delete_after") != -1;
    }

    public static long deleteAfter() {
        return getConfig().getLong("delete_after");
    }

    public static boolean useWorldSpawnLastLocation() {
        return getConfig().getBoolean("worldspawn.use_last_location");
    }

    public static boolean useSpawnLastLocation() {
        return getConfig().getBoolean("spawn.spawnpoint.use_last_location");
    }

    public static String getWorldsTableName() {
        return getConfig().getString("database.worlds_table_name");
    }

    public static String getPlayersTableName() {
        return getConfig().getString("database.players_table_name");
    }

    public static String getUUIDTableName() {
        return getConfig().getString("database.players_uuids");
    }



    public static String getDatabaseType() {
        return getConfig().getString("database.type");
    }

    public static String getSqliteFile() {
        return getConfig().getString("database.sqlite_settings.file");
    }

    public static String getMysqlHost() {
        return getConfig().getString("database.mysql_settings.host");
    }

    public static int getMysqlPort() {
        return getConfig().getInt("database.mysql_settings.port");
    }

    public static String getMysqlUser() {
        return getConfig().getString("database.mysql_settings.username");
    }

    public static String getMysqlPassword() {
        return getConfig().getString("database.mysql_settings.password");
    }

    public static String getMysqlDatabaseName() {
        return getConfig().getString("database.mysql_settings.database");
    }

    public static boolean loadWorldsASync() {
        return getConfig().getBoolean("load_worlds_async");
    }*/
}
