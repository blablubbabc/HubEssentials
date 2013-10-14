package de.blablubbabc.hubessentials;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.blablubbabc.hubessentials.listeners.HidePlayersItemListener;
import de.blablubbabc.hubessentials.listeners.PlayerListener;
import de.blablubbabc.hubessentials.listeners.PushingSnowballsListener;
import de.blablubbabc.hubessentials.listeners.WorldListener;

public class HubEssentials extends JavaPlugin {

	public String ADMIN_PERMISSION = "hubessentials.admin";
	public String KEEP_INVENTORY_PERMISSION = "hubessentials.keepinventory";
	public String BYPASS_PUSHING_PERMISSION = "hubessentials.pushing.block";
	
	public Config config;
	
	@Override
	public void onEnable() {
		// configuration
		config = new Config(this);
		
		// listeners
		new WorldListener(this);
		new PlayerListener(this);
		
		// gadgets
		if (config.hideItemEnabled) new HidePlayersItemListener(this);
		if (config.pushingSnowballsEnabled) new PushingSnowballsListener(this); 
		
		if (config.autorRespawnEnabled) {
			getServer().getScheduler().runTaskTimer(this, new Runnable() {
				
				@Override
				public void run() {
					World world = getServer().getWorld(config.mainWorld);
					for (Player player : getServer().getOnlinePlayers()) {
						if (player.getLocation().getY() < config.autoRespawnHeight) {
							// respawn:
							player.closeInventory();
							player.leaveVehicle();
							if (world != null) {
								player.teleport(world.getSpawnLocation());
							} else {
								player.damage(100.0D);
							}
						}
					}
				}
			}, 1L, 20L);
		}
	}
	
}
