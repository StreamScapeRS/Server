package com.StreamScape.world.content;

import static com.StreamScape.model.ItemRarity.COMMON;
import static com.StreamScape.model.ItemRarity.LEGENDARY;
import static com.StreamScape.model.ItemRarity.RARE;
import static com.StreamScape.model.ItemRarity.UNCOMMON;
import static com.StreamScape.model.ItemRarity.VERY_RARE;
import static com.StreamScape.model.ItemRarity.COMMMONI;
import static com.StreamScape.model.ItemRarity.COMMMONII;

import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.StreamScape.model.Animation;
import com.StreamScape.model.ItemRarity;
import com.StreamScape.util.Misc;
import com.StreamScape.world.entity.impl.player.Player;


// Author: Chris AKA Hacker.

public class MysteryChest {

    private static final int KEY = 989;
    private static final int KEYI = 17000;
    private static final int KEYII = 17001;
    /**
     * Handles the action of clicking the chest.
     *
     * @param player     The player in this action.
     * @param gameObject
     */
    public static void handleChest(Player player) {
        if (player.getInventory().contains(KEY)) {

            double numGen = Math.random();

            ItemRarity rarity = numGen >= 0.80 ? ItemRarity.COMMON
                    : numGen >= 0.60 ? ItemRarity.UNCOMMON
                    : numGen >= 0.30 ? ItemRarity.RARE
                    : numGen >= 0.10 ? ItemRarity.VERY_RARE : ItemRarity.LEGENDARY;

            Optional<MysteryChestReward> reward = MysteryChestReward.get(rarity);
            if (reward.isPresent()) {
                player.performAnimation(new Animation(827));
                player.getInventory().delete(KEY, 1);
                player.getPacketSender().sendMessage("You open the chest..");
                player.getInventory().add(reward.get().getItemId(), reward.get().getAmount()).refreshItems();
            } else {
                player.sendMessage("Critical error: Report to staff");
            }
        }
        if(player.getInventory().contains(KEYI)) {

            Optional<MysteryChestReward> reward = MysteryChestReward.get(ItemRarity.COMMMONI);
            if (reward.isPresent()) {
                player.performAnimation(new Animation(827));
                player.getInventory().delete(KEYI, 1);
                player.getPacketSender().sendMessage("You open the chest..");
                player.getInventory().add(reward.get().getItemId(), reward.get().getAmount()).refreshItems();
            } else {
                player.sendMessage("Critical error: Report to staff");
            }

        }
        if(player.getInventory().contains(KEYII)) {

            double numGen = Math.random();

            ItemRarity rarity = numGen >= 0.80 ? ItemRarity.COMMON
                    : numGen >= 0.60 ? ItemRarity.UNCOMMON
                    : numGen >= 0.30 ? ItemRarity.RARE
                    : numGen >= 0.10 ? ItemRarity.VERY_RARE : ItemRarity.LEGENDARY;

            Optional<MysteryChestReward> reward = MysteryChestReward.get(ItemRarity.COMMMONII);
            if (reward.isPresent()) {
                player.performAnimation(new Animation(827));
                player.getInventory().delete(KEYII, 1);
                player.getPacketSender().sendMessage("You open the chest..");
                player.getInventory().add(reward.get().getItemId(), reward.get().getAmount()).refreshItems();
            } else {
                player.sendMessage("Critical error: Report to staff");
            }

        } else {


            // if no key:
            player.sendMessage("You do not have a key to open this chest!");
        }
    }

    /**
     * Reward Table.
     */
    public enum MysteryChestReward {


        ITEM1(995, 100000, COMMMONI),
        Morune1(1079, 1, COMMMONI),
        Morune2(1127, 1, COMMMONI),
        Morune3(1163, 1, COMMMONI),
        Morune4(4131, 1, COMMMONI),
        Morune5(1201, 1, COMMMONI),
        Morune6(1213, 1, COMMMONI),
        Bluedragon(3641, 1, COMMMONI),
        Bluedragon1(3642, 1, COMMMONI),
        Bluedragon2(1187, 1, COMMMONI),
        Bluedragon3(11732, 1, COMMMONI),
        Bluedragon4(4986, 1, COMMMONI),
        Apollo(3644, 1, COMMMONI),
        Apollo2(3645, 1, COMMMONI),
        Apollo3(3646, 1, COMMMONI),
        Apolleo3(20696, 1, COMMMONI),
        Apolleo3e(20693, 1, COMMMONI),



