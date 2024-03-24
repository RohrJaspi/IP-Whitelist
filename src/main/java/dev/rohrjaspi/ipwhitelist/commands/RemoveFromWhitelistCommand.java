package dev.rohrjaspi.ipwhitelist.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RemoveFromWhitelistCommand implements CommandExecutor {

	private final JavaPlugin plugin;

	public RemoveFromWhitelistCommand(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be executed by players.");
			return false;
		}

		if (args.length != 2) {
			sender.sendMessage("Usage: /removewhitelist <playerName> <ipAddress>");
			return false;
		}

		String playerName = args[0];
		String ipAddress = args[1];

		FileConfiguration configuration = plugin.getConfig();
		ConfigurationSection ipSection = configuration.getConfigurationSection(ipAddress);

		if (ipSection == null || !ipSection.contains(playerName)) {
			sender.sendMessage("Player '" + playerName + "' with IP address '" + ipAddress + "' is not in the whitelist.");
			return false;
		}

		ipSection.set(playerName, null); // Remove the player from the IP section
		plugin.saveConfig(); // Save the configuration after modification

		sender.sendMessage("Player '" + playerName + "' with IP address '" + ipAddress + "' removed from the whitelist.");
		return true;
	}
}