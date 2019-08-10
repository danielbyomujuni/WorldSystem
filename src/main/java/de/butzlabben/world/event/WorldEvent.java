package de.butzlabben.world.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class WorldEvent extends Event implements Cancellable {

    private boolean cancelled;

    public WorldEvent() {
    }

    public WorldEvent(boolean cancel) {
        setCancelled(cancel);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

}
