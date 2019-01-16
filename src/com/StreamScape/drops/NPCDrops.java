package com.StreamScape.drops;

import com.StreamScape.model.*;
import com.StreamScape.model.Locations.Location;
import com.StreamScape.model.PlayerRanks.DonorRights;
import com.StreamScape.model.container.impl.Bank;
import com.StreamScape.util.JsonLoader;
import com.StreamScape.util.Misc;
import com.StreamScape.util.RandomUtility;
import com.StreamScape.world.World;
import com.StreamScape.world.content.PlayerLogs;
import com.StreamScape.world.content.clan.ClanChatManager;
import com.StreamScape.world.content.skill.impl.prayer.BonesData;
import com.StreamScape.world.content.skill.impl.summoning.CharmingImp;
import com.StreamScape.world.entity.impl.GroundItemManager;
import com.StreamScape.world.entity.impl.npc.NPC;
import com.StreamScape.world.entity.impl.player.Player;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Controls the npc drops
 *
 * @author 2012 <http://www.rune-server.org/members/dexter+morgan/>, Gabbe &
 *         Samy
 *
 */
public class NPCDrops {

	/**
	 * The map containing all the npc drops.
	 */
	private static Map<Integer, NPCDrops> dropControllers = new HashMap<Integer, NPCDrops>();

	public static JsonLoader parseDrops() {

		return new JsonLoader() {

			@Override
			public void load(JsonObject reader, Gson builder) {
				int[] npcIds = builder.fromJson(reader.get("npcIds"),
						int[].class);
				NpcDrop[] drops = builder.fromJson(reader.get("drops"),
						NpcDrop[].class);

				NPCDrops d = new NPCDrops();
				d.npcIds = npcIds;
				d.drops = drops;

				for (int id : npcIds) {
					dropControllers.put(id, d);
//					System.out.println("put: "+id+" "+d);
				}
			}

			@Override
			public String filePath() {
				return "./data/def/json/drops.json";
			}
		};
	}


	public static void init() {
		new JsonLoader() {

			@Override
			public String filePath() {
				return "./data/def/json/drops.json";
			}

			@Override // NOTE: load cycles through each object.
			public void load(JsonObject reader, Gson builder) {

				final int id[] = builder.fromJson(reader.get("npcIds"), int[].class);
				int[] npcIds = builder.fromJson(reader.get("npcIds"),
						int[].class);
				final NpcDrop[] dynamic = builder.fromJson(reader.get("drops"), NpcDrop[].class);
				NpcDrop[] drops = builder.fromJson(reader.get("drops"),
						NpcDrop[].class);

				NPCDrops d = new NPCDrops();
				d.npcIds = npcIds;
				d.drops = drops;

				for (int ids : id) {
					npcDrops.put(ids, dynamic);
				}
			}
		}.load();
	}



	/**
	 * The id's of the NPC's that "owns" this class.
	 */
	private int[] npcIds;

	/**
	 * All the drops that belongs to this class.
	 */
	private NpcDrop[] drops;

	/**
	 * Gets the NPC drop controller by an id.
	 *
	 * @return The NPC drops associated with this id.
	 */
	public static NPCDrops forId(int id) {
		return dropControllers.get(id);
	}

	public static Map<Integer, NPCDrops> getDrops() {
		return dropControllers;
	}

	/**
	 * Gets the drop list
	 *
	 * @return the list
	 */
	public NpcDrop[] getDropList() {
		return drops;
	}

	/**
	 * Gets the npcIds
	 *
	 * @return the npcIds
	 */
	public int[] getNpcIds() {
		return npcIds;
	}

	/**
	 * Represents a npc drop item
	 */
	public static class NpcDrop {
		private int id;
		private int[] count;
		private int chance;

		public NpcDrop(int id, int[] count, int chance) {
			this.setId(id);
			this.setCount(count);
			this.setChance(chance);
		}

		/**
		 * Sets the id
		 *
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * Sets the id
		 *
		 * @param id
		 *            the id
		 */
		public void setId(int id) {
			this.id = id;
		}

		/**
		 * Sets the count
		 *
		 * @return the count
		 */
		public int[] getCount() {
			return count;
		}

		/**
		 * Sets the count
		 *
		 * @param count
		 *            the count
		 */
		public void setCount(int[] count) {
			this.count = count;
		}

		/**
		 * Sets the chance
		 *
		 * @return the chance
		 */
		public int getChance() {
			return chance;
		}

		public int compareTo(NpcDrop otherDrop) {
			return this.chance - otherDrop.chance;
		}

		/**
		 * Sets the chance
		 *
		 * @param chance
		 *            the chance
		 */
		public void setChance(int chance) {
			this.chance = chance;
		}
		/**
		 * Gets the item
		 *
		 * @return the item
		 */
		public Item getItem() {
			int amount = 0;
			for (int i = 0; i < count.length; i++)
				amount += count[i];
			if (amount > count[0])
				amount = count[0] + RandomUtility.getRandom(count[1]);
			return new Item(id, amount);
		}
	}




