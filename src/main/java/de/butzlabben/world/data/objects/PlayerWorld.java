package de.butzlabben.world.data.objects;

public class PlayerWorld {
    private int worldNumber;
    private long lastLoaded;

    /**
     * Creates an new Player World Data Structure if the worldNumber given
     * @param worldNumber the index of the world held by the player
     */
    public PlayerWorld(int worldNumber) {
        this.worldNumber = worldNumber;
        this.lastLoaded = -1;
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
}
