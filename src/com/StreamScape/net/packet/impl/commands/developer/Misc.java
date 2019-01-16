package com.StreamScape.net.packet.impl.commands.developer;

import com.StreamScape.GameServer;
import com.StreamScape.engine.task.Task;
import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.model.*;
import com.StreamScape.model.container.impl.Equipment;
import com.StreamScape.model.container.impl.Shop.ShopManager;
import com.StreamScape.model.definitions.ItemDefinition;
import com.StreamScape.model.definitions.WeaponAnimations;
import com.StreamScape.model.definitions.WeaponInterfaces;
import com.StreamScape.world.World;
import com.StreamScape.world.clip.region.RegionClipping;
import com.StreamScape.world.content.BonusManager;
import com.StreamScape.world.content.WellOfGoodwill;
import com.StreamScape.world.content.clan.ClanChatManager;
import com.StreamScape.world.content.combat.weapon.CombatSpecial;
import com.StreamScape.world.content.minigames.impl.HungerGames;
import com.StreamScape.world.content.skill.SkillManager;
import com.StreamScape.world.entity.impl.npc.NPC;
import com.StreamScape.world.entity.impl.player.Player;

//import com.StreamScape.world.content.minigames.impl.HungerGamesMinigame;

public class Misc {

	public static void checkCommands(Player player, String wholeCommand, String[] command) {
		if (command[0].equals("togglehunger")) {
			HungerGames hg = (HungerGames)World.minigameHandler.getMinigame(1);
			hg.setActivated(!hg.isActivated());
			if (hg.isActivated())
				World.sendMessage("@blu@[NEWS] @red@" + hg.getName() + " have been activated");
			else {
				World.sendMessage("@blue@[NEWS] @red@" + hg.getName() + " have been de-activated");
			}
			return;
		}
		if (wholeCommand.equalsIgnoreCase("mypos")) {
			player.getPacketSender().sendMessage("X: " + player.getPosition().getX());
			player.getPacketSender().sendMessage("Y: " + player.getPosition().getY());
			player.getPacketSender().sendMessage("Z: " + player.getPosition().getZ());
		}
		if (command[0].equalsIgnoreCase("tele")) {
			int x = Integer.parseInt(command[1]);
			int y = Integer.parseInt(command[2]);
			int z = Integer.parseInt(command[3]);
			player.moveTo(new Position(x, y, z));
			player.getPacketSender().sendMessage("You teleport to X: " + x + " Y: " + y + " Z: " + z);
		}
		if (command[0].equalsIgnoreCase("master")) {
			for (Skill skill : Skill.values()) {
				int level = SkillManager.getMaxAchievingLevel(skill);
				player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
						SkillManager.getExperienceForLevel(level == 120 ? 120 : 99));
			}
			player.getPacketSender().sendConsoleMessage("You are now a master of all skills.");
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equalsIgnoreCase("bank")) {
			player.getBank(player.getCurrentBankTab()).open();
		}
		if (command[0].equalsIgnoreCase("spec")) {
			player.setSpecialPercentage(100);
			CombatSpecial.updateBar(player);
		}
		if (command[0].equalsIgnoreCase("runes")) {
			for (Item t : ShopManager.getShops().get(0).getItems()) {
				if (t != null) {
					player.getInventory().add(new Item(t.getId(), 200000));
				}
			}
		}
		// if (command[0].equalsIgnoreCase("hg")) {
		// HungerGamesMinigame.openGameSelection(player);
		// }
		if (command[0].equalsIgnoreCase("tasks")) {
			player.getPacketSender().sendConsoleMessage("Found " + TaskManager.getTaskAmount() + " tasks.");
		}
		if (command[0].equalsIgnoreCase("scroll")) {
			int child = Integer.parseInt(command[1]);
			int amount = Integer.parseInt(command[2]);
			player.getPacketSender().setScrollBar(child, amount);
		}
		if (command[0].equalsIgnoreCase("frame")) {
			int frame = Integer.parseInt(command[1]);
			String text = command[2];
			player.getPacketSender().sendString(frame, text);
		}
		if (command[0].equalsIgnoreCase("playnpc")) {
			player.setNpcTransformationId(Integer.parseInt(command[1]));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		} else if (command[0].equalsIgnoreCase("playobject")) {
			player.getPacketSender().sendObjectAnimation(new GameObject(2283, player.getPosition().copy()),
					new Animation(751));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equalsIgnoreCase("interface")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendInterface(id);
		}
		if (command[0].equalsIgnoreCase("walkableinterface")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendWalkableInterface(id);
		}
		if (command[0].equalsIgnoreCase("anim")) {
			int id = Integer.parseInt(command[1]);
			player.performAnimation(new Animation(id));
			player.getPacketSender().sendConsoleMessage("Sending animation: " + id);
		}
		if (command[0].equalsIgnoreCase("gfx")) {
			int id = Integer.parseInt(command[1]);
			player.performGraphic(new Graphic(id));
			player.getPacketSender().sendConsoleMessage("Sending graphic: " + id);
		}
		if (command[0].equalsIgnoreCase("object")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendObject(new GameObject(id, player.getPosition(), 10, 3));
			player.getPacketSender().sendConsoleMessage("Sending object: " + id);
		}
		if (command[0].equalsIgnoreCase("config")) {
			int id = Integer.parseInt(command[1]);
			int state = Integer.parseInt(command[2]);
			player.getPacketSender().sendConfig(id, state).sendConsoleMessage("Sent config.");
		}
		if (command[0].equals("pray")) {
			player.getSkillManager().setCurrentLevel(Skill.PRAYER, 15000);
		}
		if (command[0].equalsIgnoreCase("walk")) {
			player.getPacketSender().sendMessage("CAN WALK:" + player.getMovementQueue().canWalk());
			player.getPacketSender().sendMessage("BLOCKED EAST:" + RegionClipping.blockedEast(player.getPosition()));
			player.getPacketSender().sendMessage("BLOCKED WEST:" + RegionClipping.blockedWest(player.getPosition()));
			player.getPacketSender().sendMessage("BLOCKED SOUTH:" + RegionClipping.blockedSouth(player.getPosition()));
			player.getPacketSender().sendMessage("BLOCKED NORTH:" + RegionClipping.blockedNorth(player.getPosition()));
			player.getPacketSender().sendMessage("region: " + player.getPosition().getRegionId());
		}
		if (command[0].equalsIgnoreCase("find")) {
			String name = wholeCommand.substring(5).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendConsoleMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendConsoleMessage("Found item with name ["
							+ ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
			}
		}
		if (command[0].equalsIgnoreCase("pure")) {
			int[][] data = new int[][] { { Equipment.HEAD_SLOT, 1153 }, { Equipment.CAPE_SLOT, 10499 },
					{ Equipment.AMULET_SLOT, 1725 }, { Equipment.WEAPON_SLOT, 4587 }, { Equipment.BODY_SLOT, 1129 },
					{ Equipment.SHIELD_SLOT, 1540 }, { Equipment.LEG_SLOT, 2497 }, { Equipment.HANDS_SLOT, 7459 },
					{ Equipment.FEET_SLOT, 3105 }, { Equipment.RING_SLOT, 2550 }, { Equipment.AMMUNITION_SLOT, 9244 } };
			for (int i = 0; i < data.length; i++) {
				int slot = data[i][0], id = data[i][1];
				player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 500 : 1));
			}
			BonusManager.update(player);
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			player.getEquipment().refreshItems();
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getInventory().resetItems();
			player.getInventory().add(1216, 1000).add(9186, 1000).add(862, 1000).add(892, 10000).add(4154, 5000)
					.add(2437, 1000).add(2441, 1000).add(2445, 1000).add(386, 1000).add(2435, 1000);
			player.getSkillManager().newSkillManager();
			player.getSkillManager().setMaxLevel(Skill.ATTACK, 60).setMaxLevel(Skill.STRENGTH, 85)
					.setMaxLevel(Skill.RANGED, 85).setMaxLevel(Skill.PRAYER, 520).setMaxLevel(Skill.MAGIC, 70)
					.setMaxLevel(Skill.CONSTITUTION, 850);
			for (Skill skill : Skill.values()) {
				player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
						.setExperience(skill,
								SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
			}
		}
		if (command[0].equalsIgnoreCase("customs")) {
			player.getBank(player.getCurrentBankTab()).add(6500, 1).add(14667, 1).add(2996, 1).add(5510, 1).add(5512, 1)
					.add(5509, 1).add(11998, 1).add(1389, 1).add(17401, 1).add(15078, 1).add(15009, 1).add(15262, 1)
					.add(6570, 1).add(4155, 1).add(13663, 1).add(292, 1).add(19111, 1).add(13262, 1).add(989, 1000)
					.add(995, 1000000).add(17291, 1).add(20084, 1).add(6199, 1000).add(15501, 1000).add(14022, 1)
					.add(14018, 1).add(20250, 1).add(20251, 1).add(20252, 1).add(20253, 1).add(14556, 1).add(14557, 1)
					.add(14558, 1).add(14559, 1).add(14560, 1).add(14561, 1).add(14562, 1).add(14563, 1).add(14564, 1)
					.add(14565, 1).add(14566, 1).add(14567, 1).add(14568, 1).add(14569, 1).add(14570, 1).add(14571, 1)
					.add(14572, 1).add(14580, 1).add(14581, 1).add(14582, 1).add(14583, 1).add(14584, 1).add(14585, 1)
					.add(14586, 1).add(14587, 1).add(14588, 1).add(732, 1).add(14590, 1).add(14591, 1).add(79, 1)
					.add(80, 1).add(81, 1).add(14619, 1).add(14596, 1).add(14597, 1).add(14598, 1).add(14599, 1)
					.add(18742, 1).add(82, 1).add(894, 1).add(895, 1).add(896, 1).add(924, 1).add(2380, 1).add(909, 1)
					.add(898, 1).add(899, 1).add(900, 1).add(901, 1).add(902, 1).add(903, 1).add(2548, 1).add(2547, 1)
					.add(904, 1).add(3088, 1).add(906, 1).add(907, 1).add(908, 1).add(926, 1).add(3878, 1).add(910, 1)
					.add(911, 1).add(912, 1).add(3091, 1).add(914, 1).add(3089, 1).add(20259, 1000).add(20260, 1000)
					.add(20256, 1000).add(20249, 1000).add(20254, 1000).add(20255, 1000).add(3879, 1000).add(1480, 1)
					.add(1855, 1).add(2755, 1).add(2756, 1).add(2757, 1).add(2758, 1).add(2759, 1).add(2760, 1)
					.add(2761, 1).add(2762, 1).add(2763, 1).add(2764, 1).add(2765, 1).add(2766, 1).add(2767, 1)
					.add(2768, 1).add(2769, 1).add(2770, 1).add(2771, 1).add(2772, 1).add(4832, 1).add(4834, 1)
					.add(4837, 1).add(4839, 1).add(2885, 1).add(3063, 1).add(3064, 1).add(3065, 1).add(3066, 1)
					.add(3067, 1).add(3068, 1).add(3069, 1).add(3070, 1).add(3071, 1).add(3072, 1).add(3073, 1)
					.add(3074, 1).add(3075, 1).add(3076, 1).add(6862, 1).add(667, 1).add(3633, 1).add(3664, 1)
					.add(3635, 1).add(3636, 1).add(3637, 1).add(3638, 1).add(3639, 1).add(3640, 1).add(3642, 1)
					.add(4083, 1).add(4177, 1).add(3643, 1).add(3644, 1).add(3645, 1).add(3646, 1).add(941, 1)
					.add(2749, 1).add(2750, 1).add(2751, 1).add(2752, 1).add(2753, 1).add(2754, 1).add(1666, 1)
					.add(1667, 1).add(3647, 1).add(3648, 1).add(3649, 1).add(3650, 1).add(3651, 1).add(3652, 1)
					.add(3653, 1).add(3654, 1).add(3655, 1).add(3656, 1).add(3657, 1).add(3658, 1).add(3659, 1)
					.add(3660, 1).add(3661, 1).add(8675, 1).add(8677, 1).add(3619, 1).add(3620, 1).add(3621, 1)
					.add(3622, 1).add(980, 1000).add(17642, 1000).add(17644, 1000).add(3623, 1).add(3624, 1)
					.add(3625, 1).add(3626, 1).add(3627, 1).add(3628, 1).add(3629, 1).add(3630, 1).add(3631, 1)
					.add(3632, 1).add(3271, 1).add(3272, 1).add(3273, 1).add(3274, 1).add(3275, 1).add(3276, 1)
					.add(13664, 1000).add(3807, 1).add(3808, 1).add(3809, 1).add(3090, 1).add(3811, 1).add(3812, 1)
					.add(3080, 1).add(3081, 1).add(295, 1).add(3082, 1).add(3083, 1).add(3086, 1).add(3087, 1)
					.add(4150, 1).add(3092, 1).add(3135, 1).add(3242, 1).add(3279, 1).add(3280, 1).add(3281, 1)
					.add(3282, 1).add(3283, 1).add(3284, 1).add(3285, 1).add(3286, 1).add(3287, 1).add(3288, 1)
					.add(3289, 1).add(3290, 1).add(3291, 1).add(3292, 1).add(3293, 1).add(3294, 1).add(3295, 1)
					.add(3296, 1).add(3297, 1).add(3298, 1).add(3299, 1);
		}
		if (command[0].equalsIgnoreCase("customs2")) {
			player.getBank(player.getCurrentBankTab()).add(6756, 1).add(11288, 1).add(11289, 1).add(11290, 1)
					.add(11291, 1).add(11292, 1).add(3869, 1).add(3870, 1).add(3871, 1).add(3308, 1).add(3244, 1)
					.add(3078, 1).add(3079, 1).add(3084, 1).add(861, 1).add(2870, 1).add(3301, 1).add(3302, 1)
					.add(3300, 1).add(3303, 1).add(3304, 1).add(3305, 1).add(2633, 1).add(421, 1).add(85, 1).add(275, 1)
					.add(293, 1).add(298, 1).add(423, 1).add(432, 1).add(601, 1).add(605, 1).add(709, 1).add(758, 1)
					.add(759, 1).add(788, 1).add(983, 1).add(15, 1).add(16, 1).add(5, 1).add(993, 1).add(1545, 1)
					.add(1507, 1).add(1542, 1).add(1543, 1).add(1544, 1).add(625, 1).add(624, 1).add(623, 1).add(621, 1)
					.add(620, 1).add(619, 1).add(618, 1).add(665, 1).add(666, 1).add(669, 1).add(670, 1).add(671, 1)
					.add(672, 1).add(673, 1).add(21636, 1).add(695, 1).add(9707, 1).add(11529, 1).add(11526, 1)
					.add(11527, 1).add(11528, 1).add(11530, 1).add(11531, 1).add(11532, 1).add(8845, 1).add(13101, 1)
					.add(1321, 1).add(1333, 1).add(1291, 1).add(14099, 1).add(1237, 1).add(15241, 1).add(6731, 1)
					.add(6733, 1).add(6735, 1).add(6737, 1).add(13896, 1).add(13884, 1).add(13890, 1).add(20000, 1)
					.add(1059, 1).add(1007, 1).add(1540, 1).add(9174, 1);
		}
		if (command[0].equalsIgnoreCase("gear")) {
			int[][] data = wholeCommand.contains("jack") ? new int[][] { { Equipment.HEAD_SLOT, 1050 },
					{ Equipment.CAPE_SLOT, 12170 }, { Equipment.AMULET_SLOT, 15126 }, { Equipment.WEAPON_SLOT, 15444 },
					{ Equipment.BODY_SLOT, 14012 }, { Equipment.SHIELD_SLOT, 13740 }, { Equipment.LEG_SLOT, 14013 },
					{ Equipment.HANDS_SLOT, 7462 }, { Equipment.FEET_SLOT, 11732 }, { Equipment.RING_SLOT, 15220 } }
					: wholeCommand.contains("range")
							? new int[][] { { Equipment.HEAD_SLOT, 3749 }, { Equipment.CAPE_SLOT, 10499 },
									{ Equipment.AMULET_SLOT, 15126 }, { Equipment.WEAPON_SLOT, 18357 },
									{ Equipment.BODY_SLOT, 2503 }, { Equipment.SHIELD_SLOT, 13740 },
									{ Equipment.LEG_SLOT, 2497 }, { Equipment.HANDS_SLOT, 7462 },
									{ Equipment.FEET_SLOT, 11732 }, { Equipment.RING_SLOT, 15019 },
									{ Equipment.AMMUNITION_SLOT, 9244 }, }
							: new int[][] { { Equipment.HEAD_SLOT, 1163 }, { Equipment.CAPE_SLOT, 19111 },
									{ Equipment.AMULET_SLOT, 6585 }, { Equipment.WEAPON_SLOT, 4151 },
									{ Equipment.BODY_SLOT, 1127 }, { Equipment.SHIELD_SLOT, 13262 },
									{ Equipment.LEG_SLOT, 1079 }, { Equipment.HANDS_SLOT, 7462 },
									{ Equipment.FEET_SLOT, 11732 }, { Equipment.RING_SLOT, 2550 } };
			for (int i = 0; i < data.length; i++) {
				int slot = data[i][0], id = data[i][1];
				player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 500 : 1));
			}
			BonusManager.update(player);
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			player.getEquipment().refreshItems();
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equalsIgnoreCase("update")) {
			int time = Integer.parseInt(command[1]);
			if (time > 0) {
				GameServer.setUpdating(true);
				for (Player players : World.getPlayers()) {
					if (players == null)
						continue;
					players.getPacketSender().sendSystemUpdate(time);
				}
				TaskManager.submit(new Task(time) {
					@Override
					protected void execute() {
						for (Player player : World.getPlayers()) {
							if (player != null) {
								World.deregister(player);
							}
						}
						WellOfGoodwill.save();
						ClanChatManager.save();
						GameServer.getLogger().info("Update task finished!");
						stop();
					}
				});
			}
		}
	}

}
