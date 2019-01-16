package com.StreamScape.net.packet.impl;

import com.StreamScape.model.PlayerRanks.PlayerRights;
import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.world.World;
import com.StreamScape.world.entity.impl.player.Player;

//CALLED EVERY 3 MINUTES OF INACTIVITY

public class IdleLogoutPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
				|| player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.DEVELOPER)
			return;

		if (player.logout() && (player.getCurrentTask() == null || !player.isRunning())) {
			World.getPlayers().remove(player);
		}

		player.setInactive(true);
	}
}
