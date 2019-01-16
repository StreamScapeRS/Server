package com.StreamScape.net.packet.impl;

import com.StreamScape.drops.NPCDrops;
import com.StreamScape.model.*;
import com.StreamScape.model.definitions.NpcDefinition;
import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.util.Misc;
import com.StreamScape.world.content.*;
import com.StreamScape.world.content.combat.range.DwarfMultiCannon;
import com.StreamScape.world.content.dialogue.DialogueManager;
import com.StreamScape.world.content.mbox.MegaMysteryBox;
import com.StreamScape.world.content.mbox.MyticalBox;
import com.StreamScape.world.content.mbox.NormalMysteryBox;
import com.StreamScape.world.content.mbox.PetMysteryBox;
import com.StreamScape.world.content.skill.SkillManager;
import com.StreamScape.world.content.skill.impl.construction.Construction;
import com.StreamScape.world.content.skill.impl.dungeoneering.ItemBinding;
import com.StreamScape.world.content.skill.impl.herblore.Herblore;
import com.StreamScape.world.content.skill.impl.herblore.IngredientsBook;
import com.StreamScape.world.content.skill.impl.hunter.*;
import com.StreamScape.world.content.skill.impl.hunter.Trap.TrapState;
import com.StreamScape.world.content.skill.impl.prayer.Prayer;
import com.StreamScape.world.content.skill.impl.runecrafting.Runecrafting;
import com.StreamScape.world.content.skill.impl.runecrafting.RunecraftingPouches;
import com.StreamScape.world.content.skill.impl.runecrafting.RunecraftingPouches.RunecraftingPouch;
import com.StreamScape.world.content.skill.impl.slayer.SlayerDialogues;
import com.StreamScape.world.content.skill.impl.slayer.SlayerTasks;
import com.StreamScape.world.content.skill.impl.summoning.CharmingImp;
import com.StreamScape.world.content.skill.impl.summoning.SummoningData;
import com.StreamScape.world.content.skill.impl.woodcutting.BirdNests;
import com.StreamScape.world.content.teleportation.JewelryTeleporting;
import com.StreamScape.world.entity.impl.npc.CustomPet;
import com.StreamScape.world.entity.impl.player.Player;

public class ItemActionPacketListener implements PacketListener {

	public static final int SECOND_ITEM_ACTION_OPCODE = 75;

	public static final int FIRST_ITEM_ACTION_OPCODE = 122;

	public static final int THIRD_ITEM_ACTION_OPCODE = 16;

