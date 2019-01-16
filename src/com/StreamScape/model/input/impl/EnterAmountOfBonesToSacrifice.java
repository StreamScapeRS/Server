package com.StreamScape.model.input.impl;

import com.StreamScape.model.input.EnterAmount;
import com.StreamScape.world.content.skill.impl.prayer.BonesOnAltar;
import com.StreamScape.world.entity.impl.player.Player;

public class EnterAmountOfBonesToSacrifice extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		BonesOnAltar.offerBones(player, amount);
	}

}
