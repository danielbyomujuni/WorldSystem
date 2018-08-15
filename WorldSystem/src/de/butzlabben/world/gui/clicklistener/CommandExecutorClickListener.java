package de.butzlabben.world.gui.clicklistener;

import org.bukkit.entity.Player;

import de.butzlabben.inventory.OrcClickListener;
import de.butzlabben.inventory.OrcInventory;
import de.butzlabben.inventory.OrcItem;

public class CommandExecutorClickListener implements OrcClickListener {
	
	private final String message;
	
	public CommandExecutorClickListener(String message) {
		this.message = message;
	}

	@Override
	public void onClick(Player p, OrcInventory inv, OrcItem item) {
		p.closeInventory();
		String msg = message;
		p.chat(msg);
		// Fix for #9
		inv.redraw(p);
	}
}
