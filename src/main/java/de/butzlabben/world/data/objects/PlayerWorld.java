package de.butzlabben.world.data.objects;

import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.utils.Location;

import java.util.HashMap;
import java.util.Map;

public class PlayerWorld {
    private int worldNumber;
    private long lastLoaded;

    private Map<String, Location> playerLocations;
    private Location spawn;

    /**
     * Creates an new Player World Data Structure if the worldNumber given
     * WARNING: this constuctor is only for tests
     * @param worldNumber the index of the world held by the player
     */
    public PlayerWorld(int worldNumber) {
        this.worldNumber = worldNumber;
        this.lastLoaded = -1;
        this.playerLocations = new HashMap<String, Location>();
        this.spawn = new Location(0, 60, 0);
    }
    /**
     * Creates an new Player World Data Structure if the worldNumber given
     * @param worldNumber the index of the world held by the player
     * @param config the plugin's config file that some settings are loaded from
     */
    public PlayerWorld(int worldNumber, PluginConfig config) {
        this.worldNumber = worldNumber;
        this.lastLoaded = -1;
        this.playerLocations = new HashMap<String, Location>();
        this.spawn = config.getPlayerWorldSpawnPoint();
    }

    /**
     * gets the world player index
     * (player index = the index of the world in the player's
     * world array)
     * @return the world number
     */
    public int getWorldNumber() {
        return worldNumber;
    }

    /**
     * returns the last known location of the player with a given uuid
     * @param uuid the UUID string of the player you want
     * @return the players last know location in the world
     */
    public Location getLocationOfPlayer(String uuid) {
        return this.playerLocations.get(uuid);
    }

    /**
     * Adds/Updates the Location information of a player
     * @param uuid the UUID string of the player
     * @param loc the Location of the Player
     */
    public void addPlayerLocationData(String uuid, Location loc) {
        playerLocations.put(uuid, loc);
    }


    public Location getWorldSpawn() {
        return this.spawn;
    }

    public void setWorldSpawn(Location newSpawn) {
        this.spawn = newSpawn;
    }
}
