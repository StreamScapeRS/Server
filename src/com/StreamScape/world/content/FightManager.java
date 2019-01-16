package com.StreamScape.world.content;

import com.StreamScape.world.content.minigames.impl.Fight;
import com.StreamScape.world.entity.impl.player.Player;

public class FightManager {

    private Player requested;

    private boolean isFighting = false;

    public FightManager() {
    }

    public void setRequested(Player requested) {
        this.requested = requested;
    }

    public Player getRequested() {
        return this.requested;
    }

    public void setFighting(boolean isFighting) { this.isFighting = isFighting; }
    public boolean isFighting() { return this.isFighting; }

    public static void sendInvitation(Player player, Player target) {
        if (target == null)
            return;
        FightManager playerFightManager = player.getFightManager();
        FightManager targetFightManager = target.getFightManager();
        playerFightManager.setRequested(target);

        if (targetFightManager.getRequested() != null)
            if (targetFightManager.getRequested().equals(player)) {
                new Fight(player, target).start();
                return;
            }
        player.getPacketSender().sendMessage("you have sent a fight request to " + target.getUsername());
        target.getPacketSender().sendMessage("@red@" + player.getUsername() + " wishes to fight with you");
    }
}