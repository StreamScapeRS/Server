package com.StreamScape.net.packet.impl;

import java.util.Arrays;
import java.util.List;

import com.StreamScape.GameSettings;
import com.StreamScape.drops.DropLog;
import com.StreamScape.engine.task.Task;
import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.engine.task.impl.ForceMovementTask;
import com.StreamScape.model.Animation;
import com.StreamScape.model.ForceMovement;
import com.StreamScape.model.Graphic;
import com.StreamScape.model.Item;
import com.StreamScape.model.Locations.Location;
import com.StreamScape.model.PlayerRanks.DonorRights;
import com.StreamScape.model.PlayerRanks.PlayerRights;
import com.StreamScape.model.Position;
import com.StreamScape.model.Skill;
import com.StreamScape.model.container.impl.Bank;
import com.StreamScape.model.container.impl.Bank.BankSearchAttributes;
import com.StreamScape.model.definitions.WeaponInterfaces.WeaponInterface;
import com.StreamScape.model.input.Input;
import com.StreamScape.model.input.impl.EnterClanChatToJoin;
import com.StreamScape.model.input.impl.EnterSyntaxToBankSearchFor;
import com.StreamScape.model.input.impl.WithdrawBucks;
import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.net.packet.interaction.PacketInteractionManager;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.content.Achievements;
import com.StreamScape.world.content.BankPin;
import com.StreamScape.world.content.BonusManager;
import com.StreamScape.world.content.Censor;
import com.StreamScape.world.content.Consumables;
import com.StreamScape.world.content.Emotes;
import com.StreamScape.world.content.EnergyHandler;
import com.StreamScape.world.content.ExperienceLamps;
import com.StreamScape.world.content.ItemsKeptOnDeath;
import com.StreamScape.world.content.LoyaltyProgram;
import com.StreamScape.world.content.MoneyPouch;
import com.StreamScape.world.content.PlayerPanel;
import com.StreamScape.world.content.PlayersOnline;
import com.StreamScape.world.content.Sounds;
import com.StreamScape.world.content.Sounds.Sound;
import com.StreamScape.world.content.StaffList;
import com.StreamScape.world.content.StartScreen;
import com.StreamScape.world.content.WellOfGoodwill;
import com.StreamScape.world.content.clan.ClanChat;
import com.StreamScape.world.content.clan.ClanChatManager;
import com.StreamScape.world.content.combat.magic.Autocasting;
import com.StreamScape.world.content.combat.magic.MagicSpells;
import com.StreamScape.world.content.combat.magic.lunar.LunarSpellRepo;
import com.StreamScape.world.content.combat.prayer.CurseHandler;
import com.StreamScape.world.content.combat.prayer.PrayerHandler;
import com.StreamScape.world.content.combat.weapon.CombatSpecial;
import com.StreamScape.world.content.combat.weapon.FightType;
import com.StreamScape.world.content.dialogue.DialogueManager;
import com.StreamScape.world.content.dialogue.DialogueOptions;
import com.StreamScape.world.content.minigames.impl.Dueling;
import com.StreamScape.world.content.minigames.impl.Nomad;
import com.StreamScape.world.content.minigames.impl.RecipeForDisaster;
import com.StreamScape.world.content.skill.ChatboxInterfaceSkillAction;
import com.StreamScape.world.content.skill.impl.construction.Construction;
import com.StreamScape.world.content.skill.impl.crafting.LeatherMaking;
import com.StreamScape.world.content.skill.impl.crafting.Tanning;
import com.StreamScape.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.StreamScape.world.content.skill.impl.dungeoneering.DungeoneeringParty;
import com.StreamScape.world.content.skill.impl.dungeoneering.ItemBinding;
import com.StreamScape.world.content.skill.impl.fletching.Fletching;
import com.StreamScape.world.content.skill.impl.slayer.Slayer;
import com.StreamScape.world.content.skill.impl.smithing.SmithingData;
import com.StreamScape.world.content.skill.impl.summoning.PouchMaking;
import com.StreamScape.world.content.skill.impl.summoning.SummoningTab;
import com.StreamScape.world.content.teleportation.TeleportHandler;
import com.StreamScape.world.content.teleportation.TeleportManager;
import com.StreamScape.world.content.teleportation.TeleportSpellHandler;
import com.StreamScape.world.content.teleportation.TeleportType;
import com.StreamScape.world.entity.impl.player.Player;

/**
 * This packet listener manages a button that the player has clicked upon.
 * 
 * @author Gabriel Hannason
 */

public class ButtonClickPacketListener implements PacketListener {

	public static final int OPCODE = 185;

