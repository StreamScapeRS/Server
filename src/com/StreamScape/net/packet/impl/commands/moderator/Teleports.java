package com.StreamScape.net.packet.impl.commands.moderator;

import com.StreamScape.GameSettings;
import com.StreamScape.world.World;
import com.StreamScape.world.content.PlayerLogs;
import com.StreamScape.world.content.PlayerPunishments.Jail;
import com.StreamScape.world.entity.impl.player.Player;

public class Teleports {

	public static void checkTeleports(Player player, String wholeCommand, String[] command) {
		if (command[0].equalsIgnoreCase("movehome")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			target.moveTo(GameSettings.DEFAULT_POSITION.copy());
			target.getSkillManager().stopSkilling();
			target.getPacketSender().sendMessage("You've been teleported home by " + player.getUsername() + ".");
			player.getPacketSender().sendConsoleMessage("Sucessfully moved " + target.getUsername() + " to home.");
		}
		if (command[0].equalsIgnoreCase("teleto")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			player.moveTo(target.getPosition());
			player.getPacketSender().sendMessage("You teleport to " + target.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("jail")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (Jail.isJailed(target)) {
				player.getPacketSender().sendMessage("This player is already in jail.");
				return;
			}
			if (Jail.jailPlayer(target)) {
				target.getSkillManager().stopSkilling();
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " just jailed " + target.getUsername() + "!");
				player.getPacketSender().sendMessage("Jailed player: " + target.getUsername() + "");
				target.getPacketSender().sendMessage("You have been jailed by " + player.getUsername() + ".");
				return;
			}
			player.getPacketSender().sendMessage("Could not jail " + target.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("unjail")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (!Jail.isJailed(target)) {
				player.getPacketSender().sendMessage("This player is not in jail.");
				return;
			}
			Jail.unjail(target);
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " just unjailed " + target.getUsername() + "!");
			player.getPacketSender().sendMessage("Unjailed player: " + target.getUsername() + "");
			target.getPacketSender().sendMessage("You have been unjailed by " + player.getUsername() + ".");
		}
	}

}
