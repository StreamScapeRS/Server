package com.StreamScape.world.content.mbox;

import static com.StreamScape.model.ItemRarity.LEGENDARY;
import static com.StreamScape.model.ItemRarity.RARE;
import static com.StreamScape.model.ItemRarity.UNCOMMON;
import static com.StreamScape.model.ItemRarity.VERY_RARE;

import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.StreamScape.model.ItemRarity;
import com.StreamScape.model.definitions.ItemDefinition;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.entity.impl.player.Player;

// Author: Chris AKA Hacker.

public class MegaMysteryBox {

    public static final int MBOX_ITEM_ID = 15501;

    public static void giveReward(Player player) {
        if (player.getInventory().contains(MBOX_ITEM_ID) && player.getInventory().getFreeSlots() > 0) {

            double numGen = Math.random();

            ItemRarity rarity = numGen >= 0.60 ? ItemRarity.UNCOMMON
                    : numGen >= 0.30 ? ItemRarity.RARE : numGen >= 0.10 ? ItemRarity.VERY_RARE : ItemRarity.LEGENDARY;

            Optional<MegaMysteryBoxReward> reward = MegaMysteryBoxReward.get(rarity);
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

    public enum MegaMysteryBoxReward {

        LIME_SLED(4177, UNCOMMON), BLOOD_ANCHOR(3074, UNCOMMON), SLED(4083, UNCOMMON), MYSTERY_BOX(6199,
                UNCOMMON), MYSTERY_BOX_8(6199, 2, UNCOMMON), MYSTERY_BOX_9(6199, 3, UNCOMMON), MYSTERY_BOX_0(6199, 4,
                UNCOMMON), MYSTERY_BOX_10(6199, 5, UNCOMMON), StreamScape_BUCKS(13664, UNCOMMON), StreamScape_BUCKS_0(
                13664, 2, UNCOMMON), StreamScape_BUCKS_1(13664, 3, UNCOMMON), StreamScape_BUCKS_8(13664, 4,
                UNCOMMON), StreamScape_BUCKS_9(13664, 5, UNCOMMON), COINS_1000(995, 10000000,
                UNCOMMON), COINS_1020(995, 20000000, UNCOMMON), COINS_1040(995,
                30000000,
                UNCOMMON), COINS_1050(995, 40000000, UNCOMMON), COINS_1060(995,
                40000000,
                UNCOMMON), MYSTERY_KEY(989, UNCOMMON), MYSTERY_KEY_1(
                989, 2, UNCOMMON), MYSTERY_KEY_4(989, 3,
                UNCOMMON), MYSTERY_KEY_6(989, 4,
                UNCOMMON), MYSTERY_KEY_7(989, 4,
                UNCOMMON),

        PINK_SPIRIT_SHIELD(3091, RARE), GREEN_SPIRIT_SHIELD(914, RARE), LIME_SPIRIT_SHIELD(912,
                RARE), PURPLE_SPIRIT_SHIELD(3089, RARE), MYSTERY_BOX_2(6199, 6, RARE), MYSTERY_BOX_11(6199, 7,
                RARE), MYSTERY_BOX_12(6199, 8, RARE), MYSTERY_BOX_13(6199, 9, RARE), MYSTERY_BOX_14(6199, 10,
                RARE), StreamScape_BUCKS_10(13664, 6, RARE), StreamScape_BUCKS_11(13664, 7,
                RARE), StreamScape_BUCKS_12(13664, 8, RARE), StreamScape_BUCKS_13(13664, 9,
                RARE), StreamScape_BUCKS_14(13664, 10, RARE), COINS_1007(995, 50000000,
                RARE), COINS_1028(995, 60000000, RARE), COINS_1049(995,
                70000000,
                RARE), COINS_1053(995, 80000000, RARE), COINS_1064(995,
                90000000, RARE), MYSTERY_KEY_8(989, 5,
                RARE), MYSTERY_KEY_9(989, 6,
                RARE), MYSTERY_KEY_10(989, 7,
                RARE), MYSTERY_KEY_11(
                989, 8,
                RARE), MYSTERY_KEY_12(
                989, 9,
                RARE),
                RARE1(21020,RARE),
                RARE12(21021,RARE),
                RARE13(21022,RARE),
                RARE14(21023,RARE),
                RARE15(21024,RARE),
                RARE16(21025,RARE),
                RARE17(21026,RARE),
                RARE18(21027,RARE),
                RARE19(21028,RARE),
                RARE11(21029,RARE),
                RARE2(21030,RARE),
                RAR(21040,RARE),
                RAedR(21041,RARE),
                RARde(21042,RARE),
                RAR2(21043,RARE),
                RARrer2(596,RARE),
                RAR34(21044,RARE),
                RARe(21045,RARE),
                RARr(21047,RARE),
                RARt(21048,RARE),
                RARy(21049,RARE),
                RARu(21050,RARE),
                RARee(21051,RARE),
                RARwt(21052,RARE),
                RARtr(21053,RARE),

        DEATHLY_SPIRIT_SHIELD(911, VERY_RARE), MYSTICAL_SPIRIT_SHIELD(910, VERY_RARE), APOLLO_CHESTPLATE(3644,
                VERY_RARE), APOLLO_TASSETS(3645, VERY_RARE), APOLLO_BOOTS(3646, VERY_RARE), MYSTERY_BOX_19(6199, 11,
                VERY_RARE), MYSTERY_BOX_18(6199, 12, VERY_RARE), MYSTERY_BOX_17(6199, 13,
                VERY_RARE), MYSTERY_BOX_16(6199, 14, VERY_RARE), MYSTERY_BOX_15(6199, 14,
                VERY_RARE), StreamScape_BUCKS_19(13664, 11, VERY_RARE), StreamScape_BUCKS_18(13664,
                12,
                VERY_RARE), StreamScape_BUCKS_17(13664, 13, VERY_RARE), StreamScape_BUCKS_16(
                13664, 14,
                VERY_RARE), StreamScape_BUCKS_15(13664, 15, VERY_RARE), COINS_1021(
                995, 100000000, VERY_RARE), COINS_1022(995, 110000000,
                VERY_RARE), COINS_1042(995, 120000000,
                VERY_RARE), COINS_1052(995, 130000000,
                VERY_RARE), COINS_1062(995,
                140000000,
                VERY_RARE), MYSTERY_KEY_17(
                989, 10,
                VERY_RARE), MYSTERY_KEY_16(
                989, 11,
                VERY_RARE),
        MYSTERY_KEY_15(989, 12, VERY_RARE),
        tet(18011, VERY_RARE),
        tetef(18000, VERY_RARE),
        teweft(18001, VERY_RARE),
        tewefweft(18002, VERY_RARE),
        tetfwe(18003, VERY_RARE),
        tefewt(18004, VERY_RARE),
        tefwet(18005, VERY_RARE),
        tefwet32(18006, VERY_RARE),



        MYSTERY_KEY_14(
                989,
                13,
                VERY_RARE), MYSTERY_KEY_13(
                989,
                14,
                VERY_RARE),

        LAVA_PARTY_HAT(3869, LEGENDARY), STRIPED_PARTY_HAT(3878, LEGENDARY), STRIPED_SANTA_HAT(2380,
                LEGENDARY), STRIPED_HWEEN_MASK(1667, LEGENDARY), RAINBOW_HWEEN_MASK(1666, LEGENDARY), RED_PARTY_HAT(926,
                LEGENDARY), SANTA_HAT(924, LEGENDARY), CAMO_HWEEN_MASK(941, LEGENDARY), CAMO_PARTY_HAT(2548,
                LEGENDARY), BLACK_HEADBAND(2647, LEGENDARY), RAINBOW_GLOVES(3630,
                LEGENDARY), RAINBOW_BOOTS(3631, LEGENDARY), HAPPY_BATTLEAXE(3637,
                LEGENDARY), RAINBOW_TORVA_PLATEBODY(3629,
                LEGENDARY), RAINBOW_TORVA_FULLHELM(3628,
                LEGENDARY), RAINBOW_WINGS(3632,
                LEGENDARY), RAINBOW_WHIP(3626,
                LEGENDARY), MYSTERY_BOX_4(6199, 25,
                LEGENDARY), MYSTERY_BOX_5(6199,
                50,
                LEGENDARY), MYSTERY_BOX_6(
                6199, 75,
                LEGENDARY), MYSTERY_BOX_7(
                6199,
                100,
                LEGENDARY), StreamScape_BUCKS_4(
                13664,
                250,
                LEGENDARY), StreamScape_BUCKS_5(
                13664,
                500,
                LEGENDARY), StreamScape_BUCKS_6(
                13664,
                750,
                LEGENDARY), StreamScape_BUCKS_7(
                13664,
                1000,
                LEGENDARY), MYSTERY_KEY_117(
                989,
                100,
                LEGENDARY), MYSTERY_KEY_116(
                989,
                125,
                LEGENDARY), MYSTERY_KEY_115(
                989,
                130,
                LEGENDARY), MYSTERY_KEY_114(
                989,
                140,
                LEGENDARY), MYSTERY_KEY_113(
                989,
                150,
                LEGENDARY);

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
