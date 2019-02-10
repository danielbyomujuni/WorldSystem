package de.butzlabben.inventory.pages;

import de.butzlabben.inventory.OrcItem;

/**
 * @author Butzlabben
 * @since 21.05.2018
 */
public interface ItemConverter<T> {

	public OrcItem convert(T element);
	
}
