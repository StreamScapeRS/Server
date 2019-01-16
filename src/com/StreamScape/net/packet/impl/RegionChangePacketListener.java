package com.StreamScape.net.packet.impl;

import com.StreamScape.engine.task.Task;
import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.model.RegionInstance.RegionInstanceType;
import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.world.clip.region.RegionClipping;
import com.StreamScape.world.content.CustomObjects;
import com.StreamScape.world.content.Sounds;
import com.StreamScape.world.entity.impl.GroundItemManager;
import com.StreamScape.world.entity.impl.npc.NPC;
import com.StreamScape.world.entity.impl.player.Player;
import com.StreamScape.world.entity.updating.NPCUpdating;

public class RegionChangePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.isAllowRegionChangePacket()) {
			RegionClipping.loadRegion(player.getPosition().getX(), player.getPosition().getY());
			player.getPacketSender().sendMapRegion();
			CustomObjects.handleRegionChange(player);
			GroundItemManager.handleRegionChange(player);
			Sounds.handleRegionChange(player);
			player.getTolerance().reset();
			// Hunter.handleRegionChange(player);
			if (player.getRegionInstance() != null && player.getPosition().getX() != 1
					&& player.getPosition().getY() != 1) {
				if (player.getRegionInstance().equals(RegionInstanceType.BARROWS)
						|| player.getRegionInstance().equals(RegionInstanceType.WARRIORS_GUILD))
					player.getRegionInstance().destruct();
			}

			/** NPC FACING **/
			TaskManager.submit(new Task(1, player, false) {
				@Override
				protected void execute() {
					for (NPC npc : player.getLocalNpcs()) {
						if (npc == null || npc.getMovementCoordinator().getCoordinator().isCoordinate())
							continue;
						NPCUpdating.updateFacing(player, npc);
					}
					stop();
				}
			});

			player.setRegionChange(false).setAllowRegionChangePacket(false);
		}
	}
}
