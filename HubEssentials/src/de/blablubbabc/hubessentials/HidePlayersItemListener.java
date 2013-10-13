package de.blablubbabc.hubessentials;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HidePlayersItemListener extends AbstractListener {

	private Set<String> timeouts = new HashSet<String>();
	
	public HidePlayersItemListener(HubEssentials plugin) {
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		removeInventoryItemsAll(player.getInventory(), plugin.config.hideItemOn);
		removeInventoryItemsAll(player.getInventory(), plugin.config.hideItemOff);
		
		player.getInventory().addItem(plugin.config.hideItemOn.clone());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		removeInventoryItemsAll(player.getInventory(), plugin.config.hideItemOn);
		removeInventoryItemsAll(player.getInventory(), plugin.config.hideItemOff);
	}
	
	private void removeInventoryItemsAll(Inventory inv, ItemStack item) {
		for (ItemStack is : inv.getContents()) {
			if (is != null && is.isSimilar(item)) {
				inv.remove(is);
			}
		}
	}
	
	private void removeLastUseTime(String playerName) {
		timeouts.remove(playerName);
	}
	
	private void setLastUseTime(final String playerName, long time) {
		if (playerName != null) {
			timeouts.add(playerName);
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				
				@Override
				public void run() {
					removeLastUseTime(playerName);
				}
			}, plugin.config.hideItemTimeoutInTicks);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = false)
	public void playerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.PHYSICAL) return;
		
		Player player = event.getPlayer();
		String playerName = player.getName();
		ItemStack item = player.getItemInHand();
		if (item != null) {
			if (item.isSimilar(plugin.config.hideItemOn)) {
				event.setCancelled(true);
				if (timeouts.contains(playerName)) return;
				
				player.setItemInHand(plugin.config.hideItemOff.clone());
				player.sendMessage(plugin.config.hideItemMessageOnUseOn);
				for (Player other : Bukkit.getServer().getOnlinePlayers()) {
					player.hidePlayer(other);
				}
				
				setLastUseTime(playerName, System.currentTimeMillis());
				player.updateInventory();
			} else if (item.isSimilar(plugin.config.hideItemOff)) {
				event.setCancelled(true);
				if (timeouts.contains(playerName)) return;
				
				player.setItemInHand(plugin.config.hideItemOn.clone());
				player.sendMessage(plugin.config.hideItemMessageOnUseOff);	
				for (Player other : Bukkit.getServer().getOnlinePlayers()) {
					player.showPlayer(other);
				}
				
				setLastUseTime(playerName, System.currentTimeMillis());
				player.updateInventory();
			}
		}
	}
}
