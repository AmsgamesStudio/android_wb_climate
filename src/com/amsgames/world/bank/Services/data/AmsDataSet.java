package com.amsgames.world.bank.Services.data;

import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * "Ams Games Data Set" a set of Data Tables; used to handle information through out coding and program's execution.
 * 
 * @author Alejandro Mostajo (AmsGames)
 *
 */
public class AmsDataSet {
	/*
	 * Attributes
	 */
	/**
	 * List of tables.
	 */
	public List<AmsTable> Tables;
	
	/*
	 * Constructors
	 */
	/**
	 * Default constructor. Creates a Data Set with an empty list of tables.
	 */
	public AmsDataSet () {
		Tables = new ArrayList<AmsTable>();
	}
	/**
	 * Creates a Data Set with a defined list of tables.
	 * 
	 * @param tables	List of tables.
	 */
	public AmsDataSet (List<AmsTable> tables) {
		Tables = tables;
	}
	
	/**
	 * Creates a Data Set with a list of only one table in it.
	 * 
	 * @param table		A table to add to an empty list of tables.
	 */
	public AmsDataSet (AmsTable table) {
		Tables = new ArrayList<AmsTable>();
		Tables.add(table);
	}
	
	/*
	 * Functions and methods
	 */
	/**
	 * Fills the Data Set with information stored on a JSON Object. This methods creates one or more data tables depending on the data provided.
	 * 
	 * @param jsonObject	JSON Object, which contains stored information. Usually used with PHP web services.
	 * 
	 * @throws Exception	Error exception if the JSON object is not structured correctly.
	 */
	public void FillWith(JSONObject jsonObject) throws Exception {
		// Get table names
		JSONArray tableNames = jsonObject.names();
		for(int tableIndex=0; tableIndex<tableNames.length(); tableIndex++)
		{
			JSONArray jsonArray = jsonObject.getJSONArray(tableNames.getString(tableIndex));
			AmsTable auxTable = new AmsTable(tableNames.getString(tableIndex));
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					JSONArray childArray = jsonArray.getJSONArray(i);
					// Recursive method, gets all rows
					auxTable.Rows = this.getDataRows(childArray);
				} catch (Exception ex) {
					JSONObject childObject = jsonArray.getJSONObject(i);
					// Gets an specific row
					auxTable.Rows.add(this.getDataRow(childObject)); 
				}
			}
			auxTable.PopulateColumns();
			// Add new table
			Tables.add(auxTable);
		}
		tableNames = null;
	}
	/**
	 * Fills the Data Set with information stored on cursor. This methods creates one or more data tables depending on the data provided.
	 * 
	 * @param cursor	Android {@link Cursor}.
	 * 
	 * @throws Exception	Error exception if the JSON object is not structured correctly.
	 */
	public void FillWith(Cursor cursor) throws Exception {
    	// Method variables
    	AmsTable resutlsTable = new AmsTable("TABLE");
    	
		while (!cursor.isAfterLast()) {
			AmsRow row = new AmsRow();
			for(int columnIndex = 0; columnIndex < cursor.getColumnCount(); columnIndex++) {
				row.Items.add(new AmsItem(cursor.getString(columnIndex),
						new AmsColumn(cursor.getColumnName(columnIndex))));
			}
			// Add row to table
			resutlsTable.Rows.add(row);
    		// Null finished variables
			row = null;
			// Advance to next row
			cursor.moveToNext();
		}
		// Populate columns
		resutlsTable.PopulateColumns();
		// Add table to set
		Tables.add(resutlsTable);
		// Null finished variables
		resutlsTable = null;
		// Delete cursor
		cursor.deactivate();
		cursor.close();
		cursor = null;
	}
	
	/**
	 * Gets a list of Table Rows ({@link AmsRow}) stored in a JSON Array. Private method used to fill the data set with a JSON Object.
	 * 
	 * @param jsonArray		JSON Array which contains the data rows.
	 * @return	A list of Table Rows.
	 * @throws Exception	Error exception if the JSON object is not structured correctly.
	 */
	private List<AmsRow> getDataRows(JSONArray jsonArray) throws Exception {
		// Create an empty list
		List<AmsRow> rows = new ArrayList<AmsRow>();
		// Looks for all the rows
		for (int i = 0; i < jsonArray.length(); i++) {
            try {
            	JSONArray childArray = jsonArray.getJSONArray(i);
            	// Recursive method, gets all rows
            	rows = this.getDataRows(childArray);
            } catch (Exception ex) {
            	JSONObject childObject = jsonArray.getJSONObject(i);
            	// Gets an specific row
            	rows.add(this.getDataRow(childObject)); 
            }
        }
        return rows;
	}
	/**
	 * Gets a Table Rows ({@link AmsRow}) stored in a JSON Object. Private method used to fill the data set with a JSON Object.
	 * It also creates all the items ({@link AmsItem}) located in the list of items with the row.
	 * 
	 * @param jsonObject	JSON Object which contains the values and labels of a row.
	 * @return	A Table Row with a list of items and their related columns.
	 * @throws Exception	Error exception if the JSON object is not structured correctly.
	 */
	private AmsRow getDataRow(JSONObject jsonObject) throws Exception {
		// Create an empty row
		AmsRow returnRow = new AmsRow();
		// Get the names of the columns/labels
		JSONArray objectNames = jsonObject.names();
		// Loop for each column
		for(int i=0; i<objectNames.length();i++) {
			// Add new item to the row
			returnRow.Items.add(new AmsItem(jsonObject.get(objectNames.getString(i)),new AmsColumn(objectNames.getString(i))));		
		}
		
        return returnRow;
	}
}
