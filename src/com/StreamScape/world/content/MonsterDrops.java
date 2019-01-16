package com.StreamScape.world.content;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.StreamScape.drops.NPCDrops;

import com.StreamScape.model.definitions.ItemDefinition;
import com.StreamScape.model.definitions.NpcDefinition;
import com.StreamScape.util.Misc;
import com.StreamScape.world.entity.impl.player.Player;

/**
 * @Author Levi Patton
 * @rune-server.org/members/AuguryPS
 *
 * @author Tyrant (tyrant.inquiries@gmail.com) -> Fixes
 *
 */
public class MonsterDrops {
	/**
	 * A method that initializes and fills in the cache of the drops. What this
	 * does is go by every {@link NPCDrops} and attaches it to its relevant npc
	 * name so a player can view the drop table for that name.
	 */
	public static void initialize() {
		try {
			NPCDrops.getDrops().entrySet().stream().filter(Objects::nonNull)
					.filter($d -> $d.getKey() > 0 && NpcDefinition.forId($d.getKey()) != null).forEach($d -> {
				npcDrops.put(NpcDefinition.forId($d.getKey()).getName().toLowerCase(), $d.getValue());
//						System.out.println("Added: " + $d.getValue().toString() + " " + $d.getKey());
			});
			System.out.println("MonsterDrops has been initialized: size " + npcDrops.size());
		} catch (Exception i) {
			i.printStackTrace();
		}
	}


	public static Map<String, NPCDrops> npcDrops = new HashMap<>();

