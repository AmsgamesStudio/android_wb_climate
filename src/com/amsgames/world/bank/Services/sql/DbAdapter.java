package com.amsgames.world.bank.Services.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.amsgames.world.bank.Interfaces.IDbAdapter;
import com.amsgames.world.bank.Services.data.*;

/**
 * This adapter will handle all the interactions with the phones internal DB.
 * 
 * @author Alejandro Mostajo (Amsgames)
 *
 */
public class DbAdapter implements IDbAdapter {
	
	/**
	 * The name of the database.
	 */
    private static final String DATABASE_NAME = "wb_climate";
    private static final String DATABASE_CREATE_COUNTRY =
    		"create table COUNTRY (" +
    		"ISO3 text not null primary key, " +
    		"NAME text not null, " +
    		"DIFFICULTY_LEVEL int not null);";
    private static final String DATABASE_CREATE_CLIMATE =
    		"create table CLIMATE (" +
    	    "NAME text not null," +
    		"YEAR_RANGE text not null," +
    		"TEMPERATURE text not null," +
    		"PRECIPITATION text not null," +
    		"ROUND text not null," +
    		"OBJECTS_X_ROUND text not null," +
    		"OBJECT_SPEED_FACTOR text not null," +		
    		"OBJECT_FREQUENCY_FACTOR text not null," +		
    		"FACT_MESSAGE text not null," +		
    		"DIFFICULTY_LEVEL int not null);";
    private static final String DATABASE_CREATE_STATS =
    		"create table STATS (" +
    		"CODE text not null primary key," +
    		"VALUE text not null);";
    private static final String DATABASE_CREATE_ERROR_LOG =
    		"create table ERROR_LOG (" +
    		"TIME_ID int not null," +
    		"ERROR text not null);";
    /**
     * The database version.
     */
    private static final int DATABASE_VERSION = 4;
    /**
     * The related android context.
     */
    private final Context _context;
    private DatabaseHelper _helper;
    private SQLiteDatabase _db;
    
    /**
     * The DB helper for the {@link DbAdapter}.
     * 
     * @author Alejandro Mostajo (Amsgames)
     *
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_COUNTRY);
            db.execSQL(DATABASE_CREATE_CLIMATE);
            db.execSQL(DATABASE_CREATE_STATS);
            db.execSQL(DATABASE_CREATE_ERROR_LOG);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS COUNTRY");
            db.execSQL("DROP TABLE IF EXISTS CLIMATE");
            db.execSQL("DROP TABLE IF EXISTS STATS");
            db.execSQL("DROP TABLE IF EXISTS ERROR_LOG");
            onCreate(db);
        }
    }
   
    /**
     * Constructor - takes the context to allow the database to be opened/created.
     * 
     * @param context	The Context within which to work.
     */
    public DbAdapter(Context context) {
        this._context = context;
    }

    /* (non-Javadoc)
	 * @see com.amsgames.world.bank.Services.sql.IDbAdapter#open()
	 */
    public IDbAdapter open() throws SQLException {
    	_helper = new DatabaseHelper(this._context);
        _db = _helper.getWritableDatabase();
        return this;
    }

    /* (non-Javadoc)
	 * @see com.amsgames.world.bank.Services.sql.IDbAdapter#close()
	 */
    public void close() {
    	_helper.close();
    }
    
    /* (non-Javadoc)
	 * @see com.amsgames.world.bank.Services.sql.IDbAdapter#updateTableFrom(com.amsgames.world.bank.Services.data.AmsTable)
	 */
    public void updateTableFrom (AmsTable table) throws Exception {
    	// STEP 1
    	// Delete table data
    	String sql = "delete from " + table.TableName.toUpperCase();
    	_db.execSQL(sql);
    	
    	// STEP 2
    	// Create insert statement
    	sql = "insert into " + table.TableName.toUpperCase();
    	String auxStatement = "";
    	
    	// Add columns
    	for (AmsColumn column : table.Columns) {
    		auxStatement += 
    				auxStatement.equals("") 
    				?	" ("
    				:	",";
    		auxStatement += column.Name;
    	}
    	sql += auxStatement + ") ";
    	auxStatement = "";
    	
    	// Add rows
    	for (AmsRow row : table.Rows) {
    		auxStatement += 
    				auxStatement.equals("") 
    				?	" select "
    				:	" union select ";
    		// Add items
    		String itemStatement = "";
    		for (AmsItem item : row.Items) {
    			itemStatement += 
    					itemStatement.equals("") 
        				?	""
        				:	",";
    			itemStatement += "'" + item.Value.toString() + "'";
    		}
    		auxStatement += itemStatement;
    	}
    	sql += auxStatement;
    	_db.execSQL(sql);
    }
    
    /* (non-Javadoc)
	 * @see com.amsgames.world.bank.Services.sql.IDbAdapter#query(java.lang.String, java.lang.String[])
	 */
    public Cursor query (String sql, String[] args) throws Exception  {
    	
    	// Get query results
    	Cursor cursorResults = this._db.rawQuery(sql, args);
    	if (cursorResults != null) {
    		cursorResults.moveToFirst();
    	}	
    	
    	return cursorResults;
    }
    
    /* (non-Javadoc)
	 * @see com.amsgames.world.bank.Services.sql.IDbAdapter#query(java.lang.String)
	 */
    public Cursor query (String sql) throws Exception  {
    	   	
    	return query(sql, new String[0]);
    }
    
    /* (non-Javadoc)
	 * @see com.amsgames.world.bank.Services.sql.IDbAdapter#execSQL(java.lang.String)
	 */
    public void execSQL (String sql) throws Exception  {
    	this._db.execSQL(sql);
    }
    
    /* (non-Javadoc)
	 * @see com.amsgames.world.bank.Services.sql.IDbAdapter#isEmptyTable(java.lang.String, java.lang.String)
	 */
    public boolean isEmptyTable (String tableName, String whereStatememt) throws Exception {
    	boolean isEmpty = false;
    	
    	try {
    		
    		// Query total records
    		Cursor auxCursor = this.query(
    				"SELECT count(1) as total_records FROM " + tableName.trim() + " " + (whereStatememt).trim(),
    				new String[0]);
    		if (auxCursor != null) {
    			if (auxCursor.getInt(0)==0) {
    				isEmpty = true;
    			}
    		} else {
    			isEmpty = true;
    		}
    		auxCursor.deactivate();
    		auxCursor.close();
    		auxCursor = null;
    		tableName = null;

    	} catch (Exception ex) {
    		isEmpty = true;
    	}
    	
    	return isEmpty;
    }
    
    /* (non-Javadoc)
	 * @see com.amsgames.world.bank.Services.sql.IDbAdapter#isEmptyTable(java.lang.String)
	 */
    public boolean isEmptyTable (String tableName) throws Exception {
    	return this.isEmptyTable(tableName, "");
    }

}
