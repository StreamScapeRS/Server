package com.StreamScape.net.packet.impl;

import com.StreamScape.model.definitions.NpcDefinition;
import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.world.content.MonsterDrops;
import com.StreamScape.world.entity.impl.player.Player;

public class ExamineNpcPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int npc = packet.readShort();
		if (npc <= 0) {
			return;
		}
		NpcDefinition npcDef = NpcDefinition.forId(npc);
		if (npcDef != null) {
			MonsterDrops.sendNpcDrop(player, npcDef.getId(), npcDef.getName().toLowerCase());
		}
	}
}
