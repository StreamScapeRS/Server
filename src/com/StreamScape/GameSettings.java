package com.StreamScape;

import java.math.BigInteger;

import com.StreamScape.model.Position;
import com.StreamScape.net.security.ConnectionHandler;

public class GameSettings {

	public static final int[] VOTE_REWARD_IDS = { 19670 };

	/**
	 * The game port
	 */
	public static final int GAME_PORT = 43594;

	/**
	 * The game version
	 */
	public static final int GAME_VERSION = 13;

	/**
	 * The maximum amount of players that can be logged in on a single game
	 * sequence.
	 */
	public static final int LOGIN_THRESHOLD = 50;

	/**
	 * The maximum amount of players that can be logged in on a single game
	 * sequence.
	 */
	public static final int LOGOUT_THRESHOLD = 50;

	/**
	 * The maximum amount of players who can receive rewards on a single game
	 * sequence.
	 */
	public static final int VOTE_REWARDING_THRESHOLD = 15;

	/**
	 * The maximum amount of connections that can be active at a time, or in other
	 * words how many clients can be logged in at once per connection. (0 is counted
	 * too)
	 */
	public static final int CONNECTION_AMOUNT = 2;

	/**
	 * The throttle interval for incoming connections accepted by the
	 * {@link ConnectionHandler}.
	 */
	public static final long CONNECTION_INTERVAL = 1000;

	/**
	 * The number of seconds before a connection becomes idle.
	 */
	public static final int IDLE_TIME = 15;

	/**
	 * The keys used for encryption on login
	 */
	public static final BigInteger RSA_MODULUS = new BigInteger(
			"141038977654242498796653256463581947707085475448374831324884224283104317501838296020488428503639086635001378639378416098546218003298341019473053164624088381038791532123008519201622098961063764779454144079550558844578144888226959180389428577531353862575582264379889305154355721898818709924743716570464556076517");
	public static final BigInteger RSA_EXPONENT = new BigInteger(
			"73062137286746919055592688968652930781933135350600813639315492232042839604916461691801305334369089083392538639347196645339946918717345585106278208324882123479616835538558685007295922636282107847991405620139317939255760783182439157718323265977678194963487269741116519721120044892805050386167677836394617891073");

	/**
	 * The maximum amount of messages that can be decoded in one sequence.
	 */
	public static final int DECODE_LIMIT = 30;

	public static int[] BANNEDWILD_ITEMS = { 295, 1647, 14667, 2996, 5510, 5512, 5509, 11999, 1389, 1390, 17401, 17402,
			15078, 4202, 15009, 15010, 15262, 4155, 13663, 292, 989, 20084, 6199, 15501, 14018, 20250, 20251, 20252,
			20253, 14556, 14557, 14558, 14559, 14560, 14561, 14562, 14563, 14564, 14565, 14566, 14567, 14568, 14569,
			14570, 14571, 14572, 20690, 14581, 14582, 14583, 14584, 14585, 14586, 14587, 14588, 14589, 14590, 14591, 79,
			80, 81, 14619, 14596, 14597, 14598, 14599, 18742, 82, 894, 895, 896, 924, 2380, 909, 898, 899, 900, 901,
			902, 903, 2548, 2547, 904, 3088, 906, 907, 908, 926, 3878, 910, 911, 912, 3091, 914, 3089, 20259, 20260,
			20256, 20249, 20254, 20255, 3879, 3072, 3073, 3074, 3075, 3076, 6862, 667, 1247, 805, 1373, 1289, 3637,
			3638, 3639, 3640, 3641, 3642, 4083, 4177, 3643, 3644, 3645, 3646, 941, 2749, 2750, 2751, 2752, 2753, 2754,
			1666, 1667, 3647, 3648, 3649, 3650, 3651, 3652, 3653, 3654, 3655, 3656, 3657, 3658, 3659, 3660, 3661, 8675,
			8677, 3619, 3620, 3621, 3622, 980, 17642, 17644, 3623, 3624, 3625, 3626, 3627, 3628, 3629, 3630, 3631, 3632,
			3271, 3272, 3273, 3274, 3275, 3276, 13664, 3807, 3808, 3809, 3090, 3811, 3812, 3080, 3081, 3082, 3083, 3086,
			3087, 4150, 3092, 3135, 3242, 3277, 3278, 3279, 3280, 3281, 3282, 3283, 3284, 3285, 3286, 3287, 3288, 3289,
			3290, 3291, 3292, 3293, 3294, 3295, 3296, 3297, 3298, 3299, 11292, 3869, 3308, 3244, 3078, 3079, 3084,
			20080, 2870, 3301, 3302, 84, 4204, 3300, 3303, 3304, 3305, 2633, 421, 85, 2572, 596, 275, 293, 298, 423,
			432, 601, 605, 709, 758, 759, 788, 983, 20079, 5, 20075, 20073, 20074, 20082, 1543, 1544, 625, 624, 623,
			621, 620, 619, 20690, 618, 11609, 665, 666, 669, 670, 671, 672, 673, 21636, 695, 11530, 11531, 11532 };

	/** GAME **/

	/**
	 * Processing the engine
	 */
	public static final int ENGINE_PROCESSING_CYCLE_RATE = 200;
	public static final int GAME_PROCESSING_CYCLE_RATE = 600;

	/**
	 * Is it currently bonus xp?
	 */
	public static final boolean BONUS_EXP = false;
	/**
	 * 
	 * The default position
	 */
	public static final Position DEFAULT_POSITION = new Position(3095, 2990);

	public static final int MAX_STARTERS_PER_IP = 1;

	/**
	 * Untradeable items Items which cannot be traded or staked
	 */
	public static final int[] UNTRADEABLE_ITEMS = { 962, 963 };

	/**
	 * Unsellable items Items which cannot be sold to shops
	 */
	public static int UNSELLABLE_ITEMS[] = new int[] { 962, 963 };

	public static final int ATTACK_TAB = 0, SKILLS_TAB = 1, QUESTS_TAB = 2, INVENTORY_TAB = 3, EQUIPMENT_TAB = 4,
			PRAYER_TAB = 5, MAGIC_TAB = 6, CLAN_CHAT_TAB = 7, FRIEND_TAB = 8, IGNORE_TAB = 9, LOGOUT = 10,
			OPTIONS_TAB = 11, EMOTES_TAB = 12, SUMMONING_TAB = 13, ACHIEVEMENT_TAB = 14, PLAYER_PANAL = 16,
			STAFF_PANAL = 17;
}
