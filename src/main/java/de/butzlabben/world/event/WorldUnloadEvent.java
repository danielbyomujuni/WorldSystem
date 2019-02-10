package de.butzlabben.world.event;

import org.bukkit.event.HandlerList;

import de.butzlabben.world.wrapper.SystemWorld;

/**
 * @author Butzlabben
 * @since 09.05.2018
 */
public class WorldUnloadEvent extends WorldEvent {

	private final SystemWorld world;
	
	public WorldUnloadEvent(SystemWorld world) {
		this.world = world;
	}
	
	/**
	 * @return world which gets unloaded
	 */
	public SystemWorld getWorld() {
		return world;
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
