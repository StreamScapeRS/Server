package com.StreamScape.world.content.skill.impl.slayer;

import com.StreamScape.model.Item;
import com.StreamScape.model.Locations;
import com.StreamScape.model.Position;
import com.StreamScape.model.Skill;
import com.StreamScape.model.container.impl.Shop.ShopManager;
import com.StreamScape.model.definitions.NpcDefinition;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.content.Achievements;
import com.StreamScape.world.content.Achievements.AchievementData;
import com.StreamScape.world.content.PlayerPanel;
import com.StreamScape.world.content.dialogue.DialogueManager;
import com.StreamScape.world.content.teleportation.TeleportHandler;
import com.StreamScape.world.entity.impl.npc.NPC;
import com.StreamScape.world.entity.impl.player.Player;

public class Slayer {

	public static boolean checkDuoSlayer(Player p, boolean login) {
		if (p.getSlayer().getDuoPartner() == null) {
			return false;
		}
		Player partner = World.getPlayerByName(p.getSlayer().getDuoPartner());
		if (partner == null) {
			return false;
		}
		if (partner.getSlayer().getDuoPartner() == null
				|| !partner.getSlayer().getDuoPartner().equals(p.getUsername())) {
			resetDuo(p, null);
			return false;
		}
		if (partner.getSlayer().getSlayerMaster() != p.getSlayer().getSlayerMaster()) {
			resetDuo(p, partner);
			return false;
		}
		if (login) {
			p.getSlayer().setSlayerTask(partner.getSlayer().getSlayerTask());
			p.getSlayer().setAmountToSlay(partner.getSlayer().getAmountToSlay());
		}
		return true;
	}

	public static boolean handleRewardsInterface(Player player, int button) {
		if (player.getInterfaceId() == 36000) {
			switch (button) {
			case -29534:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case -29522:
				if (player.getPointsManager().getPoints("slayer") < 10) {
					player.getPacketSender().sendMessage("You do not have 10 Slayer points.");
					return true;
				}
				player.getPointsManager().refreshPanel();
				player.getPointsManager().decreasePoints("slayer", 10);
				player.getSkillManager().addExperience(Skill.SLAYER, 10000);
				player.getPacketSender().sendMessage("You've bought 10000 Slayer XP for 10 Slayer points.");
				break;
			case -29519:
				if (player.getPointsManager().getPoints("slayer") < 300) {
					player.getPacketSender().sendMessage("You do not have 300 Slayer points.");
					return true;
				}
				if (player.getSlayer().doubleSlayerXP) {
					player.getPacketSender().sendMessage("You already have this buff.");
					return true;
				}
				player.getPointsManager().decreasePoints("slayer", 300);
				player.getSlayer().doubleSlayerXP = true;
				player.getPointsManager().refreshPanel();
				player.getPacketSender().sendMessage("You will now permanently receive double Slayer experience.");
				break;
			case -29531:
				ShopManager.getShops().get(47).open(player);
				break;
			}
			player.getPacketSender().sendString(36030,
					"Current Points:   " + player.getPointsManager().getPoints("slayer"));
			return true;
		}
		return false;
	}

	public static void resetDuo(Player player, Player partner) {
		if (partner != null) {
			if (partner.getSlayer().getDuoPartner() != null
					&& partner.getSlayer().getDuoPartner().equals(player.getUsername())) {
				partner.getSlayer().setDuoPartner(null);
				partner.getPacketSender().sendMessage("Your Slayer duo team has been disbanded.");
				PlayerPanel.refreshPanel(partner);
			}
		}
		player.getSlayer().setDuoPartner(null);
		player.getPacketSender().sendMessage("Your Slayer duo team has been disbanded.");
		PlayerPanel.refreshPanel(player);
	}

	private Player player;
	private SlayerTasks slayerTask = SlayerTasks.NO_TASK, lastTask = SlayerTasks.NO_TASK;
	private SlayerMaster slayerMaster = SlayerMaster.VANNAKA;

	private int amountToSlay, taskStreak;

	private String duoPartner, duoInvitation;

	public boolean doubleSlayerXP = false;

	public Slayer(Player p) {
		this.player = p;
	}

	public boolean assignDuoSlayerTask() {
		player.getPacketSender().sendInterfaceRemoval();
		if (player.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
			player.getPacketSender().sendMessage("You already have a Slayer task.");
			return false;
		}
		Player partner = World.getPlayerByName(duoPartner);
		if (partner == null) {
			player.getPacketSender().sendMessage("");
			player.getPacketSender().sendMessage("You can only get a new Slayer task when your duo partner is online.");
			return false;
		}
		if (partner.getSlayer().getDuoPartner() == null
				|| !partner.getSlayer().getDuoPartner().equals(player.getUsername())) {
			resetDuo(player, null);
			return false;
		}
		if (partner.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
			player.getPacketSender().sendMessage("Your partner already has a Slayer task.");
			return false;
		}
		if (partner.getSlayer().getSlayerMaster() != player.getSlayer().getSlayerMaster()) {
			player.getPacketSender().sendMessage("You and your partner need to have the same Slayer master.");
			return false;
		}
		if (partner.getInterfaceId() > 0) {
			player.getPacketSender().sendMessage("Your partner must close all their open interfaces.");
			return false;
		}
		return true;
	}

