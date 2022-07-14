package de.butzlabben.world;

import de.butzlabben.world.autoupdater.AutoUpdater;
import de.butzlabben.world.command.CommandRegistry;
import de.butzlabben.world.config.*;
import de.butzlabben.world.listener.*;
import de.butzlabben.world.util.PapiExtension;
import de.butzlabben.world.util.PlayerPositions;
import de.butzlabben.world.util.VersionUtil;
import de.butzlabben.world.util.database.DatabaseProvider;
import de.butzlabben.world.wrapper.AsyncCreatorAdapter;
import de.butzlabben.world.wrapper.CreatorAdapter;
import de.butzlabben.world.wrapper.SystemWorld;
// import org.bstats.bukkit.Metrics; // Argantiu Development hates bstats, cause of data privecy. Also it isn't working.
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * @author Butzlabben
 * @author Jubeki
 * @version 2.2.0.1
 * @since 10.07.2017
 */
public class WorldSystem extends JavaPlugin {
    private static boolean is1_13Plus = false;
    final private String version = this.getDescription().getVersion();
    private CreatorAdapter creator;

    public static void createConfigs() {
        File folder = getInstance().getDataFolder();
        File dir = new File(folder + "/worldsources");
        File config = new File(folder, "config.yml");
        File dconfig = new File(folder, "dependence.yml");
        File languages = new File(folder + "/languages");
        File gui = new File(folder, "gui.yml");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!languages.exists())
            languages.mkdirs();

        PluginConfig.checkConfig(config);

        // Done with #6
        MessageConfig.checkConfig(new File(languages, "en.yml"));

        MessageConfig.checkConfig(new File(languages, "de.yml"));
        MessageConfig.checkConfig(new File(languages, "hu.yml"));
        MessageConfig.checkConfig(new File(languages, "nl.yml"));
        MessageConfig.checkConfig(new File(languages, "pl.yml"));
        MessageConfig.checkConfig(new File(languages, "es.yml"));
        MessageConfig.checkConfig(new File(languages, "ru.yml"));
        MessageConfig.checkConfig(new File(languages, "fi.yml"));
        MessageConfig.checkConfig(new File(languages, "ja.yml"));
        // Here we are for #5
        MessageConfig.checkConfig(new File(languages, "zh.yml"));
        MessageConfig.checkConfig(new File(languages, "fr.yml"));

        // If has custom language
        MessageConfig.checkConfig(new File(languages, PluginConfig.getLanguage() + ".yml"));

        if (!dconfig.exists()) {
            try {
                dconfig.createNewFile();
            } catch (IOException e) {
                System.err.println("Wasn't able to create DependenceConfig");
                e.printStackTrace();
            }
            new DependenceConfig();
        }

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(config);
        SettingsConfig.checkConfig();

        File worlddir = new File(cfg.getString("worldfolder"));
        if (!worlddir.exists()) {
            worlddir.mkdirs();
        }

        GuiConfig.checkConfig(gui);
    }

    public static WorldSystem getInstance() {
        return JavaPlugin.getPlugin(WorldSystem.class);
    }

    @Override
    public void onEnable() {
        //////
        getCommand("ws").setExecutor(new CommandRegistry());

        // Set right version
        if (VersionUtil.getVersion() >= 13)
            is1_13Plus = true;

        createConfigs();

        // Establish database connection
        DatabaseProvider.instance.util.connect();

        // Fix for  #34
        // Check if tables exist and create them if necessary.
        PlayerPositions.instance.checkTables();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new BlockListener(), this);
        pm.registerEvents(new CommandListener(), this);
        pm.registerEvents(new WorldInitSkipSpawn(), this);
        if (pm.getPlugin("WorldEdit") != null)
            pm.registerEvents(new WorldEditListener(), this);


        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new WorldCheckerRunnable(), 20 * 5,
                20 * PluginConfig.getLagCheckPeriod());

        if (PluginConfig.useGC()) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new GCRunnable(), 20 * 5,
                    20 * PluginConfig.getGCPeriod());
        }


        /* TODO better check this only on worldInitEvent
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (World w : Bukkit.getWorlds()) {
                SystemWorld sw = SystemWorld.getSystemWorld(w.getName());
                if (sw != null && sw.isLoaded())
                    SettingsConfig.editWorld(w);

            }
        }, 20, 20 * 10);*/

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (World w : Bukkit.getWorlds()) {
                SystemWorld.tryUnloadLater(w);
            }
        }, 20 * 60 * 2, 20 * 60 * 2);

        //COMMANDS
        System.out.println("Registered");

        //this.getCommand("ws").setExecutor(new WSCommandMain());
        //this.getCommand("ws").setExecutor(new CommandMain());
        //this.getCommand("ws get").setExecutor(new WSGet());
        //this.getCommand("ws reset").setExecutor(new WorldReset());
        //this.getCommand("ws home").setExecutor(new WorldHome());
        //this.getCommand("ws tnt").setExecutor(new WorldTnt());
        //this.getCommand("ws fire").setExecutor(new WorldFire());


        System.setProperty("bstats.relocatecheck", "false");
        Metrics m = new Metrics(this);
        m.addCustomChart(new Metrics.SingleLineChart("worlds", DependenceConfig::getHighestID));

        //AutoUpdater.startAsync();

        // Choose right creatoradapter for #16
        if (Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit") != null
                && Bukkit.getPluginManager().getPlugin("WorldEdit") != null
                && PluginConfig.loadWorldsASync()
                && !is1_13Plus) {

            creator = new AsyncCreatorAdapter();
            Bukkit.getConsoleSender()
                    .sendMessage(PluginConfig.getPrefix() + "Found FAWE! Worlds now will be created asynchronously");
        } else {
            creator = (c, sw, r) -> {
                Bukkit.getWorlds().add(c.createWorld());
                if (sw != null)
                    sw.setCreating(false);
                r.run();
            };
        }

        // Starting for #28
        if (PluginConfig.shouldDelete()) {
            Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix()
                    + "Searching for old worlds to delete if not loaded for " + PluginConfig.deleteAfter() + " days");
            DependenceConfig.checkWorlds();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new PapiExtension().register();

        Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "Successfully enabled WorldSystem v" + version);
    }

    @Override
    public void onDisable() {
        for (World w : Bukkit.getWorlds()) {
            SystemWorld sw = SystemWorld.getSystemWorld(w.getName());
            if (sw != null && sw.isLoaded()) {
                sw.directUnload(w);
            }
        }

        // Close database connection
        DatabaseProvider.instance.util.close();

        Bukkit.getConsoleSender()
                .sendMessage(PluginConfig.getPrefix() + "Successfully disabled WorldSystem v" + version);
    }

    public CreatorAdapter getAdapter() {
        return creator;
    }

}
