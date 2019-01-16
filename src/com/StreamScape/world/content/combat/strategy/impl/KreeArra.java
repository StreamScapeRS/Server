package com.StreamScape.world.content.combat.strategy.impl;


        import com.StreamScape.engine.task.Task;
        import com.StreamScape.engine.task.TaskManager;
        import com.StreamScape.model.Animation;
        import com.StreamScape.model.Graphic;
        import com.StreamScape.model.Position;
        import com.StreamScape.model.Projectile;
        import com.StreamScape.model.Locations.Location;
        import com.StreamScape.model.movement.MovementQueue;
        import com.StreamScape.util.Misc;
        import com.StreamScape.world.World;
        import com.StreamScape.world.content.combat.CombatContainer;
        import com.StreamScape.world.content.combat.CombatHitTask;
        import com.StreamScape.world.content.combat.CombatType;
        import com.StreamScape.world.content.combat.strategy.CombatStrategy;
        import com.StreamScape.world.entity.impl.Character;
        import com.StreamScape.world.entity.impl.npc.NPC;
        import com.StreamScape.world.entity.impl.npc.NPCMovementCoordinator;
        import com.StreamScape.world.entity.impl.player.Player;

public class KreeArra implements CombatStrategy {


    public static NPC KREE;

    public static void spawn(int id, Position pos) {
        KREE = new NPC(id, pos);
        KREE.setGraphic(new Graphic(6));
        KREE.getMovementCoordinator().setCoordinator(new NPCMovementCoordinator.Coordinator(true, 3));
        KREE.getDefinition().setRespawns(true);
        KREE.getDefinition().setRespawnTime(5);
        World.register(KREE);
    }

    @Override
    public boolean canAttack(Character entity, Character victim) {
        return true;
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    @Override
    public boolean customContainerAttack(Character entity, Character victim) {
        NPC kreearra = (NPC)entity;

        if(victim.getConstitution() <= 0) {
            return true;
        }
        if(kreearra.isChargingAttack()) {
            return true;
        }

        final CombatType style = Misc.getRandom(1) == 0 ? CombatType.MAGIC : CombatType.RANGED;
        kreearra.performAnimation(new Animation(kreearra.getDefinition().getAttackAnimation()));
        kreearra.setChargingAttack(true);
        Player target = (Player)victim;
        TaskManager.submit(new Task(1, kreearra, false) {
            int tick = 0;
            @Override
            public void execute() {
                if(tick == 1) {
                    for (final Player near : Misc.getCombinedPlayerList(target)) {

                        if(near.getPosition().distanceToPoint(kreearra.getPosition().getX(), kreearra.getPosition().getY()) > 20)
                            continue;
                        new Projectile(kreearra, victim, style == CombatType.MAGIC ? 1198 : 1197, 44, 3, 43, 43, 0).sendProjectile();
                        if(style == CombatType.RANGED) { //Moving players randomly
                            int xToMove = Misc.getRandom(1);
                            int yToMove = Misc.getRandom(1);
                            int xCoord = target.getPosition().getX();
                            int yCoord = target.getPosition().getY();
                            if (Misc.getRandom(3) <= 1 && MovementQueue.canWalk(target.getPosition(), new Position(xCoord +- xToMove, yCoord +- yToMove, 0), 1)) {
                                target.getMovementQueue().reset();
                                if(!target.isTeleporting())
                                    target.moveTo(new Position(xCoord +- xToMove, yCoord +- yToMove, 0));
                                target.performGraphic(new Graphic(128));
                            }
                        }
                    }
                } else if(tick == 2) {
                    for (final Player near : Misc.getCombinedPlayerList(target)) {
                        if(near.getPosition().distanceToPoint(kreearra.getPosition().getX(), kreearra.getPosition().getY()) > 20)
                            continue;
                        new CombatHitTask(kreearra.getCombatBuilder(), new CombatContainer(kreearra, victim, 1, style, true)).handleAttack();
                    }
                    kreearra.setChargingAttack(false);
                    stop();
                }
                tick++;
            }
        });
        return true;
    }

    @Override
    public int attackDelay(Character entity) {
        return entity.getAttackSpeed();
    }

    @Override
    public int attackDistance(Character entity) {
        return 10;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MIXED;
    }
}
