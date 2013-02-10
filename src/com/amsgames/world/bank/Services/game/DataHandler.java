package com.amsgames.world.bank.Services.game;

import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;

import com.amsgames.world.bank.Interfaces.IDbAdapter;
import com.amsgames.world.bank.Services.data.AmsColumn;
import com.amsgames.world.bank.Services.data.AmsDataSet;
import com.amsgames.world.bank.Services.data.AmsItem;
import com.amsgames.world.bank.Services.data.AmsRow;
import com.amsgames.world.bank.Services.data.AmsTable;
import com.amsgames.world.bank.Services.sql.DbAdapter;
import com.amsgames.world.bank.Services.webservice.AmsWebService;
import com.amsgames.world.bank.Services.webservice.AmsWebServiceResult;

/**
 * Handles all the game's interaction with any kind of data.
 * Either from the data base or a web service.
 * 
 * @author Alejandro Mostajo (AmsGames)
 *
 */
public class DataHandler {
	
	private IDbAdapter _adapter;
	private Context _context;
	private String _currentCountry;
	
	/**
	 * Default constructor.
	 * 
	 * @param context The Context within which to work.
	 */
	public DataHandler (Context context){
		this._context = context;
		this._adapter = new DbAdapter(this._context);
		this._currentCountry = "";
		this.initialize();
	}
	
	/**
	 * Initialize special class variables.
	 */
	private void initialize() {
		this._adapter.open();
		try {
			if (!this._adapter.isEmptyTable("STATS", "WHERE CODE = 'CURRENT_COUNTRY'")) {
				// Get current country
				Cursor cursor = this._adapter.query("SELECT VALUE FROM STATS WHERE CODE = 'CURRENT_COUNTRY'");
				this._currentCountry = cursor.getString(0);
				cursor.deactivate();
				cursor.close();
				cursor = null;
			} else {
				// Get the player location
				Locale locale = Locale.getDefault();
				this._currentCountry = locale.getISO3Country().toLowerCase();
				locale = null;
			}
		} catch (Exception ex) {
			logError(
					"",
					ex);
		}
		
		this._adapter.close();
	}
	
	/**
	 * Logs the error through a data base call and returns an exception.
	 * 
	 * @param userError Error to display to the user.
	 * @param exception Exception.
	 * 
	 * @return Exception.
	 */
	public Exception logError (String userError, Exception exception) {
		this._adapter.open();
		
		/*
		 * Log error in the database.
		 */
		String sqlError = "INSERT INTO ERROR_LOG (TIME_ID,ERROR) VALUES ";
		String errorValue = "";

        long timeID = new Date().getTime();
        sqlError += "(" + String.valueOf(timeID) + ",'";
		for (StackTraceElement element : exception.getStackTrace()) {
			// Add stack separator
			errorValue += errorValue.equals("") ? "" : "|";
			errorValue += "ERROR:" + exception.getMessage();
			errorValue += "~CLASS:" + element.getClassName();
			errorValue += "~FILENAME:" + element.getFileName();
			errorValue += "~METHOD:" + element.getMethodName();
			errorValue += "~LINE:" + String.valueOf(element.getLineNumber());
		}
		sqlError += errorValue + "')";
		errorValue = null;
		
		// Log
		try {
			this._adapter.execSQL(sqlError);
		} catch (Exception ex) {
			// Do nothing
		}
		sqlError = null;
		
    	this._adapter.close();
		
		return new Exception(userError);
	}
	
	/**
	 * Returns the available countries for the game.
	 * 
	 * @return {@link AmsTable} with the list of countries.
	 * 
	 * @exception Exception
	 */
	public AmsTable getCountries () throws Exception {
		this._adapter.open();
		AmsTable returnTable = new AmsTable();
		
		try {
			// Check for existance in the database, since we will cache this on the data base.
			if (this._adapter.isEmptyTable("COUNTRY")) {
				// Get countries from WS.
		        AmsWebServiceResult ws = 
		        		new AmsWebService(
		        				this._context, 
		        				com.amsgames.world.bank.R.string.wsGetCountries).Call();
		        // Check for results
		        if (!ws.HasError 
		        		&& ws.Returns
		        ) {
		        	returnTable = ws.Results.Tables.get(0);
		        	// Update and cache SQLlite database
		        	this._adapter.updateTableFrom(returnTable);
		        } else {
		        	// Throw error.
		        	throw new Exception("GetCountries web service didn't return results.");
		        }
		        ws = null;
			} else {
				// Get cached data from the database.
				AmsDataSet dataSet = new AmsDataSet();
				dataSet.FillWith(this._adapter.query("SELECT ISO3,NAME,DIFFICULTY_LEVEL FROM COUNTRY ORDER BY NAME"));
				returnTable = dataSet.Tables.get(0);
				returnTable.TableName = "COUNTRY";
				// Empty variables
				dataSet = null;
			}
		} catch (Exception ex) {
			throw logError(
					"There was an issue while trying to get the climate data. Please check your internet connection.",
					ex);
		}

    	this._adapter.close();
    	
		return returnTable;
	}
	
