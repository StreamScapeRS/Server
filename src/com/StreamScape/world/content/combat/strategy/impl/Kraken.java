package com.StreamScape.world.content.combat.strategy.impl;

import com.StreamScape.world.content.combat.CombatContainer;
import com.StreamScape.world.content.combat.CombatType;
import com.StreamScape.world.content.combat.strategy.CombatStrategy;
import com.StreamScape.world.entity.impl.Character;

public class Kraken implements CombatStrategy {

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public int attackDelay(Character entity) {
		return 0;
	}

	@Override
	public int attackDistance(Character entity) {
		return 0;
	}

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return false;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		return false;
	}

	@Override
	public CombatType getCombatType() {
		return null;
	}
}
