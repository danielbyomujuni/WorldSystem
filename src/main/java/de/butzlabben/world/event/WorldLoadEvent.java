package de.butzlabben.world.event;

import de.butzlabben.world.wrapper.SystemWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Event for loading a world
 *
 * @author Butzlabben
 * @since 09.05.2018
 */
public class WorldLoadEvent extends WorldEvent {

    public final static HandlerList handlers = new HandlerList();
    private final Player owner;
    private final SystemWorld world;

    public WorldLoadEvent(Player owner, SystemWorld systemWorld) {
        this.owner = owner;
        this.world = systemWorld;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return get the world which will be loaded
     */
    public SystemWorld getWorld() {
        return world;
    }

    /**
     * @return get person which intiziated the loading
     */
    public Player getOwner() {
        return owner;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

}
