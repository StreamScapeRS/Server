package com.StreamScape.net.packet.impl;

import com.StreamScape.GameSettings;
import com.StreamScape.engine.task.Task;
import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.engine.task.impl.ForceMovementTask;
import com.StreamScape.engine.task.impl.WalkToTask;
import com.StreamScape.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.StreamScape.model.*;
import com.StreamScape.model.Locations.Location;
import com.StreamScape.model.PlayerRanks.DonorRights;
import com.StreamScape.model.PlayerRanks.PlayerRights;
import com.StreamScape.model.container.impl.Bank;
import com.StreamScape.model.definitions.GameObjectDefinition;
import com.StreamScape.model.input.impl.DonateToWell;
import com.StreamScape.model.input.impl.EnterAmountOfLogsToAdd;
import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.clip.region.RegionClipping;
import com.StreamScape.world.content.CustomObjects;
import com.StreamScape.world.content.MysteryChest;
import com.StreamScape.world.content.WildernessObelisks;
import com.StreamScape.world.content.combat.magic.Autocasting;
import com.StreamScape.world.content.combat.prayer.CurseHandler;
import com.StreamScape.world.content.combat.prayer.PrayerHandler;
import com.StreamScape.world.content.combat.range.DwarfMultiCannon;
import com.StreamScape.world.content.combat.weapon.CombatSpecial;
import com.StreamScape.world.content.dialogue.DialogueManager;
import com.StreamScape.world.content.minigames.impl.Dueling;
import com.StreamScape.world.content.minigames.impl.Dueling.DuelRule;
import com.StreamScape.world.content.minigames.impl.Nomad;
import com.StreamScape.world.content.minigames.impl.RecipeForDisaster;
import com.StreamScape.world.content.skill.impl.agility.Agility;
import com.StreamScape.world.content.skill.impl.construction.Construction;
import com.StreamScape.world.content.skill.impl.crafting.Flax;
import com.StreamScape.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.StreamScape.world.content.skill.impl.fishing.Fishing;
import com.StreamScape.world.content.skill.impl.fishing.Fishing.Spot;
import com.StreamScape.world.content.skill.impl.hunter.Hunter;
import com.StreamScape.world.content.skill.impl.hunter.PuroPuro;
import com.StreamScape.world.content.skill.impl.mining.Mining;
import com.StreamScape.world.content.skill.impl.mining.MiningData;
import com.StreamScape.world.content.skill.impl.mining.Prospecting;
import com.StreamScape.world.content.skill.impl.runecrafting.Runecrafting;
import com.StreamScape.world.content.skill.impl.runecrafting.RunecraftingData;
import com.StreamScape.world.content.skill.impl.smithing.EquipmentMaking;
import com.StreamScape.world.content.skill.impl.smithing.Smelting;
import com.StreamScape.world.content.skill.impl.thieving.Stalls;
import com.StreamScape.world.content.skill.impl.woodcutting.Hatchet;
import com.StreamScape.world.content.skill.impl.woodcutting.Trees;
import com.StreamScape.world.content.skill.impl.woodcutting.Woodcutting;
import com.StreamScape.world.content.teleportation.TeleportHandler;
import com.StreamScape.world.content.teleportation.TeleportType;
import com.StreamScape.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player clicked on a game object.
 *
 * @author relex lawl
 */

public class ObjectActionPacketListener implements PacketListener {

    public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70, FOURTH_CLICK = 234,
            FIFTH_CLICK = 228;