	private boolean checkHandlers(Player player, int id) {
		if (Construction.handleButtonClick(id, player)) {
			return true;
		}

		player.getPacketSender().sendConsoleMessage("id: " + id);

		switch (id) {

		case 29756:
			player.setCrushVial(!player.isCrushVial());
			if (player.isCrushVial()) {
				player.getPacketSender().sendMessage("Your vials will now be crushed when finished.");
			} else {
				player.getPacketSender().sendMessage("Your vials will not be crushed when finished.");
			}
			break;

		case 30056:
			if (player.getSkillManager().getMaxLevel(6) < 68) {
				player.getPacketSender().sendMessage("You need a magic level of atleast 68 to cast this spell.");
				return false;
			}
			Item[] runes_req = new Item[] { new Item(9075, 1), new Item(555, 3), new Item(554, 1) };
			if (!player.getInventory().containsAll(runes_req)) {
				player.getPacketSender().sendMessage("You don't have the required runes to cast this spell");
				return false;
			}
			player.performAnimation(new Animation(6294));
			player.performGraphic(new Graphic(1061));
			int[][] humiData = new int[][] {

					{ 229, 227 }, { 1925, 1929 }, { 1935, 1937 }, { 5331, 5340 }, { 1831, 1823 }, { 1923, 1921 },

			};
			for (int index = 0; index < humiData.length; index++) {
				if (player.getInventory().contains(humiData[index][0])) {
					final int count = player.getInventory().getAmount(humiData[index][0]);
					player.getInventory().delete(new Item(humiData[index][0], count));
					player.getInventory().add(new Item(humiData[index][1], count));
				}
			}
			for (int index = 0; index < runes_req.length; index++) {
				if (runes_req[index] != null)
					player.getInventory().delete(runes_req[index]);
			}
			player.getSkillManager().addExperience(Skill.MAGIC, 65);
			return true;

		case 10091:
			if (player.getSkillManager().getMaxLevel(6) < 71) {
				player.getPacketSender().sendMessage("You need a magic level of atleast 71 to cast this spell.");
				return false;
			}
			Item[] cure_req = new Item[] { new Item(9075, 2), new Item(564, 2), new Item(563, 1) };
			if (!player.getInventory().containsAll(cure_req)) {
				player.getPacketSender().sendMessage("You don't have the required runes to cast this spell");
				return false;
			}
			if (player.getPoisonDamage() <= 0) {
				player.getPacketSender().sendMessage("You can't cast this if you are not poisoned.");
				return false;
			}
			player.performAnimation(new Animation(4411));
			player.performGraphic(new Graphic(742));
			player.setPoisonDamage(0);
			player.getPacketSender().sendMessage("You cure yourself of poison.");
			for (int index = 0; index < cure_req.length; index++) {
				if (cure_req[index] != null)
					player.getInventory().delete(cure_req[index]);
			}
			player.getSkillManager().addExperience(Skill.MAGIC, 69);
			return true;

		case 30099:
			if (player.getSkillManager().getMaxLevel(6) < 71) {
				player.getPacketSender().sendMessage("You need a magic level of atleast 71 to cast this spell.");
				return false;
			}
			Item[] hunter_req = new Item[] { new Item(9075, 2), new Item(557, 2) };
			if (!player.getInventory().containsAll(hunter_req)) {
				player.getPacketSender().sendMessage("You don't have the required runes to cast this spell");
				return false;
			}
			player.performAnimation(new Animation(6303));
			player.performGraphic(new Graphic(1074));
			List<Item> kit_items = Arrays.asList(new Item(10150, 1), new Item(10010, 1), new Item(10006, 1),
					new Item(10031, 1), new Item(10029, 1), new Item(8987, 1), new Item(10008, 1), new Item(11260, 1));
			kit_items.forEach(item -> player.getInventory().add(item));
			player.getPacketSender().sendMessage("You create yourself a hunter kit.");
			for (int index = 0; index < hunter_req.length; index++) {
				if (hunter_req[index] != null)
					player.getInventory().delete(hunter_req[index]);
			}
			player.getSkillManager().addExperience(Skill.MAGIC, 70);
			return true;

		case 30178:
			if (player.getSkillManager().getMaxLevel(6) < 79) {
				player.getPacketSender().sendMessage("You need a magic level of atleast 78 to cast this spell.");
				return false;
			}
			Item[] dream_req = new Item[] { new Item(9075, 2), new Item(564, 2), new Item(559, 5) };
			if (!player.getInventory().containsAll(dream_req)) {
				player.getPacketSender().sendMessage("You don't have the required runes to cast this spell");
				return false;
			}
			for (int index = 0; index < dream_req.length; index++) {
				if (dream_req[index] != null)
					player.getInventory().delete(dream_req[index]);
			}
			player.setCurrentTask(new Task(1) {

				int state = 0;

				@Override
				protected void execute() {
					if (state == 0) {
						player.performAnimation(new Animation(6295));
					} else if (state >= 2 && state <= 10) {
						player.performAnimation(new Animation(6296));
						player.performGraphic(new Graphic(1056));
						final int current_level = player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION);
						player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, current_level + 1, true);
					} else if (state > 10) {
						player.performAnimation(new Animation(6297));
						player.getSkillManager().addExperience(Skill.MAGIC, 82);
						this.stop();
					}
					state++;
				}

			});
			TaskManager.submit(player.getCurrentTask());
			return true;

