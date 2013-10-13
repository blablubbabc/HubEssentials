package de.blablubbabc.hubessentials;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Utils {
	
	public static void removeAllSimilairItems(Inventory inv, ItemStack... items) {
		for (ItemStack is : inv.getContents()) {
			if (is == null) continue;
			for (ItemStack item : items) {
				if (is.isSimilar(item)) {
					inv.remove(is);
					break;
				}
			}
		}
	}
	
	public static void fillUpSimilairItems(Inventory inv, ItemStack item, int amount) {
		for (ItemStack is : inv.getContents()) {
			if (is == null) continue;
			if (is.isSimilar(item)) {
				int itemAmount = is.getAmount();
				if (itemAmount <= amount) {
					amount -= is.getAmount();
				} else {
					if (amount > 0) {
						is.setAmount(amount);
						amount = 0;
					} else {
						inv.remove(is);
					}
				}
			}
		}
		if (amount > 0) {
			item = item.clone();
			item.setAmount(amount);
			inv.addItem(item);
		}
	}
}
