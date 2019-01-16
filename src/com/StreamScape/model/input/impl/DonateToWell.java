package com.StreamScape.model.input.impl;

import com.StreamScape.model.input.EnterAmount;
import com.StreamScape.world.content.WellOfGoodwill;
import com.StreamScape.world.entity.impl.player.Player;

public class DonateToWell extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		WellOfGoodwill.donate(player, amount);
	}

}
