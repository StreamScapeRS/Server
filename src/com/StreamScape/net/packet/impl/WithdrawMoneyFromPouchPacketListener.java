package com.StreamScape.net.packet.impl;

import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.world.content.MoneyPouch;
import com.StreamScape.world.entity.impl.player.Player;

public class WithdrawMoneyFromPouchPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int amount = packet.readInt();
		MoneyPouch.withdrawMoney(player, amount);
	}

}
