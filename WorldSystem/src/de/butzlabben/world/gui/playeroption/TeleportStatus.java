package de.butzlabben.world.gui.playeroption;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.butzlabben.inventory.DependListener;
import de.butzlabben.inventory.OrcItem;
import de.butzlabben.world.gui.PlayerOptionsGUI;
import de.butzlabben.world.wrapper.WorldPlayer;

public class TeleportStatus  implements DependListener {
	
	@SuppressWarnings("deprecation")
	@Override
	public ItemStack getItemStack(Player p, WorldPlayer wp) {
		wp = new WorldPlayer(Bukkit.getOfflinePlayer(PlayerOptionsGUI.data.get(p.getUniqueId())), p.getWorld().getName());
		return wp.canTeleport() ? OrcItem.enabled.getItemStack(p, wp) : null;
	}
}