package dev.rohrjaspi.ipwhitelist.listeners;

import dev.rohrjaspi.ipwhitelist.database.IPsql;
import dev.rohrjaspi.ipwhitelist.discord.LogManager;
import dev.rohrjaspi.ipwhitelist.util.Config;
import dev.rohrjaspi.ipwhitelist.util.JoinDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.concurrent.CompletableFuture;

public class JoinListener implements Listener {

	// Reference to the map to store join data for each IP address
	private final JoinDataManager joinDataManager;
	private LogManager logManager;

	// Constructor to initialize the LogManager and joinDataManager
	public JoinListener(LogManager logManager, JoinDataManager joinDataManager) {
		this.logManager = logManager;
		this.joinDataManager = joinDataManager;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String ipAddress = player.getAddress().getAddress().getHostAddress(); // Get the player's IP address
		String name = player.getName(); // Get the player's name

		// Get the join limit time from config or use default (5 minutes)
		long joinLimitTime = Config.getConfigInt("settings.time_frame");

		JoinDataManager.JoinData joinData = joinDataManager.getOrCreateJoinData(ipAddress);
		joinData.updateJoin();

		// Check if the player should be banned based on join count within the time limit
		if (joinData.getJoinCount() >= 3 && joinData.isWithinLimit(joinLimitTime)) {
			player.kickPlayer("§4--------------------------\n" +
					Config.getConfigString("message.banned") +
					"§4--------------------------");

			logManager.sendLogEmbed("BAN", "Player Banned", "Player " + name + " has been banned for joining too frequently.",
					java.awt.Color.RED, "Player", name, "IP", ipAddress, "Reason", "Frequent joins within short period");
			return;
		}

		// Check if the player's IP address is different from the one in the whitelist
		CompletableFuture<String> ipFuture = IPsql.getIP(name);
		ipFuture.thenAccept(ip -> {
			if (ip == null || !ip.equals(ipAddress)) {
				// If the player's name is not associated with the IP address in the database
				CompletableFuture<String> nameFuture = IPsql.getNameByIP(ipAddress);
				nameFuture.thenAccept(dbName -> {
					if (dbName == null || !dbName.equals(name)) {
						// Kick the player for not being whitelisted
						player.kickPlayer("§4--------------------------\n" +
								Config.getConfigString("message.notWhitelisted")+
								"§4--------------------------");
					}
				});
			}
		});
	}
}