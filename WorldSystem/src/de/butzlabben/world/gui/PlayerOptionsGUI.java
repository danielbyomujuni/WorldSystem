package de.butzlabben.world.gui;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.butzlabben.inventory.DependListener;
import de.butzlabben.inventory.OrcInventory;
import de.butzlabben.inventory.OrcItem;
import de.butzlabben.world.config.GuiConfig;
import de.butzlabben.world.gui.clicklistener.ComingSoonClickListener;
import de.butzlabben.world.gui.clicklistener.CommandExecutorClickListener;
import de.butzlabben.world.gui.playeroption.BuildStatus;
import de.butzlabben.world.gui.playeroption.GamemodeStatus;
import de.butzlabben.world.gui.playeroption.TeleportStatus;
import de.butzlabben.world.wrapper.WorldPlayer;

public class PlayerOptionsGUI extends OrcInventory {

	public static PlayerOptionsGUI instance;

	private final static String path = "options.player.";

	public final static HashMap<UUID, String> data = new HashMap<>();

	public PlayerOptionsGUI() {
		super("Player Options", GuiConfig.getRows("options.player"), true);
		loadItem("build", "/ws togglebuild %data", new BuildStatus());
		loadItem("gamemode", "/ws togglegm %data", new GamemodeStatus());
		loadItem("teleport", "/ws toggletp %data", new TeleportStatus());
		loadItem("time");
		loadItem("addmember");
		loadItem("delmember");
		loadItem("worldborder");
		loadItem("setpermissions");
		loadItem("administrateworld");
		if (instance != null)
			instance.unregister();
		instance = this;
	}

	public void loadItem(String subpath, String message, DependListener depend) {
		if (GuiConfig.isEnabled(path + subpath) == false)
			return;
		OrcItem item = GuiConfig.getItem(path + subpath);
		if (item != null) {
			if (message == null) {
				item.setOnClick(new ComingSoonClickListener());
			} else {
				item.setOnClick(new CommandExecutorClickListener(message));
			}
			addItem(GuiConfig.getSlot(path + subpath), item);
			if (depend == null) {
				addItem(GuiConfig.getState(path + subpath), OrcItem.coming_soon.clone());
			} else {
				addItem(GuiConfig.getState(path + subpath), OrcItem.disabled.clone().setDepend(depend));
			}
		}
	}

	public void loadItem(String subpath, String message) {
		loadItem(subpath, message, null);
	}

	public void loadItem(String subpath) {
		loadItem(subpath, null);
	}

	@Override
	public Inventory getInventory(Player p, String title) {
		if (data.containsKey(p.getUniqueId()))
			return super.getInventory(p, title.replaceAll("%data", data.get(p.getUniqueId())));
		return super.getInventory(p, title);
	}

	@Override
	public Inventory getInventory(Player p) {
		if (data.containsKey(p.getUniqueId()))
			return super.getInventory(p, getTitle().replaceAll("%data", data.get(p.getUniqueId())));
		return super.getInventory(p, getTitle());
	}

	@Override
	public boolean canOpen(Player p) {
		return new WorldPlayer(p).isOwnerofWorld();
	}
}
