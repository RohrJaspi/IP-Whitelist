package dev.rohrjaspi.ipwhitelist.discord.requestwhitelist;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class WhitelistAccept {

	public static void whitelistEmbed(MessageChannel channel, String username, String ip, String from, String discordName) {

		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.GREEN);
		embed.setTitle("Whitelist Request");
		embed.addField("Discord", discordName, false);
		embed.addField("Name", username, false);
		embed.addField("IP", ip, false);
		embed.addField("From", from, false);
		embed.build();

		if (channel != null) {
			channel.sendMessageEmbeds(embed.build())
					.setActionRow(
							Button.success("accept", "Accept"),
							Button.danger("deny", "Deny")
					).queue();
		}

	}

}
