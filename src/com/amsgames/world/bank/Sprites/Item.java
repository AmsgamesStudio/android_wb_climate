package com.amsgames.world.bank.Sprites;

import java.io.Serializable;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.amsgames.world.bank.GameConfig;
import com.amsgames.world.bank.Interfaces.ISpriteAITouch;
import com.amsgames.world.bank.States.GameState;

/**
 * Items
 * 
 * @author Alejandro Mostajo y Jose Cruz
 *
 */
public class Item implements ISpriteAITouch, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2746149539599032718L;
	/**
	 * The fog starts at the 60% of a half of the screen size.
	 */
	private static final double FOG_X_FACTOR = 0.6;
	/**
	 * Sucked speed
	 */
	private static double mSuckedSpeed = 1;
	/**
	 * The default speed of an object at the lowest difficulty.
	 */
	private double mSpeed = 5;
	/**
	 * The number of items in the game.
	 */
	private static final int ITEMS_QUANTITY = 7;
	/**
	 * The type of a vacuum.
	 */
	public int VacuumType;
	/**
	 * The width of an item.
	 */
	public long Width;
	/**
	 * The height of an item.
	 */
	public long Height;
	/**
	 * Contains the image resource of the item.
	 */
	private Bitmap mBitmap;
	/**
	 * Set the frame of the item in the screen.
	 * Set as variable since the object will not have frame animation but will move.
	 */
	private Rect mFrame;
	/**
	 * X position of the object in the screen.
	 */
	public long X;
	/**
	 * Y position of the object in the screen.
	 */
	public long Y;
	/**
	 * X position for the suck effect.
	 */
	public long mSuckedX;
	/**
	 * Y position for the suck effect.
	 */
	public long mSuckedY;
	/**
	 * Scale effect.
	 */
	public int mScale = 0;
	
	private boolean isTouched = false;
	private boolean hasCollided = false;
	
	/**
	 * Default constructor.
	 * 
	 * @param bitmap Contains the image resource of the item. 
	 * 
	 */
	public Item (Bitmap bitmap) {
				
		// Assign type
		VacuumType = new Random().nextInt(ITEMS_QUANTITY);
		// Assign resource
		mBitmap = bitmap;
		
		/* --------- BEGIN HEIGHT and WIDTH ---------*/
		Width = mBitmap.getWidth() / ITEMS_QUANTITY;
		Height = mBitmap.getHeight();
		/* --------- END HEIGHT and WIDTH ---------*/
		
		/* --------- BEGIN X and Y ---------*/
		// Starts at the bottoms of the screen;
		Y = GameConfig.height;
		
		// The fogFirstItemX represents the first draw-able X position within the fog
		int fogFirstItemX = (int) ( (GameConfig.width / 2) * FOG_X_FACTOR);
		// Fog Width factor determines half of the width of a fog.
		int fogWidthFactor = (int) ((GameConfig.width / 2) - fogFirstItemX);
		// the fogFinalItemX represents the last draw-able X position within the fog
		int fogLastItemX = (int) (((GameConfig.width / 2) + fogWidthFactor) - Width);
		// Random N represents the amount of distance between the initial and final X. 
		X = new Random().nextInt(fogLastItemX - fogFirstItemX) + fogFirstItemX;
		/* --------- END X and Y ---------*/
		
		/* --------- BEGIN Difficulty ---------*/
		mSpeed = GameConfig.difficultyHandler.getCurrentRoundSettings().getObjectSpeed() * GameConfig.heightFactor;
		/* --------- END Difficulty ---------*/
		
		// Set frame
		int frameX = (int) (VacuumType * Width);
		mFrame = new Rect(
				frameX,
				0,
				(int) (frameX + Width),
				(int) Height);
	}
	
	public void update(float x, float y) {
		
		if (isTouched) {
			
			Log.d("Item","Update touched");
			// Center touch
			X = (int)x - Width/2;
			Y = (int)y - Height/2;
			
		}
		
	}

	public void update() {
	
		// Self animate if it is not touched.
		if (!hasCollided && !isTouched) {
			// Normal move behavior, from bottom to top
			Y -= mSpeed;
			
		} else if (hasCollided) {
			mScale++;
			 
			if (X != mSuckedX) {
				int sign = mSuckedX<X ? -1 : 1;
				X += mSuckedSpeed * sign;
			} 
			if (Y != mSuckedY) {
				int sign = mSuckedY<Y ? -1 : 1;
				Y += mSuckedSpeed * sign;
			}
			if ((Height-mScale <= 0) || (X == mSuckedX && Y == mSuckedY))
				GameState.itemsCollection.remove(this);
		}
		
		if (X < 0 || X > GameConfig.width || Y < 0) { 
			GameState.itemsCollection.remove(this);
		}
		
	}
	
	public void draw(Canvas canvas) {
		Rect location = new Rect(
				// Initial position X
				(int) X + mScale,
				// Initial position Y
				(int) Y + mScale,
				// Final position X
				(int) (X + Width) - mScale,
				// Final position Y
				(int) (Y + Height) - mScale);
		
        canvas.drawBitmap(mBitmap, mFrame, location, null);
	}

	public boolean isBeingTouched(float x, float y) {
		
		return x > X && x < X + Width && y > Y && y < Y + Height;
		
	}

	public void setIsTouched(boolean isTouched) {
		if (!this.hasCollided) {
			this.isTouched = isTouched;
		}
		
	}
	
	public boolean getIsTouched() {
		
		return this.isTouched;
		
	}

	public boolean hasCollided() {
		return hasCollided;
	}

	/**
	 * Set the item collision and a sucking X and Y.
	 * @param suckedXY Sucked X and Y position.
	 */
	public void setHasCollided(int[] suckedXY) {
		mSuckedX = suckedXY[0];
		mSuckedY = suckedXY[1];
		mScale = 2;
		this.hasCollided = true;
		// If collision occurred then no longer is touched.
		this.isTouched = false;
	}

}
