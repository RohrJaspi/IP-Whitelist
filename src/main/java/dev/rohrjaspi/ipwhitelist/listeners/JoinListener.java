package dev.rohrjaspi.ipwhitelist.listeners;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JoinListener implements Listener {

	private Map<UUID, Integer> kickCount = new HashMap<>();
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
		/*if (player.isOp()) {
			return; // Allow OPs to connect regardless of whitelist status
		}*/

		FileConfiguration configuration = plugin.getConfig();
		ConfigurationSection ipSection = configuration.getConfigurationSection(ipAddress);

		if (ipSection == null || !ipSection.contains(playerName)) {
			player.kickPlayer("§4--------------------------\n" + "§fYou got Kicked!\n" +
					"§lReason: §rYou are not whitelisted on this Server\n" +
					"§4--------------------------");
		}
	}

	public void onKick(PlayerKickEvent event) {

		UUID uuid = event.getPlayer().getUniqueId();
		Player player = event.getPlayer();


		if (event.getReason() == "§4--------------------------\n" + "§fYou got Kicked!\n" +
				"§lReason: §rYou are not whitelisted on this Server\n" +
				"§4--------------------------") {
			kickCount.put(uuid, kickCount.getOrDefault(uuid, 0) + 1);
		}

		if (kickCount.getOrDefault(uuid, 0) >= 3) {

			Bukkit.getServer().getBanList(BanList.Type.IP).addBan(String.valueOf(player.getAddress()),"§4--------------------------\n" + "§fYou got Baned!\n" +
					"§lReason: §rYou are not welcomed on this Server.\n" +
					"§4--------------------------", null , null);
			kickCount.remove(uuid);
		}

	}

}