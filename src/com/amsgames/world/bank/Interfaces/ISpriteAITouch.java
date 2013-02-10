package com.amsgames.world.bank.Interfaces;

public interface ISpriteAITouch extends ISpriteTouch {
	
	void update();
	
	void setIsTouched(boolean isTouched);
	
	boolean getIsTouched();
	
	boolean hasCollided();
}
