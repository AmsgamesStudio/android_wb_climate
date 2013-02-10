package com.amsgames.world.bank;

import java.io.Serializable;

import android.content.res.Resources;

import com.amsgames.world.bank.Services.game.DifficultyHandler;
import com.amsgames.world.bank.States.GameState;

/**
 * Handles the atmosphere. 
 * 
 * @author Jose Cruz (Amsgames)
 *
 */
public class GameConfigSave implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4541616683124196325L;
	private GameState currentState; // Starts the game in the main menu
	private DifficultyHandler difficultyHandler;
	private boolean audio = true; // Starts the game with audio
	private int height;
	private int width;
	private Resources resources;
	private float heightFactor;
 	private float widthFactor;
 	private boolean isGameOver;
 	private boolean isGameWon;
 	private int score;
 	private int currentBackgroundSound;
	
	public GameState getCurrentState() {
		return currentState;
	}
	public void setCurrentState(GameState currentState) {
		this.currentState = currentState;
	}
	
	public DifficultyHandler getDifficultyHandler() {
		return difficultyHandler;
	}
	public void setDifficultyHandler(DifficultyHandler difficultyHandler) {
		this.difficultyHandler = difficultyHandler;
	}
	
	public boolean getAudio() {
		return audio;
	}
	public void setAudio(boolean audio) {
		this.audio = audio;
	}
	
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
	public Resources getResources() {
		return resources;
	}
	public void setResources(Resources resources) {
		this.resources = resources;
	}
	
	public float getHeightFactor() {
		return heightFactor;
	}
	public void setHeightFactor(float heightFactor) {
		this.heightFactor = heightFactor;
	}
	
	public float getWidthFactor() {
		return widthFactor;
	}
	public void setWidthFactor(float widthFactor) {
		this.widthFactor = widthFactor;
	}
	
	public boolean getIsGameOver() {
		return this.isGameOver;
	}
	public void isGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}
	
	public boolean getIsGameWon() {
		return this.isGameWon;
	}
	public void isGameWon(boolean isGameWon) {
		this.isGameWon = isGameWon;
	}
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getCurrentBackgroundSound() {
		return currentBackgroundSound;
	}
	public void setCurrentBackgroundSound(int currentBackgroundSound) {
		this.currentBackgroundSound = currentBackgroundSound;
	}
	
}