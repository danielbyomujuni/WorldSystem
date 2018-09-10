package de.butzlabben.world.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import de.butzlabben.world.wrapper.SystemWorld;

/**
 * Event when a world gets reset
 * 
 * @author Butzlabben
 * @since 09.05.2018
 */
public class WorldResetEvent extends WorldEvent {
	
	private final SystemWorld world;
	private final CommandSender executor;
	
	public WorldResetEvent(CommandSender executor, SystemWorld world) {
		this.executor = executor;
		this.world = world;
	}
	
	/**
	 * @return world which gets reset
	 */
	public SystemWorld getWorld() {
		return world;
	}
	
	/**
	 * @return Executor of the command
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
