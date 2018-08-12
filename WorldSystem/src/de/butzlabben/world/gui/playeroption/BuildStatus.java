package de.butzlabben.world.gui.playeroption;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.butzlabben.inventory.DependListener;
import de.butzlabben.inventory.OrcItem;
import de.butzlabben.world.wrapper.WorldPlayer;

public class BuildStatus implements DependListener {
	
	private final WorldPlayer wp;

	public BuildStatus(WorldPlayer wp) {
		this.wp = wp;
	}

	@Override
	public ItemStack getItemStack(Player p, WorldPlayer player) {
		return wp.canBuild() ? OrcItem.enabled.getItemStack(p, wp) : OrcItem.disabled.getItemStack(p, wp);
	}
}
