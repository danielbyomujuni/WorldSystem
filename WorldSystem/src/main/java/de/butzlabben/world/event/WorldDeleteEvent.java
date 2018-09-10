package de.butzlabben.world.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import de.butzlabben.world.wrapper.SystemWorld;

/**
 * Event if a systemworld gets deleted
 * 
 * @author Butzlabben
 * @since 09.05.2018
 */
public class WorldDeleteEvent extends WorldEvent {
	
	private final SystemWorld world;
	private final CommandSender executor;
	
	public WorldDeleteEvent(CommandSender executor, SystemWorld world) {
		this.executor = executor;
		this.world = world;
	}
	
	/**
	 * @return get the world which will be deleted
	 */
	public SystemWorld getWorld() {
		return world;
	}
	
	/**
	 * @return get the executor of the command
	 */
	public CommandSender getExecutor() {
		return executor;
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
