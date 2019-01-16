package com.StreamScape.world.content.minigames.impl;

import com.StreamScape.model.GameObject;
import com.StreamScape.model.Item;
import com.StreamScape.model.Locations.Location;
import com.StreamScape.model.Position;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.content.CustomObjects;
import com.StreamScape.world.entity.impl.npc.NPC;

public class GodsRaid extends Raid {

    public GodsRaid(int maxPlayers) {
        super(maxPlayers, Location.GODS_RAID);
        setName("gods raid");
        init();
    }

    @Override
    protected void spawnCurrentStageNpcs() {
        Stage stage = stages[currentStage];
        int npcCount = stage.countNpcs();
        NPC npc;
        for (int i = 0; i < npcCount; ++i) {
            npc = stage.getNpc(i);
            npc.getDefinition().setAggressive(true);
            npc.getDefinition().setMulti(true);
            npc.getDefinition().setAggressionDistance(50);
            npc.getDefinition().setRespawns(false);
            World.register(stage.getNpc(i));
        }
    }

    @Override
    protected void giveRewards() {
        int[] rare = {3242}; // 1:333 chance
        int[] ultraRare = {17010}; // 1:1000 chance - should put pets here
        int[][] common = {{6199,1}, {6199,1}, {6199,2}}; // item and amount
        for (int i = 0; i < playerIndex; ++i) {
            int random = Misc.random(999);
            Item reward = null;
            int randomCommon = Misc.random(common.length - 1);
            reward = new Item(common[randomCommon][0]);
            int amount = common[randomCommon][1];
            if (random == 999) {
                amount = 1;
                int randomUltra = Misc.random(ultraRare.length - 1);
                reward = new Item(ultraRare[randomUltra]);
                String itemName = reward.getDefinition().getName();
                String announcement = "@red@[Raids][Ultra Rare] @Bla@" + players[i].getUsername() + " has got " + itemName;
                World.sendMessage(announcement);
            }
            else if (random == 999 || random == 998 || random == 997) {
                amount = 1;
                int randomRare = Misc.random(rare.length - 1);
                reward = new Item(ultraRare[randomRare]);
                String itemName = reward.getDefinition().getName();
                String announcement = "@red@[Raids][Rare] @Bla@" + players[i].getUsername() + " has got " + itemName;
                World.sendMessage(announcement);
            }
            reward.setAmount(amount);
            players[i].sendMessage("you got " + amount + "x " + reward.getDefinition().getName() + " as a reward");
            if (reward.getDefinition().isStackable() || players[i].getInventory().getFreeSlots() > amount)
                players[i].getInventory().add(reward);
            else {
                players[i].sendMessage("reward was added to your bank because you have no inventory spaces");
                players[i].getBank(players[i].getCurrentBankTab()).add(reward);
            }
        }
    }

    @Override
    protected void initStages() {
        Stage[] stages = new Stage[4];
        NPC[] stage0npcs = new NPC[10];
        stage0npcs[0] = new NPC(1022, new Position(3429, 2854, 1));
        stage0npcs[1] = new NPC(1019, new Position(3429, 2852, 1));
        stage0npcs[2] = new NPC(1022, new Position(3426, 2848, 1));
        stage0npcs[3] = new NPC(1019, new Position(3423, 2851, 1));
        stage0npcs[4] = new NPC(1022, new Position(3418, 2851, 1));
        stage0npcs[5] = new NPC(1019, new Position(3419, 2847, 1));
        stage0npcs[6] = new NPC(1022, new Position(3419, 2847, 1));
        stage0npcs[7] = new NPC(1019, new Position(3423, 2845, 1));
        stage0npcs[8] = new NPC(1022, new Position(3412, 2844, 1));
        stage0npcs[9] = new NPC(1019, new Position(3418, 2843, 1));
        stages[0] = new Stage(stage0npcs);


        stages[1] = new Stage(new NPC[] {new NPC(2223, new Position(3410, 2831, 1))});
        stages[2] = new Stage(new NPC[] {new NPC(2222, new Position(3430, 2830, 1))});
        stages[3] = new Stage(new NPC[] {
                new NPC(6216, new Position(3399, 2814, 1)), // fire god
                new NPC(10216, new Position(3401, 2805, 1)) // water god
        });
        this.stages = stages;
    }

    @Override
    protected void startNextStage() {
        super.startNextStage();
        removeWallsForStage(currentStage);
    }

    @Override
    public void init() {
        super.init();
        initStages();
        prepareWalls();
    }
    private void prepareWalls() {
        //1st stage
        int y = 2848;
        int x = 3435;
        int z = 1;
        for (int tempY = y; tempY < 2859; ++tempY) {
            GameObject object = new GameObject(1864, new Position(x, tempY, z));
            CustomObjects.spawnGlobalObject(object);
        }
        //2nd
        y = 2836;
        x = 3416;
        z = 1;
        for (int tempY = y; tempY < 2841; ++tempY) {
            GameObject object = new GameObject(1864, new Position(x, tempY, z));
            CustomObjects.spawnGlobalObject(object);
        }
        y = 2840;
        x = 3415;
        z = 1;
        for (int tempX = 3405; tempX < x + 1; ++tempX) {
            GameObject object = new GameObject(1864, new Position(tempX, y, z));
            CustomObjects.spawnGlobalObject(object);
        }
        //3rd
        x = 3409;
        y = 2821;
        z = 1;
        GameObject object = new GameObject(1864, new Position(x, y, z));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(1864, new Position(3420, 2828, 1));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(1864, new Position(3420, 2829, 1));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(1864, new Position(3420, 2827, 1));
        CustomObjects.spawnGlobalObject(object);
        //done! for the 4th stage everything is open
    }
    // not done yet
    private void removeWallsForStage(int stage) {
        switch(stage) {
            case 0:
                int y = 2848;
                int x = 3435;
                int z = 1;
                for (int tempY = y; tempY < 2859; ++tempY) {
                    GameObject object = new GameObject(1864, new Position(x, tempY, z));
                    CustomObjects.deleteGlobalObject(object);
                }
                break;
            case 1:
                y = 2836;
                x = 3416;
                z = 1;
                for (int tempY = y; tempY < 2841; ++tempY) {
                    GameObject object = new GameObject(1864, new Position(x, tempY, z));
                    CustomObjects.deleteGlobalObject(object);
                }
                y = 2840;
                x = 3415;
                z = 1;
                for (int tempX = 3405; tempX < x + 1; ++tempX) {
                    GameObject object = new GameObject(1864, new Position(tempX, y, z));
                    CustomObjects.deleteGlobalObject(object);
                }
                break;
            case 2:
                GameObject object;
                object = new GameObject(1864, new Position(3420, 2828, 1));
                CustomObjects.deleteGlobalObject(object);
                object = new GameObject(1864, new Position(3420, 2829, 1));
                CustomObjects.deleteGlobalObject(object);
                object = new GameObject(1864, new Position(3420, 2827, 1));
                CustomObjects.deleteGlobalObject(object);
                break;
            case 3:
                x = 3409;
                y = 2821;
                z = 1;
                GameObject object1 = new GameObject(1864, new Position(x, y, z));
                CustomObjects.deleteGlobalObject(object1);
                break;
        }
    }


}
