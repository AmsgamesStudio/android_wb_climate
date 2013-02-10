package com.amsgames.world.bank.Services.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.amsgames.world.bank.Interfaces.IDifficultyHandler;

/**
 * Handles the game's difficulty per round.
 * @author Alejandro Mostajo (AmsGames)
 *
 */
public class DifficultyHandler implements  Serializable, IDifficultyHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9211820990762399279L;
	/**
	 * List of all rounds and their settings.
	 */
	public List<RoundSettings> RoundsSettings;
	/**
	 * Is used in order to know which rounds comes next 
	 * in case that the list of rounds is not ordered by number.
	 */
	private int[] roundIndexLocator;
	/**
	 * Indicates which round comes next.
	 */
	private int nextRoundIndex;
	
	/**
	 * Default constructor.
	 */
	public DifficultyHandler () {
		this.RoundsSettings = new ArrayList<RoundSettings>();
		this.nextRoundIndex = 0;
		this.roundIndexLocator = new int[RoundsSettings.size()];
	}
	
	/* (non-Javadoc)
	 * @see com.amsgames.world.bank.Services.game.IDifficultyHandler#getNextRoundSettings()
	 */
	public RoundSettings getNextRoundSettings() {
		RoundSettings returns = null;
		if (this.nextRoundIndex < this.RoundsSettings.size()) {
			returns = this.RoundsSettings.get(this.roundIndexLocator[this.nextRoundIndex]);
			nextRoundIndex ++;
		}
		return returns;
	}
	
	/* (non-Javadoc)
	 * @see com.amsgames.world.bank.Services.game.IDifficultyHandler#getFirstRoundSettings()
	 */
	public RoundSettings getFirstRoundSettings() {
		this.nextRoundIndex = 0;
		return getNextRoundSettings();
	}
	
	
	
	/**
	 * Generates the internal index locator, will be used in order to know which rounds comes next 
	 * in case that the list of rounds is not ordered by number.
	 */
	public void generateIndexLocator() {
		this.nextRoundIndex = 0;
		// Initialize locator size
		this.roundIndexLocator = new int[RoundsSettings.size()];
		// Create indexes
		for (int roundIndex = 0; roundIndex < RoundsSettings.size(); roundIndex++) {
			int roundNumber = this.RoundsSettings.get(roundIndex).Round;
			// Set locator
			this.roundIndexLocator[roundNumber-1] = roundIndex;
		}
	}

	public RoundSettings getCurrentRoundSettings() {
		return this.RoundsSettings.get(nextRoundIndex-1 < 0 ? 0 : nextRoundIndex-1);
	}

}
