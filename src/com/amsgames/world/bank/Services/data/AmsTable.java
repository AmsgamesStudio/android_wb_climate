package com.amsgames.world.bank.Services.data;

import java.util.ArrayList;
import java.util.List;
/**
 * "Ams Games Data Table" contains data structured as a table. It contains rows of data and columns with header names.
 * 
 * @author Alejandro Mostajo (Amsgames)
 *
 */
public class AmsTable {
	/*
	 * Attributes
	 */
	/**
	 * Table name.
	 */
	public String TableName;
	/**
	 * List of rows ({@link AmsRow}) in the table.
	 */
	public List<AmsRow> Rows;
	/**
	 * List of columns ({@link AmsColumn}) in the table.
	 */
	public List<AmsColumn> Columns;
	
	/*
	 * Constructors
	 */
	/**
	 * Default constructor. Creates an empty table with no name.
	 */
	public AmsTable () {
		TableName = "";
		Rows = new ArrayList<AmsRow>();
		Columns = new ArrayList<AmsColumn>();
	}
	/**
	 * Creates an empty table with a name.
	 * 
	 * @param tableName		Name of the table.
	 */
	public AmsTable (String tableName) {
		TableName = tableName;
		Rows = new ArrayList<AmsRow>();
		Columns = new ArrayList<AmsColumn>();
	}
	
	/**
	 * Populates the list of columns ({@link AmsColumn}) by looking at the columns related to the items ({@link AmsItem}) in the list of rows ({@link AmsRow}).
	 */
	public void PopulateColumns() {
		// Empty list of columns
		Columns = new ArrayList<AmsColumn>();
		// If there are rows in the table then
		if (Rows.size()>0)
		{
			// Loop all the items in the first row to get the list of columns
			for(int columnIndex = 0; columnIndex<Rows.get(0).Items.size(); columnIndex++) {
				// Get column related to item
				AmsColumn auxColumn = Rows.get(0).Items.get(columnIndex).Column;
				auxColumn.Index = columnIndex;
				// Add column to list
				Columns.add(auxColumn);
			}
		}
	}
	
	/**
	 * Returns the the values of a column in a String array.
	 * 
	 * @param columnIndex Column index
	 * 
	 * @return values of a column as an array.
	 */
	public String[] ColumnToArray (int columnIndex) {
		String[] values = new String[this.Rows.size()];
		// Add values to array
		for (int rowIndex = 0; rowIndex < this.Rows.size(); rowIndex++) {
			values[rowIndex] = this.Rows.get(rowIndex).getValueAt(columnIndex).toString();
		}
		return values;
	}
	
	/**
	 * Returns the the values of a column in a String array.
	 * 
	 * @param columnName Column name
	 * 
	 * @return values of a column as an array.
	 */
	public String[] ColumnToArray (String columnName) {
		String[] values = new String[this.Rows.size()];
		// Add values to array
		for (int rowIndex = 0; rowIndex < this.Rows.size(); rowIndex++) {
			values[rowIndex] = this.Rows.get(rowIndex).getValueAt(columnName).toString();
		}
		return values;
	}
	
	/**
	 * Gets the row index of a given column name and value.
	 * 
	 * @param columnName Column name.
	 * @param value	Value to find.
	 * 
	 * @return first row index in which the value was found.
	 */
	public int GetRowIndex (String columnName, Object value) {
		int index = 0;
		// Add values to array
		for (int rowIndex = 0; rowIndex < this.Rows.size(); rowIndex++) {
			if (this.Rows.get(rowIndex).getValueAt(columnName).equals(value)) {
				// Index found
				index = rowIndex;
				rowIndex = this.Rows.size();
			}
		}
		return index;
	}
}
