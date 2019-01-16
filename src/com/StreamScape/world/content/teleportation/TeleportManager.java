package com.StreamScape.world.content.teleportation;

import com.StreamScape.GameSettings;
import com.StreamScape.model.PlayerRanks.DonorRights;
import com.StreamScape.model.Position;
import com.StreamScape.model.container.impl.Equipment;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.content.ColourSet;
import com.StreamScape.world.content.dialogue.DialogueManager;
import com.StreamScape.world.entity.impl.player.Player;

/**
 * Handles teleporting
 *
 * @author 2012
 *
 */
public class TeleportManager {

	/**
	 * The favourite teleports
	 */
	private boolean[] favourites = new boolean[25];

	/**
	 * Represents the teleporting type
	 *
	 * @author 2012
	 *
	 */
	public enum TeleportType {
		/**
		 * The mobs
		 */
		MELEE(-15645, 49100),
		/**
		 * The bosses
		 */
		MAGE(-15644, 49200),
		/**
		 * The minigames
		 */
		RANGE(-15643, 49300),
		/**
		 * The pvp zones
		 */
		BOSSES(-15642, 49400),
		/**
		 * The donor zones
		 */
		MINIGAME(-15641, 49500),
		/**
		 * Other zones
		 */
		DONATOR(-15640, 49600),
		/**
		 * Other zones
		 */
		OTHERZONES(-15639, 49700);

		/**
		 * The button id
		 */
		private int button;

		/**
		 * The interface id
		 */
		private int interfaceId;

		/**
		 * Creates a new teleporting type
		 *
		 * @param button
		 *            the button
		 * @param interfaceId
		 *            the interface id
		 */
		TeleportType(int button, int interfaceId) {
			this.setButton(button);
			this.setInterfaceId(interfaceId);
		}

		/**
		 * Gets the button
		 *
		 * @return the button
		 */
		public int getButton() {
			return button;
		}

		/**
		 * Sets the button
		 *
		 * @param button
		 *            the button to set
		 */
		public void setButton(int button) {
			this.button = button;
		}

		/**
		 * Gets the interface
		 *
		 * @return the interfaceId
		 */
		public int getInterfaceId() {
			return interfaceId;
		}

		/**
		 * Sets the interface
		 *
		 * @param interfaceId
		 *            the interfaceId to set
		 */
		public void setInterfaceId(int interfaceId) {
			this.interfaceId = interfaceId;
		}
	}

	/**
	 * The teleport
	 */
	public enum Teleport {

		KEY_ZONE(-16432, -13929, new Position(2134, 4926,0)),

		PURGATORY(-16428, -13925, new Position(1748, 5325, 0)),

		//POINT_ZONE(-16424, -13921, new Position(2387, 4961, 0)),
		DRYGORE_GODS(-16424, -13921, new Position(3232, 2903, 0)),

		DAGGANOTH_SLAYER(-16420, -13917, new Position(2505, 4724, 0)),

		ANOUKE(-16416, -13913, new Position(2757, 4729, 0)),

		GOLDEN_TORMENTED_DEMON(-16412, -13909, new Position(2835, 3259, 0)),

		//CASH_TORVA_ZONE(-16408, -13905, new Position(3056, 4988, 1)),

		//SILVER_TORVA_ZONE(-16404, -13901, new Position(2911, 3612, 0)),

		//-----------------------END MELEE ----------------------------
		//-----------------------START MAGE ----------------------------

		DOOMCORE(-16332, -13829, new Position(2926, 3481, 0)),
		FIRE_DRACONIAN_MAGE(-16328, -13825, new Position(2409, 5086, 0)),
		ICE_DRACONIAN_MAGE(-16324, -13821, new Position(3056, 9562, 0)),
		TOXIC_DRYCONIAN_MAGE(-16320, -13817, new Position(2466, 9715, 0)),

		//-----------------------END MAGE ----------------------------
		//-----------------------START RANGE ----------------------------

		FIRE_DRACONIAN_RANGER(-16232, -13729, new Position(2492, 5156, 0)),
		ICE_DRACONIAN_RANGER(-16228, -13725, new Position(2837, 3806, 0)),
		TOXIC_DRYCONIAN_RANGER(-16224, -13721, new Position(2463, 4781, 0)),

		//-----------------------END RANGE ----------------------------
		//-----------------------START BOSSES ----------------------------

		SHADOW_CORPOREAL_BEAST(-16132, -13629, new Position(2832, 2829, 0)),
		ICHIGO(-16128, -13625, new Position(2023, 4660, 0)),

		//-----------------------END BOSSES ----------------------------
		//-----------------------START MINIGAME ----------------------------
		HUNGER(-16032, -13529, new Position(2524, 4776, 0)),
		GODS_RAID(-16028, -13525, new Position(3444, 2853, 0)),
		//DUEL(-16024, -13521, new Position(3364, 3267, 0)),
		//GODS_RAID(-16020, -13517, new Position(3444, 2853, 1)),

		//-----------------------END MINIGAME ----------------------------//
		//-----------------------START Donator ----------------------------//
		DONOR(-15932, -13429, new Position(2022, 4755, 0)),
		DELUXE_DONOR(-15928, -13425, new Position(2319, 9624, 0)),
		SPONSOR(-15924, -13421, new Position(2713, 9564, 0)),

		//-----------------------END DONATOR ----------------------------
		//-----------------------START OTHER ----------------------------

		GAMBLE(-15832, -13329, new Position(3046, 3371, 0)),
		DUEL(-15828, -13325, new Position(3364, 3267, 0));


		//BLOODSHOT_T	ORVA_BOSS(-16324, -13821, new Position(3236, 3941, 0)),

