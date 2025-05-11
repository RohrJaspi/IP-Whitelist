package dev.rohrjaspi.ipwhitelist.discord.requestwhitelist;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class Embed {



	public static void requestEmbed(MessageChannel channel) {

		EmbedBuilder embed = new EmbedBuilder();

		embed.setColor(Color.PINK);
		embed.setTitle("Whitelist Request");
		embed.setDescription("Here you can request a whitelist for the server");
		embed.build();

		if (channel != null ){
			channel.sendMessageEmbeds(embed.build())
					.setActionRow(
							Button.primary("whitelist", "Request Whitelist")
					).queue();

		}

	}

}
