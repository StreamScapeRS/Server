package com.StreamScape.net.packet.impl;

import com.StreamScape.engine.task.impl.WalkToTask;
import com.StreamScape.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.StreamScape.model.Animation;
import com.StreamScape.model.GameObject;
import com.StreamScape.model.Graphic;
import com.StreamScape.model.Item;
import com.StreamScape.model.Locations.Location;
import com.StreamScape.model.PlayerRanks.PlayerRights;
import com.StreamScape.model.Position;
import com.StreamScape.model.Skill;
import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketListener;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.clip.region.RegionClipping;
import com.StreamScape.world.content.ItemForging;
import com.StreamScape.world.content.dialogue.DialogueManager;
import com.StreamScape.world.content.skill.impl.cooking.Cooking;
import com.StreamScape.world.content.skill.impl.cooking.CookingData;
import com.StreamScape.world.content.skill.impl.crafting.Gems;
import com.StreamScape.world.content.skill.impl.crafting.LeatherMaking;
import com.StreamScape.world.content.skill.impl.firemaking.Firemaking;
import com.StreamScape.world.content.skill.impl.fletching.Fletching;
import com.StreamScape.world.content.skill.impl.herblore.Herblore;
import com.StreamScape.world.content.skill.impl.herblore.PotionCombinating;
import com.StreamScape.world.content.skill.impl.herblore.WeaponPoison;
import com.StreamScape.world.content.skill.impl.prayer.BonesOnAltar;
import com.StreamScape.world.content.skill.impl.prayer.Prayer;
import com.StreamScape.world.content.skill.impl.slayer.SlayerDialogues;
import com.StreamScape.world.content.skill.impl.slayer.SlayerTasks;
import com.StreamScape.world.content.skill.impl.smithing.EquipmentMaking;
import com.StreamScape.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player 'uses' an item on another
 * entity.
 * 
 * @author relex lawl
 */

public class UseItemPacketListener implements PacketListener {

	public final static int USE_ITEM = 122;

	public final static int ITEM_ON_NPC = 57;

	public final static int ITEM_ON_ITEM = 53;

	public final static int ITEM_ON_OBJECT = 192;

	public final static int ITEM_ON_GROUND_ITEM = 25;

	public static final int ITEM_ON_PLAYER = 14;

	private static void itemOnItem(Player player, Packet packet) {
		int usedWithSlot = packet.readUnsignedShort();
		int itemUsedSlot = packet.readUnsignedShortA();
		if (usedWithSlot < 0 || itemUsedSlot < 0 || itemUsedSlot > player.getInventory().capacity()
				|| usedWithSlot > player.getInventory().capacity())
			return;
		Item usedWith = player.getInventory().getItems()[usedWithSlot];
		Item itemUsedWith = player.getInventory().getItems()[itemUsedSlot];
		if (usedWith.getId() == 6573 || itemUsedWith.getId() == 6573) {
			player.getPacketSender().sendMessage("To make an Amulet of Fury, you need to put an onyx in a furnace.");
			return;
		}
		WeaponPoison.execute(player, itemUsedWith.getId(), usedWith.getId());
		if (itemUsedWith.getId() == 590 || usedWith.getId() == 590)
			Firemaking.lightFire(player, itemUsedWith.getId() == 590 ? usedWith.getId() : itemUsedWith.getId(), false,
					1);
		if (itemUsedWith.getDefinition().getName().contains("(") && usedWith.getDefinition().getName().contains("("))
			PotionCombinating.combinePotion(player, usedWith.getId(), itemUsedWith.getId());
		if (usedWith.getId() == Herblore.VIAL || itemUsedWith.getId() == Herblore.VIAL) {
			if (Herblore.makeUnfinishedPotion(player, usedWith.getId())
					|| Herblore.makeUnfinishedPotion(player, itemUsedWith.getId()))
				return;
		}
		if (Herblore.finishPotion(player, usedWith.getId(), itemUsedWith.getId())
				|| Herblore.finishPotion(player, itemUsedWith.getId(), usedWith.getId()))
			return;
		if ((usedWith.getId() == 3279) && (itemUsedWith.getId() == 3283) && (player.getInventory().contains(3279))
				&& (player.getInventory().contains(3283))) {
			player.getPacketSender().sendMessage("You attach a suppressor to your weapon.");
			player.getInventory().delete(3279, 1);
			player.getInventory().delete(3283, 1);
			player.getInventory().add(3280, 1);
		}
		if ((usedWith.getId() == 3081) && (itemUsedWith.getId() == 3282) && (player.getInventory().contains(3081))
				&& (player.getInventory().contains(3282))) {
			player.getPacketSender().sendMessage("You attach a grenade launcher to your AK-47.");
			player.getInventory().delete(3081, 1);
			player.getInventory().delete(3282, 1);
			player.getInventory().add(3082, 1);
		}
		if ((usedWith.getId() == 3092) && (itemUsedWith.getId() == 3283) && (player.getInventory().contains(3092))
				&& (player.getInventory().contains(3283))) {
			player.getPacketSender().sendMessage("You attach a suppressor to your Desert Eagle.");
			player.getInventory().delete(3092, 1);
			player.getInventory().delete(3283, 1);
			player.getInventory().add(3135, 1);
		}
		if ((usedWith.getId() == 3277) && (itemUsedWith.getId() == 3281) && (player.getInventory().contains(3277))
				&& (player.getInventory().contains(3281))) {
			player.getPacketSender().sendMessage("You attach a scope to your sniper.");
			player.getInventory().delete(3277, 1);
			player.getInventory().delete(3281, 1);
			player.getInventory().add(3278, 1);
		}
		if (usedWith.getId() == 946 || itemUsedWith.getId() == 946)
			Fletching.openSelection(player, usedWith.getId() == 946 ? itemUsedWith.getId() : usedWith.getId());
		if (usedWith.getId() == 1777 || itemUsedWith.getId() == 1777)
			Fletching.openBowStringSelection(player,
					usedWith.getId() == 1777 ? itemUsedWith.getId() : usedWith.getId());
		if (usedWith.getId() == 53 || itemUsedWith.getId() == 53 || usedWith.getId() == 52
				|| itemUsedWith.getId() == 52)
			Fletching.makeArrows(player, usedWith.getId(), itemUsedWith.getId());
		if (itemUsedWith.getId() == 1755 || usedWith.getId() == 1755)
			Gems.selectionInterface(player, usedWith.getId() == 1755 ? itemUsedWith.getId() : usedWith.getId());
		if (usedWith.getId() == 1733 || itemUsedWith.getId() == 1733)
			LeatherMaking.craftLeatherDialogue(player, usedWith.getId(), itemUsedWith.getId());
		Herblore.handleSpecialPotion(player, itemUsedWith.getId(), usedWith.getId());
		ItemForging.forgeItem(player, itemUsedWith.getId(), usedWith.getId());
		if (player.getRights() == PlayerRights.DEVELOPER)
			player.getPacketSender().sendMessage(
					"ItemOnItem - [usedItem, usedWith] : [" + usedWith.getId() + ", " + itemUsedWith.getId() + "]");
	}

