package dev.rohrjaspi.ipwhitelist;

import dev.rohrjaspi.ipwhitelist.command.IPWhitelistCommand;
import dev.rohrjaspi.ipwhitelist.database.IPsql;
import dev.rohrjaspi.ipwhitelist.database.MySQL;
import dev.rohrjaspi.ipwhitelist.database.TableCreator;
import dev.rohrjaspi.ipwhitelist.discord.BotManager;
import dev.rohrjaspi.ipwhitelist.discord.LogManager;
import dev.rohrjaspi.ipwhitelist.listeners.JoinListener;
import dev.rohrjaspi.ipwhitelist.util.JoinDataManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Main extends JavaPlugin {

   @Getter
    private static Main instance;
    private MySQL connection;
    private BotManager botManager;
    private LogManager logManager;
    private JoinDataManager joinDataManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        setupMySQL();
        botManager = new BotManager();
        logManager = new LogManager(botManager);
        joinDataManager = new JoinDataManager();

        // Plugin startup logic

        getCommand("ipwhitelist").setExecutor(new IPWhitelistCommand());

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinListener(logManager, joinDataManager), this);

        System.out.println("-------------------------------------");
        System.out.println("-                                   -");
        System.out.println("-            IP Whitelist           -");
        System.out.println("-            Is now Ready           -");
        System.out.println("-                                   -");
        System.out.println("-------------------------------------");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
        }
    }

    private void setupMySQL() {

        connection = new MySQL();
        if (connection != null) {
            connection.connect();
            try {
                // Create tables
                TableCreator tableCreator = new TableCreator(connection.getConnection());
                tableCreator.createTables();
                IPsql.init(connection);
            } catch (SQLException e) {
                e.printStackTrace();
                getLogger().severe("Failed to initialize the MariaDB connection!");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        } else {
            getLogger().severe("MariaDB connection is null!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
    }
}
