package com.StreamScape.drops;

import com.StreamScape.drops.NPCDrops.NpcDrop;
import com.StreamScape.model.definitions.ItemDefinition;
import com.StreamScape.world.World;
import com.StreamScape.world.entity.impl.player.Player;

public class DropAnnouncer {

	public static boolean shouldAnnounce(NpcDrop drop) {
		return drop.getChance() >= 50;
	}

	public static void announce(Player player, int itemId, int npcId, int amount, int value) {
		String rare_text = value > 100 ? "very rare" : value > 150 ? "ultra rare" : value > 300 ? "legendary" : "rare";
		String itemName = ItemDefinition.forId(itemId).getName();
		World.sendMessage("@bla@" +
				player.getUsername() + "@red@ has received @blu@" + amount + "x " + itemName + "@red@ as a " + rare_text + " drop!");
	}
}