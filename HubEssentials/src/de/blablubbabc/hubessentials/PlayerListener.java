package de.blablubbabc.hubessentials;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener extends AbstractListener {

	public PlayerListener(HubEssentials plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if (plugin.config.customJoinMessageEnabled) {
			event.setJoinMessage(plugin.config.customJoinMessage.replace("{player}", player.getName()));
		}
		
		if (plugin.config.spawnAtMainSpawn) {
			World world = Bukkit.getServer().getWorld(plugin.config.mainWorld);
			if (world != null) {
				player.teleport(world.getSpawnLocation());
			}
		}
		
		player.setGameMode(GameMode.ADVENTURE);
		
		if (plugin.config.clearInventory) {
			clearInv(player);
		}
		
		if (plugin.config.speed) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, plugin.config.speedlevel));
		}
		
		if (plugin.config.jump) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, plugin.config.jumplevel));
		}
		
		if (plugin.config.hungerDisabled) {
			player.setFoodLevel(20);
		}
		
		if (plugin.config.damageDisabled) {
			player.setHealth(player.getMaxHealth());
		}
		
		if (plugin.config.fakeDay) {
			player.setPlayerTime(6000, false);
		}
	}
	
	private void clearInv(Player player) {
		player.closeInventory();
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		if (plugin.config.customQuitMessageEnabled) {
			event.setQuitMessage(plugin.config.customQuitMessage.replace("{player}", player.getName()));
		}
		
		if (plugin.config.clearInventory) {
			clearInv(player);
		}
	}

	@EventHandler
	public void onPlayerHunger(FoodLevelChangeEvent event) {
		if (plugin.config.hungerDisabled) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.PLAYER && plugin.config.damageDisabled && event.getCause() != DamageCause.CUSTOM) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		if (plugin.config.chatDisabled) {
			if (!event.getMessage().startsWith("!") || !player.hasPermission(plugin.ADMIN_PERMISSION)) {
				event.setCancelled(true);
				if (plugin.config.chatDisabledInform) {
					player.sendMessage(plugin.config.chatDisabledMessage);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerDromItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (plugin.config.itemDroppingDisabled) {
			if (!plugin.config.limitedToCertainWorlds || plugin.config.enabledWorlds.contains(player.getWorld().getName())) {
				if (!player.hasPermission(plugin.ADMIN_PERMISSION)) event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if (plugin.config.itemPickupDisabled) {
			if (!plugin.config.limitedToCertainWorlds || plugin.config.enabledWorlds.contains(player.getWorld().getName())) {
				if (!player.hasPermission(plugin.ADMIN_PERMISSION)) event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (!plugin.config.limitedToCertainWorlds || plugin.config.enabledWorlds.contains(player.getWorld().getName())) {
			if (!player.hasPermission(plugin.ADMIN_PERMISSION)) event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!plugin.config.limitedToCertainWorlds || plugin.config.enabledWorlds.contains(player.getWorld().getName())) {
			if (!player.hasPermission(plugin.ADMIN_PERMISSION)) event.setCancelled(true);
		}
	}
}
