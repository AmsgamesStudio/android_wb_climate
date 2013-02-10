package com.amsgames.world.bank.Services.data;

/**
 * "Ams Games Data Item" used as part of a Table Row ({@link AmsRow}) within a Data Table ({@link AmsTable}); it contains a value and a Table Column ({@link AmsColumn}) related to.
 * 
 * @author Alejandro Mostajo (AmsGames)
 *
 */
public class AmsItem {
	
	/*
	 * Attributes
	 */
	/**
	 * Value of the Item.
	 */
	public Object Value;
	/**
	 * The Column ({@link AmsColumn}) related to the item.
	 */
	public AmsColumn Column;
	
	/**
	 * Default constructor. Creates a new empty item.
	 */
	public AmsItem () {
		Value = new Object();
		Column = new AmsColumn();
	}
	
	/**
	 * Creates an item with a value and no column related.
	 * 
	 * @param	value	The value of the Item. 
	 */
	public AmsItem (Object value) {
		Value = value;
		Column = new AmsColumn();
	}
	
	/**
	 * Creates an item with a value and a column related.
	 * 
	 * @param	value	The value of the Item. 
	 * @param	column	The column related to.
	 */
	public AmsItem (Object value, AmsColumn column) {
		Value = value;
		Column = column;
	}
}
