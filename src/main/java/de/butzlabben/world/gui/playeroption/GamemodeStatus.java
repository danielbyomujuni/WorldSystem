package de.butzlabben.world.gui.playeroption;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.butzlabben.inventory.DependListener;
import de.butzlabben.inventory.OrcItem;
import de.butzlabben.world.wrapper.WorldPlayer;

public class GamemodeStatus implements DependListener {
	
	private final WorldPlayer wp;

	public GamemodeStatus(WorldPlayer wp) {
		this.wp = wp;
	}

	@Override
	public ItemStack getItemStack(Player p, WorldPlayer player) {
		return wp.canChangeGamemode() ? OrcItem.enabled.getItemStack(p, wp) : OrcItem.disabled.getItemStack(p, wp);
	}
}