        ITEM1w(995, 100000, COMMMONII),
        Morune1qwe(1079, 1, COMMMONII),
        Moruneeqw2(1127, 1, COMMMONII),
        Morunwqee3(1163, 1, COMMMONII),
        Morunewqe4(4131, 1, COMMMONII),
        Moruqwene5(1201, 1, COMMMONII),
        Morunwe6(1213, 1, COMMMONII),
        Bluedqweragon(3641, 1, COMMMONII),
        Bluedwragon1(3642, 1, COMMMONII),
        Bluedrwagon2(1187, 1, COMMMONII),
        Bluedrawgon3(11732, 1, COMMMONII),
        Bluedragwon4(4986, 1, COMMMONII),
        Apoello(3644, 1, COMMMONII),
        Apolelo2(3645, 1, COMMMONII),
        Apolleow3(3646, 1, COMMMONII),
        Apolleewro3(20696, 1, COMMMONII),
        Apolleewro3e(20693, 1, COMMMONII),
        Apolleewrower3e(3807, 1, COMMMONII),
        Apolleewwefro3e(3808, 1, COMMMONII),
        Apolleewewfro3e(3809, 1, COMMMONII),
        Apolleewfewfro3e(16000, 1, COMMMONII),
        Apolleewewwfro3e(16001, 1, COMMMONII),
        Apolleeewewfro3e(16002, 1, COMMMONII),
        Apolleeewewfrfwo3e(20691, 1, COMMMONII),
        Apolleeewewfwfro3e(20697, 1, COMMMONII),
        Apolleeewewfwwfro3e(20694, 1, COMMMONII),
        wfef(20697, 1, COMMMONII),
        Apolleeeww3e(20695, 1, COMMMONII),





        wqITEM1gsw(995, 100000, COMMMONII),
        Mowrune1qsgwe(1079, 1, COMMMONII),
        Meoruneegsqw2(1127, 1, COMMMONII),
        Morsfunwqeegs3(1163, 1, COMMMONII),
        Morsfunewsdfqe4(4131, 1, COMMMONII),
        Morugqwdfene5(1201, 1, COMMMONII),
        Morusdfnwesdf6(1213, 1, COMMMONII),
        Bluedqfdgwesgragon(3641, 1, COMMMONII),
        Bluedwfdgragon1(3642, 1, COMMMONII),
        Bluedrswgsagon2(1187, 1, COMMMONII),
        Bluefdragffwgon3(11732, 1, COMMMONII),
        Blueddragdgwon4(4986, 1, COMMMONII),
        ITEMgf1w(995, 100000, COMMMONII),
        Morugdfn4e1qwe(1079, 1, COMMMONII),
        Morun3sfdg2eeqw2(1127, 1, COMMMONII),
        Moru5nwqedfge3(1163, 1, COMMMONII),
        Mgdfsor3unewqe4(4131, 1, COMMMONII),
        Morudfsg4qwene5(1201, 1, COMMMONII),
        Morun6wdsfge6(1213, 1, COMMMONII),
        Blue5d4qwegfdragon(3641, 1, COMMMONII),
        Blu4edwragongd1(3642, 1, COMMMONII),
        Bl3gdfguedrwagon2(1187, 1, COMMMONII),
        B2ldfgdfuedrawgon3(11732, 1, COMMMONII),
        efBgdgluedragwon4(4986, 1, COMMMONII),
        IdTdfsgEM1w(995, 100000, COMMMONII),
        Morundfgdfgey1qwe(1079, 1, COMMMONII),
        Morudfgdsfgnteeqw2(1127, 1, COMMMONII),
        Morudfgdfgdsgrnwqee3(1163, 1, COMMMONII),
        Moredgfdfgdfgdfunewqe4(4131, 1, COMMMONII),
        Mowrdfdfuqwene5(1201, 1, COMMMONII),
        Mqorungdfwe6(1213, 1, COMMMONII),
        Bluedqwefdvragon(3641, 1, COMMMONII),
        Bldudedwrsagon1(3642, 1, COMMMONII),
        Bludsgeddsrwafgon2(1187, 1, COMMMONII),
        Bluedsdgdrawgon3(11732, 1, COMMMONII),
        Blueedrdfgagwon4(4986, 1, COMMMONII),
        ITEMwgdfsg1w(995, 100000, COMMMONII),
        Morugrewne1qwe(1079, 1, COMMMONII),
        Morurregneeqw2(1127, 1, COMMMONII),
        Morufnwhweqee3(1163, 1, COMMMONII),
        Moruewrwetwrhrnewqe4(4131, 1, COMMMONII),
        Morqweruqwentrhwe5(1201, 1, COMMMONII),
        Morwerhwrtunwe6(1213, 1, COMMMONII),
        Bluwererthdqweragon(3641, 1, COMMMONII),
        Blueqwrtherdwragon1(3642, 1, COMMMONII),
        Bluedrrhwtrthwwerewragon2(1187, 1, COMMMONII),
        Blueqwtrhdrawgon3(11732, 1, COMMMONII),
        Blueedrtwhsgragwon4(4986, 1, COMMMONII),






