package de.butzlabben.world.data.objects;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {

    //TODO Write Tests
    private List<PlayerWorld> playerWorlds;


    public PlayerData() {
        playerWorlds = new ArrayList<PlayerWorld>();
    }

    public void addWorld(PlayerWorld world) {
        playerWorlds.add(world);
    }

    public int getWorldCount() {
        return playerWorlds.size();
    }

    public PlayerWorld getWorldAt(int index) {
        return playerWorlds.get(index);
    }
}