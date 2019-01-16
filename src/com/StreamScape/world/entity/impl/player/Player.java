
package com.StreamScape.world.entity.impl.player;

import com.StreamScape.GameSettings;
import com.StreamScape.drops.DropLog.DropLogEntry;
import com.StreamScape.engine.task.Task;
import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.engine.task.impl.PlayerDeathTask;
import com.StreamScape.engine.task.impl.WalkToTask;
import com.StreamScape.model.*;
import com.StreamScape.model.PlayerRanks.DonorRights;
import com.StreamScape.model.PlayerRanks.PlayerRights;
import com.StreamScape.model.container.impl.*;
import com.StreamScape.model.container.impl.Bank.BankSearchAttributes;
import com.StreamScape.model.definitions.WeaponAnimations;
import com.StreamScape.model.definitions.WeaponInterfaces;
import com.StreamScape.model.definitions.WeaponInterfaces.WeaponInterface;
import com.StreamScape.model.input.Input;
import com.StreamScape.net.PlayerSession;
import com.StreamScape.net.packet.PacketSender;
import com.StreamScape.util.FrameUpdater;
import com.StreamScape.util.Misc;
import com.StreamScape.util.Stopwatch;
import com.StreamScape.world.World;
import com.StreamScape.world.content.Achievements.AchievementAttributes;
import com.StreamScape.world.content.BankPin.BankPinAttributes;
import com.StreamScape.world.content.*;
import com.StreamScape.world.content.KillsTracker.KillsEntry;
import com.StreamScape.world.content.LoyaltyProgram.LoyaltyTitles;
import com.StreamScape.world.content.StartScreen.GameModes;
import com.StreamScape.world.content.clan.ClanChat;
import com.StreamScape.world.content.combat.CombatFactory;
import com.StreamScape.world.content.combat.CombatType;
import com.StreamScape.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.StreamScape.world.content.combat.magic.CombatSpell;
import com.StreamScape.world.content.combat.magic.CombatSpells;
import com.StreamScape.world.content.combat.magic.CustomMagicStaff;
import com.StreamScape.world.content.combat.prayer.CurseHandler;
import com.StreamScape.world.content.combat.prayer.PrayerHandler;
import com.StreamScape.world.content.combat.pvp.PlayerKillingAttributes;
import com.StreamScape.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.StreamScape.world.content.combat.strategy.CombatStrategies;
import com.StreamScape.world.content.combat.strategy.CombatStrategy;
import com.StreamScape.world.content.combat.weapon.CombatSpecial;
import com.StreamScape.world.content.combat.weapon.FightType;
import com.StreamScape.world.content.dialogue.Dialogue;
import com.StreamScape.world.content.gambling.GamblingManager;
import com.StreamScape.world.content.minigames.MinigameAttributes;
import com.StreamScape.world.content.minigames.impl.Dueling;
import com.StreamScape.world.content.skill.SkillManager;
import com.StreamScape.world.content.skill.impl.construction.HouseFurniture;
import com.StreamScape.world.content.skill.impl.construction.Portal;
import com.StreamScape.world.content.skill.impl.construction.Room;
import com.StreamScape.world.content.skill.impl.farming.Farming;
import com.StreamScape.world.content.skill.impl.slayer.Slayer;
import com.StreamScape.world.content.skill.impl.summoning.Pouch;
import com.StreamScape.world.content.skill.impl.summoning.Summoning;
import com.StreamScape.world.content.teleportation.TeleportManager;
import com.StreamScape.world.content.teleportation.TeleportManager.Teleport;
import com.StreamScape.world.entity.impl.Character;
import com.StreamScape.world.entity.impl.npc.NPC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player extends Character {
	public boolean spellBookSwap;
	public int changinPin;
	private final PointsManager pointsManager = new PointsManager(this);
	public ColourGroup yellColours = new ColourGroup();
	public ColourGroup capeColours = new ColourGroup();

	public String ipToUnban = "";
	public String macToUnban = "";
	public String uuidToUnban = "";

	public String yellTitle = "";
	public boolean crushVial = false;

	public boolean isCrushVial() {
		return crushVial;
	}

	public void setCrushVial(boolean crushVial) {
		this.crushVial = crushVial;
	}

	private TeleportManager teleport = new TeleportManager();

	private int[] maxCapeColors = { 65214, 65200, 65186, 62995 };

	private int[] compCapeColors = { 65214, 65200, 65186, 62995 };

	private int currentCape;

	private boolean modeler;
	private boolean gambler;
	private boolean gfxDesigner;
	private boolean youtuber;
	private ForceMovement forceMovement;

	public ForceMovement getForceMovement() {
		return forceMovement;
	}

	public Player setForceMovement(ForceMovement forceMovement) {
		this.forceMovement = forceMovement;

		if (forceMovement != null) {
			getUpdateFlag().flag(Flag.FORCED_MOVEMENT);
		}

		return this;
	}

	/**
	 * The gambling manager
	 */
	private final GamblingManager gamblingManager = new GamblingManager();
	/**
	 * The gambling offer container
	 */
	private final GambleOfferItemContainer gambleOffer = new GambleOfferItemContainer(this);

	private final FightManager fightManager = new FightManager();

	private Map<Integer, Integer> dropKillCount = new HashMap<>();

	public Map<Integer, Integer> getDropKillCount() {
		return dropKillCount;
	}

	public Player setDropKillCount(Map<Integer, Integer> dropKillCount) {
		this.dropKillCount = dropKillCount;
		return this;
	}

	public void increaseDKC(int npcId) {
		dropKillCount.put(npcId, dropKillCount.getOrDefault(npcId, 0) + 1);
	}

	public int lastClickedTab = 0;

	public int destination = 0;

	public int timerTick;

	/*** STRINGS ***/
	private String username;

	private String password;
	private String serial_number;
	private String mac;

	private String emailAddress;

	private String hostAddress;

	private String clanChatName;

	private String last_ip_address;

	private String last_mac_address;

	private String last_computer_address;

	private int extraDropRate = 0;

	/*** LONGS **/
	private Long longUsername;

	private long moneyInPouch;

	private long totalPlayTime;

	private long last_login = -1;

	private long last_serial_address;

	// Timers (Stopwatches)
	public long lastHelpRequest;

	private final Stopwatch sqlTimer = new Stopwatch();

	private final Stopwatch foodTimer = new Stopwatch();

	private final Stopwatch potionTimer = new Stopwatch();

	private final Stopwatch lastRunRecovery = new Stopwatch();

	private final Stopwatch clickDelay = new Stopwatch();

	private final Stopwatch lastItemPickup = new Stopwatch();

	private final Stopwatch lastYell = new Stopwatch();

	private final Stopwatch lastVengeance = new Stopwatch();

	private final Stopwatch emoteDelay = new Stopwatch();

	private final Stopwatch specialRestoreTimer = new Stopwatch();

	/*
	 * Fields
	 */

	private final Stopwatch lastSummon = new Stopwatch();
	private final Stopwatch recordedLogin = new Stopwatch();
	// private final Stopwatch creationDate = new Stopwatch();
	private final Stopwatch tolerance = new Stopwatch();
	private final Stopwatch lougoutTimer = new Stopwatch();
	/*** INSTANCES ***/
	private final CopyOnWriteArrayList<KillsEntry> killsTracker = new CopyOnWriteArrayList<KillsEntry>();
	private final CopyOnWriteArrayList<DropLogEntry> dropLog = new CopyOnWriteArrayList<DropLogEntry>();

	private ArrayList<HouseFurniture> houseFurniture = new ArrayList<HouseFurniture>();
	private ArrayList<Portal> housePortals = new ArrayList<>();
	private final List<Player> localPlayers = new LinkedList<Player>();
	private final List<NPC> localNpcs = new LinkedList<NPC>();
	private PlayerSession session;
	private final PlayerProcess process = new PlayerProcess(this);
	private final QuestJournal questJournal = new QuestJournal();
	private final PlayerKillingAttributes playerKillingAttributes = new PlayerKillingAttributes(this);
	private final MinigameAttributes minigameAttributes = new MinigameAttributes();
	private final BankPinAttributes bankPinAttributes = new BankPinAttributes();
	private final BankSearchAttributes bankSearchAttributes = new BankSearchAttributes();
	private final AchievementAttributes achievementAttributes = new AchievementAttributes();
	private CharacterAnimations characterAnimations = new CharacterAnimations();
	private final BonusManager bonusManager = new BonusManager();
	private final PacketSender packetSender = new PacketSender(this);
	private final Appearance appearance = new Appearance(this);
	private final FrameUpdater frameUpdater = new FrameUpdater();

	private PlayerRights rights = PlayerRights.PLAYER;
	private DonorRights donor = DonorRights.NONE;

	private SkillManager skillManager = new SkillManager(this);
	private PlayerRelations relations = new PlayerRelations(this);
	private ChatMessage chatMessages = new ChatMessage();
	private Inventory inventory = new Inventory(this);
	private Equipment equipment = new Equipment(this);

	private PriceChecker priceChecker = new PriceChecker(this);
	private Trading trading = new Trading(this);
	private Dueling dueling = new Dueling(this);
	private Slayer slayer = new Slayer(this);
	private Farming farming = new Farming(this);
	private Summoning summoning = new Summoning(this);
	private Bank[] bankTabs = new Bank[9];
	private Room[][][] houseRooms = new Room[5][13][13];
	private PlayerInteractingOption playerInteractingOption = PlayerInteractingOption.NONE;
	private GameMode gameMode = GameMode.NORMAL;
	private CombatType lastCombatType = CombatType.MELEE;
	private FightType fightType = FightType.UNARMED_PUNCH;
	private Prayerbook prayerbook = Prayerbook.NORMAL;
	private MagicSpellbook spellbook = MagicSpellbook.NORMAL;
	private LoyaltyTitles loyaltyTitle = LoyaltyTitles.NONE;
	private ClanChat currentClanChat;
	private Input inputHandling;
	private WalkToTask walkToTask;
	private Shop shop;
	private GameObject interactingObject;
	private Item interactingItem;
	private Dialogue dialogue;
	private DwarfCannon cannon;
	private CombatSpell autocastSpell, castSpell, previousCastSpell;
	private RangedWeaponData rangedWeaponData;
	private CombatSpecial combatSpecial;
	private WeaponInterface weapon;
	private Item untradeableDropItem;
	private Object[] usableObject;
	private Task currentTask;
	private Position resetPosition;
	private Pouch selectedPouch;
	/*** INTS ***/
	private int[] brawlerCharges = new int[9];
	private int[] leechedBonuses = new int[7];
	private int[] ores = new int[2];
	private int[] constructionCoords;
	private int recoilCharges;
	private int runEnergy = 100;
	private int currentBankTab;
	private int interfaceId, walkableInterfaceId, multiIcon;
	private int dialogueActionId;
	private Teleport teleportType;
	private int overloadPotionTimer, prayerRenewalPotionTimer;
	private int fireImmunity, fireDamageModifier;
	private int amountDonated;
	private int wildernessLevel;
	private int fireAmmo;
	private int specialPercentage = 100;
	private int skullIcon = -1, skullTimer;
	private int teleblockTimer;
	private int dragonFireImmunity;
	private int poisonImmunity;

	private int shadowState;
	private int effigy;
	private int dfsCharges;
	private int playerViewingIndex;
	private int staffOfLightEffect;
	private int minutesBonusExp = -1;
	private int selectedGeSlot = -1;
	private int selectedGeItem = -1;
	private int selectedSkillingItem;
	private Book activeBook;
	private int storedRuneEssence, storedPureEssence;
	private int trapsLaid;
	private int skillAnimation;
	private int houseServant;
	private int houseServantCharges;
	private int servantItemFetch;
	private int portalSelected;
	private int constructionInterface;
	private int buildFurnitureId;
	private int buildFurnitureX;
	private int buildFurnitureY;
	private int combatRingType;
	private int questPoints;

	/*** BOOLEANS ***/
	private boolean unlockedLoyaltyTitles[] = new boolean[12];
	private boolean[] crossedObstacles = new boolean[7];
	private PlayerDropLog playerDropLog = new PlayerDropLog();
	public Player dropLogPlayer;
	public boolean dropLogOrder;
	private boolean processFarming;
	private boolean crossingObstacle;
	private boolean targeted;
	private boolean isBanking, noteWithdrawal, swapMode;
	private boolean regionChange, allowRegionChangePacket;
	private boolean isDying;
	private boolean isRunning = true, isResting;
	private boolean experienceLocked;
	private boolean clientExitTaskActive;
	private boolean drainingPrayer;
	private boolean shopping;
	private boolean settingUpCannon;
	private boolean hasVengeance;
	private boolean killsTrackerOpen;
	private boolean acceptingAid;
	private boolean autoRetaliate;
	private boolean autocast;
	private boolean specialActivated;

	private boolean isCoughing;
	private boolean playerLocked;
	private boolean recoveringSpecialAttack;
	private boolean soundsActive, musicActive;
	private boolean newPlayer;
	private boolean openBank;
	private boolean inActive;
	private boolean inConstructionDungeon;
	private boolean isBuildingMode;
	private boolean voteMessageSent;
	private boolean receivedStarter;

	/**
	 * Player Punishment getters & setters
	 */
	public void setLastLogin(long last_login) {
		this.last_login = last_login;
	}

	public void setLastIpAddress(String last_ip) {
		last_ip_address = last_ip;
	}

	public void setLastSerialAddress(long last_serial) {
		last_serial_address = last_serial;
	}

	public void setLastMacAddress(String mac) {
		last_mac_address = mac;
	}

	public void setLastComputerAddress(String add) {
		last_computer_address = add;
	}

	public long getLastLogin() {
		return last_login;
	}

	public String getLastIpAddress() {
		return last_ip_address;
	}

	public long getLastSerialAddress() {
		return last_serial_address;
	}

	public String getLastMacAddress() {
		return last_mac_address;
	}

	public String getLastComputerAddress() {
		return last_computer_address;
	}
	public int morpted = 0;

	public int getExtraDropRate() { return this.extraDropRate; }
	public void setExtraDropRate(int extraDr) { this.extraDropRate = extraDr; }
	public int getMorpted() {
		return morpted;
	}
	public void setMorpted(int morp) {
		this.morpted = morp;
	}
	/**
	 * The current combination keys.
	 */
	private final int[] combinationKeys = { 0, 0, 0 };
	private final int[] resetCombinationKeys = { 0, 0, 0 };
	public GameModes selectedGameMode;

	public int npcId2;

	public int playerStandIndex;

	public int playerTurnIndex;

	public boolean isNpc;

	public int playerWalkIndex;

	public int playerTurn180Index;

	public int playerTurn90CWIndex;

	public int playerTurn90CCWIndex;

	public int playerRunIndex;

	public boolean updateRequired;

	public boolean appearanceUpdateRequired;

	public Player(PlayerSession playerIO) {
		super(GameSettings.DEFAULT_POSITION.copy());
		this.session = playerIO;
	}

	@Override
	public void appendDeath() {
		if (!isDying) {
			isDying = true;
			if (getFightManager().isFighting())
				TaskManager.submit(new PlayerDeathTask(this, false));
			else TaskManager.submit(new PlayerDeathTask(this));
		}
	}

	public boolean busy() {
		return interfaceId > 0 || isBanking || shopping || trading.inTrade() || dueling.inDuelScreen || isResting;
	}

	public void decrementDragonFireImmunity(int amount) {
		dragonFireImmunity -= amount;
	}

	public void decrementPoisonImmunity(int amount) {
		poisonImmunity -= amount;
	}

	public void decrementSkullTimer() {
		skullTimer -= 50;
	}

	public void decrementSpecialPercentage(int drainAmount) {
		this.specialPercentage -= drainAmount;

		if (specialPercentage < 0) {
			specialPercentage = 0;
		}
	}

	public void decrementStaffOfLightEffect() {
		this.staffOfLightEffect--;
	}

	public void decrementTeleblockTimer() {
		teleblockTimer--;
	}

	@Override
	public CombatStrategy determineStrategy() {
		if (specialActivated && castSpell == null) {
			if (combatSpecial.getCombatType() == CombatType.MELEE) {
				return CombatStrategies.getDefaultMeleeStrategy();
			} else if (combatSpecial.getCombatType() == CombatType.RANGED) {
				setRangedWeaponData(RangedWeaponData.getData(this));
				return CombatStrategies.getDefaultRangedStrategy();
			} else if (combatSpecial.getCombatType() == CombatType.MAGIC) {
				return CombatStrategies.getDefaultMagicStrategy();
			}
		}

		if (CustomMagicStaff.checkCustomStaff(this)) {
			CustomMagicStaff.handleCustomStaff(this);
			this.setCastSpell(CustomMagicStaff.CustomStaff.getSpellForWeapon(this.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()));
			return CombatStrategies.getDefaultMagicStrategy();
		}

		if (castSpell != null || autocastSpell != null) {
			return CombatStrategies.getDefaultMagicStrategy();
		}

		RangedWeaponData data = RangedWeaponData.getData(this);
		if (data != null) {
			setRangedWeaponData(data);
			return CombatStrategies.getDefaultRangedStrategy();
		}

		return CombatStrategies.getDefaultMeleeStrategy();
	}

	public boolean didReceiveStarter() {
		return receivedStarter;
	}

	public void dispose() {
		save();
		packetSender.sendLogout();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Player)) {
			return false;
		}

		Player p = (Player) o;
		return p.getIndex() == getIndex() || p.getUsername().equals(username);
	}

	public boolean experienceLocked() {
		return experienceLocked;
	}

	public AchievementAttributes getAchievementAttributes() {
		return achievementAttributes;
	}

	public int getAmountDonated() {
		return amountDonated;
	}

	public Appearance getAppearance() {
		return appearance;
	}

	/*
	 * Getters & Setters
	 */

	@Override
	public int getAttackSpeed() {
		int speed = weapon.getSpeed();
		String weapon = equipment.get(Equipment.WEAPON_SLOT).getDefinition().getName();
		if (getCurrentlyCasting() != null) {
			if (getCurrentlyCasting() == CombatSpells.BLOOD_BLITZ.getSpell()
					|| getCurrentlyCasting() == CombatSpells.SHADOW_BLITZ.getSpell()
					|| getCurrentlyCasting() == CombatSpells.SMOKE_BLITZ.getSpell()
					|| getCurrentlyCasting() == CombatSpells.ICE_BLITZ.getSpell()) {
				return 5;
			} else {
				return 6;
			}
		}
		int weaponId = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId == 1419) {
			speed -= 2;
			/*
			 * start of custom weapon speeds
			 *
			 * very slow speed
			 */
		}
		if (weaponId == 3277 || weaponId == 4204 || weaponId == 3278 || weaponId == 3242) {
			speed = 7;
			/*
			 * slow weapon speed
			 */
		}
		if(weaponId == 3289) {
			speed = 7;
		}
		if (weaponId == 3092 || weaponId == 3135 || weaponId == 3081 || weaponId == 3082) {
			speed = 4;
			/*
			 * medium weapon speed
			 */
		}
		if (weaponId == 3660 || weaponId == 21005 || weaponId == 21025 || weaponId == 21045 || weaponId == 20084) {
			speed = 4;
		}
		if (weaponId == 895 || weaponId == 3279 || weaponId == 3280 || weaponId == 3653 || weaponId == 19005) {
			speed = 3;
			/*
			 * fast weapon speed
			 */
		}
		if (weaponId == 82 || weaponId == 13047 || weaponId == 21080) {
			speed = 2;
			/*
			 * very fast weapon speed
			 */
		}
		if (weaponId == 894 || weaponId == 18001 || weaponId == 18011 || weaponId == 20005 || weaponId == 21053) {
			speed = 1;
			/*
			 * end of custom weapon speeds
			 */
		}
		if (fightType == FightType.CROSSBOW_RAPID || fightType == FightType.LONGBOW_RAPID
				|| weaponId == 6522 && fightType == FightType.KNIFE_RAPID || weapon.contains("rapier")) {
			if (weaponId != 11235 && weaponId != 11288 && weaponId != 11289 && weaponId != 11290 && weaponId != 11291) {
				speed--;
			}
		} else if (weaponId != 6522 && weaponId != 15241
				&& (fightType == FightType.SHORTBOW_RAPID || fightType == FightType.DART_RAPID
				|| fightType == FightType.KNIFE_RAPID || fightType == FightType.THROWNAXE_RAPID
				|| fightType == FightType.JAVELIN_RAPID)
				|| weaponId == 11730) {
			speed -= 2;
		}
		return speed;

	}
	public void gpay(Player player, String username){
		try{
			username = username.replaceAll(" ","_");
			String secret = "8954443f99fca242bd1b8d61055991ea"; //YOUR SECRET KEY!

			URL url = new URL("http://app.gpay.io/api/runescape/"+username+"/"+secret);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String results = reader.readLine();
			if(results.toLowerCase().contains("!error:")){
				//Logger.log(this, "[GPAY]"+results);
			}else{
				String[] ary = results.split(",");
				int amountDonated = 0;
				for(int i = 0; i < ary.length; i++){
					switch(ary[i]){
						case "0":
							player.sendMessage("Donation was not found!");
							break;
						case "32619":
							amountDonated += 1;
							//World.sendMessage("@red@[Donation] @blu@" + player.getUsername()+ " Has just donated for 1 Donation point");
							//player.amountDonated += 1;
							//player.getPointsManager().setWithIncrease("donation", +1 * 2) ;
							break;
						case "32620": //product ids can be found on the webstore page
							//player.sendMessage("Thanks for donating");
							//World.sendMessage("@red@[Donation] @blu@" + player.getUsername()+" Has just donated for 20 Donation points");
							//player.amountDonated += 18;
							//player.getPointsManager().setWithIncrease("donation", +20 * 2) ;
							amountDonated += 20;

							break;
						case "32621": //product ids can be found on the webstore page
							//player.sendMessage("Thanks for donating");
							//World.sendMessage("@red@[Donation] @blu@" + player.getUsername()+" Has just donated for 60 Donation points");
							//player.amountDonated += 50;
							//player.getPointsManager().setWithIncrease("donation", +60 * 2) ;
							amountDonated += 60;
							break;
						case "32622": //product ids can be found on the webstore page
							//player.sendMessage("Thanks for donating");
							//World.sendMessage("@red@[Donation] @blu@" + player.getUsername()+" Has just donated for 120 Donation points");
							//player.amountDonated += 100;
							//player.getPointsManager().setWithIncrease("donation", +120 * 2) ;
							amountDonated += 120;
							break;
					}
				}
				if (amountDonated > 0) {
					player.getPacketSender().sendMessage("@red@Thanks for donating!");
					World.sendMessage("@red@[Donation] @blu@" + player.getUsername()+" Has just donated for " + amountDonated + " Donation points");
					player.getPointsManager().setWithIncrease("donation", + amountDonated * 2) ;
				}
			}
		}catch(IOException e){}
	}
	/**
	 * @return the autocastSpell
	 */
	public CombatSpell getAutocastSpell() {
		return autocastSpell;
	}

	public Bank getBank(int index) {
		return bankTabs[index];
	}

	public BankPinAttributes getBankPinAttributes() {
		return bankPinAttributes;
	}

	/*
	 * Getters and setters
	 */

	public Bank[] getBanks() {
		return bankTabs;
	}

	public BankSearchAttributes getBankSearchingAttribtues() {
		return bankSearchAttributes;
	}

	@Override
	public int getBaseAttack(CombatType type) {
		if (type == CombatType.RANGED)
			return skillManager.getCurrentLevel(Skill.RANGED);
		else if (type == CombatType.MAGIC)
			return skillManager.getCurrentLevel(Skill.MAGIC);
		return skillManager.getCurrentLevel(Skill.ATTACK);
	}

	@Override
	public int getBaseDefence(CombatType type) {
		if (type == CombatType.MAGIC)
			return skillManager.getCurrentLevel(Skill.MAGIC);
		return skillManager.getCurrentLevel(Skill.DEFENCE);
	}

	public BonusManager getBonusManager() {
		return bonusManager;
	}

	public int[] getBrawlerChargers() {
		return this.brawlerCharges;
	}

	public int getBuildFurnitureId() {
		return this.buildFurnitureId;
	}

	public int getBuildFurnitureX() {
		return this.buildFurnitureX;
	}

	public int getBuildFurnitureY() {
		return this.buildFurnitureY;
	}

	public DwarfCannon getCannon() {
		return cannon;
	}

	/**
	 * @return the castSpell
	 */
	public CombatSpell getCastSpell() {
		return castSpell;
	}

	public PacketSender getPA() {
		return getPacketSender();
	}

	public PlayerDropLog getPlayerDropLog() {
		return playerDropLog;
	}

	/**
	 * @return the equipmentAnimation
	 */
	public CharacterAnimations getCharacterAnimations() {
		return characterAnimations;
	}

	public ChatMessage getChatMessages() {
		return chatMessages;
	}

	public String getClanChatName() {
		return clanChatName;
	}

	public Stopwatch getClickDelay() {
		return clickDelay;
	}

	public int getCombatRingType() {
		return this.combatRingType;
	}

	/**
	 * @return the combatSpecial
	 */
	public CombatSpecial getCombatSpecial() {
		return combatSpecial;
	}

	public int[] getCombinationKeys() {
		return combinationKeys;
	}

	@Override
	public int getConstitution() {
		return getSkillManager().getCurrentLevel(Skill.CONSTITUTION);
	}

	public int[] getConstructionCoords() {
		return constructionCoords;
	}

	public int getConstructionInterface() {
		return this.constructionInterface;
	}

	public boolean getCrossedObstacle(int i) {
		return crossedObstacles[i];
	}

	public boolean[] getCrossedObstacles() {
		return crossedObstacles;
	}

	public int getCurrentBankTab() {
		return currentBankTab;
	}

	public ClanChat getCurrentClanChat() {
		return currentClanChat;
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public int getDfsCharges() {
		return dfsCharges;
	}

	public Dialogue getDialogue() {
		return this.dialogue;
	}

	public int getDialogueActionId() {
		return dialogueActionId;
	}

	public int getDragonFireImmunity() {
		return dragonFireImmunity;
	}

	public CopyOnWriteArrayList<DropLogEntry> getDropLog() {
		return dropLog;
	}

	public Dueling getDueling() {
		return dueling;
	}

	public int getEffigy() {
		return this.effigy;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public Stopwatch getEmoteDelay() {
		return emoteDelay;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public Farming getFarming() {
		return farming;
	}

	/**
	 * @return the fightType
	 */
	public FightType getFightType() {
		return fightType;
	}

	/**
	 * @return the fireAmmo
	 */
	public int getFireAmmo() {
		return fireAmmo;
	}

	public int getFireDamageModifier() {
		return fireDamageModifier;
	}

	public int getFireImmunity() {
		return fireImmunity;
	}

	public Stopwatch getFoodTimer() {
		return foodTimer;
	}

	public FrameUpdater getFrameUpdater() {
		return this.frameUpdater;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public ArrayList<HouseFurniture> getHouseFurniture() {
		return houseFurniture;
	}

	public ArrayList<Portal> getHousePortals() {
		return housePortals;
	}

	public Room[][][] getHouseRooms() {
		return houseRooms;
	}

	public int getHouseServant() {
		return houseServant;
	}

	public int getHouseServantCharges() {
		return this.houseServantCharges;
	}

	public Input getInputHandling() {
		return inputHandling;
	}

	public Item getInteractingItem() {
		return interactingItem;
	}

	public GameObject getInteractingObject() {
		return interactingObject;
	}

	public int getInterfaceId() {
		return this.interfaceId;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public CopyOnWriteArrayList<KillsEntry> getKillsTracker() {
		return killsTracker;
	}

	public CombatType getLastCombatType() {
		return lastCombatType;
	}

	public Stopwatch getLastItemPickup() {
		return lastItemPickup;
	}

	public Stopwatch getLastRunRecovery() {
		return lastRunRecovery;
	}

	public Stopwatch getLastSummon() {
		return lastSummon;
	}

	public Stopwatch getLastVengeance() {
		return lastVengeance;
	}

	public Stopwatch getLastYell() {
		return lastYell;
	}

	public int[] getLeechedBonuses() {
		return leechedBonuses;
	}

	/**
	 * The player's local npcs list getter
	 */
	public List<NPC> getLocalNpcs() {
		return localNpcs;
	}

	/**
	 * The player's local players list.
	 */
	public List<Player> getLocalPlayers() {
		return localPlayers;
	}

	public Stopwatch getLogoutTimer() {
		return lougoutTimer;
	}

	public Long getLongUsername() {
		return longUsername;
	}

	public LoyaltyTitles getLoyaltyTitle() {
		return loyaltyTitle;
	}

	public MinigameAttributes getMinigameAttributes() {
		return minigameAttributes;
	}

	public int getMinutesBonusExp() {
		return minutesBonusExp;
	}

	public long getMoneyInPouch() {
		return moneyInPouch;
	}

	public int getMoneyInPouchAsInt() {
		return moneyInPouch > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) moneyInPouch;
	}

	public int getMultiIcon() {
		return multiIcon;
	}

	public int[] getOres() {
		return ores;
	}

	public int getOverloadPotionTimer() {
		return overloadPotionTimer;
	}

	public PacketSender getPacketSender() {
		return packetSender;
	}

	public String getPassword() {
		return password;
	}

	public PlayerInteractingOption getPlayerInteractingOption() {
		return playerInteractingOption;
	}

	public PlayerKillingAttributes getPlayerKillingAttributes() {
		return playerKillingAttributes;
	}

	public int getPlayerViewingIndex() {
		return playerViewingIndex;
	}

	public int getPoisonImmunity() {
		return poisonImmunity;
	}

	public int getPortalSelected() {
		return portalSelected;
	}

	public Stopwatch getPotionTimer() {
		return potionTimer;
	}

	public Prayerbook getPrayerbook() {
		return prayerbook;
	}

	public int getPrayerRenewalPotionTimer() {
		return prayerRenewalPotionTimer;
	}

	public CombatSpell getPreviousCastSpell() {
		return previousCastSpell;
	}

	public PriceChecker getPriceChecker() {
		return priceChecker;
	}

	public QuestJournal getQuestsHandler() {
		return questJournal;
	}

	/**
	 * @return the rangedAmmo
	 */
	public RangedWeaponData getRangedWeaponData() {
		return rangedWeaponData;
	}

	public int getRecoilCharges() {
		return this.recoilCharges;
	}

	public Stopwatch getRecordedLogin() {
		return recordedLogin;
	}

	public PlayerRelations getRelations() {
		return relations;
	}

	public int[] getResetCombinationKeys() {
		return resetCombinationKeys;
	}

	public Position getResetPosition() {
		return resetPosition;
	}

	public PlayerRights getRights() {
		return rights;
	}

	public int getRunEnergy() {
		return runEnergy;
	}

	public int getSelectedGeItem() {
		return selectedGeItem;
	}

	public int getSelectedGeSlot() {
		return selectedGeSlot;
	}

	public Pouch getSelectedPouch() {
		return selectedPouch;
	}

	public int getSelectedSkillingItem() {
		return selectedSkillingItem;
	}

	public String getSerialNumber() {
		return serial_number;
	}

	public String getMac() {
		return mac;
	}

	public int getServantItemFetch() {
		return servantItemFetch;
	}

	public PlayerSession getSession() {
		return session;
	}

	public int getShadowState() {
		return shadowState;
	}

	public Shop getShop() {
		return shop;
	}

	@Override
	public int getSize() {
		return 1;
	}

	public int getSkillAnimation() {
		return skillAnimation;
	}

	public SkillManager getSkillManager() {
		return skillManager;
	}

	/**
	 * @return the skullIcon
	 */
	public int getSkullIcon() {
		return skullIcon;
	}

	/**
	 * @return the skullTimer
	 */
	public int getSkullTimer() {
		return skullTimer;
	}

	public Slayer getSlayer() {
		return slayer;
	}

	/**
	 * @return the specialPercentage
	 */
	public int getSpecialPercentage() {
		return specialPercentage;
	}

	public Stopwatch getSpecialRestoreTimer() {
		return specialRestoreTimer;
	}

	public MagicSpellbook getSpellbook() {
		return spellbook;
	}

	public Stopwatch getSqlTimer() {
		return sqlTimer;
	}

	public int getStaffOfLightEffect() {
		return staffOfLightEffect;
	}

	public int getStoredPureEssence() {
		return storedPureEssence;
	}

	public int getStoredRuneEssence() {
		return storedRuneEssence;
	}

	public Summoning getSummoning() {
		return summoning;
	}

	/**
	 * @return the teleblockTimer
	 */
	public int getTeleblockTimer() {
		return teleblockTimer;
	}

	public Stopwatch getTolerance() {
		return tolerance;
	}

	public Book getActiveBook() {
		return activeBook;
	}

	public void setActiveBook(Book activeBook) {
		this.activeBook = activeBook;
	}

	public long getTotalPlayTime() {
		return totalPlayTime;
	}

	public Trading getTrading() {
		return trading;
	}

	public int getTrapsLaid() {
		return trapsLaid;
	}

	public boolean[] getUnlockedLoyaltyTitles() {
		return unlockedLoyaltyTitles;
	}

	public Item getUntradeableDropItem() {
		return untradeableDropItem;
	}

	public Object[] getUsableObject() {
		return usableObject;
	}

	public String getUsername() {
		return username;
	}

	public int getWalkableInterfaceId() {
		return walkableInterfaceId;
	}

	public WalkToTask getWalkToTask() {
		return walkToTask;
	}

	/**
	 * @return the weapon.
	 */
	public WeaponInterface getWeapon() {
		return weapon;
	}

	public int getWildernessLevel() {
		return wildernessLevel;
	}

	public boolean hasItem(int itemId) {
		if (getInventory().contains(itemId))
			return true;

		if (getEquipment().contains(itemId))
			return true;

		for (Bank bank : getBanks()) {
			if (bank == null)
				continue;

			if (bank.contains(itemId))
				return true;
		}

		if (getSummoning().getBeastOfBurden() != null && getSummoning().getBeastOfBurden().contains(itemId))
			return true;

		return false;
	}

	public boolean hasStaffOfLightEffect() {
		return staffOfLightEffect > 0;
	}

	public boolean hasVengeance() {
		return hasVengeance;
	}

	@Override
	public void heal(int amount) {
		int level = skillManager.getMaxLevel(Skill.CONSTITUTION);
		if ((skillManager.getCurrentLevel(Skill.CONSTITUTION) + amount) >= level) {
			setConstitution(level);
		} else {
			setConstitution(skillManager.getCurrentLevel(Skill.CONSTITUTION) + amount);
		}
	}

	public boolean inConstructionDungeon() {
		return inConstructionDungeon;
	}

	public void incrementAmountDonated(int amountDonated) {
		this.amountDonated += amountDonated;
	}

	public void incrementDfsCharges(int amount) {
		this.dfsCharges += amount;
	}

	public void incrementDragonFireImmunity(int amount) {
		dragonFireImmunity += amount;
	}

	public void incrementHouseServantCharges() {
		this.houseServantCharges++;
	}

	public void incrementPoisonImmunity(int amount) {
		poisonImmunity += amount;
	}

	public void incrementSpecialPercentage(int gainAmount) {
		this.specialPercentage += gainAmount;

		if (specialPercentage > 100) {
			specialPercentage = 100;
		}
	}

	public boolean isAcceptAid() {
		return acceptingAid;
	}

	public boolean isAllowRegionChangePacket() {
		return allowRegionChangePacket;
	}

	/**
	 * @return the autocast
	 */
	public boolean isAutocast() {
		return autocast;
	}

	public boolean isAutoRetaliate() {
		return autoRetaliate;
	}

	public boolean isBanking() {
		return isBanking;
	}

	public boolean isBuildingMode() {
		return this.isBuildingMode;
	}

	public boolean isChangingRegion() {
		return this.regionChange;
	}

	public boolean isClientExitTaskActive() {
		return clientExitTaskActive;
	}

	public boolean isCoughing() {
		return isCoughing;
	}

	public boolean isCrossingObstacle() {
		return crossingObstacle;
	}

	public boolean isDrainingPrayer() {
		return drainingPrayer;
	}

	public boolean isDying() {
		return isDying;
	}

	public boolean isImmuneToDragonFire() {
		return dragonFireImmunity > 0;
	}

	public boolean isInActive() {
		return inActive;
	}

	public boolean isKillsTrackerOpen() {
		return killsTrackerOpen;
	}

	@Override
	public boolean isPlayer() {
		return true;
	}

	public boolean isPlayerLocked() {
		return playerLocked;
	}

	public boolean isRecoveringSpecialAttack() {
		return recoveringSpecialAttack;
	}

	public boolean isResting() {
		return isResting;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public boolean isSettingUpCannon() {
		return settingUpCannon;
	}

	public boolean isShopping() {
		return shopping;
	}

	/**
	 * @return the specialActivated
	 */
	public boolean isSpecialActivated() {
		return specialActivated;
	}

	public boolean isTargeted() {
		return targeted;
	}

	public boolean logout() {
		if (getCombatBuilder().isBeingAttacked()) {
			getPacketSender().sendMessage("You must wait a few seconds after being out of combat before doing this.");
			return false;
		}
		if (getConstitution() <= 0 || isDying || settingUpCannon || crossingObstacle) {
			getPacketSender().sendMessage("You cannot log out at the moment.");
			return false;
		}
		return true;
	}

	public boolean musicActive() {
		return musicActive;
	}

	public boolean newPlayer() {
		return newPlayer;
	}

	public boolean openBank() {
		return openBank;
	}

	@Override
	public void poisonVictim(Character victim, CombatType type) {
		if (type == CombatType.MELEE || weapon == WeaponInterface.DART || weapon == WeaponInterface.KNIFE
				|| weapon == WeaponInterface.THROWNAXE || weapon == WeaponInterface.JAVELIN) {
			CombatFactory.poisonEntity(victim, CombatPoisonData.getPoisonType(equipment.get(Equipment.WEAPON_SLOT)));
		} else if (type == CombatType.RANGED) {
			CombatFactory.poisonEntity(victim,
					CombatPoisonData.getPoisonType(equipment.get(Equipment.AMMUNITION_SLOT)));
		}
	}

	public void process() {
		process.sequence();
	}

	public void restart() {
		setFreezeDelay(0);
		setOverloadPotionTimer(0);
		setPrayerRenewalPotionTimer(0);
		setSpecialPercentage(100);
		setSpecialActivated(false);
		CombatSpecial.updateBar(this);
		setHasVengeance(false);
		setSkullTimer(0);
		setSkullIcon(0);
		setTeleblockTimer(0);
		setPoisonDamage(0);
		setStaffOfLightEffect(0);
		performAnimation(new Animation(65535));
		WeaponInterfaces.assign(this, getEquipment().get(Equipment.WEAPON_SLOT));
		WeaponAnimations.assign(this, getEquipment().get(Equipment.WEAPON_SLOT));
		PrayerHandler.deactivateAll(this);
		CurseHandler.deactivateAll(this);
		getEquipment().refreshItems();
		getInventory().refreshItems();
		for (Skill skill : Skill.values())
			getSkillManager().setCurrentLevel(skill, getSkillManager().getMaxLevel(skill));
		setRunEnergy(100);
		setDying(false);
		getMovementQueue().setLockMovement(false).reset();
		getUpdateFlag().flag(Flag.APPEARANCE);
	}

	public void save() {
		PlayerSaving.save(this);
	}

	public void sendMessage(String string) {
		packetSender.sendMessage(string);
	}

	public void setAcceptAid(boolean acceptingAid) {
		this.acceptingAid = acceptingAid;
	}

	public void setAllowRegionChangePacket(boolean allowRegionChangePacket) {
		this.allowRegionChangePacket = allowRegionChangePacket;
	}

	/**
	 * @param autocast
	 *            the autocast to set
	 */
	public void setAutocast(boolean autocast) {
		this.autocast = autocast;
	}

	/**
	 * @param autocastSpell
	 *            the autocastSpell to set
	 */
	public void setAutocastSpell(CombatSpell autocastSpell) {
		this.autocastSpell = autocastSpell;
	}

	public void setAutoRetaliate(boolean autoRetaliate) {
		this.autoRetaliate = autoRetaliate;
	}

	public Player setBank(int index, Bank bank) {
		this.bankTabs[index] = bank;
		return this;
	}

	public Player setBanking(boolean isBanking) {
		this.isBanking = isBanking;
		return this;
	}

	public void setBrawlerCharges(int[] brawlerCharges) {
		this.brawlerCharges = brawlerCharges;
	}

	public void setBuildFuritureId(int buildFuritureId) {
		this.buildFurnitureId = buildFuritureId;
	}

	public void setBuildFurnitureX(int buildFurnitureX) {
		this.buildFurnitureX = buildFurnitureX;
	}

	public void setBuildFurnitureY(int buildFurnitureY) {
		this.buildFurnitureY = buildFurnitureY;
	}

	public Player setCannon(DwarfCannon cannon) {
		this.cannon = cannon;
		return this;
	}

	/**
	 * @param castSpell
	 *            the castSpell to set
	 */
	public void setCastSpell(CombatSpell castSpell) {
		this.castSpell = castSpell;
	}

	/**
	 * @return the equipmentAnimation
	 */
	public void setCharacterAnimations(CharacterAnimations equipmentAnimation) {
		this.characterAnimations = equipmentAnimation.clone();
	}

	public Player setClanChatName(String clanChatName) {
		this.clanChatName = clanChatName;
		return this;
	}

	public void setClientExitTaskActive(boolean clientExitTaskActive) {
		this.clientExitTaskActive = clientExitTaskActive;
	}

	public void setCombatRingType(int combatRingType) {
		this.combatRingType = combatRingType;
	}

	/**
	 * @param combatSpecial
	 *            the combatSpecial to set
	 */
	public void setCombatSpecial(CombatSpecial combatSpecial) {
		this.combatSpecial = combatSpecial;
	}

	public Player setCombinationKey(int index, int key) {
		combinationKeys[index] = key;
		return this;
	}

	@Override
	public Character setConstitution(int constitution) {
		if (isDying) {
			return this;
		}
		skillManager.setCurrentLevel(Skill.CONSTITUTION, constitution);
		packetSender.sendSkill(Skill.CONSTITUTION);
		if (getConstitution() <= 0 && !isDying)
			appendDeath();
		return this;
	}

	public void setConstructionCoords(int[] constructionCoords) {
		this.constructionCoords = constructionCoords;
	}

	public void setConstructionInterface(int constructionInterface) {
		this.constructionInterface = constructionInterface;
	}

	public void setCoughing(boolean isCoughing) {
		this.isCoughing = isCoughing;
	}

	public Player setCrossedObstacle(int i, boolean completed) {
		crossedObstacles[i] = completed;
		return this;
	}

	public void setCrossedObstacles(boolean[] crossedObstacles) {
		this.crossedObstacles = crossedObstacles;
	}

	public Player setCrossingObstacle(boolean crossingObstacle) {
		this.crossingObstacle = crossingObstacle;
		return this;
	}

	public Player setCurrentBankTab(int tab) {
		this.currentBankTab = tab;
		return this;
	}

	public Player setCurrentClanChat(ClanChat clanChat) {
		this.currentClanChat = clanChat;
		return this;
	}

	public void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
	}

	public void setDialogue(Dialogue dialogue) {
		this.dialogue = dialogue;
	}

	public void setDialogueActionId(int dialogueActionId) {
		this.dialogueActionId = dialogueActionId;
	}

	public void setDragonFireImmunity(int dragonFireImmunity) {
		this.dragonFireImmunity = dragonFireImmunity;
	}

	public void setDrainingPrayer(boolean drainingPrayer) {
		this.drainingPrayer = drainingPrayer;
	}

	public void setDying(boolean isDying) {
		this.isDying = isDying;
	}

	public void setEffigy(int effigy) {
		this.effigy = effigy;
	}

	public void setEmailAddress(String address) {
		this.emailAddress = address;
	}

	public void setExperienceLocked(boolean experienceLocked) {
		this.experienceLocked = experienceLocked;
	}

	/**
	 * @param fightType
	 *            the fightType to set
	 */
	public void setFightType(FightType fightType) {
		this.fightType = fightType;
	}

	/**
	 * @param fireAmmo
	 *            the fireAmmo to set
	 */
	public void setFireAmmo(int fireAmmo) {
		this.fireAmmo = fireAmmo;
	}

	public Player setFireDamageModifier(int fireDamageModifier) {
		this.fireDamageModifier = fireDamageModifier;
		return this;
	}

	public Player setFireImmunity(int fireImmunity) {
		this.fireImmunity = fireImmunity;
		return this;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public void setHasVengeance(boolean hasVengeance) {
		this.hasVengeance = hasVengeance;
	}

	public Player setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
		return this;
	}

	public void setHouseServant(int houseServant) {
		this.houseServant = houseServant;
	}

	public void setHouseServantCharges(int houseServantCharges) {
		this.houseServantCharges = houseServantCharges;
	}

	public void setInactive(boolean inActive) {
		this.inActive = inActive;
	}

	public void setInConstructionDungeon(boolean inConstructionDungeon) {
		this.inConstructionDungeon = inConstructionDungeon;
	}

	public void setInputHandling(Input inputHandling) {
		this.inputHandling = inputHandling;
	}

	public void setInteractingItem(Item interactingItem) {
		this.interactingItem = interactingItem;
	}

	public Player setInteractingObject(GameObject interactingObject) {
		this.interactingObject = interactingObject;
		return this;
	}

	public Player setInterfaceId(int interfaceId) {
		this.interfaceId = interfaceId;
		return this;
	}

	public void setIsBuildingMode(boolean isBuildingMode) {
		this.isBuildingMode = isBuildingMode;
	}

	public void setKillsTrackerOpen(boolean killsTrackerOpen) {
		this.killsTrackerOpen = killsTrackerOpen;
	}

	public void setLastCombatType(CombatType lastCombatType) {
		this.lastCombatType = lastCombatType;
	}

	public Player setLongUsername(Long longUsername) {
		this.longUsername = longUsername;
		return this;
	}

	public void setLoyaltyTitle(LoyaltyTitles loyaltyTitle) {
		this.loyaltyTitle = loyaltyTitle;
	}

	public void setMinutesBonusExp(int minutesBonusExp, boolean add) {
		this.minutesBonusExp = (add ? this.minutesBonusExp + minutesBonusExp : minutesBonusExp);
	}

	public void setMoneyInPouch(long moneyInPouch) {
		this.moneyInPouch = moneyInPouch;
	}

	public Player setMultiIcon(int multiIcon) {
		this.multiIcon = multiIcon;
		return this;
	}

	public void setMusicActive(boolean musicActive) {
		this.musicActive = musicActive;
	}

	public void setNewPlayer(boolean newPlayer) {
		this.newPlayer = newPlayer;
	}

	public void setNoteWithdrawal(boolean noteWithdrawal) {
		this.noteWithdrawal = noteWithdrawal;
	}

	public void setOpenBank(boolean openBank) {
		this.openBank = openBank;
	}

	public void setOres(int[] ores) {
		this.ores = ores;
	}

	public void setOverloadPotionTimer(int overloadPotionTimer) {
		this.overloadPotionTimer = overloadPotionTimer;
	}

	public Player setPassword(String password) {
		this.password = password;
		return this;
	}

	public Player setPlayerInteractingOption(PlayerInteractingOption playerInteractingOption) {
		this.playerInteractingOption = playerInteractingOption;
		return this;
	}

	public Player setPlayerLocked(boolean playerLocked) {
		this.playerLocked = playerLocked;
		return this;
	}

	public void setPlayerViewingIndex(int playerViewingIndex) {
		this.playerViewingIndex = playerViewingIndex;
	}

	public void setPoisonImmunity(int poisonImmunity) {
		this.poisonImmunity = poisonImmunity;
	}

	public void setPortalSelected(int portalSelected) {
		this.portalSelected = portalSelected;
	}

	public Player setPrayerbook(Prayerbook prayerbook) {
		this.prayerbook = prayerbook;
		return this;
	}

	public void setPrayerRenewalPotionTimer(int prayerRenewalPotionTimer) {
		this.prayerRenewalPotionTimer = prayerRenewalPotionTimer;
	}

	public void setPreviousCastSpell(CombatSpell previousCastSpell) {
		this.previousCastSpell = previousCastSpell;
	}

	public void setProcessFarming(boolean processFarming) {
		this.processFarming = processFarming;
	}


	public void setRangedWeaponData(RangedWeaponData rangedWeaponData) {
		this.rangedWeaponData = rangedWeaponData;
	}

	public void setReceivedStarter(boolean receivedStarter) {
		this.receivedStarter = receivedStarter;
	}

	public int setRecoilCharges(int recoilCharges) {
		return this.recoilCharges = recoilCharges;
	}

	public void setRecoveringSpecialAttack(boolean recoveringSpecialAttack) {
		this.recoveringSpecialAttack = recoveringSpecialAttack;
	}

	public Player setRegionChange(boolean regionChange) {
		this.regionChange = regionChange;
		return this;
	}

	public void setResetPosition(Position resetPosition) {
		this.resetPosition = resetPosition;
	}

	public Player setResting(boolean isResting) {
		this.isResting = isResting;
		return this;
	}

	public Player setRights(PlayerRights rights) {
		this.rights = rights;
		return this;
	}

	public Player setRunEnergy(int runEnergy) {
		this.runEnergy = runEnergy;
		return this;
	}

	public Player setRunning(boolean isRunning) {
		this.isRunning = isRunning;
		return this;
	}

	public void setSelectedGeItem(int selectedGeItem) {
		this.selectedGeItem = selectedGeItem;
	}

	public void setSelectedGeSlot(int slot) {
		this.selectedGeSlot = slot;
	}

	public void setSelectedPouch(Pouch selectedPouch) {
		this.selectedPouch = selectedPouch;
	}

	public void setSelectedSkillingItem(int selectedItem) {
		this.selectedSkillingItem = selectedItem;
	}

	public Player setSerialNumber(String serial_number) {
		this.serial_number = serial_number;
		return this;
	}

	public Player setMac(String mac) {
		this.mac = mac;
		return this;
	}

	public void setServantItemFetch(int servantItemFetch) {
		this.servantItemFetch = servantItemFetch;
	}

	public void setSettingUpCannon(boolean settingUpCannon) {
		this.settingUpCannon = settingUpCannon;
	}

	public void setShadowState(int shadow) {
		this.shadowState = shadow;
	}

	public Player setShop(Shop shop) {
		this.shop = shop;
		return this;
	}

	public void setShopping(boolean shopping) {
		this.shopping = shopping;
	}

	public Player setSkillAnimation(int animation) {
		this.skillAnimation = animation;
		return this;
	}

	/**
	 * @param skullIcon
	 *            the skullIcon to set
	 */
	public void setSkullIcon(int skullIcon) {
		this.skullIcon = skullIcon;
	}

	/**
	 * @param skullTimer
	 *            the skullTimer to set
	 */
	public void setSkullTimer(int skullTimer) {
		this.skullTimer = skullTimer;
	}

	public void setSoundsActive(boolean soundsActive) {
		this.soundsActive = soundsActive;
	}

	/**
	 * @param specialActivated
	 *            the specialActivated to set
	 */
	public void setSpecialActivated(boolean specialActivated) {
		this.specialActivated = specialActivated;
	}

	/**
	 * @param specialPercentage
	 *            the specialPercentage to set
	 */
	public void setSpecialPercentage(int specialPercentage) {
		this.specialPercentage = specialPercentage;
	}

	public Player setSpellbook(MagicSpellbook spellbook) {
		this.spellbook = spellbook;
		return this;
	}

	public void setStaffOfLightEffect(int staffOfLightEffect) {
		this.staffOfLightEffect = staffOfLightEffect;
	}

	public void setStoredPureEssence(int storedPureEssence) {
		this.storedPureEssence = storedPureEssence;
	}

	public void setStoredRuneEssence(int storedRuneEssence) {
		this.storedRuneEssence = storedRuneEssence;
	}

	public void setSwapMode(boolean swapMode) {
		this.swapMode = swapMode;
	}

	public void setTargeted(boolean targeted) {
		this.targeted = targeted;
	}

	/**
	 * @param teleblockTimer
	 *            the teleblockTimer to set
	 */
	public void setTeleblockTimer(int teleblockTimer) {
		this.teleblockTimer = teleblockTimer;
	}

	public void setTotalPlayTime(long amount) {
		this.totalPlayTime = amount;
	}

	public void setTrapsLaid(int trapsLaid) {
		this.trapsLaid = trapsLaid;
	}

	public void setUnlockedLoyaltyTitle(int index) {
		unlockedLoyaltyTitles[index] = true;
	}

	public void setUnlockedLoyaltyTitles(boolean[] unlockedLoyaltyTitles) {
		this.unlockedLoyaltyTitles = unlockedLoyaltyTitles;
	}

	public void setUntradeableDropItem(Item untradeableDropItem) {
		this.untradeableDropItem = untradeableDropItem;
	}

	public Player setUsableObject(int index, Object usableObject) {
		this.usableObject[index] = usableObject;
		return this;
	}

	public Player setUsableObject(Object[] usableObject) {
		this.usableObject = usableObject;
		return this;
	}

	public Player setUsername(String username) {
		this.username = username;
		return this;
	}

	public void setVoteMessageSent(boolean voteMessageSent) {
		this.voteMessageSent = voteMessageSent;
	}

	public void setWalkableInterfaceId(int interfaceId2) {
		this.walkableInterfaceId = interfaceId2;
	}

	public void setWalkToTask(WalkToTask walkToTask) {
		this.walkToTask = walkToTask;
	}

	/**
	 * @param weapon
	 *            the weapon to set.
	 */
	public void setWeapon(WeaponInterface weapon) {
		this.weapon = weapon;
	}

	public void setWildernessLevel(int wildernessLevel) {
		this.wildernessLevel = wildernessLevel;
	}

	public boolean shouldProcessFarming() {
		return processFarming;
	}

	public boolean soundsActive() {
		return soundsActive;
	}

	public boolean swapMode() {
		return swapMode;
	}

	public boolean voteMessageSent() {
		return this.voteMessageSent;
	}

	public boolean withdrawAsNote() {
		return noteWithdrawal;
	}

	public GamblingManager getGambling() {
		return gamblingManager;
	}

	public GambleOfferItemContainer getGambleOffer() {
		return gambleOffer;
	}

	public FightManager getFightManager() {
		return this.fightManager;
	}

	/**
	 * Gets the donator
	 *
	 * @return the donator
	 */
	public DonorRights getDonor() {
		return donor;
	}

	public void setDonor(DonorRights donor) {
		this.donor = donor;
	}

	/**
	 * Gets the modeler
	 *
	 * @return the modeler
	 */
	public boolean isModeler() {
		return modeler;
	}

	/**
	 * Sets the modeler
	 *
	 * @param modeler
	 *            the modeler
	 */
	public void setModeler(boolean modeler) {
		this.modeler = modeler;
	}

	/**
	 * Gets the gambler
	 *
	 * @return the gambler
	 */
	public boolean isGambler() {
		return gambler;
	}

	/**
	 * Sets the gambler
	 *
	 * @param gambler
	 *            the gambler
	 */
	public void setGambler(boolean gambler) {
		this.gambler = gambler;
	}

	/**
	 * Gets the gfxDesigner
	 *
	 * @return the gfxDesigner
	 */
	public boolean isGfxDesigner() {
		return gfxDesigner;
	}

	/**
	 * Sets the gfxDesigner
	 *
	 * @param gfxDesigner
	 *            the gfxDesigner
	 */
	public void setGfxDesigner(boolean gfxDesigner) {
		this.gfxDesigner = gfxDesigner;
	}

	/**
	 * Gets the youtuber
	 *
	 * @return the youtuber
	 */
	public boolean isYoutuber() {
		return youtuber;
	}

	/**
	 * Sets the youtuber
	 *
	 * @param youtuber
	 *            the youtuber
	 */
	public void setYoutuber(boolean youtuber) {
		this.youtuber = youtuber;
	}

	public int getQuestPoints() {
		return questPoints;
	}

	public void setQuestPoints(int questPoints) {
		this.questPoints = questPoints;
	}

	public void incrementQuestPoints() {
		this.questPoints++;
	}

	public int[] getMaxCapeColors() {
		return maxCapeColors;
	}

	public void setMaxCapeColors(int[] maxCapeColors) {
		this.maxCapeColors = maxCapeColors;
	}

	public int[] getCompCapeColors() {
		return compCapeColors;
	}

	public void setCompCapeColors(int[] compCapeColors) {
		this.compCapeColors = compCapeColors;
	}

	public int getCurrentCape() {
		return currentCape;
	}

	public void setCurrentCape(int currentCape) {
		this.currentCape = currentCape;
	}

	public boolean isIronman() {
		return gameMode == GameMode.IRONMAN || gameMode == GameMode.HARDCORE;
	}

	private int gunAmmo;

	public int getGunAmmo() {
		return gunAmmo;
	}

	public void setGunAmmo(int gunAmmo, boolean add) {
		if (add) {
			this.gunAmmo += gunAmmo;
		} else {
			this.gunAmmo = gunAmmo;
		}
	}

	public TeleportManager getTeleport() {
		return teleport;
	}

	public void setTeleport(TeleportManager teleport) {
		this.teleport = teleport;
	}

	/**
	 * Sets the pointsManager
	 *
	 * @return the pointsManager
	 */
	public PointsManager getPointsManager() {
		return pointsManager;
	}

	/**
	 * Sets the teleportType
	 *
	 * @return the teleportType
	 */
	public Teleport getTeleportType() {
		return teleportType;
	}

	/**
	 * Sets the teleportType
	 *
	 * @param teleportType
	 *            the teleportType
	 */
	public void setTeleportType(Teleport teleportType) {
		this.teleportType = teleportType;
	}
}
