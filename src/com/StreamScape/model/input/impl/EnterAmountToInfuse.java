package com.StreamScape.model.input.impl;

import com.StreamScape.model.input.EnterAmount;
import com.StreamScape.world.content.skill.impl.summoning.PouchMaking;
import com.StreamScape.world.entity.impl.player.Player;

public class EnterAmountToInfuse extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		if (player.getInterfaceId() != 63471) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		if (amount >= 29) {
			amount = 28;
		}
		PouchMaking.infusePouches(player, amount);
	}

}