		//DUEL_ARENA(-16232, -13729, new Position(3364, 3267, 0)),

		//HUNGER_GAMES(-16228, -13725, new Position(0, 0, 0)),

		//ZOMBIES(-16224, -13721, new Position(3504, 3565, 0)),

		//DONOR(-16032, -13529, new Position(2022, 4755, 0)),

		//DELUXE_DONOR(-16028, -13525, new Position(2319, 9624, 0)),

		//SPONSOR(-16024, -13521, new Position(2713, 9564, 0)),

		//GAMBLING_ZONE(-15932, -13429, new Position(3046, 3371, 0)),;

		//NOMAD(-15928, -13425, new Position(1891, 3177, 0)),

		//RECIPE_FOR_DISASTER(-15924, -13421, new Position(3017, 2827, 0)),;

		/**
		 * The button
		 */
		private int button;

		/**
		 * The favourite id
		 */
		private int favouriteId;

		/**
		 * The position
		 */
		private Position position;

		/**
		 * Creates a new teleport
		 *
		 * @param button
		 *            the button
		 * @param favouriteId
		 *            the favourite id
		 * @param position
		 *            the position
		 */
		Teleport(int button, int favouriteId, Position position) {
			this.setButton(button);
			this.setFavouriteId(favouriteId);
			this.setPosition(position);
		}

		/**
		 * Gets the button
		 *
		 * @return the button
		 */
		public int getButton() {
			return button;
		}

		/**
		 * Sets the button
		 *
		 * @param button
		 *            the button to set
		 */
		public void setButton(int button) {
			this.button = button;
		}

		/**
		 * Gets the id
		 *
		 * @return the favouriteId
		 */
		public int getFavouriteId() {
			return favouriteId;
		}

		/**
		 * Sets the id
		 *
		 * @param favouriteId
		 *            the favouriteId to set
		 */
		public void setFavouriteId(int favouriteId) {
			this.favouriteId = favouriteId;
		}

		/**
		 * Gets the position
		 *
		 * @return the position
		 */
		public Position getPosition() {
			return position;
		}

		/**
		 * Sets the position
		 *
		 * @param position
		 *            the position to set
		 */
		public void setPosition(Position position) {
			this.position = position;
		}
	}


	/**
	 * Handles button interaction
	 *
	 * @param player
	 *            the player
	 * @param button
	 *            the button
	 * @return the interaction
	 */
	public static boolean handleButtonInteraction(final Player player, final int button) {
		for (TeleportType type : TeleportType.values()) {
			if (type.getButton() == button) {
				player.getPacketSender().sendInterface(type.getInterfaceId());
				return true;
			}
		}
		for (Teleport teleport : Teleport.values()) {
			if (teleport.getButton() == button) {
				switch (teleport) {
					case GODS_RAID:
						World.minigameHandler.getMinigame(0).addPlayer(player);
						break;
					case DONOR:
						if (player.getDonor().ordinal() >= DonorRights.DONOR.ordinal())
							break;
						else {
							player.sendMessage("@red@You must be at least a donor to teleport here.");
							return false;
						}
					case DELUXE_DONOR:
						if (player.getDonor().ordinal() >= DonorRights.DELUXE_DONOR.ordinal())
							break;
						else {
							player.sendMessage("@red@You must be at least a deluxe donor to teleport here.");
							return false;
						}
					case SPONSOR:
						if (player.getDonor().ordinal() >= DonorRights.SPONSOR.ordinal())
							break;
						else {
							player.sendMessage("@red@You must be a sponsor to teleport here.");
							return false;
						}
					case SHADOW_CORPOREAL_BEAST:
						DialogueManager.start(player, 1500);
						player.setDialogueActionId(1501);
						return false;

					case ICHIGO:
						DialogueManager.start(player, 1550);
						player.setDialogueActionId(1551);
						return false;

					default:
						break;
				}
				player.getPA().setScrollBar(33000, 600);
				if (teleport.getPosition().equals(new Position(0, 0, 0))) {
					player.getPacketSender().sendMessage("This teleport is not avaliable.");
					return true;
				}
				TeleportHandler.teleportPlayer(player, teleport.getPosition(), player.getSpellbook().getTeleportType());
				player.getPacketSender().sendMessage(
						"You teleport to " + Misc.ucFirst(teleport.name().toLowerCase().replaceAll("_", " ")));
				return true;
			}
			if (teleport.getFavouriteId() == button) {
				int slot = teleport.ordinal();

				int config = slot;
				if (slot > 7) {
					config += 3;
				}
				if (player.getTeleport().getFavourites()[slot]) {
					player.getPacketSender().sendConfig(1200 + config, 0);
					player.getTeleport().getFavourites()[slot] = false;
				} else {
					if (getTotalFavourites(player) == 12) {
						player.getPacketSender().sendConfig(1200 + config, 0);
						player.getTeleport().getFavourites()[slot] = false;
						player.getPacketSender().sendMessage("Your favorites category is full.");
						return true;
					}
					player.getPacketSender().sendConfig(1200 + config, 1);
					player.getTeleport().getFavourites()[slot] = true;
				}
				return true;
			}
		}
		if (button == -14822) {
			player.getTeleport().setFavourites(new boolean[25]);
		}
		return false;
	}

	/**
	 * Gets the total favourites
	 *
	 * @param player
	 *            the player
	 * @return the total
	 */
	private static int getTotalFavourites(Player player) {
		int total = 0;
		for (boolean f : player.getTeleport().getFavourites()) {
			if (f) {
				total++;
			}
		}
		return total;
	}

	public boolean[] getFavourites() {
		return favourites;
	}

	public void setFavourites(boolean[] favourites) {
		this.favourites = favourites;
	}
}
