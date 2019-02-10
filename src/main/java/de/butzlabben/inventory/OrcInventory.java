package de.butzlabben.inventory;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public abstract class OrcInventory {

	protected String title;
	private int rows;
	private InventoryType type;
	private boolean fill = false;

	protected HashMap<Integer, OrcItem> items = new HashMap<>();

	public OrcInventory(String title) {
		Objects.requireNonNull(title, "title cannot be null");
		this.title = title;
	}

	public OrcInventory(String title, int rows) {
		this(title);
		if (rows <= 0 || rows > 6)
			throw new IllegalArgumentException("rows cannot be smaller than 1 or bigger than 6");
		this.rows = rows;
	}
	
	public OrcInventory(String title, int rows, boolean fill) {
		this(title, rows);
		this.fill = fill;
		if(this.fill) {
			for (int i = 0; i < rows * 9; i++) {
				items.put(i, OrcItem.fill);
			}
		}
	}

	public OrcInventory(String title, InventoryType type) {
		this(title);
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
	
	public void redraw(Player p) {
		p.closeInventory();
		p.openInventory(getInventory(p));
	}

	public Inventory getInventory(Player p, String title) {
		Inventory inv;
		int size;
		if (type == null) {
			inv = Bukkit.createInventory(null, rows * 9, title);
			size = rows * 9;
		} else {
			inv = Bukkit.createInventory(null, type, title);
			size = type.getDefaultSize();
		}

		for (Entry<Integer, OrcItem> entry : items.entrySet()) {
			if (entry.getKey() >= 0 && entry.getKey() < size) {
				inv.setItem(entry.getKey(), entry.getValue().getItemStack(p));
			} else {
				System.err.println("There is a problem with a configured Item!");
			}
		}

		OrcListener.getInstance().register(p.getUniqueId(), this);
		
		return inv;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}
