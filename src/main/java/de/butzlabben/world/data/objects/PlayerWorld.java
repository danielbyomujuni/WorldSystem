package de.butzlabben.world.data.objects;

public class PlayerWorld {
    private int worldNumber;
    private long lastLoaded;

    public PlayerWorld(int worldNumber) {
        this.worldNumber = worldNumber;
        this.lastLoaded = -1;
    }

    public int getWorldNumber() {
        return worldNumber;
    }
}
