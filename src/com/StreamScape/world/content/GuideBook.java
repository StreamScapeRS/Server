package com.StreamScape.world.content;

public class GuideBook extends Book {

	private static final String[][] pages = {

			// NOTE: 10 lines per page, 43 letters per line.

			/*
			 * Page 0.
			 */
			{ "Gear up with the best armor from the stores", "around home that you can buy with your",
					"starter cash. Wield your starter club &", "head to starter island to train your stats,",
					"train att, str & def to 99 you will also", "want to get at least 50 prayer, you can do",
					"this by picking up lugis bones & using them", "on the altar at home, get their quickly by",
					"clicking the prayer stat in the stats tab.", "Lastly rack up 1b cash, once you get 1b", },
			/*
			 * Page 1.
			 */
			{ "cash head back to the melee store to buy", "a whip any color will do they all have",
					"the same stats, once you do this gear up", "with some food/potions & head to pointzone",
					"or american torva zone the choice here is", "yours. You can A: go to pointzone and rack",
					"up points to purchase a better weapon", "before heading to american torvas or just",
					"head straight there in hope to score some", "loot! From here on out you will bascially", },
			/*
			 * Page 2.
			 */
			{ "work your way up the torvas getting better", "and better gear, eventually slaying the",
					"bosses for their loot and making bank, you", "can also do things like mini-games for nice",
					"rewards & travel to various skilling zones", "by simply clicking on a skill in the stats",
					"tab but they are probably not the best way", "to start off. Welcome to StreamScape, have",
					"a great time!", },
			/*
			 * Page 3.
			 */
			{ "Player+ Commands", "::benefits - benefits of donating", "::commands - list of all commands",
					"::home - teles to home", "::train - teles to train", "::pointzone - teles to point zone",
					"::american - teles to american torvas", "::oreo - teles to oreo torvas",
					"::sky - teles to sky torvas", "::darth - teles to darth torvas", },
			/*
			 * Page 4.
			 */
			{ "::cash - teles to cash torvas", "::silver - teles to silver torvas", "::camo - teles to camo boss",
					"::winter - teles to winter boss", "::bloodshot - teleports to bloodshot boss",
					"::gamble - teleports to gambling zone", "::duel - teles to duel arena",
					"::zombies - teles to zombies starting point", "::rfd - teles to recipe for disaster ",
					"starting point", },
			/*
			 * Page 5.
			 */
			{ "::nomad - teles to nomad starting point", "::1v1 - teles to edgeville ditch",
					"::multi - teleports to varrock ditch", "::vote - opens the webpage.",
					"::donate - opens the webpage.", "::forums - opens the webpage.", "::hiscores - opens the webpage.",
					"::store - opens the webpage.", "::droplog - opens the interface.",
					"::discord - opens the webpage.", },
			/*
			 * Page 6.
			 */
			{ "::report - opens the webpage.", "::rules - opens the webpage.", "::support - opens the webpage.",
					"::players - shows players online interface.", "::empty - emptys your inventory.",
					"::emptybank - clears the players bank.", "::save - saves your account.",
					"::guides - opens webpage for in-game guides", "", },
			/*
			 * Page 7.
			 */
			{ "Donor+ Commands", "::yell (message here) - sends yell message", "::dzone - teleports to donor zone", "",
					"Deluxe Donor+ Commands", "::ddzone - teleport to deluxe donor zone",
					"::customize - opens yell customizer interface", "::bank - opens bank", },
			/*
			 * Page 8.
			 */
			{ "Sponsor Commands", "::szone - teleports to sponsor zone" }, { "" } };

	public GuideBook() {
		super(pages);
	}

	@Override
	public String getName() {
		return "Guide Book";
	}

}
