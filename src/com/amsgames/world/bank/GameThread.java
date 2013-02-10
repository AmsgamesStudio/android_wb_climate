package com.amsgames.world.bank;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * This class is in charge of executing the game logic in a separate thread.
 * It also makes sure that the game speed (FPS and UPS) are even and constant.
 * 
 * @author Jose Cruz (Amsgames)
 *
 */

public class GameThread extends Thread {
	
	/* --------- Public --------- */
	public static boolean update = true;

	/* --------- Private fields --------- */

	private boolean running;
	private boolean paused;
	private boolean isFinished;

	private SurfaceHolder surfaceHolder;
	private GamePanel gamePanel;

	private final static int MAX_FPS = 30; // AIM to 50 FPS
	private final static int MAX_FRAME_SKIP = 5; // If running slow, skip draw method 5 time as max
	private final static int FRAME_PERIOD = 1000 / MAX_FPS; // Get the amount of milliseconds that each frame should last

	/* --------- Sets / Gets --------- */

	public void setRunning(boolean running) {

		this.running = running;

	}
	
	public void setPaused(boolean paused) {
		
		this.paused = paused;
		
	}
	
	public boolean getIsFinished() {
		
		return this.isFinished;
		
	}

	/* --------- Constructors --------- */
	public GameThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {

		super();

		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;

	}

	/* --------- Overrides ------------ */

	// Run until false
	@Override
	public void run() {

		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		
		this.isFinished = false;
		
		Canvas canvas;

		// Performance
		long beginTime; // Time when cycle begun
		long timeDiff; // Time it took the cycle to execute
		int sleepTime; // Left time that needs to be sleep
		int framesSkipped; // Track of frames that have been skipped

		sleepTime = 0;

		while (running) {
			
			// Check for game in pause
			if (paused){
				
				try {
					Thread.sleep(300);
					continue; // Execute the loop again in case it has been resumed 
				} catch (InterruptedException e) {

				}
				
			}
			
			// Start canvas every loop
			canvas = null;

			try {
		
				// Get canvas and lock it
				canvas = this.surfaceHolder.lockCanvas();
	
				// Get time when loop started
				beginTime = System.currentTimeMillis();
				framesSkipped = 0;
	
				// Update Game State
				if (update)
					this.gamePanel.update();
					
				// Avoid problems with other threads
				synchronized (surfaceHolder) {
					
					// Draw Game State
					this.gamePanel.draw(canvas);
					
				}

				// Calculate how long did the loop take
				timeDiff = System.currentTimeMillis() - beginTime;

				// Calculate time to sleep
				sleepTime = (int) (FRAME_PERIOD - timeDiff);

				// Need to wait
				// Sleep Thread
				if (sleepTime > 0) {

					try {

						Thread.sleep(sleepTime);

					} catch (InterruptedException e) {
					}

				}

				// Need to catch up
				// Call update Don't call Draw
				while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIP && running) {

					// Update Game State
					if (update)
						this.gamePanel.update();

					// Increase sleepTime for one more FPS to see if it
					// catches up
					sleepTime += FRAME_PERIOD;

					// Increase framesSkipped to control that it doesn't
					// surpass the amount of allowed frames
					framesSkipped++;

				}
				
			} finally {

				// Make sure canvas is always unlocked
				if (canvas != null) {

					surfaceHolder.unlockCanvasAndPost(canvas);

				}

			}

			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			
		}

		this.isFinished = true;
		
	}

}
