package de.butzlabben.world.event;

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
	
	/**
	 * @return player who removes somebody
	 */
	public Player getRemoving() {
		return remover;
	}
	
	/**
	 * @return uuid of player who gets removed
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
