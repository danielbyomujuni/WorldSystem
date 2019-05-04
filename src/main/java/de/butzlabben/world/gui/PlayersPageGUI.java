package de.butzlabben.world.gui;

import de.butzlabben.inventory.OrcItem;
import de.butzlabben.inventory.pages.PageGUICreator;
import de.butzlabben.world.config.GuiConfig;
import de.butzlabben.world.config.MessageConfig;
import de.butzlabben.world.config.WorldConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * @author Butzlabben
 * @since 20.04.2018
 */
public class PlayersPageGUI {

	@SuppressWarnings("deprecation")
	public static void openGUI(Player p) {
		WorldConfig config = WorldConfig.getWorldConfig(p.getWorld().getName());

		HashMap<UUID, String> members = config.getMembersWithNames();

		if (members.size() == 0) {
			p.sendMessage(MessageConfig.getNoMemberAdded());
			return;
		}

		PageGUICreator<Entry<UUID, String>> creator = new PageGUICreator<>(GuiConfig.getRows("options.players"));

		creator.create(GuiConfig.getTitle(GuiConfig.getConfig(), "options.players"), members.entrySet(), (entry) -> {
			String name = entry.getValue();
			OrcItem oi = new OrcItem(GuiConfig.getSkullItem(), GuiConfig
					.getDisplay(GuiConfig.getConfig(), "options.players.playerhead").replaceAll("%player", name));
			SkullMeta sm = (SkullMeta) oi.getItemStack().getItemMeta();
			sm.setOwner(name);
			oi.getItemStack().setItemMeta(sm);
			oi.setOnClick((player, inv, item) -> {
				player.closeInventory();
				PlayerOptionsGUI gui = new PlayerOptionsGUI(player, name, entry.getKey());
				player.openInventory(gui.getInventory(p));
			});
			return oi;
		});

		if (GuiConfig.isEnabled("options.players.back")) {
			OrcItem back = OrcItem.back.clone();
			back.setOnClick((player, inv, i) -> {
				player.closeInventory();
				player.openInventory(new WorldSystemGUI().getInventory(p));
			});
			creator.getInvPages().forEach((oi) -> {oi.addItem(GuiConfig.getSlot("options.players.back"), back);});
		}

		creator.show(p);
	}

	@SuppressWarnings("deprecation")
	public static void preloadPlayers(WorldConfig config) {
		new Thread(() -> {
			int headsPerInv = GuiConfig.getRows("options.players") * 9;
			HashMap<UUID, String> members = config.getMembersWithNames();
			if (members == null || members.size() == 0)
				return;
			int pages = Math.round(members.size() / headsPerInv) < 1 ? 1 : Math.round(members.size() / headsPerInv);
			for (int page = 0; page < pages; page++) {
				int startPos = pages == 1 ? 0 : headsPerInv * (page - 1);
				int length = pages == 1 ? members.size() : headsPerInv;

				ArrayList<UUID> list = new ArrayList<>(members.keySet());

				Inventory inv = Bukkit.createInventory(null, headsPerInv);
				for (int i = startPos; i < startPos + length; i++) {
					String name = members.get(list.get(i));
					ItemStack is = new ItemStack(GuiConfig.getSkullItem(), 1, (short) 3);
					SkullMeta sm = (SkullMeta) is.getItemMeta();
					sm.setOwner(name);
					is.setItemMeta(sm);
					inv.addItem(is);
				}
			}
		}).start();
	}
}
