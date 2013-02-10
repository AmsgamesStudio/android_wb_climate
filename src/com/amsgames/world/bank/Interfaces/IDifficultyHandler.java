package com.amsgames.world.bank.Interfaces;

import com.amsgames.world.bank.Services.game.RoundSettings;

public interface IDifficultyHandler {

	/**
	 * Gets the next round settings. If there is no current round, then it returns the first round settings.
	 * 
	 * @return Next {@link RoundSettings}.
	 */
	public abstract RoundSettings getNextRoundSettings();

	/**
	 * Resets the round counter and returns the first round settings.
	 * 
	 * @return First {@link RoundSettings}.
	 */
	public abstract RoundSettings getFirstRoundSettings();

	/**
	 * Returns the current round settings.
	 * 
	 * @return Current {@link RoundSettings}.
	 */
	public abstract RoundSettings getCurrentRoundSettings();

}