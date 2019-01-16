package com.StreamScape.engine.task.impl;

import com.StreamScape.engine.task.Task;
import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.model.Animation;
import com.StreamScape.model.Locations.Location;
import com.StreamScape.world.World;
import com.StreamScape.world.content.Achievements;
import com.StreamScape.world.content.Achievements.AchievementData;
import com.StreamScape.world.content.combat.strategy.impl.Ichigo;
import com.StreamScape.world.entity.impl.npc.NPC;
import com.StreamScape.world.entity.impl.player.Player;

/**
 * Represents an npc's death task, which handles everything an npc does before
 * and after their death animation (including it), such as dropping their drop
 * table items.
 * 
 * @author relex lawl
 */

public class NPCDeathTask extends Task {

	/**
	 * The npc setting off the death task.
	 */
	private final NPC npc;

	/**
	 * The amount of ticks on the task.
	 */
	private int ticks = 2;

	/**
	 * The player who killed the NPC
	 */
	private Player killer = null;

	/**
	 * The NPCDeathTask constructor.
	 * 
	 * @param npc
	 *            The npc being killed.
	 */

	public NPCDeathTask(NPC npc) {
		super(2);
		this.npc = npc;
		this.ticks = 2;
	}

	@Override
	public void execute() {
		try {
			npc.setEntityInteraction(null);
			switch (ticks) {
			case 2:
				npc.getMovementQueue().setLockMovement(true).reset();
				killer = npc.getCombatBuilder().getKiller(true);
				if (!(npc.getId() >= 6142 && npc.getId() <= 6145) && !(npc.getId() > 5070 && npc.getId() < 5081))
					npc.performAnimation(new Animation(npc.getDefinition().getDeathAnimation()));

				/** CUSTOM NPC DEATHS **/
				if (npc.getId() == 13447) {
				}

				break;
			case 0:
				if (killer != null) {

					int points = 0;
					if (npc.getId() == 508) {
						points = 10;
					}
					if (npc.getId() == 509) {
						points = 30;
					}
					if (points > 0) {
						killer.getPointsManager().setWithIncrease("StreamScape", points);
						killer.getPointsManager().refreshPanel();
					}
					Achievements.doProgress(killer, AchievementData.KILL_10K);
					if (npc.getId() == 3491) {
						Achievements.doProgress(killer, AchievementData.DEFEAT_CULINAROMANCER);
					} else if (npc.getId() == 8528) {
						Achievements.doProgress(killer, AchievementData.DEFEATNOMAD);
					} else if (npc.getId() == 510) {
						Achievements.doProgress(killer, AchievementData.KILL_PurgatoryS);
					} else if (npc.getId() == 508) {
						Achievements.doProgress(killer, AchievementData.KILL_MEWTWO);
					} else if (npc.getId() == 509) {
						Achievements.doProgress(killer, AchievementData.KILL_CHARMELEONS);
					}

					switch (npc.getId()) {
					case 502:
						Achievements.doProgress(killer, AchievementData.KILL50AMERICANS);
						break;
					case 503:
						Achievements.doProgress(killer, AchievementData.KILL50OEROS);
						break;
					case 504:
						Achievements.doProgress(killer, AchievementData.KILL50SKYS);
						break;
					case 505:
						Achievements.doProgress(killer, AchievementData.KILL50DARTHMAULS);
						break;
					case 506:
						Achievements.doProgress(killer, AchievementData.KILL50CASH);
						break;
					case 507:
						Achievements.doProgress(killer, AchievementData.KILL50SILVER);
						break;
					}

					/** LOCATION KILLS **/
					if (npc.getLocation().handleKilledNPC(killer, npc)) {
						stop();
						return;
					}

					if (World.minigameHandler.handleNpcDeath(npc)) {
						stop();
						return;
					}

					/** PARSE DROPS **/
					com.StreamScape.drops.NPCDrops.handleDrops(killer, npc);
					/** SLAYER **/
					killer.getSlayer().killedNpc(npc);
				}
				stop();
				break;
			}
			ticks--;
		} catch (Exception e) {
			e.printStackTrace();
			stop();
		}
	}

	@Override
	public void stop() {
		setEventRunning(false);

		npc.setDying(false);

		if (npc.getDefinition().getRespawnTime() > 0 && npc.getLocation() != Location.GRAVEYARD && npc.getLocation() != Location.DUNGEONEERING) {
			if (npc.getDefinition().getRespawns())
				TaskManager.submit(new NPCRespawnTask(npc, npc.getDefinition().getRespawnTime()));
		}
		if (npc.getId() == 666 || npc.getId() == 669) {
			Ichigo.death(npc.getId(), npc.getPosition());
		}

		World.deregister(npc);

	}
}