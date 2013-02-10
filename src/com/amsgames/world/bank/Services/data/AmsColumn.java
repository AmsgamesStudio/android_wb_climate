package com.amsgames.world.bank.Services.data;

/**
 * "Ams Games Data Table Column" is a column contained in a Data Table ({@link AmsTable}). It's also used in an item ({@link AmsItem}) to relate a value to a column.
 * 
 * @author Alejandro Mostajo (AmsGames)
 *
 */
public class AmsColumn {
	
	/*
	 * Attributes
	 */
	/**
	 * The name of the column.
	 */
	public String Name;
	/**
	 * The position/index in the column list within a Data Table ({@link AmsTable}).
	 */
	public int Index;
	
	/*
	 * Constructors
	 */
	/**
	 * Default constructor. Creates a empty column.
	 */
	public AmsColumn () {
		Name = "";
		Index = 0;
	}
	
	/**
	 * Creates a column with a name.
	 * 
	 * @param	name	The name of the column.
	 */
	public AmsColumn (String name) {
		Name = name;
		Index = 0;
	}
	
	/**
	 * Creates a column with a name and an index within a Data Table ({@link AmsTable}).
	 * 
	 * @param	name	The name of the column.
	 * @param	index	The position/index in the column list within a Data Table ({@link AmsTable}).
	 */
	public AmsColumn (String name, int index) {
		Name = name;
		Index = index;
	}

}