		case -27985:
			player.getPacketSender().sendInterface(37510);
			return true;
		case -28004:
			player.getPacketSender().sendInterfaceRemoval();
			return true;
		case -28021:
			player.changinPin = 2;
			BankPin.init(player, false);
			return true;
		case -28023:
			player.changinPin = 1;
			BankPin.init(player, false);
			return true;
		case -32304:
			player.getTrading().depositInventory();
			return true;
		case -32303:
			player.getTrading().withdrawTrade();
			return true;
		case -30534:
			player.getPacketSender().sendInterfaceRemoval();
			if ((player.getForceMovement() == null)
					&& (player.getPosition().getY() == 3520 || player.getPosition().getY() == 3521)) {
				player.getMovementQueue().reset();
				player.performAnimation(new Animation(6132));
				final Position crossDitch = new Position(0, player.getPosition().getY() < 3522 ? 3 : -3);
				TaskManager.submit(new ForceMovementTask(player, 3, new ForceMovement(player.getPosition().copy(),
						crossDitch, 33, 60, crossDitch.getY() == 3 ? 0 : 2, 6312)));
			} else {
				if (player.getTeleportType() != null) {
					player.moveTo(player.getTeleportType().getPosition());
				}
			}
			return true;
		case -30533:
			player.setTeleportType(null);
			player.getPacketSender().sendInterfaceRemoval();
			return true;
		case -26365:
			if (player.getDonor().ordinal() >= DonorRights.DELUXE_DONOR.ordinal()) {
				player.getPacketSender().sendMessage(":yellTitle:_" + player.yellTitle);
				player.getPacketSender().sendInterface(40000);
			} else {
				player.getPacketSender().sendMessage("You must be at least a deluxe donor to access this feature.");
			}
			return true;
		case -25522:
			player.getPacketSender().sendInterfaceRemoval();
			break;
		case 2494:
		case 2495:
		case 2496:
		case 2498:
		case 2471:
		case 2472:
		case 2473:
		case 2461:
		case 2462:
		case 2483:
		case 2484:
		case 2485:
			DialogueOptions.handle(player, id);
			return true;
		case 2497:
			DialogueOptions.handle(player, id);
			player.getPacketSender().sendMessage(
					"@blu@You currently have @bla@" + player.getPointsManager().getPoints("pvp") + " @blu@PK points.");
			return true;
		case 2482:
			DialogueOptions.handle(player, id);
			player.getPacketSender()
					.sendMessage("@blu@You currently have @bla@" + player.getPointsManager().getPoints("voting")
							+ " @blu@Voting points.")
					.sendMessage(
							"@blu@You can earn points and coins by voting. To do so, simply use the ::vote command.");
			return true;
		}
		if (TeleportManager.handleButtonInteraction(player, id)) {
			return true;
		}
		if (StartScreen.handleButton(player, id)) {
			return true;
		}
		if (player.isPlayerLocked() && id != 2458 && id != -12780 && id != -12779 && id != -12778 && id != -12767) {
			return true;
		}
		if (Achievements.handleButton(player, id)) {
			return true;
		}
		if (Sounds.handleButton(player, id)) {
			return true;
		}
		if (PrayerHandler.isButton(id)) {
			PrayerHandler.togglePrayerWithActionButton(player, id);
			return true;
		}
		if (CurseHandler.isButton(player, id)) {
			return true;
		}
		if (Autocasting.handleAutocast(player, id)) {
			return true;
		}
		if (SmithingData.handleButtons(player, id)) {
			return true;
		}
		if (PouchMaking.pouchInterface(player, id)) {
			return true;
		}
		if (LoyaltyProgram.handleButton(player, id)) {
			return true;
		}
		if (Fletching.fletchingButton(player, id)) {
			return true;
		}
		if (LeatherMaking.handleButton(player, id) || Tanning.handleButton(player, id)) {
			return true;
		}
		if (Emotes.doEmote(player, id)) {
			return true;
		}
		if (TeleportSpellHandler.handleSpellTeleport(player, id)) {
			return true;
		}
		if (player.getLocation() == Location.DUEL_ARENA && Dueling.handleDuelingButtons(player, id)) {
			return true;
		}
		if (Slayer.handleRewardsInterface(player, id)) {
			return true;
		}
		if (ExperienceLamps.handleButton(player, id)) {
			return true;
		}
		if (PlayersOnline.handleButton(player, id)) {
			return true;
		}
		if (ClanChatManager.handleClanChatSetupButton(player, id)) {
			return true;
		}
		return false;
	}

	@Override
	public void handleMessage(Player player, Packet packet) {

		int id = packet.readShort();

		if (player.getRights() == PlayerRights.DEVELOPER) {
			player.getPacketSender().sendConsoleMessage("Clicked button: " + id);
			player.sendMessage("[ Debug ] Clicked button: " + id);
		}
		if (PacketInteractionManager.checkButtonInteraction(player, id)) {
			return;
		}
		if (checkHandlers(player, id))
			return;
		if (id >= 32410 && id <= 32460) {
			StaffList.handleButton(player, id);
		}

		if (LunarSpellRepo.spellExists(id)) {
			LunarSpellRepo.execute(player, id);
			return;
		}

		switch (id) {
		case -4884:
			player.getPacketSender().sendInterfaceRemoval();
			break;
		case -25520:
			player.setInputHandling(new Input() {
				@Override
				public void handleSyntax(Player player, String text) {
					if (text.length() < 1 || text.length() > 10) {
						player.getPacketSender().sendMessage("Your yell title needs to be within 1 and 10 characters.");
						return;
					}
					if (text.startsWith(" ")) {
						player.getPacketSender()
								.sendMessage("You cannot use space in the beginning of your yell title.");
						return;
					}
					if (Censor.contains(text)) {
						player.getPacketSender().sendMessage("You cannot use illegal words in your title.");
						return;
					}
					player.yellTitle = text;
					player.getPacketSender().sendMessage(":yellTitle:_" + player.yellTitle);
					player.getPacketSender().sendMessage("Your yell title has been set to: " + text);
				}
			});
			player.getPacketSender().sendEnterInputPrompt("What would you like your yell title to be?");
			break;
		case 27656:
			if (player.busy() && !player.getTrading().inTrade()) {
				player.getPacketSender().sendMessage("You cannot do that right now.");
				return;
			}
			player.setInputHandling(new WithdrawBucks());
			player.getPacketSender().sendEnterAmountPrompt("How many bucks would you like to withdraw?");
			break;
		case -27454:
		case -27534:
		case 5384:
			player.getPacketSender().sendInterfaceRemoval();
			break;
		case 1036:
			EnergyHandler.rest(player);
			break;
		case -26376:
			PlayersOnline.showInterface(player);
			break;

		case 32388:
			player.getPacketSender().sendTabInterface(GameSettings.PLAYER_PANAL, 6939);
			break;
		case -26386:
			player.getPacketSender().sendTabInterface(GameSettings.PLAYER_PANAL, 46343);
			StaffList.updateInterface(player);
			break;
		case 27229:
			DungeoneeringParty.create(player);
			break;
		case 26226:
		case 26229:
			if (Dungeoneering.doingDungeoneering(player)) {
				DialogueManager.start(player, 114);
				player.setDialogueActionId(71);
			} else {
				Dungeoneering.leave(player, false, true);
			}
			break;
		case 10003:
			player.getPacketSender().sendInterface(49100);
			break;
		case 26244:
		case 26247:
			if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
				if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername()
						.equals(player.getUsername())) {
					DialogueManager.start(player, id == 26247 ? 106 : 105);
					player.setDialogueActionId(id == 26247 ? 68 : 67);
				} else {
					player.getPacketSender().sendMessage("Only the party owner can change this setting.");
				}
			}
			break;
		case 28180:
			TeleportHandler.teleportPlayer(player, new Position(3450, 3715), player.getSpellbook().getTeleportType());
			break;
		case 28177:
			TeleportHandler.teleportPlayer(player, new Position(2670, 3632), player.getSpellbook().getTeleportType());
			break;
		case 14176:
			player.setUntradeableDropItem(null);
			player.getPacketSender().sendInterfaceRemoval();
			break;
		case 14175:
			player.getPacketSender().sendInterfaceRemoval();
			if (player.getUntradeableDropItem() != null
					&& player.getInventory().contains(player.getUntradeableDropItem().getId())) {
				ItemBinding.unbindItem(player, player.getUntradeableDropItem().getId());
				player.getInventory().delete(player.getUntradeableDropItem().getId(),
						player.getUntradeableDropItem().getAmount());
				player.getPacketSender().sendMessage("Your item vanishes as it hits the floor.");
				Sounds.sendSound(player, Sound.DROP_ITEM);
			}
			player.setUntradeableDropItem(null);
			break;
		case 1013:
			player.getSkillManager().setTotalGainedExp(0);
			break;
		case -26374:
			if (WellOfGoodwill.isActive()) {
				player.getPacketSender()
						.sendMessage("<col=008FB2>The Well of Goodwill is granting 30% bonus experience for another "
								+ WellOfGoodwill.getMinutesRemaining() + " minutes.");
			} else {
				player.getPacketSender()
						.sendMessage("<col=008FB2>The Well of Goodwill needs another "
								+ Misc.insertCommasToNumber("" + WellOfGoodwill.getMissingAmount())
								+ " coins before becoming full.");
			}
			break;
		case -26348:
			player.getPacketSender().sendMessage("This feature is currently disabled.");
			break;
		case -26347:
			DropLog.open(player);
			break;
		case -10531:
			if (player.isKillsTrackerOpen()) {
				player.setKillsTrackerOpen(false);
				player.getPacketSender().sendTabInterface(GameSettings.PLAYER_PANAL, 6939);
				PlayerPanel.refreshPanel(player);
			}
			break;
		/*
		 * Links
		 */
		case -26339:
			player.getPacketSender().sendString(1, "www.StreamScape.com");
			player.getPacketSender().sendMessage("Attempting to open the home page.");
			break;
		case -26338:
			player.getPacketSender().sendString(1, "www.StreamScape.com/forums/");
			player.getPacketSender().sendMessage("Attempting to open the forums.");
			break;
		case -26337:
			player.getPacketSender().sendString(1, "www.StreamScape.com/hiscores/");
			player.getPacketSender().sendMessage("Attempting to open the hiscores.");
			break;
		case -26336:
			player.getPacketSender().sendString(1, "www.StreamScape.com/vote/");
			player.getPacketSender().sendMessage("Attempting to open the vote.");
			break;
		case -26335:
			player.getPacketSender().sendString(1, "www.StreamScape.com/store/");
			player.getPacketSender().sendMessage("Attempting to open the store.");
			break;
		case -26334:
			player.getPacketSender().sendString(1, "http://StreamScape.com/forums/#discord");
			player.getPacketSender().sendMessage("Attempting to open the discord chat section on the forums.");
			break;
		case -26333:
			player.getPacketSender().sendString(1,
					"http://StreamScape.com/forums/index.php?/forum/47-StreamScape-support/");
			player.getPacketSender().sendMessage("Attempting to open the support section on the forums.");
			break;
		case -26332:
			player.getPacketSender().sendString(1, "www.StreamScape.com/forums/index.php?/forum/51-reports/");
			player.getPacketSender().sendMessage("Attempting to open the report section on the forums.");
			break;
		case -26331:
			player.getPacketSender().sendString(1, "www.StreamScape.com/forums/index.php?/forum/15-rules/");
			player.getPacketSender().sendMessage("Attempting to open the rules section on the forums.");
			break;
		case -26286:
			RecipeForDisaster.openQuestLog(player);
			break;
		case -26285:
			Nomad.openQuestLog(player);
			break;
		case 350:
			player.getPacketSender()
					.sendMessage("To autocast a spell, please right-click it and choose the autocast option.")
					.sendTab(GameSettings.MAGIC_TAB).sendConfig(108, player.getAutocastSpell() == null ? 3 : 1);
			break;
		case 12162:
			DialogueManager.start(player, 61);
			player.setDialogueActionId(28);
			break;
		case 29335:
			if (player.getInterfaceId() > 0) {
				player.getPacketSender()
						.sendMessage("Please close the interface you have open before opening another one.");
				return;
			}
			DialogueManager.start(player, 60);
			player.setDialogueActionId(27);
			break;
		case 29455:
			if (player.getInterfaceId() > 0) {
				player.getPacketSender()
						.sendMessage("Please close the interface you have open before opening another one.");
				return;
			}
			ClanChatManager.toggleLootShare(player);
			break;
		case 8658:
			DialogueManager.start(player, 55);
			player.setDialogueActionId(26);
			break;
		case 11001:
		case 1195:
			TeleportHandler.teleportPlayer(player, GameSettings.DEFAULT_POSITION.copy(),
					player.getSpellbook().getTeleportType());
			break;
		case 8667:
			TeleportHandler.teleportPlayer(player, new Position(2742, 3443), player.getSpellbook().getTeleportType());
			break;
		case 8672:
			TeleportHandler.teleportPlayer(player, new Position(2595, 4772), player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage(
					"<img=10> To get started with Runecrafting, buy a talisman and use the locate option on it.");
			break;
		case 8861:
			TeleportHandler.teleportPlayer(player, new Position(2914, 3450), player.getSpellbook().getTeleportType());
			break;
		case 8656:
			player.setDialogueActionId(47);
			DialogueManager.start(player, 86);
			break;
		case 8659:
			TeleportHandler.teleportPlayer(player, new Position(3024, 9741), player.getSpellbook().getTeleportType());
			break;
		case 8664:
			TeleportHandler.teleportPlayer(player, new Position(3063, 2870), player.getSpellbook().getTeleportType());
			break;
		case 8666:
			TeleportHandler.teleportPlayer(player, new Position(3017, 2869), player.getSpellbook().getTeleportType());
			break;
		case 8671:
			player.setDialogueActionId(56);
			DialogueManager.start(player, 89);
			break;
		case 8670:
			TeleportHandler.teleportPlayer(player, new Position(2717, 3499), player.getSpellbook().getTeleportType());
			break;
		case 8668:
			TeleportHandler.teleportPlayer(player, new Position(2709, 3437), player.getSpellbook().getTeleportType());
			break;
		case 8665:
			TeleportHandler.teleportPlayer(player, new Position(3079, 3495), player.getSpellbook().getTeleportType());
			break;
		case 8662:
			TeleportHandler.teleportPlayer(player, new Position(2345, 3698), player.getSpellbook().getTeleportType());
			break;
		case 13928:
			TeleportHandler.teleportPlayer(player, new Position(3052, 3304), player.getSpellbook().getTeleportType());
			break;
		case 28179:
			TeleportHandler.teleportPlayer(player, new Position(2209, 5348), player.getSpellbook().getTeleportType());
			break;
		case 28178:
			DialogueManager.start(player, 54);
			player.setDialogueActionId(25);
			break;
		case 1159: // Bones to Bananas
		case 15877:// Bones to peaches
		case 30306:
			MagicSpells.handleMagicSpells(player, id);
			break;
		case 10001:
			if (player.getInterfaceId() == -1) {
				Consumables.handleHealAction(player);
			} else {
				player.getPacketSender().sendMessage("You cannot heal yourself right now.");
			}
			break;
		case 18025:
			if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
				PrayerHandler.deactivatePrayer(player, PrayerHandler.AUGURY);
			} else {
				PrayerHandler.activatePrayer(player, PrayerHandler.AUGURY);
			}
			break;
		case 18018:
			if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
				PrayerHandler.deactivatePrayer(player, PrayerHandler.RIGOUR);
			} else {
				PrayerHandler.activatePrayer(player, PrayerHandler.RIGOUR);
			}
			break;
		case 10000:
		case 950:
			if (player.getInterfaceId() < 0)
				player.getPacketSender().sendInterface(29740);
			else
				player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
			break;
		case -31304:
		case -32301:
			if (System.currentTimeMillis() - player.getTrading().lastAction <= 600)
				return;
			player.getTrading().lastAction = System.currentTimeMillis();
			if (player.getTrading().inTrade()) {
				player.getTrading().acceptTrade(id == -31304 ? 2 : 1);
			} else {
				player.getPacketSender().sendInterfaceRemoval();
			}
			break;
		case -32300:
		case -31303:
			player.getPacketSender().sendInterfaceRemoval();
			break;

		case 10162:
			player.setActiveBook(null);
			player.getPacketSender().sendInterfaceRemoval();
			player.performAnimation(new Animation(65535));
			break;
		case -18269:
			player.getPacketSender().sendInterfaceRemoval();
			break;
		case 841:
		case -6997:
		case -6993:
			if (player.getActiveBook() == null) {
				return;
			}

			player.getActiveBook().increasePage();
			player.getActiveBook().readBook(player, true);
			break;
		case 839:
		case -6994:
		case -6990:
			if (player.getActiveBook() == null) {
				return;
			}

			player.getActiveBook().decreasePage();
			player.getActiveBook().readBook(player, true);
			break;
		case 14922:
			player.getPacketSender().sendClientRightClickRemoval().sendInterfaceRemoval();
			break;
		case 14921:
			player.getPacketSender().sendMessage("Please visit the forums and ask for help in the support section.");
			break;
		case 5294:
			player.getPacketSender().sendClientRightClickRemoval().sendInterfaceRemoval();
			if (!player.getBankPinAttributes().hasBankPin()) {
				player.setDialogueActionId(player.getBankPinAttributes().hasBankPin() ? 8 : 7);
				DialogueManager.start(player,
						DialogueManager.getDialogues().get(player.getBankPinAttributes().hasBankPin() ? 12 : 9));
			} else {
				player.getPacketSender().sendInterface(37510);
			}
			break;
		case 27653:
			if (!player.busy() && !player.getCombatBuilder().isBeingAttacked()
					&& !Dungeoneering.doingDungeoneering(player)) {
				player.getSkillManager().stopSkilling();
				player.getPriceChecker().open();
			} else {
				player.getPacketSender().sendMessage("You cannot open this right now.");
			}
			break;
		case 2735:
		case 1511:
			if (player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().toInventory();
				player.getPacketSender().sendInterfaceRemoval();
			} else {
				player.getPacketSender().sendMessage("You do not have a familiar who can hold items.");
			}
			break;
		case -11501:
		case -11504:
		case -11498:
		case -11507:
		case 1020:
		case 1021:
		case 1019:
		case 1018:
			if (id == 1020 || id == -11504)
				SummoningTab.renewFamiliar(player);
			else if (id == 1019 || id == -11501)
				SummoningTab.callFollower(player);
			else if (id == 1021 || id == -11498)
				SummoningTab.handleDismiss(player, false);
			else if (id == -11507)
				player.getSummoning().store();
			else if (id == 1018)
				player.getSummoning().toInventory();
			break;
		case 11004:
			player.getPacketSender().sendTab(GameSettings.SKILLS_TAB);
			DialogueManager.sendStatement(player, "Simply press on the skill you want to train in the skills tab.");
			player.setDialogueActionId(-1);
			break;
		case 8654:
		case 8657:
		case 8655:
		case 8663:
		case 8669:
		case 8660:
			TeleportHandler.teleportPlayer(player, new Position(2134, 4926), TeleportType.NORMAL);
			break;
		/*
		 * case 28177: TeleportHandler.teleportPlayer(player, new Position(2524, 4776),
		 * TeleportType.NORMAL); break;
		 */
		case 2799:
		case 2798:
		case 1747:
		case 1748:
		case 8890:
		case 8886:
		case 8875:
		case 8871:
		case 8894:
			ChatboxInterfaceSkillAction.handleChatboxInterfaceButtons(player, id);
			break;
		case -27986:
			player.getPacketSender().sendString(1, "http://StreamScape.com/forums/");
			break;
		case -27987:
		case -27988:
			player.getPacketSender().sendString(1,
					"http://StreamScape.com/forums/index.php?/forum/47-StreamScape-support/");
			break;
		case -27998:
		case -27997:
		case -27996:
		case -27995:
		case -27994:
		case -27993:
		case -27992:
		case -27991:
		case -27990:
		case -27989:
			BankPin.clickedButton(player, id);
			break;
		case 27005:
		case 22012:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				return;
			Bank.depositItems(player, id == 27005 ? player.getEquipment() : player.getInventory(), false);
			break;
		case 27023:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				return;
			if (player.getSummoning().getBeastOfBurden() == null) {
				player.getPacketSender().sendMessage("You do not have a familiar which can hold items.");
				return;
			}
			Bank.depositItems(player, player.getSummoning().getBeastOfBurden(), false);
			break;
		case 22008:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				return;
			player.setNoteWithdrawal(!player.withdrawAsNote());
			break;
		case 22016:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				return;
			player.setSwapMode(false);
			player.getPacketSender().sendConfig(304, 0).sendMessage("This feature is coming soon!");
			// player.setSwapMode(!player.swapMode());
			break;
		case 27009:
			MoneyPouch.toBank(player);
			break;
		case 27014:
		case 27015:
		case 27016:
		case 27017:
		case 27018:
		case 27019:
		case 27020:
		case 27021:
		case 27022:
			if (!player.isBanking())
				return;
			if (player.getBankSearchingAttribtues().isSearchingBank())
				BankSearchAttributes.stopSearch(player, true);
			int bankId = id - 27014;
			boolean empty = bankId > 0 ? Bank.isEmpty(player.getBank(bankId)) : false;
			if (!empty || bankId == 0) {
				player.setCurrentBankTab(bankId);
				player.getPacketSender().sendString(5385, "scrollreset");
				player.getPacketSender().sendString(27002, Integer.toString(player.getCurrentBankTab()));
				player.getPacketSender().sendString(27000, "1");
				player.getBank(bankId).open();
			} else
				player.getPacketSender().sendMessage("To create a new tab, please drag an item here.");
			break;
		case 24830:
			if (!player.isBanking())
				return;
			if (!player.getBankSearchingAttribtues().isSearchingBank()) {
				player.getBankSearchingAttribtues().setSearchingBank(true);
				player.setInputHandling(new EnterSyntaxToBankSearchFor());
				player.getPacketSender().sendEnterInputPrompt("What would you like to search for?");
			} else {
				BankSearchAttributes.stopSearch(player, true);
			}
			break;
		case 22845:
		case 24115:
		case 24010:
		case 24041:
		case 150:
			player.setAutoRetaliate(!player.isAutoRetaliate());
			break;
		case 29332:
			ClanChat clan = player.getCurrentClanChat();
			if (clan == null) {
				player.getPacketSender().sendMessage("You are not in a clanchat channel.");
				return;
			}
			ClanChatManager.leave(player, false);
			player.setClanChatName(null);
			break;
		case 29329:
			if (player.getInterfaceId() > 0) {
				player.getPacketSender()
						.sendMessage("Please close the interface you have open before opening another one.");
				return;
			}
			player.setInputHandling(new EnterClanChatToJoin());
			player.getPacketSender().sendEnterInputPrompt("Enter the name of the clanchat channel you wish to join:");
			break;
		case 19158:
		case 152:
			if (player.getRunEnergy() <= 1) {
				player.getPacketSender().sendMessage("You do not have enough energy to do this.");
				player.setRunning(false);
			} else
				player.setRunning(!player.isRunning());
			player.getPacketSender().sendRunStatus();
			break;
		case -26366:
		case 27658:
			player.setExperienceLocked(!player.experienceLocked());
			String type = player.experienceLocked() ? "locked" : "unlocked";
			player.getPacketSender().sendMessage("Your experience is now " + type + ".");
			PlayerPanel.refreshPanel(player);
			break;
		case 27651:
		case 21341:
			if (player.getInterfaceId() == -1) {
				player.getSkillManager().stopSkilling();
				BonusManager.update(player);
				player.getPacketSender().sendInterface(65234);
			} else
				player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
			break;
		case 27654:
			if (player.getInterfaceId() > 0) {
				player.getPacketSender()
						.sendMessage("Please close the interface you have open before opening another one.");
				return;
			}
			player.getSkillManager().stopSkilling();
			ItemsKeptOnDeath.sendInterface(player);
			break;
		case 2458: // Logout
			if (player.logout()) {
				World.getPlayers().remove(player);
			}
			break;
		case 29138:
		case 29038:
		case 29063:
		case 29113:
		case 29163:
		case 29188:
		case 29213:
		case 29238:
		case 30007:
		case 48023:
		case 33033:
		case 30108:
		case 7473:
		case 7562:
		case 7487:
		case 7788:
		case 8481:
		case 7612:
		case 7587:
		case 7662:
		case 7462:
		case 7548:
		case 7687:
		case 7537:
		case 12322:
		case 7637:
		case 12311:
			CombatSpecial.activate(player);
			break;
		case 1772: // shortbow & longbow
			if (player.getWeapon() == WeaponInterface.SHORTBOW) {
				player.setFightType(FightType.SHORTBOW_ACCURATE);
			} else if (player.getWeapon() == WeaponInterface.LONGBOW) {
				player.setFightType(FightType.LONGBOW_ACCURATE);
			} else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
				player.setFightType(FightType.CROSSBOW_ACCURATE);
			}
			break;
		case 1771:
			if (player.getWeapon() == WeaponInterface.SHORTBOW) {
				player.setFightType(FightType.SHORTBOW_RAPID);
			} else if (player.getWeapon() == WeaponInterface.LONGBOW) {
				player.setFightType(FightType.LONGBOW_RAPID);
			} else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
				player.setFightType(FightType.CROSSBOW_RAPID);
			}
			break;
		case 1770:
			if (player.getWeapon() == WeaponInterface.SHORTBOW) {
				player.setFightType(FightType.SHORTBOW_LONGRANGE);
			} else if (player.getWeapon() == WeaponInterface.LONGBOW) {
				player.setFightType(FightType.LONGBOW_LONGRANGE);
			} else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
				player.setFightType(FightType.CROSSBOW_LONGRANGE);
			}
			break;
		case 2282: // dagger & sword
			if (player.getWeapon() == WeaponInterface.DAGGER) {
				player.setFightType(FightType.DAGGER_STAB);
			} else if (player.getWeapon() == WeaponInterface.SWORD) {
				player.setFightType(FightType.SWORD_STAB);
			}
			break;
		case 2285:
			if (player.getWeapon() == WeaponInterface.DAGGER) {
				player.setFightType(FightType.DAGGER_LUNGE);
			} else if (player.getWeapon() == WeaponInterface.SWORD) {
				player.setFightType(FightType.SWORD_LUNGE);
			}
			break;
		case 2284:
			if (player.getWeapon() == WeaponInterface.DAGGER) {
				player.setFightType(FightType.DAGGER_SLASH);
			} else if (player.getWeapon() == WeaponInterface.SWORD) {
				player.setFightType(FightType.SWORD_SLASH);
			}
			break;
		case 2283:
			if (player.getWeapon() == WeaponInterface.DAGGER) {
				player.setFightType(FightType.DAGGER_BLOCK);
			} else if (player.getWeapon() == WeaponInterface.SWORD) {
				player.setFightType(FightType.SWORD_BLOCK);
			}
			break;
		case 2429: // scimitar & longsword
			if (player.getWeapon() == WeaponInterface.SCIMITAR) {
				player.setFightType(FightType.SCIMITAR_CHOP);
			} else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
				player.setFightType(FightType.LONGSWORD_CHOP);
			}
			break;
		case 2432:
			if (player.getWeapon() == WeaponInterface.SCIMITAR) {
				player.setFightType(FightType.SCIMITAR_SLASH);
			} else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
				player.setFightType(FightType.LONGSWORD_SLASH);
			}
			break;
		case 2431:
			if (player.getWeapon() == WeaponInterface.SCIMITAR) {
				player.setFightType(FightType.SCIMITAR_LUNGE);
			} else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
				player.setFightType(FightType.LONGSWORD_LUNGE);
			}
			break;
		case 2430:
			if (player.getWeapon() == WeaponInterface.SCIMITAR) {
				player.setFightType(FightType.SCIMITAR_BLOCK);
			} else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
				player.setFightType(FightType.LONGSWORD_BLOCK);
			}
			break;
		case 3802: // mace
			player.setFightType(FightType.MACE_POUND);
			break;
		case 3805:
			player.setFightType(FightType.MACE_PUMMEL);
			break;
		case 3804:
			player.setFightType(FightType.MACE_SPIKE);
			break;
		case 3803:
			player.setFightType(FightType.MACE_BLOCK);
			break;
		case 4454: // knife, thrownaxe, dart & javelin
			if (player.getWeapon() == WeaponInterface.KNIFE) {
				player.setFightType(FightType.KNIFE_ACCURATE);
			} else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
				player.setFightType(FightType.THROWNAXE_ACCURATE);
			} else if (player.getWeapon() == WeaponInterface.DART) {
				player.setFightType(FightType.DART_ACCURATE);
			} else if (player.getWeapon() == WeaponInterface.JAVELIN) {
				player.setFightType(FightType.JAVELIN_ACCURATE);
			}
			break;
		case 4453:
			if (player.getWeapon() == WeaponInterface.KNIFE) {
				player.setFightType(FightType.KNIFE_RAPID);
			} else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
				player.setFightType(FightType.THROWNAXE_RAPID);
			} else if (player.getWeapon() == WeaponInterface.DART) {
				player.setFightType(FightType.DART_RAPID);
			} else if (player.getWeapon() == WeaponInterface.JAVELIN) {
				player.setFightType(FightType.JAVELIN_RAPID);
			}
			break;
		case 4452:
			if (player.getWeapon() == WeaponInterface.KNIFE) {
				player.setFightType(FightType.KNIFE_LONGRANGE);
			} else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
				player.setFightType(FightType.THROWNAXE_LONGRANGE);
			} else if (player.getWeapon() == WeaponInterface.DART) {
				player.setFightType(FightType.DART_LONGRANGE);
			} else if (player.getWeapon() == WeaponInterface.JAVELIN) {
				player.setFightType(FightType.JAVELIN_LONGRANGE);
			}
			break;
		case 4685: // spear
			player.setFightType(FightType.SPEAR_LUNGE);
			break;
		case 4688:
			player.setFightType(FightType.SPEAR_SWIPE);
			break;
		case 4687:
			player.setFightType(FightType.SPEAR_POUND);
			break;
		case 4686:
			player.setFightType(FightType.SPEAR_BLOCK);
			break;
		case 4711: // 2h sword
			player.setFightType(FightType.TWOHANDEDSWORD_CHOP);
			break;
		case 4714:
			player.setFightType(FightType.TWOHANDEDSWORD_SLASH);
			break;
		case 4713:
			player.setFightType(FightType.TWOHANDEDSWORD_SMASH);
			break;
		case 4712:
			player.setFightType(FightType.TWOHANDEDSWORD_BLOCK);
			break;
		case 5576: // pickaxe
			player.setFightType(FightType.PICKAXE_SPIKE);
			break;
		case 5579:
			player.setFightType(FightType.PICKAXE_IMPALE);
			break;
		case 5578:
			player.setFightType(FightType.PICKAXE_SMASH);
			break;
		case 5577:
			player.setFightType(FightType.PICKAXE_BLOCK);
			break;
		case 7768: // claws
			player.setFightType(FightType.CLAWS_CHOP);
			break;
		case 3289: // claws
			player.setFightType(FightType.CLAWS_CHOP);
			break;

		case 7771:
			player.setFightType(FightType.CLAWS_SLASH);
			break;
		case 7770:
			player.setFightType(FightType.CLAWS_LUNGE);
			break;
		case 7769:
			player.setFightType(FightType.CLAWS_BLOCK);
			break;
		case 8466: // halberd
			player.setFightType(FightType.HALBERD_JAB);
			break;
		case 8468:
			player.setFightType(FightType.HALBERD_SWIPE);
			break;
		case 8467:
			player.setFightType(FightType.HALBERD_FEND);
			break;
		case 5862: // unarmed
			player.setFightType(FightType.UNARMED_PUNCH);
			break;
		case 5861:
			player.setFightType(FightType.UNARMED_KICK);
			break;
		case 5860:
			player.setFightType(FightType.UNARMED_BLOCK);
			break;
		case 12298: // whip
			player.setFightType(FightType.WHIP_FLICK);
			break;
		case 12297:
			player.setFightType(FightType.WHIP_LASH);
			break;
		case 12296:
			player.setFightType(FightType.WHIP_DEFLECT);
			break;
		case 336: // staff
			player.setFightType(FightType.STAFF_BASH);
			break;
		case 335:
			player.setFightType(FightType.STAFF_POUND);
			break;
		case 334:
			player.setFightType(FightType.STAFF_FOCUS);
			break;
		case 433: // warhammer
			player.setFightType(FightType.WARHAMMER_POUND);
			break;
		case 432:
			player.setFightType(FightType.WARHAMMER_PUMMEL);
			break;
		case 431:
			player.setFightType(FightType.WARHAMMER_BLOCK);
			break;
		case 782: // scythe
			player.setFightType(FightType.SCYTHE_REAP);
			break;
		case 784:
			player.setFightType(FightType.SCYTHE_CHOP);
			break;
		case 785:
			player.setFightType(FightType.SCYTHE_JAB);
			break;
		case 783:
			player.setFightType(FightType.SCYTHE_BLOCK);
			break;
		case 1704: // battle axe
			player.setFightType(FightType.BATTLEAXE_CHOP);
			break;
		case 1707:
			player.setFightType(FightType.BATTLEAXE_HACK);
			break;
		case 1706:
			player.setFightType(FightType.BATTLEAXE_SMASH);
			break;
		case 1705:
			player.setFightType(FightType.BATTLEAXE_BLOCK);
			break;
		}
	}
}
