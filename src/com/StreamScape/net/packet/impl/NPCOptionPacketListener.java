package com.StreamScape.net.packet.impl;

import com.StreamScape.engine.task.impl.WalkToTask;
import com.StreamScape.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.StreamScape.model.Graphic;
import com.StreamScape.model.Locations.Location;
import com.StreamScape.model.PlayerRanks.DonorRights;
import com.StreamScape.model.PlayerRanks.PlayerRights;
import com.StreamScape.model.Position;
import com.StreamScape.model.Skill;
import com.StreamScape.model.container.impl.Shop.ShopManager;
import com.StreamScape.model.definitions.NpcDefinition;
import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.world.World;
import com.StreamScape.world.content.EnergyHandler;
import com.StreamScape.world.content.LoyaltyProgram;
import com.StreamScape.world.content.combat.CombatFactory;
import com.StreamScape.world.content.combat.magic.CombatSpell;
import com.StreamScape.world.content.combat.magic.CombatSpells;
import com.StreamScape.world.content.combat.weapon.CombatSpecial;
import com.StreamScape.world.content.dialogue.DialogueManager;
import com.StreamScape.world.content.skill.impl.crafting.Tanning;
import com.StreamScape.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.StreamScape.world.content.skill.impl.fishing.Fishing;
import com.StreamScape.world.content.skill.impl.hunter.PuroPuro;
import com.StreamScape.world.content.skill.impl.runecrafting.DesoSpan;
import com.StreamScape.world.content.skill.impl.slayer.SlayerDialogues;
import com.StreamScape.world.content.skill.impl.slayer.SlayerTasks;
import com.StreamScape.world.content.skill.impl.summoning.BossPets;
import com.StreamScape.world.content.skill.impl.summoning.Summoning;
import com.StreamScape.world.content.skill.impl.summoning.SummoningData;
import com.StreamScape.world.content.teleportation.TeleportHandler;
import com.StreamScape.world.entity.impl.npc.CustomPet;
import com.StreamScape.world.entity.impl.npc.NPC;
import com.StreamScape.world.entity.impl.player.Player;

public class NPCOptionPacketListener implements PacketListener {