	/**
	 * Returns the climate data as a difficulty handler for a given country.
	 * Sets the current country.
	 * 
	 * @param iso3Code Country ISO3 code.
	 * 
	 * @return A game's {@link DifficultyHandler}.
	 * 
	 * @exception Exception
	 */
	public DifficultyHandler getClimateData (String iso3Code) throws Exception {
		this._adapter.open();
		DifficultyHandler difficulty = new DifficultyHandler();
		AmsTable results = new AmsTable();
		
		try {
			// Call WS if there country was changed or if there is no data cached data.
			if (this.setCurrentCountry(iso3Code)
					|| this._adapter.isEmptyTable("CLIMATE")) {
				// Get data from WS
				// Set WS parameters.
				String[] params = new String[1];
				params[0] = iso3Code;
				// Call WS
		        AmsWebServiceResult ws = 
		        		new AmsWebService(
		        				this._context, 
		        				com.amsgames.world.bank.R.string.wsGetClimateData,
		        				params).Call();
		        // Check for results
		        if (!ws.HasError 
		        		&& ws.Returns
		        ) {
		        	results = ws.Results.Tables.get(0);
		        	// Update and cache SQLlite database
		        	this._adapter.updateTableFrom(results);
		        } else {
		        	// Throw error.
		        	throw new Exception("getClimateData web service didn't return results.");
		        }
		        ws = null;
			} else {
				// Get cached data from the database.
				AmsDataSet dataSet = new AmsDataSet();
				dataSet.FillWith(this._adapter.query(
						"SELECT NAME,YEAR_RANGE,TEMPERATURE,PRECIPITATION,DIFFICULTY_LEVEL," +
						" ROUND,OBJECTS_X_ROUND,OBJECT_SPEED_FACTOR,OBJECT_FREQUENCY_FACTOR,FACT_MESSAGE " +
						"FROM CLIMATE ORDER BY ROUND"
						));
				results = dataSet.Tables.get(0);
				results.TableName = "CLIMATE";
				// Empty variables
				dataSet = null;
			}
			
			/*
			 * Convert from AmsTable to DifficultyHandler.
			 */
			for(AmsRow row : results.Rows) {
				RoundSettings round = new RoundSettings();
				round.Round = Integer.parseInt(row.getValueAt("ROUND").toString()) + 1;
				round.Temperature = Double.parseDouble(row.getValueAt("TEMPERATURE").toString());
				round.Precipitation = Double.parseDouble(row.getValueAt("PRECIPITATION").toString());
				round.SpeedFactor = Double.parseDouble(row.getValueAt("OBJECT_SPEED_FACTOR").toString());
				round.FrequencyFactor = Double.parseDouble(row.getValueAt("OBJECT_FREQUENCY_FACTOR").toString());
				round.ObjectQuantity = Integer.parseInt(row.getValueAt("OBJECTS_X_ROUND").toString());
				round.YearRange = row.getValueAt("YEAR_RANGE").toString();
				round.CountryName = row.getValueAt("NAME").toString();
				round.FactMessage = row.getValueAt("FACT_MESSAGE").toString();
				round.DifficultyLevel = Integer.parseInt(row.getValueAt("DIFFICULTY_LEVEL").toString());
				difficulty.RoundsSettings.add(round);
			}
			// Generate index locator
			difficulty.generateIndexLocator();
			results = null;
			
		} catch (Exception ex) {
			throw logError(
					"There was an issue while trying to get the climate data. Please check your internet connection or report to Amsgames.",
					ex);
		}

    	this._adapter.close();
    	
		return difficulty;
	}
	
