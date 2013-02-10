package com.amsgames.world.bank.Sprites;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.amsgames.world.bank.Interfaces.ISpriteSelfAnimated;
import com.amsgames.world.bank.States.GameState;

/**
 * Point SPRITE.
 * 
 * @author Alejandro Mostajo (AmsGames)
 *
 */
public class Point implements ISpriteSelfAnimated, Serializable {

	/**
	 * Indicates the number of frames that will last the animation.
	 */
	private static final int FRAMES_TO_ANIMATE = 15;
	/**
	 * The scale growth per frame.
	 */
	private static final int SCALE_GROWTH_PER_FRAME = 1;
	/**
	 * Numeric value that represents the alpha as opaque.
	 */
	private static final int FULL_ALPHA = 255;
	/**
	 * Digit quantity plus the sign. 0123456789[+-]
	 */
	private static final int DIGIT_QUIATITY = 11;
	/**
	 * The quantity of backgrounds available. 
	 * Each background must have its own digit set.
	 */
	private static final int BACKGROUND_QUANTITY = 2;
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 2497217205352395015L;
	/**
	 * X position of the object in the screen.
	 */
	private int mX;
	/**
	 * Y position of the object in the screen.
	 */
	private int mY;
	/**
	 * Contains the image resource with the backgrounds.
	 */
	private Bitmap mBmpBackgrounds;
	/**
	 * Contains the image resource with the digits.
	 */
	private Bitmap mBmpDigits;
	/**
	 * The background's width.
	 */
	private int mBackgroundWidth;
	/**
	 * The background's height.
	 */
	private int mBackgroundHeight;
	/**
	 * The digit's width.
	 */
	private int mDigitWidth;
	/**
	 * The digit's height.
	 */
	private int mDigitHeight;
	/**
	 * Points to display (example: 2 or -5).
	 */
	private int mPoints;
	/**
	 * Indicates the current alpha or transparency to display.
	 * Initialized as 1 (100%) in the constructor.
	 */
	private double mCurrentAlpha;
	/**
	 * Used in order to make the sprint bigger trough time.
	 * Initialized as 0 for non-scale.
	 */
	private int mCurrentScale;
	/**
	 * Indicates the current frame in the animation.
	 */
	private int mCurrentFrame;
	
	/**
	 * Default constructor. X & Y must be passed as parameters.
	 * 
	 * @param bmpBackgrounds Contains the image resource with the backgrounds.
	 * @param bmpDigits Contains the image resource with the digits.
	 * @param points Points to display (example: 2 or -5).
	 * @param x Initial X position in the screen.
	 * @param y Initial Y position in the screen.
	 */
	public Point (
			Bitmap bmpBackgrounds,
			Bitmap bmpDigits,
			int points,
			int x,
			int y
	) {
		// Assign bitmaps
		mBmpBackgrounds = bmpBackgrounds;
		mBmpDigits = bmpDigits;
		
		// Assing points
		mPoints = points;
		
		// Alpha, Frame and Scale
		mCurrentAlpha = 1;
		mCurrentScale = -1;
		mCurrentFrame = 1;
		
		/* --------- BEGIN HEIGHT and WIDTH ---------*/
		mBackgroundWidth = (int) (mBmpBackgrounds.getWidth() / BACKGROUND_QUANTITY);
		mBackgroundHeight = (int) mBmpBackgrounds.getHeight();
		mDigitWidth = (int) (mBmpDigits.getWidth() / DIGIT_QUIATITY);
		mDigitHeight = (int) (mBmpDigits.getHeight() / BACKGROUND_QUANTITY);
		/* --------- END HEIGHT and WIDTH ---------*/
		
		/* --------- BEGIN X and Y ---------*/
		mX = x;
		mY = y;
		/* --------- END X and Y ---------*/
	}

	public void update(long currentDateTime) {
		
		if (mCurrentAlpha > 0) {
			/* ALPHA */
			mCurrentAlpha = 1 - ((double)mCurrentFrame / FRAMES_TO_ANIMATE);
			
			/* SCALE */
			mCurrentScale += mCurrentScale < 0 ? 1 : SCALE_GROWTH_PER_FRAME;
			
			mCurrentFrame++;
		} else {
			// Remove sprite
			GameState.pointsCollection.remove(this);
		}
	}

	public void draw(Canvas canvas) {
		// Set transparency (alpha)
		Paint paint = new Paint();
		paint.setAlpha((int)(FULL_ALPHA * mCurrentAlpha));
		
		// Negative points will use frame 0, positive will use frame 1
		int frameNumber = mPoints <= 0 ? 0 : 1;
		
		/* --------- BEGIN DRAW BACKGROUND ---------*/
		Rect frame = new Rect (
				frameNumber * mBackgroundWidth,
				0,
				(frameNumber * mBackgroundWidth) + mBackgroundWidth,
				mBackgroundHeight);
		Rect location = new Rect (
				mX - mCurrentScale,
				mY - mCurrentScale,
				mX + mBackgroundWidth + mCurrentScale,
				mY + mBackgroundHeight + mCurrentScale);
		canvas.drawBitmap(mBmpBackgrounds, frame, location, paint);
		/* --------- END DRAW BACKGROUND ---------*/
		
		/* --------- BEGIN DRAW NUMBER ---------*/
		// Get draw-able x position from right to left.
		// We are going to center the number in the background
		int digitY = ((mBackgroundHeight-mDigitHeight) / 2) + mY;
		int digitCount = String.valueOf(mPoints).replace("-","").length() + 1;
		int digitX = (mX + mBackgroundWidth) - (((mBackgroundWidth-(mDigitWidth*digitCount)) / 2) + mDigitWidth);
		if (mPoints == 0) {
			// Get digit frame
			frame = new Rect (
					0,
					frameNumber * mDigitHeight,
					mDigitWidth,
					(frameNumber * mDigitHeight) + mDigitHeight);
			// Get location and scale
			location = new Rect (
					digitX - mCurrentScale,
					digitY - mCurrentScale,
					digitX + mDigitWidth + mCurrentScale,
					digitY + mDigitHeight + mCurrentScale);
			// Draw FILL
			canvas.drawBitmap(mBmpDigits, frame, location, paint);
		} else {
			// Draw numbers
			int tempNumber = mPoints * (mPoints < 0 ? -1 : 1);
			while (tempNumber > 0) {
				int frameX = tempNumber % 10;
				// Get digit frame
				frame = new Rect (
						frameX * mDigitWidth,
						frameNumber * mDigitHeight,
						(frameX * mDigitWidth) + mDigitWidth,
						(frameNumber * mDigitHeight) + mDigitHeight);
				// Get location
				location = new Rect (
						digitX - mCurrentScale,
						digitY - mCurrentScale,
						digitX + mDigitWidth + mCurrentScale,
						digitY + mDigitHeight + mCurrentScale);
				// Draw digit
				canvas.drawBitmap(mBmpDigits, frame, location, paint);
				// Move to next digit
				tempNumber = tempNumber / 10;
				digitX -= mDigitWidth;
			}
			// Draw sign
			frame = new Rect (
					10 * mDigitWidth,
					frameNumber * mDigitHeight,
					(10 * mDigitWidth) + mDigitWidth,
					(frameNumber * mDigitHeight) + mDigitHeight);
			// Get location
			location = new Rect (
					digitX - mCurrentScale,
					digitY - mCurrentScale,
					digitX + mDigitWidth + mCurrentScale,
					digitY + mDigitHeight + mCurrentScale);
			// Draw digit
			canvas.drawBitmap(mBmpDigits, frame, location, paint);
		}
		/* --------- END DRAW NUMBER ---------*/
		
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
