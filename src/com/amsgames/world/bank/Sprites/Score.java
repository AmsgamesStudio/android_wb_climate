package com.amsgames.world.bank.Sprites;

import android.graphics.Bitmap;
import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.amsgames.world.bank.GameConfig;
import com.amsgames.world.bank.GameUtility;
import com.amsgames.world.bank.Interfaces.ISpriteStatic;

/**
 * Score sprite.
 * 
 * @author Alejandro Mostajo (Amsgames), Jose Cruz (Amsgames)
 *
 */
public class Score implements ISpriteStatic, Serializable {

	/**
	 * Score to display
	 */
	private int score;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8778168073735770891L;
	
	private final int DIGIT_QUANTITY = 10;
	/**
	 * Right margin, relative to the screen.
	 */
	private final double MARGIN_RIGHT = 0.0125;
	/**
	 * Top margin, relative to the screen.
	 */
	private final double MARGIN_TOP = 0.01;
	/**
	 * Contains the image resource of the score.
	 */
	private Bitmap mBmpDigits;
	/**
	 * Contains the image resource of the score text.
	 */
	private Bitmap mBmpText;
	/**
	 * First digit X position of the object in the screen.
	 */
	private int mFirstDigitX;
	/**
	 * Y position of the object in the screen.
	 */
	private int mY;
	/**
	 * The width of a digit.
	 */
	private int mWidth;
	/**
	 * The height of a digit.
	 */
	private int mHeight;

	/**
	 * Default constructor.
	 * 
	 * @param bmpDigits Contains the image resource of the score.
	 * @param bmpText Contains the image resource of the score text.
	 * @param score Score to display.
	 */
	public Score (Bitmap bmpDigits, Bitmap bmpText, int score) {
		// Assign resource
		mBmpDigits = bmpDigits;
		mBmpText = bmpText;
		// Assign score
		this.score = score;
		
		/* --------- BEGIN HEIGHT and WIDTH ---------*/
		mWidth = (int) (mBmpDigits.getWidth() / DIGIT_QUANTITY);
		mHeight = (int) mBmpDigits.getHeight();
		/* --------- END HEIGHT and WIDTH ---------*/
		
		/* --------- BEGIN X and Y ---------*/
		mFirstDigitX = (int) (GameConfig.width - ((GameConfig.width * MARGIN_RIGHT) + mWidth));
		mY = (int)(GameConfig.height * MARGIN_TOP);
		/* --------- END X and Y ---------*/
	}
	
	public void setScore(int score){
		
		this.score = score;
		
	}

	public void Draw(Canvas canvas) {
		
		int currentX = mFirstDigitX;
		
		if (this.score <= 0) {
			
			// Draw empty zero digit
			// Get digit frame
			Rect frame = GameUtility.createRect(0, 0, mWidth, mHeight);
			
			// Get location and scale
			Rect location = GameUtility.createRect(mFirstDigitX, mY, mFirstDigitX + mWidth, mY + mHeight);
	
			// Draw FILL
			canvas.drawBitmap(mBmpDigits, frame, location, null);
			
			currentX -= mWidth;
			
		} else {
			
			// Draw score
			int tempScore = this.score;
			
			while (tempScore > 0) {
				
				int frameX = tempScore % 10;
				// Get digit frame
				Rect frame = GameUtility.createRect(frameX * mWidth, 0, (frameX * mWidth) + mWidth, mHeight);
				
				// Get location
				Rect location = GameUtility.createRect(currentX, mY, currentX + mWidth, mY + mHeight);
				
				// Draw digit
				canvas.drawBitmap(mBmpDigits, frame, location, null);
				
				// Move to next digit
				tempScore = tempScore / 10;
				
				currentX -= mWidth;
				
			}
			
		}
		
		// DRAW SCORE TEXT
		// Assuming that the score will not reach more than 99999
		canvas.drawBitmap(mBmpText, (int) (currentX - mBmpText.getWidth()), mY, null);
		
	}

}
