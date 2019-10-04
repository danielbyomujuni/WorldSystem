package de.butzlabben.world.event;

import org.bukkit.WorldCreator;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Event if a SystemWorld gets created.
 * Do mix up with the WorldCreateEvent from Bukkit
 *
 * @author Butzlabben
 * @since 09.05.2018
 */
public class WorldCreateEvent extends WorldEvent {

    public final static HandlerList handlers = new HandlerList();
    private final UUID owner;
    private WorldCreator worldCreator;

    public WorldCreateEvent(UUID owner, WorldCreator creator) {
        this.owner = owner;
        this.setWorldCreator(creator);
    }

    public final static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return owner of world that gets created
     */
    public UUID getOwner() {
        return owner;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @return the worldcreator which will be used
     */
    public WorldCreator getWorldCreator() {
        return worldCreator;
    }

    public void setWorldCreator(WorldCreator worldCreator) {
        this.worldCreator = worldCreator;
    }

}
