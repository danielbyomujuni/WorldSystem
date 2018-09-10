package de.butzlabben.world.event;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Event for adding somebody to a world
 * 
 * @author Butzlabben
 * @since 09.05.2018
 */
public class WorldAddmemberEvent extends WorldEvent {

	private final String worldname;
	private final UUID uuid;
	private Player adder;
	
	public WorldAddmemberEvent(UUID uuid, String worldname, Player adder) {
		this.uuid = uuid;
		this.worldname = worldname;
		this.adder = adder;
	}
	
	/**
	 * @return player who adds somebody
	 */
	public Player getAdding() {
		return adder;
	}
	
	/**
	 * @return UUID of player who gets added
	 */
	public UUID getUUID() {
		return uuid;
	}
	
	/**
	 * @return worldname for which it happens
	 */
	public String getWorldname() {
		return worldname;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public final static HandlerList handlers = new HandlerList();
}
