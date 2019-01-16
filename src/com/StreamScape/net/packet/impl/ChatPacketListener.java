package com.StreamScape.net.packet.impl;

import com.StreamScape.model.ChatMessage.Message;
import com.StreamScape.model.Flag;
import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.util.Misc;
import com.StreamScape.world.content.PlayerPunishments;
import com.StreamScape.world.content.dialogue.DialogueManager;
import com.StreamScape.world.entity.impl.player.Player;

/**
 * This packet listener manages the spoken text by a player.
 * 
 * @author relex lawl
 */

public class ChatPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int effects = packet.readUnsignedByteS();
		int color = packet.readUnsignedByteS();
		int size = packet.getSize();
		byte[] text = packet.readReversedBytesA(size);
		if (PlayerPunishments.isMuted(player.getUsername()) || PlayerPunishments.isIpMuted(player.getHostAddress())
				|| PlayerPunishments.isMacMuted(player.getMac())) {
			player.getPacketSender().sendMessage("You are muted and cannot chat.");
			return;
		}
		String str = new String(text);
		System.out.println(str);
		if (Misc.blockedWord(str)) {
			DialogueManager.sendStatement(player, "A word was blocked in your sentence. Please do not repeat it!");
			return;
		}
		player.getChatMessages().set(new Message(color, effects, text));
		player.getUpdateFlag().flag(Flag.CHAT);
	}

}
