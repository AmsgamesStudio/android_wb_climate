package com.amsgames.world.bank.Sprites;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.amsgames.world.bank.GameConfig;
import com.amsgames.world.bank.GamePanel;
import com.amsgames.world.bank.GameUtility;
import com.amsgames.world.bank.Interfaces.ISprite;
import com.amsgames.world.bank.Interfaces.ISpriteSelfAnimated;

/**
 * Handles an in-game vacuum. 
 * Generic class for all the vacuums available in the game.
 * 
 * @author Alejandro Mostajo (Amsgames), Jose Cruz (Amsgames)
 *
 */
public class Vacuum implements ISpriteSelfAnimated, Serializable{
	
	// Self animated logic
	private long lastGameTime = 0l;
	private int animationTime = 1000 / 15; // 1 second / FPS
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4466230081462240834L;
	
	/**
	 * The number of vacuums in the game.
	 */
	private final int VACUUMS_QUANTITY = 4;
	/**
	 * The number of animated frames in a vacuum.
	 */
	private final int VACUUM_ANIMATIONS = 6;
	/**
	 * Factor to apply to the {@link GamePanel}'s height in order to obtain a Y position in the screen.
	 */
	private final double SECOND_LEVEL_Y_FACTOR = 0.47;

	/**
	 * Factor to apply to the {@link GamePanel}'s height in order to obtain a Y position in the screen.
	 */
	private final double FIRST_LEVEL_Y_FACTOR = 0.205;

	List<ISprite> items = new ArrayList<ISprite>();

	/* ---------  Private fields ---------*/
	
	/**
	 * X position of the object in the screen.
	 */
	private long mX;
	/**
	 * X position of the object in the screen. For collision purposes.
	 */
	private long mXBound;
	/**
	 * Y position of the object in the screen.
	 */
	private long mY;
	/**
	 * The width of a vacuum.
	 */
	private long mWidth;
	/**
	 * The height of a vacuum.
	 */
	private long mHeight;
	/**
	 * The width bound of a vacuum. For collision purposes.
	 */
	private long mWidthBound;
	/**
	 * The type of the vacuum.
	 */
	private int mVacuumType;
	/**
	 * Contains the image resource of the vacuum.
	 */
	private Bitmap mBitmap;
	/**
	 * Indicates the current animation frame.
	 */
	private int mCurrentFrame;
	/**
	 * Set the location of the vacuum in the screen.
	 * Set as variable since the object will not move.
	 */
	private Rect mLocation;

	/**
	 * Default constructor.
	 * 
	 * @param bitmap Contains the image resource of the vacuum. 
	 * @param vacuumType The type of vacuum to create.
	 * 
	 */
	public Vacuum (Bitmap bitmap, int vacuumType) {
		// Assign type
		mVacuumType = vacuumType;
		// Assign resource
		mBitmap = bitmap;
		
		/* --------- BEGIN HEIGHT and WIDTH ---------*/
		mWidth = mBitmap.getWidth() / VACUUMS_QUANTITY;
		mHeight = mBitmap.getHeight() / VACUUM_ANIMATIONS;
		mWidthBound = (int)(mWidth * 0.8);
		/* --------- END HEIGHT and WIDTH ---------*/

		/* --------- BEGIN X and Y ---------*/
		
		// Set Y
		switch(mVacuumType) {
		case 0:
		case 2:
			// First level vacuums
			mY = (long) (GameConfig.height * FIRST_LEVEL_Y_FACTOR);
			break;
		default:
			// Second level vacuums
			mY = (long) (GameConfig.height * SECOND_LEVEL_Y_FACTOR);
			break;
		}
		
		// Set X
		switch(mVacuumType) {
		case 0:
		case 1:
			// Left vacuums should be at the left screen border
			mX = 0;
			mXBound = mX;
			break;
		default:
			// Right vacuums should be at the right screen border
			// The position should be the screen width minus the width of a vacuum
			mX = (long) (GameConfig.width - (mBitmap.getWidth() / VACUUMS_QUANTITY));
			mXBound = (mX+mWidth) - mWidthBound;
			break;
		}
		
		/* --------- END X and Y ---------*/
		
		
		// Set location in the screen
		mLocation = GameUtility.createRect((int)mX, (int)mY, (int)(mX + mWidth), (int)(mY + mHeight));
		
	}

	public void update(long currentGameTime) {
		
		if (currentGameTime > lastGameTime + animationTime){
			lastGameTime = currentGameTime;

			/* Animation - update current frame*/
			mCurrentFrame = ++mCurrentFrame % VACUUM_ANIMATIONS;
			
		}
		
	}

	public void draw(Canvas canvas) {
		
		// Animate
		int frameX = (int)(mVacuumType * mWidth);
        int frameY = (int)(mCurrentFrame * mHeight);
        
		Rect frame = GameUtility.createRect(frameX, frameY, frameX + (int)mWidth, frameY + (int)mHeight);
		
        canvas.drawBitmap(mBitmap, frame, mLocation, null);
        
	}
	
	/**
	 * Indicates if the X and Y point passed as parameter has collided with the object.
	 * @param x X point
	 * @param y Y point
	 * @return True if there is a collision and false if not.
	 */
	public boolean hasCollision (float x, float y) {
		
		return x > mXBound && x < mXBound + mWidthBound && y > mY && y < mY + mHeight;
		
	}

	public int getCollisionType() {
		return mVacuumType;
	}

	public int[] getCollisionXYEffect() {
		int[] xy = new int[2];
		xy[0] = (int) (mX + mWidth / 2);
		xy[1] = (int) (mY + mHeight / 2);
		return xy;
	}

}
