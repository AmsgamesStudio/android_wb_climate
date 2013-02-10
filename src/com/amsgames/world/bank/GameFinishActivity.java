package com.amsgames.world.bank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amsgames.world.bank.Services.game.DataHandler;
import com.amsgames.world.bank.Services.game.DifficultyHandler;

/**
 * This class will show the information when the user reaches the game win or
 * game over states.
 * 
 * @author Alejandro Mostajo (Amsgames)
 * 
 */

public class GameFinishActivity extends Activity {

	private static final String HANDLER = "handler";

	private DifficultyHandler mHandler;
	private int mScore;
	private boolean mGameWin;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set full screen and no sleep
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// Get bundled objects
		mHandler = (DifficultyHandler)GameConfig.difficultyHandler;
		mScore = GameConfig.score;
		mGameWin = GameConfig.isGameWon;

		String txtMessageText = "";
		String countryName = "";
		int temperatureChange = 0;

		if (mHandler != null) {
			// Get temperature change
			int tmp = (int) ((mHandler.RoundsSettings
					.get(mHandler.RoundsSettings.size() - 1).Temperature / mHandler.RoundsSettings
					.get(0).Temperature) * 100);
			
			temperatureChange = tmp < 100 ? tmp : tmp - 100; 
					
			countryName = mHandler.RoundsSettings.get(0).CountryName;
			
			// Use data handler
			DataHandler data = new DataHandler(this);
			// Initialize content depending on game state
			if (mGameWin) {
				setContentView(R.layout.gamewin);

				// Set win specific components
				txtMessageText = getString(R.string.win_message);

				TextView txtScore = (TextView) findViewById(R.id.txtScore);
				TextView txtHighscore = (TextView) findViewById(R.id.txtHighscore);
				// Set text
				txtScore.setText(Html.fromHtml(String.format(
						getString(R.string.score), mScore)));

				/*
				 * Handle high score
				 */
				boolean newHighscore = false;
				int highscore = 0;
				try {
					newHighscore = data.addScore(mScore);
					highscore = data.getHighscore();
				} catch (Exception ex) {
					// Nothing
				}
				if (!newHighscore) {
					// Hide NEW HIGHSCORE image
					txtHighscore.setCompoundDrawables(null, null, null, null);
				}
				txtHighscore.setText(String.valueOf(highscore));

			} else {
				setContentView(R.layout.gameover);
				// Set game over specific components
				txtMessageText = getString(R.string.gameover_message);

				TextView txtScore = (TextView) findViewById(R.id.txtScore);
				TextView txtHighscore = (TextView) findViewById(R.id.txtHighscore);
				// Set text
				txtScore.setText(Html.fromHtml(String.format(
						getString(R.string.score), mScore)));

				int highscore = 0;
				try {
					highscore = data.getHighscore();
				} catch (Exception ex) {
					// Nothing
				}
				txtHighscore.setText(String.valueOf(highscore));
			}
		}

		// Get VIEW components
		TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
		ImageButton btnRetry = (ImageButton) findViewById(R.id.btnRetry);
		ImageButton btnContinue = (ImageButton) findViewById(R.id.btnContinue);

		if (mHandler == null) {

			// Hide retry button since we don't have settings to bundle
			txtMessage.setVisibility(View.GONE);
			btnRetry.setVisibility(View.GONE);
		}
		// Set text
		txtMessage.setText(Html.fromHtml(String.format(txtMessageText,
				countryName, temperatureChange)));

		// Intents
		final Intent menuIntent = new Intent(this, MenuActivity.class);
		final Intent gameIntent = new Intent(this, GameActivity.class);
		gameIntent.putExtra(HANDLER, mHandler);

		// Set button listeners
		btnContinue.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(menuIntent);
				finish();
			}
		});
		btnRetry.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(gameIntent);
				finish();
			}
		});
	}
}
