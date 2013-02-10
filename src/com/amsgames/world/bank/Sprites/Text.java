package com.amsgames.world.bank.Sprites;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.amsgames.world.bank.GameConfig;
import com.amsgames.world.bank.GameUtility;
import com.amsgames.world.bank.Interfaces.ISpriteSelfAnimated;
import com.amsgames.world.bank.States.GameState;

/**
 * Text modal sprite.
 * 
 * @author Alejandro Mostajo (Amsgames), Jose Cruz (Amsgames)
 *
 */
public class Text implements ISpriteSelfAnimated, Serializable {

	private static final long serialVersionUID = -3171938681724700870L;
	
	// @TODO Add the mdpi resolutions as statics in the GameConfig.
	// @TODO Add the fps as static in the GameConfig.
	private long lastGameTime = 0l;
	
	
	/**
	 * Background border.
	 */
	private final double BACKGROUND_BORDER = 0.21;
	/**
	 * Background alpha.
	 */
	private final double BACKGROUND_ALPHA = 0.95;
	/**
	 * The percentage of the background's width that is writable.
	 */
	private final double PERCENTAGE_WRITABLE = 0.67;
	/**
	 * Frame to wait or to show in the screen.
	 */
	private final int SECONDS_TO_WAIT = 6;
	/**
	 * Movement speed. 15 will be the default for MDPI. 
	 */
	private int mSpeed = 15;
	/**
	 * Line spacing; 3 will be the default for MDPI.
	 */
	private int mLineSpacing = 4;
	/**
	 * Text size; 15 will be the default for MDPI. 
	 */
	private int mTextSize = 15;
	/**
	 * Margin top.
	 */
	private final double BACKGROUND_MARGIN_TOP = 0.02;
	/**
	 * Numeric value that represents the alpha as opaque.
	 */
	private final int FULL_ALPHA = 255;
	/**
	 * Contains the image resource with the background.
	 */
	private Bitmap mBmpBackground;
	/**
	 * Contains the image resource with the Dr. Climate.
	 */
	private Bitmap mBmpDrClimate;
	/**
	 * X position of the object in the screen.
	 */
	private int mX;
	/**
	 * Y position of the object in the screen.
	 */
	private int mY;
	/**
	 * Indicates the the modal should hide or not.
	 */
	private boolean mHide;
	/**
	 * Indicates if the modal is currently being displayed on the screen.
	 */
	private boolean mDisplaying;
	/**
	 * Paint properties.
	 */
	private Paint mPaint;
	/**
	 * Array of lines. Multiple partitions of the text to display depending on its length.
	 */
	private ArrayList<String> mTextLines;
	/**
	 * The background's border height.
	 */
	private int mBorderHeight;
	/**
	 * A character's height.
	 */
	private int mCharacterHeight;
	/**
	 * A line's height.
	 */
	private int mLineHeight;
	
	/**
	 * Default constructor.
	 * 
	 * @param bmpBackground Contains the image resource with the background.
	 * @param bmpDrClimate Contains the image resource for the Dr. Climate Avatar.
	 * @param text Text to display.
	 */
	public Text (Bitmap bmpBackground, Bitmap bmpDrClimate, String text) {
		
		/* --------- SCREEN CONFIG ---------*/
		mLineSpacing = (int) (mLineSpacing * GameConfig.heightFactor);
		mSpeed = (int) (mSpeed * GameConfig.widthFactor);
		mTextSize = (int) (mTextSize * GameConfig.widthFactor);
		/* --------- SCREEN CONFIG ---------*/

		// Assign bitmaps
		mBmpBackground = bmpBackground;
		mBmpDrClimate = bmpDrClimate;
		
		// Frame and Others
		mHide = false;
		mDisplaying = false;
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(mTextSize);
		mPaint.setColor(Color.BLACK);
		mPaint.setTextAlign(Paint.Align.LEFT);
		mPaint.setTypeface(
				Typeface.create(Typeface.SERIF, Typeface.NORMAL)
				);
						
		/* --------- BEGIN X and Y ---------*/
		mX = (int) (0 - mBmpBackground.getWidth());
		mY = (int) (GameConfig.height * BACKGROUND_MARGIN_TOP);
		/* --------- END X and Y ---------*/

		
		/* --------- END STRING PARSE ---------*/
		mTextLines = new ArrayList<String>();
		String[] words = text.split(" ");
		String line = "";
		for (String word : words) {
			if (mPaint.measureText(line + word) > (mBmpBackground.getWidth() * PERCENTAGE_WRITABLE)) {
				mTextLines.add(line);
				line = word + " ";
			} else {
				line += word + " ";
			}
		}
		if (!line.trim().equals("")) {
			mTextLines.add(line.trim());
		}
		line = "";
		/* --------- BEGIN STRING PARSE ---------*/
		

		/* --------- BEGIN HEIGHT and WIDTH ---------*/
		mBorderHeight = (int) (mBmpBackground.getHeight() * BACKGROUND_BORDER);

		Rect textBounds = new Rect();
		mPaint.getTextBounds("A", 0, 1, textBounds);
		mCharacterHeight = (textBounds.top * (textBounds.top < 0 ? -1
				: 1)) - textBounds.bottom;
		mLineHeight = mCharacterHeight + mLineSpacing;
		textBounds = null;
		/* --------- END HEIGHT and WIDTH ---------*/
	}

