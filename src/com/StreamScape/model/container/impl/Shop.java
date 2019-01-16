package com.StreamScape.model.container.impl;

import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.engine.task.impl.ShopRestockTask;
import com.StreamScape.model.GameMode;
import com.StreamScape.model.Item;
import com.StreamScape.model.PlayerRanks.PlayerRights;
import com.StreamScape.model.Skill;
import com.StreamScape.model.container.ItemContainer;
import com.StreamScape.model.container.StackType;
import com.StreamScape.model.definitions.ItemDefinition;
import com.StreamScape.model.input.impl.EnterAmountToBuyFromShop;
import com.StreamScape.model.input.impl.EnterAmountToSellToShop;
import com.StreamScape.util.JsonLoader;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.content.minigames.impl.RecipeForDisaster;
import com.StreamScape.world.entity.impl.player.Player;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Messy but perfect Shop System
 *
 * @author Gabriel Hannason
 */

public class Shop extends ItemContainer {

	public static class ShopManager {

		private static Map<Integer, Shop> shops = new HashMap<Integer, Shop>();

		public static Object[] getCustomShopData(int shop, int item) {
			if (shop == VOTING_REWARDS_STORE) {
				switch (item) {
					case 6199:
						return new Object[] { 10, "Voting points" };
					case 15501:
						return new Object[] { 50, "Voting points" };
					case 15332:
						return new Object[] { 2, "Voting points" };
					case 13663:
						return new Object[] { 1, "Voting points" };
					case 11530:
						return new Object[] { 150, "Voting points" };
					case 11531:
						return new Object[] { 125, "Voting points" };
					case 11532:
						return new Object[] { 100, "Voting points" };
					case 989:
						return new Object[] { 3, "Voting points" };
					case 295:
						return new Object[] { 200, "Voting points" };
					case 421:
						return new Object[] { 500, "Voting points" };
					case 2633:
						return new Object[] { 750, "Voting points" };
					case 11586:
						return new Object[] { 5, "Voting points" };
					case 11587:
						return new Object[] { 5, "Voting points" };
					case 11588:
						return new Object[] { 5, "Voting points" };
					case 11589:
						return new Object[] { 5, "Voting points" };
					case 11590:
						return new Object[] { 5, "Voting points" };
					case 11591:
						return new Object[] { 5, "Voting points" };
					case 20692:
						return new Object[] { 20, "Voting points" };
				}
			} else if (shop == PKING_REWARDS_STORE) {
				switch (item) {
					case 6918:
					case 6914:
					case 6889:
					case 2579:
						return new Object[] { 5, "Pk points" };
					case 6916:
						return new Object[] { 8, "Pk points" };
					case 6924:
						return new Object[] { 6, "Pk points" };
					case 6920:
					case 6922:
						return new Object[] { 4, "Pk points" };
					case 2581:
					case 11730:
						return new Object[] { 25, "Pk points" };
					case 2577:
						return new Object[] { 20, "Pk points" };
					case 15486:
					case 19111:
						return new Object[] { 30, "Pk points" };
					case 13879:
					case 13883:
					case 15243:
					case 15332:
						return new Object[] { 1, "Pk points" };
					case 15241:
					case 17273:
						return new Object[] { 20, "Pk points" };
					case 10548:
					case 10547:
					case 10551:
						return new Object[] { 12, "Pk points" };
					case 6570:
					case 20072:
					case 11235:
					case 4151:
					case 13262:
						return new Object[] { 8, "Pk points" };
					case 14484:
					case 19780:
					case 11696:
					case 11698:
					case 11700:
						return new Object[] { 50, "Pk points" };
					case 11728:
					case 15018:
					case 15020:
					case 15220:
						return new Object[] { 20, "Pk points" };
					case 11694:
						return new Object[] { 55, "Pk points" };
					// start
					//
					//
					case 19810:
						return new Object[] { 150, "Pk points" };
					case 11526:
						return new Object[] { 60, "Pk points" };
					case 11527:
						return new Object[] { 65, "Pk points" };
					case 11528:
						return new Object[] { 80, "Pk points" };
					case 11529:
						return new Object[] { 90, "Pk points" };
					case 9707:
						return new Object[] { 100, "Pk points" };
				}
			} else if (shop == ENERGY_FRAGMENT_STORE) {
				switch (item) {
					case 5509:
						return new Object[] { 400, "energy fragments" };
					case 5510:
						return new Object[] { 750, "energy fragments" };
					case 5512:
						return new Object[] { 1100, "energy fragments" };
				}
			} else if (shop == AGILITY_TICKET_STORE) {
				switch (item) {
					case 14936:
					case 14938:
						return new Object[] { 60, "agility tickets" };
					case 10941:
					case 10939:
					case 10940:
					case 10933:
						return new Object[] { 20, "agility tickets" };
					case 13661:
						return new Object[] { 100, "agility tickets" };
				}
			} else if (shop == GRAVEYARD_STORE) {
				switch (item) {
					case 18337:
						return new Object[] { 350, "zombie fragments" };
					case 7592:
					case 7593:
					case 7594:
					case 7595:
					case 7596:
						return new Object[] { 25, "zombie fragments" };
					case 3092:
						return new Object[] { 1500, "zombie fragments" };
					case 3279:
						return new Object[] { 5000, "zombie fragments" };
					case 3081:
						return new Object[] { 10000, "zombie fragments" };
					case 3277:
						return new Object[] { 12500, "zombie fragments" };
					case 3281:
						return new Object[] { 750, "zombie fragments" };
					case 3282:
						return new Object[] { 500, "zombie fragments" };
					case 3283:
						return new Object[] { 300, "zombie fragments" };
					// ray gun
					/*
					 * case 601: return new Object[] { 5000, "zombie fragments" }; // thunder gun
					 * case 423: return new Object[] { 10000, "zombie fragments" };
					 */
					case 11609:
						return new Object[] { 1, "zombie fragments" };
				}
			} else if (shop == StreamScape_POINT_STORE) {
				switch (item) {
					case 3271:
						return new Object[] { 3000, "StreamScape points" };
					case 3272:
						return new Object[] { 3000, "StreamScape points" };
					case 3273:
						return new Object[] { 3000, "StreamScape points" };
					case 3274:
						return new Object[] { 2000, "StreamScape points" };
					case 3275:
						return new Object[] { 2000, "StreamScape points" };
					case 3276:
						return new Object[] { 6000, "StreamScape points" };
					case 3647:
						return new Object[] { 4000, "StreamScape points" };
					case 3648:
						return new Object[] { 4000, "StreamScape points" };
					case 3649:
						return new Object[] { 4000, "StreamScape points" };
					case 3650:
						return new Object[] { 2250, "StreamScape points" };
					case 3651:
						return new Object[] { 2250, "StreamScape points" };
					case 3652:
						return new Object[] { 5000, "StreamScape points" };
					case 3653:
						return new Object[] { 7500, "StreamScape points" };
					case 895:
						return new Object[] { 50000, "StreamScape points" };
					case 82:
						return new Object[] { 75000, "StreamScape points" };
					case 894:
						return new Object[] { 125000, "StreamScape points" };
					case 3083:
						return new Object[] { 1500, "StreamScape points" };
					case 3287:
						return new Object[] { 25, "StreamScape points" };
					case 3288:
						return new Object[] { 1200, "StreamScape points" };
					case 3291:
						return new Object[] { 1500, "StreamScape points" };
					case 3284:
						return new Object[] { 1750, "StreamScape points" };
					case 3242:
						return new Object[] { 800, "StreamScape points" };
					case 3293:
						return new Object[] { 550, "StreamScape points" };
					case 3292:
						return new Object[] { 500, "StreamScape points" };
					case 3285:
						return new Object[] { 350, "StreamScape points" };
					case 3286:
						return new Object[] { 450, "StreamScape points" };
					case 3640:
						return new Object[] { 300, "StreamScape points" };
					case 3637:
						return new Object[] { 300, "StreamScape points" };
					case 3638:
						return new Object[] { 300, "StreamScape points" };
					case 3639:
						return new Object[] { 300, "StreamScape points" };
					case 3290:
						return new Object[] { 400, "StreamScape points" };
					case 3080:
						return new Object[] { 8000, "StreamScape points" };
					case 8677:
						return new Object[] { 1350, "StreamScape points" };
					case 8675:
						return new Object[] { 3250, "StreamScape points" };
					case 3298:
						return new Object[] { 1850, "StreamScape points" };
					case 667:
						return new Object[] { 200, "StreamScape points" };
					case 989:
						return new Object[] { 100, "StreamScape points" };
					case 13664:
						return new Object[] { 250, "StreamScape points" };
					case 6199:
						return new Object[] { 500, "StreamScape points" };
				}
			} else if (shop == TOKKUL_EXCHANGE_STORE) {
				switch (item) {
					case 11978:
						return new Object[] { 300000, "tokkul" };
					case 438:
					case 436:
						return new Object[] { 10, "tokkul" };
					case 440:
						return new Object[] { 25, "tokkul" };
					case 453:
						return new Object[] { 30, "tokkul" };
					case 442:
						return new Object[] { 30, "tokkul" };
					case 444:
						return new Object[] { 40, "tokkul" };
					case 447:
						return new Object[] { 70, "tokkul" };
					case 449:
						return new Object[] { 120, "tokkul" };
					case 451:
						return new Object[] { 250, "tokkul" };
					case 1623:
						return new Object[] { 20, "tokkul" };
					case 1621:
						return new Object[] { 40, "tokkul" };
					case 1619:
						return new Object[] { 70, "tokkul" };
					case 1617:
						return new Object[] { 150, "tokkul" };
					case 1631:
						return new Object[] { 1600, "tokkul" };
					case 6571:
						return new Object[] { 50000, "tokkul" };
					case 11128:
						return new Object[] { 22000, "tokkul" };
					case 6522:
						return new Object[] { 20, "tokkul" };
					case 6524:
					case 6523:// this the item id
					case 6526:
						return new Object[] { 5000, "tokkul" };// these will be the
					// prices
					case 6528:
					case 6568:
						return new Object[] { 800, "tokkul" };
				}
			} else if (shop == DUNGEONEERING_STORE) {
				switch (item) {
					case 18351:
					case 18349:
					case 18353:
					case 18357:
					case 18355:
					case 18359:
						return new Object[] { 200000, "Dungeoneering tokens" };
					case 18335:
						return new Object[] { 75000, "Dungeoneering tokens" };
				}
			} else if (shop == DONATION_STORE) {
				switch (item) {
					case 20250:
						return new Object[] { 5, "Donation points" };
					case 20251:
					case 20252:
					case 20253:
					case 14556:
					case 14557:
					case 14558:
						return new Object[] { 3, "Donation points" };
					case 20690:
						return new Object[] { 10, "Donation points" };
					case 15505:
						return new Object[] { 15, "Donation points" };
					case 21070:
					case 21071:
					case 21072:
					case 21073:
						return new Object[] { 25, "Donation points" };
					case 14581:
					case 14582:
					case 14583:
					case 14584:
					case 14585:
					case 14586:
						return new Object[] { 8, "Donation points" };
					case 665:
						return new Object[] { 75, "Donation points" };
					case 666:
					case 669:
					case 670:
					case 671:
					case 672:
					case 673:
						return new Object[] { 25, "Donation points" };
					case 4204:
						return new Object[] { 200, "Donation points" };
					case 84:
						return new Object[] { 15, "Donation points" };
					case 3082:
						return new Object[] { 20, "Donation points" };
					case 3135:
						return new Object[] { 5, "Donation points" };
					case 3278:
						return new Object[] { 25, "Donation points" };
					case 3280:
						return new Object[] { 10, "Donation points" };
					case 421:
						return new Object[] { 50, "Donation points" };
					case 295:
						return new Object[] { 25, "Donation points" };
					case 894:
						return new Object[] { 50, "Donation points" };
					case 20692:
						return new Object[] { 1, "Donation points" };
					case 6199:
						return new Object[] { 5, "Donation points" };
					case 15501:
						return new Object[] { 10, "Donation points" };
					case 2380:
					case 3878:
					case 1667:
					case 18003:
					case 18004:
					case 18005:
					case 18006:
					case 18007:
					case 21040:
					case 21041:
					case 21042:
					case 21043:
					case 21044:
					case 21047:
					case 21048:
					case 21049:
					case 21050:
						return new Object[] { 10, "Donation points" };
					case 3869:
					case 21045:
					case 21051:
						return new Object[] { 20, "Donations points" };
					case 18001:
						return new Object[] { 20, "Donations points" };

					case 773:
						return new Object[] { 350, "Donations points" };

					case 18002:
						return new Object[] { 15, "Donations points" };
					case 18011:
						return new Object[] { 50, "Donations points" };
					case 20005:
						return new Object[] { 75, "Donations points" };
					case 85:
						return new Object[] { 25, "Donation points" };
					case 275:
						return new Object[] { 75, "Donation points" };
					case 293:
						return new Object[] { 250, "Donation points" };
				}
			} else if (shop == PRESTIGE_STORE) {
				switch (item) {
					case 11599:
						return new Object[] { 325, "Prestige points" };
					case 11600:
						return new Object[] { 275, "Prestige points" };
					case 11601:
					case 11602:
						return new Object[] { 200, "Prestige points" };
					case 11603:
						return new Object[] { 250, "Prestige points" };
					case 14018:
						return new Object[] { 90, "Prestige points" };
					case 19335:
						return new Object[] { 30, "Prestige points" };
					case 15078:
						return new Object[] { 1250, "Prestige points" };
					case 15220:
					case 15020:
					case 15019:
					case 15018:
						return new Object[] { 20, "Prestige points" };
					case 20000:
					case 20001:
					case 20002:
						return new Object[] { 50, "Prestige points" };
					case 6570:
						return new Object[] { 215, "Prestige points" };
					case 19111:
						return new Object[] { 500, "Prestige points" };
					case 13855:
					case 13848:
					case 13856:
					case 13854:
					case 13853:
					case 138352:
					case 13851:
					case 13850:
					case 13849:
						return new Object[] { 5, "Prestige points" };
					case 10400:
					case 10402:
					case 10416:
					case 10418:
					case 10408:
					case 10410:
					case 10412:
					case 10414:
					case 10404:
					case 10406:
						return new Object[] { 2, "Prestige points" };
					case 14595:
					case 14603:
						return new Object[] { 5, "Prestige points" };
					case 14602:
					case 14605:
						return new Object[] { 3, "Prestige points" };
					case 21636:
						return new Object[] { 250, "Prestige points" };
					case 695:
						return new Object[] { 100, "Prestige points" };
					case 21077:
						return new Object[] { 1000, "Prestige points" };
				}
			} else if (shop == SLAYER_STORE) {
				switch (item) {
					case 13263:
						return new Object[] { 200, "Slayer points" };
					case 11592: // malevolent 2h
					case 11595: // maul
						return new Object[] { 500, "Slayer points" };
					case 11594: // sword
						return new Object[] { 225, "Slayer points" };
					case 11593: // guard
						return new Object[] { 200, "Slayer points" };
					case 11596: // gloves
					case 11597: // boots
						return new Object[] { 150, "Slayer points" };
					case 11598: // wings
						return new Object[] { 250, "Slayer points" };
					case 13281:
						return new Object[] { 5, "Slayer points" };
					case 11604:
						return new Object[] { 300, "Slayer points" };
					case 11605:
						return new Object[] { 350, "Slayer points" };
					case 11606:
					case 11607:
						return new Object[] { 300, "Slayer points" };
					case 11608:
						return new Object[] { 350, "Slayer points" };
					case 84:
						return new Object[] { 1000, "Slayer points" };
					case 4204:
						return new Object[] { 10000, "Slayer points" };
					case 15403:
					case 11730:
					case 10887:
					case 15241:
						return new Object[] { 175, "Slayer points" };
					case 11235:
					case 15486:
						return new Object[] { 150, "Slayer points" };
					case 15243:
						return new Object[] { 2, "Slayer points" };
					case 10551:
						return new Object[] { 125, "Slayer points" };
					case 21082:
					case 21083:
					case 21084:
						return new Object[] { 350, "Slayer points" };
					case 1321:
						return new Object[] { 250, "Slayer points" };
					case 2572:
						return new Object[] { 350, "Slayer points" };
					case 21077:
						return new Object[] { 350, "Slayer points" };
				}
			}
			return null;
		}

