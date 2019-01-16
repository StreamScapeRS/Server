package com.StreamScape.net.packet.impl;

import com.StreamScape.model.Skill;
import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.world.entity.impl.player.Player;

public class PrestigeSkillPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int prestigeId = packet.readShort();
		Skill skill = Skill.forPrestigeId(prestigeId);
		if (skill == null) {
			return;
		}
		if (skill == Skill.CONSTITUTION) {
			player.getPacketSender().sendMessage("@red@Sorry, this skill cannot be prestiged!");
			return;
		}
		if (player.getInterfaceId() > 0) {
			player.getPacketSender().sendMessage("Please close all interfaces before doing this.");
			return;
		}
		player.getSkillManager().resetSkill(skill, true);
	}

}
