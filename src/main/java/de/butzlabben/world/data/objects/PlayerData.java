package de.butzlabben.world.data.objects;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {

    //TODO Write Tests
    private List<PlayerWorld> playerWorlds;

    /**
     * Inits a new Player Object
     */
    public PlayerData() {
        playerWorlds = new ArrayList<PlayerWorld>();
    }

    /**
     * adds a world to the playerWorlds Array
     * @param world the world data object to be added
     */
    public void addWorld(PlayerWorld world) {
        playerWorlds.add(world);
    }

    /**
     * gets the current count of worlds that the player owns
     * @return
     */
    public int getWorldCount() {
        return playerWorlds.size();
    }

    /**
     * returns the world at a certain index
     * @param index index of the world you want
     * @return the World Data Object
     */
    public PlayerWorld getWorldAt(int index) {
        return playerWorlds.get(index);
    }
}