package de.butzlabben.inventory.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import de.butzlabben.inventory.OrcItem;

/**
 * @author Butzlabben
 * @since 21.05.2018
 */
public class PageGUICreator<T> {

	private final int elementsPerPage;
	private List<InventoryPage> invpages;

	public void create(String title, Collection<T> elements, ItemConverter<T> converter) {
		List<OrcItem> items = elements.stream().map(r -> converter.convert(r)).collect(Collectors.toList());
		if (items == null || items.size() == 0)
			return;

		int pages = (int) (Math.ceil((items.size() / (double) elementsPerPage) < 1 ? 1 : Math.ceil((double) items.size() / (double) elementsPerPage)));

		invpages = new ArrayList<>(pages);

		for (int i = 1; i < pages + 1; i++) {
			int start = i == 1 ? 0 : elementsPerPage * (i - 1);
			int end = items.size() < elementsPerPage * i ? items.size() : elementsPerPage * i;
			List<OrcItem> page = items.subList(start, end);

			InventoryPage invpage = new InventoryPage(title, i, pages);
			page.forEach(invpage::addItem);
			invpages.add(invpage);
		}

		for (int i = 0; i < invpages.size(); i++) {

			int beforeIndex = i == 0 ? invpages.size() - 1 : i - 1;
			int nextIndex = i == invpages.size() - 1 ? 0 : i + 1;
			
			invpages.get(i).before = invpages.get(beforeIndex);
			invpages.get(i).next = invpages.get(nextIndex);
		}
	}

	public void show(Player p) {
		p.openInventory(invpages.get(0).getInventory(p));
	}

	public PageGUICreator() {
		this(4 * 9);
	}
	
	public List<InventoryPage> getInvPages() {
		return invpages;
	}

	public PageGUICreator(int elementsPerPage) {
		this.elementsPerPage = elementsPerPage;
	}
}
