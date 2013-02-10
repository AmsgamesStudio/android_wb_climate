package com.amsgames.world.bank.Sprites;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.amsgames.world.bank.GameConfig;
import com.amsgames.world.bank.Interfaces.ISpriteSelfAnimated;

/**
 * Handles the precipitation fog. 
 * 
 * @author Alejandro Mostajo
 *
 */
public class Fog implements ISpriteSelfAnimated, Serializable{

	// Self animated logic
	private long lastGameTime = 0l;
	private int animationTime = 1000 / 10; // 1 second / FPS
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9114016815512730634L;
	
	private int mSpeed = 10;
	/**
	 * Full opaque value.
	 */
	private final int FULL_ALPHA = 255;
	/**
	 * The precipitation value that will determine an opaque alpha.
	 */
	private final int MAX_PRECIPITATION = 2700;
	/**
	 * The current alpha assigned.
	 */
	private double mCurrentAlpha;
	/**
	 * Contains the image resource of the earth.
	 */
	private Bitmap mBitmap;
	/**
	 * X position of the object in the screen.
	 */
	private float mX;
	/**
	 * Quantity of fog parts
	 */
	private int mFogQuantity;
	/**
	 * Indicates the index of the initial fog.
	 */
	private int mFirstFogIndex;
	/**
	 * Array with the list of Y variables, one per fog part.
	 */
	private int[] mY;
	
	/**
	 * Default constructor.
	 * 
	 * @param bitmap Contains the image resource of the fog.
	 * 
	 */
	public Fog (Bitmap bitmap) {
		
		/* --------- SCREEN CONFIG ---------*/
		mSpeed = (int) (mSpeed * GameConfig.widthFactor);
		/* --------- SCREEN CONFIG ---------*/
		
		// Assign resource
		mBitmap = bitmap;
		
		// Default alpha.
		mCurrentAlpha = GameConfig.difficultyHandler.getCurrentRoundSettings().Precipitation / MAX_PRECIPITATION;
		
		/* SET X AND Y */
		// Center of the screen minus half of the object's width
		mX = (GameConfig.width / 2) - (mBitmap.getWidth() / 2);
		
		/* Calculate the number of fog parts to show */
		mFogQuantity = (int) GameConfig.height / mBitmap.getHeight();
		mFogQuantity += 2;
		
		/* Initialize Y fog parts */
		mY = new int[mFogQuantity];
		for (int fogPart = 0; fogPart < mFogQuantity; fogPart++) {
			mY[fogPart] = GameConfig.height - (fogPart * mBitmap.getHeight());
		}
		mFirstFogIndex = 0;
	
	}
	
	public void update(long currentGameTime) {
		
		/* ANIMATION */
		if (currentGameTime > lastGameTime + animationTime){
			lastGameTime = currentGameTime;
		
			/* Move fog y parts */
			for (int fogPart = 0; fogPart < mFogQuantity; fogPart++) {
				
				// Check if the fog is out of the screen in order to move it as the first fog part
				if ((mY[fogPart] - mSpeed) <= -mBitmap.getHeight()) {
					mFirstFogIndex = fogPart;
				} 
				
				// Normal animation
				mY[fogPart] -= mSpeed;
			}
			// Update out of screen fog part
			int nextFogIndex = (mFirstFogIndex+1) % mFogQuantity;
			mY[mFirstFogIndex] = mY[nextFogIndex] + mBitmap.getHeight();
			
		}
		
		/* ALPHA */
		mCurrentAlpha = GameConfig.difficultyHandler.getCurrentRoundSettings().Precipitation / MAX_PRECIPITATION;
		mCurrentAlpha = mCurrentAlpha > 1 ? 1 : mCurrentAlpha;
		
	}

	public void draw(Canvas canvas) {
		// Set alpha
		Paint paint = new Paint();
		paint.setAlpha((int)(FULL_ALPHA * mCurrentAlpha));
		
		/* Animate fog parts */
		for (int y : mY) {
	        // Draw part
	        canvas.drawBitmap(mBitmap, (int)mX, y, paint);
		}
	}

	public boolean hasCollision(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getCollisionType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int[] getCollisionXYEffect() {
		// TODO Auto-generated method stub
		return null;
	}

}
