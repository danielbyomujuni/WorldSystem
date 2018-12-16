package de.butzlabben.inventory;

/**
 * @author Butzlabben
 * @since 28.06.2018
 */
public class CostumInv extends OrcInventory {

	public CostumInv(String title, int rows) {
		super(title, rows);
	}
	
	public CostumInv(String title, int rows, boolean fill) {
		super(title, rows, fill);
	}
}
