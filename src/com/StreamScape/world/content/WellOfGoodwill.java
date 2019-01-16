package com.StreamScape.world.content;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.content.dialogue.Dialogue;
import com.StreamScape.world.content.dialogue.DialogueExpression;
import com.StreamScape.world.content.dialogue.DialogueManager;
import com.StreamScape.world.content.dialogue.DialogueType;
import com.StreamScape.world.entity.impl.player.Player;

public class WellOfGoodwill {

	public enum WellState {
		EMPTY, FULL;
	}

	private static final int AMOUNT_NEEDED = 100000000; // 100m
	private static final int LEAST_DONATE_AMOUNT_ACCEPTED = 1000000; // 1m

	private static final int BONUSES_DURATION = 120; // 2 hours in minutes
	private static CopyOnWriteArrayList<Player> DONATORS = new CopyOnWriteArrayList<Player>();
	private static WellState STATE = WellState.EMPTY;
	private static long START_TIMER = 0;

	private static int MONEY_IN_WELL = 0;

	public static boolean bonusLoyaltyPoints(Player player) {
		updateState();
		return STATE == WellState.FULL && DONATORS.contains(player);
	}

	public static boolean checkFull(Player player) {
		if (STATE == WellState.FULL) {
			DialogueManager.start(player, new Dialogue() {

				@Override
				public DialogueExpression animation() {
					return DialogueExpression.NORMAL;
				}

				@Override
				public String[] dialogue() {
					return new String[] { "The well is already full of coins and Saradomin",
							"has granted players with bonus experience for their",
							"generosity! There are currently " + getMinutesRemaining() + " minutes",
							"of bonus experience left." };
				}

				@Override
				public Dialogue nextDialogue() {
					return DialogueManager.getDialogues().get(75);
				}

				@Override
				public int npcId() {
					return 802;
				}

				@Override
				public DialogueType type() {
					return DialogueType.NPC_STATEMENT;
				}

			});
			return true;
		}
		return false;
	}

	public static void donate(Player player, int amount) {
		if (checkFull(player)) {
			return;
		}
		if (amount < LEAST_DONATE_AMOUNT_ACCEPTED) {
			DialogueManager.sendStatement(player, "You must donate at least 1 million coins.");
			return;
		}
		if (amount > getMissingAmount()) {
			amount = getMissingAmount();
		}
		boolean usePouch = player.getMoneyInPouch() >= amount;
		if (!usePouch && player.getInventory().getAmount(995) < amount) {
			DialogueManager.sendStatement(player, "You do not have that much money in your inventory or money pouch.");
			return;
		}
		if (usePouch) {
			player.setMoneyInPouch(player.getMoneyInPouch() - amount);
			player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
		} else {
			player.getInventory().delete(995, amount);
		}
		if (!DONATORS.contains(player)) {
			DONATORS.add(player);
		}
		MONEY_IN_WELL += amount;
		if (amount > 25000000) {
			World.sendMessage("<col=6666FF>" + player.getUsername() + " has donated "
					+ Misc.insertCommasToNumber("" + amount + "") + " coins to the Well of Goodwill!");
		}
		DialogueManager.sendStatement(player, "Thank you for your donation.");
		if (getMissingAmount() <= 0) {
			STATE = WellState.FULL;
			START_TIMER = System.currentTimeMillis();
			World.sendMessage("<col=6666FF>The Well of Goodwill has been filled!");
			World.sendMessage("<col=6666FF>It is now granting everyone 2 hours of 30% bonus experience.");
			World.getPlayers()
					.forEach(p -> p.getPacketSender().sendString(39162, "@or2@Well of Goodwill: @yel@Active"));
		}
	}

	public static int getMinutesRemaining() {

		return (BONUSES_DURATION - Misc.getMinutesPassed(System.currentTimeMillis() - START_TIMER));
	}

	public static int getMissingAmount() {
		return (AMOUNT_NEEDED - MONEY_IN_WELL);
	}

	public static void init() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("./data/saves/edgeville-well.txt"));
			if (in != null) {
				String line = in.readLine();
				if (line != null) {
					long startTimer = Long.parseLong(line);
					if (startTimer > 0) {
						STATE = WellState.FULL;
						START_TIMER = startTimer;
						MONEY_IN_WELL = AMOUNT_NEEDED;
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isActive() {
		updateState();
		return STATE == WellState.FULL;
	}

	public static void lookDownWell(Player player) {
		if (checkFull(player)) {
			return;
		}
		DialogueManager.start(player, new Dialogue() {

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}

			@Override
			public String[] dialogue() {
				return new String[] { "It looks like the well could hold another "
						+ Misc.insertCommasToNumber("" + getMissingAmount() + "") + " coins." };
			}

			@Override
			public Dialogue nextDialogue() {
				return DialogueManager.getDialogues().get(75);
			}

			@Override
			public int npcId() {
				return 802;
			}

			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

		});
	}

	public static void save() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("./data/saves/edgeville-well.txt"));
			out.write("" + START_TIMER);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setDefaults() {
		DONATORS.clear();
		STATE = WellState.EMPTY;
		START_TIMER = 0;
		MONEY_IN_WELL = 0;
	}

	public static void updateState() {
		if (STATE == WellState.FULL) {
			if (getMinutesRemaining() <= 0) {
				World.sendMessage("<col=6666FF>The Well of Goodwill is no longer granting bonus experience.");
				World.getPlayers()
						.forEach(p -> p.getPacketSender().sendString(39162, "@or2@Well of Goodwill: @yel@N/A"));
				setDefaults();
			}
		}
	}
}
