package de.butzlabben.world.gui;

import org.bukkit.entity.Player;

import de.butzlabben.inventory.OrcClickListener;
import de.butzlabben.inventory.OrcInventory;
import de.butzlabben.inventory.OrcItem;
import de.butzlabben.world.config.GuiConfig;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.gui.clicklistener.InventoryOpenClickListener;

public class WorldSystemGUI extends OrcInventory {

	private final static String path = "worldsystem.";

	public WorldSystemGUI() {
		super("WorldSystem", GuiConfig.getRows("worldsystem"), true);
		new WorldOptionsGUI();
		new PlayerOptionsGUI();

		loadItem("playeroptions", (p, inv, item) -> {
			p.closeInventory();
			PlayersPageGUI ppg = PlayersGUIManager.getFirstPage(p);
			if (ppg != null)
				p.openInventory(ppg.getInventory(p));
			else
				p.sendMessage(MessageConfig.getNoMemberAdded());
		});
		loadItem("worldoptions", new InventoryOpenClickListener(WorldOptionsGUI.instance));
	}

	public void loadItem(String subpath, OrcClickListener listener) {
		if (GuiConfig.isEnabled(path + subpath) == false)
			return;
		OrcItem item = GuiConfig.getItem(path + subpath);
		if (item != null) {
			item.setOnClick(listener);
			addItem(GuiConfig.getSlot(path + subpath), item);
		}
	}

	public void loadItem(String subpath) {
		loadItem(subpath, null);
	}

	@Override
	public boolean canOpen(Player p) {
		return true;
	}

}
