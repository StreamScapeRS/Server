package com.StreamScape.model.input.impl;

import com.StreamScape.model.input.EnterAmount;
import com.StreamScape.world.content.skill.impl.crafting.Flax;
import com.StreamScape.world.entity.impl.player.Player;

public class EnterAmountToSpin extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		Flax.spinFlax(player, amount);
	}

}
