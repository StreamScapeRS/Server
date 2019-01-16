package com.StreamScape.world.entity;

import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.model.GameObject;
import com.StreamScape.net.PlayerSession;
import com.StreamScape.net.SessionState;
import com.StreamScape.world.World;
import com.StreamScape.world.clip.region.RegionClipping;
import com.StreamScape.world.content.CustomObjects;
import com.StreamScape.world.content.gambling.GamblingManager;
import com.StreamScape.world.content.gambling.GamblingManager.GambleStage;
import com.StreamScape.world.entity.impl.npc.NPC;
import com.StreamScape.world.entity.impl.player.Player;

public class EntityHandler {

	public static void deregister(Entity entity) {
		if (entity.isPlayer()) {
			Player player = (Player) entity;
			if (!player.getGambling().getStage().equals(GambleStage.OFFLINE)) {
				GamblingManager.autoDisconnectWin(player);
			}
			World.getPlayers().remove(player);
		} else if (entity.isNpc()) {
			NPC npc = (NPC) entity;
			TaskManager.cancelTasks(npc.getCombatBuilder());
			TaskManager.cancelTasks(entity);
			World.getNpcs().remove(npc);
		} else if (entity.isGameObject()) {
			GameObject gameObject = (GameObject) entity;
			RegionClipping.removeObject(gameObject);
			CustomObjects.deleteGlobalObjectWithinDistance(gameObject);
		}
	}

	public static void register(Entity entity) {
		if (entity.isPlayer()) {
			Player player = (Player) entity;
			PlayerSession session = player.getSession();
			if (session.getState() == SessionState.LOGGING_IN && !World.getLoginQueue().contains(player)) {
				World.getLoginQueue().add(player);
			}
		}
		if (entity.isNpc()) {
			NPC npc = (NPC) entity;
			World.getNpcs().add(npc);
		} else if (entity.isGameObject()) {
			GameObject gameObject = (GameObject) entity;
			RegionClipping.addObject(gameObject);
			CustomObjects.spawnGlobalObjectWithinDistance(gameObject);
		}
	}
}
