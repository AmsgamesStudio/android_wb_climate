package com.amsgames.world.bank.Save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.os.Environment;

import com.amsgames.world.bank.GameConfig;
import com.amsgames.world.bank.GameConfigSave;
import com.amsgames.world.bank.Services.game.DifficultyHandler;
import com.amsgames.world.bank.States.GameState;

/**
 * Handles the atmosphere. 
 * 
 * @author Jose Cruz (Amsgames)
 *
 */
public class GameSave {

	private static final String SAVE_FILE = "gamestate.hob";
	
	private GameConfigSave gameConfigSave;
	
	private FileOutputStream fOut = null;
	private ObjectOutputStream oOut = null;
	
	private FileInputStream fIn = null;
	private ObjectInputStream oIn = null;
	
	public void saveGame(){
		
		try{
			
			File fullPath = Environment.getExternalStorageDirectory();
			
			File gameFile = new File(fullPath, SAVE_FILE);
			
			if (gameFile.exists()){
				
				boolean isFileDeleted = gameFile.delete();
				
				while (!isFileDeleted){
					
					isFileDeleted = gameFile.delete();
					
				}
				
			}
			
			// Create file
			gameFile.createNewFile();
			
			this.gameConfigSave = this.getInstance();
			
			fOut = new FileOutputStream(gameFile);
			oOut = new ObjectOutputStream(fOut);
			oOut.writeObject(gameConfigSave);
			oOut.close();
			
		}catch (IOException e) {
			
		}
		
	}
	
	public void loadGame(){
		
		try{
			
			File fullPath = Environment.getExternalStorageDirectory();
			
			File gameFile = new File(fullPath, SAVE_FILE);
			
			if (gameFile.exists()){
			
				fIn = new FileInputStream(gameFile);
				oIn = new ObjectInputStream(fIn);
				gameConfigSave = (GameConfigSave) oIn.readObject();
				oIn.close();
				
				this.restoreData();
				
			}
			
		}catch (IOException ex){
			
		}catch (ClassNotFoundException ex){
			
		}
		
	}
	
	public static void deleteFileConfig() {
		
		File fullPath = Environment.getExternalStorageDirectory();
		
		File gameFile = new File(fullPath, SAVE_FILE);
		
		if (gameFile.exists()){
			
			boolean isFileDeleted = gameFile.delete();
			
			while (!isFileDeleted){
				
				isFileDeleted = gameFile.delete();
				
			}
			
		}
		
	}
	
	private GameConfigSave getInstance() {
		
		GameConfigSave gameConfigSave = new GameConfigSave();
		
		gameConfigSave.setCurrentState((GameState)GameConfig.currentState);
		gameConfigSave.setDifficultyHandler((DifficultyHandler)GameConfig.difficultyHandler);
		gameConfigSave.setAudio(GameConfig.audio);
		gameConfigSave.setHeight(GameConfig.height);
		gameConfigSave.setWidth(GameConfig.width);
		gameConfigSave.setResources(GameConfig.resources);
		gameConfigSave.setHeightFactor(GameConfig.heightFactor);
		gameConfigSave.setWidthFactor(GameConfig.widthFactor);
		gameConfigSave.isGameOver(GameConfig.isGameOver);
		gameConfigSave.isGameWon(GameConfig.isGameWon);
		gameConfigSave.setScore(GameConfig.score);
		gameConfigSave.setCurrentBackgroundSound(GameConfig.currentBackgroundSound);
		
		return gameConfigSave;
		
	}
	
	private void restoreData() {
		
		GameConfig.currentState = this.gameConfigSave.getCurrentState();
		GameConfig.difficultyHandler = this.gameConfigSave.getDifficultyHandler();
		GameConfig.audio = this.gameConfigSave.getAudio();
		GameConfig.height = this.gameConfigSave.getHeight();
		GameConfig.width = this.gameConfigSave.getWidth();
		GameConfig.resources = this.gameConfigSave.getResources();
		GameConfig.heightFactor = this.gameConfigSave.getHeightFactor();
		GameConfig.widthFactor = this.gameConfigSave.getWidthFactor();
		GameConfig.isGameOver = this.gameConfigSave.getIsGameOver();
		GameConfig.isGameWon = this.gameConfigSave.getIsGameWon();
		GameConfig.score = this.gameConfigSave.getScore();
		GameConfig.currentBackgroundSound = this.gameConfigSave.getCurrentBackgroundSound();
		
	}
	
}
