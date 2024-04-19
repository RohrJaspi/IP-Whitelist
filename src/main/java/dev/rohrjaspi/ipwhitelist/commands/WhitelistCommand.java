package dev.rohrjaspi.ipwhitelist.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class WhitelistCommand implements CommandExecutor {

	private final JavaPlugin plugin;

	public WhitelistCommand(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		/*if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be executed by players.");
			return false;
		}*/

		if (args.length != 2) {
			sender.sendMessage("Usage: /whitelist <playerName> <ipAddress>");
			return false;
		}

		String playerName = args[0];
		String ipAddress = args[1];

		FileConfiguration configuration = plugin.getConfig();
		ConfigurationSection ipSection = configuration.getConfigurationSection(ipAddress);

		// If the IP section doesn't exist, create it
		if (ipSection == null) {
			ipSection = configuration.createSection(ipAddress);
		}

		ipSection.set(playerName, true); // Add the player to the IP section
		plugin.saveConfig(); // Save the configuration after modification

		sender.sendMessage("Player '" + playerName + "' with IP address '" + ipAddress + "' added to the whitelist.");
		return true;
	}
}