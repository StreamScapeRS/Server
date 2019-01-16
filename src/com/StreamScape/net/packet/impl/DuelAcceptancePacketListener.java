package com.StreamScape.net.packet.impl;

import com.StreamScape.model.Locations.Location;
import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.world.World;
import com.StreamScape.world.content.gambling.GamblingManager;
import com.StreamScape.world.entity.impl.player.Player;

public class DuelAcceptancePacketListener implements PacketListener {

	public static final int OPCODE = 128;

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		player.getSkillManager().stopSkilling();
		int index = packet.getOpcode() == OPCODE ? (packet.readShort() & 0xFF) : packet.readLEShort();
		if (index > World.getPlayers().capacity())
			return;
		Player target = World.getPlayers().get(index);
		if (target == null)
			return;
		if (player.getLocation() == Location.GAMBLING_ZONE) {
			GamblingManager.acceptInvitation(player, target);
			return;
		}
		if (target.getIndex() != player.getIndex())
			player.getDueling().challengePlayer(target);
	}
}
