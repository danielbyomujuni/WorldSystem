package de.butzlabben.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import de.butzlabben.world.wrapper.SystemWorld;

public class WorldLoadEvent extends WorldEvent {
	
	private final Player owner;
	private final SystemWorld world;
	
	public WorldLoadEvent(Player owner, SystemWorld systemWorld) {
		this.owner = owner;
		this.world = systemWorld;
	}
	
	public SystemWorld getWorld() {
		return world;
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
