package com.StreamScape.world.content.skill.impl.prayer;

public enum BonesData {
	GOLDEN_TORMENTED_DEMON_BONES(6904, 45000),
	SILVER_BONES(20249, 7500), CASH_BONES(20254, 6000), DARTH_BONES(20260, 5000), SKY_BONES(20255, 4000), OREO_BONES(
			3879, 3000), AMERICAN_BONES(3086, 2000), Purgatory_BONES(20259, 2750), SPONGEBOB_BONES(20256,
					1750), PIKACHU_BONES(3087, 1750), CAMOUFLAGE_BONE(980,
							10000), WINTER_CAMO_BONE(17642, 12500), BLOODSHOT_CAMO_BONE(17644, 15000),FORST_DRAGON_BONE(18830, 30000);

	public static BonesData forId(int bone) {
		for (BonesData prayerData : BonesData.values()) {
			if (prayerData.getBoneID() == bone) {
				return prayerData;
			}
		}
		return null;
	}

	private int boneId;
	private int buryXP;

	BonesData(int boneId, int buryXP) {
		this.boneId = boneId;
		this.buryXP = buryXP;
	}

	public int getBoneID() {
		return this.boneId;
	}

	public int getBuryingXP() {
		return this.buryXP;
	}

}
