package de.butzlabben.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class WorldCreateEvent extends WorldEvent {
	
	private final Player owner;
	
	public WorldCreateEvent(Player owner) {
		this.owner = owner;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public final static HandlerList handlers = new HandlerList();
	
	public final static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public final HandlerList getHandlers() {
		return handlers;
	}
}
