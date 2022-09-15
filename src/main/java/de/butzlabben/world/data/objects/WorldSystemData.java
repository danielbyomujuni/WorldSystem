package de.butzlabben.world.data.objects;


import de.butzlabben.WorldSystem;
import de.butzlabben.world.data.WorldDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldSystemData {

    public Map<String, PlayerData> players;
    public WorldSystemData() {
        players = new HashMap<String, PlayerData>();
    }

    public Boolean addplayer(String uuid) {
        if (players.get(uuid) == null) {
            players.put(uuid, new PlayerData());
            return true;
        }
        return false;
    }

    public void addWorldToPlayer(String uuid, PlayerWorld world) {
        if (players.get(uuid) != null) {
            players.get(uuid).addWorld(world);
        }
    }

    public int getPlayers() {
        return players.size();
    }



}

