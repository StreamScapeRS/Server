package com.StreamScape.world.entity.impl.player;

import com.StreamScape.model.RegionInstance.RegionInstanceType;
import com.StreamScape.util.Misc;
import com.StreamScape.world.content.LoyaltyProgram;
import com.StreamScape.world.content.combat.pvp.BountyHunter;
import com.StreamScape.world.content.skill.impl.construction.House;
import com.StreamScape.world.entity.impl.GroundItemManager;

public class PlayerProcess {

	/*
	 * The player (owner) of this instance
	 */
	private Player player;

	/*
	 * The loyalty tick, once this reaches 6, the player will be given loyalty
	 * points. 6 equals 3.6 seconds.
	 */
	private int loyaltyTick;

	/*
	 * The timer tick, once this reaches 2, the player's total play time will be
	 * updated. 2 equals 1.2 seconds.
	 */
	private int timerTick;

	/*
	 * Makes sure ground items are spawned on height change
	 */
	private int previousHeight;

	public PlayerProcess(Player player) {
		this.player = player;
		this.previousHeight = player.getPosition().getZ();
	}

	public void sequence() {

		/** SKILLS **/
		if (player.shouldProcessFarming()) {
			player.getFarming().sequence();
		}

		/** MISC **/

		if (previousHeight != player.getPosition().getZ()) {
			GroundItemManager.handleRegionChange(player);
			previousHeight = player.getPosition().getZ();
		}

		if (!player.isInActive()) {
			if (loyaltyTick >= 6) {
				LoyaltyProgram.incrementPoints(player);
				loyaltyTick = 0;
			}
			loyaltyTick++;
		}

		if (timerTick >= 1) {
			player.getPacketSender().sendString(39167, "@red@Time played:  @bla@"
					+ Misc.getTimePlayed((player.getTotalPlayTime() + player.getRecordedLogin().elapsed())));
			timerTick = 0;
		}
		timerTick++;

		BountyHunter.sequence(player);

		if (player.getRegionInstance() != null
				&& (player.getRegionInstance().getType() == RegionInstanceType.CONSTRUCTION_HOUSE
						|| player.getRegionInstance().getType() == RegionInstanceType.CONSTRUCTION_DUNGEON)) {
			((House) player.getRegionInstance()).process();
		}
	}
}