	private static void firstAction(final Player player, Packet packet) {
		int interfaceId = packet.readUnsignedShort();
		int slot = packet.readShort();
		int itemId = packet.readShort();

		if (interfaceId == 38274) {
			Construction.handleItemClick(itemId, player);
			return;
		}

		if (slot < 0 || slot > player.getInventory().capacity())
			return;
		if (player.getInventory().getItems()[slot].getId() != itemId)
			return;
		player.setInteractingItem(player.getInventory().getItems()[slot]);
		if (Prayer.buryBone(player, itemId)) {
			return;
		}

		if (Consumables.isFood(player, itemId, slot))
			return;
		if (Consumables.isPotion(itemId)) {
			Consumables.handlePotion(player, itemId, slot);
			return;
		}
		if (BirdNests.isNest(itemId)) {
			BirdNests.searchNest(player, itemId);
			return;
		}
		if (Herblore.cleanHerb(player, itemId))
			return;
		if (DonorTickets.handleTicket(player, itemId))
			return;
		if (ExperienceLamps.handleLamp(player, itemId)) {
			return;
		}
		if (CustomPet.forItemId(itemId) != -1) {
			if (player.getSummoning().getFamiliar() == null)
				CustomPet.spawnForItem(player, itemId);
			else player.getPacketSender().sendMessage("you already have a pet");
			return;
		}
		switch (itemId) {
			case 20054:
				player.setNpcTransformationId(9463);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				player.getPacketSender().sendInterfaceRemoval();
				player.setMorpted(9463);
				player.sendMessage("<col=ff0000>You transformed into a "+NpcDefinition.forId(9463).getName()+ ".");
				break;

			case 18336:
				Skill skill = Skill.CONSTITUTION;
				int level = SkillManager.getMaxAchievingLevel(Skill.CONSTITUTION);
				player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
						SkillManager.getExperienceForLevel(level == 120 ? 120 : 99));
				player.getInventory().delete(18336, 1);
				player.sendMessage("you are now the master of life.");
				break;
			case 1321:
				player.getInventory().delete(1321, 1);
				player.getPointsManager().setWithIncrease("voting", 1);
				player.getPacketSender().sendMessage("Your vote point has been added to your vote points.");
				break;
			case 1856:
				player.setActiveBook(new GuideBook());
				player.getActiveBook().readBook(player, false);
				break;
			case 13663:
				if (player.getInterfaceId() > 0) {
					player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
					return;
				}
				player.setUsableObject(new Object[2]).setUsableObject(0, "reset");
				player.getPacketSender().sendString(38006, "Choose stat to reset!")
						.sendMessage("@red@Please select a skill you wish to reset and then click on the 'Confirm' button.")
						.sendString(38090, "Which skill would you like to reset?");
				player.getPacketSender().sendInterface(38000);
				break;
			case 19670:
				if (player.busy()) {
					player.getPacketSender().sendMessage("You can not do this right now.");
					return;
				}
				player.setDialogueActionId(70);
				DialogueManager.start(player, player.getGameMode() == GameMode.NORMAL ? 108 : 109);
				break;
			case 7956:
				player.getInventory().delete(7956, 1);
				int[] rewards = { 200, 202, 204, 206, 208, 210, 212, 214, 216, 218, 220, 2486, 3052, 1624, 1622, 1620, 1618,
						1632, 1516, 1514, 454, 448, 450, 452, 378, 372, 7945, 384, 390, 15271, 533, 535, 537, 18831, 556,
						558, 555, 554, 557, 559, 564, 562, 566, 9075, 563, 561, 560, 565, 888, 890, 892, 11212, 9142, 9143,
						9144, 9341, 9244, 866, 867, 868, 2, 10589, 10564, 6809, 4131, 15126, 4153, 1704, 1149 };
				int[] rewardsAmount = { 200, 200, 200, 120, 50, 100, 70, 60, 90, 40, 30, 15, 10, 230, 140, 70, 20, 10, 400,
						200, 400, 250, 100, 100, 1000, 800, 500, 200, 100, 50, 150, 100, 50, 5, 1500, 1500, 1500, 1500,
						1500, 1500, 1000, 1000, 500, 500, 500, 500, 500, 500, 3000, 2500, 800, 300, 3500, 3500, 500, 150,
						80, 3000, 1500, 400, 500, 1, 1, 1, 1, 1, 1, 1, 1 };
				int rewardPos = Misc.getRandom(rewards.length - 1);
				player.getInventory().add(rewards[rewardPos],
						(int) ((rewardsAmount[rewardPos] * 0.5) + (Misc.getRandom(rewardsAmount[rewardPos]))));
				break;
			case 15387:
				player.getInventory().delete(15387, 1);
				rewards = new int[] { 1377, 1149, 7158, 3000, 219, 5016, 6293, 6889, 2205, 3051, 269, 329, 3779, 6371, 2442,
						347, 247 };
				player.getInventory().add(rewards[Misc.getRandom(rewards.length - 1)], 1);
				break;
			case 20692:
				player.getInventory().delete(20692, 1);
				player.getInventory().add(11609, 1000);
				break;
			case 407:
				player.getInventory().delete(407, 1);
				if (Misc.getRandom(3) < 3) {
					player.getInventory().add(409, 1);
				} else if (Misc.getRandom(4) < 4) {
					player.getInventory().add(411, 1);
				} else
					player.getInventory().add(413, 1);
				break;
			case 405:
				player.getInventory().delete(405, 1);
				if (Misc.getRandom(1) < 1) {
					int coins = Misc.getRandom(30000);
					player.getInventory().add(995, coins);
					player.getPacketSender().sendMessage("The casket contained " + coins + " coins!");
				} else
					player.getPacketSender().sendMessage("The casket was empty.");
				break;
			case 15084:
				Gambling.rollDice(player);
				break;
			case 299:
				Gambling.plantSeed(player);
				break;
			case 4155:
				if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK) {
					player.getPacketSender().sendInterfaceRemoval();
					player.getPacketSender().sendMessage("Your Enchanted gem will only work if you have a Slayer task.");
					return;
				}
				DialogueManager.start(player, SlayerDialogues.dialogue(player));
				break;
			case 11858:
			case 11860:
			case 11862:
			case 11848:
			case 11856:
			case 11850:
			case 11854:
			case 11852:
			case 11846:
				if (!player.getClickDelay().elapsed(2000) || !player.getInventory().contains(itemId))
					return;
				if (player.busy()) {
					player.getPacketSender().sendMessage("You cannot open this right now.");
					return;
				}

