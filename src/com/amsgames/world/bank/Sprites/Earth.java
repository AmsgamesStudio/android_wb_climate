package com.amsgames.world.bank.Sprites;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.amsgames.world.bank.GameConfig;
import com.amsgames.world.bank.Interfaces.ISpriteSelfAnimated;

/**
 * Handles the game's earth. 
 * 
 * @author Alejandro Mostajo (Amsgames), Jose Cruz (Amsgames) 
 *
 */
public class Earth implements ISpriteSelfAnimated, Serializable{

	// Self animated logic
	private long lastGameTime = 0l;
	private int animationTime = 1000 / 15; // 1 second / FPS
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3707288696558603085L;
	
	private final int TOTAL_DEGREES = 360;
	/**
	 * Sets the rotation speed of the earth.
	 */
	private final int ROTATION_SPEED = 10;
	/**
	 * Indicates the percentage of the bitmap that will be visible in the screen.
	 */
	private final float Y_FACTOR = 0.33f;
	/**
	 * X position of the object in the screen.
	 */
	private float mX;
	/**
	 * Y position of the object in the screen.
	 */
	private float mY;
	/**
	 * Contains the image resource of the earth.
	 */
	private Bitmap mBitmap;
	/**
	 * Rotation current degree.
	 */
	private float mCurrentDegree;

	/**
	 * Default constructor.
	 * 
	 * @param bitmap Contains the image resource of the earth.
	 * 
	 */
	public Earth (Bitmap bitmap) {
		
		// Assign resource
		mBitmap = bitmap;
		
		// Set rotation current degrees
		mCurrentDegree = 0;
		
		/* SET X AND Y */
		// Center of the screen minus half of the object's width
		mX = (GameConfig.width / 2) - (mBitmap.getWidth() / 2);
		// Bottom of the screen minus the percentage of height to show.
		mY = GameConfig.height - (mBitmap.getHeight() * Y_FACTOR);
		
	}
	
	public void update(long currentGameTime) {
		
		if (currentGameTime > lastGameTime + animationTime){
			lastGameTime = currentGameTime;
			
			/* Set animation degree speed*/
			mCurrentDegree = (mCurrentDegree + ROTATION_SPEED) % TOTAL_DEGREES;
			
		}
		
	}

	public void draw(Canvas canvas) {
		
        Matrix transformationMatrix = new Matrix();
        
        transformationMatrix.setTranslate(mX, mY);
        
        transformationMatrix.preRotate(mCurrentDegree, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
        
        canvas.drawBitmap(mBitmap, transformationMatrix, null);
        
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
