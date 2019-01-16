package com.StreamScape.world.content;

import com.StreamScape.model.GroundItem;
import com.StreamScape.model.Item;
import com.StreamScape.model.Position;
import com.StreamScape.model.container.impl.Bank;
import com.StreamScape.util.Misc;
import com.StreamScape.world.content.LoyaltyProgram.LoyaltyTitles;
import com.StreamScape.world.entity.impl.GroundItemManager;
import com.StreamScape.world.entity.impl.player.Player;

public class Achievements {

	public enum AchievementData {

		CREATE_AN_ACCOUNT(Difficulty.EASY, "Create an account", null),

		USE_YOUR_BANK(Difficulty.EASY, "Use your bank", null),

		COMBAT_LVL_50(Difficulty.EASY, "Reach combat level 50", null),

		CHOP_A_TREE(Difficulty.EASY, "Chop a tree", null),

		CATCH_SOME_FISH(Difficulty.EASY, "Catch some fish", null),

		COOK_SOME_FISH(Difficulty.EASY, "Cook some fish", null),

		MINE_SOME_ORE(Difficulty.EASY, "Mine some ore", null),

		MAKE_SOME_BARS(Difficulty.EASY, "Make some bars", null),

		CRAFT_SOME_RUNES(Difficulty.EASY, "Craft some runes", null),

		BURY_SOME_BONES(Difficulty.EASY, "Bury some bones", null),

		FLETCH_SOME_ARROWS(Difficulty.EASY, "Fletch some arrows", null),

		HARVEST_A_CROP(Difficulty.EASY, "Harvest a crop", null),

		KILL_PLAYER(Difficulty.EASY, "Kill another player", null),

		CLEAN_HERB(Difficulty.EASY, "Clean a herb", null),

		STEAl_STALL(Difficulty.EASY, "Steal from a stall", null),

		CLIMB_OBSTACLE(Difficulty.EASY, "Climb an obstacle", null),

		ENTER_HOME(Difficulty.EASY, "Enter your home", null),

		MAKE_ARMOUR(Difficulty.EASY, "Make some armor", null),

		SUMMON_FAMILIAR(Difficulty.EASY, "Summon a familiar", null),

		COMPLETE_SLAYER_TASK(Difficulty.EASY, "Complete a slayer task", null),

		DUEL_PLAYER(Difficulty.EASY, "Win a duel", null),

		COMPLETE_QUEST(Difficulty.EASY, "Complete a quest", null),

		SET_EMAIL(Difficulty.EASY, "Set an email", null),

		TELEPORT_VARROCK(Difficulty.EASY, "Teleport to varrock", null),

		DEAL_1000_MELEE(Difficulty.EASY, "Deal 1000 melee damage", new int[] { 0, 1000 }),

		DEAL_1000_RANGE(Difficulty.EASY, "Deal 1000 range damage", new int[] { 1, 1000 }),

		DEAL_1000_MAGIC(Difficulty.EASY, "Deal 1000 magic damage", new int[] { 2, 1000 }),

		KILL_PurgatoryS(Difficulty.EASY, "Kill 25 Purgatorys", new int[] { 3, 25 }),

		KILL_MEWTWO(Difficulty.EASY, "Kill 25 MewTwos", new int[] { 4, 25 }),

		KILL_CHARMELEONS(Difficulty.EASY, "Kill 25 Charmeleons", new int[] { 5, 25 }),

		PLAY_ZOMBIES(Difficulty.EASY, "Play the zombie minigame", null),

		PLAY_HUNGER_GAMES(Difficulty.EASY, "Play the hunger games", null),

		VOTE(Difficulty.EASY, "Vote for StreamScape", null),

		COMPLETE_ALL_EASY(Difficulty.MEDIUM, "Compelete all easy tasks", new int[] { 62, 33 }),

		KILLSTREAK_3(Difficulty.MEDIUM, "Reach a killstreak of 3", new int[] { 6, 4 }),

		KILL_10_PLAYERS(Difficulty.MEDIUM, "Kill 10 players", new int[] { 7, 10 }),

		WIN5DUELS(Difficulty.MEDIUM, "Win 5 duels", new int[] { 8, 5 }),

		KILL50AMERICANS(Difficulty.MEDIUM, "Kill 50 American Torvas", new int[] { 9, 50 }),

