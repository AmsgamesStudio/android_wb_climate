package com.amsgames.world.bank;

import android.content.res.Resources;

import com.amsgames.world.bank.Interfaces.IDifficultyHandler;
import com.amsgames.world.bank.Interfaces.IState;

/**
 * Handles the atmosphere. 
 * 
 * @author Jose Cruz (Amsgames)
 *
 */
public class GameConfig {

	public static IState currentState; // Starts the game in the main menu
 	public static IDifficultyHandler difficultyHandler;
 	public static boolean audio = true; // Starts the game with audio
 	public static int height;
 	public static int width;
 	public static Resources resources;
 	public static float heightFactor;
 	public static float widthFactor;
 	public static boolean isGameOver;
 	public static boolean isGameWon;
 	public static int score;
 	public static int currentBackgroundSound = GameAudio.THEME_EASY;
 	
}