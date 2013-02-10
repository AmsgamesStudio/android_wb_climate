package com.amsgames.world.bank.Sprites;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.amsgames.world.bank.GameConfig;
import com.amsgames.world.bank.GameUtility;
import com.amsgames.world.bank.Interfaces.ISpriteStatic;

/**
 * Handles the game background. 
 * 
 * @author Alejandro Mostajo (Amsgames), Jose Cruz (Amsgames)
 *
 */
public class Background implements ISpriteStatic, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1334441612268310040L;
	/**
	 * The max temperature factor.
	 */
	private final int MAX_TEMPERATURE = 30;
	/**
	 * The max alpha to apply to temperature effect.
	 */
	private final double MAX_ALPHA = 0.2;
	/**
	 * Full opaque value.
	 */
	private final int FULL_ALPHA = 255;
	/**
	 * The current alpha assigned.
	 */
	private double mCurrentAlpha;
	/**
	 * Contains the image resource of the background.
	 */
	private Bitmap mBitmap;

	/**
	 * Default constructor.
	 * 
	 * @param bitmap Contains the image resource of the background.
	 * 
	 */
	public Background (Bitmap bitmap) {
		// Assign resource
		mBitmap = bitmap;
	}
	
	public void Draw(Canvas canvas) {
		
		// Alpha
		mCurrentAlpha = (MAX_ALPHA * GameConfig.difficultyHandler.getCurrentRoundSettings().Temperature) / MAX_TEMPERATURE;
		mCurrentAlpha = mCurrentAlpha > MAX_ALPHA ? MAX_ALPHA : mCurrentAlpha < 0 ? 0 : mCurrentAlpha;
		Paint paint = new Paint();
		paint.setColor(Color.rgb(FULL_ALPHA, 0, 0));
		paint.setAlpha((int)(FULL_ALPHA * mCurrentAlpha));
		
		Rect location = GameUtility.createRect(0, 0, GameConfig.width, GameConfig.height);
		
		Rect alphaEffect = GameUtility.createRect(0, 0, GameConfig.width, GameConfig.height);
		
        canvas.drawBitmap(mBitmap, null, location, null);
        	
        // DRAW TEMPERATURE EFFECT
        canvas.drawRect(alphaEffect, paint);
	}

}