	/**
	 * Adds a certain score the STATS of the game. 
	 * Will be added only if it is a new highscore.
	 * 
	 * @param score The player score.
	 * 
	 * @return True if score is the new highscore false if not.
	 * 
	 * @exception Exception
	 */
	public boolean addScore(float score) throws Exception {
		boolean added = false;
		this._adapter.open();
		try {
			if (this._adapter.isEmptyTable("STATS", "WHERE CODE = 'HIGHSCORE'")) {
				// INSERT HIGHSCORE
				this._adapter.execSQL(
						"INSERT INTO STATS (CODE,VALUE) VALUES ('HIGHSCORE','" + String.valueOf(score) +"')");
				added = true;
			} else {
				// UPDATE HIGHSCORE
				Cursor cursor = this._adapter.query("SELECT VALUE FROM STATS WHERE CODE = 'HIGHSCORE'");
				float currentHighscore = cursor.getFloat(0);
				cursor.deactivate();
				cursor.close();
				cursor = null;
				if (score > currentHighscore) {
					this._adapter.execSQL(
							"UPDATE STATS SET VALUE = '" + String.valueOf(score) +"' WHERE CODE = 'HIGHSCORE'");
					added = true;
				}
			}
		} catch (Exception ex) {
			throw logError(
					"",
					ex);
		}

    	this._adapter.close();
    	return added;
	}
	
	/**
	 * Returns the highscore stored in the data base.
	 * 
	 * @return Highscore stored in the data base.
	 * 
	 * @exception Exception
	 */
	public int getHighscore() throws Exception {
		int highscore = 0;
		this._adapter.open();
		try {
			if (!this._adapter.isEmptyTable("STATS", "WHERE CODE = 'HIGHSCORE'")) {
				Cursor cursor = this._adapter.query("SELECT VALUE FROM STATS WHERE CODE = 'HIGHSCORE'");
				highscore = cursor.getInt(0);
				cursor.deactivate();
				cursor.close();
				cursor = null;
			}
		} catch (Exception ex) {
			throw logError(
					"",
					ex);
		}

    	this._adapter.close();
    	return highscore;
	}
	
	/**
	 * Sets the current country in play.
	 * 
	 * @param iso3Code Country ISO3 code.
	 * 
	 * @return TRUE if the country was changed and FALSE if not.
	 * 
	 * @exception Exception
	 */
	private boolean setCurrentCountry(String iso3Code) throws Exception {
		boolean different = false;
		if (!this._currentCountry.equalsIgnoreCase(iso3Code)) {
			try {
				if (this._adapter.isEmptyTable("STATS", "WHERE CODE = 'CURRENT_COUNTRY'")) {
					// INSERT HIGHSCORE
					this._adapter.execSQL(
							"INSERT INTO STATS (CODE,VALUE) VALUES ('CURRENT_COUNTRY','" + iso3Code +"')");
				} else {
					// UPDATE HIGHSCORE
					this._adapter.execSQL(
							"UPDATE STATS SET VALUE = '" + iso3Code +"' WHERE CODE = 'CURRENT_COUNTRY'");
				}
				// Update current country
				this._currentCountry = iso3Code;
				different = true;
			} catch (Exception ex) {
				throw logError(
						"",
						ex);
			}
		}
    	
    	return different;
	}
	
	/**
	 * Returns the current country in play.
	 * 
	 * @return Country ISO3 code.
	 */
	public String getCurrentCountry() {
		return this._currentCountry;
	}
	
	/**
	 * Returns the name of current country in play.
	 * 
	 * @return Country name.
	 */
	public AmsRow getCurrentCountryAsRow() throws Exception {
		AmsRow row = null;
		this._adapter.open();
		
		try {
			Cursor cursor = this._adapter.query("SELECT NAME FROM COUNTRY WHERE ISO3 = '"+ this._currentCountry +"'");
			row = new AmsRow();
			// Add ISO3 code
			row.Items.add(new AmsItem(this._currentCountry, new AmsColumn("ISO3")));
			row.Items.add(new AmsItem(cursor.getString(0), new AmsColumn("NAME")));
			cursor.deactivate();
			cursor.close();
			cursor = null;
		} catch (Exception ex) {
			throw logError(
					"There was an issue while trying to get the climate data. Please check your internet connection.",
					ex);
		}

    	this._adapter.close();
    	
		return row;
	}
}
