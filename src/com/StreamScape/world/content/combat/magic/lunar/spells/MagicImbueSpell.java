package com.StreamScape.world.content.combat.magic.lunar.spells;

import com.StreamScape.model.Item;
import com.StreamScape.world.content.combat.magic.lunar.LunarSpell;
import com.StreamScape.world.entity.impl.player.Player;

public class MagicImbueSpell extends LunarSpell {

	@Override
	public void execute(Player player) {
		// TODO: Find out wtf this spell does
	}

	@Override
	public Item[] getItemRequirements() {
		return new Item[] {

		};
	}

	@Override
	public int getLevelRequirement() {
		return 82;
	}

}
