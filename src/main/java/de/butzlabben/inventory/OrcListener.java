package de.butzlabben.inventory;

import de.butzlabben.world.WorldSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Butzlabben
 * @since 10.06.2018
 */
public class OrcListener implements Listener {

	private static OrcListener instance;
	
	private final HashMap<UUID, OrcInventory> invs = new HashMap<>();

	public static synchronized OrcListener getInstance() {
		if (instance == null)
			instance = new OrcListener();
		return instance;
	}

	private OrcListener() {
		Bukkit.getPluginManager().registerEvents(this, WorldSystem.getInstance());
	}
	
	@EventHandler
	public void on(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && invs.containsKey(e.getWhoClicked().getUniqueId())) {
			e.setCancelled(true);
			OrcItem item = invs.get(e.getWhoClicked().getUniqueId()).items.get(e.getSlot());
			if (item != null)
				item.onClick((Player) e.getWhoClicked(), invs.get(e.getWhoClicked().getUniqueId()));
		}
	}
	
	public void register(UUID uuid, OrcInventory inv) {
		invs.put(uuid, inv);
	}
	
	@EventHandler
	public void on(InventoryCloseEvent e) {
		if (e.getInventory() != null) {
			invs.remove(e.getPlayer().getUniqueId());
		}
	}
}
