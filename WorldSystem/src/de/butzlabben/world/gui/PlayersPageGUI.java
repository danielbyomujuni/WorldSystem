package de.butzlabben.world.gui;

import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import de.butzlabben.inventory.OrcClickListener;
import de.butzlabben.inventory.OrcInventory;
import de.butzlabben.inventory.OrcItem;
import de.butzlabben.world.config.GuiConfig;
import de.butzlabben.world.wrapper.WorldPlayer;

/**
 * @author Butzlabben
 * @since 20.04.2018
 */
public class PlayersPageGUI extends OrcInventory {

	private final static String path = "options.players.";
	private static HashMap<UUID, Pair<Integer, Integer>> pages = new HashMap<>();

	@SuppressWarnings("deprecation")
	public PlayersPageGUI(int page, UUID ex, HashMap<UUID, String> players, int next, int before) {
		super("Players added to this world", GuiConfig.getRows("options.players"), false);
		pages.put(ex, Pair.of(next, before));

		loadItem("nextpage", (p, inv, orcitem) -> {
			p.closeInventory();
			new BukkitRunnable() {
				@Override
				public void run() {
					p.closeInventory();

					inv.unregister();
					int nextPage = pages.get(p.getUniqueId()).getLeft();
					pages.remove(p.getUniqueId());

					p.openInventory(PlayersGUIManager.getPage(p, nextPage).getInventory(p));
				}
			}.run();
		});

		loadItem("pagebefore", (p, inv, orcitem) -> {
			p.closeInventory();
			new BukkitRunnable() {
				@Override
				public void run() {
					p.closeInventory();
					int pageBefore = pages.get(p.getUniqueId()).getRight();
					pages.remove(p.getUniqueId());
					inv.unregister();
					p.openInventory(PlayersGUIManager.getPage(p, pageBefore).getInventory(p));
				}
			}.run();
		});

		String subpath = "currentpage";
		String path = PlayersPageGUI.path + subpath;
		YamlConfiguration cfg = GuiConfig.getConfig();
		OrcItem oi = null;
		try {
			oi = new OrcItem(GuiConfig.getMaterial(cfg, path), GuiConfig.getData(cfg, path),
					GuiConfig.getDisplay(cfg, path).replaceAll("%page", "" + page), GuiConfig.getLore(cfg, path));
		} catch (Exception e) {
		}
		try {
			oi = new OrcItem(GuiConfig.getMaterial(cfg, path),
					GuiConfig.getDisplay(cfg, path).replaceAll("%page", "" + page), GuiConfig.getLore(cfg, path));
		} catch (Exception e) {
		}
		addItem(GuiConfig.getSlot(path), oi);

		// Load players
		int i = 0;
		for (UUID uuid : players.keySet()) {
			String name = players.get(uuid);
			Material skullItem = GuiConfig.getSkullItem();
			ItemStack is = new ItemStack(skullItem, 1, (short) 3);
			SkullMeta sm = (SkullMeta) is.getItemMeta();
			sm.setOwner(name);
			sm.setDisplayName(
					GuiConfig.getDisplay(cfg, PlayersPageGUI.path + "playerhead").replaceAll("%player", name));
			is.setItemMeta(sm);
			OrcItem item = new OrcItem(is);
			item.setOnClick((p, inv, orcitem) -> {
				p.closeInventory();
				PlayerOptionsGUI.data.put(ex, name);
				pages.remove(p.getUniqueId());
				p.openInventory(PlayerOptionsGUI.instance.getInventory(p));
			});
			addItem(i, item);
			i++;

		}
	}

	public void loadItem(String subpath, OrcClickListener depend) {
		if (GuiConfig.isEnabled(path + subpath) == false)
			return;
		OrcItem item = GuiConfig.getItem(path + subpath);
		if (item != null) {

			item.setOnClick(depend);

			addItem(GuiConfig.getSlot(path + subpath), item);
		}
	}

	public void loadItem(String subpath) {
		loadItem(subpath, null);
	}

	@Override
	public boolean canOpen(Player p) {
		return new WorldPlayer(p).isOwnerofWorld();
	}

}