	public void assignTask() {
		boolean hasTask = getSlayerTask() != SlayerTasks.NO_TASK && player.getSlayer().getLastTask() != getSlayerTask();
		boolean duoSlayer = duoPartner != null;
		if (duoSlayer && !player.getSlayer().assignDuoSlayerTask())
			return;
		if (hasTask) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		int[] taskData = SlayerTasks.getNewTaskData(slayerMaster);
		int slayerTaskId = taskData[0], slayerTaskAmount = taskData[1];
		SlayerTasks taskToSet = SlayerTasks.forId(slayerTaskId);

		if (taskToSet == null) {
			player.sendMessage("lol");
			return;
		}
		if (taskToSet == player.getSlayer().getLastTask() || NpcDefinition.forId(taskToSet.getNpcId())
				.getSlayerLevel() > player.getSkillManager().getMaxLevel(Skill.SLAYER)) {
			assignTask();
			return;
		}
		player.getPacketSender().sendInterfaceRemoval();
		this.amountToSlay = slayerTaskAmount;
		this.slayerTask = taskToSet;
		DialogueManager.start(player, SlayerDialogues.receivedTask(player, getSlayerMaster(), getSlayerTask()));
		PlayerPanel.refreshPanel(player);
		if (duoSlayer) {
			Player duo = World.getPlayerByName(duoPartner);
			duo.getSlayer().setSlayerTask(taskToSet);
			duo.getSlayer().setAmountToSlay(slayerTaskAmount);
			duo.getPacketSender().sendInterfaceRemoval();
			DialogueManager.start(duo, SlayerDialogues.receivedTask(duo, slayerMaster, taskToSet));
			PlayerPanel.refreshPanel(duo);
		}
	}

	public int getAmountToSlay() {
		return this.amountToSlay;
	}

	public String getDuoPartner() {
		return duoPartner;
	}

	public SlayerTasks getLastTask() {
		return this.lastTask;
	}

	public SlayerMaster getSlayerMaster() {
		return slayerMaster;
	}

	public SlayerTasks getSlayerTask() {
		return slayerTask;
	}

	public int getTaskStreak() {
		return this.taskStreak;
	}

	@SuppressWarnings("incomplete-switch")
	public void givePoints(SlayerMaster master) {
		int pointsReceived = 4;
		switch (master) {
		case DURADEL:
			pointsReceived = 7;
			break;
		case KURADEL:
			pointsReceived = 10;
			break;
		case SUMONA:
			pointsReceived = 16;
			break;
		}

		int per5 = pointsReceived * 3;
		int per10 = pointsReceived * 5;

		if (player.getSlayer().getTaskStreak() == 5) {
			player.getPointsManager().setWithIncrease("slayer", per5);
			player.getPacketSender().sendMessage("You received " + per5 + " Slayer points.");

		} else if (player.getSlayer().getTaskStreak() == 10) {
			player.getPointsManager().setWithIncrease("slayer", per10);
			player.getPacketSender()
					.sendMessage("You received " + per10 + " Slayer points and your Task Streak has been reset.");
			player.getSlayer().setTaskStreak(0);

		} else if (player.getSlayer().getTaskStreak() >= 0 && player.getSlayer().getTaskStreak() < 5
				|| player.getSlayer().getTaskStreak() >= 6 && player.getSlayer().getTaskStreak() < 10) {
			player.getPointsManager().setWithIncrease("slayer", pointsReceived);
			player.getPacketSender().sendMessage("You received " + pointsReceived + " Slayer points.");
		}
		player.getPointsManager().refreshPanel();
	}

	public void handleInvitation(boolean accept) {
		if (duoInvitation != null) {
			Player inviteOwner = World.getPlayerByName(duoInvitation);
			if (inviteOwner != null) {
				if (accept) {
					if (duoPartner != null) {
						player.getPacketSender().sendMessage("You already have a Slayer duo partner.");
						inviteOwner.getPacketSender()
								.sendMessage("" + player.getUsername() + " already has a Slayer duo partner.");
						return;
					}
					inviteOwner.getPacketSender()
							.sendMessage("" + player.getUsername() + " has joined your duo Slayer team.")
							.sendMessage("Seek respective Slayer master for a task.");
					inviteOwner.getSlayer().setDuoPartner(player.getUsername());
					PlayerPanel.refreshPanel(inviteOwner);
					player.getPacketSender()
							.sendMessage("You have joined " + inviteOwner.getUsername() + "'s duo Slayer team.");
					player.getSlayer().setDuoPartner(inviteOwner.getUsername());
					PlayerPanel.refreshPanel(player);
				} else {
					player.getPacketSender().sendMessage("You've declined the invitation.");
					inviteOwner.getPacketSender()
							.sendMessage("" + player.getUsername() + " has declined your invitation.");
				}
			} else
				player.getPacketSender().sendMessage("Failed to handle the invitation.");
		}
	}

