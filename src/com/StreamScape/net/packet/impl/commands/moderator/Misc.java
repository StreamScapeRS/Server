package com.StreamScape.net.packet.impl.commands.moderator;

import com.StreamScape.model.Locations;
import com.StreamScape.world.World;
import com.StreamScape.world.content.PlayerLogs;
import com.StreamScape.world.content.PlayerPunishments;
import com.StreamScape.world.entity.impl.player.Player;

public class Misc {

	public static void checkCommands(Player player, String wholeCommand, String[] command) {
		if (command[0].equalsIgnoreCase("mute")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (PlayerPunishments.isMuted(target.getUsername())) {
				player.getPacketSender().sendMessage("This player has already been muted.");
				return;
			}
			PlayerPunishments.mute(target.getUsername());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has muted " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been muted by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have muted " + target.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("unmute")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (!PlayerPunishments.isMuted(target.getUsername())) {
				player.getPacketSender().sendMessage("This player has not been muted.");
				return;
			}
			PlayerPunishments.unMute(target.getUsername());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has unmuted " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been unmuted by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have unmuted " + target.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("ipmute")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (PlayerPunishments.isIpMuted(target.getHostAddress())) {
				player.getPacketSender().sendMessage("This player has already been ip-muted.");
				return;
			}
			PlayerPunishments.ipMute(target.getHostAddress());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has ip-muted " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been ip-muted by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have ip-muted " + target.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("unipmute")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (!PlayerPunishments.isIpMuted(target.getHostAddress())) {
				player.getPacketSender().sendMessage("This player has not been ip-muted.");
				return;
			}
			PlayerPunishments.unIpMute(target.getHostAddress());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has un-ip-muted " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been un-ip-muted by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have un-ip-muted " + target.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("macmute")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (PlayerPunishments.isMacMuted(target.getMac())) {
				player.getPacketSender().sendMessage("This player has already been mac-muted.");
				return;
			}
			PlayerPunishments.macMute(target.getMac());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has mac-muted " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been mac-muted by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have mac-muted " + target.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("unmacmute")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (!PlayerPunishments.isMacMuted(target.getMac())) {
				player.getPacketSender().sendMessage("This player has not been mac-muted.");
				return;
			}
			PlayerPunishments.unMacMute(target.getMac());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has un-mac-muted " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been un-mac-muted by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have un-mac-muted " + target.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("kick")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			World.deregister(target);
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has kicked " + target.getUsername() + ".");
			player.getPacketSender().sendMessage("You have kicked " + target.getUsername() + ".");
		}
		if (wholeCommand.equalsIgnoreCase("saveall")) {
			World.savePlayers();
			player.getPacketSender()
					.sendMessage("Game successfully saved for " + World.getPlayers().size() + " players.");
		}
		if (wholeCommand.equalsIgnoreCase("remindvote")) {
			World.sendMessage("@blu@Reminder: Do ::vote every 12 hours for more players and content on the server!");
		}
		if (wholeCommand.equalsIgnoreCase("reminddonate")) {
			World.sendMessage("@mag@Reminder: Donate for more players, content, & to keep StreamScape online!");
		}
		if (wholeCommand.equalsIgnoreCase("bank")) {
			if (player.getLocation() == Locations.Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You can not bank here.");
			} else {
				player.getBank(player.getCurrentBankTab()).open();
			}
		}
	}
}
