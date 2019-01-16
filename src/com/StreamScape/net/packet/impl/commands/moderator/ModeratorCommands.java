package com.StreamScape.net.packet.impl.commands.moderator;

import com.StreamScape.world.entity.impl.player.Player;

public class ModeratorCommands {

	public static void execute(Player player, String command, String[] parts) {
		Teleports.checkTeleports(player, command, parts);
		Misc.checkCommands(player, command, parts);
	}

}
