package com.StreamScape.world.content;

import com.StreamScape.model.Flag;
import com.StreamScape.model.GameMode;
import com.StreamScape.model.Item;
import com.StreamScape.model.Skill;
import com.StreamScape.world.content.Achievements.AchievementData;
import com.StreamScape.world.content.skill.SkillManager;
import com.StreamScape.world.entity.impl.player.Player;

public class StartScreen {
	public enum GameModes {
		NORMAL("Normal", -18533, -18533, 1, 0,
				new Item[] { new Item(1856, 1), new Item(995, 1000000), new Item(1349, 1), new Item(1267, 1),
						new Item(9177, 1), new Item(3289, 1), (new Item(1191, 1)), new Item(1153, 1),
						(new Item(1115, 1)), (new Item(1067, 1)), (new Item(4121, 1)), (new Item(380, 10)),
						(new Item(3024, 1)), (new Item(2436, 1)), (new Item(2440, 1)), (new Item(2442, 1)) },
				"", "Play StreamScape on normal mode.",
				"As a normal player you will be able to play the game without any restrictions,", "nor any boosts.", "",
				"", ""),

		IRONMAN("  Ironman", -18532, -18532, 1, 1,
				new Item[] { new Item(1856, 1), new Item(995, 1500000), new Item(1349, 1), new Item(1267, 1),
						new Item(9177, 1), new Item(3289, 1), (new Item(1191, 1)), new Item(1153, 1),
						(new Item(1115, 1)), (new Item(1067, 1)), (new Item(4121, 1)), (new Item(380, 15)),
						(new Item(3024, 2)), (new Item(2436, 2)), (new Item(2440, 2)), (new Item(2442, 2)) },
				"Play StreamScape as an Iron man.",
				"You will be restricted from trading, staking and looting items from killed players. You will not",
				"get a npc drop if another player has done more damage. You will have to rely on your starter,",
				"skilling, pvming, and shops. This game mode is for players that love a challenge.", "", "", ""),

		HARDCORE("  Hardcore", -18531, -18531, 1, 2,
				new Item[] { new Item(1856, 1), new Item(995, 100000), new Item(1349, 1), new Item(1267, 1),
						new Item(9177, 1), new Item(3289, 1), (new Item(1191, 1)), new Item(1153, 1),
						(new Item(1115, 1)), (new Item(1067, 1)), (new Item(4121, 1)), (new Item(380, 5)),
						(new Item(3024, 1)), (new Item(2436, 1)), (new Item(2440, 1)), (new Item(2442, 1)) },
				"Play StreamScape on Hardcore mode.",
				"Hardcore mode has a slower xp rate than normal players w/ 5% increased drop rate,",
				"x1.5 StreamScape points, x2 vote points, x2.5 slayer points, & x3 prestige points. These will",
				"stack with other ranks & items such as donor & ring of wealth.", "", "", "");

		private String name;
		private int stringId;
		private int checkClick;
		private int textClick;
		private int configId;
		protected Item[] starterPackItems;
		private String line1;
		private String line2;
		private String line3;
		private String line4;
		private String line5;
		private String line6;
		private String line7;

		private GameModes(String name, int stringId, int checkClick, int textClick, int configId,
				Item[] starterPackItems, String line1, String line2, String line3, String line4, String line5,
				String line6, String line7) {
			this.name = name;
			this.stringId = stringId;
			this.checkClick = checkClick;
			this.textClick = textClick;
			this.configId = configId;
			this.starterPackItems = starterPackItems;
			this.line1 = line1;
			this.line2 = line2;
			this.line3 = line3;
			this.line4 = line4;
			this.line5 = line5;
			this.line6 = line6;
			this.line7 = line7;
		}
	}

	public static void addStarterToInv(Player player) {
		for (Item item : player.selectedGameMode.starterPackItems) {
			player.getInventory().add(item);
		}
		Achievements.doProgress(player, AchievementData.CREATE_AN_ACCOUNT);
	}

	public static void resetStarter(Player player) {
		for (GameModes g : GameModes.values()) {
			if (g.toString().equalsIgnoreCase(player.getGameMode().toString()))
				for (Item item : g.starterPackItems) {
					player.getInventory().add(item);
				}
		}
	}

	public static void check(Player player, GameModes mode) {
		for (GameModes gameMode : GameModes.values()) {
			if (player.selectedGameMode == gameMode) {
				player.getPacketSender().sendConfig(gameMode.configId, 1);
				continue;
			}
			player.getPacketSender().sendConfig(gameMode.configId, 0);
		}
	}

	public static boolean handleButton(Player player, int buttonId) {
		final int CONFIRM = -18530;
		if (buttonId == CONFIRM) {
			handleConfirm(player);
			return true;
		}
		for (GameModes mode : GameModes.values()) {
			if (mode.checkClick == buttonId || mode.textClick == buttonId) {
				selectMode(player, mode);
				return true;
			}
		}
		return false;
	}

	public static void handleConfirm(Player player) {
		if (!player.getClickDelay().elapsed(10000)) {
			return;
		}
		player.getClickDelay().reset();
		player.getPacketSender().sendInterfaceRemoval();
		if (player.selectedGameMode == GameModes.HARDCORE) {
			player.setGameMode(GameMode.HARDCORE);
		} else if (player.selectedGameMode == GameModes.IRONMAN) {
			player.setGameMode(GameMode.IRONMAN);
		} else {
			player.setGameMode(GameMode.NORMAL);
		}
		addStarterToInv(player);
		player.setPlayerLocked(false);
		player.getPacketSender().sendIronmanMode(player.getGameMode().ordinal());
		player.getUpdateFlag().flag(Flag.APPEARANCE);
	}

	public static void open(Player player) {
		sendNames(player);
		player.getPacketSender().sendInterface(47000);
		player.selectedGameMode = GameModes.NORMAL;
		check(player, GameModes.NORMAL);
		sendStartPackItems(player, GameModes.NORMAL);
		sendDescription(player, GameModes.NORMAL);
		}

	public static void selectMode(Player player, GameModes mode) {
		player.selectedGameMode = mode;
		check(player, mode);
		sendStartPackItems(player, mode);
		sendDescription(player, mode);
	}

	public static void sendDescription(Player player, GameModes mode) {
		int s = 47002;
		String text = mode.line1 + "\\n" + mode.line2 + "\\n" + mode.line3 + "\\n" + mode.line4 + "\\n" + mode.line5
				+ "\\n" + mode.line6 + "\\n" + mode.line7;

		player.getPacketSender().sendString(s, text);
	}

	public static void sendNames(Player player) {
		for (GameModes mode : GameModes.values()) {
			player.getPacketSender().sendString(mode.stringId, mode.name);
		}
	}

	public static void sendStartPackItems(Player player, GameModes mode) {
		final int START_ITEM_INTERFACE = 59025;
		for (int i = 0; i < 28; i++) {
			int id = -1;
			int amount = 0;
			try {
				id = mode.starterPackItems[i].getId();
				amount = mode.starterPackItems[i].getAmount();
			} catch (Exception e) {

			}
			player.getPacketSender().sendItemOnInterface(START_ITEM_INTERFACE + i, id, amount);
		}
	}
}
