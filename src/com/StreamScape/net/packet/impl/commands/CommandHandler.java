package com.StreamScape.net.packet.impl.commands;

import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.net.packet.impl.commands.administrator.AdministratorCommands;
import com.StreamScape.net.packet.impl.commands.developer.DeveloperCommands;
import com.StreamScape.net.packet.impl.commands.donators.DeluxeCommands;
import com.StreamScape.net.packet.impl.commands.donators.DonatorCommands;
import com.StreamScape.net.packet.impl.commands.donators.SponsorCommands;
import com.StreamScape.net.packet.impl.commands.moderator.ModeratorCommands;
import com.StreamScape.net.packet.impl.commands.owner.OwnerCommands;
import com.StreamScape.net.packet.impl.commands.player.PlayerCommands;
import com.StreamScape.util.Misc;
import com.StreamScape.world.entity.impl.player.Player;

public class CommandHandler implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		String command = Misc.readString(packet.getBuffer());
		String[] parts = command.toLowerCase().split(" ");
		if (command.contains("\r") || command.contains("\n")) {
			return;
		}
		getUserCommands(player, command, parts);
	}

	public void getUserCommands(Player player, String command, String[] parts) {
		switch (player.getDonor()) {
		case DONOR:
			DonatorCommands.execute(player, command, parts);
			break;
		case DELUXE_DONOR:
			DonatorCommands.execute(player, command, parts);
			DeluxeCommands.execute(player, command, parts);
			break;
		case SPONSOR:
			DonatorCommands.execute(player, command, parts);
			DeluxeCommands.execute(player, command, parts);
			SponsorCommands.execute(player, command, parts);
			break;
		default:
			break;
		}
		switch (player.getRights()) {
		case PLAYER:
			PlayerCommands.execute(player, command, parts);
			break;
		case TRIAL_FORUM_MODERATOR:
			PlayerCommands.execute(player, command, parts);
			break;
		case MODERATOR:
		case TRIAL_MODERATOR:
		case GLOBAL_MODERATOR:
			PlayerCommands.execute(player, command, parts);
			ModeratorCommands.execute(player, command, parts);
			break;
		case GLOBAL_ADMINISTRATOR:
		case ADMINISTRATOR:
			PlayerCommands.execute(player, command, parts);
			ModeratorCommands.execute(player, command, parts);
			AdministratorCommands.execute(player, command, parts);
			break;
		case DEVELOPER:
			PlayerCommands.execute(player, command, parts);
			ModeratorCommands.execute(player, command, parts);
			AdministratorCommands.execute(player, command, parts);
			DeveloperCommands.execute(player, command, parts);
			break;
		case OWNER:
			PlayerCommands.execute(player, command, parts);
			ModeratorCommands.execute(player, command, parts);
			AdministratorCommands.execute(player, command, parts);
			DeveloperCommands.execute(player, command, parts);
			OwnerCommands.execute(player, command, parts);
			break;
		default:
			System.err.println("Error with User Command Handler.");
			break;
		}
	}

}
