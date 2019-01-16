package com.StreamScape.net.packet.impl.commands.donators;

import com.StreamScape.model.Locations.Location;
import com.StreamScape.model.Position;
import com.StreamScape.world.content.teleportation.TeleportHandler;
import com.StreamScape.world.entity.impl.player.Player;

public class DeluxeCommands {

	public static void execute(Player player, String command, String[] parts) {
		if (command.equalsIgnoreCase("ddzone")) {
			TeleportHandler.teleportPlayer(player, new Position(2319, 9624, 0),
					player.getSpellbook().getTeleportType());
		}
		if (command.equalsIgnoreCase("bank")) {
			if (player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You can not bank here.");
			} else {
				player.getBank(player.getCurrentBankTab()).open();
			}
		}
	}
}