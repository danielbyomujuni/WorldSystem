package de.butzlabben.event;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * @author Butzlabben
 * @since 09.05.2018
 */
public class WorldRemovememberEvent extends WorldEvent {

	private final String worldname;
	private final UUID uuid;
	private Player remover;
	
	public WorldRemovememberEvent(UUID uuid, String worldname, Player remover) {
		this.uuid = uuid;
		this.worldname = worldname;
		this.remover = remover;
	}
	
	public Player getRemoving() {
		return remover;
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
