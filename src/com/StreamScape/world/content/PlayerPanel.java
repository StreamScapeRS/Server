package com.StreamScape.world.content;

import com.StreamScape.util.Misc;
import com.StreamScape.world.content.skill.impl.slayer.SlayerTasks;
import com.StreamScape.world.entity.impl.player.Player;

public class PlayerPanel {

	public static void refreshPanel(Player player) {
		QuestJournal.refreshPanel(player);
		int component = 39100;
		/**
		 * General info
		 */
		player.getPacketSender().sendString(component + 59, "@red@ - @whi@ General Information");
		if (WellOfGoodwill.isActive()) {
			player.getPacketSender().sendString(component + 62, "@red@Well of Goodwill: @bla@Active");
		} else {
			player.getPacketSender().sendString(component + 62, "@red@Well of Goodwill: @bla@N/A");
		}
		/**
		 * Account info
		 */
		player.getPacketSender().sendString(component + 63, "");
		player.getPacketSender().sendString(component + 64, "@red@ - @whi@ Account Information");
		player.getPacketSender().sendString(component + 65, "@red@Username: @bla@" + player.getUsername());
		player.getPacketSender().sendString(component + 66, "@red@Rank(s):");
		player.getPacketSender().sendString(component + 68, "@red@Claimed:  @bla@$" + player.getAmountDonated());
		player.getPacketSender().sendString(component + 69,
				"@red@Email:  @bla@"
						+ (player.getEmailAddress() == null || player.getEmailAddress().equals("null") ? "-"
								: player.getEmailAddress()));
		player.getPacketSender().sendString(component + 70,
				"@red@Exp Lock:  @bla@" + (player.experienceLocked() ? "Locked" : "Unlocked") + "");
		player.getPacketSender().sendString(component + 71, "@red@Yell Customizer" + Misc.formatText(""));
		player.getPacketSender().sendString(component + 72, "");
		/**
		 * Points
		 */
		player.getPacketSender().sendString(component + 73, "@red@ - @whi@ Statistics");
		player.getPointsManager().refreshPanel();
		player.getPacketSender().sendString(component + 86, "");
		/**
		 * Slayer
		 */
		player.getPacketSender().sendString(component + 87, "@red@ - @whi@ Slayer");
		player.getPacketSender().sendString(component + 88, "@red@Open Kills Tracker" + Misc.formatText(""));
		player.getPacketSender().sendString(component + 89, "@red@Open Drop Log" + Misc.formatText(""));
		player.getPacketSender().sendString(component + 90, "@red@Master:  @bla@"
				+ Misc.formatText(player.getSlayer().getSlayerMaster().toString().toLowerCase().replaceAll("_", " ")));
		if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK)
			player.getPacketSender().sendString(component + 91,
					"@red@Task:  @bla@"
							+ Misc.formatText(
									player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))
							+ "");
		else
			player.getPacketSender().sendString(component + 91,
					"@red@Task:  @bla@"
							+ Misc.formatText(
									player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))
							+ "s");
		player.getPacketSender().sendString(component + 92,
				"@red@Task Streak:  @bla@" + player.getSlayer().getTaskStreak() + "");
		player.getPacketSender().sendString(component + 93,
				"@red@Task Amount:  @bla@" + player.getSlayer().getAmountToSlay() + "");

		if (player.getSlayer().getDuoPartner() != null)
			player.getPacketSender().sendString(component + 94,
					"@red@Duo Partner:  @bla@" + player.getSlayer().getDuoPartner() + "");
		else
			player.getPacketSender().sendString(component + 94, "@red@Duo Partner: @bla@N/A");
		player.getPacketSender().sendString(component + 95, "");
		/**
		 * Links
		 */
		player.getPacketSender().sendString(component + 96, "@red@ - @whi@ Links");
		player.getPacketSender().sendString(component + 97, "@red@Home Page" + Misc.formatText(""));
		player.getPacketSender().sendString(component + 98, "@red@Forums Page" + Misc.formatText(""));
		player.getPacketSender().sendString(component + 99, "@red@Hiscores Page" + Misc.formatText(""));
		player.getPacketSender().sendString(component + 100, "@red@Vote Page" + Misc.formatText(""));
		player.getPacketSender().sendString(component + 101, "@red@Store Page" + Misc.formatText(""));
		player.getPacketSender().sendString(component + 102, "@red@Discord Chat" + Misc.formatText(""));
		player.getPacketSender().sendString(component + 103, "@red@Request Support" + Misc.formatText(""));
		player.getPacketSender().sendString(component + 104, "@red@Make a Report" + Misc.formatText(""));
		player.getPacketSender().sendString(component + 105, "@red@In-game Rules" + Misc.formatText(""));
	}

}