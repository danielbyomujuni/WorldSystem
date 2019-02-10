package de.butzlabben.world.gui.clicklistener;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.butzlabben.inventory.OrcClickListener;
import de.butzlabben.inventory.OrcInventory;
import de.butzlabben.inventory.OrcItem;
import de.butzlabben.world.config.MessageConfig;

public class InventoryOpenClickListener implements OrcClickListener {

	private final OrcInventory open;

	public InventoryOpenClickListener(OrcInventory inv) {
		open = inv;
	}

	@Override
	public void onClick(Player p, OrcInventory inv, OrcItem item) {
		p.closeInventory();
		if (open == null) {
			return;
		}
		Inventory to = open.getInventory(p);
		if (to != null) {
			p.openInventory(to);
		} else {
			p.closeInventory();
			p.sendMessage(MessageConfig.getNoPermission());
		}
	}

}
