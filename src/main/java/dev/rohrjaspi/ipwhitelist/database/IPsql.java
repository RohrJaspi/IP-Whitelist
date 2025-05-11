package dev.rohrjaspi.ipwhitelist.database;

import java.util.concurrent.CompletableFuture;

public class IPsql {

	private static MySQL dbConnection;

	public static void init(MySQL connection) {
		dbConnection = connection; // Ensure tables are created when the class is initialized
	}

	// Asynchronously check if the player already exists by the name
	public static CompletableFuture<Boolean> existsByName(String name) {
		String sql = "SELECT 1 FROM ip_whitelist WHERE NAME = ?";
		return dbConnection.asyncQuery(sql, rs -> rs != null && rs.next(), name);
	}

	// Asynchronously check if the player exists by their name
	public static CompletableFuture<Boolean> playerExistsByName(String name) {
		String query = "SELECT 1 FROM ip_whitelist WHERE NAME = ?";
		return dbConnection.asyncQuery(query, rs -> rs != null && rs.next(), name);
	}

	// Asynchronously create the player record if not registered
	public static void createPlayer(String name, String ip) {
		playerExistsByName(name).thenAccept(exists -> {
			if (!exists) {
				String query = "INSERT INTO ip_whitelist (NAME, IP, BALANCE) VALUES (?, ?, 0)";
				dbConnection.asyncUpdate(query, name, ip);
			}
		});
	}

	// Asynchronously delete a player by name and IP
	public static CompletableFuture<String> deletePlayer(String name, String ip) {
		return existsByName(name).thenCompose(exists -> {
			if (exists) {
				return dbConnection.asyncUpdate("DELETE FROM ip_whitelist WHERE NAME = ? AND IP = ?", name, ip)
						.thenApply(rowsAffected -> rowsAffected > 0 ? name : null);
			}
			return CompletableFuture.completedFuture(null);
		});
	}

	// Asynchronously get the player's IP by their name
	public static CompletableFuture<String> getIP(String name) {
		String query = "SELECT IP FROM ip_whitelist WHERE NAME = ?";
		return dbConnection.asyncQuery(query, rs -> {
			if (rs != null && rs.next()) {
				return rs.getString("IP");
			}
			return null;
		}, name);
	}

	// Asynchronously get the player's name by their IP
	public static CompletableFuture<String> getNameByIP(String ip) {
		String query = "SELECT NAME FROM ip_whitelist WHERE IP = ?";
		return dbConnection.asyncQuery(query, rs -> {
			if (rs != null && rs.next()) {
				return rs.getString("NAME");
			}
			return null;
		}, ip);
	}

	// Asynchronously add a new player to the whitelist
	public static CompletableFuture<Void> addPlayer(String name, String ip) {
		return playerExistsByName(name).thenCompose(exists -> {
			if (!exists) {
				String query = "INSERT INTO ip_whitelist (NAME, IP, BALANCE) VALUES (?, ?, 0)";
				return dbConnection.asyncUpdate(query, name, ip).thenApply(rowsAffected -> null);
			}
			return CompletableFuture.completedFuture(null);
		});
	}
}