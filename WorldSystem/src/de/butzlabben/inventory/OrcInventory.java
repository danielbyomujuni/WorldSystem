package de.butzlabben.inventory;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.wrapper.WorldPlayer;

public abstract class OrcInventory implements Listener {

	private String title;
	private int rows;
	private InventoryType type;
	private final boolean isStatic;

	private HashMap<Integer, OrcItem> items = new HashMap<>();

	public OrcInventory(String title, boolean isStatic) {
		this.isStatic = isStatic;
		Objects.requireNonNull(title, "title cannot be null");
		this.title = title;
		Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(WorldSystem.class));
	}

	public OrcInventory(String title, int rows, boolean isStatic) {
		this(title, isStatic);
		if (rows <= 0 || rows > 6)
			throw new IllegalArgumentException("rows cannot be smaller than 1 or bigger than 6");
		this.rows = rows;
	}

	public OrcInventory(String title, InventoryType type, boolean isStatic) {
		this(title, isStatic);
		if (type == null || type == InventoryType.CHEST) {
			this.type = null;
			rows = 3;
		} else {
			this.type = type;
		}
	}

	public void addItem(int slot, OrcItem item) {
		if (item == null) {
			removeItem(slot);
		} else {
			items.put(slot, item);
		}
	}

	public void addItem(int row, int col, OrcItem item) {
		addItem(row * 9 + col, item);
	}

	public void removeItem(int slot) {
		items.remove(slot);
	}

	public void removeItem(int row, int col) {
		removeItem(row * 9 + col);
	}

	public Inventory getInventory(Player p) {
		return getInventory(p, title);
	}

	public Inventory getInventory(Player p, String title) {
		if (canOpen(p) == false)
			return null;
		Inventory inv;
		int size;
		if (type == null) {
			inv = Bukkit.createInventory(null, rows * 9, title);
			size = rows * 9;
		} else {
			inv = Bukkit.createInventory(null, type, title);
			size = type.getDefaultSize();
		}
		WorldPlayer wp = new WorldPlayer(p);

		for (Entry<Integer, OrcItem> entry : items.entrySet()) {
			if (entry.getKey() >= 0 && entry.getKey() < size) {
				inv.setItem(entry.getKey(), entry.getValue().getItemStack(p, wp));
			} else {
				System.err.println("[WorldSystem] There is a problem with a configured Item!");
			}
		}

		return inv;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	@EventHandler
	public void on(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && e.getClickedInventory().getTitle().equals(title)) {
			e.setCancelled(true);
			OrcItem item = items.get(e.getSlot());
			if (item != null)
				item.onClick((Player) e.getWhoClicked(), this);
		}
	}
	
	@EventHandler
	public void on(InventoryCloseEvent e) {
		if (e.getInventory() != null && e.getInventory().getTitle().equals(title) && !isStatic) {
			unregister();
		}
	}
	
	public void unregister() {
		HandlerList.unregisterAll(this);
	}

	public abstract boolean canOpen(Player p);

}
