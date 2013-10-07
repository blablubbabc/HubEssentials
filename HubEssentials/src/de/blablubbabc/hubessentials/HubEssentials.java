package de.blablubbabc.hubessentials;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HubEssentials extends JavaPlugin {

	public String ADMIN_PERMISSION = "hubessentials.admin";
	
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
		
		
		getServer().getScheduler().runTaskTimer(this, new Runnable() {
			
			@Override
			public void run() {
				World world = getServer().getWorld(config.mainWorld);
				for (Player player : getServer().getOnlinePlayers()) {
					if (player.getLocation().getY() < 0) {
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
