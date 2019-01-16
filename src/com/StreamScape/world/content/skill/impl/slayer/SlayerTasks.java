package com.StreamScape.world.content.skill.impl.slayer;

import com.StreamScape.model.Position;
import com.StreamScape.util.Misc;

/**
 * @author Gabriel Hannason
 */

public enum SlayerTasks {

	NO_TASK(null, -1, null, -1, null),

	/**
	 * Easy tasks
	 */
	CRYSTAL_SPIDER(SlayerMaster.VANNAKA, 898, "Can be found at Key zone.", 5000,
			new Position(2134, 4926)), CRYSTAL_WOLF(SlayerMaster.VANNAKA, 900,
			"Can be found at the Key zone.", 5000,
			new Position(2134, 4926)), Purgatory(SlayerMaster.VANNAKA, 510,
			"Purgatory's can be found in the Training Island Teleport.", 7500, new Position(2524, 4776)),

	FIRE_DRACONIAN_RANGER(SlayerMaster.VANNAKA, 1200, "Can be found at Draconian Teleport", 500, new Position(2387,4961)),
	FIRE_DRACONIAN_MAGE(SlayerMaster.VANNAKA, 1201, "Can be found at Draconian Teleport", 500, new Position(2387,4961)),

	/**
	 * Medium tasks
	 */
	DRYGORE_GODS(SlayerMaster.DURADEL, 550, "Drygore gods can be found in the Drygore Gods Teleport.", 12120,
			new Position(2505, 2500)),
	DAGGANOTH_SLAYER(SlayerMaster.DURADEL, 1100, "DagganothSlayers can be found in the DagganothSlayer Teleport.", 12120,
			new Position(2505, 4724)),
	ICE_DRACONIAN_RANGER(SlayerMaster.DURADEL, 1202, "Can be found at Draconian Teleport", 500, new Position(2387,4961)),
	ICE_DRACONIAN_MAGE(SlayerMaster.DURADEL, 1203, "Can be found at Draconian Teleport", 500, new Position(2387,4961)),


	/**
	 * Hard tasks
	 */
	TOXIC_DRACONIAN_RANGER(SlayerMaster.KURADEL, 1204, "Can be found at Draconian Teleport", 500, new Position(2387,4961)),
	TOXIC_DRACONIAN_MAGE(SlayerMaster.KURADEL, 1206, "Can be found at Draconian Teleport", 500, new Position(2387,4961)),

	ANOUKE(SlayerMaster.KURADEL, 1101, "Anouke's can be found in the Anouke's Teleport.", 20000,
			new Position(2505, 2500)),
	/**
	 * Elite
	 */

	SHADOW_CORP(SlayerMaster.SUMONA, 8133, "The Shadow Corporeal Beast can be found at the Shadow Corp Teleport.",50000,new Position(2832, 2829)),
	ICHIGO(SlayerMaster.SUMONA, 666, "Ichigo can be found at the Ichigo Teleport.",50000,new Position(2832, 2829)),
	Zangetsu(SlayerMaster.SUMONA, 669, "Zangetsu Can be found at the Zangetsu Teleport.",50000,new Position(2023, 4660));

//	CAMO_TORVA(SlayerMaster.KURADEL, 515, "Camo Torva Bosses can be found in the Camo Torva Boss Teleport.", 39500,
//			new Position(2540, 5785)), WINTER_CAMO_TORVA(SlayerMaster.KURADEL, 517,
//					"Winter Camo Torva Bosses can be found in the Winter Camo Torva Boss Teleport.", 44500,
//					new Position(2792, 3796)), BLOODSHOT_CAMO_TORVA(SlayerMaster.KURADEL, 518,
//							"Bloodshot Camo Torva Bosses can be found in the Bloodshot Camo Torva Boss Teleport.",
//							49500, new Position(3236, 3941)),
//
//	CAMO__TORVA(SlayerMaster.SUMONA, 515, "Camo Torva Bosses can be found in the Camo Torva Boss Teleport.", 39500,
//			new Position(2540, 5785)), WINTER__CAMO_TORVA(SlayerMaster.SUMONA, 517,
//					"Winter Camo Torva Bosses can be found in the Winter Camo Torva Boss Teleport.", 44500,
//					new Position(2792, 3796)), BLOODSHOT__CAMO_TORVA(SlayerMaster.SUMONA, 518,
//							"Bloodshot Camo Torva Bosses can be found in the Bloodshot Camo Torva Boss Teleport.",
//							49500, new Position(3236, 3941));

	;
	/*
	 * @param taskMaster
	 *
	 * @param npcId
	 *
	 * @param npcLocation
	 *
	 * @param XP
	 *
	 * @param taskPosition
	 */

	public static SlayerTasks forId(int id) {
		for (SlayerTasks tasks : SlayerTasks.values()) {
			if (tasks.ordinal() == id) {
				return tasks;
			}
		}
		return null;
	}

	public static int[] getNewTaskData(SlayerMaster master) {
		int slayerTaskId = 1, slayerTaskAmount = 20;
		int easyTasks = 0, mediumTasks = 0, hardTasks = 0, eliteTasks = 0;

		/*
		 * Calculating amount of tasks
		 */
		for (SlayerTasks task : SlayerTasks.values()) {
			if (task.getTaskMaster() == SlayerMaster.VANNAKA)
				easyTasks++;
			else if (task.getTaskMaster() == SlayerMaster.DURADEL)
				mediumTasks++;
			else if (task.getTaskMaster() == SlayerMaster.KURADEL)
				hardTasks++;
			else if (task.getTaskMaster() == SlayerMaster.SUMONA)
				eliteTasks++;
		}

		/*
		 * Getting a task
		 */
		if (master == SlayerMaster.VANNAKA) {
			slayerTaskId = 1 + Misc.getRandom(easyTasks);
			if (slayerTaskId > easyTasks)
				slayerTaskId = easyTasks;
			slayerTaskAmount = 15 + Misc.getRandom(15);
		} else if (master == SlayerMaster.DURADEL) {
			slayerTaskId = easyTasks - 1 + Misc.getRandom(mediumTasks);
			slayerTaskAmount = 12 + Misc.getRandom(13);
		} else if (master == SlayerMaster.KURADEL) {
			slayerTaskId = 1 + easyTasks + mediumTasks + Misc.getRandom(hardTasks - 1);
			slayerTaskAmount = 10 + Misc.getRandom(15);
		} else if (master == SlayerMaster.SUMONA) {
			slayerTaskId = 1 + easyTasks + mediumTasks + hardTasks + Misc.getRandom(eliteTasks - 1);
			slayerTaskAmount = 2 + Misc.getRandom(7);
		}
		return new int[] { slayerTaskId, slayerTaskAmount };
	}

	private SlayerMaster taskMaster;
	private int npcId;
	private String npcLocation;
	private int XP;

	private Position taskPosition;

	private SlayerTasks(SlayerMaster taskMaster, int npcId, String npcLocation, int XP, Position taskPosition) {
		this.taskMaster = taskMaster;
		this.npcId = npcId;
		this.npcLocation = npcLocation;
		this.XP = XP;
		this.taskPosition = taskPosition;
	}

	public int getNpcId() {
		return this.npcId;
	}

	public String getNpcLocation() {
		return this.npcLocation;
	}

	public SlayerMaster getTaskMaster() {
		return this.taskMaster;
	}

	public Position getTaskPosition() {
		return this.taskPosition;
	}

	public int getXP() {
		return this.XP;
	}
}
