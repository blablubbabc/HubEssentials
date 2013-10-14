package de.blablubbabc.hubessentials.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import de.blablubbabc.hubessentials.HubEssentials;

public class PushingSnowballsListener extends AbstractListener {
	
	public PushingSnowballsListener(HubEssentials plugin) {
		super(plugin);
	}

	@EventHandler(ignoreCancelled = false)
	public void onSnowballHit(EntityDamageByEntityEvent event) {
		if (event.getEntityType() != EntityType.PLAYER) return;
		Entity damager = event.getDamager();
		if (damager.getType() == EntityType.SNOWBALL) {
			Player target = (Player) event.getEntity();
			if (!target.hasPermission(plugin.BYPASS_PUSHING_PERMISSION)) {
				Vector velocity = damager.getVelocity();
				double length = velocity.length();
				// y value:
				velocity.setY(plugin.config.pushingSnowballsForceY).normalize();
				// final strength:
				velocity.multiply(length * plugin.config.pushingSnowballsForce);
				
				// just in case:
				if (velocity.lengthSquared() > 10) velocity.normalize().multiply(10);
				
				target.setVelocity(velocity);
			}
		}
	}
}
