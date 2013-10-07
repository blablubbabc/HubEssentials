package de.blablubbabc.hubessentials;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Config {
	
	// world settings:
	public boolean limitedToCertainWorlds;
	public List<String> enabledWorlds;
	public String mainWorld;
	
	// potion effects:
	public boolean speed;
	public int speedlevel;
	public boolean jump;
	public int jumplevel;
	
	// disabled:
	public boolean hungerDisabled;
	public boolean damageDisabled;
	public boolean rainDisabled;
	public boolean itemDroppingDisabled;
	public boolean itemPickupDisabled;
	// chat
	public boolean chatDisabled;
	public boolean chatDisabledInform;
	public String chatDisabledMessage;
	
	// join and quit messages:
	public boolean customJoinMessageEnabled;
	public String customJoinMessage;
	public boolean customQuitMessageEnabled;
	public String customQuitMessage;
	
	// joining:
	public boolean spawnAtMainSpawn;
	public boolean fakeDay;
	
	// clear inventory on join and leave:
	public boolean clearInventory;
	
	// extra gadgets:
	
	// hide torch:
	public boolean hideItemEnabled;
	public long hideItemTimeoutInTicks;
	public ItemStack hideItemOn;
	public ItemStack hideItemOff;
	public String hideItemMessageOnUseOn;
	public String hideItemMessageOnUseOff;
	
	public Config(Plugin plugin) {
		// default config:
		FileConfiguration config = plugin.getConfig();
		config.options().copyDefaults(true);
		plugin.saveDefaultConfig();
		
		// load values:
		
		// world settings:
		this.limitedToCertainWorlds = config.getBoolean("world settings.limit to enabled worlds");
		this.enabledWorlds = config.getStringList("world settings.enabled worlds");
		this.mainWorld = config.getString("world settings.main world");
		
		// potion effects:
		this.speed = config.getBoolean("effects.speed.enabled");
		this.speedlevel = config.getInt("effects.speed.level");
		this.jump = config.getBoolean("effects.jump.enabled");
		this.jumplevel = config.getInt("effects.jump.level");
		
		// disabled:
		this.hungerDisabled = config.getBoolean("disabled.hunger");
		this.damageDisabled = config.getBoolean("disabled.damage");
		this.rainDisabled = config.getBoolean("disabled.raining");
		this.itemDroppingDisabled = config.getBoolean("disabled.item dropping");
		this.itemPickupDisabled = config.getBoolean("disabled.item pickup");
		// chat
		this.chatDisabled = config.getBoolean("disabled.chat.disabled");
		this.chatDisabledInform = config.getBoolean("disabled.chat.inform");
		this.chatDisabledMessage = ChatColor.translateAlternateColorCodes('&', config.getString("disabled.chat.inform message"));
		
		// leave and join messages:
		this.customJoinMessageEnabled = config.getBoolean("messages.join.enabled");
		this.customJoinMessage = ChatColor.translateAlternateColorCodes('&', config.getString("messages.join.message"));
		this.customQuitMessageEnabled = config.getBoolean("messages.quit.enabled");
		this.customQuitMessage = ChatColor.translateAlternateColorCodes('&', config.getString("messages.quit.message"));
		
		// joining:
		this.spawnAtMainSpawn = config.getBoolean("spawn at main world spawn");
		this.fakeDay = config.getBoolean("fake day");
		
		// clear inventory on join and leave:
		this.clearInventory = config.getBoolean("clear inventory");
		
		// extra gadgets:
		
		// hide torch:
		this.hideItemEnabled = config.getBoolean("hide players item.enabled");
		this.hideItemTimeoutInTicks = config.getLong("hide players item.timeout in ticks");
		// on
		Material itemOn = Material.getMaterial(config.getString("hide players item.on.item"));
		if (itemOn == null) itemOn = Material.REDSTONE_TORCH_ON;
		String hideItemOnName = ChatColor.translateAlternateColorCodes('&', config.getString("hide players item.on.item name"));
		List<String> hideItemOnLore = new ArrayList<String>();
		for (String string : config.getStringList("hide players item.on.lore")) {
			hideItemOnLore.add(ChatColor.translateAlternateColorCodes('&', string));
		}
		this.hideItemOn = setItemMeta(new ItemStack(itemOn), hideItemOnName, hideItemOnLore);
		
		// off
		Material itemOff = Material.getMaterial(config.getString("hide players item.off.item"));
		if (itemOff == null) itemOn = Material.REDSTONE_TORCH_OFF;
		String hideItemOffName = ChatColor.translateAlternateColorCodes('&', config.getString("hide players item.off.item name"));
		List<String> hideItemOffLore = new ArrayList<String>();
		for (String string : config.getStringList("hide players item.off.lore")) {
			hideItemOffLore.add(ChatColor.translateAlternateColorCodes('&', string));
		}
		this.hideItemOff = setItemMeta(new ItemStack(itemOff), hideItemOffName, hideItemOffLore);
		
		this.hideItemMessageOnUseOn = ChatColor.translateAlternateColorCodes('&', config.getString("hide players item.on.message on use"));
		this.hideItemMessageOnUseOff = ChatColor.translateAlternateColorCodes('&', config.getString("hide players item.off.message on use"));
		
	}
	
	private ItemStack setItemMeta(ItemStack item, String name, List<String> description) {
		if (item != null) {
			ItemMeta meta = item.getItemMeta();
			if (name != null) {
				meta.setDisplayName(name);
			}
			if (description != null) {
				meta.setLore(description);
			}
			item.setItemMeta(meta);
		}
		return item;
	}
}
