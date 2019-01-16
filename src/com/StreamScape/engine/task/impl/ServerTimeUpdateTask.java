package com.StreamScape.engine.task.impl;

import com.StreamScape.engine.task.Task;
import com.StreamScape.model.Locations;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;

/**
 * @author Gabriel Hannason
 */
public class ServerTimeUpdateTask extends Task {

	private int tick = 0;

	public ServerTimeUpdateTask() {
		super(40);
	}

	@Override
	protected void execute() {
		World.updateServerTime();

		if (tick >= 6 && (Locations.PLAYERS_IN_WILD >= 3 || Locations.PLAYERS_IN_DUEL_ARENA >= 3)) {
			if (Locations.PLAYERS_IN_WILD > Locations.PLAYERS_IN_DUEL_ARENA
					|| Misc.getRandom(3) == 1 && Locations.PLAYERS_IN_WILD >= 2) {
				World.sendMessage(
						"@blu@There are currently " + Locations.PLAYERS_IN_WILD + " players roaming the Wilderness!");
			} else if (Locations.PLAYERS_IN_DUEL_ARENA > Locations.PLAYERS_IN_WILD) {
				World.sendMessage(
						"@blu@There are currently " + Locations.PLAYERS_IN_DUEL_ARENA + " players at the Duel Arena!");
			}
			tick = 0;
		}

		tick++;
	}
}