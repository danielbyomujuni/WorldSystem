package de.butzlabben.event;

import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class WorldCreateEvent extends WorldEvent {

	private final Player owner;
	private  WorldCreator worldCreator;

	public WorldCreateEvent(Player owner, WorldCreator creator) {
		this.owner = owner;
		this.setWorldCreator(creator);
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

	/**
	 * @return the worldcreator which will be used
	 */
	public WorldCreator getWorldCreator() {
		return worldCreator;
	}

	public void setWorldCreator(WorldCreator worldCreator) {
		this.worldCreator = worldCreator;
	}

}
