package com.amsgames.world.bank;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amsgames.world.bank.Services.data.AmsRow;
import com.amsgames.world.bank.Services.data.AmsTable;
import com.amsgames.world.bank.Services.game.DataHandler;
import com.amsgames.world.bank.Services.game.DifficultyHandler;

/**
 * 
 * @author Alejandro Mostajo (AmsGames)
 *
 */
public class MenuActivity extends Activity {
	private static final String HANDLER = "handler";
	/**
	 * Data handler.
	 */
	private DataHandler _data;
	/**
	 * Activity's dialog.
	 */
	private Dialog _dialog;
	private TextView _txtSelectedCountry;
	private String _selectedCountry;
	private AmsTable _countries;
	private View _vwSearch;
	private View _vwBtnGameStart;
	private int _selectedCountryIndex = 0;
	private DifficultyHandler _handler;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Set full screen and no sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this._data = new DataHandler(this);
        // Text view
        this._txtSelectedCountry = (TextView) findViewById(R.id.txtSelectedCountry);
        // Search field listener
        this._vwSearch = (View) findViewById(R.id.laySearch);
        this._vwSearch.setOnClickListener(new OnClickListener() {
        	
            public void onClick(View v) {
            	showCountrySelectionDialog();
            }
        });
        // Start button listener
        this._vwBtnGameStart = (View) findViewById(R.id.layGameStartButton);
        this._vwBtnGameStart.setOnClickListener(new OnClickListener() {
        	
            public void onClick(View v) {
            	loadClimateData();
            }
        });
        // Tutorial button listener
        View lblTutorial = (View) findViewById(R.id.lblTutorial);
        lblTutorial.setOnClickListener(new OnClickListener() {
        	
            public void onClick(View v) {
            	showTutorialDialog();
            }
        });    
        // Credits button listener
        View lblCredits = (View) findViewById(R.id.lblCredits);
        lblCredits.setOnClickListener(new OnClickListener() {
        	
            public void onClick(View v) {
            	showCreditsDialog();
            }
        });    
        this._selectedCountry = "";
        // Check for SD card
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
        	// Load countries
        	loadCountries();
        } else {
        	// Show error
        	showError(getString(R.string.error_missing_sdcard));
        }
    }
        
    /**
     * Async task that will be called to obtain the list of playable locations.
     * 
     * @author Alejandro Mostajo (AmsGames)
     *
     */
    public class LoaderCountry extends AsyncTask<Object,Void,AmsTable> {
    	MenuActivity caller;
    	
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    	}

		@Override
		protected AmsTable doInBackground(Object... params) {
			caller = (MenuActivity)params[0];
			DataHandler data = new DataHandler (caller);
			try {
				// Download Countries
				return data.getCountries();
			} catch (Exception ex) {
				return null;
			}
		}
		
	    protected void onPostExecute(AmsTable table) {
	    	caller.postLoadCountries(table);
	    }

    }
    
    /**
     * Async task that will be called to obtain the climate data for a selected location.
     * 
     * @author Alejandro Mostajo
     *
     */
    public class LoaderClimateData extends AsyncTask<Object,Void,DifficultyHandler> {
    	MenuActivity caller;

    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    	}

    	@Override
    	protected DifficultyHandler doInBackground(Object... params) {
    		caller = (MenuActivity)params[0];
    		String iso3Code = params[1].toString();
			DataHandler data = new DataHandler (caller);
    		try {
    			// Download Countries
    			return data.getClimateData(iso3Code);
    		} catch (Exception ex) {
    			return null;
    		}
    	}

    	protected void onPostExecute(DifficultyHandler handler) {
    		caller.postLoadClimateData(handler);
    	}

    }

    
    /**
     * Loads the countries from the WB or DB, this will be done only once.
     * Opens a loading dialog.
     */
    private void loadCountries() {
		_dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Get the layout view for this dialog.
        LinearLayout lytContainer = (LinearLayout) View.inflate(
                this, R.layout.drclimate_loader, null);
        // Get the button continue and hide it
        TextView btnContinue = (TextView) lytContainer.findViewById(R.id.btnContinue);
        btnContinue.setVisibility(View.GONE);
        // Set dialog's content view
		_dialog.setContentView(lytContainer);
		_dialog.setCancelable(false);
		_dialog.show();
        
        // Call loader
        LoaderCountry loader = new LoaderCountry();
        loader.execute(this);
    }
    

    
    /**
     * Loads the countries from the WB, this will be done only once.
     * Opens a loading dialog.
     */
    private void loadClimateData() {
		_dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Get the layout view for this dialog.
        LinearLayout lytContainer = (LinearLayout) View.inflate(
                this, R.layout.drclimate_loader, null);
        // Get the button continue and hide it
        TextView btnContinue = (TextView) lytContainer.findViewById(R.id.btnContinue);
        btnContinue.setVisibility(View.GONE);
        btnContinue.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	_dialog.dismiss();
            }
        });
        // Get text message
        TextView txtLoadingMessage = (TextView) lytContainer.findViewById(R.id.txtLoadingMessage);
        txtLoadingMessage.setText(
        		Html.fromHtml(
        				String.format(
        						getString(R.string.loading_climate_data),
        						_txtSelectedCountry.getText()
        				)
        		)
        );
        // Set dialog's content view
		_dialog.setContentView(lytContainer);
		_dialog.setCancelable(false);
		_dialog.show();
        
        // Call loader
		LoaderClimateData loader = new LoaderClimateData();
        loader.execute(this, this._selectedCountry);
    }
    
    /**
     * Method to be called after the countries have been loaded in the db.
     * 
     * @param countries List of countries.
     */
    public void postLoadCountries(AmsTable countries) {
    	try {
    		if (countries == null
    				|| countries.Rows.size() == 0) {
    			throw new Exception(getString(R.string.error_internet_connection));
    		}
    		AmsRow selectedCountry = new AmsRow();
    		try {
    			selectedCountry = this._data.getCurrentCountryAsRow();
    		} catch (Exception ex) {
    			throw this._data.logError("", ex);
    		}
    		// Set selected country
    		this._txtSelectedCountry.setText(selectedCountry.getValueAt("NAME").toString());
    		this._selectedCountry = selectedCountry.getValueAt("ISO3").toString();
    		this._countries = countries;
    		this._selectedCountryIndex = this._countries.GetRowIndex("ISO3", this._selectedCountry);
    		// Close dialog
    		_dialog.dismiss();
    		_dialog = null;
    	} catch (Exception ex) {
    		// Show error
    		this.showError(ex.getMessage());
    	}
    }
    
    public void postLoadClimateData(DifficultyHandler handler) {
    	try {
    		if (handler == null) {
				throw new Exception(getString(R.string.error_internet_connection));
    		}
    		// Get handler
    		this._handler = handler;
    		// Show new content
    		// Get the layout view for this dialog.
    		LinearLayout lytContainer = (LinearLayout) View.inflate(
    				this, R.layout.drclimate_loader, null);
    		// Get the button continue and hide it
    		TextView btnContinue = (TextView) lytContainer.findViewById(R.id.btnContinue);
    		btnContinue.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				_dialog.dismiss();
    				callGame();
    				finish();
    			}
    		});
    		// Progress bar
    		ProgressBar pBar = (ProgressBar) lytContainer.findViewById(R.id.progressBar);
    		pBar.setVisibility(View.GONE);
    		// Get text message
    		TextView txtLoadingMessage = (TextView) lytContainer.findViewById(R.id.txtLoadingMessage);
    		txtLoadingMessage.setText(
    				Html.fromHtml(
    						String.format(
    								getString(R.string.loading_climate_data),
    								_txtSelectedCountry.getText()
							)
					)
			);
    		// Set dialog's content view
    		_dialog.setContentView(lytContainer);
    	} catch (Exception ex) {
    		// Show error
    		this.showError(ex.getMessage());
    	}
    }
    
    /**
     * Method to be called after a country is selected.
     * 
     * @param listPosition Position of the country selected in the list.
     */
    public void postCountrySelection (int location) {
    	// Set selected country
    	this._txtSelectedCountry.setText(_countries.Rows.get(location).getValueAt("NAME").toString());
    	this._selectedCountry = _countries.Rows.get(location).getValueAt("ISO3").toString();
    	this._selectedCountryIndex = location;
    	// Close dialog
        _dialog.dismiss();
        _dialog = null;
    }
    
    /**
     * Shows a dialog with the list of available countries to play.
     */
    private void showCountrySelectionDialog () {
    	_dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Get the layout view for this dialog.
        LinearLayout lytContainer = (LinearLayout) View.inflate(
                this, R.layout.drclimate_select_country, null);
        // Get country list and set items
        ListView listCountry = (ListView) lytContainer.findViewById(R.id.listCountry);
        listCountry.setAdapter(
            	new ArrayAdapter<String>(this, R.layout.drclimate_listitem_country, this._countries.ColumnToArray("NAME")) {

            	    @Override
            	    public View getView( int position, View convertView, ViewGroup parent )
            	    {
            	        LayoutInflater inflater = (LayoutInflater)
            	                getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            	        TextView listItem = (TextView) inflater.inflate( R.layout.drclimate_listitem_country, parent, false );

                    	// listItem = (TextView) View.inflate(
                        //        this, R.layout.drclimate_listitem_country, null);
            	        final String item = (String) getItem(position);
            	        listItem.setText( item );
                		Drawable level_pic = getResources().getDrawable(R.drawable.level_easy);
                    	switch (Integer.parseInt(_countries.Rows.get(position).getValueAt("DIFFICULTY_LEVEL").toString())) {
                    	case 3:
                    		level_pic = getResources().getDrawable(R.drawable.level_hard);
                    		break;
                    	case 2:
                    		level_pic = getResources().getDrawable(R.drawable.level_normal);
                    		break;
                    	default:
                    		break;
                    	}
                    	level_pic.setBounds(
                    			0, 
                    			0, 
                    			(int)(13 * (getWindowManager().getDefaultDisplay().getWidth()/(double)320)), 
                    			(int)(16 * (getWindowManager().getDefaultDisplay().getHeight()/(double)480)));
                		listItem.setCompoundDrawables(level_pic, null, null, null);
            	        return listItem;
            	    }
            	}
            );
        listCountry.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            		postCountrySelection(position);
				
			}
        });
        listCountry.setSelection(this._selectedCountryIndex);
		// Set dialog's content view
		_dialog.setContentView(lytContainer);
		_dialog.show();
    }
    
    /**
     * Shows a dialog with the tutorial.
     */
    private void showTutorialDialog () {
    	_dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		_dialog.setContentView(R.layout.dialog_tutorial);
		_dialog.show();
    }
    
    /**
     * Shows a dialog with the credits.
     */
    private void showCreditsDialog () {
    	_dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		_dialog.setContentView(R.layout.dialog_credits);
		_dialog.show();
    }

    /**
     * To be called when an error occurs.
     * 
     * @param message The error message to display.
     */
    private void showError (String message) {
		_dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Get the layout view for this dialog.
        LinearLayout lytContainer = (LinearLayout) View.inflate(
                this, R.layout.drclimate_loader, null);
        // Get the button continue and hide it
        TextView btnContinue = (TextView) lytContainer.findViewById(R.id.btnContinue);
        btnContinue.setText(getString(R.string.exit));
        btnContinue.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
        // Get text message
        TextView txtLoadingMessage = (TextView) lytContainer.findViewById(R.id.txtLoadingMessage);
        txtLoadingMessage.setText(
        		message.equals("") ? getString(R.string.default_error) : message
        );
        // Progress bar
        ProgressBar pBar = (ProgressBar) lytContainer.findViewById(R.id.progressBar);
        pBar.setVisibility(View.GONE);
        // Set dialog's content view
		_dialog.setContentView(lytContainer);
		_dialog.setCancelable(false);
		_dialog.show();
    }
    
    private void callGame () {
    	// Remove dialog
    	this._dialog = null;
        
    	// Pass settings
    	Intent intent = new Intent(this, GameActivity.class);
    	intent.putExtra(HANDLER, this._handler);
    	
    	// Start game
    	startActivity(intent);
    }
}