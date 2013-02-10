package com.amsgames.world.bank.States;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

import com.amsgames.world.bank.GameAudio;
import com.amsgames.world.bank.GameConfig;
import com.amsgames.world.bank.R;
import com.amsgames.world.bank.Interfaces.ISprite;
import com.amsgames.world.bank.Interfaces.ISpriteAITouch;
import com.amsgames.world.bank.Interfaces.ISpriteSelfAnimated;
import com.amsgames.world.bank.Interfaces.ISpriteStatic;
import com.amsgames.world.bank.Interfaces.IState;
import com.amsgames.world.bank.Sprites.Atmosphere;
import com.amsgames.world.bank.Sprites.Background;
import com.amsgames.world.bank.Sprites.Earth;
import com.amsgames.world.bank.Sprites.Fog;
import com.amsgames.world.bank.Sprites.Item;
import com.amsgames.world.bank.Sprites.Point;
import com.amsgames.world.bank.Sprites.Score;
import com.amsgames.world.bank.Sprites.Text;
import com.amsgames.world.bank.Sprites.Thermometer;
import com.amsgames.world.bank.Sprites.Vacuum;

/**
 * This class represents the game state. It is the core of the game.
 * It includes the following methods:
 * Initialize - Input - Update - Draw -
 * 
 * @author Jose Cruz (Amsgames)
 *
 */

