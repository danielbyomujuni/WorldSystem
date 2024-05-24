package de.butzlabben.WorldSystem.data;

import de.butzlabben.world.WorldSystem;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


public class WorldSystemData {

    private static final String WS_WORLDS = "ws_worlds";

    private static WorldSystemData con;
    private static SqlLiteDatabase core;

    private WorldSystemData() {
        core = SqlLiteDatabase.connect();
  }

    @SneakyThrows
    public static WorldSystemData connect() {
            if (con == null) {
                con = new WorldSystemData();
            }
            return con;
    }


    public int getHighestID() {
        try {
            ResultSet res = core.query(String.format("SELECT count(*) FROM %s", WS_WORLDS));
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

            ResultSet res = core.query(String.format("SELECT * FROM WS_WORLDS;", WS_WORLDS));

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
            WorldSystem.logger().log(Level.SEVERE, "Unable To get all the worlds in the SQLite Database");
            WorldSystem.logger().log(Level.SEVERE, e.getMessage());
            WorldSystem.disable_plugin();
            return new PlayerWorld[0];
        }
    }

    public void update_name(String uuid, String new_name) {
        try {
            core.void_query(String.format("UPDATE %s SET player_name = '%s' WHERE player_uuid = '%s;'", WS_WORLDS, new_name, uuid));
        } catch (SQLException e) {
            WorldSystem.logger().log(Level.SEVERE, "Tried to update player name but failed skipping");
            WorldSystem.logger().log(Level.SEVERE, e.getMessage());
        }
    }

    public void create_new_world_record(PlayerWorld world) {
        try {
            core.void_query(String.format("INSERT INTO %s (world_id, player_uuid, player_name, last_loaded) VALUES (%d, '%s', '%s', %d);",
                    WS_WORLDS, world.getWorld_id(), world.getPlayer_uuid(), world.getPlayer_name(), world.getLast_loaded()));
        } catch (SQLException e) {
            WorldSystem.logger().log(Level.SEVERE, "File to Insert to world for player name but failed Disabling");
            WorldSystem.logger().log(Level.SEVERE, e.getMessage());
            WorldSystem.disable_plugin();
        }
    }

    public int getWorldCountForPlayer(String uuid) {
        try {
            ResultSet res = core.query(String.format("SELECT count(*) FROM %s WHERE player_uuid = '%s';", WS_WORLDS, uuid));
            res.next();
            int count = res.getInt("count(*)");
            return count;
        } catch (SQLException e) {
            WorldSystem.logger().log(Level.SEVERE, "File to Insert to world for player name but failed Disabling");
            WorldSystem.disable_plugin();
            return 0;
        }
    }

    public PlayerWorld[] getWorldsForPlayer(String uuid) {
        try {
            List<PlayerWorld> worlds = new ArrayList<PlayerWorld>();

            ResultSet res = core.query(String.format("SELECT * FROM %s WHERE player_uuid = '%s';", WS_WORLDS, uuid));

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
            WorldSystem.logger().log(Level.SEVERE, "Unable To get all the worlds for player in the SQLite Database");
            WorldSystem.disable_plugin();
            return new PlayerWorld[0];
        }
    }

    public void update_last_loaded(String uuid, long new_time) {
        try {
            core.void_query(String.format("UPDATE %s SET last_loaded = %d WHERE player_uuid = '%s';", WS_WORLDS, new_time, uuid));
        } catch (SQLException e) {
            WorldSystem.logger().log(Level.SEVERE, "[WorldSystem] Tried to update player name but failed skipping");
        }
    }

    public void close() {
        core.close();
    }

}