		public static Map<Integer, Shop> getShops() {
			return shops;
		}

		public static JsonLoader parseShops() {
			return new JsonLoader() {
				@Override
				public String filePath() {
					return "./data/def/json/world_shops.json";
				}

				@Override
				public void load(JsonObject reader, Gson builder) {
					int id = reader.get("id").getAsInt();
					String name = reader.get("name").getAsString();
					Item[] items = builder.fromJson(reader.get("items").getAsJsonArray(), Item[].class);
					Item currency = new Item(reader.get("currency").getAsInt());
					shops.put(id, new Shop(null, id, name, currency, items));
				}
			};
		}
	}

	/**
	 * The shop interface id.
	 */
	public static final int INTERFACE_ID = 3824;

	/**
	 * The starting interface child id of items.
	 */
	public static final int ITEM_CHILD_ID = 3900;

	/**
	 * The interface child id of the shop's name.
	 */
	public static final int NAME_INTERFACE_CHILD_ID = 3901;

	/**
	 * The inventory interface id, used to set the items right click values to
	 * 'sell'.
	 */
	public static final int INVENTORY_INTERFACE_ID = 3823;

	/*
	 * Declared shops
	 */
	public static final int GENERAL_STORE = 12;

	public static final int RECIPE_FOR_DISASTER_STORE = 36;

