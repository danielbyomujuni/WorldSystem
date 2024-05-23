package de.butzlabben.WorldSystem.data;

public class PlayerWorld {

    private int world_id;
    private String player_uuid;
    private String player_name;
    private long last_loaded;

    public PlayerWorld(int world_id, String player_uuid, String player_name, long last_loaded) {
        this.world_id = world_id;
        this.player_uuid = player_uuid;
        this.player_name = player_name;
        this.last_loaded = last_loaded;
    }

    public int getWorld_id() {
        return world_id;
    }

    public String getPlayer_uuid() {
        return player_uuid;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public long getLast_loaded() {
        return last_loaded;
    }
}