        ITEM1gsw(995, 100000, COMMMONII),
        Morune1qsgwe(1079, 1, COMMMONII),
        Moruneegsqw2(1127, 1, COMMMONII),
        Morunwqeegs3(1163, 1, COMMMONII),
        Morunewsdfqe4(4131, 1, COMMMONII),
        Moruqwdfene5(1201, 1, COMMMONII),
        Morunwesdf6(1213, 1, COMMMONII),
        Bluedqwesgragon(3641, 1, COMMMONII),
        Bluedwfdragon1(3642, 1, COMMMONII),
        Bluedrswagon2(1187, 1, COMMMONII),
        Bluefdrawgon3(11732, 1, COMMMONII),
        Blueddragwon4(4986, 1, COMMMONII),
        ITEMf1w(995, 100000, COMMMONII),
        Morun4e1qwe(1079, 1, COMMMONII),
        Morun32eeqw2(1127, 1, COMMMONII),
        Moru5nwqee3(1163, 1, COMMMONII),
        Mor3unewqe4(4131, 1, COMMMONII),
        Moru4qwene5(1201, 1, COMMMONII),
        Morun6we6(1213, 1, COMMMONII),
        Blue5d4qweragon(3641, 1, COMMMONII),
        Blu4edwragon1(3642, 1, COMMMONII),
        Bl3uedrwagon2(1187, 1, COMMMONII),
        B2luedrawgon3(11732, 1, COMMMONII),
        efBluedragwon4(4986, 1, COMMMONII),
        IdTEM1w(995, 100000, COMMMONII),
        Moruney1qwe(1079, 1, COMMMONII),
        Morunteeqw2(1127, 1, COMMMONII),
        Morurnwqee3(1163, 1, COMMMONII),
        Moreunewqe4(4131, 1, COMMMONII),
        Mowruqwene5(1201, 1, COMMMONII),
        Mqorunwe6(1213, 1, COMMMONII),
        Bluedqwevragon(3641, 1, COMMMONII),
        Bluedwrsagon1(3642, 1, COMMMONII),
        Blueddsrwagon2(1187, 1, COMMMONII),
        Blueddrawgon3(11732, 1, COMMMONII),
        Blueedragwon4(4986, 1, COMMMONII),
        ITEMw1w(995, 100000, COMMMONII),
        Moruwne1qwe(1079, 1, COMMMONII),
        Morurrneeqw2(1127, 1, COMMMONII),
        Morufnwqee3(1163, 1, COMMMONII),
        Moruewrwernewqe4(4131, 1, COMMMONII),
        Morqweruqwene5(1201, 1, COMMMONII),
        Morwerunwe6(1213, 1, COMMMONII),
        Bluweredqweragon(3641, 1, COMMMONII),
        Blueqwerdwragon1(3642, 1, COMMMONII),
        Bluedrwwerewragon2(1187, 1, COMMMONII),
        Blueqwdrawgon3(11732, 1, COMMMONII),
        Blueedsgragwon4(4986, 1, COMMMONII),


