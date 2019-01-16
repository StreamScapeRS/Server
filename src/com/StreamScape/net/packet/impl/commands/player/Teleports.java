package com.StreamScape.net.packet.impl.commands.player;

import com.StreamScape.GameSettings;
import com.StreamScape.model.Position;
import com.StreamScape.world.World;
import com.StreamScape.world.content.teleportation.TeleportHandler;
import com.StreamScape.world.entity.impl.player.Player;

public class Teleports {

	public static void checkTeleports(Player player, String wholeCommand, String[] command) {
		if (wholeCommand.toLowerCase().startsWith("raid")) {
			if (World.minigameHandler.getMinigame(0).isActive())
			{
				player.sendMessage("raids have already started");
				return;
			}
			TeleportHandler.teleportPlayer(player, new Position(3444, 2853, 1),
					player.getSpellbook().getTeleportType());
		}
		if (wholeCommand.equalsIgnoreCase("home")) {
			TeleportHandler.teleportPlayer(player, new Position(3095, 2990, 0),
					player.getSpellbook().getTeleportType());
		}
		if(wholeCommand.equalsIgnoreCase("event")){
			if(player.getInventory().contains(17005)) {
				TeleportHandler.teleportPlayer(player, new Position(3489, 3288, 0),
						player.getSpellbook().getTeleportType());
				player.getPacketSender().sendMessage("You step through the portal..");
			} else {
				player.getPacketSender().sendMessage("@red@You need the Mytical key to open the portal!");
				player.getPacketSender().sendMessage("@blu@Could it be in the Presents around here?");

			}
		}
	}
}