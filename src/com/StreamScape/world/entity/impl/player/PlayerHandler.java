package com.StreamScape.world.entity.impl.player;

import com.StreamScape.GameServer;
import com.StreamScape.GameSettings;
import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.engine.task.impl.BonusExperienceTask;
import com.StreamScape.engine.task.impl.CombatSkullEffect;
import com.StreamScape.engine.task.impl.FireImmunityTask;
import com.StreamScape.engine.task.impl.OverloadPotionTask;
import com.StreamScape.engine.task.impl.PlayerSkillsTask;
import com.StreamScape.engine.task.impl.PlayerSpecialAmountTask;
import com.StreamScape.engine.task.impl.PrayerRenewalPotionTask;
import com.StreamScape.engine.task.impl.StaffOfLightSpecialAttackTask;
import com.StreamScape.model.Flag;
import com.StreamScape.model.Locations;
import com.StreamScape.model.PlayerRanks.PlayerRights;
import com.StreamScape.model.Skill;
import com.StreamScape.model.container.impl.Bank;
import com.StreamScape.model.container.impl.Equipment;
import com.StreamScape.model.definitions.WeaponAnimations;
import com.StreamScape.model.definitions.WeaponInterfaces;
import com.StreamScape.net.PlayerSession;
import com.StreamScape.net.SessionState;
import com.StreamScape.net.security.ConnectionHandler;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.content.Achievements;
import com.StreamScape.world.content.BonusManager;
import com.StreamScape.world.content.Lottery;
import com.StreamScape.world.content.PlayerLogs;
import com.StreamScape.world.content.PlayerPanel;
import com.StreamScape.world.content.PlayersOnline;
import com.StreamScape.world.content.StaffList;
import com.StreamScape.world.content.StartScreen;
import com.StreamScape.world.content.clan.ClanChatManager;
import com.StreamScape.world.content.combat.effect.CombatPoisonEffect;
import com.StreamScape.world.content.combat.effect.CombatTeleblockEffect;
import com.StreamScape.world.content.combat.magic.Autocasting;
import com.StreamScape.world.content.combat.prayer.CurseHandler;
import com.StreamScape.world.content.combat.prayer.PrayerHandler;
import com.StreamScape.world.content.combat.pvp.BountyHunter;
import com.StreamScape.world.content.combat.range.DwarfMultiCannon;
import com.StreamScape.world.content.combat.weapon.CombatSpecial;
import com.StreamScape.world.content.skill.impl.hunter.Hunter;
import com.StreamScape.world.content.skill.impl.slayer.Slayer;
import com.StreamScape.world.content.teleportation.TeleportManager;

import mysql.impl.Hiscores;
import mysql.impl.Login;

public class PlayerHandler {

	public static void handleLogin(Player player) {
		// Register the player
		System.out.println("[World] Registering player - [username, host] : [" + player.getUsername() + ", "
				+ player.getHostAddress() + "]");
		ConnectionHandler.add(player.getHostAddress());
		World.getPlayers().add(player);
		World.updatePlayersOnline();
		PlayersOnline.add(player);
		player.getSession().setState(SessionState.LOGGED_IN);

		// Packets
		player.getPacketSender().sendMapRegion().sendDetails();

		player.getRecordedLogin().reset();

		player.getPacketSender().sendTabs();

		// Setting up the player's item containers..
		for (int i = 0; i < player.getBanks().length; i++) {
			if (player.getBank(i) == null) {
				player.setBank(i, new Bank(player));
			}
		}
		player.getInventory().refreshItems();
		player.getEquipment().refreshItems();

		// Weapons and equipment..
		WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
		WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
		CombatSpecial.updateBar(player);
		BonusManager.update(player);

		// Skills
		player.getSummoning().login();
		player.getFarming().load();
		Slayer.checkDuoSlayer(player, true);
		for (Skill skill : Skill.values()) {
			player.getSkillManager().updateSkill(skill);
		}

		// Relations
		player.getRelations().setPrivateMessageId(1).onLogin(player).updateLists(true);

		// Client configurations
		player.getPacketSender().sendConfig(172, player.isAutoRetaliate() ? 1 : 0)
				.sendTotalXp(player.getSkillManager().getTotalGainedExp())
				.sendConfig(player.getFightType().getParentId(), player.getFightType().getChildId()).sendRunStatus()
				.sendRunEnergy(player.getRunEnergy()).sendString(8135, "" + player.getMoneyInPouch())
				.sendInteractionOption("Follow", 3, false).sendInteractionOption("Trade With", 4, false)
				.sendInterfaceRemoval().sendString(39161, "@red@Server time: @bla@" + Misc.getCurrentServerTime() + "");

		Autocasting.onLogin(player);
		PrayerHandler.deactivateAll(player);
		CurseHandler.deactivateAll(player);
		BonusManager.sendCurseBonuses(player);
		Achievements.updateInterface(player);
		// Tasks
		TaskManager.submit(new PlayerSkillsTask(player));
		if (player.isPoisoned()) {
			TaskManager.submit(new CombatPoisonEffect(player));
		}
		if (player.getPrayerRenewalPotionTimer() > 0) {
			TaskManager.submit(new PrayerRenewalPotionTask(player));
		}
		if (player.getOverloadPotionTimer() > 0) {
			TaskManager.submit(new OverloadPotionTask(player));
		}
		if (player.getTeleblockTimer() > 0) {
			TaskManager.submit(new CombatTeleblockEffect(player));
		}
		if (player.getSkullTimer() > 0) {
			player.setSkullIcon(1);
			TaskManager.submit(new CombatSkullEffect(player));
		}
		if (player.getFireImmunity() > 0) {
			FireImmunityTask.makeImmune(player, player.getFireImmunity(), player.getFireDamageModifier());
		}
		if (player.getSpecialPercentage() < 100) {
			TaskManager.submit(new PlayerSpecialAmountTask(player));
		}
		if (player.hasStaffOfLightEffect()) {
			TaskManager.submit(new StaffOfLightSpecialAttackTask(player));
		}
		if (player.getMinutesBonusExp() >= 0) {
			TaskManager.submit(new BonusExperienceTask(player));
		}

		// Update appearance
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		if (player.isCrushVial()) {
			player.getPacketSender().sendConfig(5009, 1);
		} else {
			player.getPacketSender().sendConfig(5009, 0);
		}
		// Others
		Lottery.onLogin(player);
		Locations.login(player);
		player.getPacketSender().sendMessage("Welcome to StreamScape!");
		if (player.experienceLocked())
			player.getPacketSender().sendMessage("@red@Warning: your experience is currently locked.");
		ClanChatManager.handleLogin(player);

		if (GameSettings.BONUS_EXP) {
			player.getPacketSender()
					.sendMessage("@blu@It is currently double exp weekend on StreamScape, take advantage of it!");
		}
		//Login.login();
		PlayerPanel.refreshPanel(player);
		// New player
		if (player.newPlayer()) {
			StartScreen.open(player);
			player.setPlayerLocked(true);
		}

		player.getPacketSender().updateSpecialAttackOrb().sendIronmanMode(player.getGameMode().ordinal());

		if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.TRIAL_MODERATOR
				|| player.getRights() == PlayerRights.GLOBAL_MODERATOR
				|| player.getRights() == PlayerRights.ADMINISTRATOR
				|| player.getRights() == PlayerRights.GLOBAL_ADMINISTRATOR)
			World.sendMessage("<img=15><col=6600CC> " + Misc.formatText(player.getRights().toString().toLowerCase())
					+ " " + player.getUsername() + " has just logged in, feel free to message them for support.");

