package com.StreamScape.net.packet.impl.commands.player;

import com.StreamScape.model.PlayerRanks.DonorRights;
import com.StreamScape.model.PlayerRanks.PlayerRights;
import com.StreamScape.world.World;
import com.StreamScape.world.content.PlayerPunishments;
import com.StreamScape.world.content.PlayersOnline;
import com.StreamScape.world.content.combat.CombatFactory;
import com.StreamScape.world.content.combat.DesolaceFormulas;
import com.StreamScape.world.content.dialogue.Dialogue;
import com.StreamScape.world.content.dialogue.DialogueExpression;
import com.StreamScape.world.content.dialogue.DialogueManager;
import com.StreamScape.world.content.dialogue.DialogueType;
import com.StreamScape.world.entity.impl.player.Player;

//import mysql.impl.Store;

public class Misc {

	public static void checkCommands(Player player, String wholeCommand, String[] command) {
		if (wholeCommand.toLowerCase().startsWith("yell")) {
			if (PlayerPunishments.isMuted(player.getUsername()) || PlayerPunishments.isIpMuted(player.getHostAddress())
					|| PlayerPunishments.isMacMuted(player.getMac())) {
				player.getPacketSender().sendMessage("You are muted and cannot yell.");
				return;
			}
			if (player.getRights() == PlayerRights.PLAYER && player.getDonor() == DonorRights.NONE) {
				player.getPacketSender().sendMessage("You must be a staff member or donator to use yell.");
				return;
			}
			int delay = player.getRights().getYellDelay();
			if (!player.getLastYell().elapsed((delay * 1000))) {
				player.getPacketSender().sendMessage(
						"You must wait at least " + delay + " seconds between every yell-message you send.");
				return;
			}
			String yellMessage = com.StreamScape.util.Misc.ucFirst(wholeCommand.substring(5, wholeCommand.length()));
			String yell = "";
			yell += "_" + player.getRights().ordinal();
			yell += "_" + player.getGameMode().ordinal();
			yell += "_" + player.getDonor().ordinal();
			yell += "_" + player.isModeler();
			yell += "_" + player.isGambler();
			yell += "_" + player.isGfxDesigner();
			yell += "_" + player.isYoutuber();
			yell += "_" + player.getUsername();
			yell += "_" + yellMessage;
			yell += "_" + player.yellColours.set.titleColour;
			yell += "_" + player.yellColours.set.textColour;
			yell += "_" + player.yellColours.set.titleShadowColour;
			yell += "_" + player.yellColours.set.textShadowColour;
			yell += "_" + player.yellColours.set.titleAlpha;
			yell += "_" + player.yellColours.set.textAlpha;
			yell += "_" + player.yellColours.set.titleShadowAlpha;
			yell += "_" + player.yellColours.set.textShadowAlpha;
			yell += "_" + player.yellTitle;
			World.sendMessage(":yell:" + yell);
			player.getLastYell().reset();
			return;
		}

		if (wholeCommand.toLowerCase().startsWith("yell") && player.getRights() == PlayerRights.PLAYER) {
			player.getPacketSender().sendMessage("@red@You must be at least a donor to use this feature.");
		}
		if (command[0].equalsIgnoreCase("attacks")) {
			int attack = DesolaceFormulas.getMeleeAttack(player);
			int range = DesolaceFormulas.getRangedAttack(player);
			int magic = DesolaceFormulas.getMagicAttack(player);
			player.getPacketSender().sendMessage("@bla@Melee attack: @or2@" + attack + "@bla@, ranged attack: @or2@"
					+ range + "@bla@, magic attack: @or2@" + magic);
		}
		if (command[0].equals("save")) {
			player.save();
			player.getPacketSender().sendMessage("Your progress has been saved.");
		}
		if (command[0].equals("empty")) {
			DialogueManager.start(player, new Dialogue() {

				@Override
				public DialogueExpression animation() {
					return null;
				}

				@Override
				public String[] dialogue() {
					return new String[] { "Yes, empty my inventory", "No, don't empty my inventory" };
				}

				@Override
				public void specialAction() {
					player.setDialogueActionId(420);
				}

				@Override
				public DialogueType type() {
					return DialogueType.OPTION;
				}

			});
		}
		if (command[0].equals("emptybank")) {
			player.getPacketSender().sendInterfaceRemoval().sendMessage("You empty your bank.");
			player.getSkillManager().stopSkilling();
			for (int i = 0; i < player.getBanks().length; i++) {
				player.getBank(i).resetItems().refreshItems();
			}
		}
		if (command[0].equals("players")) {
			player.getPacketSender().sendInterfaceRemoval();
			PlayersOnline.showInterface(player);
		}

		if (wholeCommand.equalsIgnoreCase("forums")) {
			player.getPacketSender().sendString(1, "https://www.StreamScape.ca");
		}
		if (wholeCommand.equalsIgnoreCase("discord")) {
			player.getPacketSender().sendString(1, "https://discord.gg/VBwxqg");
		}
		if (wholeCommand.equalsIgnoreCase("donate")) {
			player.getPacketSender().sendString(1, "http://StreamScape.ca");
		}
		if (wholeCommand.equalsIgnoreCase("commands")) {
			for (int i = 8145; i < 8196; i++) {
				player.getPacketSender().sendString(i, "");
			}

			for (int i = 12174; i < 12224; i++) {
				player.getPacketSender().sendString(i, "");
				player.getPacketSender().sendInterface(61994);
				player.getPacketSender().sendString(8136, "Close window");
				player.getPacketSender().sendString(637, "Commands");
				player.getPacketSender().sendString(8145, "");
				player.getPA().setScrollBar(8143, 600);

				player.getPacketSender().sendString(8147, "::players");
				player.getPacketSender().sendString(8148, "::empty");
				player.getPacketSender().sendString(8149, "::emptybank");
				player.getPacketSender().sendString(8150, "::support");
				player.getPacketSender().sendString(8151, "::discord");
				player.getPacketSender().sendString(8152, "::save");
				player.getPacketSender().sendString(8153, "::attacks");
				player.getPacketSender().sendString(8154, "::skull");
				player.getPacketSender().sendString(8155, "::donate");
				player.getPacketSender().sendString(8156, "::claim");
				player.getPacketSender().sendString(8157, "::raid");
			}

		}
		if (wholeCommand.equalsIgnoreCase("claim")) {
			try{
				player.gpay(player, player.getUsername());
			}catch(Exception e){}
		}
		if (wholeCommand.equalsIgnoreCase("skull")) {
			if (player.getSkullTimer() > 0) {
				player.getPacketSender().sendMessage("You are already skulled.");
			} else {
				CombatFactory.skullPlayer(player);
			}
		}
	}
}