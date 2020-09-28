package de.butzlabben.world.config;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.wrapper.SystemWorld;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SettingsConfig {

    private static final HashMap<String, Long> borderSizes = new HashMap<>();
    public static final HashMap<String, Long> worldLimit = new HashMap<>();
    private static File file;

    private SettingsConfig() {
    }

    @SuppressWarnings("deprecation")
    public static void editWorld(World w) {
        YamlConfiguration cfg = getConfig();

        SystemWorld sw = SystemWorld.getSystemWorld(w.getName());

        boolean shouldChange = cfg.getBoolean("worldborder.should_change", false);
        if (shouldChange) {
            long size = cfg.getLong("worldborder.normal", 1000);
            if (sw != null && sw.isLoaded()) {
                String worldname = w.getName();
                UUID uuid = UUID.fromString(worldname.substring(worldname.length() - 36));
                Player p = Bukkit.getPlayer(uuid);

                // Only edit worldborder size if owner is online
                if (p != null && p.isOnline()) {

                    // Check permissions
                    for (String string : borderSizes.keySet()) {
                        if (p.hasPermission(string) && size < borderSizes.get(string)) {
                            size = borderSizes.get(string);
                        }
                    }

                    w.getWorldBorder().setSize(size);
                }

                if (cfg.getBoolean("worldborder.center.as_spawn", true)) {
                    if (PluginConfig.useWorldSpawn()) {
                        w.getWorldBorder().setCenter(PluginConfig.getWorldSpawn(w));
                    } else {
                        w.getWorldBorder().setCenter(w.getSpawnLocation());
                    }
                } else {
                    Location loc = new Location(w, cfg.getDouble("worldborder.center.x", 0),
                            cfg.getDouble("worldborder.center.y", 20), cfg.getDouble("worldborder.center.z", 0));
                    w.getWorldBorder().setCenter(loc);
                }
                if (cfg.getBoolean("worldborder.center.as_home")) {
                    WorldConfig config = WorldConfig.getWorldConfig(w.getName());
                    if (config.getHome() != null)
                        w.getWorldBorder().setCenter(config.getHome());
                }
            }
        }

        // Fix for #17
        String diff = cfg.getString("difficulty");
        try {
            Difficulty difficulty = Difficulty.valueOf(diff.toUpperCase());
            w.setDifficulty(difficulty);
        } catch (IllegalArgumentException e) {
            Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "§cUnknown difficulty \"" + diff + "\" in settings.yml");
        }

        if (w.isGameRule("announceAdvancements"))
            w.setGameRuleValue("announceAdvancements", cfg.getString("announceAdvancements"));

        if (w.isGameRule("commandBlockOutput"))
            w.setGameRuleValue("commandBlockOutput", cfg.getString("commandBlockOutput"));

        if (w.isGameRule("disableElytraMovementCheck"))
            w.setGameRuleValue("disableElytraMovementCheck", cfg.getString("disableElytraMovementCheck"));

        if (w.isGameRule("doDaylightCycle"))
            w.setGameRuleValue("doDaylightCycle", cfg.getString("doDaylightCycle"));

        if (w.isGameRule("doEntityDrops"))
            w.setGameRuleValue("doEntityDrops", cfg.getString("doEntityDrops"));

        if (w.isGameRule("doFireTick"))
            w.setGameRuleValue("doFireTick", cfg.getString("doFireTick"));

        if (w.isGameRule("doLimitedCrafting"))
            w.setGameRuleValue("doLimitedCrafting", cfg.getString("doLimitedCrafting"));

        if (w.isGameRule("doMobLoot"))
            w.setGameRuleValue("doMobLoot", cfg.getString("doMobLoot"));

        if (w.isGameRule("doMobSpawning"))
            w.setGameRuleValue("doMobSpawning", cfg.getString("doMobSpawning"));

        if (w.isGameRule("doTileDrops"))
            w.setGameRuleValue("doTileDrops", cfg.getString("doTileDrops"));

        if (w.isGameRule("doWeatherCycle"))
            w.setGameRuleValue("doWeatherCycle", cfg.getString("doWeatherCycle"));

        if (w.isGameRule("gameLoopFunction"))
            w.setGameRuleValue("gameLoopFunction", cfg.getString("gameLoopFunction"));

        if (w.isGameRule("keepInventory"))
            w.setGameRuleValue("keepInventory", cfg.getString("keepInventory"));

        if (w.isGameRule("logAdminCommands"))
            w.setGameRuleValue("logAdminCommands", cfg.getString("logAdminCommands"));

        if (w.isGameRule("maxCommandChainLength"))
            w.setGameRuleValue("maxCommandChainLength", cfg.getString("maxCommandChainLength"));

        if (w.isGameRule("maxEntityCramming"))
            w.setGameRuleValue("maxEntityCramming", cfg.getString("maxEntityCramming"));

        if (w.isGameRule("mobGriefing"))
            w.setGameRuleValue("mobGriefing", cfg.getString("mobGriefing"));

        if (w.isGameRule("naturalRegeneration"))
            w.setGameRuleValue("naturalRegeneration", cfg.getString("naturalRegeneration"));

        if (w.isGameRule("randomTickSpeed"))
            w.setGameRuleValue("randomTickSpeed", cfg.getString("randomTickSpeed"));

        if (w.isGameRule("reducedDebugInfo"))
            w.setGameRuleValue("reducedDebugInfo", cfg.getString("reducedDebugInfo"));

        if (w.isGameRule("sendCommandFeedback"))
            w.setGameRuleValue("sendCommandFeedback", cfg.getString("sendCommandFeedback"));

        if (w.isGameRule("showDeathMessages"))
            w.setGameRuleValue("showDeathMessages", cfg.getString("showDeathMessages"));

        if (w.isGameRule("spawnRadius"))
            w.setGameRuleValue("spawnRadius", cfg.getString("spawnRadius"));

        if (w.isGameRule("spectatorsGenerateChunks"))
            w.setGameRuleValue("spectatorsGenerateChunks", cfg.getString("spectatorsGenerateChunks"));
    }

    private static YamlConfiguration getConfig() {
        try {
            return YamlConfiguration
                    .loadConfiguration(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void checkConfig() {
        File file = new File(WorldSystem.getInstance().getDataFolder(), "settings.yml");
        SettingsConfig.file = file;
        if (!file.exists()) {
            try {
                InputStream in = JavaPlugin.getPlugin(WorldSystem.class).getResource("settings.yml");
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                System.err.println("Wasn't able to create Config");
                e.printStackTrace();
            }
        }
        YamlConfiguration cfg = getConfig();
        for (String s : cfg.getConfigurationSection("worldborder.ranks").getKeys(true)) {
            if (cfg.isInt("worldborder.ranks." + s) || cfg.isLong("worldborder.ranks." + s))
                borderSizes.put(s, cfg.getLong("worldborder.ranks." + s));
        }
        for (String s : cfg.getConfigurationSection("worldLimit.ranks").getKeys(true)) {
            if (cfg.isInt("worldLimit.ranks." + s) || cfg.isLong("worldLimit.ranks." + s))
                worldLimit.put(s, cfg.getLong("worldLimit.ranks." + s));
        }
    }

    /**
     * @return the commands specified in settings.yml on /ws get
     */
    public static List<String> getCommandsonGet() {
        YamlConfiguration cfg = getConfig();
        return cfg.getStringList("commands_on_get");
    }
}
