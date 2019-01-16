package com.StreamScape.model.definitions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.StreamScape.util.JsonLoader;

/**
 * A single npc definition.
 *
 * @author lare96
 */
public class NpcDefinition {

	public static final int MAX_AMOUNT_OF_NPCS = 14500;


	/** An array containing all of the npc definitions. */
	private static NpcDefinition[] definitions = new NpcDefinition[MAX_AMOUNT_OF_NPCS];

	public static NpcDefinition forId(int id) {
		return id > definitions.length ? null : definitions[id];
	}

	/**
	 * Gets an array containing all of the npc definitions.
	 *
	 * @return an array containing all of the npc definitions.
	 */
	public static NpcDefinition[] getDefinitions() {
		return definitions;
	}

	/**
	 * Prepares the dynamic json loader for loading npc definitions.
	 *
	 * @return the dynamic json loader.
	 * @throws Exception
	 *             if any errors occur while preparing for load.
	 */
	public static JsonLoader parseNpcs() {
		return new JsonLoader() {
			@Override
			public String filePath() {
				return "./data/def/json/npc_definitions.json";
			}

			@Override
			public void load(JsonObject reader, Gson builder) {
				int index = reader.get("id").getAsInt();
				definitions[index] = new NpcDefinition();
				definitions[index].setId(index);
				definitions[index].setName(reader.get("name").getAsString());
				definitions[index].setExamine(reader.get("examine").getAsString());
				definitions[index].setCombatLevel(reader.get("combat").getAsInt());
				definitions[index].setNpcSize(reader.get("size").getAsInt());
				definitions[index].setAttackable(reader.get("attackable").getAsBoolean());
				definitions[index].setAggressive(reader.get("aggressive").getAsBoolean());
				definitions[index].setRetreats(reader.get("retreats").getAsBoolean());
				definitions[index].setPoisonous(reader.get("poisonous").getAsBoolean());
				definitions[index].setRespawnTime(reader.get("respawn").getAsInt());
				definitions[index].setMaxHit(reader.get("maxHit").getAsInt());
				definitions[index].setHitpoints(reader.get("hitpoints").getAsInt());
				definitions[index].setAttackSpeed(reader.get("attackSpeed").getAsInt());
				definitions[index].setAttackAnimation(reader.get("attackAnim").getAsInt());
				definitions[index].setDefenceAnimation(reader.get("defenceAnim").getAsInt());
				definitions[index].setDeathAnimation(reader.get("deathAnim").getAsInt());
				definitions[index].setAttackBonus(reader.get("attackBonus").getAsInt());
				definitions[index].setDefenceMelee(reader.get("defenceMelee").getAsInt());
				definitions[index].setDefenceRange(reader.get("defenceRange").getAsInt());
				definitions[index].setDefenceMage(reader.get("defenceMage").getAsInt());
				if (reader.has("slayerLevel")) {
					definitions[index].setSlayerLevel(reader.get("slayerLevel").getAsInt());
				}
				else if (reader.has("multi"))
					definitions[index].setMulti(reader.get("multi").getAsBoolean());

			}
		};
	}

	/** The id of the npc. */
	private int id;

	/** The name of the npc. */
	private String name;

	/** The examine of the npc. */
	private String examine;

	/** The combat level of the npc. */
	private int combat;

	/** The npc size. */
	private int size;

	/** If the npc is attackable. */
	private boolean attackable;

	/** If the npc is aggressive. */
	private boolean aggressive;

	/** the dustabce if boc aggressuin */
	private int aggressionDistance = 7;

	/** If the npc is multi attackable which means it can be attacked by more than 1 player */
	private boolean multi = false;

	/** If the npc retreats. */
	private boolean retreats;

	/** If the npc poisons. */
	private boolean poisonous;

	/** Time it takes for this npc to respawn. */
	private int respawn;

	/** The max hit of this npc. */
	private int maxHit;

	/** The amount of hp this npc has. */
	private int hitpoints;

	/** The attack speed of this npc. */
	private int attackSpeed;

	/** The attack animation of this npc. */
	private int attackAnim;

