package dev.rohrjaspi.ipwhitelist.discord;

import dev.rohrjaspi.ipwhitelist.util.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;
import java.time.Instant;

public class LogManager {

	private JDA api;

	public LogManager(BotManager bot) {
		this.api = bot.getApi();
	}





	public void sendLogEmbed(String type, String title, String description, Color color, String... fields) {

		TextChannel logChannel = api.getTextChannelById(Config.getConfigString("discord.channels.log"));

		if (logChannel == null) return;

		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(title);
		embed.setDescription(description);
		embed.setColor(color);
		embed.setTimestamp(Instant.now());
		embed.setFooter("Log Type: " + type);

		// Add fields (expecting pairs of field name and value)
		for (int i = 0; i < fields.length; i += 2) {
			// Make sure we have both a name and value pair
			if (i + 1 < fields.length) {
				String fieldName = fields[i];
				String fieldValue = fields[i + 1];
				embed.addField(fieldName, fieldValue, false);
			}
		}
		logChannel.sendMessageEmbeds(embed.build()).queue();
	}


}
