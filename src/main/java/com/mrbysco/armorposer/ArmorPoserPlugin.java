package com.mrbysco.armorposer;

import com.mrbysco.armorposer.handler.EventHandlers;
import com.mrbysco.armorposer.handler.RenameHandler;
import com.mrbysco.armorposer.handler.SwapHandler;
import com.mrbysco.armorposer.handler.SyncHandler;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class ArmorPoserPlugin extends JavaPlugin {
	public static Plugin Plugin;

	public static final String USE_PERMISSION = "armorposer.use";
	public static final String RESIZE_PERMISSION = "armorposer.resize";
	public static final String LOCK_PERMISSION = "armorposer.lock";
	public static final String RELOAD_PERMISSION = "armorposer.reload";

	public static boolean enableConfigGui;
	public static boolean requirePermissions;
	public static boolean restrictResizeToOP;
	public static double minArmorStandScale;
	public static double maxArmorStandScale;
	public static List<String> resizeWhitelist = new ArrayList<>();

	public static boolean canUse(Player player) {
		if (!requirePermissions) return true;
		return player.hasPermission(ArmorPoserPlugin.USE_PERMISSION);
	}

	@Override
	public void onEnable() {
		setupConfig();

		final Server server = getServer();
		final Messenger messenger = server.getMessenger();
		messenger.registerIncomingPluginChannel(this, "armorposer:sync_packet", new SyncHandler());
		messenger.registerIncomingPluginChannel(this, "armorposer:swap_packet", new SwapHandler());
		messenger.registerIncomingPluginChannel(this, "armorposer:rename_packet", new RenameHandler());
		messenger.registerOutgoingPluginChannel(this, "armorposer:screen_packet");
		messenger.registerOutgoingPluginChannel(this, "armorposer:locked_packet");

		server.getPluginManager().registerEvents(new EventHandlers(), this);

		Plugin = this;
		registerCommands();
	}

	private void registerCommands() {
		try {
			Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);
			CommandMap commandMap = (CommandMap) commandMapField.get(getServer());

			Command fallbackCommand = new Command("armorposer", "ArmorPoser admin command", "/armorposer reload", List.of()) {
				@Override
				public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
					return ArmorPoserPlugin.this.onCommand(sender, this, commandLabel, args);
				}
			};
			commandMap.register(getName().toLowerCase(), fallbackCommand);
			getLogger().warning("Registered /armorposer via fallback command registration.");
		} catch (Exception exception) {
			getLogger().severe("Failed to register /armorposer command: " + exception.getMessage());
		}
	}

	/**
	 * Setup the config file
	 */
	private void setupConfig() {
		var config = getConfig();
		config.addDefault("enableConfigGui", true);
		config.addDefault("requirePermissions", false);
		config.addDefault("restrictResizeToOP", false);
		config.addDefault("minArmorStandScale", 0.25D);
		config.addDefault("maxArmorStandScale", 4.0D);
		config.addDefault("resizeWhitelist", List.of(""));
		config.options().copyDefaults(true);
		saveConfig();

		loadSettings();
	}

	private void loadSettings() {
		var config = getConfig();
		enableConfigGui = config.getBoolean("enableConfigGui");
		requirePermissions = config.getBoolean("requirePermissions");
		restrictResizeToOP = config.getBoolean("restrictResizeToOP");
		minArmorStandScale = config.getDouble("minArmorStandScale", 0.25D);
		maxArmorStandScale = config.getDouble("maxArmorStandScale", 4.0D);
		if (minArmorStandScale > 0 && maxArmorStandScale > 0 && minArmorStandScale > maxArmorStandScale) {
			getLogger().warning("minArmorStandScale is greater than maxArmorStandScale. Using maxArmorStandScale as the minimum.");
			minArmorStandScale = maxArmorStandScale;
		}
		resizeWhitelist = config.getStringList("resizeWhitelist");
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!command.getName().equalsIgnoreCase("armorposer")) {
			return false;
		}

		if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			if (!(sender instanceof Player player) || player.hasPermission(RELOAD_PERMISSION) || player.isOp()) {
				reloadConfig();
				loadSettings();
				sender.sendMessage("[ArmorPoser] Configuration reloaded.");
			} else {
				sender.sendMessage("[ArmorPoser] You don't have permission to reload this plugin.");
			}
			return true;
		}

		sender.sendMessage("[ArmorPoser] Usage: /armorposer reload");
		return true;
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public static boolean isFolia() {
		try {
			Class.forName("io.papermc.paper.threadedregions.ThreadedRegionizer");
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
