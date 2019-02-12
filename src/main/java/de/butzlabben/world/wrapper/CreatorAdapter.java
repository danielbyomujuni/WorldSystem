package de.butzlabben.world.wrapper;

import org.bukkit.WorldCreator;

/**
 * @author Butzlabben
 * @since 07.06.2018
 */
public interface CreatorAdapter {

	void create(WorldCreator creator, SystemWorld world, Runnable sendPlayerMessageCallback);
}
