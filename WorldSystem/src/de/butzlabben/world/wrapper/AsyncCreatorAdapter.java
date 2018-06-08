package de.butzlabben.world.wrapper;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;

import com.boydti.fawe.bukkit.wrapper.AsyncWorld;
import com.boydti.fawe.util.TaskManager;

/**
 * @author Butzlabben
 * @since 08.06.2018
 */
public class AsyncCreatorAdapter implements CreatorAdapter{

	// Create worlds async to close #16
	@Override
	public void create(WorldCreator creator) {
		TaskManager.IMP.async(new Runnable() {
		    @Override
		    public void run() {
		        // Create or load a world async with the provided WorldCreator settings
		        AsyncWorld world = AsyncWorld.create(creator);
		        // AsyncWorld world = AsyncWorld.wrap(bukkitWorld); // Or wrap existing world
		        Block block = world.getBlockAt(0, 0, 0);
		        block.setType(Material.BEDROCK);
		        // When you are done
		        world.commit();

		        Bukkit.getWorlds().add(world);
		    }
		});
		return;
	}

}