	/** The defence animation of this npc. */
	private int defenceAnim;

	/** The death animation of this npc. */
	private int deathAnim;

	/** This npc's attack bonus. */
	private int attackBonus;

	/** This npc's melee resistance. */
	private int defenceMelee;

	/** This npc's range resistance. */
	private int defenceRange;

	/** This npc's defence resistance. */
	private int defenceMage;

	/** This npc's slayer level required to attack. */
	private int slayerLevel;

	private boolean respawns = true;
	/**
	 * Gets the attacking animation for this npc.
	 *
	 * @return the attack animation.
	 */

	public boolean getRespawns() {
		return this.respawns;
	}

	public int getAttackAnimation() {
		return attackAnim;
	}

	/**
	 * Gets the attack bonus.
	 *
	 * @return the attack bonus.
	 */
	public int getAttackBonus() {
		return attackBonus;
	}

	/**
	 * Gets how fast this npc can attack.
	 *
	 * @return the attack speed.
	 */
	public int getAttackSpeed() {
		return attackSpeed;
	}

	/**
	 * Gets the combat level of this npc.
	 *
	 * @return the combat level.
	 */
	public int getCombatLevel() {
		return combat;
	}

	/**
	 * Gets the death animation for this npc.
	 *
	 * @return the death animation.
	 */
	public int getDeathAnimation() {
		return deathAnim;
	}

	/**
	 * Gets the defending animation for this npc.
	 *
	 * @return the defence animation.
	 */
	public int getDefenceAnimation() {
		return defenceAnim;
	}

	/**
	 * Gets the magic resistance.
	 *
	 * @return the defence mage.
	 */
	public int getDefenceMage() {
		return defenceMage;
	}

	/**
	 * Gets the melee resistance.
	 *
	 * @return the defence melee.
	 */
	public int getDefenceMelee() {
		return defenceMelee;
	}

	/**
	 * Gets the range resistance.
	 *
	 * @return the defence range.
	 */
	public int getDefenceRange() {
		return defenceRange;
	}

	/**
	 * Gets the examine info for this npc.
	 *
	 * @return the examine.
	 */
	public String getExamine() {
		return examine;
	}

	/**
	 * Gets the amount of hitpoints this npc has.
	 *
	 * @return the hitpoints.
	 */
	public int getHitpoints() {
		return hitpoints;
	}

	/**
	 * Gets the id of the npc.
	 *
	 * @return the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the maximum damage this npc is allowed to deal.
	 *
	 * @return the max hit.
	 */
	public int getMaxHit() {
		return maxHit;
	}

	/**
	 * Gets the name of the npc.
	 *
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the respawn time for this npc.
	 *
	 * @return the respawn time.
	 */
	public int getRespawnTime() {
		return respawn;
	}

	/**
	 * Gets the size of this npc.
	 *
	 * @return the npc size.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * This npc's slayer level required to attack
	 *
	 * @return required slayer level to attack npc
	 */
	public int getSlayerLevel() {
		return slayerLevel;
	}

	public int getAggressionDistance() {
		return this.aggressionDistance;
	}

	/**
	 * Gets if this npc is aggressive.
	 *
	 * @return true if this npc is aggressive.
	 */
	public boolean isAggressive() {
		return aggressive;
	}

	public boolean isMulti() {
		return this.multi;
	}

	/**
	 * Gets if this npc is attackable.
	 *
	 * @return true if this npc is attackable.
	 */
	public boolean isAttackable() {
		return attackable;
	}

	/**
	 * Gets if this npc is poisonous.
	 *
	 * @return the poisonous.
	 */
	public boolean isPoisonous() {
		return poisonous;
	}

	/**
	 * Gets if this npc retreats or not when their health is low.
	 *
	 * @return true if this npc retreats.
	 */
	public boolean isRetreats() {
		return retreats;
	}

	/**
	 * Sets if this npc is aggressive.
	 *
	 * @param aggressive
	 *            the aggressive to set.
	 */

	public void setAggressive(boolean aggressive) {
		this.aggressive = aggressive;
	}

