package com.grtpath.database;

import com.grtpath.database.contract.DatabaseContract.FavouriteStopsEntry;

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
	
	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FavouriteStopsEntry.TABLE_NAME;
	
	 // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FavouriteStops.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FavouriteStopsEntry.CREATE_TABLE);
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
