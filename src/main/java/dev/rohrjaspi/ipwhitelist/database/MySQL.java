package dev.rohrjaspi.ipwhitelist.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.rohrjaspi.ipwhitelist.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;
import java.util.concurrent.CompletableFuture;

public class MySQL {

	private HikariDataSource dataSource;

	public void connect() {
		FileConfiguration config = Main.getInstance().getConfig();

		String dataSourceClassName = config.getString("database.dataSourceClassName");
		String url = config.getString("database.url");
		String user = config.getString("database.user");
		String password = config.getString("database.password");

		int maxPoolSize = config.getInt("database.hikari.maximumPoolSize");
		int minIdle = config.getInt("database.hikari.minimumIdle");
		long idleTimeout = config.getLong("database.hikari.idleTimeout");
		long maxLifetime = config.getLong("database.hikari.maxLifetime");
		long connectionTimeout = config.getLong("database.hikari.connectionTimeout");

		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDataSourceClassName(dataSourceClassName);
		hikariConfig.addDataSourceProperty("url", url);
		hikariConfig.addDataSourceProperty("user", user);
		hikariConfig.addDataSourceProperty("password", password);

		hikariConfig.setMaximumPoolSize(maxPoolSize);
		hikariConfig.setMinimumIdle(minIdle);
		hikariConfig.setIdleTimeout(idleTimeout);
		hikariConfig.setMaxLifetime(maxLifetime);
		hikariConfig.setConnectionTimeout(connectionTimeout);

		hikariConfig.addDataSourceProperty("cachePrepStmts", config.getBoolean("database.hikari.cachePrepStmts"));
		hikariConfig.addDataSourceProperty("prepStmtCacheSize", config.getInt("database.hikari.prepStmtCacheSize"));
		hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", config.getInt("database.hikari.prepStmtCacheSqlLimit"));
		hikariConfig.addDataSourceProperty("useServerPrepStmts", config.getBoolean("database.hikari.useServerPrepStmts"));

		dataSource = new HikariDataSource(hikariConfig);
		Bukkit.getServer().getConsoleSender().sendMessage("[MariaDB] HikariCP connection pool initialized successfully.");
	}

	public void disconnect() {
		if (dataSource != null && !dataSource.isClosed()) {
			dataSource.close();
			Bukkit.getServer().getConsoleSender().sendMessage("[MariaDB] HikariCP connection pool closed successfully.");
		}
	}

	public boolean isConnected() {
		return dataSource != null && !dataSource.isClosed();
	}

	// Asynchronous database update (for INSERT, UPDATE, DELETE queries)
	public CompletableFuture<Integer> asyncUpdate(String sql, Object... params) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection connection = dataSource.getConnection();
				 PreparedStatement statement = connection.prepareStatement(sql)) {

				for (int i = 0; i < params.length; i++) {
					statement.setObject(i + 1, params[i]);
				}

				return statement.executeUpdate();  // Return the number of affected rows
			} catch (SQLException e) {
				e.printStackTrace();
				return 0;  // Return 0 if an error occurs
			}
		});
	}

	// Asynchronous database query (for SELECT queries) with CompletableFuture
	public <T> CompletableFuture<T> asyncQuery(String sql, ResultSetHandler<T> handler, Object... params) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection connection = dataSource.getConnection();
				 PreparedStatement statement = connection.prepareStatement(sql)) {

				for (int i = 0; i < params.length; i++) {
					statement.setObject(i + 1, params[i]);
				}

				try (ResultSet resultSet = statement.executeQuery()) {
					return handler.handle(resultSet);  // Pass the ResultSet to a handler
				}

			} catch (SQLException e) {
				e.printStackTrace();
				return null;  // Return null if an error occurs
			}
		});
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	// A functional interface to handle the ResultSet
	public interface ResultSetHandler<T> {
		T handle(ResultSet resultSet) throws SQLException;
	}
}
