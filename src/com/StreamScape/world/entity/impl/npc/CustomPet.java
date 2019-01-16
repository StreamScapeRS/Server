package com.StreamScape.world.entity.impl.npc;

import com.StreamScape.model.Item;
import com.StreamScape.world.content.skill.impl.summoning.Familiar;
import com.StreamScape.world.content.skill.impl.summoning.FamiliarData;
import com.StreamScape.world.entity.impl.player.Player;

public class CustomPet {
    /*
     * tomorrow
     *
     */


    // itemId - npcId
    public static final int[][] DATA = {{1647, 502}, {17013, 1800}, {17010, 700}, {17011, 701},
            {17015,541},{17017,538},{17019,539},{17021, 537},{17023,531},{17027,535},{17029,533}

    };

    public static int forItemId(int itemId) {
        for (int i = 0; i < DATA.length; ++i) {
            if (DATA[i][0] == itemId)
                return DATA[i][1];
        }
        return -1;
    }

    public static int forNpcId(int npcId) {
        for (int i = 0; i < DATA.length; ++i) {
            if (DATA[i][1] == npcId)
                return DATA[i][0];
        }
        return -1;
    }

    public static boolean isCustomFamiliar(Familiar fam) {
        int npcId = fam.getSummonNpc().getId();
        return (!(forNpcId(npcId) == -1));
    }

    public static boolean isCustomNpcPet(NPC npc) {
        if (!npc.isSummoningNpc())
            return false;
        int npcId = npc.getId();
        return !(forNpcId(npcId) == -1);
    }

    public static boolean spawnForItem(Player player, int itemId, boolean removeItem) {
        int npcId = forItemId(itemId);
        if (npcId == -1)
            return false;
        FamiliarData fam = FamiliarData.NONE;
        fam.setNpcId(npcId);
        if (!player.getSummoning().summon(fam, false, true))
            return false;
        if (removeItem)
            player.getInventory().delete(new Item(itemId));
        return true;
    }

    public static boolean spawnForItem(Player player, int itemId) {
        return spawnForItem(player, itemId, true);
    }

    public void processPet() {
        // to be continued and called in Summoning.java
    }

    // i have to edit familiardata thing - 6830
    public static boolean pickUp(Player player, NPC npc) {
        int itemId = forNpcId(npc.getId());
        if (itemId == -1)
            return false;
        if (player.getSummoning().getFamiliar().getSummonNpc().getIndex() != npc.getIndex()) {
            player.getPacketSender().sendMessage("this is not your pet so you can't pick it up");
            return false;
        }
        player.getSummoning().unsummon(true, true);
        player.getInventory().add(new Item(itemId));
        return true;
    }

    public static boolean hasPet(Player player) {
        return forNpcId(player.getSummoning().getFamiliar().getSummonNpc().getId()) != -1;
    }

    public static int getPetItemId(Player p) {
        int npcId;
        try {
            npcId = p.getSummoning().getFamiliar().getSummonNpc().getId();
        } catch (Exception e) {
            return -1;
        }
        return forNpcId(npcId);
    }

}