				int[] items = itemId == 11858 ? new int[] { 10350, 10348, 10346, 10352 }
						: itemId == 11860 ? new int[] { 10334, 10330, 10332, 10336 }
						: itemId == 11862 ? new int[] { 10342, 10338, 10340, 10344 }
						: itemId == 11848 ? new int[] { 4716, 4720, 4722, 4718 }
						: itemId == 11856 ? new int[] { 4753, 4757, 4759, 4755 }
						: itemId == 11850 ? new int[] { 4724, 4728, 4730, 4726 }
						: itemId == 11854 ? new int[] { 4745, 4749, 4751, 4747 }
						: itemId == 11852
						? new int[] { 4732, 4734, 4736, 4738 }
						: itemId == 11846
						? new int[] { 4708, 4712, 4714,
						4710 }
						: new int[] { itemId };

				if (player.getInventory().getFreeSlots() < items.length) {
					player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
					return;
				}
				player.getInventory().delete(itemId, 1);
				for (int i : items) {
					player.getInventory().add(i, 1);
				}
				player.getPacketSender().sendMessage("You open the set and find items inside.");
				player.getClickDelay().reset();
				break;
			case 952:
				Digging.dig(player);
				break;
			case 10006:
				// Hunter.getInstance().laySnare(client);
				Hunter.layTrap(player, new SnareTrap(new GameObject(19175, new Position(player.getPosition().getX(),
						player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
				break;
			case 10008:
				Hunter.layTrap(player, new BoxTrap(new GameObject(19187, new Position(player.getPosition().getX(),
						player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
				break;
			case 5509:
			case 5510:
			case 5512:
				RunecraftingPouches.fill(player, RunecraftingPouch.forId(itemId));
				break;
			case 292:
				player.setActiveBook(new IngredientsBook());
				player.getActiveBook().readBook(player, false);
				break;

			case 85:
				if (player.getDonor() != PlayerRanks.DonorRights.NONE) {
					player.sendMessage("You cannot claim this rank.");
					return;
				}
				player.getInventory().delete(85, 1);
				player.setDonor(PlayerRanks.DonorRights.DONOR);
				player.getPacketSender().sendRights();
				player.sendMessage("Thank you for your donation, you are now a donor.");
				break;
			case 275:
				if (player.getDonor() == PlayerRanks.DonorRights.DELUXE_DONOR
						|| player.getDonor() == PlayerRanks.DonorRights.SPONSOR) {
					player.sendMessage("You cannot claim this rank.");
					return;
				}
				player.getInventory().delete(275, 1);
				player.setDonor(PlayerRanks.DonorRights.DELUXE_DONOR);
				player.getPacketSender().sendRights();
				player.sendMessage("Thank you for your donation, you are now a dexlue donor.");
				break;
			case 293:
				if (player.getDonor() == PlayerRanks.DonorRights.SPONSOR) {
					player.sendMessage("You cannot claim this rank.");
					return;
				}
				player.getInventory().delete(293, 1);
				player.setDonor(PlayerRanks.DonorRights.SPONSOR);
				player.getPacketSender().sendRights();
				player.sendMessage("Thank you for your donation, you are now a sponsor.");
				break;
			case 6199:
				if (player.getInventory().getFreeSlots() <= 0) {
					player.sendMessage("You need 1 open space to receive your reward.");
					return;
				}

				NormalMysteryBox.giveReward(player);
				break;
			case 15501:
				if (player.getInventory().getFreeSlots() <= 0) {
					player.sendMessage("You need 1 open space to receive your reward.");
					return;
				}
				MegaMysteryBox.giveReward(player);
				break;
			case 15507:
				if (player.getInventory().getFreeSlots() <= 0) {
					player.sendMessage("You need 1 open space to receive your reward.");
					return;
				}
				MyticalBox.giveReward(player);
				break;
			case 15505:
				if (player.getInventory().getFreeSlots() <= 0) {
					player.sendMessage("You need 1 open space to receive your reward.");
					return;
				}

				PetMysteryBox.giveReward(player);
				break;
			case 11882:
				if (player.getInventory().getFreeSlots() <= 3) {
					player.sendMessage("You need 4 open spaces to open this box.");
					return;
				}

				player.getInventory().delete(11882, 1);
				player.getInventory().add(2595, 1);
				player.getInventory().add(2591, 1);
				player.getInventory().add(3473, 1);
				player.getInventory().add(2597, 1);
				player.getInventory().refreshItems();
				break;
			case 11884:
				if (player.getInventory().getFreeSlots() <= 3) {
					player.sendMessage("You need 4 open spaces to open this box.");
					return;
				}

				player.getInventory().delete(11884, 1);
				player.getInventory().add(2595, 1);
				player.getInventory().add(2591, 1);
				player.getInventory().add(2593, 1);
				player.getInventory().add(2597, 1);
				player.getInventory().refreshItems();
				break;
			case 11906:
				if (player.getInventory().getFreeSlots() <= 2) {
					player.sendMessage("You need 3 open spaces to open this box.");
					return;
				}

				player.getInventory().delete(11906, 1);
				player.getInventory().add(7394, 1);
				player.getInventory().add(7390, 1);
				player.getInventory().add(7386, 1);
				player.getInventory().refreshItems();
				break;
			case 15262:
				if (!player.getClickDelay().elapsed(1000))
					return;
				player.getInventory().delete(15262, 1);
				player.getInventory().add(18016, 10000).refreshItems();
				player.getClickDelay().reset();
				break;
			case 6:
				DwarfMultiCannon.setupCannon(player);
				break;
		}
	}

	@SuppressWarnings("unused")
	public static void secondAction(Player player, Packet packet) {
		int interfaceId = packet.readLEShortA();
		int slot = packet.readLEShort();
		int itemId = packet.readShortA();
		if (slot < 0 || slot > player.getInventory().capacity())
			return;
		if (player.getInventory().getItems()[slot].getId() != itemId)
			return;
		if (SummoningData.isPouch(player, itemId, 2))
			return;
		switch (itemId) {
			case 6500:
				if (player.getCombatBuilder().isAttacking() || player.getCombatBuilder().isBeingAttacked()) {
					player.getPacketSender().sendMessage("You cannot configure this right now.");
					return;
				}
				player.getPacketSender().sendInterfaceRemoval();
				DialogueManager.start(player, 101);
				player.setDialogueActionId(60);
				break;
			case 1712:
			case 1710:
			case 1708:
			case 1706:
			case 11118:
			case 11120:
			case 11122:
			case 11124:
				JewelryTeleporting.rub(player, itemId);
				break;
			case 3278:
				player.getInventory().delete(3278, 1);
				player.getInventory().add(3277, 1);
				player.getInventory().add(3281, 1);
				player.getPacketSender().sendMessage("You remove the scope from your sniper.");
				break;
			case 3280:
				player.getInventory().delete(3280, 1);
				player.getInventory().add(3279, 1);
				player.getInventory().add(3283, 1);
				player.getPacketSender().sendMessage("You remove the suppressor from your submachine gun.");
				break;
			case 3135:
				player.getInventory().delete(3135, 1);
				player.getInventory().add(3092, 1);
				player.getInventory().add(3283, 1);
				player.getPacketSender().sendMessage("You remove the suppressor from your Desert Eagle.");
				break;
			case 3082:
				player.getInventory().delete(3082, 1);
				player.getInventory().add(3081, 1);
				player.getInventory().add(3282, 1);
				player.getPacketSender().sendMessage("You remove the grenade launcher from your AK-47.");
				break;
			case 15078:
				Morphing.rub(player, itemId);
				break;
			case 1704:
				player.getPacketSender().sendMessage("Your amulet has run out of charges.");
				break;
			case 11126:
				player.getPacketSender().sendMessage("Your bracelet has run out of charges.");
				break;
			case 13281:
			case 13282:
			case 13283:
			case 13284:
			case 13285:
			case 13286:
			case 13287:
			case 13288:
				player.getSlayer().handleSlayerRingTP(itemId);
				break;
			case 5509:
			case 5510:
			case 5512:
				RunecraftingPouches.check(player, RunecraftingPouch.forId(itemId));
				break;
			case 995:
				MoneyPouch.depositMoney(player, player.getInventory().getAmount(995), false);
				break;
			case MoneyPouch.BUCKS_ID:
				MoneyPouch.depositMoney(player, player.getInventory().getAmount(MoneyPouch.BUCKS_ID), true);
				break;
			case 1438:
			case 1448:
			case 1440:
			case 1442:
			case 1444:
			case 1446:
			case 1454:
			case 1452:
			case 1462:
			case 1458:
			case 1456:
			case 1450:
				Runecrafting.handleTalisman(player, itemId);
				break;
		}
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		System.out.println(packet.getOpcode());
		switch (packet.getOpcode()) {
			case SECOND_ITEM_ACTION_OPCODE:
				secondAction(player, packet);
				break;
			case FIRST_ITEM_ACTION_OPCODE:
				firstAction(player, packet);
				break;
			case THIRD_ITEM_ACTION_OPCODE:
				thirdClickAction(player, packet);
				break;
		}
	}

	@SuppressWarnings("unused")
	public void thirdClickAction(Player player, Packet packet) {
		int itemId = packet.readShortA();
		int slot = packet.readLEShortA();
		int interfaceId = packet.readLEShortA();
		if (slot < 0 || slot > player.getInventory().capacity())
			return;
		if (player.getInventory().getItems()[slot].getId() != itemId)
			return;
		if (JarData.forJar(itemId) != null) {
			PuroPuro.lootJar(player, new Item(itemId, 1), JarData.forJar(itemId));
			return;
		}
		if (SummoningData.isPouch(player, itemId, 3)) {
			return;
		}
		if (ItemBinding.isBindable(itemId)) {
			ItemBinding.bindItem(player, itemId);
			return;
		}
		switch (itemId) {
			case 596:
			case 2572:
				double mod = NPCDrops.dropModification(player);
				mod = mod - 1;
				double val = mod * 100;
				player.getPacketSender()
						.sendMessage("Your drop rate is currently increased by " + String.format("%.1f", val) + "%");
				break;
			case 20054:
				player.setNpcTransformationId(9463);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				player.getPacketSender().sendInterfaceRemoval();
				player.setMorpted(9463);
				player.sendMessage("<col=ff0000>You transformed into a " + NpcDefinition.forId(9463).getName() + ".");
				break;
			case 14019:
			case 14022:
				player.setCurrentCape(itemId);
				int[] colors = itemId == 14019 ? player.getMaxCapeColors() : player.getCompCapeColors();
				String[] join = new String[colors.length];
				for (int i = 0; i < colors.length; i++) {
					join[i] = Integer.toString(colors[i]);
				}
				player.getPacketSender().sendString(60000, "[CUSTOMIZATION]" + itemId + "," + String.join(",", join));
				player.getPacketSender().sendInterface(60000);
				break;
			case 19670:
				if (player.busy()) {
					player.getPacketSender().sendMessage("You can not do this right now.");
					return;
				}
				player.setDialogueActionId(71);
				DialogueManager.start(player, player.getGameMode() == GameMode.NORMAL ? 108 : 109);
				break;
			case 6500:
				CharmingImp.sendConfig(player);
				break;
			case 4155:
				player.getPacketSender().sendInterfaceRemoval();
				DialogueManager.start(player, 103);
				player.setDialogueActionId(65);
				break;
			case 13281:
			case 13282:
			case 13283:
			case 13284:
			case 13285:
			case 13286:
			case 13287:
			case 13288:
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendMessage(player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK
						? ("You do not have a Slayer task.")
						: ("Your current task is to kill another " + (player.getSlayer().getAmountToSlay()) + " "
						+ Misc.formatText(
						player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))
						+ "s."));
				break;
			case 6570:
				if (player.getInventory().contains(6570) && player.getInventory().getAmount(6529) >= 50000) {
					player.getInventory().delete(6570, 1).delete(6529, 50000).add(19111, 1);
					player.getPacketSender().sendMessage("You have upgraded your Fire cape into a TokHaar-Kal cape!");
				} else {
					player.getPacketSender().sendMessage(
							"You need at least 50.000 Tokkul to upgrade your Fire Cape into a TokHaar-Kal cape.");
				}
				break;
			case 15262:
				if (!player.getClickDelay().elapsed(1300))
					return;
				int amt = player.getInventory().getAmount(15262);
				if (amt > 0)
					player.getInventory().delete(15262, amt).add(18016, 10000 * amt);
				player.getClickDelay().reset();
				break;
			case 5509:
			case 5510:
			case 5512:
				RunecraftingPouches.empty(player, RunecraftingPouch.forId(itemId));
				break;
			case 21084: // DFS
			case 21083:
			case 21082:
				player.getPacketSender()
						.sendMessage("Your Dragonfire shield has " + player.getDfsCharges() + "/20 dragon-fire charges.");
				break;
		}
	}

}