package com.StreamScape.net.packet.impl.commands.donators;

import com.StreamScape.model.Position;
import com.StreamScape.world.content.teleportation.TeleportHandler;
import com.StreamScape.world.entity.impl.player.Player;

public class SponsorCommands {

	public static void execute(Player player, String command, String[] parts) {
		if (command.equalsIgnoreCase("szone")) {
			TeleportHandler.teleportPlayer(player, new Position(2713, 9564, 0),
					player.getSpellbook().getTeleportType());
		}
	}

}
