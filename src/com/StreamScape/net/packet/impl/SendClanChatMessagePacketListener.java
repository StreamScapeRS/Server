package com.StreamScape.net.packet.impl;

import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.util.Misc;
import com.StreamScape.world.content.PlayerPunishments;
import com.StreamScape.world.content.clan.ClanChatManager;
import com.StreamScape.world.content.dialogue.DialogueManager;
import com.StreamScape.world.entity.impl.player.Player;

public class SendClanChatMessagePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		String clanMessage = Misc.readString(packet.getBuffer());
		if (clanMessage == null || clanMessage.length() < 1)
			return;
		if (PlayerPunishments.isMuted(player.getUsername()) || PlayerPunishments.isIpMuted(player.getHostAddress())
				|| PlayerPunishments.isMacMuted(player.getMac())) {
			player.getPacketSender().sendMessage("You are muted and cannot chat.");
			return;
		}
		if (Misc.blockedWord(clanMessage)) {
			DialogueManager.sendStatement(player, "A word was blocked in your sentence. Please do not repeat it!");
			return;
		}
		ClanChatManager.sendMessage(player, clanMessage);
	}

}
