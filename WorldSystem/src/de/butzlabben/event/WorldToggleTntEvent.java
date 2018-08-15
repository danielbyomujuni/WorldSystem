package de.butzlabben.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import de.butzlabben.world.wrapper.SystemWorld;

/**
 * @author Butzlabben
 * @since 09.05.2018
 */
public class WorldToggleTntEvent extends WorldEvent {
	
	private final SystemWorld world;
	private final CommandSender executor;
	private boolean value;
	
	public WorldToggleTntEvent(CommandSender executor, SystemWorld world, boolean value) {
		this.executor = executor;
		this.world = world;
		this.value = value;
	}
	
	/**
	 * @return if tnt now gets enabled or disabled
	 */
	public boolean getValue() {
		return value;
	}
	
	/**
	 * @param val if tnt should be enabled or disabled
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
	 * @return get executor who toggles tnt
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
