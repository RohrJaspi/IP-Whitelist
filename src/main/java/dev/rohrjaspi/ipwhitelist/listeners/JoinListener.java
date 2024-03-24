package dev.rohrjaspi.ipwhitelist.listeners;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinListener implements Listener {

	private final JavaPlugin plugin;

	public JoinListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		String prefix = "[IP-Adresse]" + "[" + player.getName() + "]";

		System.out.println(prefix + player.getAddress());

		String playerName = player.getName();
		String ipAddress = player.getAddress().getAddress().getHostAddress(); // Get IP address
		String key = ipAddress + playerName;

		// Check if the player is OP
		if (player.isOp()) {
			return; // Allow OPs to connect regardless of whitelist status
		}

		FileConfiguration configuration = plugin.getConfig();
		ConfigurationSection ipSection = configuration.getConfigurationSection(ipAddress);

		if (ipSection == null || !ipSection.contains(playerName)) {
			player.kickPlayer("You are not whitelisted to join this server.");
		}
	}
}