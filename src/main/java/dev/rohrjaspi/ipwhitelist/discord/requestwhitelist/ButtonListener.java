package dev.rohrjaspi.ipwhitelist.discord.requestwhitelist;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ButtonListener extends ListenerAdapter {

	private JDA api;

	public ButtonListener(JDA api) {
		this.api = api;
	}

	@Override
	public void onReady(ReadyEvent event) {
		System.out.println("✅ Bot is ready.");

		String channelId = "123456789012345678"; // Replace with your real channel ID
		TextChannel channel = event.getJDA().getTextChannelById(channelId);

		if (channel == null) {
			System.out.println("❌ Channel not found.");
			return;
		}

		Button btn = Button.primary("whitelist", "Whitelist");

		channel.sendMessage("Click to whitelist:")
				.setActionRow(btn)
				.queue(
						success -> System.out.println("✅ Message sent."),
						error -> {
							System.out.println("❌ Failed to send message:");
							error.printStackTrace();
						}
				);
	}

	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {

		switch (event.getComponentId()) {
			case "whitelist":
				event.reply("Whitelist requested").queue();
				WhitelistModal.requestModal();
				break;
			case "accept":
				event.reply("Whitelist accepted").queue();
				break;
		}
	}
}
