package com.StreamScape.world.content.combat.strategy.impl;

import com.StreamScape.engine.task.Task;
import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.model.Animation;
import com.StreamScape.model.Graphic;
import com.StreamScape.model.Position;
import com.StreamScape.model.Projectile;
import com.StreamScape.model.definitions.NpcDefinition;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.content.combat.CombatContainer;
import com.StreamScape.world.content.combat.CombatType;
import com.StreamScape.world.content.combat.magic.CombatSpells;
import com.StreamScape.world.content.combat.strategy.CombatStrategy;
import com.StreamScape.world.entity.impl.Character;
import com.StreamScape.world.entity.impl.npc.NPC;
import com.StreamScape.world.content.combat.CombatHitTask;

import com.StreamScape.world.entity.impl.npc.NPCMovementCoordinator;
import com.StreamScape.world.entity.impl.player.Player;

public class DraconianMage implements CombatStrategy {

    public static NPC ICHIGO;


    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    } // 1ssec got a call

    @Override
    public int attackDelay(Character entity) {
        return 7;
    }

    @Override
    public int attackDistance(Character entity) {
        return 5;
    }

    @Override
    public boolean canAttack(Character entity, Character victim) {
        return true;
    }

    @Override
    public boolean customContainerAttack(Character attacker, Character target) {
        NPC ICHIGO = (NPC) attacker;
        Player p = (Player) target;


        if (p.getConstitution() <= 0 || ICHIGO.getConstitution() <= 0) {
            return true;
        }

        if (ICHIGO.isChargingAttack() || p.getConstitution() <= 0) {
            return true;
        }

        switch (ICHIGO.getId()) {
            case 1201:
                ICHIGO.performAnimation(new Animation(811));
                ICHIGO.prepareSpell(CombatSpells.FIRE_BLAST.getSpell(), p);
                ICHIGO.getCombatBuilder().setContainer(new CombatContainer(ICHIGO, target, 1, 3, CombatType.MAGIC, true));
                new CombatContainer(ICHIGO, p, 1, CombatType.MAGIC, true);
                break;
             case 1203:
                 ICHIGO.performAnimation(new Animation(811));
                 ICHIGO.prepareSpell(CombatSpells.ICE_BLITZ.getSpell(), p);
                 ICHIGO.getCombatBuilder().setContainer(new CombatContainer(ICHIGO, target, 1, 3, CombatType.MAGIC, true));
                 new CombatContainer(ICHIGO, p, 1, CombatType.MAGIC, true);
                 break;

            case 1206:
                ICHIGO.performAnimation(new Animation(811));
                ICHIGO.prepareSpell(CombatSpells.EARTH_WAVE.getSpell(), p);
                ICHIGO.getCombatBuilder().setContainer(new CombatContainer(ICHIGO, target, 1, 3, CombatType.MAGIC, true));
                new CombatContainer(ICHIGO, p, 1, CombatType.MAGIC, true);

                break;
        }

        if (ICHIGO.getCurrentlyCasting() == null) {
            ICHIGO.prepareSpell(CombatSpells.WIND_STRIKE.getSpell(), p);
    }
		return true;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MAGIC;
    }
}