	private static Map<Integer, NpcDrop[]> npcDrops = new HashMap<Integer, NpcDrop[]>();
	private static List<NpcDrop> constantDrops = new ArrayList<>();
	private static List<NpcDrop> potentialDrops = new ArrayList<>();
	private static List<NpcDrop> finalDropList = new ArrayList<>();

	public static double dropModification(Player player) {
		double mod = 0.0;

			if (player.getDonor() == DonorRights.DONOR) {
				mod = 8.0;
			} else if (player.getDonor() == DonorRights.DELUXE_DONOR) {
				mod = 13.0;
			} else if (player.getDonor() == DonorRights.SPONSOR) {
				mod = 20.0;
			}
			if (player.getGameMode() == GameMode.HARDCORE) {
				mod += 10.0;
			}
			if (player.getGameMode() == GameMode.IRONMAN) {
				mod += 30.0;
			}

			if (player.getEquipment().contains(596)) {
				mod += 5.0;
			}
			if (player.getEquipment().contains(2572)) {
				mod += 2.0;
			}
			if (player.getEquipment().contains(773)) {
				mod += 10.0;
			}
			if (player.getEquipment().contains(774)) {
				mod += 10.0;
			}
		if(player.getRights() == PlayerRanks.PlayerRights.OWNER) {
			mod = 100.0;
		}
		return mod;
	}



	public static void order(NpcDrop[] npcDrops) {
		for (int i = 0; i < npcDrops.length; ++i) {
			for (int j = i; j < npcDrops.length; ++j) {
				if (npcDrops[i].compareTo(npcDrops[j]) > 0) {
					NpcDrop temp = npcDrops[i];
					npcDrops[i] = npcDrops[j];
					npcDrops[j] = temp;
				}
			}
		}// done
	}

	public static void oppositeOrder(NpcDrop[] npcDrops) {
		for (int i = 0; i < npcDrops.length; ++i) {
			for (int j = 0; j < npcDrops.length; ++j) {
				if (npcDrops[i].compareTo(npcDrops[j]) > 0) {
					NpcDrop temp = npcDrops[i];
					npcDrops[i] = npcDrops[j];
					npcDrops[j] = temp;
				}
			}
		}// done
	}


	public static ArrayList<NpcDrop> getRareDrops(NpcDrop[] drops) {
		ArrayList<NpcDrop> rareDrops = new ArrayList<NpcDrop>();
		for (NpcDrop drop : drops) {
			if (drop.getChance() > 1)
				rareDrops.add(drop);
		}
		return rareDrops;
	}


