package de.butzlabben.world.gui.clicklistener;

import org.bukkit.entity.Player;

import de.butzlabben.inventory.OrcClickListener;
import de.butzlabben.inventory.OrcInventory;
import de.butzlabben.inventory.OrcItem;
import de.butzlabben.world.gui.PlayerOptionsGUI;

public class CommandExecutorClickListener implements OrcClickListener {
	
	private final String message;
	
	public CommandExecutorClickListener(String message) {
		this.message = message;
	}

	@Override
	public void onClick(Player p, OrcInventory inv, OrcItem item) {
		p.closeInventory();
		String msg = message;
		if(PlayerOptionsGUI.data.containsKey(p.getUniqueId()))
			msg = message.replaceAll("%data", PlayerOptionsGUI.data.get(p.getUniqueId()));
		p.chat(msg);
	}
}
