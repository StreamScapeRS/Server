package com.StreamScape.world.content.minigames;

import com.StreamScape.engine.task.Task;
import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.world.content.minigames.impl.GodsRaid;
import com.StreamScape.world.content.minigames.impl.HungerGames;
import com.StreamScape.world.entity.impl.npc.NPC;
import com.StreamScape.world.entity.impl.player.Player;

import java.util.ArrayList;

public class MinigameHandler {

    private ArrayList<Minigame> minigames = new ArrayList<Minigame>();

    public MinigameHandler(Minigame... minigames) {
        for (Minigame minigame : minigames) {
            this.minigames.add(minigame);
        }
    }

    public void addMinigame(Minigame minigame) {
        if (!minigames.contains(minigame))
            this.minigames.add(minigame);
    }
    public void removeMinigame(Minigame minigame) { this.minigames.remove(minigame); }

    public void loadMinigames() {
        TaskManager.submit(new Task() {
            @Override
            public void execute() {
                for (int i = 0; i < minigames.size(); ++i) {
                    TaskManager.submit(minigames.get(i));
                }
                stop();
            }
        });
    }

    public void handleLogout(Player p) {
        if (p == null)
            return;
        for (Minigame minigame : minigames) {
            minigame.handleLogout(p);
        }
    }

    public void handlePlayerDeath(Player p) {
        if (p == null)
            return;
        for (Minigame minigame : minigames) {
            minigame.handlePlayerDeath(p);
        }
    }

    public boolean isInGame(Player p) {
        for (Minigame minigame : minigames)
            if (minigame.isInGame(p))
                return true;
        return false;
    }

    public boolean handleNpcDeath(NPC npc) {
        if (npc == null)
            return false;
        for (Minigame minigame : minigames)
            if (minigame.handleNpcDeath(npc))
                return true;
        return false;
    }

    public final static MinigameHandler defaultHandler() {
        return new MinigameHandler(
                new GodsRaid(50), // 50 max players for gods raid
                new HungerGames()
        );
    }

    public Minigame getMinigame(int index) {
        return this.minigames.get(index);
    }

}
