package de.butzlabben.inventory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.butzlabben.world.wrapper.WorldPlayer;

public class OrcItem {

	public static OrcItem enabled, disabled, coming_soon, error = new OrcItem(Material.BARRIER, null,
			"§cERROR: Item is wrong configured!", "§cPath in config: see Displayname");

	private ItemStack is;
	private OrcClickListener listener;
	private DependListener depend;

	public OrcItem(Material mat, String display, String... lore) {
		setItemStack(mat, display, lore);
	}

	public OrcItem(ItemStack is) {
		setItemStack(is);
	}

	public OrcItem(Material mat, String display, List<String> lore) {
		setItemStack(mat, display, lore);
	}

	public OrcItem(int id, byte data, String display, List<String> lore) {
		setItemStack(id, data, display, lore);
	}

	@SuppressWarnings("deprecation")
	public OrcItem setItemStack(int id, byte data, String display, List<String> lore) {
		is = new ItemStack(id, 1, data);
		ItemMeta meta = is.getItemMeta();
		if (meta != null) {			
			meta.setDisplayName(display);
			meta.setLore(lore);
			is.setItemMeta(meta);
		}
		return this;
	}

	public OrcItem(int id, byte data, String display, String... lore) {
		setItemStack(id, data, display, lore);
	}

	public OrcItem setItemStack(int id, byte data, String display, String[] lore) {
		return setItemStack(id, data, display, Arrays.asList(lore));
	}

	public OrcItem setItemStack(Material mat, String display, List<String> lore) {
		is = new ItemStack(mat);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(display);
		meta.setLore(lore);
		is.setItemMeta(meta);
		return this;
	}

	public ItemStack getItemStack(Player p, WorldPlayer wp) {
		if (p != null && depend != null) {
			ItemStack is = depend.getItemStack(p, wp);
			if (is != null)
				return is;
		}
		return is;
	}

	public OrcItem setOnClick(OrcClickListener listener) {
		this.listener = listener;
		return this;
	}

	public OrcItem onClick(Player p, OrcInventory inv) {
		if (listener != null)
			listener.onClick(p, inv, this);
		return this;
	}

	public OrcItem setDisplay(String display) {
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(display);
		is.setItemMeta(meta);
		return this;
	}

	public OrcItem setLore(String... lore) {
		ItemMeta meta = is.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		is.setItemMeta(meta);
		return this;
	}

	public OrcItem removeLore() {
		ItemMeta meta = is.getItemMeta();
		meta.setLore(null);
		is.setItemMeta(meta);
		return this;
	}

	public OrcItem setItemStack(ItemStack is) {
		Objects.requireNonNull(is, "ItemStack cannot be null");
		this.is = is;
		return this;
	}

	public OrcItem setItemStack(Material mat, String display, String... lore) {
		return setItemStack(mat, display, Arrays.asList(lore));
	}

	public OrcItem setDepend(DependListener listener) {
		depend = listener;
		return this;
	}

	public OrcItem clone() {
		return new OrcItem(is);
	}
}
