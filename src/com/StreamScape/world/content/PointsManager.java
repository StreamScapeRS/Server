package com.StreamScape.world.content;

import java.util.HashMap;

import com.StreamScape.world.entity.impl.player.Player;

public class PointsManager {

	private Player player;

	public static final String[] POINTS = { "pvp", "StreamScape", "prestige", "loyalty", "voting", "slayer", "dung",
			"donation", "pc", "achievement" };

	public PointsManager(Player player) {
		this.player = player;
	}

	private HashMap<String, Integer> points = new HashMap<String, Integer>();

	public void refreshPanel() {
		// TODO player.getPacketSender().sendString(39174, "@red@Donation Points: @bla@"
		// + getPoints("donation"));
		player.getPacketSender().sendString(39174, "@red@StreamScape Points: @bla@" + getPoints("StreamScape"));
		player.getPacketSender().sendString(39175, "@red@Prestige Points: @bla@" + getPoints("prestige"));
		player.getPacketSender().sendString(39176, "@red@Loyalty Points: @bla@" + getPoints("loyalty"));
		player.getPacketSender().sendString(39177, "@red@Dung. Tokens: @bla@ " + getPoints("dung"));
		player.getPacketSender().sendString(39178, "@red@Voting Points: @bla@ " + getPoints("voting"));
		player.getPacketSender().sendString(39179, "@red@Slayer Points: @bla@" + getPoints("slayer"));
		player.getPacketSender().sendString(39180, "@red@Pk Points: @bla@" + getPoints("pvp"));
		player.getPacketSender().sendString(39181,
				"@red@Wilderness Killstreak: @bla@" + player.getPlayerKillingAttributes().getPlayerKillStreak());
		player.getPacketSender().sendString(39182,
				"@red@Wilderness Kills: @bla@" + player.getPlayerKillingAttributes().getPlayerKills());
		player.getPacketSender().sendString(39183,
				"@red@Wilderness Deaths: @bla@" + player.getPlayerKillingAttributes().getPlayerDeaths());
		player.getPacketSender().sendString(39184, "@red@Arena Victories: @bla@" + player.getDueling().arenaStats[0]);
		player.getPacketSender().sendString(39185, "@red@Arena Losses: @bla@" + player.getDueling().arenaStats[1]);
	}

	private double getModifier() {
		double more = 0.0;
		switch (player.getDonor()) {
		case DELUXE_DONOR:
			more = 2.0;
			break;
		case DONOR:
			more = 1.5;
			break;
		case SPONSOR:
			more = 3.0;
			break;
		default:
			break;
		}
		switch (player.getGameMode()) {
		case HARDCORE:
		case IRONMAN:
			more += 1.5;
			break;
		default:
			break;

		}
		return more;
	}

	public void create(String key) {
		points.put(key, 0);
	}

	public int getPoints(String key) {
		return points.get(key) == null ? 0 : points.get(key);
	}

	public void setPoints(String key, int value) {
		if (points.get(key) == null) {
			create(key);
		}
		points.put(key, value);
	}

	public void setWithIncrease(String key, int value) {
		if (points.get(key) == null) {
			create(key);
		}
		if (getModifier() > 1.0 && (key != "prestige") && (key != "pc") && (!key.equals("donation"))) {
			value *= getModifier();
		}
		points.put(key, points.get(key) + value);
	}

	public boolean decreasePoints(String key) {
		if (points.get(key) == null) {
			create(key);
		}
		if (points.get(key) < 1) {
			return false;
		}
		setPoints(key, points.get(key) - 1);
		return true;
	}

	public void decreasePoints(String key, int amount) {
		for (int i = 1; i <= amount; i++) {
			if (!decreasePoints(key)) {
				break;
			}
		}
	}

	public void clearPoints(String[] keys) {
		for (String key : keys) {
			points.remove(key);
		}
	}

	public void clearAll() {
		clearPoints(POINTS);
	}
}