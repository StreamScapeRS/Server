package com.StreamScape.model;

public class CharacterAnimations {

	/** The standing animation for this player. */
	private int standingAnimation = -1;

	/** The walking animation for this player. */
	private int walkingAnimation = -1;

	/** The running animation for this player. */
	private int runningAnimation = -1;

	/**
	 * Create a new {@link CharacterAnimations}.
	 */
	public CharacterAnimations() {

	}

	/**
	 * Create a new {@link CharacterAnimations}.
	 * 
	 * @param standingAnimation
	 *            the standing animation for this player.
	 * @param walkingAnimation
	 *            the walking animation for this player.
	 * @param runningAnimation
	 *            the running animation for this player.
	 */
	public CharacterAnimations(int standingAnimation, int walkingAnimation, int runningAnimation) {
		this.standingAnimation = standingAnimation;
		this.walkingAnimation = walkingAnimation;
		this.runningAnimation = runningAnimation;
	}

	@Override
	public CharacterAnimations clone() {
		CharacterAnimations ca = new CharacterAnimations();
		ca.standingAnimation = standingAnimation;
		ca.walkingAnimation = walkingAnimation;
		ca.runningAnimation = runningAnimation;
		return ca;
	}

	/**
	 * Gets the running animation for this player.
	 * 
	 * @return the running animation.
	 */
	public int getRunningAnimation() {
		return runningAnimation;
	}

	/**
	 * Gets the standing animation for this player.
	 * 
	 * @return the standing animation.
	 */
	public int getStandingAnimation() {
		return standingAnimation;
	}

	/**
	 * Gets the walking animation for this player.
	 * 
	 * @return the walking animation.
	 */
	public int getWalkingAnimation() {
		return walkingAnimation;
	}

	/**
	 * Resets the animation indexes.
	 */
	public void reset() {
		standingAnimation = -1;
		walkingAnimation = -1;
		runningAnimation = -1;
	}

	/**
	 * Sets the running animation for this player.
	 * 
	 * @param runningAnimation
	 *            the new running animation to set.
	 */
	public void setRunningAnimation(int runningAnimation) {
		this.runningAnimation = runningAnimation;
	}

	/**
	 * Sets the standing animation for this player.
	 * 
	 * @param standingAnimation
	 *            the new standing animation to set.
	 */
	public void setStandingAnimation(int standingAnimation) {
		this.standingAnimation = standingAnimation;
	}

	/**
	 * Sets the walking animation for this player.
	 * 
	 * @param walkingAnimation
	 *            the new walking animation to set.
	 */
	public void setWalkingAnimation(int walkingAnimation) {
		this.walkingAnimation = walkingAnimation;
	}

	@Override
	public String toString() {
		return "CHARACTER ANIMATIONS[standing= " + standingAnimation + ", walking= " + walkingAnimation + ", running= "
				+ runningAnimation + "]";
	}
}
