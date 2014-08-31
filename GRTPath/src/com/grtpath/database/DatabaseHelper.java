package com.grtpath.database;

import com.grtpath.database.contract.FavouriteStopsContract.FavouriteStopsEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * This database helper is for storing favorites
 * Can be written to
 * @author Charles
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES =
	    "CREATE TABLE " + FavouriteStopsEntry.TABLE_NAME + " (" +
	    		FavouriteStopsEntry.COLUMN_NAME_FAVOURITES_ID + " INTEGER PRIMARY KEY," +
	    		FavouriteStopsEntry.COLUMN_NAME_STOP_NAME + TEXT_TYPE + COMMA_SEP +
	    		FavouriteStopsEntry.COLUMN_NAME_STOP_ID + TEXT_TYPE + COMMA_SEP +
	    		" )";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FavouriteStopsEntry.TABLE_NAME;
	
	 // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FavouriteStops.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
