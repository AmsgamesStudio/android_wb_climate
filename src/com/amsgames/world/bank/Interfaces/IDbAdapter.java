package com.amsgames.world.bank.Interfaces;

import android.database.Cursor;
import android.database.SQLException;

import com.amsgames.world.bank.Services.data.AmsTable;

public interface IDbAdapter {

	/**
	 * Opens the database. If it cannot open, tries to create a new instance of the database. 
	 * If it cannot be created, throw an exception to signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 *         
	 * @throws SQLException if the database could be neither opened or created
	 */
	public abstract IDbAdapter open() throws SQLException;

	/**
	 * Closes the database.
	 */
	public abstract void close();

	/**
	 * Updates a database table with the data stored in a {@link AmsTable}.
	 * This will truncate first and then insert.
	 * 
	 * @param table A table {@link AmsTable}
	 * 
	 * @exception Exception Normal exception.
	 */
	public abstract void updateTableFrom(AmsTable table) throws Exception;

	/**
	 * Executes a SQL query and returns the results in a {@link Cursor}. 
	 * Use ? as parameter place-holder in the string.
	 * 
	 * @param sql  SQL statement as a string.
	 * @param args Arguments of parameter values.
	 * 
	 * @exception Exception Normal exception.
	 * 
	 * @return {@link Cursor} with the results.
	 */
	public abstract Cursor query(String sql, String[] args) throws Exception;

	/**
	 * Executes a SQL query and returns the results in a {@link Cursor}. 
	 * 
	 * @param sql  SQL statement as a string.
	 * 
	 * @exception Exception Normal exception.
	 * 
	 * @return {@link Cursor} with the results.
	 */
	public abstract Cursor query(String sql) throws Exception;

	/**
	 * Executes a SQL statement.
	 * 
	 * @param sql  SQL statement as a string.
	 * 
	 * @exception Exception Normal exception.
	 */
	public abstract void execSQL(String sql) throws Exception;

	/**
	 * Indicates if a table is empty or has data.
	 * 
	 * @param tableName 	 Table name to query.
	 * @param whereStatememt A where SQL statement to concatenate to que query.
	 * 
	 * @return True if is empty and false if is not.
	 * 
	 * @throws Exception Normal exception.
	 */
	public abstract boolean isEmptyTable(String tableName, String whereStatememt)
			throws Exception;

	/**
	 * Indicates if a table is empty or has data.
	 * 
	 * @param tableName 	 Table name to query.
	 * 
	 * @return True if is empty and false if is not.
	 * 
	 * @throws Exception Normal exception.
	 */
	public abstract boolean isEmptyTable(String tableName) throws Exception;

}