		KILL50OEROS(Difficulty.MEDIUM, "Kill 50 Oreo Torvas", new int[] { 10, 50 }),

		KILL50SKYS(Difficulty.MEDIUM, "Kill 50 Sky Torvas", new int[] { 11, 50 }),

		DEFEATNOMAD(Difficulty.MEDIUM, "Defea Nomad", null),

		DEFEAT_CULINAROMANCER(Difficulty.MEDIUM, "Defeat Culinaromancer", null),

		DEAL_100_000_MELEE(Difficulty.MEDIUM, "Deal 100k melee damage", new int[] { 12, 100_000 }),

		DEAL_100_000_RANGE(Difficulty.MEDIUM, "Deal 100k range damage", new int[] { 13, 100_000 }),

		DEAL_100_000_MAGIC(Difficulty.MEDIUM, "Deal 100k magic damage", new int[] { 14, 100_000 }),

		COMBAT_LVL_75(Difficulty.MEDIUM, "Reach 75 combat level", null),

		TELEPORT_CAMELOT(Difficulty.MEDIUM, "Teleport to camelot", null),

		CHOP_50_TREES(Difficulty.MEDIUM, "Chop 50 trees", new int[] { 15, 50 }),

		CATCH_50_FISH(Difficulty.MEDIUM, "Catch 50 fish", new int[] { 16, 50 }),

		COOK_50_FISH(Difficulty.MEDIUM, "Cook 50 fish", new int[] { 17, 50 }),

		MINE_50_ORES(Difficulty.MEDIUM, "Mine 50 ores", new int[] { 18, 50 }),

		MAKE_50_BARS(Difficulty.MEDIUM, "Make 50 bars", new int[] { 19, 50 }),

		CRAFT_50_RUNES(Difficulty.MEDIUM, "Craft 50 runes", new int[] { 20, 50 }),

		BURY_50_BONES(Difficulty.MEDIUM, "Bury 50 bones", new int[] { 21, 50 }),

		FLETCH_50_ARROWS(Difficulty.MEDIUM, "Fletch 50 sets arrows", new int[] { 22, 50 }),

		HARVEST_50_CROPS(Difficulty.MEDIUM, "Harvest 50 crops", new int[] { 23, 50 }),

		CLEAN_50_HERBS(Difficulty.MEDIUM, "Clean 50 herbs", new int[] { 24, 50 }),

		STEAL_50_STALLS(Difficulty.MEDIUM, "Steal from 50 stalls", new int[] { 25, 50 }),

		CLIMB_50_OBSTACLES(Difficulty.MEDIUM, "Climb 50 obstacles", new int[] { 26, 50 }),

		BUILD_AN_OBJECT(Difficulty.MEDIUM, "Build an object in your house", null),

		SMITH_50_ITEMS(Difficulty.MEDIUM, "Smith 50 items", new int[] { 27, 50 }),

		SUMMON_25_FAMILIARS(Difficulty.MEDIUM, "Summon 25 familiars", new int[] { 28, 25 }),

		COMPLETE_15_SLAYER(Difficulty.MEDIUM, "Complete 15 slayer tasks", new int[] { 29, 15 }),

		VOTE_10_TIMES(Difficulty.MEDIUM, "Vote 10 times", new int[] { 30, 10 }),

		COMPLETE_ALL_MEDIUM(Difficulty.HARD, "Complete all medium tasks", new int[] { 63, 31 }),

		KILLSTREAK_6(Difficulty.HARD, "Reach killstreak of 6", new int[] { 31, 6 }),

		KILL_30_PLAYERS(Difficulty.HARD, "Kill 30 players", new int[] { 32, 30 }),

		WIN10DUELS(Difficulty.HARD, "Win 10 duel", new int[] { 33, 10 }),

		KILL50DARTHMAULS(Difficulty.HARD, "Kill 50 darth mauls", new int[] { 34, 50 }),

		KILL50CASH(Difficulty.HARD, "Kill 50 cash torvas", new int[] { 35, 50 }),

		KILL50SILVER(Difficulty.HARD, "Kill 50 silver torvas", new int[] { 36, 50 }),

		DEAL_10M_MELEE(Difficulty.HARD, "Deal 10,000,000 melee damage", new int[] { 37, 10_000_000 }),

