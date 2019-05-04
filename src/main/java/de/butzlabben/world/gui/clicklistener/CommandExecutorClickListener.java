package de.butzlabben.world.gui.clicklistener;

import de.butzlabben.inventory.OrcClickListener;
import de.butzlabben.inventory.OrcInventory;
import de.butzlabben.inventory.OrcItem;
import org.bukkit.entity.Player;

public class CommandExecutorClickListener implements OrcClickListener {
	
	private final String message;
	
	public CommandExecutorClickListener(String message) {
		this.message = message;
	}

	@Override
	public void onClick(Player p, OrcInventory inv, OrcItem item) {
		p.closeInventory();
        p.chat(message);
		// Fix for #9
		inv.redraw(p);
	}
}
