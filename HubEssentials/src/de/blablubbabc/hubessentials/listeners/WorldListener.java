package de.blablubbabc.hubessentials.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import de.blablubbabc.hubessentials.HubEssentials;

public class WorldListener extends AbstractListener {

	public WorldListener(HubEssentials plugin) {
		super(plugin);
	}

	@EventHandler
	public void onRainStart(WeatherChangeEvent event) {
		if (event.toWeatherState()) {
			if (!plugin.config.limitedToCertainWorlds || plugin.config.enabledWorlds.contains(event.getWorld().getName())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onThunderStart(ThunderChangeEvent event) {
		if (event.toThunderState()) {
			if (!plugin.config.limitedToCertainWorlds || plugin.config.enabledWorlds.contains(event.getWorld().getName())) {
				event.setCancelled(true);
			}
		}
	}
}
