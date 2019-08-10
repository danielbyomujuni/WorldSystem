package de.butzlabben.world.event;

import de.butzlabben.world.wrapper.SystemWorld;
import org.bukkit.event.HandlerList;

/**
 * @author Butzlabben
 * @since 09.05.2018
 */
public class WorldUnloadEvent extends WorldEvent {

    public final static HandlerList handlers = new HandlerList();
    private final SystemWorld world;

    public WorldUnloadEvent(SystemWorld world) {
        this.world = world;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return world which gets unloaded
     */
    public SystemWorld getWorld() {
        return world;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

}
