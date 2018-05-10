package de.butzlabben.event;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
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
	
	public Player getAdding() {
		return adder;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public String getWorldname() {
		return worldname;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}


	public final static HandlerList handlers = new HandlerList();
}
