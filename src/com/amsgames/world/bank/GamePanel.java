package com.amsgames.world.bank;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.amsgames.world.bank.Interfaces.IDifficultyHandler;
import com.amsgames.world.bank.Services.game.DifficultyHandler;
import com.amsgames.world.bank.States.GameState;

/**
 * This class is in charge of calling the game logic. 
 * It has three main methods:
 * update - draw - input
 * 
 * @author Jose Cruz (Amsgames)
 *
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
	 	
		/* ---------  Private fields ---------*/  
	
		private GameThread mainThread;
		private Context context;
		private boolean existRequest = false;

		private long mLastTouchTime = 0L;
		
		/* ---------  Constructors ---------*/
		
		/**
		 * Creates a game panel based on a context a settings handler.
		 * 
		 * @param context The Context within which to work.
		 * @param difficultyHandler Game settings handler.
		 */
		public GamePanel(Context context, IDifficultyHandler difficultyHandler) {

			super(context);
			
			this.context = context;

			this.initialize(difficultyHandler);
		}
		
		/**
		 * Creates a game panel based on a context.
		 * 
		 * @param context The Context within which to work.
		 */
		public GamePanel(Context context) {

			super(context);

			this.initialize(null);
			
		}
		
		/**
		 * Initialize variables only used by the constructor.
		 * 
		 * @param settingsHandler Game settings handler.
		 */
		private void initialize(IDifficultyHandler settingsHandler) {
			// Add callback to intercept events
			getHolder().addCallback(this);
			
			// Add settings
			GameConfig.difficultyHandler =
					settingsHandler == null
					? new DifficultyHandler()
					: (DifficultyHandler) settingsHandler;

			// Make the GamePanel focusable so it can be handle events
			setFocusable(true);
			
		}
		
		/* ---------  Interface Implementations ------------*/
		 
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
		}

		// Execute when surface is ready
		public void surfaceCreated(SurfaceHolder holder) {
						
			// In case the thread has never being started
			// Otherwise the game is coming back from a pause
			if (mainThread == null){
				// Set dimensions
				GameConfig.height = getHeight();
				GameConfig.width = getWidth();
				GameConfig.widthFactor = (float)GameConfig.width / (float)320;
				GameConfig.heightFactor = (float)GameConfig.height / (float)480;
				GameConfig.score = 0;
				
				// Set resources
				GameConfig.resources = getResources();
				
				// Initialize audio
				GameAudio.initializeSound(context);
				
				GameConfig.currentState = new GameState();
				
				// Create main thread
				mainThread = new GameThread(getHolder(), this);
				
				GameThread.update = true;
				
				// Start main thread
				mainThread.setRunning(true);
				mainThread.setPaused(false);
				mainThread.start();
				
				// Start background audio
				GameAudio.playBackground(GameAudio.THEME_EASY);
			}

		}
		
		public void surfaceDestroyed(SurfaceHolder holder) {
			
		}
		
		/* --------- Overrides ------------*/
		@Override
		public boolean onTouchEvent(MotionEvent Event) {
			
			final long time = System.currentTimeMillis();
			if (Event.getAction() == MotionEvent.ACTION_MOVE && time - mLastTouchTime < 32) {
				
				try {
					
					Thread.sleep(22);
					
				} catch (InterruptedException e) {}
				
			}
			
			GameConfig.currentState.input(Event);
			
			mLastTouchTime  = time;
			
			return true;
			
		}

		/* --------- Methods ------------*/
		
		// Save the game state and resources
		// Release the resources
		public void onPause(){
			
			if (this.mainThread != null){
				// Manually clear resources
				this.mainThread.setPaused(true);
			}
			
		}
		
		// Retrieve saved game state and resources
		// Resume game from last point
		public void onResume(){
			
			if (this.mainThread != null){
				// Check what is needed to start game again
				this.mainThread.setPaused(false);
				GameThread.update = true;
			}
			
		}
		
		// Destroys all objects and resources
		public void onDestroy(){
			
			if (this.mainThread != null){
				// Wait until main thread is destroyed
				mainThread.setRunning(false);
				while(mainThread.getIsFinished() == false){
					try{
						
						Thread.sleep(300);
						
					}
					catch (InterruptedException e){
						
					}
				}
			}
			
		}
		
		// Update the game logic and physics
		public void update(){
			
			if ((GameConfig.isGameOver || GameConfig.isGameWon) && existRequest == false){
				
				GameAudio.destroySound();
				
				GameThread.update = false;
				
				final Intent finishIntent = new Intent(context, GameFinishActivity.class);
				context.startActivity(finishIntent);
				
				// Kills this Activity in order to show the
				// GameOver or GameWon Activities
				((Activity)this.context).finish();
				existRequest = true;
				
			}
			else {
				
				GameConfig.currentState.update();
				
			}
			
		}
		
		// Update the game screen
		public void draw(Canvas canvas){
			
			GameConfig.currentState.draw(canvas);
			
		}

	}
