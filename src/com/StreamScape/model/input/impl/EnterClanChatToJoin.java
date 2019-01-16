package com.StreamScape.model.input.impl;

import com.StreamScape.model.input.Input;
import com.StreamScape.world.content.clan.ClanChatManager;
import com.StreamScape.world.entity.impl.player.Player;

public class EnterClanChatToJoin extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if (syntax.length() <= 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		ClanChatManager.join(player, syntax);
	}
}
