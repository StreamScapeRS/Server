package com.StreamScape.world.content.minigames.impl;

import com.StreamScape.GameSettings;
import com.StreamScape.model.*;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.content.CustomObjects;
import com.StreamScape.world.content.minigames.Minigame;
import com.StreamScape.world.content.teleportation.TeleportHandler;
import com.StreamScape.world.entity.impl.npc.NPC;
import com.StreamScape.world.entity.impl.player.Player;

public final class HungerGames extends Minigame {

    // status
    public static final int IN_BIG_ARENA = 1; // the main map
    public static final int IN_SMALL_ARENA = 2; // the shrinked small map
    public static final int IN_LOBBY = 0;

    // times
    public static final int SMALL_ARENA_TIME = 3 * 60; // 3 minutes
    public static final int BIG_ARENA_TIME = 10 * 60; // 10 minutes
    public static final int LOBBY_TIME = 2 * 60; // 2 minutes

    private int status = IN_LOBBY;

    private int timeToSmallArena = -1;
    private int timeToBigArena = -1;
    private int timeToStart = -1;
    private int timeToEnd = -1;

    private boolean refreshNeeded = false;

    public static final int OPEN_CHEST_ID = 49896;
    public static final int CLOSED_CHEST_ID = 49886;

    public static final int MIN_PLAYERS = 5;

    public static final Position lastFightPos = new Position(2827, 3689);

    protected int[][] chestCoords = {
            {2890, 3677}, {2886, 3683}, {2887, 3666}, {2877, 3676}, {2875, 3680}, {2873, 3689}, {2873, 3698},
            {2885, 3702}, {2899, 3700}, {2899, 3715}, {2898, 3715}, {2909, 3693}, {2912, 3684}, {2912, 3683},
            {2916, 3676}, {2907, 3654}, {2908, 3654}, {2909, 3654}, {2863, 3665}, {2862, 3664}, {2862, 3658},
            {2849, 3663}, {2841, 3670}, {2843, 3685}, {2855, 3689}, {2877, 3678}, {2904, 3669}, {2881, 3659},
            {2889, 3654}, {2907, 3669}, {2871, 3706}, {2897, 3685}, {2901, 3679}
    };

    protected int[][] spawnCoords = {
            {2887, 3675}, {2891, 3680}, {2893, 3686}, {2887, 3682}, {2879, 3678}, {2883, 3685}, {2885, 3690},
            {2896, 3694}, {2902, 3690}, {2872, 3674}, {2871, 3682}, {2877, 3687}, {2881, 3692}, {2886, 3696},
            {2873, 3690}, {2867, 3682}, {2867, 3672}, {2869, 3666}, {2874, 3692}, {2873, 3690}, {2887, 3703},
            {2899, 3707}, {2906, 3697}, {2914, 3679}, {2912, 3671}, {2907, 3657}, {2856, 3664}, {2851, 3671},
            {2850, 3684}, {2854, 3689}, {2857, 3693}, {2849, 3696}, {2844, 3691}
    };

    private Chest[] chests;

    private int[][] chestRewards = {
            {423, 1}, {21011, 1}, {21031, 1}, {21051, 1}, {21005, 1}, {21025, 1}, {21045, 1}, {3653, 1}
    };

    private class Chest {
        Position pos;
        boolean open = false;
        public Chest(Position pos) {
            this.pos = pos;
        }
        public boolean isOpen() {
            return this.open;
        }
        public boolean open() {
            if (open == true)
                return false;
            CustomObjects.spawnGlobalObject(new GameObject(OPEN_CHEST_ID, pos));
            return true;
        }
        public void close() {
            if (open == false)
                return;
            CustomObjects.spawnGlobalObject(new GameObject(CLOSED_CHEST_ID, pos));
        }
        public Position getPos() {
            return this.pos;
        }
    }

    // delays
    public static final int LOCAL_PLAYER_INDEX_DELAY = 45;

    // time to nexts!
    private int timeToNextPlayerIndex = 0;


    public HungerGames() {
        super(100, Locations.Location.HUNGER_GAMES_LOBBY); // should fix location
        setMaxRunTime(-1);
        initChests();
        setName("Hunger Games");
        setActivated(false);
    }

