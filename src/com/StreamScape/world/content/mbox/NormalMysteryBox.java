package com.StreamScape.world.content.mbox;

import static com.StreamScape.model.ItemRarity.COMMON;
import static com.StreamScape.model.ItemRarity.RARE;
import static com.StreamScape.model.ItemRarity.UNCOMMON;

import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.StreamScape.model.ItemRarity;
import com.StreamScape.util.Misc;
import com.StreamScape.world.entity.impl.player.Player;

// Author: Chris AKA Hacker.

public class NormalMysteryBox {

    public static final int MBOX_ITEM_ID = 6199;

    public static void giveReward(Player player) {
        if (player.getInventory().contains(MBOX_ITEM_ID) && player.getInventory().getFreeSlots() > 0) {

            double numGen = Math.random();
            ItemRarity rarity = numGen >= 0.5 ? COMMON : numGen >= 0.20 ? UNCOMMON : RARE;
            Optional<MysteryBoxReward> reward = MysteryBoxReward.get(rarity);
            if (reward.isPresent()) {
                player.getInventory().delete(MBOX_ITEM_ID, 1);
                player.getInventory().add(reward.get().getItemId(), reward.get().getAmount()).refreshItems();
            } else {
                player.sendMessage("Critical error: Report to staff");
            }
        }
    }

    public enum MysteryBoxReward {

        TEXAS_HAT(3075, COMMON), WOOLY_HAT(6862, COMMON), LIME_BOBBLE_HAT(3072, COMMON), LIME_BOBBLE_SCARF(3073,
                COMMON), PINK_WOOLLY_SCARF(3076, COMMON), PURPLE_HWEEN_MASK(2753, COMMON), PURPLE_PARTY_HAT(904,
                COMMON), PURPLE_SANTA_HAT(903, COMMON), StreamScape_BUCKS(13664, COMMON), StreamScape_BUCKS_0(13664,
                1, COMMON), StreamScape_BUCKS_1(13664, 2, COMMON), StreamScape_BUCKS_8(13664, 3,
                COMMON), StreamScape_BUCKS_9(13664, 4, COMMON), COINS_1000(995, 1000000,
                COMMON), COINS_1020(995, 2000000, COMMON), COINS_1040(995, 3000000,
                COMMON), COINS_1050(995, 4000000, COMMON), COINS_1060(995,
                4000000,
                COMMON), MYSTERY_KEY(989, COMMON), MYSTERY_KEY_1(989, 2,
                COMMON), MYSTERY_KEY_4(989, 3,
                COMMON), MYSTERY_KEY_6(989, 4, COMMON),

        CYAN_SANTA_HAT(896, UNCOMMON), BLUE_SANTA_HAT(898, UNCOMMON), PINK_SANTA_HAT(899, UNCOMMON), YELLOW_SANTA_HAT(
                900, UNCOMMON), GREEN_SANTA_HAT(901, UNCOMMON), LIME_SANTA_HAT(902, UNCOMMON), LIME_PARTY_HAT(906,
                UNCOMMON), PINK_PARTY_HAT(907, UNCOMMON), CYAN_PARTY_HAT(908, UNCOMMON), ORANGE_SANTA_HAT(909,
                UNCOMMON), MYSTERY_KEY_2(989, UNCOMMON), COINS_100(995, 100, UNCOMMON), PINK_HWEEN_MASK(
                2750, UNCOMMON), ORANGE_HWEEN_MASK(2749, UNCOMMON), YELLOW_HWEEN_MASK(2751,
                UNCOMMON), LIME_HWEEN_MASK(2752, UNCOMMON), CYAN_HWEEN_MASK(2754,
                UNCOMMON), ORANGE_PARTY_HAT(3088,
                UNCOMMON), SARADOMIN_KITESHIELD(3294,
                UNCOMMON), SARADOMIN_KITESHIELD_G(3295,
                UNCOMMON), SARADOMIN_LONGSWORD(3296,
                UNCOMMON), SARADMIN_LONGSWORD_G(
                3297,
                UNCOMMON), StreamScape_BUCKS_2(
                13664,
                UNCOMMON), StreamScape_BUCKS_10(
                13664,
                5,
                UNCOMMON), StreamScape_BUCKS_11(
                13664,
                6,
                UNCOMMON), StreamScape_BUCKS_12(
                13664,
                7,
                UNCOMMON), StreamScape_BUCKS_13(
                13664,
                9,
                UNCOMMON), StreamScape_BUCKS_14(
                13664,
                10,
                UNCOMMON), COINS_1(
                995,
                5000000,
                UNCOMMON), COINS_2(
                995,
                6000000,
                UNCOMMON), COINS_3(
                995,
                7000000,
                UNCOMMON), COINS_4(
                995,
                8000000,
                UNCOMMON), COINS_5(
                995,
                9000000,
                UNCOMMON), MYSTERY_KEY_8(
                989,
                5,
                UNCOMMON), MYSTERY_KEY_12(
                989,
                6,
                UNCOMMON), MYSTERY_KEY_9(
                989,
                7,
                UNCOMMON), MYSTERY_KEY_10(
                989,
                8,
                UNCOMMON), MYSTERY_KEY_11(
                989,
                9,
                UNCOMMON),