	private static final int VOTING_REWARDS_STORE = 27;

	private static final int PKING_REWARDS_STORE = 26;

	private static final int ENERGY_FRAGMENT_STORE = 33;

	private static final int AGILITY_TICKET_STORE = 39;

	private static final int GRAVEYARD_STORE = 42;

	private static final int TOKKUL_EXCHANGE_STORE = 43;

	private static final int SKILLCAPE_STORE_1 = 8;

	private static final int SKILLCAPE_STORE_2 = 9;

	private static final int SKILLCAPE_STORE_3 = 10;

	public static final int StreamScape_POINT_STORE = 29;

	private static final int DUNGEONEERING_STORE = 44;

	private static final int PRESTIGE_STORE = 46;

	private static final int SLAYER_STORE = 47;

	private static final int DONATION_STORE = 48;

	/**
	 * Checks if a player has enough inventory space to buy an item
	 *
	 * @param item
	 *            The item which the player is buying
	 * @return true or false if the player has enough space to buy the item
	 */
	public static boolean hasInventorySpace(Player player, Item item, int currency, int pricePerItem) {
		if (player.getInventory().getFreeSlots() >= 1) {
			return true;
		}
		if (item.getDefinition().isStackable()) {
			if (player.getInventory().contains(item.getId())) {
				return true;
			}
		}
		if (currency != -1) {
			if (player.getInventory().getFreeSlots() == 0
					&& player.getInventory().getAmount(currency) == pricePerItem) {
				return true;
			}
		}
		return false;
	}