	@SuppressWarnings("unused")
	private static void itemOnNpc(final Player player, Packet packet) {
		int id = packet.readShortA();
		int index = packet.readShortA();
		final int slot = packet.readLEShort();
	}

	@SuppressWarnings("unused")
	private static void itemOnObject(Player player, Packet packet) {
		int interfaceType = packet.readShort();
		final int objectId = packet.readShort();
		final int objectY = packet.readLEShortA();
		final int itemSlot = packet.readLEShort();
		final int objectX = packet.readLEShortA();
		final int itemId = packet.readShort();

		if (itemSlot < 0 || itemSlot > player.getInventory().capacity())
			return;
		final Item item = player.getInventory().getItems()[itemSlot];
		if (item == null)
			return;
		final GameObject gameObject = new GameObject(objectId,
				new Position(objectX, objectY, player.getPosition().getZ()));
		if (objectId > 0 && objectId != 6 && !RegionClipping.objectExists(gameObject)) {
			// player.getPacketSender().sendMessage("An error occured. Error
			// code: "+id).sendMessage("Please report the error to a staff
			// member.");
			return;
		}
		player.setInteractingObject(gameObject);
		player.setWalkToTask(new WalkToTask(player, gameObject.getPosition().copy(), gameObject.getSize(),
				new FinalizedMovementTask() {
					@Override
					public void execute() {
						if (CookingData.forFish(item.getId()) != null && CookingData.isRange(objectId)) {
							player.setPositionToFace(gameObject.getPosition());
							Cooking.selectionInterface(player, CookingData.forFish(item.getId()));
							return;
						}
						if (Prayer.isBone(itemId) && objectId == 409) {
							BonesOnAltar.openInterface(player, itemId);
							return;
						}
						if (player.getFarming().plant(itemId, objectId, objectX, objectY))
							return;
						if (player.getFarming().useItemOnPlant(itemId, objectX, objectY))
							return;
						switch (objectId) {
						case 6189:
							if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 80) {
								player.getPacketSender()
										.sendMessage("You need a Crafting level of at least 80 to make that item.");
								return;
							}
							if (player.getInventory().contains(6573)) {
								if (player.getInventory().contains(1597)) {
									if (player.getInventory().contains(1759)) {
										player.performAnimation(new Animation(896));
										player.getInventory().delete(new Item(1759)).delete(new Item(6573))
												.add(new Item(6585));
										player.getPacketSender().sendMessage(
												"You put the items into the furnace to forge an Amulet of Fury.");
									} else {
										player.getPacketSender().sendMessage("You need some Ball of Wool to do this.");
									}
								} else {
									player.getPacketSender().sendMessage("You need a Necklace mould to do this.");
								}
							}
							break;
						case 7836:
						case 7808:
							if (itemId == 6055) {
								int amt = player.getInventory().getAmount(6055);
								if (amt > 0) {
									player.getInventory().delete(6055, amt);
									player.getPacketSender().sendMessage("You put the weed in the compost bin.");
									player.getSkillManager().addExperience(Skill.FARMING, 20 * amt);
								}
							}
							break;
						case 4306:
							EquipmentMaking.handleAnvil(player);
							break;
						}
					}
				}));
	}

	@SuppressWarnings("unused")
	private static void itemOnPlayer(Player player, Packet packet) {
		int interfaceId = packet.readUnsignedShortA();
		int targetIndex = packet.readUnsignedShort();
		int itemId = packet.readUnsignedShort();
		int slot = packet.readLEShort();
		if (slot < 0 || slot > player.getInventory().capacity() || targetIndex > World.getPlayers().capacity())
			return;
		Player target = World.getPlayers().get(targetIndex);
		if (target == null)
			return;
		switch (itemId) {
		case 962:
			if (!player.getInventory().contains(962) || player.getRights() == PlayerRights.ADMINISTRATOR)
				return;
			player.setPositionToFace(target.getPosition());
			player.performGraphic(new Graphic(1006));
			player.performAnimation(new Animation(451));
			player.getPacketSender().sendMessage("You pull the Christmas cracker...");
			target.getPacketSender().sendMessage("" + player.getUsername() + " pulls a Christmas cracker on you..");
			player.getInventory().delete(962, 1);
			player.getPacketSender().sendMessage("The cracker explodes and you receive a Party hat!");
			player.getInventory().add(1038 + Misc.getRandom(10), 1);
			target.getPacketSender().sendMessage("" + player.getUsername() + " has received a Party hat!");
			/*
			 * if(Misc.getRandom(1) == 1) { target.getPacketSender().
			 * sendMessage("The cracker explodes and you receive a Party hat!");
			 * target.getInventory().add((1038 + Misc.getRandom(5)*2), 1);
			 * player.getPacketSender().sendMessage(""+target.getUsername()
			 * +" has received a Party hat!"); } else { player.getPacketSender().
			 * sendMessage("The cracker explodes and you receive a Party hat!");
			 * player.getInventory().add((1038 + Misc.getRandom(5)*2), 1);
			 * target.getPacketSender().sendMessage(""+player.getUsername()
			 * +" has received a Party hat!"); }
			 */
			break;
		case 4155:
			if (player.getSlayer().getDuoPartner() != null) {
				player.getPacketSender().sendMessage("You already have a duo partner.");
				return;
			}
			if (player.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
				player.getPacketSender().sendMessage("You already have a Slayer task. You must reset it first.");
				return;
			}
			Player duoPartner = World.getPlayers().get(targetIndex);
			if (duoPartner != null) {
				if (duoPartner.getSlayer().getDuoPartner() != null) {
					player.getPacketSender().sendMessage("This player already has a duo partner.");
					return;
				}
				if (duoPartner.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
					player.getPacketSender().sendMessage("This player already has a Slayer task.");
					return;
				}
				if (duoPartner.getSlayer().getSlayerMaster() != player.getSlayer().getSlayerMaster()) {
					player.getPacketSender().sendMessage("You do not have the same Slayer master as that player.");
					return;
				}
				if (duoPartner.busy() || duoPartner.getLocation() == Location.WILDERNESS) {
					player.getPacketSender().sendMessage("This player is currently busy.");
					return;
				}
				DialogueManager.start(duoPartner, SlayerDialogues.inviteDuo(duoPartner, player));
				player.getPacketSender()
						.sendMessage("You have invited " + duoPartner.getUsername() + " to join your Slayer duo team.");
			}
			break;
		}
	}

	/**
	 * The PacketListener logger to debug information and print out errors.
	 */
	// private final static Logger logger =
	// Logger.getLogger(UseItemPacketListener.class);

	@SuppressWarnings("unused")
	private static void useItem(Player player, Packet packet) {
		if (player.isTeleporting() || player.getConstitution() <= 0)
			return;
		int interfaceId = packet.readLEShortA();
		int slot = packet.readShortA();
		int id = packet.readLEShort();
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		switch (packet.getOpcode()) {
		case ITEM_ON_ITEM:
			itemOnItem(player, packet);
			break;
		case USE_ITEM:
			useItem(player, packet);
			break;
		case ITEM_ON_OBJECT:
			itemOnObject(player, packet);
			break;
		case ITEM_ON_GROUND_ITEM:
			// TODO
			break;
		case ITEM_ON_NPC:
			itemOnNpc(player, packet);
			break;
		case ITEM_ON_PLAYER:
			itemOnPlayer(player, packet);
			break;
		}
	}
}