    private static void fifthClick(final Player player, Packet packet) {
        final int id = packet.readUnsignedShortA();
        final int y = packet.readUnsignedShortA();
        final int x = packet.readShort();
        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        if (!Construction.buildingHouse(player)) {
            if (id > 0 && !RegionClipping.objectExists(gameObject)) {
                // player.getPacketSender().sendMessage("An error occured. Error
                // code: "+id).sendMessage("Please report the error to a staff
                // member.");
                return;
            }
        }
        player.setPositionToFace(gameObject.getPosition());
        int distanceX = player.getPosition().getX() - position.getX();
        int distanceY = player.getPosition().getY() - position.getY();
        if (distanceX < 0) {
            distanceX = -distanceX;
        }
        if (distanceY < 0) {
            distanceY = -distanceY;
        }
        int size = distanceX > distanceY ? distanceX : distanceY;
        gameObject.setSize(size);
        player.setInteractingObject(gameObject);
        player.setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
            @Override
            public void execute() {
                switch (id) {
                }
                Construction.handleFifthObjectClick(x, y, id, player);
            }
        }));
    }

    /**
     * The PacketListener logger to debug information and print out errors.
     */
    // private final static Logger logger =
    // Logger.getLogger(ObjectActionPacketListener.class);
    private static void firstClick(final Player player, Packet packet) {
        final int x = packet.readLEShortA();
        final int id = packet.readUnsignedShort();
        final int y = packet.readUnsignedShortA();
        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        if (id > 0 && id != 15480 && id != 6 && !RegionClipping.objectExists(gameObject)) {
            return;
        }
        int distanceX = player.getPosition().getX() - position.getX();
        int distanceY = player.getPosition().getY() - position.getY();
        if (distanceX < 0) {
            distanceX = -distanceX;
        }
        if (distanceY < 0) {
            distanceY = -distanceY;
        }
        int size = distanceX > distanceY ? GameObjectDefinition.forId(id).getSizeX()
                : GameObjectDefinition.forId(id).getSizeY();
        if (size <= 0) {
            size = 1;
        }
        gameObject.setSize(size);
        if (player.getMovementQueue().isLockMovement()) {
            return;
        }
        if (player.getRights() == PlayerRights.DEVELOPER) {
            player.getPacketSender()
                    .sendMessage("First click object id; [id, position] : [" + id + ", " + position.toString() + "]");
        }
        player.setInteractingObject(gameObject)
                .setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
                    @Override
                    public void execute() {
                        player.setPositionToFace(gameObject.getPosition());
                        if (Trees.forId(id) != null) {
                            Woodcutting.cutWood(player, gameObject, false);
                            return;
                        }
                        if (MiningData.forRock(gameObject.getId()) != null) {
                            Mining.startMining(player, gameObject);
                            return;
                        }
                        if (player.getFarming().click(player, x, y, 1)) {
                            return;
                        }
                        if (Runecrafting.runecraftingAltar(player, gameObject.getId())) {
                            RunecraftingData.RuneData rune = RunecraftingData.RuneData.forId(gameObject.getId());
                            if (rune == null) {
                                return;
                            }
                            Runecrafting.craftRunes(player, rune);
                            return;
                        }
                        if (Agility.handleObject(player, gameObject)) {
                            return;
                        }
                        if (player.getLocation() == Location.WILDERNESS
                                && WildernessObelisks.handleObelisk(gameObject.getId())) {
                            return;
                        }
                        switch (id) {

                            case 8987:
                                TeleportHandler.teleportPlayer(player, GameSettings.DEFAULT_POSITION.copy(),
                                        player.getSpellbook().getTeleportType());
                                break;

                            case 15480:
                                Construction.newHouse(player);
                                Construction.enterHouse(player, player, true);
                                break;
                            case 23271:
                                if (player.getInventory().containsAny(GameSettings.BANNEDWILD_ITEMS)) {
                                    player.sendMessage("You have items that are not allowed in the wilderness.");
                                } else if (player.getEquipment().containsAny(GameSettings.BANNEDWILD_ITEMS)) {
                                    player.sendMessage("You're wearing an item that is now allowed in the wilderness.");
                                } else if (player.getPosition().getY() <= 3520) {
                                    player.getPacketSender().sendInterface(35_000);
                                } else if (player.getPosition().getY() > 3520 && player.getForceMovement() == null) {
                                    player.getPacketSender().sendInterfaceRemoval();
                                    player.getMovementQueue().reset();
                                    player.performAnimation(new Animation(6132));
                                    final Position crossDitch = new Position(0,
                                            player.getPosition().getY() < 3522 ? 3 : -3);
                                    TaskManager.submit(
                                            new ForceMovementTask(player, 3, new ForceMovement(player.getPosition().copy(),
                                                    crossDitch, 33, 60, crossDitch.getY() == 3 ? 0 : 2, 6312)));
                                }
                                break;
                            case 5259:
                                if (player.getPosition().getY() == 4757)
                                    player.moveTo(player.getPosition().translate(0, 2));
                                else
                                    player.moveTo(player.getPosition().translate(0, -2)); // okay try this

                                break;
                            case 38700:
                                player.moveTo(new Position(3095, 2990));
                                break;
                            case 2465:
                                player.getPacketSender().sendMessage(
                                        "@blu@Welcome to the free-for-all arena! You will not lose any items on death here.");
                                player.moveTo(new Position(2815, 5511));
                                break;
                            case 45803:
                            case 1767:
                                DialogueManager.start(player, 114);
                                player.setDialogueActionId(72);
                                break;
                            case 7352:
                                if (Dungeoneering.doingDungeoneering(player) && player.getMinigameAttributes()
                                        .getDungeoneeringAttributes().getParty().getGatestonePosition() != null) {
                                    player.moveTo(player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
                                            .getGatestonePosition());
                                    player.setEntityInteraction(null);
                                    player.getPacketSender().sendMessage("You are teleported to your party's gatestone.");
                                    player.performGraphic(new Graphic(1310));
                                } else {
                                    player.getPacketSender().sendMessage(
                                            "Your party must drop a Gatestone somewhere in the dungeon to use this portal.");
                                }
                                break;
                            case 7353:
                                player.moveTo(new Position(2439, 4956, player.getPosition().getZ()));
                                break;
                            case 7321:
                                player.moveTo(new Position(2452, 4944, player.getPosition().getZ()));
                                break;
                            case 7322:
                                player.moveTo(new Position(2455, 4964, player.getPosition().getZ()));
                                break;
                            case 7315:
                                player.moveTo(new Position(2447, 4956, player.getPosition().getZ()));
                                break;
                            case 7316:
                                player.moveTo(new Position(2471, 4956, player.getPosition().getZ()));
                                break;
                            case 7318:
                                player.moveTo(new Position(2464, 4963, player.getPosition().getZ()));
                                break;
                            case 7319:
                                player.moveTo(new Position(2467, 4940, player.getPosition().getZ()));
                                break;
                            case 7324:
                                player.moveTo(new Position(2481, 4956, player.getPosition().getZ()));
                                break;
                            case 12003:
                                player.getPacketSender().sendMessage("@blu@You searched and found nothing...");
                                break;

                            case 12004:
                                if(player.hasItem(17005)|| player.getBank(player.getCurrentBankTab()).contains(17005))  {
                                    player.getPacketSender().sendMessage("@red@ You already have a key!");
                                    return;
                                }
                                if (player.getInventory().isFull()) {
                                    player.getPacketSender().sendMessage("This players inventory is full plese tell them.");
                                    player.getPacketSender()
                                            .sendMessage(player.getUsername() + " is trying to give you an item, please make some room.");
                                    return;
                                }
                                player.getPacketSender().sendMessage("@blu@You searched and found a @cya@Mystical key..");
                                player.getInventory().add(new Item(17005, 1));
                                break;
                            case 11356:
                              player.getPacketSender().sendMessage("use ::event");
                                break;
                            case 47180:
                                if (player.getDonor() != DonorRights.SPONSOR) {
                                    player.getPacketSender().sendMessage("You must be at least a Sponsor to use this.");
                                    return;
                                }
                                player.getPacketSender().sendMessage("You activate the device..");
                                player.moveTo(new Position(2586, 3912));
                                break;
                            case 10091:
                            case 8702:
                                if (gameObject.getId() == 8702) {
                                    if (player.getDonor() != DonorRights.SPONSOR
                                            && player.getDonor() != DonorRights.DELUXE_DONOR) {
                                        player.getPacketSender()
                                                .sendMessage("You must be at least a deluxe donor to use this.");
                                        return;
                                    }
                                }
                                Fishing.setupFishing(player, Spot.ROCKTAIL);
                                break;
                            case 9319:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 61) {
                                    player.getPacketSender().sendMessage(
                                            "You need an Agility level of at least 61 or higher to climb this");
                                    return;
                                }
                                if (player.getPosition().getZ() == 0) {
                                    player.moveTo(new Position(3422, 3549, 1));
                                } else if (player.getPosition().getZ() == 1) {
                                    if (gameObject.getPosition().getX() == 3447) {
                                        player.moveTo(new Position(3447, 3575, 2));
                                    } else {
                                        player.moveTo(new Position(3447, 3575, 0));
                                    }
                                }
                                break;

                            case 9320:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 61) {
                                    player.getPacketSender().sendMessage(
                                            "You need an Agility level of at least 61 or higher to climb this");
                                    return;
                                }
                                if (player.getPosition().getZ() == 1) {
                                    player.moveTo(new Position(3422, 3549, 0));
                                } else if (player.getPosition().getZ() == 0) {
                                    player.moveTo(new Position(3447, 3575, 1));
                                } else if (player.getPosition().getZ() == 2) {
                                    player.moveTo(new Position(3447, 3575, 1));
                                }
                                player.performAnimation(new Animation(828));
                                break;
                            case 2274:
                                if (gameObject.getPosition().getX() == 2912 && gameObject.getPosition().getY() == 5300) {
                                    player.moveTo(new Position(2914, 5300, 1));
                                } else if (gameObject.getPosition().getX() == 2914
                                        && gameObject.getPosition().getY() == 5300) {
                                    player.moveTo(new Position(2912, 5300, 2));
                                } else if (gameObject.getPosition().getX() == 2919
                                        && gameObject.getPosition().getY() == 5276) {
                                    player.moveTo(new Position(2918, 5274));
                                } else if (gameObject.getPosition().getX() == 2918
                                        && gameObject.getPosition().getY() == 5274) {
                                    player.moveTo(new Position(2919, 5276, 1));
                                } else if (gameObject.getPosition().getX() == 3001
                                        && gameObject.getPosition().getY() == 3931
                                        || gameObject.getPosition().getX() == 3652
                                        && gameObject.getPosition().getY() == 3488) {
                                    player.moveTo(GameSettings.DEFAULT_POSITION.copy());
                                    player.getPacketSender().sendMessage("The portal teleports you to Edgeville.");
                                }
                                break;
                            case 7836:
                            case 7808:
                                int amt = player.getInventory().getAmount(6055);
                                if (amt > 0) {
                                    player.getInventory().delete(6055, amt);
                                    player.getPacketSender().sendMessage("You put the weed in the compost bin.");
                                    player.getSkillManager().addExperience(Skill.FARMING, 20 * amt);
                                } else {
                                    player.getPacketSender().sendMessage("You do not have any weeds in your inventory.");
                                }
                                break;
                            case 5960: // Levers
                            case 5959:
                                player.setDirection(Direction.WEST);
                                TeleportHandler.teleportPlayer(player, new Position(3090, 3475), TeleportType.LEVER);
                                break;
                            case 5096:
                                if (gameObject.getPosition().getX() == 2644 && gameObject.getPosition().getY() == 9593) {
                                    player.moveTo(new Position(2649, 9591));
                                }
                                break;

                            case 5094:
                                if (gameObject.getPosition().getX() == 2648 && gameObject.getPosition().getY() == 9592) {
                                    player.moveTo(new Position(2643, 9594, 2));
                                }
                                break;

                            case 5098:
                                if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9511) {
                                    player.moveTo(new Position(2637, 9517));
                                }
                                break;

                            case 5097:
                                if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9514) {
                                    player.moveTo(new Position(2636, 9510, 2));
                                }
                                break;
                            case 23093:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 70) {
                                    player.getPacketSender().sendMessage(
                                            "You need an Agility level of at least 70 to go through this portal.");
                                    return;
                                }
                                if (!player.getClickDelay().elapsed(2000)) {
                                    return;
                                }
                                int plrHeight = player.getPosition().getZ();
                                if (plrHeight == 2) {
                                    player.moveTo(new Position(2914, 5300, 1));
                                } else if (plrHeight == 1) {
                                    int x = gameObject.getPosition().getX();
                                    int y = gameObject.getPosition().getY();
                                    if (x == 2914 && y == 5300) {
                                        player.moveTo(new Position(2912, 5299, 2));
                                    } else if (x == 2920 && y == 5276) {
                                        player.moveTo(new Position(2920, 5274, 0));
                                    }
                                } else if (plrHeight == 0) {
                                    player.moveTo(new Position(2920, 5276, 1));
                                }
                                player.getClickDelay().reset();
                                break;
                            case 26439:
                                if (player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) <= 700) {
                                    player.getPacketSender()
                                            .sendMessage("You need a Constitution level of at least 70 to swim across.");
                                    return;
                                }
                                if (!player.getClickDelay().elapsed(1000)) {
                                    return;
                                }
                                if (player.isCrossingObstacle()) {
                                    return;
                                }
                                final String startMessage = "You jump into the icy cold water..";
                                final String endMessage = "You climb out of the water safely.";
                                final int jumpGFX = 68;
                                final int jumpAnimation = 772;
                                player.setSkillAnimation(773);
                                player.setCrossingObstacle(true);
                                player.getUpdateFlag().flag(Flag.APPEARANCE);
                                player.performAnimation(new Animation(3067));
                                final boolean goBack2 = player.getPosition().getY() >= 5344;
                                player.getPacketSender().sendMessage(startMessage);
                                player.moveTo(new Position(2885, !goBack2 ? 5335 : 5342, 2));
                                player.setDirection(goBack2 ? Direction.SOUTH : Direction.NORTH);
                                player.performGraphic(new Graphic(jumpGFX));
                                player.performAnimation(new Animation(jumpAnimation));
                                TaskManager.submit(new Task(1, player, false) {
                                    int ticks = 0;

                                    @Override
                                    public void execute() {
                                        ticks++;
                                        player.getMovementQueue().walkStep(0, goBack2 ? -1 : 1);
                                        if (ticks >= 10) {
                                            stop();
                                        }
                                    }

                                    @Override
                                    public void stop() {
                                        player.setSkillAnimation(-1);
                                        player.setCrossingObstacle(false);
                                        player.getUpdateFlag().flag(Flag.APPEARANCE);
                                        player.getPacketSender().sendMessage(endMessage);
                                        player.moveTo(
                                                new Position(2885, player.getPosition().getY() < 5340 ? 5333 : 5345, 2));
                                        setEventRunning(false);
                                    }
                                });
                                player.getClickDelay().reset(System.currentTimeMillis() + 9000);
                                break;
                            case 26384:
                                if (player.isCrossingObstacle()) {
                                    return;
                                }
                                if (!player.getInventory().contains(2347)) {
                                    player.getPacketSender()
                                            .sendMessage("You need to have a hammer to bang on the door with.");
                                    return;
                                }
                                player.setCrossingObstacle(true);
                                final boolean goBack = player.getPosition().getX() <= 2850;
                                player.performAnimation(new Animation(377));
                                TaskManager.submit(new Task(2, player, false) {
                                    @Override
                                    public void execute() {
                                        player.moveTo(new Position(goBack ? 2851 : 2850, 5333, 2));
                                        player.setCrossingObstacle(false);
                                        stop();
                                    }
                                });
                                break;
                            case 26303:
                                if (!player.getClickDelay().elapsed(1200)) {
                                    return;
                                }
                                if (player.getSkillManager().getCurrentLevel(Skill.RANGED) < 70) {
                                    player.getPacketSender()
                                            .sendMessage("You need a Ranged level of at least 70 to swing across here.");
                                } else if (!player.getInventory().contains(9418)) {
                                    player.getPacketSender().sendMessage(
                                            "You need a Mithril grapple to swing across here. Explorer Jack might have one.");
                                    return;
                                } else {
                                    player.performAnimation(new Animation(789));
                                    TaskManager.submit(new Task(2, player, false) {
                                        @Override
                                        public void execute() {
                                            player.getPacketSender().sendMessage(
                                                    "You throw your Mithril grapple over the pillar and move across.");
                                            player.moveTo(new Position(2871,
                                                    player.getPosition().getY() <= 5270 ? 5279 : 5269, 2));
                                            stop();
                                        }
                                    });
                                    player.getClickDelay().reset();
                                }
                                break;
                            case 4493:
                                if (player.getPosition().getX() >= 3432) {
                                    player.moveTo(new Position(3433, 3538, 1));
                                }
                                break;
                            case 4494:
                                player.moveTo(new Position(3438, 3538, 0));
                                break;
                            case 4495:
                                player.moveTo(new Position(3417, 3541, 2));
                                break;
                            case 4496:
                                player.moveTo(new Position(3412, 3541, 1));
                                break;
                            case 2491:
                                player.setDialogueActionId(48);
                                DialogueManager.start(player, 87);
                                break;
                            case 25339:
                            case 25340:
                                player.moveTo(new Position(1778, 5346, player.getPosition().getZ() == 0 ? 1 : 0));
                                break;
                            case 10229:
                            case 10230:
                                boolean up = id == 10229;
                                player.performAnimation(new Animation(up ? 828 : 827));
                                player.getPacketSender().sendMessage("You climb " + (up ? "up" : "down") + " the ladder..");
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        player.moveTo(up ? new Position(1912, 4367) : new Position(2900, 4449));
                                        stop();
                                    }
                                });
                                break;
                            case 1568:
                                player.moveTo(new Position(3097, 9868));
                                break;
                            case 5103: // Brimhaven vines
                            case 5104:
                            case 5105:
                            case 5106:
                            case 5107:
                                if (!player.getClickDelay().elapsed(4000)) {
                                    return;
                                }
                                if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) < 30) {
                                    player.getPacketSender()
                                            .sendMessage("You need a Woodcutting level of at least 30 to do this.");
                                    return;
                                }
                                if (com.StreamScape.world.content.skill.impl.woodcutting.Hatchet.getHatchet(player) < 0) {
                                    player.getPacketSender().sendMessage(
                                            "You do not have a hatchet which you have the required Woodcutting level to use.");
                                    return;
                                }
                                final Hatchet axe = Hatchet.forId(
                                        com.StreamScape.world.content.skill.impl.woodcutting.Hatchet.getHatchet(player));
                                player.performAnimation(new Animation(axe.getAnim()));
                                gameObject.setFace(-1);
                                TaskManager.submit(new Task(3 + Misc.getRandom(4), player, false) {
                                    @Override
                                    protected void execute() {
                                        if (player.getMovementQueue().isMoving()) {
                                            stop();
                                            return;
                                        }
                                        int x = 0;
                                        int y = 0;
                                        if (player.getPosition().getX() == 2689 && player.getPosition().getY() == 9564) {
                                            x = 2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2691
                                                && player.getPosition().getY() == 9564) {
                                            x = -2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2683
                                                && player.getPosition().getY() == 9568) {
                                            x = 0;
                                            y = 2;
                                        } else if (player.getPosition().getX() == 2683
                                                && player.getPosition().getY() == 9570) {
                                            x = 0;
                                            y = -2;
                                        } else if (player.getPosition().getX() == 2674
                                                && player.getPosition().getY() == 9479) {
                                            x = 2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2676
                                                && player.getPosition().getY() == 9479) {
                                            x = -2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2693
                                                && player.getPosition().getY() == 9482) {
                                            x = 2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2672
                                                && player.getPosition().getY() == 9499) {
                                            x = 2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2674
                                                && player.getPosition().getY() == 9499) {
                                            x = -2;
                                            y = 0;
                                        }
                                        CustomObjects.objectRespawnTask(player,
                                                new GameObject(-1, gameObject.getPosition().copy()), gameObject, 10);
                                        player.getPacketSender().sendMessage("You chop down the vines..");
                                        player.getSkillManager().addExperience(Skill.WOODCUTTING, 45);
                                        player.performAnimation(new Animation(65535));
                                        player.getMovementQueue().walkStep(x, y);
                                        stop();
                                    }
                                });
                                player.getClickDelay().reset();
                                break;
                            case 29942:
                                if (player.getSkillManager().getCurrentLevel(Skill.SUMMONING) == player.getSkillManager()
                                        .getMaxLevel(Skill.SUMMONING)) {
                                    player.getPacketSender()
                                            .sendMessage("You do not need to recharge your Summoning points right now.");
                                    return;
                                }
                                player.performGraphic(new Graphic(1517));
                                player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
                                        player.getSkillManager().getMaxLevel(Skill.SUMMONING), true);
                                player.getPacketSender().sendString(18045,
                                        " " + player.getSkillManager().getCurrentLevel(Skill.SUMMONING) + "/"
                                                + player.getSkillManager().getMaxLevel(Skill.SUMMONING));
                                player.getPacketSender().sendMessage("You recharge your Summoning points.");
                                break;
                            case 884:
                                player.setDialogueActionId(41);
                                DialogueManager.start(player, 75);
                                break;
                            case 9294:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 80) {
                                    player.getPacketSender()
                                            .sendMessage("You need an Agility level of at least 80 to use this shortcut.");
                                    return;
                                }
                                player.performAnimation(new Animation(769));
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        player.moveTo(
                                                new Position(player.getPosition().getX() >= 2880 ? 2878 : 2880, 9813));
                                        stop();
                                    }
                                });
                                break;
                            case 9293:
                                boolean back = player.getPosition().getX() > 2888;
                                player.moveTo(back ? new Position(2886, 9799) : new Position(2891, 9799));
                                break;
                            case 2320:
                                back = player.getPosition().getY() == 9969 || player.getPosition().getY() == 9970;
                                player.moveTo(back ? new Position(3120, 9963) : new Position(3120, 9969));
                                break;
                            case 32015:
                                player.performAnimation(new Animation(828));
                                player.getPacketSender().sendMessage("You climb the ladder..");
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        if (gameObject.getPosition().getX() == 3005
                                                && gameObject.getPosition().getY() == 10363) {
                                            player.moveTo(new Position(3005, 3964));
                                        }
                                        stop();
                                    }
                                });
                                break;
                            case 1755:
                                player.performAnimation(new Animation(828));
                                player.getPacketSender().sendMessage("You climb the stairs..");
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        if (gameObject.getPosition().getX() == 2547
                                                && gameObject.getPosition().getY() == 9951) {
                                            player.moveTo(new Position(2548, 3551));
                                        } else if (gameObject.getPosition().getX() == 3005
                                                && gameObject.getPosition().getY() == 10363) {
                                            player.moveTo(new Position(3005, 3962));
                                        } else if (gameObject.getPosition().getX() == 3084
                                                && gameObject.getPosition().getY() == 9672) {
                                            player.moveTo(new Position(3117, 3244));
                                        } else if (gameObject.getPosition().getX() == 3097
                                                && gameObject.getPosition().getY() == 9867) {
                                            player.moveTo(new Position(3096, 3468));
                                        }
                                        stop();
                                    }
                                });
                                break;
                            case 5110:
                                player.moveTo(new Position(2647, 9557));
                                player.getPacketSender().sendMessage("You pass the stones..");
                                break;
                            case 5111:
                                player.moveTo(new Position(2649, 9562));
                                player.getPacketSender().sendMessage("You pass the stones..");
                                break;
                            case 6434:
                                player.performAnimation(new Animation(827));
                                player.getPacketSender().sendMessage("You enter the trapdoor..");
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        player.moveTo(new Position(3085, 9672));
                                        stop();
                                    }
                                });
                                break;
                            case 19187:
                            case 19175:
                                Hunter.dismantle(player, gameObject);
                                break;
                            case 25029:
                                PuroPuro.goThroughWheat(player, gameObject);
                                break;
                            case 47976:
                                Nomad.endFight(player, false);
                                break;
                            case 2182:
                                if (!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
                                    player.getPacketSender()
                                            .sendMessage("You have no business with this chest. Talk to the Gypsy first!");
                                    return;
                                }
                                RecipeForDisaster.openRFDShop(player);
                                break;
                            case 12356:
                                if (!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
                                    player.getPacketSender()
                                            .sendMessage("You have no business with this portal. Talk to the Gypsy first!");
                                    return;
                                }
                                if (player.getPosition().getZ() > 0) {
                                    RecipeForDisaster.leave(player);
                                } else {
                                    player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(1,
                                            true);
                                    RecipeForDisaster.enter(player);
                                }
                                break;
                            case 6704:
                                player.moveTo(new Position(3577, 3282, 0));
                                break;
                            case 6706:
                                player.moveTo(new Position(3554, 3283, 0));
                                break;
                            case 6705:
                                player.moveTo(new Position(3566, 3275, 0));
                                break;
                            case 6702:
                                player.moveTo(new Position(3564, 3289, 0));
                                break;
                            case 6703:
                                player.moveTo(new Position(3574, 3298, 0));
                                break;
                            case 6707:
                                player.moveTo(new Position(3556, 3298, 0));
                                break;
                            case 3203:
                                if (player.getLocation() == Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
                                    if (Dueling.checkRule(player, DuelRule.NO_FORFEIT)) {
                                        player.getPacketSender().sendMessage("Forfeiting has been disabled in this duel.");
                                        return;
                                    }
                                    player.getCombatBuilder().reset(true);
                                    if (player.getDueling().duelingWith > -1) {
                                        Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
                                        if (duelEnemy == null) {
                                            return;
                                        }
                                        duelEnemy.getCombatBuilder().reset(true);
                                        duelEnemy.getMovementQueue().reset();
                                        duelEnemy.getDueling().duelVictory();
                                    }
                                    player.moveTo(new Position(3368 + Misc.getRandom(5), 3267 + Misc.getRandom(3), 0));
                                    player.getDueling().reset();
                                    player.getCombatBuilder().reset(true);
                                    player.restart();
                                }
                                break;
                            case 1738:
                                if (player.getLocation() == Location.LUMBRIDGE && player.getPosition().getZ() == 0) {
                                    player.moveTo(
                                            new Position(player.getPosition().getX(), player.getPosition().getY(), 1));
                                } else {
                                    player.moveTo(new Position(2840, 3539, 2));
                                }
                                break;
                            case 15638:
                                player.moveTo(new Position(2840, 3539, 0));
                                break;
                            case 15644:
                            case 15641:
                                switch (player.getPosition().getZ()) {
                                    case 0:
                                        player.moveTo(new Position(2855, player.getPosition().getY() >= 3546 ? 3545 : 3546));
                                        break;
                                }
                                break;
                            case 28714:
                                player.performAnimation(new Animation(828));
                                player.delayedMoveTo(new Position(3089, 3492), 2);
                                break;
                            case 1746:
                                player.performAnimation(new Animation(827));
                                player.delayedMoveTo(new Position(2209, 5348), 2);
                                break;
                            case 19191:
                            case 19189:
                            case 19180:
                            case 19184:
                            case 19182:
                            case 19178:
                                Hunter.lootTrap(player, gameObject);
                                break;
                            case 817:
                                if (player.getDonor() != DonorRights.DONOR && player.getDonor() != DonorRights.DELUXE_DONOR
                                        && player.getDonor() != DonorRights.SPONSOR) {
                                    player.getPacketSender()
                                            .sendMessage("You must be at least a deluxe donor to use this.");
                                    return;
                                }
                                int loot = 1127;
                                Stalls.stealFromStall(player, 1, 15000, loot, "You stole some morune equipment.");
                                break;
                            case 7100:
                                if (player.getDonor() != DonorRights.DELUXE_DONOR
                                        && player.getDonor() != DonorRights.SPONSOR) {
                                    player.getPacketSender()
                                            .sendMessage("You must be at least a deluxe donor to use this.");
                                    return;
                                }
                                int reward = 1187;
                                Stalls.stealFromStall(player, 1, 20000, reward, "You stole some blue dragon equipment.");
                                break;
                            case 30205:
                                player.setDialogueActionId(11);
                                DialogueManager.start(player, 20);
                                break;
                            case 28716:
                                if (!player.busy()) {
                                    player.getSkillManager().updateSkill(Skill.SUMMONING);
                                    player.getPacketSender().sendInterface(63471);
                                } else {
                                    player.getPacketSender()
                                            .sendMessage("Please finish what you're doing before opening this.");
                                }
                                break;
                            case 6:
                                DwarfCannon cannon = player.getCannon();
                                if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
                                    player.getPacketSender().sendMessage("This is not your cannon!");
                                } else {
                                    DwarfMultiCannon.startFiringCannon(player, cannon);
                                }
                                break;
                            case 2:
                                player.moveTo(new Position(player.getPosition().getX() > 2690 ? 2687 : 2694, 3714));
                                player.getPacketSender().sendMessage("You walk through the entrance..");
                                break;
                            case 2026:
                            case 2028:
                            case 2029:
                            case 2030:
                            case 2031:
                                player.setEntityInteraction(gameObject);
                                Fishing.setupFishing(player, Fishing.forSpot(gameObject.getId(), false));
                                return;
                            case 12692:
                            case 2783:
                            case 4306:
                                player.setInteractingObject(gameObject);
                                EquipmentMaking.handleAnvil(player);
                                break;
                            case 2732:
                                EnterAmountOfLogsToAdd.openInterface(player);
                                break;
                            case 409:
                            case 27661:
                            case 2640:
                            case 36972:
                                player.performAnimation(new Animation(645));
                                if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager()
                                        .getMaxLevel(Skill.PRAYER)) {
                                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                                            player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
                                    player.getPacketSender().sendMessage("You recharge your Prayer points.");
                                }
                                if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) < player.getSkillManager()
                                        .getMaxLevel(Skill.CONSTITUTION)) {
                                    player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
                                            player.getSkillManager().getMaxLevel(Skill.CONSTITUTION), true);
                                    player.getPacketSender().sendMessage("You heal back!.");
                                }
                                break;
                            case 8749:
                                boolean restore = player.getSpecialPercentage() < 100;
                                if (restore) {
                                    player.setSpecialPercentage(100);
                                    CombatSpecial.updateBar(player);
                                    player.getPacketSender().sendMessage("Your special attack energy has been restored.");
                                }
                                for (Skill skill : Skill.values()) {
                                    int increase = skill != Skill.PRAYER && skill != Skill.CONSTITUTION
                                            && skill != Skill.SUMMONING ? 19 : 0;
                                    if (player.getSkillManager().getCurrentLevel(
                                            skill) < player.getSkillManager().getMaxLevel(skill) + increase) {
                                        player.getSkillManager().setCurrentLevel(skill,
                                                player.getSkillManager().getMaxLevel(skill) + increase);
                                    }
                                }
                                player.performGraphic(new Graphic(1302));
                                player.getPacketSender().sendMessage("Your stats have received a major buff.");
                                break;
                            case 4859:
                                player.performAnimation(new Animation(645));
                                if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager()
                                        .getMaxLevel(Skill.PRAYER)) {
                                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                                            player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
                                    player.getPacketSender().sendMessage("You recharge your Prayer points.");
                                }
                                break;
                            case 411:
                                if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 30) {
                                    player.getPacketSender()
                                            .sendMessage("You need a Defence level of at least 30 to use this altar.");
                                    return;
                                }
                                player.performAnimation(new Animation(645));
                                if (player.getPrayerbook() == Prayerbook.NORMAL) {
                                    player.getPacketSender()
                                            .sendMessage("You sense a surge of power flow through your body!");
                                    player.setPrayerbook(Prayerbook.CURSES);
                                } else {
                                    player.getPacketSender()
                                            .sendMessage("You sense a surge of purity flow through your body!");
                                    player.setPrayerbook(Prayerbook.NORMAL);
                                }
                                player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB,
                                        player.getPrayerbook().getInterfaceId());
                                PrayerHandler.deactivateAll(player);
                                CurseHandler.deactivateAll(player);
                                break;
                            case 6552:
                                player.performAnimation(new Animation(645));
                                player.setSpellbook(player.getSpellbook() == MagicSpellbook.ANCIENT ? MagicSpellbook.NORMAL
                                        : MagicSpellbook.ANCIENT);
                                player.getPacketSender()
                                        .sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
                                        .sendMessage("Your magic spellbook is changed..");
                                Autocasting.resetAutocast(player, true);
                                break;
                            case 410:
                                if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 40) {
                                    player.getPacketSender()
                                            .sendMessage("You need a Defence level of at least 40 to use this altar.");
                                    return;
                                }
                                player.performAnimation(new Animation(645));
                                player.setSpellbook(player.getSpellbook() == MagicSpellbook.LUNAR ? MagicSpellbook.NORMAL
                                        : MagicSpellbook.LUNAR);
                                player.getPacketSender()
                                        .sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
                                        .sendMessage("Your magic spellbook is changed..");
                                ;
                                Autocasting.resetAutocast(player, true);
                                break;
                            case 172:
                                if (player.getInventory().getFreeSlots() <= 0) {
                                    player.sendMessage("You need 1 open space to receive your reward.");
                                    return;
                                }
                                MysteryChest.handleChest(player);
                                break;
                            case 6910:
                            case 4483:
                            case 3193:
                            case 2213:
                            case 11758:
                            case 5276:
                            case 14367:
                            case 27663:
                            case 42192:
                            case 75:
                            case 26969:
                            case 26972:
                            case 26193:
                                player.getBank(player.getCurrentBankTab()).open();
                                break;
                        }
                    }
                }));
    }

    @SuppressWarnings("unused")
    private static void fourthClick(Player player, Packet packet) {
    }

    private static void secondClick(final Player player, Packet packet) {
        final int id = packet.readLEShortA();
        final int y = packet.readLEShort();
        final int x = packet.readUnsignedShortA();
        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        if (id > 0 && id != 6 && !RegionClipping.objectExists(gameObject)) {
            // player.getPacketSender().sendMessage("An error occured. Error
            // code: "+id).sendMessage("Please report the error to a staff
            // member.");
            return;
        }
        player.setPositionToFace(gameObject.getPosition());
        int distanceX = player.getPosition().getX() - position.getX();
        int distanceY = player.getPosition().getY() - position.getY();
        if (distanceX < 0) {
            distanceX = -distanceX;
        }
        if (distanceY < 0) {
            distanceY = -distanceY;
        }
        int size = distanceX > distanceY ? distanceX : distanceY;
        gameObject.setSize(size);
        player.setInteractingObject(gameObject)
                .setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
                    @Override
                    public void execute() {
                        if (MiningData.forRock(gameObject.getId()) != null) {
                            Prospecting.prospectOre(player, id);
                            return;
                        }
                        if (player.getFarming().click(player, x, y, 1)) {
                            return;
                        }
                        switch (gameObject.getId()) {
                            case 28716:
                                if (!player.busy()) {
                                    if (player.getSkillManager().getCurrentLevel(Skill.SUMMONING) == player
                                            .getSkillManager().getMaxLevel(Skill.SUMMONING)) {
                                        player.getPacketSender().sendMessage("You do not need to recharge right now.");
                                        return;
                                    }
                                    player.performGraphic(new Graphic(1517));
                                    player.performAnimation(new Animation(8502));
                                    player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
                                            player.getSkillManager().getMaxLevel(Skill.SUMMONING), true);
                                    player.getPacketSender().sendString(18045,
                                            " " + player.getSkillManager().getCurrentLevel(Skill.SUMMONING) + "/"
                                                    + player.getSkillManager().getMaxLevel(Skill.SUMMONING));
                                    player.getPacketSender().sendMessage("You recharge your Summoning points.");
                                } else {
                                    player.getPacketSender()
                                            .sendMessage("Please finish what you're doing before doing this.");
                                }
                                break;
                            case 884:
                                player.setDialogueActionId(41);
                                player.setInputHandling(new DonateToWell());
                                player.getPacketSender().sendInterfaceRemoval()
                                        .sendEnterAmountPrompt("How much money would you like to contribute with?");
                                break;
                            case 2646:
                            case 312:
                                if (!player.getClickDelay().elapsed(1200)) {
                                    return;
                                }
                                if (player.getInventory().isFull()) {
                                    player.getPacketSender().sendMessage("You don't have enough free inventory space.");
                                    return;
                                }
                                String type = gameObject.getId() == 312 ? "Potato" : "Flax";
                                player.performAnimation(new Animation(827));
                                player.getInventory().add(gameObject.getId() == 312 ? 1942 : 1779, 1);
                                player.getPacketSender().sendMessage("You pick some " + type + "..");
                                gameObject.setPickAmount(gameObject.getPickAmount() + 1);
                                if (Misc.getRandom(3) == 1 && gameObject.getPickAmount() >= 1
                                        || gameObject.getPickAmount() >= 6) {
                                    player.getPacketSender().sendClientRightClickRemoval();
                                    gameObject.setPickAmount(0);
                                    CustomObjects.globalObjectRespawnTask(new GameObject(-1, gameObject.getPosition()),
                                            gameObject, 10);
                                }
                                player.getClickDelay().reset();
                                break;
                            case 2644:
                                Flax.showSpinInterface(player);
                                break;
                            case 6:
                                DwarfCannon cannon = player.getCannon();
                                if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
                                    player.getPacketSender().sendMessage("This is not your cannon!");
                                } else {
                                    DwarfMultiCannon.pickupCannon(player, cannon, false);
                                }
                                break;
                            case 4875:
                                Stalls.stealFromStall(player, 1, 5100, 18199, "You steal a banana.");
                                break;
                            case 4874:
                                Stalls.stealFromStall(player, 30, 6130, 15009, "You steal a golden ring.");
                                break;
                            case 4876:
                                Stalls.stealFromStall(player, 60, 7370, 17401, "You steal a damaged hammer.");
                                break;
                            case 4877:
                                Stalls.stealFromStall(player, 65, 7990, 1389, "You steal a staff.");
                                break;
                            case 4878:
                                Stalls.stealFromStall(player, 80, 9230, 11998, "You steal a scimitar.");
                                break;
                            case 6189:
                            case 26814:
                            case 11666:
                                Smelting.openInterface(player);
                                break;
                            case 2152:
                                player.performAnimation(new Animation(8502));
                                player.performGraphic(new Graphic(1308));
                                player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
                                        player.getSkillManager().getMaxLevel(Skill.SUMMONING));
                                player.getPacketSender().sendMessage("You renew your Summoning points.");
                                break;
                        }
                    }
                }));
    }

    @SuppressWarnings("unused")
    private static void thirdClick(Player player, Packet packet) {
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.isTeleporting() || player.isPlayerLocked() || player.getMovementQueue().isLockMovement()) {
            return;
        }
        switch (packet.getOpcode()) {
            case FIRST_CLICK:
                firstClick(player, packet);
                break;
            case SECOND_CLICK:
                secondClick(player, packet);
                break;
            case THIRD_CLICK:
                // thirdClick(player, packet);
                break;
            case FOURTH_CLICK:
                // fourthClick(player, packet);
                break;
            case FIFTH_CLICK:
                fifthClick(player, packet);
                break;
        }
    }
}
