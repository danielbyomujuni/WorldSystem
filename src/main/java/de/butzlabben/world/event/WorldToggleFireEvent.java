package de.butzlabben.world.event;

import de.butzlabben.world.wrapper.SystemWorld;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

/**
 * @author Butzlabben
 * @since 09.05.2018
 */
public class WorldToggleFireEvent extends WorldEvent {

    public final static HandlerList handlers = new HandlerList();
    private final SystemWorld world;
    private final CommandSender executor;
    private boolean value;

    public WorldToggleFireEvent(CommandSender executor, SystemWorld world, boolean value) {
        this.executor = executor;
        this.world = world;
        this.value = value;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return if fire now gets enabled or disabled
     */
    public boolean getValue() {
        return value;
    }

    /**
     * @param val if fire should be enabled or disabled
     */
    public void setValue(boolean val) {
        value = val;
    }

    /**
     * @return world get world
     */
    public SystemWorld getWorld() {
        return world;
    }

    /**
     * @return get executor who toggles fire
     */
    public CommandSender getExecutor() {
        return executor;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }
}
