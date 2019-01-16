package com.StreamScape.world.content.minigames.impl;

import com.StreamScape.world.content.combat.CombatFactory;
import com.StreamScape.world.entity.impl.player.Player;

public class Fight {
    private Player player, target;

    public Fight(Player player, Player target) {
        this.player = player;
        this.target = target;
    }

    public void start() {
        player.getFightManager().setFighting(true);
        target.getFightManager().setFighting(true);
        if (player.getCombatBuilder().getStrategy() == null) {
            player.getCombatBuilder().determineStrategy();
        }
        if (CombatFactory.checkAttackDistance(player, target)) {
            player.getMovementQueue().reset();
        }
        player.getCombatBuilder().attack(target);
    }
}