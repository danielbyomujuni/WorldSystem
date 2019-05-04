package de.butzlabben.world.gui.clicklistener;

import de.butzlabben.inventory.OrcClickListener;
import de.butzlabben.inventory.OrcInventory;
import de.butzlabben.inventory.OrcItem;
import org.bukkit.entity.Player;

public class ComingSoonClickListener implements OrcClickListener {

	@Override
	public void onClick(Player p, OrcInventory inv, OrcItem item) {
		p.closeInventory();
		p.sendMessage("§cComing soon...");
	}

}
