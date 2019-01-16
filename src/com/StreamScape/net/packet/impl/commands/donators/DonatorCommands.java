package com.StreamScape.net.packet.impl.commands.donators;

import com.StreamScape.model.Locations;
import com.StreamScape.model.Position;
import com.StreamScape.world.content.teleportation.TeleportHandler;
import com.StreamScape.world.entity.impl.player.Player;

public class DonatorCommands {

	public static void execute(Player player, String command, String[] parts) {
		if (command.equalsIgnoreCase("dzone")) {
			TeleportHandler.teleportPlayer(player, new Position(2022, 4755, 0),
					player.getSpellbook().getTeleportType());
		}
		if (command.equalsIgnoreCase("bank")) {
			if (player.getLocation() == Locations.Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You can not bank here.");
			} else {
				player.getBank(player.getCurrentBankTab()).open();
			}
		}
	}

}
