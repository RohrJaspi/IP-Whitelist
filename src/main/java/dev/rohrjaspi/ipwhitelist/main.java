package dev.rohrjaspi.ipwhitelist;

import dev.rohrjaspi.ipwhitelist.commands.RemoveFromWhitelistCommand;
import dev.rohrjaspi.ipwhitelist.commands.WhitelistCommand;
import dev.rohrjaspi.ipwhitelist.listeners.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        getCommand("ipwhitelist").setExecutor(new WhitelistCommand(this));
        getCommand("removeip").setExecutor(new RemoveFromWhitelistCommand(this));

        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);

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
    }
}