	public static boolean shopBuysItem(int shopId, Item item) {
		if (shopId == GENERAL_STORE)
			return true;
		if (shopId == DUNGEONEERING_STORE || shopId == PKING_REWARDS_STORE || shopId == StreamScape_POINT_STORE
				|| shopId == VOTING_REWARDS_STORE || shopId == RECIPE_FOR_DISASTER_STORE
				|| shopId == ENERGY_FRAGMENT_STORE || shopId == AGILITY_TICKET_STORE || shopId == GRAVEYARD_STORE
				|| shopId == TOKKUL_EXCHANGE_STORE || shopId == PRESTIGE_STORE || shopId == SLAYER_STORE
				|| shopId == DONATION_STORE)
			return false;
		Shop shop = ShopManager.getShops().get(shopId);
		if (shop != null && shop.getOriginalStock() != null) {
			for (Item it : shop.getOriginalStock()) {
				if (it != null && it.getId() == item.getId())
					return true;
			}
		}
		return false;
	}

	private final int id;

	private String name;

	private Item currency;

	private Item[] originalStock;

	private boolean restockingItems;

	/*
	 * The shop constructor
	 */
	public Shop(Player player, int id, String name, Item currency, Item[] stockItems) {
		super(player);
		if (stockItems.length > 42)
			throw new ArrayIndexOutOfBoundsException(
					"Stock cannot have more than 40 items; check shop[" + id + "]: stockLength: " + stockItems.length);
		this.id = id;
		this.name = name.length() > 0 ? name : "General Store";
		this.currency = currency;
		this.originalStock = new Item[stockItems.length];
		for (int i = 0; i < stockItems.length; i++) {
			Item item = new Item(stockItems[i].getId(), stockItems[i].getAmount());
			add(item, false);
			this.originalStock[i] = item;
		}
	}