	public void handleSlayerRingTP(int itemId) {
		if (!player.getClickDelay().elapsed(4500))
			return;
		if (player.getMovementQueue().isLockMovement())
			return;
		SlayerTasks task = getSlayerTask();
		if (task == SlayerTasks.NO_TASK)
			return;
		Position slayerTaskPos = new Position(task.getTaskPosition().getX(), task.getTaskPosition().getY(),
				task.getTaskPosition().getZ());
		if (!TeleportHandler.checkReqs(player, slayerTaskPos))
			return;
		TeleportHandler.teleportPlayer(player, slayerTaskPos, player.getSpellbook().getTeleportType());
		Item slayerRing = new Item(itemId);
		player.getInventory().delete(slayerRing);
		if (slayerRing.getId() < 13288)
			player.getInventory().add(slayerRing.getId() + 1, 1);
		else
			player.getPacketSender().sendMessage("Your Ring of Slaying crumbles to dust.");
	}

	public void handleSlayerTaskDeath(boolean giveXp) {
		int xp = slayerTask.getXP() + Misc.getRandom(slayerTask.getXP() / 5);

		if (amountToSlay > 1) {
			amountToSlay--;
		} else {
			player.getPacketSender().sendMessage("")
					.sendMessage("You've completed your Slayer task! Return to a Slayer master for another one.");
			taskStreak++;
			lastTask = slayerTask;
			slayerTask = SlayerTasks.NO_TASK;
			amountToSlay = 0;
			Achievements.doProgress(player, AchievementData.COMPLETE_SLAYER_TASK);
			Achievements.doProgress(player, AchievementData.COMPLETE_15_SLAYER);
			givePoints(slayerMaster);
		}

		if (giveXp) {
			player.getSkillManager().addExperience(Skill.SLAYER, doubleSlayerXP ? xp * 2 : xp);
		}

		PlayerPanel.refreshPanel(player);
	}

	public void killedNpc(NPC npc) {
		if (slayerTask != SlayerTasks.NO_TASK) {
			if (slayerTask.getNpcId() == npc.getId()) {
				handleSlayerTaskDeath(true);
				if (duoPartner != null) {
					Player duo = World.getPlayerByName(duoPartner);
					if (duo != null) {
						if (checkDuoSlayer(player, false)) {
							duo.getSlayer().handleSlayerTaskDeath(
									Locations.goodDistance(player.getPosition(), duo.getPosition(), 20));
						} else {
							resetDuo(player, duo);
						}
					}
				}
			}
		}
	}

	public void resetSlayerTask() {
		SlayerTasks task = getSlayerTask();
		if (task == SlayerTasks.NO_TASK)
			return;
		this.slayerTask = SlayerTasks.NO_TASK;
		this.amountToSlay = 0;
		this.taskStreak = 0;
		player.getPointsManager().decreasePoints("slayer", 5);
		PlayerPanel.refreshPanel(player);
		Player duo = duoPartner == null ? null : World.getPlayerByName(duoPartner);
		if (duo != null) {
			duo.getSlayer().setSlayerTask(SlayerTasks.NO_TASK).setAmountToSlay(0).setTaskStreak(0);
			duo.getPacketSender()
					.sendMessage("Your partner exchanged 5 Slayer points to reset your team's Slayer task.");
			PlayerPanel.refreshPanel(duo);
			player.getPacketSender().sendMessage("You've successfully reset your team's Slayer task.");
		} else {
			player.getPacketSender().sendMessage("Your Slayer task has been reset.");
		}
	}

	public Slayer setAmountToSlay(int amountToSlay) {
		this.amountToSlay = amountToSlay;
		return this;
	}

	public void setDuoInvitation(String player) {
		this.duoInvitation = player;
	}

	public Slayer setDuoPartner(String duoPartner) {
		this.duoPartner = duoPartner;
		return this;
	}

	public void setLastTask(SlayerTasks lastTask) {
		this.lastTask = lastTask;
	}

	public void setSlayerMaster(SlayerMaster master) {
		this.slayerMaster = master;
	}

	public Slayer setSlayerTask(SlayerTasks slayerTask) {
		this.slayerTask = slayerTask;
		return this;
	}

	public Slayer setTaskStreak(int taskStreak) {
		this.taskStreak = taskStreak;
		return this;
	}
}