        MYSTERY_KEY_3(989, RARE), GOLD_SANTA_HAT(3090, RARE), GOLD_GRANITE_MAUL(3643, RARE), GOLD_AP_CHESTPLATE(3807,
                RARE), GOLD_AP_TASSETS(3808, RARE), GOLD_AP_BOOTS(3809, RARE), GOLD_PARTY_HAT(3811,
                RARE), GOLD_HWEEN_MASK(3812, RARE), GOLD_BOXING_GLOVES(11292, RARE), StreamScape_BUCKS_3(13664,
                RARE), StreamScape_BUCKS_15(13664, RARE), StreamScape_BUCKS_16(13664, 11,
                RARE), StreamScape_BUCKS_17(13664, 12, RARE), StreamScape_BUCKS_18(13664, 13,
                RARE), StreamScape_BUCKS_19(13664, 14, RARE), StreamScape_BUCKS_20(13664, 15,
                RARE), COINS_6(995, 10000000, RARE), COINS_7(995, 11000000,
                RARE), COINS_8(995, 12000000, RARE), COINS_9(995,
                13000000, RARE), COINS_10(995, 14000000,
                RARE), MYSTERY_KEY_13(989, 10,
                RARE), MYSTERY_KEY_14(989, 11,
                RARE), MYSTERY_KEY_15(
                989, 12,
                RARE),
        MYSTERY_KEY_16( 989, 13,RARE),
        MYSTERY_KEY_17( 989, 14, RARE),
        ELITE_MYSTERY_BOX(15501,RARE),
        DRACONIAN1(15501,RARE),
        DRACONIAN2(21000,RARE),
        DRACONIAN3(21001,RARE),
        DRACONIAN4(21002,RARE),
        DRACONIAN5(21003,RARE),
        DRACONIAN51(21004,RARE),
        DRACONIAN6(21005,RARE),
        DRACONIAN7(21006,RARE),
        DRACONIAN8(21007,RARE),
        DRACONIAN9(21008,RARE),
        DRACONIAN10(21009,RARE),
        DRACONIAN11(21010,RARE),
        DRACONIAN512(21011,RARE),
        Pur(3654,UNCOMMON),
        Pur1(3655,UNCOMMON),
        Pur2(3656,UNCOMMON),
        Pur3(3657,UNCOMMON),
        Pur4(3658,UNCOMMON),
        row(2572,UNCOMMON),
        Pur5(3659,UNCOMMON),
        Pur6(3660,UNCOMMON),
        Pur63(3661,UNCOMMON),
        Pur7(3661,UNCOMMON),
        drygore(3647,RARE),
        drygore1(3648,RARE),
        drygore2(3649,RARE),
        drygore3(3650,RARE),
        drygore4(3651,RARE),
        drygore5(3652,RARE),
        drygore6(3653,RARE);


        private static final ImmutableSet<MysteryBoxReward> REWARDS = Sets
                .immutableEnumSet(EnumSet.allOf(MysteryBoxReward.class));
        private final int itemId;
        private final int amount;
        private final ItemRarity rarity;

        MysteryBoxReward(int itemId, ItemRarity rarity) {
            this.itemId = itemId;
            this.amount = 1;
            this.rarity = rarity;
        }

        MysteryBoxReward(int itemId, int amount, ItemRarity rarity) {
            this.itemId = itemId;
            this.amount = amount;
            this.rarity = rarity;
        }

        public static Optional<MysteryBoxReward> get(ItemRarity rarity) {
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
