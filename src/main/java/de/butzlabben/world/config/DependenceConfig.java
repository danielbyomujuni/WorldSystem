package de.butzlabben.world.config;

import de.butzlabben.WorldSystem.data.PlayerWorld;
import de.butzlabben.WorldSystem.data.WorldSystemData;
import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.util.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class DependenceConfig {

    private UUID uuid;

    public DependenceConfig() {
        setConfig();
    }

    public DependenceConfig(String s) {
        OfflinePlayer op = null;
        try {
            op = PlayerWrapper.getOfflinePlayer(UUID.fromString(s));
        } catch (Exception ignored) {
        }
        if (op == null) {
            op = PlayerWrapper.getOfflinePlayer(s);
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
        WorldSystemData db = WorldSystemData.connect();
        int highest_id =  db.getHighestID();
         

        return highest_id;
    }

    public static void checkWorlds() {
        long deleteTime = 1000 * 60 * 60 * 24 * PluginConfig.deleteAfter();
        long now = System.currentTimeMillis();

        WorldSystemData db = WorldSystemData.connect();

        PlayerWorld[] worlds = db.getAllWorlds();

        for (PlayerWorld s : worlds) {
            long lastLoaded = s.getLast_loaded();
            long diff = now - lastLoaded;
            if (diff > deleteTime) {
                Bukkit.getConsoleSender().sendMessage(
                        PluginConfig.getPrefix() + "World of " + s + " was not loaded for too long. Deleting!");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ws delete " + s);
            }
        }
    }

    private void setConfig() {
       //deprecated
    }

    public void refreshName() {
        if (hasWorld()) {
            String uuid = this.uuid.toString();
            String new_name = PlayerWrapper.getOfflinePlayer(this.uuid).getName();

            WorldSystemData db = WorldSystemData.connect();
            db.update_name(uuid, new_name);
             
        }
    }

    public void createNewEntry() {

        WorldSystemData db = WorldSystemData.connect();

        int id = db.getHighestID();
        String uuid = this.uuid.toString();
        String name = PlayerWrapper.getOfflinePlayer(this.uuid).getName();
        long last_loaded = System.currentTimeMillis();

        PlayerWorld world = new PlayerWorld(id, uuid, name, last_loaded);

        db.create_new_world_record(world);
         
    }

    public boolean hasWorld() {
        WorldSystemData db = WorldSystemData.connect();
        int count = db.getWorldCountForPlayer(this.uuid.toString());

        //Fix for #40
        return count >= 1;
    }

    public String getWorldname() {
        String uuid = this.uuid.toString();

        WorldSystemData db = WorldSystemData.connect();
        PlayerWorld[] worlds = db.getWorldsForPlayer(uuid);
         

        return String.format("ID%d-%s", worlds[0].getWorld_id(), uuid);
    }

    public String getWorldNameByOfflinePlayer() {
        return getWorldname();
    }

    public void setLastLoaded() {
        String uuid = this.uuid.toString();
        long new_time = System.currentTimeMillis();

        WorldSystemData db = WorldSystemData.connect();
        db.update_last_loaded(uuid, new_time);
         

    }

    public int getID() {
        WorldSystemData db = WorldSystemData.connect();
        PlayerWorld[] worlds = db.getWorldsForPlayer(this.uuid.toString());
         

        return worlds[0].getWorld_id();
    }

    public OfflinePlayer getOwner() {
        return PlayerWrapper.getOfflinePlayer(uuid);
    }
}
