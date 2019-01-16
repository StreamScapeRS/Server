package com.StreamScape.net.packet.impl;

import com.StreamScape.model.PlayerRanks.PlayerRights;
import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.world.content.clan.ClanChatManager;
import com.StreamScape.world.entity.impl.player.Player;

public class ClickTextMenuPacketListener implements PacketListener {

	public static final int OPCODE = 222;

	@Override
	public void handleMessage(Player player, Packet packet) {

		int interfaceId = packet.readShort();
		int menuId = packet.readByte();

		if (player.getRights() == PlayerRights.DEVELOPER) {
			player.getPacketSender().sendConsoleMessage("Clicked text menu: " + interfaceId + ", menuId: " + menuId);
		}

		if (interfaceId >= 29344 && interfaceId <= 29443) { // Clan chat list
			int index = interfaceId - 29344;
			ClanChatManager.handleMemberOption(player, index, menuId);
		}

	}
}
