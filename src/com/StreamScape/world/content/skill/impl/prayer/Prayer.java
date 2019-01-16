package com.StreamScape.world.content.skill.impl.prayer;

import com.StreamScape.engine.task.Task;
import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.model.Animation;
import com.StreamScape.model.Item;
import com.StreamScape.model.Skill;
import com.StreamScape.world.content.Achievements;
import com.StreamScape.world.content.Achievements.AchievementData;
import com.StreamScape.world.content.Sounds;
import com.StreamScape.world.content.Sounds.Sound;
import com.StreamScape.world.entity.impl.player.Player;

/**
 * The prayer skill is based upon burying the corpses of enemies. Obtaining a
 * higher level means more prayer abilities being unlocked, which help out in
 * combat.
 * 
 * @author Gabriel Hannason
 */

public class Prayer {

	public static boolean buryBone(Player player, int itemId) {
		if (!player.getClickDelay().elapsed(2000)) {
			return true;
		}

		final BonesData currentBone = BonesData.forId(itemId);
		if (currentBone == null) {
			return false;
		}

		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		player.performAnimation(new Animation(827));
		player.getPacketSender().sendMessage("You dig a hole in the ground..");
		Item bone = new Item(itemId);
		player.getInventory().delete(bone);
		Achievements.doProgress(player, AchievementData.BURY_SOME_BONES);
		Achievements.doProgress(player, AchievementData.BURY_50_BONES);
		Achievements.doProgress(player, AchievementData.BURY_500_BONES);
		TaskManager.submit(new Task(3, player, false) {
			@Override
			public void execute() {
				player.getPacketSender().sendMessage("..and bury the " + bone.getDefinition().getName() + ".");
				player.getSkillManager().addExperience(Skill.PRAYER, currentBone.getBuryingXP());
				Sounds.sendSound(player, Sound.BURY_BONE);
				this.stop();
			}
		});

		player.getClickDelay().reset();
		return true;
	}

	public static boolean isBone(int bone) {
		return BonesData.forId(bone) != null;
	}

}
