package de.butzlabben.world.data.objects;

public class PlayerWorld {
    public int worldNumber;
    public long lastLoaded;

    public PlayerWorld(String OWNER, String OWNERname, int worldNumber) {
        this.worldNumber = worldNumber;
        this.lastLoaded = 0;
    }
}
