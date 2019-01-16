package com.StreamScape.world.content.combat.strategy.impl;

import com.StreamScape.engine.task.Task;
import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.model.Animation;
import com.StreamScape.model.Graphic;
import com.StreamScape.model.Position;
import com.StreamScape.model.definitions.NpcDefinition;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.content.combat.CombatContainer;
import com.StreamScape.world.content.combat.CombatType;
import com.StreamScape.world.content.combat.strategy.CombatStrategy;
import com.StreamScape.world.entity.impl.Character;
import com.StreamScape.world.entity.impl.npc.NPC;
import com.StreamScape.world.content.combat.CombatHitTask;

import com.StreamScape.world.entity.impl.npc.NPCMovementCoordinator;
import com.StreamScape.world.entity.impl.player.Player;

public class Ichigo implements CombatStrategy {

	public static NPC ICHIGO;

	public static void death(final int id, final Position pos) {
		TaskManager.submit(new Task(id == 669 ? 40 : 2) {
			@Override
			protected void execute() {
				spawn(id == 669 ? 666 : 669, pos);
				stop();
			}
		});
	}

	public static void spawn(int id, Position pos) {
		ICHIGO = new NPC(id, pos);
		ICHIGO.setGraphic(new Graphic(6));
		ICHIGO.getMovementCoordinator().setCoordinator(new NPCMovementCoordinator.Coordinator(true, 3));
		ICHIGO.getDefinition().setRespawns(false); // that should do it. sorry xDo you ran it with false righta? yeah
		World.register(ICHIGO);
	}


	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	} // 1ssec got a call

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 2;
	}

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public boolean customContainerAttack(Character attacker, Character target) {
		NPC ICHIGO = (NPC) attacker;
		Player p = (Player)target;

		int rnd = Misc.getRandom(15);

		if(p.getConstitution() <= 0 || ICHIGO.getConstitution() <= 0) {
			return true;
		}

		if(ICHIGO.isChargingAttack() || p.getConstitution() <= 0) {
			return true;
		}
		if(rnd < 5)
		{
			ICHIGO.forceChat("Feel the power of my Bankai!");
			ICHIGO.performAnimation(new Animation(15072));
			p.performGraphic(new Graphic(1935));
			new CombatHitTask(ICHIGO.getCombatBuilder(), new CombatContainer(ICHIGO, p, 1, CombatType.MELEE, true)).handleAttack();
			return true;
		}
		else if(rnd > 5)
		{
			ICHIGO.performAnimation(new Animation(15072));
			p.performGraphic(new Graphic(2166));
			ICHIGO.forceChat("Easy match!");
			TaskManager.submit(new Task(1, ICHIGO, false) {
				@Override
				public void execute() {
					new CombatHitTask(ICHIGO.getCombatBuilder(), new CombatContainer(ICHIGO, p, 1, CombatType.MELEE, true)).handleAttack();
					stop();
				}
			});
			return true;
		} else {
			ICHIGO.performAnimation(new Animation(15071));
			p.performGraphic(new Graphic(1898));
			ICHIGO.forceChat("Feel my darkness!");
			new CombatHitTask(ICHIGO.getCombatBuilder(), new CombatContainer(ICHIGO, p, 1, CombatType.MELEE, true)).handleAttack();
			return true;
		}
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}
}
