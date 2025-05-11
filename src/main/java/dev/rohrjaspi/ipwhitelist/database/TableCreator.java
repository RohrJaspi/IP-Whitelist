package dev.rohrjaspi.ipwhitelist.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableCreator {


	private final Connection connection;

	// Constructor accepting a Connection object
	public TableCreator(Connection connection) {
		this.connection = connection;
	}

	// Method to create all tables
	public void createTables() {
		String[] tableCreationSQLs = {
				"CREATE TABLE IF NOT EXISTS ip_whitelist (" +
						"NAME VARCHAR(32) PRIMARY KEY, " +
						"IP VARCHAR(64)" +
						");"
		};

		for (String sql : tableCreationSQLs) {
			createTable(sql);
		}
	}

	// Private method to create a single table
	private void createTable(String createTableSQL) {
		try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
			statement.executeUpdate();
			System.out.println("[Database] Table created successfully.");
		} catch (SQLException e) {
			System.err.println("[Database] Failed to create table: " + e.getMessage());
		}
	}
}
