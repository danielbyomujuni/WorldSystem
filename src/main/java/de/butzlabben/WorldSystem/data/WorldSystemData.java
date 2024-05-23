package de.butzlabben.WorldSystem.data;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.config.PluginConfig;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;


public class WorldSystemData extends AbstractSqlLiteDatabase {

    private static final String WS_WORLDS = "ws_worlds";

    private WorldSystemData() throws SQLException {
        super(PluginConfig.getSqliteFile());
    }

    @SneakyThrows
    public static WorldSystemData connect() {
        try {
            return new WorldSystemData();
        } catch (SQLException e) {
            WorldSystem.disable_plugin();
            return new WorldSystemData(); //doesn't create
        }
    }

    @Override
    protected void construct_database() {
        try {
            //create the worlds table
            this.query(String.format("CREATE TABLE IF NOT EXISTS %s (world_id INTEGER PRIMARY KEY, player_uuid varchar(40), player_name varchar(20), last_loaded long);", WS_WORLDS)).close();
        } catch (SQLException e) {
            WorldSystem.logger().log(Level.SEVERE, "[WorldSystem] Unable To create the SQLite Database");
            WorldSystem.disable_plugin();
        }
    }

    public int getHighestID() {
        try {
            ResultSet res = this.query(String.format("SELECT count(*) FROM %s", WS_WORLDS));
            res.next();
            int count = res.getInt("count(*)");

            res.close();
            return count - 1;
        } catch (SQLException e) {
            return 0;
        }
    }

    public PlayerWorld[] getAllWorlds() {
        try {
            List<PlayerWorld> worlds = new ArrayList<PlayerWorld>();

            ResultSet res = this.query(String.format("SELECT * FROM WS_WORLDS;", WS_WORLDS));

            while (res.next()) {
                int world_id = res.getInt("world_id");
                String player_uuid = res.getString("player_uuid");
                String player_name = res.getString("player_name");
                long last_loaded = res.getLong("last_loaded");

                worlds.add(new PlayerWorld(world_id, player_uuid, player_name, last_loaded));
            }
            res.close();
            return worlds.toArray(new PlayerWorld[worlds.size()]);
        } catch (SQLException e) {
            WorldSystem.logger().log(Level.SEVERE, "[WorldSystem] Unable To get all the worlds in the SQLite Database");
            WorldSystem.disable_plugin();
            return new PlayerWorld[0];
        }
    }

    public void update_name(String uuid, String new_name) {
        try {
            this.query(String.format("UPDATE %s SET player_name = %s WHERE player_uuid = %s;", WS_WORLDS, new_name, uuid)).close();
        } catch (SQLException e) {
            WorldSystem.logger().log(Level.SEVERE, "[WorldSystem] Tried to update player name but failed skipping");
        }
    }

    public void create_new_world_record(PlayerWorld world) {
        try {
            this.query(String.format("INSERT INTO %s (world_id, player_uuid, player_name, last_loaded)\n" +
                    "VALUES (%d, '%s', '%s', %d);", WS_WORLDS, world.getWorld_id(), world.getPlayer_uuid(), world.getPlayer_name(), world.getLast_loaded())).close();
        } catch (SQLException e) {
            WorldSystem.logger().log(Level.SEVERE, "[WorldSystem] File to Insert to world for player name but failed Disabling");
            WorldSystem.disable_plugin();
        }
    }

    public int getWorldCountForPlayer(String uuid) {
        try {
            ResultSet res = this.query(String.format("SELECT count(*) FROM %s WHERE player_uuid = '%s';", WS_WORLDS, uuid));
            res.next();
            int count = res.getInt("count(*)");
            return count;
        } catch (SQLException e) {
            WorldSystem.logger().log(Level.SEVERE, "[WorldSystem] File to Insert to world for player name but failed Disabling");
            WorldSystem.disable_plugin();
            return 0;
        }
    }

    public PlayerWorld[] getWorldsForPlayer(String uuid) {
        try {
            List<PlayerWorld> worlds = new ArrayList<PlayerWorld>();

            ResultSet res = this.query(String.format("SELECT * FROM %s WHERE player_uuid = '%s';", WS_WORLDS, uuid));

            while (res.next()) {
                int world_id = res.getInt("world_id");
                String player_uuid = res.getString("player_uuid");
                String player_name = res.getString("player_name");
                long last_loaded = res.getLong("last_loaded");

                worlds.add(new PlayerWorld(world_id, player_uuid, player_name, last_loaded));
            }
            res.close();
            return worlds.toArray(new PlayerWorld[worlds.size()]);
        } catch (SQLException e) {
            WorldSystem.logger().log(Level.SEVERE, "[WorldSystem] Unable To get all the worlds for player in the SQLite Database");
            WorldSystem.disable_plugin();
            return new PlayerWorld[0];
        }
    }

    public void update_last_loaded(String uuid, long new_time) {
        try {
            this.query(String.format("UPDATE %s SET last_loaded = %s WHERE player_uuid = %s;", WS_WORLDS, new_time, uuid)).close();
        } catch (SQLException e) {
            WorldSystem.logger().log(Level.SEVERE, "[WorldSystem] Tried to update player name but failed skipping");
        }
    }

}