    @Override
    public void handlePlayerDeath(Player p) {
        if (removePlayer(p)) {
            p.getInventory().deleteAll();
            p.getEquipment().deleteAll();
        }
    }

    @Override
    public boolean handleNpcDeath(NPC npc) {
        return false;
    }

    @Override
    public void process() {
        if (!activated)
            return;
        if (active) {
            processActive();
        } else {
            processInActive();
        }
    }

    private void processActive() {
        int currentTime = (int)System.currentTimeMillis();
        if (playerIndex == 1) {
            players[0].moveTo(GameSettings.DEFAULT_POSITION);
            sendWorldMessage(players[0].getUsername() + " has won the hunger games!");
            init();
            refreshChests();
            stop();
            winner(players[0]);
            return;
        }
        if (playerIndex == 0) {
            init();
            refreshChests();
            stop();
            return;
        }
        if (status == IN_BIG_ARENA) {
            if (timeToSmallArena == -1) {
                timeToSmallArena = currentTime + BIG_ARENA_TIME * 1000;
                return;
            }
            if (currentTime > timeToSmallArena) {
                startSmallArena();
                status = IN_SMALL_ARENA;
                return;
            }
        }
        if (status == IN_SMALL_ARENA) {
            if (timeToEnd == -1) {
                timeToEnd = currentTime + SMALL_ARENA_TIME * 1000;
                return;
            }
            if (currentTime > timeToEnd) {
                end();
                return;
            }
        }
        if (currentTime > timeToNextPlayerIndex) {
            sendLocalMessage("@red@[Local Hunger Games] @bla@There are " + playerIndex + " left in hunger games");
            timeToNextPlayerIndex = currentTime + LOCAL_PLAYER_INDEX_DELAY * 1000;
        }
    }

    private void processInActive() {
        int currentTime = (int)System.currentTimeMillis();
        if (playerIndex > 0) {
            if (playerIndex >= MIN_PLAYERS) {
                if (timeToStart == -1) {
                    timeToStart = currentTime + LOBBY_TIME * 1000;
                    sendWorldMessage("game will start in " + LOBBY_TIME + " seconds");
                    return;
                }
                if (currentTime < timeToStart)
                    return;
                start();
                startBigArena();
                World.sendMessage("@red@[Hunger Games] @bla@Happy hunger games and may the odds be ever if your favor");
                refreshNeeded = true;
            }
        } else {
            if (refreshNeeded) {
                refreshChests();
                refreshNeeded = false;
                timeToSmallArena = -1;
                timeToBigArena = -1;
                timeToStart = -1;
                timeToEnd = -1;
            }
        }
    }

    private void refreshChests() {
        for (Chest chest : chests) {
            chest.close();
        }
    }

    private void initChests() {
        this.chests = new Chest[chestCoords.length];
        for (int i = 0; i < chests.length; ++i) {
            chests[i] = new Chest(new Position(chestCoords[i][0], chestCoords[i][1]));
        }
    }

    private void despawnChests() {
        for (Chest chest : chests) {
            CustomObjects.deleteGlobalObject(new GameObject(chest.isOpen() ? OPEN_CHEST_ID : CLOSED_CHEST_ID, chest.getPos()));
        }
    }

    private void startBigArena() {
        for (int i = 0; i < chestCoords.length; ++i) {
            Position chestPos = new Position(chestCoords[i][0], chestCoords[i][1]);
            CustomObjects.spawnGlobalObject(new GameObject(CLOSED_CHEST_ID, chestPos));
        }
        int random;
        for (int i = 0; i < playerIndex; ++i) {
            random = Misc.random(spawnCoords.length - 1);
            TeleportHandler.teleportPlayer(players[i], new Position(spawnCoords[random][0], spawnCoords[random][1], 0),
                    players[i].getSpellbook().getTeleportType());
        }
        sendLocalMessage("You teleport to the hunger games arena, last one standing wins, good luck!");
        status = IN_BIG_ARENA;
    }

