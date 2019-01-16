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

// Author: Chris AKA Hacker.

public class PetMysteryBox {

    public static final int MBOX_ITEM_ID = 15505;

    public static void giveReward(Player player) {
        if (player.getInventory().contains(MBOX_ITEM_ID) && player.getInventory().getFreeSlots() > 0) {

            double numGen = Math.random();

            ItemRarity rarity = numGen >= 0.40 ? ItemRarity.UNCOMMON
                    : numGen >= 0.10 ? ItemRarity.RARE : numGen >= 0.03 ? ItemRarity.VERY_RARE : ItemRarity.LEGENDARY;

            Optional<MegaMysteryBoxReward> reward = MegaMysteryBoxReward.get(rarity);
            if (reward.isPresent()) {
                player.getInventory().delete(MBOX_ITEM_ID, 1);
                player.getInventory().add(reward.get().getItemId(), reward.get().getAmount()).refreshItems();
                if (reward.get().getRarity() == LEGENDARY) {
                    World.getPlayers()
                            .forEach(p -> p.sendMessage("@red@" + player.getUsername() + "@bla@ has received @red@"
                                    + reward.get().getAmount() + "x "
                                    + ItemDefinition.forId(reward.get().getItemId()).getName()
                                    + "@bla@ from a pet mystery box."));
                }
            } else {
                player.sendMessage("Critical error: Report to staff");
            }
        }
    }

    public enum MegaMysteryBoxReward {

        KRABS(17017, 1, COMMON),
        SQUIRDLE(17015,1,COMMON),

        HOMER(17027, 1, RARE),
        SONIC(17021, 1, RARE),
        LUIGI(17029, 1, RARE),

        LUCARIO(17019, 1, VERY_RARE),
        MEWTWO(17023, 1, VERY_RARE),

        SHADOW_CORP(17013, 1, LEGENDARY),
        ICHIGO(17010, 1, LEGENDARY),
        ZANGETSU(17011, 1, LEGENDARY);


        private static final ImmutableSet<MegaMysteryBoxReward> REWARDS = Sets
                .immutableEnumSet(EnumSet.allOf(MegaMysteryBoxReward.class));
        private final int itemId;
        private final int amount;
        private final ItemRarity rarity;

        MegaMysteryBoxReward(int itemId, ItemRarity rarity) {
            this.itemId = itemId;
            this.amount = 1;
            this.rarity = rarity;
        }

        MegaMysteryBoxReward(int itemId, int amount, ItemRarity rarity) {
            this.itemId = itemId;
            this.amount = amount;
            this.rarity = rarity;
        }

        public static Optional<MegaMysteryBoxReward> get(ItemRarity rarity) {
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
