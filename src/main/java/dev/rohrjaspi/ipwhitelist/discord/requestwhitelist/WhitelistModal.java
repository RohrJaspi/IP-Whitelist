package dev.rohrjaspi.ipwhitelist.discord.requestwhitelist;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class WhitelistModal {

	public static void requestModal() {

		TextInput  username = TextInput.create("username","Username", TextInputStyle.SHORT)
				.setPlaceholder("Enter your username")
				.setMinLength(3)
				.setMaxLength(16)
				.build();

		TextInput ip = TextInput.create("ip","IP", TextInputStyle.SHORT)
				.setPlaceholder("Enter your IPV4 address")
				.setMinLength(16)
				.setMaxLength(32)
				.build();

		TextInput from = TextInput.create("from", "From", TextInputStyle.PARAGRAPH)
				.setPlaceholder("From where do you know the server?")
				.setMinLength(10)
				.setMaxLength(1000)
				.build();

		Modal modal = Modal.create("whitelistModal", "Whitelist Request")
				.addComponents(ActionRow.of(username), ActionRow.of(ip), ActionRow.of(from))
				.build();

	}
}
