package de.butzlabben.WorldSystem.worldgen;

public class WorldTemplate {



    public static WorldTemplate default_template() {
        return new WorldTemplate();
    }

    public boolean create() {
        return false;
    }
}
