package com.StreamScape.world.content.combat.strategy.impl;

import com.StreamScape.engine.task.Task;
import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.model.Animation;
import com.StreamScape.model.Graphic;
import com.StreamScape.model.GraphicHeight;
import com.StreamScape.model.Position;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.content.combat.CombatContainer;
import com.StreamScape.world.content.combat.CombatHitTask;
import com.StreamScape.world.content.combat.CombatType;
import com.StreamScape.world.content.combat.range.CombatRangedAmmo;
import com.StreamScape.world.content.combat.strategy.CombatStrategy;
import com.StreamScape.world.entity.impl.Character;
import com.StreamScape.world.entity.impl.npc.NPC;
import com.StreamScape.world.entity.impl.npc.NPCMovementCoordinator;
import com.StreamScape.world.entity.impl.player.Player;

public class TerrorSanta implements CombatStrategy {

    public static NPC ICHIGO;


    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    } // 1ssec got a call

    @Override
    public int attackDelay(Character entity) {
        return 2;
    }

    @Override
    public int attackDistance(Character entity) {
        return 8;
    }

    @Override
    public boolean canAttack(Character entity, Character victim) {
        return true;
    }

    @Override
    public boolean customContainerAttack(Character attacker, Character target) {
        NPC ICHIGO = (NPC) attacker;
        Player p = (Player) target;

        int rnd = Misc.getRandom(15);

        if (p.getConstitution() <= 0 || ICHIGO.getConstitution() <= 0) {
            return true;
        }

        if (ICHIGO.isChargingAttack() || p.getConstitution() <= 0) {
            return true;
        }
        if (rnd < 5) {
            ICHIGO.forceChat("OHOHOHOHOH!");
            ICHIGO.performAnimation(new Animation(15072));

            p.performGraphic(new Graphic(1014));
            p.performGraphic(new Graphic(1285));


            //p.performGraphic(new Graphic(1014) , new Position(p.getPosition().getX(),p.getPosition().getY()));
            p.performGraphic(new Graphic(1014));
            new CombatHitTask(ICHIGO.getCombatBuilder(), new CombatContainer(ICHIGO, p, 1, CombatType.MAGIC, true)).handleAttack();
            return true;
        } else if (rnd > 5) {
            CombatRangedAmmo.AmmunitionData ammo = CombatRangedAmmo.AmmunitionData.ICE_BALL;

            ICHIGO.performAnimation(new Animation(ICHIGO.getDefinition().getAttackAnimation()));

            ICHIGO.performGraphic(new Graphic(ammo.getStartGfxId(),
                    ammo.getStartHeight() >= 43 ? GraphicHeight.HIGH : GraphicHeight.MIDDLE));
            p.performGraphic(new Graphic(1285));


            DefaultRangedCombatStrategy.fireProjectile(ICHIGO, p, ammo, false);

            new CombatHitTask(ICHIGO.getCombatBuilder(), new CombatContainer(ICHIGO, p, 1, CombatType.RANGED, true)).handleAttack();
            return true;
        }
        return true;
    }


    @Override
    public CombatType getCombatType() {
        return CombatType.MIXED;
    }
}
