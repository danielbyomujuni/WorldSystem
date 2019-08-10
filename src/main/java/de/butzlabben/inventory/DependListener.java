package de.butzlabben.inventory;

import de.butzlabben.world.wrapper.WorldPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface DependListener {

    ItemStack getItemStack(Player p, WorldPlayer wp);

}
