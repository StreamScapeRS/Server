package com.StreamScape.world.content.combat.magic.lunar.spells;

import com.StreamScape.model.Animation;
import com.StreamScape.model.Graphic;
import com.StreamScape.model.Item;
import com.StreamScape.world.content.combat.magic.lunar.LunarSpell;
import com.StreamScape.world.entity.impl.player.Player;

public class SuperGlassMakeSpell extends LunarSpell {
	private int[] convertable = {};

	@Override
	public void execute(Player player) {

		for (int item : convertable) {
			if (player.hasItem(item)) {
				// ... TODO: This is a click on item spell
			}
		}

		player.performAnimation(new Animation(4412));
		player.performGraphic(new Graphic(729));
	}

	@Override
	public Item[] getItemRequirements() {
		return new Item[] {

		};
	}

	@Override
	public int getLevelRequirement() {
		return 77;
	}

}
