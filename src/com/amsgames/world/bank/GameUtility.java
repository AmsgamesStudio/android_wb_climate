package com.amsgames.world.bank;

import android.graphics.Rect;

/**
 * Handles the atmosphere. 
 * 
 * @author Jose Cruz (Amsgames)
 *
 */
public class GameUtility {

	public static Rect createRect(int initX, int initY, int finalX, int finalY){
		
		return new Rect(
				// Initial position X in bitmap
				initX,
				// Initial position Y in bitmap
				initY,
				// Final position X in bitmap
				finalX,
				// Final position Y in bitmap
				finalY
				);
		
	}
	
}
