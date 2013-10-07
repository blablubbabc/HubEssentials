package de.blablubbabc.hubessentials;

import org.bukkit.event.Listener;

public abstract class AbstractListener implements Listener {
	protected HubEssentials plugin;
	
	public AbstractListener(HubEssentials plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
}
