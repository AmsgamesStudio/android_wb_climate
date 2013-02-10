package com.amsgames.world.bank.Interfaces;

import android.graphics.Canvas;

public interface ISpriteSelfAnimated {

	void update(long currentDateTime);
	
	void draw(Canvas canvas);
	
	boolean hasCollision (float x, float y);
	
	int getCollisionType();
	
	int[] getCollisionXYEffect();
}