	@Override
	public Shop add(Item item, boolean refresh) {
		super.add(item, false);
		if (id != RECIPE_FOR_DISASTER_STORE)
			publicRefresh();
		return this;
	}

	@Override
	public int capacity() {
		return 42;
	}

	/**
	 * Checks a value of an item in a shop
	 *
	 * @param player
	 *            The player who's checking the item's value
	 * @param slot
	 *            The shop item's slot (in the shop!)
	 * @param sellingItem
	 *            Is the player selling the item?
	 */
	public void checkValue(Player player, int slot, boolean sellingItem) {
		this.setPlayer(player);
		Item shopItem = new Item(getItems()[slot].getId());
		if (!player.isShopping()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Item item = sellingItem ? player.getInventory().getItems()[slot] : getItems()[slot];
		if (item.getId() == 995)
			return;
		if (sellingItem) {
			if (!shopBuysItem(id, item)) {
				player.getPacketSender().sendMessage("You cannot sell this item to this store.");
				return;
			}

		}
		int finalValue = 0;
		String finalString = sellingItem ? "" + ItemDefinition.forId(item.getId()).getName() + ": shop will buy for "
				: "" + ItemDefinition.forId(shopItem.getId()).getName() + " currently costs ";
		if (getCurrency().getId() != -1) {
			finalValue = ItemDefinition.forId(item.getId()).getValue();
			String s = currency.getDefinition().getName().toLowerCase().endsWith("s")
					? currency.getDefinition().getName().toLowerCase()
					: currency.getDefinition().getName().toLowerCase() + "s";
			/** CUSTOM CURRENCY, CUSTOM SHOP VALUES **/
			if (id == TOKKUL_EXCHANGE_STORE || id == ENERGY_FRAGMENT_STORE || id == StreamScape_POINT_STORE
					|| id == AGILITY_TICKET_STORE || id == GRAVEYARD_STORE) {
				Object[] obj = ShopManager.getCustomShopData(id, item.getId());
				if (obj == null)
					return;
				finalValue = (int) obj[0];
				s = (String) obj[1];
			}
			if (sellingItem) {
				if (finalValue != 1) {
					finalValue = (int) (finalValue * 0.85);
				}
			}
			finalString += "" + finalValue + " " + s + "" + shopPriceEx(finalValue) + ".";
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, item.getId());
			if (obj == null)
				return;
			finalValue = (int) obj[0];
			if (sellingItem) {
				if (finalValue != 1) {
					finalValue = (int) (finalValue * 0.85);
				}
			}
			finalString += "" + finalValue + " " + (String) obj[1] + ".";
		}
		if (player != null && finalValue > 0) {
			player.getPacketSender().sendMessage(finalString);
			return;
		}
	}

	public void fireRestockTask() {
		if (isRestockingItems() || fullyRestocked())
			return;
		setRestockingItems(true);
		TaskManager.submit(new ShopRestockTask(this));
	}

	@Override
	public Shop full() {
		getPlayer().getPacketSender().sendMessage("The shop is currently full. Please come back later.");
		return this;
	}

	public boolean fullyRestocked() {
		if (id == GENERAL_STORE) {
			return getValidItems().size() == 0;
		} else if (id == RECIPE_FOR_DISASTER_STORE) {
			return true;
		}
		if (getOriginalStock() != null) {
			for (int shopItemIndex = 0; shopItemIndex < getOriginalStock().length; shopItemIndex++) {
				if (getItems()[shopItemIndex].getAmount() != getOriginalStock()[shopItemIndex].getAmount())
					return false;
			}
		}
		return true;
	}