public class GameState implements IState, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4227968239397799919L;
	
	/* --------- Private fields --------- */
	
	private ISpriteStatic background;
	private ISpriteSelfAnimated fog;
	private ISprite thermometer;
	private ISpriteStatic score;
	private ISpriteSelfAnimated earth;
	/**
	 * Flag that indicates if an Item was grabbed.
	 */
	private boolean itemGrabbed;
	/**
	 * Flag that indicates if the game needs to be finished.
	 */
	private boolean finishGame;
	/**
	 * Points to full fill the thermometer. 
	 */
	private final int MAXSCORE = 50;
	/**
	 * Thermometer min score initializer.
	 */
	private final int MINSCORE = 0;
	/**
	 * Current points that determine the fill of the thermometer.
	 */
	private int currentThermometerScore;
	/**
	 * Total items thrown per round.
	 */
	private int totalItemsThrown;
	/**
	 * Stores the last time an item was thrown
	 */
	private long nextGameTimeItemThrown = 0l;
	/**
	 * Indicates if a fact has been shown for the round.
	 */
	private boolean roundFactShown = false;
	

	/* --------- IState implementation --------- */
	
	/**
	 * Default constructor. Will initialize all dependable SPRITES.
	 */
	public GameState () {
		
		initialize();
	}
	
	/**
	 * Initializes all the SPRITES.
	 */
	public void initialize() {
				
		// Initialize is called once so lists should be clean
		background = null;
		fog = null;
		thermometer = null;
		score = null;
		earth = null;
		finishGame = false;
		pointsCollection.clear();
		itemsCollection.clear();
		contactItemsCollection.clear();
		totalItemsThrown = 0;
		nextGameTimeItemThrown = 0;
		
		// SET FIRST ROUND SETTINGS
		GameConfig.difficultyHandler.getFirstRoundSettings();
		GameConfig.isGameOver = false;
		GameConfig.isGameWon = false;
		
		// Background
		background = new Background(getBitmap(R.drawable.sky_bg));
		
		// Fog
		fog = new Fog(getBitmap(R.drawable.fog));
		
		// Add atmosphere to contactItemsCollection
		contactItemsCollection.add(new Atmosphere(getBitmap(R.drawable.space)));
		
		// Add Earth to contactItemsCollection
		earth = new Earth(getBitmap(R.drawable.earth));
		
		// Add vacuums to contactItemsCollection
		for (int vacuumType = 0; vacuumType < 4; vacuumType++) {
			contactItemsCollection.add(new Vacuum(getBitmap(R.drawable.vacuums), vacuumType));
		}
		
		// Add thermometer
		thermometer = new Thermometer(getBitmap(R.drawable.thermometer), MAXSCORE, MINSCORE);
		
		// Add score
		score = new Score(getBitmap(R.drawable.digits), getBitmap(R.drawable.text), 0);
		
		// Add first round text and set first round settings.
		pointsCollection.add(
				new Text(
						getBitmap(R.drawable.modal_bg), 
						getBitmap(R.drawable.dr_climate), 
						"YEARS: " + GameConfig.difficultyHandler.getCurrentRoundSettings().YearRange  
							+ " Temperature: " + String.valueOf(GameConfig.difficultyHandler.getCurrentRoundSettings().Temperature) 
							+ "° Precipitation: " + String.valueOf(GameConfig.difficultyHandler.getCurrentRoundSettings().Precipitation)
						)
				);
		
		itemsCollection.add(new Item(getBitmap(R.drawable.objects)));
		
	}
	
	public void input(MotionEvent event) {
		
		int eventAction = event.getAction();
		float x = event.getX();
		float y = event.getY();
		
		switch (eventAction) {
		
		case MotionEvent.ACTION_DOWN:
			
			if (itemGrabbed == false){
				
				// Check if the user grabbed an item
				for (ISpriteAITouch sprite : itemsCollection){
					
					if (!sprite.hasCollided() && sprite.isBeingTouched(x, y)){
						
						itemGrabbed = true;
						sprite.setIsTouched(true);
						sprite.update(x, y);
						handleCollision((Item)sprite);
						break;
						
					}
					
				}
				
			}
			
			break;

		case MotionEvent.ACTION_MOVE:
			
			if (itemGrabbed){
				
				for (ISpriteAITouch sprite : itemsCollection){
					
					if (!sprite.hasCollided() && sprite.getIsTouched()){
						
						sprite.update(x, y);
						handleCollision((Item)sprite);
						break;
						
					}
					
				}
				
			} else {

				// Check if the user grabbed an item
				for (ISpriteAITouch sprite : itemsCollection){
					
					if (!sprite.hasCollided() && sprite.isBeingTouched(x, y)){
						
						itemGrabbed = true;
						sprite.setIsTouched(true);
						sprite.update(x, y);
						handleCollision((Item)sprite);
						break;
						
					}
					
				}
			}
				
			break;
			
		case MotionEvent.ACTION_UP:
			
			if (itemGrabbed){
				
				for (ISpriteAITouch sprite : itemsCollection){
					
					if (sprite.getIsTouched()){
						
						itemGrabbed = false;
						sprite.setIsTouched(false);
						sprite.update(x, y);
						handleCollision((Item)sprite);
						break;
						
					}
					
				}
				
			}
			
			break;
			
		}
		
	}

	public void update() {
		
		/* ---------- GAME LOGIC ---------- */
		// (1) Add new item
		if (!finishGame
				&& System.currentTimeMillis() > nextGameTimeItemThrown 
		) {
			// Add item to list
			itemsCollection.add(new Item(getBitmap(R.drawable.objects)));
			// Update counters
			nextGameTimeItemThrown = (long) (System.currentTimeMillis() 
					+ GameConfig.difficultyHandler.getCurrentRoundSettings().getFrequency());
			totalItemsThrown++;
		}
		// (2) Show fact
		if (!roundFactShown
				&& totalItemsThrown >= GameConfig.difficultyHandler.getCurrentRoundSettings().ObjectQuantity / 2
				&& !GameConfig.difficultyHandler.getCurrentRoundSettings().FactMessage.equals("")
		) {

			// Show message with round change
			pointsCollection.add(
					new Text(
							getBitmap(R.drawable.modal_bg), 
							getBitmap(R.drawable.dr_climate), 
							GameConfig.difficultyHandler.getCurrentRoundSettings().FactMessage
							)
					);
			roundFactShown = true;
		}
		/* ---------- GAME LOGIC ---------- */
		
		// Update fog
		if (this.fog != null)
			this.fog.update(System.currentTimeMillis());
		
		// Update earth
		if (this.earth != null)
			this.earth.update(System.currentTimeMillis());
		
		// Update hybrid sprite (Artificial Intelligence and Input)
		for (int location = itemsCollection.size()-1; 0 <= location; location--){
			
			if (!itemsCollection.get(location).getIsTouched()){

				handleCollision((Item)itemsCollection.get(location));
				itemsCollection.get(location).update();
			
			}
			
		}
		
		// Update all artificial intelligence based sprite
		for (int location = pointsCollection.size()-1; 0 <= location; location--){
			
			pointsCollection.get(location).update(System.currentTimeMillis());
			
		}
		
		// Update all self animated based sprite
		for (ISpriteSelfAnimated sprite : contactItemsCollection){
			
			sprite.update(System.currentTimeMillis());
			
		}
		
		// Update score 
		if (this.score != null){
			
			// Comes from collisions
			((Score)this.score).setScore(GameConfig.score);
		
		}
		
		// GameOver and GameWon detection
		// Comes from collisions
		if (currentThermometerScore > MAXSCORE) {
			
			GameConfig.isGameOver = true;
			
		}
		// Get next round and check if win
		else if (totalItemsThrown > GameConfig.difficultyHandler.getCurrentRoundSettings().ObjectQuantity) {
			int currentDifficultyLevel = GameConfig.difficultyHandler.getCurrentRoundSettings().DifficultyLevel;
			// Set to finish the game and stop throwing more objects
			// Here we are changing the round as well, this must be called just ONCE.
			if (finishGame || GameConfig.difficultyHandler.getNextRoundSettings() == null) {
				finishGame = true;
			} else {
				totalItemsThrown = 0;
				roundFactShown = false;
			}
			
			// If the game is set to finish and there are no more items in stage, then the game is won
			if (finishGame && itemsCollection.size() == 0
			) {
				
				GameConfig.isGameWon = true;
				
			} else if (!finishGame) {
			
				// Show message with round change
				pointsCollection.add(
						new Text(
								getBitmap(R.drawable.modal_bg), 
								getBitmap(R.drawable.dr_climate), 
								"YEARS: " + GameConfig.difficultyHandler.getCurrentRoundSettings().YearRange  
									+ " Temperature: " + String.valueOf(GameConfig.difficultyHandler.getCurrentRoundSettings().Temperature) 
									+ "° Precipitation: " + String.valueOf(GameConfig.difficultyHandler.getCurrentRoundSettings().Precipitation)
								)
						);
			}
			
			// Change background music
			if (currentDifficultyLevel != GameConfig.difficultyHandler.getCurrentRoundSettings().DifficultyLevel) {
				GameAudio.stopBackground();
				switch (GameConfig.difficultyHandler.getCurrentRoundSettings().DifficultyLevel) {
				case 1:
					GameAudio.playBackground(GameAudio.THEME_EASY);
					break;
				case 2:
					GameAudio.playBackground(GameAudio.THEME_NORMAL);
					break;
				default:
					GameAudio.playBackground(GameAudio.THEME_HARD);
					break;
				}
			}
			
		}
		
		// Update thermometer after updating scores
		if (this.thermometer != null) {
						
			((Thermometer)this.thermometer).setFillPercentage(MAXSCORE, currentThermometerScore);
			this.thermometer.update();
			
		}
		
	}

	public void draw(Canvas canvas) {

		canvas.drawColor(Color.BLACK);
		
		// Static sprite
		if (this.background != null)
			this.background.Draw(canvas);
		
		// Update fog
		if (this.fog != null)
			this.fog.draw(canvas);

		// Input sprite are drawn first (on top of the background and the fog)
		for (int location = itemsCollection.size()-1; 0 <= location; location--){
			
			itemsCollection.get(location).draw(canvas);
			
		}
		
		// Thermometer is drawn before the world
		if (this.thermometer != null)
			this.thermometer.draw(canvas);
		
		// Draw earth
		if (this.earth != null)
			this.earth.draw(canvas);
		
		
		// Self animated sprite are drawn second (on top of the items)
		for (ISpriteSelfAnimated sprite : contactItemsCollection){
			
			sprite.draw(canvas);
			
		}
		
		// Score is drawn after all
		if (this.score != null)
			this.score.Draw(canvas);
		
		// Artificial Intelligence sprite are drawn third (on top of everything)
		for (int location = pointsCollection.size()-1; 0 <= location; location--){
			
			pointsCollection.get(location).draw(canvas);	
			
		}
	
	}
	
	/* --------- Utility methods --------- */
	
	/**
	 * Returns a bitmap from a resource id.
	 * @param resourceId Resource ID.
	 * @return bitmap.
	 */
	private Bitmap getBitmap (int resourceId) {
		
		return BitmapFactory.decodeResource(GameConfig.resources, resourceId);
		
	}
	
	/**
	 * Checks if an item collided with a contact item and applies game rules.
	 *  
	 * @param item Item to check.
	 */
	private void handleCollision(Item item) {
		if (!item.hasCollided()) {
			for (ISpriteSelfAnimated vacuum : contactItemsCollection) {

				// Check for the borders of the object
				if (vacuum.hasCollision(item.X, item.Y)
						|| vacuum.hasCollision(item.X+item.Width, item.Y)
						|| vacuum.hasCollision(item.X, item.Y+item.Height)
						|| vacuum.hasCollision(item.X+item.Width, item.Y+item.Height)) {
					int pointsToAdd = 0;
					// Check for game rules
					// Non-Friendly object into correct vacuum
					if (item.VacuumType == vacuum.getCollisionType()) {
						
						GameAudio.playFX(GameAudio.VACUUM);
						if (item.VacuumType == 0)
							GameAudio.playFX(GameAudio.COLLISION_ONE);
						else if (item.VacuumType == 1)
							GameAudio.playFX(GameAudio.COLLISION_TWO);
						else if (item.VacuumType == 2)
							GameAudio.playFX(GameAudio.COLLISION_THREE);
						else if	(item.VacuumType == 3)
							GameAudio.playFX(GameAudio.COLLISION_FOUR);
						
						pointsToAdd = 2;

						// Friendly object into atmosphere
					} else if (item.VacuumType > 3
							&& vacuum.getCollisionType() == -1) {
						
						GameAudio.playFX(GameAudio.COLLISION_CORRECT);
						pointsToAdd = 5;

						// Non-Friendly object into atmosphere
					} else if (vacuum.getCollisionType() == -1) {
						
						GameAudio.playFX(GameAudio.COLLISION_WRONG);
						pointsToAdd = -7;

						// Friendly object into vacuum
					} else if (item.VacuumType > 3
							&& vacuum.getCollisionType() > -1) {
						
						GameAudio.playFX(GameAudio.VACUUM);
						GameAudio.playFX(GameAudio.COLLISION_WRONG);
						pointsToAdd = -10;

						// Non-Friendly object into wrong vacuum
					} else if (item.VacuumType != vacuum.getCollisionType()) {
						
						GameAudio.playFX(GameAudio.VACUUM);
						GameAudio.playFX(GameAudio.COLLISION_WRONG);
						pointsToAdd = -4;
						
					}
					
					// Un-grab
					if (item.getIsTouched()) {
						itemGrabbed = false;
					}
					// Set collision in item
					item.setHasCollided(vacuum.getCollisionXYEffect());

					// Add points to score and thermometer
					GameConfig.score += pointsToAdd;
					GameConfig.score = GameConfig.score < 0 ? 0 : GameConfig.score;
					currentThermometerScore += pointsToAdd * -1;
					currentThermometerScore = currentThermometerScore < 0 ? 0 : currentThermometerScore;
					
					// Add point sprite
					pointsCollection.add(
							new Point(
									getBitmap(R.drawable.points_bg), 
									getBitmap(R.drawable.points_digits), 
									pointsToAdd, 
									(int)(item.X), 
									(int)(item.Y) 
									)
							);
					
				// Check for screen borders collision
				} else if (item.X < 0) {
					item.X = 0;
				} else if (item.X > GameConfig.width - item.Width) {
					item.X = GameConfig.width - item.Width;
				}

			}
		}
	}
	
}
