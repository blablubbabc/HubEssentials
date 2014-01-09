package de.blablubbabc.hubessentials;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Config {
	
	// world settings:
	public boolean limitedToCertainWorlds;
	public List<String> enabledWorlds;
	public String mainWorld;
	
	// respawn if below this height:
	public boolean autoRespawnEnabled;
	public int autoRespawnHeight;
	
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
	
	// gamemode
	public boolean changeGamemode;
	public GameMode gamemode;
	
	// clear inventory on join and leave:
	public boolean clearInventory;
	
	// spawn items:
	public List<ItemStack> spawnItems = new ArrayList<ItemStack>();
	
	// extra gadgets:
	
	// pushing snowballs:
	public boolean pushingSnowballsEnabled;
	public double pushingSnowballsForce;
	public double pushingSnowballsForceY;
	
	// double jump:
	public boolean doubleJumpEnabled;
	public double doubleJumpForce;
	public double doubleJumpForceY;
	
	// hide torch:
	public boolean hideItemEnabled;
	
	public long hideItemTimeoutInTicks;
	public String hideItemTimeoutMessage;
	
	public ItemStack hideItemOn;
	public String hideItemMessageOnUseOn;
	
	public ItemStack hideItemOff;
	public String hideItemMessageOnUseOff;
	
	public Config(Plugin plugin) {
		// default config:
		plugin.saveDefaultConfig();
		FileConfiguration config = plugin.getConfig();
		
		// load values:
		
		// world settings:
		this.limitedToCertainWorlds = config.getBoolean("world settings.limit to enabled worlds");
		this.enabledWorlds = config.getStringList("world settings.enabled worlds");
		this.mainWorld = config.getString("world settings.main world");
		
		// respawn if below this height:
		ConfigurationSection respawnIfBelowSection = config.getConfigurationSection("respawn if below");
		this.autoRespawnEnabled = respawnIfBelowSection.getBoolean("enabled");
		this.autoRespawnHeight = respawnIfBelowSection.getInt("height");
		
		// potion effects:
		this.speed = config.getBoolean("effects.speed.enabled");
		this.speedlevel = config.getInt("effects.speed.level") - 1; // internal levels seem to be be starting at 0 ..
		this.jump = config.getBoolean("effects.jump.enabled");
		this.jumplevel = config.getInt("effects.jump.level") - 1;
		
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
		
		// gamemode:
		ConfigurationSection gamemodeSection = config.getConfigurationSection("initial gamemode");
		this.changeGamemode = gamemodeSection.getBoolean("apply");
		this.gamemode = GameMode.getByValue(gamemodeSection.getInt("gamemode"));
		
		// spawn items:
		ConfigurationSection spawnItemsSection = config.getConfigurationSection("spawn items");
		for (String itemMaterial : spawnItemsSection.getKeys(false)) {
			Material material = Material.getMaterial(itemMaterial);
			if (material == null) {
				plugin.getLogger().warning("Couldn't load spawn item of type '" + itemMaterial + "'");
				continue;
			}
			
			ConfigurationSection itemSection = spawnItemsSection.getConfigurationSection(itemMaterial);
			
			int amount = itemSection.getInt("amount", 1);
			String itemName = ChatColor.translateAlternateColorCodes('&', itemSection.getString("name"));
			List<String> itemLore = new ArrayList<String>();
			for (String loreString : itemSection.getStringList("lore")) {
				itemLore.add(ChatColor.translateAlternateColorCodes('&', loreString));
			}
			
			spawnItems.add(setItemMeta(new ItemStack(material, amount), itemName, itemLore));
		}
		
		
		// extra gadgets:
		
		// pushing snowballs:
		ConfigurationSection pushingSnowballsSection = config.getConfigurationSection("pushing snowballs");
		pushingSnowballsEnabled = pushingSnowballsSection.getBoolean("enabled");
		pushingSnowballsForce = pushingSnowballsSection.getDouble("force");
		pushingSnowballsForceY = pushingSnowballsSection.getDouble("force y");
		
		// double jump:
		ConfigurationSection doubleJumpSection = config.getConfigurationSection("double jump");
		doubleJumpEnabled = doubleJumpSection.getBoolean("enabled");
		doubleJumpForce = doubleJumpSection.getDouble("force");
		doubleJumpForceY = doubleJumpSection.getDouble("force y");
		
		// hide torch:
		ConfigurationSection hideItemSection = config.getConfigurationSection("hide players item");
		this.hideItemEnabled = hideItemSection.getBoolean("enabled");
		this.hideItemTimeoutInTicks = hideItemSection.getLong("timeout in ticks");
		this.hideItemTimeoutMessage = ChatColor.translateAlternateColorCodes('&', hideItemSection.getString("timeout message"));
		
		// on_state section:
		ConfigurationSection onSection = hideItemSection.getConfigurationSection("on state");
		
		// item:
		Material itemOn = Material.getMaterial(onSection.getString("item"));
		if (itemOn == null) itemOn = Material.INK_SACK;
		short itemDataOn = (short) onSection.getInt("data", 10);
		String hideItemOnName = ChatColor.translateAlternateColorCodes('&', onSection.getString("item name"));
		List<String> hideItemOnLore = new ArrayList<String>();
		for (String string : onSection.getStringList("lore")) {
			hideItemOnLore.add(ChatColor.translateAlternateColorCodes('&', string));
		}
		this.hideItemOn = setItemMeta(new ItemStack(itemOn, 1, itemDataOn), hideItemOnName, hideItemOnLore);
		
		// message:
		this.hideItemMessageOnUseOn = ChatColor.translateAlternateColorCodes('&', onSection.getString("message on use"));
		
		// off-state section:
		ConfigurationSection offSection = hideItemSection.getConfigurationSection("off state");
		
		// item:
		Material itemOff = Material.getMaterial(offSection.getString("item"));
		if (itemOff == null) itemOff = Material.INK_SACK;
		short itemDataOff = (short) offSection.getInt("data", 8);
		String hideItemOffName = ChatColor.translateAlternateColorCodes('&', offSection.getString("item name"));
		List<String> hideItemOffLore = new ArrayList<String>();
		for (String string : offSection.getStringList("lore")) {
			hideItemOffLore.add(ChatColor.translateAlternateColorCodes('&', string));
		}
		this.hideItemOff = setItemMeta(new ItemStack(itemOff, 1, itemDataOff), hideItemOffName, hideItemOffLore);
		
		// message
		this.hideItemMessageOnUseOff = ChatColor.translateAlternateColorCodes('&', offSection.getString("message on use"));
		
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
