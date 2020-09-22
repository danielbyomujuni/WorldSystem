package de.butzlabben.world.config;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class DependenceConfig {

    private UUID uuid;

    public DependenceConfig() {
        setConfig();
    }

    @SuppressWarnings("deprecation")
    public DependenceConfig(String s) {
        OfflinePlayer op = null;
        try {
            op = Bukkit.getOfflinePlayer(UUID.fromString(s));
        } catch (Exception ignored) {
        }
        if (op == null) {
            op = Bukkit.getOfflinePlayer(s);
        }
        uuid = op.getUniqueId();
    }

    public DependenceConfig(Player p) {
        uuid = p.getUniqueId();
        refreshName();
    }

    public DependenceConfig(OfflinePlayer p) {
        uuid = p.getUniqueId();
        refreshName();
    }

    public DependenceConfig(UUID uuid) {
        this.uuid = uuid;
    }

    public static int getHighestID() {
        File dconfig = new File("plugins//WorldSystem//dependence.yml");
        YamlConfiguration dcfg = YamlConfiguration.loadConfiguration(dconfig);
        return dcfg.getInt("HighestID");
    }

    public static void checkWorlds() {//TODO Bug Fix?
        File dconfig = new File("plugins//WorldSystem//dependence.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);

        long deleteTime = 1000 * 60 * 60 * 24 * PluginConfig.deleteAfter();
        long now = System.currentTimeMillis();
        for (String s : cfg.getConfigurationSection("Dependences").getKeys(false)) {
            for (String w : cfg.getConfigurationSection("Dependences." + s).getKeys(false)) {
                if (!cfg.isLong("Dependences." + s + "." + w + ".last_loaded") && !cfg.isInt("Dependences." + s + ".last_loaded"))
                    continue;
                long lastLoaded = cfg.getLong("Dependences." + s + ".last_loaded");
                long diff = now - lastLoaded;
                if (diff > deleteTime) {
                    Bukkit.getConsoleSender().sendMessage(
                            PluginConfig.getPrefix() + "World " + w + " of " + s + " was not loaded for too long. Deleting!");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ws delete " + s);
                }
            }
        }
    }

    private void setConfig() {
        File dconfig = new File("plugins//WorldSystem//dependence.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
        cfg.set("HighestID", -1);
        try {
            cfg.save(dconfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshName() {//TODO Bug Fix?
        if (hasWorld()) {
            File dconfig = new File("plugins//WorldSystem//dependence.yml");
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
            String uuid = this.uuid.toString();
            for (String w : cfg.getConfigurationSection("Dependences." + uuid).getKeys(false)) {
                cfg.set("Dependences." + uuid + "." + w + ".ActualName", Bukkit.getOfflinePlayer(this.uuid).getName());
            }
            try {
                cfg.save(dconfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createNewEntry() {//TODO Bug Fix?
        File dconfig = new File("plugins//WorldSystem//dependence.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
        String uuid = this.uuid.toString();
        int id = cfg.getInt("HighestID");
        id++;
        cfg.set("HighestID", id);
        cfg.set("Dependences." + uuid + ".world" + cfg.getConfigurationSection("Dependences." + uuid).getKeys(false).size() + ".ID", id);
        cfg.set("Dependences." + uuid + ".world" + cfg.getConfigurationSection("Dependences." + uuid).getKeys(false).size() + ".ActualName", Bukkit.getOfflinePlayer(this.uuid).getName());
        try {
            cfg.save(dconfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasWorld() {//TODO Bug Fix?
        File dconfig = new File("plugins//WorldSystem//dependence.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
        String uuid = this.uuid.toString();
        //Fix for #40
        if (cfg.getConfigurationSection("Dependences." + uuid).getKeys(false).size() >= 0) {
            return true;
        }
        return false;
    }

    public String getWorldname(int WorldNumber) {//TODO Bug Fix?
        File dconfig = new File("plugins//WorldSystem//dependence.yml");
        YamlConfiguration dcfg = YamlConfiguration.loadConfiguration(dconfig);
        String uuid = this.uuid.toString();
        int id = dcfg.getInt("Dependences." + uuid + ".world" + WorldNumber + ".ID");
        return "ID" + id + "-" + uuid;
    }

    public String getWorldNameByOfflinePlayer(int WorldNumber) {//TODO Bug Fix?
        String name;
        String uuid = this.uuid.toString();
        File dconfig = new File("plugins//WorldSystem//dependence.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
        if (cfg.getString("Dependences." + uuid + ".world" + WorldNumber + ".ActualName") == null) {
            name = "n";
        } else {
            name = "ID" + cfg.getInt("Dependences." + uuid + ".world" + WorldNumber + ".ID") + "-" + uuid;
        }
        return name;
    }

    public void setLastLoaded(int WorldNumber) {//TODO Bug Fix?
        File dconfig = new File("plugins//WorldSystem//dependence.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig);
        String uuid = this.uuid.toString();
        cfg.set("Dependences." + uuid + ".world" + WorldNumber + ".last_loaded", System.currentTimeMillis());
        try {
            cfg.save(dconfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getID(int WorldNumber) {//TODO Bug Fix?top
        File dconfig = new File("plugins//WorldSystem//dependence.yml");
        YamlConfiguration dcfg = YamlConfiguration.loadConfiguration(dconfig);
        return dcfg.getInt("Dependences." + this.uuid.toString() + ".world" + WorldNumber + ".ID");
    }

    public OfflinePlayer getOwner() {
        return Bukkit.getOfflinePlayer(uuid);
    }
}