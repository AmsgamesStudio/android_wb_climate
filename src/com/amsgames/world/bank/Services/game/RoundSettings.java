package com.amsgames.world.bank.Services.game;

import java.io.Serializable;
import java.util.Random;

/**
 * Game round settings.
 * 
 * @author Alejandro Mostajo (AmsGames)
 *
 */
public class RoundSettings implements  Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6008605780677883725L;
	
	/**
	 * Round's year range.
	 */
	public String YearRange;
	/**
	 * Country's name.
	 */
	public String CountryName;
	/**
	 * Round's temperature.
	 */
	public double Temperature;
	/**
	 * Round's precipitation;
	 */
	public double Precipitation;
	/**
	 * Round's speed factor. Will help to set the speed of an object in a round.
	 * <br>In-game use {@link getObjectSpeed()} method instead.
	 */
	public double SpeedFactor;
	/**
	 * Round's frequency factor. Will help to set the appearance frequency of an object in the game's stage.
	 * <br>In-game use {@link getFrequency()} method instead.
	 */
	public double FrequencyFactor;
	/**
	 * Quantity of objects to appear in a round.
	 */
	public int ObjectQuantity;
	/**
	 * Round's number.
	 */
	public int Round;
	/**
	 * Fact Message.
	 */
	public String FactMessage;
	/**
	 * Difficulty Level.
	 */
	public int DifficultyLevel;
	
	/**
	 * Default constructor with parameter inputs.
	 * 
	 * @param yearRange Round's year range.
	 * @param temperature Round's temperature.
	 * @param precipitation Round's precipitation;
	 * @param speedFactor Round's speed factor. Will help to set the speed of an object in a round.
	 * @param frequencyFactor Round's frequency factor. Will help to set the appearance frequency of an object in the game's stage.
	 * @param objectQuantity Quantity of objects to appear in a round.
	 * @param round Round's number.
	 */
	public RoundSettings(
			String yearRange,
			double temperature,
			double precipitation,
			double speedFactor,
			double frequencyFactor,
			int objectQuantity,
			int round) {
		// Initialize variables
		this.YearRange = yearRange;
		this.Temperature = temperature;
		this.Precipitation = precipitation;
		this.SpeedFactor = speedFactor;
		this.FrequencyFactor = frequencyFactor;
		this.ObjectQuantity = objectQuantity;
		this.Round = round;
		this.CountryName = "";
		this.FactMessage = "";
	}
	
	/**
	 * Default constructor.
	 */
	public RoundSettings() {
		// Initialize variables
		this.YearRange = "";
		this.Temperature = 0;
		this.Precipitation = 0;
		this.SpeedFactor = 0;
		this.FrequencyFactor = 0;
		this.ObjectQuantity = 0;
		this.Round = 0;
		this.CountryName = "";
		this.FactMessage = "";
	}
	
	/**
	 * Returns the random frequency in which an object appears in the stage.
	 * It uses the frequency factor and other variables to set a different
	 * frequency per object.
	 * <br> <br> 
	 * This should be used as the following example:
	 * <br> Step 1-> Object appear in stage.
	 * <br> Step 2-> frequency = getFrequency()
	 * <br> Step 3-> Wait for frequency time to finish,
	 * <br> Step 4-> Show next object in stage.
	 * <br> Step 5-> Repeat [Step 2]
	 * 
	 * @return Frequency.
	 */
	public int getFrequency () {
		return new Random().nextInt((int)((FrequencyFactor*.5) * 100))+ (int)((FrequencyFactor*.7)*100);
	}
	
	/**
	 * Returns the random speed of an object in a round.
	 * It uses the speed factor and other variables to set a different
	 * speed value per object.
	 * 
	 * @return Speed.
	 */
	public int getObjectSpeed () {
		return new Random().nextInt((int)SpeedFactor)+1;
	}
	

}