		DEAL_10M_RANGE(Difficulty.HARD, "Deal 10,000,000 range damage", new int[] { 38, 10_000_000 }),

		DEAL_10M_MAGIC(Difficulty.HARD, "Deal 10,000,000 magic damage", new int[] { 39, 10_000_000 }),

		COMBAT_LVL_100(Difficulty.HARD, "Reach combat level of 100", null),

		TELEPORT_APE_ATOL(Difficulty.HARD, "Teleport to ape atol", null),

		CHOP_500_TREES(Difficulty.HARD, "Chop 500 trees", new int[] { 40, 500 }),

		CATCH_500_FISH(Difficulty.HARD, "Catch 500 fish", new int[] { 41, 500 }),

		MINE_500_ORES(Difficulty.HARD, "Mine 500 ores", new int[] { 42, 500 }),

		MAKE_500_BARS(Difficulty.HARD, "Make 500 bars", new int[] { 43, 500 }),

		CRAFT_500_RUNES(Difficulty.HARD, "Craft 500 runes", new int[] { 44, 500 }),

		BURY_500_BONES(Difficulty.HARD, "Bury 500 bones", new int[] { 45, 500 }),

		FLETCH_500_ARROWS(Difficulty.HARD, "Fletch 500 arrows", new int[] { 46, 500 }),

		HARVEST_500_CROPS(Difficulty.HARD, "Harvest 500 crops", new int[] { 47, 500 }),

		CLEAN_500_HERBS(Difficulty.HARD, "Clean 500 herbs", new int[] { 48, 500 }),

		STEAL_500_STALLS(Difficulty.HARD, "Steal 500 stalls", new int[] { 49, 500 }),

		CLIMB_500_OBSTACLES(Difficulty.HARD, "Climb 500 obstacles", new int[] { 50, 500 }),

		BUILD_50_OBJECTS(Difficulty.HARD, "Build 50 objects", new int[] { 51, 50 }),

		SMITH_500_ITEMS(Difficulty.HARD, "Smith 500 items", new int[] { 52, 500 }),

		SUMMON_100_FAMILIARS(Difficulty.HARD, "Summon 100 familiars", new int[] { 53, 100 }),

		COMPLETE_50_TASKS(Difficulty.HARD, "Complete 50 tasks", new int[] { 54, 50 }),

		VOTE_50_TIMES(Difficulty.HARD, "Vote 50 times", new int[] { 55, 50 }),

		COMPLETE_ALL_HARD(Difficulty.ELITE, "Complete all hard tasks", new int[] { 64, 28 }),

		KILLSTREAK_10(Difficulty.ELITE, "Reach a killstreak of 10", new int[] { 56, 10 }),

		KILL_50_PLAYERS(Difficulty.ELITE, "Kill 50 players", new int[] { 57, 50 }),

		WIN_25_DUELS(Difficulty.ELITE, "Win 25 duels", new int[] { 58, 25 }),

		KILL_10K(Difficulty.ELITE, "Kill 10k npcs", new int[] { 59, 10_000 }),

		KILL_500_BOSSES(Difficulty.ELITE, "Kill 500 bosses", new int[] { 60, 500 }),

		MAX_COMBAT(Difficulty.ELITE, "Reach max combat", null),

		MAX_SKILL(Difficulty.ELITE, "Get max skill in all skills", null),

		VOTE_100(Difficulty.ELITE, "Vote 100 times", new int[] { 61, 100 }),

		LOYALTY_TITLES(Difficulty.ELITE, "Unlock all loyalty titles", new int[] { 62, LoyaltyTitles.values().length });

		AchievementData(Difficulty difficulty, String interfaceLine, int[] progressData) {
			this.difficulty = difficulty;
			this.interfaceLine = interfaceLine;
			this.progressData = progressData;
		}

		private Difficulty difficulty;
		private String interfaceLine;
		private int[] progressData;

		public Difficulty getDifficulty() {
			return difficulty;
		}
	}

	public enum Difficulty {
		BEGINNER, EASY, MEDIUM, HARD, ELITE, TEMPORARY;
	}

