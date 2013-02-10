package com.amsgames.world.bank.Services.data;

import java.util.ArrayList;
import java.util.List;

/**
 * "Ams Games Data Table Row" is a row with a list of items ({@link AmsItem}) contained in a Data Table ({@link AmsTable}).
 * 
 * @author Alejandro Mostajo (AmsGames)
 *
 */
public class AmsRow {
	
	/*
	 * Attributes
	 */
	/**
	 * List of items ({@link AmsItem}) within a row.
	 */
	public List<AmsItem> Items;
	
	/*
	 * Constructors
	 */
	/**
	 * Default constructor. Creates a row with empty items ({@link AmsItem}).
	 */
	public AmsRow () {
		Items = new ArrayList<AmsItem>();
	}
	/**
	 * Default constructor. Creates a row with a list of specific items ({@link AmsItem}).
	 */
	public AmsRow (List<AmsItem> items) {
		Items = items;
	}
	
	/*
	 * Functions and methods
	 */
	/**
	 * Gets an item's ({@link AmsItem}) value at a specific column's position.
	 * 
	 * @param	columnIndex	Column's position.
	 * @return	The Item's value as an {@link Object} data type.
	 */
	public Object getValueAt(int columnIndex) {
		return Items.get(columnIndex).Value;
	}
	
	/**
	 * Gets an item's ({@link AmsItem}) value of a specific column, searching by the column's names.
	 * 
	 * @param	columnName	The name of the column to look at.
	 * @return	The Item's value as an {@link Object} data type.
	 */
	public Object getValueAt (String columnName) {
		Object value = null;
		for (int itemIndex=0; itemIndex<Items.size(); itemIndex++) {
			if(Items.get(itemIndex).Column.Name.equals(columnName)) {
				value = Items.get(itemIndex).Value;
			}
		}
		return value;
	}

}
