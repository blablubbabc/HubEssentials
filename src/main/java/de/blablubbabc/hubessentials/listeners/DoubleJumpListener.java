/**
 * Copyright (c) blablubbabc <http://www.blablubbabc.de>
 * All rights reserved.
 */
package de.blablubbabc.hubessentials.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import de.blablubbabc.hubessentials.HubEssentials;

public class DoubleJumpListener extends AbstractListener {

	public DoubleJumpListener(HubEssentials plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		Location to = event.getTo();
		Location from = event.getFrom();
		if (to.getBlockX() != from.getBlockX() || to.getBlockY() != from.getBlockZ() || to.getBlockY() != from.getBlockY()) {
			Player player = event.getPlayer();
			Block block = player.getLocation().getBlock();
			if (block.getType() != Material.AIR || block.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
				player.setAllowFlight(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
		Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.CREATIVE) return;
		event.setCancelled(true);
		player.setAllowFlight(false);
		player.setFlying(false);

		Location location = player.getLocation();
		Vector velocity = location.getDirection().setY(0).normalize();
		// y value:
		velocity.setY(plugin.config.doubleJumpForceY).normalize();
		// final strength:
		velocity.multiply(plugin.config.doubleJumpForce);

		// just in case:
		if (velocity.lengthSquared() > 100) velocity.normalize().multiply(10);

		player.setVelocity(velocity);
		player.playSound(location, Sound.ENTITY_ENDERDRAGON_FLAP, 1.0F, 2.0F);
	}
}