        /*
         * COMMON
         */
        COINS_100k(995, 100000, COMMON), LOGS(1512, 6, COMMON), OAK(1522, 5, COMMON), WILLOW(1520, 4, COMMON), MAPLE(
                1518, 3, COMMON), YEW(1516, 2, COMMON), MAGIC(1513, 1, COMMON), AIR(556, 30, COMMON), WATER(555, 20,
                COMMON), FIRE(554, 20, COMMON), EARTH(557, 20, COMMON), MIND(558, 35, COMMON), BODY(559, 30,
                COMMON), COSMIC(564, 10, COMMON), CHOAS(562, 25, COMMON), SOUL(566, 10, COMMON), ASTRAL(
                9075, 10, COMMON), LAW(563, 10, COMMON), NATURE(561, 10, COMMON), DEATH(560, 15,
                COMMON), BLOOD(565, 10, COMMON), GRIMY(200, 5, COMMON), GRIMY1(202, 4,
                COMMON), GRIMY2(204, 3, COMMON), GRIMY3(206, 2, COMMON), GRIMY4(
                208, 2, COMMON), GRIMY5(210, 2, COMMON), GRIMY6(212, 1,
                COMMON), GRIMY7(214, 1, COMMON), GRIMY8(216, 1,
                COMMON), ESS(1437, 25,
                COMMON), PURE_ESS(7937, 10,
                COMMON), COPPER(437, 5,
                COMMON), TIN(
                439, 5,
                COMMON), ICE(
                441,
                5,
                COMMON), COAL(
                454,
                5,
                COMMON), GOLD(
                445,
                4,
                COMMON), MITHRIL(
                448,
                3,
                COMMON), ADMIN(
                450,
                2,
                COMMON), MORUNE(
                452,
                1,
                COMMON), LOBSTER(
                380,
                5,
                COMMON), SWORDFISH(
                374,
                3,
                COMMON), SHARK(
                386,
                2,
                COMMON), ROCKTAIL(
                15272,
                1,
                COMMON), STR(
                2441,
                1,
                COMMON), ATT(
                2437,
                1,
                COMMON), DEF(
                2443,
                1,
                COMMON), PRAYER(
                2435,
                1,
                COMMON), RESTORE(
                3025,
                1,
                COMMON), RANGE(
                2445,
                1,
                COMMON), MAGIC_POT(
                3041,
                1,
                COMMON), SEED(
                5291,
                5,
                COMMON), SEED1(
                5292,
                5,
                COMMON), SEED2(
                5293,
                5,
                COMMON), SEED3(
                5294,
                5,
                COMMON), SEED4(
                5295,
                5,
                COMMON), SEED5(
                5296,
                5,
                COMMON), SEED6(
                5297,
                5,
                COMMON), SEED7(
                5298,
                4,
                COMMON), SEED8(
                5299,
                4,
                COMMON), SEED9(
                5300,
                4,
                COMMON), SEED10(
                5301,
                4,
                COMMON), SEED11(
                5302,
                3,
                COMMON), SEED12(
                5303,
                3,
                COMMON), SEED13(
                5304,
                3,
                COMMON), SEED14(
                5318,
                3,
                COMMON), SEED15(
                5096,
                3,
                COMMON), SEED16(
                5319,
                3,
                COMMON), SEED17(
                5324,
                3,
                COMMON), SEED18(
                5322,
                2,
                COMMON), SEED19(
                5320,
                2,
                COMMON), SEED20(
                5323,
                2,
                COMMON), SEED21(
                5097,
                2,
                COMMON), SEED22(
                5098,
                1,
                COMMON), SEED23(
                5099,
                1,
                COMMON), SEED24(
                5100,
                1,
                COMMON), ICE_LEGS(
                1067,
                COMMON), ICE_PL8(
                1115,
                COMMON), ICE_HELM(
                1153,
                COMMON), ICE_BOOTS(
                4121,
                COMMON), ICE_KITE(
                1191,
                COMMON), ICE_JAV(
                826,
                10,
                COMMON), ICE_KNIVES(
                863,
                10,
                COMMON), ICE_THROWN(
                801,
                10,
                COMMON), ICE_SPEAR(
                1239,
                COMMON), ICE_DARTS(
                807,
                10,
                COMMON), ICE_SWORD(
                1279,
                COMMON), ICE_BAXE(
                1363,
                COMMON), ICE_2H(
                1309,
                COMMON), ICE_SQ(
                1175,
                COMMON), ICE_DAGGER(
                1203,
                COMMON), ICE_PICKAXE(
                1267,
                COMMON), ICE_HATCHET(
                1349,
                COMMON), ICE_CROSSBOW(
                9177,
                COMMON), ICE_HALBERD(
                3192,
                COMMON), STARTER_CLAWS(
                3096,
                COMMON), ICE_MACE(
                1420,
                COMMON), ICE_WARHAMMER(
                1335,
                COMMON),
        /*
         * UNCOMMON
         */
        LOGS1(1512, 7, UNCOMMON), OAK1(1522, 6, UNCOMMON), WILLOW1(1520, 5, UNCOMMON), MAPLE1(1518, 4, UNCOMMON), YEW1(
                1516, 3, UNCOMMON), MAGIC1(1513, 2, UNCOMMON), AIR1(556, 35, UNCOMMON), WATE1R(555, 25,
                UNCOMMON), FIRE1(554, 25, UNCOMMON), EART1H(557, 25, UNCOMMON), MIN1D(558, 40, UNCOMMON), BOD1Y(
                559, 35, UNCOMMON), COS1MIC(564, 15, UNCOMMON), CHO1AS(562, 30, UNCOMMON), SOU1L(566,
                15, UNCOMMON), ASTR1AL(9075, 15, UNCOMMON), LAW1(563, 15, UNCOMMON), NATU1RE(
                561, 15,
                UNCOMMON), DEAT1H(560, 20, UNCOMMON), BLOO1D(565, 15, UNCOMMON), GRIM1Y(
                200, 6, UNCOMMON), GRIM1Y1(202, 5, UNCOMMON), GRIMI1Y2(204, 4,
                UNCOMMON), GRIM1Y3(206, 3, UNCOMMON), GRIM1Y4(208, 3,
                UNCOMMON), GRIM1Y5(210, 3, UNCOMMON), GRIMY16(
                212, 2, UNCOMMON), GRIMY17(214, 2,
                UNCOMMON), GRIMY18(216, 2,
                UNCOMMON), ES1S(1437,
                30,
                UNCOMMON), PUR1E_ESS(
                7937,
                15,
                UNCOMMON), COPP1ER(
                437,
                7,
                UNCOMMON), T1IN(
                439,
                7,
                UNCOMMON), IICE(
                441,
                7,
                UNCOMMON), ICOAL(
                454,
                6,
                UNCOMMON), GOILD(
                445,
                5,
                UNCOMMON), MIITHRIL(
                448,
                4,
                UNCOMMON), ADMIIN(
                450,
                2,
                UNCOMMON), MORUINE(
                452,
                2,
                UNCOMMON), LOBSTIER(
                380,
                6,
                UNCOMMON), SWORDFIISH(
                374,
                4,
                UNCOMMON), ISHARK(
                386,
                3,
                UNCOMMON), RIOCKTAIL(
                15272,
                2,
                UNCOMMON), STIR(
                2441,
                2,
                UNCOMMON), ATTI(
                2437,
                2,
                UNCOMMON), IDEF(
                2443,
                2,
                UNCOMMON), PIRAYER(
                2435,
                2,
                UNCOMMON), REISTORE(
                3025,
                2,
                UNCOMMON), RANIGE(
                2445,
                2,
                UNCOMMON), MAGIIC_POT(
                3041,
                2,
                UNCOMMON), ISEED(
                5291,
                6,
                UNCOMMON), SIEED1(
                5292,
                6,
                UNCOMMON), SIEED2(
                5293,
                6,
                UNCOMMON), SEIED3(
                5294,
                6,
                UNCOMMON), SEEID4(
                5295,
                6,
                UNCOMMON), SEEDI5(
                5296,
                6,
                UNCOMMON), SEED6I(
                5297,
                6,
                UNCOMMON), SIEED7(
                5298,
                5,
                UNCOMMON), SEIIED8(
                5299,
                5,
                UNCOMMON), SEEID9(
                5300,
                5,
                UNCOMMON), SEEID10(
                5301,
                5,
                UNCOMMON), SEEDI11(
                5302,
                4,
                UNCOMMON), SEED1I2(
                5303,
                4,
                UNCOMMON), SEED13I(
                5304,
                4,
                UNCOMMON), SEEDI14(
                5318,
                4,
                UNCOMMON), ISEED15(
                5096,
                4,
                UNCOMMON), SIEED16(
                5319,
                4,
                UNCOMMON), SEIED17(
                5324,
                4,
                UNCOMMON), SEEID18(
                5322,
                3,
                UNCOMMON), SEEDI19(
                5320,
                3,
                UNCOMMON), SEED2I0(
                5323,
                3,
                UNCOMMON), SEEID21(
                5097,
                3,
                UNCOMMON), ISEED22(
                5098,
                2,
                UNCOMMON), SIEED23(
                5099,
                2,
                UNCOMMON), SEIED24(
                5100,
                2,
                UNCOMMON), COINS_1M(
                995,
                1000000,
                UNCOMMON), MKEY(
                989,
                1,
                UNCOMMON), COINS_10M(
                995,
                10000000,
                UNCOMMON), STAT_RESET(
                13663,
                UNCOMMON), MORUNE_LEGS(
                1079,
                UNCOMMON), MORUNE_PL8(
                1127,
                UNCOMMON), MORUNE_HELM(
                1163,
                UNCOMMON), MORUNE_BOOTS(
                4131,
                UNCOMMON), MORUNE_KITE(
                1201,
                UNCOMMON), MORUNE_JAV(
                830,
                10,
                UNCOMMON), MORUNE_KNIVES(
                868,
                10,
                UNCOMMON), MORUNE_THROWN(
                805,
                10,
                UNCOMMON), MORUNE_SPEAR(
                1247,
                UNCOMMON), MORUNE_DARTS(
                811,
                10,
                UNCOMMON), MORUNE_SWORD(
                1289,
                UNCOMMON), MORUNE_BAXE(
                1373,
                UNCOMMON), MORUNE_2H(
                1319,
                UNCOMMON), MORUNE_SQ(
                1185,
                UNCOMMON), MORUNE_DAGGER(
                1213,
                UNCOMMON), MORUNE_PICKAXE(
                1275,
                UNCOMMON), MORUNE_HATCHET(
                1359,
                UNCOMMON), MORUNE_CROSSBOW(
                9185,
                UNCOMMON), MORUNE_HALBERD(
                3202,
                UNCOMMON), MORUNE_CLAWS(
                3101,
                UNCOMMON), MORUNE_MACE(
                1432,
                UNCOMMON), MORUNE_WARHAMMER(
                1347,
                UNCOMMON),
        /*
         * RARE
         */
        ILOGS1(1512, 8, RARE), OIAK1(1522, 7, RARE), WIILLOW1(1520, 6, RARE), MAPILE1(1518, 5, RARE), YEW1I(1516, 4,
                RARE), IMAGIC1(1513, 3, RARE), AIIR1(556, 40, RARE), WAITE1R(555, 30, RARE), FIRIE1(554, 30,
                RARE), EARTI1H(557, 30, RARE), MIN1DI(558, 45, RARE), BOID1Y(559, 40, RARE), ICOS1MIC(564, 20,
                RARE), CIHO1AS(562, 35, RARE), SOIU1L(566, 20, RARE), ASTIR1AL(9075, 20,
                RARE), LAW1I(563, 20, RARE), NATU1IRE(561, 20, RARE), DIEAT1H(560, 25,
                RARE), BILOO1D(565, 20, RARE), GIRIM1Y(200, 7, RARE), GIRIM1Y1(202, 6,
                RARE), GRIM1Y2(204, 5, RARE), GRIIIM1Y3(206, 4, RARE), GRIMI1Y4(
                208, 4, RARE), GRIM1IY5(210, 4, RARE), GRIMY1I6(212, 3,
                RARE), GRIIMY17(214, 3, RARE), GRIIMY18(216, 3,
                RARE), ES1SI(1437, 35,
                RARE), PUR1EI_ESS(7937, 20,
                RARE), COPP1IER(437, 8,
                RARE), T1IIN(
                439, 8,
                RARE), IICIE(
                441,
                8,
                RARE), ICOAIL(
                454,
                7,
                RARE), GOIILD(
                445,
                6,
                RARE), MIIITHRIL(
                448,
                5,
                RARE), ADMIIIN(
                450,
                3,
                RARE), MORUIINE(
                452,
                3,
                RARE), LOBSTIIER(
                380,
                7,
                RARE), SWORDFIIISH(
                374,
                5,
                RARE), ISHAIRK(
                386,
                4,
                RARE), RIOICKTAIL(
                15272,
                3,
                RARE), STIIR(
                2441,
                3,
                RARE), ATTII(
                2437,
                3,
                RARE), IDIEF(
                2443,
                3,
                RARE), PIRAIYER(
                2435,
                3,
                RARE), REIIISTORE(
                3025,
                3,
                RARE), RANIGIE(
                2445,
                3,
                RARE), MAGIICI_POT(
                3041,
                3,
                RARE), ISEEID(
                5291,
                7,
                RARE), SIEEID1(
                5292,
                7,
                RARE), SIEEID2(
                5293,
                7,
                RARE), SEIEDI3(
                5294,
                7,
                RARE), SEEIID4(
                5295,
                7,
                RARE), SEEIDI5(
                5296,
                7,
                RARE), SEEID6I(
                5297,
                7,
                RARE), SIEEID7(
                5298,
                6,
                RARE), SEIIIIED8(
                5299,
                6,
                RARE), SEEIID9(
                5300,
                6,
                RARE), SEEIDI10(
                5301,
                6,
                RARE), SEEDI11I(
                5302,
                5,
                RARE), ISEED1I2(
                5303,
                5,
                RARE), SIEED13I(
                5304,
                5,
                RARE), SEIEDI14(
                5318,
                5,
                RARE), ISEIED15(
                5096,
                5,
                RARE), SIEEID16(
                5319,
                5,
                RARE), SEIEDI17(
                5324,
                5,
                RARE), SEEID1I8(
                5322,
                4,
                RARE), SEEDII19(
                5320,
                4,
                RARE), SEEIID2I0(
                5323,
                4,
                RARE), SEEIID21(
                5097,
                4,
                RARE), ISEIED22(
                5098,
                3,
                RARE), SIEIED23(
                5099,
                3,
                RARE), SEIIED24(
                5100,
                3,
                RARE), COINS_15M(
                995,
                1500000,
                RARE), MKEY_1(
                989,
                RARE), COINS_20M(
                995,
                20000000,
                RARE), STAT_RESET_1(
                13663,
                RARE), BLUE_HATCHET(
                6739,
                RARE), BLUE_BOOTS(
                11732,
                RARE), BLUE_HELM(
                3642,
                RARE), BLUE_BODY(
                3641,
                RARE), BLUE_LEGS(
                4087,
                RARE), BLUE_SPEAR(
                1249,
                RARE), BLUE_SQ(
                1187,
                RARE), BLUE_BAXE(
                1377,
                RARE),
        row(2572,UNCOMMON),


