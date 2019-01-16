package com.StreamScape.world.content.combat.magic;

import com.StreamScape.model.Graphic;
import com.StreamScape.model.container.impl.Equipment;
import com.StreamScape.world.content.combat.CombatContainer;
import com.StreamScape.world.content.combat.CombatType;
import com.StreamScape.world.entity.impl.Character;
import com.StreamScape.world.entity.impl.player.Player;

public class CustomMagicStaff {

    public static enum CustomStaff {
        TORNADO_STAFF(new int[] { 3242 }, CombatSpells.TORNADO.getSpell()),
        BLOOD_STAFF(new int[] { 21011 }, CombatSpells.FIRE_BLAST_Staff.getSpell()),
        ICY_STAFF(new int[] { 21031 }, CombatSpells.ICE_BLITZ_STAFF.getSpell()),
        TOXIC_STAFF(new int[] { 21051 }, CombatSpells.EARTH_STRIKE_STAFF.getSpell()),
        DEATH_CALLER(new int[] {21074}, CombatSpells.SPAWN_SKELETON.getSpell()),
        GOLDEN_STAFF(new int[] {21054}, CombatSpells.GOLDEN_TORNADO.getSpell()),
        STARTER_STAFF(new int[] { 18009 }, CombatSpells.STARTER_STAFF_SPELL.getSpell());

        private int[] itemIds;
        private CombatSpell spell;

        CustomStaff(int[] itemIds, CombatSpell spell) {
            this.itemIds = itemIds;
            this.spell = spell;
        }

        public int[] getItems() {
            return this.itemIds;
        }

        public CombatSpell getSpell() {
            return this.spell;
        }

        public static CombatSpell getSpellForWeapon(int weaponId) {
            for (CustomStaff staff : CustomStaff.values()) {
                for (int itemId : staff.getItems())
                    if (weaponId == itemId)
                        return staff.getSpell();
            }
            return null;
        }
    }

    public static boolean checkCustomStaff(Character c) {
        int weapon;
        if (!c.isPlayer())
            return false;
        Player player = (Player)c;
        weapon = player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
        return CustomStaff.getSpellForWeapon(weapon) != null;
    }

    public static void handleCustomStaff(Character c) {
        Player player = (Player) c;
        int weapon = player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
        CombatSpell spell = CustomStaff.getSpellForWeapon(weapon);
        player.setCastSpell(spell);
        player.setAutocast(true);
        player.setAutocastSpell(spell);
        player.setCurrentlyCasting(spell);
        player.setLastCombatType(CombatType.MAGIC);
    }
    public static CombatContainer getCombatContainer(Character player, Character target) {
        ((Player)player).setLastCombatType(CombatType.MAGIC);
        return new CombatContainer(player, target, 1, 1, CombatType.MAGIC, true) {
            @Override
            public void onHit(int damage, boolean accurate) {
                target.performGraphic(new Graphic(1730));
            }
        };
    }

}
