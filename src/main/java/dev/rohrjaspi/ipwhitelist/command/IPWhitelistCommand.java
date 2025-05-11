package dev.rohrjaspi.ipwhitelist.command;

import dev.rohrjaspi.ipwhitelist.database.IPsql;
import dev.rohrjaspi.ipwhitelist.util.Config;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IPWhitelistCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player player)) return false;

		if (args.length != 3) {
			sender.sendMessage(Config.getConfigString("message.usage"));
			return false;
		}

		String name = args[1];
		String ipAdress = args[2];

		String action = args[0];

		switch (action.toLowerCase()) {
			case "add":

			if (name != null && ipAdress != null) {
				if (IPsql.getIP(name).join().equals(ipAdress)) {
					player.sendMessage(MiniMessage.miniMessage().deserialize(Config.getConfigString("message.alreadyWhitelist ")));
					return false;
				} else {
					IPsql.addPlayer(name, ipAdress);
					player.sendMessage(MiniMessage.miniMessage().deserialize(Config.getConfigString("message.IPadded")));
				}
			}

				break;
			case "remove":

				if (IPsql.getIP(name).join().equals(ipAdress)) {
					IPsql.deletePlayer(name, ipAdress);
					player.sendMessage(MiniMessage.miniMessage().deserialize(Config.getConfigString("message.removeIP")));
				} else {
					player.sendMessage(MiniMessage.miniMessage().deserialize(Config.getConfigString("message.playerNotFound")));
				}

				break;
		}


		return true;
	}

}
