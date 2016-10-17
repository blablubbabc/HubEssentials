/**
 * Copyright (c) blablubbabc <http://www.blablubbabc.de>
 * All rights reserved.
 */
package de.blablubbabc.hubessentials.listeners;

import org.bukkit.event.Listener;

import de.blablubbabc.hubessentials.HubEssentials;

public abstract class AbstractListener implements Listener {

	protected HubEssentials plugin;

	public AbstractListener(HubEssentials plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
}
