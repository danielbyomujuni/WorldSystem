package de.butzlabben.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import de.butzlabben.world.wrapper.SystemWorld;

public class WorldToggleFireEvent extends WorldEvent {
	
	private final SystemWorld world;
	private final CommandSender executor;
	private boolean value;
	
	public WorldToggleFireEvent(CommandSender executor, SystemWorld world, boolean value) {
		this.executor = executor;
		this.world = world;
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}
	
	public void setValue(boolean val) {
		value = val;
	}
	
	public SystemWorld getWorld() {
		return world;
	}
	
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
