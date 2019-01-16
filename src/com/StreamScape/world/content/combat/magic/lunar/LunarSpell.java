package com.StreamScape.world.content.combat.magic.lunar;

import com.StreamScape.model.Item;
import com.StreamScape.world.entity.impl.player.Player;

public abstract class LunarSpell {
	public abstract void execute(Player player);

	public abstract Item[] getItemRequirements();

	public abstract int getLevelRequirement();
}
