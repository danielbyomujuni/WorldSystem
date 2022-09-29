package de.butzlabben.world.data.objects;


//import de.butzlabben.WorldSystem;
//import de.butzlabben.world.data.WorldDatabase;

//import java.io.File;
//import java.util.ArrayList;
import java.util.HashMap;
//import java.util.List;
import java.util.Map;

public class WorldSystemData {

    public Map<String, PlayerData> players;

    /**
     * Creates the WorldSystemData objects
     * and creates a blank map to hold player data
     * with the uuid is the key
     */
    public WorldSystemData() {
        players = new HashMap<String, PlayerData>();
    }

    /**
     * Adds a Player to the data record
     * @param uuid the UUID of the player to be added
     * @return returns whether the player was added successfully or not
     */
    public Boolean addplayer(String uuid) {
        if (players.get(uuid) == null) {
            players.put(uuid, new PlayerData());
            return true;
        }
        return false;
    }

    /**
     * adds a world Object to the player specified
     * @param uuid the UUID of the player
     * @param world the World Object to be added
     */
    public void addWorldToPlayer(String uuid, PlayerWorld world) {
        if (players.get(uuid) != null) {
            players.get(uuid).addWorld(world);
        }
    }

    /**
     * Gets the Amount of playerdata stored by WorldSystem
     * @return player count
     */
    public int getPlayers() {
        return players.size();
    }

    /**
     * returns the player data of the specifed uuid
     * @param uuid the UUID of the player you want
     * @return returns the player data object
     */
    public PlayerData getPlayer(String uuid) {
        return players.get(uuid);
    }



}

