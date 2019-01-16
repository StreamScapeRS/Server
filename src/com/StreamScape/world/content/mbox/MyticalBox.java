package com.StreamScape.world.content.mbox;

import com.StreamScape.model.ItemRarity;
import com.StreamScape.model.definitions.ItemDefinition;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.entity.impl.player.Player;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.StreamScape.model.ItemRarity.*;

public class MyticalBox {

    public static final int MBOX_ITEM_ID = 15507;

    public static void giveReward(Player player) {
        if (player.getInventory().contains(MBOX_ITEM_ID) && player.getInventory().getFreeSlots() > 0) {

            double numGen = Math.random();

            ItemRarity rarity = numGen >= 0.02 ? ItemRarity.COMMON : numGen > 0.003 ? ItemRarity.LEGENDARY : ItemRarity.LEGENDARY;

            Optional<MyticalBox.MyticalBoxReward> reward = MyticalBox.MyticalBoxReward.get(rarity);

            if (reward.isPresent()) {
                player.getInventory().delete(MBOX_ITEM_ID, 1);
                player.getInventory().add(reward.get().getItemId(), reward.get().getAmount()).refreshItems();
                if (reward.get().getRarity() == LEGENDARY) {
                    World.getPlayers()
                            .forEach(p -> p.sendMessage("@red@" + player.getUsername() + "@bla@ has received @red@"
                                    + reward.get().getAmount() + "x "
                                    + ItemDefinition.forId(reward.get().getItemId()).getName()
                                    + "@bla@ from a mega mystery box."));
                }
            } else {
                player.sendMessage("Critical error: Report to staff");
            }
        }
    }

    public enum MyticalBoxReward {

        buck1(13664,1, COMMON),
        buck2(13664,2, COMMON),
        buck3(13664,3, COMMON),
        SantaTop(14601,1, LEGENDARY),
        santaGloves(14602,1, LEGENDARY),
        santaLegs(14603,1, LEGENDARY),
        santaSkirt(14604,1, LEGENDARY),
        santaBoots(14605,1, LEGENDARY),
        Wyrm(20054,1, LEGENDARY),
        MG(21080,1, LEGENDARY);


        private static final ImmutableSet<MyticalBox.MyticalBoxReward> REWARDS = Sets
                .immutableEnumSet(EnumSet.allOf(MyticalBox.MyticalBoxReward.class));
        private final int itemId;
        private final int amount;
        private final ItemRarity rarity;

        MyticalBoxReward(int itemId, ItemRarity rarity) {
            this.itemId = itemId;
            this.amount = 1;
            this.rarity = rarity;
        }

        MyticalBoxReward(int itemId, int amount, ItemRarity rarity) {
            this.itemId = itemId;
            this.amount = amount;
            this.rarity = rarity;
        }

        public static Optional<MyticalBox.MyticalBoxReward> get(ItemRarity rarity) {
            long count = REWARDS.stream().filter(reward -> reward.getRarity() == rarity).count();
            int randomIndex = Misc.RANDOM.nextInt((int) ((count - 0))) + 0;
            return Optional.of(REWARDS.stream().filter(reward -> reward.getRarity() == rarity)
                    .collect(Collectors.toList()).get(randomIndex));
        }

        public int getItemId() {
            return itemId;
        }

        public int getAmount() {
            return amount;
        }

        public ItemRarity getRarity() {
            return rarity;
        }
    }
}
