package com.StreamScape.net.packet.impl;

import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.world.content.dialogue.DialogueManager;
import com.StreamScape.world.entity.impl.player.Player;

/**
 * This packet listener handles player's mouse click on the "Click here to
 * continue" option, etc.
 * 
 * @author relex lawl
 */

public class DialoguePacketListener implements PacketListener {

	public static final int DIALOGUE_OPCODE = 40;

	@Override
	public void handleMessage(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case DIALOGUE_OPCODE:
			DialogueManager.next(player);
			break;
		}
	}
}
