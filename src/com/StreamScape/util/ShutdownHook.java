package com.StreamScape.util;

import java.util.logging.Logger;

import com.StreamScape.GameServer;
import com.StreamScape.world.World;
import com.StreamScape.world.content.WellOfGoodwill;
import com.StreamScape.world.content.clan.ClanChatManager;
import com.StreamScape.world.entity.impl.player.Player;
import com.StreamScape.world.entity.impl.player.PlayerHandler;

public class ShutdownHook extends Thread {

	/**
	 * The ShutdownHook logger to print out information.
	 */
	private static final Logger logger = Logger.getLogger(ShutdownHook.class.getName());

	@Override
	public void run() {
		logger.info("The shutdown hook is processing all required actions...");
		// World.savePlayers();
		GameServer.setUpdating(true);
		for (Player player : World.getPlayers()) {
			if (player != null) {
				// World.deregister(player);
				PlayerHandler.handleLogout(player);
			}
		}
		WellOfGoodwill.save();
		ClanChatManager.save();
		logger.info("The shudown hook actions have been completed, shutting the server down...");
	}
}
