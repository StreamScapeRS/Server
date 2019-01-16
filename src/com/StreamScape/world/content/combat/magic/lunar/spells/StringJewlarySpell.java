package com.StreamScape.world.content.combat.magic.lunar.spells;

import java.util.ArrayList;
import java.util.List;

import com.StreamScape.model.Animation;
import com.StreamScape.model.Graphic;
import com.StreamScape.model.Item;
import com.StreamScape.world.content.combat.magic.lunar.LunarSpell;
import com.StreamScape.world.entity.impl.player.Player;

public class StringJewlarySpell extends LunarSpell {
	class Amulet {
		int unstrungId;
		int amuletId;

		Amulet(int unstrungId, int amuletId) {
			this.unstrungId = unstrungId;
			this.amuletId = amuletId;
		}
	}

	private List<Amulet> unstrung = new ArrayList<>();

	@Override
	public void execute(Player player) {
		for (Amulet amulet : unstrung) {
			if (player.getInventory().contains(amulet.unstrungId)) {
				Item item = player.getInventory().getById(amulet.unstrungId);
				if (item != null) {
					player.getInventory().delete(item);
					player.getInventory().add(new Item(amulet.amuletId));

					player.performAnimation(new Animation(4412));
					player.performGraphic(new Graphic(729));

				}
			}
		}
	}

	@Override
	public Item[] getItemRequirements() {
		return new Item[] {

		};
	}

	@Override
	public int getLevelRequirement() {
		return 80;
	}

}