	public static void handleDrops(Player player, NPC npc) {
		int npcId = npc.getId();

		NpcDrop[] drops = npcDrops.get(npcId);

		if (drops == null) {
			return;
		}

		player.increaseDKC(npc.getId());
		Integer kc = player.getDropKillCount().get(npcId);
		if (kc == null) {
			return;
		}
		double roll_chance = new SecureRandom().nextDouble();
		double mod = dropModification(player);

		for (int i = 0; i < drops.length; i++) {
			NpcDrop drop = drops[i];
			if (drop == null)
				continue;

			double divisor = drop.getChance();
			double required = 1 / divisor;
			if (drop.getChance() == 1.0) {
				finalDropList.add(drop);
			} else {
				if (roll_chance -(roll_chance /100 * mod) <= required) {
					potentialDrops.add(drop);
				}
			}
		}
		if (!constantDrops.isEmpty()) {
			for(int i = 0; i < constantDrops.size(); i++ ) {
				finalDropList.add(constantDrops.get(Misc.randomMinusOne(constantDrops.size())));

			}
		}
		if (!potentialDrops.isEmpty()) {
			finalDropList.add(potentialDrops.get(Misc.randomMinusOne(potentialDrops.size())));
		}
		if (!finalDropList.isEmpty()) {
			for (NpcDrop drop : finalDropList) {
				int amount = drop.getCount()[0];
				if (drop.getCount().length > 1) {
					amount = drop.getCount()[0] + new SecureRandom().nextInt(drop.getCount()[1] - drop.getCount()[0]);
				}
				GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(drop.getId(), amount),
						npc.getPosition().copy(), player.getUsername(), false, 150, false, 200));
				DropLog.submit(player, new DropLog.DropLogEntry(drop.getId(), amount));
				if (DropAnnouncer.shouldAnnounce(drop)) {
					DropAnnouncer.announce(player, drop.getId(), npcId, amount, drop.getChance());
				}
			}
			finalDropList.clear();
			constantDrops.clear();
			potentialDrops.clear();
		}
	}

	public static void drop(Player player, NpcDrop drop, NPC npc, Position pos,
							boolean goGlobal) {
		Item item = drop.getItem();
		//	if (player.getEquipment().contains(12601)) {
		//	player.getPacketSender().sendMessage("@red@Your Ring of the Gods has boosted your drop rate by 25%.");
		//}

		//if (player.getEquipment().contains(2572)) {
		//player.getPacketSender().sendMessage("@red@Your Ring of Wealth has boosted your drop rate by 10%.");
		//}
		if(npc.getId() == 2042 || npc.getId() == 2043 || npc.getId() == 2044) {
			pos = player.getPosition().copy();
		}

		if (player.getInventory().contains(18337)
				&& BonesData.forId(item.getId()) != null) {
			player.getPacketSender().sendGlobalGraphic(new Graphic(777), pos);
			player.getSkillManager().addExperience(Skill.PRAYER,
					BonesData.forId(item.getId()).getBuryingXP());
			return;
		}
		int itemId = item.getId();
		int amount = item.getAmount();

		if (itemId == 6731 ||  itemId == 6914 || itemId == 7158 ||  itemId == 6889 || itemId == 6733 || itemId == 15019 || itemId == 11235 || itemId == 15020 || itemId == 15018 || itemId == 15220 || itemId == 6735 || itemId == 6737 || itemId == 6585 || itemId == 4151 || itemId == 4087 || itemId == 2577 || itemId == 2581 || itemId == 11732 || itemId == 18782 ) {
			player.getPacketSender().sendMessage("@red@ YOU HAVE RECEIVED A MEDIUM DROP, CHECK THE GROUND!");

		}

		if (itemId == CharmingImp.GOLD_CHARM
				|| itemId == CharmingImp.GREEN_CHARM
				|| itemId == CharmingImp.CRIM_CHARM
				|| itemId == CharmingImp.BLUE_CHARM) {
			if (player.getInventory().contains(6500)
					&& CharmingImp.handleCharmDrop(player, itemId, amount)) {
				return;
			}
		}

		Player toGive = player;

		boolean ccAnnounce = false;
		if(Location.inMulti(player)) {
			if(player.getCurrentClanChat() != null && player.getCurrentClanChat().getLootShare()) {
				CopyOnWriteArrayList<Player> playerList = new CopyOnWriteArrayList<Player>();
				for(Player member : player.getCurrentClanChat().getMembers()) {
					if(member != null) {
						if(member.getPosition().isWithinDistance(player.getPosition())) {
							playerList.add(member);
						}
					}
				}
				if(playerList.size() > 0) {
					toGive = playerList.get(RandomUtility.getRandom(playerList.size() - 1));
					if(toGive == null || toGive.getCurrentClanChat() == null || toGive.getCurrentClanChat() != player.getCurrentClanChat()) {
						toGive = player;
					}
					ccAnnounce = true;
				}
			}
		}

		if(itemId == 18778) { //Effigy, don't drop one if player already has one
			if(toGive.getInventory().contains(18778) || toGive.getInventory().contains(18779) || toGive.getInventory().contains(18780) || toGive.getInventory().contains(18781)) {
				return;
			}
			for(Bank bank : toGive.getBanks()) {
				if(bank == null) {
					continue;
				}
				if(bank.contains(18778) || bank.contains(18779) || bank.contains(18780) || bank.contains(18781)) {
					return;
				}
			}
		}

		if (ItemDropAnnouncer.announce(drop)) {
			String itemName = item.getDefinition().getName();
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			String npcName = Misc.formatText(npc.getDefinition().getName());

			if (player.getRights() == PlayerRanks.PlayerRights.DEVELOPER) {
				GroundItemManager.spawnGroundItem(toGive, new GroundItem(item, pos,
						toGive.getUsername(), false, 150, goGlobal, 200));
			}
			switch (itemId) {
				case 14484:
					itemMessage = "a pair of Dragon Claws";
					break;
				case 20000:
				case 20001:
				case 20002:
					itemMessage = itemName;
					break;
			}
			switch (npc.getId()) {
				case 81:
					npcName = "a Cow";
					break;
				case 50:
				case 3200:
				case 8133:
				case 4540:
				case 1160:
				case 8549:
					npcName = "The " + npcName + "";
					break;
				case 51:
				case 54:
				case 5363:
				case 8349:
				case 1592:
				case 1591:
				case 1590:
				case 1615:
				case 9463:
				case 9465:
				case 9467:
				case 1382:
				case 13659:
				case 11235:
					npcName = "" + Misc.anOrA(npcName) + " " + npcName + "";
					break;
			}
			String message = "@blu@[RARE DROP] " + toGive.getUsername()
					+ " has just received @red@" + itemMessage + "@blu@ from " + npcName
					+ "!";
			World.sendMessage(message);

			if(ccAnnounce) {
				ClanChatManager.sendMessage(player.getCurrentClanChat(), "<col=16777215>[<col=255>Lootshare<col=16777215>]<col=3300CC> "+toGive.getUsername()+" received " + itemMessage + " from "+npcName+"!");
			}

			PlayerLogs.log(toGive.getUsername(), "" + toGive.getUsername() + " received " + itemMessage + " from " + npcName + "");
		}

		GroundItemManager.spawnGroundItem(toGive, new GroundItem(item, pos,
				toGive.getUsername(), false, 150, goGlobal, 200));
		DropLog.submit(toGive, new DropLog.DropLogEntry(itemId, item.getAmount()));
	}



	public static class ItemDropAnnouncer {

		public static boolean announce(NpcDrop drop) {
			return drop.getChance() >= 50;
		}
	}
}