	public void update(long currentDateTime) {
		
		if (mDisplaying) {
			// Display message
			if (currentDateTime > lastGameTime + 1000 * SECONDS_TO_WAIT) {
				mDisplaying = false;
				mHide = true;
			}
		} else {
			// Show and hide animation
			if (!mHide) {
				mX += (mX + mSpeed > 0) ? (0 - mX) : mSpeed;
				if (mX == 0) {
					mDisplaying = true;
					lastGameTime = currentDateTime;
				}
			} else {
				mX -= (mX + mBmpBackground.getWidth() < 0) ? 0 : mSpeed;
				if (mX < 0 - mBmpBackground.getWidth()) {
					// Remove item
					GameState.pointsCollection.remove(this);
				}
			}

		}

	}

	public void draw(Canvas canvas) {

		// Set transparency (alpha)
		Paint paint = new Paint();
		paint.setAlpha((int)(FULL_ALPHA * BACKGROUND_ALPHA));
		
		// 
		// Content and side border

		/* --------- BEGIN DRAW BACKGROUND --------- */
		if (mTextLines.size() * mLineHeight > mBmpDrClimate.getHeight()) {
			
			// Top Border
			Rect frame = GameUtility.createRect(0, 0, mBmpBackground.getWidth(), mBorderHeight);
			
			Rect location = GameUtility.createRect(mX, mY, mX + mBmpBackground.getWidth(), mY + mBorderHeight);
			
			canvas.drawBitmap(mBmpBackground, frame, location, paint);

			int frameStartY = mBorderHeight;
			frame = GameUtility.createRect(0, frameStartY, mBmpBackground.getWidth(), frameStartY + mBorderHeight);
			
			location = GameUtility.createRect(mX, mY + mBorderHeight, mX + mBmpBackground.getWidth(), mY + mBorderHeight + (mLineHeight * mTextLines.size()));
			
			canvas.drawBitmap(mBmpBackground, frame, location, paint);
			
			// Bottom Border
			frameStartY = (int) (mBmpBackground.getHeight() - mBorderHeight);
			frame = GameUtility.createRect(0, frameStartY, mBmpBackground.getWidth(), frameStartY + mBorderHeight);
			
			location = GameUtility.createRect(mX, mY + mBorderHeight + (mLineHeight * mTextLines.size()), mX + mBmpBackground.getWidth(), mY + (mBorderHeight * 2) + (mLineHeight * mTextLines.size()));
			
			canvas.drawBitmap(mBmpBackground, frame, location, paint);
			
		} else {
			
			// Draw not partitioned background
			canvas.drawBitmap(mBmpBackground, mX, mY, paint);		
			
		}
		/* --------- END DRAW BACKGROUND ---------*/
		
		/* --------- BEGIN DRAW DR CLIMATE ---------*/
		int marginDoctor = ((mBmpBackground.getHeight() - mBmpDrClimate.getHeight()) / 2);
		canvas.drawBitmap(mBmpDrClimate, mX + marginDoctor, mY + marginDoctor, null);
		/* --------- END DRAW DR CLIMATE ---------*/
		
		/* --------- BEGIN DRAW TEXT ---------*/
		for (int index = 0; index < mTextLines.size(); index++) {
			
			canvas.drawText(mTextLines.get(index), (float)(mX + (mBmpBackground.getWidth() * (1 - (PERCENTAGE_WRITABLE + 0.05)))), (float)(mY + (mBorderHeight + mCharacterHeight) + (index * mLineHeight)), mPaint);
		}
		/* --------- END DRAW TEXT ---------*/
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
