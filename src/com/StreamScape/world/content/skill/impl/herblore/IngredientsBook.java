package com.StreamScape.world.content.skill.impl.herblore;

import com.StreamScape.world.content.Book;

/**
 * Handles the Ingridient's book
 *
 * @author Gabriel Hannason
 *
 */
public class IngredientsBook extends Book {

	private static final String[][] pages = {
			{ "Lvl 1: Attack Potion", "Eye of newt", "Guam", " " + "" + "", "Lvl 9: Defence Potion", "Bear fur",
					"Marrentill", " ",

					"Lvl 12: Strength Potion", "Limpwurt root", "Tarrorim", "", },
			{ "Lvl 13: Antipoison Potion", "Unicorn horn dust", "Marrentill", " ",

					"Lvl 15: Serum 207", "Ashes", "Tarrorim", " ",

					"Lvl 22: Restore Potion", "Red spider's eggs", "Harralander", },
			{ "Lvl 26: Energy Potion", "Chocolate dust", "Harralander", " ", "Lvl 34: Agility Potion", "Toad's legs",
					"Toadflax", " ",

					"Lvl 36: Combat Potion", "Goat horn dust", "Harralander", "", },
			{ "Lvl 38: Prayer Potion", "Snape grass", "Ranarr", " ", "Lvl 40: Summoning Potion", "Cockatrice egg",
					"Spirit weed", " ",

					"Lvl 42: Crafting Potion", "Wergali", "Frog spawn", "", },
			{ "Lvl 45: Super Attack", "Eye of newt", "Irit", " ", "Lvl 48: Super Antipoison", "Unicorn horn dust",
					"Irit", " ",

					"Lvl 50: Fishing Potion", "Snape grass", "Avantoe", "", },
			{ "Lvl 53: Hunter Potion", "Kebbit teeth dust", "Avantoe", " ", "Lvl 55: Super Strength", "Limpwurt root",
					"Kwuarm", " ",

					"Lvl 58: Fletching Potion", "Wimpy feather", "Wergali", "", },
			{ "Lvl 60: Weapon Poison", "Dragon scale dust", "Kwuarm", " ", "Lvl 63: Super Restore", "Red spider's eggs",
					"Snapdragon", " ",

					"Lvl 66: Super Defence", "White berries", "Cadantine", "", },
			{ "Lvl 68: Antipoison+", "Yew roots", "Toadflax", " ", "Lvl 69: Antifire", "Dragon scale dust", "Lantadyme",
					" ",

					"Lvl 72: Ranging Potion", "Wine of Zamorak", "Dwarf weed", "", },
			{ "Lvl 76: Magic Potion", "Potato cactus", "Lantadyme", " ", "Lvl 78: Zamorak Brew", "Jangerberries",
					"Torstol", " ",

					"Lvl 81: Saradomin Brew", "Crushed bird nest", "Toadflax", "", },
			{ "Lvl 84: Restore Special", "Super energy(3)", "Papaya", " ", "Lvl 85: Super Antifire", "Antifire(3)",
					"Phoenix feather", " ",

					"Lvl 88: Extreme Attack", "Super Attack(3)", "Avantoe", "", },
			{ "Lvl 89: Extreme Strength", "Super Strength(3)", "Dwarf weed", " ", "Lvl 90: Extreme Defence",
					"Super Defence(3)", "Lantadyme", " ",

					"Lvl 91: Extreme Magic", "Magic Potion (3)", "Ground mud runes", "", },
			{ "Lvl 92: Extreme Ranging", "Ranging Potion (3)", "5 Grenwall Spikes", " ", "Lvl 94: Prayer Renewal",
					"Morchella mushroom", "Fellstalk", " ",

					"", "", "", "", "", },
			{ "Lvl 96: Overload Potion", "Extreme Attack(3)", "Extreme Strength(3)", "Extreme Defence(3)",
					"Extreme Ranging(3)", "Extreme Magic(3)", "", "", "", "", "", "" },
			{ "", "", "", "", "", "", " ", " ", " ", " ", " ", " ", "", "" } };

	public IngredientsBook() {
		super(pages);
	}

	@Override
	public String getName() {
		return "Ingredients";
	}

}