	public void setAggressionDistance(int distance) {
		this.aggressionDistance = distance;
	}

	public void setMulti(Boolean multi) { this.multi = multi; }
	public void setRespawns(Boolean respawns) { this.respawns = respawns; }

	/**
	 * Sets if this npc is attackable.
	 *
	 * @param attackable
	 *            the attackable to set.
	 */
	public void setAttackable(boolean attackable) {
		this.attackable = attackable;
	}

	/**
	 * Sets the attacking animation for this npc.
	 *
	 * @param attackAnimation
	 *            the attack animation to set.
	 */
	public void setAttackAnimation(int attackAnim) {
		this.attackAnim = attackAnim;
	}

	/**
	 * Sets the attack bonus.
	 *
	 * @param attackBonus
	 *            the attack bonus to set.
	 */
	public void setAttackBonus(int attackBonus) {
		this.attackBonus = attackBonus;
	}

	/**
	 * Sets how fast this npc can attack.
	 *
	 * @param attackSpeed
	 *            the attack speed to set.
	 */
	public void setAttackSpeed(int attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	/**
	 * Sets the combat level for this npc.
	 *
	 * @param combatLevel
	 *            the combat level to set.
	 */
	public void setCombatLevel(int combat) {
		this.combat = combat;
	}

	/**
	 * Sets the death animation for this npc.
	 *
	 * @param deathAnimation
	 *            the death animation to set.
	 */
	public void setDeathAnimation(int deathAnim) {
		this.deathAnim = deathAnim;
	}

	/**
	 * Sets the defending animation for this npc.
	 *
	 * @param defenceAnimation
	 *            the defence animation to set.
	 */
	public void setDefenceAnimation(int defenceAnim) {
		this.defenceAnim = defenceAnim;
	}

	/**
	 * Sets the magic resistance.
	 *
	 * @param defenceMage
	 *            the defence mage to set.
	 */
	public void setDefenceMage(int defenceMage) {
		this.defenceMage = defenceMage;
	}

	/**
	 * Sets the melee resistance.
	 *
	 * @param defenceMelee
	 *            the defence melee to set.
	 */
	public void setDefenceMelee(int defenceMelee) {
		this.defenceMelee = defenceMelee;
	}

	/**
	 * Sets the range resistance.
	 *
	 * @param defenceRange
	 *            the defence range to set.
	 */
	public void setDefenceRange(int defenceRange) {
		this.defenceRange = defenceRange;
	}

	/**
	 * Sets the examine info for this npc.
	 *
	 * @param examine
	 *            the examine info to set.
	 */
	public void setExamine(String examine) {
		this.examine = examine;
	}

	/**
	 * Sets the amount of hitpoints this npc has.
	 *
	 * @param hitpoints
	 *            the hitpoints to set.
	 */
	public void setHitpoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}

	/**
	 * Sets the id of the npc.
	 *
	 * @param id
	 *            the id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the maximum damage this npc is allowed to deal.
	 *
	 * @param maxHit
	 *            the max hit to set.
	 */
	public void setMaxHit(int maxHit) {
		this.maxHit = maxHit;
	}

	/**
	 * Sets the name for this npc.
	 *
	 * @param name
	 *            the name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the size of this npc.
	 *
	 * @param size
	 *            the npc size to set.
	 */
	public void setNpcSize(int size) {
		this.size = size;
	}

	/**
	 * Sets if this npc is poisonous.
	 *
	 * @param poisonous
	 *            the poisonous to set.
	 */
	public void setPoisonous(boolean poisonous) {
		this.poisonous = poisonous;
	}

	/**
	 * Sets the respawn time for this npc.
	 *
	 * @param respawnTime
	 *            the respawn time to set.
	 */
	public void setRespawnTime(int respawn) {
		this.respawn = respawn;
	}

	/**
	 * Sets if this npc retreats or not when their health is low.
	 *
	 * @param retreats
	 *            the retreats to set.
	 */
	public void setRetreats(boolean retreats) {
		this.retreats = retreats;
	}

	public void setSlayerLevel(int slayerLevel) {
		this.slayerLevel = slayerLevel;
	}
}
