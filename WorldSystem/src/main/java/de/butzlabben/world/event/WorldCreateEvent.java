package de.butzlabben.world.event;

import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Event if a SystemWorld gets created.
 * Do mix up with the WorldCreateEvent from Bukkit
 * 
 * @author Butzlabben
 * @since 09.05.2018
 */
public class WorldCreateEvent extends WorldEvent {

	private final Player owner;
	private  WorldCreator worldCreator;

	public WorldCreateEvent(Player owner, WorldCreator creator) {
		this.owner = owner;
		this.setWorldCreator(creator);
	}

	/**
	 * @return owner of world that gets created
	 */
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
