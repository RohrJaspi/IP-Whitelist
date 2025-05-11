package dev.rohrjaspi.ipwhitelist.discord.requestwhitelist;

import dev.rohrjaspi.ipwhitelist.util.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ModalListener extends ListenerAdapter {

	private JDA api;

	public ModalListener(JDA api) {
		this.api = api;
	}


	@Override
	public void onModalInteraction(ModalInteractionEvent event) {
		if (event.getModalId().equals("whitelistModal")) {
			event.reply("Whitelist requested").queue();

			String username = event.getValue("username").getAsString();
			String ip = event.getValue("ip").getAsString();
			String from = event.getValue("from").getAsString();

			TextChannel channel = api.getTextChannelById(Config.getConfigString("discord.channels.admin"));

			WhitelistAccept.whitelistEmbed(channel, username, ip, from, event.getUser().getName());

		}
	}
}