		if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
				|| player.getRights() == PlayerRights.DEVELOPER
				|| player.getRights() == PlayerRights.FORUM_ADMINISTRATOR
				|| player.getRights() == PlayerRights.FORUM_MODERATOR
				|| player.getRights() == PlayerRights.GLOBAL_ADMINISTRATOR
				|| player.getRights() == PlayerRights.GLOBAL_MODERATOR || player.getRights() == PlayerRights.OWNER
				|| player.getRights() == PlayerRights.TRIAL_FORUM_MODERATOR
				|| player.getRights() == PlayerRights.TRIAL_MODERATOR
				|| player.getRights() == PlayerRights.WEB_DEVELOPER || player.getRights() == PlayerRights.YOUTUBER
				|| player.getRights() == PlayerRights.GFX_ARTIST || player.getRights() == PlayerRights.ITEM_MODELLER) {
			StaffList.login(player);
		}
		StaffList.updateGlobalInterface();
		ClanChatManager.join(player, "StreamScape");
		if (player.getPointsManager().getPoints("achievement") == 0) {
			Achievements.setPoints(player);
		}
		PlayerLogs.log(player.getUsername(),
				"Login from host " + player.getHostAddress() + ", serial number: " + player.getSerialNumber());
	}

	public static boolean handleLogout(Player player) {
		try {

			PlayerSession session = player.getSession();

			if (session.getChannel().isOpen()) {
				session.getChannel().close();
			}

			if (!player.isRegistered()) {
				return true;
			}

			boolean exception = GameServer.isUpdating()
					|| World.getLogoutQueue().contains(player) && player.getLogoutTimer().elapsed(90000);
			if (player.logout() || exception) {
				System.out.println("[World] Deregistering player - [username, host] : [" + player.getUsername() + ", "
						+ player.getHostAddress() + "]");
				player.getSession().setState(SessionState.LOGGING_OUT);
				ConnectionHandler.remove(player.getHostAddress());
				player.setTotalPlayTime(player.getTotalPlayTime() + player.getRecordedLogin().elapsed());
				player.getPacketSender().sendInterfaceRemoval();
				if (player.getCannon() != null) {
					DwarfMultiCannon.pickupCannon(player, player.getCannon(), true);
				}
				if (exception && player.getResetPosition() != null) {
					player.moveTo(player.getResetPosition());
					player.setResetPosition(null);
				}
				if (player.getRegionInstance() != null) {
					player.getRegionInstance().destruct();
				}
				StaffList.logout(player);
				StaffList.updateGlobalInterface();
				Hunter.handleLogout(player);
				Locations.logout(player);
				//World.minigameHandler.handleLogout(player);
				//player.getSummoning().unsummon(false, false);
				player.getFarming().save();
				new Hiscores(player).run();
				BountyHunter.handleLogout(player);
				ClanChatManager.leave(player, false);
				player.getRelations().updateLists(false);
				PlayersOnline.remove(player);
				TaskManager.cancelTasks(player.getCombatBuilder());
				TaskManager.cancelTasks(player);
				player.save();
				World.getPlayers().remove(player);
				session.setState(SessionState.LOGGED_OUT);
				World.updatePlayersOnline();
				//Login.login();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
