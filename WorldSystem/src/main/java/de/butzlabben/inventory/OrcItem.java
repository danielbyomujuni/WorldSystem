package de.butzlabben.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.butzlabben.world.wrapper.WorldPlayer;

public class OrcItem {

	public static OrcItem enabled, disabled, coming_soon, back, error = new OrcItem(Material.BARRIER, null,
			"§cERROR: Item is wrong configured!", "§cPath in config: see Displayname");

	private ItemStack is;
	private OrcClickListener listener;
	private DependListener depend;
	private Runnable callback;

	public void setCallback(Runnable r) {
		callback = r;
	}

	public OrcItem(Material mat, String display, String... lore) {
		setItemStack(mat, display, lore);
	}

	public OrcItem(ItemStack is) {
		setItemStack(is);
	}

	public OrcItem(Material mat, String display, List<String> lore) {
		setItemStack(mat, (byte) 0, display, lore);
	}

	public OrcItem(Material mat) {
		this(new ItemStack(mat));
	}

	public OrcItem(Material material, byte data, String display, ArrayList<String> lore) {
		setItemStack(material, data, display, lore);
	}

	@SuppressWarnings("deprecation")
	public OrcItem setItemStack(Material mat, byte data, String display, List<String> lore) {
		is = new ItemStack(mat, 1 , data);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(display);
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(meta);
		return this;
	}

	public ItemStack getItemStack(Player p) {
		if (p != null && depend != null) {
			ItemStack is = depend.getItemStack(p, new WorldPlayer(p));
			if (is != null)
				return is;
		}
		return is;
	}
	
	public ItemStack getItemStack(Player p, WorldPlayer wp) {
		if (p != null && depend != null) {
			ItemStack is = depend.getItemStack(p, wp);
			if (is != null)
				return is;
		}
		return is;
	}

	public ItemStack getItemStack() {
		return is;
	}

	public OrcItem setOnClick(OrcClickListener listener) {
		this.listener = listener;
		return this;
	}

	public OrcItem onClick(Player p, OrcInventory inv) {
		if (listener != null) {
			listener.onClick(p, inv, this);
		}
		if (callback != null)
			callback.run();
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
		ItemMeta meta = is.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(meta);
		return this;
	}

	public OrcItem setItemStack(Material mat, String display, String... lore) {
		return setItemStack(mat, (byte) 0, display, Arrays.asList(lore));
	}

	public OrcItem setDepend(DependListener listener) {
		depend = listener;
		return this;
	}

	public OrcItem clone() {
		return new OrcItem(is);
	}
}