	public static final int ATTACK_NPC = 72, FIRST_CLICK_OPCODE = 155, MAGE_NPC = 131, SECOND_CLICK_OPCODE = 17,
			THIRD_CLICK_OPCODE = 21, FOURTH_CLICK_OPCODE = 18;

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.isTeleporting() || player.isPlayerLocked() || player.getMovementQueue().isLockMovement())
			return;
		switch (packet.getOpcode()) {
			case ATTACK_NPC:
				attackNPC(player, packet);
				break;
			case FIRST_CLICK_OPCODE:
				firstClick(player, packet);
				break;
			case SECOND_CLICK_OPCODE:
				handleSecondClick(player, packet);
				break;
			case THIRD_CLICK_OPCODE:
				handleThirdClick(player, packet);
				break;
			case FOURTH_CLICK_OPCODE:
				handleFourthClick(player, packet);
				break;
			case MAGE_NPC:
				int npcIndex = packet.readLEShortA();
				int spellId = packet.readShortA();

				if (npcIndex < 0 || spellId < 0 || npcIndex > World.getNpcs().capacity()) {
					return;
				}

				NPC n = World.getNpcs().get(npcIndex);
				player.setEntityInteraction(n);

				CombatSpell spell = CombatSpells.getSpell(spellId);

				if (n == null || spell == null) {
					player.getMovementQueue().reset();
					return;
				}

				if (!NpcDefinition.getDefinitions()[n.getId()].isAttackable()) {
					player.getMovementQueue().reset();
					return;
				}

				if (n.getConstitution() <= 0) {
					player.getMovementQueue().reset();
					return;
				}

				player.setPositionToFace(n.getPosition());
				player.setCastSpell(spell);
				if (player.getCombatBuilder().getStrategy() == null) {
					player.getCombatBuilder().determineStrategy();
				}
				if (CombatFactory.checkAttackDistance(player, n)) {
					player.getMovementQueue().reset();
				}
				player.getCombatBuilder().resetCooldown();
				player.getCombatBuilder().attack(n);
				break;
		}
	}

	private static void attackNPC(Player player, Packet packet) {
		int index = packet.readShortA();
		if (index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC interact = World.getNpcs().get(index);
		if (interact == null)
			return;
		if (interact.isSummoningNpc() && CustomPet.isCustomNpcPet(interact)) {
			CustomPet.pickUp(player, interact);
			player.getPacketSender().sendMessage("You pick up your pet.");
			return;
		}
		if (player.getRights().isDeveloper())
			player.sendMessage("NPC ID: " + interact.getId());

		if (!NpcDefinition.getDefinitions()[interact.getId()].isAttackable()) {
			return;
		}

		if (interact.getConstitution() <= 0) {
			player.getMovementQueue().reset();
			return;
		}

		if (player.getCombatBuilder().getStrategy() == null) {
			player.getCombatBuilder().determineStrategy();
		}
		if (CombatFactory.checkAttackDistance(player, interact)) {
			player.getMovementQueue().reset();
		}

		player.getCombatBuilder().attack(interact);
	}

	private static void firstClick(Player player, Packet packet) {
		int index = packet.readLEShort();
		if (index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC npc = World.getNpcs().get(index);
		if (npc == null)
			return;
		player.setEntityInteraction(npc);
		if (player.getRights() == PlayerRights.DEVELOPER)
			player.getPacketSender().sendMessage("First click npc id: " + npc.getId());
		if (BossPets.pickup(player, npc)) {
			player.getMovementQueue().reset();
			return;
		}
		player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				if (SummoningData.beastOfBurden(npc.getId())) {
					Summoning summoning = player.getSummoning();
					if (summoning.getBeastOfBurden() != null && summoning.getFamiliar() != null
							&& summoning.getFamiliar().getSummonNpc() != null
							&& summoning.getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
						summoning.store();
						player.getMovementQueue().reset();
					} else {
						player.getPacketSender().sendMessage("That familiar is not yours!");
					}
					return;
				}
				switch (npc.getId()) {
					case 705:
						ShopManager.getShops().get(4).open(player);
						break;
					case 457:
						DialogueManager.start(player, 117);
						player.setDialogueActionId(74);
						break;
					case 8710:
					case 8707:
					case 8706:
					case 8705:
						EnergyHandler.rest(player);
						break;
					case 947:
					case 11226:
						if (Dungeoneering.doingDungeoneering(player)) {
							ShopManager.getShops().get(45).open(player);
						}
						break;
					case 9713:
						player.setDialogueActionId(69);
						break;
					case 2622:
						ShopManager.getShops().get(43).open(player);
						break;
					case 3101:
						DialogueManager.start(player, 90);
						player.setDialogueActionId(57);
						break;
					case 1597:
					case 8275:
					case 9085:
					case 7780:
						if (npc.getId() != player.getSlayer().getSlayerMaster().getNpcId()) {
							player.getPacketSender().sendMessage("This is not your current Slayer master.");
							return;
						}
						DialogueManager.start(player, SlayerDialogues.dialogue(player));
						break;
					case 437:
						DialogueManager.start(player, 99);
						player.setDialogueActionId(58);
						break;
					case 5112:
						ShopManager.getShops().get(38).open(player);
						break;
					case 8591:
						// player.nomadQuest[0] = player.nomadQuest[1] =
						// player.nomadQuest[2] = false;
						if (!player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(0)) {
							DialogueManager.start(player, 48);
							player.setDialogueActionId(23);
						} else if (player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(0)
								&& !player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1)) {
							DialogueManager.start(player, 50);
							player.setDialogueActionId(24);
						} else if (player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1))
							DialogueManager.start(player, 53);
						break;
					case 3385:
						if (player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0) && player
								.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() < 6) {
							DialogueManager.start(player, 39);
							return;
						}
						if (player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
							DialogueManager.start(player, 46);
							return;
						}
						DialogueManager.start(player, 38);
						player.setDialogueActionId(20);
						break;
					case 6139:
						DialogueManager.start(player, 30);
						player.setDialogueActionId(17);
						break;
					case 3789:
						player.getPacketSender().sendInterface(18730);
						player.getPacketSender().sendString(18729,
								"Commendations: " + Integer.toString(player.getPointsManager().getPoints("pc")));
						break;
					case 650:
						ShopManager.getShops().get(35).open(player);
						break;
					case 6055:
					case 6056:
					case 6057:
					case 6058:
					case 6059:
					case 6060:
					case 6061:
					case 6062:
					case 6063:
					case 6064:
					case 7903:
						if (npc.getId() == 7903 && player.getLocation() == Location.MEMBER_ZONE) {
							if (player.getDonor() != DonorRights.SPONSOR) {
								player.getPacketSender().sendMessage("You must be at least a Sponsor to use this.");
								return;
							}
						}
						PuroPuro.catchImpling(player, npc);
						break;
					case 8022:
					case 8028:
						DesoSpan.siphon(player, npc);
						break;
					case 2579:
						player.setDialogueActionId(13);
						DialogueManager.start(player, 24);
						break;
					case 8725:
						player.setDialogueActionId(10);
						DialogueManager.start(player, 19);
						break;
					case 4249:
						player.setDialogueActionId(9);
						DialogueManager.start(player, 64);
						break;
					case 6807:
					case 6994:
					case 6995:
					case 6867:
					case 6868:
					case 6794:
					case 6795:
					case 6815:
					case 6816:
					case 6874:
					case 6873:
					case 3594:
					case 3590:
					case 3596:
						if (player.getSummoning().getFamiliar() == null
								|| player.getSummoning().getFamiliar().getSummonNpc() == null
								|| player.getSummoning().getFamiliar().getSummonNpc().getIndex() != npc.getIndex()) {
							player.getPacketSender().sendMessage("That is not your familiar.");
							return;
						}
						player.getSummoning().store();
						break;
					case 605:
						player.setDialogueActionId(8);
						DialogueManager.start(player, 13);
						break;
					case 6970:
						player.setDialogueActionId(3);
						DialogueManager.start(player, 3);
						break;
					case 318:
					case 316:
					case 313:
					case 312:
						player.setEntityInteraction(npc);
						Fishing.setupFishing(player, Fishing.forSpot(npc.getId(), false));
						break;
					case 805:
						ShopManager.getShops().get(34).open(player);
						break;
					case 462:
						ShopManager.getShops().get(33).open(player);
						break;
					case 461:
						ShopManager.getShops().get(32).open(player);
						break;
					case 8444:
						if (player.getDonor() != DonorRights.DONOR && player.getDonor() != DonorRights.DELUXE_DONOR
								&& player.getDonor() != DonorRights.SPONSOR) {
							player.getPacketSender().sendMessage("You must be a donor to access this store.");
							return;
						}
						ShopManager.getShops().get(31).open(player);
						break;
					case 8459:
						ShopManager.getShops().get(30).open(player);
						break;
					case 3299:
						ShopManager.getShops().get(21).open(player);
						break;
					case 548:
						ShopManager.getShops().get(20).open(player);
						break;
					case 1685:
						ShopManager.getShops().get(19).open(player);
					case 4657:
						ShopManager.getShops().get(48).open(player);
						player.getPacketSender()
								.sendMessage("@red@You currently have @bla@"
										+ player.getPointsManager().getPoints("donation") + " @red@donation points.")
								.sendMessage("You can buy donation points by doing ::donate.");
						break;
					case 308:
						ShopManager.getShops().get(18).open(player);
						break;
					case 802:
						ShopManager.getShops().get(17).open(player);
						break;
					case 278:
						ShopManager.getShops().get(16).open(player);
						break;
					case 4946:
						ShopManager.getShops().get(15).open(player);
						break;
					case 948:
						ShopManager.getShops().get(13).open(player);
						break;
					case 4906:
						ShopManager.getShops().get(14).open(player);
						break;
					case 520:
					case 521:
						ShopManager.getShops().get(12).open(player);
						break;
					case 2292:
						ShopManager.getShops().get(11).open(player);
						break;
					case 2676:
						player.getPacketSender().sendInterface(3559);
						player.getAppearance().setCanChangeAppearance(true);
						break;
					case 494:
					case 1360:
						player.getBank(player.getCurrentBankTab()).open();
						break;
				}
				if (!(npc.getId() >= 8705 && npc.getId() <= 8710)) {
					npc.setPositionToFace(player.getPosition());
				}
				player.setPositionToFace(npc.getPosition());
			}
		}));
	}

	public void handleFourthClick(Player player, Packet packet) {
		int index = packet.readLEShort();
		if (index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC npc = World.getNpcs().get(index);
		if (npc == null)
			return;
		player.setEntityInteraction(npc);
		if (player.getRights() == PlayerRights.DEVELOPER)
			player.getPacketSender().sendMessage("Fourth click npc id: " + npc.getId());
		player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				switch (npc.getId()) {
					case 1861:
						ShopManager.getShops().get(2).open(player);
						break;
					case 705:
						ShopManager.getShops().get(7).open(player);
						break;
					case 2253:
						ShopManager.getShops().get(8).open(player);
						break;
					case 1597:
					case 9085:
					case 8275:
					case 7780:
						player.getPacketSender().sendString(36030,
								"Current Points:   " + player.getPointsManager().getPoints("slayer"));
						player.getPacketSender().sendInterface(36000);
						break;
				}
				npc.setPositionToFace(player.getPosition());
				player.setPositionToFace(npc.getPosition());
			}
		}));
	}

	public void handleSecondClick(Player player, Packet packet) {
		int index = packet.readLEShortA();
		if (index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC npc = World.getNpcs().get(index);
		if (npc == null)
			return;
		player.setEntityInteraction(npc);
		final int npcId = npc.getId();
		if (player.getRights() == PlayerRights.DEVELOPER)
			player.getPacketSender().sendMessage("Second click npc id: " + npcId);
		player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				switch (npc.getId()) {
					case 705:
						ShopManager.getShops().get(5).open(player);
						break;
					case 2579:
						ShopManager.getShops().get(46).open(player);
						player.getPacketSender().sendMessage("@blu@You currently have @bla@"
								+ player.getPointsManager().getPoints("prestige") + " @blu@Prestige points!");
						break;
					case 457:
						player.getPacketSender().sendMessage("The ghost teleports you away.");
						player.getPacketSender().sendInterfaceRemoval();
						player.moveTo(new Position(3651, 3486));
						break;
					case 511:
						ShopManager.getShops().get(29).open(player);
						player.getPacketSender().sendMessage("<col=255>You currently have "
								+ player.getPointsManager().getPoints("StreamScape") + " StreamScape points!");
						break;
					case 2622:
						ShopManager.getShops().get(43).open(player);
						break;
					case 462:
						npc.performAnimation(CombatSpells.CONFUSE.getSpell().castAnimation().get());
						npc.forceChat("Off you go!");
						TeleportHandler.teleportPlayer(player, new Position(2911, 4832),
								player.getSpellbook().getTeleportType());
						break;
					case 3101:
						DialogueManager.start(player, 95);
						player.setDialogueActionId(57);
						break;
					case 7969:
						ShopManager.getShops().get(28).open(player);
						break;
					case 605:
						player.getPacketSender()
								.sendMessage("@blu@You currently have @bla@" + player.getPointsManager().getPoints("voting")
										+ " @blu@Voting points.")
								.sendMessage(
										"@blu@You can earn points and coins by voting. To do so, simply use the ::vote command.");
						;
						ShopManager.getShops().get(27).open(player);
						break;
					case 1597:
					case 9085:
					case 7780:
						if (npc.getId() != player.getSlayer().getSlayerMaster().getNpcId()) {
							player.getPacketSender().sendMessage("This is not your current Slayer master.");
							return;
						}
						if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK)
							player.getSlayer().assignTask();
						else
							DialogueManager.start(player, SlayerDialogues.findAssignment(player));
						break;
					case 8591:
						if (!player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1)) {
							player.getPacketSender()
									.sendMessage("You must complete Nomad's quest before being able to use this shop.");
							return;
						}
						ShopManager.getShops().get(37).open(player);
						break;
					case 805:
						Tanning.selectionInterface(player);
						break;
					case 318:
					case 316:
					case 313:
					case 312:
						player.setEntityInteraction(npc);
						Fishing.setupFishing(player, Fishing.forSpot(npc.getId(), true));
						break;
					case 4946:
						ShopManager.getShops().get(15).open(player);
						break;
					case 946:
						ShopManager.getShops().get(1).open(player);
						break;
					case 961:
						ShopManager.getShops().get(6).open(player);
						break;
					case 1861:
						ShopManager.getShops().get(3).open(player);
						break;

					case 2253:
						ShopManager.getShops().get(9).open(player);
						break;
					case 6970:
						player.setDialogueActionId(35);
						DialogueManager.start(player, 63);
						break;
				}
				npc.setPositionToFace(player.getPosition());
				player.setPositionToFace(npc.getPosition());
			}
		}));
	}

	public void handleThirdClick(Player player, Packet packet) {
		int index = packet.readShort();
		if (index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC npc = World.getNpcs().get(index);
		if (npc == null)
			return;
		player.setEntityInteraction(npc).setPositionToFace(npc.getPosition().copy());
		npc.setPositionToFace(player.getPosition());
		if (player.getRights() == PlayerRights.DEVELOPER)
			player.getPacketSender().sendMessage("Third click npc id: " + npc.getId());
		player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				switch (npc.getId()) {
					case 3101:
						ShopManager.getShops().get(42).open(player);
						break;
					case 1597:
					case 8275:
					case 9085:
					case 7780:
						ShopManager.getShops().get(40).open(player);
						break;
					case 605:
						LoyaltyProgram.open(player);
						break;
					case 946:
						ShopManager.getShops().get(0).open(player);
						break;
					case 1861:
						ShopManager.getShops().get(48).open(player);
						break;
					case 961:
						if (player.getDonor() == DonorRights.NONE) {
							player.getPacketSender().sendMessage("This feature is currently only available for donors.");
							return;
						}
						boolean restore = player.getSpecialPercentage() < 100;
						if (restore) {
							player.setSpecialPercentage(100);
							CombatSpecial.updateBar(player);
							player.getPacketSender().sendMessage("Your special attack energy has been restored.");
						}
						for (Skill skill : Skill.values()) {
							if (player.getSkillManager().getCurrentLevel(skill) < player.getSkillManager()
									.getMaxLevel(skill)) {
								player.getSkillManager().setCurrentLevel(skill,
										player.getSkillManager().getMaxLevel(skill));
								restore = true;
							}
						}
						if (restore) {
							player.performGraphic(new Graphic(1302));
							player.getPacketSender().sendMessage("Your stats have been restored.");
						} else
							player.getPacketSender().sendMessage("Your stats do not need to be restored at the moment.");
						break;
					case 705:
						ShopManager.getShops().get(49).open(player);
						break;
					case 2253:
						ShopManager.getShops().get(10).open(player);
						break;
				}
				npc.setPositionToFace(player.getPosition());
				player.setPositionToFace(npc.getPosition());
			}
		}));
	}
}
