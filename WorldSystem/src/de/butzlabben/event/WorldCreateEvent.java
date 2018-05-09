package de.butzlabben.event;

import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class WorldCreateEvent extends WorldEvent {

	private final Player owner;
	private Environment env;
	private WorldType type;
	private long seed;

	public WorldCreateEvent(Player owner, Environment env, WorldType type, long seed) {
		this.owner = owner;
		this.env = env;
		this.type = type;
		this.setSeed(seed);
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

	public WorldType getType() {
		return type;
	}

	public void setType(WorldType type) {
		this.type = type;
	}

	public Environment getEnv() {
		return env;
	}

	public void setEnvironment(Environment env) {
		this.env = env;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}
}
