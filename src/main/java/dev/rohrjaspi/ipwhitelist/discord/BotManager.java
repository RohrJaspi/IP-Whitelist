package dev.rohrjaspi.ipwhitelist.discord;

import dev.rohrjaspi.ipwhitelist.discord.requestwhitelist.ButtonListener;
import dev.rohrjaspi.ipwhitelist.discord.requestwhitelist.ModalListener;
import dev.rohrjaspi.ipwhitelist.util.Config;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class BotManager {

	@Getter private JDA api;
	private JDABuilder builder;

	public BotManager() {
		createBot();
	}

	private void createBot() {
		try {
			builder = JDABuilder.createDefault(Config.getConfigString("discord.token"));
			builder.setActivity(Activity.watching("IP Whitelist"));
			builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES);
			api = builder.build();
			api.awaitReady();
			register();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void register() {
		//api.addEventListener(new ServerStatusCommand());
		api.addEventListener(new ButtonListener(api));
		api.addEventListener(new ModalListener(api));
	}

}
