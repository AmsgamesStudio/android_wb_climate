package com.amsgames.world.bank.Sprites;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.amsgames.world.bank.GameConfig;
import com.amsgames.world.bank.GameUtility;
import com.amsgames.world.bank.Interfaces.ISprite;

/**
 * Thermometer and its fill.
 * 
 * @author Alejandro Mostajo (Amsgames), Jose Cruz (Amsgames)
 *
 */
public class Thermometer implements ISprite, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3582241327692115536L;
	
	private final int FILL_MARGIN = 2;
	private final double THERMOMETER_MARGIN = 0.031;
	/**
	 * Number of sprite in the resource.
	 */
	private final int SPRITE_QUANTITY = 2;
	/**
	 * X position of the thermometer in the screen.
	 */
	private int mThermometerX;
	/**
	 * Y position of the thermometer in the screen.
	 */
	private int mThermometerY;
	/**
	 * X position of the thermometer's fill in the screen.
	 */
	private int mFillX;
	/**
	 * Bottom Y position of the thermometer's fill in the screen.
	 */
	private int mFillBottomY;
	/**
	 * The width of a thermometer.
	 */
	private int mWidth;
	/**
	 * The height of a thermometer.
	 */
	private int mHeight;
	/**
	 * The max height of a thermometer's fill.
	 */
	private int mFillMaxHeight;
	/**
	 * The height of a thermometer's fill.
	 */
	private int mFillHeight;
	/**
	 * The percentage to fill in the thermometer.
	 */
	private float mFillPercentage;
	/**
	 * Contains the image resource of the thermometer.
	 */
	private Bitmap mBitmap;
	
	/**
	 * Default constructor.
	 * 
	 * @param bitmap The image resource of the thermometer.
	 * @param pointsFillFull The amount of points needed in order to full fill the thermometer.
	 * @param pointsFillCurrent The amount of current points set to fill in the thermometer.
	 */
	public Thermometer (Bitmap bitmap, float pointsFillFull, float pointsFillCurrent) {
		// Assign resource
		mBitmap = bitmap;
		
		// Assign points
		setFillPercentage(pointsFillFull, pointsFillCurrent);
		
		/* --------- BEGIN HEIGHT and WIDTH ---------*/
		mWidth = (int) (mBitmap.getWidth() / SPRITE_QUANTITY);
		mHeight = (int) mBitmap.getHeight();
		/* --------- END HEIGHT and WIDTH ---------*/
		

		/* --------- BEGIN X and Y ---------*/
		mThermometerX = (int) (GameConfig.width * THERMOMETER_MARGIN);
		mThermometerY = (int) (GameConfig.height - ((GameConfig.height * (THERMOMETER_MARGIN * 4)) + mHeight));
		
		mFillX = mThermometerX;
		mFillBottomY = (mThermometerY + mHeight) - FILL_MARGIN;
		
		mFillMaxHeight = mHeight - (FILL_MARGIN * 2);
		mFillHeight = (int) (mFillMaxHeight * mFillPercentage);
		/* --------- END X and Y ---------*/
		
	}
	
	public void setFillPercentage(float pointsFillFull, float pointsFillCurrent){
		
		float result = pointsFillCurrent / pointsFillFull;
		
		mFillPercentage = result > 1 ? 1 : result < 0 ? 0 : result;
		
	}
	
	public void update() {
		
		/* Calculate the fill to display*/
		mFillHeight = (int) (mFillMaxHeight * mFillPercentage);
		
	}

	public void draw(Canvas canvas) {
		
		/* --------- FILL ---------*/
		// Get fill frame
		Rect frame = GameUtility.createRect(mWidth, 0, mWidth * 2, mFillHeight);
		
		// Get location and scale
		Rect location = GameUtility.createRect(mFillX, mFillBottomY - mFillHeight, mFillX + mWidth, mFillBottomY);
		
		// Draw FILL
		canvas.drawBitmap(mBitmap, frame, location, null);
		/* --------- FILL ---------*/
		
		/* --------- THERMOMETER ---------*/
		// Get thermometer frame
		frame = GameUtility.createRect(0, 0, mWidth, mHeight);
		
		// Get location
		location = GameUtility.createRect(mThermometerX, mThermometerY, mThermometerX + mWidth, mThermometerY + mHeight);
		
		// Draw THERMOMETER
		canvas.drawBitmap(mBitmap, frame, location, null);
		/* --------- THERMOMETER ---------*/
		
	}

}