	public static boolean allComplete(Player player, Difficulty diff) {
		for (AchievementData data : AchievementData.values()) {
			if (data.getDifficulty().equals(diff)) {
				if (!player.getAchievementAttributes().getCompletion()[data.ordinal()]) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean handleButton(Player player, int button) {
		if (!(button >= -28531 && button <= -28425)) {
			return false;
		}
		int index = -1;
		if (button >= -28531 && button <= -28499) {
			index = 28531 + button;
		} else if (button >= -28497 && button <= -28467) {
			index = 33 + 28497 + button;
		} else if (button >= -28465 && button <= -28438) {
			index = 64 + 28465 + button;
		} else if (button >= -28436 && button <= -28427) {
			index = 92 + 28436 + button;
		}
		if (index >= 0 && index < AchievementData.values().length) {
			AchievementData achievement = AchievementData.values()[index];

			if (achievement == AchievementData.COMBAT_LVL_50 && player.getSkillManager().getCombatLevel() < 50) {
				player.getPacketSender().sendMessage("<col=FFFF00>Your progress for this achievement is currently at: "
						+ player.getSkillManager().getCombatLevel() + "/50.");
				return true;
			}
			if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()]) {
				player.getPacketSender().sendMessage(
						"<col=339900>You have completed the achievement: " + achievement.interfaceLine + ".");
			} else if (achievement.progressData == null) {
				player.getPacketSender().sendMessage(
						"<col=660000>You have not started the achievement: " + achievement.interfaceLine + ".");
			} else {
				int progress = player.getAchievementAttributes().getProgress()[achievement.progressData[0]];
				int requiredProgress = achievement.progressData[1];
				if (progress == 0) {
					player.getPacketSender().sendMessage(
							"<col=660000>You have not started the achievement: " + achievement.interfaceLine + ".");
				} else if (progress != requiredProgress) {
					player.getPacketSender()
							.sendMessage("<col=FFFF00>Your progress for this achievement is currently at: "
									+ Misc.insertCommasToNumber("" + progress) + "/"
									+ Misc.insertCommasToNumber("" + requiredProgress) + ".");
				}
			}
		}
		return true;
	}

	public static void updateInterface(Player player) {

		int easy = 37005;
		int med = 37039;
		int hard = 37071;
		int elite = 37100;
		for (AchievementData achievement : AchievementData.values()) {
			boolean completed = player.getAchievementAttributes().getCompletion()[achievement.ordinal()];
			boolean progress = achievement.progressData != null
					&& player.getAchievementAttributes().getProgress()[achievement.progressData[0]] > 0;
			switch (achievement.difficulty) {
			case EASY:
				player.getPacketSender().sendString(easy,
						(completed ? "@gre@" : progress ? "@yel@" : "@red@") + achievement.interfaceLine);
				easy++;
				break;
			case MEDIUM:
				player.getPacketSender().sendString(med,
						(completed ? "@gre@" : progress ? "@yel@" : "@red@") + achievement.interfaceLine);
				med++;
				break;
			case HARD:
				player.getPacketSender().sendString(hard,
						(completed ? "@gre@" : progress ? "@yel@" : "@red@") + achievement.interfaceLine);
				hard++;
				break;
			case ELITE:
				player.getPacketSender().sendString(elite,
						(completed ? "@gre@" : progress ? "@yel@" : "@red@") + achievement.interfaceLine);
				elite++;
				break;
			}
		}
		player.getPacketSender().sendString(37001, "Achievements: " + player.getPointsManager().getPoints("achievement")
				+ "/" + AchievementData.values().length);
	}

	public static void setPoints(Player player) {
		int points = 0;
		for (AchievementData achievement : AchievementData.values()) {
			if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()]) {
				points++;
			}
		}
		player.getPointsManager().setPoints("achievement", points);
	}

	public static void doProgress(Player player, AchievementData achievement) {
		doProgress(player, achievement, 1);
	}

	public static void doProgress(Player player, AchievementData achievement, int amt) {
		if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
			return;
		if (achievement.progressData != null) {
			int progressIndex = achievement.progressData[0];
			int amountNeeded = achievement.progressData[1];
			player.getAchievementAttributes().getProgress()[progressIndex] += amt;
			if (player.getAchievementAttributes().getProgress()[progressIndex] >= amountNeeded) {
				finishAchievement(player, achievement);
			}
			updateInterface(player);
		} else {
			finishAchievement(player, achievement);
		}
	}

	public static void finishAchievement(Player player, AchievementData achievement) {
		if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
			return;
		player.getAchievementAttributes().getCompletion()[achievement.ordinal()] = true;
		player.getPacketSender()
				.sendMessage("<col=339900>You have completed the achievement "
						+ Misc.formatText(achievement.toString().toLowerCase() + "."))
				.sendString(37001, "Achievements: " + player.getPointsManager().getPoints("achievement") + "/"
						+ AchievementData.values().length);
		switch (achievement.difficulty) {
		case EASY:
			if (player.getInventory().getFreeSlots() >= 1) {
				player.getInventory().add(new Item(995, 50_000_000));
			} else {
				GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(995, 50000000), new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()), player.getUsername(), false, 0, false, 0));
				player.getPacketSender()
						.sendMessage("Your reward for completing the achievement has been placed on the floor.");
			}
			Achievements.doProgress(player, AchievementData.COMPLETE_ALL_EASY);
			break;
		case MEDIUM:
			if (player.getInventory().getFreeSlots() > 1) {
				player.getInventory().add(new Item(995, 100_000_000));
			} else {
				GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(995, 100000000), player.getPosition(), player.getUsername(), false, 0, false, 0));
				player.getPacketSender()
						.sendMessage("Your reward for completing the achievement has been placed on the floor.");
			}
			Achievements.doProgress(player, AchievementData.COMPLETE_ALL_MEDIUM);
			break;
		case HARD:
			if (player.getInventory().getFreeSlots() > 0) {
				player.getInventory().add(new Item(995, 250_000_000));
			} else {
				GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(995, 250000000), player.getPosition(), player.getUsername(), false, 0, false, 0));
				player.getPacketSender()
						.sendMessage("Your reward for completing the achievement has been placed on the floor.");
			}
			Achievements.doProgress(player, AchievementData.COMPLETE_ALL_HARD);
			break;
		case ELITE:
			if (player.getInventory().getFreeSlots() > 0) {
				player.getInventory().add(new Item(995, 500_000_000));
			} else {
				GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(995, 500000000), player.getPosition(), player.getUsername(), false, 0, false, 0));
				player.getPacketSender()
						.sendMessage("Your reward for completing the achievement has been placed on the floor.");
			}
			break;
		}

		boolean completed = true;
		for (int i = 0; i < player.getAchievementAttributes().getCompletion().length; i++) {
			if (!player.getAchievementAttributes().getCompletion()[i]) {
				completed = false;
			}
		}
		Achievements.doProgress(player, AchievementData.COMPLETE_50_TASKS);
		if (completed) {
			player.getBank(0).add(13654, 1);
		}
		updateInterface(player);
		player.getPointsManager().setWithIncrease("achievement", 1);
	}

	public static class AchievementAttributes {

		public AchievementAttributes() {
		}

		/** ACHIEVEMENTS **/
		private boolean[] completed = new boolean[AchievementData.values().length];
		private int[] progress = new int[100];

		public boolean[] getCompletion() {
			return completed;
		}

		public void setCompletion(int index, boolean value) {
			this.completed[index] = value;
		}

		public void setCompletion(boolean[] completed) {
			this.completed = completed;
		}

		public int[] getProgress() {
			return progress;
		}

		public void setProgress(int index, int value) {
			this.progress[index] = value;
		}

		public void setProgress(int[] progress) {
			this.progress = progress;
		}

		/** MISC **/
		private int coinsGambled;
		private double totalLoyaltyPointsEarned;
		private boolean[] godsKilled = new boolean[5];

		public int getCoinsGambled() {
			return coinsGambled;
		}

		public void setCoinsGambled(int coinsGambled) {
			this.coinsGambled = coinsGambled;
		}

		public double getTotalLoyaltyPointsEarned() {
			return totalLoyaltyPointsEarned;
		}

		public void incrementTotalLoyaltyPointsEarned(double totalLoyaltyPointsEarned) {
			this.totalLoyaltyPointsEarned += totalLoyaltyPointsEarned;
		}

		public boolean[] getGodsKilled() {
			return godsKilled;
		}

		public void setGodKilled(int index, boolean godKilled) {
			this.godsKilled[index] = godKilled;
		}

		public void setGodsKilled(boolean[] b) {
			this.godsKilled = b;
		}
	}
}
