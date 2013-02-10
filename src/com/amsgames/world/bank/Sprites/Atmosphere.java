package com.amsgames.world.bank.Sprites;

import java.io.Serializable;

import android.graphics.Canvas;

import com.amsgames.world.bank.Interfaces.ISpriteSelfAnimated;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.amsgames.world.bank.GameConfig;
import com.amsgames.world.bank.GameUtility;

/**
 * Handles the atmosphere. 
 * 
 * @author Alejandro Mostajo (Amsgames), Jose Cruz (Amsgames)
 *
 */
public class Atmosphere implements ISpriteSelfAnimated, Serializable{


	// Self animated logic
	private long lastGameTime = 0l;
	private int animationTime = 2000; // It should wait 2 seconds before next animation
	/**
	 * 
	 */
	private static final long serialVersionUID = 5272242525251813119L;
	/**
	 * The quantity of sprite.
	 */
	private static final int SPRITE_QUANTITY = 4;
	/**
	 * Current frame to animate.
	 */
	private int mCurrentFrame;
	/**
	 * The height of an item.
	 */
	private int mHeight;
	/**
	 * The height bounds of an item. Used for collisions.
	 */
	private int mHeightBound;
	/**
	 * Contains the image resource of the atmosphere.
	 */
	private Bitmap mBitmap;
	/**
	 * The type of the vacuum.
	 */
	private int mVacuumType;

	/**
	 * Default constructor.
	 * 
	 * @param bitmap Contains the image resource of the atmosphere.
	 * 
	 */
	public Atmosphere (Bitmap bitmap) {
		// Assign resource
		mBitmap = bitmap;
		
		// Current frame
		mCurrentFrame = 0;
		mVacuumType = -1;
		
		/* --------- BEGIN HEIGHT and WIDTH ---------*/
		mHeight = mBitmap.getHeight() / SPRITE_QUANTITY;
		mHeightBound = (int)(mHeight * 0.72);
		/* --------- END HEIGHT and WIDTH ---------*/
	}
	
	public void update(long currentGameTime) {
		
		/* ANIMATION */
		if (currentGameTime > lastGameTime + animationTime){
			lastGameTime = currentGameTime;
			
			mCurrentFrame = ++mCurrentFrame % SPRITE_QUANTITY;
			
		}
		
	}

	public void draw(Canvas canvas) {
		
		Rect frame = GameUtility.createRect(0, mCurrentFrame * mHeight, mBitmap.getWidth(), (mCurrentFrame * mHeight) + mHeight);
		
		Rect location = GameUtility.createRect(0, 0, GameConfig.width, mHeight);
		
        canvas.drawBitmap(mBitmap, frame, location, null);
	}
	
	
	/**
	 * Indicates if the X and Y point passed as parameter has collided with the object.
	 * @param x X point
	 * @param y Y point
	 * @return True if there is a collision and false if not.
	 */
	public boolean hasCollision (float x, float y) {
		
		return x > 0 && x < 0 + GameConfig.width && y > 0 && y < 0 + mHeightBound;
		
	}

	public int getCollisionType() {
		return mVacuumType;
	}

	public int[] getCollisionXYEffect() {
		int[] xy = new int[2];
		xy[0] = GameConfig.width / 2;
		xy[1] = mHeight / 2;
		return xy;
	}

}