	public Item getCurrency() {
		return currency;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public Item[] getOriginalStock() {
		return this.originalStock;
	}

	public boolean isRestockingItems() {
		return restockingItems;
	}

	/**
	 * Opens a shop for a player
	 *
	 * @param player
	 *            The player to open the shop for
	 * @return The shop instance
	 */
	public Shop open(Player player) {
		setPlayer(player);
		getPlayer().getPacketSender().sendInterfaceRemoval().sendClientRightClickRemoval();
		getPlayer().setShop(ShopManager.getShops().get(id)).setInterfaceId(INTERFACE_ID).setShopping(true);
		refreshItems();
		if (Misc.getMinutesPlayed(getPlayer()) <= 190)
			getPlayer().getPacketSender()
					.sendMessage("Note: When selling an item to a store, it loses 15% of its original value.");
		return this;
	}

	/**
	 * Refreshes a shop for every player who's viewing it
	 */
	public void publicRefresh() {
		Shop publicShop = ShopManager.getShops().get(id);
		if (publicShop == null)
			return;
		publicShop.setItems(getItems());
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getShop() != null && player.getShop().id == id && player.isShopping())
				player.getShop().setItems(publicShop.getItems());
		}
	}

	@Override
	public Shop refreshItems() {
		if (id == RECIPE_FOR_DISASTER_STORE) {
			RecipeForDisaster.openRFDShop(getPlayer());
			return this;
		}
		for (Player player : World.getPlayers()) {
			if (player == null || !player.isShopping() || player.getShop() == null || player.getShop().id != id)
				continue;
			player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);
			player.getPacketSender().sendItemContainer(ShopManager.getShops().get(id), ITEM_CHILD_ID);
			player.getPacketSender().sendString(NAME_INTERFACE_CHILD_ID, name);
			if (player.getInputHandling() == null || !(player.getInputHandling() instanceof EnterAmountToSellToShop
					|| player.getInputHandling() instanceof EnterAmountToBuyFromShop))
				player.getPacketSender().sendInterfaceSet(INTERFACE_ID, INVENTORY_INTERFACE_ID - 1);
		}
		return this;
	}

	public void sellItem(Player player, int slot, int amountToSell) {
		this.setPlayer(player);
		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		if (id == GENERAL_STORE) {
			if (player.getRights() == PlayerRights.ADMINISTRATOR || player.getRights() == PlayerRights.OWNER) {
				player.getPacketSender().sendMessage("You cannot sell items.");
				return;
			}
		}
		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Item itemToSell = player.getInventory().getItems()[slot];
		if (!itemToSell.sellable()) {
			player.getPacketSender().sendMessage("This item cannot be sold.");
			return;
		}
		if (!shopBuysItem(id, itemToSell)) {
			player.getPacketSender().sendMessage("You cannot sell this item to this store.");
			return;
		}
		if (!player.getInventory().contains(itemToSell.getId()) || itemToSell.getId() == 995)
			return;
		if (this.full(itemToSell.getId()))
			return;
		if (player.getInventory().getAmount(itemToSell.getId()) < amountToSell)
			amountToSell = player.getInventory().getAmount(itemToSell.getId());
		if (amountToSell == 0)
			return;
		/*
		 * if(amountToSell > 300) { String s =
		 * ItemDefinition.forId(itemToSell.getId()).getName().endsWith("s") ?
		 * ItemDefinition.forId(itemToSell.getId()).getName() :
		 * ItemDefinition.forId(itemToSell.getId()).getName() + "s";
		 * player.getPacketSender().sendMessage("You can only sell 300 "
		 * +s+" at a time."); return; }
		 */
		int itemId = itemToSell.getId();
		boolean customShop = this.getCurrency().getId() == -1;
		boolean inventorySpace = customShop ? true : false;
		if (!customShop) {
			if (!itemToSell.getDefinition().isStackable()) {
				if (!player.getInventory().contains(this.getCurrency().getId()))
					inventorySpace = true;
			}
			if (player.getInventory().getFreeSlots() <= 0
					&& player.getInventory().getAmount(this.getCurrency().getId()) > 0)
				inventorySpace = true;
			if (player.getInventory().getFreeSlots() > 0
					|| player.getInventory().getAmount(this.getCurrency().getId()) > 0)
				inventorySpace = true;
		}
		int itemValue = 0;
		if (getCurrency().getId() > 0) {
			itemValue = ItemDefinition.forId(itemToSell.getId()).getValue();
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, itemToSell.getId());
			if (obj == null)
				return;
			itemValue = (int) obj[0];
		}
		if (itemValue <= 0)
			return;
		itemValue = (int) (itemValue * 0.85);
		if (itemValue <= 0) {
			itemValue = 1;
		}
		for (int i = amountToSell; i > 0; i--) {
			itemToSell = new Item(itemId);
			if (this.full(itemToSell.getId()) || !player.getInventory().contains(itemToSell.getId())
					|| !player.isShopping())
				break;
			if (!itemToSell.getDefinition().isStackable()) {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), -1);
					if (!customShop) {
						player.getInventory().add(new Item(getCurrency().getId(), itemValue), false);
					} else {
						// Return points here
					}
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			} else {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), amountToSell);
					if (!customShop) {
						player.getInventory().add(new Item(getCurrency().getId(), itemValue * amountToSell), false);
					} else {
						// Return points here
					}
					break;
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			}
			amountToSell--;
		}
		if (customShop) {
			player.getPointsManager().refreshPanel();
		}
		player.getInventory().refreshItems();
		fireRestockTask();
		refreshItems();
		publicRefresh();
	}

	public Shop setCurrency(Item currency) {
		this.currency = currency;
		return this;
	}

	public Shop setName(String name) {
		this.name = name;
		return this;
	}

	public void setRestockingItems(boolean restockingItems) {
		this.restockingItems = restockingItems;
	}

	public String shopPriceEx(int shopPrice) {
		String ShopAdd = "";
		if (shopPrice >= 1000 && shopPrice < 1000000) {
			ShopAdd = " (" + shopPrice / 1000 + "K)";
		} else if (shopPrice >= 1000000) {
			ShopAdd = " (" + shopPrice / 1000000 + " million)";
		}
		return ShopAdd;
	}

	private boolean shopSellsItem(Item item) {
		return contains(item.getId());
	}

	@Override
	public StackType stackType() {
		return StackType.STACKS;
	}

	/**
	 * Buying an item from a shop
	 */
	@Override
	public Shop switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		final Player player = getPlayer();
		if (player == null)
			return this;
		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return this;
		}
		if (this.id == GENERAL_STORE) {
			if (player.getGameMode() == GameMode.IRONMAN) {
				player.getPacketSender()
						.sendMessage("Ironman-players are not allowed to buy items from the general-store.");
				return this;
			}

		}
		if (!shopSellsItem(item))
			return this;
		if (getItems()[slot].getAmount() <= 1 && id != GENERAL_STORE) {
			player.getPacketSender().sendMessage("The shop has run out of stock for this item.");
			return this;
		}
		if (item.getAmount() > getItems()[slot].getAmount())
			item.setAmount(getItems()[slot].getAmount());
		int amountBuying = item.getAmount();
		if (amountBuying == 0)
			return this;
		if (amountBuying > 5000) {
			player.getPacketSender().sendMessage(
					"You can only buy 5000 " + ItemDefinition.forId(item.getId()).getName() + "s at a time.");
			return this;
		}
		boolean customShop = getCurrency().getId() == -1;
		boolean usePouch = false;
		int playerCurrencyAmount = 0;
		int value = ItemDefinition.forId(item.getId()).getValue();
		String currencyName = "";
		if (getCurrency().getId() != -1) {
			playerCurrencyAmount = player.getInventory().getAmount(currency.getId());
			currencyName = ItemDefinition.forId(currency.getId()).getName().toLowerCase();
			if (currency.getId() == 995) {
				if (player.getMoneyInPouch() >= value) {
					playerCurrencyAmount = player.getMoneyInPouchAsInt();
					if (!(player.getInventory().getFreeSlots() == 0
							&& player.getInventory().getAmount(currency.getId()) == value)) {
						usePouch = true;
					}
				}
			} else {
				/** CUSTOM CURRENCY, CUSTOM SHOP VALUES **/
				if (id == TOKKUL_EXCHANGE_STORE || id == ENERGY_FRAGMENT_STORE || id == StreamScape_POINT_STORE
						|| id == AGILITY_TICKET_STORE || id == GRAVEYARD_STORE) {
					value = (int) ShopManager.getCustomShopData(id, item.getId())[0];
				}
			}
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, item.getId());
			if (obj == null)
				return this;
			value = (int) obj[0];
			currencyName = (String) obj[1];
			if (id == PKING_REWARDS_STORE) {
				playerCurrencyAmount = player.getPointsManager().getPoints("pvp");
			} else if (id == VOTING_REWARDS_STORE) {
				playerCurrencyAmount = player.getPointsManager().getPoints("voting");
			} else if (id == DUNGEONEERING_STORE) {
				playerCurrencyAmount = player.getPointsManager().getPoints("dung");
			} else if (id == StreamScape_POINT_STORE) {
				playerCurrencyAmount = player.getPointsManager().getPoints("StreamScape");
			} else if (id == PRESTIGE_STORE) {
				playerCurrencyAmount = player.getPointsManager().getPoints("prestige");
			} else if (id == SLAYER_STORE) {
				playerCurrencyAmount = player.getPointsManager().getPoints("slayer");
			} else if (id == DONATION_STORE) {
				playerCurrencyAmount = player.getPointsManager().getPoints("donation");
			}
		}
		if (value <= 0) {
			return this;
		}
		if (!hasInventorySpace(player, item, getCurrency().getId(), value)) {
			player.getPacketSender().sendMessage("You do not have any free inventory slots.");
			return this;
		}
		if (playerCurrencyAmount <= 0 || playerCurrencyAmount < value) {
			player.getPacketSender().sendMessage("You do not have enough "
					+ (currencyName.endsWith("s") ? currencyName : currencyName + "s") + " to purchase this item.");
			return this;
		}
		if (id == SKILLCAPE_STORE_1 || id == SKILLCAPE_STORE_2 || id == SKILLCAPE_STORE_3) {
			for (int i = 0; i < item.getDefinition().getRequirement().length; i++) {
				int req = item.getDefinition().getRequirement()[i];
				if ((i == 3 || i == 5) && req == 99)
					req *= 10;
				if (req > player.getSkillManager().getMaxLevel(i)) {
					player.getPacketSender().sendMessage("You need to have at least level 99 in "
							+ Misc.formatText(Skill.forId(i).toString().toLowerCase()) + " to buy this item.");
					return this;
				}
			}
		}

		if (item.getDefinition().isStackable()) {

			if (playerCurrencyAmount >= value && hasInventorySpace(player, item, getCurrency().getId(), value)) {

				int canBeBought = playerCurrencyAmount / value;

				if (canBeBought >= amountBuying) {
					canBeBought = amountBuying;
				}

				long invAmount = player.getInventory().getAmount(item.getId());

				if (invAmount + canBeBought > Integer.MAX_VALUE) {
					canBeBought = (int) (Integer.MAX_VALUE - invAmount);
				}

				if (!customShop) {

					if (usePouch) {
						player.setMoneyInPouch(player.getMoneyInPouch() - value * canBeBought);
					} else {
						player.getInventory().delete(currency.getId(), value * canBeBought, false);
					}

				} else {

					if (id == PKING_REWARDS_STORE) {
						player.getPointsManager().decreasePoints("pvp", value * canBeBought);
					} else if (id == VOTING_REWARDS_STORE) {
						player.getPointsManager().decreasePoints("voting", value * canBeBought);
					} else if (id == DUNGEONEERING_STORE) {
						player.getPointsManager().decreasePoints("dung", value * canBeBought);
					} else if (id == StreamScape_POINT_STORE) {
						player.getPointsManager().decreasePoints("StreamScape", value * canBeBought);
					} else if (id == PRESTIGE_STORE) {
						player.getPointsManager().decreasePoints("prestige", value * canBeBought);
					} else if (id == SLAYER_STORE) {
						player.getPointsManager().decreasePoints("slayer", value * canBeBought);
					} else if (id == DONATION_STORE) {
						player.getPointsManager().decreasePoints("donation", value * canBeBought);
					}

				}

				super.switchItem(to, new Item(item.getId(), canBeBought), slot, false, false);
				playerCurrencyAmount -= value;

			}

		} else {

			for (int i = amountBuying; i > 0; i--) {

				if (!shopSellsItem(item)) {
					break;
				}

				if (getItems()[slot].getAmount() <= 1 && id != GENERAL_STORE) {
					player.getPacketSender().sendMessage("The shop has run out of stock for this item.");
					break;
				}

				if (!item.getDefinition().isStackable()) {
					if (playerCurrencyAmount >= value
							&& hasInventorySpace(player, item, getCurrency().getId(), value)) {

						if (!customShop) {
							if (usePouch) {
								player.setMoneyInPouch(player.getMoneyInPouch() - value);
							} else {
								player.getInventory().delete(currency.getId(), value, false);
							}
						} else {
							if (id == PKING_REWARDS_STORE) {
								player.getPointsManager().decreasePoints("pvp", value);
							} else if (id == VOTING_REWARDS_STORE) {
								player.getPointsManager().decreasePoints("voting", value);
							} else if (id == DUNGEONEERING_STORE) {
								player.getPointsManager().decreasePoints("dung", value);
							} else if (id == StreamScape_POINT_STORE) {
								player.getPointsManager().decreasePoints("StreamScape", value);
							} else if (id == PRESTIGE_STORE) {
								player.getPointsManager().decreasePoints("prestige", value);
							} else if (id == SLAYER_STORE) {
								player.getPointsManager().decreasePoints("slayer", value);
							} else if (id == DONATION_STORE) {
								player.getPointsManager().decreasePoints("donation", value);
							}
						}

						super.switchItem(to, new Item(item.getId(), 1), slot, false, false);

						playerCurrencyAmount -= value;
					} else {
						break;
					}
				}
				amountBuying--;

			}

		}

		if (!customShop) {
			if (usePouch) {
				player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch()); // Update
				// the
				// money
				// pouch
			}
		} else {
			player.getPointsManager().refreshPanel();
		}

		player.getInventory().refreshItems();
		fireRestockTask();
		refreshItems();
		publicRefresh();
		return this;
	}
}