	public static void sendNpcDrop(Player player, int id, String name) {
		// NPCDrops drops = NPCDrops.forId(id);
		NPCDrops drops = npcDrops.get(name);

		if (drops == null) {
			player.sendMessage(
					"No drop table found for " + name + " " + id + " " + name);
			return;
		}


		NPCDrops.order(drops.getDropList());
		String colour;

		int line = 8147;
		//NPCDrops drops = NPCDrops.forId(NpcDefinition.forName(name).getId());
		System.out.println("put: " + drops + " " + npcDrops.get(name));

		for (int i = 8145; i < 8196; i++) {
			player.getPacketSender().sendString(i, "");
		}

		for (int i = 12174; i < 12224; i++) {
			player.getPacketSender().sendString(i, "");
			player.getPacketSender().sendInterface(61994);
			player.getPacketSender().sendString(8136, "Close window");
			player.getPacketSender().sendString(637, name + " Drops");
			player.getPacketSender().sendString(8145, "");
			player.getPA().setScrollBar(8143, 600);
		}


		for (int i = 0; i < drops.getDropList().length; i++) {

			if (drops.getDropList()[i].getItem().getId() <= 0
					|| drops.getDropList()[i].getItem().getId() > ItemDefinition.getMaxAmountOfItems()
					|| drops.getDropList()[i].getItem().getAmount() <= 0) {

			}
			if(drops.getDropList()[i].getChance() >= 1 && drops.getDropList()[i].getChance() <= 5) {
				colour =  "@gre@";
			} else if(drops.getDropList()[i].getChance() >= 6 && drops.getDropList()[i].getChance() <= 15) {
				colour = "@blu@";
			} else if(drops.getDropList()[i].getChance() >= 15 && drops.getDropList()[i].getChance() <= 100) {
				colour = "@red@";
			} else {
				colour = "@mag@";
			}/*
			double format = (100/ (double) drops.getDropList()[i].getChance());
			DecimalFormat format1 = new DecimalFormat("0.00");*/
			player.getPacketSender().sendString(line, colour+""+ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName() + " x"+ Misc.format(drops.getDropList()[i].getItem().getAmount()) + " @or1@ 1 / " + drops.getDropList()[i].getChance() );
			line++;

//
//            final NPCDrops.DropChance dropChance = drops.getDropList()[i].getChance();
//            final NPCDrops.WellChance wellChance = drops.getDropList()[i].getWellChance();
//
//            if (dropChance == NPCDrops.DropChance.ALWAYS) {
//                player.getPA().sendFrame126(line,
//                        ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName() + " x"
//                                + Misc.format(drops.getDropList()[i].getItem().getAmount()));
//                line++;
//            }
//            if (wellChance == NPCDrops.WellChance.ALWAYS) {
//                player.getPA().sendFrame126(line,
//                        ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName() + " x"
//                                + Misc.format(drops.getDropList()[i].getItem().getAmount()));
//                line++;
//            }
//        }
//        if (line == 29081) {
//            player.getPA().sendFrame126(28901 + 4, "\\n\\n\\n\\n\\nNo 100% drops");
//        }
//        line++;
//
//        ArrayList<String> added = new ArrayList<>();
//        line = 28922;
//        for (int i = 28922; i < 28962; i++) {
//            player.getPA().sendFrame126(i, "");
//        }
//
//        for (int i = 0; i < drops.getDropList().length; i++) {
//            if (drops.getDropList()[i].getItem().getId() <= 0
//                    || drops.getDropList()[i].getItem().getId() > ItemDefinition.getMaxAmountOfItems()
//                    || drops.getDropList()[i].getItem().getAmount() <= 0) {
//                continue;
//            }
//            final NPCDrops.DropChance dropChance = drops.getDropList()[i].getChance();
//            if (dropChance.ordinal() > NPCDrops.DropChance.ALWAYS.ordinal()
//                    && dropChance.ordinal() <= NPCDrops.DropChance.NOTTHATRARE.ordinal()) {
//                String itemName = ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName();
//                if (!added.contains(itemName)) {
//                    added.add(itemName);
//                    player.getPA().sendFrame126(line,
//                            itemName + " x" + Misc.format(drops.getDropList()[i].getItem().getAmount()));
//                    ;
//                    line++;
//                }
//            }
//            final NPCDrops.WellChance wellChance = drops.getDropList()[i].getWellChance();
//            if (wellChance.ordinal() > NPCDrops.WellChance.ALWAYS.ordinal()
//                    && wellChance.ordinal() <= NPCDrops.WellChance.NOTTHATRARE.ordinal()) {
//                String itemName = ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName();
//                if (!added.contains(itemName)) {
//                    added.add(itemName);
//                    player.getPA().sendFrame126(line,
//                            itemName + " x" + Misc.format(drops.getDropList()[i].getItem().getAmount()));
//                    ;
//                    line++;
//                }
//            }
//        }
//        player.getPA().sendFrame126(28901 + 5, "DropList: " + added.size());
//        added.clear();
//
//        for (int i = 29002; i < 29042; i++) {
//            player.getPA().sendFrame126(i, "");
//        }
//        line = 29002;
//        for (int i = 0; i < drops.getDropList().length; i++) {
//            if (drops.getDropList()[i].getItem().getId() <= 0
//                    || drops.getDropList()[i].getItem().getId() > ItemDefinition.getMaxAmountOfItems()
//                    || drops.getDropList()[i].getItem().getAmount() <= 0) {
//                continue;
//            }
//            final NPCDrops.DropChance dropChance = drops.getDropList()[i].getChance();
//            if (dropChance.ordinal() >= NPCDrops.DropChance.RARE.ordinal()) {
//                String itemName = ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName();
//                if (!added.contains(itemName)) {
//                    added.add(itemName);
//                    player.getPA().sendFrame126(line,
//                            itemName + " x" + Misc.format(drops.getDropList()[i].getItem().getAmount()));
//                    ;
//                    line++;
//                }
//            }
//            final NPCDrops.WellChance wellChance = drops.getDropList()[i].getWellChance();
//            if (wellChance.ordinal() >= NPCDrops.WellChance.RARE.ordinal()) {
//                String itemName = ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName();
//                if (!added.contains(itemName)) {
//                    added.add(itemName);
//                    player.getPA().sendFrame126(line,
//                            itemName + " x" + Misc.format(drops.getDropList()[i].getItem().getAmount()));
//                    ;
//                    line++;
//                }
//            }
//        }
//        player.getPA().sendFrame126(28901 + 6, "Rare Drops: " + added.size());
//
//        player.getPA().sendFrame126(28901 + 7, "");
//        player.getPA().sendFrame126(28901 + 3, Misc.formatPlayerName(name));
//        player.getPA().sendInterface(16453);
		}
	}
}
