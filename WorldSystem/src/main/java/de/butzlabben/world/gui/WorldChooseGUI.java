package de.butzlabben.world.gui;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import de.butzlabben.inventory.OrcClickListener;
import de.butzlabben.inventory.OrcInventory;
import de.butzlabben.inventory.OrcItem;
import de.butzlabben.world.config.GuiConfig;
import de.butzlabben.world.wrapper.WorldTemplate;
import de.butzlabben.world.wrapper.WorldTemplateProvider;

/**
 * @author Butzlabben
 * @since 15.12.2018
 */
public class WorldChooseGUI extends OrcInventory {

	private final static String path = "worldchoose.";
	
	public WorldChooseGUI() {
		this(null);
	}

	public WorldChooseGUI(Consumer<WorldTemplate> onClick) {
		super(GuiConfig.getTitle(GuiConfig.getConfig(), "worldchoose"), GuiConfig.getRows("worldchoose"),
				GuiConfig.isFill("worldchoose"));

		for (WorldTemplate template : WorldTemplateProvider.getInstace().getTemplates()) {
			OrcItem icon = template.getIcon();
			if(onClick != null)
				icon.setOnClick((p, inv, item) -> {
					p.closeInventory();
					onClick.accept(template);
				});
			int slot = template.getSlot();
			addItem(slot, icon);
		}

		if (GuiConfig.isEnabled(path + "back")) {
			OrcItem back = OrcItem.back.clone();
			back.setOnClick((p, inv, item) -> {
				p.closeInventory();
			});
			addItem(GuiConfig.getSlot(path + "back"), back);
		}
	}
	
	public static void letChoose(Player player, Consumer<WorldTemplate> template) {
		player.openInventory(new WorldChooseGUI(template).getInventory(player));
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

	public boolean canOpen(Player p) {
		return true;
	}
}