        BLUE_MACE(
                1434,
                RARE), BLUE_HALBERD(
                3204,
                RARE), BLUE_DDS(
                5698,
                RARE), BLUE_FLAIL(
                6756,
                RARE),
        /*
         * VERY RARE
         */
        LOG1S1(1512, 70, VERY_RARE), OAK11(1522, 60, VERY_RARE), WILL1OW1(1520, 50, VERY_RARE), MAP1LE1(1518, 40,
                VERY_RARE), YEW11(1516, 30, VERY_RARE), MAGI1C1(1513, 20, VERY_RARE), AIR11(556, 350,
                VERY_RARE), WATE11R(555, 250, VERY_RARE), FIRE11(554, 250, VERY_RARE), EART11H(557, 250,
                VERY_RARE), MIN11D(558, 400, VERY_RARE), BOD11Y(559, 350, VERY_RARE), COS11MIC(564, 150,
                VERY_RARE), CHO11AS(562, 300, VERY_RARE), SOU1L1(566, 150, VERY_RARE), ASTR11AL(
                9075, 150, VERY_RARE), L1AW1(563, 150, VERY_RARE), NAT1U1RE(561, 150,
                VERY_RARE), DEA11T1H(560, 200, VERY_RARE), BLO1O1D(565, 150,
                VERY_RARE), GRIM11Y(200, 60, VERY_RARE), GRIM11Y1(202,
                50, VERY_RARE), GRIM11I1Y2(204, 40,
                VERY_RARE), GRI1M1Y3(206, 30,
                VERY_RARE), GRI1M1Y4(208, 30,
                VERY_RARE), GRI1M1Y5(
                210, 30,
                VERY_RARE), GRI1MY16(
                212, 20,
                VERY_RARE), GRI1MY17(
                214,
                20,
                VERY_RARE), GRI1MY18(
                216,
                20,
                VERY_RARE), ES11S(
                1437,
                300,
                VERY_RARE), PUR1E1_ESS(
                7937,
                150,
                VERY_RARE), COP1P1ER(
                437,
                70,
                VERY_RARE), T1IN1(
                439,
                70,
                VERY_RARE), IIC1E(
                441,
                70,
                VERY_RARE), ICO1AL(
                454,
                60,
                VERY_RARE), GOI1LD(
                445,
                50,
                VERY_RARE), MIIT1HRIL(
                448,
                40,
                VERY_RARE), ADMII11N(
                450,
                20,
                VERY_RARE), MORUI1NE(
                452,
                20,
                VERY_RARE), LOBS1TIER(
                380,
                60,
                VERY_RARE), SWOR11DFIISH(
                374,
                40,
                VERY_RARE), ISHA1RK(
                386,
                30,
                VERY_RARE), RI1OCKTAIL(
                15272,
                20,
                VERY_RARE), ST1IR(
                2441,
                20,
                VERY_RARE), AT1TI(
                2437,
                20,
                VERY_RARE), ID1EF(
                2443,
                20,
                VERY_RARE), PI11RAYER(
                2435,
                20,
                VERY_RARE), RE1ISTORE(
                3025,
                20,
                VERY_RARE), R1ANIGE(
                2445,
                20,
                VERY_RARE), M1AGIIC_POT(
                3041,
                20,
                VERY_RARE), ISEE1D(
                5291,
                60,
                VERY_RARE), SIEE11D1(
                5292,
                60,
                VERY_RARE), SIEE1D2(
                5293,
                60,
                VERY_RARE), SEI1ED3(
                5294,
                60,
                VERY_RARE), SEE1ID4(
                5295,
                60,
                VERY_RARE), SEEDI15(
                5296,
                60,
                VERY_RARE), SEED16I(
                5297,
                60,
                VERY_RARE), SIEE1D7(
                5298,
                50,
                VERY_RARE), SEII1ED8(
                5299,
                50,
                VERY_RARE), SEEI1D9(
                5300,
                50,
                VERY_RARE), S1EEID10(
                5301,
                50,
                VERY_RARE), SE1EDI11(
                5302,
                40,
                VERY_RARE), SEE1D1I2(
                5303,
                40,
                VERY_RARE), SEED113I(
                5304,
                40,
                VERY_RARE), SEEDI114(
                5318,
                40,
                VERY_RARE), ISEED115(
                5096,
                40,
                VERY_RARE), SIEED116(
                5319,
                40,
                VERY_RARE), S1EIED17(
                5324,
                40,
                VERY_RARE), SE1EID18(
                5322,
                30,
                VERY_RARE), SEE11DI19(
                5320,
                30,
                VERY_RARE), SEED21I0(
                5323,
                30,
                VERY_RARE), S1EEID21(
                5097,
                30,
                VERY_RARE), IS1EED22(
                5098,
                20,
                VERY_RARE), SIE1ED23(
                5099,
                20,
                VERY_RARE), SEIE1D24(
                5100,
                20,
                VERY_RARE), COINS_25M(
                995,
                2500000,
                VERY_RARE), MKEY_2(
                989,
                VERY_RARE), COINS_30M(
                995,
                30000000,
                VERY_RARE), RB(
                13664,
                VERY_RARE), COINS_35M(
                995,
                3500000,
                VERY_RARE), MKEY_3(
                989,
                VERY_RARE), COINS_40M(
                995,
                40000000,
                VERY_RARE), MKEY_4(
                989,
                VERY_RARE), COINS_45M(
                995,
                4500000,
                VERY_RARE), MKEY_5(
                989,
                VERY_RARE), COINS_50M(
                995,
                50000000,
                VERY_RARE), MKEY_6(
                989,
                VERY_RARE), ORNATE_KATANA(
                14018,
                VERY_RARE),
        /*
         * Legendary
         */
        L1OG1S1(1512, 80, VERY_RARE), OA1K11(1522, 70, VERY_RARE), WIL1L1OW1(1520, 60, VERY_RARE), MAP31LE1(1518, 50,
                VERY_RARE), YEW111(1516, 40, VERY_RARE), MAGI11C1(1513, 30, VERY_RARE), A1IR11(556, 450,
                VERY_RARE), WA1TE11R(555, 350, VERY_RARE), FIR1E11(554, 350, VERY_RARE), EART111H(557, 350,
                VERY_RARE), MIN111D(558, 500, VERY_RARE), BOD11Y1(559, 450, VERY_RARE), COS11MI1C(564,
                250, VERY_RARE), C1HO11AS(562, 400, VERY_RARE), SO1U1L1(566, 250,
                VERY_RARE), AST1R11AL(9075, 250, VERY_RARE), L1AW11(563, 250,
                VERY_RARE), N1AT1U1RE(561, 250, VERY_RARE), DE1A11T1H(560, 300,
                VERY_RARE), BLO11O1D(565, 250, VERY_RARE), GRIM111Y(200,
                70, VERY_RARE), GRIM111Y1(202, 60,
                VERY_RARE), GRIM111I1Y2(204, 50,
                VERY_RARE), GRI1M1Y13(206, 40,
                VERY_RARE), GRI1M1Y41(
                208, 40,
                VERY_RARE), G1RI1M1Y5(
                210, 40,
                VERY_RARE), GR1I1MY16(
                212,
                30,
                VERY_RARE), GRI1MY117(
                214,
                30,
                VERY_RARE), GRI11MY18(
                216,
                30,
                VERY_RARE), ES1111S(
                1437,
                300,
                VERY_RARE), PUR1E11_ESS(
                7937,
                250,
                VERY_RARE), COP1P11ER(
                437,
                80,
                VERY_RARE), T1IN11(
                4139,
                80,
                VERY_RARE), IIC11E(
                441,
                80,
                VERY_RARE), ICO11AL(
                454,
                70,
                VERY_RARE), GOI1L1D(
                445,
                60,
                VERY_RARE), MIIT1H1RIL(
                448,
                50,
                VERY_RARE), ADMII111N(
                450,
                30,
                VERY_RARE), MORUI1NE1(
                452,
                30,
                VERY_RARE), LOBS1T1IER(
                380,
                70,
                VERY_RARE), SW1OR11DFIISH(
                374,
                50,
                VERY_RARE), ISH1A1RK(
                386,
                40,
                VERY_RARE), RI1O1CKTAIL(
                15272,
                30,
                VERY_RARE), ST1IR1(
                2441,
                30,
                VERY_RARE), A1T1TI(
                2437,
                30,
                VERY_RARE), ID11EF(
                2443,
                30,
                VERY_RARE), PI111RAYER(
                2435,
                30,
                VERY_RARE), RE1I1STORE(
                3025,
                30,
                VERY_RARE), R1ANI1GE(
                2445,
                30,
                VERY_RARE), M1AGII1C_POT(
                3041,
                30,
                VERY_RARE), ISEE1D1(
                5291,
                70,
                VERY_RARE), SIEE111D1(
                5292,
                70,
                VERY_RARE), SI1EE1D2(
                5293,
                70,
                VERY_RARE), S1EI1ED3(
                5294,
                70,
                VERY_RARE), SE1E1ID4(
                5295,
                70,
                VERY_RARE), SEE1DI15(
                5296,
                70,
                VERY_RARE), SEED116I(
                5297,
                70,
                VERY_RARE), SIEE11D7(
                5298,
                60,
                VERY_RARE), SEII1E11D8(
                5299,
                60,
                VERY_RARE), S1EEI1D9(
                5300,
                60,
                VERY_RARE), S11EEID10(
                5301,
                60,
                VERY_RARE), S1E1EDI11(
                5302,
                50,
                VERY_RARE), SE1E1D1I2(
                5303,
                50,
                VERY_RARE), SEED1113I(
                5304,
                50,
                VERY_RARE), SEE1DI114(
                5318,
                50,
                VERY_RARE), ISEE11D115(
                5096,
                50,
                VERY_RARE), SIEED1116(
                5319,
                50,
                VERY_RARE), S1EIED1117(
                5324,
                50,
                VERY_RARE), SE1E1ID18(
                5322,
                40,
                VERY_RARE), SEE11DI1119(
                5320,
                40,
                VERY_RARE), S1EED21I0(
                5323,
                40,
                VERY_RARE), S1EEID121(
                5097,
                40,
                VERY_RARE), IS11EED22(
                5098,
                30,
                VERY_RARE), SIE11ED23(
                5099,
                30,
                VERY_RARE), SEIE111D24(
                5100,
                30,
                VERY_RARE), COINS_55M(
                995,
                5500000,
                LEGENDARY), MKEY_7(
                989,
                LEGENDARY), COINS_60M(
                995,
                60000000,
                LEGENDARY), RB_1(
                13664,
                5,
                LEGENDARY), COINS_65M(
                995,
                6500000,
                LEGENDARY), MKEY_8(
                989,
                2,
                LEGENDARY), COINS_70M(
                995,
                70000000,
                LEGENDARY), MKEY_10(
                989,
                4,
                LEGENDARY), COINS_75M(
                995,
                7500000,
                LEGENDARY), MKEY_9(
                989,
                3,
                LEGENDARY), COINS_80M(
                995,
                80000000,
                LEGENDARY), MKEY_11(
                989,
                5,
                LEGENDARY), RB_2(
                13664,
                LEGENDARY), RB_3(
                13664,
                2,
                LEGENDARY), RB_4(
                13664,
                3,
                LEGENDARY), RB_5(
                13664,
                4,
                LEGENDARY), BEATZ(
                1543,
                LEGENDARY), BAT(
                3304,
                LEGENDARY), IPHONE(
                759,
                LEGENDARY), D_SLAY_SHIELD(
                3302,
                RARE), PURGATORY_SWORD(
                3660,
                RARE), PURGATORY_DEF(
                3661,
                RARE), PURGATORY_LEGS(
                3656,
                RARE), PURGATORY_PL8(
                3655,
                RARE), PURGATORY_HELM(
                3654,
                RARE), PURGATORY_BOOTS(
                3657,
                RARE), PURGATORY_GLOVES(
                3658,
                RARE), PURGATORY_WINGS(
                3659,
                LEGENDARY), RB_0(
                13664,
                10,
                LEGENDARY), RB_6(
                13664,
                15,
                LEGENDARY), RB_7(
                13664,
                20,
                LEGENDARY), MBOX(
                6199,
                LEGENDARY),
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

        private static final ImmutableSet<MysteryChestReward> REWARDS = Sets
                .immutableEnumSet(EnumSet.allOf(MysteryChestReward.class));
        private final int itemId;
        private final int amount;
        private final ItemRarity rarity;

        MysteryChestReward(int itemId, ItemRarity rarity) {
            this.itemId = itemId;
            this.amount = 1;
            this.rarity = rarity;
        }

        MysteryChestReward(int itemId, int amount, ItemRarity rarity) {
            this.itemId = itemId;
            this.amount = amount;
            this.rarity = rarity;
        }

        public static Optional<MysteryChestReward> get(ItemRarity rarity) {
            long count = REWARDS.stream().filter(reward -> reward.getRarity() == rarity).count();
            int randomIndex = Misc.RANDOM.nextInt((int) ((count - 0))) + 0; // count
            // -
            // 0)
            // +
            // 1
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
