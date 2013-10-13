package de.blablubbabc.hubessentials.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.blablubbabc.hubessentials.HubEssentials;

public class PushingSnowballsListener extends AbstractListener {

	public PushingSnowballsListener(HubEssentials plugin) {
		super(plugin);
	}

	@EventHandler
	public void onSnowballHit(EntityDamageByEntityEvent event) {
		if (event.getEntityType() != EntityType.PLAYER) return;
		Entity damager = event.getDamager();
		if (damager.getType() == EntityType.SNOWBALL) {
			Player target = (Player) event.getEntity();
			if (!target.hasPermission(plugin.BYPASS_PUSHING_PERMISSION)) {
				target.setVelocity(damager.getVelocity().multiply(3));
			}
		}
	}
}
