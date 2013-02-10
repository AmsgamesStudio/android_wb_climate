package com.amsgames.world.bank.Interfaces;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.view.MotionEvent;



public interface IState {
	
	/**
	 * Aluminum can - Aerosol - Car pollution - Oil - Bulb - Trash can - Tree
	 */
	List<ISpriteAITouch> itemsCollection = new ArrayList<ISpriteAITouch>();
	
	/**
	 * Points - Text
	 */
	List<ISpriteSelfAnimated> pointsCollection = new ArrayList<ISpriteSelfAnimated>();
	
	/**
	 * Atmosphere - Vacuum 1 - Vacuum 2 - Vacuum 3 - Vacuum 4
	 */
	List<ISpriteSelfAnimated> contactItemsCollection = new ArrayList<ISpriteSelfAnimated>();
		
	/**
	 * Initializes all the sprite needed for the game
	 */
	void initialize();
	
	/**
	 * Handles all the inputs done by the user/player.
	 * @param event
	 */
	void input(MotionEvent event);
	
	/**
	 * Updates game physics and logic.
	 */
	void update();
	
	/**
	 * Draws every frame in the game.
	 */
	void draw(Canvas canvas);
	
}