    private void startSmallArena() {
        for (int i = 0; i < playerIndex; ++i) {
            players[i].moveTo(lastFightPos);
        }
        sendLocalMessage("this is your last chance! kill everyone to win");
    }

    @Override
    protected void end() {
        for (int i = 0; i < playerIndex; ++i) {
            players[i].moveTo(GameSettings.DEFAULT_POSITION);
        }
        stop();
        despawnChests();
        status = IN_LOBBY;
    }

    public void handleChest(Player p, Position chestPos) {
        if (!isInGame(p)) {
            p.getPacketSender().sendMessage("you cant open this");
            return;
        }
        Chest chest = getChest(chestPos);
        if (chest.isOpen()) {
            p.getPacketSender().sendMessage("this chest has already been opened before");
        } else {
            p.getPacketSender().sendMessage("you open the chest hoping for some good loot to survive");
            giveChestReward(p);
            chest.open();
            p.performAnimation(new Animation(827));
        }
    }

    private void giveChestReward(Player player) {
        int maxRewards = 3;
        int rewardCount = Misc.random(maxRewards-1) + 1;
        for (int i = 0; i < rewardCount; ++i) {
            int random = Misc.random(this.chestRewards.length - 1);
            Item reward = new Item(chestRewards[random][0], chestRewards[random][1]);
            player.getInventory().add(reward);
        }
    }

    private Chest getChest(Position p) {
        for (Chest chest : chests) {
            if (chest.getPos().equals(p))
                return chest;
        }
        return null;
    }

    private void winner(Player player) {
        handlePlayerDeath(player);
        player.moveTo(GameSettings.DEFAULT_POSITION);
        player.getInventory().add(new Item(6199));
    }

    private void sendWorldMessage(String message) {
        World.sendMessage("@red@[Hunger Games] @blu@" + message);
    }

    @Override
    public void handleLogout(Player p) {
        if (!removePlayer(p))
            return;
        if (!Locations.Location.inLocation(p, Locations.Location.HUNGER_GAMES_ARENA))
            return;
        p.getInventory().deleteAll();
        p.getEquipment().deleteAll();
        p.moveTo(GameSettings.DEFAULT_POSITION);
    }

    @Override
    public void handleLocationEntry(Player p) {
        if (status == IN_LOBBY) {
            p.getPacketSender().sendInteractionOption("null", 2, false);
            addPlayer(p);
            p.sendMessage("Welcome to Hunger Games!");
            int playersToStart = MIN_PLAYERS - playerIndex;
            if (playersToStart > 0) {
                p.sendMessage(playersToStart + " more players are needed to start!");
            } else if (timeToStart != -1) {
                int secondsToStart = (timeToStart - (int) System.currentTimeMillis()) / 1000;
                p.sendMessage("Hunger Games will start in " + secondsToStart + " seconds, get ready!");
            }
        } else if (status == IN_BIG_ARENA || status == IN_SMALL_ARENA) {
            if (p.getPlayerInteractingOption() != PlayerInteractingOption.ATTACK)
                p.getPacketSender().sendInteractionOption("Attack", 2, true);
        }
    }

    public boolean canEnter(Player p) {
        if (active)
            return false;
        if (p.getEquipment().isEmpty()) {
            if (p.getInventory().isEmpty())
                return true;
        }
        return false;
    }

    @Override
    public void handleLocationOutry(Player p) {
        if (!isInGame(p))
            return;
        if (Locations.Location.inLocation(p, Locations.Location.HUNGER_GAMES_ARENA))
            return;
        if (status == IN_LOBBY && !Locations.Location.inLocation(p, Locations.Location.HUNGER_GAMES_ARENA)) {
            //p.getPacketSender().sendMessage("rip");
            removePlayer(p);
        } else if (status == IN_LOBBY) {
            return;
        } else if ((status == IN_BIG_ARENA || status == IN_SMALL_ARENA) && !Locations.Location.inLocation(p, Locations.Location.HUNGER_GAMES_ARENA)) {
            //p.getPacketSender().sendMessage("rip2");
            p.getPacketSender().sendInteractionOption("null", 2, false);
            handlePlayerDeath(p);
            removePlayer(p);
        }
    }
}
