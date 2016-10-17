/**
 * Copyright (c) blablubbabc <http://www.blablubbabc.de>
 * All rights reserved.
 */
package de.blablubbabc.hubessentials.listeners;

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
import org.bukkit.inventory.ItemStack;

import de.blablubbabc.hubessentials.HubEssentials;
import de.blablubbabc.hubessentials.Utils;

public class HidePlayersItemListener extends AbstractListener {

	private Set<String> timeouts = new HashSet<String>();
	private Set<String> toggledInvisibility = new HashSet<String>();

	public HidePlayersItemListener(HubEssentials plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Utils.removeAllSimilairItems(player.getInventory(), plugin.config.hideItemOn, plugin.config.hideItemOff);

		player.getInventory().addItem(plugin.config.hideItemOn.clone());

		for (String playerName : toggledInvisibility) {
			Player otherPlayer = Bukkit.getPlayer(playerName);
			otherPlayer.hidePlayer(player);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Utils.removeAllSimilairItems(player.getInventory(), plugin.config.hideItemOn, plugin.config.hideItemOff);
		toggledInvisibility.remove(player.getName());
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

	private void toggleInvisibility(Player player) {
		String playerName = player.getName();
		if (timeouts.contains(playerName)) {
			player.sendMessage(plugin.config.hideItemTimeoutMessage);
		} else {
			if (toggledInvisibility.contains(playerName)) {
				player.getInventory().setItemInMainHand(plugin.config.hideItemOn.clone());
				player.sendMessage(plugin.config.hideItemMessageOnUseOff);
				for (Player other : Bukkit.getServer().getOnlinePlayers()) {
					player.showPlayer(other);
				}
				toggledInvisibility.remove(playerName);
			} else {
				player.getInventory().setItemInMainHand(plugin.config.hideItemOff.clone());
				player.sendMessage(plugin.config.hideItemMessageOnUseOn);
				for (Player other : Bukkit.getServer().getOnlinePlayers()) {
					player.hidePlayer(other);
				}
				toggledInvisibility.add(playerName);
			}
			setLastUseTime(playerName, System.currentTimeMillis());
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = false)
	public void playerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.PHYSICAL) return;
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		if (item != null) {
			if (item.isSimilar(plugin.config.hideItemOn) || item.isSimilar(plugin.config.hideItemOff)) {
				event.setCancelled(true);
				toggleInvisibility(player);
				player.updateInventory();
			}
		}
	}
}
