package com.amsgames.world.bank;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.amsgames.world.bank.Interfaces.IDifficultyHandler;
import com.amsgames.world.bank.Save.GameSave;

/**
 * This activity is in charge of setting the ContentView to the SurfaceView (GamePanel)
 * It also handles all the events of the activity itself:
 * onCreate - onPause - onResume - onDestroy
 * 
 * @author Jose Cruz (Amsgames), Alejandro Mostajo (Amsgames)
 *
 */
public class GameActivity extends Activity {
	
	/* --------- Private fields --------- */
	
	private static final String HANDLER = "handler";
	
	private GamePanel gamePanel;
	private boolean showingModal = false;
	
	/* --------- Overrides ------------*/
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		
        // Set full screen and no sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// Get bundled objects
		IDifficultyHandler handler = null;
		if (getIntent() != null) {
			handler = (IDifficultyHandler) getIntent().getSerializableExtra(HANDLER);
		}
		
		// Delete prior save file
		GameSave.deleteFileConfig();
		
		// Set content view to GamePanel
		this.gamePanel = new GamePanel(this, handler);
		setContentView(gamePanel);
	
    	showingModal = false;		
	}
	
	@Override
	protected void onDestroy() {
		
		this.gamePanel.onDestroy();
		
		// Delete prior save file
		GameSave.deleteFileConfig();
		
		super.onDestroy();
		
	}
	
	@Override
	protected void onPause(){
		
		GameSave gameSave = new GameSave();
		gameSave.saveGame();
		
		this.gamePanel.onPause();
		
		super.onPause();
	}
	
	@Override
	protected void onResume(){
		
		GameSave gameSave = new GameSave();
		
		gameSave.loadGame();
		
		this.gamePanel.onResume();
		
		super.onResume();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		
	    // Control KEYCODE_BACK to pause the game and prompt the user if he/she wants to exit.
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    
	    	// If we are showing modal, means that the event was a dummy call to resume the game.
	    	if (showingModal) {
	    		
	        	showingModal = false;
		    	this.gamePanel.onResume();
		    	GameAudio.resumeBackground();
		    	return true;
		    	
		    }else{
	    	
		    	this.gamePanel.onPause();
		    	GameAudio.pauseBackground();
		    	showingModal = true;
		    	
		    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		    	dialog.setTitle(getString(R.string.prompt_exit_game));
		    	dialog.setCancelable(false);
		    	final Intent intent = new Intent(this, MenuActivity.class);
		    	
		    	// Finish the activity if selected YES
		    	dialog.setPositiveButton(getString(R.string.prompt_exit_game_yes), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		            	startActivity(intent);
		            	finish();
		                }
		            });
		    	
		    	// Resume game if selected NO
		    	dialog.setNegativeButton(getString(R.string.prompt_exit_game_no), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,    int which) {
		            	
		            	// Emulate an onKeyPress to resume game.
		            	onKeyDown (KeyEvent.KEYCODE_BACK, new KeyEvent(0,0));
		            	
		            }
		        });
		    	
		    	dialog.show();
		    	
		    	return true;
		    
		    }
	 
	    // Normal behavior handled by android.
	    } if (keyCode != KeyEvent.KEYCODE_BACK) {
	    	
	    	return false;
	    	
	    } 
	    
	    return true;
	    
	}
	
}